#include <Wire.h>
#include <Adafruit_Sensor.h> 
#include <Adafruit_ADXL345_U.h>
#include <MySQL_Connection.h>
#include <MySQL_Cursor.h>
#include<ArduinoHttpClient.h>
#include <SPI.h>
#include <WiFiNINA.h> //Include this instead of WiFi101.h as needed
#include <WiFiUdp.h>
#include <RTCZero.h>
#include "RTClib.h"
#include "arduino_secrets.h"
Adafruit_ADXL345_Unified accel = Adafruit_ADXL345_Unified();

RTCZero rtc;

//1.set the wifi name and possword
WiFiClient wifi;
MySQL_Connection conn((Client *)&wifi);
char ssid[] = SECRET_SSID;        // your network SSID (name)
char pass[] = SECRET_PASS;    // your network password (use for WPA, or use as key for WEP)
int status = WL_IDLE_STATUS;     // the Wifi radio's status
const int GMT = 1;

//2.set the http format
int    HTTP_PORT   = 9090;
String HTTP_METHOD = "GET";
char   HOST_NAME[] = "192.168.0.198";//localhost ip
String PATH_NAME   = "/event/";
String queryString = "";
String checkScheduler = "";
String result;


//set the pin of arduino
const int buttonPin1 = 1;     // the number of the pushbutton pin
const int buttonPin2 = 2;     // the number of the pushbutton pin
const int buttonPin3 = 3; 
const int buttonPin4 = 4; 
const int buttonPin5 = 5; 
const int buttonPin6 = 6; 
const int buttonPin7 = 7; 
const int ledPin =  14;      // the number of the LED pin
const int buzzer =  0; 

// set the states of switches:
int buttonState1 = 0;         // variable for reading the pushbutton status
int buttonState2 = 0;  
int buttonState3 = 0; 
int buttonState4 = 0; 
int buttonState5 = 0; 
int buttonState6 = 0; 
int buttonState7 = 0; 

//set the AccSignal & swithSignal
int accSignal = 0;
int switchSignal = 0;
String datetime;

unsigned long lastTime = 0;
unsigned long timerDelay = 5000;
//int isHttpSent = 0;
//IPAddress server_addr(192,168,0,185);  // IP of the MySQL *server* here
//char user[] = "root";              // MySQL user login username
//char password[] = "123456";        // MySQL user login password
  
void setup(void) {
   // initialize the LED pin as an output:
  pinMode(ledPin, OUTPUT);
  pinMode(buzzer, OUTPUT);
  // initialize the pushbutton pin as an input:
  pinMode(buttonPin1, INPUT);
  pinMode(buttonPin2, INPUT);
  pinMode(buttonPin3, INPUT);
  pinMode(buttonPin4, INPUT);
  pinMode(buttonPin5, INPUT);
  pinMode(buttonPin6, INPUT);
  pinMode(buttonPin7, INPUT);
 
  //Initialize serial and wait for port to open:
  Serial.begin(9600);
  if(!accel.begin())
   {
      Serial.println("No ADXL345 sensor detected.");
      while(1);
   }
   ConnectWifi();
   printWiFiStatus();

}
  




void loop(void) {
  // read the state of the pushbutton value:
  //connect to wifi
  ConnectWifi();
  
  switchSignal = getSwitchSignal();
  accSignal = getAccSignal();
   

  //get the real time
  datetime = getTimefromRtc();
  queryString = datetime+"/"+switchSignal+"/"+accSignal;
  Serial.println(datetime);
  if(switchSignal!=0 || accSignal!=0)
    eventHttpRequest();  

  schedulerHttpRequest();        
  delay(1000);
}

//send http request and get data back
void eventHttpRequest(){
  HttpClient client = HttpClient(wifi, HOST_NAME, HTTP_PORT);
  if(client.connect(HOST_NAME, HTTP_PORT)) {
    // if connected:
//    Serial.println("Connected to server");
    // make a HTTP request:
    // send HTTP header
    client.println(HTTP_METHOD + " " + PATH_NAME + queryString + " HTTP/1.1");
    client.println("Host: " + String(HOST_NAME));
    client.println("Connection: close");
    client.println(); // end HTTP header

    while(client.connected()) {
      if(client.available()){
        // read an incoming byte from the server and print it to serial monitor:
        char c = client.read();
//        Serial.print(i);
        if (c == '#'){
          while(client.available() == 0){}
          char c = client.read();
          if(c == '0'){
            result = "Correct!";
          }
          else {
            result = "Wrong";
            digitalWrite(ledPin, HIGH);
            digitalWrite(buzzer,HIGH);
            delay(5000);
            digitalWrite(ledPin, LOW);
            digitalWrite(buzzer,LOW);
          } 
        Serial.print(c);
      }
     }
    }
    Serial.println();
//    Serial.print(result);

    // the server's disconnected, stop the client:
    client.stop();
    Serial.println();
//    Serial.println("disconnected");
  } else {// if not connected:
//    Serial.println("connection failed");
  }
  
  
}

//get the switchSignal
int getSwitchSignal(){
  buttonState1 = digitalRead(buttonPin1);
  buttonState2 = digitalRead(buttonPin2);
  buttonState3 = digitalRead(buttonPin3);
  buttonState4 = digitalRead(buttonPin4);
  buttonState5 = digitalRead(buttonPin5);
  buttonState6 = digitalRead(buttonPin6);
  buttonState7 = digitalRead(buttonPin7);
  if (buttonState1 == HIGH){
    switchSignal = 7;
  }
  else if (buttonState2 == HIGH){
    switchSignal = 6;
  }
  else if (buttonState3 == HIGH){
    switchSignal = 5;
  }
  else if (buttonState4 == HIGH){
    switchSignal = 4;
  }
  else if (buttonState5 == HIGH){
    switchSignal = 3;
  }
  else if (buttonState6 == HIGH){
    switchSignal = 2;
  }
  else if (buttonState7 == HIGH){
    switchSignal = 1;
  }
  else{
    switchSignal = 0;
  }
  return switchSignal;
}


//get the Accelerometer Signal
int getAccSignal(){
  accSignal = 0;
  sensors_event_t event; 
  accel.getEvent(&event);
  float vector;
  vector = pow(event.acceleration.x,2)+pow(event.acceleration.y,2)+pow(event.acceleration.z,2);
//  Serial.println(vector);
  if(abs(vector-98)>20)
    accSignal = 1;
  else
    accSignal = 0;  
  return accSignal;
  
}

//connect to the wifi
void ConnectWifi(){
  while ( status != WL_CONNECTED) {
    Serial.print("Attempting to connect to Network named: ");
    Serial.println(ssid);   // print the network name (SSID);

    // Connect to WPA/WPA2 network:
    status = WiFi.begin(ssid, pass);
    //   print the SSID of the network you're attached to:
    delay(5000);  

    rtc.begin();
  unsigned long epoch;
  int numberOfTries = 0, maxTries = 6;
  
  do {
    epoch = WiFi.getTime();
    numberOfTries++;
  }while ((epoch == 0) && (numberOfTries < maxTries));
  
  if (numberOfTries == maxTries) {
    Serial.print("NTP unreachable!!");
    while (1);
  }
  else {
    Serial.print("Epoch received: ");
    Serial.println(epoch);
    rtc.setEpoch(epoch);
    Serial.println();

  }
  }
}

// you're connected now, so print out the status:
void printWiFiStatus() {

  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());
  
  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);
  
  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}

//get the time string for NTP
String getTimefromRtc(){
  int year = rtc.getYear()+2000;
  int month = rtc.getMonth();
  int day = rtc.getDay();
  int hours = rtc.getHours() + GMT;
  int minutes = rtc.getMinutes();
  int seconds = rtc.getSeconds();
  char buffer[25];
  sprintf(buffer, "%04d%02d%02d%02d%02d%02d", year, month, day, hours, minutes, seconds);
//  Serial.print(buffer);
  return buffer;
}


//send http request and get data back
void schedulerHttpRequest(){
  HttpClient client = HttpClient(wifi, HOST_NAME, HTTP_PORT);
  if(client.connect(HOST_NAME, HTTP_PORT)) {
    // if connected:
    Serial.println("Connected to server");
    // make a HTTP request:
    // send HTTP header
    client.println(HTTP_METHOD + " " +"/time/checkScheduler"+ " HTTP/1.1");
    client.println("Host: " + String(HOST_NAME));
    client.println("Connection: close");
    client.println(); // end HTTP header

    while(client.connected()) {
      if(client.available()){
        // read an incoming byte from the server and print it to serial monitor:
        char c = client.read();
//        Serial.print(i);
        if (c == '#'){
          while(client.available() == 0){}
          char c = client.read();
          if(c == '1'){
            result = "Wrong";
            digitalWrite(ledPin, HIGH);
            digitalWrite(buzzer,HIGH);
            delay(5000);
            digitalWrite(ledPin, LOW);
            digitalWrite(buzzer,LOW);
          }
          else
            result = "Correct!";           
        Serial.print(c);
      }
     }
    }
    Serial.println();
    Serial.print(result);

    // the server's disconnected, stop the client:
    client.stop();
    Serial.println();
    Serial.println("disconnected");
  } else {// if not connected:
    Serial.println("connection failed");
  }
  
  
}

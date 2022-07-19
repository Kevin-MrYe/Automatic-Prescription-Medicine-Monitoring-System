# Automatic Prescription Medicine Monitoring System
In order to improve medication adherence of the patient, this project proposes an APMM smart pill box based on an automatic prescription medicine monitoring system. The
APMM smart pill box is based on MKR WiFi 1010, which transmits medication data of the patient to the web appliction via WiFi. The smart pill box can effectively monitor
the patient taking the medicine at the correct time, and remind the patient to take the medicine at a specific time. The APMM smart pill box establishes a patient medication information sharing platform among patients, family members and carers, which records and displays all medication event data to the web application deployed on the cloud server.

## Table of Contents
* [1.System Architecture](#1system-architecture)
* [2.Component Used](#2component-used)
* [3.Software Used](#3software-used)
* [4.Breadboard Circuit Diagram](#4breadboard-circuit-diagram)
* [5.PCB Design](#5pcb-design)
* [6.3D Box Design](#63d-box-design)
* [7.Web Application Architecture](#7web-application-architecture)
* [8.Arduino Code Design](#8arduino-code-design)

## 1.System Architecture
The project is based on an automatic prescription medicine monitoring system. As
shown in following figure, the entire system architecture includes three parts: Arduino, Connection, and Web application.

<img src ="https://github.com/Kevin-MrYe/Automatic-Prescription-Medicine-Monitoring-System/blob/master/images/systemarchitecture.png" width = '600px'>

The first part is Arduino, including microcontroller, accelerometer, micro switches, LED, buzzer and Li-Po battery. The above components are integrated in a common pill box with seven compartments. 

The second part is Connection. The design goal of this smart pill box is mainly for home use scenarios, so Wi-Fi wireless transmission is selected as the communication method.

The third part is Web Application. The web application will accept the medicationevent data from Arduino, put it into the database and display it on the front end of theweb application so that family members and carers can view it in time.

## 2.Component Used
- Basic Board Kit
- Pill Boxes with Seven Compartments
- MKR WiFi 1010 Development Board
- ADXL345 Accelerometer
- Mini Micro Switch with Roller Lever
- 3.7V 1100mAh Rechargeable Li-Po Battery
- Printed Circuit Board
- 3D Box

## 3.Software Used
- **PCB Design**: Altium Designer 21.6
- **3D Printing Box Design**: 3D Builder 18.0
- **Breadboard Circuit Diagram Design**: Fritzing 0.9
- **Arduino Coding**: Arduino IDE 1.8
- **Web Application**: Intellij IDEA 2021.2 ( Java language )
- **Database**: Mysql 5.7 Navicat 15.0
- **Cloud Server**: CentOS 7.6 (64bit) 1 Core 1G Memory 20G System Volume

## 4.Breadboard Circuit Diagram
Before actually testing the components, a breadboard circuit diagram should be constructed to confirm the placement of the components and the use of the pins of the development board. As shown in the following figure, the breadboard is connected to MKR WiFi development board, seven micro-switches, an ADXL accelerometer, an LED and a buzzer. 

<img src ="https://github.com/Kevin-MrYe/Automatic-Prescription-Medicine-Monitoring-System/blob/master/images/breadboarddiagram.jpg" width = '600px'>


## 5.PCB Design
Printed Circuit Boards (PCBs) are used in almost all electronic applications. In this project, the goal is to build a portable, small actual smart pill box, not just a concept product to be tested on a breadboard. This project uses Altium Design to design the PCB of the smart pill box.

As shown in the following figure, place the line connected to the development board on one side (red line), and place the line connected to GND and VCC on the other side (blue line). In order to allow the USB interface of the development board to be used for external power supply, the component MKR WiFi 1010 is placed close to the right edge. In order for the LED light to be displayed to the patient, place it near the bottom edge. In order to be able to solder the seven microswitches to the interface with good rules, place the microswitches on the top edge.

<img src ="https://github.com/Kevin-MrYe/Automatic-Prescription-Medicine-Monitoring-System/blob/master/images/pcblayout.jpg" width = '600px'>

## 6.3D Box Design
In order to be able to wrap the circuit details instead of exposing it, this project uses 3D Builder to design two 3D printing boxes. The outer size of box A is 22.8 x 5.9 x 3.3 cm, and the outer size of box B is 22.8 x 5.9 x 1.0 cm. The edge thickness of both boxes is 2mm. The model of the 3D printing box is shown in the following figure:

<img src ="https://github.com/Kevin-MrYe/Automatic-Prescription-Medicine-Monitoring-System/blob/master/images/3dbox.jpg" width = '600px'>

## 7.Web Application Architecture
In order to solve the problem of high front-end and back-end coupling and improve the scalability of web application, this project separates the front-end application from the back-end application. Write client code in the front-end application. Write the server code in the back-end application and provide the data interface. Under this architecture, the front-end can test the function by manufacturing test data, and the back-end test can test the function by other tools, such as postman. Application developed in this way is called front-end and back-end separated application, and its system
architecture is shown in the following figure:

<img src ="https://github.com/Kevin-MrYe/Automatic-Prescription-Medicine-Monitoring-System/blob/master/images/Frontendbackendseparation.jpg" width = '600px'>

## 8.Arduino Code Design
Regarding the Arduino code design of this project, the goal is to obtain the current time, switch signal, and accelerometer signal. Then, when the patient interacts with the smart pill box, the above data is sent to the Web application for processing through HTTP requests. The Arduino code design flow chart is shown in the following figure:


<img src ="https://github.com/Kevin-MrYe/Automatic-Prescription-Medicine-Monitoring-System/blob/master/images/arduinocode.jpg" width = '600px'>


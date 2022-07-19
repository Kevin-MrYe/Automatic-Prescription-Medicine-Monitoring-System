<template>
<!--  <div style="width: 100%; height: 100vh; background-color: lightskyblue;overflow: hidden">-->
  <div id="logo">
      <div style="width: 400px; margin: 200px 150px">
        <div style="color: white; font-size: 25px; text-align: center; padding: 30px">Welcome to APMM System</div>
        <el-form ref="form" :model="form" label-width="80px" :rules="rules" >
          <el-form-item label="Username: "  prop="username" class="whiteitem">
            <el-input v-model="form.username" prefix-icon="el-icon-user-solid"></el-input>
          </el-form-item>
          <el-form-item label="Password:" prop="password" class="whiteitem">
            <el-input v-model="form.password" prefix-icon="el-icon-lock" show-password></el-input>
          </el-form-item>
          <el-form-item >
            <el-button style="width: 40%;" type="success" @click="login">Login in</el-button>
            <el-button style="width: 40%;" type="success" @click="register">Register</el-button>
          </el-form-item>
        </el-form>
      </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Login",
  data() {
    return {
      form:{},
      rules: {
        username: [
          {required: true, message: 'Please enter username', trigger: 'blur'},
          // { min: 6, max: 10, message: 'Username needs to be between 6 and 10 characters long', trigger: 'blur' }
        ],

        password: [
          {required: true, message: 'Please enter password', trigger: 'blur'},
          // { min: 6, max: 10, message: 'Username needs to be between 6 and 10 characters long', trigger: 'blur' }
        ],
      },
      note: {
        backgroundImage: "url(" + require("@/assets/background.jpg") + ") ",
        backgroundRepeat: "no-repeat",
        backgroundSize: "100% 100vh",
        height: "100%",
      },

      // backgroundDiv: {
      //
      //   backgroundImage:'url(' + require('../assets/background.jpg') + ')',
      //
      //   backgroundRepeat:'no-repeat',
      //
      //   backgroundSize:'100% 100%',
      //
      //
      // }
    }
  },

  methods:{
    login(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          request.post('/user/login',this.form).then(res => {
            if (res.code === '0'){
              this.$message({
                type:"success",
                message:"Login in successfully"
              })
              sessionStorage.setItem("user", JSON.stringify(res.data))
              this.$router.push("/user")  //get in next page
            }else{
              this.$message({
                type:"error",
                message:res.msg
              })
            }
          })
        }
      });
    },

    register(){
      this.$router.push("/register")

    }
  }
}
</script>

<style>
  #logo{
    background: url("../assets/background.jpg");
    background-size: 100% 100%;
    height: 100%;
    position: fixed;
    width: 100%
  }
  .whiteitem .el-form-item__label{
    color: white;
  }
</style>
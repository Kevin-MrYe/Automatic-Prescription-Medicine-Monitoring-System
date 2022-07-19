<template>
  <div id="logo">
      <div style="width: 400px; margin: 200px 150px">
        <div style="color: white; font-size: 30px; text-align: center; padding: 30px">Welcome to Register</div>
        <el-form ref="form" :model="form" label-width="80px" :rules="rules">
          <el-form-item label="Username:  " prop="username" class="whiteitem">
            <el-input v-model="form.username" prefix-icon="el-icon-user-solid"></el-input>
          </el-form-item>
          <el-form-item label="Email: " prop="email" class="whiteitem">
            <el-input v-model="form.email" prefix-icon="el-icon-message"></el-input>
          </el-form-item>
          <el-form-item label="Password: " prop="password" class="whiteitem">
            <el-input v-model="form.password" prefix-icon="el-icon-lock" show-password></el-input>
          </el-form-item>
          <el-form-item label="Confirm: " prop="confirm" class="whiteitem">
            <el-input v-model="form.confirm" prefix-icon="el-icon-lock" show-password></el-input>
          </el-form-item>
          <el-form-item >
            <el-button style="width: 40%;" type="success" @click="back">Back</el-button>
            <el-button style="width: 40%;" type="success" @click="register">Register</el-button>
          </el-form-item>
        </el-form>
      </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Register",
  data() {
    return {
      form:{},
      rules: {
        username: [
          {required: true, message: 'Please enter username', trigger: 'blur'}
        ],

        email: [
          { required: true, message: 'Please enter your email address', trigger: 'blur' },
          { type: 'email', message: 'Please enter correct email address', trigger: ['blur', 'change'] }
        ],

        password: [
          {required: true, message: 'Please enter password', trigger: 'blur'},
        ],
        confirm: [
          {required: true, message: 'Please enter confirm password', trigger: 'blur'},
        ],
      }
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

    back(){
      this.$router.push("/login")
    },
    register(){

      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.password === this.form.confirm) {
            request.post('/user/register', this.form).then(res => {
              if (res.code === '0') {
                this.$message({
                  type: "success",
                  message: "Register successfully"
                })
                this.$router.push("/login")  //get in next page
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
          }else {
            this.$message({
              type:"error",
              message:"Oops! The passwords don't match"
            })
          }
        }
      })
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
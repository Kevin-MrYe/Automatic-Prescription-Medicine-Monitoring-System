<template>
  <div style="height: 50px ;line-height: 50px; border-bottom: 1px solid #ccc; display: flex">
    <div style="width: 600px; padding-left: 30px; font-size:20px;font-weight: bold; color: dodgerblue"> Automatic Prescription Medicine Monitoring System</div>
    <div style="flex: 1;margin-left: 80px" >{{nowTime}}</div>
    <div style="width: 100px">
      <el-dropdown>
      <span class="el-dropdown-link">
        {{ user.username }}<i class="el-icon-arrow-down el-icon--right"></i>
      </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>Profile</el-dropdown-item>
            <el-dropdown-item @click="$router.push('/login')">Logout</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

  </div>
</template>

<script>
import moment from "moment";

export default {
  name: "Header",

  data(){
    return{
      user: {},
      nowTime:this.nowTime,
    }
  },
  methods:{
    getTime(){
      setInterval(()=>{
        //new Date() new一个data对象，当前日期和时间
        //toLocaleString() 方法可根据本地时间把 Date 对象转换为字符串，并返回结果。
        this.nowTime = moment().format('dddd, DD MMM yyyy HH:mm:ss z');
      },1000)
    }
  },
  created(){
    this.getTime();
    let str = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(str)
  }
}



</script>

<style scoped>

</style>
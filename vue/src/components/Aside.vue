<template>
  <div>
    <el-menu
        style="width: 200px; min-height: calc(100vh - 50px)"
        :default-active="path"
        router
        class="el-menu-vertical-demo">
      <el-menu-item index="/time">
        <i class="el-icon-menu"></i>
        <template #title>Medication Timetable</template>
      </el-menu-item>
      <el-menu-item index="/user" >
        <i class="el-icon-document"></i>
        <template #title>Medication Event</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside",
  data(){
    return{
      path:this.$route.path,
    }
  },
}
</script>

<style scoped>

</style>
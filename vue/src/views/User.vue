<template>
  <div style="padding: 10px">
<!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">Add</el-button>
<!--      <el-button type="primary">Import</el-button>-->
<!--      <el-button type="primary">Export</el-button>-->
    </div>

<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="Enter key words" style="width: 20% " clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">Search</el-button>
    </div>

<!--    内容显示区-->
    <el-table
        :data="tableData"
        border
        stripe
        style="width: 100%">
      <el-table-column prop="datetime" label="Datetime" :width=160 align="center"></el-table-column>
      <el-table-column prop="dayOfweek" label="DayOfweek" :width=120 align="center"></el-table-column>
      <el-table-column prop="pickStatus" label="PickStatus" :width=120 align="center"></el-table-column>
      <el-table-column prop="openStatus" label="OpenStatus" :width=120 align="center"></el-table-column>
      <el-table-column prop="compartment" label="OpenCompartment" :width=150 align="center"></el-table-column>
      <el-table-column prop="globalStatus" label="GlobalStatus" :width=100 align="center"></el-table-column>
      <el-table-column class="cell" prop="event" label="Event" :width=270 ></el-table-column>
      <el-table-column label="Operation">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)" >Edit</el-button>
          <el-popconfirm title="Confirm delete?？" @confirm="handleDelete(scope.row.id)" >
            <template #reference>
              <el-button size="mini" type="danger" >Delete</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSzie"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

      <el-dialog title="Infomation" v-model="dialogVisible" width="35%">
        <el-form :model="form" label-width="150px">
          <el-form-item  label="Datetime">
            <el-date-picker
                v-model="form.datetime"
                type="datetime"
                placeholder="Choose datetime">
            </el-date-picker>
          </el-form-item>
          <el-form-item  label="PickStatus">
            <el-radio v-model="form.pickStatus" label="Yes">Yes</el-radio>
            <el-radio v-model="form.pickStatus" label="No">No</el-radio>
          </el-form-item>
          <el-form-item  label="OpenStatus">
            <el-radio v-model="form.openStatus" label="Yes">Yes</el-radio>
            <el-radio v-model="form.openStatus" label="No">No</el-radio>
          </el-form-item>
          <el-form-item  label="OpenCompartment">
            <el-select v-model="form.compartment" placeholder="Please choose">
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item  label="GlobalStatus">
            <el-radio v-model="form.globalStatus" label="Normal">Normal</el-radio>
            <el-radio v-model="form.globalStatus" label="Abnormal">Abnormal</el-radio>
          </el-form-item>

          <el-form-item  label="Event">
            <el-input  type="textarea" v-model="form.event" style="width: 80%"></el-input>
          </el-form-item>

        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="save">Confirm</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>


import request from "@/utils/request";

export default {
  name: 'User',
  components: {
  },
  data(){
    return{
      form:{

      },
      dialogVisible:false,
      currentPage:1,
      pageSize:10,
      total:0,
      search:'',
      tableData:[

      ],

      options: [{
        value: 'Sunday',
        label: 'Sunday'
      }, {
        value: 'Monday',
        label: 'Monday'
      }, {
        value: 'Tuesday',
        label: 'Tuesday'
      }, {
        value: 'Wednesday',
        label: 'Wednesday'
      }, {
        value: 'Thursday',
        label: 'Thursday'
      }, {
        value: 'Friday',
        label: 'Friday'
      }, {
        value: 'Saturday',
        label: 'Saturday'
      }, {
        value: 'Null',
        label: 'Null'
      }],
      value: ''
    }
  },

  created() {
    this.load()
  },
  methods:{

    load(){
      request.get('/event',{
        params:{
          pageNum : this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res =>{
        console.log(typeof res.data.records)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add(){
      this.dialogVisible=true
      this.form={}

    },

    save(){
      if(this.form.id){  //update
        request.put('/event',this.form).then(res =>{
          console.log(res)
          if (res.code === '0'){
            this.$message({
              type:"success",
              message:"Update successfully"
            })
          }else{
            this.$message({
              type:"error",
              message:res.msg
            })
          }
          this.load()   //refresh data
          this.dialogVisible=false  //close the dialog

        })
      }else{
        request.post('/event',this.form).then(res =>{  //add
          console.log(typeof this.form.datetime)
          console.log(res)
          if (res.code === '0'){
            this.$message({
              type:"success",
              message:"Add successfully"
            })
          }else{
            this.$message({
              type:"error",
              message:res.msg
            })
          }
          this.load()   //refresh data
          this.dialogVisible=false  //close the dialog
        })
      }


    },

    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible=true
    },
    handleDelete(id){
      console.log(id)
      request.delete('/event/'+ id).then(res => {
        console.log(res)
        if (res.code === '0'){
          this.$message({
            type:"success",
            message:"Delete successfully"
          })
        }else{
          this.$message({
            type:"error",
            message:res.msg
          })
        }
        this.load() //refresh the table data
      })

    },

    handleSizeChange(pageSize){   //change PageSize
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){  //Change PageNumber
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>

<style scoped>
   /deep/ .cell {
   word-break: keep-all;
   word-wrap: break-word;
 }
</style>

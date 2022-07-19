<template>
  <div style="padding: 10px">

<!--    <div style="margin: 10px 0">-->
<!--      <el-button type="primary" @click="add">新增</el-button>-->
<!--    </div>-->
    <!--    内容显示区-->
    <div style="display: inline-block ;height: 100px;position: absolute;top: 110px;font-size: 15px">
      <div  style="height: auto;margin: 28px 10px;text-align: right">Morning:</div>
      <div  style="height: auto;margin: 28px 10px;text-align: right">Noon:</div>
      <div  style="height: auto;margin: 28px 10px;text-align: right">Evening:</div>
    </div>
    <el-table
        :data="tableData"
        border
        stripe
        align="center"
        style="width: 90%; font-weight: bold;font-size: 15px; margin-top: 30px;margin-left: 100px; display: inline-block">

      <el-table-column
          align="center"
          prop="sunday"
          label="Sunday">
      </el-table-column>
      <el-table-column
          align="center"
          prop="monday"
          label="Monday">
      </el-table-column>
      <el-table-column
          align="center"
          prop="tuesday"
          label="Tuesday">
      </el-table-column>
      <el-table-column
          align="center"
          prop="wednesday"
          label="Wednesday">
      </el-table-column>
      <el-table-column
          align="center"
          prop="thursday"
          label="Thursday">
      </el-table-column>
      <el-table-column
          align="center"
          prop="friday"
          label="Friday">
      </el-table-column>
      <el-table-column
          align="center"
          prop="saturday"
          label="Saturday">
      </el-table-column>
      <el-table-column label="Operation">
        <template #default="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)" >Edit</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">

      <el-dialog title="Time" v-model="dialogVisible" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item  label="Sunday">
            <el-time-select
                v-model="form.sunday"
                start='08:00'
                step='00:30'
                end='22:00'
                placeholder="Select the Time">
            </el-time-select>
          </el-form-item>

          <el-form-item  label="Monday">
            <el-time-select
                v-model="form.monday"
                start='08:00'
                step='00:30'
                end='22:00'
                placeholder="Select the Time">
            </el-time-select>
          </el-form-item>

          <el-form-item  label="Tuesday">
            <el-time-select
                v-model="form.tuesday"
                start='08:00'
                step='00:30'
                end='22:00'
                placeholder="Select the Time">
            </el-time-select>
          </el-form-item>

          <el-form-item  label="Wednesday">
            <el-time-select
                v-model="form.wednesday"
                start='08:00'
                step='00:30'
                end='22:00'
                placeholder="Select the Time">
            </el-time-select>
          </el-form-item>

          <el-form-item  label="Thursday">
            <el-time-select
                v-model="form.thursday"
                start='08:00'
                step='00:30'
                end='22:00'
                placeholder="Select the Time">
            </el-time-select>
          </el-form-item>

          <el-form-item  label="Friday">
            <el-time-select
                v-model="form.friday"
                start='08:00'
                step='00:30'
                end='22:00'
                placeholder="Select the Time">
            </el-time-select>
          </el-form-item>

          <el-form-item  label="Saturday">
            <el-time-select
                v-model="form.saturday"
                start='08:00'
                step='00:30'
                end='22:00'
                placeholder="Select the Time">
            </el-time-select>
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
  name: 'Time',
  components: {
  },
  data(){
    return{
      form:{},
      dialogVisible:false,
      tableData:[

      ]
    }
  },

  created() {
    this.load()
  },
  methods:{

    load(){
      request.get('/time').then(res =>{
        console.log(res)
        this.tableData = res.data.records
        // this.total = res.data.total
      })
    },
    add(){
      this.dialogVisible=true
      this.form={}

    },

    save(){
      if(this.form.id){  //update
        console.log(typeof (this.form.sunday))
        request.put('/time',this.form).then(res =>{
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
        request.post('/time',this.form).then(res =>{  //add
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

  }
}
</script>

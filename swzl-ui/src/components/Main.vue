<template>
  <div>
    <div>
      <el-input v-model="name" placeholder="请输入名字" style="width: 200px" @keyup.enter.native="loadPost" />
      <el-button type="primary" style="margin-left: 5px" @click="loadPost">搜索</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="add">新增</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background:'#f5f7fa',color:'#606266'}"
              border
              style="width: 100%;height: 100%">
      <el-table-column prop="id" label="ID" align="center" />
      <el-table-column prop="name" label="名字"  align="center"/>
      <el-table-column prop="no" label="账号"  align="center" />
      <el-table-column prop="roleId" label="角色"  align="center">
        <template v-slot="scope">
          <el-tag
            :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
            disable-transitions
          >
            {{ scope.row.roleId === 0 ? '超级管理员' : (scope.row.roleId === 1 ? '管理员' : '普通用户') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话"  align="center" />
      <el-table-column prop="operate" label="操作"  align="center" >
        <template v-slot="scope">

          <el-button type="primary" size="small" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="del(scope.row.id)">
            <template #reference>
              <el-button slot="reference" type="danger" size="small" >删除</el-button>
            </template>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-size="pageSize"
      :page-sizes="[10, 50, 100, 200]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    />
<!--   新增弹窗-->
    <el-dialog v-model="centerDialogVisible" title="Warning" width="30%" center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item label="名字" prop="name">
          <el-col :span="25">
            <el-input v-model="form.name" />
          </el-col>
        </el-form-item>
        <el-form-item label="账号" prop="no">
          <el-col :span="25">
            <el-input v-model="form.no" />
          </el-col>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-col :span="25">
            <el-input v-model="form.password" />
          </el-col>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-col :span="25">
            <el-input v-model="form.phone" />
          </el-col>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-radio-group v-model="form.roleId">
            <el-radio label="0">超级管理员</el-radio>
            <el-radio label="1">管理员</el-radio>
            <el-radio label="2">普通用户</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>













<script>
import {ref} from "vue";

export default {
  name: "Main",
  data() {
    let checkDuplicate = (rule, value, callback) => {
      if (this.form.id) {
        return callback();
      }
      this.$axios.get(this.$api+`/user/findByNo?no=`+this.form.no).then(res=>res.data).then(res=>{
        if (res.code!= 200){
          callback()
        }else {
          callback(new Error('账号重复'))
        }
      })
    };
    return {
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      name:'',
      centerDialogVisible: false,
      form: {
        id: '',
        name: '',
        no: '',
        password: '',
        phone: '',
        roleId: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入名字', trigger: 'blur' },
          { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' }
        ],
        no: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { min: 8, max: 8, message: '请输入3 到 8位账号', trigger: 'blur' },
          { validator: checkDuplicate, trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 12, message: '请输入6 到 12位密码', trigger: 'blur' }
        ],
        roleId: [
          { required: true, message: '请选择角色', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { min: 11, max: 11, message: '请输入11位电话号码', trigger: 'blur' },
          { pattern: /^1[3|4|5|7|8][0-9]\d{8}$/, message: '请输入正确电话号码', trigger: 'blur' }
        ]
      }
    }
  },
  methods:{
    resetForm(){
      // this.$refs.form.resetFields();
      this.form = {
        name: '',
        no: '',
        password: '',
        phone: '',
        roleId: ''
      }
    },
    del(id){
      console.log(id)
      this.$axios.get(this.$api+'/user/del?id='+id).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code === 200){
          this.$message.success({
            message: '保存成功',
            type: 'success'
          });
          this.loadPost()
        }else {
          this.$message.error({
            message: '保存失败',
            type: 'error'
          });
        }
        // this.tableData = res
      })
    },
    mod(row){
      console.log(row)
      this.form.id = row.id
      this.form.name = row.name
      this.form.no = row.no
      this.form.password = row.password
      this.form.roleId = row.roleId+''
      this.form.phone = row.phone
      this.centerDialogVisible = true
    },
    add(){
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm()
      })
    },
    doMod(){
      this.$axios.post(this.$api+'/user/update',this.form).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code === 200){
          this.$message.success({
            message: '保存成功',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
        }else {
          this.$message.error({
            message: '保存失败',
            type: 'error'
          });
        }
        // this.tableData = res
      })
    },
    doSave(){
      this.$axios.post(this.$api+'/user/save',this.form).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code === 200){
          this.$message.success({
            message: '保存成功',
            type: 'success'
          });
          this.centerDialogVisible = false
          this.loadPost()
        }else {
          this.$message.error({
            message: '保存失败',
            type: 'error'
          });
        }
        // this.tableData = res
      })
    },
    save(){
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.id){
            this.doMod();
          }else {
            this.doSave();
          }
        } else {
          console.log('error submit!');
          return false;
        }
      });
    },
    handleSizeChange (val) {
      console.log(`每页 ${val} 条`);
      this.pageNum = 1
      this.pageSize = val
      this.loadPost()
    },
    handleCurrentChange (val) {
      console.log(`当前页 ${val} `);
      this.pageNum = val
      this.loadPost()
    },
    loadGet(){
      this.$axios.get(this.$api+`/user/list`).then(res=>res.data).then(res=>{
        console.log(res)
      })
    },
    resetParam(){
      this.name=''
      this.loadPost()
    },
    loadPost(){
      this.$axios.post(this.$api+'/user/listPageC1',{
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        param:{
          name:this.name
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code === 200){
          this.tableData=res.data
          this.total=res.total
        }else {
          alert('获取失败')
        }
        // this.tableData = res
      })
    }
  },
  beforeMount() {
    //this.loadGet();
    this.loadPost()
  }
}
</script>


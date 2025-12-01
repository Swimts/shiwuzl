<template>
  <div class="page-container">
    <div>
      <el-input v-model="name" placeholder="请输入名称" style="width: 200px" @keyup.enter.native="loadPost" />
      <el-button type="primary" style="margin-left: 5px" @click="loadPost">搜索</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="add">新增</el-button>
    </div>
    <el-table :data="tableDataWithUser"
              :header-cell-style="{background:'#f5f7fa',color:'#606266'}"
              border
              style="width: 100%;height: 100%">
      <el-table-column label="用户账号" align="center" v-if="user.roleId!=2">
        <template v-slot="scope">
          {{ scope.row.userNo }} <!-- 显示用户账号 -->
        </template>
      </el-table-column>
      <el-table-column prop="itemname" label="名称"  align="center"/>
      <el-table-column prop="description" label="描述"  align="center" />
      <el-table-column prop="location" label="地点"  align="center" />
      <el-table-column prop="imagepath" label="图片"  align="center">
        <template  v-slot="scope">
          <el-image
            style="width: 60px;height: 60px;"
            :src="this.$api+`/lostandfound/getImage/${scope.row.imagepath}`"
            :preview-src-list="[this.$api+`/lostandfound/getImage/${scope.row.imagepath}`]"
            :hide-on-click-modal="true"
            :preview-teleported="true"
          >
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="claimed" label="认领人"  align="center" >
        <template v-slot="scope">
          {{ scope.row.claimedNo }} <!-- 显示用户账号 -->
        </template>
      </el-table-column>
      <el-table-column prop="operate" label="操作"  align="center" >
        <template v-slot="scope">
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
      <el-form :model="lostandfound" :rules="rules"  ref="form" label-width="100px">
        <el-form-item label="物品名称" prop="itemname">
          <el-input v-model="lostandfound.itemname" v-text="xxx" placeholder="列：手机" />
        </el-form-item>
        <el-form-item label="物品描述" prop="description">
          <el-input v-model="lostandfound.description" placeholder="列：小米6，颜色：黑色" />
        </el-form-item>
        <el-form-item label="物品地点" prop="location">
          <el-input v-model="lostandfound.location" placeholder="列：图书馆" />
        </el-form-item>
        <el-form-item label="上传图片">
          <input type="file" @change="handleFileUpload" />
        </el-form-item>
        <el-form-item label="类型" prop="lostorfound">
          <el-radio-group v-model="lostandfound.lostorfound">
            <el-radio label="0">失物</el-radio>
            <el-radio label="1">拾物</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确认</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<!--加上scoped能够防止样式之间的冲突-->
<style scoped>

.page-container {
  display: flex;
  flex-direction: column;
  //min-height: 10vh;
  width: 100%;
  height: calc(100vh - 80px);
}

</style>


<script>
import {ref} from "vue";
import user from "@element-plus/icons/es/user.js";

export default {
  name: "Mainsw",
  data() {
    const curUser = JSON.parse(sessionStorage.getItem('CurUser'));
    return {
      imageData: [],
      imageUrl: '',
      user: curUser,
      userList: [],
      fileList: [],
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      name:'',
      centerDialogVisible: false,
      itemname: '',
      description: '',
      location: '',
      imagePath: '', // 这里存储图片路径
      lostandfound: { // 新增的lostandfound数据
        id: '',
        userid: curUser ? curUser.id: '',
        lostorfound: '',
        itemname: '',
        description: '',
        location: '',
        imagePath: '',
        claimed: '',
      },
      imagePreview: null,
      form: {
        id: '',
        userid: '',
        lostorfound: '',
        itemname: '',
        description: '',
        location: '',
        imagepath: '',
        name: '',
        no: '',

      },
      rules: {
        itemname: [
          { required: true, message: '请输入物品名称', trigger: 'blur' },
          { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入物品描述', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        location: [
          { required: true, message: '请输入物品地点', trigger: 'blur' },
          { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        lostorfound: [
          { required: true, message: '请选择类型', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    //this.fetchImage();
  },
  methods:{
    //弹窗重置
    resetForm(){
      this.lostandfound = {
        id: '',
        userid: '',
        lostorfound: '',
        itemname: '',
        description: '',
        location: '',
        imagePath: '',
      }
    },
    //图片上传
    handleFileUpload(event) {
      const file = event.target.files[0]; // 获取上传的文件
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.imagePreview = e.target.result;
          this.lostandfound.imagePath = e.target.result;
        };
        this.uploadedImage = file; // 保存文件数据
      }
    },
    handleSubmit() {
      // 提交表单时，首先检查表单是否合法
      this.$refs.form.validate((valid) => {
        if (valid) {
          const formData = new FormData();

          if (this.uploadedImage) {
            formData.append('image', this.uploadedImage);
          }
          this.lostandfound.userid = this.user.id;
          formData.append('lostandfound', JSON.stringify(this.lostandfound));

          // 发起请求，提交数据到服务器
          this.$axios.post(this.$api+'/lostandfound/saveWithImage', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          })
            .then(response => {
              // 提交成功后的操作
              this.$message.success('上传成功');
              this.centerDialogVisible = false
              this.loadPost()
            })
            .catch(error => {
              // 提交失败后的操作
              this.$message.error('上传失败');
            });
        } else {
          this.$message.error('表单验证失败');
        }
      });
    },
    handleCancel() {
      this.centerDialogVisible = false
    },
    //删除
    del(id) {
      console.log(id);
      this.$axios.get(this.$api + '/lostandfound/del?id=' + id).then(res => res.data).then(res => {
        console.log(res);
        if (res.code === 200) {
          this.$message.success({
            message: '保存成功',
            type: 'success'
          });
          this.loadPost();
          this.$forceUpdate(); // 强制更新组件，确保数据状态同步
        } else {
          this.$message.error({
            message: '保存失败',
            type: 'error'
          });
        }
      })
    },
    //编辑
    //新增弹窗
    add(){
      this.centerDialogVisible = true
      this.$nextTick(() => {
        this.resetForm()
      })
    },

    //保存和修改
    //分页
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
    //查询
    created(){
      this.$axios.get(this.$api+'/user/list')
        .then(res => res.data)
        .then(res => {
          console.log(res);
          this.userList = res;
        });
    },
    resetParam(){
      this.name=''
      this.loadPost()
    },
    loadPost(){
      this.$axios.post(this.$api+'/lostandfound/listPage',{
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        param:{
          name:this.name,
          userid:this.user.id,
          roleId: this.user.roleId,
          lostorfound: 0
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if (res.code === 200){
          this.tableData = res.data
          this.total = res.total
        }else {
          alert('获取失败')
        }
        // this.tableData = res
      })
    }
  },
  mounted() {
    //this.loadGet();
    this.created();
    this.loadPost();
  },
  computed: {
    tableDataWithUser(){
      return this.tableData.map(item => {
        const user = this.userList.find(user => user.id === item.userid);
        const userclaimed = this.userList.find(user => user.id === item.claimed);
        return {
          ...item,
          userNo: user ? user.no : '未知用户',
          claimedNo: userclaimed ? userclaimed.phone : '未认领'
        };
      });
    }
  },
  beforeMount() {
    //this.loadGet();
    this.loadPost()
  }
}
</script>


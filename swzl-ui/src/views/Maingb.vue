<template>
  <div class="page-container">
    <div>
      <el-input v-model="name" placeholder="请输入名称" style="width: 200px" @keyup.enter.native="loadPost" />
      <el-button type="primary" style="margin-left: 5px" @click="loadPost">搜索</el-button>
      <el-button type="success" @click="resetParam">重置</el-button>
      <el-button type="primary" @click="filterClaimed" style="margin-left: 5px">已认领</el-button>
      <el-button type="primary" @click="filterUnclaimed" style="margin-left: 5px">未认领</el-button>
    </div>
    <el-table :data="filteredTableData"
              :header-cell-style="{background:'#f5f7fa',color:'#606266'}"
              border
              style="width: 100%;height: 100%">
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
      <el-table-column prop="operate" label="操作"  align="center" >
        <template v-slot="scope">
          <el-popconfirm title="确定认领该物品吗？" @confirm="claim(scope.row.id)">
            <!-- 将 slot="reference" 的按钮文本修改为 '认领' -->
            <template #reference>
              <el-button slot="reference" type="primary" size="small"
                         :class="{ 'claimed-button': scope.row.buttonText === '已认领' }"
                         :disabled="scope.row.buttonDisabled">
                {{ scope.row.buttonText }}
              </el-button>
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
  </div>
</template>

<!--加上scoped能够防止样式之间的冲突-->
<style scoped>


.page-container {
  display: flex;
  flex-direction: column;
  //min-height: 10vh;
  width: 100%;
  height: calc(100vh - 140px);

}

body {
  background-color: #ff0000;
}

.claimed-button {
  //color: red;
  background-color: red;
}
</style>


<script>
import {ref} from "vue";
import user from "@element-plus/icons/es/user.js";

export default {
  name: "Maingb",
  data() {
    const curUser = JSON.parse(sessionStorage.getItem('CurUser'));
    return {
       // 用于存储当前选择的认领状态筛选条件，初始化为null表示不筛选
      filteredTableData: [],
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
    }
  },
  created() {
    //this.fetchImage();
  },
  methods:{
    //认领
    claim(id){
      this.$axios.post(this.$api+'/lostandfound/claim?itemId='+id,{
        userId: this.user.id
      }).then(res => {
        console.log(res);
        if (res.status === 200){
          this.$message({
            message: '认领成功',
            type: 'success'
          });
          this.loadPost()
        }else {
          alert('认领失败')
        }
      })
    },
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
      this.filteredTableData = this.tableData;
      this.loadPost()
    },
    loadPost() {
      this.$axios.post(this.$api + '/lostandfound/listPageC1', {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        param: {
          name: this.name,
          userid: this.user.id,
          roleId: this.user.roleId,
          lostorfound: 0
        }
      }).then(res => res.data).then(res => {
        console.log(res);
        if (res.code === 200) {
          this.tableData = res.data.map(item => ({
            ...item,
            buttonText: item.claimed? '已认领' : '未认领',
            buttonDisabled: item.claimed
          }));
          this.filteredTableData = this.tableData; // 初始获取数据时，先显示全部数据
          this.total = res.total;
        } else {
          alert('获取失败');
        }
      })
    },
    filterClaimed() {
      this.filteredTableData = this.tableData.filter(item => item.claimed); // 筛选出已认领的数据
    },
    filterUnclaimed() {
      this.filteredTableData = this.tableData.filter(item =>!item.claimed); // 筛选出未认领的数据
    }
  },
  mounted() {
    //this.loadGet();
    this.created();
    this.loadPost();
  },
  computed: {
    tableDataWithUser(){
      return this.filteredTableData.map(item => {
        const user = this.userList.find(user => user.id === item.userid);
        return {
          ...item,
          userNo: user ? user.no : '未知用户'
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


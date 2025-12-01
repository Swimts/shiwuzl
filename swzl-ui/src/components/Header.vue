<template>
  <div>
    <el-icon style="display: flex;font-size: 24px;margin-top: 15px" @click="collapse">
      <Fold />
    </el-icon>
  </div>
  <div style="flex: 1;margin-top: -13px">
    <span style="color: #000000;font-size: 15px">{{ user.name }}</span>
    <el-dropdown>
      <el-icon style=" margin-top: 1px;font-size: 20px;margin-left: 5px;color: #000000">
        <setting />
      </el-icon>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
          <el-dropdown-item @click.native="logout">退出登入</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<!--加上scoped能够防止样式之间的冲突-->
<style scoped>
body {
  background-color: rgba(80, 25, 8, 0.9);
}
</style>

<script>
import {Location, Setting, Menu ,Document,Fold} from "@element-plus/icons-vue";

export default {
  name: "Header",
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser'))
    }
  },
  emits: ['doCollapse'],
  methods: {
    toUser () {
      console.log('to_user')
      this.$router.push('/Home')
    },
    logout() {
      console.log('logout')

      this.$confirm('确定退出登入？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '退出登入成功!'
        });
        this.$router.push('/')
        sessionStorage.clear()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出登入'
        });
      });

    },
    collapse(){
      this.$emit('doCollapse')
    }
  },
  created(){
    this.$router.push('/Home')
  },
  components: {
    Location, Setting, Menu ,Document,Fold
  }
}
</script>


<template>
  <el-scrollbar style="height: 100%;">
    <el-menu background-color="#545c64" text-color="#fff" active-text-color="#ffd04b" style="height: 100%;"
             default-active=""
             :collapse="isCollapse"
             :collapse-transition="false"
    >
      <el-menu-item index="Home">
        <el-icon @click="tohome">
          <House />
        </el-icon>
        <span @click="tohome">首页</span>
      </el-menu-item>
      <el-menu-item index="1" v-if="user.roleId==0">
        <el-icon @click="toun">
          <User />
        </el-icon>
        <span @click="toun" >用户信息</span>
      </el-menu-item>
      <el-menu-item index="2">
        <el-icon @click="tolost">
          <Minus />
        </el-icon>
        <span @click="tolost">失物信息</span>
      </el-menu-item>
      <el-menu-item index="3">
        <el-icon @click="tofound">
          <Plus />
        </el-icon>
        <span @click="tofound">拾物信息</span>
      </el-menu-item>
      <el-menu-item index="4">
        <el-icon @click="tomod">
          <Setting />
        </el-icon>
        <span @click="tomod">修改信息</span>
      </el-menu-item>
    </el-menu>
  </el-scrollbar>

</template>




<script>
import {Message, Setting, House, Menu, Tools, UserFilled, Minus, Plus, User} from "@element-plus/icons-vue";

export default {
  components: {Setting, Message, House, Menu, Tools, UserFilled, Minus, Plus, User},
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser')),
      //isCollapse:false,
    }
  },
  props: {
    isCollapse: Boolean
  },
  methods: {
    tohome() {
      this.$router.push('/Maingb')
    },
    toun() {
      this.$router.push('/Main')
    },
    tolost() {
      console.log('tolost')
      this.$router.push('/Mainsw')
    },
    tofound() {
      console.log('tofound')
      this.$router.push('/Mainzl')
    },
    tomod() {
      console.log('tomod')
      this.$router.push('/Modin')
    },
  }
}
</script>


<style scoped>
.sidebar-item {
  position: absolute;
  left: -9999px; /* 或其他样式 */
}
</style>

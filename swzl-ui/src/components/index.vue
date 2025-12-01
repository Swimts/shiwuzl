<template>
  <el-container class="layout-container-demo" style="height: 100%;border: 1px solid #eee">
    <el-aside :width="aside_witdh" style="height: 100vh;background-color: #545c64;margin-left: -1px;">
      <Aside :isCollapse="isCollapse" style="height: 100%"></Aside>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px;background-color: #ffffff;margin-left: -1px;" >
          <Header @doCollapse="doCollapse"></Header>
      </el-header>

      <el-main>
        <el-scrollbar>
          <router-view/>
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";

import aside from "@/components/Aside.vue";

export default {
  name: "Index",
  computed: {
    aside() {
      return aside
    }
  },
  components:{Aside,Header},
  data(){
    return {
      isCollapse:false,
      aside_witdh:'200px'
    }
  },
  methods:{
    doCollapse(){
      console.log(111)
      this.isCollapse = !this.isCollapse
      if(!this.isCollapse){
        this.aside_witdh='200px'
      }else {
        this.aside_witdh='64px'
      }
    }
  }
}
</script>




<!--<script lang="ts" setup>-->
<!--import { ref } from 'vue'-->
<!--import {Document, Fold, Location, Menu, Menu as IconMenu, Message, Setting} from '@element-plus/icons-vue'-->
<!--import Aside from "@/components/Aside.vue";-->
<!--import Header from "@/components/Header.vue";-->
<!--import Main from "@/components/Main.vue";-->
<!--import {log} from "echarts/types/src/util/log";-->

<!--</script>-->

<style scoped>
.layout-container-demo .el-header {
  position: relative;
  background-color: var(--el-color-primary-light-7);
  color: var(--el-text-color-primary);
}
.layout-container-demo .el-aside {
  color: var(--el-text-color-primary);
  background: var(--el-color-primary-light-8);
}
.layout-container-demo .el-menu {
  border-right: none;
}
.layout-container-demo .el-main {
  padding: 0;
}
.layout-container-demo .toolbar {
  display: flex;
  //align-items: center;
  //justify-content: center;
  //height: 100%;
  right: 20px;
}
</style>

<template>
  <div>
    <!-- 顶部导航栏 -->
    <el-header style="background-color: #2a3d66">
      <Headergc style="width: 100%;"></Headergc>
    </el-header>

    <!-- 主内容区 -->
    <el-main>
      <el-scrollbar>
        <div>
        <router-view></router-view>
<!--        <Maingc></Maingc>-->
        </div>
      </el-scrollbar>
    </el-main>

    <!-- 页脚 -->
    <el-footer class="footer">
      <div >
        <Footergc style="width: 100%"></Footergc>
      </div>
    </el-footer>
  </div>
</template>

<script>
import Headergc from "@/views/Headergc.vue";
import Footergc from "@/views/Footergc.vue";
import aside from "@/components/Aside.vue";

export default {
  name: 'Indexgc',
  computed: {
    aside() {
      return aside
    }
  },
  components:{Headergc,Footergc},
  data() {
    return {
      // active: '1', // 默认选中失物
      // currentPage: 'lost' // 当前展示的页面
    }
  },
  methods: {
  }
}
</script>



<style scoped>


.logo h1 {
  font-size: 24px;
  margin: 0;
}

.footer {
  background-color: #2a3d66;
  color: white;
  text-align: center;
  padding: 10px;
  position: fixed;
  bottom: 0;
  z-index: 10;
  width: 100%;
  height: 50px;
}
</style>

<template>
  <div>
    <el-menu

      :default-active="activeIndex"
      class="nav-menu"
      mode="horizontal"
    >
      <el-menu-item index="lost-items" @click="tosw">失物信息</el-menu-item>
      <el-menu-item index="found-items" @click="tozl">拾物信息</el-menu-item>
      <el-menu-item index="personal-center" @click="tohom">个人中心</el-menu-item>

    </el-menu>

  </div>
</template>

<!--加上scoped能够防止样式之间的冲突-->
<style scoped>

 .nav-menu {

  color: white;
  text-align: center;
  padding: 10px;
}

</style>

<script>
export default {
  name: 'Headergc',
  data() {
    return {msg: ''}
  },
  methods: {
    tohom(){
      this.$router.push('/Home')
    },
    tosw(){
      this.$router.push('/Maingb')
    },
    tozl(){
      this.$router.push('/Maingc')
    }
  }
}
</script>


<template>
  <div class="profile-container">
    <div class="header">
      <h1>个人中心</h1>
      <p>欢迎回来，{{ user.name }}！</p>
    </div>

    <div class="content">
      <div class="info">
        <p><strong>用户名:</strong> {{ user.no }}</p>
        <p><strong>电话:</strong> {{ user.phone }}</p>
        <p><strong>登录时间:</strong> {{ currentTime }}</p>
      </div>
      <div class="time">
        <p>当前时间: {{ currentTime }}</p>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  name: "Home",
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('CurUser'))
    };
  },
}
</script>



<script setup>
import { ref, onMounted } from 'vue';

// 模拟用户信息
const userInfo = ref({
  name: "张三",
  username: "zhangsan",
  email: "zhangsan@example.com"
});

// 当前时间
const currentTime = ref("");

// 更新当前时间
const updateTime = () => {
  const now = new Date();
  currentTime.value = now.toLocaleString();
};

// 页面加载后更新一次时间
onMounted(() => {
  updateTime();
  setInterval(updateTime, 1000); // 每秒更新一次时间
});
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
  background-color: #f9f9f9;
}



.header {
  text-align: center;
  margin-bottom: 20px;
}

.content {
  font-size: 16px;
}

.info p {
  margin: 5px 0;
}

.time {
  margin-top: 20px;
  text-align: center;
  font-size: 14px;
  color: #888;
}
</style>

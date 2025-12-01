<template>
  <div>
    {{ msg}}
  </div>
</template>

<!--加上scoped能够防止样式之间的冲突-->
<style scoped>
body {
  background-color: #ff0000;
}
</style>

<script>
export default {
  data() {
    return {msg: '齐健锋 © 2024 校园失物招领平台'}
  }
}
</script>


<template>
  <div id="app">
<!--    <Index></Index>-->
    <router-view />
  </div>
</template>


<!--<script setup lang="ts">-->
<!--import { RouterLink, RouterView } from 'vue-router'-->
<!--</script>-->
<script setup>
</script>

<script>

import Index from "@/components/Index.vue";

export default {
  name: 'App',
  components: {
     //Index
  }
};
</script>

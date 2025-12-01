import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import './assets/global.css';
import axios from 'axios';
import './assets/fonts/fonts.css'

import router from './router';

//axios.defaults.baseURL = 'http://localhost:8080'
const app = createApp(App);

app.config.globalProperties.$axios = axios
app.config.globalProperties.$api = 'http://localhost:8090'
app.use(ElementPlus,{ size: 'small'});

app.use(createPinia());
app.use(router);

app.mount('#app')



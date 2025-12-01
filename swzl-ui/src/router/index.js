import { createRouter, createWebHistory } from 'vue-router'
import Login from '../components/Login.vue'; // 导入 Login.vue 页面
import Lndgc from '../views/Indexgc.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login
    },
    {
      path: '/Index',
      name: 'index',
      component: () => import('../components/Index.vue'),
      children: [
        {
          path: '/Main',
          name: 'main',
          component: () => import('../components/Main.vue')
        },
        {
          path: '/Home',
          name: 'home',
          component: () => import('../components/Home.vue')
        },
        {
          path: '/Mainsw',
          name: 'Mainsw',
          component: () => import('../components/Mainsw.vue')
          // props: true
        },
        {
          path: '/Mainzl',
          name: 'Mainzl',
          component: () => import('../components/Mainzl.vue')
          // props: true
        },
        {
          path: '/Modin',
          name: 'Modin',
          component: () => import('../components/Modin.vue')
        },
      ]
    },
    {
      path: '/Indexgc',
      name: 'indexgc',
      component: Lndgc,
      children: [
        {
          path: '/Maingb',
          name: 'Maingb',
          component: () => import('../views/Maingb.vue')
          // props: true
        },
        {
          path: '/Maingc',
          name: 'Maingc',
          component: () => import('../views/Maingc.vue')
        },
      ]
    }
  ]
});

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = JSON.parse(sessionStorage.getItem('CurUser')); // 从本地存储中获取 token
  if (to.path === '/') {
    // 如果访问的是登录页面
    if (token) {
      // 如果已经登录，则重定向到首页
      next('/Index');
    } else {
      // 如果没有登录，则允许访问登录页面
      next();
    }
  } else {
    // 如果访问的不是登录页面
    if (token) {
      // 如果已经登录，则允许访问其他页面
      next();
    } else {
      // 如果没有登录，则重定向到登录页面
      next('/');
    }
  }
});



// const router = createRouter({
//   mode: 'history',
//   routes
// });
export default router;





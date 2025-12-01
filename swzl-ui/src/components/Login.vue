<template>
  <div id="app">

    <div class="loginBody">
      <span style="font-family: my-self-font;font-size: 150px;display: block;text-align: center;">校园失物招领</span>

      <div class="loginDiv">
        <div class="login-content">
          <h2 class="login-title">用户登入</h2>
          <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="100px">
            <el-form-item label="账号" prop="no">
              <el-input style="" v-model="loginForm.no" autocomplete="off" size="small" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input style="" v-model="loginForm.password" show-password @keyup.enter.native="confirm" autocomplete="off" size="small" placeholder="请输入密码" type="password" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="confirm" size="small" style="width:100%;" :disabled="confirm_disabled">登入</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<!--加上scoped能够防止样式之间的冲突-->
<style scoped>
.loginBody {
  position: absolute;
  width: 100%;
  height: 100%;
  /* 设置背景图片，并使其覆盖整个区域，固定位置不随滚动而移动 */
  background-image: url('../assets/hu2.jpg'); /* 将这里替换为你实际的图片路径或网络链接 */
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  /* 设置背景颜色作为备用，当图片加载失败等情况时显示 */
  background-color: #B3C0D1;
}

.loginDiv {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -200px;
  margin-left: -250px;
  width: 450px;
  height: 330px;
  /* 设置登录框为半透明 */
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 5%;
}

.login-title {
  margin: 20px 0;
  text-align: center;
  /* 设置标题颜色 */
  color: #333;
}

.login-content {
  width: 400px;
  height: 250px;
  position: absolute;
  top: 45px;
  left: 1px;
}

/* 以下是对表单输入框和按钮的一些样式微调，可根据实际需求调整 */
.el-input {
  border: 1px solid #ccc;
  border-radius: 3px;
}

.el-button[type="primary"] {
  background-color: #409EFF;
  border-color: #409EFF;
  color: white;
}
</style>

<script>
import login from "@/components/Login.vue";

export default {
  name: "Login",
  data() {
    return {
      confirm_disabled: false,
      loginForm: {
        no: "",
        password: ""
      },
      rules: {
        no: [
          { required: true, message: "请输入账号", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" }
        ],
      }
    }
  },
  mounted() {
    app.title = "校园失物招领";
  },
  methods: {
    confirm() {
      this.confirm_disabled = true;
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.$axios.post(this.$api + '/user/login', this.loginForm).then(res => res.data).then(res => {
            console.log(res)
            if (res.code === 200) {
              sessionStorage.setItem('CurUser', JSON.stringify(res.data))
              this.$router.replace('/Index');
            } else {
              this.confirm_disabled = false;
              alert('账号或密码错误')
              return false;
            }
          });
        } else {
          this.confirm_disabled = false;
          console.log('error submit!');
          return false;
        }
      });
    }
  }
}
</script>
















<!--<template>-->
<!--  <div class="loginBody">-->
<!--    <div class="loginDiv">-->
<!--      <div class="login-content">-->
<!--        <h1 class="login-title">用户登入</h1>-->
<!--        <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="100px" >-->
<!--          <el-form-item label="账号" prop="no">-->
<!--            <el-input style="" v-model="loginForm.no" autocomplete="off" size="small" placeholder="请输入用户名" />-->
<!--          </el-form-item>-->
<!--          <el-form-item label="密码" prop="password">-->
<!--            <el-input style="" v-model="loginForm.password" show-password @keyup.enter.native="confirm" autocomplete="off" size="small" placeholder="请输入密码" type="password" />-->
<!--          </el-form-item>-->
<!--          <el-form-item>-->
<!--            <el-button type="primary" @click="confirm" size="small" style="width:100%;" :disabled="confirm_disabled">登入</el-button>-->
<!--          </el-form-item>-->
<!--        </el-form>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->
<!--&lt;!&ndash;加上scoped能够防止样式之间的冲突&ndash;&gt;-->
<!--<style scoped>-->
<!--    .loginBody {-->
<!--      position: absolute;-->
<!--      width: 100%;-->
<!--      height: 100%;-->
<!--      background-color: #B3C0D1;-->
<!--    }-->
<!--    .loginDiv {-->
<!--      position: absolute;-->
<!--      top: 50%;-->
<!--      left: 50%;-->
<!--      margin-top: -200px;-->
<!--      margin-left: -250px;-->
<!--      width: 450px;-->
<!--      height: 330px;-->
<!--      background-color: #fff;-->
<!--      border-radius: 5%;-->
<!--    }-->
<!--    .login-title {-->
<!--      margin: 20px 0;-->
<!--      text-align: center;-->
<!--    }-->
<!--    .login-content {-->
<!--      width: 400px;-->
<!--      height: 250px;-->
<!--      position: absolute;-->
<!--      top: 25px;-->
<!--      left: 25px;-->
<!--    }-->
<!--</style>-->

<!--<script>-->
<!--import login from "@/components/Login.vue";-->

<!--export default {-->
<!--  name: "Login",-->
<!--  data() {-->
<!--    return {-->
<!--      confirm_disabled: false,-->
<!--      loginForm: {-->
<!--        no: "",-->
<!--        password: ""-->
<!--      },-->
<!--      rules: {-->
<!--        no: [-->
<!--          { required: true, message: "请输入账号", trigger: "blur" }-->
<!--        ],-->
<!--        password: [-->
<!--          { required: true, message: "请输入密码", trigger: "blur" }-->
<!--        ],-->
<!--      }-->
<!--    }-->
<!--  },-->
<!--  methods: {-->
<!--    confirm() {-->
<!--      this.confirm_disabled = true;-->
<!--      this.$refs.loginForm.validate(valid => {-->
<!--        if (valid) {-->
<!--          this.$axios.post(this.$api+'/user/login',this.loginForm).then(res=>res.data).then(res=>{-->
<!--            console.log(res)-->
<!--            if (res.code === 200){-->
<!--              sessionStorage.setItem('CurUser',JSON.stringify(res.data))-->
<!--              this.$router.replace('/Index');-->
<!--            }else {-->
<!--              this.confirm_disabled = false;-->
<!--              alert('账号或密码错误')-->
<!--              return false;-->
<!--            }-->
<!--          });-->
<!--        } else {-->
<!--          this.confirm_disabled = false;-->
<!--          console.log('error submit!');-->
<!--          return false;-->
<!--        }-->
<!--      });-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->


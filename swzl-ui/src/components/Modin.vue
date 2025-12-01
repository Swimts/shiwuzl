<template>
  <div class="user-info-container">
    <el-card class="user-info-card">
<!--      <h3>修改个人信息</h3>-->
      <el-form :model="editForm" label-width="100px" class="edit-form" :rules="formRules">
        <el-form-item label="账号">
          <el-input v-model="editForm.no" disabled></el-input>
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="editForm.password" type="password" placeholder="请输入新密码" v-if="isPasswordEditable">
          </el-input>
          <el-button v-else @click="togglePasswordVisibility">显示密码</el-button>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="editForm.confirmPassword" type="password" placeholder="请再次输入新密码"></el-input>
        </el-form-item>

        <el-form-item label="电话" prop="phone">
          <el-input v-model="editForm.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button :loading="isLoading" type="primary" @click="saveUserInfo">保存</el-button>
          <el-button @click="cancelEdit">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Modin',
  data() {
    const curUser = JSON.parse(sessionStorage.getItem('CurUser'));
    return {
      isLoading: false,
      isPasswordEditable: true,
      editForm: {
        id: curUser.id,
        no: curUser.no,
        name: '',
        password: '',
        phone: '',
        roleId: '',
        confirmPassword: ''
      },
      formRules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 12, message: '请输入6 到 12位密码', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的电话号码', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: this.validateConfirmPassword, trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    togglePasswordVisibility() {
      this.isPasswordEditable = true;
    },
    validateConfirmPassword(rule, value, callback) {
      if (value === '') {
        callback();
      } else if (value === this.editForm.password) {
        callback();
      } else {
        callback(new Error('两次密码输入不一致'));
      }
    },
    // 获取当前用户信息
    loadUserInfo() {
      if (!this.editForm.no) {
        this.$message.error({
          message: '账号（no）为空，请重新登录。',
          type: 'error'
        });
        return;
      }

      this.isLoading = true;
      this.$axios
        .get(`${this.$api}/user/findByNo`, { params: { no: this.editForm.no } })
        .then(res => res.data)
        .then(res => {
          if (res.code === 200) {
            if (res.data && res.data.length > 0) {

              // this.editForm = res.data[0];
              this.editForm = {
                ...res.data[0],
                     // 将 name 设置为空
                phone: '',     // 将 phone 设置为空
                password: ''   // 将 password 设置为空
              };
              this.$message.success({
                message: '加载成功',
                type: 'success'
              });
            } else {
              this.$message.error({
                message: '未找到用户信息',
                type: 'error'
              });
            }
          } else {
            this.$message.error({
              message: '加载失败',
              type: 'error'
            });
          }
          this.isLoading = false;
        })
        .catch(error => {
          this.isLoading = false;
          console.error(error);
          this.$message.error({
            message: '加载失败',
            type: 'error'
          });
        });
    },

    // 保存用户信息
    saveUserInfo() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.isLoading = true;
          this.$axios.post(this.$api + '/user/update', this.editForm).then(res => res.data).then(res => {
            if (res.code === 200) {
              this.$message.success({
                message: '保存成功',
                type: 'success'
              });
            } else {
              this.$message.error({
                message: '保存失败：' + res.msg,
                type: 'error'
              });
            }
            this.isLoading = false;
          }).catch(error => {
            this.isLoading = false;
            this.$message.error('保存失败');
          });
        }
      });
    },

    cancelEdit() {
      // 取消编辑逻辑，可能需要清空表单或跳转回上一页
      this.$router.push('/Home');
    }
  },
  mounted() {
    if (this.editForm.no) {
      this.loadUserInfo();
    }
  }
};
</script>

<style scoped>
.user-info-container {
  margin: 20px;
}

.user-info-card {
  padding: 20px;
}

.edit-form {
  max-width: 600px;
  margin: 0 auto;
}
</style>

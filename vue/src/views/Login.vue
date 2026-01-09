<template>
  <div class="container">
    <div style="width: 500px; background-color: #ffd153;display: flex; align-items: center; justify-content: center;">
      <img src="../assets/imgs/bg.jpg" alt="" style="width: 100%;">
    </div>
    <div style="flex: 1; background-color: #f8f8f8">
      <div style="height: 50px; display: flex; align-items: center; background-color: white; padding-left: 20px">
        <img src="@/assets/imgs/logo.png" alt="" style="width: 40px">
        <span style="font-size: 24px; margin-left: 5px">TradeLoop</span>
      </div>
      <div style="height: calc(100vh - 50px); display: flex; align-items: center; justify-content: center">
        <div style="width: 400px; padding: 40px; background-color: white; border-radius: 5px;">
          <div style="display: flex; margin-bottom: 50px; font-size: 24px">
            <div style="border-bottom: 2px solid orangered; padding-bottom: 10px">Login</div>
            <a style="color: #333" href="/register"><div style="margin-left: 10px;">Register</div></a>
          </div>
          <el-form :model="form" :rules="rules" ref="formRef">
            <el-form-item prop="username">
              <el-input size="medium" prefix-icon="el-icon-user" placeholder="Please enter username" v-model="form.username"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input size="medium" prefix-icon="el-icon-lock" placeholder="Please enter password" show-password  v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item prop="role">
              <el-select v-model="form.role" style="width: 100%">
                <el-option label="Admin" value="ADMIN"></el-option>
                <el-option label="User" value="USER"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button size="medium" style="width: 100%; background-color: orangered; border-color: orangered; color: white" @click="login">Login</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: { role: 'ADMIN' },
      dialogVisible: true,
      rules: {
        username: [
          { required: true, message: 'Please enter username', trigger: 'blur' },
        ],
        password: [
          { required: true, message: 'Please enter password', trigger: 'blur' },
        ]
      }
    }
  },
  created() {

  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))
              if (res.data.role === 'ADMIN') {
                this.$router.push('/home')
              } else {
                this.$router.push('/front/home')
              }
              this.$message.success('Login successfully')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  display: flex;
}
a {
  color: #2a60c9;
}
</style>
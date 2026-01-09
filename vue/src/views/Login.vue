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
<template>
  <div class="container">
    <!-- Left side - Image -->
    <div class="image-section">
      <img src="../assets/imgs/bg.jpg" alt="" class="bg-image">
    </div>
    
    <!-- Right side - Login Form -->
    <div class="form-section">
      <!-- Header with Logo -->
      <div class="header">
        <img src="@/assets/imgs/logo.png" alt="" class="logo">
        <span class="brand-name">TradeLoop</span>
      </div>
      
      <!-- Login Form Container -->
      <div class="login-container">
        <div class="login-box">
          <!-- Tabs -->
          <div class="tabs">
            <div class="tab-active">Login</div>
            <a href="/register" class="tab-link">Register</a>
          </div>
          
          <!-- Form -->
          <el-form :model="form" :rules="rules" ref="formRef">
            <el-form-item prop="username">
              <el-input 
                size="large" 
                prefix-icon="el-icon-user" 
                placeholder="Please enter username" 
                v-model="form.username"
                class="input-field">
              </el-input>
            </el-form-item>
            
            <el-form-item prop="password">
              <el-input 
                size="large" 
                prefix-icon="el-icon-lock" 
                placeholder="Please enter password" 
                show-password  
                v-model="form.password"
                class="input-field">
              </el-input>
            </el-form-item>
            
            <el-form-item prop="role">
              <el-select v-model="form.role" size="large" class="role-select">
                <el-option label="Admin" value="ADMIN"></el-option>
                <el-option label="User" value="USER"></el-option>
              </el-select>
            </el-form-item>
            
            <el-form-item>
              <el-button 
                size="large" 
                class="login-button" 
                @click="login">
                Login
              </el-button>
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

/* Left side - Image */
.image-section {
  width: 500px;
  background: linear-gradient(135deg, #ffd153 0%, #ffb347 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
}

.bg-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* Right side - Form */
.form-section {
  flex: 1;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
}

/* Header */
.header {
  height: 70px;
  display: flex;
  align-items: center;
  background-color: white;
  padding-left: 30px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.logo {
  width: 45px;
  height: 45px;
}

.brand-name {
  font-size: 26px;
  margin-left: 12px;
  font-weight: 600;
  color: #2c3e50;
  letter-spacing: -0.5px;
}

/* Login Container */
.login-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-box {
  width: 420px;
  max-width: 100%;
  padding: 45px 40px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

/* Tabs */
.tabs {
  display: flex;
  margin-bottom: 40px;
  font-size: 24px;
  gap: 20px;
}

.tab-active {
  color: #ff5722;
  border-bottom: 3px solid #ff5722;
  padding-bottom: 10px;
  font-weight: 600;
}

.tab-link {
  color: #909399;
  text-decoration: none;
  padding-bottom: 10px;
  transition: color 0.3s;
}

.tab-link:hover {
  color: #606266;
}

/* Form Inputs */
.input-field >>> .el-input__inner {
  height: 48px;
  border-radius: 8px;
  font-size: 15px;
  padding-left: 45px;
}

.el-form-item {
  margin-bottom: 22px;
}

.role-select {
  width: 100%;
}

.role-select >>> .el-input__inner {
  height: 48px;
  border-radius: 8px;
  font-size: 15px;
}

/* Login Button */
.login-button {
  width: 100%;
  height: 50px;
  background: linear-gradient(135deg, #ff6b35 0%, #ff5722 100%);
  border: none;
  color: white;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
  margin-top: 10px;
  transition: all 0.3s;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(255, 87, 34, 0.4);
}

.login-button:active {
  transform: translateY(0);
}

/* Responsive */
@media (max-width: 768px) {
  .image-section {
    display: none;
  }
  
  .login-box {
    padding: 30px 25px;
  }
}
</style>

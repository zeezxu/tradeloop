<template>
  <div>
    <el-card style="width: 600px; margin: 0 auto">
      <div slot="header" style="text-align: center">
        <span style="font-size: 18px; font-weight: bold">Personal Information</span>
      </div>
      <el-form :model="user" label-width="100px" style="padding: 20px 50px">
        <div style="margin: 20px 0; text-align: center">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="user.avatar" :src="user.avatar" class="avatar" alt="Avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <div style="margin-top: 10px; color: #999; font-size: 12px">Click to upload avatar</div>
        </div>
        <el-form-item label="Username" prop="username">
          <el-input v-model="user.username" placeholder="Username" disabled></el-input>
        </el-form-item>
        <el-form-item label="Name" prop="name">
          <el-input v-model="user.name" placeholder="Enter your name"></el-input>
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="user.phone" placeholder="Enter phone number"></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="user.email" placeholder="Enter email address"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-top: 30px">
          <el-button type="primary" @click="update" style="width: 200px">Save Changes</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "AdminPerson",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}')
    }
  },
  created() {

  },
  methods: {
    update() {
      // Basic validation
      if (!this.user.name) {
        this.$message.warning('Please enter your name')
        return
      }

      this.$request.put('/admin/update', this.user).then(res => {
        if (res.code === '200') {
          this.$message.success('Saved successfully')

          localStorage.setItem('xm-user', JSON.stringify(this.user))

          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response, file, fileList) {
      this.$set(this.user, 'avatar', response.data)
      this.$message.success('Avatar uploaded successfully')
    },
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
  color: #606266;
}

/deep/.el-card__header {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

/deep/.el-upload {
  border-radius: 50%;
}

/deep/.avatar-uploader .el-upload {
  border: 2px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
  transition: border-color 0.3s;
}

/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 32px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-radius: 50%;
}

.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
  object-fit: cover;
}

/deep/.el-input.is-disabled .el-input__inner {
  background-color: #f5f7fa;
  border-color: #e4e7ed;
  color: #606266;
  cursor: not-allowed;
}
</style>
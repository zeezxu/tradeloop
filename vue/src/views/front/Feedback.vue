<template>
  <div style="width: 50%; margin: 10px auto; padding: 30px" class="card">
    <div style="font-size: 20px; margin-bottom: 30px; text-align: center">Thank you for your feedback</div>
    <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
      <el-form-item label="Title" prop="title">
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item label="Content" prop="content">
        <el-input type="textarea" v-model="form.content"></el-input>
      </el-form-item>
      <el-form-item label="Phone" prop="phone">
        <el-input v-model="form.phone"></el-input>
      </el-form-item>
      <el-form-item label="Email" prop="email">
        <el-input v-model="form.email"></el-input>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="save">Submit</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Feedback",
  data() {
    return {
      form: {},
      rules: {
        title: [
          { required: true, message: 'Please enter title', trigger: 'blur' }
        ],
        content: [
          { required: true, message: 'Please enter content', trigger: 'blur' }
        ]
      }
    }
  },
  created() {

  },
  methods: {
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: '/feedback/add',
            method: 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('Saved successfully')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>

</style>
<template>
  <div>
    <div class="search">
      <el-input placeholder="Search by username" style="width: 200px" v-model="username"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">Add New</el-button>
      <el-button type="danger" plain @click="delBatch">Batch Delete</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe class="wrap-header-table" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="ID" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="username" label="Username" width="150" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="name" label="Name" width="150" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="phone" label="Phone" width="150" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="email" label="Email" width="220" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column label="Avatar" width="100" align="center">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 40px; border-radius: 50%" v-if="scope.row.avatar"
                      :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]"></el-image>
            <span v-else style="color: #999">No Avatar</span>
          </template>
        </el-table-column>
        <el-table-column prop="role" label="Role" width="100" align="center">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.role === 'ADMIN'">Admin</el-tag>
            <el-tag type="success" v-else>User</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" align="center" width="180" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">Edit</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>


    <el-dialog title="User Information" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="Username" prop="username">
          <el-input v-model="form.username" placeholder="Enter username"></el-input>
        </el-form-item>
        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" placeholder="Enter name"></el-input>
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="form.phone" placeholder="Enter phone number"></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="form.email" placeholder="Enter email address"></el-input>
        </el-form-item>
        <el-form-item label="Avatar">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary" size="small">Upload Avatar</el-button>
          </el-upload>
          <div v-if="form.avatar" style="margin-top: 10px">
            <el-image style="width: 80px; height: 80px; border-radius: 50%" :src="form.avatar" :preview-src-list="[form.avatar]"></el-image>
          </div>
        </el-form-item>
        <el-form-item label="Role" prop="role">
          <el-select v-model="form.role" style="width: 100%" placeholder="Select role">
            <el-option value="ADMIN" label="Administrator"></el-option>
            <el-option value="USER" label="User"></el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      username: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        username: [
          {required: true, message: 'Please enter username', trigger: 'blur'},
        ],
        name: [
          {required: true, message: 'Please enter name', trigger: 'blur'},
        ]
      },
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    handleAdd() {
      this.form = { role: 'USER' }
      this.fromVisible = true
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.fromVisible = true
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/user/update' : '/user/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {
              this.$message.success('Saved successfully')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    },
    del(id) {
      this.$confirm('Are you sure you want to delete?', 'Confirm', {type: "warning"}).then(response => {
        this.$request.delete('/user/delete/' + id).then(res => {
          if (res.code === '200') {
            this.$message.success('Deleted successfully')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {
      this.ids = rows.map(v => v.id)
    },
    delBatch() {
      if (!this.ids.length) {
        this.$message.warning('Please select items to delete')
        return
      }
      this.$confirm('Are you sure you want to delete these items?', 'Confirm', {type: "warning"}).then(response => {
        this.$request.delete('/user/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {
            this.$message.success('Deleted successfully')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/user/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.username = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleAvatarSuccess(response, file, fileList) {
      this.form.avatar = response.data
      this.$message.success('Avatar uploaded successfully')
    },
  }
}
</script>

<style scoped>

</style>
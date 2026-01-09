<template>
  <div>
    <div class="search">
      <el-input placeholder="Search by keyword" style="width: 200px" v-model="title"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">Search</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">Reset</el-button>
    </div>

    <div class="operation">
      <el-button type="danger" plain @click="delBatch">Batch Delete</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe class="wrap-header-table" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="ID" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="title" label="Title" width="180" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="Content" width="250" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="img" label="Image" width="100" align="center">
          <template v-slot="scope">
            <el-image v-if="scope.row.img" style="width: 50px" :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="User" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="Release Time" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="solved" label="Resolution" width="110">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.solved === 'unsolved'">Unsolved</el-tag>
            <el-tag type="success" v-if="scope.row.solved === 'solved'">Solved</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Review Status" width="130">
          <template v-slot="scope">
            <el-tag type="info" v-if="scope.row.status === 'Pending'">Pending</el-tag>
            <el-tag type="success" v-if="scope.row.status === 'Approved'">Approved</el-tag>
            <el-tag type="danger" v-if="scope.row.status === 'Rejected'">Rejected</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" align="center" width="300" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="success" plain @click="changeStatus(scope.row, 'Approved')">Approve</el-button>
            <el-button size="mini" type="danger" plain @click="changeStatus(scope.row, 'Rejected')">Reject</el-button>
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

    <el-dialog title="Purchase Request" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="120px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="Title" prop="title">
          <el-input v-model="form.title" placeholder="Title"></el-input>
        </el-form-item>
        <el-form-item label="Content" prop="content">
          <el-input type="textarea" v-model="form.content" placeholder="Content"></el-input>
        </el-form-item>
        <el-form-item label="Image URL" prop="img">
          <el-input v-model="form.img" placeholder="Image URL"></el-input>
        </el-form-item>
        <el-form-item label="Review Status" prop="status">
          <el-select v-model="form.status" style="width: 100%" placeholder="Select status">
            <el-option value="pending" label="Pending"></el-option>
            <el-option value="pass" label="Approved"></el-option>
            <el-option value="rejected" label="Rejected"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="User ID" prop="userId">
          <el-input v-model="form.userId" placeholder="User ID"></el-input>
        </el-form-item>
        <el-form-item label="Release Time" prop="time">
          <el-input v-model="form.time" placeholder="Release time"></el-input>
        </el-form-item>
        <el-form-item label="Resolution" prop="solved">
          <el-select v-model="form.solved" style="width: 100%" placeholder="Select resolution status">
            <el-option value="unsolved" label="Unsolved"></el-option>
            <el-option value="solved" label="Solved"></el-option>
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
  name: "Help",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      title: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
      },
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    changeStatus(row, status) {
      const action = status === 'pass' ? 'approve' : 'reject'
      this.$confirm(`Are you sure you want to ${action} this request?`, 'Confirm', {type: "warning"}).then(response => {
        this.form = JSON.parse(JSON.stringify(row))
        this.form.status = status
        this.$request.put('/help/update', this.form).then(res => {
          if (res.code === '200') {
            this.$message.success('Operation successful')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(err => {})
    },
    handleAdd() {
      this.form = {}
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
            url: this.form.id ? '/help/update' : '/help/add',
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
        this.$request.delete('/help/delete/' + id).then(res => {
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
        this.$request.delete('/help/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/help/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    reset() {
      this.title = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>
<template>
  <div style="width: 70%; margin: 10px auto" class="card">

    <div>
      <el-table :data="tableData" stripe class="wrap-header-table">
        <el-table-column prop="name" label="Product Name" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="price" label="Price" width="100">
          <template v-slot="scope">
            <span style="color: red">￥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="img" label="Image" width="100" align="center">
          <template v-slot="scope">
            <el-image v-if="scope.row.img" style="width: 50px" :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="Address" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="date" label="Release Date" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="Status" width="110">
          <template v-slot="scope">
            <el-tag type="info" v-if="scope.row.status === 'Pending'">Pending</el-tag>
            <el-tag type="success" v-if="scope.row.status === 'Approved'">Approved</el-tag>
            <el-tag type="danger" v-if="scope.row.status === 'Rejected'">Rejected</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="Category" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="saleStatus" label="Sale Status" width="110"></el-table-column>
        <el-table-column prop="readCount" label="Views" width="80"></el-table-column>
        <el-table-column label="Actions" align="center" width="180" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">Edit</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin: 15px 0">
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

    <el-dialog title="Product Content" :visible.sync="fromVisible1" width="60%" :close-on-click-modal="false" destroy-on-close>
      <div class="w-e-text" v-html="content"></div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible1 = false">Close</el-button>
      </div>
    </el-dialog>


  </div>
</template>
<script>
export default {
  name: "FrontGoods",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      name: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
      },
      ids: [],
      content: '',
      fromVisible1: false
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    preview(content) {
      this.content = content
      this.fromVisible1 = true
    },
    changeStatus(row, status) {
      this.$confirm('Are you sure you want to ' + status.toLowerCase() + ' this item?', 'Confirm', {type: "warning"}).then(response => {
        this.form = JSON.parse(JSON.stringify(row))
        this.form.status = status
        this.$request.put('/goods/update', this.form).then(res => {
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
      this.$router.push('/front/addGoods?id=' + row.id)
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/goods/update' : '/goods/add',
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
        this.$request.delete('/goods/delete/' + id).then(res => {
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
        this.$request.delete('/goods/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/goods/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
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
      this.name = null
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
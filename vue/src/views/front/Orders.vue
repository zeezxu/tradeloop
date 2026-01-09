<template>
  <div style="width: 85%; margin: 10px auto" class="card">

    <div style="margin-bottom: 15px; display: flex">
      <div style="flex: 1">
        <el-radio-group v-model="url" @change="load(1)">
          <el-radio-button label="selectPage">My Order List</el-radio-button>
          <el-radio-button label="selectSalePage">My Selling List</el-radio-button>
        </el-radio-group>
      </div>
      <div>
        <el-input v-model="goodsName" placeholder="Product name" style="width: 200px; margin-right: 5px" clearable></el-input>
        <el-input v-model="orderNo" placeholder="Order number" style="width: 200px; margin-right: 5px" clearable></el-input>
        <el-select v-model="status" placeholder="Select status" style="width: 200px; margin-right: 5px" clearable>
          <el-option value="Cancelled" label="Cancelled"></el-option>
          <el-option value="Pending Payment" label="Pending Payment"></el-option>
          <el-option value="Pending Shipment" label="Pending Shipment"></el-option>
          <el-option value="Pending Receipt" label="Pending Receipt"></el-option>
          <el-option value="Completed" label="Completed"></el-option>
        </el-select>
        <el-button type="primary" @click="load(1)">Search</el-button>
        <el-button type="warning" @click="reset">Reset</el-button>
      </div>
    </div>

    <div style="margin: 10px 0">
      <el-table :data="tableData" stripe class="wrap-header-table">
        <el-table-column prop="id" label="ID" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="goodsName" label="Product Name" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="goodsImg" label="Product Image" width="100" align="center">
          <template v-slot="scope">
            <el-image v-if="scope.row.goodsImg" style="width: 50px" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="Order Number" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="total" label="Total Price" width="100">
          <template v-slot="scope">
            <span style="color: red">￥{{ scope.row.total }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="Order Time" width="110" show-overflow-tooltip></el-table-column>
        <el-table-column prop="payNo" label="Payment Number" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="payTime" label="Payment Time" width="110" show-overflow-tooltip></el-table-column>
        <el-table-column prop="user" label="Customer Name" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="address" label="Delivery Address" width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="phone" label="Contact Phone" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="Order Status" width="130">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.status === 'Cancelled'">Cancelled</el-tag>
            <el-tag type="danger" v-if="scope.row.status === 'Pending Payment'">Pending Payment</el-tag>
            <el-tag type="primary" v-if="scope.row.status === 'Pending Shipment'">Pending Shipment</el-tag>
            <el-tag type="info" v-if="scope.row.status === 'Pending Receipt'">Pending Receipt</el-tag>
            <el-tag type="success" v-if="scope.row.status === 'Completed'">Completed</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" align="center" width="200" fixed="right">
          <template v-slot="scope">
            <el-button v-if="scope.row.status === 'Pending Payment' && scope.row.userId === user.id" size="mini" type="primary" plain @click="pay(scope.row.orderNo)">Pay</el-button>
            <el-button v-if="scope.row.status === 'Pending Payment'" size="mini" type="danger" plain @click="changeStatus(scope.row, 'cancel')">Cancel</el-button>
            <el-button v-if="scope.row.status === 'Pending Shipment' && scope.row.saleId === user.id" size="mini" type="info" plain @click="changeStatus(scope.row, 'ship')">Ship</el-button>
            <el-button v-if="scope.row.status === 'Pending Receipt' && scope.row.userId === user.id" size="mini" type="primary" plain @click="changeStatus(scope.row, 'confirm')">Confirm Receipt</el-button>
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

    <el-dialog title="Order Information" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="130px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="Product Name" prop="goodsName">
          <el-input v-model="form.goodsName" placeholder="Product Name"></el-input>
        </el-form-item>
        <el-form-item label="Product Image" prop="goodsImg">
          <el-input v-model="form.goodsImg" placeholder="Product Image"></el-input>
        </el-form-item>
        <el-form-item label="Order Number" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="Order Number"></el-input>
        </el-form-item>
        <el-form-item label="Total Price" prop="total">
          <el-input v-model="form.total" placeholder="Total Price"></el-input>
        </el-form-item>
        <el-form-item label="Order Time" prop="time">
          <el-input v-model="form.time" placeholder="Order Time"></el-input>
        </el-form-item>
        <el-form-item label="Payment Number" prop="payNo">
          <el-input v-model="form.payNo" placeholder="Payment Number"></el-input>
        </el-form-item>
        <el-form-item label="Payment Time" prop="payTime">
          <el-input v-model="form.payTime" placeholder="Payment Time"></el-input>
        </el-form-item>
        <el-form-item label="User ID" prop="userId">
          <el-input v-model="form.userId" placeholder="User ID"></el-input>
        </el-form-item>
        <el-form-item label="Delivery Address" prop="address">
          <el-input v-model="form.address" placeholder="Delivery Address"></el-input>
        </el-form-item>
        <el-form-item label="Contact Phone" prop="phone">
          <el-input v-model="form.phone" placeholder="Contact Phone"></el-input>
        </el-form-item>
        <el-form-item label="User Name" prop="userName">
          <el-input v-model="form.userName" placeholder="User Name"></el-input>
        </el-form-item>
        <el-form-item label="Order Status" prop="status">
          <el-input v-model="form.status" placeholder="Order Status"></el-input>
        </el-form-item>
        <el-form-item label="Seller ID" prop="saleId">
          <el-input v-model="form.saleId" placeholder="Seller ID"></el-input>
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
  name: "Orders",
  data() {
    return {
      url: 'selectPage',
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
      },
      ids: [],
      goodsName: null,
      orderNo: null,
      status: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    pay(orderNo) {
      window.open('http://localhost:9090/alipay/pay?orderNo=' + orderNo)
    },
    changeStatus(row, status) {
      this.$confirm('Are you sure to '+status+'?', 'Confirm', {type: "warning"}).then(response => {
        this.form = JSON.parse(JSON.stringify(row))
        this.form.status = status
        this.$request.put('/orders/update', this.form).then(res => {
          if (res.code === '200') {
            this.$message.success('Operation successful')
            this.load(1)
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch(e => {})
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
            url: this.form.id ? '/orders/update' : '/orders/add',
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
        this.$request.delete('/orders/delete/' + id).then(res => {
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
      this.$confirm('Are you sure you want to delete the selected items?', 'Confirm', {type: "warning"}).then(response => {
        this.$request.delete('/orders/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/orders/' + this.url, {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          goodsName: this.goodsName,
          status: this.status,
          orderNo: this.orderNo,
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
      this.goodsName = null
      this.orderNo = null
      this.status = null
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
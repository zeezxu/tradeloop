<template>
  <div>
    <div class="search">
      <el-input v-model="orderNo" placeholder="Enter order number" style="width: 200px; margin-right: 5px" clearable></el-input>
      <el-select v-model="status" placeholder="Select status" style="width: 200px; margin-right: 5px" clearable>
        <el-option value="Cancelled" label="Cancelled"></el-option>
        <el-option value="Pending Payment" label="Pending Payment"></el-option>
        <el-option value="Pending Shipment" label="Pending Shipment"></el-option>
        <el-option value="Pending Receipt" label="Pending Receipt"></el-option>
        <el-option value="Completed" label="Completed"></el-option>
      </el-select>
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
        <el-table-column prop="goodsName" label="Product Name" width="150" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="goodsImg" label="Product Image" width="100" align="center">
          <template v-slot="scope">
            <el-image v-if="scope.row.goodsImg" style="width: 50px" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="Order Number" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="total" label="Total Price" width="100">
          <template v-slot="scope">
            <span style="color: red">￥{{ scope.row.total }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="Order Time" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="payNo" label="Payment Number" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="payTime" label="Payment Time" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="user" label="Customer" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="address" label="Delivery Address" width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="phone" label="Contact Phone" width="130" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userName" label="Recipient" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="Order Status" width="140">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.status === 'Cancelled'">Cancelled</el-tag>
            <el-tag type="danger" v-if="scope.row.status === 'Pending Payment'">Pending Payment</el-tag>
            <el-tag type="primary" v-if="scope.row.status === 'Pending Shipment'">Pending Shipment</el-tag>
            <el-tag type="info" v-if="scope.row.status === 'Pending Receipt'">Pending Receipt</el-tag>
            <el-tag type="success" v-if="scope.row.status === 'Completed'">Completed</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="saleName" label="Seller" width="120" show-overflow-tooltip></el-table-column>
        <el-table-column label="Actions" align="center" width="100" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" plain type="danger" @click="del(scope.row.id)">Delete</el-button>
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
          <el-input v-model="form.goodsName" placeholder="Product name"></el-input>
        </el-form-item>
        <el-form-item label="Product Image URL" prop="goodsImg">
          <el-input v-model="form.goodsImg" placeholder="Image URL"></el-input>
        </el-form-item>
        <el-form-item label="Order Number" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="Order number"></el-input>
        </el-form-item>
        <el-form-item label="Total Price" prop="total">
          <el-input v-model="form.total" placeholder="Total price"></el-input>
        </el-form-item>
        <el-form-item label="Order Time" prop="time">
          <el-input v-model="form.time" placeholder="Order time"></el-input>
        </el-form-item>
        <el-form-item label="Payment Number" prop="payNo">
          <el-input v-model="form.payNo" placeholder="Payment number"></el-input>
        </el-form-item>
        <el-form-item label="Payment Time" prop="payTime">
          <el-input v-model="form.payTime" placeholder="Payment time"></el-input>
        </el-form-item>
        <el-form-item label="Customer ID" prop="userId">
          <el-input v-model="form.userId" placeholder="Customer ID"></el-input>
        </el-form-item>
        <el-form-item label="Delivery Address" prop="address">
          <el-input v-model="form.address" placeholder="Delivery address"></el-input>
        </el-form-item>
        <el-form-item label="Contact Phone" prop="phone">
          <el-input v-model="form.phone" placeholder="Contact phone"></el-input>
        </el-form-item>
        <el-form-item label="Recipient Name" prop="userName">
          <el-input v-model="form.userName" placeholder="Recipient name"></el-input>
        </el-form-item>
        <el-form-item label="Order Status" prop="status">
          <el-select v-model="form.status" style="width: 100%" placeholder="Select status">
            <el-option value="Cancelled" label="Cancelled"></el-option>
            <el-option value="Pending Payment" label="Pending Payment"></el-option>
            <el-option value="Pending Shipment" label="Pending Shipment"></el-option>
            <el-option value="Pending Receipt" label="Pending Receipt"></el-option>
            <el-option value="Completed" label="Completed"></el-option>
          </el-select>
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
      orderNo: null,
      status: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
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
      this.$confirm('Are you sure you want to delete these items?', 'Confirm', {type: "warning"}).then(response => {
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
      this.$request.get('/orders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
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
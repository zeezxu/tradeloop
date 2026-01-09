<template>
  <div style="margin: 0 auto; padding: 10px 0; width: 50%">
    <div style="display: flex; grid-gap: 20px; margin-bottom: 40px">
      <img :src="goods.img" alt="" style="width: 50%; height: 400px; display: block">
      <div style="flex: 1; width: 0" >
        <el-tooltip :content="goods.name" placement="top-start">
          <div style="width: 100%; font-weight: bold; font-size: 26px; margin: 20px 0" class="line1">{{ goods.name }}</div>
        </el-tooltip>
        <div style="color: #666; font-size: 16px;">
          <span>Views {{ goods.readCount }}</span>
          <span style="margin-left: 20px">Likes {{ goods.likesCount }}</span>
          <span style="margin-left: 20px">Favorites {{ goods.collectCount }}</span>
        </div>
        <div style="color: red; font-size: 24px; margin: 40px 0">￥{{ goods.price }}</div>
        <div style="margin-bottom: 20px"><span style="color: #666">Address: </span> {{ goods.address }}</div>
        <div style="margin-bottom: 20px">
          <span style="color: #666">Seller: </span> {{ goods.userName }}
          <el-tooltip effect="light" content="Chat with seller" placement="right" :hide-after="2000">
            <i @click="chat(goods.userId)" class="el-icon-chat-dot-round" style="font-size: 18px; margin-left: 3px; cursor: pointer; color: #409EFF"></i>
          </el-tooltip>
        </div>
        <div style="margin-bottom: 40px"><span style="color: #666">Publish date: </span> {{ goods.date }}</div>
        <div>
          <div style="display: flex; gap: 10px; margin-bottom: 10px;">
            <el-button v-if="!goods.userLikes" @click="addLikes" size="medium"
                       style="background-color: orangered; color: #eee; border-color: orangered;">
              <i class="el-icon-star-off"></i> Like
            </el-button>
            <el-button v-if="goods.userLikes" @click="addLikes" size="medium"
                       style="background-color: orangered; color: #eee; border-color: orangered;">
              <i class="el-icon-star-on"></i> Liked
            </el-button>
            <el-button v-if="!goods.userCollect" @click="addCollect" size="medium" type="warning">
              <i class="el-icon-collection"></i> Add to Favorites
            </el-button>
            <el-button v-if="goods.userCollect" @click="addCollect" size="medium" type="warning">
              <i class="el-icon-collection-tag"></i> Favorited
            </el-button>
          </div>

          <div>
            <el-button size="medium" type="danger" @click="handleBuy">
              <i class="el-icon-shopping-cart-2"></i> Buy Now
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <div>
      <div style="display: flex; border-bottom: 1px solid orangered; margin-bottom: 10px">
        <div style="padding: 10px 20px; cursor: pointer" :class="{ 'active' : current === 'product details' }" @click="changeItem('product details')">Product Details</div>
        <div style="padding: 10px 20px; cursor: pointer" :class="{ 'active' : current === 'product reviews' }" @click="changeItem('product reviews')">Product Reviews</div>
      </div>

      <div v-if="current === 'product details'">
        <div v-html="goods.content"></div>
      </div>

      <div v-if="current === 'product reviews'" class="card">
        <Comment :fid="id" module="goods" />
      </div>

      <el-dialog title="Choose shipping address" :visible.sync="fromVisible" width="30%" :close-on-click-modal="false" destroy-on-close>
        <div style="padding: 0 20px">
          <el-radio-group v-model="form.addressId">
            <el-radio v-for="item in addressList" :key="item.id" :label="item.id" style="margin-bottom: 10px">
              {{ item.name + ' ' + item.address + ' ' + item.phone}}
            </el-radio>
          </el-radio-group>
          <a v-if="addressList.length === 0" href="/front/address" target="_blank">No shipping address yet? Create one</a>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="fromVisible = false">Cancel</el-button>
          <el-button type="primary" @click="addOrder">Confirm</el-button>
        </div>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import Comment from "@/components/Comment";
export default {
  name: "GoodsDetail",
  components: {Comment},
  data() {
    return {
      id: this.$route.query.id,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      goods: {},
      current: 'product details',
      form: {},
      fromVisible: false,
      addressList: []
    }
  },
  created() {
    this.$request.put('/goods/updateReadCount/' + this.id).then(res => {
      this.load()
    })

    this.loadAddress()
  },
  methods: {
    chat(userId) {
      this.$request.post('/chatGroup/add', { chatUserId: userId, userId: this.user.id }).then(res => {
        if (res.code === '200') {
          this.$router.push('/front/chat')
        } else {
          this.$message.error(res.msg || 'Failed to start chat')
        }
      }).catch(err => {
        console.error('Chat error:', err)
        this.$message.error('Failed to start chat')
      })
    },
    addOrder() {
      if (!this.form.addressId) {
        this.$message.warning('Please choose a shipping address.')
        return
      }
      this.form.goodsId = this.id
      this.$request.post('/orders/add', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('Order placed successfully')
          this.$router.push('/front/orders')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadAddress() {
      this.$request.get('/address/selectAll').then(res => {
        this.addressList = res.data || []
      })
    },
    handleBuy() {
      this.form = {}
      this.fromVisible = true
    },
    addCollect() {
      this.$request.post('/collect/add', { fid: this.goods.id, module: 'goods' }).then(res => {
        if (res.code === '200') {
          const message = this.goods.userCollect ? 'Removed from favorites' : 'Added to favorites'
          this.$message.success(message)
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    addLikes() {
      this.$request.post('/likes/add', { fid: this.goods.id, module: 'goods' }).then(res => {
        if (res.code === '200') {
          const message = this.goods.userLikes ? 'Unliked' : 'Liked'
          this.$message.success(message)
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    changeItem(current) {
      this.current = current
    },
    load() {
      this.$request.get('/goods/selectById/' + this.id).then(res => {
        this.goods = res.data || {}
      })
    }
  }
}
</script>

<style scoped>
.active {
  background-color: orangered;
  color: #eee;
}
</style>
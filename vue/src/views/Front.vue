<template>
  <div style="background-color: #f8f8f8; min-height: 100vh">
    <div class="front-header">
      <a href="/front/home">
        <div class="front-header-left">
          <img src="@/assets/imgs/logo.png" alt="">
          <div class="title">TradeLoop</div>
        </div>
      </a>
      <div class="front-header-center">
        <div @click="$router.push(item.path)" class="menu" v-for="item in menus" :key="item.path"
             :class="{'menu-active' : item.path === $route.path }">{{ item.text }}</div>
      </div>
      <div>
        <span @click="$router.push('/front/chat')" style="font-size: 16px; color: white; cursor: pointer"><i class="el-icon-chat-dot-round"></i> Chat</span>
      </div>
      <div class="front-header-right">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">Login</el-button>
          <el-button @click="$router.push('/register')">Register</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="front-header-dropdown">
              <img :src="user.avatar" alt="" style="border-radius: 50%">
              <div style="margin-left: 10px; color: #eee; cursor: pointer">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div @click="$router.push('/front/orders')">My Order</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/goods')">My Products</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/userPosts')">My Posts</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/userHelp')">My Requests</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/userFeedback')">My Feedback</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/address')">My Address</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/person')">Personal Info</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div @click="$router.push('/front/collect')">My Favorites</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="logout">Log Out</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div class="main-body">
      <router-view ref="child" @update:user="updateUser" />
    </div>

    <Footer />

  </div>
</template>

<script>
import Footer from "@/components/Footer";
export default {
  name: "FrontLayout",
  components: {
    Footer
  },
  data () {
    return {
      notice: [],
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
      menus: [
        { text: 'Hot Deals', path: '/front/home' },
        { text: 'Community', path: '/front/posts' },
        { text: 'Requests', path: '/front/help' },
        { text: 'Announcements', path: '/front/notice' },
        { text: 'Feedback', path: '/front/feedback' },
      ]
    }
  },

  mounted() {

  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
    },
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
  }

}
</script>

<style scoped>
  @import "@/assets/css/front.css";

  .menu {
    color: #eee;
    font-size: 16px;
    padding: 0 20px;
    cursor: pointer;
    white-space: nowrap;
    flex-shrink: 0;
  }
  .menu:hover {
    color: orange;
  }
  .menu-active {
    color: orange;
  }
</style>
<style scoped>
  @import "@/assets/css/front.css";

  .menu {
    color: #eee;
    font-size: 16px;
    padding: 0 20px;
    cursor: pointer;
    white-space: nowrap;
    flex-shrink: 0;
  }
  
  .menu:hover {
    color: orange;
  }
  
  .menu-active {
    color: orange;
  }

  /* Ensure header doesn't break on smaller screens */
  .front-header-center {
    display: flex;
    gap: 5px;
    flex-wrap: nowrap;
  }

  /* Make menu items slightly smaller on medium screens */
  @media (max-width: 1200px) {
    .menu {
      padding: 0 15px;
      font-size: 15px;
    }
  }

  /* Even smaller on tablet */
  @media (max-width: 992px) {
    .menu {
      padding: 0 12px;
      font-size: 14px;
    }
  }
</style>

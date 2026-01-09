import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// Solve navigation bar and tabBar bug in vue-router 3.0
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/login',
    children: [
      { path: '403', name: 'NoAuth', meta: { name: 'No Permission' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: 'Dashboard' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: 'Administrators' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: 'Personal Info' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: 'Change Password' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: 'Announcements' }, component: () => import('../views/manager/Notice') },
      { path: 'user', name: 'User', meta: { name: 'Users' }, component: () => import('../views/manager/User') },
      { path: 'category', name: 'Category', meta: { name: 'Categories' }, component: () => import('../views/manager/Category') },
      { path: 'goods', name: 'Goods', meta: { name: 'Products' }, component: () => import('../views/manager/Goods') },
      { path: 'circles', name: 'Circles', meta: { name: 'Circles' }, component: () => import('../views/manager/Circles') },
      { path: 'posts', name: 'Posts', meta: { name: 'Community Posts' }, component: () => import('../views/manager/Posts') },
      { path: 'help', name: 'Help', meta: { name: 'Purchase Requests' }, component: () => import('../views/manager/Help') },
      { path: 'address', name: 'Address', meta: { name: 'Delivery Addresses' }, component: () => import('../views/manager/Address') },
      { path: 'feedback', name: 'Feedback', meta: { name: 'User Feedback' }, component: () => import('../views/manager/Feedback') },
      { path: 'comment', name: 'Comment', meta: { name: 'Comments' }, component: () => import('../views/manager/Comment') },
      { path: 'orders', name: 'Orders', meta: { name: 'Orders' }, component: () => import('../views/manager/Orders') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', component: () => import('../views/front/Home') },
      { path: 'person', component: () => import('../views/front/Person') },
      { path: 'goodsDetail', component: () => import('../views/front/GoodsDetail') },
      { path: 'collect', component: () => import('../views/front/Collect') },
      { path: 'notice', component: () => import('../views/front/Notice') },
      { path: 'addGoods', component: () => import('../views/front/AddGoods') },
      { path: 'goods', component: () => import('../views/front/Goods') },
      { path: 'address', component: () => import('../views/front/Address') },
      { path: 'orders', component: () => import('../views/front/Orders') },
      { path: 'posts', component: () => import('../views/front/Posts') },
      { path: 'userPosts', component: () => import('../views/front/UserPosts') },
      { path: 'postsDetail', component: () => import('../views/front/PostsDetail') },
      { path: 'help', component: () => import('../views/front/Help') },
      { path: 'userHelp', component: () => import('../views/front/UserHelp') },
      { path: 'feedback', component: () => import('../views/front/Feedback') },
      { path: 'userFeedback', component: () => import('../views/front/UserFeedback') },
      { path: 'chat', component: () => import('../views/front/Chat') },
      { path: 'search', component: () => import('../views/front/Search') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: 'Login' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: 'Register' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: 'Page Not Found' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router

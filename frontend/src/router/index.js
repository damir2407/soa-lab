import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'MainView',
        component: () => import('../views/./MainView')
    },
]

const router = new VueRouter({
    mode: 'history',
    routes
})

export default router

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/store'
import vuetify from './plugins/vuetify'
import vueSimpleAlert from './plugins/simplealert'

//import axios from "axios";

Vue.mixin({
  methods: {
    /*
    doRefresh(status) {
        let data = {
            refreshToken: localStorage.refreshToken
        }
        if (status === 403) {
            let flag = false
            axios.create({
                baseURL: this.hostname,
                headers: {
                    Authorization: localStorage.token
                }
            }).post("/api/refresh/token", data)
                .then(async resp => {
                    localStorage.token = resp.data.token
                    localStorage.refreshToken = resp.data.refreshToken
                    await new Promise(resolve => setTimeout(resolve, this.awaitTimer))

                    flag = true
                })
            return flag
        }
    },*/
    getHeader() {
      return {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Headers': '*',
        'Access-Control-Allow-Methods': 'true'

      }

    }
  },
  data: function () {
    return {
      get firstHostname() {
        return "https://localhost:7008";
      },
      get secondHostname() {
        return "http://localhost:7010";
      },
      get awaitTimer() {
        return 2000;
      },
      get errorTimer() {
        return 2000;
      }
    }
  }
})

new Vue({
  store,
  router,
  vuetify,
  vueSimpleAlert,
  render: h => h(App),
}).$mount('#app')

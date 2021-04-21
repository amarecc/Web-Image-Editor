import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import Toast from "vue-toastification";
import VueFinalModal from 'vue-final-modal'

// Import the CSS or use your own!
import "vue-toastification/dist/index.css";
import 'vueperslides/dist/vueperslides.css'

const app = createApp(App)

app.use(router).use(store).mount('#app')

const options = {
    // You can set your default options here
};
app.use(Toast, options)

app.use(VueFinalModal());
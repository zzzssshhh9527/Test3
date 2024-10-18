import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import axios from 'axios';

const app = createApp(App)

axios.defaults.baseURL = "http://localhost:8088"
app.use(ElementPlus)
app.mount('#app')

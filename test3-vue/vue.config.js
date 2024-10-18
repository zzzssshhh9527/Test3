const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        proxy: {
            '/api': {
                target: 'http://localhost:8088', // 后端API服务器地址
                changeOrigin: true, // 允许跨域
                pathRewrite: {
                    '^/api': '' // 重写路径：移除路径中的`/api`
                }
            }
        }
    }

})

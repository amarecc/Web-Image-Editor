module.exports = {
  outputDir: 'target/dist',
  assetsDir: 'static',
  devServer: {
    port: 8089,
    proxy: {
      '^/images': {
        target: 'http://localhost:8090', // Spring boot backend address
        ws: true,
        changeOrigin: true
      }
    }
  }
}
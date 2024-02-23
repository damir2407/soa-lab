module.exports = {
  publicPath: process.env.NODE_ENV === 'production'
      ? '/my-project/'
      : '/my-project-dev',
  transpileDependencies: [
    'vuetify'
  ]
}

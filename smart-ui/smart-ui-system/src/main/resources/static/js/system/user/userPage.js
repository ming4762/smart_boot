ready(function () {
  require(['system/user/User'], function ({User}) {
    var user = new User()
    user.init()
  })
})
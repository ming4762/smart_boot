ready(function () {
  require(['system/onlineUser/OnlineUser'], function ({OnlineUser}) {
    var onlineUser = new OnlineUser()
    onlineUser.init()
  })
})
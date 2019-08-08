ready(function () {
  require(['system/auth/AccountMessage'], function ({AccountMessage}) {
    var accountMessage = new AccountMessage()
    accountMessage.init()
  })
})
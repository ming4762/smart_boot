ready(function () {
  require(['system/error/Error403'], function ({Error403}) {
    var error403 = new Error403()
    error403.init()
  })
})
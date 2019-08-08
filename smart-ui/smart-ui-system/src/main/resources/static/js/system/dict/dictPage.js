ready(function () {
  requirejs(['system/dict/Dict'], function ({Dict}) {
    var dict = new Dict()
    dict.init()
  })
})
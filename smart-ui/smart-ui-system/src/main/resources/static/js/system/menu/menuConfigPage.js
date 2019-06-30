ready(function () {
  require(['system/menu/MenuConfig'], function ({MenuConfig}) {
    var menu = new MenuConfig()
    menu.init()
  })
})
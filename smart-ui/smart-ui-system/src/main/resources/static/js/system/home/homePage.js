var busVue
ready(function () {
  require(['system/home/Home'], function ({Home}) {
    var home = new Home()
    busVue = home.initBus()
    home.init()
  })
})

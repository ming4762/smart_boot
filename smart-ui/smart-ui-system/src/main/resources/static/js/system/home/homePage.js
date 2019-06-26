var busVue
ready(function () {
  require(['home/Home'], function ({Home}) {
    var home = new Home()
    busVue = home.initBus()
    home.init()
  })
})

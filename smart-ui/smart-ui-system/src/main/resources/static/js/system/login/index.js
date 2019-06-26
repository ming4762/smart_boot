ready(function () {
    require(['system/login/login'], function ({Login}) {
        var login = new Login()
        login.init()
    })
})

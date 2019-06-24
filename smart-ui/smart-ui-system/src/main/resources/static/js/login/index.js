ready(function () {
    require(['login/login'], function ({Login}) {
        var login = new Login();
        login.init();
    });
});

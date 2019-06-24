requirejs.config({
  baseUrl: '../../js/'
})
function ready(fn){
  if(document.addEventListener){		//标准浏览器
    document.addEventListener('DOMContentLoaded',function(){
      //注销时间，避免重复触发
      document.removeEventListener('DOMContentLoaded',arguments.callee,false);
      fn();		//运行函数
    },false);
  }else if(document.attachEvent){		//IE浏览器
    document.attachEvent('onreadystatechange',function(){
      if(document.readyState === 'complete'){
        document.detachEvent('onreadystatechange',arguments.callee);
        fn();		//函数运行
      }
    });
  }
}
/**
 * Created by bdqn on 2016/5/3.
 */
//登录的方法
function login(){
    var loginName=$("#loginName").val();
    var password=$("#password").val();
    $.ajax({
        url:contextPath+"/Login/login",
        method:"post",
        data:{loginName:loginName,password:password},
        success:function(result){
            //var result=eval("("+jsonStr+")");
            if(result.status==1){
                window.location.href=contextPath+"/Home/index";
            }else{
                showMessage(result.message)
            }
        },
        error:function (XMLHttpRequest,textStatus,errorThrown) {
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus);
        }
    })
}
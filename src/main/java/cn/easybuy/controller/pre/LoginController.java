package cn.easybuy.controller.pre;

import cn.easybuy.entity.User;
import cn.easybuy.service.user.UserService;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.ReturnResult;
import cn.easybuy.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/Login")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到登陆界面
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "pre/login";
    }
    /**
     * 登陆的方法
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login",method= RequestMethod.POST)
    public String login(HttpSession session, String loginName, String password){
        ReturnResult result=new ReturnResult();
        User user=userService.getUser(null, loginName);
        if(EmptyUtils.isEmpty(user)){
            result.returnFail("用户不存在");
            System.out.println("用户不存在");
        }else{
            if(user.getPassword().equals(SecurityUtils.md5Hex(password))){
                //登陆成功
                session.setAttribute("loginUser", user);
                result.returnSuccess("登陆成功");
                System.out.println("登陆成功");
            }else{
                result.returnFail("密码错误");
                System.out.println("密码错误");
            }
        }

        return JSON.toJSONString(result);
    }
    /**
     * 登陆的方法
     * @return
     */
    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session){
        ReturnResult result=new ReturnResult();
        try {
            User user=(User)session.getAttribute("loginUser");
            session.removeAttribute("loginUser");
            // 清除购物车
            session.removeAttribute("cart");
            session.removeAttribute("cart2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.returnSuccess("注销成功");
        return "pre/login";
    }
}

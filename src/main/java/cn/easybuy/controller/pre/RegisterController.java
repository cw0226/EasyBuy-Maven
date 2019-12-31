package cn.easybuy.controller.pre;

import cn.easybuy.entity.User;
import cn.easybuy.service.user.UserService;
import cn.easybuy.utils.*;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/Register")
public class RegisterController {
    @Autowired
    private UserService userService;

    /**
     * 跳到注册页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/toRegister")
    public String toRegister(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "pre/register";
    }

    /**
     * 保存用户信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveUserToDatabase",method = RequestMethod.POST)
    public String saveUserToDatabase(String loginName,String sex,String userName,String password,String identityCode,String email,String mobile) throws Exception {
        ReturnResult result = new ReturnResult();
        try {
            User user = new User();
            User oldUser = userService.getUser(null, loginName);
            //判断用户
            if (EmptyUtils.isNotEmpty(oldUser)) {
                result.returnFail("用户已经存在");
                return JSON.toJSONString(result);
            }
            user.setLoginName(loginName);
            user.setUserName(userName);
            user.setSex(EmptyUtils.isEmpty(sex) ? 1 : 0);
            user.setPassword(SecurityUtils.md5Hex(password));
            user.setIdentityCode(identityCode);
            user.setEmail(email);
            user.setMobile(mobile);
            user.setType(Constants.UserType.PRE);
            result=checkUser(user);
            //是否验证通过
            if(result.getStatus()==Constants.ReturnResult.SUCCESS){
                if(!userService.add(user)){
                    return JSON.toJSONString(result.returnFail("注册失败！"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.returnSuccess("注册成功");
        return JSON.toJSONString(result);
    }

    private ReturnResult checkUser(User user){
        ReturnResult result = new ReturnResult();
        boolean flag=true;
        if(EmptyUtils.isNotEmpty(user.getMobile())){
            if(!RegUtils.checkMobile(user.getMobile())){
                return result.returnFail("手机格式不正确");
            }
        }

        if(EmptyUtils.isNotEmpty(user.getIdentityCode())){
            if(!RegUtils.checkIdentityCodeReg(user.getIdentityCode())){
                return result.returnFail("身份证号码不正确");
            }
        }

        if(EmptyUtils.isNotEmpty(user.getEmail())){
            if(!RegUtils.checkEmail(user.getEmail())){
                return result.returnFail("邮箱格式不正确");
            }
        }
        return result.returnSuccess();
    }
}

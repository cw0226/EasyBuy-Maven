package cn.easybuy.controller.backend;

import cn.easybuy.entity.User;
import cn.easybuy.service.user.UserService;
import cn.easybuy.utils.*;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private UserService userService;

    /**
     * 跳到用户主页
     *
     * @return
     */
    @RequestMapping("/index")
    public String index(HttpSession session, Model model) throws Exception {
        //获取登陆用户
        User user = (User)session.getAttribute("loginUser");
        model.addAttribute("user", user);
        model.addAttribute("menu", 2);
        return "backend/user/userInfo";
    }

    /**
     * 订单的主页面
     *
     * @return
     */
    @RequestMapping("/queryUserList")
    public String queryUserList(Model model,String currentPage,String pageSize) throws Exception {
        int rowPerPage = EmptyUtils.isEmpty(pageSize) ? 10 : Integer.parseInt(pageSize);
        int currentPageStr = EmptyUtils.isEmpty(currentPage) ? 1 : Integer.parseInt(currentPage);
        int total = userService.count();
        Pager pager = new Pager(total, rowPerPage, currentPageStr);
        List<User> userList = userService.getUserList((currentPageStr-1)*rowPerPage, rowPerPage);
        pager.setUrl("/admin/user/queryUserList");
        model.addAttribute("userList", userList);
        model.addAttribute("pager", pager);
        model.addAttribute("menu", 8);
        return "backend/user/userList";
    }

    /**
     * 修改用户信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/toUpdateUser/{id}")
    public String toUpdateUser(Model model,@PathVariable String id) throws Exception {
        User user = userService.getUser(Integer.parseInt(id), null);
        model.addAttribute("user", user);
        return "backend/user/toUpdateUser";
    }

    /**
     * 去添加用户
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/toAddUser")
    public String toAddUser(Model model) throws Exception {
        User user = new User();
        model.addAttribute("user", user);
        return "backend/user/toUpdateUser";
    }
    /**
     * 更新用户
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public String updateUser(String id,String loginName,String sex,String userName,String password,String identityCode,String email,String mobile,String type) throws Exception {
        ReturnResult result = new ReturnResult();
        User user = new User();
        User oldUser = userService.getUser(null, loginName);

        //判断用户
        if (EmptyUtils.isNotEmpty(oldUser) && (EmptyUtils.isEmpty(id) || oldUser.getId() != Integer.parseInt(id))) {
            result.returnFail("用户已经存在");
            return JSON.toJSONString(result);
        }
        user.setLoginName(loginName);
        user.setUserName(userName);
        user.setSex(EmptyUtils.isEmpty(sex) ? 1 : 0);
        if (EmptyUtils.isEmpty(id) || id.equals("0")) {
            user.setPassword(SecurityUtils.md5Hex(password));
        }
        user.setIdentityCode(identityCode);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setType(Integer.parseInt(type));

        result=checkUser(user);
        //是否验证通过
        if(result.getStatus()== Constants.ReturnResult.SUCCESS){
            if (EmptyUtils.isEmpty(id) || id.equals("0")) {
                if(!userService.add(user)){
                    return JSON.toJSONString(result.returnFail("增加失败！"));
                }
            } else {
                user.setId(Integer.parseInt(id));
                if(!userService.update(user)){
                    return JSON.toJSONString(result.returnFail("修改失败！"));
                }
            }
        }
        result.returnSuccess();
        return JSON.toJSONString(result);
    }
    @ResponseBody
    @RequestMapping(value = "/deleteUserById",method = RequestMethod.POST)
    public String deleteUserById(String id) throws Exception {
        System.out.println("deleteUserById");
        ReturnResult result = new ReturnResult();
        userService.deleteUserById(Integer.parseInt(id));
        result.returnSuccess();
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

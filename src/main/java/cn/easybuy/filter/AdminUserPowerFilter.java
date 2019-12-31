package cn.easybuy.filter;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.easybuy.entity.User;
import cn.easybuy.utils.EmptyUtils;

@WebFilter(urlPatterns = {"/admin/productCategory","/admin/product","/admin/user","/admin/order"})
public class AdminUserPowerFilter implements Filter {
	
	String extUrl="queryUserList";
	
	public void destroy() {		
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)request;
		HttpServletResponse resp=(HttpServletResponse)response;
		HttpSession session = req.getSession();
		User user=(User) session.getAttribute("loginUser");
		if(null==user){
			resp.sendRedirect(req.getContextPath()+"/Login?action=toLogin");
			return;			
		}
		String action=req.getParameter("action");
		if(EmptyUtils.isEmpty(action)){
			resp.sendRedirect(req.getContextPath()+"/Login?action=toLogin");
			return;		
		}else{
			if(extUrl.contains(action) && user.getType()!=1){
				resp.sendRedirect(req.getContextPath()+"/Login?action=toLogin");
				return;		
			}
		}
		chain.doFilter(request, response);
	}
	public void init(FilterConfig filterConfig) throws ServletException {		
	}	
}

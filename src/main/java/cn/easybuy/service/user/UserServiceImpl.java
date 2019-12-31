package cn.easybuy.service.user;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.easybuy.dao.user.UserDao;
import cn.easybuy.dao.user.UserDaoImpl;
import cn.easybuy.utils.DataSourceUtil;
import org.apache.log4j.Logger;
import cn.easybuy.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userDao;
	private Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Override
	public boolean add(User user){
		Integer count=0;
		try {
			count=userDao.add(user);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			return  count>0;
		}
	}

	@Override
	public boolean update(User user) {
		Integer count=0;
		try {
			count=userDao.update(user);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			return  count>0;
		}
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		Integer count=0;
		try {
			count=userDao.deleteUserById(userId+"");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			return  count>0;
		}
	}

	@Override
	public User getUser(Integer userId, String loginName) {
		User user=null;
		try {
			user=userDao.getUser(userId,loginName);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			return user;
		}
	}

	@Override
	public List<User> getUserList(Integer currentPageNo, Integer pageSize) {
		List<User> userList=null;
		try {
			userList=userDao.getUserList(currentPageNo,pageSize);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			return userList;
		}
	}

	@Override
	public int count() {
		Integer count=null;
		try {
			count=userDao.count();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			return count;
		}
	}
}

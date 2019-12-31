package cn.easybuy.service.user;
import cn.easybuy.entity.User;
import java.util.List;

public interface UserService {

	public boolean add(User user);
	
	public boolean update(User user);
	
	public boolean deleteUserById(Integer userId);
	
	public User getUser(Integer userId, String loginName);
	
	public List<User> getUserList(Integer currentPageNo, Integer pageSize);
	
	public int count();
}

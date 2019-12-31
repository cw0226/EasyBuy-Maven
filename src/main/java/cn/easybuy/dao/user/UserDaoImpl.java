package cn.easybuy.dao.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.easybuy.dao.BaseDaoImpl;
import cn.easybuy.entity.Product;
import cn.easybuy.entity.User;
import cn.easybuy.param.UserParam;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.MapperUtils;
import cn.easybuy.utils.Pager;
import cn.easybuy.utils.Params;
import org.apache.ibatis.session.SqlSession;

/**
 * 用户dao
 */
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

    public UserDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public User tableToClass(ResultSet rs) throws Exception {
        User user = new User();
        user.setLoginName(rs.getString("loginName"));
        user.setUserName(rs.getString("userName"));
        user.setPassword(rs.getString("password"));
        user.setSex(rs.getInt("sex"));
        user.setIdentityCode(rs.getString("identityCode"));
        user.setEmail(rs.getString("email"));
        user.setMobile(rs.getString("mobile"));
        user.setType(rs.getInt("type"));
        user.setId(rs.getInt("id"));
        return user;
    }
    /**
     * 保存用户
     *
     * @param user
     * @throws SQLException
     */
    public int add(User user){//新增用户信息
    	//获取SqlSession对象
		SqlSession sqlSession = MapperUtils.getSqlSession().sqlSessionFactory.openSession();
		Integer result = 0;
		try {
			result = sqlSession.getMapper(UserDao.class).add(user);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		}finally {
			sqlSession.close();
		}
		return result;
    }

    //更新用户信息
    public int update(User user) {
		Integer result = 0;
		//获取SqlSession对象
		SqlSession sqlSession = MapperUtils.getSqlSession().sqlSessionFactory.openSession();
		try {
			result=sqlSession.getMapper(UserDao.class).update(user);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		}finally {
			sqlSession.close();
		}
		return result;
    }

	@Override
	public int deleteUserById(String id) {
    	Integer result = 0;
		//获取SqlSession对象
		SqlSession sqlSession = MapperUtils.getSqlSession().sqlSessionFactory.openSession();
		try {
			result = sqlSession.getMapper(UserDao.class).deleteUserById(id);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			e.printStackTrace();
		}finally {
			sqlSession.close();
		}
		return result;
	}

	@Override
	public List<User> getUserList(Integer currentPageNo,Integer pageSize) throws Exception {
		List<User> userList=new ArrayList<User>();
		try {
			int total = count();
			Pager pager = new Pager(total, pageSize, currentPageNo);
			//通过工具类创建SqlSession
			SqlSession sqlSession = MapperUtils.getSqlSession().sqlSessionFactory.openSession();
			userList = sqlSession.getMapper(UserDao.class).getUserList(((pager.getCurrentPage() - 1) * pager.getRowPerPage()), pager.getRowPerPage());
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public Integer count() throws Exception {
		//通过工具类创建SqlSession
		SqlSession sqlSession = MapperUtils.getSqlSession().sqlSessionFactory.openSession();
		Integer count = sqlSession.getMapper(UserDao.class).count();
		return count;
	}

	@Override
	public User getUser(Integer id,String loginName) throws Exception {
		//通过工具类创建SqlSession
		SqlSession sqlSession = MapperUtils.getSqlSession().sqlSessionFactory.openSession();
		User user = sqlSession.getMapper(UserDao.class).getUser(id,loginName);
		return user;
	}

}

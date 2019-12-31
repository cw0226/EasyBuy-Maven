package cn.easybuy.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.easybuy.entity.ProductCategory;
import cn.easybuy.utils.EmptyUtils;
import cn.easybuy.utils.Params;

/**
 * 基础dao的实现类，实现最基本的增删查改的方法
 */
public abstract  class BaseDaoImpl implements IBaseDao {

	protected Connection connection;

	protected PreparedStatement pstm;
	
	static Logger logger=Logger.getLogger(BaseDaoImpl.class);
			
	public BaseDaoImpl(Connection connection) {
		this.connection = connection;
	}

	public ResultSet executeQuery(String sql,Object[] params){
		ResultSet rs=null;
		try {
			pstm = connection.prepareStatement(sql);
			for(int i = 0; i < params.length; i++){
				pstm.setObject(i+1, params[i]);
			}
			rs = pstm.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
	}
	
	//增删改操作 delete from news_detail where id=? and title=?
	public int executeUpdate(String sql,Object[] params){
		int updateRows = 0;
		try {
			pstm = connection.prepareStatement(sql);
			for(int i = 0; i < params.length; i++){
				pstm.setObject(i+1, params[i]);
			}
			updateRows = pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			updateRows = -1;
		}
		
		return updateRows;
	}
	
	public int executeInsert(String sql,Object[] params){
		Long id = 0L;
		try {
			pstm = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			for(int i = 0; i < params.length; i++){
				pstm.setObject(i+1, params[i]);
			}
			pstm.executeUpdate();
			ResultSet rs = pstm.getGeneratedKeys(); 
			if (rs.next()) { 
				id = rs.getLong(1);
				System.out.println("数据主键：" + id); 
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			id =null;
		}
		
		return id.intValue();
	}
	
	
	//释放资源
	public boolean closeResource(){
		if(pstm != null){
			try {
				pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public boolean closeResource(ResultSet reSet){
		if(reSet != null){
			try {
				reSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	/**
	 * 需要重写的方法
	 *
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public abstract Object tableToClass(ResultSet rs) throws Exception;
	
}
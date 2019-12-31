package cn.easybuy.dao.news;

import java.util.List;

import cn.easybuy.dao.IBaseDao;
import cn.easybuy.entity.News;
import cn.easybuy.param.NewsParams;

/**
 * 新闻的dao
 */
public interface NewsDao extends IBaseDao {
	/**
	 * 添加新闻
	 * @param news
	 * @throws Exception
	 */
	public void add(News news) throws Exception;
	/**
	 * 修改新闻
	 * @param news
	 * @throws Exception
	 */
	public void update(News news) throws Exception;
	/**
	 * 根据id删除新闻
	 * @param id
	 * @throws Exception
	 */
	public void deleteById(Integer id) throws Exception;
	/**
	 * 根据id查询新闻
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public News getNewsById(Integer id)throws Exception; 
	/**
	 * 查询新闻列表
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<News> queryNewsList(NewsParams params)throws Exception; 
	/**
	 * 查询新闻的数目
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Integer queryNewsCount(NewsParams params)throws Exception;
}

package cn.easybuy.service.news;

import java.util.List;

import cn.easybuy.entity.News;
import cn.easybuy.param.NewsParams;
import cn.easybuy.utils.Pager;

public interface NewsService{
	/**
	 * 保存新闻
	 * @param news
	 */
	void addNews(News news);//保存新闻
	/**
	 * 根据id查询新闻
	 * @param parameter
	 * @return
	 */
	News findNewsById(String parameter);
	/***
	 * 删除新闻
	 * @param parameter
	 */
	void deleteNews(String parameter);
	/***
	 * 查询新闻列表
	 * @param param
	 * @return
	 */
	List<News> queryNewsList(NewsParams param);
	/***
	 * 查询数目
	 * @param param
	 * @return
	 */
	Integer queryNewsCount(NewsParams param);

}

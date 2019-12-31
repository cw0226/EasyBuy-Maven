package cn.easybuy.service.news;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.easybuy.dao.news.NewsDao;
import cn.easybuy.entity.News;
import cn.easybuy.param.NewsParams;
import cn.easybuy.utils.DataSourceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class NewsServiceImpl implements NewsService {
	@Resource
	private NewsDao newsDao;

	public void deleteNews(String id) {// 删除新闻
		try {
			newsDao.deleteById(Integer.parseInt(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public News findNewsById(String id) {// 根据ID获取新闻
		News news = null;
		try {
			news = newsDao.getNewsById(Integer.parseInt(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return news;
	}

	public void addNews(News news) {// 保存新闻
		try {
			newsDao.add(news);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateNews(News news) {// 更新留言
		try {
			newsDao.update(news);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public List<News> queryNewsPageList(NewsParams param) throws SQLException {
		List<News> newsList=new ArrayList<News>();
		try {
			newsList=newsDao.queryNewsList(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsList;
	}
	
	@Override
	public List<News> queryNewsList(NewsParams param) {
		List<News> newsList=new ArrayList<News>();
		try {
			newsList=newsDao.queryNewsList(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsList;
	}

	@Override
	public Integer queryNewsCount(NewsParams param) {
		Integer count=0;
		try {
			count=newsDao.queryNewsCount(param);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return count;
		}
	}

}

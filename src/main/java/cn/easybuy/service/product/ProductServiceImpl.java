package cn.easybuy.service.product;
import java.sql.Connection;
import java.util.List;
import cn.easybuy.dao.product.ProductDao;
import cn.easybuy.utils.DataSourceUtil;
import org.apache.log4j.Logger;
import cn.easybuy.entity.Product;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 商品的业务类
 */
@Service
public class ProductServiceImpl implements ProductService {
	@Resource
	private ProductDao productDao;
	
	private Logger logger = Logger.getLogger(ProductServiceImpl.class);
	
	@Override
	public boolean add(Product product) {
		Integer count=0;
		try {
			count=productDao.add(product);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return count>0;
		}
	}

	@Override
	public boolean update(Product product) {
		Integer count=0;
		try {
			count=productDao.update(product);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return count>0;
		}
	}

	@Override
	public boolean deleteProductById(Integer productId) {
		Integer count=0;
		try {
			count=productDao.deleteProductById(productId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return count>0;
		}
	}

	@Override
	public Product getProductById(Integer productId) {
		Product product=null;
		try {
			product=productDao.getProductById(productId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return product;
		}
	}

	@Override
	public List<Product> getProductList(Integer currentPageNo,Integer pageSize,String proName, Integer categoryId, Integer level) {
		List<Product> productList=null;
		try {
			productList=productDao.getProductList(currentPageNo,pageSize,proName,categoryId,level);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return productList;
		}
	}

	@Override
	public int count(String proName,Integer categoryId, Integer level) {
		Integer count=0;
		try {
			count=productDao.queryProductCount(proName,categoryId,level);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return count;
		}
	}

	@Override
	public boolean updateStock(Integer productId, Integer stock) {
		Integer count=0;
		try {
			count=productDao.updateStock(productId,stock);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return count>0;
		}
	}
   
}

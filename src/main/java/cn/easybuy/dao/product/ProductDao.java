package cn.easybuy.dao.product;

import java.util.List;

import cn.easybuy.dao.IBaseDao;
import cn.easybuy.entity.News;
import cn.easybuy.entity.Product;
import cn.easybuy.param.NewsParams;
import cn.easybuy.param.ProductParam;
import org.apache.ibatis.annotations.Param;

/**
 * 商品查询Dao
 *
 * deleteById(Integer id)
 * getById(Integer id)
 * getRowCount(params)
 * getRowList(params)
 *
 */
public interface ProductDao extends IBaseDao {

	Integer updateStock(@Param("id") Integer id, @Param("stock") Integer stock) throws Exception;
	
	public Integer add(Product product) throws Exception;

	public Integer update(Product product) throws Exception;
	
	public Integer deleteProductById(Integer id) throws Exception;
	
	public Product getProductById(Integer id)throws Exception;
	
	public List<Product> getProductList(@Param("currentPageNo") Integer currentPageNo, @Param("pageSize") Integer pageSize, @Param("proName") String proName, @Param("categoryId") Integer categoryId, @Param("level") Integer level)throws Exception;
	
	public Integer queryProductCount(@Param("proName") String proName, @Param("categoryId") Integer categoryId, @Param("level") Integer level)throws Exception;
}

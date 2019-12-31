package cn.easybuy.service.product;

import cn.easybuy.entity.ProductCategory;
import cn.easybuy.param.ProductCategoryParam;
import cn.easybuy.utils.Params;
import cn.easybuy.utils.ProductCategoryVo;

import java.util.List;

/**
 * Created by bdqn on 2016/5/8.
 */
public interface ProductCategoryService {
    /**
     * 根据id查询商品分类
     * @param id
     * @return
     */
    public ProductCategory getById(Integer id);
    /**
     * 查询商品分类列表
     * @param params
     * @return
     */
    public List<ProductCategory> queryProductCategoryList(ProductCategoryParam params);
    /**
     * 查询数目
     * @param params
     * @return
     */
    public int queryProductCategoryCount(ProductCategoryParam params);
    /**
     * 修改商品分类
     * @param params
     */
    public void modifyProductCategory(ProductCategory productCategory);
    /**
     * 添加商品分类
     * @param params
     */
    public void addProductCategory(ProductCategory productCategory);
    /**
     * 根据id删除
     * @param id
     */
    public void deleteById(Integer id);
    /**
     * 查询全部的商品分类
     * @return
     */
    public List<ProductCategoryVo> queryAllProductCategoryList();
}

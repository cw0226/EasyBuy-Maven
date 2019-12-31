package cn.easybuy.utils;

import cn.easybuy.entity.Order;
import cn.easybuy.entity.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart implements Serializable{
	public List<ShoppingCartItem> items = new ArrayList<ShoppingCartItem>();
	private Double sum;

	//获取购物车中所有商品
	public List<ShoppingCartItem> getItems() {
		return items;
	}	
	//添加一项
	public ReturnResult addItem(Product product, Integer quantity) {
		ReturnResult result=new ReturnResult();
		int flag=0;
		for(int i=0;i<items.size();i++){
			//判断购物车中是否已有此商品，如果有则累计数量
			if((items.get(i).getProduct().getId()).equals(product.getId())){
				if(items.get(i).getQuantity()+quantity>product.getStock()){
					return result.returnFail("商品数量不足");
				}else{
					items.get(i).setQuantity(items.get(i).getQuantity()+quantity);
					flag=1;
				}
			}
		}
		if(quantity>product.getStock()){
			return result.returnFail("商品数量不足");
		}
		if(flag==0){
			items.add(new ShoppingCartItem(product, quantity));
		}
		return result.returnSuccess();
	}

	//移除一项
	public void removeItem(int index) {
		items.remove(index);
	}

	//修改数量
	public void modifyQuantity(int index, Integer quantity) {
		items.get(index).setQuantity(quantity);
	}

	//计算总价格
	public float getTotalCost() {
		float sum = 0;
		for (ShoppingCartItem item : items) {
			sum = sum + item.getCost();
		}
		return sum;
	}

	public void setItems(List<ShoppingCartItem> items) {
		this.items = items;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}
}

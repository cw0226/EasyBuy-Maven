package cn.easybuy.entity;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by bdqn on 2016/5/12.
 */
public class UserAddress implements Serializable {

    private Integer id;

    private String address;

    private Integer userId;

    private Date createTime;//创建时间

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

   
}

package cn.easybuy.service.user;

import cn.easybuy.dao.order.UserAddressDao;
import cn.easybuy.entity.UserAddress;
import cn.easybuy.param.UserAddressParam;
import cn.easybuy.utils.DataSourceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by bdqn on 2016/5/12.
 */
@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Resource
    private UserAddressDao userAddressDao;
    /**
     * 查询用户地址列表
     *
     * @param id
     * @return
     * @throws Exception
     */
    public List<UserAddress> queryUserAdressList(Integer id) throws Exception{
        List<UserAddress> userAddressList = null;
        try {
            UserAddressParam params = new UserAddressParam();
            params.setUserId(id);
            userAddressList = userAddressDao.queryUserAddressList(params);
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
        }
        return userAddressList;
    }
    /**
     * 添加用户地址
     *
     * @param id
     * @param address
     * @return
     */
    @Override
    public Integer addUserAddress(Integer id, String address,String remark) {
        Integer userAddressId = null;
        try {
            UserAddress userAddress=new UserAddress();
            userAddress.setUserId(id);
            userAddress.setAddress(address);
            userAddress.setRemark(remark);
            userAddressId = userAddressDao.add(userAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userAddressId;
    }

    @Override
    public UserAddress getUserAddressById(Integer id) {
        UserAddress userAddress= null;
        try {
            userAddress = (UserAddress) userAddressDao.getUserAddressById(id);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            return  userAddress;
        }
    }
}

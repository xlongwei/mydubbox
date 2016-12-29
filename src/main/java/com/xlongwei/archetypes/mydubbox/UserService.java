package com.xlongwei.archetypes.mydubbox;

import javax.validation.constraints.Min;

import com.xlongwei.archetypes.mydubbox.model.User;
import com.xlongwei.archetypes.mydubbox.util.Result;

public interface UserService {
	/** 获取用户，返回对象类型 */
	User getUser(@Min(1L) Long id);
	/** 更新用户状态，返回Result类型 */
	Result<Boolean> updateUser(@Min(1L) Long id, String status);
	/** 删除用户，返回原始类型 */
	boolean deleteUser(@Min(1L) Long id);
    /** 注册用户，返回ID */
	Long registerUser(User user);
}

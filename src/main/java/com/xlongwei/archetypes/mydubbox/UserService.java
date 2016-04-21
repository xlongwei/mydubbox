package com.xlongwei.archetypes.mydubbox;

import javax.validation.constraints.Min;

import com.xlongwei.archetypes.mydubbox.model.User;
import com.xlongwei.archetypes.mydubbox.util.Result;

public interface UserService {

	Result<User> getUser(@Min(1L) Long id);
    
	Result<Boolean> registerUser(User user);
}

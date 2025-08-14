package com.playstation.manager.utilz;

import com.playstation.manager.entity.Role;
import com.playstation.manager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    public boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }


}
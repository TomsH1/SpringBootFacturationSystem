package com.backend.facturationsystem.models.services;

import com.backend.facturationsystem.models.entities.User;

public interface UserServices {
    public User findByUsername(String username);
}

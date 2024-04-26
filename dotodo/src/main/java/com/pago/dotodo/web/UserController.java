package com.pago.dotodo.web;

import com.pago.dotodo.service.UserService;

public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}

package com.pago.dotodo.web;

import com.pago.dotodo.model.dto.binding.UserTokenDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {

    private final UserTokenDto loggedUser;

    public AuthController(UserTokenDto loggedUser) {
        this.loggedUser = loggedUser;
    }

}

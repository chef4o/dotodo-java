package com.pago.dotodo.user.service;

import com.pago.dotodo.common.util.CustomStringUtil;
import com.pago.dotodo.user.model.dto.AdminPanelUserDto;
import com.pago.dotodo.user.model.entity.RoleEntity;
import com.pago.dotodo.user.model.entity.UserEntity;
import com.pago.dotodo.user.model.enums.RoleEnum;
import com.pago.dotodo.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminPanelService {

    private final CustomStringUtil customStringUtil;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public AdminPanelService(CustomStringUtil customStringUtil, UserService userService, ModelMapper modelMapper,
                             UserRepository userRepository) {
        this.customStringUtil = customStringUtil;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public List<String> getPossibleRoles(Long userId) {
        RoleEnum currentAdminUserRole = userService.getUserById(userId).getRole().getRole();

        return Arrays.stream(RoleEnum.values())
                .filter(role -> role.ordinal() < currentAdminUserRole.ordinal())
                .map(role -> customStringUtil.normalize(role.name()))
                .toList();
    }

    public void editUser(AdminPanelUserDto userToEdit, Long id) {
        UserEntity targetUser = userService.getUserById(id);
        RoleEntity roleToEdit = modelMapper.map(userToEdit.getRole(), RoleEntity.class);

        if (!userToEdit.getEmail().equals(targetUser.getEmail())) {
            targetUser.setEmail(userToEdit.getEmail());
        }

        if (!userToEdit.getUsername().equals(targetUser.getUsername())) {
            targetUser.setUsername(userToEdit.getUsername());
        }

        if (!roleToEdit.equals(targetUser.getRole())) {
            targetUser.setRole(roleToEdit);
        }

        userRepository.saveAndFlush(targetUser);
    }
}

package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.ChecklistDto;
import com.pago.dotodo.model.dto.binding.UserTokenDto;
import com.pago.dotodo.model.entity.Checklist;
import com.pago.dotodo.model.entity.User;
import com.pago.dotodo.repository.ChecklistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChecklistService {
    private final ChecklistRepository checklistRepository;
    private final UserService userService;
    private final UserTokenDto loggedUser;
    private final ModelMapper modelMapper;

    public ChecklistService(ChecklistRepository checklistRepository,
                            UserService userService,
                            UserTokenDto loggedUser,
                            ModelMapper modelMapper) {
        this.checklistRepository = checklistRepository;
        this.userService = userService;
        this.loggedUser = loggedUser;
        this.modelMapper = modelMapper;
    }

    public List<ChecklistDto> getAll(Long userId) {
        return this.checklistRepository
                .findChecklistByOwnerId(userId)
                .stream()
                .map(checklist -> modelMapper.map(checklist, ChecklistDto.class))
                .collect(Collectors.toList());
    }

    public Optional<ChecklistDto> getById(Long checklistId) {
        return Optional.ofNullable(modelMapper
                .map(this.checklistRepository.findById(checklistId), ChecklistDto.class));
    }

    public void deleteById(Long checklistId) {
        checklistRepository.deleteById(checklistId);
    }

    public long addChecklist(ChecklistDto checklistDto) {
        Checklist newChecklist = new Checklist()
                .setTitle(checklistDto.getTitle())
                .setContent(checklistDto.getContent())
                .setArchived(false)
                .setStartDate(LocalDateTime.now())
                .setTrackProgress("New")
                .setOwner(modelMapper
                        .map(userService.getUserById(loggedUser.getId()), User.class))
                .setElements(new HashSet<>());

        return checklistRepository.save(newChecklist).getId();
    }
}

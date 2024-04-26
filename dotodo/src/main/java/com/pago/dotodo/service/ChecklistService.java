package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.ChecklistDto;
import com.pago.dotodo.model.dto.binding.UserTokenDto;
import com.pago.dotodo.model.entity.Checklist;
import com.pago.dotodo.repository.ChecklistRepository;
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

    public ChecklistService(ChecklistRepository checklistRepository,
                            UserService userService,
                            UserTokenDto loggedUser) {
        this.checklistRepository = checklistRepository;
        this.userService = userService;
        this.loggedUser = loggedUser;
    }

    public List<ChecklistDto> getAll(Long userId) {
        return this.checklistRepository
                .findChecklistByOwnerId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<ChecklistDto> getById(Long checklistId) {
        return this.checklistRepository
                .findById(checklistId)
                .map(this::mapToDto);
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
                .setOwner(userService.getUser(loggedUser.getId()).orElseThrow())
                .setElements(new HashSet<>());

        return checklistRepository.save(newChecklist).getId();
    }

    private ChecklistDto mapToDto(Checklist checklist) {
        return new ChecklistDto()
                .setTitle(checklist.getTitle())
                .setContent(checklist.getContent())
                .setArchived(checklist.getArchived())
                .setStartDate(checklist.getStartDate())
                .setDueDate(checklist.getDueDate())
                .setCompletedOn(checklist.getCompletedOn())
                .setTrackProgress(checklist.getTrackProgress())
                .setElements(checklist.getElements());
    }
}

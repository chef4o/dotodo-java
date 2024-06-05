package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.ChecklistDto;
import com.pago.dotodo.model.entity.ChecklistEntity;
import com.pago.dotodo.model.entity.UserEntity;
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
    private final ModelMapper modelMapper;

    public ChecklistService(ChecklistRepository checklistRepository,
                            UserService userService,
                            ModelMapper modelMapper) {
        this.checklistRepository = checklistRepository;
        this.userService = userService;
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
        ChecklistEntity newChecklist = new ChecklistEntity()
                .setTitle(checklistDto.getTitle())
                .setContent(checklistDto.getContent())
                .setArchived(false)
                .setStartDate(LocalDateTime.now())
                .setTrackProgress("New")
                .setOwner(modelMapper
                        .map(userService.getUserById(1L), UserEntity.class)) //TODO: change ID
                .setElements(new HashSet<>());

        return checklistRepository.save(newChecklist).getId();
    }
}

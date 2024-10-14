package com.pago.dotodo.event.service;

import com.pago.dotodo.auth.service.AuthService;
import com.pago.dotodo.event.model.dto.EventDto;
import com.pago.dotodo.event.model.entity.EventEntity;
import com.pago.dotodo.common.error.ObjectNotFoundException;
import com.pago.dotodo.event.repository.EventRepository;
import com.pago.dotodo.common.util.DateTimeUtil;
import com.pago.dotodo.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final DateTimeUtil dateTimeUtil;
    private final AuthService authService;

    public EventService(EventRepository eventRepository,
                        UserService userService,
                        ModelMapper modelMapper,
                        DateTimeUtil dateTimeUtil,
                        AuthService authService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.dateTimeUtil = dateTimeUtil;
        this.authService = authService;
    }


    public List<EventDto> getAll(Long userId) {
        return this.eventRepository
                .findEventsByOwnerId(userId)
                .stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .collect(Collectors.toList());
    }

    public EventDto getById(Long eventId, Long currentUserId) {
        EventEntity eventEntity = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("event", eventId));

        authService.validateOwnerAccess(eventEntity.getOwner().getId(), currentUserId);

        return modelMapper.map(eventEntity, EventDto.class);
    }

    public void deleteById(Long eventId, Long currentUserId) {
        EventDto eventDto = this.getById(eventId, currentUserId);
        eventRepository.deleteById(eventDto.getId());
    }

    public long addEvent(EventDto eventDto, Long userId) {
        EventEntity newEvent = new EventEntity()
                .setTitle(eventDto.getTitle())
                .setContent(eventDto.getContent())
                .setArchived(false)
                .setDate(dateTimeUtil
                        .formatToDateTime(eventDto.getDate(), eventDto.getTime(),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setOwner(userService.getUserById(userId))
                .setPeers(new HashSet<>());

        return eventRepository.save(newEvent).getId();
    }
}

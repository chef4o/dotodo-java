package com.pago.dotodo.web;

import com.pago.dotodo.model.dto.ChecklistDto;
import com.pago.dotodo.model.dto.binding.UserTokenDto;
import com.pago.dotodo.service.ChecklistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/checklists")
public class ChecklistController extends BaseController {

    private final ChecklistService checklistService;
    private final UserTokenDto loggedUser;

    public ChecklistController(ChecklistService checklistService, UserTokenDto loggedUser) {
        this.checklistService = checklistService;
        this.loggedUser = loggedUser;
    }

    @GetMapping
    public ResponseEntity<List<ChecklistDto>> getAll() {
        return ResponseEntity.ok(checklistService.getAll(this.loggedUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChecklistDto> getById(@PathVariable("id") Long id) {
        Optional<ChecklistDto> currentChecklist = checklistService.getById(id);

        return currentChecklist.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChecklistDto> deleteById(@PathVariable("id") Long id) {
        checklistService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<ChecklistDto> createChecklist(@RequestBody ChecklistDto checklistDto,
                                                        UriComponentsBuilder uriComponentsBuilder) {
        long checklistId = checklistService.addChecklist(checklistDto);

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/api/books/{id}").build(checklistId))
                .build();
    }
}

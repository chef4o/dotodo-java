package com.pago.dotodo.web.api;

import com.pago.dotodo.model.dto.ChecklistDto;
import com.pago.dotodo.security.CustomAuthUserDetails;
import com.pago.dotodo.service.ChecklistService;
import com.pago.dotodo.web.mvc.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/checklists")
public class ChecklistApiController extends BaseController {

    private final ChecklistService checklistService;

    public ChecklistApiController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @GetMapping
    public ResponseEntity<List<ChecklistDto>> getAll(Long userId) {
        return ResponseEntity.ok(checklistService.getAll(userId));
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
                                                        @AuthenticationPrincipal CustomAuthUserDetails userDetails,
                                                        UriComponentsBuilder uriComponentsBuilder) {
        long checklistId = checklistService.addChecklist(checklistDto, userDetails.getId());

        return ResponseEntity.created(uriComponentsBuilder
                        .path("/api/books/{id}").build(checklistId))
                .build();
    }
}

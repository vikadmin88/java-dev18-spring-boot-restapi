package org.springtest.controller.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springtest.controller.request.CreateNoteRequest;
import org.springtest.controller.request.UpdateNoteRequest;
import org.springtest.controller.response.CustomErrorResponse;
import org.springtest.controller.response.NoteResponse;
import org.springtest.data.entity.Note;
import org.springtest.service.dto.NoteDto;
import org.springtest.service.exception.NoteNotFoundException;
import org.springtest.service.mapper.NoteMapper;
import org.springtest.service.service.NoteService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @GetMapping("/")
    @Operation(summary = "Get all notes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List notes",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Note.class)) })
    })
    public ResponseEntity<List<NoteResponse>> notesList() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(noteMapper.toNoteResponses(noteService.listAll()));
}

    @GetMapping("/{id}")
    @Operation(summary = "Get note by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found note by id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Note.class)) }),
            @ApiResponse(responseCode = "404", description = "Note not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) })

    })
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable("id") UUID id) throws NoteNotFoundException {
        NoteDto noteDto = noteService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(noteMapper.toNoteResponse(noteDto));
    }

    @PostMapping("/create")
    @Operation(summary = "Create note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created note",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Note.class))}),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    public ResponseEntity<NoteResponse> createNote(@Valid @NotNull @RequestBody CreateNoteRequest request) {
        NoteDto noteDto = noteService.add(noteMapper.toNoteDto(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(noteMapper.toNoteResponse(noteDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note updated"),
            @ApiResponse(responseCode = "404", description = "Note not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Validation errors",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class))})
    })
    @ResponseStatus(HttpStatus.OK)
    public void updateNote(@PathVariable("id") UUID id,
                           @RequestBody @Valid @NotNull UpdateNoteRequest request) throws NoteNotFoundException {
        noteService.update(noteMapper.toNoteDto(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note deleted"),
            @ApiResponse(responseCode = "404", description = "Note not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomErrorResponse.class)) }) })
    @ResponseStatus(HttpStatus.OK)
    public void deleteNoteById(@PathVariable("id") UUID id) throws NoteNotFoundException {
        noteService.deleteById(id);
    }
}

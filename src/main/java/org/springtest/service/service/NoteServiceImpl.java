package org.springtest.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springtest.data.entity.Note;
import org.springtest.data.repository.NoteRepository;
import org.springframework.stereotype.Service;
import org.springtest.service.dto.NoteDto;
import org.springtest.service.exception.NoteNotFoundException;
import org.springtest.service.mapper.NoteMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public List<NoteDto> listAll() {
        log.info("Getting all notes from repository");
        return noteMapper.toNoteDtos(noteRepository.findAll());
    }

    @Override
    public NoteDto add(NoteDto noteDto) {
        log.info("Adding noteDto: {}", noteDto);
        return noteMapper.toNoteDto(noteRepository.save(noteMapper.toNoteEntity(noteDto)));
    }

    @Override
    public void deleteById(UUID id) throws NoteNotFoundException {
        log.info("Deleting note with id: {}", id);
        if (Objects.isNull(id)) {
            throw new NoteNotFoundException("Not found note Id");
        }
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException("Not found note Id: " + id);
        }
        noteRepository.deleteById(id);
    }

    @Override
    public void update(NoteDto noteDto) throws NoteNotFoundException {
        log.info("Updating noteDto: {}", noteDto);
        if (!noteRepository.existsById(noteDto.getId())) {
            throw new NoteNotFoundException("Not found noteDto Id: " + noteDto.getId());
        }
        noteRepository.save(noteMapper.toNoteEntity(noteDto));
    }

    @Override
    public NoteDto getById(UUID id) throws NoteNotFoundException {
        log.info("Getting note with id: {}", id);
        Optional<Note> opNote = noteRepository.findById(id);
        return noteMapper.toNoteDto(opNote.orElseThrow(() -> new NoteNotFoundException(String.format("Note with id %s not found", id))));
    }
}

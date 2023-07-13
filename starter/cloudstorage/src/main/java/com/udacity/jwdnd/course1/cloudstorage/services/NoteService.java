package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }


    public Note getNoteById(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public Note[] getAllNotes(Integer userId) {
        return noteMapper.getAllNotesByUserId(userId);
    }

    public int createNewNote (Note note) {
        return noteMapper.insert(note);
    }

    public int updateExistingNote (Note note) {
        return noteMapper.update(note);
    }
    public int addOrEditNote(Note note) {
        try {
            if (note.getNoteId() == null) {
                return createNewNote(note);
            } else {
                return updateExistingNote(note);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void deleteNote(Integer noteId, Integer userId) {
        noteMapper.delete(noteId, userId);
    }
}

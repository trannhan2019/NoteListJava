package com.example.notelistjava.bean;

import java.io.Serializable;

public class Note implements Serializable {
    private int noteId;
    private String noteTile;
    private String noteContent;

    public Note() {
    }

    public Note(String noteTile, String noteContent) {
        this.noteTile = noteTile;
        this.noteContent = noteContent;
    }

    public Note(int noteId, String noteTile, String noteContent) {
        this.noteId = noteId;
        this.noteTile = noteTile;
        this.noteContent = noteContent;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTile() {
        return noteTile;
    }

    public void setNoteTile(String noteTile) {
        this.noteTile = noteTile;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    @Override
    public String toString() {
        return this.noteTile;
    }
}

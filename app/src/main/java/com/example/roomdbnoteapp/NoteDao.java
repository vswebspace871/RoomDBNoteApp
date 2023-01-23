package com.example.roomdbnoteapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {


    String TABLE_NAME = "MyNote";
    @Query("SELECT * FROM "+TABLE_NAME)
    List<NoteModel> getNotes();

    @Insert
    void insert(NoteModel note);

    @Delete
    void delete(NoteModel note);

    @Update
    void update(NoteModel note);


}

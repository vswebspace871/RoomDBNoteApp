package com.example.roomdbnoteapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = NoteModel.class, version = 1, exportSchema = false)
public abstract class DBHelper extends RoomDatabase {

    private static final String DB_NAME = "notes_db";
    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DBHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract NoteDao noteDao();
}

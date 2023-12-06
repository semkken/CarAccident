package com.kasemsan.accident.entity;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DataEntity.class}, version = 1, exportSchema = false)
public abstract class DataRoomDatabase extends RoomDatabase {
    private static volatile DataRoomDatabase INSTANCE;
    public abstract DataInterface dataInter();

    public static DataRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DataRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                                    DataRoomDatabase.class, "car_accident")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

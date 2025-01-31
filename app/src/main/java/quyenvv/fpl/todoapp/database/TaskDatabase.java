package quyenvv.fpl.todoapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import quyenvv.fpl.todoapp.model.Task;

@Database(entities = {Task.class},version = 1)
public abstract class TaskDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "task.db";
    private static TaskDatabase instance;

    public static synchronized TaskDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),TaskDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract TaskDao taskDao();

}

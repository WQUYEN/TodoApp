package quyenvv.fpl.todoapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import quyenvv.fpl.todoapp.model.Task;

@Dao
public interface TaskDao {
    @Insert
    void insertTask(Task task);

    @Query("SELECT * FROM task")
    List<Task> getListTask();
    @Update
    void updateTask(Task task);
    @Delete
    void deleteTask(Task task);
}

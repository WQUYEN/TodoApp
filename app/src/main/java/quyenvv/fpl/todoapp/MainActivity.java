package quyenvv.fpl.todoapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import quyenvv.fpl.todoapp.adapter.TaskAdapter;
import quyenvv.fpl.todoapp.database.TaskDatabase;
import quyenvv.fpl.todoapp.model.Task;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcv_task;
    private FloatingActionButton fab_Add;
    private TaskAdapter taskAdapter;
    private List<Task> listTask;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        listTask = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, listTask, new TaskAdapter.ClickItemTask() {
            @Override
            public void updateTask(Task task) {
                dialog_edit(task);
            }

            @Override
            public void deleteTask(Task task) {
                dialog_delete(task);
            }

            @Override
            public void detailTask(Task task) {

            }
        });
        taskAdapter.setData(listTask);

        //-----RecycleView-------------
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_task.setLayoutManager(linearLayoutManager);

        rcv_task.setAdapter(taskAdapter);

        loadData();
        fab_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_add();

            }
        });
    }

    private void dialog_delete(Task task) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm delete task")
                .setMessage("Are you sure ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TaskDatabase.getInstance(MainActivity.this).taskDao().deleteTask(task);
                        Toast.makeText(MainActivity.this, "Delete task successfully", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void initView() {
        rcv_task = findViewById(R.id.rcv_task);
        fab_Add = findViewById(R.id.fab_add_task);
    }

    private void dialog_add() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_task, null);
        builder.setView(dialogView);

//        initView
        TextInputLayout ed_title = dialogView.findViewById(R.id.ed_title_task_dialog);
        TextInputLayout ed_description = dialogView.findViewById(R.id.ed_description_task_dialog);
        AppCompatButton btn_add = dialogView.findViewById(R.id.btn_add_dialog);
        TextView tv_deadline = dialogView.findViewById(R.id.tv_deadline_dialog);
        LinearLayout layout_status = dialogView.findViewById(R.id.layout_status);
        ImageView img_back = dialogView.findViewById(R.id.img_back_dialog);

        tv_deadline.setVisibility(View.GONE);
        layout_status.setVisibility(View.GONE);
        //setView


        //Show Dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        //Click Add Button
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle;
                String input_newTitle = ed_title.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(input_newTitle)) {
                    ed_title.setError("Title không được để trống !");
                    return;
                } else {
                    newTitle = input_newTitle;
                    ed_title.setError(null);
                }


                String newDescription ;
                String input_newDescription = ed_description.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(input_newDescription)) {
                    ed_description.setError("Description không được để trống !");
                    return;
                } else if(input_newDescription.length()>150){
                    ed_description.setError("Description không quá 150 kí tự !");
                    return;
                } else {
                    newDescription = input_newDescription;
                    ed_description.setError(null);
                }

                Task task = new Task(newTitle, newDescription, false);
                TaskDatabase.getInstance(MainActivity.this).taskDao().insertTask(task);
                Toast.makeText(MainActivity.this, "Add task successfully", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                loadData();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void loadData() {
        listTask = TaskDatabase.getInstance(this).taskDao().getListTask();
        taskAdapter.setData(listTask);
    }

    private void dialog_edit(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_task, null);
        builder.setView(dialogView);

//        initView
        TextView title_dialog = dialogView.findViewById(R.id.title_dialog);
        TextInputLayout ed_title = dialogView.findViewById(R.id.ed_title_task_dialog);
        TextInputLayout ed_description = dialogView.findViewById(R.id.ed_description_task_dialog);
        AppCompatButton btn_add = dialogView.findViewById(R.id.btn_add_dialog);
        TextView tv_deadline = dialogView.findViewById(R.id.tv_deadline_dialog);
        TextView tv_status = dialogView.findViewById(R.id.tv_status_dialog);
        Switch sw_status = dialogView.findViewById(R.id.sw_status_dialog);
        ImageView img_back = dialogView.findViewById(R.id.img_back_dialog);

        //set
        title_dialog.setText("Edit Task");
        ed_title.getEditText().setText(task.getTitle());
        ed_description.getEditText().setText(task.getDescription());
        btn_add.setText("Save");
        tv_deadline.setVisibility(View.VISIBLE);
        String dueDate = task.getDueDate();
        if (dueDate == null || dueDate.length() == 0) {
            tv_deadline.setText("Add date time");
        } else {
            tv_deadline.setText("Deadline: "+dueDate);
        }
        if (task.isCompleted()) {
            tv_status.setText("Đã hoàn thành");
            sw_status.setChecked(true);
        } else {
            tv_status.setText("Chưa hoàn thành");
            sw_status.setChecked(false);
        }
        //Show Dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        sw_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sw_status.isChecked()) {
                    tv_status.setText("Đã hoàn thành");
                } else {
                    tv_status.setText("Chưa hoàn thành");
                }
            }
        });
        //Add Deadline
        tv_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy năm, tháng, ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog với giá trị mặc định là ngày, tháng, năm hiện tại
                DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
                    // Xử lý ngày đã chọn
                    int month = monthOfYear + 1; // Tháng được đánh số từ 0-11
                    int day = dayOfMonth;
                    int yearSelected = year;

                    // Hiển thị ngày, tháng, năm đã chọn lên tv_deadline
                    selectedDate = String.format("%02d/%02d/%04d", day, month, yearSelected);
                    tv_deadline.setText(selectedDate);

                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        onDateSetListener,
                        currentYear, // Năm mặc định
                        currentMonth, // Tháng mặc định (0-11)
                        currentDay // Ngày mặc định
                );
                datePickerDialog.show();
            }
        });
        //Click Add Button
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDueDate = selectedDate;
                String newTitle;
                String input_newTitle = ed_title.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(input_newTitle)) {
                    ed_title.setError("Title không được để trống !");
                    return;
                } else {
                    newTitle = input_newTitle;
                    ed_title.setError(null);
                }


                String newDescription ;
                String input_newDescription = ed_description.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(input_newDescription)) {
                    ed_description.setError("Description không được để trống !");
                    return;
                } else if(input_newDescription.length()>150){
                    ed_description.setError("Description không quá 150 kí tự !");
                    return;
                } else {
                    newDescription = input_newDescription;
                    ed_description.setError(null);
                }


                task.setTitle(newTitle);
                task.setDescription(newDescription);
                task.setDueDate(newDueDate);
                task.setCompleted(sw_status.isChecked());
                TaskDatabase.getInstance(MainActivity.this).taskDao().updateTask(task);
                Toast.makeText(MainActivity.this, "Edit task successfully", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                loadData();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
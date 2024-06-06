package quyenvv.fpl.todoapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import quyenvv.fpl.todoapp.R;
import quyenvv.fpl.todoapp.model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> mListTask;
    private ClickItemTask clickItemTask;
    public interface ClickItemTask{
        void updateTask(Task task);
        void deleteTask(Task task);
        void detailTask(Task task);
    }
    public void setData(List<Task> list){
        this.mListTask = list;
        notifyDataSetChanged();
    }

    public TaskAdapter(Context context, List<Task> mListTask, ClickItemTask clickItemTask) {
        this.context = context;
        this.mListTask = mListTask;
        this.clickItemTask = clickItemTask;
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        Task task = mListTask.get(position);
        if (task == null){
            return;
        }
        holder.tv_title.setText(task.getTitle());
        holder.tv_desription.setText(task.getDescription());
        String dueDate = task.getDueDate();
        if (dueDate == null || dueDate.length() == 0){
            holder.tv_dueDate.setVisibility(View.GONE);

        }else{
            holder.tv_dueDate.setText(task.getDueDate());
            holder.tv_dueDate.setVisibility(View.VISIBLE);

        }

        if (task.isCompleted()){
            holder.tv_status.setText("Đã hoàn thành");
            holder.tv_status.setTextColor(Color.GREEN);
            holder.tv_dueDate.setVisibility(View.GONE);
        }else{
            holder.tv_status.setText("Chưa hoàn thành");
            holder.tv_status.setTextColor(Color.RED);
            holder.tv_dueDate.setVisibility(View.VISIBLE);
        }


        holder.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo popup menu
                PopupMenu popupMenu = new PopupMenu(context, holder.img_menu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_itemlv, popupMenu.getMenu());

                // Đăng ký các sự kiện cho các menu item
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.menu_update) {
                            // Xử lý update task
                           // updateTask(mListTask.get(holder.getAdapterPosition()));
                            //Toast.makeText(context, "Update", Toast.LENGTH_SHORT).show();
                            clickItemTask.updateTask(task);
                            return true;
                        } else if (itemId == R.id.menu_delete) {
                            // Xử lý delete task
                           // deleteTask(mListTask.get(holder.getAdapterPosition()));
                           // Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                            clickItemTask.deleteTask(task);

                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListTask != null){
            return mListTask.size();
        }
        return 0;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title, tv_desription, tv_status, tv_dueDate;
        private ImageView img_menu;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_task);
            tv_desription = itemView.findViewById(R.id.tv_description_itemLv);
            tv_status = itemView.findViewById(R.id.tv_status_itemLv);
            tv_dueDate = itemView.findViewById(R.id.tv_dueDate_itemLv);
            img_menu = itemView.findViewById(R.id.menu_itemLv);
        }
    }
}

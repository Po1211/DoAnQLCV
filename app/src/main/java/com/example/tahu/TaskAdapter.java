package com.example.tahu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<MyViewHolder2>{

    private Context context;
    private List<TasksModel> taskList;

    public TaskAdapter(Context context, List<TasksModel> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent,false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {

        holder.tvTaskTitle.setText(taskList.get(position).getT_title());
        holder.tvTaskDesc.setText(taskList.get(position).getT_description());
        holder.tvTaskStatus.setText(taskList.get(position).getT_status());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void searchTaskList(ArrayList<TasksModel> searchList){
        taskList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder2 extends RecyclerView.ViewHolder{

    TextView tvTaskTitle, tvTaskDesc, tvTaskStatus;
    CardView cardViewProject;

    public MyViewHolder2(@NonNull View itemView) {
        super(itemView);

        cardViewProject = itemView.findViewById(R.id.card_view_task);
        tvTaskTitle = itemView.findViewById(R.id.tv_task_title);
        tvTaskDesc = itemView.findViewById(R.id.tv_task_des);
        tvTaskStatus = itemView.findViewById(R.id.tv_task_status);
    }
}

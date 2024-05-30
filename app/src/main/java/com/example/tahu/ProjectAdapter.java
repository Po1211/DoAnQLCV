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

public class ProjectAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private Context context;
    private List<ProjectsModel> projectList;

    public ProjectAdapter(Context context, List<ProjectsModel> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvProjectName.setText(projectList.get(position).getP_name());
        holder.tvProjectDesc.setText(projectList.get(position).getP_description());

        holder.cardViewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProjectDetail.class);
                intent.putExtra("p_name", projectList.get(holder.getAdapterPosition()).getP_name());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public void searchProjectList(ArrayList<ProjectsModel> searchList){
        projectList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    TextView tvProjectName, tvProjectDesc;
    CardView cardViewProject;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        cardViewProject = itemView.findViewById(R.id.card_view_project);
        tvProjectName = itemView.findViewById(R.id.tv_project_name);
        tvProjectDesc = itemView.findViewById(R.id.tv_project_des);

    }
}

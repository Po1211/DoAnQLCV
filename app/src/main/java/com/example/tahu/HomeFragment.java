package com.example.tahu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvUsername, tvProjects, tvTasks;
    private RecyclerView rvProjects, rvTasks;
    private ProjectAdapter projectAdapter;
    private TaskAdapter taskAdapter;
    private DatabaseReference projectsRef, tasksRef;
    private ValueEventListener projectsListener, tasksListener;

    private List<ProjectsModel> projectList;
    private List<TasksModel> taskList;

    private String currentUsername;

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvUsername = view.findViewById(R.id.tv_username);
        tvProjects = view.findViewById(R.id.tv_projects);
        tvTasks = view.findViewById(R.id.tv_task);

        rvProjects = view.findViewById(R.id.rv_projects);
        rvTasks = view.findViewById(R.id.rv_tasks);

        rvProjects.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvTasks.setLayoutManager(new LinearLayoutManager(requireContext()));

        projectList = new ArrayList<>();
        taskList = new ArrayList<>();

        projectAdapter = new ProjectAdapter(requireContext(), projectList);
        taskAdapter = new TaskAdapter(requireContext(), taskList);

        rvProjects.setAdapter(projectAdapter);
        rvTasks.setAdapter(taskAdapter);

        projectsRef = FirebaseDatabase.getInstance().getReference("projects");
        tasksRef = FirebaseDatabase.getInstance().getReference("tasks");

        sharedPreferences = requireActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        currentUsername = sharedPreferences.getString("username", null);
        if (currentUsername != null) {
            tvUsername.setText(currentUsername);

            loadRecentProjects();

            loadRecentTasks();
        } else {

        }
        return view;
    }

    private void loadRecentProjects() {
        Query projectsQuery = projectsRef.orderByChild("timestamp").limitToLast(3);
        projectsListener = projectsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProjectsModel project = dataSnapshot.getValue(ProjectsModel.class);
                    if (project != null) {
                        if (currentUsername.equals(project.getP_owner()) ||
                                (project.getP_members() != null && project.getP_members().contains(currentUsername))) {
                            projectList.add(project);
                        }
                    }
                }
                Collections.reverse(projectList);
                projectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void loadRecentTasks() {
        Query tasksQuery = tasksRef.orderByChild("timestamp").limitToLast(3);
        tasksListener = tasksQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                taskList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TasksModel task = dataSnapshot.getValue(TasksModel.class);
                    if (task != null && currentUsername.equals(task.getT_assigned_to()) && "inprogress".equals(task.getT_status())) {
                        taskList.add(task);
                    }
                }
                Collections.reverse(taskList);
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (projectsListener != null) {
            projectsRef.removeEventListener(projectsListener);
        }
        if (tasksListener != null) {
            tasksRef.removeEventListener(tasksListener);
        }
    }
}

package com.example.tahu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TodoFragment extends Fragment {

    private RecyclerView rvTodo;
    private TaskAdapter taskAdapter;
    private DatabaseReference tasksRef;

    private List<TasksModel> todoTasksList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        rvTodo = view.findViewById(R.id.rv_todo);
        rvTodo.setLayoutManager(new LinearLayoutManager(requireContext()));

        tasksRef = FirebaseDatabase.getInstance().getReference("tasks");

        todoTasksList = new ArrayList<>();

        taskAdapter = new TaskAdapter(requireContext(), todoTasksList);
        rvTodo.setAdapter(taskAdapter);

        loadTodoTasks();

        return view;
    }

    private void loadTodoTasks() {
        tasksRef.orderByChild("t_status").equalTo("todo")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        todoTasksList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            TasksModel task = dataSnapshot.getValue(TasksModel.class);
                            if (task != null) {
                                todoTasksList.add(task);
                            }
                        }
                        taskAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
    }
}

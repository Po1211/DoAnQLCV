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

public class CompletedFragment extends Fragment {

    private RecyclerView rvCompleted;
    private TaskAdapter taskAdapter;
    private DatabaseReference tasksRef;

    private List<TasksModel> completedTasksList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);

        rvCompleted = view.findViewById(R.id.rv_completed);
        rvCompleted.setLayoutManager(new LinearLayoutManager(requireContext()));

        tasksRef = FirebaseDatabase.getInstance().getReference("tasks");

        completedTasksList = new ArrayList<>();

        taskAdapter = new TaskAdapter(requireContext(), completedTasksList);
        rvCompleted.setAdapter(taskAdapter);

        loadCompletedTasks();

        return view;
    }

    private void loadCompletedTasks() {
        tasksRef.orderByChild("t_status").equalTo("completed")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        completedTasksList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            TasksModel task = dataSnapshot.getValue(TasksModel.class);
                            if (task != null) {
                                completedTasksList.add(task);
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

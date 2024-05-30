package com.example.tahu;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    RecyclerView rvTask;
    List<TasksModel> tasksList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    SearchView searchTask;
    TaskAdapter taskAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        rvTask = view.findViewById(R.id.rv_tasks);
        searchTask =  view.findViewById(R.id.search_task);
        searchTask.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(),1);
        rvTask.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        tasksList = new ArrayList<>();

        taskAdapter = new TaskAdapter(requireActivity(), tasksList);
        rvTask.setAdapter(taskAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("tasks");
        dialog.show();

        SharedPreferences preferences = getActivity().getSharedPreferences("loginPrefs", getContext().MODE_PRIVATE);
        String currentUsername = preferences.getString("username", null);

        Query query = databaseReference.orderByChild("t_assigned_to").equalTo(currentUsername);

        eventListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasksList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    TasksModel tasks = itemSnapshot.getValue(TasksModel.class);

                    if (tasks != null) {
                        tasksList.add(tasks);
                    }
                }
                taskAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchTask.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        return view;
    }

    public void searchList(String text){
        ArrayList<TasksModel> searchlist = new ArrayList<>();
        for (TasksModel tasks: tasksList){
            if(tasks.getT_title().toLowerCase().contains(text.toLowerCase())){
                searchlist.add(tasks);
            }
        }
        taskAdapter.searchTaskList(searchlist);
    }
}

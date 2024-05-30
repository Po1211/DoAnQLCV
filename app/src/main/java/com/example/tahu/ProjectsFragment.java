package com.example.tahu;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjectsFragment extends Fragment {

    FloatingActionButton fab_addproject;
    RecyclerView rvProject;
    List<ProjectsModel> projectsList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    SearchView searchProject;
    ProjectAdapter projectAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        fab_addproject = view.findViewById(R.id.fab_addproject);
        rvProject = view.findViewById(R.id.rv_projects);
        searchProject =  view.findViewById(R.id.search_project);
        searchProject.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(),1);
        rvProject.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        projectsList = new ArrayList<>();

        projectAdapter = new ProjectAdapter(requireActivity(), projectsList);
        rvProject.setAdapter(projectAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("projects");
        dialog.show();

        // Retrieve the current username from SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("loginPrefs", getContext().MODE_PRIVATE);
        String currentUsername = preferences.getString("username", null);

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                projectsList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    ProjectsModel projects = itemSnapshot.getValue(ProjectsModel.class);

                    if (projects != null && currentUsername != null) {
                        if (currentUsername.equals(projects.getP_owner()) ||
                                (projects.getP_members() != null && projects.getP_members().contains(currentUsername))) {
                            projectsList.add(projects);
                        }
                    }
                }
                projectAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchProject.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        fab_addproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), UploadActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void searchList(String text){
        ArrayList<ProjectsModel> searchlist = new ArrayList<>();
        for (ProjectsModel projects: projectsList){
            if(projects.getP_name().toLowerCase().contains(text.toLowerCase())){
                searchlist.add(projects);
            }
        }
        projectAdapter.searchProjectList(searchlist);
    }
}

package com.example.tahu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends AppCompatActivity {

    Button addButton;
    EditText addProjectName, addProjectDesc;
    DatabaseReference databaseReference;
    SharedPreferences sharedPref;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        addProjectName = findViewById(R.id.addProjectName);
        addProjectDesc = findViewById(R.id.addProjectDesc);
        addButton = findViewById(R.id.addButton);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("projects");

        sharedPref = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        currentUser = sharedPref.getString("username", "");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfProjectNameExists();
            }
        });
    }

    private void checkIfProjectNameExists() {
        String projectName = addProjectName.getText().toString().trim();
        if (projectName.isEmpty()) {
            addProjectName.setError("Project Name is required");
            addProjectName.requestFocus();
            return;
        }

        databaseReference.child(projectName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    addProjectName.setError("Project Name already exists. Please choose a different name.");
                    addProjectName.requestFocus();
                } else {
                    addProject();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UploadActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProject() {
        String projectName = addProjectName.getText().toString().trim();
        String projectDesc = addProjectDesc.getText().toString().trim();

        if (currentUser.isEmpty()) {
            Toast.makeText(UploadActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> membersList = new ArrayList<>();
        membersList.add(currentUser);

        List<String> tasksList = new ArrayList<>();

        ProjectsModel project = new ProjectsModel(projectName, projectDesc, currentUser, membersList, tasksList);

        databaseReference.child(projectName).setValue(project);

        Toast.makeText(UploadActivity.this, "Project added successfully", Toast.LENGTH_SHORT).show();

        addProjectName.setText("");
        addProjectDesc.setText("");
    }
}

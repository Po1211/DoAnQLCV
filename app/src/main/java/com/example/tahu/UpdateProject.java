package com.example.tahu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProject extends AppCompatActivity {

    private EditText projectNameEditText, projectDescEditText;
    private Button updateButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_project);

        projectNameEditText = findViewById(R.id.upProjectName);
        projectDescEditText = findViewById(R.id.upProjectDesc);
        updateButton = findViewById(R.id.updateButton);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        String projectName = getIntent().getStringExtra("p_name");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedProjectName = projectNameEditText.getText().toString().trim();
                String updatedProjectDesc = projectDescEditText.getText().toString().trim();

                updateProject(projectName, updatedProjectName, updatedProjectDesc);
            }
        });
    }

    private void updateProject(String oldProjectName, String newProjectName, String newProjectDesc) {
        DatabaseReference projectRef = databaseReference.child("projects").child(oldProjectName);

        projectRef.child("p_name").setValue(newProjectName);
        projectRef.child("p_desc").setValue(newProjectDesc, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(UpdateProject.this, "Project updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateProject.this, "Failed to update project: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

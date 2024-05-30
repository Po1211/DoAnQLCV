package com.example.tahu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetail extends AppCompatActivity {

    private TextView projectNameTextView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button btnUpdate, btnDelete;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        projectNameTextView = findViewById(R.id.detail_project_name);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        String projectName = getIntent().getStringExtra("p_name");

        projectNameTextView.setText(projectName);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProject(projectName);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProject(projectName);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TodoFragment(), "To Do");
        adapter.addFragment(new InProgressFragment(), "In Progress");
        adapter.addFragment(new CompletedFragment(), "Completed");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    public void updateProject(String projectName) {
        Intent intent = new Intent(ProjectDetail.this, UpdateProject.class);
        intent.putExtra("p_name", projectName);
        startActivity(intent);
    }

    public void deleteProject(String projectName) {
        DatabaseReference projectRef = databaseReference.child("projects").child(projectName);
        projectRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(ProjectDetail.this, "Project deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProjectDetail.this, "Failed to delete project: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


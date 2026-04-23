package com.example.part2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.part2.data.SafetyCheckWithDefects;
import com.example.part2.ui.SafetyCheckAdapter;
import com.example.part2.ui.SafetyViewModel;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CHECK_ID = "com.example.part2.EXTRA_CHECK_ID";

    private SafetyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewChecks);
        Button buttonAddCheck = findViewById(R.id.buttonAddCheck);

        SafetyCheckAdapter adapter = new SafetyCheckAdapter(new SafetyCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SafetyCheckWithDefects item) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(EXTRA_CHECK_ID, item.safetyCheck.getCheckId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(SafetyCheckWithDefects item) {
                viewModel.deleteCheck(item.safetyCheck.getCheckId());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SafetyViewModel.class);
        viewModel.getAllChecks().observe(this, adapter::setItems);

        buttonAddCheck.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddCheckActivity.class);
            startActivity(intent);
        });
    }
}
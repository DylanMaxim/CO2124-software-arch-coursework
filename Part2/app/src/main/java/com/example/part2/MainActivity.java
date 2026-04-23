package com.example.part2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.part2.data.SafetyCheckWithDefects;
import com.example.part2.ui.SafetyCheckAdapter;
import com.example.part2.ui.SafetyViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String EXTRA_CHECK_ID = "com.example.part2.EXTRA_CHECK_ID";

    private SafetyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewChecks);
        Button buttonAddCheck = findViewById(R.id.buttonAddCheck);

        SafetyCheckAdapter adapter = new SafetyCheckAdapter(new SafetyCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SafetyCheckWithDefects item) {
                Log.d(TAG, "Check item clicked");
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(EXTRA_CHECK_ID, item.safetyCheck.getCheckId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(SafetyCheckWithDefects item) {
                Log.d(TAG, "Long press - deleting check");
                viewModel.deleteCheck(item.safetyCheck.getCheckId());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SafetyViewModel.class);
        viewModel.getAllChecks().observe(this, checks -> {
            Log.d(TAG, "List updated, size: " + checks.size());
            adapter.setItems(checks);
        });

        buttonAddCheck.setOnClickListener(v -> {
            Log.d(TAG, "Add check button clicked");
            Intent intent = new Intent(MainActivity.this, AddCheckActivity.class);
            startActivity(intent);
        });
    }
}
package br.edu.ifsp.scl.sdm.pa1.intents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import br.edu.ifsp.scl.sdm.pa1.intents.databinding.ActivityActionBinding;

public class ActionActivity extends AppCompatActivity {

    public ActivityActionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.mainTb.appTb.setTitle(ActionActivity.class.getSimpleName());
        binding.mainTb.appTb.setSubtitle(getIntent().getAction());
        setSupportActionBar(binding.mainTb.appTb);

        binding.parameterTv.setText(
                getIntent().getStringExtra(Intent.EXTRA_TEXT));
    }
}
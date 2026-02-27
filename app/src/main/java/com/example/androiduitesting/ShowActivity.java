package com.example.androiduitesting;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ShowActivity extends AppCompatActivity {

    public static final String key_selected_city = "key selected city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        TextView cityText = findViewById(R.id.text_city_name);
        Button backBtn = findViewById(R.id.button_back);

        String cityName = getIntent().getStringExtra(key_selected_city);
        if (cityName == null) cityName = "";

        cityText.setText(cityName);

        // Goes back to MainActivity
        backBtn.setOnClickListener(v -> finish());
    }

}

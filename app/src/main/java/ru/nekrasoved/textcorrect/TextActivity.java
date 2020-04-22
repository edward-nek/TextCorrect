package ru.nekrasoved.textcorrect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        TextView tvOutputText = (TextView) findViewById(R.id.tvOutputText);
        tvOutputText.setText(MainActivity.outText);
    }
}

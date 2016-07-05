package io.harry.doodlenow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.R;

public class DoodleActivity extends AppCompatActivity {

    @BindView(R.id.contents)
    EditText contents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodle);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(Intent.ACTION_SEND.equals(intent.getAction())) {
            contents.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}

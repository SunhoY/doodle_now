package io.harry.doodlenow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

public class DoodleActivity extends AppCompatActivity {

    @BindView(R.id.content)
    EditText content;

    private DoodleService doodleService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodle);

        ButterKnife.bind(this);

        doodleService = ((DoodleApplication) getApplication()).getDoodleComponent().contentService();

        Intent intent = getIntent();
        if(Intent.ACTION_SEND.equals(intent.getAction())) {
            content.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }

    @OnClick(R.id.submit)
    void onSubmitClick() {
        doodleService.saveContent(content.getText().toString(), new ServiceCallback<Void>() {
            @Override
            public void onSuccess(Void item) {
                Toast.makeText(DoodleActivity.this, "참 잘했어요! 엄지 척!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}

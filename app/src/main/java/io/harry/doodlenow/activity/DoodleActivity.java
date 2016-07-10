package io.harry.doodlenow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.callback.JsoupCallback;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;
import io.harry.doodlenow.wrapper.JsoupWrapper;

public class DoodleActivity extends AppCompatActivity {

    private String doodleUrl;

    @BindView(R.id.doodle_content)
    EditText doodleContent;
    @BindView(R.id.doodle_title)
    EditText doodleTitle;

    @Inject
    DoodleService doodleService;
    @Inject
    JsoupWrapper jsoupWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodle);

        ButterKnife.bind(this);

        ((DoodleApplication) getApplication()).getDoodleComponent().inject(this);

        Intent intent = getIntent();
        doodleUrl = intent.getStringExtra(Intent.EXTRA_TEXT);

        if(Intent.ACTION_SEND.equals(intent.getAction())) {
            jsoupWrapper.getDocument(doodleUrl, new JsoupCallback() {
                @Override
                public void onSuccess(String title, String content) {
                    doodleTitle.setText(title);
                    doodleContent.setText(content);
                }

                @Override
                public void onFailure() {
                    doodleTitle.setText("");
                    doodleContent.setText("");
                }
            });
        }
    }

    @OnClick(R.id.submit)
    void onSubmitClick() {
        Doodle doodle = new Doodle("", doodleTitle.getText().toString(), doodleContent.getText().toString(), doodleUrl);

        doodleService.saveDoodle(doodle, new ServiceCallback<Void>() {
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

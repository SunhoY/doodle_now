package io.harry.doodlenow.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;
import io.harry.doodlenow.wrapper.JsoupWrapper;

public class DoodleActivity extends AppCompatActivity {
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
    }

    @OnClick(R.id.submit)
    void onSubmitClick() {
        doodleService.saveDoodle(new Doodle(doodleTitle.getText().toString(), doodleContent.getText().toString()), new ServiceCallback<Void>() {
            @Override
            public void onSuccess(Void item) {
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}

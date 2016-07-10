package io.harry.doodlenow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

    public static final String DOODLE_ID = "DOODLE_ID";
    private String doodleUrl;
    private String doodleId;
    private String doodleRevision;

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

        setTitleAndContentByContext(intent);
    }

    @OnClick(R.id.submit)
    void onSubmitClick() {
        Doodle doodle = new Doodle(
                doodleId,
                doodleRevision,
                doodleTitle.getText().toString(),
                doodleContent.getText().toString(),
                doodleUrl);

        ServiceCallback<Void> serviceCallback = new ServiceCallback<Void>() {
            @Override
            public void onSuccess(Void item) {
                Toast.makeText(DoodleActivity.this, "참 잘했어요! 엄지 척!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        };

        if(TextUtils.isEmpty(doodle._id)) {
            doodleService.saveDoodle(doodle, serviceCallback);
        }
        else {
            doodleService.updateDoodle(doodle, serviceCallback);
        }
    }

    public static Intent getIntent(Context context, String doodleId) {
        Intent intent = new Intent(context, DoodleActivity.class);
        intent.putExtra(DOODLE_ID, doodleId);

        return intent;
    }

    private void setTitleAndContentByContext(Intent intent) {
        if(Intent.ACTION_SEND.equals(intent.getAction())) {
            doodleId = "";
            doodleRevision = "";
            doodleUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
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
        } else {
            doodleId = intent.getStringExtra(DOODLE_ID);
            doodleService.getDoodle(doodleId, new ServiceCallback<Doodle>() {
                @Override
                public void onSuccess(Doodle item) {
                    doodleContent.setText(item.content);
                    doodleRevision = item._rev;
                    doodleTitle.setText(item.title);
                    doodleUrl = item.url;
                }

                @Override
                public void onFailure(String message) {

                }
            });
        }
    }
}

package io.harry.doodlenow.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

public class CreateDoodleActivity extends AppCompatActivity {
    @Inject
    DoodleService doodleService;

    @BindView(R.id.doodle_title)
    EditText doodleTitle;
    @BindView(R.id.doodle_content)
    EditText doodleContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doodle);

        ((DoodleApplication)getApplication()).getDoodleComponent().inject(this);

        ButterKnife.bind(this);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_save:
                saveDoodle();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveDoodle() {
        if(!validate(doodleTitle, doodleContent)) {
            Toast.makeText(this, "저장할 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        Doodle doodle = new Doodle(doodleTitle.getText().toString(),
                doodleContent.getText().toString(),
                "", "",
                DateTime.now().getMillis()
        );

        doodleService.saveDoodle(doodle, new ServiceCallback<Void>() {
            @Override
            public void onSuccess(Void item) {
                Toast.makeText(CreateDoodleActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(String message) {

            }
        });

    }

    private boolean validate(EditText doodleTitle, EditText doodleContent) {
        return !TextUtils.isEmpty(doodleTitle.getText()) && !TextUtils.isEmpty(doodleContent.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_doodle_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

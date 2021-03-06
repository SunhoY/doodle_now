package io.harry.doodlenow.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.firebase.FirebaseHelper;
import io.harry.doodlenow.firebase.FirebaseHelperWrapper;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.DoodleJson;

public class CreateDoodleActivity extends AppCompatActivity implements ValueEventListener{
    @Inject
    FirebaseHelperWrapper firebaseHelperWrapper;

    @BindView(R.id.doodle_title)
    EditText doodleTitle;
    @BindView(R.id.doodle_content)
    EditText doodleContent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.doodle_url)
    EditText doodleUrl;

    private FirebaseHelper firebaseHelper;
    private String createdDoodleKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doodle);

        ((DoodleApplication)getApplication()).getDoodleComponent().inject(this);

        firebaseHelper = firebaseHelperWrapper.getFirebaseHelper("doodles");

        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow_vector);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData primaryClip = clipboardManager.getPrimaryClip();

        if(primaryClip == null) {
            return;
        }

        String clippedData = primaryClip.getItemAt(0).getText().toString();

        if(URLUtil.isValidUrl(clippedData)) {
            doodleUrl.setText(clippedData);
        }
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
            Toast.makeText(this, R.string.unable_to_save, Toast.LENGTH_SHORT).show();
            return;
        }

        String doodleLink = doodleUrl.getText().toString();

        if(!URLUtil.isValidUrl(doodleLink)) {
            Toast.makeText(this, R.string.link_form_is_not_valid, Toast.LENGTH_SHORT).show();
            return;
        }

        Doodle doodle = new Doodle(doodleTitle.getText().toString(),
                doodleContent.getText().toString(),
                doodleLink, "",
                DateTime.now().getMillis()
        );

        createdDoodleKey = firebaseHelper.saveDoodle(new DoodleJson(doodle));
        firebaseHelper.addSingleValueChangeListener(createdDoodleKey, this);
    }

    private boolean validate(EditText doodleTitle, EditText doodleContent) {
        return !TextUtils.isEmpty(doodleTitle.getText()) && !TextUtils.isEmpty(doodleContent.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_doodle_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(!dataSnapshot.getKey().equals(createdDoodleKey)) {
            return;
        }

        Toast.makeText(CreateDoodleActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

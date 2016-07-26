package io.harry.doodlenow.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.firebase.FirebaseHelper;
import io.harry.doodlenow.firebase.FirebaseHelperWrapper;
import io.harry.doodlenow.model.Doodle;

public class CreateDoodleActivity extends AppCompatActivity implements ChildEventListener {
    @Inject
    FirebaseHelperWrapper firebaseHelperWrapper;

    @BindView(R.id.doodle_title)
    EditText doodleTitle;
    @BindView(R.id.doodle_content)
    EditText doodleContent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_doodle);

        ((DoodleApplication)getApplication()).getDoodleComponent().inject(this);

        firebaseHelper = firebaseHelperWrapper.getFirebaseHelper("doodles");
        firebaseHelper.addChildEventListener(this);

        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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

        firebaseHelper.saveDoodle(doodle);
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
        firebaseHelper.removeChildEventListener(this);
        super.onDestroy();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Toast.makeText(CreateDoodleActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

package io.harry.doodlenow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.wrapper.PicassoWrapper;

public class DoodleActivity extends AppCompatActivity {

    public static final String DOODLE = "doodle";

    @BindView(R.id.doodle_title)
    TextView doodleTitle;
    @BindView(R.id.hours_elapsed)
    TextView hoursElapsed;
    @BindView(R.id.doodle_content)
    TextView doodleContent;
    @BindView(R.id.doodle_image)
    ImageView doodleImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    PicassoWrapper picassoWrapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodle);

        ((DoodleApplication)getApplication()).getDoodleComponent().inject(this);

        ButterKnife.bind(this);

        Doodle doodle = (Doodle) getIntent().getSerializableExtra(DOODLE);

        setDoodle(doodle);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow_vector);
    }

    private void setDoodle(Doodle doodle) {
        doodleTitle.setText(doodle.getTitle());
        hoursElapsed.setText(doodle.getElapsedHours());
        doodleContent.setText(doodle.getContent());
        if(TextUtils.isEmpty(doodle.getImageUrl())) {
            picassoWrapper.getPicasso(this).load(R.drawable.main_logo).into(doodleImage);
        } else {
            picassoWrapper.getPicasso(this).load(doodle.getImageUrl()).into(doodleImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getIntent(Context context, Doodle doodle) {
        Intent intent = new Intent(context, DoodleActivity.class);
        intent.putExtra(DOODLE, doodle);

        return intent;
    }
}

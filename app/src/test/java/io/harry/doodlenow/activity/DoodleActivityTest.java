package io.harry.doodlenow.activity;

import android.content.Intent;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleActivityTest {
    private DoodleActivity subject;
    private EditText contents;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void onCreate_showsContentsInEditText() throws Exception {
        setupActivityWithSendIntent();

        assertThat(contents.getText().toString()).isEqualTo("hello friends");
    }

    private void setupActivityWithSendIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "hello friends");
        subject = Robolectric.buildActivity(DoodleActivity.class)
                .withIntent(intent)
                .create().get();

        setupViews();
    }

    private void setupViews() {
        contents = (EditText) subject.findViewById(R.id.contents);
    }
}
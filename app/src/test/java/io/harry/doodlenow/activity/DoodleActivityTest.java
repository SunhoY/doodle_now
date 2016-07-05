package io.harry.doodlenow.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.R;
import io.harry.doodlenow.TestDoodleApplication;
import io.harry.doodlenow.service.ContentService;
import io.harry.doodlenow.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleActivityTest {
    private DoodleActivity subject;
    private EditText contents;

    ContentService mockContentService;
    Button submit;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void onCreate_showsContentsInEditText() throws Exception {
        setupActivityWithSendIntent();

        assertThat(contents.getText().toString()).isEqualTo("hello friends");
    }

    @Test
    public void onSubmitClick_callsContentServiceToSave() throws Exception {
        submit.performClick();

        verify(mockContentService).saveContent(Matchers.<ServiceCallback<Void>>any());

    }

    private void setupActivityWithSendIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "hello friends");
        subject = Robolectric.buildActivity(DoodleActivity.class)
                .withIntent(intent)
                .create().get();

        setupViews();
        injectDependencies();
    }

    private void setupViews() {
        contents = (EditText) subject.findViewById(R.id.contents);
        submit = (Button) subject.findViewById(R.id.submit);
    }

    private void injectDependencies() {
        mockContentService = ((TestDoodleApplication) RuntimeEnvironment.application).getContentComponent().contentService();
    }
}
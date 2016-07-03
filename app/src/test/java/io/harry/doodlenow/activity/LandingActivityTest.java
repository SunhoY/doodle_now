package io.harry.doodlenow.activity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.service.ContentService;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LandingActivityTest {
    private LandingActivity subject;

    @Inject
    ContentService contentService;

    @Before
    public void setUp() throws Exception {



        subject = Robolectric.setupActivity(LandingActivity.class);

    }

    @Test
    public void onResume_callsContentServiceToGetContents() throws Exception {

    }
}
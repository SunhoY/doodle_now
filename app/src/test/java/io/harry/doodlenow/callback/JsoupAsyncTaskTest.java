package io.harry.doodlenow.callback;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.expect;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jsoup.class})
public class JsoupAsyncTaskTest {

    private JsoupAsyncTask subject;

    @Test
    public void constructor_transformUrl_whenUrlIsInvalid() throws Exception {
        mockStatic(Jsoup.class);
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.get()).thenReturn(mock(Document.class));
        when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
        expect(Jsoup.connect("https://overwatch.com/ko/kr")).andReturn(mockConnection);

        replay(Jsoup.class);
        subject = new JsoupAsyncTask("오버워치/nhttps://overwatch.com/ko/kr", mock(JsoupCallback.class));

        subject.doInBackground(null);
    }
}
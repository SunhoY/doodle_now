package io.harry.doodlenow.wrapper;

import android.content.Context;
import android.util.Log;

import com.cloudant.mazha.Document;
import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DatastoreManager;
import com.cloudant.sync.datastore.DatastoreNotCreatedException;
import com.cloudant.sync.datastore.DocumentBodyFactory;
import com.cloudant.sync.datastore.DocumentException;
import com.cloudant.sync.datastore.DocumentNotFoundException;
import com.cloudant.sync.datastore.DocumentRevision;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.harry.doodlenow.model.Doodle;

public class CloudantDoodleDatabase {
    public static final String DATASTORES = "datastores";
    private static final String TAG = "Doodle Database";
    private Datastore datastore;
    private Context context;

    public CloudantDoodleDatabase(Context context, String database) {
        this.context = context;

        File path = context.getDir(DATASTORES, Context.MODE_PRIVATE);
        DatastoreManager manager = new DatastoreManager(path.getAbsolutePath());
        try {
            datastore = manager.openDatastore(database);
        } catch (DatastoreNotCreatedException e) {
            Log.e(TAG, "data store is not initialized");
            e.printStackTrace();
        }
    }

    public String createDoodle(Doodle doodle) {
        DocumentRevision documentRevision = new DocumentRevision();

        Map<String, Object> document = new HashMap<>();
        document.put("content", doodle.content);
        document.put("title", doodle.title);
        document.put("createdAt", doodle.createdAt);
        document.put("url", doodle.url);

        documentRevision.setBody(DocumentBodyFactory.create(document));
        DocumentRevision result;
        try {
            result = datastore.createDocumentFromRevision(documentRevision);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }

        return result.getId();
    }

    public Doodle retrieveDoodle(String id) {
        DocumentRevision document;
        try {
            document = datastore.getDocument(id);
        } catch (DocumentNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        Map<String, Object> map = document.getBody().asMap();

        String documentId = (String) map.get("id");
        String revision = (String) map.get("rev");
        String title = (String) map.get("title");
        String content = (String) map.get("content");
        String url = (String) map.get("url");
        long createdAt = (long) map.get("createdAt");

        return new Doodle(id, revision, title, content, url, createdAt);
    }
}

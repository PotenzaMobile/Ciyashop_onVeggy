package com.potenza.onveggy.fcm;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.potenza.onveggy.utils.Constant;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;

import java.util.ArrayList;

public class AppIndexingUpdateService extends JobIntentService {

    // Job-ID must be unique across your whole app.
    private static final int UNIQUE_JOB_ID = 42;

    public static void enqueueWork(Context context) {
        enqueueWork(context, AppIndexingUpdateService.class, UNIQUE_JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // TODO Insert your Indexable objects — for potenza, the recipe notes look as follows:

        ArrayList<Indexable> indexableNotes = new ArrayList<>();

        Indexable noteToIndex = Indexables.noteDigitalDocumentBuilder()
                .setName(Constant.CATEGORYDETAIL.name + " Note")
                .setText(Constant.CATEGORYDETAIL.description + "")
                .setUrl(Constant.CATEGORYDETAIL.externalUrl + "")
                .build();

        indexableNotes.add(noteToIndex);

        if (indexableNotes.size() > 0) {
            Indexable[] notesArr = new Indexable[indexableNotes.size()];
            notesArr = indexableNotes.toArray(notesArr);

            // batch insert indexable notes into index
            FirebaseAppIndex.getInstance(getApplicationContext()).update(notesArr);
        }
    }

    // ...
}

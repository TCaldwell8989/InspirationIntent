package com.tyler.inspirationintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.tyler.inspirationintent.Inspiration;
import com.tyler.inspirationintent.database.InspirationDbSchema.InspirationTable;

import java.util.UUID;

public class InspirationCursorWrapper extends CursorWrapper {
    public InspirationCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Inspiration getInspiration() {
        String uuidString = getString(getColumnIndex(InspirationTable.Cols.UUID));
        String note = getString(getColumnIndex(InspirationTable.Cols.NOTE));

        Inspiration inspiration = new Inspiration(UUID.fromString(uuidString));
        inspiration.setNote(note);

        return inspiration;
    }
}



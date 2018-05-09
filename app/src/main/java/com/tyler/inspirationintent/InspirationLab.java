package com.tyler.inspirationintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tyler.inspirationintent.database.InspirationBaseHelper;
import com.tyler.inspirationintent.database.InspirationCursorWrapper;
import com.tyler.inspirationintent.database.InspirationDbSchema.InspirationTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Class handling data and database management
public class InspirationLab {
    private static InspirationLab sInspirationLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static InspirationLab get(Context context) {
        if (sInspirationLab == null) {
            sInspirationLab = new InspirationLab(context);
        }
        return sInspirationLab;
    }

    private InspirationLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new InspirationBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addInspiration(Inspiration i) {
        ContentValues values = getContentValues(i);

        mDatabase.insert(InspirationTable.NAME, null, values);
    }

    public boolean deleteInspiration(Inspiration i) {
        String where = InspirationTable.Cols.UUID + " = ? ";
        String[] whereArgs = { i.getId().toString() };
        int rowsDeleted = mDatabase.delete(InspirationTable.NAME, where, whereArgs);

        if (rowsDeleted == 1) {
            return true;
        }

        return false;
    }

    public List<Inspiration> getInspirations() {
        List<Inspiration> inspirations = new ArrayList<>();
        InspirationCursorWrapper cursor = queryInspirations(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                inspirations.add(cursor.getInspiration());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return inspirations;
    }

    public List<Inspiration> getInspirations(String where, String[] whereArgs) {
        List<Inspiration> inspirations = new ArrayList<>();
        InspirationCursorWrapper cursor = queryInspirations(where, whereArgs);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                inspirations.add(cursor.getInspiration());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return inspirations;
    }

    public Inspiration getInspiration(UUID id) {
        InspirationCursorWrapper cursor = queryInspirations(
                InspirationTable.Cols.UUID + " = ?",
                new String[] { id.toString() });

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getInspiration();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Inspiration inspiration) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, inspiration.getPhotoFilename());
    }

    public void updateInspiration(Inspiration inspiration) {
        String uuidString = inspiration.getId().toString();
        ContentValues values = getContentValues(inspiration);

        mDatabase.update(InspirationTable.NAME, values,
                InspirationTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private InspirationCursorWrapper queryInspirations(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                InspirationTable.NAME, null, where, whereArgs,
                null, null, InspirationTable.Cols.CREATED + " DESC");
        return new InspirationCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Inspiration inspiration) {
        ContentValues values = new ContentValues();
        values.put(InspirationTable.Cols.UUID, inspiration.getId().toString());
        values.put(InspirationTable.Cols.NOTE, inspiration.getNote());

        return values;
    }
}

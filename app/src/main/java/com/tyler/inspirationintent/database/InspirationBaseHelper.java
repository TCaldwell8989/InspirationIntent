package com.tyler.inspirationintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tyler.inspirationintent.Inspiration;
import com.tyler.inspirationintent.database.InspirationDbSchema.InspirationTable;

public class InspirationBaseHelper extends SQLiteOpenHelper {

    // Constans for DB name and version
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "inspirationDataBase.db";

    public InspirationBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Create Table and Columns
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + InspirationTable.NAME + "(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InspirationTable.Cols.UUID + ", " +
                InspirationTable.Cols.NOTE + " TEXT, " +
                InspirationTable.Cols.CREATED + " TEXT default CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}

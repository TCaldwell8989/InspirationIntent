package com.tyler.surveysqlite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tyler.surveysqlite.database.SurveyDbSchema.SurveyTable;

public class SurveyBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "surveyDataBase.db";

    public SurveyBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SurveyTable.NAME + "(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SurveyTable.Cols.UUID + ", " +
                SurveyTable.Cols.QUESTION + " TEXT, " +
                SurveyTable.Cols.FIRST_RESPONSE + " TEXT, " +
                SurveyTable.Cols.SECOND_RESPONSE + " TEXT, " +
                SurveyTable.Cols.FIRST_SCORE + " INTEGER, " +
                SurveyTable.Cols.SECOND_SCORE + " INEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}

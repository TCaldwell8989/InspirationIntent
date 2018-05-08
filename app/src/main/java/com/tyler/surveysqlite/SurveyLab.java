package com.tyler.surveysqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tyler.surveysqlite.database.SurveyBaseHelper;
import com.tyler.surveysqlite.database.SurveyCursorWrapper;
import com.tyler.surveysqlite.database.SurveyDbSchema;
import com.tyler.surveysqlite.database.SurveyDbSchema.SurveyTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SurveyLab {
    private static SurveyLab sSurveyLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static SurveyLab get(Context context) {
        if (sSurveyLab == null) {
            sSurveyLab = new SurveyLab(context);
        }
        return sSurveyLab;
    }

    private SurveyLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new SurveyBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addSurvey(Survey s) {
        ContentValues values = getContentValues(s);

        mDatabase.insert(SurveyTable.NAME, null, values);
    }

    public List<Survey> getSurveys() {
        List<Survey> surveys = new ArrayList<>();
        SurveyCursorWrapper cursor = querySurveys(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                surveys.add(cursor.getSurvey());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return surveys;
    }

    public Survey getSurvey(UUID id) {
        SurveyCursorWrapper cursor = querySurveys(
                SurveyTable.Cols.UUID + " = ?",
                new String[] { id.toString() });

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getSurvey();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Survey survey) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, survey.getPhotoFilename());
    }

    public void updateSurvey(Survey survey) {
        String uuidString = survey.getId().toString();
        ContentValues values = getContentValues(survey);

        mDatabase.update(SurveyTable.NAME, values,
                SurveyTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private SurveyCursorWrapper querySurveys(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                SurveyTable.NAME, null, where, whereArgs,
                null, null, null);
        return new SurveyCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Survey survey) {
        ContentValues values = new ContentValues();
        values.put(SurveyTable.Cols.UUID, survey.getId().toString());
        values.put(SurveyTable.Cols.QUESTION, survey.getQuestion());
        values.put(SurveyTable.Cols.FIRST_RESPONSE, survey.getFirst_response());
        values.put(SurveyTable.Cols.SECOND_RESPONSE, survey.getSecond_response());
        values.put(SurveyTable.Cols.FIRST_SCORE, survey.getFirst_score());
        values.put(SurveyTable.Cols.SECOND_SCORE, survey.getSecond_score());

        return values;
    }
}

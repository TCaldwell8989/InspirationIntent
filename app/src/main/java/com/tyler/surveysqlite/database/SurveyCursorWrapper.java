package com.tyler.surveysqlite.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.tyler.surveysqlite.Survey;
import com.tyler.surveysqlite.database.SurveyDbSchema.SurveyTable;

import java.util.UUID;

public class SurveyCursorWrapper extends CursorWrapper {
    public SurveyCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Survey getSurvey() {
        String uuidString = getString(getColumnIndex(SurveyTable.Cols.UUID));
        String question = getString(getColumnIndex(SurveyTable.Cols.QUESTION));
        String first_response = getString(getColumnIndex(SurveyTable.Cols.FIRST_RESPONSE));
        String second_response = getString(getColumnIndex(SurveyTable.Cols.SECOND_RESPONSE));
        int first_score = getInt(getColumnIndex(SurveyTable.Cols.FIRST_SCORE));
        int second_score = getInt(getColumnIndex(SurveyTable.Cols.SECOND_SCORE));

        Survey survey = new Survey(UUID.fromString(uuidString));
        survey.setQuestion(question);
        survey.setFirst_response(first_response);
        survey.setSecond_response(second_response);
        survey.setFirst_score(first_score);
        survey.setSecond_score(second_score);

        return survey;
    }
}



package com.tyler.surveysqlite.database;

public class SurveyDbSchema {
    public static final class SurveyTable {
        public static final String NAME = "surveys";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String QUESTION = "question";
            public static final String FIRST_RESPONSE = "first_response";
            public static final String SECOND_RESPONSE = "second_response";
            public static final String FIRST_SCORE = "first_score";
            public static final String SECOND_SCORE = "second_score";
        }
    }
}

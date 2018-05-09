package com.tyler.inspirationintent.database;

// Class containing constants for identifying table and columns
public class InspirationDbSchema {
    public static final class InspirationTable {
        public static final String NAME = "inspirations";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NOTE = "note";
            public static final String CREATED = "created";
        }
    }
}

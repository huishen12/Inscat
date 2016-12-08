package com.spacebunny.hshen.inscat.database;

import android.provider.BaseColumns;

public final class DBReaderContract {

    private DBReaderContract() {}

        public static class DBEntry implements BaseColumns {
            public static final String DATABASE_NAME = "PostCategoryReader.db";
            public static final String CATEGORY_TABLE_NAME = "category";
            public static final String CATEGORY_COLUMN_ID = "_id";
            public static final String CATEGORY_COLUMN_NAME = "name";
            public static final String CATEGORY_COLUMN_DESCRIPTION = "description";
            public static final String CATEGORY_COLUMN_POST_COUNT = "post_count";
            public static final String CATEGORY_COLUMN_CREATED_TS = "created_ts";
            public static final String CATEGORY_COLUMN_USER_ID = "user_id";

            public static final String POST_CATEGORY_TABLE_NAME = "postcategory";
            public static final String POST_CATEGORY_COLUMN_ID = "_id";
            public static final String POST_CATEGORY_COLUMN_POST_ID = "post_id";
            public static final String POST_CATEGORY_COLUMN_CATEGORY_ID = "category_id";
            public static final String POST_CATEGORY_COLUMN_USER_ID = "user_id";
            public static final String POST_CATEGORY_COLUMN_JSON = "post_json";

            public static final String POST_TABLE_NAME = "post";
            public static final String POST_COLUMN_ID = "_id";
            public static final String POST_COLUMN_POST_ID = "post_id";
            public static final String POST_COLUMN_CATEGORY_COUNT = "category_count";
        }
}

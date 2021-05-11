package com.academia.gorillas.helper;

import android.provider.BaseColumns;

public class TablesInfo {



    public static final class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POST_ID = "post_id";
        public static final String COLUMN_TITLE = "post_title";
        public static final String COLUMN_IMAGE = "post_image";
        public static final String COLUMN_CONTENT= "post_content";
        public static final String COLUMN_EXCERPT = "post_excerpt";
        public static final String COLUMN_LINK = "post_link";
        public static final String COLUMN_DATE = "post_date";
    }

}

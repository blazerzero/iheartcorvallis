package edu.oregonstate.studentlife.ihcv2.data;

import android.provider.BaseColumns;

/**
 * Created by Omeed on 4/16/18.
 */

public class IHCDBContract {

    private IHCDBContract() {}

    public static class SavedImages implements BaseColumns {
        public static final String TABLE_NAME = "savedImages";
        public static final String COLUMN_IMAGE = "Image";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}

package edu.oregonstate.studentlife.ihcv2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Omeed on 4/16/18.
 */

public class IHCDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ihc_images.db";
    private static int DATABASE_VERSION = 1;

    public IHCDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SAVED_IMAGES_TABLE =
                "CREATE TABLE " + IHCDBContract.SavedImages.TABLE_NAME + "(" +
                        IHCDBContract.SavedImages._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        IHCDBContract.SavedImages.COLUMN_IMAGE + " TEXT NOT NULL, " +
                        IHCDBContract.SavedImages.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ");";
        db.execSQL(SQL_CREATE_SAVED_IMAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + IHCDBContract.SavedImages.TABLE_NAME + ";");
        onCreate(db);
    }
}

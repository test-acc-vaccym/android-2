package com.edroplet.qxx.saneteltabactivity.beans;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by qxs on 2017/9/24.
 */

public class UsefulDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "sanetel.db";
    private static final int DATABASE_VERSION = 1;

    public UsefulDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getEmployees() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"0 _id", "FullName"};
        String sqlTables = "Employees";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        c.moveToFirst();
        return c;

    }
}

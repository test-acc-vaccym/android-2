package top.edroplet.encdec.utils.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xw on 2017/5/2.
 */

public class StepCounterSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "step_table";
    public static final String ID = "id";
    public static final String STEP = "step";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_DATE = "upTime";

    public StepCounterSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " +
                TABLE_NAME + "(" //创建数据库表
                + ID + " integer primary key,"
                + STEP + " integer"
                + CREATE_TIME + " DATETIME "
                + UPDATE_DATE + " DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')) "
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

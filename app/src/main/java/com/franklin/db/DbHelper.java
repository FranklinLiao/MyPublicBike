package com.franklin.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Franklin on 2015/8/12.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String dbName = "Site";
    private static DbHelper db = null;
    private String createTableSql = "CREATE TABLE if not exists site (id integer primary key autoincrement,name varchar(30),code varchar(10),address varchar(30),lon numeric(10,6),lat numeric(9,6),num integer,quyu varchar(10))";

    private DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private DbHelper(Context context) {
        this(context, dbName, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static SQLiteDatabase getDb(Context ctx) {
        if(db==null) {
            db = new DbHelper(ctx);
        }
        return db.getWritableDatabase();
    }
    public static void closeDb() {
        if(db!=null) {
            db.close();
        }
    }
}

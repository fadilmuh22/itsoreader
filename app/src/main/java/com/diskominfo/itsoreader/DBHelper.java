//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.diskominfo.itsoreader;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DBNAME = "transaction.db";
    private static final int VERSION = 4;
    private static final String TABLE_NAME = "history";
    private static final String TABLE_NAME1 = "konfigurasi";
    private static final String TABLE_NAME2 = "admin";
    private static final String TABLE_NAME3 = "sam";
    public static final String ID = "_id";
    public static final String id = "id";
    public static final String Datetime = "datetime";
    public static final String KodePerusahaan = "kodePerusahaan";
    public static final String NamaPerusahaan = "namaPerusahaan";
    public static final String NIK = "nik";
    public static final String RESPONSE_CODE = "responseCode";
    public static final String RESPONSE_DESC = "responseDescription";
    public static final String ACTIVE_CARD = "activeCard";
    public static final String TYPE_AUTH = "typeAuth";
    public static final String FINGER_AUTH = "fingerAuth";
    public static final String NAME_ADMIN = "nameAdmin";
    public static final String NIK_ADMIN = "nikAdmin";
    public static final String READ_STATUS = "readStatus";
    public static final String SEND_STATUS = "sendStatus";
    public static final String PROTOKOL = "protokol";
    public static final String IP_ADDR = "ipaddress";
    public static final String PORT = "port";
    public static final String NAME = "name";
    public static final String MINU1 = "minutea1";
    public static final String MINU2 = "minutea2";
    public static final String pcid = "pccid";
    public static final String config = "config";
    private SQLiteDatabase myDB;

    public DBHelper(Context context) {
        super(context, "/storage/emulated/0/transaction.db", (SQLiteDatabase.CursorFactory)null, 4);
    }

    public void onCreate(SQLiteDatabase db) {
        String queryTable = "CREATE TABLE IF NOT EXISTS history(_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ,datetime DATETIME ,kodePerusahaan STRING ,namaPerusahaan STRING, nik STRING ,responseCode STRING ,responseDescription STRING ,activeCard STRING ,typeAuth STRING ,fingerAuth INTEGER, nameAdmin STRING, nikAdmin STRING , readStatus STRING, sendStatus STRING )";
        db.execSQL(queryTable);
        String queryadmin = "CREATE TABLE IF NOT EXISTS admin(id INTEGER ,nikAdmin STRING ,nameAdmin STRING ,minutea1 TEXT ,minutea2 TEXT )";
        db.execSQL(queryadmin);
        String querykonfigurasi = "CREATE TABLE IF NOT EXISTS konfigurasi(protokol STRING ,ipaddress STRING ,port STRING )";
        db.execSQL(querykonfigurasi);
        String querysam = "CREATE TABLE IF NOT EXISTS sam(pccid STRING ,config STRING )";
        db.execSQL(querysam);
        db.execSQL("CREATE TABLE IF NOT EXISTS identity (sn STRING, pn STRING, code STRING, sam STRING);");
        db.execSQL("INSERT INTO identity (sn, pn, code, sam) VALUES ('', '', '', '');");
        db.execSQL("INSERT INTO admin (id, nikAdmin, nameAdmin, minutea1, minutea2) VALUES (1, '', '', '', '');");
        db.execSQL("INSERT INTO admin (id, nikAdmin, nameAdmin, minutea1, minutea2) VALUES (2, '', '', '', '');");
        db.execSQL("INSERT INTO admin (id, nikAdmin, nameAdmin, minutea1, minutea2) VALUES (3, '', '', '', '');");
        db.execSQL("INSERT INTO admin (id, nikAdmin, nameAdmin, minutea1, minutea2) VALUES (4, '', '', '', '');");
        db.execSQL("INSERT INTO admin (id, nikAdmin, nameAdmin, minutea1, minutea2) VALUES (5, '', '', '', '');");
        db.execSQL("INSERT INTO admin (id, nikAdmin, nameAdmin, minutea1, minutea2) VALUES (6, '', '', '', '');");
        db.execSQL("INSERT INTO admin (id, nikAdmin, nameAdmin, minutea1, minutea2) VALUES (7, '', '', '', '');");
        db.execSQL("INSERT INTO admin (id, nikAdmin, nameAdmin, minutea1, minutea2) VALUES (8, '', '', '', '');");
        db.execSQL("INSERT INTO admin (id, nikAdmin, nameAdmin, minutea1, minutea2) VALUES (9, '', '', '', '');");
        db.execSQL("INSERT INTO admin (id, nikAdmin, nameAdmin, minutea1, minutea2) VALUES (10, '', '', '', '');");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS history");
        db.execSQL("DROP TABLE IF EXISTS konfigurasi");
        db.execSQL("DROP TABLE IF EXISTS admin");
        db.execSQL("DROP TABLE IF EXISTS sam");
        this.onCreate(db);
    }

    public void openDB() {
        this.myDB = this.getWritableDatabase();
    }

    public void closeDB() {
        if (this.myDB != null && this.myDB.isOpen()) {
            this.myDB.close();
        }

    }

    public void insert(String date, String nik, String responsecode, String responsedesc, String activecard, String typeauth, String fingerAuth, String nameadmin, String nikadmin, String readstatus, String sendstatus) {
        String query = "INSERT INTO history VALUES(null, '" + date + "', '001' , ' PT.Industri Telekomunikasi Indonesia ', '" + nik + "', '" + responsecode + "', '" + responsedesc + "', '" + activecard + "', '" + typeauth + "', '" + fingerAuth + "', '" + nameadmin + "', '" + nikadmin + "', '" + readstatus + "', '" + sendstatus + "');";

        try {
            Log.d("SQL_INSERT =", query);
            this.myDB.execSQL(query);
        } catch (SQLException var14) {
            SQLException e = var14;
            e.printStackTrace();
        }

    }

    public void insertAdmin(String nik, String name, String minu1, String minu2) {
        String query = "INSERT INTO admin VALUES(null, '" + nik + "', '" + name + "', '" + minu1 + "', '" + minu2 + "');";

        try {
            Log.d("SQL_INSERT =", query);
            this.myDB.execSQL(query);
        } catch (SQLException var7) {
            SQLException e = var7;
            e.printStackTrace();
        }

    }

    public void insertKonfigurasi(String protokol, String ipaddr, String port) {
        String query = "INSERT INTO konfigurasi VALUES('" + protokol + "', '" + ipaddr + "', '" + port + "');";

        try {
            Log.d("SQL_INSERT =", query);
            this.myDB.execSQL(query);
        } catch (SQLException var6) {
            SQLException e = var6;
            e.printStackTrace();
        }

    }

    public void setSAM(String pccid, String config) {
        String query = "INSERT INTO sam VALUES('" + pccid + "' , ' " + config + "');";

        try {
            Log.d("SQL_INSERT =", query);
            this.myDB.execSQL(query);
        } catch (SQLException var5) {
            SQLException e = var5;
            e.printStackTrace();
        }

    }

    public Cursor getAllRecords() {
        String query = "SELECT _id ,datetime, nik,typeAuth,fingerAuth, nikAdmin , nameAdmin, kodePerusahaan, namaPerusahaan   FROM history ORDER BY _id ASC";
        return this.myDB.rawQuery(query, (String[])null);
    }

    public Cursor getAdmin() {
        String query = "SELECT * FROM admin ORDER BY _id ASC LIMIT 10";
        return this.myDB.rawQuery(query, (String[])null);
    }

    public Cursor getsam() {
        String query = "SELECT * FROM sam ";
        return this.myDB.rawQuery(query, (String[])null);
    }

    public Cursor getKonfigurasi() {
        String query = "SELECT * FROM konfigurasi ";
        return this.myDB.rawQuery(query, (String[])null);
    }

    @SuppressLint("Range")
    public ArrayList<String> readIdentity() {
        ArrayList<String> identity = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM identity", (String[])null);
        if (mCursor != null) {
            mCursor.moveToFirst();

            while(!mCursor.isAfterLast()) {
                identity.add(0, mCursor.getString(mCursor.getColumnIndex("pn")));
                identity.add(1, mCursor.getString(mCursor.getColumnIndex("sn")));
                identity.add(2, mCursor.getString(mCursor.getColumnIndex("code")));
                identity.add(3, mCursor.getString(mCursor.getColumnIndex("sam")));
                mCursor.moveToNext();
            }
        }

        return identity;
    }

    public void setConfig(Integer port, Integer tcp, Integer fp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("port", port);
        contentValues.put("tcp", tcp);
        contentValues.put("fp", fp);
        db.update("config", contentValues, (String)null, (String[])null);
    }

    @SuppressLint("Range")
    public ArrayList<String> readConfig() {
        ArrayList<String> config = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM konfigurasi", (String[])null);
        if (mCursor != null) {
            mCursor.moveToFirst();

            while(!mCursor.isAfterLast()) {
                config.add(0, mCursor.getString(mCursor.getColumnIndex("protokol")));
                config.add(1, mCursor.getString(mCursor.getColumnIndex("ipaddress")));
                config.add(2, mCursor.getString(mCursor.getColumnIndex("port")));
                mCursor.moveToNext();
            }
        }

        return config;
    }

    public void setAdmin(Integer id, String nik, String name, String minutea1, String minutea2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nikAdmin", nik);
        contentValues.put("nameAdmin", name);
        contentValues.put("minutea1", minutea1);
        contentValues.put("minutea2", minutea2);
        Log.d("tag", "nik: " + nik + "nama : " + name + " minutea1 : " + minutea1 + " minutea2: " + minutea2);
        db.update("admin", contentValues, "id = ? ", new String[]{Integer.toString(id)});
    }

    public void removeAdmin(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nikAdmin", "");
        contentValues.put("nameAdmin", "");
        contentValues.put("minutea1", "");
        contentValues.put("minutea2", "");
        db.update("admin", contentValues, "id = ? ", new String[]{Integer.toString(id)});
    }

    @SuppressLint("Range")
    public ArrayList<String> readAdmin(Integer id) {
        ArrayList<String> admin = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM admin WHERE id = " + id;
        Cursor mCursor = db.rawQuery(query, (String[])null);
        if (mCursor != null) {
            mCursor.moveToFirst();

            while(!mCursor.isAfterLast()) {
                admin.add(0, mCursor.getString(mCursor.getColumnIndex("nikAdmin")));
                admin.add(1, mCursor.getString(mCursor.getColumnIndex("nameAdmin")));
                admin.add(2, mCursor.getString(mCursor.getColumnIndex("minutea1")));
                admin.add(3, mCursor.getString(mCursor.getColumnIndex("minutea2")));
                mCursor.moveToNext();
            }
        }

        return admin;
    }

    public void setIdentity(String sn, String pn, String code, String sam) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sn", sn);
        contentValues.put("pn", pn);
        contentValues.put("code", code);
        contentValues.put("sam", sam);
        db.update("identity", contentValues, (String)null, (String[])null);
    }

    @SuppressLint("Range")
    public ArrayList<String> readSAM() {
        ArrayList<String> sam = new ArrayList();
        String query = "SELECT * FROM sam ";
        this.myDB = this.getReadableDatabase();
        Cursor mCursor = this.myDB.rawQuery(query, (String[])null);
        if (mCursor != null) {
            mCursor.moveToFirst();

            while(!mCursor.isAfterLast()) {
                sam.add(0, mCursor.getString(mCursor.getColumnIndex("pccid")));
                sam.add(1, mCursor.getString(mCursor.getColumnIndex("config")));
                mCursor.moveToNext();
            }
        }

        return sam;
    }
}

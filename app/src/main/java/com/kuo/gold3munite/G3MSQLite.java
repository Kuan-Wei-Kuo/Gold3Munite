package com.kuo.gold3munite;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 2015/4/3.
 */
public class G3MSQLite extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "MySQLite.db";
    private static String DB_PATH = null;

    private static Context context = null;
    private SQLiteDatabase db = null;
    private static String MATH_TABLE_NAME = "math_table";
    private static String PHYSICS_TABLE_NAME = "physics_table";
    private static String ENG_TABLE_NAME = "eng_table";
    private static String OLD_ENG_TABLE_NAME = "old_eng_table";
    private static String OLD_MATH_TABLE_NAME = "old_math_table";
    private static String OLD_PHYSICS_TABLE_NAME = "old_physics_table";
    private static String STATISTICS_DATA_TABLE_NAME = "statistics_data";
    private static String _ID = "_id";
    private static String SCIENCE_NAME = "name_f";
    private static String SCIENCE_K = "science_k";
    private static String SCIENCE_Q = "science_q";
    private static String SCIENCE_F = "science_f";
    private static String SCIENCE_P = "science_p";
    private static String SCIENCE_A = "science_a";
    private static String CLIENT_A = "client_a";
    private static String ENG_WORD = "eng_word";
    private static String ENG_PT = "eng_pt";
    private static String CNI_WORD = "cni_word";
    private static String CNI_EX01 = "cni_ex01";
    private static String CNI_EX02 = "cni_ex02";
    private static String CNI_EX03 = "cni_ex03";
    private static String CNI_EX04 = "cni_ex04";
    private static String CNI_EX05 = "cni_ex05";
    private static String ENG_EX01 = "eng_ex01";
    private static String ENG_EX02 = "eng_ex02";
    private static String ENG_EX03 = "eng_ex03";
    private static String ENG_EX04 = "eng_ex04";
    private static String ENG_EX05 = "eng_ex05";
    private static String TAG = "tag";
    private static String TAG_ID="tag_id";
    private static String DATA_TIME = "data_time";
    private static String KIND = "kind";

    public G3MSQLite(Context context) {
        super(context, DB_NAME, null, VERSION);
        G3MSQLite.context = context;
        //DB_PATH="/data/data/com.golsql/databases/";
        DB_PATH = context.getFilesDir().getAbsolutePath() +"/";
        executeAssetsDB();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        executeAssetsDB();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    private static void  executeAssetsDB(){
        if(checkDbExists()==false)
            try {
                CopyDB();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static Boolean checkDbExists(){
        return new File(DB_PATH+DB_NAME).exists();
    }

    public static void CopyDB() throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream in = assetManager.open(DB_NAME);
        OutputStream out = new FileOutputStream(DB_PATH+DB_NAME);
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
        in.close();
        out.flush();
        out.close();
    }

    public void OpenDB() throws SQLException {
        this.db = SQLiteDatabase.openDatabase(DB_PATH+DB_NAME
                , null,SQLiteDatabase.OPEN_READWRITE);
    }
    public void CloseDB() {
        if(db != null)
            db.close();
    }

    public long Update(int tag, String table, long id){
        ContentValues values = new ContentValues();
        values.put(TAG, tag);
        return db.update(table, values,  _ID + "=" + id, null);
    }

    public long append(int id, String table){
        ContentValues values = new ContentValues();
        values.put(TAG_ID, id);
        return db.insert(table, null, values);
    }

    public Cursor getEnglish(){
        return db.query(ENG_TABLE_NAME, new String[] {_ID, ENG_WORD,  ENG_PT, CNI_WORD, CNI_EX01, ENG_EX01, CNI_EX02, ENG_EX02, CNI_EX03, ENG_EX03, CNI_EX04, ENG_EX04, CNI_EX05, ENG_EX05, TAG}, null, null, null, null, null);
    }

    public Cursor eng_get(long id, int choes) throws SQLException {
        String table = null;
        if(choes==1){
            Cursor cursor = db.query(ENG_TABLE_NAME,
                    new String[] {_ID, ENG_WORD,  ENG_PT, CNI_WORD, CNI_EX01, ENG_EX01,
                            CNI_EX02, ENG_EX02, CNI_EX03, ENG_EX03, CNI_EX04, ENG_EX04, CNI_EX05, ENG_EX05, TAG},
                    _ID +"=" + id, null, null, null, null,null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            return cursor;
        }
        else {
            Cursor cursor = db.query(OLD_ENG_TABLE_NAME,
                    new String[] {_ID, TAG_ID},
                    _ID +"=" + id, null, null, null, null,null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            return cursor;
        }
    }

    public Cursor science_get(long id, int choes) throws SQLException {
        String table = null;
        if(choes == 1){
            table = MATH_TABLE_NAME;
        }
        else{
            table = PHYSICS_TABLE_NAME;
        }
        Cursor cursor = db.query(table,
                new String[] {_ID, SCIENCE_NAME , SCIENCE_K, SCIENCE_F, SCIENCE_Q,
                        SCIENCE_P, SCIENCE_A, CLIENT_A, TAG},
                _ID +"=" + id, null, null, null, null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor old_science_get(long id, int choes) throws SQLException {
        String table = null;
        if(choes == 1){
            table = OLD_MATH_TABLE_NAME;
        }
        else{
            table = OLD_PHYSICS_TABLE_NAME;
        }
        Cursor cursor = db.query(table,
                new String[] {_ID, TAG_ID},_ID +"=" + id, null, null, null, null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public long set_statistics_data(String kind){
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date mDate = new Date(System.currentTimeMillis());
        String dateNow = mSimpleDateFormat.format(mDate);
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(DATA_TIME, dateNow);
        mContentValues.put(KIND, kind);
        return db.insert(STATISTICS_DATA_TABLE_NAME, null, mContentValues);
    }

    public Cursor get_statisics(long id) throws SQLException{
        Cursor mCursor = db.query(STATISTICS_DATA_TABLE_NAME, new String[] {_ID, DATA_TIME, KIND}, _ID + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int maxID(int choes){
        String query = null;
        if(choes == 1)
            query = "SELECT MAX(_id) AS max_id FROM eng_table";
        else if(choes == 2)
            query = "SELECT MAX(_id) AS max_id FROM math_table";
        else if(choes == 3)
            query = "SELECT MAX(_id) AS max_id FROM physics_table";
        else if(choes == 4)
            query = "SELECT MAX(_id) AS max_id FROM old_eng_table";
        else if(choes == 5)
            query = "SELECT MAX(_id) AS max_id FROM old_math_table";
        else if(choes == 6)
            query = "SELECT MAX(_id) AS max_id FROM statistics_data";
        else
            query = "SELECT MAX(_id) AS max_id FROM old_physics_table";
        Cursor cursor = db.rawQuery(query, null);
        int id = 0;
        if (cursor.moveToFirst())
        {
            do
            {
                id = cursor.getInt(0);
            } while(cursor.moveToNext());
        }
        return id;
    }
}

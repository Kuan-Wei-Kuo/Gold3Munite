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

    public static final int MATH = 0;
    public static final int PHYSICS = 1;
    public static final String MATH_FORMULA_URL = "math_formula_";
    public static final String MATH_QUESTION_URL = "math_question_";
    public static final String MATH_ANSWER_URL = "math_answer_";
    public static final String PHYSICS_FORMULA_URL = "physics_f";

    private static final int VERSION = 1;
    private static final String DB_NAME = "Gold3Munite.db";
    private static final String MATH_TABLE_NAME = "math_table";
    private static final String PHYSICS_TABLE_NAME = "physics_table";
    private static final String ENG_TABLE_NAME = "english_table";
    private static final String STATISTICS_DATA_TABLE_NAME = "statisics_table";

    private static final String _ID = "_id";
    private static final String FORMULA_NAME = "formulaName";
    private static final String ANSWER_CLIENT = "answerClient";
    private static final String ENG_WORD = "eng_word";
    private static final String ENG_PT = "eng_pt";
    private static final String CNI_WORD = "cni_word";
    private static final String CNI_EX01 = "cni_ex01";
    private static final String CNI_EX02 = "cni_ex02";
    private static final String ENG_EX01 = "eng_ex01";
    private static final String ENG_EX02 = "eng_ex02";
    private static final String STATE = "state";
    private static final String TAG_ID="tag_id";
    private static final String DATE = "date";
    private static final String KIND = "kind";
    private static final String TYPE = "type";
    private static final String TIMER_TEXT = "timerText";
    private static final String TIMER_TYPE_TEXT = "timerTypeText";
    private static final String TYPE_TEXT = "typeText";
    private static final String WEEK_TEXT = "weekText";
    private static final String SHOCK_STATE = "shockState";
    private static final String SOUND_STATE = "soundState";
    private static final String INTERVAL_TIME = "intervalTime";

    private static String DB_PATH = null;
    private static Context context = null;
    private SQLiteDatabase db = null;

    public G3MSQLite(Context context) {
        super(context, DB_NAME, null, VERSION);
        G3MSQLite.context = context;
        //DB_PATH="/data/data/com.golsql/databases/";
        DB_PATH = context.getFilesDir().getAbsolutePath() +"/";
        executeAssetsDB();
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

    public Cursor getEnglish(){
        return db.query(ENG_TABLE_NAME, new String[] {_ID, ENG_WORD,  ENG_PT, CNI_WORD, CNI_EX01, ENG_EX01, CNI_EX02, ENG_EX02, STATE}, null, null, null, null, null);
    }

    public Cursor getEnglish(long id){
            Cursor cursor = db.query(ENG_TABLE_NAME,
                    new String[] {_ID, ENG_WORD,  ENG_PT, CNI_WORD, CNI_EX01, ENG_EX01,
                            CNI_EX02, ENG_EX02, STATE},
                    _ID +"=" + id, null, null, null, null,null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            return cursor;
    }

    public Cursor getScience(int TYPE){
        String table = null;
        if(TYPE == MATH){
            return db.query(MATH_TABLE_NAME, new String[] {_ID, FORMULA_NAME , ANSWER_CLIENT, STATE}, null, null, null, null, null);
        }
        else{
            return db.query(PHYSICS_TABLE_NAME, new String[] {_ID, FORMULA_NAME , ANSWER_CLIENT, STATE}, null, null, null, null, null);
        }
    }

    public Cursor getScience(long id, int TYPE){
        String table = null;
        if(TYPE == MATH){
            Cursor cursor = db.query(MATH_TABLE_NAME, new String[] {_ID, FORMULA_NAME, ANSWER_CLIENT, STATE}, _ID +"=" + id, null, null, null, null,null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            return cursor;
        }
        else{
            Cursor cursor = db.query(PHYSICS_TABLE_NAME, new String[] {_ID, FORMULA_NAME, ANSWER_CLIENT, STATE}, _ID +"=" + id, null, null, null, null,null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            return cursor;
        }
    }

    public long insterStatisics(String date, String kind, String type){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, date);
        contentValues.put(KIND, kind);
        contentValues.put(TYPE, type);
        return db.insert(STATISTICS_DATA_TABLE_NAME, null, contentValues);
    }
    public Cursor getStatisics(){
        return db.query(STATISTICS_DATA_TABLE_NAME, new String[] {_ID, DATE, KIND, TYPE}, null, null, null, null, null, null);
    }

    public Cursor getStatisics(long id){
        Cursor mCursor = db.query(STATISTICS_DATA_TABLE_NAME, new String[] {_ID, DATE, KIND, TYPE}, _ID + "=" + id, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getStatisics(String data, String kind, String type){
        Cursor mCursor = db.query(STATISTICS_DATA_TABLE_NAME, new String[] {_ID, DATE, KIND, TYPE},
                                    DATE + "=" +  "'" + data + "'" + " AND " +
                                    KIND + "=" + "'" + kind + "'" + " AND " +
                                    TYPE + "=" + "'" + type + "'",
                                    null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}

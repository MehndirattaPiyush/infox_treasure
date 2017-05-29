package in.infox.qr.qrcodereader;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.util.Log;

import java.sql.SQLException;

/*
 * Created by Piyush on 8/27/2016.
 *  set a B-318,canteen,map,amul,parking
 *  set b amul,B-318,parking,map,canteen
 *  set c map,amul,canteen,B-318,parking
 *  set d parkng,map,B-318,canteen,amul
 */
class DBAdapter {
    private SQLiteDatabase database;
    private Context context;
    private DataBaseHelper dbHelper;
    String TAG = "qwerty";

    public static final String TABLE = "Hint";
    public static final String TABLE_A = "Hint_A";
    public static final String TABLE_B = "Hint_B";
    public static final String TABLE_C = "Hint_C";
    public static final String TABLE_D = "Hint_D";
    public static final String TABLE_found = "Hint_found";
    //public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_HINT = "hint";
    public static final String COLUMN_POINTS = "points";
    public static final String COLUMN_ANSWER = "to_be_scanned";
    public static final String COLUMN_POINTS_SCORED = "points_scored";
    public static final String COLUMN_FOUND = "found";

    private static final String DATABASE_NAME = "treasure.db";
    private static final int DATABASE_VERSION = 1;

    private String canteen = "Analyze the 2268336 buttons on your phone screen because they will lead to the location where your next qr code will be seen.";
    private String map = "I have blocks but no classes, library but no books, pool but no water, canteen but no food, hostel but no rooms... What am I?";
    private String amul = "Polka Dots from the West! ";
    private String parking = "Caution is needed when you going this way. You start looking for places when you reach midway. You will hopefully find a good one someday and even if you dont, its ok.";
    private String morse = "dashdotdotdot dotdotdotdashdash dotdashdashdashdash dashdashdashdotdot\n";

    private String one   = "<u>1</u>. Flying fish with 2<u>3</u> gold feathers is the predator of the water wor<u>L</u>d. Find this place in the university.\n";
    private String two   = "2. Rose was his favorite, so were the <u>C</u>hildren. Find the place where one if his mentioned favorite thing exists. \n";
    private String three = " <u>3</u>. A great player of his <u>O</u>wn. Now a real life dronacharya. Teaches  his students to do air battles inside his temple. Your next clue is in his temple. Go find it.";
    private String four  = "4. Have a canopy like structure, <u>B</u>ut is not a tree. Provides shade to people, but is not a tree. Present near the favorite place of all the students. The air around it is always in rhythm. Find the place.";
    private String five  = "5. All is one, one has many forms. One form rests on a snake, on the other form the snake rests. Find the form which grants wisdom and <u>K</u>nowledge.\n";
    private String six   = "6. As rain turns to streams, as it falls down the black mount<u>A</u>in, droplets flow down you, when they leap from it.  Your next clue is near this black mountain.\n";
    private String seven = "7. Congratulations, you have reached to your final clue. Now, all the clues that you have collected together will lead you to your prize. All the clues have some bold numbers and letters in them. These bold letters and numbers are a part of an anagram. Solving this anagram will lead you to the room where your prize is kept. Go get it! ";




    private class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_found + "(" + COLUMN_KEY + " TEXT ," + COLUMN_HINT + " TEXT ," + COLUMN_FOUND + " TEXT ," + COLUMN_POINTS_SCORED + " TEXT)");
            db.execSQL("CREATE TABLE " + TABLE + "(" + COLUMN_KEY + " TEXT ," + COLUMN_HINT + " TEXT ," + COLUMN_FOUND + " TEXT ," + COLUMN_POINTS + " TEXT)");
            db.execSQL("CREATE TABLE " + TABLE_A + "(" + COLUMN_KEY + " TEXT ," + COLUMN_HINT + " TEXT ," + COLUMN_FOUND + " TEXT ," + COLUMN_POINTS + " TEXT ," + COLUMN_ANSWER + " TEXT)");
            db.execSQL("CREATE TABLE " + TABLE_B + "(" + COLUMN_KEY + " TEXT ," + COLUMN_HINT + " TEXT ," + COLUMN_FOUND + " TEXT ," + COLUMN_POINTS + " TEXT ," + COLUMN_ANSWER + " TEXT)");
            db.execSQL("CREATE TABLE " + TABLE_C + "(" + COLUMN_KEY + " TEXT ," + COLUMN_HINT + " TEXT ," + COLUMN_FOUND + " TEXT ," + COLUMN_POINTS + " TEXT ," + COLUMN_ANSWER + " TEXT)");
            db.execSQL("CREATE TABLE " + TABLE_D + "(" + COLUMN_KEY + " TEXT ," + COLUMN_HINT + " TEXT ," + COLUMN_FOUND + " TEXT ," + COLUMN_POINTS + " TEXT ," + COLUMN_ANSWER + " TEXT)");
            // db.execSQL("CREATE TABLE " + TABLE + "(" + COLUMN_KEY + " TEXT ," + COLUMN_HINT + " TEXT ," + COLUMN_SET + " TEXT ," +COLUMN_FOUND+" TEXT)");
            // db.execSQL("CREATE TABLE " + TABLE + "(" + COLUMN_KEY + " TEXT ," + COLUMN_HINT + " TEXT ,"+COLUMN_SET+" TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST " + TABLE);
            db.execSQL("DROP TABLE IF EXIST " + TABLE_A);
            db.execSQL("DROP TABLE IF EXIST " + TABLE_B);
            db.execSQL("DROP TABLE IF EXIST " + TABLE_C);
            db.execSQL("DROP TABLE IF EXIST " + TABLE_D);
            db.execSQL("DROP TABLE IF EXIST " + TABLE_found);
        }
    }

    public DBAdapter(Context ctx) {
        this.context = ctx;

    }

    public DBAdapter open() throws SQLException {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();
//        database.execSQL("INSERT INTO " + TABLE + " VALUES ('A','SET A','','0');");
//        database.execSQL("INSERT INTO " + TABLE + " VALUES ('B','SET B','','0');");
//        database.execSQL("INSERT INTO " + TABLE + " VALUES ('C','SET C','','0');");
//        database.execSQL("INSERT INTO " + TABLE + " VALUES ('D','SET D','','0');");
//        database.execSQL("INSERT INTO " + TABLE + " VALUES ('1','" + canteen + "'" +
//                ",'A','70');");// 2ND FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('1','" + morse + "'," +
                "'B','40');");// 2ND FOR B
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('1','" + amul + "'," +
                "'C','60');");//2ND FOR C
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('1','" + map + "'," +
                "'D','30');");//2ND FOR D
//        database.execSQL("INSERT INTO " + TABLE + " VALUES ('2','" + map + "'," +
//                "'A','30');");//3RD FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('2','" + parking + "'," +
                "'B','60');");//3RD FOR B
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('2','" + canteen + "'," +
                "'C','70');"); //3RD FOR C
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('2','" + morse + "'," +
                "'D','40');");//3RD FOR D
//        database.execSQL("INSERT INTO " + TABLE + " VALUES ('3','" + amul + "'," +
//                "'A','60');");//4TH FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('3','" + map + "'," +
                "'B','30');");//4TH FOR B
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('3','" + morse + "'," +
                "'C','40');");//4TH FOR C
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('3','" + canteen + "'," +
                "'D','70');");//4TH FOR D
//        database.execSQL("INSERT INTO " + TABLE + " VALUES ('4','" + parking + "'," +
//                "'A','60');");// 5TH FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('4','" + canteen + "'," +
                "'B','70');");//5TH FOR B
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('4','" + parking + "'," +
                "'C','60');");//5TH FOR C
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('4','" + amul + "'," +
                "'D','60');");//5TH FOR D
//        database.execSQL("INSERT INTO " + TABLE + " VALUES ('A','" + morse + "'," +
//                "'0','40');");//1ST FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('B','" + amul + "'," +
                "'0','60');");//1ST FOR B
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('C','" + map + "'," +
                "'0','30');");//1ST FOR C
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('D','" + parking + "'," +
                "'0','60');");//1ST FOR D

        database.execSQL("INSERT INTO " + TABLE + " VALUES ('1','" +  one + "'," +
                "'A','30');");//3RD FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('2','" +  two + "'," +
                "'2','30');");//3RD FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('3','" +  three + "'," +
                "'3','30');");//3RD FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('4','" +  four + "'," +
                "'4','30');");//3RD FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('5','" +  five + "'," +
                "'5','30');");//3RD FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('6','" +  six + "'," +
                "'6','30');");//3RD FOR A
        database.execSQL("INSERT INTO " + TABLE + " VALUES ('7','" +  seven + "'," +
                "'7','30');");//3RD FOR A


        return this;
    }

    public DBAdapter openSetA() throws SQLException {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

        database.execSQL("INSERT INTO " + TABLE_A + " VALUES ('A','" +  one + "'" +
                ",'0','40','one');");
        database.execSQL("INSERT INTO " + TABLE_A + " VALUES ('one','" +  two + "'" +
                ",'0','70','two');");
        database.execSQL("INSERT INTO " + TABLE_A + " VALUES ('two','" +  three + "'" +
                ",'0','30','three');");
        database.execSQL("INSERT INTO " + TABLE_A + " VALUES ('three','" +  four + "'," +
                "'0','60','four');");
        database.execSQL("INSERT INTO " + TABLE_A + " VALUES ('four','" +  five + "'," +
                "'0','60','five');");
        database.execSQL("INSERT INTO " + TABLE_A + " VALUES ('five','" +  six + "'," +
                "'0','60','six');");
        database.execSQL("INSERT INTO " + TABLE_A + " VALUES ('six','" +  seven + "'," +
                "'0','60','seven');");
        return this;
    }

    public DBAdapter openSetB() throws SQLException {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

        database.execSQL("INSERT INTO " + TABLE_B + " VALUES ('B','" + amul + "'" +
                ",'0','60','1');");
        database.execSQL("INSERT INTO " + TABLE_B + " VALUES ('1','" + morse + "'" +
                ",'0','40','3');");
        database.execSQL("INSERT INTO " + TABLE_B + " VALUES ('2','" + parking + "'" +
                ",'0','60','4');");
        database.execSQL("INSERT INTO " + TABLE_B + " VALUES ('3','" + map + "'," +
                "'0','30','2');");
        database.execSQL("INSERT INTO " + TABLE_B + " VALUES ('4','" + canteen + "'," +
                "'0','70','DONE');");
        return this;
    }

    public DBAdapter openSetC() throws SQLException {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

        database.execSQL("INSERT INTO " + TABLE_C + " VALUES ('C','" + map + "'" +
                ",'0','30','3');");
        database.execSQL("INSERT INTO " + TABLE_C + " VALUES ('1','" + amul + "'" +
                ",'0','70','2');");
        database.execSQL("INSERT INTO " + TABLE_C + " VALUES ('2','" + canteen + "'" +
                ",'0','60','4');");
        database.execSQL("INSERT INTO " + TABLE_C + " VALUES ('3','" + morse + "'," +
                "'0','40','1');");
        database.execSQL("INSERT INTO " + TABLE_C + " VALUES ('4','" + parking + "'," +
                "'0','60','DONE');");
        return this;
    }

    public DBAdapter openSetD() throws SQLException {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

        database.execSQL("INSERT INTO " + TABLE_D + " VALUES ('D','" + parking + "'" +
                ",'0','70','2');");
        database.execSQL("INSERT INTO " + TABLE_D + " VALUES ('1','" + map + "'" +
                ",'0','30','4');");
        database.execSQL("INSERT INTO " + TABLE_D + " VALUES ('2','" + morse + "'" +
                ",'0','40','1');");
        database.execSQL("INSERT INTO " + TABLE_D + " VALUES ('3','" + canteen + "'," +
                "'0','60','DONE');");
        database.execSQL("INSERT INTO " + TABLE_D + " VALUES ('4','" + amul + "'," +
                "'0','60','3');");
        return this;
    }


    public Cursor executeQuery() {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getReadableDatabase();
        //Cursor result = database.rawQuery("SELECT * FROM " + TABLE+" WHERE "+COLUMN_FOUND+" = '1'", null);
        Cursor result = database.rawQuery("SELECT * FROM " + TABLE_found, null);
        return result;
    }

    public String getHint(String key, String set) {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getReadableDatabase();
        String table;
        if (set.equals("0")) {
            table = TABLE;
        } else {
            table = TABLE + "_" + set;
        }
        Log.d("TAGOOT2", "this SELECT * FROM " + table + " WHERE " + COLUMN_KEY + " = '" + key + "'");

        String hint = "";
        // Cursor result = database.rawQuery("SELECT * FROM " + TABLE + " WHERE " + COLUMN_KEY + " = '" + key + "' AND " + COLUMN_FOUND + " = '" + set + "'", null);
            Cursor result = database.rawQuery("SELECT * FROM " + table
                + " WHERE " + COLUMN_KEY + " = '" + key + "'", null);

        if (result.moveToNext()) {
            Log.d("TAGOOT", "onCreate: retoun");
            hint = result.getString(1);
            Log.d("TAGOOT", "onCreate: retoun" + hint);
        }
        return hint;
    }

//    public String getHintPoint(String key, String set) {
//        String table;
//        if (set.equals("0")) {
//            table = TABLE;
//        } else {
//            table = TABLE + "_" + set;
//        }
//        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
//        database = dbHelper.getReadableDatabase();
//        Log.d("TAGOOT2", "SELECT * FROM " + table + " WHERE " + COLUMN_KEY + " = '" + key + "'");
//
//        String hint = "";
//        Cursor result = database.rawQuery("SELECT * FROM " + table + " WHERE " + COLUMN_KEY + " = '" + key + "'", null);
//        if (result.moveToNext()) {
//            Log.d("TAGOOT", "onCreate: retoun");
//            hint = result.getString(3);
//            Log.d("TAGOOT", "onCreate: retoun" + hint);
//        }
//        return hint;
//    }

    public String getToBeScanned(String key, String set) {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getReadableDatabase();
        String table = TABLE + "_" + set;
        Log.d("TAGOOT2", "SELECT * FROM " + table + " WHERE " + COLUMN_KEY + " = '" + key + "'");

        String hint = "";
        Cursor result = database.rawQuery("SELECT * FROM " + table + " WHERE " + COLUMN_KEY + " = '" + key + "'", null);
        if (result.moveToNext()) {
            Log.d("TAGOOT", "onCreate: retoun");
            hint = result.getString(4);
            Log.d("TAGOOT", "onCreate: retoun" + hint);
        }
        return hint;
    }

    public DBAdapter updateHint(String key, String value) {
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();
        database.execSQL("INSERT INTO " + TABLE_found + " VALUES ('" + key + "','" + value + "','0','0');");
        // database.execSQL("UPDATE "+TABLE+" SET "+COLUMN_FOUND+" = '1' WHERE "+COLUMN_KEY +" = '"+key+"'" );
        return this;
    }

    public DBAdapter updateSetHint(String key, String set) {
        String table = TABLE + "_" + set;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();
        database.execSQL("UPDATE " + table + " SET " + COLUMN_FOUND + " = '1' WHERE " + COLUMN_KEY + " = '" + key + "'");
        return this;
    }
}


package com.spacebunny.hshen.inscat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.model.Category;
import com.spacebunny.hshen.inscat.model.Post;
import com.spacebunny.hshen.inscat.utils.ModelUtils;

import java.util.ArrayList;
import java.util.List;

import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.CATEGORY_COLUMN_DESCRIPTION;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.CATEGORY_COLUMN_ID;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.CATEGORY_COLUMN_NAME;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.CATEGORY_COLUMN_POST_COUNT;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.CATEGORY_COLUMN_USER_ID;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.CATEGORY_TABLE_NAME;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.DATABASE_NAME;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.POST_CATEGORY_COLUMN_CATEGORY_ID;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.POST_CATEGORY_COLUMN_ID;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.POST_CATEGORY_COLUMN_JSON;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.POST_CATEGORY_COLUMN_POST_ID;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.POST_CATEGORY_COLUMN_USER_ID;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.POST_CATEGORY_TABLE_NAME;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.POST_COLUMN_CATEGORY_COUNT;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.POST_COLUMN_ID;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.POST_COLUMN_POST_ID;
import static com.spacebunny.hshen.inscat.database.DBReaderContract.DBEntry.POST_TABLE_NAME;

public class DBReaderHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 5;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_CATEGORY_ENTRIES = "CREATE TABLE " + CATEGORY_TABLE_NAME + "(" +
            CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            CATEGORY_COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
            CATEGORY_COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            CATEGORY_COLUMN_USER_ID + TEXT_TYPE + COMMA_SEP +
            CATEGORY_COLUMN_POST_COUNT + " INTEGER)";

    private static final String SQL_DELETE_CATEGORY_ENTRIES = "DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME;

    private static final String SQL_CREATE_POST_CATEGORY_ENTRIES = "CREATE TABLE " + POST_CATEGORY_TABLE_NAME + "(" +
            POST_CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            POST_CATEGORY_COLUMN_POST_ID + TEXT_TYPE + COMMA_SEP +
            POST_CATEGORY_COLUMN_CATEGORY_ID + TEXT_TYPE + COMMA_SEP +
            POST_CATEGORY_COLUMN_USER_ID + TEXT_TYPE + COMMA_SEP +
            POST_CATEGORY_COLUMN_JSON + TEXT_TYPE + ")";

    private static final String SQL_DELETE_POST_CATEGORY_ENTRIES = "DROP TABLE IF EXISTS " + POST_CATEGORY_TABLE_NAME;

    private static final String SQL_CREATE_POST_ENTRIES = "CREATE TABLE " + POST_TABLE_NAME + "(" +
            POST_COLUMN_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            POST_COLUMN_POST_ID + TEXT_TYPE + COMMA_SEP +
            POST_COLUMN_CATEGORY_COUNT + " INTEGER)";

    private static final String SQL_DELETE_POST_ENTRIES = "DROP TABLE IF EXISTS " + POST_TABLE_NAME;

    public DBReaderHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_POST_CATEGORY_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_POST_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_CATEGORY_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_POST_CATEGORY_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_POST_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public boolean insertCategory(String name, String description, String userid, int post_count) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, name);
        values.put(CATEGORY_COLUMN_DESCRIPTION, description);
        values.put(CATEGORY_COLUMN_USER_ID, userid);
        values.put(CATEGORY_COLUMN_POST_COUNT, post_count);
        db.insert(CATEGORY_TABLE_NAME, null, values);
        return true;
    }

    public boolean updateCategory(Integer id, String name, String description, String userid, int post_count) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, name);
        values.put(CATEGORY_COLUMN_DESCRIPTION, description);
        values.put(CATEGORY_COLUMN_USER_ID, userid);
        values.put(CATEGORY_COLUMN_POST_COUNT, post_count);
        db.update(CATEGORY_TABLE_NAME, values, CATEGORY_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
        return true;
    }

    public boolean updateCategory(Integer id, int post_count) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_POST_COUNT, post_count);
        db.update(CATEGORY_TABLE_NAME, values, CATEGORY_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
        return true;
    }

    public Integer deleteCategory(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(CATEGORY_TABLE_NAME, CATEGORY_COLUMN_ID + " = ? ", new String[] {Integer.toString(id)});
    }

    public boolean insertPostCategory(String categoryId, Post post) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POST_CATEGORY_COLUMN_POST_ID, post.id);
        values.put(POST_CATEGORY_COLUMN_CATEGORY_ID, categoryId);
        values.put(POST_CATEGORY_COLUMN_USER_ID, Ins.getCurrentUser().id);

        TypeToken<Post> POST_TYPE = new TypeToken<Post>(){};
        String postJson = ModelUtils.toString(post, POST_TYPE);
        values.put(POST_CATEGORY_COLUMN_JSON, postJson);

        db.insert(POST_CATEGORY_TABLE_NAME, null, values);
        return true;
    }

    public Integer deletePostCategory(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(POST_CATEGORY_TABLE_NAME, POST_CATEGORY_COLUMN_ID + " = ? ", new String[] {Integer.toString(id)});
    }

    public boolean insertPost(String postid, int categoryCount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POST_COLUMN_POST_ID, postid);
        values.put(POST_COLUMN_CATEGORY_COUNT, categoryCount);
        db.insert(POST_TABLE_NAME, null, values);
        return true;
    }

    public boolean updatePost(String postid, int categoryCount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(POST_COLUMN_POST_ID, postid);
        values.put(POST_COLUMN_CATEGORY_COUNT, categoryCount);
        db.update(POST_TABLE_NAME, values, POST_COLUMN_POST_ID + " = ? ",
                new String[] {postid});
        return true;

    }

    public Integer deletePost(String postid) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(POST_TABLE_NAME, POST_COLUMN_POST_ID + " = ? ", new String[] {postid});
    }

    public Cursor getCategoryOfId(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE " +
                CATEGORY_COLUMN_ID + "=?", new String[] {id} );
        return res;
    }

    public int getCategoryPostCount(String id) {
        Cursor c = getCategoryOfId(id);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex(CATEGORY_COLUMN_POST_COUNT));
    }

    public Cursor getCategoryOfUser(String userid) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + CATEGORY_TABLE_NAME + " WHERE " +
                CATEGORY_COLUMN_USER_ID + "=?", new String[] {userid} );
        return res;
    }

    public List<Category> getUserCategoryList(String userid) {
        List<Category> categories = new ArrayList<>();
        Cursor c = getCategoryOfUser(userid);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Category curCategory = new Category();
            curCategory.name = c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME));
            Log.d("DS", "Name is " + curCategory.name);
            curCategory.description = c.getString(c.getColumnIndex(CATEGORY_COLUMN_DESCRIPTION));
            Log.d("DS", "Description is " + curCategory.description);
            curCategory.post_count = c.getInt(c.getColumnIndex(CATEGORY_COLUMN_POST_COUNT));
            curCategory.isChoosing = false;
            curCategory.id = c.getString(c.getColumnIndex(CATEGORY_COLUMN_ID));
            Log.d("DS", "ID is " + curCategory.id);
            categories.add(curCategory);
            c.moveToNext();
        }
        return categories;
    }

    public Cursor getCategoryOfPost(String postid) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + POST_CATEGORY_TABLE_NAME + " WHERE " +
                POST_CATEGORY_COLUMN_POST_ID + "=?", new String[] {postid});
        return res;
    }

    public List<String> getPostCategoryIdList(String postid) {
        List<String> categoryIds = new ArrayList<>();
        Cursor c = getCategoryOfPost(postid);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String curCategoryId = c.getString(c.getColumnIndex(POST_CATEGORY_COLUMN_CATEGORY_ID));
            categoryIds.add(curCategoryId);
            c.moveToNext();
        }
        return categoryIds;
    }

    public Cursor getIdOfPostInCategory(String postid, String categoryid) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + POST_CATEGORY_TABLE_NAME + " WHERE " +
                POST_CATEGORY_COLUMN_POST_ID + " =? AND  " + POST_CATEGORY_COLUMN_CATEGORY_ID + " =? ", new String[] {postid, categoryid});
        return res;
    }

    public Integer getPostInCategoryId(String postid, String categoryid) {
        Cursor c = getIdOfPostInCategory(postid, categoryid);
        c.moveToFirst();
        Integer id = c.getInt(c.getColumnIndex(POST_CATEGORY_COLUMN_ID));
        return id;
    }

    public Cursor getPostsInCategory(String userid, String categoryid) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + POST_CATEGORY_TABLE_NAME + " WHERE " +
                POST_CATEGORY_COLUMN_USER_ID + " =? AND  " + POST_CATEGORY_COLUMN_CATEGORY_ID + " =? ", new String[] {userid, categoryid});
        return res;
    }

    public List<Post> getPostListInCategory(String userid, String categoryid) {
        TypeToken<Post> POST_TYPE = new TypeToken<Post>(){};
        List<Post> postList = new ArrayList<>();
        Post curPost;

        Cursor c = getPostsInCategory(userid, categoryid);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String curPostJson = c.getString(c.getColumnIndex(POST_CATEGORY_COLUMN_JSON));
            curPost = ModelUtils.toObject(curPostJson, POST_TYPE);
            postList.add(curPost);
            c.moveToNext();
        }
        return postList;
    }

    public Integer getPostCategoryCount(String postid) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + POST_TABLE_NAME + " WHERE " +
                POST_COLUMN_POST_ID + "=?", new String[] {postid});
        if (c.getCount() > 0) {
            c.moveToFirst();
            Integer count = c.getInt(c.getColumnIndex(POST_COLUMN_CATEGORY_COUNT));
            return count;
        }
        else {
            return null;
        }
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }
}

package student.pxl.be.mealapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import student.pxl.be.mealapp.domain.Meal;

public class LocalMealsTableManager extends SQLiteOpenHelper{

    //TODO: Refactor this class for better performance and readability, see contentvalues and set meals

    private static final String DATABASE_NAME = "Meals";
    private static final String TABLE_LOCALMEALS = "LOCALMEALS";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_THUMBNAIL = "thumbnail";
    private static final String[] COLUMNS = {KEY_ID, KEY_TITLE, KEY_INGREDIENTS, KEY_THUMBNAIL, KEY_DESCRIPTION};

    private SQLiteDatabase db;

    public LocalMealsTableManager(Context context){
        //TODO: Make sure that MealsDB is only created once
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCALMEALS_TABLE = "CREATE TABLE LOCALMEALS(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "ingredients TEXT," +
                "thumbnail TEXT," +
                "description TEXT)"; //TODO: thumbnail from camera
        db.execSQL(CREATE_LOCALMEALS_TABLE);
        //TODO: FAVORITEMEALS DB
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS LOCALMEALS");

        //create fresh table
        this.onCreate(db);
    }

    public void addMeal(Meal meal){
        Log.d("addMeal", meal.toString());
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, meal.getTitle());
        values.put(KEY_INGREDIENTS, meal.getIngredients());
        values.put(KEY_THUMBNAIL, meal.getThumbnail());
        values.put(KEY_DESCRIPTION, meal.getDescription());

        db.insert(TABLE_LOCALMEALS, null, values);
        db.close();
    }

    public Meal getMeal(int id) {
        db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCALMEALS,
                COLUMNS, "id = ?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Meal meal = new Meal();
        meal.setId(cursor.getInt(0));
        meal.setTitle(cursor.getString(1));
        meal.setIngredients(cursor.getString(2));
        meal.setThumbnail(cursor.getString(3));
        meal.setDescripion(cursor.getString(4));

        db.close();
        return meal;
    }

    public ArrayList<Meal> getAllMeals() {
        ArrayList<Meal> meals = new ArrayList<Meal>();

        String query = "SELECT * FROM " + TABLE_LOCALMEALS;

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Meal meal = null;
        if (cursor.moveToFirst()) {
            do {
                meal = new Meal();
                meal.setId(cursor.getInt(0));
                meal.setTitle(cursor.getString(1));
                meal.setIngredients(cursor.getString(2));
                meal.setThumbnail(cursor.getString(3));
                meal.setDescripion(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        Log.d("getAllMeals", meals.toString());
        return meals;
    }

    public int updateMeal(Meal meal) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, meal.getTitle());
        values.put(KEY_INGREDIENTS, meal.getIngredients());
        values.put(KEY_THUMBNAIL, meal.getThumbnail());
        values.put(KEY_DESCRIPTION, meal.getDescription());

        int i = db.update(TABLE_LOCALMEALS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(meal.getId())});

        db.close();
        Log.d("updateMeal", meal.toString());
        return i;
    }

    public void deleteMeal(Meal meal) {
        db = this.getWritableDatabase();

        db.delete(TABLE_LOCALMEALS, KEY_ID + " = ?",
                new String[] { String.valueOf(meal.getId())});

        db.close();
        Log.d("deleteMeal", meal.toString());
    }
    //TODO: Delete all local meals at once
}

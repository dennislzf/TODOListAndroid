package taskstodo.persistence;
 
import java.util.ArrayList;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class TODODatabaseManager
{
	// the Activity or Application that is creating an object from this class.
	Context context;
	private SQLiteDatabase db;
	private final String DB_NAME = "TODOList";
	private final int DB_VERSION = 1;
	private CustomSQLiteOpenHelper DBHelper;
 
	// These constants are specific to the database table.  They should be
	// changed to suit your needs.
	private final String TABLE_NAME = "TODO_table";
	private final String TABLE_ROW_ID = "id";
	private final String TABLE_ROW_ONE = "title";
	private final String TABLE_ROW_TWO = "description";
	private final String TABLE_ROW_THREE = "hour";
	private final String TABLE_ROW_FOUR = "minute";
	private final String TABLE_ROW_FIVE = "ampm";
	private final String TABLE_ROW_SIX = "myday";
	private final String TABLE_ROW_SEVEN = "mymonth";
	private final String TABLE_ROW_EIGHT = "myyear";
	private final String TABLE_ROW_NINE = "mypriority";
	
	 
	 
 
	public TODODatabaseManager(Context context)
	{
		this.context = context;
 
		// create or open the database
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
		this.db = helper.getWritableDatabase();
		DBHelper = new CustomSQLiteOpenHelper(context);
	}
 
 

 
	//add row to DB
	public void addRow(String rowStringOne, String rowStringTwo,  String rowStringThree, String rowStringFour, String rowStringFive,
			 String rowStringSix, String rowStringSeven, String rowStringEight, String rowStringNine)
	{
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues();
		values.put(TABLE_ROW_ONE, rowStringOne);
		values.put(TABLE_ROW_TWO, rowStringTwo);
		values.put(TABLE_ROW_THREE, rowStringThree);
		values.put(TABLE_ROW_FOUR, rowStringFour);
		values.put(TABLE_ROW_FIVE, rowStringFive);
		values.put(TABLE_ROW_SIX, rowStringSix);
		values.put(TABLE_ROW_SEVEN, rowStringSeven);
		values.put(TABLE_ROW_EIGHT, rowStringEight);
		values.put(TABLE_ROW_NINE, rowStringNine);
		
		// ask the database object to insert the new data 
		try{db.insert(TABLE_NAME, null, values);}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
 
 
 

	public void deleteRow(long rowID)
	{
		// ask the database manager to delete the row of given id
		try {db.delete(TABLE_NAME, TABLE_ROW_ID + "=" + rowID, null);}
		catch (Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
 

	public void updateRow(long rowID, String rowStringOne, String rowStringTwo,  String rowStringThree, String rowStringFour, String rowStringFive,
			 String rowStringSix, String rowStringSeven, String rowStringEight, String rowStringNine)
	{
		// this is a key value pair holder used by android's SQLite functions
		ContentValues values = new ContentValues();
		values.put(TABLE_ROW_ONE, rowStringOne);
		values.put(TABLE_ROW_TWO, rowStringTwo);
		values.put(TABLE_ROW_THREE, rowStringThree);
		values.put(TABLE_ROW_FOUR, rowStringFour);
		values.put(TABLE_ROW_FIVE, rowStringFive);
		values.put(TABLE_ROW_SIX, rowStringSix);
		values.put(TABLE_ROW_SEVEN, rowStringSeven);
		values.put(TABLE_ROW_EIGHT, rowStringEight);
		values.put(TABLE_ROW_NINE, rowStringNine);
		
 
		// update the database row of given rowID
		try {db.update(TABLE_NAME, values, TABLE_ROW_ID + "=" + rowID, null);}
		catch (Exception e)
		{
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
	}
 

	public ArrayList<Object> getRowAsArray(long rowID)
	{
		// create an array list to store data from the database row.
		ArrayList<Object> rowArray = new ArrayList<Object>();
		Cursor cursor;
 
		try
		{

			cursor = db.query
			(
					TABLE_NAME,
					new String[] {TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO,TABLE_ROW_THREE,TABLE_ROW_FOUR,TABLE_ROW_FIVE,TABLE_ROW_SIX,TABLE_ROW_SEVEN,TABLE_ROW_EIGHT,
							TABLE_ROW_NINE},
					TABLE_ROW_ID + "=" + rowID,
					null, null, null, null, null
			);
 
			// move the pointer to position zero in the cursor.
			cursor.moveToFirst();
 
			// if there is data available after the cursor's pointer, add
			// it to the ArrayList that will be returned by the method.
			if (!cursor.isAfterLast())
			{
				do
				{
					rowArray.add(cursor.getLong(0));
					rowArray.add(cursor.getString(1));
					rowArray.add(cursor.getString(2));
					rowArray.add(cursor.getString(3));
					rowArray.add(cursor.getString(4));
					rowArray.add(cursor.getString(5));
					rowArray.add(cursor.getString(6));
					rowArray.add(cursor.getString(7));
					rowArray.add(cursor.getString(8));
					rowArray.add(cursor.getString(9));
				}
				while (cursor.moveToNext());
			}
 
			cursor.close();
		}
		catch (SQLException e) 
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
 
		// return the ArrayList containing the given row from the database.
		return rowArray;
	}
 
 
 
 

	public ArrayList<ArrayList<Object>> getAllRowsAsArrays()
	{
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
 
		// this is a database call that creates a "cursor" object.
		// the cursor object store the information collected from the
		// database and is used to iterate through the data.
		Cursor cursor;
 
		try
		{
			// ask the database object to create the cursor.
			cursor = db.query(
					TABLE_NAME,
					new String[]{TABLE_ROW_ID, TABLE_ROW_ONE, TABLE_ROW_TWO,TABLE_ROW_THREE,TABLE_ROW_FOUR,TABLE_ROW_FIVE,TABLE_ROW_SIX,TABLE_ROW_SEVEN,TABLE_ROW_EIGHT,
							TABLE_ROW_NINE},
					null, null, null, null, null
			);
 
			// move the cursor's pointer to position zero.
			cursor.moveToFirst();
 
			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast())
			{
				do
				{
					ArrayList<Object> dataList = new ArrayList<Object>();
 
					dataList.add(cursor.getLong(0));
					dataList.add(cursor.getString(1));
					dataList.add(cursor.getString(2));
					dataList.add(cursor.getString(3));
					dataList.add(cursor.getString(4));
					dataList.add(cursor.getString(5));
					dataList.add(cursor.getString(6));
					dataList.add(cursor.getString(7));
					dataList.add(cursor.getString(8));
					dataList.add(cursor.getString(9));
					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		}
		catch (SQLException e)
		{
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}
 
		// return the ArrayList that holds the data collected from
		// the database.
		return dataArrays;
	}
 
 
	public TODODatabaseManager open() throws SQLException
	{
	
	db = DBHelper.getWritableDatabase();
	return this;
	}
	
	public void deleteAll()
	{
	    SQLiteDatabase db= DBHelper.getWritableDatabase();
	    db.delete(TABLE_NAME, null, null);

	}
	//---closes the database---
	public void close()
	{
		DBHelper.close();
	}
 
	
	private class CustomSQLiteOpenHelper extends SQLiteOpenHelper
	{
		public CustomSQLiteOpenHelper(Context context)
		{
			super(context, DB_NAME, null, DB_VERSION);
		}
 
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			//create db
			String newTableQueryString = "create table " +
										TABLE_NAME +
										" (" +
										TABLE_ROW_ID + " integer primary key autoincrement not null," +
										TABLE_ROW_ONE + " text," +
										TABLE_ROW_TWO + " text," +
										TABLE_ROW_THREE + " text," +
										TABLE_ROW_FOUR + " text," +
										TABLE_ROW_FIVE + " text," +
										TABLE_ROW_SIX + " text," +
										TABLE_ROW_SEVEN + " text," +
										TABLE_ROW_EIGHT + " text," +
										TABLE_ROW_NINE + " text" +
										");";
			// execute the query string to the database.
			db.execSQL(newTableQueryString);
		}
 
 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// NOTHING TO DO HERE. THIS IS THE ORIGINAL DATABASE VERSION.
			// OTHERWISE, YOU WOULD SPECIFIY HOW TO UPGRADE THE DATABASE.
		}
	}
}
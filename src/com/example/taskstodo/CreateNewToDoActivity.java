package com.example.taskstodo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.taskstodo.persistence.TODODatabaseManager;

@SuppressLint("ValidFragment")
public class CreateNewToDoActivity extends Activity {
	String myday, mymonth, myyear, myhour,myminute,mypriority,ampm;
	boolean dateselected = false;
	boolean timeselected = false;
	boolean priorityselected = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_to_do);
	
		
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
 // show time selection when setting the time
	public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	    
	}
	// show date selection UI when selecting date for To Do item
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	// high priority is set for the task
	public void priorityHighClick(View v){
		mypriority = "High";
		priorityselected = true;
	}
	// low priority is set for the task
	public void priorityLowClick(View v){
		mypriority = "Low";
		priorityselected = true;
	}
	
	//add task to the database
	public void addToDo(View v){
		int added = addItemToDB();
		if (added == 0 ){
		Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
		}
	}
	//Add task to DB and wait clear the fields to add another
	public void addAnotherToDo(View v){
		 int added = addItemToDB();
		 if (added == 0 ){
		dateselected = false;
		timeselected = false;
		priorityselected = false;
		myhour = "";
		myminute= "";
		ampm= "";
		myday= "";
		mymonth= "";
		myyear= "";
		mypriority= "";
		((EditText) findViewById(R.id.item_to_do)).setText("");
		((EditText) findViewById(R.id.description)).setText("");
		((Button) findViewById(R.id.date_selected)).setText("Pick A Date");
		((Button) findViewById(R.id.time_selected)).setText("Pick A Time");
		 }
		
	}
	
	@SuppressWarnings("deprecation")
	private int addItemToDB() {
		Context context = getApplicationContext();
		EditText titlebox = (EditText) findViewById(R.id.item_to_do);
		
		// if user did not create title for todo item, make sure that they create one.
				if (titlebox.getText().toString().equals("")){
					Toast toast = Toast.makeText(context, "You must choose a title for your TODO item!", Toast.LENGTH_SHORT);
					toast.show();
					return -1;
				}
				//if user doesnt select date or time then prompt user to select a time and date
				if(dateselected == false || timeselected == false){
					Toast toast = Toast.makeText(context, "You must select and date and a time for your TODO item!", Toast.LENGTH_SHORT);
					toast.show();
					return -1;
				}
				// if user does not select priority setting for event, make prompt tellig user to select one.
				if(priorityselected = false){
					Toast toast = Toast.makeText(context, "You must select a priority for your TODO task!", Toast.LENGTH_SHORT);
					toast.show();
					return -1;
				}
			
		
		//if date selected is past or time selected has passed, do not allow selection.
		Time date = new Time();
		date.setToNow();
		
		int isampmhour;
		if(ampm.equals("pm")){
			isampmhour = Integer.parseInt(myhour) + 12;
		}
		else isampmhour = Integer.parseInt(myhour);
		Time selecteddate = new Time();
		selecteddate.set(2,Integer.parseInt(myminute), Integer.parseInt(myhour), Integer.parseInt(myday), Integer.parseInt(mymonth),Integer.parseInt(myyear));
		
		if(selecteddate.before(date)){
			Toast toast = Toast.makeText(context, "You must choose a date and time that is in the future!", Toast.LENGTH_SHORT);
			toast.show();
			return -1;
		}
		
	
		
		
		
		
		EditText descriptionbox = (EditText) findViewById(R.id.description);
		String title = titlebox.getText().toString();
		String description = descriptionbox.getText().toString();
		TODODatabaseManager db = new TODODatabaseManager(this);
		db.open();
		db.addRow(title, description, myhour, myminute, ampm, myday, mymonth, myyear, mypriority);
		db.close();
		return 0;
		
	}

	public  class DatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	// Use the current date as the default date in the picker
	final Calendar c = Calendar.getInstance();
	int year = c.get(Calendar.YEAR);
	int month = c.get(Calendar.MONTH);
	int day = c.get(Calendar.DAY_OF_MONTH);
	

	// Create a new instance of DatePickerDialog and return it
	return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		 myday  = String.valueOf(day);
		 mymonth  = String.valueOf(month);
		 myyear  = String.valueOf(year);
		 dateselected = true;
		Button datebutton = (Button) findViewById(R.id.date_selected);
		datebutton.setText(myday + "/" + mymonth + "/" + myyear);
	}
	}

	
	
	
	
	public  class TimePickerFragment extends DialogFragment
	implements TimePickerDialog.OnTimeSetListener {

		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	// Use the current time as the default values for the picker
	final Calendar c = Calendar.getInstance();
	int hour = c.get(Calendar.HOUR_OF_DAY);
	int minute = c.get(Calendar.MINUTE);

	// Create a new instance of TimePickerDialog and return it
	return new TimePickerDialog(getActivity(), this, hour, minute,
	DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		 ampm = "am";
		// if Hour of day is greater then 12 then set am to pm and minus 12 hours.
		if (hourOfDay>12){
			hourOfDay = hourOfDay - 12 ; 
			ampm = "pm";
		}
		if (hourOfDay == 0){
			hourOfDay = 12;
		}
		 myhour = String.valueOf(hourOfDay);
		 myminute = String.valueOf(minute); 
		 timeselected = true;
		Button buttontochange = (Button) findViewById(R.id.time_selected);
		buttontochange.setText(myhour + ":" + myminute + ampm);
		
		 
		  

	}
	}

}

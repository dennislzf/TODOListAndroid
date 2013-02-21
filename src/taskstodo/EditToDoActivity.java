package taskstodo;

import java.util.ArrayList;
import java.util.Calendar;

import com.taskstodo.R;
import taskstodo.CreateNewToDoActivity.DatePickerFragment;
import taskstodo.CreateNewToDoActivity.TimePickerFragment;
import taskstodo.persistence.TODODatabaseManager;


import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditToDoActivity extends Activity {
	
	String myday, mymonth, myyear, myhour,myminute,ampm;
	String mypriority = "";
	String monthval;
	int idedited;
	boolean dateselected = true;
	boolean timeselected = true;
	boolean priorityselected = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Context context = getApplicationContext();
		int idediting;
		setContentView(R.layout.activity_edit_to_do);
		Bundle extras = getIntent().getExtras();
		 idediting = (Integer) extras.get("editidval");
		setItemValues(idediting);
		
		
		
	}
	//set edit item boxes to values of item to do
	private void setItemValues(int idediting) {
		idedited = idediting;
		ArrayList<Object> itemvalues;
		TODODatabaseManager db = new TODODatabaseManager(this);
		db.open();
		itemvalues = db.getRowAsArray(idediting);
		db.close();
		//set title box
		EditText titlebox = (EditText) findViewById(R.id.item_to_do);
		titlebox.setText(itemvalues.get(1).toString());
		//set description box if not null
		if(itemvalues.get(2) != null){
			EditText descriptionbox = (EditText) findViewById(R.id.description);
			descriptionbox.setText(itemvalues.get(2).toString());
		}
		//set time selected value
		Button timeselected = (Button) findViewById(R.id.time_selected);
		myhour = itemvalues.get(3).toString();
		myminute = itemvalues.get(4).toString();
		if(Integer.parseInt(myminute) <10){
			myminute= "0" + myminute;
		}
		ampm = itemvalues.get(5).toString();
		timeselected.setText(myhour + ":"+ myminute + ampm);
		//set date  value
		Button dateselected = (Button) findViewById(R.id.date_selected);
		myday = itemvalues.get(6).toString();
		mymonth = itemvalues.get(7).toString();
		myyear = itemvalues.get(8).toString();
		monthval =String.valueOf(mymonth+1);
		dateselected.setText(myday + "/" + monthval + "/" + myyear);
		//set priority value
		mypriority = itemvalues.get(9).toString();
		if(mypriority.equals("High")){
			RadioButton highpriority = (RadioButton) findViewById(R.id.high);
			highpriority.setChecked(true);
		}else {
			RadioButton lowpriority = (RadioButton) findViewById(R.id.low);
			lowpriority.setChecked(true);
		}
	}
		
		public void cancelToDo(View v)
		{
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		
		public void saveToDo(View v){
			Context context = getApplicationContext();
			EditText titlebox = (EditText) findViewById(R.id.item_to_do);
			
			// if user did not create title for todo item, make sure that they create one.
					if (titlebox.getText().toString().equals("")){
						Toast toast = Toast.makeText(context, "You must choose a title for your TODO item!", Toast.LENGTH_SHORT);
						toast.show();
						return ;
					}
					//if user doesnt select date or time then prompt user to select a time and date
					if(dateselected == false || timeselected == false){
						Toast toast = Toast.makeText(context, "You must select and date and a time for your TODO item!", Toast.LENGTH_SHORT);
						toast.show();
						return ;
					}
					// if user does not select priority setting for event, make prompt tellig user to select one.
					if(priorityselected = false){
						Toast toast = Toast.makeText(context, "You must select a priority for your TODO task!", Toast.LENGTH_SHORT);
						toast.show();
						return ;
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
				return;
			}
			
			EditText descriptionbox = (EditText) findViewById(R.id.description);
			String title = titlebox.getText().toString();
			String description = descriptionbox.getText().toString();
			TODODatabaseManager db = new TODODatabaseManager(this);
			db.open();
			db.updateRow(idedited,title, description, myhour, myminute, ampm, myday, mymonth, myyear, mypriority);
			db.close();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);

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
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_edit_to_do, menu);
		return true;
	}
	
	
	public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	    
	}
	// show date selection UI when selecting date for To Do item
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
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
		 mymonth  = String.valueOf(month) ;
		 myyear  = String.valueOf(year);
		 dateselected = true;
		 monthval = String.valueOf(month+1);
		Button datebutton = (Button) findViewById(R.id.date_selected);
		datebutton.setText(myday + "/" + monthval + "/" + myyear);
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
		if(minute < 10){
			buttontochange.setText(myhour + ":" +"0"+ myminute + ampm);
		}else buttontochange.setText(myhour + ":" + myminute + ampm);
		
	}
	}

}

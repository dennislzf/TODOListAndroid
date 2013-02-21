package taskstodo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.taskstodo.R;
import taskstodo.persistence.TODODatabaseManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


@SuppressLint("ValidFragment")
public class MainActivity extends Activity {

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ArrayList<ArrayList<Object>> listoftodo;
		//delete items that are passed
		listoftodo = deleteItemsOffList();
		//check if there are todoitems
		if(listoftodo.size() > 0){
			listoftodo = sortTODOList(listoftodo);
			displayTodoList(listoftodo);
			
			
			
			
			
		}else{
			
			ScrollView todoscrolllist = (ScrollView) findViewById(R.id.TableScroller);
			TextView noitemsinlist = new TextView(this);
			noitemsinlist.setText("No items To Do!!");
			todoscrolllist.addView(noitemsinlist);
			
		}
		
		
	
		
	}
	

private ArrayList<ArrayList<Object>> deleteItemsOffList() {
	TODODatabaseManager db = new TODODatabaseManager(this);
	ArrayList<ArrayList<Object>> listnotdeleted;
	ArrayList<ArrayList<Object>> listafterdelete;
	db.open();
	listnotdeleted = db.getAllRowsAsArrays();
	
	db.close();
	Time currenttime = new Time();
	currenttime.setToNow();
	
	Time comparetime = new Time();
	int hourvalue;
	
	for(int i = 0 ; i < listnotdeleted.size(); i++){
		if(listnotdeleted.get(i).get(5).toString().equals("am")){
			hourvalue = (Integer.parseInt(listnotdeleted.get(i).get(3).toString()));
			}
			else hourvalue = (Integer.parseInt(listnotdeleted.get(i).get(3).toString())) +12;
		
		comparetime.set(1,
				Integer.parseInt(listnotdeleted.get(i).get(4).toString()),
				hourvalue,
				Integer.parseInt(listnotdeleted.get(i).get(6).toString()), 
				Integer.parseInt(listnotdeleted.get(i).get(7).toString()), 
				Integer.parseInt(listnotdeleted.get(i).get(8).toString()));
		
		if(comparetime.before(currenttime)){
			db.open();
			db.deleteRow(Integer.parseInt(listnotdeleted.get(i).get(0).toString()));
			db.close();
		}
	}
	
	db.open();
	listafterdelete = db.getAllRowsAsArrays();
	db.close();
	return listafterdelete;
		
	}


private void displayTodoList(ArrayList<ArrayList<Object>> listoftodo) {
	  LinearLayout todolinearlayout = new LinearLayout(this);
	  todolinearlayout.setOrientation(LinearLayout.VERTICAL);
		ScrollView todoscrolllist = (ScrollView) findViewById(R.id.TableScroller);
		todoscrolllist.addView(todolinearlayout);
	displayHighPriority(listoftodo, todolinearlayout);
	displayLowPriority(listoftodo, todolinearlayout);
		
	}

private void displayLowPriority(ArrayList<ArrayList<Object>> listoftodo, LinearLayout lowprioritylist) {
	
	Time tempdate= new Time();
	tempdate.set(0, 0, 0, 0, 0, 0);
	int hourvalue;
	boolean isheaderdisplayed= false; 
	TextView lowpriorityheader = new TextView(this);
	lowpriorityheader.setText("Low Priority");
	lowpriorityheader.setBackgroundColor(Color.rgb(205, 255, 255));
	lowpriorityheader.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
	
for( int i = 0; i < listoftodo.size(); i++)
{
	if(listoftodo.get(i).get(9).toString().equals("Low")){
		if(isheaderdisplayed == false){
			lowprioritylist.addView(lowpriorityheader);
			isheaderdisplayed = true;
		}
		if(listoftodo.get(i).get(5).toString().equals("am")){
			hourvalue = (Integer.parseInt(listoftodo.get(i).get(3).toString()));
			}
			else hourvalue = (Integer.parseInt(listoftodo.get(i).get(3).toString())) +12;
			Time testnewdate = new Time();
			testnewdate.set(Integer.parseInt(listoftodo.get(i).get(6).toString()),
							Integer.parseInt(listoftodo.get(i).get(7).toString()),
							Integer.parseInt(listoftodo.get(i).get(8).toString()));
			if(testnewdate.monthDay != tempdate.monthDay || testnewdate.month != tempdate.month || testnewdate.year != tempdate.year){
				TextView dateheader = new TextView(this);
				String dateformat = changeToDateFormat(listoftodo.get(i));
				dateheader.setText(dateformat);
				dateheader.setBackgroundColor(Color.rgb(183, 175, 163));
				lowprioritylist.addView(dateheader);
				tempdate = testnewdate;	
			}
			
		final int idoftodo = Integer.parseInt(listoftodo.get(i).get(0).toString());
		LinearLayout todoitem = new LinearLayout(this);
		Button removebutton = new Button(this);
		removebutton.setGravity(Gravity.LEFT);
		LayoutParams removebuttonparams = new  LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		removebuttonparams.setMargins(0, 0, -11, 0);
		removebutton.setLayoutParams(removebuttonparams);
		removebutton.setText(" X");
		Button itemsinlist = new Button(this);
		LayoutParams itemsinlistparam = new  LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		itemsinlistparam.setMargins(0, 0, -11, 0);
		itemsinlistparam.weight = 3f;
		itemsinlist.setLayoutParams(itemsinlistparam);
		itemsinlist.setText(listoftodo.get(i).get(1).toString());
		itemsinlist.setGravity(Gravity.LEFT);
		itemsinlist.setLines(1);
		itemsinlist.setOnClickListener( new OnClickListener() {
			@Override
			  public void onClick(View v) 
			  {
				
				   EditToDoItem(v,idoftodo);
				 
			  }    
		});
		removebutton.setOnClickListener( new OnClickListener() {
			@Override
			  public void onClick(View v) 
			  {
				
				   removeTODOItem(idoftodo);
				 
			  }    
		});
		Button timebutton = new Button(this);
		//timebutton.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
		timebutton.setGravity(Gravity.LEFT);
		timebutton.setTextSize(12);
		LayoutParams timebuttonparam = new  LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		timebuttonparam.height = itemsinlistparam.height;
		timebuttonparam.setMargins(0, 2, 0, 0);
		timebutton.setLayoutParams(timebuttonparam);
		timebutton.setGravity(Gravity.CENTER_VERTICAL);
		String minute = listoftodo.get(i).get(4).toString();
		int minval= Integer.parseInt(minute);
		if(minval<10){
			minute = "0"+minute;
		}
		timebutton.setText(listoftodo.get(i).get(3).toString()+":"+minute+" " +listoftodo.get(i).get(5).toString());
		todoitem.addView(removebutton);
		todoitem.addView(itemsinlist);
		todoitem.addView(timebutton);
		lowprioritylist.addView(todoitem);
			}
}

		
	}


private void displayHighPriority(final ArrayList<ArrayList<Object>> listoftodo, LinearLayout highprioritylist) {
 
	
	Time tempdate= new Time();
	tempdate.set(0, 0, 0, 0, 0, 0);
	TextView highpriorityheader = new TextView(this);
	highpriorityheader.setText("High Priority");
	highpriorityheader.setBackgroundColor(Color.rgb(255, 0, 0));
	highpriorityheader.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
	
	boolean isheaderdisplayed = false;
	
for( int i = 0; i < listoftodo.size(); i++)
{
	if(listoftodo.get(i).get(9).toString().equals("High")){
		if(isheaderdisplayed == false){
			highprioritylist.addView(highpriorityheader);
			isheaderdisplayed = true;
		}
			Time testnewdate = new Time();
			testnewdate.set(Integer.parseInt(listoftodo.get(i).get(6).toString()),
							Integer.parseInt(listoftodo.get(i).get(7).toString()),
							Integer.parseInt(listoftodo.get(i).get(8).toString()));
			if(testnewdate.monthDay != tempdate.monthDay || testnewdate.month != tempdate.month || testnewdate.year != tempdate.year){
				TextView dateheader = new TextView(this);
				String dateformat = changeToDateFormat(listoftodo.get(i));
				dateheader.setBackgroundColor(Color.rgb(183, 175, 163));
				dateheader.setText(dateformat);
				highprioritylist.addView(dateheader);
				tempdate = testnewdate;	
			}
			
		
		final int idoftodo = Integer.parseInt(listoftodo.get(i).get(0).toString());
		LinearLayout todoitem = new LinearLayout(this);
		Button removebutton = new Button(this);
		//removebutton.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
		removebutton.setGravity(Gravity.LEFT);
		removebutton.setText(" X");
		LayoutParams removebuttonparams = new  LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		removebuttonparams.setMargins(0, 0, -11, 0);
		removebutton.setLayoutParams(removebuttonparams);
		Button itemsinlist = new Button(this);
		//itemsinlist.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 3f));
		LayoutParams itemsinlistparam = new  LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		itemsinlistparam.setMargins(0, 0, -11, 0);
		itemsinlistparam.weight = 3f;
		itemsinlist.setLayoutParams(itemsinlistparam);
		itemsinlist.setText(listoftodo.get(i).get(1).toString());
		itemsinlist.setGravity(Gravity.LEFT);
		itemsinlist.setLines(1);
		itemsinlist.setOnClickListener( new OnClickListener() {
			@Override
			  public void onClick(View v) 
			  {
				
				   EditToDoItem(v,idoftodo);
				 
			  }    
		});
		removebutton.setOnClickListener( new OnClickListener() {
			@Override
			  public void onClick(View v) 
			  {
				
				   removeTODOItem(idoftodo);
				 
			  }    
		});
		Button timebutton = new Button(this);
		//timebutton.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
		timebutton.setGravity(Gravity.LEFT);
		timebutton.setTextSize(12);
		LayoutParams timebuttonparam = new  LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		timebuttonparam.height = itemsinlistparam.height;
		String minute = listoftodo.get(i).get(4).toString();
		int minval= Integer.parseInt(minute);
		if(minval<10){
			minute = "0"+minute;
		}
		timebuttonparam.setMargins(0, 2, 0, 0);
		timebutton.setLayoutParams(timebuttonparam);
		timebutton.setGravity(Gravity.CENTER_VERTICAL);
		timebutton.setText(listoftodo.get(i).get(3).toString()+":"+minute+" " +listoftodo.get(i).get(5).toString());
		todoitem.addView(removebutton);
		todoitem.addView(itemsinlist);
		todoitem.addView(timebutton);
		highprioritylist.addView(todoitem);}
		}
	}
			




private String changeToDateFormat(ArrayList<Object> dateitem) {
String dateformat;
String day = "";
String month;
Time todaysdate = new Time();
todaysdate.setToNow();
Time selecteddate = new Time();
selecteddate.set(Integer.parseInt(dateitem.get(6).toString()), Integer.parseInt(dateitem.get(7).toString()), Integer.parseInt(dateitem.get(8).toString()));
if(todaysdate.monthDay == selecteddate.monthDay && todaysdate.month == selecteddate.month && todaysdate.YEAR == selecteddate.YEAR){
day = "Today";
}


switch(Integer.parseInt(dateitem.get(7).toString())){
case 0:
	month = "January";
	break;
case 1:
	month = "Febuary";
	break;
case 2:
	month = "March";
	break;
case 3:
	month = "April";
	break;
case 4:
	month = "May";
	break;
case 5:
	month = "June";
	break;
case 6:
	month = "July";
	break;
case 7:
	month = "August";
	break;
case 8:
	month = "September";
	break;
case 9:
	month = "October";
	break;
case 10:
	month = "November";
	break;
case 11:
	month = "December";
	break;
default:
	month = "";
	break;
}

if(day.equals("Today")){
	dateformat = day;
}else{
	String dayval = dateitem.get(6).toString();
dateformat = month + " " + dayval +" "+ dateitem.get(8).toString();
}

return dateformat;

}


protected void removeTODOItem(int idoftodo) {
	 TODODatabaseManager db = new TODODatabaseManager(this);
	    db.open();
	    ArrayList<Object> listoftodo2; 
	    listoftodo2 = db.getRowAsArray(idoftodo);
	    Toast toast = Toast.makeText(this,listoftodo2.get(1).toString() , Toast.LENGTH_SHORT);
	    db.deleteRow(idoftodo);
	    db.close();
	    this.onCreate(null);
}


// sort TODOList by date; Insertion sort
private ArrayList<ArrayList<Object>> sortTODOList(ArrayList<ArrayList<Object>> listoftodo) {
		ArrayList<ArrayList<Object>> newtodolist;
		int lowestdateindex;
		ArrayList<Object> templowest;
		//get current date value to compare which date is the closest.
		
		
		int hourvalue;
		for(int i = 0; i < listoftodo.size(); i++){
			Time lowestdate = new Time();
			lowestdateindex = i;
			templowest = listoftodo.get(i);
			//check if its am or pm
			if(listoftodo.get(i).get(5).toString().equals("am")){
				hourvalue = (Integer.parseInt(listoftodo.get(i).get(3).toString()));
				}
				else hourvalue = (Integer.parseInt(listoftodo.get(i).get(3).toString())) +12;
				lowestdate.set(1,
							Integer.parseInt(listoftodo.get(i).get(4).toString()),
							hourvalue,
							Integer.parseInt(listoftodo.get(i).get(6).toString()), 
							Integer.parseInt(listoftodo.get(i).get(7).toString()), 
							Integer.parseInt(listoftodo.get(i).get(8).toString())
						   );
			
	
			for (int x  = i +1; x < listoftodo.size(); x++)
			{
				Time tempdate = new Time();
				if(listoftodo.get(x).get(5).toString().equals("am")){
					hourvalue = (Integer.parseInt(listoftodo.get(x).get(3).toString()));
					}
					else hourvalue = (Integer.parseInt(listoftodo.get(x).get(3).toString())) +12;
			tempdate.set(1,
						Integer.parseInt(listoftodo.get(x).get(4).toString()),
						hourvalue,
						Integer.parseInt(listoftodo.get(x).get(6).toString()), 
						Integer.parseInt(listoftodo.get(x).get(7).toString()), 
						Integer.parseInt(listoftodo.get(x).get(8).toString()));
			
				if(tempdate.before(lowestdate)){
					lowestdate = tempdate;
					//swap list at i and x
					listoftodo.set(i, listoftodo.get(x));
					listoftodo.set(x, templowest);
					//temp lowest is now lowest value
					templowest = listoftodo.get(i);
				}
				
				}
			}
			
		return listoftodo;
	}


public void createNewToDo(View v){
	Intent intent = new Intent(this, CreateNewToDoActivity.class);
	startActivity(intent);
}

public void EditToDoItem(View v, int editid){
	Intent intent = new Intent(this, EditToDoActivity.class);
	Bundle extras = new Bundle();
	extras.putInt("editidval", editid);
	intent.putExtras(extras);
	startActivity(intent);
	
}

}


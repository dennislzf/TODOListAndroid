package com.example.taskstodo;

import java.util.ArrayList;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.taskstodo.persistence.TODODatabaseManager;

@SuppressLint("ValidFragment")
public class MainActivity extends Activity {

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ArrayList<ArrayList<Object>> listoftodo;
		TODODatabaseManager db = new TODODatabaseManager(this);
		db.open();
		listoftodo = db.getAllRowsAsArrays();
		db.close();
		//check if there are todoitems
		if(listoftodo.size() > 0){
			listoftodo = sortTODOList(listoftodo);
			displayHighPriority(listoftodo);
			
			
			
		}else{
			ScrollView todoscrolllist = (ScrollView) findViewById(R.id.TableScroller);
			TextView noitemsinlist = new TextView(this);
			noitemsinlist.setText("No items To Do!!");
			todoscrolllist.addView(noitemsinlist);
			
		}
		
		
	
		
	}
	

private void displayHighPriority(ArrayList<ArrayList<Object>> listoftodo) {
//		
//	ScrollView todoscrolllist = (ScrollView) findViewById(R.id.TableScroller);
//	
//	for(int i = 0; i < listoftodo.size(); i++){
//	TextView noitemsinlist = new TextView(this);
//	noitemsinlist.setText(listoftodo.get(i).get(1).toString());
//	todoscrolllist.addView(noitemsinlist);
//	}
	
}

// sort TODOList by date; Insertion sort
private ArrayList<ArrayList<Object>> sortTODOList(ArrayList<ArrayList<Object>> listoftodo) {
		ArrayList<ArrayList<Object>> newtodolist;
		int lowestdateindex;
		ArrayList<Object> templowest;
		//date value is used to compare date values to show whether or not an item in the todo list should appear before another... calculated as minutes
		//away from present time. 
		long datevalue;
		long tempdatevalue;
		//get current date value to compare which date is the closest.
		Date tempdate;
		Date lowestdate;
		

		int hourvalue;
		
		for(int i = 0; i < listoftodo.size(); i++){
			lowestdateindex = i;
			templowest = listoftodo.get(i);
			//check if its am or pm
			if(listoftodo.get(i).get(5).toString().equals("am")){
				hourvalue = (Integer.parseInt(listoftodo.get(i).get(3).toString()));
				}
				else hourvalue = (Integer.parseInt(listoftodo.get(i).get(3).toString())) +12;
			lowestdate = new Date(Integer.parseInt(listoftodo.get(i).get(8).toString()), 
						   Integer.parseInt(listoftodo.get(i).get(7).toString()), 
						   Integer.parseInt(listoftodo.get(i).get(6).toString()), 
						   hourvalue, 
						   Integer.parseInt(listoftodo.get(i).get(4).toString()));
			
	
			for (int x  = i +1; x < listoftodo.size(); x++)
			{
				if(listoftodo.get(x).get(5).toString().equals("am")){
					hourvalue = (Integer.parseInt(listoftodo.get(x).get(3).toString()));
					}
					else hourvalue = (Integer.parseInt(listoftodo.get(x).get(3).toString())) +12;
			tempdate = new Date(Integer.parseInt(listoftodo.get(x).get(8).toString()), 
						   Integer.parseInt(listoftodo.get(x).get(7).toString()), 
						   Integer.parseInt(listoftodo.get(x).get(6).toString()), 
						   hourvalue, 
						   Integer.parseInt(listoftodo.get(x).get(4).toString()));
			
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

}


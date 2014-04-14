/*
 * NotepadApp  -> Android Notepad Application which performs basic functionalities of notepad
 * 				  such as creating, saving and loading of Android Text files 
 */

package com.androidx.notepad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.zip.Inflater;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ActionBar.OnNavigationListener;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/*
 * NotepadActivity -> Main Activity for the Notepad App
 * 
 * Authors  ->  Basant Khati
 * 				Dinesh Appavoo
 * 				Srivatsan Varadarajan
 */

public class NotepadActivity extends ActionBarActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static final int POSITION_ZERO = 0;
	private static String UNTITLED = "Untitled";
	
	
	String[] MenuOptions;
	EditText text;
	int firstNavigation = 0;
	String FileName;
	View fileTitle;
	ActionBar actionBar;
	FileOutputStream fileOutputStream;
	View fileMenuName;
	int i=5;
	File recentFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notepad_layout);

		//storing Options in String array
		MenuOptions = getResources().getStringArray(R.array.MenuOptions);
		
		//setting the cursor in the top position
		text =  (EditText)findViewById(R.id.TEXT);
		text.setSelection(POSITION_ZERO);
		
		
		
		File file1 = Environment.getExternalStorageDirectory().getAbsoluteFile();
		File file2 = new File(file1,"/RecentFileHistory"); 
		if(!file2.exists())
			file2.mkdirs();
		
		recentFile = new File(file2,"RecentFile.txt");
		if(!recentFile.exists())
			try {
				recentFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//System.out.println(text.getText().toString());
		//Toast.makeText(getApplicationContext(), text.getText().toString(), Toast.LENGTH_LONG).show();
		//fileTitle = findViewById(R.id.fileName);
		
		// Set up the action bar to show a dropdown list.
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		actionBar.setTitle(UNTITLED);
		
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, MenuOptions), this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notepad, menu);
		
		SubMenu subMenu = menu.addSubMenu(R.id.recent_files);
		subMenu.clear();
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id)
		{
		case R.id.recent_files:{
			//Activity here
			//through activity call the file, then get the last 5 contents from Set  
			RecentFileListDisplay();
		}
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * Module to call recentfilelistactivity.java
	 * 
	 * Author  -> Srivatsan Varadarajan
	 */
	
	private void RecentFileListDisplay() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(NotepadActivity.this,RecentFileListActivity.class);
		startActivityForResult(intent,200);
	}

	
	//Menu Options
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		
		
		System.out.println("Position given:"+position);
		switch(position){
		case 0:{
			text.setEnabled(true);
			text.setFocusable(true);
			
			break;
		}
		
			case 1:{
				//Toast.makeText(getApplicationContext(), "Selected ", Toast.LENGTH_SHORT).show();
				createNotepad(this);
				break;
				}
		
				case 2:{
				//Toast.makeText(getApplicationContext(), "Selected ", Toast.LENGTH_SHORT).show();
				OpenFile();
				
				break;
				}
				
				case 3:{
					//Toast.makeText(getApplicationContext(), "In Save ", Toast.LENGTH_SHORT).show();
					SavingFile();
					break;
					
				}
				
				case 4:{
				//Toast.makeText(getApplicationContext(), "Save As ",Toast.LENGTH_SHORT).show();	
				//Toast.makeText(getApplicationContext(), "Text: "+text.getText().toString(),Toast.LENGTH_SHORT).show();	
				saveAsFile();
				break;
				}
					
				case 5:{
					//Toast.makeText(getApplicationContext(), "Exit" , Toast.LENGTH_SHORT).show();
					Exit();
					
				}
				
		
		}
		
		actionBar.setSelectedNavigationItem(0);
		
		return true;
	}
 	
	/*
	 * Save File in External SD
	 * 
	 * Author   ->    Dinesh Appavoo
	 * 
	 */
	private void SavingFile() {
		// TODO Auto-generated method stub
		
		if(actionBar.getTitle().equals(UNTITLED))
		{
			saveAsFile();
		}
		else
		{
			deleteFile(FileName);
			saveFile(FileName);
		}
		
		
	}

	private void saveAsFile() {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(NotepadActivity.this);
		final View promptsView = li.inflate(R.layout.notepad_save_as_dialog_layout, null);
		AlertDialog.Builder saveAlert = new AlertDialog.Builder(NotepadActivity.this);
		saveAlert.setView(promptsView);
		
	
		saveAlert.setTitle("Save File")
		.setMessage("Save the file As")
		.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
				EditText fname = (EditText) promptsView.findViewById(R.id.saveText1);
				if(!fname.getText().toString().contains("."))
					FileName = fname.getText().toString()+".txt";
				else
					FileName = fname.getText().toString();
				//Toast.makeText(getApplicationContext(), "fname:"+fname.toString() , Toast.LENGTH_SHORT).show();
				//Toast.makeText(getApplicationContext(), "FileName:"+FileName , Toast.LENGTH_SHORT).show();
				saveFile(FileName);
			}
		})
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
			dialog.cancel();
			}
		}).create().show();
		
	}
	
	
	private void saveFile(String fileName) {
		// TODO Auto-generated method stub
		try {
			
			
	        if (isSdReadable()) {
	            File fullPath = Environment.getExternalStorageDirectory();
	            File fullPath1 = new File(fullPath,"/notepadAppDirectory");
	            if(!fullPath1.exists()){
	            	fullPath1.mkdirs();}
	            FileName = fileName;
	            
	            
	            text =  (EditText)findViewById(R.id.TEXT);
	           
	            
	            
	            //Toast.makeText(getApplicationContext(), FileName, Toast.LENGTH_SHORT).show();
	            //Toast.makeText(getApplicationContext(), text.getText().toString(), Toast.LENGTH_LONG).show();
	            File myFile = new File(fullPath1,FileName);
	             
	            if (!myFile.exists()) 
	            {    
	                myFile.createNewFile();
	            } 
	            //text.setText("Hi hello");
	            //Toast.makeText(getApplicationContext(), "Text: "+text.getText().toString(), Toast.LENGTH_LONG).show();
	            FileOutputStream recentFileWrite = new FileOutputStream(recentFile,true);
	            
	            FileOutputStream fOut = new FileOutputStream(myFile);	
	            actionBar.setTitle(FileName);
	            //OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
	            fOut.write(text.getText().toString().getBytes());
	            Toast.makeText(getApplicationContext(), "File Saved", Toast.LENGTH_SHORT).show();
	            recentFileWrite.write((myFile.getPath()+"\n").getBytes());
	            fOut.close();
	           
	            //Toast.makeText(getBaseContext(), "File Saved in External SD", Toast.LENGTH_SHORT).show();
	        }
		}
		
		catch (Exception e) {
	        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT)
	                .show();
	    }
	}
		
	
	public boolean isSdReadable() {

	    boolean mExternalStorageAvailable = false;
	    try {
	        String state = Environment.getExternalStorageState();

	        if (Environment.MEDIA_MOUNTED.equals(state)) {
	            // We can read and write the media
	            mExternalStorageAvailable = true;
	            Log.i("isSdReadable", "External storage card is readable.");
	        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	            // We can only read the media
	            Log.i("isSdReadable", "External storage card is readable.");
	            mExternalStorageAvailable = true;
	        } else {
	            // Something else is wrong. It may be one of many other
	            // states, but all we need to know is we can neither read nor
	            // write
	            mExternalStorageAvailable = false;
	        }
	    } catch (Exception ex) {

	    }
	    return mExternalStorageAvailable;
	}
	
	
	
	
	/*
	 * Exit the Application
	 * 
	 * Author -> Srivatsan Varadarajan
	 */

	private void Exit() {
		// TODO Auto-generated method stub
		AlertDialog.Builder exitAlert = new AlertDialog.Builder(NotepadActivity.this);
		exitAlert.setTitle("Close Application")
		.setMessage("Do you want to close")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
				NotepadActivity.this.finish();
				System.exit(POSITION_ZERO);
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// TODO Auto-generated method stub
			dialog.cancel();
			}
		}).create().show();
		
		
		
	}
	
	
	/*
	 * Open the file from the directory
	 * 
	 * Author  ->  Basant Khati
	 */

	private void OpenFile() {
		// TODO Auto-generated method stub
		Intent intentOpen = new Intent(NotepadActivity.this,OpenFileList.class);
		startActivityForResult(intentOpen,100);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0 == 100){
			String fileName2 = arg2.getExtras().getString("fileOpen");
			OpenFileFromSD(fileName2);
		}
		
		else if(arg0 == 200){
			String fileName3 = arg2.getExtras().getString("fileSelected");
			OpenFileFromSD(fileName3);
		}
		
	}
	
	private void OpenFileFromSD(String fileName2) {
		// TODO Auto-generated method stub
		try {
			File abc = new File(fileName2);
			FileInputStream fileInputStream = new FileInputStream(new File(fileName2));
			BufferedReader myReader = new BufferedReader(new InputStreamReader(fileInputStream));
			FileOutputStream fos = new FileOutputStream(recentFile,true);
			fos.write((abc.getAbsolutePath().concat("\n")).getBytes());
	        String aDataRow = "";
	        String aBuffer = "";
	        while ((aDataRow = myReader.readLine()) != null) {
	            aBuffer += aDataRow + "\n";
	        }
	        text.setText(aBuffer);
	        myReader.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void createNotepad(NotepadActivity notepadActivity) {
		
		// TODO Auto-generated method stub
		text.setText("");
		actionBar.setTitle(UNTITLED);
		//System.out.println(text.getText().toString());
		this.setContentView(R.layout.notepad_layout);
	}

	

}

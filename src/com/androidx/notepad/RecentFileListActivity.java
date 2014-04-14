package com.androidx.notepad;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/*
 * Recent File List Activity -> To list last 5 recent files
 * 
 * Author: Srivatsan Varadarajan
 * 
 */

public class RecentFileListActivity extends ListActivity {

	FileInputStream fileReader;
	ArrayList<String> fileArrayList;
	String texts;
	String[] fileStringArray;
	String[] fileStringArrayWithoutPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		try {
			fileReader = new FileInputStream(Environment.getExternalStorageDirectory()+"/RecentFileHistory"+"/RecentFile.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(fileReader));
			fileArrayList = new ArrayList<String>();
			try {
				while((texts = br.readLine())!=null)
				{
					fileArrayList.add(texts);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Set<String> fileListSet = new LinkedHashSet<String>();
		
		for(int i = fileArrayList.size()-1; i > 0; i--)
		{
			if(fileListSet.size()!=5)
			{
				fileListSet.add(fileArrayList.get(i));
			}
			else
				break;
		}
		
		fileStringArray = new String[fileListSet.size()];
		
		int i=0;
		for(String s: fileListSet){
			fileStringArray[i++] = s;
		}
		
		int j=0;
		 fileStringArrayWithoutPath = new String[fileListSet.size()];
		 for(String s: fileListSet){
			 String test = s;
			 int position = test.lastIndexOf('/');
			 if(position!=-1)
				 fileStringArrayWithoutPath[j++] = test.substring(position+1);
			 
		 }
		
		ArrayAdapter<String> fileList =	new ArrayAdapter<String>(this, R.layout.row, fileStringArrayWithoutPath);
		setListAdapter(fileList);
		
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String fileName = fileStringArrayWithoutPath[position];
		String fileName2 = fileStringArray[position];
		Intent i = new Intent(RecentFileListActivity.this, NotepadActivity.class);
		i.putExtra("fileSelected", fileName2);
		setResult(200, i);
		finish();
		
	}
	
	
}

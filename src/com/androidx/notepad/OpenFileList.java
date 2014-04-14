package com.androidx.notepad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * To Open files from the directory
 * 
 * Author : Basant Khati
 * 
 */

public class OpenFileList extends ListActivity {

	private List<String> item = null;
	private List<String> path = null;
	String folder = Environment.getExternalStorageDirectory().getAbsolutePath();
	String root1 = folder + "/notepadAppDirectory";
	private String root = "/";
	private TextView myPath;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.filelist);
		
		myPath = (TextView) findViewById(R.id.path);
		getDir(root1);

	}

	private void getDir(String dirPath)

	{

		myPath.setText("Location: " + dirPath);
		item = new ArrayList<String>();
		path = new ArrayList<String>();
		File f = new File(dirPath);
		File[] files = f.listFiles();

		if (!dirPath.equals(root))
		{
			item.add(root);
			path.add(root);
			item.add("../");
			path.add(f.getParent());
		}

		for (int i = 0; i < files.length; i++)
		{
			File file = files[i];
			path.add(file.getPath());
			if (file.isDirectory())
				item.add(file.getName() + "/");
			else
				item.add(file.getName());
		}

		ArrayAdapter<String> fileList =	new ArrayAdapter<String>(this, R.layout.row, item);
		setListAdapter(fileList);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final File file = new File(path.get(position));
		if (file.isDirectory())
		{
			if (file.canRead())
				getDir(path.get(position));
			else
			{
				new AlertDialog.Builder(this)
				.setTitle("[" + file.getName() + "] folder can't be read!")
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						// TODO Auto-generated method stub

					}

				}).show();
			}
		}

		else
		{
			new AlertDialog.Builder(this)
			.setTitle("[" + file.getName() + "]")
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					// TODO Auto-generated method stub
					//Toast.makeText(getApplicationContext(), file.getAbsoluteFile().toString(), Toast.LENGTH_SHORT).show();
					Intent fileOpen = new Intent(OpenFileList.this,NotepadActivity.class);
					fileOpen.putExtra("fileOpen", file.getAbsolutePath().toString());
					setResult(100,fileOpen);
					finish();
				}

			}).show();

		}

	}

}

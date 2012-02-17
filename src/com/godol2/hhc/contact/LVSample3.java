package com.godol2.hhc.contact;

import java.util.ArrayList;
import java.util.List;

import com.godol2.hhc.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LVSample3 extends Activity implements OnClickListener {

	private Button btnSearch;
	private Button btnAdd;
	private Button btnDelete;

	private ListView lv;
	private List<LVSample3Item> dataSources;
	private ListAdapter adapter;

	private DialogInterface.OnClickListener deleteYesListener;
	private DialogInterface.OnClickListener deleteNoListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.contact_main);

		// For Buttons
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);

		btnAdd = (Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);

		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);

		// For Custom ListView
		dataSources = getDataSource();
		adapter = new LVSample3Adapter(dataSources, this);
		this.lv = (ListView) findViewById(R.id.listview);
		this.lv.setAdapter(adapter);
		this.lv.setItemsCanFocus(false);
		this.lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		this.lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapt, View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView tvTitle = (TextView) view.findViewById(R.id.title);
				TextView tvSummary = (TextView) view.findViewById(R.id.summary);
				String message = "Title: " + tvTitle.getText() + "\n" + "Summary:" + tvSummary.getText();
				ShowMessageBox(LVSample3.this, message);
			}
		});

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnSearch) {
			// long[] checkedItems = this.lv.getCheckItemIds();
			Long[] checkedItems = ((LVSample3Adapter) adapter).getCheckItemIds();
			if (checkedItems == null || checkedItems.length == 0) {
				ShowMessageBox(this, "Selected Items is Nothing.");
				return;
			}

			String message = "";
			for (int index = 0; index < checkedItems.length; index++) {
				long pos = checkedItems[index];
				LVSample3Item item = dataSources.get((int) pos);
				message += String.format("%d[%s, %s]\n", pos, item.getTitle(), item.getSummary());
			}
			ShowMessageBox(this, message);

		} else if (view.getId() == R.id.btnAdd) {
			addDataSource(this.dataSources);
			((LVSample3Adapter) adapter).notifyDataSetChanged();
			ShowMessageBox(this, String.format("All items count is %d.", dataSources.size()));
		} else if (view.getId() == R.id.btnDelete) {
			// long[] checkedItems = this.lv.getCheckItemIds();
			Long[] checkedItems = ((LVSample3Adapter) adapter).getCheckItemIds();
			if (checkedItems == null || checkedItems.length == 0) {
				ShowMessageBox(this, "Selected items is Nothing.");
				return;
			}

			// Dialog Setting
			deleteYesListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// long[] checkedItems = lv.getCheckItemIds();
					Long[] checkedItems = ((LVSample3Adapter) adapter).getCheckItemIds();

					for (int index = checkedItems.length - 1; index >= 0; index--) {
						long pos = checkedItems[index];
						dataSources.remove((int) pos);
					}
					// lv.clearChoices();
					((LVSample3Adapter) adapter).clearChoices();
					((LVSample3Adapter) adapter).notifyDataSetChanged();
					ShowMessageBox(LVSample3.this, String.format("All items count is %d.", dataSources.size()));
				}
			};

			deleteNoListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			};

			ShowMessageYesNo(this, "Delete", "Are you sure you want to delete selected item(s)?", deleteYesListener,
					deleteNoListener);
		}
	}

	private static List<LVSample3Item> getDataSource() {
		List<LVSample3Item> lstItems = new ArrayList<LVSample3Item>();

		for (int i = 0; i < 20; i++) {
			LVSample3Item item = new LVSample3Item("Action" + i, "This is a test mssage.");
			lstItems.add(item);
		}
		return lstItems;
	}

	private static List<LVSample3Item> addDataSource(List<LVSample3Item> dataSources) {
		LVSample3Item item = new LVSample3Item("Added" + dataSources.size(), "This is a added mssage.");
		dataSources.add(item);

		return dataSources;
	}

	private static void ShowMessageBox(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void ShowMessageYesNo(Context context, String title, String message,
			DialogInterface.OnClickListener YesListener, DialogInterface.OnClickListener NoListener) {
		AlertDialog.Builder alterBuilder = new AlertDialog.Builder(context);
		alterBuilder.setMessage(message).setCancelable(false).setPositiveButton(android.R.string.yes, YesListener)
				.setNegativeButton(android.R.string.no, NoListener);
		AlertDialog alert = alterBuilder.create();
		// Title for AlertDialog
		alert.setTitle(title);
		// Icon for AlertDialog
		alert.setIcon(R.drawable.icon);
		alert.show();
	}
}

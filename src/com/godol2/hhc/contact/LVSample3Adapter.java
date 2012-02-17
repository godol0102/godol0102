package com.godol2.hhc.contact;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.godol2.hhc.R;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LVSample3Adapter extends BaseAdapter {

	private List<LVSample3Item> itemList;
	private Context context;
	public Hashtable<Integer, View> hashConvertView = new Hashtable<Integer, View>();

	public LVSample3Adapter(List<LVSample3Item> itemList, Context context) {
		this.itemList = itemList;
		this.context = context;
	}

	public int getCount() {
		return itemList.size();
	}

	public LVSample3Item getItem(int position) {
		return itemList.get(position);
	}

	public long getItemId(int position) {
		return itemList.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LVSample3Item item = itemList.get(position);

		ViewHolder holder;
		if (hashConvertView.containsKey(position) == false) {
			convertView = (CheckableRelativeLayout) LayoutInflater.from(context).inflate(
					R.layout.my_list_item3, parent, false);
			holder = new ViewHolder();

			holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			holder.tvSummary = (TextView) convertView.findViewById(R.id.summary);
			holder.chk = (CheckBox) convertView.findViewById(R.id.check);
			holder.chk.setId(position);
			holder.chk.setOnClickListener(listener);

			convertView.setTag(holder);
			hashConvertView.put(position, convertView);
		} else {
			convertView = (View) hashConvertView.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvTitle.setText(item.getTitle());
		holder.tvSummary.setText(item.getSummary());

		return convertView;
	}

	private final OnClickListener listener = new View.OnClickListener() {
		public void onClick(View v) {
			if (v instanceof CheckBox) {
				CheckBox chk = (CheckBox) v;

				// switch (chk.getId()) {
				// }
				notifyDataSetChanged();
				// notifyDataSetInvalidated();
			}
		}
	};

	public Long[] getCheckItemIds() {
		if (hashConvertView == null || hashConvertView.size() == 0)
			return null;

		Integer key;
		View view;
		List<Long> lstIds = new ArrayList<Long>();
		Enumeration<Integer> e = hashConvertView.keys();
		long index = 0;
		while (e.hasMoreElements()) {
			key = (Integer) e.nextElement();
			view = (View) hashConvertView.get(key);
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder.chk.isChecked()) {
				lstIds.add(index);
			}
			index++;
		}

		Long[] arrLong = (Long[]) lstIds.toArray(new Long[0]);
		return arrLong;
	}

	public void clearChoices() {
		if (hashConvertView == null || hashConvertView.size() == 0)
			return;

		Integer key;
		View view;
		List<Long> lstIds = new ArrayList<Long>();
		Enumeration<Integer> e = hashConvertView.keys();
		long index = 0;
		while (e.hasMoreElements()) {
			key = (Integer) e.nextElement();
			view = (View) hashConvertView.get(key);
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder.chk.isChecked()) {
				holder.chk.setChecked(false);
			}
			index++;
		}
	}

	static class ViewHolder {
		CheckBox chk;
		TextView tvTitle;
		TextView tvSummary;
	}
}

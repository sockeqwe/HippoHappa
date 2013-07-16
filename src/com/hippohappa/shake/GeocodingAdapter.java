package com.hippohappa.shake;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hippohappa.HippoBaseAdapter;
import com.hippohappa.R;
import com.hippohappa.model.google.GeoResult;
import com.hippohappa.model.google.GeocodingResult;

/**
 * An adapter to present the {@link GeocodingResult}
 * 
 * @author Hannes Dorfmann
 * 
 */
public class GeocodingAdapter extends HippoBaseAdapter {

	private class ViewHolder {
		private final TextView text;

		public ViewHolder(View v) {
			text = (TextView) v.findViewById(R.id.text);
		}
	}

	private List<GeoResult> results;

	public GeocodingAdapter(Context context) {
		super(context);
	}

	public void setGeoResults(List<GeoResult> results) {
		this.results = results;
	}

	@Override
	public int getCount() {
		return results == null ? 0 : results.size();
	}

	@Override
	public GeoResult getItem(int position) {
		return results.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View newView(int type, ViewGroup parent) {
		View v = inflater.inflate(R.layout.list_geocoding_result_item, parent,
				false);
		v.setTag(new ViewHolder(v));

		return v;
	}

	@Override
	public void bindView(int position, int type, View view) {

		ViewHolder vh = (ViewHolder) view.getTag();
		vh.text.setText(results.get(position).getTitle());

	}

}

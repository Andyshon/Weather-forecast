package com.andyshon.weather_forecast.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.databinding.RowItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andyshon on 29.07.18.
 */

public class MapsListAdapter extends BaseAdapter implements Filterable {

    private List<String> mData;
    private List<String> mStringFilterList;
    private ValueFilter valueFilter;
    private LayoutInflater inflater;
    private Context context;


    public MapsListAdapter(Context context, List<String> cancel_type) {
        this.context = context;
        mData=cancel_type;
        mStringFilterList = cancel_type;
    }


    @Override
    public int getCount() {
        return mData.size();
    }


    @Override
    public String getItem(int position) {
        return mData.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        RowItemBinding rowItemBinding = DataBindingUtil.inflate(inflater, R.layout.row_item, parent, false);
        rowItemBinding.stringName.setText(mData.get(position));

        rowItemBinding.rowCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newCity = mData.get(position).trim();
                GlobalConstants.setCurrentCity(newCity);
                ((MapsActivity)context).findCityByName(newCity);
            }
        });

        return rowItemBinding.getRoot();
    }


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<String> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                ((MapsActivity)context).setMapFragment();
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count != 0) {
                mData = (List<String>) results.values;
                notifyDataSetChanged();
            }
        }
    }

}
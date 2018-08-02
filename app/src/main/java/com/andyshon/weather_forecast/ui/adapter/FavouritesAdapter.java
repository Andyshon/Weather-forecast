package com.andyshon.weather_forecast.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.databinding.FavouritesItemBinding;
import com.andyshon.weather_forecast.data.entity.weather_today_forecast.WeatherTodayForecast;
import com.andyshon.weather_forecast.utils.WeatherUtils;

import java.util.List;

/**
 * Created by andyshon on 30.07.18.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    private List<WeatherTodayForecast> mFavouritesList;

    private final FavouriteClickCallback callback;


    public interface FavouriteClickCallback {
        void onClick(WeatherTodayForecast weatherToday);
        void onLongClick(WeatherTodayForecast weatherToday);
    }


    public FavouritesAdapter(FavouriteClickCallback callback) {
        this.callback = callback;
    }


    /*public void setFavouritesList(final List<WeatherTodayForecast> favouritesList) {
        mFavouritesList = favouritesList;
        notifyDataSetChanged();
    }*/
    public void setFavouritesList(final List<WeatherTodayForecast> favouritesList) {
        if (mFavouritesList == null) {
            mFavouritesList = favouritesList;
            notifyItemRangeInserted(0, favouritesList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mFavouritesList.size();
                }

                @Override
                public int getNewListSize() {
                    return favouritesList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mFavouritesList.get(oldItemPosition).getId() == favouritesList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    WeatherTodayForecast newW = favouritesList.get(newItemPosition);
                    WeatherTodayForecast oldW = mFavouritesList.get(oldItemPosition);
                    return newW.getId() == oldW.getId()
                            && newW.getItems().get(0).getDescription().description.equals(oldW.getItems().get(0).getDescription().description)
                            && newW.getItems().get(0).getTempDay().equals(oldW.getItems().get(0).getTempDay())
                            && newW.getItems().get(0).getTempMax().equals(oldW.getItems().get(0).getTempMax())
                            && newW.getItems().get(0).getTempMin().equals(oldW.getItems().get(0).getTempMin())
                            && newW.getItems().get(0).getHumidity().equals(oldW.getItems().get(0).getHumidity());
                }
            });
            mFavouritesList = favouritesList;
            result.dispatchUpdatesTo(this);
        }
    }


    @Override
    public FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FavouritesItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.favourites_item, parent, false);

        return new FavouritesViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(FavouritesViewHolder holder, int position) {
        holder.binding.setFavourites(mFavouritesList.get(position));
        holder.binding.name.setText(mFavouritesList.get(position).getItems_city().getCityName());
        holder.binding.tvDate.setText(WeatherUtils.getDateTitle(mFavouritesList.get(position).getItems().get(0).getDt()));
        holder.binding.tvTempCur.setText(String.valueOf(mFavouritesList.get(position).getItems().get(0).getTempDay()).concat("˚"));
        String tempFull = String.valueOf(mFavouritesList.get(position).getItems().get(0).getTempMax()).concat("˚/")
                .concat(String.valueOf(mFavouritesList.get(position).getItems().get(0).getTempMin())).concat("˚");
        holder.binding.tvTempFull.setText(tempFull);
        holder.binding.ivWeatherState.setImageResource(WeatherUtils.getIconByWeatherState(mFavouritesList.get(position).getItems().get(0).getDescription().description,false));

        if (holder.binding.name.getText().toString().trim().equals(GlobalConstants.CURRENT_LOCATION_CITY_EN)) {
            holder.binding.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_black_mylocation_5, 0, 0, 0);
            holder.binding.name.setCompoundDrawablePadding(10);
            holder.binding.name.setPadding(0,0,0,0);
        }

        holder.binding.executePendingBindings();


        holder.binding.layoutItem.setOnClickListener(view -> callback.onClick(mFavouritesList.get(position)));

        holder.binding.layoutItem.setOnLongClickListener(view -> {

            if (mFavouritesList.get(position).getItems_city().getCityName().equals(GlobalConstants.CURRENT_LOCATION_CITY_EN)) return false;
            callback.onLongClick(mFavouritesList.get(position));
            return false;
        });
    }


    @Override
    public int getItemCount() {
        return mFavouritesList == null ? 0 : mFavouritesList.size();
    }

    static class FavouritesViewHolder extends RecyclerView.ViewHolder {

        final FavouritesItemBinding binding;

        public FavouritesViewHolder(FavouritesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

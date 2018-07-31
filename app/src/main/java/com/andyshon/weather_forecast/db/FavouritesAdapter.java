package com.andyshon.weather_forecast.db;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andyshon.weather_forecast.GlobalConstants;
import com.andyshon.weather_forecast.R;
import com.andyshon.weather_forecast.databinding.FavouritesItemBinding;
import com.andyshon.weather_forecast.db.entity.WeatherToday;
import com.andyshon.weather_forecast.utils.WeatherUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by andyshon on 30.07.18.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    List<WeatherToday> mFavouritesList;

    private final FavouriteClickCallback callback;

    public interface FavouriteClickCallback {
        void onClick(WeatherToday weatherToday);
        void onLongClick(WeatherToday weatherToday);
    }

    public FavouritesAdapter(FavouriteClickCallback callback) {
        this.callback = callback;
    }

    public void setFavouritesList(final List<WeatherToday> favouritesList) {
        if (mFavouritesList == null) {
            mFavouritesList = favouritesList;
            System.out.println("HERE:" + mFavouritesList.size());
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
                    WeatherToday newW = favouritesList.get(newItemPosition);
                    WeatherToday oldW = mFavouritesList.get(oldItemPosition);
                    return newW.getId() == oldW.getId()
                            && Objects.equals(newW.getDescription().get(0).getDescription(), oldW.getDescription().get(0).getDescription())
                            && Objects.equals(newW.getTemp().getTemp(), oldW.getTemp().getTemp())
                            && Objects.equals(newW.getTemp().getTemp_max(), oldW.getTemp().getTemp_max())
                            && Objects.equals(newW.getTemp().getTemp_min(), oldW.getTemp().getTemp_min())
                            && Objects.equals(newW.getTemp().getHumidity(), oldW.getTemp().getHumidity());
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
        holder.binding.name.setText(mFavouritesList.get(position).getCity());
        holder.binding.tvDate.setText(WeatherUtils.getDateTitle(mFavouritesList.get(position).getDt()));
        holder.binding.tvTempCur.setText(String.valueOf(mFavouritesList.get(position).getTemp().getTemp().intValue()).concat("˚"));
        String tempFull = String.valueOf(mFavouritesList.get(position).getTemp().getTemp_max().intValue()).concat("˚/")
                .concat(String.valueOf(mFavouritesList.get(position).getTemp().getTemp_min().intValue())).concat("˚");
        holder.binding.tvTempFull.setText(tempFull);
        holder.binding.ivWeatherState.setImageResource(WeatherUtils.getIconByWeatherState(mFavouritesList.get(position).getDescription().get(0).getDescription(),false));

        if (holder.binding.name.getText().toString().trim().equals(GlobalConstants.CURRENT_LOCATION_CITY_EN)) {
            holder.binding.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_black_mylocation_5, 0, 0, 0);
            holder.binding.name.setCompoundDrawablePadding(10);
            holder.binding.name.setPadding(0,0,0,0);
        }

        holder.binding.executePendingBindings();


        holder.binding.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.onClick(mFavouritesList.get(position));
            }
        });

        holder.binding.layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (mFavouritesList.get(position).getCity().equals(GlobalConstants.CURRENT_LOCATION_CITY_EN)) return false;
                callback.onLongClick(mFavouritesList.get(position));
                return false;
            }
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

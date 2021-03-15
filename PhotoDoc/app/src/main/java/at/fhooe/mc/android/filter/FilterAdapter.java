package at.fhooe.mc.android.filter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import at.fhooe.mc.android.EditingActivity;
import at.fhooe.mc.android.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private List<Filter> mFilter;
    private EditingActivity editingActivity;

    public FilterAdapter(List<Filter> _filter, EditingActivity editingActivity) {
    mFilter = _filter;
    this.editingActivity = editingActivity;
    }

    /**
     *  stores and recycles views as they are scrolled off screen
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl;
        ImageView iv;
        TextView tv;

        ViewHolder(View _view) {
            super(_view);
            rl = _view.findViewById(R.id.filter_rl);
            iv = _view.findViewById(R.id.filter_iv);
            tv = _view.findViewById(R.id.filter_tv);
        }
    }

    /**
     *  Create new views (invoked by the layout manager)
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View filterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_editing_filterbar, parent, false);
        return new ViewHolder(filterView);
    }

    /**
     *  Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Bitmap fromAsset = getBitmapFromAsset(holder.itemView.getContext(), mFilter.get(position).getImagePath());
        holder.iv.setImageBitmap(fromAsset);
        holder.tv.setText(mFilter.get(position).getFilterText());

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                int id = mFilter.get(position).getId();
                editingActivity.setFilterEffect(id);
            }
        });
    }

    private Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream in;
        try {
            in = assetManager.open(strName);
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  Returns the size of the dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() {
        return mFilter.size();
    }
}
package at.fhooe.mc.android.tools;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import at.fhooe.mc.android.EditingActivity;
import at.fhooe.mc.android.R;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {
    private List<Tools> mTools;
    private EditingActivity editingActivity;

    public ToolsAdapter(List<Tools> _tools, EditingActivity editingActivity) {
        mTools = _tools;
        this.editingActivity = editingActivity;
    }

    /**
     *  stores and recycles views as they are scrolled off screen
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll;
        ImageView iv;
        TextView tv;

        private ViewHolder(View _view) {
            super(_view);
            ll = _view.findViewById(R.id.tools_ll);
            iv = _view.findViewById(R.id.tools_iv);
            tv = _view.findViewById(R.id.tools_tv);
        }
    }

    /**
     *  Create new views (invoked by the layout manager)
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View toolsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_editing_toolsbar, parent, false);
        return new ViewHolder(toolsView);
    }

    /**
     *  Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.iv.setImageResource(mTools.get(position).getImageId());
        holder.tv.setText(mTools.get(position).getToolText());

        int toolsID = mTools.get(position).getId();
        if(toolsID == EditingActivity.BRUSH && editingActivity.brushActive)
            holder.iv.setImageResource(R.drawable.ic_close);
        else if(toolsID == EditingActivity.ERASE && editingActivity.eraserActive)
            holder.iv.setImageResource(R.drawable.ic_close);
        else if(toolsID == EditingActivity.FILTER && editingActivity.mFilterRv.getVisibility() == View.VISIBLE)
            holder.iv.setImageResource(R.drawable.ic_close);

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedID = mTools.get(position).getId();
                if(clickedID == EditingActivity.CROP) {
                    editingActivity.openCropViewAndHideTools();
                }
                if(clickedID == EditingActivity.ROTATE) {
                    editingActivity.rotate();
                }
                else if(clickedID == EditingActivity.FLIP_VERTICAL) {
                    editingActivity.flipVertical();
                }
                else if(clickedID == EditingActivity.FLIP_HORIZONTAL) {
                    editingActivity.flipHorizontal();
                }
                else if(clickedID == EditingActivity.TEXT) {
                    editingActivity.textDialog();
                }
                else if(clickedID == EditingActivity.BRUSH) {
                    editingActivity.toggleBrush();
                    editingActivity.mFilterRv.setVisibility(View.GONE);
                }
                else if(clickedID == EditingActivity.ERASE) {
                    editingActivity.toggleErase();
                }
                else if(clickedID == EditingActivity.FILTER){
                    if(editingActivity.mFilterRv.getVisibility() == View.GONE) {
                        editingActivity.closeBrushMode();
                        editingActivity.mFilterRv.setVisibility(View.VISIBLE);
                    } else {
                        editingActivity.mFilterRv.setVisibility(View.GONE);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     *  Returns the size of the dataset (invoked by the layout manager)
     */
    @Override
    public int getItemCount() { return mTools.size(); }
}
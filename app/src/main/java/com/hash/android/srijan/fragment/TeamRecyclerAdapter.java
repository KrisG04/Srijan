package com.hash.android.srijan.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hash.android.srijan.R;

import java.util.List;

import static com.hash.android.srijan.fragment.TeamFragment.teamArrayList;

/**
 * Created by Spandita Ghosh on 2/7/2017.
 */
public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter.ViewHolder> {

    String ecellImage = "https://scontent.fmaa1-2.fna.fbcdn.net/v/t1.0-9/14681653_1660805520878333_1013220169343217181_n.jpg?oh=85845ea757d696297e9cd0deab07665f&oe=5943CFBD";

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public TeamRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeamRecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_child_event, parent, false));

    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override  instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(TeamRecyclerAdapter.ViewHolder holder, int position) {
        holder.eventDetailHeading.setText(teamArrayList.get(position).getName());
        holder.eventDetailDescription.setText(teamArrayList.get(position).getShortDescription());
        Glide.with(holder.eventDetailDescription.getContext()).load(ecellImage)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.eventDetailImageView);
//        Glide.with()
//        holder.eventDetailImageView.setImageResource(teamArrayList.get(position).getImage());
        holder.eventDetailIcon.setImageResource(teamArrayList.get(position).getIcon());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return teamArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventDetailImageView, eventDetailIcon;
        TextView eventDetailHeading, eventDetailDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            eventDetailImageView = (ImageView) itemView.findViewById(R.id.eventDetailImageView);
            eventDetailIcon = (ImageView) itemView.findViewById(R.id.eventDetailImageViewIcon);
            eventDetailHeading = (TextView) itemView.findViewById(R.id.eventDetailTextView);
            eventDetailDescription = (TextView) itemView.findViewById(R.id.contentDetailEventTextView);

        }
    }
}

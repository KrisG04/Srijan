package com.hash.android.srijan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Spandita Ghosh on 2/1/2017.
 */


public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    private int lastPosition = -1;
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
    public EventRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_child, parent, false));
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
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(EventRecyclerAdapter.ViewHolder holder, int position) {

        holder.heading.setText(DashboardActivity.eventArrayListTextHeading.get(position));
        holder.content.setText(DashboardActivity.eventArrayListTextContent.get(position));
        holder.image.setImageResource(DashboardActivity.eventArrayListImage.get(position));
        holder.icon.setImageResource(DashboardActivity.eventArrayListIcon.get(position));
//
//        Animation animation = AnimationUtils.loadAnimation(holder.heading.getContext(),
//                R.anim.up_from_bottom);
//        holder.itemView.startAnimation(animation);
//        lastPosition = position;

    }

    /**
     * Called when a view created by this adapter has been detached from its window.
     * <p>
     * <p>Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.</p>
     *
     * @param holder Holder of the view being detached
     */
    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return DashboardActivity.eventArrayListTextContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView heading;
        TextView content;
        ImageView image;
        ImageView icon;

        public ViewHolder(View inflate) {
            super(inflate);
            heading = (TextView) inflate.findViewById(R.id.eventDetailTextView);
            content = (TextView) inflate.findViewById(R.id.contentEventTextView);
            image = (ImageView) inflate.findViewById(R.id.eventImageView);
            icon = (ImageView) inflate.findViewById(R.id.eventDetailImageViewIcon);
        }

    }
}

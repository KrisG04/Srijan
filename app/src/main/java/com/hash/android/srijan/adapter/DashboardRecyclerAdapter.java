package com.hash.android.srijan.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hash.android.srijan.Event;
import com.hash.android.srijan.R;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Spandita Ghosh on 2/1/2017.
 */


public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.ViewHolder> {

    private final SortedList.Callback<Event> callback = new SortedList.Callback<Event>() {

        /**
         * Called when {@code count} number of items are inserted at the given position.
         *
         * @param position The position of the new item.
         * @param count    The number of items that have been added.
         */
        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        /**
         * Called when {@code count} number of items are removed from the given position.
         *
         * @param position The position of the item which has been removed.
         * @param count    The number of items which have been removed.
         */
        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        /**
         * Called when an item changes its position in the list.
         *
         * @param fromPosition The previous position of the item before the move.
         * @param toPosition   The new position of the item.
         */
        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        /**
         * Similar to {@link Comparator#compare(Object, Object)}, should compare two and
         * return how they should be ordered.
         *
         * @param o1 The first object to compare.
         * @param o2 The second object to compare.
         * @return a negative integer, zero, or a positive integer as the
         * first argument is less than, equal to, or greater than the
         * second.
         */
        @Override
        public int compare(Event o1, Event o2) {
            return 0;
        }

        /**
         * Called by the SortedList when the item at the given position is updated.
         *
         * @param position The position of the item which has been updated.
         * @param count    The number of items which has changed.
         */
        @Override
        public void onChanged(int position, int count) {

        }

        /**
         * Called by the SortedList when it wants to check whether two items have the same data
         * or not. SortedList uses this information to decide whether it should call
         * {@link #onChanged(int, int)} or not.
         * <p>
         * SortedList uses this method to check equality instead of {@link Object#equals(Object)}
         * so
         * that you can change its behavior depending on your UI.
         * <p>
         * For example, if you are using SortedList with a {@link RecyclerView.Adapter
         * RecyclerView.Adapter}, you should
         * return whether the items' visual representations are the same or not.
         *
         * @param oldItem The previous representation of the object.
         * @param newItem The new object that replaces the previous one.
         * @return True if the contents of the items are the same or false if they are different.
         */
        @Override
        public boolean areContentsTheSame(Event oldItem, Event newItem) {
            return false;
        }

        /**
         * Called by the SortedList to decide whether two object represent the same Item or not.
         * <p>
         * For example, if your items have unique ids, this method should check their equality.
         *
         * @param item1 The first item to check.
         * @param item2 The second item to check.
         * @return True if the two items represent the same object or false if they are different.
         */
        @Override
        public boolean areItemsTheSame(Event item1, Event item2) {
            return false;
        }
    };
    //    private int lastPosition = -1;
    private ArrayList<String> eventArrayListTextHeading;
    private ArrayList<Integer> eventArrayListIcon;
    private ArrayList<String> eventArrayListTextContent;
    private ArrayList<Integer> eventArrayListImage;

    public DashboardRecyclerAdapter(ArrayList<String> eventArrayListTextHeading, ArrayList<Integer> eventArrayListIcon, ArrayList<String> eventArrayListTextContent, ArrayList<Integer> eventArrayListImage) {
        this.eventArrayListTextHeading = eventArrayListTextHeading;
        this.eventArrayListIcon = eventArrayListIcon;
        this.eventArrayListTextContent = eventArrayListTextContent;
        this.eventArrayListImage = eventArrayListImage;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * . Since it will be re-used to display
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
    public DashboardRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.child_test, parent, false));
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
    public void onBindViewHolder(DashboardRecyclerAdapter.ViewHolder holder, int position) {

        holder.heading.setText(eventArrayListTextHeading.get(position));
        holder.content.setText(eventArrayListTextContent.get(position));
        holder.image.setImageResource(eventArrayListImage.get(position));
        holder.icon.setImageResource(eventArrayListIcon.get(position));
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
        return eventArrayListTextContent.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView heading;
        TextView content;
        ImageView image;
        ImageView icon;


        ViewHolder(View inflate) {
            super(inflate);
            heading = (TextView) inflate.findViewById(R.id.eventDetailTextView);
            content = (TextView) inflate.findViewById(R.id.contentEventTextView);
            image = (ImageView) inflate.findViewById(R.id.eventImageView);
            icon = (ImageView) inflate.findViewById(R.id.eventDetailImageViewIcon);
        }

    }
}

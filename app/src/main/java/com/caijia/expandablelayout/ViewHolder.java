package com.caijia.expandablelayout;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by ${cai.jia} on 2014/9/16.
 */
public class ViewHolder {

    private ViewHolder() {

    }

    public static <T extends View> T get(View contentView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) contentView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            contentView.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = contentView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public static <T extends View> T get(View contentView, int id,int key) {
        SparseArray<View> viewHolder = (SparseArray<View>) contentView.getTag(key);
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            contentView.setTag(key,viewHolder);
        }

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = contentView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}

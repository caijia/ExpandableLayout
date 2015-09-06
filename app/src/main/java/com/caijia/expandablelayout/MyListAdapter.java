package com.caijia.expandablelayout;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.caijia.library.ExpandableLayout;


/**
 * Created by cai.jia on 2015/9/5.
 */
public class MyListAdapter extends ArrayAdapter<String> {

    SparseArray<Boolean> mCollapsedStatus;

    public MyListAdapter(Context context, String[] objects) {
        super(context, 0, objects);
        mCollapsedStatus = new SparseArray<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Button toggle = ViewHolder.get(convertView, R.id.toggle);
        final ExpandableLayout layout = ViewHolder.get(convertView, R.id.expand_layout_parent);
        layout.setCollapsed(position, mCollapsedStatus);

        TextView textView = ViewHolder.get(convertView, R.id.expand_layout);
        textView.setText(getItem(position));
        toggle.setText("toggle-"+position);

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.toggle();
            }
        });

        return convertView;
    }
}

package com.example.firebaseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private String[] categoryNames;
    private int[] categoryIcons;

    public CategoryAdapter(Context context, String[] categoryNames, int[] categoryIcons) {
        this.context = context;
        this.categoryNames = categoryNames;
        this.categoryIcons = categoryIcons;
    }

    @Override
    public int getCount() {
        return categoryNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dashboard_grid_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.icon = convertView.findViewById(R.id.icon_image);
            viewHolder.name = convertView.findViewById(R.id.icon_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.icon.setImageResource(categoryIcons[position]);
        viewHolder.name.setText(categoryNames[position]);

        return convertView;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView name;
    }
}

package com.parse.starter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.starter.R;

import java.util.List;

public class UserListAdapter extends BaseAdapter {

    private List<ParseUser> objects;
    private LayoutInflater layoutInflater;

    public UserListAdapter(Context context, List<ParseUser> objects) {
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.user_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.txvUsername = (TextView) view.findViewById(R.id.user_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ParseUser user = objects.get(i);

        if (viewHolder.txvUsername != null) {
            viewHolder.txvUsername.setText(user.getString("username"));
        }

        return view;
    }

    private static class ViewHolder {
        private TextView txvUsername;
    }
}

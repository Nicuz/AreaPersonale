package com.fast0n.ipersonalarea.fragments.AboutFragment;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fast0n.ipersonalarea.R;

import java.util.ArrayList;

public class CustomAdapterAboutFragment extends ArrayAdapter<DataAboutFragment> implements View.OnClickListener {

    private ArrayList<DataAboutFragment> dataSet;
    private Context mContext;

    CustomAdapterAboutFragment(ArrayList<DataAboutFragment> data, Context context) {
        super(context, R.layout.fragment_about, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DataAboutFragment DataAboutFragment = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_about, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.name);
            viewHolder.icon = convertView.findViewById(R.id.icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.txtName.setText(DataAboutFragment.getName());
        viewHolder.txtName.setText(Html.fromHtml(DataAboutFragment.getName()));
        viewHolder.icon.setImageResource(DataAboutFragment.getIcon());
        viewHolder.icon.setOnClickListener(this);
        viewHolder.icon.setTag(position);

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ImageView icon;
    }
}
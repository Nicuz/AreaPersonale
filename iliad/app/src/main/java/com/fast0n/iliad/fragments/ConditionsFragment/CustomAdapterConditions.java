package com.fast0n.iliad.fragments.ConditionsFragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fast0n.iliad.R;

import java.util.List;

public class CustomAdapterConditions extends RecyclerView.Adapter<CustomAdapterConditions.MyViewHolder> {

    private List<com.fast0n.iliad.fragments.ConditionsFragment.DataConditionsFragments> conditionList;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, url;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            url = view.findViewById(R.id.url);

        }
    }

    public CustomAdapterConditions(
            List<com.fast0n.iliad.fragments.ConditionsFragment.DataConditionsFragments> conditionList) {
        this.conditionList = conditionList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        com.fast0n.iliad.fragments.ConditionsFragment.DataConditionsFragments c = conditionList.get(position);

        holder.title.setText(c.title);
        holder.date.setText(c.date);
        holder.url.setText(c.url);

    }

    @Override
    public int getItemCount() {
        return conditionList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conditions, parent, false);
        return new MyViewHolder(v);
    }
}
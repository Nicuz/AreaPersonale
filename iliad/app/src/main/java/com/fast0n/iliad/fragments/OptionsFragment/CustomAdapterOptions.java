package com.fast0n.iliad.fragments.OptionsFragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fast0n.iliad.R;
import com.github.angads25.toggle.LabeledSwitch;

import java.util.List;

public class CustomAdapterOptions extends RecyclerView.Adapter<CustomAdapterOptions.MyViewHolder> {

    Context context;
    private List<DataOptionsFragments> optionsList;


    public CustomAdapterOptions(Context context, List<DataOptionsFragments> optionsList) {
        this.context = context;
        this.optionsList = optionsList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataOptionsFragments c = optionsList.get(position);

        holder.textView.setText(c.textView);
        if (c.toggle.equals("false")) {
            holder.toggle.setOn(false);
            holder.toggle.setColorOn((ContextCompat.getColor(context, R.color.colorPrimary)));
            holder.toggle.setLabelOff(c.toggle.replace("false", "Non attivo"));
            holder.toggle.setLabelOn(context.getString(R.string.toggle_enable));
        } else {
            holder.toggle.setOn(true);
            holder.toggle.setColorOn(Color.parseColor("#0d8200"));
            holder.toggle.setLabelOn(c.toggle.replace("true", "Attivo"));
            holder.toggle.setLabelOff(context.getString(R.string.toggle_disable));
        }
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_options_services, parent, false);
        return new MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        LabeledSwitch toggle;


        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            toggle = view.findViewById(R.id.toggle);

        }
    }
}
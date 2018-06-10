package com.fast0n.iliad.fragments.OptionsFragment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fast0n.iliad.R;
import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CustomAdapterOptions extends RecyclerView.Adapter<CustomAdapterOptions.MyViewHolder> {

    Context context;
    private List<DataOptionsFragments> optionsList;
    String token;

    CustomAdapterOptions(Context context, List<DataOptionsFragments> optionsList, String token) {
        this.context = context;
        this.optionsList = optionsList;
        this.token = token;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final DataOptionsFragments c = optionsList.get(position);
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

        if (position == 0) {
            holder.toggle.setEnabled(false);
        }

        holder.toggle.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {

                final String site_url = context.getString(R.string.site_url);

                if (!isOn) {
                    holder.toggle.setColorOn((ContextCompat.getColor(context, R.color.colorPrimary)));
                    holder.toggle.setLabelOn(context.getString(R.string.toggle_enable));

                    String url = site_url + "?change_options=true&update=" + c.name + "&token=" + token + "&activate=0";
                    request_options_services(url, holder.textView.getText() + " " + holder.toggle.getLabelOff());
                }

                else if (isOn) {
                    holder.toggle.setColorOn(Color.parseColor("#0d8200"));
                    holder.toggle.setLabelOff(context.getString(R.string.toggle_disable));

                    String url = site_url + "?change_options=true&update=" + c.name + "&token=" + token + "&activate=1";
                    request_options_services(url, holder.textView.getText() + " " + holder.toggle.getLabelOn());
                }

            }

            private void request_options_services(String url, final String labelOn) {

                RequestQueue queue = Volley.newRequestQueue(context);

                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject json_raw = new JSONObject(response.toString());
                                    String iliad = json_raw.getString("iliad");

                                    JSONObject json = new JSONObject(iliad);
                                    String string_response = json.getString("0");

                                    if (string_response.equals("true")) {

                                        Toasty.warning(context, labelOn, Toast.LENGTH_SHORT, true).show();
                                    }

                                } catch (JSONException ignored) {
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                queue.add(getRequest);

            }

        });

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
            textView = view.findViewById(R.id.textView1);
            toggle = view.findViewById(R.id.toggle);

        }
    }
}
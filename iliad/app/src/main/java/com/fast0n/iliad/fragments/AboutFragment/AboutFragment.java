package com.fast0n.iliad.fragments.AboutFragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fast0n.iliad.BuildConfig;
import com.fast0n.iliad.R;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;


public class AboutFragment extends Fragment {


    public AboutFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_about, container, false);

        ArrayList<DataAboutFragment> DataAboutFragments;
        ListView listView;
        CustomAdapterAboutFragment adapter;
        final Context context;
        context = Objects.requireNonNull(getActivity()).getApplicationContext();

        // java adresses
        listView = view.findViewById(R.id.list_info);
        DataAboutFragments = new ArrayList<>();


        // add data element in listview
        DataAboutFragments
                .add(new DataAboutFragment(
                        getString(R.string.version) + "<br><small>" + BuildConfig.VERSION_NAME + " ("
                                + BuildConfig.VERSION_CODE + ") (" + BuildConfig.APPLICATION_ID + ")</small>",
                        R.drawable.ic_info_outline));
        DataAboutFragments.add(new DataAboutFragment(getString(R.string.source_code), R.drawable.ic_github));
        DataAboutFragments.add(new DataAboutFragment(getString(R.string.donate), R.drawable.ic_credit));
        DataAboutFragments.add(new DataAboutFragment(getString(R.string.author) + "<br><small>Massimiliano Montaleone</small>",
                R.drawable.ic_user));
        DataAboutFragments.add(new DataAboutFragment(getString(R.string.follow) +  "<br><small>@Fast0n</small>", R.drawable.ic_instagram));

        DataAboutFragments.add(new DataAboutFragment(getString(R.string.author) + "<br><small>Matteo Monteleone</small>",
                R.drawable.ic_user));
        DataAboutFragments.add(new DataAboutFragment(getString(R.string.follow) +  "<br><small>@matte_monteleone</small>", R.drawable.ic_instagram));

        DataAboutFragments.add(new DataAboutFragment(getString(R.string.content), R.drawable.ic_warning));

        // set data to Adapter
        adapter = new CustomAdapterAboutFragment(DataAboutFragments, context);
        listView.setAdapter(adapter);


        // setOnItemClickListener listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fast0n/iliad")));
                        break;
                    case 2:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/Fast0n/0.5")));
                        break;
                    case 3:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fast0n/")));
                        break;
                    case 4:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/fast0n/")));
                        break;

                    case 5:

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/mattvoid/")));
    break;

                    case 6:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/matte_monteleone/")));
                        break;

                    case 7:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Fast0n/iliad/blob/master/LICENSE")));
                        break;

                }

            }
        });


        return view;
    }

}

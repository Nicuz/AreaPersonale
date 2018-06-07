package com.fast0n.iliad;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fast0n.iliad.fragments.CreditFragment.DataCreditFragments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
            throws ExecutionException, InterruptedException {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        RequestOptions options = new RequestOptions().override(50, 50);

        AppWidgetTarget img2 = new AppWidgetTarget(context, R.id.img2, views, appWidgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };
        Glide.with(context.getApplicationContext()).asBitmap()
                .load("http://android12.altervista.org/res/widget/ic_call.png").apply(options).into(img2);

        AppWidgetTarget img6 = new AppWidgetTarget(context, R.id.img6, views, appWidgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };
        Glide.with(context.getApplicationContext()).asBitmap()
                .load("http://android12.altervista.org/res/widget/ic_call.png").apply(options).into(img6);

        AppWidgetTarget img4 = new AppWidgetTarget(context, R.id.img4, views, appWidgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };
        Glide.with(context.getApplicationContext()).asBitmap()
                .load("http://android12.altervista.org/res/widget/ic_gb.png").apply(options).into(img4);

        AppWidgetTarget img8 = new AppWidgetTarget(context, R.id.img8, views, appWidgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };
        Glide.with(context.getApplicationContext()).asBitmap()
                .load("http://android12.altervista.org/res/widget/ic_gb.png").apply(options).into(img8);

        AppWidgetTarget img3 = new AppWidgetTarget(context, R.id.img3, views, appWidgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };
        Glide.with(context.getApplicationContext()).asBitmap()
                .load("http://android12.altervista.org/res/widget/ic_email.png").apply(options).into(img3);

        AppWidgetTarget img7 = new AppWidgetTarget(context, R.id.img7, views, appWidgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };
        Glide.with(context.getApplicationContext()).asBitmap()
                .load("http://android12.altervista.org/res/widget/ic_email.png").apply(options).into(img7);

        AppWidgetTarget img5 = new AppWidgetTarget(context, R.id.img5, views, appWidgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };
        Glide.with(context.getApplicationContext()).asBitmap()
                .load("http://android12.altervista.org/res/widget/ic_mms.png").apply(options).into(img5);

        AppWidgetTarget img9 = new AppWidgetTarget(context, R.id.img9, views, appWidgetId) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };
        Glide.with(context.getApplicationContext()).asBitmap()
                .load("http://android12.altervista.org/res/widget/ic_mms.png").apply(options).into(img9);

        /*
         * 
         * 
         * // credit views.setTextViewText(R.id.textView, "50,00€"); // number
         * views.setTextViewText(R.id.textView2, "329000000"); // call
         * views.setTextViewText(R.id.textView3, "1000m 0s"); // sms
         * views.setTextViewText(R.id.textView4, "1000 SMS"); // data
         * views.setTextViewText(R.id.textView5, "999,00MB / \n 30GB"); // MMS
         * views.setTextViewText(R.id.textView6, "1000 MMS"); // callroaming
         * views.setTextViewText(R.id.textView7, "1000m 0s"); // smsroaming
         * views.setTextViewText(R.id.textView8, "1000 SMS"); // dataroaming
         * views.setTextViewText(R.id.textView9, "999,00MB / \n 2GB"); // MMSroaming
         * views.setTextViewText(R.id.textView10, "1000 MMS");
         * 
         */
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }
    /*
     * @Override public void onReceive(Context context, Intent intent) {
     * 
     * final Bundle extras = intent.getExtras(); assert extras != null; final String
     * token = extras.getString("token"); final String site_url =
     * context.getString(R.string.site_url); String url = site_url +
     * "?credit=true&token=" + token; System.out.println(url); RemoteViews views =
     * new RemoteViews(context.getPackageName(), R.layout.widget);
     * getObject(context, url, views); }
     * 
     * private static void getObject(Context context, String url, RemoteViews views)
     * {
     * 
     * 
     * RequestQueue queue = Volley.newRequestQueue(context); JsonObjectRequest
     * getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new
     * Response.Listener<JSONObject>() {
     * 
     * @Override public void onResponse(JSONObject response) { try {
     * 
     * JSONObject json_raw = new JSONObject(response.toString()); String iliad =
     * json_raw.getString("iliad");
     * 
     * JSONObject json = new JSONObject(iliad);
     * 
     * String string1 = json.getString("0"); JSONObject json_strings1 = new
     * JSONObject(string1); String stringCredit = json_strings1.getString("0");
     * 
     * for (int j = 1; j < json.length(); j++) {
     * 
     * String string = json.getString(String.valueOf(j)); JSONObject json_strings =
     * new JSONObject(string);
     * 
     * String c = json_strings.getString("0"); String b =
     * json_strings.getString("1"); String a = json_strings.getString("2"); String d
     * = json_strings.getString("3");
     * 
     * views.setTextViewText(R.id.textView1, b);
     * 
     * 
     * } } catch (JSONException e) { } } }, new Response.ErrorListener() {
     * 
     * @Override public void onErrorResponse(VolleyError error) {
     * 
     * 
     * } });
     * 
     * // add it to the RequestQueue queue.add(getRequest);
     * 
     * }
     * 
     */

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            try {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

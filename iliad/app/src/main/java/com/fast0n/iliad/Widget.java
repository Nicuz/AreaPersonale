package com.fast0n.iliad;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fast0n.iliad.java.GenerateToken;

import org.json.JSONException;
import org.json.JSONObject;


public class Widget extends AppWidgetProvider {


    String token = GenerateToken.randomString(20);
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            RequestOptions options = new RequestOptions().override(50, 50);
            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.widget);


            Intent iSetting = new Intent(context, LoginActivity.class);
            PendingIntent piSetting = PendingIntent.getActivity(context, 0, iSetting, 0);
            views.setOnClickPendingIntent(R.id.widget_click, piSetting);


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


            AppWidgetTarget button = new AppWidgetTarget(context, R.id.button, views, appWidgetId) {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    super.onResourceReady(resource, transition);
                }
            };
            Glide.with(context.getApplicationContext()).asBitmap()
                    .load("http://android12.altervista.org/res/widget/ic_update.png").apply(options).into(button);


/*

            SharedPreferences settings = context.getSharedPreferences("sharedPreferences", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.apply();
            final String site_url = context.getString(R.string.site_url);
            String userid = settings.getString("userid", null);
            String password = settings.getString("password", null);



            String url22 = site_url + "?userid=" + userid + "&password=" + password + "&token=" + token;
            RequestQueue login = Volley.newRequestQueue(context);
            JsonObjectRequest getRequest22 = new JsonObjectRequest(Request.Method.GET, url22, null,
                    response -> {

                        SharedPreferences settings1 = context.getSharedPreferences("sharedPreferences", 0);
                        SharedPreferences.Editor editor1 = settings1.edit();
                        editor1.putString("token", token);
                        editor1.apply();

                        Intent intent1 = new Intent(context, Widget.class);
                        intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,
                                0, intent1, 0);

                        context.sendBroadcast(intent1);

                    }, error1 -> {});

            login.add(getRequest22);








            String url = site_url + "?credit=true&token=" + token;


            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");
                            JSONObject json = new JSONObject(iliad);

                            String stringCredit = json.getString("0");
                            JSONObject json_Credit = new JSONObject(stringCredit);
                            String credit = json_Credit.getString("0");
                            views.setTextViewText(R.id.textView, credit.replace("Offerta iliad - Credito : ",""));

                            String stringCall = json.getString("1");
                            JSONObject json_Call = new JSONObject(stringCall);
                            String call = json_Call.getString("0");
                            views.setTextViewText(R.id.textView3, call.replace("Chiamate: ",""));

                            String stringSms = json.getString("2");
                            JSONObject json_Sms = new JSONObject(stringSms);
                            String sms = json_Sms.getString("0");
                            views.setTextViewText(R.id.textView4, sms);

                            String stringGb = json.getString("3");
                            JSONObject json_Gb = new JSONObject(stringGb);
                            String gb = json_Gb.getString("0");
                            views.setTextViewText(R.id.textView5, gb.replace("/","/ \n"));

                            String stringMms = json.getString("4");
                            JSONObject json_Mms = new JSONObject(stringMms);
                            String mms = json_Mms.getString("0");
                            views.setTextViewText(R.id.textView6, mms);

                            appWidgetManager.updateAppWidget(appWidgetId, views);



                        } catch (JSONException ignored) {
                        }

                    }, error -> {
            });
                queue.add(getRequest);


            /*
            String url1 = site_url + "?creditestero=true&token=" + token;
            RequestQueue queue1 = Volley.newRequestQueue(context);
            JsonObjectRequest getRequest1 = new JsonObjectRequest(Request.Method.GET, url1, null,
                    response -> {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");
                            JSONObject json = new JSONObject(iliad);


                            String stringCall = json.getString("1");
                            JSONObject json_Call = new JSONObject(stringCall);
                            String call = json_Call.getString("0");
                            views.setTextViewText(R.id.textView7, call.replace("Chiamate: ",""));

                            String stringSms = json.getString("2");
                            JSONObject json_Sms = new JSONObject(stringSms);
                            String sms = json_Sms.getString("0");
                            views.setTextViewText(R.id.textView8, sms);

                            String stringGb = json.getString("3");
                            JSONObject json_Gb = new JSONObject(stringGb);
                            String gb = json_Gb.getString("0");
                            views.setTextViewText(R.id.textView9, gb.replace("/","/ \n"));

                            String stringMms = json.getString("4");
                            JSONObject json_Mms = new JSONObject(stringMms);
                            String mms = json_Mms.getString("0");
                            views.setTextViewText(R.id.textView10, mms);



                            appWidgetManager.updateAppWidget(appWidgetId, views);



                        } catch (JSONException ignored) {
                        }

                    }, error -> {

                        System.out.println("ciao");

                        SharedPreferences settings12 = context.getSharedPreferences("sharedPreferences", 0);
                        String userid = settings12.getString("userid", null);
                        String password = settings12.getString("password", null);
                        SharedPreferences.Editor editor12 = settings12.edit();
                        editor12.apply();
                        final String site_url1 = context.getString(R.string.site_url);
                        String url22 = site_url1 + "?userid=" + userid + "&password=" + password + "&token=" + token1;
                        RequestQueue login = Volley.newRequestQueue(context);
                        JsonObjectRequest getRequest22 = new JsonObjectRequest(Request.Method.GET, url22, null,
                                response -> {

                                    SharedPreferences settings1 = context.getSharedPreferences("sharedPreferences", 0);
                                    SharedPreferences.Editor editor1 = settings1.edit();
                                    editor1.putString("token", token1);
                                    editor1.apply();

                                    Intent intent1 = new Intent(context, Widget.class);
                                    intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                                    intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,
                                            0, intent1, 0);

                                    context.sendBroadcast(intent1);

                                }, error1 -> {});

                        login.add(getRequest22);
                    });

            queue1.add(getRequest1);


            String url2 = site_url + "?getNumTell=true&token=" + token;
            RequestQueue queue2 = Volley.newRequestQueue(context);
            JsonObjectRequest getRequest2 = new JsonObjectRequest(Request.Method.GET, url2, null,
                    response -> {
                        try {

                            JSONObject json_raw = new JSONObject(response.toString());
                            String iliad = json_raw.getString("iliad");
                            JSONObject json = new JSONObject(iliad);

                            String stringNum = json.getString("0");
                            views.setTextViewText(R.id.textView2, stringNum.replace("Numero: ",""));




                        } catch (JSONException ignored) {
                        }

                    }, error -> {

                System.out.println("ciao");

                SharedPreferences settings12 = context.getSharedPreferences("sharedPreferences", 0);
                String userid = settings12.getString("userid", null);
                String password = settings12.getString("password", null);
                SharedPreferences.Editor editor12 = settings12.edit();
                editor12.apply();
                final String site_url1 = context.getString(R.string.site_url);
                String url22 = site_url1 + "?userid=" + userid + "&password=" + password + "&token=" + token1;
                RequestQueue login = Volley.newRequestQueue(context);
                JsonObjectRequest getRequest22 = new JsonObjectRequest(Request.Method.GET, url22, null,
                        response -> {

                            SharedPreferences settings1 = context.getSharedPreferences("sharedPreferences", 0);
                            SharedPreferences.Editor editor1 = settings1.edit();
                            editor1.putString("token", token1);
                            editor1.apply();

                            Intent intent1 = new Intent(context, Widget.class);
                            intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                            intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,
                                    0, intent1, 0);

                            context.sendBroadcast(intent1);

                        }, error1 -> {});

                login.add(getRequest22);
            });

            queue2.add(getRequest2);

*/

            Intent intent1 = new Intent(context, Widget.class);
            intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,
                    0, intent1, 0);


            views.setOnClickPendingIntent(R.id.button, pendingIntent1);
            appWidgetManager.updateAppWidget(appWidgetId, views);


        }

    }



}

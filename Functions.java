package com.dealsheel.dealsheeluser.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dealsheel.dealsheeluser.BuildConfig;
import com.dealsheel.dealsheeluser.R;
import com.dealsheel.dealsheeluser.interfaces.LatLngInterface;
import com.dealsheel.dealsheeluser.interfaces.LocationAddress;
import com.dealsheel.dealsheeluser.interfaces.YesNoClickInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ocpsoft.prettytime.PrettyTime;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Functions {

    public static void hideStatusBar(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void setPrettyCompleteTimeString(TextView timeTV, String date){
        PrettyTime p = new PrettyTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss zzzzz yyyy");
        try {
            Date d = sdf.parse(date);
            timeTV.setText(p.format(d));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public static String getCurrentDateTime(){
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.toString();
    }

    public static String dateFormatWithTime(String inputString) {
        String[] date1 = new String[1];
        //end_to : "2020-09-17 18:08:39"
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM yyyy | hh:mm aa";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(inputString);
            str = outputFormat.format(date);
            date1 = str.split(" ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }

    public static String timeFormat(String inputString) {
        String[] date1 = new String[1];
        String inputPattern = "HH:mm:ss";
        String outputPattern = "hh:mm aa";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(inputString);
            str = outputFormat.format(date);
            date1 = str.split(" ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }

    public static void showToast(String message){
        /*LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, null);
        CustomTextView text = layout.findViewById(R.id.text);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();*/
        if (message==null){
            message="";
        }
        Toast.makeText(SingleTone.getInstance(), message, Toast.LENGTH_LONG).show();
    }

    public static void printLog(String msgToPrint){
        Log.d(Constants.LOG,"\n\n\n\n"+msgToPrint);
    }

    public static RecyclerView.LayoutManager layoutManager(Context context, String direction, int gridCount){
        LinearLayoutManager layoutManager;
        if (direction.equals(Constants.VERTICAL)){
            layoutManager= new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        }else if (direction.equals(Constants.HORIZONTAL)){
            layoutManager= new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        }else {
            layoutManager= new GridLayoutManager(context, gridCount);
        }
        return layoutManager;
    }

    public static int getScreenWidth(Context context){
        int width=0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm!=null){
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            width = metrics.widthPixels;
        }else {
            width=200;
        }
        return width;
    }

    public static int getScreenHeight(Context context){
        int height=0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm!=null) {
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            height=metrics.heightPixels;
        }else {
            height=300;
        }
        return height;
    }

    public static AlertDialog showProgressBar(Context context){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setCancelable(false);

        View view=LayoutInflater.from(context).inflate(R.layout.progress_layout, null);
        builder.setView(view);

        AlertDialog alertDialog=builder.create();



        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();

        if (alertDialog.getWindow()!=null){
            alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
        return alertDialog;
    }

    public static void openSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void permissionAlertView(final Context context, String title, String message, String positiveButton, String negativeButton){
        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(context);
        dialog.setCancelable(false);

        dialog.setTitle(title)
                .setMessage(message)
                .setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.dismiss();
                        Constants.PERMISSION_DIALOG_VISIBLE=false;
                        ((Activity)context).finishAffinity();
                    }
                })
                .setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.dismiss();
                        Functions.openSettings(context);
                        Constants.PERMISSION_DIALOG_VISIBLE=false;
                    }
                }).show();
    }

    public static void getCurrentLatLng(Context context, LatLngInterface latLng){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED*/) {
            return;
        }else {
            Functions.printLog("Granted");
        }

        if (locationManager!=null){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, new LocationListenerClass(new LatLngInterface() {
                @Override
                public void handleLatLng(double lat, double lng) {
                    Functions.printLog("latGetting"+lat+"1");
                }
            }));
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new LocationListenerClass(new LatLngInterface() {
                @Override
                public void handleLatLng(double lat, double lng) {
                    Functions.printLog("latGetting"+lat+"2");
                }
            }));

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null){
               // Functions.showToast(context.getString(R.string.turnOnGPS));
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (location != null){
                latLng.handleLatLng(location.getLatitude(), location.getLongitude());
            }
        }else {
           // Functions.showToast(context.getString(R.string.turnOnGPS));
        }

    }

    public static void getAddress(Context context, double latitude, double longitude, LocationAddress locationAddress){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            for (int i=0; i<addresses.size(); i++){
                Functions.printLog("\n\n\nallAddress"+addresses.get(i));
            }

            String city = addresses.get(0).getLocality();
            String address = addresses.get(0).getAddressLine(0);
            String admin = addresses.get(0).getAdminArea();

            locationAddress.getCity(city);
            locationAddress.getAddress(address);
            locationAddress.getAdmin(admin);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                addresses = geocoder.getFromLocation(Double.valueOf("30.7416"), Double.valueOf("76.8000"), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                locationAddress.getAddress(address);
                locationAddress.getCity(city);

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }


    }

    public static void setImageUsingGlideSameURL(Context context, String url, ImageView imageView, int placeHolder){
        if (url!=null){
            if (url.equals("")){
                imageView.setImageResource(placeHolder);
            }else {
                RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(context).load(url)
                        .thumbnail(Glide.with(context).load(url).apply(requestOptions.placeholder(placeHolder).override(300, 100)))
                        .apply(requestOptions.placeholder(placeHolder).override(600, 300)).into(imageView);
            }

        }else {
            imageView.setImageResource(placeHolder);
        }
    }

    public static void setImageUsingGlideDiffURL(Context context, String url, String thumbnailUrl, ImageView imageView){
        RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        if (url!=null && thumbnailUrl!=null && !url.equals("") && !thumbnailUrl.equals("")){
            Glide.with(context).load(url)
                    .thumbnail(Glide.with(context).load(thumbnailUrl))
                    .apply(requestOptions.placeholder(R.drawable.user)).into(imageView);
        }

    }

    public static String dateFormat(String inputString) {
        String[] date1 = new String[1];
        //end_to : "2020-09-17 18:08:39"
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(inputString);
            str = outputFormat.format(date);
            date1 = str.split(" ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }

    public static String dateFormatSmallMonth(String inputString) {
        String[] date1 = new String[1];
        //end_to : "2020-09-17 18:08:39"
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(inputString);
            str = outputFormat.format(date);
            date1 = str.split(" ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }

    public static String firstLetterCap(String text){
        return text.substring(0,1).toUpperCase() + text.substring(1);
    }

    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getFileDataFromFile(File file){
        FileInputStream fileInputStreamReader = null;
        try {
            fileInputStreamReader = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes = new byte[(int)file.length()];
        try {
            fileInputStreamReader.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Base64.encode(bytes, Base64.DEFAULT);
    }

    public static String readJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("fileNameHere.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void setSpannable(int spanTo, int spanFrom, String stringToSpan, CustomTextView customTextView){
        Spannable wordtoSpan = new SpannableString(stringToSpan);
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), spanTo, spanFrom, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        customTextView.setText(wordtoSpan);
    }

    public static void getLatLngFromCityName(Context context, String cityName, final LatLngInterface latLngInt){
        if(Geocoder.isPresent()){
            try {
                Geocoder gc = new Geocoder(context);
                List<Address> addresses= gc.getFromLocationName(cityName, 5); // get the found Address Objects

                List<LatLngInterface> ll = new ArrayList<>(addresses.size()); // A list to save the coordinates if they are available
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        LatLngInterface latLngInterface=new LatLngInterface() {
                            @Override
                            public void handleLatLng(double lat, double lng) {
                                latLngInt.handleLatLng(lat, lng);
                            }
                        };

                        latLngInterface.handleLatLng(a.getLatitude(), a.getLongitude());

                    }
                }
            } catch (IOException e) {
                // handle the exception
            }
        }
    }

    public static void twoButtonsPopup(final Context context, String positiveButtonText, String negativeButtonText, String title, String message, final YesNoClickInterface yesNoClickInterface){
        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(context);
        dialog.setCancelable(false);

        dialog.setTitle(title)
                .setMessage(message)
                .setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        yesNoClickInterface.onNegativeClick(dialoginterface);
                    }
                })
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        yesNoClickInterface.onPositiveClick(dialoginterface);
                    }
                }).show();
    }

    public static void share(Context context,  String subject, String textToShare){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        shareIntent.setType("text/plain");
        context.startActivity(shareIntent);
    }

    public static void setPrettyTime(TextView timeTV, String date){
        PrettyTime p = new PrettyTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = sdf.parse(date);
            timeTV.setText(p.format(d));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public static double distance(Preferences preferences, double locationLat, double locationLon) {
        double theta = Double.valueOf(preferences.getString(Preferences.MY_LONGITUDE,"")) - locationLon;
        double dist = Math.sin(deg2rad(Double.valueOf(preferences.getString(Preferences.MY_LATITUDE,""))))
                * Math.sin(deg2rad(locationLat))
                + Math.cos(deg2rad(Double.valueOf(preferences.getString(Preferences.MY_LATITUDE,""))))
                * Math.cos(deg2rad(locationLat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        Double finalValue=Double.valueOf(new DecimalFormat("##.#").format(dist));
        return (finalValue);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    static boolean isObject;
    public static boolean isObject(String data){
        Object json = null;
        try {
            json = new JSONTokener(data).nextValue();
            if (json instanceof JSONObject){
                //you have an object
                isObject=true;
            }
            else if (json instanceof JSONArray){
                //you have an array
                isObject=false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return isObject;

    }

    public static void takeScreenshot(Context context, View v1) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            //View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            createPdf(context, bitmap, now.toString());

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    public static void createPdf(Context context, Bitmap bitmap, String invoiceName){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        if (wm!=null){
            Display display = wm.getDefaultDisplay();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);


        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);


        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "DealSheelInvoice");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }

        //String targetPdf = "/sdcard/"+invoiceName+".pdf";
        String targetPdf =Environment.getExternalStorageDirectory().getPath()+"/DealSheelInvoice/"+invoiceName+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Functions.showToast(context.getString(R.string.invoiceIsDownload));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

    public static void openDatePickerForCards(final CustomTextView tv, Context context){
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelForCards(tv, myCalendar);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    public static void openDatePicker(final CustomTextView tv, Context context){
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tv, myCalendar);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    private static void updateLabel(CustomTextView tv, Calendar calendar) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tv.setText(sdf.format(calendar.getTime()));
    }

    private static void updateLabelForCards(CustomTextView tv, Calendar calendar) {
        String myFormat = "MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tv.setText(sdf.format(calendar.getTime()));
    }

    public static android.app.AlertDialog alertDialog;
    public static RecyclerView categoryListDialog(Context context){
        android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(context);
        View view= LayoutInflater.from(context).inflate(R.layout.business_category_list_layout,null);
        RecyclerView recyclerView=view.findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(Functions.layoutManager(context,Constants.VERTICAL, 0));
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);
        alertDialog.show();

        return recyclerView;
    }

    private void toggleView(View viewToShow) {
        Transition transition = new Slide(Gravity.TOP);
        transition.setDuration(500);
        transition.addTarget(viewToShow);

        //todo uncomment line below to use animation

       // TransitionManager.beginDelayedTransition("rootView", transition);
        viewToShow.setVisibility(View.VISIBLE);
    }

    private void toggleHide(View viewToShow) {
        Transition transition = new Slide(Gravity.TOP);
        transition.setDuration(500);
        transition.addTarget(viewToShow);

        //todo uncomment line below to use animation

        // TransitionManager.beginDelayedTransition("rootView", transition);
        viewToShow.setVisibility(View.GONE);
    }

}

  public static void launchActivityWithFinish(Context context, Class<?> launchingActivity) {
        (context).startActivity(new Intent(context, launchingActivity));
        ((Activity) context).finish();
    }
    
 //Sorting Array
            Collections.sort(placesModels, new Comparator<PlacesModel>() {
                @Override
                public int compare(PlacesModel item, PlacesModel t1) {
                    String s1 = item.getPlaceName();
                    String s2 = t1.getPlaceName();
                    return s1.compareToIgnoreCase(s2);
                }

            });


            //onbackpress
                boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.pleaseClickAgain), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



    public Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int width = bitmap.getWidth();
        if(bitmap.getWidth()>bitmap.getHeight())
            width = bitmap.getHeight();
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, width);
        final RectF rectF = new RectF(rect);
        final float roundPx = width / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
 public static void hideStatusBar(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static RecyclerView.LayoutManager layoutManager(Context context, String direction, int gridCount){
        LinearLayoutManager layoutManager;
        if (direction.equals(Constant.VERTICAL)){
            layoutManager= new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        }else if (direction.equals(Constant.HORIZONTAL)){
            layoutManager= new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        }else {
            layoutManager= new GridLayoutManager(context, gridCount);
        }
        return layoutManager;
    }

    public static int getScreenWidth(Context context){
        int width=0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm!=null){
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            width = metrics.widthPixels;
        }else {
            width=200;
        }
        return width;
    }

    public static int getScreenHeight(Context context){
        int height=0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm!=null){
            wm.getDefaultDisplay().getMetrics(displayMetrics);
             height = displayMetrics.heightPixels;
        }
        return height;

    }


    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void printHashMap(HashMap<String, String> hashMap){
        for (Map.Entry<String,String> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d("\n\nallMapDataSukh\n",  "key=" + key + "  value=" + value);
        }
    }

    public static void openDatePicker(final TextView tv, Context context){
        final Calendar myCalendar = Calendar.getInstance();

        myCalendar.add(Calendar.YEAR, -18);
        myCalendar.add(Calendar.DAY_OF_MONTH, -1);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tv, myCalendar);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        datePickerDialog.show();

    }

    private static void updateLabel(TextView tv, Calendar calendar) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tv.setText(sdf.format(calendar.getTime()));
    }

    public static String getCurrentDate(String dateFormat){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(c);
    }


    public static androidx.appcompat.app.AlertDialog showProgressBar(Context context){

        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setCancelable(false);

        View view= LayoutInflater.from(context).inflate(R.layout.progress_layout, null);
        builder.setView(view);

        androidx.appcompat.app.AlertDialog alertDialog=builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();

        if (alertDialog.getWindow()!=null){
            alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
        return alertDialog;
    }

    public static void hideKeyboard(LinearLayout linearLayout, Context context) {
        InputMethodManager inputMethodManager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager!=null){
            inputMethodManager.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
        }
    }

    public static String getAddress(Context context, String prefix, double lat, double lng) {
        String add="";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = prefix+obj.getAddressLine(0);
          /*  add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();*/

          Log.v("IGAsdfffdfgdfgdf", "Address" + add);


        } catch (IOException e) {
        }
        return add;
    }


    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static void setImageWithPicasso(Context context, final String thumbUrl, final ImageView imageView){
        if (thumbUrl!=null){
            if (thumbUrl.isEmpty()){
                imageView.setImageResource(R.drawable.corrupt);
            }else {
                Picasso.get()
                        .load(thumbUrl) // thumbnail url goes here
                        .placeholder(R.drawable.corrupt)
                        .resize(100, 100)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.get()
                                        .load(thumbUrl) // image url goes here
                                        .resize(600, 600)
                                        .placeholder(imageView.getDrawable())
                                        .into(imageView);
                            }

                            @Override
                            public void onError(Exception e) {
                                imageView.setImageResource(R.drawable.corrupt);
                            }
                        });
            }
        }else {
            imageView.setImageResource(R.drawable.corrupt);
        }
    }
    public static void setImageWithPicasso2(Context context, final String thumbUrl, final ImageView imageView){
        if (thumbUrl!=null){
            if (thumbUrl.isEmpty()){
            }else {
                Picasso.get()
                        .load(thumbUrl) // thumbnail url goes here
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                Picasso.get()
                                        .load(thumbUrl) // image url goes here
                                        .placeholder(imageView.getDrawable())
                                        .into(imageView);
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });
            }
        }else {
        }
    }
    public static String changeDateFormat(String date, String foundFormat, String requiredFormat){
        //yyyy-MM-dd hh:mm:ss
        SimpleDateFormat spf=new SimpleDateFormat(foundFormat);
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat(requiredFormat);
        return spf.format(newDate);
    }

public static void openNotifyPopup(Context context, View view, final FrameLayout frameLayoutToShowHide, final ClickViewInterface clickViewInterface) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = null;
        if (inflater != null) {
            popupView = inflater.inflate(R.layout.menu_popup_layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.setOutsideTouchable(false);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                frameLayoutToShowHide.setVisibility(View.GONE);
            }
        });


        popupWindow.showAsDropDown(view);
        frameLayoutToShowHide.setVisibility(View.VISIBLE);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                frameLayoutToShowHide.setVisibility(View.GONE);
                clickViewInterface.onClickViewCall(v, 0);
                return true;
            }
        });
    }
    
    ///////transaction animations on click any view
    
      View sharedView = ivProfilePic;
                    String transitionName = getString(R.string.profile_transition);
                    ActivityOptions activityOptions = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        activityOptions = ActivityOptions.makeSceneTransitionAnimation(BaseActivity.this, android.util.Pair.create(sharedView, transitionName));
                    }
                    Intent toOpen = new Intent(BaseActivity.this, MyProfileActivity.class);
                    if (activityOptions != null) {
                        startActivity(toOpen, activityOptions.toBundle());
                    } else {
                        startActivity(toOpen);
                    }
    }

   private void removeDuplicates(ArrayList<SearchedLocationModel> list) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++)
            {
                if (list.get(i).getPlaceAddress().equalsIgnoreCase(list.get(j).getPlaceAddress()))
                {
                    list.remove(j--);
                    count--;
                }
            }
        }


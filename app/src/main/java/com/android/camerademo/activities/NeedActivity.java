package com.android.camerademo.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.snapit.up.Model.User;
import com.snapit.up.common.API;
import com.snapit.up.common.AddImageAPI;
import com.snapit.up.common.Global;
import com.snapit.up.common.Gps;
import com.snapit.up.common.ImagesAddedListener;
import com.snapit.up.common.Utils;
import com.snapitupandroid.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.echodev.resizer.Resizer;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/** Create a class add property */

public class NeedActivity extends AppCompatActivity implements View.OnClickListener {

    File imgFile;
    Gps gps;
    double source_lat, source_long;

    ImageView get_image, back_arrow, no_of_bedrooms_plus, no_of_bedroom_minus, bathroom_plus, no_of_bathroom_minus, no_of_garages_plus, no_of_garages_minus;
    TextView location_user, tv_output1, tv_output2, tv_output3;
    Toolbar toolbar;
    Button button_submit;
    SwitchCompat swimming_pool_garden, garden_switch;
    String mini_price = "500000", max_price = "10000000";

    CrystalRangeSeekbar rangeSeekbar;
    int l = 0;
    int j = 0;
    int k = 0;
    int swim = 0;
    int garden = 0;
    public int RC_REQUEST_CAMERA = 12313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need);

        GetFilepath gs = new GetFilepath();
        imgFile = gs.getFilepath();

        /** Getting the location using lat long*/
        gps = new Gps(NeedActivity.this);
        if (gps.canGetLocation()) {
            source_lat = gps.getLatitude();
            source_long = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

        try {
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "iwant1.jpg");
            if (f.exists()) {
                f.delete();
            }
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        } catch (Exception e) {
        }

/** initialize method find id*/

        button_submit = (Button) findViewById(R.id.button_submit);
        no_of_bedrooms_plus = (ImageView) findViewById(R.id.no_of_bedrooms_plus);
        no_of_bedroom_minus = (ImageView) findViewById(R.id.no_of_bedroom_minus);
        bathroom_plus = (ImageView) findViewById(R.id.bathroom_plus);
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        no_of_bathroom_minus = (ImageView) findViewById(R.id.no_of_bathroom_minus);
        no_of_garages_plus = (ImageView) findViewById(R.id.no_of_garages_plus);
        no_of_garages_minus = (ImageView) findViewById(R.id.no_of_garages_minus);
        location_user = (TextView) findViewById(R.id.location_user);
        tv_output1 = (TextView) findViewById(R.id.tv_output1);
        tv_output2 = (TextView) findViewById(R.id.tv_output2);
        tv_output3 = (TextView) findViewById(R.id.tv_output3);
        swimming_pool_garden = (SwitchCompat) findViewById(R.id.swimming_pool_garden);
        garden_switch = (SwitchCompat) findViewById(R.id.garden_switch);
        get_image = (ImageView) findViewById(R.id.get_image);


        /** Call method below */
        rangePicker();

        if (imgFile != null) {
            Glide.with(NeedActivity.this).load(imgFile).into(get_image);
        }

        /** set location*/
        getAddress(location_user, this, new ProgressDialog(this), source_lat, source_long);

        /** toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        /**Click on switch button functionality*/
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** Back press method use to back previous activity*/
                onBackPressed();
            }
        });

        /**Click on switch button functionality*/
        swimming_pool_garden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    swim = 1;
                } else {
                    swim = 0;
                }
            }
        });

        /**Click on switch button functionality*/
        garden_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    garden = 1;
                } else {
                    garden = 0;
                }
            }
        });

        /** Click submit button*/
        button_submit.setOnClickListener(this);

        /** Click all buttons functionality */
        no_of_bedrooms_plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (l < 20) {
                    l++;
                }
                tv_output1.setText(String.valueOf(l));

            }
        });

        no_of_bedroom_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (l > 0) {

                    l--;
                }
                tv_output1.setText(String.valueOf(l));

            }
        });

        bathroom_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (j < 20) {
                    j++;
                }
                tv_output2.setText(String.valueOf(j));

            }
        });

        no_of_bathroom_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (j > 0) {
                    j--;
                }
                tv_output2.setText(String.valueOf(j));

            }
        });

        no_of_garages_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (k < 20) {
                    k++;
                }
                tv_output3.setText(String.valueOf(k));

            }
        });

        no_of_garages_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (k > 0) {
                    k--;
                }
                tv_output3.setText(String.valueOf(k));

            }
        });

        /** Call method below*/
        proceedAfterPermission();

        /** Using Range seekbar set maximum and minimum value*/
        rangeSeekbar.setMinStartValue(500000).setMaxStartValue(10000000).apply();
    }

    /**
     * Range seekbar method
     */
    private void rangePicker() {
        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar1);
        rangeSeekbar.setMinValue(500000);
        rangeSeekbar.setMaxValue(10000000);

/** get min and max text view*/
        final TextView tvMin = (TextView) findViewById(R.id.mini_price);
        final TextView tvMax = (TextView) findViewById(R.id.max_price);

        /** set listener*/
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                /** change number format*/
                String min = NumberFormat.getIntegerInstance().format(minValue);
                String max = NumberFormat.getIntegerInstance().format(maxValue);
                if (max.equals("10,000,000")) {
                    tvMax.setText("R " + max + "+");
                } else if (max.equals("1,00,00,000")) {
                    tvMax.setText("R " + max + "+");
                } else {
                    tvMax.setText("R " + max);
                }
                tvMin.setText("R " + min);

            }
        });

        /** set range Seekbar  final value listener*/
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                mini_price = String.valueOf(minValue);
                max_price = String.valueOf(maxValue);

            }
        });
    }

    /**
     * Proceed permission use easy image for open camera
     */
    private void proceedAfterPermission() {
        EasyImage.openCamera(NeedActivity.this, RC_REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            finish();
        }

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Utils.showShortToast(NeedActivity.this, "AA");
                finish();
            }

            /** Picked image method using easy image*/
//            @Override
            public void onImagePicked(final File imageFile, EasyImage.ImageSource source, int type) {
               final Bitmap bmp=decodeFile(imageFile.getPath());
                get_image.setImageBitmap(bmp);


               /** Using progress dialog to load the image*/
                final ProgressDialog loader = new ProgressDialog(NeedActivity.this);
                loader.setMessage("Loading...");
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                new AsyncTask<Void, Void, File>() {
                    @Override
                    protected File doInBackground(Void... voids) {
                        File resizedImage = null;
                        try {

                            File file = savebitmap(bmp);

                            resizedImage = new Resizer(NeedActivity.this)
                                    .setTargetLength(800)
                                    .setQuality(60)
                                    .setOutputFormat("JPEG")
                                    .setOutputDirPath(Environment.getExternalStorageDirectory().getPath() + "/image")
                                    .setSourceImage(file)
                                    .getResizedFile();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return resizedImage;
                    }

                    @Override
                    protected void onPostExecute(File aVoid) {
                        super.onPostExecute(aVoid);
                        loader.dismiss();
                        if (aVoid != null) {
                                setImage(aVoid);
                        }
                    }
                }.execute();

            }
        });
    }

    /**
     * Using glide for set image
     */
    private void setImage(File aVoid) {
        Glide.with(NeedActivity.this).load(aVoid).into(get_image);
        imgFile = aVoid;
    }

    /**
     * Getting address using lat long
     */
    public static void getAddress(final TextView tv, final Context c, final ProgressDialog pd, final double source_lat, final double source_long) {
        tv.setText("Please wait..");
        if (pd != null && !pd.isShowing()) {
            pd.show();
        }
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String address = null;
                double Lat = source_lat;
                double Long = source_long;

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(c, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(Lat, Long, 1);
                    if (addresses != null && addresses.size() > 0) {
                        String address1 = addresses.get(0).getAddressLine(0);
                        String address11 = addresses.get(0).getAddressLine(1);
                        String city = addresses.get(0).getLocality();
                        if (address1 != null) {
                            address = address1;
                        }
                        if (address11 != null) {
                            address = address + address11;
                        }
                    }
                } catch (IOException e) {
                }
                return address;
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                tv.setText(aVoid);
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        }.execute();
    }

    /**
     * Method on OptionsItemSelected menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }

    /**
     * Click submit button using dialog box error message display no image found
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_submit:
                if (imgFile == null) {
                    AlertDialog dialog;
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NeedActivity.this, R.style.AlertDialogCustom);
                    alertDialog.setTitle("Snapitup");

                    /** Using this line Alert dialog box not canceled */
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage("No image found");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /** Alert dialog box dismiss*/
                            dialog.dismiss();
                            finish();
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /** Alert dialog box dismiss*/
                            dialog.dismiss();
                        }
                    });
                    dialog = alertDialog.create();

                    /** Display Alert dialog box*/
                    dialog.show();

                    return;
                } else if (Utils.isConnectingToInternet(NeedActivity.this)) {
                    /*
            ** Sending parameters to server
            * @params user_security_hash
            * @params no_of_bedroom
            * @params no_of_bathroom
            * @params gargage
            * @params swimming_pool
            * @params landscaped_garden
            * @params property_latitude
            * @params property_longitude
            * @params property_range_from
            * @params property_range_to
            * @params location_name
            * @params user_id
            * */
                    Gson gson = new Gson();
                    String json = Utils.getUserPreferences(NeedActivity.this, Global.USER);
                    User obj = gson.fromJson(json, User.class);

                    // Pass the parameters to according to the API.
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("no_of_bedroom", "" + tv_output1.getText().toString());
                    params.put("no_of_bathroom", "" + tv_output2.getText().toString());
                    params.put("gargage", "" + tv_output3.getText().toString());
                    params.put("property_range_from", "" + mini_price);
                    params.put("property_range_to", "" + max_price);
                    params.put("swimming_pool", "" + swim);
                    params.put("landscaped_garden", "" + garden);
                    params.put("property_latitude", String.valueOf(source_lat));
                    params.put("property_longitude", String.valueOf(source_long));
                    params.put("location_name", "" + location_user.getText().toString());
                    params.put("user_security_hash", obj.user_security_hash);
                    params.put("user_id", obj.user_id);
                   Log.e("BB",""+params);

                    AddImageAPI api = new AddImageAPI(NeedActivity.this, imgFile, params, new ImagesAddedListener() {
                        @Override
                        public void onImageAdded(String response) {
                            try {
                                final JSONObject obj = new JSONObject(response);
                                if (API.success(obj)) {
                                    Log.e("CC","cc"+response);
                                    AlertDialog dialog;
                                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NeedActivity.this, R.style.AlertDialogCustom);
                                    alertDialog.setTitle("Snapitup");
                                    alertDialog.setMessage(API.rMessage(obj));

                                    /** Using this line Alert dialog box not canceled */
                                    alertDialog.setCancelable(false);
                                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            /** Alert dialog box is dismiss*/
                                            dialog.dismiss();
                                            Intent intent = new Intent(NeedActivity.this, ViewActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                                            intent.putExtra("prop", API.getData(obj));
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                    dialog = alertDialog.create();

                                    /** Display alert dialog box*/
                                    dialog.show();
                                }
                                else {
                                    Toast.makeText(NeedActivity.this, ""+API.rMessage(obj), Toast.LENGTH_SHORT).show();}
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    api.execute();
                    break;
                } else {
                    /** Check internet connection is connect or not*/
                    Utils.showShortToast(NeedActivity.this, "Internet not connected");
                }
        }
    }

    /**
     * Click on back button
     */
    @Override
    public void onBackPressed() {
        /** Alert dialog method display the error message*/
        AlertDialog dialog;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NeedActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Snapitup");

        /** Using this line Alert dialog box not canceled */
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Do you really want to leave the screen?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /** Alert dialog box is dismiss*/
                dialog.dismiss();
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /** Alert dialog box is dismiss*/
                dialog.dismiss();
            }
        });
        dialog = alertDialog.create();

        /** Display alert dialog dialog box */
        dialog.show();
    }

    /**
 * Using ImageRotator class
 */
private class ImageRotator extends AsyncTask<String, Void, Bitmap> {
    protected Bitmap doInBackground(String... image_path) {

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 1;
        Bitmap bmOriginal = BitmapFactory.decodeFile(image_path[0], bitmapOptions);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bmOriginal = Bitmap.createBitmap(bmOriginal, 0, 0, bmOriginal.getWidth(), bmOriginal.getHeight(), matrix, true);
        return bmOriginal;
    }

    protected void onPostExecute(Bitmap result) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    /**
     * Using Bitmap to save the image jpeg
     */
    public File savebitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        long millis = new Date().getTime();
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "iwant" + millis + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        callBroadCast();
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }

    /**
     * Using broadcast method for external storage
     */
    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    /** orientation code*/

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        /** First decode with inJustDecodeBounds=true to check dimensions*/
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        /** Calculate inSampleSize*/
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

       /** Decode bitmap with inSampleSize set*/
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

           /** Calculate ratios of height and width to requested height and width*/
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

/* using bitmap for rotate image*/
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Log.e("AA",orientation+" orientation");
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        Log.e("AA",rotatedImg+" rotatedImg");
        img.recycle();
        return rotatedImg;
    }

    /** you can provide file path here*/
    public Bitmap decodeFile(String path) {
        int orientation;
        try {
            if (path == null) {
                return null;
            }
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 0;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            /** decode with inSampleSize*/
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bm = BitmapFactory.decodeFile(path, o2);
            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);

            orientation = exif
                    .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Matrix m = new Matrix();

            if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
                m.postRotate(180);
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90);
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270);
                Log.e("in orientation", "" + orientation);
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
                return bitmap;
            }
            return bitmap;
        } catch (Exception e) {
            return null;
        }

    }
}



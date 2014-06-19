package com.mercadopago.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fingerprint {
    private static final String TAG = "Fingerprint";
    private static final String SHARED_PREFS_FINGERPRINT_LOCATION = "FINGERPRINT_LOCATION";

    private transient Context mContext;
    private transient LocationManager mLocationManager;
    private transient LocationListener mLocationListener;
    public ArrayList<VendorId> vendorIds;
    public String model;
    public String os;
    public String systemVersion;
    public String resolution;
    public Long ram;
    public long diskSpace;
    public long freeDiskSpace;
    public VendorSpecificAttributes vendorSpecificAttributes;
    public Location location;


    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        }
        catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    public Fingerprint(Context context) {
        mContext = context;

        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new FingerprintLocationListener();
        vendorIds = getVendorIds();
        model = getModel();
        os = getOs();
        systemVersion = getSystemVersion();
        resolution = getResolution();
        ram = getRam();
        diskSpace = getDiskSpace();
        freeDiskSpace = getFreeDiskSpace();
        vendorSpecificAttributes = getVendorSpecificAttributes();
        location = getLocation();
    }

    public ArrayList<VendorId> getVendorIds() {
        ArrayList<VendorId> vendorIds = new ArrayList<VendorId>();

        // android_id
        String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        vendorIds.add(new VendorId("android_id", androidId));

        // serial
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            if (!TextUtils.isEmpty(Build.SERIAL) && !"unknown".equals(Build.SERIAL)) {
                vendorIds.add(new VendorId("serial", Build.SERIAL));
            }
        }

        // SecureRandom
        String randomId = SecureRandomId.getValue(mContext);
        if (!TextUtils.isEmpty(randomId)) {
            vendorIds.add(new VendorId("fsuuid", randomId));
        }

        return vendorIds;
    }

    private static class SecureRandomId {
        private static final String FILENAME_FSUUID = "fsuuid";

        private static String mFSUUID = null;

        private static String readFile(File file) throws IOException {
            RandomAccessFile f = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            f.close();
            return new String(bytes);
        }

        private static void writeFile(File file) throws IOException {
            FileOutputStream out = new FileOutputStream(file);
            SecureRandom random = new SecureRandom();
            String id = new BigInteger(64, random).toString(16);
            out.write(id.getBytes());
            out.close();
        }

        public synchronized static String getValue(Context context) {
            if (mFSUUID == null) {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File(path + "/" + context.getPackageName(), FILENAME_FSUUID);
                try {
                    if (!file.exists()) {
                        boolean dirs = file.getParentFile().mkdirs();
                        if (dirs) {
                            writeFile(file);
                        }
                    }
                    mFSUUID = readFile(file);
                } catch (Exception ignored) {}
            }

            return mFSUUID;
        }
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getOs() {
        return "android";
    }

    public String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getResolution() {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();

        return display.getWidth() + "x" + display.getHeight();
    }

    public Long getRam() {
        Long ram = null;
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
            String load = reader.readLine();

            Pattern pattern = Pattern.compile("(\\d+)");
            Matcher matcher = pattern.matcher(load);
            if (matcher.find()) {
                ram = Long.valueOf(matcher.group(0));
            }
        } catch (Exception ex) {}

        return ram;
    }

    public long getDiskSpace() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockSize() * (long) statFs.getBlockCount()) / 1048576;
    }

    public long getFreeDiskSpace() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockSize() * statFs.getAvailableBlocks()) / 1048576;
    }

    public VendorSpecificAttributes getVendorSpecificAttributes() {
        return new VendorSpecificAttributes();
    }

    public Location getLocation() {
        Location location = new Location();
        try {
            // we're always gonna request a new location to keep it updated on local storage, check if we've
            // location provider available since it crashes on simulator.
            if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        0, 0, mLocationListener, Looper.getMainLooper());
            }

            // we've a few options here to get the location while we pull a new one.
            // 1. get it from the local storage.
            // 2. get it from provider's cache on the case we don't have any stored.
            android.location.Location cached = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location.hasLocation()) {
                return location;
            } else if (cached != null) {
                location = new Location(cached.getLatitude(), cached.getLongitude());
                setLocation(location);

                return location;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void setLocation(Location location) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SHARED_PREFS_FINGERPRINT_LOCATION, location.toString());
        editor.commit();
    }

    private class VendorId {
        private String name;
        private String value;

        public VendorId(String mname, String mvalue) {
            name = mname;
            value = mvalue;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    private class VendorSpecificAttributes {

        boolean featureCamera;
        boolean featureFlash;
        boolean featureFrontCamera;
        String product;
        String device;
        String platform;
        String brand;
        boolean featureAccelerometer;
        boolean featureBluetooth;
        boolean featureCompass;
        boolean featureGps;
        boolean featureGyroscope;
        boolean featureMicrophone;
        boolean featureNfc;
        boolean featureTelephony;
        boolean featureTouchScreen;
        String manufacturer;
        float screenDensity;

        private VendorSpecificAttributes() {
            this.featureCamera = getFeatureCamera();
            this.featureFlash = getFeatureFlash();
            this.featureFrontCamera = getFeatureFrontCamera();
            this.product = getProduct();
            this.device = getDevice();
            this.platform = getPlatform();
            this.brand = getBrand();
            this.featureAccelerometer = getFeatureAccelerometer();
            this.featureBluetooth = getFeatureBluetooth();
            this.featureCompass = getFeatureCompass();
            this.featureGps = getFeatureGps();
            this.featureGyroscope = getFeatureGyroscope();
            this.featureMicrophone = getFeatureMicrophone();
            this.featureNfc = getFeatureNfc();
            this.featureTelephony = getFeatureTelephony();
            this.featureTouchScreen = getFeatureTouchScreen();
            this.manufacturer = getManufacturer();
            this.screenDensity = getScreenDensity();
        }

        public boolean getFeatureCamera() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        }

        public boolean getFeatureFlash() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        }

        public boolean getFeatureFrontCamera() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        }

        public String getProduct() {
            return Build.PRODUCT;
        }

        public String getDevice() {
            return Build.DEVICE;
        }

        public String getPlatform() {
            return getSystemProperty("ro.product.cpu.abi");
        }

        public String getBrand() {
            return Build.BRAND;
        }

        public boolean getFeatureAccelerometer() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        }

        public boolean getFeatureBluetooth() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
        }

        public boolean getFeatureCompass() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
        }

        public boolean getFeatureGps() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        }

        public boolean getFeatureGyroscope() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        }

        public boolean getFeatureMicrophone() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
        }

        public boolean getFeatureNfc() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
        }

        public boolean getFeatureTelephony() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        }

        public boolean getFeatureTouchScreen() {
            return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN);
        }

        public String getManufacturer() {
            return Build.MANUFACTURER;
        }

        public float getScreenDensity() {
            return mContext.getResources().getDisplayMetrics().density;
        }
    }

    private class Location {
        private static final String LOCATION_TIMESTAMP = "timestamp";
        private static final String LOCATION_LATITUDE = "latitude";
        private static final String LOCATION_LONGITUDE = "longitude";

        private JSONObject location;

        public Location() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            try {
                location = new JSONObject(prefs.getString(SHARED_PREFS_FINGERPRINT_LOCATION, "{}"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public Location(double latitude, double longitude) {
            location = new JSONObject();
            try {
                location.put(LOCATION_TIMESTAMP, System.currentTimeMillis() / 1000L);
                location.put(LOCATION_LATITUDE, latitude);
                location.put(LOCATION_LONGITUDE, longitude);
            } catch (JSONException ignored) {}
        }

        @Override
        public String toString() {
            Gson gson = new Gson();
            try {
                return gson.toJson(this);
            } catch (Exception ignored) {
            }

            return null;
        }

        public boolean hasLocation() {
            return getLatitude() != 0 && getLatitude() != 0;
        }

        public long getTimestamp() {
            if (location != null) {
                try {
                    return location.getLong(LOCATION_TIMESTAMP);
                } catch (JSONException ignored) {}
            }

            return System.currentTimeMillis() / 1000L;
        }


        public double getLatitude() {
            if (location != null) {
                try {
                    return location.getDouble(LOCATION_LATITUDE);
                } catch (JSONException ignored) {}
            }

            return 0;
        }

        public double getLongitude() {
            if (location != null) {
                try {
                    return location.getDouble(LOCATION_LONGITUDE);
                } catch (JSONException e) {}
            }

            return 0;
        }
    }

    private class FingerprintLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(android.location.Location location) {
            setLocation(new Location(location.getLatitude(), location.getLongitude()));
            mLocationManager.removeUpdates(mLocationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    }
}

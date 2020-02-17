package com.fauzan.smartpeaklib;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.basewin.aidl.OnPrinterListener;
import com.basewin.services.PrinterBinder;
import com.basewin.services.ServiceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmartpeakPrint {
    private Context mContext;

    public static final String TEXT_CENTER = "center";
    public static final String TEXT_LEFT = "left";
    public static final String TEXT_RIGHT = "right";
    public static final String TEXT_BOLD = "1";
    public static final String TEXT_NORMAL = "0";

    public static final String DIMENSION_ONE = "one-dimension";
    public static final String DIMENSION_TWO = "two-dimension";


    private static volatile SmartpeakPrint smartPeakPrintInstance;

    //private constructor.
    private SmartpeakPrint(Context context) {

        //Prevent form the reflection api.
        if (smartPeakPrintInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        ServiceManager.getInstence().init(context.getApplicationContext());

        this.mContext = context;
    }

    public static SmartpeakPrint getInstance(Context context) {
        //Double check locking pattern
        if (smartPeakPrintInstance == null) { //Check for the first time

            synchronized (SmartpeakPrint.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (smartPeakPrintInstance == null)
                    smartPeakPrintInstance = new SmartpeakPrint(context);
            }
        }

        return smartPeakPrintInstance;
    }

    public static void print(JSONArray jsonArray, PrinterCallback printerCallback) {
        try {
            ServiceManager.getInstence().getPrinter().printBottomFeedLine(3);
            ServiceManager.getInstence().getPrinter().print(new JSONObject().put("spos", jsonArray).toString(), null, new PrinterListener(printerCallback));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void print(JSONArray jsonArray, Bitmap[] bitmap, PrinterCallback printerCallback) {
        try {
            ServiceManager.getInstence().getPrinter().printBottomFeedLine(3);
            ServiceManager.getInstence().getPrinter().print(new JSONObject().put("spos", jsonArray).toString(), bitmap, new PrinterListener(printerCallback));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject LineSplit() {
        return PrintText("------------------------------------------------", 2, SmartpeakPrint.TEXT_CENTER, SmartpeakPrint.TEXT_NORMAL);
    }

    public static JSONObject PrintLogo(String contentType, String position) {
        JSONObject logo = new JSONObject();
        try {
            logo.put("content-type", contentType);
            logo.put("position", position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return logo;
    }

    public static JSONObject PrintDimensionObject(String contentType, String text, int size, String position, int height) {
        if (!contentType.equals(DIMENSION_ONE) && !contentType.equals(DIMENSION_TWO))
            throw new RuntimeException("Invalid content type");

        JSONObject json = new JSONObject();
        try {
            json.put("content-type", contentType);
            json.put("content", text);
            json.put("size", size);
            json.put("position", position);
            json.put("height", height);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject EmptySpace() {
        return PrintText("                        ", 2, SmartpeakPrint.TEXT_CENTER, SmartpeakPrint.TEXT_BOLD);
    }

    public static JSONObject PrintText(String text, int size, String position) {
        JSONObject json = new JSONObject();
        try {
            json.put("content-type", "txt");
            json.put("content", text);
            json.put("size", size);
            json.put("position", position);
            json.put("offset", "0");
            json.put("bold", "0");
            json.put("italic", "0");
            json.put("height", "-1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject PrintText(String text, int size, String position, String textStyle) {
        JSONObject json = new JSONObject();
        try {
            json.put("content-type", "txt");
            json.put("content", text);
            json.put("size", size);
            json.put("position", position);
            json.put("offset", "0");
            json.put("bold", textStyle);
            json.put("italic", "0");
            json.put("height", "-1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    static class PrinterListener implements OnPrinterListener {
        private final String TAG = PrinterListener.class.getSimpleName();
        private PrinterCallback printerCallback;

        public PrinterListener(PrinterCallback printerCallback) {
            this.printerCallback = printerCallback;
        }

        @Override
        public void onStart() {
            Log.d(TAG, "Start print");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    printerCallback.onPrintStart();
                }
            });
        }

        @Override
        public void onFinish() {
            Log.d(TAG, "SmartpeakPrint success");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    printerCallback.onPrintFinish();
                }
            });
        }

        @Override
        public void onError(int errorCode, String detail) {
            Log.e(TAG, "SmartpeakPrint error: " + "error code = " + errorCode + " detail = " + detail);

            if (errorCode == PrinterBinder.PRINTER_ERROR_NO_PAPER) {
                Log.e(TAG, "onError: paper runs out during printing");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        printerCallback.onPrintError("Paper runs out during printing");
                    }
                });
            }
            if (errorCode == PrinterBinder.PRINTER_ERROR_OVER_HEAT) {
                Log.e(TAG, "onError: over heat during printing");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        printerCallback.onPrintError("Over heat during printing");
                    }
                });
            }
            if (errorCode == PrinterBinder.PRINTER_ERROR_OTHER) {
                Log.e(TAG, "onError: other error happen during printing");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        printerCallback.onPrintError("Other error happen during printing");
                    }
                });
            }
        }
    }

    public interface PrinterCallback {
        void onPrintStart();

        void onPrintFinish();

        void onPrintError(String errMsg);
    }
}

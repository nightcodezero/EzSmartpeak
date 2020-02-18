package com.fauzan.smartpeaklib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.basewin.aidl.OnBarcodeCallBack;
import com.basewin.aidl.OnPrinterListener;
import com.basewin.define.FontsType;
import com.basewin.services.PrinterBinder;
import com.basewin.services.ServiceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EzSmartpeak {
    private Context mContext;

    private static volatile EzSmartpeak smartPeakInstanceEz;

    //private constructor.
    private EzSmartpeak(Context context) {

        //Prevent form the reflection api.
        if (smartPeakInstanceEz != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        ServiceManager.getInstence().init(context.getApplicationContext());

        this.mContext = context;
    }

    public static EzSmartpeak getInstance(Context context) {
        //Double check locking pattern
        if (smartPeakInstanceEz == null) { //Check for the first time

            synchronized (EzSmartpeak.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (smartPeakInstanceEz == null)
                    smartPeakInstanceEz = new EzSmartpeak(context);
            }
        }

        return smartPeakInstanceEz;
    }

    public static void getPrint(JSONArray jsonArray, PrinterCallback printerCallback) {
        try {
            ServiceManager.getInstence().getPrinter().setPrintFont(FontsType.simsun);
            ServiceManager.getInstence().getPrinter().printBottomFeedLine(3);
            ServiceManager.getInstence().getPrinter().print(new JSONObject().put("spos", jsonArray).toString(), null, new PrinterListener(printerCallback));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPrint(JSONArray jsonArray, int image, PrinterCallback printerCallback) {
        try {
            Bitmap qr = BitmapFactory.decodeResource(smartPeakInstanceEz.mContext.getResources(), image);
            Bitmap[] bitmaps = new Bitmap[]{qr};
            ServiceManager.getInstence().getPrinter().setPrintFont(FontsType.simsun);
            ServiceManager.getInstence().getPrinter().printBottomFeedLine(3);
            ServiceManager.getInstence().getPrinter().print(new JSONObject().put("spos", jsonArray).toString(), bitmaps, new PrinterListener(printerCallback));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject LineSplit() {
        return PrintText("--------------------------------", 2, Position.CENTER, TextStyle.BOLD);
    }

    public static JSONObject PrintLogo(String contentType, Position position) {
        JSONObject json = new JSONObject();
        try {
            json.put("content-type", contentType);
            if (position == Position.CENTER)
                json.put("position", "center");
            else if (position == Position.LEFT)
                json.put("position", "left");
            else
                json.put("position", "right");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject PrintDimensionObject(Dimension dimension, String text, int size, Position position, int height) {
        JSONObject json = new JSONObject();
        try {
            if (dimension == Dimension.ONE)
                json.put("content-type", "one-dimension");
            else
                json.put("content-type", "two-dimension");
            json.put("content", text);
            json.put("size", size);
            if (position == Position.CENTER)
                json.put("position", "center");
            else if (position == Position.LEFT)
                json.put("position", "left");
            else
                json.put("position", "right");
            json.put("height", height);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject EmptySpace() {
        return PrintText("                        ", 2, Position.CENTER, TextStyle.BOLD);
    }

    public static JSONObject PrintText(String text, int size, Position position) {
        JSONObject json = new JSONObject();
        try {
            json.put("content-type", "txt");
            json.put("content", text);
            json.put("size", size);
            if (position == Position.CENTER)
                json.put("position", "center");
            else if (position == Position.LEFT)
                json.put("position", "left");
            else
                json.put("position", "right");
            json.put("offset", "0");
            json.put("bold", "0");
            json.put("italic", "0");
            json.put("height", "-1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject PrintText(String text, int size, Position position, TextStyle textStyle) {
        JSONObject json = new JSONObject();
        try {
            json.put("content-type", "txt");
            json.put("content", text);
            json.put("size", size);
            if (position == Position.CENTER)
                json.put("position", "center");
            else if (position == Position.LEFT)
                json.put("position", "left");
            else
                json.put("position", "right");
            json.put("offset", "0");
            if (textStyle == TextStyle.BOLD)
                json.put("bold", "1");
            else
                json.put("bold", "0");
            if (textStyle == TextStyle.ITALIC)
                json.put("italic", "1");
            else
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
            Log.d(TAG, "Print start");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    printerCallback.onPrintStart();
                }
            });
        }

        @Override
        public void onFinish() {
            Log.d(TAG, "Print success");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    printerCallback.onPrintFinish();
                }
            });
        }

        @Override
        public void onError(int errorCode, String detail) {
            Log.e(TAG, "Print error: " + "error code = " + errorCode + " detail = " + detail);

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

    // Scan QR Code
    public static void getScanner(final ScanCallback scanCallback) {
        try {
            ServiceManager.getInstence().getScan().startScan(10, new OnBarcodeCallBack() {
                @Override
                public void onScanResult(String s) {
                    scanCallback.onScanResult(s);
                }

                @Override
                public void onFinish() {
                    scanCallback.onFinish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getScanner(int timeout, final ScanCallback scanCallback) {
        try {
            ServiceManager.getInstence().getScan().startScan(timeout, new OnBarcodeCallBack() {
                @Override
                public void onScanResult(String s) {
                    scanCallback.onScanResult(s);
                }

                @Override
                public void onFinish() {
                    scanCallback.onFinish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Beep sound
    // default value : time = 100ms; freq = 500;voice = 1
    public static void getBeeper() {
        try {
            ServiceManager.getInstence().getBeeper().beep(100, 500, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBeeper(int time) {
        try {
            ServiceManager.getInstence().getBeeper().beep(time, 500, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBeeper(int time, int freq) {
        try {
            ServiceManager.getInstence().getBeeper().beep(time, freq, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getBeeper(int time, int freq, int voice) {
        try {
            ServiceManager.getInstence().getBeeper().beep(time, freq, voice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ScanCallback {
        void onScanResult(String s);

        void onFinish();
    }

    public interface PrinterCallback {
        void onPrintStart();

        void onPrintFinish();

        void onPrintError(String errMsg);
    }
}

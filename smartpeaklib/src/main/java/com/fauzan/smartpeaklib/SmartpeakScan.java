package com.fauzan.smartpeaklib;

import android.os.RemoteException;

import com.basewin.aidl.OnBarcodeCallBack;
import com.basewin.services.ServiceManager;

public class SmartpeakScan {

    public static void getInstance(final ScanCallback scanCallback) {
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

    public static void getInstance(int timeout, final ScanCallback scanCallback) {
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

    public interface ScanCallback {
        void onScanResult(String s);

        void onFinish();
    }
}

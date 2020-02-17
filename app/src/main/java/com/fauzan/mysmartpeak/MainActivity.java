package com.fauzan.mysmartpeak;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fauzan.smartpeaklib.EzSmartpeak;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EzSmartpeak.getInstance(this);

        Button btn_print = findViewById(R.id.btn_print);
        Button btn_scan = findViewById(R.id.btn_scan_qr);
        Button btn_beep = findViewById(R.id.btn_beep);

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray printValues = new JSONArray();
                printValues.put(EzSmartpeak.PrintLogo("jpg", EzSmartpeak.TEXT_CENTER));
                printValues.put(EzSmartpeak.PrintText("My Company name", 2, EzSmartpeak.TEXT_CENTER, EzSmartpeak.TEXT_BOLD));
                printValues.put(EzSmartpeak.EmptySpace());
                printValues.put(EzSmartpeak.PrintText("INVOICE NO    : " + "8632588981149056", 2, EzSmartpeak.TEXT_LEFT));
                printValues.put(EzSmartpeak.PrintText("TRANSACTION ID: " + "1000000002960639", 2, EzSmartpeak.TEXT_LEFT));
                printValues.put(EzSmartpeak.PrintText("MERCHANT NAME : " + "ONEPAY TPM", 2, EzSmartpeak.TEXT_LEFT));
                printValues.put(EzSmartpeak.PrintText("TERMINAL ID   : " + "10000069", 2, EzSmartpeak.TEXT_LEFT));
                printValues.put(EzSmartpeak.PrintText("MERCHANT ID   : " + "6057000000100001", 2, EzSmartpeak.TEXT_LEFT));
                printValues.put(EzSmartpeak.PrintText("WALLET TOKEN  : " + "1234567890192039009", 2, EzSmartpeak.TEXT_LEFT));
                printValues.put(EzSmartpeak.PrintText("AUTH CODE     : " + "12345678901920390091", 2, EzSmartpeak.TEXT_LEFT));
                printValues.put(EzSmartpeak.PrintText("DATE/TIME     : " + "27/11/2019 08:46:53 AM", 2, EzSmartpeak.TEXT_LEFT));
                printValues.put(EzSmartpeak.EmptySpace());
                printValues.put(EzSmartpeak.PrintText("E-WALLET PAYMENT", 2, EzSmartpeak.TEXT_CENTER, EzSmartpeak.TEXT_BOLD));
                printValues.put(EzSmartpeak.EmptySpace());
                printValues.put(EzSmartpeak.LineSplit());
                printValues.put(EzSmartpeak.PrintText("TOTAL:         RM 1.00 ", 2, EzSmartpeak.TEXT_LEFT, EzSmartpeak.TEXT_BOLD));
                printValues.put(EzSmartpeak.LineSplit());
                printValues.put(EzSmartpeak.PrintDimensionObject(EzSmartpeak.DIMENSION_TWO, "1000000002960639", 3, EzSmartpeak.TEXT_CENTER, 1));
                printValues.put(EzSmartpeak.PrintText("**** CUSTOMER COPY ****", 2, EzSmartpeak.TEXT_CENTER));
                EzSmartpeak.getPrint(printValues, R.drawable.m1paywallet_print, new EzSmartpeak.PrinterCallback() {
                    @Override
                    public void onPrintStart() {
                        Toast.makeText(getApplicationContext(), "Start getPrint", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrintFinish() {
                        Toast.makeText(getApplicationContext(), "Print success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrintError(String errMsg) {
                        Toast.makeText(getApplicationContext(), "Print error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EzSmartpeak.getScanner(10, new EzSmartpeak.ScanCallback() {
                    @Override
                    public void onScanResult(String s) {
                        Log.d(TAG, "onScanResult: " + s);
                    }

                    @Override
                    public void onFinish() {
                        Log.d(TAG, "Scan Finish");

                    }
                });
            }
        });

        btn_beep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EzSmartpeak.getBeeper();
            }
        });


    }
}



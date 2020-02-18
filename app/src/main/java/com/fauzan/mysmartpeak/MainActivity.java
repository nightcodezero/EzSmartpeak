package com.fauzan.mysmartpeak;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fauzan.smartpeaklib.Dimension;
import com.fauzan.smartpeaklib.EzSmartpeak;
import com.fauzan.smartpeaklib.Position;
import com.fauzan.smartpeaklib.TextStyle;

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
                printValues.put(EzSmartpeak.PrintLogo("jpg", Position.CENTER));
                printValues.put(EzSmartpeak.PrintText("My Company name", 2, Position.CENTER, TextStyle.BOLD));
                printValues.put(EzSmartpeak.EmptySpace());
                printValues.put(EzSmartpeak.PrintText("INVOICE NO: " + "8632588981149056", 2, Position.LEFT));
                printValues.put(EzSmartpeak.PrintText("TXN ID: " + "1000000002960639", 2, Position.LEFT));
                printValues.put(EzSmartpeak.PrintText("MERCHANT: " + "ONEPAY TPM", 2, Position.LEFT));
                printValues.put(EzSmartpeak.PrintText("TERMINAL ID: " + "10000069", 2, Position.LEFT));
                printValues.put(EzSmartpeak.PrintText("MERCHANT ID: " + "6057000000100001", 2, Position.LEFT));
                printValues.put(EzSmartpeak.PrintText("WALLET TOKEN: " + "1234567890192039009", 2, Position.LEFT));
                printValues.put(EzSmartpeak.PrintText("AUTH CODE: " + "12345678901920390091", 2, Position.LEFT));
                printValues.put(EzSmartpeak.PrintText("DATE/TIME: " + "27/11/2019 08:46:53 AM", 2, Position.LEFT));
                printValues.put(EzSmartpeak.EmptySpace());
                printValues.put(EzSmartpeak.PrintText("E-WALLET PAYMENT", 2, Position.CENTER, TextStyle.BOLD));
                printValues.put(EzSmartpeak.EmptySpace());
                printValues.put(EzSmartpeak.LineSplit());
                printValues.put(EzSmartpeak.PrintText("TOTAL:              RM " + "1.00", 2, Position.LEFT, TextStyle.BOLD));
                printValues.put(EzSmartpeak.LineSplit());
                printValues.put(EzSmartpeak.PrintDimensionObject(Dimension.TWO, "1000000002960639", 3, Position.CENTER, 1)); // print qrcode
                printValues.put(EzSmartpeak.PrintText("**** CUSTOMER COPY ****", 2, Position.CENTER));
                EzSmartpeak.getPrint(printValues, R.drawable.logo, new EzSmartpeak.PrinterCallback() {
                    @Override
                    public void onPrintStart() {
                        Log.d(TAG, "Start print");
                    }

                    @Override
                    public void onPrintFinish() {
                        Log.d(TAG, "Print success");
                    }

                    @Override
                    public void onPrintError(String errMsg) {
                        Log.e(TAG, "onPrintError: "+errMsg );
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



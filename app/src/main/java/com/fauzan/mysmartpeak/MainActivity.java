package com.fauzan.mysmartpeak;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fauzan.smartpeaklib.SmartpeakPrint;
import com.fauzan.smartpeaklib.SmartpeakScan;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartpeakPrint.getInstance(this);

        Button btn_print = findViewById(R.id.btn_print);
        Button btn_scan = findViewById(R.id.btn_scan_qr);

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray printValues = new JSONArray();
//        printValues.put(SmartpeakPrint.PrintLogo("jpg", SmartpeakPrint.TEXT_CENTER));
                printValues.put(SmartpeakPrint.PrintText("MobilityOne Sdn Bhd", 2, SmartpeakPrint.TEXT_CENTER, SmartpeakPrint.TEXT_BOLD));
                printValues.put(SmartpeakPrint.EmptySpace());
                printValues.put(SmartpeakPrint.PrintText("INVOICE NO    : " + "8632588981149056",2,  SmartpeakPrint.TEXT_LEFT));
                printValues.put(SmartpeakPrint.PrintText("TRANSACTION ID: " + "1000000002960639",2,  SmartpeakPrint.TEXT_LEFT));
                printValues.put(SmartpeakPrint.PrintText("MERCHANT NAME : " + "ONEPAY TPM",2,  SmartpeakPrint.TEXT_LEFT));
                printValues.put(SmartpeakPrint.PrintText("TERMINAL ID   : " + "10000069",2,  SmartpeakPrint.TEXT_LEFT));
                printValues.put(SmartpeakPrint.PrintText("MERCHANT ID   : " + "6057000000100001",2,  SmartpeakPrint.TEXT_LEFT));
                printValues.put(SmartpeakPrint.PrintText("WALLET TOKEN  : " + "1234567890192039009",2,  SmartpeakPrint.TEXT_LEFT));
                printValues.put(SmartpeakPrint.PrintText("AUTH CODE     : " + "12345678901920390091",2,  SmartpeakPrint.TEXT_LEFT));
                printValues.put(SmartpeakPrint.PrintText("DATE/TIME     : " + "27/11/2019 08:46:53 AM",2,  SmartpeakPrint.TEXT_LEFT));
                printValues.put(SmartpeakPrint.EmptySpace());
                printValues.put(SmartpeakPrint.PrintText("BOOST PAYMENT",2,  SmartpeakPrint.TEXT_CENTER, SmartpeakPrint.TEXT_BOLD));
                printValues.put(SmartpeakPrint.EmptySpace());
                printValues.put(SmartpeakPrint.LineSplit());
                printValues.put(SmartpeakPrint.PrintText("TOTAL:         RM 1.00 ",2,  SmartpeakPrint.TEXT_LEFT, SmartpeakPrint.TEXT_BOLD));
                printValues.put(SmartpeakPrint.LineSplit());
                printValues.put(SmartpeakPrint.PrintDimensionObject(SmartpeakPrint.DIMENSION_TWO, "HELLO", 3, SmartpeakPrint.TEXT_CENTER, 1));
                printValues.put(SmartpeakPrint.PrintText("**** CUSTOMER COPY ****",2,  SmartpeakPrint.TEXT_CENTER));

                SmartpeakPrint.print(printValues, null, new SmartpeakPrint.PrinterCallback() {
                    @Override
                    public void onPrintStart() {
                        Toast.makeText(getApplicationContext(), "Start print", Toast.LENGTH_SHORT).show();
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
                SmartpeakScan.getInstance(10, new SmartpeakScan.ScanCallback() {
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


    }
}

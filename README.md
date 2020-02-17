# EzSmartpeak
EzSmartpeak is an Android library that simplifies the process of implement Smartpeak Terminal Library.

Currently supported features:
* Print receipt
* Scan QRCode/Barcode
* Play beep sound

Usage
-----

### Dependency

Step 1. Add the JitPack repository to your build file. Add it in your root build.gradle at the end of repositories:

```java
allprojects {
  repositories {
    ...
    maven { url "https://jitpack.io" }
  }
}
```
Step 2. Add the dependency
```java
dependencies {
        implementation 'com.github.muhdfauzan93:EzSmartpeak:<latest version>'
}
```

How to use it
-----
### Set up
Pass the application context to EzSmartpeak
```java
EzSmartpeak.getInstance(this);
```

### Usage
#### Set Print Text
```java
EzSmartpeak.PrintText("Test print", 2, EzSmartpeak.TEXT_CENTER);
```
with text styles
```java
EzSmartpeak.PrintText("Test print", 2, EzSmartpeak.TEXT_CENTER, EzSmartpeak.TEXT_BOLD);
```
| Attribute | Use |
| ----------| --- |
| TEXT_BOLD | sets the text bold |
| TEXT_CENTER | sets the text to centre |
| TEXT_LEFT | sets the text to left 
| TEXT_RIGHT | sets the text to right |
#### Templates
Add empty space between texts
```java
EzSmartpeak.EmptySpace()
```
Add line split
```java
EzSmartpeak.LineSplit() // Result : =======================
```
#### Set Print Logo
```java
EzSmartpeak.PrintLogo("jpg", EzSmartpeak.TEXT_CENTER))
```
#### Set Print Barcode or QRCode
```java
EzSmartpeak.PrintDimensionObject(EzSmartpeak.DIMENSION_TWO, "123131231", 3, EzSmartpeak.TEXT_CENTER, 1);
```
| Attribute | Use |
| ----------| --- |
| DIMENSION_ONE | to print barcode |
| DIMENSION_TWO | to print qrcode |
#### Set Print Callback - MUST IMPLEMENT
``printValues`` must be a JSON Array
```java
EzSmartpeak.getPrint(printValues, new EzSmartpeak.PrinterCallback() {
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
```
with logo
```java
EzSmartpeak.getPrint(printValues, R.drawable.my_logo, new EzSmartpeak.PrinterCallback() {
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
```
#### Scan QRCode or Barcode
```java
EzSmartpeak.getScanner(new EzSmartpeak.ScanCallback() {
    @Override
    public void onScanResult(String s) {
        Log.d(TAG, "onScanResult: " + s);
    }

    @Override
    public void onFinish() {
        Log.d(TAG, "Scan Finish");

    }
});
```
with custom processing timeout. Default : 10
```java
EzSmartpeak.getScanner(30, new EzSmartpeak.ScanCallback() {
    @Override
    public void onScanResult(String s) {
        Log.d(TAG, "onScanResult: " + s);
    }

    @Override
    public void onFinish() {
        Log.d(TAG, "Scan Finish");

    }
});
```
#### Beep Sound
```java
EzSmartpeak.getBeeper();
```
Code Sample - Print Receipt
-----
```java
JSONArray printValues = new JSONArray();
printValues.put(EzSmartpeak.PrintText("DATE/TIME     : " + "27/11/2019 08:46:53 AM", 2, EzSmartpeak.TEXT_LEFT));
printValues.put(EzSmartpeak.EmptySpace());
printValues.put(EzSmartpeak.PrintText("TEST PAYMENT", 2, EzSmartpeak.TEXT_CENTER, EzSmartpeak.TEXT_BOLD));
printValues.put(EzSmartpeak.LineSplit());
printValues.put(EzSmartpeak.PrintText("TOTAL:         RM 1.00 ", 2, EzSmartpeak.TEXT_LEFT, EzSmartpeak.TEXT_BOLD));
printValues.put(EzSmartpeak.LineSplit());
printValues.put(EzSmartpeak.PrintDimensionObject(EzSmartpeak.DIMENSION_TWO, "12312332312", 3, EzSmartpeak.TEXT_CENTER, 1)); // Set qrcode
printValues.put(EzSmartpeak.PrintLogo("jpg", EzSmartpeak.TEXT_CENTER)); // set logo
// Set callback
EzSmartpeak.getPrint(printValues, R.drawable.my_logo, new EzSmartpeak.PrinterCallback() {
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

```
### Author
Maintained by [Fauzan](https://github.com/muhdfauzan93)

License
-------

    Copyright 2020 Fauzan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

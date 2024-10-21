//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.diskominfo.itsoreader;

import SecuGen.FDxSDKPro.JSGFPLib;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.opencsv.CSVReader;
import com.szzt.sdk.device.Constants;
import com.szzt.sdk.device.card.ContactlessCardReader;
import com.szzt.sdk.device.card.SmartCardReader;
import com.szzt.sdk.device.led.Led;
import com.techshino.fingerprint.FingerJNI;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.LogHelper;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.module.fingerprint.FingerprintCaptureListener;
import com.zkteco.android.biometric.module.fingerprint.FingerprintFactory;
import com.zkteco.android.biometric.module.fingerprint.FingerprintSensor;
import com.zkteco.android.biometric.module.fingerprint.exception.FingerprintSensorException;
import com.zkteco.zkfinger.FingerprintService;
import id.co.inti.ztlib.BaseActivity;
import id.co.inti.ztlib.R.raw;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import id.co.inti.ztlib.util.*;

public class readcard extends BaseActivity {
    private ContactlessCardReader mContactlessCardReader;
    private SmartCardReader mSmartCardReader;
    HandlerThread mWaitForCardHandlerThread;
    int mTestDeviceType = -1;
    int mdevicetype = -1;
    private Handler mFPHander;
    private static final String tag = "Card Reader";
    private byte[] uidCard = new byte[8];
    private byte[] challenge = new byte[8];
    private byte[] cardcontrol = new byte[54];
    private byte card_active = 0;
    private String mntkanan;
    private String mntkiri;
    private byte card_valid = 0;
    private byte[] packetBuffer = new byte[256];
    private byte[] secureprotocolBuffer = new byte[256];
    private byte[] calcChallengeBuf = new byte[41];
    private byte[] digitalSignature = new byte[80];
    private byte[] photo = new byte[4096];
    private byte[] tmpphoto = new byte[4096];
    private byte[] biodata = new byte[4096];
    private byte[] signature = new byte[4096];
    private byte[] minutea1 = new byte[4096];
    private byte[] minutea2 = new byte[4096];
    private byte[] datalain = new byte[4096];
    private FingerJNI fingerdrver;
    private Thread task;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private int photoLen;
    private int signatureLen;
    private int biodataLen;
    private int minutea1Len;
    private int minutea2Len;
    private int RETRIEVEPHOTO_LEN = 200;
    private int TOBEREAD_LEN = 200;
    private int PHOTOTOBEREAD_LEN = 240;
    private boolean DEBUG = false;
    private int validminuteakanan = 0;
    private int validminuteakiri = 0;
    private String Biodata;
    private String sidikkanan;
    private String sidikiri;
    private String sidikjari;
    private String tempatlahir;
    private String tanggallahir;
    private String nort;
    private String norw;
    private String NIK1;
    private String NAME1;
    private String TTL1;
    private String AGAMA1;
    private String ALAMAT1;
    private String RTRW1;
    private String KELURAHAN1;
    private String KECAMATAN1;
    private String KOTA1;
    private String PROPINSI1;
    private String JK1;
    private String GOL1;
    private String STATUS1;
    private String PEKERJAAN1;
    private String KEWARGANEGARAAN1;
    private String BERLAKUHINGGA1;
    private JSGFPLib sgfplib;
    public static byte[] grey_bmp_buffer = new byte[93238];
    public static byte[] feature = new byte[513];
    public static byte[] feature_templet = new byte[513];
    public static byte[] grey_bmpdata_buffer = new byte[122880];
    private boolean isworking = false;
    private int outoftime_count = 0;
    private int ignorelatestimage = 0;
    private String foto;
    private String tandatangan;
    private static final int VID = 6997;
    private static final int PID = 289;
    private FingerprintSensor fingerprintSensor = null;
    private TextView textView = null;
    private ImageView fpImageView = null;
    private boolean isRegister = false;
    private int uid11 = 1;
    private IntentFilter filter;
    private byte[][] regtemparray = new byte[3][2048];
    private int enrollidx = 0;
    private byte[] mLastRegTemplate = null;
    private boolean bstart = false;
    private int current_PID = 25771;
    private final boolean mUseUsbManager = true;
    private int ret;
    private int fvVid = 6997;
    private int fvPid = 289;
    DBHelper db;
    private Led led;
    private int match = 0;
    private String pccid;
    private String config;
    private String minute1buf;
    private String minutea2buf;
    private int statled;
    private int loopFP = 0;
    private int matchFP = 0;
    private static String TAG = "PermissionDemo";
    private static final int REQUEST_WRITE_STORAGE = 112;
    private String nameadmin;
    private String nikadmin;
    private Bitmap fpImage;
    private boolean bypass = false;
    private byte[] minutealive;
    private int skorfp;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.android.example.USB_PERMISSION".equals(action)) {
                synchronized(this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra("device");
                    if (intent.getBooleanExtra("permission", false)) {
                        if (device != null) {
                            Log.d("TAG", "Vendor ID : " + device.getVendorId() + "\n");
                            Log.d("TAG", "Product ID: " + device.getProductId() + "\n");
                        } else {
                            Log.e("TAG", "mUsbReceiver.onReceive() Device is null");
                        }
                    } else {
                        Log.e("TAG", "mUsbReceiver.onReceive() permission denied for device " + device);
                    }
                }
            }

        }
    };
    final FingerprintCaptureListener listener = new FingerprintCaptureListener() {
        public void captureOK(int captureMode, byte[] imageBuffer, int[] imageAttributes, byte[] templateBuffer) {
            int[] attributes = imageAttributes;
            byte[] imgBuffer = imageBuffer;
            byte[] tmpBuffer = templateBuffer;
            final int[] score = new int[1];
            readcard.this.fpImage = ToolUtils.renderCroppedGreyScaleBitmap(imgBuffer, attributes[0], attributes[1]);
            readcard.this.loopFP++;
            Log.d("Card Reader", "loop:" + readcard.this.loopFP);
            int retx;
            byte ret;
            if (readcard.this.minutea1Len > 64) {
                retx = FingerprintService.verify(tmpBuffer, readcard.this.minutea1);
                Log.d("Tag", "Skor:" + retx);
                readcard.this.skorfp = retx;
                if (retx > 30) {
                    Log.d("Card Reader", "Matched");
                    readcard.this.loopFP = 10;
                    readcard.this.matchFP = 2;
                    readcard.this.isworking = false;
                    readcard.this.sidikjari = readcard.this.sidikkanan;
                    ret = 2;
                    readcard.this.appendLog("Score Match " + ret);
                    readcard.this.alert();
                    readcard.this.match = 2;
                    return;
                }
            }

            if (readcard.this.minutea2Len > 64) {
                retx = FingerprintService.verify(tmpBuffer, readcard.this.minutea2);
                readcard.this.skorfp = retx;
                Log.d("Tag", "Skor:" + retx);
                if (retx > 30) {
                    Log.d("Card Reader", "Matched");
                    readcard.this.isworking = false;
                    readcard.this.sidikjari = readcard.this.sidikiri;
                    ret = 2;
                    readcard.this.alert();
                    readcard.this.appendLog("Score Match " + ret);
                    readcard.this.match = 2;
                    readcard.this.loopFP = 10;
                    readcard.this.matchFP = 2;
                    return;
                }

                readcard.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(readcard.this, "Sidik Jari Tidak Cocok, Silahkan Ulangi Lagi", Toast.LENGTH_SHORT).show();
                        readcard.this.alert();
                        readcard.this.appendLog("Score " + score[0]);
                    }
                });
            }

        }

        public void captureError(FingerprintSensorException e) {
            final FingerprintSensorException exp = e;
            readcard.this.runOnUiThread(new Runnable() {
                public void run() {
                    exp.printStackTrace();
                }
            });
        }
    };

    public readcard() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isConnected = this.mainApplication.isDeviceManagerConnetcted();
        Log.d("Card Reader", "bool:" + Boolean.toString(isConnected));
        this.mdevicetype = 3;
        this.mTestDeviceType = 0;
        this.statled = 9;

        try {
            this.db = new DBHelper(this);
            this.db.openDB();
            List<String> sam = this.db.readSAM();
            this.pccid = (String)sam.get(0);
            this.config = (String)sam.get(1);
            System.out.println("PCID" + this.pccid);
        } catch (Exception var6) {
            Exception ex = var6;
            ex.printStackTrace();
        }

        int nret = 0;

        try {
            this.init(isConnected);
            this.startFingerprintSensor();
            this.enrollidx = 0;
            int[] limit = new int[1];
            if (0 != FingerprintService.init(limit)) {
                Log.d("Card Reader", "init fpengine fail");
                return;
            }
        } catch (Exception var5) {
            Exception ex = var5;
            ex.printStackTrace();
        }

    }

    public int ledoff() {
        return this.statled = this.led.setLedSwitch(-1, 0);
    }

    private void initusbdevice() {
        try {
            UsbManager usbManager = (UsbManager)this.getSystemService(Context.USB_SERVICE);
            Intent intent = new Intent("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            intent.addCategory("android.hardware.usb.action.USB_DEVICE_DETACHED");
            Map<String, UsbDevice> usbDeviceList = usbManager.getDeviceList();
            LogHelper.i("init usb devices,device size = " + usbDeviceList.size());
            if (null != usbDeviceList && usbDeviceList.size() > 0) {
                Iterator var4 = usbDeviceList.values().iterator();

                while(var4.hasNext()) {
                    UsbDevice device = (UsbDevice)var4.next();
                    LogHelper.d("request Pression vid=" + device.getVendorId() + ",pid =" + device.getProductId());
                    if (this.fvVid == device.getVendorId() && this.fvPid == device.getProductId() && !usbManager.hasPermission(device)) {
                        PendingIntent mIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                        usbManager.requestPermission(device, mIntent);
                        break;
                    }
                }
            }
        } catch (Exception var7) {
            Exception ex = var7;
            ex.printStackTrace();
        }

    }

    private void startFingerprintSensor() {
        LogHelper.setLevel(7);
        Map fingerprintParams = new HashMap();
        fingerprintParams.put("param.key.vid", 6997);
        fingerprintParams.put("param.key.pid", 289);
        this.fingerprintSensor = FingerprintFactory.createFingerprintSensor(this, TransportType.USB, fingerprintParams);
    }

    protected void onDestroy() {
        int nStatus = 1;
        if (this.mContactlessCardReader != null && this.mContactlessCardReader.getStatus() != 242) {
            this.mContactlessCardReader.close();
        } else if (this.mSmartCardReader != null && this.mSmartCardReader.getStatus() != 242) {
            this.mSmartCardReader.close();
        }

        this.led.close();
        FingerprintFactory.destroy(this.fingerprintSensor);
        this.db.close();
        super.onDestroy();
    }

    public void onServiceConnected() {
        super.onServiceConnected();
        this.init(true);
    }

    public void onServiceDisconnected() {
        super.onServiceDisconnected();
        this.init(false);
    }

    private void init(boolean isConnected) {
        if (isConnected) {
            if (this.mTestDeviceType == 0) {
                this.mContactlessCardReader = this.mainApplication.getContactlessCardReader();
            }

            if (this.mdevicetype == 3) {
                this.mSmartCardReader = this.mainApplication.getSmartCardReader();
            }

            this.led = this.mainApplication.getLedWrapperImpl();
        } else {
            this.mContactlessCardReader = null;
            this.mSmartCardReader = null;
        }

    }

    public int initCR() {
        this.statled = this.led.setLedSwitch(-1, 0);
        this.statled = this.led.setLedSwitch(1, 1);
        Log.d("Card Reader", "stat led " + this.statled);
        int nret = this.mContactlessCardReader.open();
        if (nret == 0) {
            Log.d("Card Reader", "nfc open");
            if (this.mdevicetype == 3) {
                nret = this.mSmartCardReader.open(1, new SmartCardReader.SCReaderListener() {
                    public void notify(int i, int i1) {
                        if (i1 == 2) {
                            Log.d("Card Reader", "SAM not Detected ");
                        }

                    }
                });
            }

            if (nret >= 0) {
                Log.d("Card Reader", "SAM Open");
                byte[] data = new byte[256];
                nret = this.mSmartCardReader.powerOn(1, data);
                Log.d("Card Reader", "Power On ATR:" + (nret > 0 ? StringUtility.ByteArrayToString(data, nret) : ""));
                return 1;
            } else {
                Log.d("Card Reader", "SAM not Open");
                return -1;
            }
        } else {
            return -1;
        }
    }

    private int TransmitCR(byte[] pbSendBuffer, int dwSendLength) {
        int dwRecvLength = 0;

        try {
            byte[] result = new byte[256];

            try {
                int retByte = this.mContactlessCardReader.transmit(pbSendBuffer, result);
                dwRecvLength = retByte;
                System.arraycopy(result, 0, this.packetBuffer, 0, dwRecvLength);
            } catch (Exception var6) {
                Exception e = var6;
                Log.d("Card Reader", "Check NFC timeout...");
                e.printStackTrace();
                return -1;
            }

            if (this.packetBuffer[dwRecvLength - 2] == -112 && this.packetBuffer[dwRecvLength - 1] == 0) {
                return dwRecvLength;
            } else {
                Log.d("Card Reader", "Wrong return code : " + this.packetBuffer[dwRecvLength - 2] + " " + this.packetBuffer[dwRecvLength - 1]);
                this.appendLog("Wrong return code : " + this.packetBuffer[dwRecvLength - 2] + " " + this.packetBuffer[dwRecvLength - 1]);
                return -1;
            }
        } catch (Exception var7) {
            Exception ex = var7;
            ex.printStackTrace();
            return -1;
        }
    }

    private int TransmitCRFast(byte[] pbSendBuffer, int dwSendLength) {
        int dwRecvLength = 0;

        try {
            byte[] result = new byte[256];

            try {
                int retByte = this.mContactlessCardReader.transmit(-2094853629, pbSendBuffer, result);
                dwRecvLength = retByte;
                System.arraycopy(result, 0, this.packetBuffer, 0, dwRecvLength);
            } catch (Exception var6) {
                Exception e = var6;
                e.printStackTrace();
                return -1;
            }

            if (this.packetBuffer[dwRecvLength - 2] == -112 && this.packetBuffer[dwRecvLength - 1] == 0) {
                if (this.packetBuffer[0] == 107 && this.packetBuffer[1] == 0) {
                    Log.d("Card Reader", "Wrong return code : " + this.packetBuffer[0] + " " + this.packetBuffer[1]);
                    this.appendLog("Wrong return code : " + this.packetBuffer[0] + " " + this.packetBuffer[1]);
                    return -1;
                } else {
                    return dwRecvLength;
                }
            } else {
                Log.d("Card Reader", "Wrong return code : " + this.packetBuffer[dwRecvLength - 2] + " " + this.packetBuffer[dwRecvLength - 1]);
                this.appendLog("Wrong return code : " + this.packetBuffer[dwRecvLength - 2] + " " + this.packetBuffer[dwRecvLength - 1]);
                return -1;
            }
        } catch (Exception var7) {
            Exception ex = var7;
            ex.printStackTrace();
            return -1;
        }
    }

    private int samencript(byte[] apdu, int len) {
        byte[] protocol = new byte[]{0, -13, 0, 0, (byte)len};
        int ptr = 0;

        int i;
        for(i = 0; i < 5; ++i) {
            this.secureprotocolBuffer[ptr++] = protocol[i];
        }

        for(i = 0; i < len; ++i) {
            this.secureprotocolBuffer[ptr++] = apdu[i];
        }

        return ptr;
    }

    private int samdecript(byte[] apdu, int len) {
        byte[] protocol = new byte[]{0, -12, 0, 0, (byte)len};
        int ptr = 0;

        int i;
        for(i = 0; i < 5; ++i) {
            this.secureprotocolBuffer[ptr++] = protocol[i];
        }

        for(i = 0; i < len; ++i) {
            this.secureprotocolBuffer[ptr++] = apdu[i];
        }

        return ptr;
    }

    private int secureapdu(byte[] apdu, int len) {
        int l = this.samencript(apdu, len);
        byte[] command = new byte[l];
        int ptr = 0;

        int ret;
        for(ret = 0; ret < l; ++ret) {
            command[ptr++] = this.secureprotocolBuffer[ret];
        }

        ret = this.TransmitCRFast(command, l);
        return ret == -1 ? -1 : 0;
    }

    private int unsecureapdu(byte[] apdu, int len) {
        int ret = this.TransmitSAM(apdu, len);
        if (ret == -1) {
            return -1;
        } else {
            len = ret - 2;
            byte[] p = new byte[len];
            System.arraycopy(this.packetBuffer, 0, p, 0, p.length);
            ret = this.TransmitCR(this.packetBuffer, len);
            if (ret == -1) {
                return -1;
            } else {
                len = ret - 2;
                int l = this.samdecript(this.packetBuffer, len);
                ret = this.TransmitSAM(this.secureprotocolBuffer, l);
                return ret == -1 ? -1 : 0;
            }
        }
    }

    private int readblocksecure(int offset, int len) {
        try {
            byte highByte = (byte)((offset & '\uff00') >> 8);
            byte lowByte = (byte)(offset & 255);
            byte[] apdu = new byte[]{0, -80, highByte, lowByte, (byte)len};
            int l = this.samencript(apdu, apdu.length);
            int ptr = 0;
            byte[] command = new byte[l];

            int ret;
            for(ret = 0; ret < l; ++ret) {
                command[ptr++] = this.secureprotocolBuffer[ret];
            }

            ret = this.TransmitCRFast(command, l);
            return ret == -1 ? -1 : 0;
        } catch (Exception var10) {
            Exception ex = var10;
            ex.printStackTrace();
            return -1;
        }
    }

    private int TransmitSAM(byte[] pbSendBuffer, int dwSendLength) {
        int dwRecvLength = 0;

        try {
            byte[] apdu = new byte[dwSendLength];
            System.arraycopy(pbSendBuffer, 0, apdu, 0, apdu.length);
            byte[] retByte = new byte[256];
            int ret = this.mSmartCardReader.transmit(1, apdu, retByte);
            System.arraycopy(retByte, 0, this.packetBuffer, 0, ret);
            dwRecvLength = ret;
            if (this.packetBuffer[dwRecvLength - 2] == -112 && this.packetBuffer[dwRecvLength - 1] == 0) {
                return dwRecvLength;
            } else {
                Log.d("Card Reader", "Wrong return code : " + this.packetBuffer[dwRecvLength - 2] + " " + this.packetBuffer[dwRecvLength - 1]);
                this.appendLog("Wrong return code : " + this.packetBuffer[dwRecvLength - 2] + " " + this.packetBuffer[dwRecvLength - 1]);
                return -1;
            }
        } catch (Exception var7) {
            Exception ex = var7;
            this.appendLog(String.valueOf(ex));
            return -1;
        }
    }

    public void closesmartcard() {
        this.mContactlessCardReader.close();
        this.mSmartCardReader.close();
    }

    public int waitcard() {
        int result = this.mContactlessCardReader.waitForCard(Constants.WAIT_INFINITE);
        if (result == 0) {
            byte[] data = new byte[256];
            result = this.mContactlessCardReader.powerOn(data);
            Log.d("Card Reader", "tag" + result);
            int cardType = this.mContactlessCardReader.getCardType();
            ContactlessCardReader.CardInfo cardInfo = this.mContactlessCardReader.getCardInfo();
            byte[] tempID = cardInfo.uid;
            System.arraycopy(tempID, 3, this.uidCard, 1, 7);
            this.uidCard[0] = -128;
            Log.d("Card Reader", Hex.bytesToHexString(this.uidCard));
            this.statled = this.led.setLedSwitch(2, 1);
            this.alert();
            this.appendLog("Kartu Ditemukan");
        } else {
            if (result != -106) {
                if (result == -109) {
                    Log.d("Card Reader", "Device Force Close");
                    this.appendLog("Device Force Close");
                    return -1;
                }

                Log.d("Card Reader", "Read Card Error");
                this.appendLog("Read Card Error");
                return -1;
            }

            Log.d("Card Reader", "Time Out");
            this.appendLog("Time Out");
        }

        return 1;
    }

    public String UID() {
        return Hex.bytesToHexString(this.uidCard);
    }

    public int selectMF() {
        try {
            byte[] apdu = new byte[]{0, -92, 0, 0, 2, 127, 10};
            int i = this.TransmitCR(apdu, apdu.length);
            this.appendLog("Select MF");
            if (i == -1) {
                this.appendLog("Select MF Failed");
                return -1;
            }
        } catch (Exception var3) {
            Exception e = var3;
            e.printStackTrace();
        }

        return 1;
    }

    public int readPhoto() {
        Log.d("Card Reader", "Read Photograph");
        this.appendLog("Read Photo");
        int offset = 0;
        byte length = 0;
        int toberead = this.PHOTOTOBEREAD_LEN;
        byte[] select_apdu = new byte[]{0, -92, 0, 0, 2, 111, -14};
        byte[] read_block_apdu = new byte[]{0, -80, (byte)((offset & '\uff00') >> 8), (byte)(offset & 255), length};
        int ret = this.TransmitCR(select_apdu, select_apdu.length);
        if (ret == -1) {
            this.appendLog("Read Photo Failed");
            return -1;
        } else {
            toberead = this.PHOTOTOBEREAD_LEN;
            read_block_apdu[2] = 0;
            read_block_apdu[3] = 0;
            read_block_apdu[4] = (byte)toberead;
            ret = this.TransmitCR(read_block_apdu, read_block_apdu.length);
            if (ret == -1) {
                this.appendLog("Read Photo Failed");
                return -1;
            } else {
                long bit = (long)(this.packetBuffer[0] & 255);
                long bit2 = (long)(this.packetBuffer[1] & 255);
                int photolen = (int)(bit * 256L + bit2);
                int ptr = 0;

                int i;
                for(i = 0; i < toberead; ++i) {
                    this.photo[ptr++] = this.packetBuffer[i];
                }

                for(offset = toberead; offset < photolen + 2; this.photoLen = offset) {
                    if (photolen - offset + 2 > this.PHOTOTOBEREAD_LEN) {
                        toberead = this.PHOTOTOBEREAD_LEN;
                    } else {
                        toberead = photolen - offset + 2;
                    }

                    read_block_apdu[2] = (byte)((offset & '\uff00') >> 8);
                    read_block_apdu[3] = (byte)(offset & 255);
                    read_block_apdu[4] = (byte)toberead;
                    ret = this.TransmitCR(read_block_apdu, read_block_apdu.length);
                    if (ret == -1) {
                        Log.d("Card Reader", "block not complete");
                        return -1;
                    }

                    offset += toberead;

                    for(i = 0; i < toberead; ++i) {
                        this.photo[ptr++] = this.packetBuffer[i];
                    }
                }

                this.tmpphoto = new byte[offset - 2];
                System.arraycopy(this.photo, 2, this.tmpphoto, 0, offset - 2);
                Log.d("Card Reader", "PHOTO : " + Hex.bytesToHexString(this.tmpphoto));
                this.foto = Base64.encodeToString(this.tmpphoto, 2);
                return offset;
            }
        }
    }

    public int open_sam() {
        Log.d("Card Reader", "Open SAM");
        this.appendLog("OPEN SAM");
        Log.d("Card Reader", "PCID: " + this.pccid + " Config: " + this.config);
        byte[] pcid = Base64.decode(this.pccid, 0);
        byte[] configfile = Base64.decode(this.config, 0);
        byte[] data = new byte[32];
        byte[] sam = new byte[]{0, -16, 0, 0, 32};
        int i = 0;

        int j;
        for(j = 0; j < 16; ++j) {
            data[i++] = (byte)(pcid[j] ^ configfile[j]);
        }

        for(j = 0; j < 16; ++j) {
            data[i++] = (byte)(pcid[j] ^ configfile[j + 16]);
        }

        byte[] apdu = new byte[sam.length + data.length];
        System.arraycopy(sam, 0, apdu, 0, sam.length);
        System.arraycopy(data, 0, apdu, sam.length, data.length);
        int ret = this.TransmitSAM(apdu, apdu.length);
        if (ret == -1) {
            this.appendLog("OPEN SAM Failed");
            return -1;
        } else if (this.Reset_SAM() == -1) {
            this.appendLog("RESET SAM Failed");
            return -1;
        } else {
            return 1;
        }
    }

    private int Reset_SAM() {
        Log.d("Card Reader", "Reset SAM");
        byte[] protocol = new byte[]{0, -1, 0, 0, 0};
        int ret = this.TransmitSAM(protocol, protocol.length);
        return ret == -1 ? -1 : 0;
    }

    private int readblock(int offset, int len) {
        byte[] read_block_apdu = new byte[]{0, -80, (byte)((offset & '\uff00') >> 8), (byte)(offset & 255), (byte)len};
        int ret = this.TransmitCR(read_block_apdu, read_block_apdu.length);
        return ret == -1 ? -1 : 0;
    }

    private int read_card_control() {
        try {
            Log.d("Card Reader", "Read Card Control");
            byte[] select_cc_apdu = new byte[]{0, -92, 0, 0, 2, 111, -16};
            int ret = this.TransmitCR(select_cc_apdu, select_cc_apdu.length);
            if (ret == -1) {
                return -1;
            } else {
                ret = this.readblock(0, 54);
                if (ret == -1) {
                    return -1;
                } else {
                    if (this.packetBuffer[1] == 51) {
                        this.card_active = 1;
                    } else {
                        this.card_active = 0;
                    }

                    this.cardcontrol[0] = this.packetBuffer[0];
                    this.cardcontrol[1] = this.packetBuffer[1];

                    for(int i = 0; i < this.cardcontrol[1]; ++i) {
                        this.cardcontrol[i + 2] = this.packetBuffer[2 + i];
                    }

                    Log.d("Card Reader", "Card Control: " + Hex.bytesToHexString(this.cardcontrol, this.cardcontrol[1]));
                    return this.card_active;
                }
            }
        } catch (Exception var4) {
            Exception ex = var4;
            ex.printStackTrace();
            return -1;
        }
    }

    private int getChallenge() {
        try {
            Log.d("Card Reader", "Get Challange");
            byte[] getChallenge_apdu = new byte[]{0, -124, 0, 0, 8};
            int ret = this.TransmitCR(getChallenge_apdu, getChallenge_apdu.length);
            if (ret == -1) {
                return -1;
            } else {
                for(int i = 0; i < 8; ++i) {
                    this.challenge[i] = this.packetBuffer[i];
                }

                Log.d("Card Reader", "Challange: " + Hex.bytesToHexString(this.challenge, 8));
                return 0;
            }
        } catch (Exception var4) {
            Exception ex = var4;
            ex.printStackTrace();
            return -1;
        }
    }

    private int calcChallenge() {
        try {
            Log.d("Card Reader", "Calc Challange");
            byte cc_len = this.cardcontrol[1];
            int ch_len = (byte)(16 + cc_len);
            byte[] protocol = new byte[5 + ch_len + 1];
            byte[] apdu = new byte[]{0, -15, 0, this.card_active, (byte)ch_len};
            int ptr = 0;

            int ret;
            for(ret = 0; ret < 5; ++ret) {
                protocol[ptr++] = apdu[ret];
            }

            for(ret = 0; ret < cc_len; ++ret) {
                protocol[ptr++] = this.cardcontrol[ret + 2];
            }

            for(ret = 0; ret < 8; ++ret) {
                protocol[ptr++] = this.uidCard[ret];
            }

            for(ret = 0; ret < 8; ++ret) {
                protocol[ptr++] = this.challenge[ret];
            }

            ret = this.TransmitSAM(protocol, ptr);
            if (ret == -1) {
                return -1;
            }

            for(int i = 0; i < 41; ++i) {
                this.calcChallengeBuf[i] = this.packetBuffer[i];
            }

            Log.d("Card Reader", "Calc Challange: " + Hex.bytesToHexString(this.calcChallengeBuf, 41));
        } catch (Exception var8) {
            Exception ex = var8;
            ex.printStackTrace();
        }

        return 0;
    }

    private int Authenticate() {
        try {
            Log.d("Card Reader", "Authenticate");
            byte[] external_auth = new byte[]{0, -126, 0, 0, 40};
            byte[] internal_auth = new byte[]{0, -14, 0, 0, 40};
            byte[] protocol = new byte[46];
            int ptr = 0;

            int ret;
            for(ret = 0; ret < 5; ++ret) {
                protocol[ptr++] = external_auth[ret];
            }

            for(ret = 0; ret < 41; ++ret) {
                protocol[ptr++] = this.calcChallengeBuf[ret];
            }

            ret = this.TransmitCR(protocol, ptr);
            if (ret == -1) {
                return -1;
            } else {
                ptr = 0;

                int j;
                for(j = 0; j < 5; ++j) {
                    protocol[ptr++] = internal_auth[j];
                }

                for(j = 0; j < 40; ++j) {
                    protocol[ptr++] = this.packetBuffer[j];
                }

                ret = this.TransmitSAM(protocol, protocol.length);
                return ret == -1 ? -1 : 0;
            }
        } catch (Exception var7) {
            Exception ex = var7;
            ex.printStackTrace();
            return -1;
        }
    }

    private int readDigitalSignature() {
        int len = 0;

        try {
            Log.d("Card Reader", "Read Digital Signature");
            byte[] ef = new byte[]{0, -92, 0, 0, 2, 111, -10};
            int ret = this.secureapdu(ef, ef.length);
            if (ret == -1) {
                return -1;
            } else {
                ret = this.readblocksecure(0, 80);
                if (ret == -1) {
                    return -1;
                } else {
                    for(int i = 0; i < 80; ++i) {
                        this.digitalSignature[i] = this.packetBuffer[i];
                    }

                    len = this.digitalSignature[0] * 256 + this.digitalSignature[1];
                    Log.d("Card Reader", "DIGITAL SIGNATURE " + len + " : " + Hex.bytesToHexString(this.digitalSignature, 80));
                    return len;
                }
            }
        } catch (Exception var5) {
            Exception ex = var5;
            ex.printStackTrace();
            return -1;
        }
    }

    private int StartDSAutoVerification() {
        try {
            Log.d("Card Reader", "Start DS AutoVerif");
            byte[] protocol = new byte[]{0, -6, 0, 0, 0};
            int ret = this.TransmitSAM(protocol, protocol.length);
            return ret == -1 ? -1 : 0;
        } catch (Exception var3) {
            Exception ex = var3;
            ex.printStackTrace();
            return -1;
        }
    }

    private int retrieveDS() {
        try {
            Log.d("Card Reader", "Retrieve DS");
            byte[] protocol = new byte[]{0, -6, 6, 0, 80};
            int ptr = 0;

            int ret;
            for(ret = 0; ret < 5; ++ret) {
                this.secureprotocolBuffer[ptr++] = protocol[ret];
            }

            for(ret = 0; ret < 80; ++ret) {
                this.secureprotocolBuffer[ptr++] = this.digitalSignature[ret];
            }

            ret = this.TransmitSAM(this.secureprotocolBuffer, ptr);
            return ret == -1 ? -1 : 0;
        } catch (Exception var4) {
            Exception ex = var4;
            ex.printStackTrace();
            return -1;
        }
    }

    public int StopDSAutoVerification() {
        Log.d("Card Reader", "Stop DS Auto Verif");
        this.appendLog("Stop DS Auto Verif");
        byte[] protocol = new byte[]{0, -6, 2, 0, 0};
        int ret = this.TransmitSAM(protocol, protocol.length);
        if (ret == -1) {
            this.appendLog("Stop DS Auto Verif Failed");
            return -1;
        } else {
            return 0;
        }
    }

    public int VerifyDS() {
        if (this.DEBUG) {
            Log.d("Card Reader", "Verify DS");
        }

        this.appendLog("Verify DS");
        if (this.card_active != 0) {
            if (this.ActiveCard() != 0) {
                Log.d("Card Reader", "Aktifasi Kartu Gagal");
            } else {
                Log.d("Card Reader", "Aktifasi Kartu Berhasil");
            }
        } else {
            Log.d("Card Reader", "Kartu Sudah Aktif");
        }

        byte[] protocol = new byte[]{0, -6, 4, 0, 0};
        int ret = this.TransmitSAM(protocol, protocol.length);
        return ret == -1 ? 1 : 1;
    }

    public int Autentikasi() {
        try {
            this.appendLog("Autentikasi");
            int ret = this.read_card_control();
            if (ret == -1) {
                Log.d("Card Reader", "Read Card Control Failed");
                this.appendLog("Read Card Control Failed");
                return -1;
            } else {
                ret = this.getChallenge();
                if (ret == -1) {
                    Log.d("Card Reader", "Get Challenge Failed");
                    this.appendLog("Get Challenge Failed");
                    return -1;
                } else {
                    ret = this.calcChallenge();
                    if (ret == -1) {
                        this.appendLog("Calc Challenge Failed");
                        Log.d("Card Reader", "Calc Challenge Failed");
                        return -1;
                    } else {
                        ret = this.Authenticate();
                        if (ret == -1) {
                            this.appendLog("Authenticate Failed");
                            Log.d("Card Reader", "Authenticate Failed");
                            return -1;
                        } else {
                            ret = this.readDigitalSignature();
                            if (ret == -1) {
                                Log.d("Card Reader", "read DS Failed");
                                this.appendLog("read DS Failed");
                                return -1;
                            } else {
                                ret = this.StartDSAutoVerification();
                                if (ret == -1) {
                                    Log.d("Card Reader", "start DS Failed");
                                    this.appendLog("start DS Failed");
                                    return -1;
                                } else {
                                    ret = this.retrieveDS();
                                    if (ret == -1) {
                                        Log.d("Card Reader", "retrieve DS Failed");
                                        this.appendLog("retrieve DS Failed");
                                        return -1;
                                    } else {
                                        return 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception var2) {
            Exception ex = var2;
            ex.printStackTrace();
            return -1;
        }
    }

    private int StartAutomaticDechipering() {
        Log.d("Card Reader", "Start Auto Dechip");
        byte[] protocol = new byte[]{0, -6, 5, 0, 0};
        int ret = this.TransmitSAM(protocol, protocol.length);
        return ret == -1 ? -1 : 0;
    }

    public int ReadBiodata() {
        int offset = 0;

        try {
            Log.d("Card Reader", "Read Biodata");
            this.appendLog("Read Biodata");
            byte[] ef = new byte[]{0, -92, 0, 0, 2, 111, -15};
            int ret = this.secureapdu(ef, ef.length);
            if (ret == -1) {
                this.appendLog("Read Biodata Failed");
                return -1;
            } else {
                ret = this.readblocksecure(0, this.TOBEREAD_LEN);
                if (ret == -1) {
                    this.appendLog("Read Biodata Failed");
                    return -1;
                } else {
                    int ptr = 0;

                    int i;
                    for(i = 0; i < this.TOBEREAD_LEN; ++i) {
                        this.datalain[ptr++] = this.packetBuffer[i];
                    }

                    offset = this.TOBEREAD_LEN;
                    long bit = (long)(this.packetBuffer[0] & 255);
                    long bit2 = (long)(this.packetBuffer[1] & 255);
                    int len = (int)(bit * 256L + bit2);

                    for(int toberead = this.TOBEREAD_LEN; offset < len + 2; offset += toberead) {
                        if (len - offset + 2 > this.TOBEREAD_LEN) {
                            toberead = this.TOBEREAD_LEN;
                        } else {
                            toberead = len - offset + 2;
                        }

                        ret = this.readblocksecure(offset, toberead);
                        if (ret == -1) {
                            Log.d("Card Reader", "block not complete");
                            break;
                        }

                        for(i = 0; i < toberead; ++i) {
                            this.datalain[ptr++] = this.packetBuffer[i];
                        }
                    }

                    this.biodata = new byte[offset - 2];
                    System.arraycopy(this.datalain, 2, this.biodata, 0, offset - 2);
                    this.biodataLen = offset;

                    try {
                        this.Biodata = new String(this.biodata, "UTF-8");
                    } catch (UnsupportedEncodingException var14) {
                        UnsupportedEncodingException e = var14;
                        e.printStackTrace();
                    }

                    try {
                        this.CSV(this.Biodata);
                        return offset;
                    } catch (Exception var13) {
                        var13.printStackTrace();
                        return -1;
                    }
                }
            }
        } catch (Exception var15) {
            Exception ex = var15;
            ex.printStackTrace();
            return -1;
        }
    }

    public int readSignature() {
        int offset = 0;

        try {
            if (this.DEBUG) {
                Log.d("Card Reader", "Read Signature");
            }

            if (this.ret == -1) {
                this.appendLog("Retrieve Photo Failed");
                return -1;
            } else {
                this.ret = this.StartAutomaticDechipering();
                if (this.ret == -1) {
                    this.appendLog("Start Dechipering Failed");
                    return -1;
                } else {
                    byte[] ef = new byte[]{0, -92, 0, 0, 2, 111, -13};
                    this.ret = this.secureapdu(ef, ef.length);
                    if (this.ret == -1) {
                        this.appendLog("Read Signature Failed");
                        return -1;
                    } else {
                        this.ret = this.readblocksecure(0, this.TOBEREAD_LEN);
                        if (this.ret == -1) {
                            this.appendLog("Read Signature Failed");
                            return -1;
                        } else {
                            int ptr = 0;

                            int i;
                            for(i = 0; i < this.TOBEREAD_LEN; ++i) {
                                this.datalain[ptr++] = this.packetBuffer[i];
                            }

                            offset = this.TOBEREAD_LEN;
                            long bit = (long)(this.packetBuffer[0] & 255);
                            long bit2 = (long)(this.packetBuffer[1] & 255);
                            int len = (int)(bit * 256L + bit2);

                            for(int toberead = this.TOBEREAD_LEN; offset < len + 2; offset += toberead) {
                                if (len - offset + 2 > this.TOBEREAD_LEN) {
                                    toberead = this.TOBEREAD_LEN;
                                } else {
                                    toberead = ((len - offset + 2) / 8 + 1) * 8;
                                }

                                this.ret = this.readblocksecure(offset, toberead);
                                if (this.ret == -1) {
                                    Log.d("Card Reader", "block not complete");
                                    break;
                                }

                                for(i = 0; i < toberead; ++i) {
                                    this.datalain[ptr++] = this.packetBuffer[i];
                                }
                            }

                            this.signature = new byte[offset - 2];
                            System.arraycopy(this.datalain, 2, this.signature, 0, offset - 2);
                            this.signatureLen = offset;
                            Log.d("Card Reader", "SIGNATURE : " + Hex.bytesToHexString(this.signature, this.signatureLen));

                            try {
                                byte[] TTD = this.deCompressSign(this.signature, 168, 44);
                                this.tandatangan = Base64.encodeToString(TTD, 2);
                            } catch (Exception var12) {
                                Exception e = var12;
                                e.printStackTrace();
                            }

                            return offset;
                        }
                    }
                }
            }
        } catch (Exception var13) {
            Exception ex = var13;
            ex.printStackTrace();
            return -1;
        }
    }

    public int readMinutae1() {
        int offset = 0;

        try {
            Log.d("Card Reader", "Read Minutea1");
            byte[] ef = new byte[]{0, -92, 0, 0, 2, 111, -12};
            int ret = this.secureapdu(ef, ef.length);
            if (ret == -1) {
                this.appendLog("Read Minutea 1 Failed");
                return -1;
            } else {
                ret = this.readblocksecure(0, this.TOBEREAD_LEN);
                if (ret == -1) {
                    this.appendLog("Read Minutea 1 Failed");
                    return -1;
                } else {
                    int ptr = 0;

                    int i;
                    for(i = 0; i < this.TOBEREAD_LEN; ++i) {
                        this.datalain[ptr++] = this.packetBuffer[i];
                    }

                    offset = this.TOBEREAD_LEN;
                    long bit = (long)(this.packetBuffer[0] & 255);
                    long bit2 = (long)(this.packetBuffer[1] & 255);
                    int len = (int)(bit * 256L + bit2);

                    for(int toberead = this.TOBEREAD_LEN; offset < len + 2; offset += toberead) {
                        if (len - offset + 2 > this.TOBEREAD_LEN) {
                            toberead = this.TOBEREAD_LEN;
                        } else {
                            toberead = ((len - offset + 2) / 8 + 1) * 8;
                        }

                        ret = this.readblocksecure(offset, toberead);
                        if (ret == -1) {
                            Log.d("Card Reader", "block not complete");
                            break;
                        }

                        for(i = 0; i < toberead; ++i) {
                            this.datalain[ptr++] = this.packetBuffer[i];
                        }
                    }

                    this.minutea1 = new byte[offset - 2];
                    System.arraycopy(this.datalain, 2, this.minutea1, 0, offset - 2);
                    this.minutea1Len = offset;
                    this.validminuteakanan = 1;
                    return offset;
                }
            }
        } catch (Exception var12) {
            Exception ex = var12;
            ex.printStackTrace();
            return -1;
        }
    }

    public int readMinutae2() {
        int offset = 0;

        try {
            Log.d("Card Reader", "Read Minutea2");
            byte[] ef = new byte[]{0, -92, 0, 0, 2, 111, -11};
            int ret = this.secureapdu(ef, ef.length);
            if (ret == -1) {
                this.appendLog("Read Minutea 2 Failed");
                return -1;
            }

            ret = this.readblocksecure(0, this.TOBEREAD_LEN);
            if (ret == -1) {
                this.appendLog("Read Minutea 2 Failed");
                return -1;
            }

            int ptr = 0;

            int i;
            for(i = 0; i < this.TOBEREAD_LEN; ++i) {
                this.datalain[ptr++] = this.packetBuffer[i];
            }

            offset = this.TOBEREAD_LEN;
            long bit = (long)(this.packetBuffer[0] & 255);
            long bit2 = (long)(this.packetBuffer[1] & 255);
            int len = (int)(bit * 256L + bit2);
            int toberead = this.TOBEREAD_LEN;

            while(true) {
                if (offset < len + 2) {
                    if (len - offset + 2 > this.TOBEREAD_LEN) {
                        toberead = this.TOBEREAD_LEN;
                    } else {
                        toberead = ((len - offset + 2) / 8 + 1) * 8;
                    }

                    ret = this.readblocksecure(offset, toberead);
                    if (ret != -1) {
                        for(i = 0; i < toberead; ++i) {
                            this.datalain[ptr++] = this.packetBuffer[i];
                        }

                        offset += toberead;
                        continue;
                    }

                    Log.d("Card Reader", "block not complete");
                }

                this.minutea2 = new byte[offset - 2];
                System.arraycopy(this.datalain, 2, this.minutea2, 0, offset - 2);
                this.minutea2Len = offset;
                break;
            }
        } catch (Exception var12) {
            Exception ex = var12;
            ex.printStackTrace();
            return -1;
        }

        this.validminuteakiri = 1;
        return offset;
    }

    public int ActiveCard() {
        if (this.DEBUG) {
            Log.d("Card Reader", "Activation Card\n");
        }

        byte[] select_cc_apdu = new byte[]{0, -92, 0, 0, 2, 111, -16};
        byte[] encript_activ = new byte[]{0, -8, 0, 0, 0};
        int ret = this.secureapdu(select_cc_apdu, select_cc_apdu.length);
        if (ret == -1) {
            return -1;
        } else {
            ret = this.unsecureapdu(encript_activ, encript_activ.length);
            return ret == -1 ? -1 : 0;
        }
    }

    private int retrievePhoto() {
        try {
            if (this.DEBUG) {
                Log.d("Card Reader", "Retrieve Photo");
            }

            int photo_len = this.photo[0] * 256 + this.photo[1];
            int len_per_sent = this.RETRIEVEPHOTO_LEN;
            this.secureprotocolBuffer = new byte[4096];
            byte[] apdu = new byte[]{0, -6, 3, 0, (byte)len_per_sent};
            int offset = 0;
            int ptr = 0;

            int ret;
            do {
                if (offset >= photo_len + 2) {
                    return 0;
                }

                ptr = 0;

                int i;
                for(i = 0; i < 5; ++i) {
                    this.secureprotocolBuffer[ptr++] = apdu[i];
                }

                if (photo_len + 2 - offset > this.RETRIEVEPHOTO_LEN) {
                    len_per_sent = this.RETRIEVEPHOTO_LEN;
                } else {
                    len_per_sent = photo_len - offset + 2;
                }

                this.secureprotocolBuffer[4] = (byte)(len_per_sent & 255);

                try {
                    for(i = 0; i < len_per_sent; ++i) {
                        this.secureprotocolBuffer[ptr++] = this.photo[offset];
                        ++offset;
                    }
                } catch (Exception var8) {
                    Exception ex = var8;
                    ex.printStackTrace();
                }

                ret = this.TransmitSAM(this.secureprotocolBuffer, ptr);
            } while(ret != -1);

            return -1;
        } catch (Exception var9) {
            Exception ex = var9;
            ex.printStackTrace();
            return -1;
        }
    }

    private byte[] deCompressSign(byte[] signCompressedBytes, int outSignWidth, int outSignHeight) throws Exception {
        try {
            byte[] signDecompressedBytes = new byte[outSignWidth * outSignHeight];

            for(int h = 0; h < outSignHeight; ++h) {
                for(int w = 0; w < outSignWidth / 8; ++w) {
                    long num = (long)signCompressedBytes[h * (outSignWidth / 8) + w];
                    int ByteIndex = h * outSignWidth + w * 8;
                    long[] mirror = new long[8];

                    for(int i = 0; i < 8; ++i) {
                        mirror[7 - i] = num >> i & 1L;
                    }

                    for(byte s = 0; s < 8; ++s) {
                        if (mirror[s] == 1L) {
                            signDecompressedBytes[ByteIndex + s] = -1;
                        } else {
                            signDecompressedBytes[ByteIndex + s] = 0;
                        }
                    }
                }
            }

            return this.byteArrayToBitmap(signDecompressedBytes, outSignWidth, outSignHeight);
        } catch (Exception var12) {
            Exception ex = var12;
            ex.printStackTrace();
            return null;
        }
    }

    private byte[] byteArrayToBitmap(byte[] bytes, int width, int height) throws IOException {
        System.gc();
        MyBitmapFile myBitmap = new MyBitmapFile(width, height, bytes);
        Bitmap img = BitmapFactory.decodeByteArray(myBitmap.toBytes(), 0, myBitmap.toBytes().length);
        Bitmap cd = Bitmap.createBitmap(img);
        Bitmap mutableBitmap = cd.copy(Config.ARGB_8888, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mutableBitmap.compress(CompressFormat.JPEG, 0, bos);
        byte[] temp = bos.toByteArray();
        return temp;
    }

    private static String toBinary(byte num) {
        return String.format("%8s", Integer.toBinaryString(num & 255)).replace(' ', '0');
    }

    private byte[] readsecureblock(int offset, byte len) {
        byte[] res = new byte[256];

        try {
            int ret = 0;
            byte highByte = (byte)((offset & '\uff00') >> 8);
            byte lowByte = (byte)(offset & 255);
            byte[] apdu = new byte[]{0, -80, highByte, lowByte, len};
            int l = this.samencript(apdu, (byte)apdu.length);
            int ptr = 0;
            byte[] command = new byte[l];

            for(int i = 0; i < l; ++i) {
                command[ptr++] = this.secureprotocolBuffer[i];
            }

            res = new byte[256];
            ret = this.mContactlessCardReader.transmit(-2094853629, command, res);
            if (ret == -1) {
                return res;
            }

            this.secureprotocolBuffer = new byte[ret];
            System.arraycopy(res, 0, this.secureprotocolBuffer, 0, ret);
        } catch (Exception var12) {
            Exception e = var12;
            e.printStackTrace();
            return null;
        }

        return this.secureprotocolBuffer;
    }

    public JSONObject mnt() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("jarikanan", this.mntkanan);
            obj.put("jarikiri", this.mntkiri);
            obj.put("minuteakanan", Base64.encodeToString(this.minutea1, 0));
            obj.put("minuteakiri", Base64.encodeToString(this.minutea2, 0));
        } catch (JSONException var3) {
            JSONException e = var3;
            e.printStackTrace();
        }

        return obj;
    }

    private void CSV(final String buffer) {
        Thread t = new Thread() {
            public void run() {
                try {
                    CSVReader csvReader = new CSVReader(new StringReader(buffer));
                    String[] row = null;

                    while((row = csvReader.readNext()) != null) {
                        readcard.this.sidikkanan = row[17];
                        readcard.this.sidikiri = row[18];
                        readcard.this.mntkiri = readcard.this.sidikiri;
                        readcard.this.mntkanan = readcard.this.sidikkanan;
                        readcard.this.NIK1 = row[0];
                        readcard.this.ALAMAT1 = row[1];
                        readcard.this.nort = row[2];
                        readcard.this.norw = row[3];
                        readcard.this.RTRW1 = row[2] + "/" + row[3];
                        readcard.this.KOTA1 = "PROPINSI " + row[15];
                        readcard.this.KECAMATAN1 = row[5];
                        readcard.this.KELURAHAN1 = row[6];
                        readcard.this.TTL1 = row[4] + ", " + row[14];
                        readcard.this.tempatlahir = row[4];
                        readcard.this.tanggallahir = row[14];
                        readcard.this.JK1 = row[8];
                        readcard.this.GOL1 = row[9];
                        readcard.this.AGAMA1 = row[10];
                        readcard.this.STATUS1 = row[11];
                        readcard.this.PEKERJAAN1 = row[12];
                        readcard.this.NAME1 = row[13];
                        readcard.this.PROPINSI1 = row[7];
                        readcard.this.BERLAKUHINGGA1 = row[16];
                        readcard.this.KEWARGANEGARAAN1 = row[19];
                    }

                    if (readcard.this.sidikkanan.equals("1")) {
                        readcard.this.sidikkanan = "Jempol Kanan";
                    }

                    if (readcard.this.sidikkanan.equals("2")) {
                        readcard.this.sidikkanan = "Telunjuk Kanan";
                    }

                    if (readcard.this.sidikkanan.equals("3")) {
                        readcard.this.sidikkanan = "Tengah Kanan";
                    }

                    if (readcard.this.sidikkanan.equals("4")) {
                        readcard.this.sidikkanan = "Manis Kanan";
                    }

                    if (readcard.this.sidikkanan.equals("5")) {
                        readcard.this.sidikkanan = "Kelingking Kanan";
                    }

                    if (readcard.this.sidikiri.equals("6")) {
                        readcard.this.sidikiri = "Jempol Kiri";
                    }

                    if (readcard.this.sidikiri.equals("7")) {
                        readcard.this.sidikiri = "Telunjuk Kiri";
                    }

                    if (readcard.this.sidikiri.equals("8")) {
                        readcard.this.sidikiri = "Tengah Kiri";
                    }

                    if (readcard.this.sidikiri.equals("9")) {
                        readcard.this.sidikiri = "Manis Kiri";
                    }

                    if (readcard.this.sidikiri.equals("10")) {
                        readcard.this.sidikiri = "Kelingking Kiri";
                    }

                    Log.d("Card Reader", readcard.this.sidikkanan);
                } catch (IOException var3) {
                    IOException ex = var3;
                    ex.printStackTrace();
                }

            }
        };
        t.start();
    }

    public String getjari() {
        String ret = null;
        if (this.minutea1Len > 64 && this.minutea2Len > 64) {
            return "Taruh jari " + this.sidikkanan + " atau " + this.sidikiri + " Anda \n pada pemindai Sidik Jari";
        } else if (this.minutea1Len < 64 && this.minutea2Len > 64) {
            return "Taruh jari " + this.sidikiri + " Anda \n pada pemindai Sidik Jari";
        } else {
            return (String)(this.minutea1Len > 64 && this.minutea2Len < 64 ? "Taruh jari " + this.sidikkanan + " Anda \n pada pemindai Sidik Jari" : ret);
        }
    }

    public JSONObject getdataktp() {
        this.statled = this.led.setLedSwitch(-1, 1);
        if (this.bypass) {
            this.logdbadmin(this.nikadmin, this.nameadmin);
        } else {
            this.logdb();
        }

        JSONObject cred = new JSONObject();

        try {
            cred.put("type", "biodata");
            cred.put("nik", this.NIK1);
            cred.put("namaLengkap", this.NAME1);
            cred.put("jenisKelamin", this.JK1);
            cred.put("tempatLahir", this.tempatlahir);
            cred.put("tanggalLahir", this.tanggallahir);
            cred.put("agama", this.AGAMA1);
            cred.put("statusKawin", this.STATUS1);
            cred.put("jenisPekerjaan", this.PEKERJAAN1);
            cred.put("namaProvinsi", this.KOTA1);
            cred.put("namaKabupaten", this.PROPINSI1);
            cred.put("namaKecamatan", this.KECAMATAN1);
            cred.put("namaKelurahan", this.KELURAHAN1);
            cred.put("alamat", this.ALAMAT1);
            cred.put("nomorRt", this.nort);
            cred.put("nomorRw", this.norw);
            cred.put("berlakuHingga", this.BERLAKUHINGGA1);
            cred.put("golonganDarah", this.GOL1);
            cred.put("kewarganegaraan", "WNI");
            cred.put("foto", this.foto);
            cred.put("ttd", this.tandatangan);
            cred.put("fingerAuth", this.sidikjari);
            cred.put("minutea1", Base64.encodeToString(this.minutea1, 0));
            cred.put("minutea2", Base64.encodeToString(this.minutea2, 0));
        } catch (JSONException var3) {
            JSONException e = var3;
            e.printStackTrace();
        }

        return cred;
    }

    private void logdb() {
        this.task = new Thread() {
            public void run() {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(c.getTime());
                readcard.this.db.insert(strDate, readcard.this.NIK1, "0", "Sukses", "1", "pemilik", readcard.this.sidikjari, "", "", "1", "1");
            }
        };
        this.task.start();
    }

    private void logdbadmin(final String nameadmin, final String nikadmin) {
        this.task = new Thread() {
            public void run() {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(c.getTime());
                readcard.this.db.insert(strDate, readcard.this.NIK1, "0", "Sukses", "1", "admin", "", nameadmin, nikadmin, "1", "1");
            }
        };
        this.task.start();
    }

    private void fp1() throws FingerprintSensorException {
        try {
            this.statled = this.led.setLedSwitch(4, 1);
            this.alert();
            Log.i("Card Reader", "deal with the fingerprint data on this handler");
            this.isRegister = true;
            this.fingerprintSensor.startCapture(0);
            this.fingerprintSensor.setFingerprintCaptureMode(0, 1);
        } catch (Exception var2) {
            Exception ex = var2;
            ex.printStackTrace();
        }

    }

    private void appendLog(String text) {
        File logFile = new File("storage/emulated/0/ektpSystem.log");
        IOException e;
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException var10) {
                e = var10;
                e.printStackTrace();
            }
        }

        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            sdf1.format(c.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(c.getTime());
            buf.append(String.valueOf(strDate + " " + text + "\r\n"));
            buf.newLine();
            buf.close();
        } catch (IOException var9) {
            e = var9;
            e.printStackTrace();
        }

    }

    public int fp() {
        this.loopFP = 0;
        this.matchFP = -1;
        this.bypass = false;

        try {
            this.fingerprintSensor.open(0);
            this.fingerprintSensor.setTemplateFmt(0, 51);
            FingerprintService.setParameter(5010, 1);
            this.fingerprintSensor.wakeUp(0);
            this.fingerprintSensor.setFingerprintCaptureListener(0, this.listener);
            this.fp1();
        } catch (FingerprintSensorException var4) {
            FingerprintSensorException e = var4;
            e.printStackTrace();
        }

        Log.d("Card Reader", "Matching :" + this.match);
        if (this.match == 2) {
            return this.match;
        } else {
            int loopRemain = 0;

            while(this.loopFP < 10) {
                try {
                    Thread.sleep(100L);
                    ++loopRemain;
                    if (loopRemain > 150) {
                        break;
                    }
                } catch (InterruptedException var5) {
                    InterruptedException e = var5;
                    e.printStackTrace();
                }
            }

            try {
                this.fingerprintSensor.stopCapture(0);
                this.fingerprintSensor.close(0);
            } catch (FingerprintSensorException var3) {
                var3.printStackTrace();
            }

            if (this.matchFP == -1) {
                Log.d("Card Reader", "Sidik Jari Tidak Cocok");
                this.ret = -1;
                this.match = -1;
                return -1;
            } else {
                return this.matchFP;
            }
        }
    }

    private void writefile(String sidik) {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            sdf1.format(c.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String strDate = sdf.format(c.getTime());
            String path = "sdcard/fingerprint/" + this.NIK1 + "_" + sidik + "_" + strDate;
            File Directory = new File(path);
            Directory.mkdirs();
            FileOutputStream out = null;

            Exception photoStreamException;
            IOException e;
            try {
                out = new FileOutputStream(path + "/Probe.bmp");
                this.fpImage.compress(CompressFormat.JPEG, 50, out);
            } catch (Exception var24) {
                photoStreamException = var24;
                photoStreamException.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException var21) {
                    e = var21;
                    e.printStackTrace();
                }

            }

            photoStreamException = null;
            OutputStream photoStream = new FileOutputStream(path + "/Probe.iso");
            photoStream.write(this.minutealive);
            photoStream.close();
            photoStream = new FileOutputStream(path + "/Gallery_primaryFinger.iso");
            photoStream.write(this.minutea1);
            photoStream.close();
            photoStream = new FileOutputStream(path + "/Gallery_secondaryFinger.iso");
            photoStream.write(this.minutea2);
            photoStream.close();
            File logFile = new File(path + "/Scores.txt");
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException var23) {
                    e = var23;
                    e.printStackTrace();
                }
            }

            try {
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.write("Skor : " + this.skorfp);
                buf.close();
            } catch (IOException var22) {
                e = var22;
                e.printStackTrace();
            }
        } catch (Exception var26) {
            Exception ex = var26;
            ex.printStackTrace();
        }

    }

    public int bypassadmin() {
        this.loopFP = 0;
        this.matchFP = -1;
        this.bypass = true;

        try {
            List<String> admin = this.db.readAdmin(1);
            this.nikadmin = (String)admin.get(0);
            this.nameadmin = (String)admin.get(1);
            this.minutea1 = Base64.decode((String)admin.get(2), 0);
            this.minutea2 = Base64.decode((String)admin.get(3), 0);
            Log.d("Card Reader", this.nikadmin + " " + this.nameadmin + " " + this.minutea1);
            this.fingerprintSensor.open(0);
            this.fingerprintSensor.setTemplateFmt(0, 51);
            FingerprintService.setParameter(5010, 1);
            this.fingerprintSensor.setFingerprintCaptureListener(0, this.listener);
            this.fp1();
        } catch (FingerprintSensorException var4) {
            FingerprintSensorException e = var4;
            e.printStackTrace();
        }

        Log.d("Card Reader", "Matching :" + this.match);
        if (this.match == 2) {
            return this.match;
        } else {
            int loopRemain = 0;

            while(this.loopFP < 5) {
                try {
                    Thread.sleep(100L);
                    ++loopRemain;
                    if (loopRemain > 150) {
                        break;
                    }
                } catch (InterruptedException var5) {
                    InterruptedException e = var5;
                    e.printStackTrace();
                }
            }

            try {
                this.fingerprintSensor.stopCapture(0);
                this.fingerprintSensor.close(0);
            } catch (FingerprintSensorException var3) {
                var3.printStackTrace();
            }

            if (this.matchFP == -1) {
                Log.d("Card Reader", "Sidik Jari Tidak Cocok");
                this.ret = -1;
                this.match = -1;
                return -1;
            } else {
                return this.matchFP;
            }
        }
    }

    private void alert() {
        try {
            MediaPlayer mMediaPlayer = MediaPlayer.create(this, raw.beep);
            mMediaPlayer.setAudioStreamType(3);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        } catch (Exception var2) {
            Exception e = var2;
            e.printStackTrace();
        }

    }
}

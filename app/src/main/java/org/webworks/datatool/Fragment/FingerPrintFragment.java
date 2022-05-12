package org.webworks.datatool.Fragment;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.webworks.datatool.Model.FingerPrint;
import org.webworks.datatool.Repository.FingerPrintRepository;
import org.webworks.datatool.Repository.ReferralFormRepository;
import org.webworks.datatool.Model.ClientForm;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.webworks.datatool.R;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxSecurityLevel;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerPresentEvent;


public class FingerPrintFragment extends Fragment implements SGFingerPresentEvent {
    private OnFragmentInteractionListener mListener;
    Context context;
    private final String TAG = "PBS";
    private static final int IMAGE_CAPTURE_TIMEOUT_MS = 10000;
    private static final int IMAGE_CAPTURE_QUALITY = 50;
    private String PREFS_NAME;
    private String PREF_VERSION_CODE_KEY;
    private String PREF_USER_GUID;
    private String PREF_FACILITY_GUID;
    private String PREF_LAST_CODE;
    ReferralFormRepository referralFormRepository;
    FingerPrintRepository fingerPrintRepository;
    private final String EXTRA_FORM_ID = "FORM_ID";
//    private final String REFERRAL_FORM = "Referral_Form";
    private final String POST_TEST_INFORMATION = "Post_Test";
    private boolean formFilled;
    private ClientForm form;
    private String packageName;
    private PendingIntent mPermissionIntent;
    private byte[] mRegisterImage;
    private byte[] mVerifyImage;
    private byte[] mRegisterTemplate;
    private byte[] mVerifyTemplate;
    private int[] mMaxTemplateSize;
    private int mImageWidth;
    private int mImageHeight;
    private int mImageDPI;
    private int[] grayBuffer;
    private Bitmap grayBitmap;
    private IntentFilter filter; //2014-04-11
    private SGAutoOnEventNotifier autoOn;
    private JSGFPLib sgfplib;
    private boolean usbPermissionRequested, bSecuGenDeviceOpened;
    private ImageView finger_lt_thumb, finger_rt_thumb, finger_lt_index, finger_rt_index,finger_lt_middle, finger_rt_middle,finger_lt_ring, finger_rt_ring,finger_lt_pinky, finger_rt_pinky;
    private Button btnSavePBS, btnSkipPBS;
    private View view;
    private String clientIdentifier;
    private ArrayList<FingerPrint> fingerPrintArrayList;
    private ArrayList<String> existingFingerPrints;

    public FingerPrintFragment() {
        // Required empty public constructor
    }

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.d(TAG,"Enter mUsbReceiver.onReceive()");
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
                            Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
                        }
                        else
                            Log.e(TAG, "mUsbReceiver.onReceive() Device is null");
                    }
                    else
                        Log.e(TAG, "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };


    public static FingerPrintFragment newInstance() {
        FingerPrintFragment fragment = new FingerPrintFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("BioMetrics Capture");
        context = getActivity().getApplicationContext();
        packageName = context.getPackageName();
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            int formId = bundle.getInt(EXTRA_FORM_ID);
            if (formId != 0) {
                formFilled = true;
                form = new ClientForm();
                referralFormRepository = new ReferralFormRepository(context);
                form = referralFormRepository.getReferralFormById(formId);
            }
        }

        fingerPrintRepository = new FingerPrintRepository(context);

        PREFS_NAME = context.getResources().getString(R.string.pref_name);
        PREF_VERSION_CODE_KEY = context.getResources().getString(R.string.pref_version);
        PREF_USER_GUID = context.getResources().getString(R.string.pref_user);
        PREF_FACILITY_GUID = context.getResources().getString(R.string.pref_facility);
        PREF_LAST_CODE = context.getResources().getString(R.string.pref_code);
        mMaxTemplateSize = new int[1];
        fingerPrintArrayList = new ArrayList<>();
        existingFingerPrints = fingerPrintRepository.getAllFingerPrints();

        //USB Permissions
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        sgfplib = new JSGFPLib((UsbManager)context.getSystemService(Context.USB_SERVICE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_finger_print, container, false);
        initializeFields(view);
            setListeners();
        if (formFilled)
            assignValuesToFields(form, view);

        return view;
    }

    private void assignValuesToFields(ClientForm form, View view) {
        if (fingerPrintRepository.hasFingerPrintCaptured(form.getClientIdentifier()))
            btnSavePBS.setVisibility(View.GONE);

    }

    private void initializeFields(View view) {
        finger_lt_thumb = view.findViewById(R.id.left_thumb);
        finger_rt_thumb = view.findViewById(R.id.right_thumb);

        finger_lt_index = view.findViewById(R.id.left_index);
        finger_rt_index = view.findViewById(R.id.right_index);

        finger_lt_middle = view.findViewById(R.id.left_middle);
        finger_rt_middle = view.findViewById(R.id.right_middle);

        finger_lt_ring = view.findViewById(R.id.left_wedding);
        finger_rt_ring = view.findViewById(R.id.right_wedding);

        finger_lt_pinky = view.findViewById(R.id.left_small);
        finger_rt_pinky = view.findViewById(R.id.right_small);

        btnSavePBS = view.findViewById(R.id.btn_pbs_save);
        btnSkipPBS = view.findViewById(R.id.btn_skip_pbs);
    }

    private void setListeners() {
        btnSkipPBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(EXTRA_FORM_ID, form.getId());
                    mListener.onSkipButtonClicked(POST_TEST_INFORMATION, bundle);
                }
            }
        });

        btnSavePBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fingerPrintArrayList.size() > 6){
                    fingerPrintRepository.saveBulkFingerprint(fingerPrintArrayList);
                    fingerPrintArrayList.clear();
                    Snackbar.make(view, "Saved Bulk", Snackbar.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putInt(EXTRA_FORM_ID, form.getId());
                    mListener.onSkipButtonClicked(POST_TEST_INFORMATION, bundle);

                }else{
                    Snackbar.make(view, "Not enough finger prints captured", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


        if (form != null){
            clientIdentifier = form.getClientIdentifier();
            finger_lt_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureFingerPrint("left_thumb", clientIdentifier);
                }
            });

            finger_rt_thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureFingerPrint("right_thumb", clientIdentifier);
                }
            });

            finger_lt_index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureFingerPrint("left_index", clientIdentifier);
                }
            });

            finger_rt_index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureFingerPrint("right_index", clientIdentifier);
                }
            });

            finger_lt_middle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureFingerPrint("left_middle", clientIdentifier);
                }
            });

            finger_rt_middle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureFingerPrint("right_middle", clientIdentifier);
                }
            });

            finger_lt_ring.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureFingerPrint("left_wedding", clientIdentifier);
                }
            });

            finger_rt_ring.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureFingerPrint("right_wedding", clientIdentifier);
                }
            });

            finger_lt_pinky.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureFingerPrint("left_small", clientIdentifier);
                }
            });

            finger_rt_pinky.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CaptureFingerPrint("right_small", clientIdentifier);
                }
            });

        }else{
            Snackbar.make(view, "No Client info found, PBS won't work properly", Snackbar.LENGTH_LONG).show();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume(){
        Log.d(TAG, "Enter onResume()");
        super.onResume();
        getActivity().registerReceiver(mUsbReceiver, filter);
        long error = sgfplib.Init( SGFDxDeviceName.SG_DEV_AUTO);
        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE){
            if (error == SGFDxErrorCode.SGFDX_ERROR_DEVICE_NOT_FOUND)
                Toast.makeText(context, "The attached fingerprint device is not supported on Android", Toast.LENGTH_SHORT).show();

            else
                Toast.makeText(context, "Fingerprint device initialization failed!", Toast.LENGTH_SHORT).show();
        }
        else {
            UsbDevice usbDevice = sgfplib.GetUsbDevice();
            if (usbDevice == null){
                Toast.makeText(context, "SecuGen fingerprint sensor not found!", Toast.LENGTH_SHORT).show();
            }
            else {
                boolean hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                if (!hasPermission) {
                    if (!usbPermissionRequested)
                    {
                        Log.d(TAG, "Requesting USB Permission\n");
                        //Log.d(TAG, "Call GetUsbManager().requestPermission()");
                        usbPermissionRequested = true;
                        sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
                    }
                    else
                    {
                        //wait up to 20 seconds for the system to grant USB permission
                        hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                        Log.d(TAG, "Waiting for USB Permission\n");
                        int i=0;
                        while ((hasPermission == false) && (i <= 40))
                        {
                            ++i;
                            hasPermission = sgfplib.GetUsbManager().hasPermission(usbDevice);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //Log.d(TAG, "Waited " + i*50 + " milliseconds for USB permission");
                        }
                    }
                }
                if (hasPermission) {
                    Log.d(TAG, "Opening SecuGen Device\n");
                    error = sgfplib.OpenDevice(0);
                    Log.d(TAG, "OpenDevice() ret: " + error + "\n");
                    if (error == SGFDxErrorCode.SGFDX_ERROR_NONE)
                    {
                        bSecuGenDeviceOpened = true;
                        SecuGen.FDxSDKPro.SGDeviceInfoParam deviceInfo = new SecuGen.FDxSDKPro.SGDeviceInfoParam();
                        error = sgfplib.GetDeviceInfo(deviceInfo);
                        Log.d(TAG,"GetDeviceInfo() ret: " + error + "\n");
                        mImageWidth = deviceInfo.imageWidth;
                        mImageHeight= deviceInfo.imageHeight;
                        mImageDPI = deviceInfo.imageDPI;
                        sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
                        sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
                    }
                    else
                    {
                        Log.d(TAG, "Waiting for USB Permission\n");
                    }
                }
            }
        }
        Log.d(TAG, "Exit onResume()");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////
    //This message handler is used to access local resources not
    //accessible by SGFingerPresentCallback() because it is called by
    //a separate thread.
    public Handler fingerDetectedHandler = new Handler(){
        // @Override
        public void handleMessage(Message msg) {
        }
    };


    @Override
    public void SGFingerPresentCallback() {
        fingerDetectedHandler.sendMessage(new Message());
    }

    public void CaptureFingerPrint(String fingerPosition, String clientIdentifier){
        mRegisterImage = new byte[mImageWidth*mImageHeight];
        int[] imgQuality = new int[1];

        if (sgfplib.GetImageEx(mRegisterImage, IMAGE_CAPTURE_TIMEOUT_MS, IMAGE_CAPTURE_QUALITY) == SGFDxErrorCode.SGFDX_ERROR_NONE) {
            sgfplib.GetImageQuality(mImageWidth, mImageHeight, mRegisterImage, imgQuality);
            if (imgQuality[0] < 60){
                Snackbar.make(view, "Low Quality, Capture again", Snackbar.LENGTH_LONG).show();
            }else{
            try {
                mRegisterTemplate = new byte[mMaxTemplateSize[0]];
                mVerifyTemplate = new byte[mMaxTemplateSize[0]];

                sgfplib.CreateTemplate(null, mRegisterImage, mRegisterTemplate);

                if (mRegisterTemplate != null){
                    boolean[] matched = new boolean[1];
                    matched[0] = false;
                    long sl = SGFDxSecurityLevel.SL_NORMAL;

                    if (existingFingerPrints.size() > 0)

                    for (String fingerPrintTemplate : existingFingerPrints){
                        mVerifyTemplate = Base64.decode(fingerPrintTemplate, Base64.DEFAULT);
                        sgfplib.MatchTemplate(mRegisterTemplate, mVerifyTemplate, sl, matched);

                        if (matched[0]){
                            Snackbar.make(view, "Matched Found !!", Snackbar.LENGTH_LONG).show();
                            break;
                        }
                    }

                    if (!matched[0]){
                        //kinda late here though
                        //check if same finger is already on the memory list
                        if (exists(fingerPosition)){
                            Toast.makeText(context, "Finger cannot be captured twice", Toast.LENGTH_LONG).show();
                        }else{
                            FingerPrint fingerPrint = new FingerPrint();
                            fingerPrint.setFingerPosition(fingerPosition);
                            fingerPrint.setFpClientIdentifier(clientIdentifier);
                            fingerPrint.setCaptureQuality(imgQuality[0]);
                            fingerPrint.setFingerPrintCapture(Base64.encodeToString(mRegisterTemplate, Base64.DEFAULT));


                            int imgViewId = getResources().getIdentifier(fingerPosition, "id", packageName);
                            ImageView fingerImageView = view.findViewById(imgViewId);
                            fingerImageView.setImageBitmap(toGrayscale(mRegisterImage));

                            fingerPrintArrayList.add(fingerPrint);
                        }

                    }

                }
            } catch (Exception ex) {
                Toast.makeText(context, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            }
        }
        mRegisterImage = null;
    }

    //Converts image to grayscale (NEW)
    public Bitmap toGrayscale(byte[] mImageBuffer)
    {
        byte[] Bits = new byte[mImageBuffer.length * 4];
        for (int i = 0; i < mImageBuffer.length; i++) {
            Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = mImageBuffer[i]; // Invert the source bits
            Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.
        }

        Bitmap bmpGrayscale = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
        bmpGrayscale.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
        return bmpGrayscale;
    }

    public interface OnFragmentInteractionListener {
        void onSkipButtonClicked(String fragmentTag, Bundle bundle);

        void onContinueButtonClicked(String fragmentTag, Fragment fragment, Bundle bundle);
    }

    private boolean exists(String position){
        if (fingerPrintArrayList.size() > 0){
            for (FingerPrint fingerPrint : fingerPrintArrayList) {
                if (fingerPrint.getFingerPosition().equals(position)){
                    return true;
                }

            }
        }
        return false;
    }
}

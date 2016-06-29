/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.questron.WMicroWave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

//import com.example.android.BluetoothChat.DeviceListActivity;
import com.questron.WMicroWave.R.color;
import com.questron.WMicroWave.R.drawable;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
//import android.support.v4.app.FragmentActivity;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
//import android.content.Context;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
//import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This is the main Activity that displays the current chat session.
 */
public class WQblockBluetooth extends Activity implements DlgSelVessel.NoticeDialogListener{
	

	private ArrayList<GridDataModel> gridArray = new ArrayList<GridDataModel>();
	private CustomGridViewAdapter customGridAdapter;

	//chart engine
	private XYMultipleSeriesDataset mwDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mwRenderer = new XYMultipleSeriesRenderer();
	private XYSeries xySeriesPress=new XYSeries("Press");
	private XYSeries xySeriesOptTemp=new XYSeries("Opt Temp");
	private XYSeries xySeriesPower=new XYSeries("Power");
	private XYSeries xySeriesT1=new XYSeries("Temp1");
	private XYSeries xySeriesT2=new XYSeries("Temp2");
	private XYSeries xySeriesT3=new XYSeries("Temp3");
	private XYSeries xySeriesT4=new XYSeries("Temp4");
	private XYSeries xySeriesT5=new XYSeries("Temp5");
	private XYSeries xySeriesT6=new XYSeries("Temp6");
	private XYSeries xySeriesT7=new XYSeries("Temp7");
	private XYSeries xySeriesT8=new XYSeries("Temp8");
	private XYSeries xySeriesT9=new XYSeries("Temp9");
	private XYSeries xySeriesT10=new XYSeries("Temp10");
	private XYSeries xySeriesT11=new XYSeries("Temp11");
	private XYSeries xySeriesT12=new XYSeries("Temp12");
	private XYSeries xySeriesBar=new XYSeries("Temp Bar");
	
	private XYSeriesRenderer rendererPress = new XYSeriesRenderer();
	private XYSeriesRenderer rendererOptTemp = new XYSeriesRenderer();
	private XYSeriesRenderer rendererPower = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT1 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT2 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT3 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT4 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT5 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT6 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT7 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT8 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT9 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT10 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT11 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererT12 = new XYSeriesRenderer();
	private XYSeriesRenderer rendererBar = new XYSeriesRenderer();
	private GraphicalView tempChartView;
	private double mXcounter;
    
	// Debugging
    private static final String TAG = "BluetoothChat";
    private static final String TAG2 = "vessel";
    private static final boolean D = false;
    private static final boolean mPressureFlag=false;
    private Menu _menu = null;
    private ProgressDialog progressDialog=null;

    public static final int TEMP_MINIMUM = 59;
    // Message types sent from the BlueToothService Handler
    
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_FAILED = 6;
    public static final int MESSAGE_CONNECTION_LOST = 7;
    public static final int MAX_DATA = 200;
    //--------------------- runmodes
    public static final int METHOD_RUNNING = 1;
    public static final int MAG_ON_MANUAL =	2;
    public static final int RUNPRESCAL=	3;
    public static final int RUNIRCAL  =	4;
    public static final int METHOD_COMPLETED = 5;
    public static final int METHOD_STOPED = 6;
    public static final int METHODTERMINATED=	7;
    public static final int METHODIRCALCOMPLETED= 8;
    public static final int IRFAILED=9;
    public static final int VESSEL_COUNT_FAILED=10;
    public static final int ZERO_CROSS_FAILED=	11;
    public static final int OPT_VES_DET_FAIED=	12;
    public static final int POWER_TIME_PROTECT= 13;
    public static final int PRES_CAL_COMPLETED=	14;
    public static final int DELTA_T_FAILED	=	15;
    public static final int OVD_KEY_FAIED	=   16;
    
    public static final int	NO_INSITU_TEMP =255;//232;
    public static final int	TEMPMODE =0;
    public static final int POWERMODE= 1;
    
    public static final int	PRESENABLE= 0;
    public static final int PRESDISABLE= 1;
    public static final int TOTALVESSEL= 12;
    //----------------------  commands 
    public static final short METHOD_WRITE_CMND=1100;	//WRITE the following method to eeprom 
    public static final short METHOD_READ_CMND=1101;
    public static final short METHOD_START_CMND=1102;
    public static final short METHOD_STOP_CMND=1103;
    public static final short READ_STATUS_CMND=1104;
    public static final short SEND_TEST_DATA_ROWS_CMND=1105;
    public static final short SEND_TEST_RESULT_CMND=1106;
    public static final short CALIB_CMND=1107;
    public static final short SEND_CALIB_EXIT_CMND=1108;
    public static final short CMND_MAN_MAG_ON	=1109;
    public static final short CMND_MAN_MAG_OFF	=1110;
    public static final short CMND_MAN_COOL_ON	=1111;
    public static final short CMND_MAN_COOL_OFF	=1112;
    public static final short CMND_MAN_EX_ON	=1113;
    public static final short CMND_MAN_EX_OFF	=1114;
    public static final short CMND_MAN_MOT_ON	=1115;
    public static final short CMND_MAN_MOT_OFF	=1116;
    public static final short CMND_STARTPRESCAL =1117;
    public static final short CMND_WRITE_CONFIG =1118;
    public static final short CMND_READ_SETTINGS	=1119;
    public static final short CMND_WRITE_SETTINGS	=1120;
    public static final short CMND_IGNORE_IRFAIL	=1121;
    // Key names received from the BlueToothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_ADDRESS = "device_address";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int REQUEST_RECIPE = 4;
    private static final int REQUEST_WRITE_RECIPE = 5;
    private static final int REQUEST_SETTINGS = 6;
    private static final int REQUEST_START_BATCH = 7;

    //public Boolean[] 	bSelectedVesselCopy=new Boolean[16]; ;
    private Spinner batches_spinner ;
    private ArrayAdapter<String> spinnerAdapter=null;
    private List<String> batches_list;
    private String block_type;
    private Boolean bToggle=false;
    private Boolean bFullScreenFlag=false;
    private Boolean bOnDestroyFlag=false;
    private Boolean bCommunicationFlag=false;
    private Boolean mManualPageFlag=false;
    private Boolean bMethodValidFlag=false;
    private Boolean mStartTimerFlag=true;
    public Boolean mAskStatusFlag=false;
    public Boolean mStartMethodFlag=false;
    public Boolean mStartPresCalFlag=false;
    public Boolean mStopMethodFlag=false;
    private Boolean mDetailsFlag=false;
    public Boolean mBatchPageFlag=false;
    public Boolean mWriteMethodFlag=false;
    public Boolean bGatheringDataFlag=false;
    private int		mConnectingCntr=0;
    private Boolean bOfflineFlag=false;
    private Boolean bConnectingFlag=false;
    private Boolean bConnectedFlag=false;
    private Boolean mEnableButton=false;
    private Boolean bDialogIRFail=false;
    private Button mSendMethodButton;
    public Button mStartMethodButton;
    private Button mStopMethodButton;
    private Button mSaveBatchButton;
    private Button mDeleteBatchButton;
    
    public short[] mVesselHi = new short[TOTALVESSEL];
    public short[] mVesselLo = new short[TOTALVESSEL];
    public short[] mVesselTemp = new short[TOTALVESSEL];
    // Name of the connected device
    private String mConnectedDeviceName = null;
    private String mConnectedDeviceAddress = null;
    private String mRecipeName=null;
    private String mBatchName=null;
    private int  mlastTime;
    // Array adapter for the conversation thread
    //private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    //private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    public BluetoothService mBlthService = null;
    public QBlockMethod mQblockMethod=new QBlockMethod();
    public QBlockMethod mQblockMethodWrite=new QBlockMethod();
    private QBlockBatch  mQblockBatch;//=new QBlockBatch();
    //private Boolean mRefreshBlocks=false;
    private short mCal20,mCal200;
    // timer 
    public int mTimer=0,mTimeOut=0;
    public long mTimerRefBlocksInterval=10;
    protected Handler taskHandler = new Handler();
    //public Boolean mEnableTimerRefBlocks = false;
    private 	Timer status_timer=null;
    public	int swRevision=0;
    
    
  //Set how long before to start calling the TimerTask (in milliseconds)
  //0,
  //Set the amount of time between each execution (in milliseconds)
  //1000);
    
    
    // -- Set Timer --
     public void setTimerRefBlocks( )
    {
    	 if(status_timer!=null)
    		 return;
    	 status_timer = new Timer();
         TimerTask updateProfile = new StatusTimerTask(this);
         status_timer.scheduleAtFixedRate(updateProfile, 0, 200);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
        setContentView(R.layout.title);
        mStartTimerFlag=true;
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth Is Not Available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        initChartEngin();
        
    }
    
    
   // @SuppressLint("NewApi")
    @Override
    public void onStart() {
        super.onStart();
 
        if(D) Log.e(TAG, "++ ON START ++");
        Global gs = (Global) getApplication();
        gs.openSetting();

        //final ActionBar actionBar = getActionBar();
       	//actionBar.setTitle(R.string.app_name);
        //block_type = getString(R.string.block_type); 
        final ActionBar actionBar = getActionBar();
       	actionBar.setTitle(R.string.app_name);
       	actionBar.setIcon(drawable.questron_icon_small);
        block_type = getString(R.string.block_type); 
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } 
    }
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mBlthService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mBlthService.getState() == BluetoothService.STATE_NONE) {
              // Start the Bluetooth chat services
              mBlthService.start();
            }
        }
        if ((tempChartView != null) && mDetailsFlag &&(mBatchPageFlag==false)) {
                     tempChartView.repaint();
                     if(D) Log.d("chart","repaint on resume");
          }  
        
     
    }
    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mEnableTimerRefBlocks=false;
        bOnDestroyFlag=true;
        // Stop the Bluetooth chat services
        if(status_timer!=null)
        	status_timer.cancel();
        if (mBlthService != null) 
        	mBlthService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }
    
    public void initChartEngin(){
    	Global gs = (Global) getApplication();
    	//initialize chart engine
        mwRenderer.setAxisTitleTextSize(16);
        mwRenderer.setChartTitleTextSize(20);
        mwRenderer.setLabelsTextSize(15);
        mwRenderer.setLegendTextSize(15);
        mwRenderer.setMargins(new int[] {20, 35, 10, 15});
        mwRenderer.setAxesColor(getResources().getColor(R.color.Blue1));
        mwRenderer.setShowGrid(true);
        mwRenderer.setApplyBackgroundColor(true);
        mwRenderer.setBackgroundColor(getResources().getColor(R.color.LightBackground));
        mwRenderer.setMarginsColor(getResources().getColor(R.color.Blue6));
        mwRenderer.setZoomButtonsVisible(true);
        Log.d(TAG,"-----------------------------this is settings.sysconfig="+gs.mQblockSettings.SystemConfig);
        if(gs.mQblockSettings.SystemConfig<3){//power only
        	mwRenderer.setYTitle("Power (watts)");
        }
        else
        	mwRenderer.setYTitle("Temperature (\u2103)");
        mwRenderer.setXTitle("Time (sec)");
        mwRenderer.setLabelsColor(getResources().getColor(R.color.DarkRed));
        mwRenderer.setXLabelsColor(getResources().getColor(R.color.DarkRed));
        mwRenderer.setYLabelsColor( 0, getResources().getColor(R.color.DarkRed));        
        mwRenderer.setXAxisMin(0);
        mwRenderer.setYAxisMin(0);
        mwRenderer.setXAxisMax(100);
        if(gs.mQblockSettings.SystemConfig>3)
        	mwRenderer.setYAxisMax(200);
        else
        	mwRenderer.setYAxisMax(1250);
        double[] zoomarray={0,5000,0,1300};
        mwRenderer.setPanLimits(zoomarray);
        mwRenderer.setZoomLimits(zoomarray);
        mwRenderer.setGridColor(getResources().getColor(R.color.TextHint));
        mwDataset.addSeries(xySeriesT1);
        mwDataset.addSeries(xySeriesT2);
        mwDataset.addSeries(xySeriesT3);
        mwDataset.addSeries(xySeriesT4);
        mwDataset.addSeries(xySeriesT5);
        mwDataset.addSeries(xySeriesT6);
        mwDataset.addSeries(xySeriesT7);
        mwDataset.addSeries(xySeriesT8);
        mwDataset.addSeries(xySeriesT9);
        mwDataset.addSeries(xySeriesT10);
        mwDataset.addSeries(xySeriesT11);
        mwDataset.addSeries(xySeriesT12);
        mwDataset.addSeries(xySeriesPower);
        mwDataset.addSeries(xySeriesBar);
        
        //
        //mwRenderer.setAntialiasing(false);	//slowing test
        //
        
        rendererT1.setColor(getResources().getColor(R.color.Red));
        rendererT1.setLineWidth(2f);
        //rendererT1.setPointStyle(PointStyle.DIAMOND);
        //rendererT1.setFillPoints(true);
        
        rendererT2.setColor(getResources().getColor(R.color.Blue));
        rendererT2.setLineWidth(2f);
        //rendererT2.setPointStyle(PointStyle.DIAMOND);
        //rendererT2.setFillPoints(true);
        
        rendererT3.setColor(getResources().getColor(R.color.Green));
        rendererT3.setLineWidth(2f);
        //rendererT3.setPointStyle(PointStyle.DIAMOND);
        //rendererT3.setFillPoints(true);
        
        rendererT4.setColor(getResources().getColor(R.color.Orange));
        rendererT4.setLineWidth(2f);
        //rendererT4.setPointStyle(PointStyle.DIAMOND);
        //rendererT4.setFillPoints(true);
        
        rendererT5.setColor(getResources().getColor(R.color.Brown));
        rendererT5.setLineWidth(2f);
        //rendererT5.setPointStyle(PointStyle.DIAMOND);
        //rendererT5.setFillPoints(true);
        
        rendererT6.setColor(getResources().getColor(R.color.DarkRed));//.RoyalBlue));
        rendererT6.setLineWidth(2f);
        //rendererT6.setPointStyle(PointStyle.DIAMOND);
        //rendererT6.setFillPoints(true);
        
        rendererT7.setColor(getResources().getColor(R.color.BlueViolet));
        rendererT7.setLineWidth(2f);
        //rendererT7.setPointStyle(PointStyle.DIAMOND);
        //rendererT7.setFillPoints(true);
        
        rendererT8.setColor(getResources().getColor(R.color.Chocolate));
        rendererT8.setLineWidth(2f);
        //rendererT8.setPointStyle(PointStyle.DIAMOND);
        //rendererT8.setFillPoints(true);
        
        rendererT9.setColor(getResources().getColor(R.color.Aqua));
        rendererT9.setLineWidth(2f);
        //rendererT9.setPointStyle(PointStyle.DIAMOND);
        //rendererT9.setFillPoints(true);
        
        rendererT10.setColor(getResources().getColor(R.color.DarkBlue));
        rendererT10.setLineWidth(2f);
        //rendererT10.setPointStyle(PointStyle.DIAMOND);
        //rendererT10.setFillPoints(true);
        
        rendererT11.setColor(getResources().getColor(R.color.DarkGreen));
        rendererT11.setLineWidth(2f);
        //rendererT11.setPointStyle(PointStyle.DIAMOND);
        //rendererT11.setFillPoints(true);
        
        rendererT12.setColor(getResources().getColor(R.color.DarkRed));
        rendererT12.setLineWidth(2f);
        //rendererT12.setPointStyle(PointStyle.DIAMOND);
        //rendererT12.setFillPoints(true);
        
        rendererPower.setColor(getResources().getColor(R.color.DarkCyan));
        rendererPower.setLineWidth(1f);
        rendererPower.setPointStyle(PointStyle.CIRCLE);
        rendererPower.setFillPoints(true);
        
        rendererPress.setColor(getResources().getColor(R.color.RoyalBlue));
        rendererPress.setLineWidth(1f);
        //rendererPress.setPointStyle(PointStyle.CIRCLE);
        //rendererPress.setFillPoints(true);
        
        rendererOptTemp.setColor(getResources().getColor(R.color.RoyalBlue));//.DarkCyan));
        rendererOptTemp.setLineWidth(1f);
        //rendererOptTemp.setPointStyle(PointStyle.CIRCLE);
        //rendererOptTemp.setFillPoints(true);
        
        
        
        rendererBar.setColor(getResources().getColor(R.color.RoyalBlue));
        rendererBar.setLineWidth(0.1f);
        rendererBar.setPointStyle(PointStyle.DIAMOND);
        rendererBar.setFillPoints(false);
        //rendererBar.setBarSpacing(1);
        loadxySeriesBar((short)1,(short)50);
        
        mwRenderer.addSeriesRenderer(rendererT1);
        mwRenderer.addSeriesRenderer(rendererT2);
        mwRenderer.addSeriesRenderer(rendererT3);
        mwRenderer.addSeriesRenderer(rendererT4);
        mwRenderer.addSeriesRenderer(rendererT5);
        mwRenderer.addSeriesRenderer(rendererT6);
        mwRenderer.addSeriesRenderer(rendererT7);
        mwRenderer.addSeriesRenderer(rendererT8);
        mwRenderer.addSeriesRenderer(rendererT9);
        mwRenderer.addSeriesRenderer(rendererT10);
        mwRenderer.addSeriesRenderer(rendererT11);
        mwRenderer.addSeriesRenderer(rendererT12);
        mwRenderer.addSeriesRenderer(rendererPower);
        mwRenderer.addSeriesRenderer(rendererBar);
       
    }
    public void loadxySeriesBar(short vesno,short temp){
    	if((vesno>0)&&(vesno<=TOTALVESSEL)){
    		if(temp<TEMP_MINIMUM)
    			this.mVesselTemp[vesno-1]=5;
    		else
    			this.mVesselTemp[vesno-1]=temp;
    	}
    	xySeriesBar.clear();
    	int xend=(int) this.mwRenderer.getXAxisMax();
    	int xstart=(int) this.mwRenderer.getXAxisMin();
    	int xwidth=xend-xstart;
    	int xspace=xwidth/TOTALVESSEL;
    	for (int i=0;i<TOTALVESSEL;i++)
    		xySeriesBar.add((double)(i*xspace+xspace/2),(double)this.mVesselTemp[i]);
    }
   
    //@SuppressLint("NewApi")
	

    // The dialog fragment receives a reference to this Activity through the    
    // Fragment.onAttach() callback, which it uses to call the following methods    
    // defined by the NoticeDialogFragment.NoticeDialogListener interface    
    @Override    
    public void onDialogPositiveClick(DialogFragment dialog) {        
    	// User touched the dialog's positive button        ... 
    	String st=this.mQblockMethod.mRecipeName;
        if(st.equals("presCal.rcp")){//start pres calibration
        	startPresCalibration();
        }
        else{//	normal start
		    	Global gs = (Global) getApplication();
				addGraphDataSet_Renderer(gs.mQblockSettings.SystemConfig,gs.mQblockSettings.bSelectedVessel);
		    		mStartMethodFlag=true;
					mTimer=2;
		    		mStartMethodButton.setEnabled(false);
				
        }
    }
    /*
    ** set buttons and their click listener for main page
    */
    public void setButtonsInMain(){
    	// Initialise the Send method button with a listener that for click events
        mSendMethodButton = (Button) findViewById(R.id.btSendMethod);
        mSendMethodButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(v.getContext(),RecipeActivity.class);
                mQblockMethod.mRecipeName=mRecipeName;
                intent.putExtra("method", mQblockMethod);//put obj. in intent
                intent.putExtra("RecipeName", mRecipeName);
                Global gs = (Global) getApplication();
                gs.bEnableSendBut=true;
            	startActivityForResult(intent, REQUEST_WRITE_RECIPE);
            	
            }
        });
 
        // Initialise the Start method button with a listener that for click events
        mSaveBatchButton = (Button) findViewById(R.id.btSaveData);
        mSaveBatchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(v.getContext(),startBatchActivity.class);
                Global gs = (Global) getApplication();
                startActivityForResult(intent, REQUEST_START_BATCH);
                
            }
        });
        
        // Initialise the Start method button with a listener that for click events
        mStartMethodButton = (Button) findViewById(R.id.btStartMethod);
        mStartMethodButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Global gs = (Global) getApplication();
            	DialogFragment newFragment = new DlgSelVessel();   
            	
            	newFragment.show(getFragmentManager(), "DlgSelVessel");
            	
            }
        });
        
        // Initialise the Stop method button with a listener that for click events 
        mStopMethodButton = (Button) findViewById(R.id.btStopMethod);
        mStopMethodButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	if(mBlthService.mSentCmnd!=0){
            		mStopMethodFlag=true;
            		mTimer=2;
            	}
        		else{
        			mBlthService.stopMethod();
        			v.setEnabled(false);
        		}
            }
        });   
        
        enableDisButton();
    }
    
    private void setupChat() {
    	if(mBlthService!=null)
    		return;
    	if(D) Log.d(TAG, "setupChat()");
        // Initialise the BlueToothService to perform bluetooth connections
        mBlthService = new BluetoothService(this, mHandler);
       
    }
   
    
    /**
     * Sends a message.
     */
    
    private final void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    };

    private final void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        
        actionBar.setSubtitle(subTitle);
        actionBar.setTitle(R.string.app_name);
        
    }
    
    /*
     * read received string from bluetooth adapter and according to the
     * current command(mBlthService.mSentCmnd) interpret it
     */
    private void readMessage(Message msg){
    	Global gs = (Global) getApplication();
    	//byte[] readBuf = (byte[]) msg.obj;
        //*********************************************** 
        switch	(mBlthService.mSentCmnd){
        //------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------
        case	READ_STATUS_CMND://************************* status back from qblock
    		
        	if(!mBlthService.bEnableAskStatus){//**********************    this is status reading after connection
    			
    			mBlthService.readSystemSettings();
    			progressDialog.setMessage("Status Received! Reading Settings...");
    			this.mTimer=0;//reset to wait another 2 second for method back
        	    Log.d(TAG,"readSettings cmnd after status&connection");
        	    return;//exit do not mBlthService.mReadBufferIndex=0 and mBlthService.mSentCmnd=0;
        	}
        	
        	else if(!mBatchPageFlag){// ********************************** status reading because of timer refresh data in details page
    				refViewsDetail();
    				Log.d(TAG,"status read by ref data timer, totaltime="+mBlthService.mTotalTime+" testtime="
    						+mBlthService.mTestTime+" curstep="+mBlthService.mCurStep);
    		}
        	else{//refresh comm in batch page
        		ImageView imageview =(ImageView)findViewById(R.id.ivComm);
            	if(bCommunicationFlag){
            		bCommunicationFlag=false;
            		imageview.setImageResource(R.drawable.com_1);
            	}
            	else{
            		bCommunicationFlag=true;
            		imageview.setImageResource(R.drawable.com_2);
            	}
        	}
    		
    		break;
    		//------------------------------------------------------------------------------------------------------------
            //------------------------------------------------------------------------------------------------------------
            case	CMND_READ_SETTINGS://************************ read system settings confirmed
        		
        		int sysConfig=0,ccwOneTurnTime,cwOneTurnTime,vesselPos,deltaT,purgeCycle,purgeTemp;
        		
        		if((mBlthService.mReadBuffer[25]=='S')&&(mBlthService.mReadBuffer[26]=='E')&&(mBlthService.mReadBuffer[27]=='T')){
        			
        			sysConfig=(short)(mBlthService.mReadBuffer[0] & 0xff);
        			deltaT=   (short)(mBlthService.mReadBuffer[1] & 0xff);
        			purgeCycle=(short)(mBlthService.mReadBuffer[2] & 0xff);
        			purgeTemp=(short)((mBlthService.mReadBuffer[3] & 0xff) | mBlthService.mReadBuffer[4]<<8);
        			
        			ccwOneTurnTime=(short)((mBlthService.mReadBuffer[9] & 0xff) | mBlthService.mReadBuffer[10]<<8);
        			cwOneTurnTime=(short)((mBlthService.mReadBuffer[11] & 0xff) | mBlthService.mReadBuffer[12]<<8);
        			vesselPos=(short)((mBlthService.mReadBuffer[13] & 0xff) | mBlthService.mReadBuffer[14]<<8);
        			swRevision=(short)(mBlthService.mReadBuffer[23] & 0xff);
    	    		Log.d(TAG,"------read system settings confirmed sysConfig="+sysConfig+" detaT="+deltaT+" purgeCycle="+
    	    				purgeCycle+" pTemp="+purgeTemp+" ccw oneTurnTime="+ccwOneTurnTime+" cw oneTurnTime="+cwOneTurnTime);
    	    		//check if there was changes in settings send new settings to cpu
    	    		if((gs.mQblockSettings.DeltaT!=deltaT)|| (gs.mQblockSettings.PurgeCycle!=purgeCycle)||
    	    		  (gs.mQblockSettings.PurgeTemp!=purgeTemp)){
    	    			//send new settings
    	    			mBlthService.sendSystemSettings((short)gs.mQblockSettings.DeltaT,(short)gs.mQblockSettings.PurgeCycle
    	    					,(short)gs.mQblockSettings.PurgeTemp);
            			progressDialog.setMessage("Settings has changed ! Sending New Settings...");
    	    		}
    	    		else{// no change send read method command
    	    			if(gs.mQblockSettings.SystemConfig!=sysConfig){
    	    				//if sysConfig in cpu and tablet not same update tablet side
        	    			Log.d(TAG,"---------old system config was="+gs.mQblockSettings.SystemConfig);
        	    			gs.mQblockSettings.SystemConfig=sysConfig;
        	    			gs.SaveSettings();
        	    			Log.d(TAG,"---------new system config saved="+sysConfig);
        	    		}
        	    		else
        	    			Log.d(TAG,"--------- system config is same at MW and tablet="+sysConfig);
    	    			
    	    			if(gs.mQblockSettings.swRevision!=this.swRevision){
    	    				//if sysConfig in cpu and tablet not same update tablet side
        	    			Log.d(TAG,"---------old system swRev. was="+gs.mQblockSettings.swRevision);
        	    			gs.mQblockSettings.swRevision=this.swRevision;
        	    			gs.SaveSettings();
        	    			Log.d(TAG,"---------new system swRev. saved="+this.swRevision);
        	    		}
        	    		else
        	    			Log.d(TAG,"--------- system config is same at MW and tablet="+sysConfig);
        	    		Log.d(TAG,"---------------------------read system settings.vesPos="+vesselPos);
        	    		mBlthService.readMethod();
            			progressDialog.setMessage("Settings Received! Reading Recipe...");
            			Log.d(TAG,"readMethod cmnd after status&connection&settings");
    	    		}
    	    		this.mTimer=0;//reset to wait another 2 second for method back
            	    
            	    return;//exit do not mBlthService.mReadBufferIndex=0 and mBlthService.mSentCmnd=0;
        		}
        		else{
        			Log.d(TAG,"---------read system settings failed="+sysConfig);
        			mBlthService.askStatusDum();
        		}
        		
        		break;
    			
    	//------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------
        case	METHOD_READ_CMND://******************************** method received with readMethod() cmnd
        	openRecipe(mConnectedDeviceAddress);	
        	loadBufferToMethod(mQblockMethod); //load bluetooth read buffer to qblock method
        	if(mDetailsFlag&&!mBatchPageFlag){// if we are in details page  check status and enable/disable btns
        		loadMethodToString(mQblockMethod);
            	//refTemp();
        		progressDialog.setMessage("Please wait...!Recipe received, Reading batch...Expecting Total bytes="+mBlthService.mDataTotalNo*mBlthService.mDataLenght);
            	refRecipeName();
            	//mEnableButton=true;
        		//enableDisButton();
        		mXcounter=0;
        		clearAllDateSeries();
        		if(mBlthService.mDataTotalNo!=0){//****************   ask for test result up to now
        			mBlthService.askTestResult((short)(mBlthService.mDataTotalNo));
        			this.mTimeOut=mBlthService.mDataTotalNo/10;
        			if(this.mTimeOut<10)
        				this.mTimeOut=10;
        			this.mTimer=0;
            		//mBlthService.mAskTestResultBegin=true;
            		Log.d(TAG,"ASK TEST RESULT after method rcvd after connection mDataTotalNo="+mBlthService.mDataTotalNo);
            		return;  //exit do not mBlthService.mReadBufferIndex=0 and mBlthService.mSentCmnd=0;
        		}
        		else{	//there is no test res just start ref status
        			this.bGatheringDataFlag=false;//disable timeout checking
        			this.mTimer=0;
        			mBlthService.bEnableAskStatus=true;	//start refreshing status
        			mEnableButton=true;
                	mTimer=3;
                	progressDialog.dismiss();
        			if(mBlthService.mRunMode==METHOD_RUNNING){
        			//method running do something
        					
        			}
        		}
        		
        	}
        	Log.d(TAG,"method recvd");
        	break;
        	//------------------------------------------------------------------------------------------------------------
        	//------------------------------------------------------------------------------------------------------------
        	case	SEND_TEST_RESULT_CMND://***************************** whole test data received
        		//Log.d(TAG,"received back test result ,No of test data up to now was rcvd="+mBlthService.mRxDataBufferIndex+"  3="
        		//		+mBlthService.mRxDataBuffer[3][mBlthService.mRxDataBufferIndex-1]+"  4="+mBlthService.mRxDataBuffer[4][mBlthService.mRxDataBufferIndex-1]);
            	
        		Log.d(TAG,"received back test result ,No of test data up to now was rcvd="+mBlthService.mRxDataBufferIndex);
        		mEnableButton=true;
        		int yend=10;
        		int yaxis=0;//power only
        		Log.d(TAG,"-----------------------------this is settings.sysconfig="+gs.mQblockSettings.SystemConfig);
        		if(gs.mQblockSettings.SystemConfig>3){
        			yaxis=1;//ir temp
        			
        		}
            	for(int i=0;i<mBlthService.mRxDataBufferIndex;i++){//add data to chart
            		if(yend<(short)(mBlthService.mRxDataBuffer[yaxis][i] & 0xff)){
            			yend=(short)(mBlthService.mRxDataBuffer[yaxis][i] & 0xff);
            			setYMax(1,yend);
            			
            		}
            		addTempPresPower(i);
            			
            	}
            	
            	
            	mBlthService.mRxDataBufferIndexOnChart=mBlthService.mRxDataBufferIndex;
            	if(mBlthService.bAskTestResultBigFlag){
            		short startData=MAX_DATA;
            		short noOfData=(short) (mBlthService.noOfDataCopy-MAX_DATA);
            		if(noOfData>MAX_DATA){
            			noOfData=MAX_DATA;
            			mBlthService.noOfDataCopyPage+=1;
            			
            		}
            		else
            			mBlthService.noOfDataCopy=0;
            		mBlthService.askTestResultLastData2(startData,noOfData);
            		progressDialog.setMessage("Please wait...! Reading batch...Received bytes="+startData*mBlthService.mDataLenght+" Expecting Total bytes="+mBlthService.mDataTotalNo*mBlthService.mDataLenght);
            		return;
    			}
            	setXMax(1);
    			this.customGridAdapter.notifyDataSetChanged();
            	
            	this.bGatheringDataFlag=false;//disable timeout checking
    			this.mTimer=0;
            	aliRepaint();
            	mTimer=3;
            	mBlthService.bEnableAskStatus=true;	//start refreshing status
            	progressDialog.dismiss();
            	if(mBlthService.mRunMode==METHOD_RUNNING){//method running 
    					
    			}
    			break;
        //------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------
        case	SEND_TEST_DATA_ROWS_CMND://************************** test last data
        		if((mBlthService.mRxDataBufferIndex>mBlthService.mRxDataBufferIndexOnChart)&&!mBatchPageFlag){
        			//------------------------------we are in details page so add temp to graph
        			Log.d(TAG," message test row back before add temp");
        			int yend2=(int)this.mwRenderer.getYAxisMax();
        			yaxis=0;//power only
        			if(gs.mQblockSettings.SystemConfig>3){
            			yaxis=1;//ir temp
            		}
            		for(int i=mBlthService.mRxDataBufferIndexOnChart;i<mBlthService.mRxDataBufferIndex;i++){
        				//add data to chart
        				if(yend2<(short)(mBlthService.mRxDataBuffer[yaxis][i] & 0xff)){
                			yend2=(short)(mBlthService.mRxDataBuffer[yaxis][i] & 0xff);
                			setYMax(1,yend2);
                			
                		}
        				addTempPresPower(i);
        				
        			}
        			Log.d(TAG," message test row back after add temp");
        			mBlthService.mRxDataBufferIndexOnChart=mBlthService.mRxDataBufferIndex;
        			//----------------------------------------------------------------------
        			setXMax(1);
        			this.customGridAdapter.notifyDataSetChanged();
                	
        			refViewsDetail();
        			
        			//----------------------------------------------------------------------
        		}
        		if(mBlthService.bAskTestResultBigFlag){
        			if(mBlthService.noOfDataCopy>0){
        				short startData=(short) (mBlthService.noOfDataCopyPage*MAX_DATA);
                		short noOfData=(short) (mBlthService.noOfDataCopy-mBlthService.noOfDataCopyPage*MAX_DATA);
                		if(noOfData>MAX_DATA){
                			noOfData=MAX_DATA;
                			mBlthService.noOfDataCopyPage+=1;
                		}
                		else
                			mBlthService.noOfDataCopy=0;
                		mBlthService.askTestResultLastData2(startData,noOfData);
                		progressDialog.setMessage("Please Wait...! Reading batch...Received bytes="+startData*mBlthService.mDataLenght+" Expecting Total bytes="+mBlthService.mDataTotalNo*mBlthService.mDataLenght);
                		return;
        			}
        			mBlthService.bAskTestResultBigFlag=false;
        			
        			this.bGatheringDataFlag=false;//disable timeout checking
        			this.mTimer=0;
         			shrinkAll(false);
        			aliRepaint();
                	mTimer=3;
                	mBlthService.bEnableAskStatus=true;	//start refreshing status
                	progressDialog.dismiss();
                	if(mBlthService.mRunMode==METHOD_RUNNING){//method running 
                		
        			}
        		}
        		else{/*
        			setXMax(1);
        			this.customGridAdapter.notifyDataSetChanged();
                	
        			if(!mManualPageFlag &&!mBatchPageFlag){
        				refViewsDetail();
        				enableDisButton();
        				
    							
    				}
        			else if (!mBatchPageFlag){//refManualPage
        				refTemp();
        				refStatus();
        			}*/
        		}
        		if(D) Log.d(TAG,"last test no data was rcvd "+mBlthService.mRxDataBufferIndex);
        		break;	
        //------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------
        case	METHOD_WRITE_CMND://********************* method sent and confirmed	
        	refTemp();
        	mQblockMethod=mQblockMethodWrite;
    		refRecipeName();
    		loadVesPosVolToSetting();
    		addGraphDataSet_Renderer(gs.mQblockSettings.SystemConfig,gs.mQblockSettings.bSelectedVessel);
    		saveRecipe();
    		Log.d(TAG,"method sent confirmed");
    		loadMethodToString(mQblockMethod);
    		Toast.makeText(getApplicationContext(), "Recipe Tansferred TO Interface Device.", Toast.LENGTH_LONG).show();
    		break;
        
    
        
    	//------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------
        case	CALIB_CMND://********************* calibration= read cal20,200
        	break;
        //------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------
        case	METHOD_START_CMND://********************* start method cmnd confirmed
        	if(D) Log.d(TAG,"start method confirmed");
        	Time mTime =new Time();
        	mTime.setToNow();
        	String st=mTime.format3339(true)+",  "+mTime.hour+":"+mTime.minute+":"+mTime.second;
        	saveBatchTime(st);//.format2445();
    		mXcounter=0;
    		clearAllDateSeries();
    		mwRenderer.setXAxisMin(0);
            mwRenderer.setYAxisMin(0);
            mwRenderer.setXAxisMax(100);
            if(gs.mQblockSettings.SystemConfig>3)
            	mwRenderer.setYAxisMax(200);
    		gridArray.clear();
    		tempChartView.repaint();
    		mBlthService.mRxDataBufferIndexOnChart=0;
    		mTimer=1; //to refresh status
    		break;
    		
    	//------------------------------------------------------------------------------------------------------------
        case	CMND_IGNORE_IRFAIL://********************* IGNORE IR FAIL cmnd confirmed
        	Log.d(TAG,"IGNORE IR FAIL confirmed");
        	this.bDialogIRFail=false;
        	break;	
    	
    	//------------------------------------------------------------------------------------------------------------
        case	CMND_STARTPRESCAL://********************* start pres calib cmnd confirmed
        	Log.d(TAG,"start pres calibration confirmed");
        	
        	mTime =new Time();
        	mTime.setToNow();
        	st=mTime.format3339(true)+",  "+mTime.hour+":"+mTime.minute+":"+mTime.second;
        	saveBatchTime(st);
        	
        	mXcounter=0;
    		clearAllDateSeries();
    		mwRenderer.setXAxisMin(0);
            mwRenderer.setYAxisMin(0);
            mwRenderer.setXAxisMax(100);
            mwRenderer.setYAxisMax(200);
    		gridArray.clear();
    		tempChartView.repaint();
    		mBlthService.mRxDataBufferIndexOnChart=0;
    		mTimer=1; //to refresh status
    		break;	
    	//------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------
        case	METHOD_STOP_CMND://************************ stop method cmnd confirmed
    		if(D) Log.d(TAG,"stop method confirmed");
    		this.bDialogIRFail=false;
    		mTimer=1;
    		break;
    	//------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------
        case	CMND_WRITE_CONFIG://************************ send system config confirmed
    		
    		sysConfig=(short)(mBlthService.mReadBuffer[0] & 0xff);
    		Log.d(TAG,"send and received system config confirmed="+sysConfig);
    		gs.mQblockSettings.SystemConfig=sysConfig;
    		gs.SaveSettings();
    		
    		mTimer=1;
    		break;	
    	//------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------
            
        default:
    		break;
        }
        mBlthService.mReadBufferIndex=0;
    	mBlthService.mSentCmnd=0;
    }
    private void addTempPresPower(int i){
    	Global gs = (Global) getApplication();
    	if(gs.mQblockSettings.SystemConfig<3){
			addPowerToChart((i+1)*10,(short)(mBlthService.mRxDataBuffer[0][i] & 0xff));
		}
		else if(gs.mQblockSettings.SystemConfig==this.mBlthService.IR_OPTTEMP){//for ir_opt datalength=6
			addTempOptToChart((short)((mBlthService.mRxDataBuffer[3][i] & 0xff) | (mBlthService.mRxDataBuffer[4][i]<<8 )),
				(short)(mBlthService.mRxDataBuffer[1][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[2][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[0][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[5][i] & 0xff),
				gs.mQblockSettings.bSelectedVessel);
		}
		else if(gs.mQblockSettings.SystemConfig==this.mBlthService.IR_PRES){//for ir_pres datalength=6
			addTempPresToChart((short)((mBlthService.mRxDataBuffer[3][i] & 0xff) | (mBlthService.mRxDataBuffer[4][i]<<8 )),
				(short)(mBlthService.mRxDataBuffer[1][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[2][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[0][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[5][i] & 0xff),
				gs.mQblockSettings.bSelectedVessel);
		}
		else if(gs.mQblockSettings.SystemConfig==this.mBlthService.IR_PRES_OPTTEMP){//for ir_pres_opt datalength=7
			addTempOptPresToChart((short)((mBlthService.mRxDataBuffer[3][i] & 0xff) | (mBlthService.mRxDataBuffer[4][i]<<8 )),
				(short)(mBlthService.mRxDataBuffer[1][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[2][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[0][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[5][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[6][i] & 0xff),
				gs.mQblockSettings.bSelectedVessel);
		}
		
		else {//default is ir_only datalength=5
			addTempToChart((short)((mBlthService.mRxDataBuffer[3][i] & 0xff) | (mBlthService.mRxDataBuffer[4][i]<<8 )),
				(short)(mBlthService.mRxDataBuffer[1][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[2][i] & 0xff),
				(short)(mBlthService.mRxDataBuffer[0][i] & 0xff));
		}
    	
    }
    private void shrinkAll(Boolean shrink){
    	Global gs = (Global) getApplication();
    	if(shrink){
	    	if(xySeriesT1!=null)
	    		shrinkData(xySeriesT1);
	    	if(xySeriesT2!=null)
		    	shrinkData(xySeriesT2);
	    	if(xySeriesT3!=null)
				shrinkData(xySeriesT3);
	    	if(xySeriesT4!=null)
				shrinkData(xySeriesT4);
	    	if(xySeriesT5!=null)
				shrinkData(xySeriesT5);
	    	if(xySeriesT6!=null)
				shrinkData(xySeriesT6);
	    	if(xySeriesT7!=null)
				shrinkData(xySeriesT7);
	    	if(xySeriesT8!=null)
				shrinkData(xySeriesT8);
	    	if(xySeriesT9!=null)
				shrinkData(xySeriesT9);
	    	if(xySeriesT10!=null)
				shrinkData(xySeriesT10);
	    	if(xySeriesT11!=null)
				shrinkData(xySeriesT11);
	    	if(xySeriesT12!=null)
				shrinkData(xySeriesT12);
	    	if(xySeriesPower!=null)
				shrinkData(xySeriesPower);
	    	if(xySeriesPress!=null)
				shrinkData(xySeriesPress);
    	}
		this.customGridAdapter.notifyDataSetChanged();
		
		setXMax(1);
    }
    private void aliRepaint(){
    	if ((tempChartView != null) && mDetailsFlag && !mBatchPageFlag) {
	           LinearLayout layout = (LinearLayout) findViewById(R.id.HRchart);
		    	layout.removeAllViews();	//to refresh the graph
		    	layout.addView(tempChartView);
	        }
    }
    private void stateChanged(Message msg){
    	if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
        switch (msg.arg1) {
	        case BluetoothService.STATE_CONNECTED:
	        	bConnectingFlag=false;
	        	bConnectedFlag=true;
	            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
	            progressDialog.setMessage("Connection passed! Reading status...");
	            //*********************************it's now connected ask for status
	            if(mBlthService.mSentCmnd!=0){
	            	Toast.makeText(getApplicationContext(), "Warning: Device is busy can't get Status at this time cmnd ="+
	            				mBlthService.mSentCmnd+"  waitbyte="+mBlthService.mWaitByte, Toast.LENGTH_LONG).show();
	            	if(D) Log.d(TAG,"after connection can not ask status");
	            }
	    		else {
	    			mBlthService.askStatus();
	    			Log.d(TAG,"after connection asked status");
	    			if(mStartTimerFlag){	//if timer is not started start the timer
        				mStartTimerFlag=false;
        				setTimerRefBlocks();
                	}
	    			bGatheringDataFlag=true;
	    			mTimeOut=10;//wait for 2 second (10*200ms=2sec)
	    			mTimer=0;
	    		}
	            break;
	            
	        case BluetoothService.STATE_CONNECTING:
	            setStatus(R.string.title_connecting);
	            break;
	            
	        case BluetoothService.STATE_LISTEN:
	        case BluetoothService.STATE_NONE:
	            setStatus(R.string.title_not_connected);
	            break;
	    }
    }
    /*
     * ----------------------------------------------------------------
     * The Handler that gets information back from the BlueToothService
     * ----------------------------------------------------------------
    */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	//Global gs = (Global) getApplication();
            switch (msg.what) {
            
            case MESSAGE_STATE_CHANGE:
            	//**** message = state of the bluetooth adapter has changed *********************************
            	stateChanged(msg);
                break;
                 
            case MESSAGE_WRITE:
            	//**** message = a string has written to bluetooth stream ***********************************
            	byte[] writeBuf = (byte[]) msg.obj; // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                if(D) Log.d(TAG,"the following string has written to bluetooth stream="+writeMessage);
                break;
                
                
            case MESSAGE_CONNECTION_FAILED:
            	//**** message = connection failed try 3 times **********************************************
            	if(mConnectingCntr++<4){//it's locked up
            		bConnectingFlag=false;
            		progressDialog.setMessage("Connection failed! Trying again...");
            		connectDevice();
            	}
            	else{	//connection failed go to offline mode
            		mConnectingCntr=0;
            		bConnectingFlag=false;
            		bOfflineFlag=true;
            		progressDialog.dismiss();
            	}
            	
            	break;
              
            case MESSAGE_CONNECTION_LOST:
            	if(!bOnDestroyFlag){
	            	//if it reaches here some error happened and reset cpu board bluetooth
	            	Log.d(TAG,"after connection lost error");
	            	if (progressDialog.isShowing()){
	            		//we are at gathering data from microwave and error happened
	            		progressDialog.dismiss();
	            	}
            	}
            	break;
            	
            case MESSAGE_READ:
            	//**** message = read
            	readMessage(msg);
                break;
                
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
            	String alias=getAlias(msg.getData().getString(DEVICE_NAME));
            	if(alias==null)
            		mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
            	else
            		mConnectedDeviceName =alias;
                mConnectedDeviceAddress = msg.getData().getString(DEVICE_ADDRESS);
                break;
            case MESSAGE_TOAST:
                //Toast.makeText(getApplicationContext(),"timer="+mTimer+", "+ msg.getData().getString(TOAST),
                //               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
    	Global gs = (Global) getApplication();
        switch (requestCode) {
        case REQUEST_WRITE_RECIPE:	// ***** write method to qblock
        	fillContentView();
        	if (resultCode == Activity.RESULT_OK) {//request write recipe to qblock
        		QBlockMethod mm = (QBlockMethod) data.getSerializableExtra("method");
        		mQblockMethodWrite=mm;
        		mRecipeName=data.getStringExtra("RecipeName");
        		this.mWriteMethodFlag=true;
        		
            }
        	else{
        		loadMethodToString(mQblockMethod);
        		if(mBlthService.mSentCmnd!=0)
        			Toast.makeText(this, "Warning: Work in progress can't ask Status", Toast.LENGTH_LONG).show();
        		else
        			mBlthService.askStatus();
        	}
        	
        	clearAllDateSeries();
        	gridArray.clear();
        	int yend=0;
        	setXMax(1);
        	int yaxis=0;//power only
        	if(gs.mQblockSettings.SystemConfig>3){
    			yaxis=1;//ir temp
    		}
    		for(int i=0;i<mBlthService.mRxDataBufferIndex;i++){//add data to chart
        		if(yend<(short)(mBlthService.mRxDataBuffer[yaxis][i] & 0xff)){
        			yend=(short)(mBlthService.mRxDataBuffer[yaxis][i] & 0xff);
        			setYMax(1,yend);
        			
        		}
        		addTempPresPower(i);
        		
			}
        	shrinkAll(false);
        	aliRepaint();
            mBlthService.mRxDataBufferIndexOnChart=mBlthService.mRxDataBufferIndex;
        	return;
            
        case REQUEST_RECIPE:
        	if (resultCode == Activity.RESULT_OK) {
        		
            }
        	
        	if(D) Log.d("recipe ","gone");
            break;
        case REQUEST_START_BATCH:	//saving batch file
        	if (resultCode == Activity.RESULT_OK) {
        		if(D) Log.d(TAG,"saving bach file");
        		String st1=data.getStringExtra("BatchName");//mBatchName;
        		String st2=data.getStringExtra("BatchNote");//mBatchName;
        		String st3=data.getStringExtra("ChemistName");
        		if(D) Log.d(TAG,"st1st2="+st1+st2+st3);
        		
        		mBatchPageFlag=true;
                fillContentView();
                Menu mm=this.getMenu();
            	mm.getItem(0).setEnabled(true);//qblock page
                mm.getItem(2).setEnabled(false);//batch page
                mm.getItem(5).setEnabled(true);//full screen
                Log.d(TAG,"set4");
                saveBatch(st1,st2,st3);
                Log.d(TAG,"set5");
                return;
        		
            }
        	else{
        		if(D) Log.d("batcch","is nullgone");
        	}
            break;    
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                //connectDevice(data, true);
            }
            break;
        case REQUEST_SETTINGS:
            // When DeviceListActivity returns with a device to connect
        	if (resultCode == Activity.RESULT_OK) {
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occurred
                if(D) Log.d(TAG, "BT not enabled");
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
      getSelectedQblock();
      
      fillContentView();
      
    }
    public void setYMax(int mode,int ymax){
    	Global gs = (Global) getApplication();
    	ymax/=50;
		ymax=ymax*50+50;
		if(ymax<200)
			ymax=200;
		if(mode==1){
			if(gs.mQblockSettings.SystemConfig>3)
				this.mwRenderer.setYAxisMax(ymax);
		}
		else{
			if(mQblockBatch.mSystemConfig>3)
				this.mwRenderer.setYAxisMax(ymax);
		}
    }
    public void setXMax(int mode){
    	Global gs = (Global) getApplication();
    	int xend=100;
    	if((mode==1)&&(this.mBlthService!=null)){
    		if(mBlthService.mRxDataBufferIndex>0){
	    		if(gs.mQblockSettings.SystemConfig<3){//power only
	    			xend=mBlthService.mRxDataBufferIndex*10;
	    		}
		    	else{
	    	    	xend=(int)((mBlthService.mRxDataBuffer[3][mBlthService.mRxDataBufferIndex-1] & 0xff) 
					| (mBlthService.mRxDataBuffer[4][mBlthService.mRxDataBufferIndex-1]<<8 ));
			    }
    		}
    		else
    			return;
    	}
    	else{
	    	if(mQblockBatch.mSystemConfig<3){//power only
	    		xend=mQblockBatch.mRxDataBuffer[0].length*10;
	    	}
	    	else{
		    	xend=(int)((mQblockBatch.mRxDataBuffer[3][mQblockBatch.mRxDataBuffer[0].length-1] & 0xff) 
						| (mQblockBatch.mRxDataBuffer[4][mQblockBatch.mRxDataBuffer[0].length-1]<<8 ));
	    	}
    	}
    	xend/=50;
		xend=xend*50+50;
		if(xend<100)
			return;
		this.mwRenderer.setXAxisMax(xend);
		
    }
    public void connectDevice() {
    	if(this.bConnectingFlag||this.bConnectedFlag||this.bOfflineFlag){
    		
    		return;
    	}
    	getSelectedQblock();
    	setupChat();
    	this.bConnectingFlag=true;
    	mBlthService.bEnableAskStatus=false;
    	BluetoothAdapter mBtAdapter=BluetoothAdapter.getDefaultAdapter();;
    	// Get a set of currently paired devices
    	Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
    	Global gs = (Global) getApplication();
    	gs.iSelectedQblockCntr=0;
		int deviceNo=gs.iSelectedQblock[0];
		if(deviceNo>0){
			
    	// 	If there are paired devices, add each one to the ArrayAdapter
			if (pairedDevices.size() >= deviceNo) {
				int ii=0;
				for (BluetoothDevice device : pairedDevices) {
					ii++;
					if(ii==deviceNo){
						//if(D) Log.d(TAG, "device name="+device.getName()+" address="+device.getAddress());
						mBlthService.connect(device, true);
						if(progressDialog==null)
							progressDialog = ProgressDialog.show(this, "Gathering data Please Wait:", " Connecting...");
						
						//else if (!progressDialog.isShowing())	
						//	progressDialog.setm = ProgressDialog.show(this, "", "Connecting...");
						if(D) Log.d(TAG,"connecting to deviceNO="+deviceNo);
					}
				}
			}
        }
        // Get the device MAC address
     /*   String address = data.getExtras()
            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBlthService.connect(device, secure);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        MenuItem myMenuItem = menu.findItem(R.id.digest_sample);
        myMenuItem.setTitle(getString(R.string.block_type)+getString(R.string.s_string));
        myMenuItem.setTitle(block_type);
        _menu = menu;
        return true;
    }
    
     
    private Menu getMenu()
    {
        //use it like this
        return _menu;
    }

    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        Global gs = (Global) getApplication();
        switch (item.getItemId()) {
            
        case R.id.digest_sample:	//menu=QBLOCKS
        	
        	Menu mm=this.getMenu();
        	if(mDetailsFlag&&mBatchPageFlag){
        		//we are in batch page and returning to main page
        		fillPageMain();
        		mBatchPageFlag=false;
        		mManualPageFlag=false;
        		mm.getItem(2).setEnabled(true);//batch page
            	mm.getItem(0).setEnabled(false);//main page
            	mm.getItem(4).setEnabled(true);//manual menu
            	mm.getItem(5).setEnabled(false);//full screen
            	return true;
        	}
        	mm.getItem(0).setEnabled(false);//main page
        	mm.getItem(1).setEnabled(true);//recipe page
        	mm.getItem(2).setEnabled(true);//batch page
        	mm.getItem(4).setEnabled(true);//manual menu
        	mm.getItem(5).setEnabled(false);//full screen menu
        	mm.getItem(6).setEnabled(true);//settings
        	
        	if(mManualPageFlag){
        		//we are in manual page and returning to main page
        		FrameLayout mylay = (FrameLayout) findViewById(R.id.frameLeftDownMain); 
            	mylay.setVisibility(View.VISIBLE);
            	mylay = (FrameLayout) findViewById(R.id.frameLeftDownManual); 
            	mylay.setVisibility(View.INVISIBLE);
            	this.mManualPageFlag=false;
        		return true;
        	}
        	if(mStartTimerFlag)
        		mEnableButton=false;
        	mDetailsFlag=true;
        	mBatchPageFlag=false;
        	mManualPageFlag=false;
        	fillContentView();
        	connectDevice();
        	return true;
            
        case R.id.recipe:     //MENU=DIGESTION RECIPE	
        	Intent intent1=new Intent(this,RecipeActivity.class);
        	gs.bEnableSendBut=false;
            mm=this.getMenu();
            mm.getItem(4).setEnabled(false);//manual menu
            mm.getItem(0).setEnabled(true);//main page
            Log.d(TAG,"        ves"+this.mQblockMethod.mVesselNo);
        	intent1.putExtra("method", mQblockMethod);//put obj. in intent
        	startActivityForResult(intent1,REQUEST_RECIPE);
           return true;
        
        case R.id.view_batch:    //menu = batch 	
        	mBatchPageFlag=true;
           fillContentView();
           item.setEnabled(false);
           mm=this.getMenu();
           mm.getItem(2).setEnabled(false);//batch page
       	   mm.getItem(0).setEnabled(true);//main page
       	   mm.getItem(4).setEnabled(false);//manual menu
       	   mm.getItem(5).setEnabled(true);//full screen
           return true;
           
        case R.id.settings: 
        	SubMenu subMenu = item.getSubMenu();
        	if(mDetailsFlag&&!this.mBatchPageFlag){//if we are in details page do nothing
        		if(mPressureFlag)
                	subMenu.getItem(0).setVisible(true);
                return true;
        	}
        	else
        		subMenu.getItem(0).setVisible(false);
        	Intent intent = new Intent(this,SettingsActivity.class);
           startActivityForResult(intent, REQUEST_SETTINGS);
           mm=this.getMenu();
           mm.getItem(4).setEnabled(false);
           return true;  
        case R.id.manual:
        	manualPage();//loadManualPage and handle manual cammand
        	mm=this.getMenu();
            mm.getItem(0).setEnabled(true);
            mm.getItem(4).setEnabled(false);//manual menu
            return true;
            
        case R.id.fullScreen:
        	FrameLayout mylay = (FrameLayout) findViewById(R.id.frameLeft); 
        	LinearLayout mypar=(LinearLayout) mylay.getParent();
        	int mywidth=mypar.getWidth();
        	if(bFullScreenFlag){
        		bFullScreenFlag=false;
        		
        		mylay.getLayoutParams().width=(mywidth/2);
        		mylay = (FrameLayout) findViewById(R.id.frameRight);
        		mylay.getLayoutParams().width=(mywidth/2);
        		
        	}
        	else{	
        		bFullScreenFlag=true;
        		mylay.getLayoutParams().width=1;
        		mylay = (FrameLayout) findViewById(R.id.frameRight);
        		mylay.getLayoutParams().width=(mywidth-1);
        		
        		
        	}
        	if(mBatchPageFlag)
        		refViewsBatch();
        	return true;
        	
        /*case R.id.settingsPrsCal://pressure calibration
        	startPresCalibration();
        	return true;
        	*/
        case R.id.settingsSysConfig:
        	//readSystemSettings();
        	showSystemConfigAlert(gs.mQblockSettings.SystemConfig);
        	return true;
        case R.id.calibration:     	
        	return true;
        	
        
        }
        return false;
    }
    public void showSystemConfigAlert(int syscon){
    	TextView viewPower= (TextView) findViewById(R.id.power1);
    	AlertDialog.Builder alert = new AlertDialog.Builder(viewPower.getContext());   
        alert.setTitle("System Configuration");   
        alert.setMessage("The Current SystemConfig ="+syscon+"Do You Want to Change it?  ");  
        final EditText input = new EditText(this.getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);   
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
            public void onClick(DialogInterface dialog, int whichButton){
            	//start;
            	if(input.length()==0)
            		return;
            	int newSysConfig =Integer.parseInt(input.getText().toString());// input.getText();
            	Log.d(TAG,"new sys con="+newSysConfig);
            	mBlthService.sendSystemConfig((short)newSysConfig);
            }   
        });   
           
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
            public void onClick(DialogInterface dialog, int whichButton){   
                //canceled   
            }   
        });   
        alert.show();   
    }
    public void showDialogIRFailAlert(){
    	TextView viewPower= (TextView) findViewById(R.id.power1);
    	AlertDialog.Builder alert = new AlertDialog.Builder(viewPower.getContext());   
        alert.setTitle("Warning!!!"); 
        alert.setMessage("There is not expected Temperature rise in vessels, To Continue Digestion press Ignore OR press Stop Digestion to Fininsh! ");  
        alert.setPositiveButton("Ignore Warning", new DialogInterface.OnClickListener(){   
            public void onClick(DialogInterface dialog, int whichButton){
            	//start;
            	Log.d(TAG,"ignore button pressed");
            	mBlthService.ignoreIRFail();
            }   
        });   
           
        alert.setNegativeButton("Stop Digestion", new DialogInterface.OnClickListener(){   
            public void onClick(DialogInterface dialog, int whichButton){   
                //canceled   
            	Log.d(TAG,"stop button pressed");
            	mBlthService.stopMethod();
            }   
        });   
        alert.show();   
    }
    public void readSystemSettings(){
    	
    	this.mBlthService.readSystemSettings();
    }
    /*
    public void refManualPage(){
    	if(mBlthService== null)
        	return ;
        
        Switch s= (Switch) findViewById(R.id.swExhaust); 
        s.setChecked(mBlthService.bExhaust);
        s = (Switch) findViewById(R.id.swMotor); 
        s.setChecked(mBlthService.bMotorEnable);
        s = (Switch) findViewById(R.id.swMicrowave); 
        s.setChecked(mBlthService.bMagLo&mBlthService.bMagHi);
    }
*/
    public void manualPage(){
    	
    	FrameLayout mylay = (FrameLayout) findViewById(R.id.frameLeftDownMain); 
    	mylay.setVisibility(View.INVISIBLE);
    	mylay = (FrameLayout) findViewById(R.id.frameLeftDownManual); 
    	mylay.setVisibility(View.VISIBLE);
    	this.mManualPageFlag=true;
    
        if(mBlthService== null)
        	return ;
       
        Switch s = (Switch) findViewById(R.id.swExhaust); 
        s.setChecked(mBlthService.bExhaust);
        if (s != null) {            
        	s.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        		
        		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        		// TODO Auto-generated method stub20String state="OFF";
        			if(isChecked){
        			//state="ON";
        				mBlthService.manExOn();
        				Toast.makeText(getApplicationContext(), "exh ON cmnd",Toast.LENGTH_LONG).show();
        			}
        			else{
        				Toast.makeText(getApplicationContext(), "exh OFF cmnd",Toast.LENGTH_LONG).show();
        				mBlthService.manExOff();
        			}
        		}});}
        
        s = (Switch) findViewById(R.id.swMotor); 
        s.setChecked(mBlthService.bMotorEnable);
        if (s != null) {            
        	s.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        		
        		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        		// TODO Auto-generated method stub20String state="OFF";
        			if(isChecked){
        			//state="ON";
        				mBlthService.manMotorOn();
        				Toast.makeText(getApplicationContext(), "motor ON cmnd",Toast.LENGTH_LONG).show();
        			}
        			else{
        				Toast.makeText(getApplicationContext(), "motor OFF cmnd",Toast.LENGTH_LONG).show();
        				mBlthService.manMotorOff();
        			}
        		}});}
        
        s = (Switch) findViewById(R.id.swMicrowave); 
        s.setChecked(mBlthService.bMagLo&mBlthService.bMagHi);
        if (s != null) {            
        	s.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        		
        		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        		// TODO Auto-generated method stub20String state="OFF";
        			if(isChecked){
        			//state="ON";
        				TextView view =(TextView) findViewById(R.id.etPowerMW);
        				String st = view.getText().toString();
        		        int int1,int2;
        		        int2=1200;
        		        try {
        		        	if(st.isEmpty()){
        		        		Toast.makeText(getApplicationContext(), "Warning: Not a Valid Power Value ...", Toast.LENGTH_LONG).show();
        		        		view.setText("000");
        		        		buttonView.setChecked(false);
        		        		return;
        		        	}
        		        	else {
        		        			int1=Integer.parseInt(st);
        		        			if(int1>int2){
        		        				Toast.makeText(getApplicationContext(), "Warning: Power Entered is Higher than Maximum Limit..", Toast.LENGTH_LONG).show();
        		        				view.setText(Integer.toString(int2));
        		        				buttonView.setChecked(false);
        		        				return;
        		        			}
        		        			
        		        	}
        		        		
        		        } catch(NumberFormatException nfe) {
        				          // Handle parse error.
        		        		buttonView.setChecked(false);
        		    		  	return ;
        		        		}      
        				mBlthService.manMWon((short)int1);
        				Toast.makeText(getApplicationContext(), "MW ON cmnd",Toast.LENGTH_LONG).show();
        			}
        			else{
        				Toast.makeText(getApplicationContext(), "MW OFF cmnd",Toast.LENGTH_LONG).show();
        				mBlthService.manMWOff();
        			}
        		}});}
    }
    
    
    
    
    
    public void makeTab(){
    	
    	TabHost tabHost=(TabHost)findViewById(R.id.tabhost);//R.id.tabHost1);
        tabHost.setup();

        TabSpec spec1=tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("");

        TabSpec spec2=tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("");
        spec2.setContent(R.id.tab2);

        ListView listView = (ListView) findViewById(R.id.listview);
        
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
      //suhas change
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab1);
        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab2);
        
        GridDataModel gvdata=new GridDataModel();
        gvdata.setTime(1100);
        gvdata.setPower(1200);
        for (int i=1;i<TOTALVESSEL;i++)
        	gvdata.setTemp(i, 150+i);
        
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.grid_row, gridArray);
        listView.setAdapter(customGridAdapter);


    }
    
    public void fillPageMain(){
    	setContentView(R.layout.main);
    	makeTab();
		Global gs = (Global) getApplication();
    	/*
		if(gs.mQblockSettings.SystemConfig==this.mBlthService.IR_OPTTEMP){//for ir_opt
    		TextView tv=(TextView) this.findViewById(R.id.lastColTitle);
    		tv.setText("INS");
    	}
    	*/
    	addGraphDataSet_Renderer(gs.mQblockSettings.SystemConfig,gs.mQblockSettings.bSelectedVessel);
		mXcounter=0;
		int yend=0;
		setXMax(1);
		gridArray.clear();
		this.customGridAdapter.notifyDataSetChanged();
		clearAllDateSeries();
		int yaxis=0;//power only
		if(gs.mQblockSettings.SystemConfig>3){
			yaxis=1;//ir temp
		}
		for(int i=0;i<mBlthService.mRxDataBufferIndex;i++){//add data to chart
    		if(yend<(short)(mBlthService.mRxDataBuffer[yaxis][i] & 0xff)){
    			yend=(short)(mBlthService.mRxDataBuffer[yaxis][i] & 0xff);
    			setYMax(1,yend);
    			
    		}
    		addTempPresPower(i);
    		
		}
    	shrinkAll(false);
    	aliRepaint();
        mBlthService.mRxDataBufferIndexOnChart=mBlthService.mRxDataBufferIndex;
		setButtonsInMain();
    	loadMethodToString(mQblockMethod);
    	if(mBlthService.mSentCmnd!=0)
    		Log.d(TAG, "Warning: Work in progress can't ask Status");
    	else
    		mBlthService.askStatus();
    }
    
    public QBlockMethod loadStringToMethod(){
    	QBlockMethod mm=new QBlockMethod();
        return mm;
    }
    
    public void loadPresCalMethodToMethod(QBlockMethod mm){
    	
    		mm.TarTemp[0]=180;
			mm.RampTime[0]=5;
			mm.HoldTime[0]=3;
			mm.TarTemp[1]=200;
			mm.RampTime[1]=3;
			mm.HoldTime[1]=3;
			mm.TarTemp[2]=230;
			mm.RampTime[2]=3;
			mm.HoldTime[2]=3;
			mm.TarTemp[3]=0;
			mm.RampTime[3]=0;
			mm.HoldTime[3]=0;
			mm.Type=TEMPMODE;
			mm.PresMode=PRESENABLE;
			mm.PowerLimit=1200;
			mm.PresLimit=600;
			mm.TempLimit=240;
    	}
    
    /*
     * load bluetooth read buffer to qblock method
     */
    public void loadBufferToMethod(QBlockMethod mm){
    	//load buffer to method
	    	mm.TarTemp[0]=(short)((mBlthService.mReadBuffer[0] & 0xff) | mBlthService.mReadBuffer[1]<<8);
			mm.RampTime[0]=(short)((mBlthService.mReadBuffer[2] & 0xff) | mBlthService.mReadBuffer[3]<<8);
			mm.HoldTime[0]=(short)((mBlthService.mReadBuffer[4] & 0xff) | mBlthService.mReadBuffer[5]<<8);
			mm.TarTemp[1]=(short)((mBlthService.mReadBuffer[6] & 0xff) | mBlthService.mReadBuffer[7]<<8);
			mm.RampTime[1]=(short)((mBlthService.mReadBuffer[8] & 0xff) | mBlthService.mReadBuffer[9]<<8);
			mm.HoldTime[1]=(short)((mBlthService.mReadBuffer[10] & 0xff) | mBlthService.mReadBuffer[11]<<8);
			mm.TarTemp[2]=(short)((mBlthService.mReadBuffer[12] & 0xff) | mBlthService.mReadBuffer[13]<<8);
			mm.RampTime[2]=(short)((mBlthService.mReadBuffer[14] & 0xff) | mBlthService.mReadBuffer[15]<<8);
			mm.HoldTime[2]=(short)((mBlthService.mReadBuffer[16] & 0xff) | mBlthService.mReadBuffer[17]<<8);
			mm.TarTemp[3]=(short)((mBlthService.mReadBuffer[18] & 0xff) | mBlthService.mReadBuffer[19]<<8);
			mm.RampTime[3]=(short)((mBlthService.mReadBuffer[20] & 0xff) | mBlthService.mReadBuffer[21]<<8);
			mm.HoldTime[3]=(short)((mBlthService.mReadBuffer[22] & 0xff) | mBlthService.mReadBuffer[23]<<8);
			mm.Type=(short)(mBlthService.mReadBuffer[24] & 0xff);
			mm.PresMode=(short)(mBlthService.mReadBuffer[25] & 0xff);
			mm.PowerLimit=(short)((mBlthService.mReadBuffer[26] & 0xff) | mBlthService.mReadBuffer[27]<<8);
			mm.PresLimit=(short)((mBlthService.mReadBuffer[28] & 0xff) | mBlthService.mReadBuffer[29]<<8);
			mm.TempLimit=(short)((mBlthService.mReadBuffer[30] & 0xff) | mBlthService.mReadBuffer[31]<<8);
	    
		this.bMethodValidFlag=true;
    }
    public void loadMethodToString(QBlockMethod mm){
    	if(!bMethodValidFlag)
    		return;
    	
        TextView view = (TextView) findViewById(R.id.targetView1);
        view.setText(Integer.toString(mm.TarTemp[0]));
        
        view = (TextView) findViewById(R.id.holdView1);
        view.setText(Integer.toString(mm.HoldTime[0]));
        
        view = (TextView) findViewById(R.id.targetView2);
        view.setText(Integer.toString(mm.TarTemp[1]));
        view = (TextView) findViewById(R.id.holdView2);
        view.setText(Integer.toString(mm.HoldTime[1]));
        
        view = (TextView) findViewById(R.id.targetView3);
        view.setText(Integer.toString(mm.TarTemp[2]));
        view = (TextView) findViewById(R.id.holdView3);
        view.setText(Integer.toString(mm.HoldTime[2]));
        
        view = (TextView) findViewById(R.id.targetView4);
        view.setText(Integer.toString(mm.TarTemp[3]));
        view = (TextView) findViewById(R.id.holdView4);
        view.setText(Integer.toString(mm.HoldTime[3]));
        view = (TextView) findViewById(R.id.methodTempLim);
        view.setText(Integer.toString(mm.TempLimit));
        view = (TextView) findViewById(R.id.methodPowerLim);
        view.setText(Integer.toString(mm.PowerLimit));
        view = (TextView) findViewById(R.id.recipe_name1);
        view.setText(mm.mRecipeName);
        view = (TextView) findViewById(R.id.valueNoVessel);
        view.setText(Integer.toString(mm.mVesselNo));
        view = (TextView) findViewById(R.id.valueVolume);
        int mVol=0;
        if(this.mQblockMethod.mReagentVolume!=null){
        for(int i=0;i<5;i++)
    		mVol+=this.mQblockMethod.mReagentVolume[i];
        }
    	if(mVol>40)//limit the max vol to 40ml
    		mVol=40;
    	view.setText(Integer.toString(mVol));
        if(mm.Type==TEMPMODE){//(short)1)
        	view = (TextView) findViewById(R.id.valueRecipeType);
            view.setText("Temp.");
    		view = (TextView) findViewById(R.id.rampView1);
            view.setText(Integer.toString(mm.RampTime[0]));
            view = (TextView) findViewById(R.id.rampView2);
            view.setText(Integer.toString(mm.RampTime[1]));
            view = (TextView) findViewById(R.id.rampView3);
            view.setText(Integer.toString(mm.RampTime[2]));
            view = (TextView) findViewById(R.id.rampView4);
            view.setText(Integer.toString(mm.RampTime[3]));
            view = (TextView) findViewById(R.id.targetTemp);
    		view.setText("Temp");
    		view = (TextView) findViewById(R.id.targetTemp1);
    		view.setText("(\u2103)");
            
    	}
    	else{
    		view = (TextView) findViewById(R.id.valueRecipeType);
            view.setText("Power");
    		view = (TextView) findViewById(R.id.rampView1);
            view.setText("--");
            view = (TextView) findViewById(R.id.rampView2);
            view.setText("--");
            view = (TextView) findViewById(R.id.rampView3);
            view.setText("--");
            view = (TextView) findViewById(R.id.rampView4);
            view.setText("--");
            view = (TextView) findViewById(R.id.targetTemp);
    		view.setText("Power");
    		view = (TextView) findViewById(R.id.targetTemp1);
    		view.setText("W");
            
    	}
        Log.d(TAG,"loadMethodToString");
        
    }
    public void loadMethodToStringBatch(QBlockMethod mm){
    	
        TextView view = (TextView) findViewById(R.id.b_targetView1);
        view.setText(Integer.toString(mm.TarTemp[0]));
        
        view = (TextView) findViewById(R.id.b_sample_amount_Text);
        view.setText(Integer.toString(mm.mSampleAmount));
        view = (TextView) findViewById(R.id.b_valueNoVessel);
        view.setText(Integer.toString(mm.mVesselNo));
        if(mm.mReagentVolume != null){
	        view = (TextView) findViewById(R.id.b_reagentml1);
	        view.setText(Integer.toString(mm.mReagentVolume[0]));
	        
	        view = (TextView) findViewById(R.id.b_reagentml2);
	        view.setText(Integer.toString(mm.mReagentVolume[1]));
	        
	        view = (TextView) findViewById(R.id.b_reagentml3);
	        view.setText(Integer.toString(mm.mReagentVolume[2]));
	        
	        view = (TextView) findViewById(R.id.b_reagentml4);
	        view.setText(Integer.toString(mm.mReagentVolume[3]));
	        
	        view = (TextView) findViewById(R.id.b_reagentml5);
	        view.setText(Integer.toString(mm.mReagentVolume[4]));
        }
        else{
        	view = (TextView) findViewById(R.id.b_reagentml1);
	        view.setText(Integer.toString(0));
	        
	        view = (TextView) findViewById(R.id.b_reagentml2);
	        view.setText(Integer.toString(0));
	        
	        view = (TextView) findViewById(R.id.b_reagentml3);
	        view.setText(Integer.toString(0));
	        
	        view = (TextView) findViewById(R.id.b_reagentml4);
	        view.setText(Integer.toString(0));
	        
	        view = (TextView) findViewById(R.id.b_reagentml5);
	        view.setText(Integer.toString(0));
        }
        view = (TextView) findViewById(R.id.b_holdView1);
        view.setText(Integer.toString(mm.HoldTime[0]));
        
        view = (TextView) findViewById(R.id.b_targetView2);
        view.setText(Integer.toString(mm.TarTemp[1]));
        view = (TextView) findViewById(R.id.b_holdView2);
        view.setText(Integer.toString(mm.HoldTime[1]));
        
        view = (TextView) findViewById(R.id.b_targetView3);
        view.setText(Integer.toString(mm.TarTemp[2]));
        view = (TextView) findViewById(R.id.b_holdView3);
        view.setText(Integer.toString(mm.HoldTime[2]));
        
        view = (TextView) findViewById(R.id.b_targetView4);
        view.setText(Integer.toString(mm.TarTemp[3]));
        view = (TextView) findViewById(R.id.b_holdView4);
        view.setText(Integer.toString(mm.HoldTime[3]));
        view = (TextView) findViewById(R.id.b_methodTempLim);
        view.setText(Integer.toString(mm.TempLimit));
        view = (TextView) findViewById(R.id.b_methodPowerLim);
        view.setText(Integer.toString(mm.PowerLimit));
        if(mm.Type==TEMPMODE){//(short)1)
    		view = (TextView) findViewById(R.id.b_recipe_type);
            view.setText("Temp.");
    		view = (TextView) findViewById(R.id.b_rampView1);
            view.setText(Integer.toString(mm.RampTime[0]));
            view = (TextView) findViewById(R.id.b_rampView2);
            view.setText(Integer.toString(mm.RampTime[1]));
            view = (TextView) findViewById(R.id.b_rampView3);
            view.setText(Integer.toString(mm.RampTime[2]));
            view = (TextView) findViewById(R.id.b_rampView4);
            view.setText(Integer.toString(mm.RampTime[3]));
            view = (TextView) findViewById(R.id.b_targetTemp);
    		view.setText("Temp");
    		view = (TextView) findViewById(R.id.b_targetTemp1);
    		view.setText("(\u2103)");
           
    	}
    	else{
    		view = (TextView) findViewById(R.id.b_recipe_type);
            view.setText("Power");
    		view = (TextView) findViewById(R.id.b_rampView1);
            view.setText("--");
            view = (TextView) findViewById(R.id.b_rampView2);
            view.setText("--");
            view = (TextView) findViewById(R.id.b_rampView3);
            view.setText("--");
            view = (TextView) findViewById(R.id.b_rampView4);
            view.setText("--");
            view = (TextView) findViewById(R.id.b_targetTemp);
    		view.setText("Power");
    		view = (TextView) findViewById(R.id.b_targetTemp1);
    		view.setText("W");
            
    	}
        
        
    }
    /*
     * returns the no of selected block iNoOfSelectedQblock=0 no selected
     * =1 one selected example= we have 5 paired and 2 and 3 selected
     * result -> iNo=2 and iSelected[0]=2 and iSelected[1]=3
     */  
    private void getSelectedQblock(){
    	Global gs = (Global) getApplication();
    	gs.iNoOfSelectedQblock=0;
    	for (int i=0;i<8;i++){
    		if(gs.mQblockSettings.bSelectedBlock[i]){
    			gs.iSelectedQblock[gs.iNoOfSelectedQblock++]=i+1;
    		}
    	}
    	
    }
    /*
     * first check connection state:
     * if it's connected to right block refresh temp view and send read method command
     * if it's connected to wrong block disconnect and mConnectOnce=true, timer=17
     * if it's connecting to right block mConnectOnce=true, timer=8
     * if it's connecting to wrong block disconnect and mConnectOnce=true, timer=17
     * else mConnectOnce=true timer=19
     * for all above cases mKeepConnection=true, fillContentView, mEnableButton=false, enable menu qblock
     */
   
    
   /* 
   * first remove all series&renderers and then based on selected
   * graph adds the dataset and renderer
    */
    private void addGraphDataSet_Renderer(int sysConfig ,Boolean bSelectedVessel[]){
    	Global gs = (Global) getApplication();
    	List<String> types = new ArrayList<String>();;
    	mwRenderer.removeAllRenderers();
    	mwDataset.clear();
    	if(sysConfig<3){//power only
    		mwDataset.addSeries(xySeriesPower);
    		mwRenderer.addSeriesRenderer(rendererPower);
    		tempChartView = ChartFactory.getLineChartView(this, mwDataset, mwRenderer);
    	}
    	if(sysConfig>3){//ir exist so add vessels graph based on selection
    		
        	
		    	if(bSelectedVessel[0]){
		    		mwDataset.addSeries(xySeriesT1);
		    		mwRenderer.addSeriesRenderer(rendererT1);
		    		types.add(LineChart.TYPE);
		    	}
		    	
		    	if(bSelectedVessel[1]){
		    		mwDataset.addSeries(xySeriesT2);
		    		mwRenderer.addSeriesRenderer(rendererT2);
		    		types.add(LineChart.TYPE);
		    	}
		    	
		    	if(bSelectedVessel[2]){
		    		mwDataset.addSeries(xySeriesT3);
		    		mwRenderer.addSeriesRenderer(rendererT3);
		    		types.add(LineChart.TYPE);
		    	}
		    	
		    	if(bSelectedVessel[3]){
		    		mwDataset.addSeries(xySeriesT4);
		    		mwRenderer.addSeriesRenderer(rendererT4);
		    		types.add(LineChart.TYPE);
		    	}
		    	
		    	if(bSelectedVessel[4]){
		    		mwDataset.addSeries(xySeriesT5);
		    		mwRenderer.addSeriesRenderer(rendererT5);
		    		types.add(LineChart.TYPE);
		    	}
		    	
		    	if(bSelectedVessel[5]){
		    		mwDataset.addSeries(xySeriesT6);
		    		mwRenderer.addSeriesRenderer(rendererT6);
		    		types.add(LineChart.TYPE);
		    	}
		    	
		    	if(bSelectedVessel[6]){
		    		mwDataset.addSeries(xySeriesT7);
		    		mwRenderer.addSeriesRenderer(rendererT7);
		    		types.add(LineChart.TYPE);
		    	}
		    	if(bSelectedVessel[7]){
		    		mwDataset.addSeries(xySeriesT8);
		    		mwRenderer.addSeriesRenderer(rendererT8);
		    		types.add(LineChart.TYPE);
		    	}
		    	if(bSelectedVessel[8]){
		    		mwDataset.addSeries(xySeriesT9);
		    		mwRenderer.addSeriesRenderer(rendererT9);
		    		types.add(LineChart.TYPE);
		    	}
		    	if(bSelectedVessel[9]){
		    		mwDataset.addSeries(xySeriesT10);
		    		mwRenderer.addSeriesRenderer(rendererT10);
		    		types.add(LineChart.TYPE);
		    	}
		    	if(bSelectedVessel[10]){
		    		mwDataset.addSeries(xySeriesT11);
		    		mwRenderer.addSeriesRenderer(rendererT11);
		    		types.add(LineChart.TYPE);
		    	}
		    	if(bSelectedVessel[13]){
		    		mwDataset.addSeries(xySeriesT12);
		    		mwRenderer.addSeriesRenderer(rendererT12);
		    		types.add(LineChart.TYPE);
		    	}
		    	
		    	if(bSelectedVessel[12]){//add bar
		    		mwDataset.addSeries(xySeriesBar);
		    		mwRenderer.addSeriesRenderer(rendererBar);
		    		types.add(BarChart.TYPE);
		    	}
		    	if(bSelectedVessel[11]){//add power
		    		mwDataset.addSeries(xySeriesPower);
		    		mwRenderer.addSeriesRenderer(rendererPower);
		    		types.add(LineChart.TYPE);
		    	}
		    	if((sysConfig==5)||(sysConfig==7)){//add press
		    		if(bSelectedVessel[15]){//add press
			    		mwDataset.addSeries(xySeriesPress);
			    		mwRenderer.addSeriesRenderer(rendererPress);
			    		types.add(LineChart.TYPE);
		    		}
		    	}
		    	if((sysConfig==6)||(sysConfig==7)){//add opt temp
		    		if(bSelectedVessel[14]){//add opt
			    		mwDataset.addSeries(xySeriesOptTemp);
			    		mwRenderer.addSeriesRenderer(rendererOptTemp);
			    		types.add(LineChart.TYPE);
		    		}
		    	}
		    	
		    	
		    	if(types.size()==0)
		    		return;
		    	String[] types2 = new String[types.size()];
		    	for(int i=0;i<types.size();i++)
		    		types2[i]=types.get(i);
		    	
		    	if(bSelectedVessel[12]){
		    		mwRenderer.setBarSpacing(5);
		    		tempChartView = ChartFactory.getCombinedXYChartView(this, mwDataset, mwRenderer, types2);
		    	}
		    	else
		    		tempChartView = ChartFactory.getLineChartView(this, mwDataset, mwRenderer);
    	}
    	
    	LinearLayout layout = (LinearLayout) findViewById(R.id.HRchart);
    	layout.removeAllViews();	//to refresh the graph
    	layout.addView(tempChartView);
        Log.d(TAG,"chart data set and renderer was added ");
        
    }
    
    private void clearAllDateSeries(){
    	xySeriesPower.clear();
    	xySeriesPress.clear();
    	xySeriesOptTemp.clear();
    	xySeriesT1.clear();
    	xySeriesT2.clear();
    	xySeriesT3.clear();
    	xySeriesT4.clear();
    	xySeriesT5.clear();
    	xySeriesT6.clear();
    	xySeriesT7.clear();
    	xySeriesT8.clear();
    	xySeriesT9.clear();
    	xySeriesT10.clear();
    	xySeriesT11.clear();
    	xySeriesT12.clear();
    	xySeriesBar.clear();
    	for(int i=0;i<TOTALVESSEL;i++){
    		this.mVesselTemp[i]=5;
    		
    	}
    	this.mlastTime=0;
    	Log.d(TAG,"clearAllDateSeries done");
    }
    private Boolean fillContentView(){
    	//fill page with batch,welcome,main
    	
    	if(mBatchPageFlag){//batch page
    		setContentView(R.layout.main);
    		FrameLayout mylay = (FrameLayout) findViewById(R.id.frameLeft_main); 
        	mylay.setVisibility(View.INVISIBLE);
        	mylay = (FrameLayout) findViewById(R.id.frameLeft_batch); 
        	mylay.setVisibility(View.VISIBLE);
        	setBatchSpinner();
        	Log.d(TAG,"set1");
        	makeTab();
        	
        	Log.d(TAG,"set2");
        	if(this.mQblockBatch!=null){
        		addGraphDataSet_Renderer(this.mQblockBatch.mSystemConfig,this.mQblockBatch.bSelectedVessel);//adds the data series and renderers
        		
        	}
        		Log.d(TAG,"set3");
    		return true;
    	}
    	if(mDetailsFlag){//main page
    		setContentView(R.layout.main);
    		FrameLayout mylay = (FrameLayout) findViewById(R.id.frameLeft_main); 
        	mylay.setVisibility(View.VISIBLE);
        	mylay = (FrameLayout) findViewById(R.id.frameLeftDownMain); 
        	mylay.setVisibility(View.VISIBLE);
        	mylay = (FrameLayout) findViewById(R.id.frameLeft_batch); 
        	mylay.setVisibility(View.INVISIBLE);
        	mylay = (FrameLayout) findViewById(R.id.frameLeftDownManual); 
        	mylay.setVisibility(View.INVISIBLE);
        	
        	
    		if(this.bMethodValidFlag)
    			loadMethodToString(mQblockMethod);
    		makeTab();
    		Global gs = (Global) getApplication();
    		/*
    		if(gs.mQblockSettings.SystemConfig==this.mBlthService.IR_OPTTEMP){//for ir_opt
        		TextView tv=(TextView) this.findViewById(R.id.lastColTitle);
        		tv.setText("INS");
        	}
        	*/
    		if(this.mBlthService!=null){
    			mBlthService.mRxDataBufferIndexOnChart=0;
    			addGraphDataSet_Renderer(gs.mQblockSettings.SystemConfig,gs.mQblockSettings.bSelectedVessel);
    		}
    		else
    			addGraphDataSet_Renderer(gs.mQblockSettings.SystemConfig,gs.mQblockSettings.bSelectedVessel);
    		setButtonsInMain();
    		gridArray.clear();
    		this.customGridAdapter.notifyDataSetChanged();
    		return true;
    	}
    	if(mStartTimerFlag){//welcome page
    		setContentView(R.layout.title); 
    		return true;
    	}
    	
    	
    	return false;
    }
    public void refStatusViews(){
    	//refresh temp1,elapsed & estimated time,progress bar
    	Global gs = (Global) getApplication();
    	TextView view=null;
    	TextView view_degree=null;
    	TextView velapseTime=null;
    	TextView vestimateTime=null;
    	TextView vblockDescription=null;
        ProgressBar progTestTime=null;
        view_degree= (TextView) findViewById(R.id.degree);
    	
        switch(gs.iSelectedQblockCntr){
			case 0:
				view= (TextView) findViewById(R.id.temp1);
				velapseTime=(TextView) findViewById(R.id.elapsedTime1);
				vestimateTime=(TextView) findViewById(R.id.estimatedTime1);
				progTestTime=(ProgressBar) findViewById(R.id.progressBar1);
				//vblockDescription=(TextView) findViewById(R.id.tvDescription1);
				break;
		}
    	
    	if(mBlthService.mTemp==401){
    		view.setText("-- ");
    	}else if(mBlthService.mTemp > 400){ 		
    		view.setText("--");
    		}else {
    		view.setText(Integer.toString(mBlthService.mTemp));
    		progTestTime.setMax(mBlthService.mTotalTime);
    		progTestTime.setProgress(mBlthService.mTestTime);
    		showTime(velapseTime,mBlthService.mTestTime);
    		showTime(vestimateTime,mBlthService.mTotalTime);
    		if (mBlthService.mTemp <=170 ){
    			int mycolor = Color.argb(255,(int) (mBlthService.mTemp * 1.5), 20 + (int) (mBlthService.mTemp * 0.1), (255-(int)(mBlthService.mTemp* 1.5)));
       			view.setTextColor( mycolor);		  
    			view_degree.setTextColor(mycolor);
		    	
    		}else if(mBlthService.mTemp >170 && mBlthService.mTemp <= 400){
       			int mycolor = Color.argb(255,(int) (255), 50 + (int) (mBlthService.mTemp * 0.3), (0));
       			view.setTextColor( mycolor);		  
		    	view_degree.setTextColor(mycolor);
    			}else{
    			view.setTextColor(getResources().getColor(R.color.Black));
		    	view_degree.setTextColor(getResources().getColor(R.color.Black));
    			}
    		}
    	}
    public void refProg(){
    	ProgressBar progTestTime=(ProgressBar) findViewById(R.id.progressBar1);
    	progTestTime.setMax(mBlthService.mTotalTime);
    	progTestTime.setProgress(mBlthService.mTestTime);
    }
    public void refTemp(){
    	TextView viewPower= (TextView) findViewById(R.id.power1);
    	viewPower.setText(Integer.toString(mBlthService.mPower));
    	TextView viewVessel= (TextView) findViewById(R.id.vesNo1);
    	viewVessel.setText(Integer.toString(mBlthService.mVesselNo));
    	TextView view= (TextView) findViewById(R.id.temp1);
    	TextView view_degree= (TextView) findViewById(R.id.degree);
    	loadxySeriesBar(mBlthService.mVesselNo,mBlthService.mTemp);
    	aliRepaint();
    	if(mBlthService.mTemp==401){
    		view.setText("--");
    	}else if((mBlthService.mTemp > 400)||(mBlthService.mTemp < TEMP_MINIMUM)){ 		
    		view.setText("--");
    	}else {
    		view.setText(Integer.toString(mBlthService.mTemp));
    		if (mBlthService.mTemp <=170 ){
    			int mycolor = Color.argb(255,(int) (mBlthService.mTemp * 1.5), 20 + (int) (mBlthService.mTemp * 0.1), (255-(int)(mBlthService.mTemp* 1.5)));
       			view.setTextColor( mycolor);		  
    			//view.setTextColor(getResources().getColor(R.color.blue));
		    	view_degree.setTextColor(mycolor);
		    	
    		}else if(mBlthService.mTemp >170 && mBlthService.mTemp <= 400){
       			int mycolor = Color.argb(255,(int) (255), 50 + (int) (mBlthService.mTemp * 0.3), (0));
       			view.setTextColor( mycolor);		  
		    	view_degree.setTextColor(mycolor);
    		}else{
    			view.setTextColor(getResources().getColor(R.color.Black));
		    	view_degree.setTextColor(getResources().getColor(R.color.Black));
    		}
    	}
 
    }
    public void refRecipeName(){
    	TextView view= (TextView) findViewById(R.id.digestion_recipe);
    	view.setText(mRecipeName);
    }
    public void disableAll(){
    	TextView view= (TextView) findViewById(R.id.btSendMethod);
    	view.setEnabled(false);
    	view= (TextView) findViewById(R.id.btStartMethod);
    	view.setEnabled(false);
    	view= (TextView) findViewById(R.id.btStopMethod);
    	view.setEnabled(false);
    }
    public void enableDisButton(){
    	if(!mEnableButton){
    		disableAll();
        	return;
    	}
    	if((mBlthService.mRunMode==METHOD_RUNNING)||(mBlthService.mRunMode==RUNPRESCAL)){
    		TextView view= (TextView) findViewById(R.id.btSendMethod);
        	view.setEnabled(false);
        	view= (TextView) findViewById(R.id.btStartMethod);
        	view.setEnabled(false);
        	view= (TextView) findViewById(R.id.btStopMethod);
        	view.setEnabled(true);
    	}
    	else if(mBlthService.mRunMode>4){//METHOD_COMPLETED=5,METHOD_STOPED=6,METHODTERMINATED=	7;
    	    //METHODIRCALCOMPLETED= 8;IRFAILED=9;VESSEL_COUNT_FAILED=10;
    		TextView view= (TextView) findViewById(R.id.btSendMethod);
        	view.setEnabled(true);
        	view= (TextView) findViewById(R.id.btStartMethod);
        	view.setEnabled(true);
        	view= (TextView) findViewById(R.id.btStopMethod);
        	view.setEnabled(false);
    	}
    	
    	else {
    		TextView view= (TextView) findViewById(R.id.btSendMethod);
        	view.setEnabled(true);
        	view= (TextView) findViewById(R.id.btStartMethod);
        	view.setEnabled(true);
        	view= (TextView) findViewById(R.id.btStopMethod);
        	view.setEnabled(false);
    	}
    	TextView view= (TextView) findViewById(R.id.btStartMethod);
    	if(this.mBlthService.bInterlock)//if emergency power activated disable start but.
    		view.setEnabled(false);
    	
    }
    public void shrinkData(XYSeries myxySeries){
    	
    	if(myxySeries.getItemCount()>300){
    		Log.d(TAG,"length of xyseries before="+myxySeries.getItemCount());
    		int shrinkNo=(myxySeries.getItemCount()/300)+1;
    		XYSeries xys=new XYSeries(myxySeries.getTitle());
    		for(int i=0;i<myxySeries.getItemCount();i+=shrinkNo){
    			xys.add(myxySeries.getX(i),myxySeries.getY(i));
    		}
    		myxySeries.clear();
    		for(int i=0;i<xys.getItemCount();i++){
    			myxySeries.add(xys.getX(i),xys.getY(i));
    		}
    		Log.d(TAG,"length of xyseries after="+myxySeries.getItemCount());
    	}
    	
    }
    public void checkXYSeriesLength(XYSeries myxySeries){
    	
    	if(myxySeries.getItemCount()>300){
    		XYSeries xys=new XYSeries(myxySeries.getTitle());
    		for(int i=0;i<myxySeries.getItemCount();i+=4){
    			xys.add(myxySeries.getX(i),myxySeries.getY(i));
    		}
    		myxySeries.clear();
    		for(int i=0;i<xys.getItemCount();i++){
    			myxySeries.add(xys.getX(i),xys.getY(i));
    		}
    		Log.d(TAG,"length of xyseries after="+myxySeries.getItemCount());
    	}
    	
    }
    public void addPowerToChart(int mTime,short power)
    {
    	GridDataModel gvdata=new GridDataModel();
        gvdata.setTime(mTime);
        gvdata.setPower(power*10);
        gridArray.add(gvdata);
       	xySeriesPower.add((double)mTime,(double)power*10);
    }
    public void addTempToChart(int mTime,short temp,short vesNo,short power)
    {
    	GridDataModel gvdata=new GridDataModel();
        gvdata.setTime(mTime);
        gvdata.setPower(power*10);
        gvdata.setTemp(vesNo, temp);
        if(mTime>(mlastTime+9)){//add(save) power every 10second
			mlastTime=mTime;
			xySeriesPower.add((double)mTime,(double)power);
    	}
        if(temp<TEMP_MINIMUM)
        	return;
        switch(vesNo){
    	case 0:
    		
    		break;
    	case 1:
    			xySeriesT1.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		
    		break;
    	case 2:
    			xySeriesT2.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 3:
    			xySeriesT3.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 4:
    			xySeriesT4.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 5:
    			xySeriesT5.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 6:
    			xySeriesT6.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 7:
    			xySeriesT7.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 8:
    			xySeriesT8.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 9:
    			xySeriesT9.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 10:
    			xySeriesT10.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;	
    	case 11:
    			xySeriesT11.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;	
    	case 12:
			xySeriesT12.add((double)mTime,(double)temp);
			gridArray.add(gvdata);
		break;	
	
    	default:
    		return;
    	}
        

    }
    public void addTempPresToChart(int mTime,short temp,short vesNo,short power,short pres,Boolean bSelectedVessel[])
    {
    	GridDataModel gvdata=new GridDataModel();
    	gvdata.setTime(mTime);
        gvdata.setPower(power*10);
        gvdata.setTemp(vesNo, temp);
        if(bSelectedVessel[15])
        	gvdata.setPress(pres*10);
        if(mTime>(mlastTime+9)){//add(save) power every 10second
			mlastTime=mTime;
			xySeriesPower.add((double)mTime,(double)power);
			if(bSelectedVessel[15])
				xySeriesPress.add((double)mTime,(double)(pres*10));
			
    	}
        if(temp<TEMP_MINIMUM)
        	return;
        switch(vesNo){
    	case 0:
    		
    		break;
    	case 1:
    			xySeriesT1.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		
    		break;
    	case 2:
    			xySeriesT2.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 3:
    			xySeriesT3.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 4:
    			xySeriesT4.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 5:
    			xySeriesT5.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 6:
    			xySeriesT6.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 7:
    			xySeriesT7.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 8:
    			xySeriesT8.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 9:
    			xySeriesT9.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 10:
    			xySeriesT10.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;	
    	case 11:
    			xySeriesT11.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;	
    	case 12:
				xySeriesT12.add((double)mTime,(double)temp);
				gridArray.add(gvdata);
		break;	
	
    	default:
    		return;
    	}
        

    }
    public void addTempOptToChart(int mTime,short temp,short vesNo,short power,short optTemp,Boolean bSelectedVessel[])
    {
    	GridDataModel gvdata=new GridDataModel();
    	gvdata.setTime(mTime);
        gvdata.setPower(power*10);
        gvdata.setTemp(vesNo, temp);
        if((optTemp!=NO_INSITU_TEMP)&& bSelectedVessel[14])
        	gvdata.setOptTemp(optTemp);
        if(mTime>(mlastTime+9)){//add(save) power every 10second
			mlastTime=mTime;
			xySeriesPower.add((double)mTime,(double)power);
			if((optTemp!=NO_INSITU_TEMP)&& bSelectedVessel[14])
				xySeriesOptTemp.add((double)mTime,(double)(optTemp));
			
    	}
        if(temp<TEMP_MINIMUM)
        	return;
        switch(vesNo){
    	case 0:
    		
    		break;
    	case 1:
    			xySeriesT1.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		
    		break;
    	case 2:
    			xySeriesT2.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 3:
    			xySeriesT3.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 4:
    			xySeriesT4.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 5:
    			xySeriesT5.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 6:
    			xySeriesT6.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 7:
    			xySeriesT7.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 8:
    			xySeriesT8.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 9:
    			xySeriesT9.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 10:
    			xySeriesT10.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;	
    	case 11:
    			xySeriesT11.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;
    	case 12:
			xySeriesT12.add((double)mTime,(double)temp);
			gridArray.add(gvdata);
		break;	
    	default:
    		return;
    	}
        
    }
        public void addTempOptPresToChart(int mTime,short temp,short vesNo,short power,short optTemp,short pres,Boolean bSelectedVessel[])
        {
        	GridDataModel gvdata=new GridDataModel();
        	gvdata.setTime(mTime);
            gvdata.setPower(power*10);
            gvdata.setTemp(vesNo, temp);
            if((optTemp!=NO_INSITU_TEMP)&& bSelectedVessel[14])
            	gvdata.setOptTemp(optTemp);
            if(bSelectedVessel[15])
            	gvdata.setPress(pres*10);
            if(mTime>(mlastTime+9)){//add(save) power every 10second
    			mlastTime=mTime;
    			xySeriesPower.add((double)mTime,(double)power);
    			if((optTemp!=NO_INSITU_TEMP)&& bSelectedVessel[14])
    				xySeriesOptTemp.add((double)mTime,(double)(optTemp));
    			if(bSelectedVessel[15])
    				xySeriesPress.add((double)mTime,(double)(pres*10));
        	}
            if(temp<TEMP_MINIMUM)
            	return;
            switch(vesNo){
        	case 0:
        		
        		break;
        	case 1:
        			xySeriesT1.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		
        		break;
        	case 2:
        			xySeriesT2.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		break;
        	case 3:
        			xySeriesT3.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		break;
        	case 4:
        			xySeriesT4.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		break;
        	case 5:
        			xySeriesT5.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		break;
        	case 6:
        			xySeriesT6.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		break;
        	case 7:
        			xySeriesT7.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		break;
        	case 8:
        			xySeriesT8.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		break;
        	case 9:
        			xySeriesT9.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		break;
        	case 10:
        			xySeriesT10.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		break;	
        	case 11:
        			xySeriesT11.add((double)mTime,(double)temp);
        			gridArray.add(gvdata);
        		break;
        	case 12:
    			xySeriesT12.add((double)mTime,(double)temp);
    			gridArray.add(gvdata);
    		break;	
        	default:
        		return;
        	}
            
    }
    public void changeStepColor(int stepNo){
    	//LinearLayout li=null;
    	TableRow tr=null;
    	if((mBlthService.mRunMode!=METHOD_RUNNING)&&(mBlthService.mRunMode!=RUNPRESCAL))
    		return;
    	switch(stepNo){
    	case 0:
    		tr=(TableRow)findViewById(R.id.TableRecipeRow2);
    		break;
    	case 1:
    		tr=(TableRow)findViewById(R.id.TableRecipeRow3);
    		break;
    	case 2:
    		tr=(TableRow)findViewById(R.id.TableRecipeRow4);
    		break;
    	case 3:
    		tr=(TableRow)findViewById(R.id.TableRecipeRow5);
    		break;
    	default:
    		return;
    	}
    	if(tr!=null)
    		tr.setBackgroundColor(getResources().getColor(R.color.MidiumBackground));
    }
    public void showTime(TextView v,int time){
    	if (time<0)
    		return;
    	int h,m,s;
    	h=time/3600;
    	m=(time-(h*3600))/60;
    	s=time-((h*3600)+(m*60));
    	v.setText(h+":"+m+":"+s);
    }
    public void refViewsBatch(){
    	if((mBatchPageFlag==false) || (mQblockBatch==null))
    		return;
    	Global gs = (Global) getApplication();
    	TextView tv=(TextView) this.findViewById(R.id.lastColTitle);
    	/*
    	 if(mQblockBatch.mSystemConfig==this.mBlthService.IR_OPTTEMP) //for ir_opt
    		tv.setText("INS");
    	else
    		tv.setText("PRS");
    	*/
    	tv=(TextView) findViewById(R.id.tvOrganisation);
    	tv.setText(gs.mQblockSettings.mOrganization);
    	
    	tv=(TextView) findViewById(R.id.start_time);
    	tv.setText(this.mQblockBatch.mQblockMethod.mBatchTime);
    	
    	tv=(TextView) findViewById(R.id.chemist_name);
    	tv.setText(this.mQblockBatch.mChemistName);
    	
    	tv=(TextView) findViewById(R.id.batch_note);
    	tv.setText("power="+this.mQblockBatch.mTestTotalUsedPower+this.mQblockBatch.mNote);
    	
    	tv=(TextView) findViewById(R.id.recipe_name_batch);
    	tv.setText(this.mQblockBatch.mQblockMethod.mRecipeName);
    	
    	tv=(TextView) findViewById(R.id.sample_name);
    	tv.setText(this.mQblockBatch.mQblockMethod.mSampleType);
    	
    	gridArray.clear();
    	this.customGridAdapter.notifyDataSetChanged();
    	clearAllDateSeries();
    	addGraphDataSet_Renderer(this.mQblockBatch.mSystemConfig,this.mQblockBatch.bSelectedVessel);//adds the data series and renderers
    	
		int ymax=0;
    	mwRenderer.setXAxisMin(0);
        mwRenderer.setYAxisMin(0);
        if(mQblockBatch.mRxDataBuffer[0].length>1)
        {
        		this.mlastTime=0;
        		int yaxis=0;//power only
        		if(mQblockBatch.mSystemConfig>3){
        			yaxis=1;//ir temp
        		}
        		for(int i=0;i<mQblockBatch.mRxDataBuffer[0].length;i++){//add data to chart
					
		    		if(ymax<(short)(mQblockBatch.mRxDataBuffer[yaxis][i] & 0xff)){//find max y amount
		    			ymax=(short)(mQblockBatch.mRxDataBuffer[yaxis][i] & 0xff);
			    		setYMax(2,ymax);
		    			
		    		}
		    		if(mQblockBatch.mSystemConfig<3){//power only
		    			addPowerToChart((i+1)*10,(short)(mQblockBatch.mRxDataBuffer[0][i] & 0xff));
		    		}
		    		else if(mQblockBatch.mSystemConfig==this.mBlthService.IR_OPTTEMP){//for ir_opt datalength=6
		    			addTempOptToChart((short)((mQblockBatch.mRxDataBuffer[3][i] & 0xff) | (mQblockBatch.mRxDataBuffer[4][i]<<8 )),
		    				(short)(mQblockBatch.mRxDataBuffer[1][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[2][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[0][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[5][i] & 0xff),
		    				mQblockBatch.bSelectedVessel);
		    		}
		    		else if(mQblockBatch.mSystemConfig==this.mBlthService.IR_PRES){//for ir_pres datalength=6
		    			addTempPresToChart((short)((mQblockBatch.mRxDataBuffer[3][i] & 0xff) | (mQblockBatch.mRxDataBuffer[4][i]<<8 )),
		    				(short)(mQblockBatch.mRxDataBuffer[1][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[2][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[0][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[5][i] & 0xff),
		    				mQblockBatch.bSelectedVessel);
		    		}
		    		else if(mQblockBatch.mSystemConfig==this.mBlthService.IR_PRES_OPTTEMP){//for ir_pres_opt datalength=7
		    			addTempOptPresToChart((short)((mQblockBatch.mRxDataBuffer[3][i] & 0xff) | (mQblockBatch.mRxDataBuffer[4][i]<<8 )),
		    				(short)(mQblockBatch.mRxDataBuffer[1][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[2][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[0][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[5][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[6][i] & 0xff),
		    				mQblockBatch.bSelectedVessel);
		    		}
		    		else {//default is ir_only datalength=5
		    			addTempToChart((short)((mQblockBatch.mRxDataBuffer[3][i] & 0xff) | (mQblockBatch.mRxDataBuffer[4][i]<<8 )),
		    				(short)(mQblockBatch.mRxDataBuffer[1][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[2][i] & 0xff),
		    				(short)(mQblockBatch.mRxDataBuffer[0][i] & 0xff));
		    		}
		    		
				}
		    	setXMax(2);
        }
    	LinearLayout layout = (LinearLayout) findViewById(R.id.HRchart);
    	layout.removeAllViews();	//to refresh the graph
    	layout.addView(tempChartView);
    	
    	loadMethodToStringBatch(mQblockBatch.mQblockMethod);
    }
    public void refViewsDetail(){
    	Global gs = (Global) getApplication();
    	if(mDetailsFlag==false||mBatchPageFlag)
    		return;
    	Log.d(TAG,"***********************************refviewdetail");
    	/*if(this.bToggle){
    		Log.d(TAG,"-------- true-----------");
    		
    		this.bToggle=false;
    	}
    	else{
    		this.bToggle=true;
    		refTemp();
    		Log.d(TAG,"-------- false----------");
    	}
    	*/
    	refTemp();
		refStatus();
		if(mManualPageFlag)// in manual page return
			return;
		
		refProg();
		//--------------------------------- refresh message line ------------------------
		//--------------------------------- refresh message line ------------------------
		TextView view= (TextView) findViewById(R.id.digestion_recipe);
		if((mBlthService.mRunMode==METHOD_RUNNING)||(mBlthService.mRunMode==RUNPRESCAL)){
			int yend=(int) 	this.mwRenderer.getYAxisMax();
			if(yend<(int)mBlthService.mTemp){
				yend=(int)mBlthService.mTemp/50;
				setYMax(1,yend);
				
			}
			
			//--------  method is running get the right message   ----------------------------------
			String st1="";
			
			if(this.mBlthService.bPauseFlag){
				if(this.mBlthService.bDoor)
					st1="Door Open";
				if(!this.mBlthService.bMotorSW)
					st1="Carousel off";
				if(this.mBlthService.bInterlock)
					st1="Inter Lock";
				if(!this.mBlthService.bSyncOnLineFlag)
					st1="Zero Cross";
				view.setTextColor(getResources().getColor(R.color.DarkRed));
				view.setText("!Digestion Paused:"+st1);
			}
			else if(!this.mBlthService.bOptVesDetFlag){//ovd is not having interrupt
				st1="Optical Vessel Detect Failed!";
				view.setTextColor(getResources().getColor(R.color.DarkRed));
				view.setText("!Digestion Paused:"+st1);
			}
			else{
				if(this.mBlthService.bRampHoldFlag)
					st1=" Hold Time";
				else
					st1=" Ramp Time";
				view.setTextColor(getResources().getColor(R.color.DarkBlue));
				view.setText("!!!Digesting:Step="+(this.mBlthService.mCurStep+1)+st1);
			}
			
			Log.d(TAG,"this is softstatus="+this.mBlthService.mSoftStatus+"  status1="+this.mBlthService.mStatus1
					+"   status2="+this.mBlthService.mStatus2);
				
				
		}
		else if(this.mBlthService.mRunMode==METHODTERMINATED){
			view.setText("Dig. Stopped,Emergency Power Off");
		}
		
		else if(this.mBlthService.mRunMode==METHOD_COMPLETED){
			view.setText("Digestion Completed");
		}
		else if(this.mBlthService.mRunMode==METHOD_STOPED){
			view.setText("Digestion Stopped");
		}
		else if(this.mBlthService.mRunMode==MAG_ON_MANUAL){
			view.setText("MicroWave On Manual");
		}
		else if(this.mBlthService.mRunMode==VESSEL_COUNT_FAILED){
			view.setText("Detecting ves#"+this.mBlthService.mFailedIRVesselNo+" Failed");
		}
		else if(this.mBlthService.mRunMode==ZERO_CROSS_FAILED){
			view.setText("Dig. Stopped,Zero Cross Failed");
		}
		else if(this.mBlthService.mRunMode==OPT_VES_DET_FAIED){
			view.setText("Dig. Stopped,Opt_Sensor Failed");
		}
		else if(this.mBlthService.mRunMode==POWER_TIME_PROTECT){
			view.setText("Dig. Stopped,Power Time Limit");
		}
		else if(this.mBlthService.mRunMode==PRES_CAL_COMPLETED){
			view.setText("Pressure Calibration Completed");
		}
		else if(this.mBlthService.mRunMode==DELTA_T_FAILED){
			view.setText("Dig. Stopped,Delta T Limit Failed");
		}
		else if(this.mBlthService.mRunMode==OVD_KEY_FAIED){
			view.setText("Dig. Stopped,Optical key not found");
		}
		//--------------------------------- refresh message line done -------------------
		/* temp
		int tempHi,tempLo;
		if(this.mBlthService.mVesselHi>3)
			tempHi=this.mBlthService.mVesselHi+50;
		else
			tempHi=50;
		if(this.mBlthService.mVesselLo>3)
			tempLo=this.mBlthService.mVesselLo+50;
		else
			tempLo=50;
		
		if((this.mBlthService.mVesselNo<12)&&(this.mBlthService.mVesselNo>0)){
    		
    		mVesselHi[this.mBlthService.mVesselNo-1]=(short) tempHi;
    		mVesselLo[this.mBlthService.mVesselNo-1]=(short) tempLo;
    	}
		*/
		if(this.mBlthService.bIRFailedFlag&&!this.bDialogIRFail){
			this.bDialogIRFail=true;
			this.showDialogIRFailAlert();
		}
		TextView tv=(TextView) findViewById(R.id.elapsedTime);
		showTime(tv,mBlthService.mTestTime);
		changeStepColor(mBlthService.mCurStep);
		tv=(TextView) findViewById(R.id.estimatedTime);
		showTime(tv,mBlthService.mTotalTime);
		enableDisButton();
    }
  //--------------------------------- refresh status images  ---------------------------
    public void refStatus(){
    	ImageView imageview =(ImageView)findViewById(R.id.ivExhaust);
    	if(this.mBlthService.bExhaust){
    		imageview.setImageResource(R.drawable.on_state);
    	}
    	else{
    		imageview.setImageResource(R.drawable.off_state);
    	}
    	
    	imageview =(ImageView)findViewById(R.id.ivDoor);
    	if(this.mBlthService.bDoor){
    		imageview.setImageResource(R.drawable.on_state);
    	}
    	else{
    		imageview.setImageResource(R.drawable.off_state);
    	}
    	
    	imageview =(ImageView)findViewById(R.id.ivInterlock);
    	if(this.mBlthService.bInterlock){
    		imageview.setImageResource(R.drawable.on_state);
    	}
    	else{
    		imageview.setImageResource(R.drawable.off_state);
    	}
    	
    	imageview =(ImageView)findViewById(R.id.ivMotor);
    	if(this.mBlthService.bMotorEnable){
    		imageview.setImageResource(R.drawable.on_state);
    	}
    	else{
    		imageview.setImageResource(R.drawable.off_state);
    	}
    	
    	imageview =(ImageView)findViewById(R.id.ivMW);
    	if(this.mBlthService.bMagLo&this.mBlthService.bMagHi){
    		imageview.setImageResource(R.drawable.on_state);
    	}
    	else{
    		imageview.setImageResource(R.drawable.off_state);
    	}
    	
    	imageview =(ImageView)findViewById(R.id.ivComm);
    	if(bCommunicationFlag){
    		bCommunicationFlag=false;
    		imageview.setImageResource(R.drawable.com_1);
    	}
    	else{
    		bCommunicationFlag=true;
    		imageview.setImageResource(R.drawable.com_2);
    	}
    }
  //--------------------------------- refresh status images  done ---------------------------
    
    
    // 	input=mQblockMethod.mReagentVolume ,mQblockMethod.mVesselNo
    //	output=gs.mQblockSettings.VesselVolume ,gs.mQblockSettings.bSelectedVessel[] array
    // 	is called after sending method to microwave to change settings
    void loadVesPosVolToSetting(){
    	Global gs = (Global) getApplication();
    	gs.mQblockSettings.VesselVolume=0;
    	if(this.mQblockMethod.mReagentVolume!=null){
    	for(int i=0;i<5;i++)
    		gs.mQblockSettings.VesselVolume+=this.mQblockMethod.mReagentVolume[i];
    	}
    	if(gs.mQblockSettings.VesselVolume<5)//limit the min vol to 5ml
    		gs.mQblockSettings.VesselVolume=5;
    	if(gs.mQblockSettings.VesselVolume>40)//limit the max vol to 40ml
    		gs.mQblockSettings.VesselVolume=40;
    	
    	loadVesNoToPosition(this.mQblockMethod.mVesselNo,gs.mQblockSettings.bSelectedVessel);
    	gs.SaveSettings();
        Log.d(TAG,"volume="+gs.mQblockSettings.VesselVolume+"   vessel no= "+this.mQblockMethod.mVesselNo);	
        
    }
    
    
    // 	input:	vesNo 
    //	output: set/reset bSelectedVessel[] array based on vesNo
    void loadVesNoToPosition(short vesNo,Boolean bSelectedVessel[] ){
    	for(int i=0;i<(TOTALVESSEL-1);i++)
			bSelectedVessel[i]=false;
    	bSelectedVessel[13]=false;	//ves12=false
    	
    	switch	(vesNo){
        	case	1:
        		bSelectedVessel[0]=true;//ves=1
        	break;	
        	case	2:
        		bSelectedVessel[0]=true;//ves=1
        		bSelectedVessel[6]=true;//ves=7
        	break;
        	case	3:
        		bSelectedVessel[1]=true;//ves=2 1
        		bSelectedVessel[5]=true;//ves=6 5
        		bSelectedVessel[9]=true;//ves=10 9
        	break;
        	case	4:
        		bSelectedVessel[0]=true;//ves=1
        		bSelectedVessel[3]=true;//ves=4 3 4
        		bSelectedVessel[6]=true;//ves=7 6 7
        		bSelectedVessel[9]=true;//ves=10 9 10
        	break;
        	case	5:
        		bSelectedVessel[0]=true;//ves=1 2
        		bSelectedVessel[2]=true;//ves=3 4
        		bSelectedVessel[5]=true;//ves=6
        		bSelectedVessel[7]=true;//ves=8
        		bSelectedVessel[9]=true;//ves=10
        		
        	break;
        	case	6:
        		bSelectedVessel[1]=true;//ves=2 1
        		bSelectedVessel[3]=true;//ves=4 3 2 3
        		bSelectedVessel[5]=true;//ves=6 5 4 5
        		bSelectedVessel[7]=true;//ves=8 7 6 7
        		bSelectedVessel[9]=true;//ves=10 9 8 9
        		bSelectedVessel[13]=true;//ves=12 11 10 11
        	break;
        	case	7:
        		bSelectedVessel[0]=true;//ves=1
        		bSelectedVessel[2]=true;//ves=3
        		bSelectedVessel[3]=true;//ves=4 5
        		bSelectedVessel[5]=true;//ves=6
        		bSelectedVessel[7]=true;//ves=8
        		bSelectedVessel[8]=true;//ves=9
        		bSelectedVessel[10]=true;//ves=11
        		
        	break;
        	case	8:
        		for(int i=0;i<(TOTALVESSEL-1);i++)
        			bSelectedVessel[i]=true;
        		bSelectedVessel[2]=false;//ves=3
        		bSelectedVessel[5]=false;//ves=6 7
        		bSelectedVessel[8]=false;//ves=9 11
        		bSelectedVessel[13]=false;	//ves12=false
        		
        	break;
        	case	9:
        		for(int i=0;i<(TOTALVESSEL-1);i++)
        			bSelectedVessel[i]=true;
        		bSelectedVessel[3]=false;//ves=4
        		bSelectedVessel[7]=false;//ves=8 9
        		bSelectedVessel[13]=false;	//ves12=false
        		
        	break;	
        	case	10:
        		for(int i=0;i<(TOTALVESSEL-1);i++)
        			bSelectedVessel[i]=true;
        		bSelectedVessel[4]=false;//ves=5 6
        		bSelectedVessel[10]=false;	//ves11
        		bSelectedVessel[13]=true;	//ves12=true
        		
        	break;
        	case	11:
        		for(int i=0;i<(TOTALVESSEL-1);i++)
        			bSelectedVessel[i]=true;
        		bSelectedVessel[13]=false;	//ves12=false
        	break;
        	case	12:
        		for(int i=0;i<(TOTALVESSEL-1);i++)
        			bSelectedVessel[i]=true;
        		bSelectedVessel[13]=true;	//ves12=true
        	break;
			default:
			break;
    	}
    }
    Boolean openRecipe(String st) {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        File path = Environment.getExternalStoragePublicDirectory("questron\\MW\\settings");
        File file = new File(path, st);
        try  {  
    		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));  
    		Object o = ois.readObject();
    		mQblockMethod=(QBlockMethod) o;
    		Log.d(TAG,"this is openening of recipe "+mQblockMethod.TarTemp[0]);
    		return true;
        }
        catch(Exception ex){  

    		Log.v("not qblock method",ex.getMessage());
    		ex.printStackTrace(); 
    		return false;
    	} 
        //file.delete();
    }
    public Boolean saveRecipe(){
    	File path=Environment.getExternalStoragePublicDirectory("questron\\MW\\settings");
    	//File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
    	String st=mConnectedDeviceAddress;
    	   
    	try {        // Make sure the Pictures directory exists.        
    		path.mkdirs();  
    		File file = new File(path,st); 
    		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));//new File("save.bin"))); //Select where you wish to save the file...
			//ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.bin"))); //Select where you wish to save the file...
			oos.writeObject(mQblockMethod);//mQblockSettings); // write the class as an 'object' 
			oos.flush(); // flush the stream to insure all of the information was written to 'save.bin' 
			oos.close();
			/*
			// Tell the media scanner about the new file so that it is        
			// immediately available to the user.        
			MediaScannerConnection.scanFile(this,               
					new String[] { file.toString() }, null,               
					new MediaScannerConnection.OnScanCompletedListener() {            
				public void onScanCompleted(String path, Uri uri) {               
					Log.i("ExternalStorage", "Scanned " + path + ":");               
					Log.i("ExternalStorage", "-> uri=" + uri);           
					}        
				});
				*/
			//bSettingsChanged=false;

			//this.loadFileList();
			return true;
			}
			catch(Exception ex) {  

				Log.v("Address Book",ex.getMessage());  
				ex.printStackTrace();  
				return false;
			}  
    }
    public void saveBatchTime(String bTime){
    	openRecipe(this.mConnectedDeviceAddress);
    	mQblockMethod.mBatchTime=bTime;
    	loadMethodToString(mQblockMethod);
    	saveRecipe();
		Toast.makeText(this, "The batch starting data saved ...", Toast.LENGTH_LONG).show();
		Log.d(TAG,"saveBatchTime done");
    }
    Boolean openBatch(String st) {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        File path = Environment.getExternalStoragePublicDirectory("questron\\MW\\batches");
        File file = new File(path, st);
        try  {  
    		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));  
    		Object o = ois.readObject();
    		mQblockBatch=(QBlockBatch) o;
    		if(D) Log.d("TAG","this is open"+mQblockMethod.TarTemp[0]);
    		
    		return true;
        }
        catch(Exception ex){  

    		Log.v("qblock method",ex.getMessage());
    		ex.printStackTrace(); 
    		
    		return false;
    	} 
    }
    public Boolean saveBatchText(String st1){
    	File path=Environment.getExternalStoragePublicDirectory("questron\\MW\\Digestion Batches");
    
	    String filename = st1 + ".txt";
	    String st ="Batch Name,"+st1+"\r\n";
	    FileOutputStream outputStream;

	    try {
	    	path.mkdirs();
	    	File file = new File(path,filename); 
	      outputStream = new FileOutputStream(file);
	      outputStream.write(st.getBytes());
	      st="Qlab Pro,"+this.mQblockBatch.mBlockName+"\r\n";
	      outputStream.write(st.getBytes());
	      st="Batch Time,"+this.mQblockBatch.mQblockMethod.mBatchTime+"\r\n";
	      outputStream.write(st.getBytes());
	      st="Chemist Name,"+this.mQblockBatch.mChemistName+"\r\n";
	      outputStream.write(st.getBytes());
	      st="Batch Note,"+this.mQblockBatch.mNote+"\r\n";
	      outputStream.write(st.getBytes());
	      st="Recipe Name,"+this.mQblockBatch.mQblockMethod.mRecipeName+"\r\n";
	      outputStream.write(st.getBytes());
	      st="Sample Type,"+this.mQblockBatch.mQblockMethod.mSampleType+"\r\n";
	      outputStream.write(st.getBytes());
	      
	      st="Recipe Time,"+this.mQblockBatch.mQblockMethod.mRecipeTime+"\r\n";
	      outputStream.write(st.getBytes());
	      st="Reagents,"+this.mQblockBatch.mQblockMethod.mReagents+"\r\n\r\n";
	      outputStream.write(st.getBytes());
	     
	      if(this.mQblockBatch.mQblockMethod.Type==0){
		      st="Step1:Target temp.,"+Integer.toString(this.mQblockBatch.mQblockMethod.TarTemp[0])+"\t"+
		    		  "Ramp time,"+Integer.toString(this.mQblockBatch.mQblockMethod.RampTime[0])+"\t"+
		    		  "Hold time,"+Integer.toString(this.mQblockBatch.mQblockMethod.HoldTime[0])+"\r\n";
		      outputStream.write(st.getBytes());
		      st="Step2:Target temp.,"+Integer.toString(this.mQblockBatch.mQblockMethod.TarTemp[1])+"\t"+
		    		  "Ramp time,"+Integer.toString(this.mQblockBatch.mQblockMethod.RampTime[1])+"\t"+
		    		  "Hold time,"+Integer.toString(this.mQblockBatch.mQblockMethod.HoldTime[1])+"\r\n";
		      outputStream.write(st.getBytes());
		      st="Step3:Target temp.,"+Integer.toString(this.mQblockBatch.mQblockMethod.TarTemp[2])+"\t"+
		    		  "Ramp time,"+Integer.toString(this.mQblockBatch.mQblockMethod.RampTime[2])+"\t"+
		    		  "Hold time,"+Integer.toString(this.mQblockBatch.mQblockMethod.HoldTime[2])+"\r\n";
		      outputStream.write(st.getBytes());
		      st="Step4:Target temp.,"+Integer.toString(this.mQblockBatch.mQblockMethod.TarTemp[3])+"\t"+
		    		  "Ramp time,"+Integer.toString(this.mQblockBatch.mQblockMethod.RampTime[3])+"\t"+
		    		  "Hold time,"+Integer.toString(this.mQblockBatch.mQblockMethod.HoldTime[3])+"\r\n\r\n";
		      outputStream.write(st.getBytes());
	      }
	      else{
	    	  st="Step1:Target power,"+Integer.toString(this.mQblockBatch.mQblockMethod.TarTemp[0])+"\t"+
		    		  
		    		  "Hold time,"+Integer.toString(this.mQblockBatch.mQblockMethod.HoldTime[0])+"\r\n";
		      outputStream.write(st.getBytes());
		      st="Step2:Target power,"+Integer.toString(this.mQblockBatch.mQblockMethod.TarTemp[1])+"\t"+
		    		  
		    		  "Hold time,"+Integer.toString(this.mQblockBatch.mQblockMethod.HoldTime[1])+"\r\n";
		      outputStream.write(st.getBytes());
		      st="Step3:Target power,"+Integer.toString(this.mQblockBatch.mQblockMethod.TarTemp[2])+"\t"+
		    		  
		    		  "Hold time,"+Integer.toString(this.mQblockBatch.mQblockMethod.HoldTime[2])+"\r\n";
		      outputStream.write(st.getBytes());
		      st="Step4:Target power,"+Integer.toString(this.mQblockBatch.mQblockMethod.TarTemp[3])+"\t"+
		    		  
		    		  "Hold time,"+Integer.toString(this.mQblockBatch.mQblockMethod.HoldTime[3])+"\r\n\r\n";
		      outputStream.write(st.getBytes());
	      }
	      st="Total used power(kw)="+Integer.toString(this.mQblockBatch.mTestTotalUsedPower) + "\r\n\r\n";
	      outputStream.write(st.getBytes());
	      if(this.mQblockBatch.mQblockMethod.Type==0)
	    	  st="Time"+ "\t\t"+ "Temp."+ "\t\t"+ "Vessel No."+ "\t"+ "Power(w)"+ "\r\n\r\n";
	      else
	    	  st="Time"+ "\t\t"+ "Power"+ "\r\n\r\n";
	      outputStream.write(st.getBytes());
	      
	      
	      int sec=0,min=0,hour=0,time=0;
	      for (int i=0;i<this.mQblockBatch.mRxDataBuffer[0].length;i++){
	    	  if(this.mQblockBatch.mSystemConfig>3){
	    		  time=(short)((this.mQblockBatch.mRxDataBuffer[3][i] & 0xff) | (this.mQblockBatch.mRxDataBuffer[4][i]<<8 ));
		    	  hour=time/3600;
		    	  min=(time-(hour*3600))/60;
		    	  sec=time-((hour*3600)+(min*60));
		    	  st=Integer.toString(hour)+":"+Integer.toString(min)+":"+Integer.toString(sec)+"\t\t"+
		    			  Integer.toString((int)(this.mQblockBatch.mRxDataBuffer[1][i]&0xff))+"\t\t"+
		    			  Integer.toString((int)(this.mQblockBatch.mRxDataBuffer[2][i]&0xff))+"\t\t"+
		    			  Integer.toString((int)(this.mQblockBatch.mRxDataBuffer[0][i]&0xff)*10)+"\r\n";
		    	  if((time>0)&&((int)(this.mQblockBatch.mRxDataBuffer[1][i]&0xff)>=TEMP_MINIMUM))
		    		  outputStream.write(st.getBytes());
	    	  }
	    	  else{
	    		  time=(short)((i+1)*10);
	    		  hour=time/3600;
		    	  min=(time-(hour*3600))/60;
		    	  sec=time-((hour*3600)+(min*60));
		    	  String st2=Integer.toString((int)(this.mQblockBatch.mRxDataBuffer[0][i]&0xff));
		    	  if(this.mQblockBatch.mQblockMethod.Type!=0)
		    		  st2=Integer.toString((int)(this.mQblockBatch.mRxDataBuffer[0][i]&0xff)*10);
	    		  st=Integer.toString(hour)+":"+Integer.toString(min)+":"+Integer.toString(sec)+"\t\t"+
		    			  st2+"\t\t"+
		    			  "\r\n";
	    		  if(time>0)
	    			  outputStream.write(st.getBytes());
	    	  }
	    	  
	      }
	      outputStream.close();
	      return true;
	    } catch (Exception e) {
	      e.printStackTrace();
	      return false;
	    }
    }
    public Boolean saveBatch(String st1,String st2,String st3){
    	File path=Environment.getExternalStoragePublicDirectory("questron\\MW\\batches");
    	//File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
    	Global gs = (Global) getApplication();
    	mQblockBatch=new QBlockBatch();
    	mQblockBatch.mQblockMethod=mQblockMethod;
		this.mQblockBatch.mTestTotalUsedPower=mBlthService.mTestTotalUsedPower;
		this.mQblockBatch.mSystemConfig=gs.mQblockSettings.SystemConfig;
		mBatchName=st1;//data.getStringExtra("BatchName");
		
		mQblockBatch.mNote=st2;//data.getStringExtra("BatchNote");
		mQblockBatch.mChemistName=st3;
		for(int i=0;i<16;i++){
			mQblockBatch.bSelectedVessel[i]=gs.mQblockSettings.bSelectedVessel[i];
		}
		
		mQblockBatch.mBlockName=this.mConnectedDeviceName;
		mQblockBatch.mRxDataBuffer=new byte[mBlthService.mDataLenght][mBlthService.mRxDataBufferIndex];
		for(int i=0;i<(mBlthService.mRxDataBufferIndex);i++)
			for(int j=0;j<mBlthService.mDataLenght;j++){
				mQblockBatch.mRxDataBuffer[j][i]=mBlthService.mRxDataBuffer[j][i];
			}
		if(D) Log.d(TAG,"nooooooo ="+mBlthService.mRxDataBufferIndex);
		try {        // Make sure the Pictures directory exists.        
    		path.mkdirs();  
    		File file = new File(path,st1); 
    		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));//new File("save.bin"))); //Select where you wish to save the file...
			//ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.bin"))); //Select where you wish to save the file...
			oos.writeObject(mQblockBatch);//mQblockSettings); // write the class as an 'object' 
			oos.flush(); // flush the stream to insure all of the information was written to 'save.bin' 
			oos.close();
			batches_list.add(st1);
			spinnerAdapter.notifyDataSetChanged();
			int ii=batches_list.size();
	        if(ii>0)
	        	batches_spinner.setSelection(ii-1);
			/*
			// Tell the media scanner about the new file so that it is        
			// immediately available to the user.        
			MediaScannerConnection.scanFile(this,               
					new String[] { file.toString() }, null,               
					new MediaScannerConnection.OnScanCompletedListener() {            
				public void onScanCompleted(String path, Uri uri) {               
					Log.i("ExternalStorage", "Scanned " + path + ":");               
					Log.i("ExternalStorage", "-> uri=" + uri);           
					}        
				});
			*/	
			if(saveBatchText(st1))
				Toast.makeText(this, "The Batch File Is Saved...", Toast.LENGTH_LONG).show();
			else
				return false;
			return true;
			}
			catch(Exception ex) {  

				Log.v("Address Book",ex.getMessage());  
				ex.printStackTrace();  
				return false;
			}  
    }
    public void setBatchSpinner(){
    	batches_spinner = (Spinner) findViewById(R.id.spinnerBatch);
        batches_list = new ArrayList<String>();
        //batches_list.add("null");
        fillBatchList();
        //ArrayAdapter<String> 
        spinnerAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1,batches_list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        batches_spinner.setAdapter(spinnerAdapter);
        int ii=batches_list.size();
        if(ii>0)
        	batches_spinner.setSelection(ii-1);
        batches_spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id) {
                // TODO Auto-generated method stub
            	if(D) Log.d("TAG","this is positiion"+position);
            	//if(progressDialog==null)
					//progressDialog = ProgressDialog.show(arg1.getContext(), "Opening Batch File Please Wait:", " ...");
            	//else
            		//progressDialog.show(arg1.getContext(), "Opening Batch File Please Wait:", " ...");
            	String st=batches_list.get(position);
            	if(openBatch(st)){
            		mBatchName=st;
            		Log.d(TAG,"**************************-----------------------------41");
            		
            		refViewsBatch();
            		Log.d(TAG,"**************************-----------------------------42");
            		
        		}
            	else 
            		Toast.makeText(getApplicationContext(), "Warning: The Batch is not a valid Recipe and can not be opended...", Toast.LENGTH_LONG).show();
            	//progressDialog.dismiss();
            	Log.d(TAG,"**************************-----------------------------43");
            }

               @Override
               public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub

               }

          });
        
        mDeleteBatchButton=(Button) findViewById(R.id.btDeleteBatch);
        mDeleteBatchButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        	try{
        		AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
                alert.setTitle("Delete Batch");   
                String st=batches_list.get(batches_spinner.getSelectedItemPosition());
                alert.setMessage("Do You Want to Delet Batch  "+st);   
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){
                    	String st=batches_list.get(batches_spinner.getSelectedItemPosition());
                    	deleteBatch(st);
                    	fillBatchList();
        				spinnerAdapter.notifyDataSetChanged();
                    }   
                });   
                   
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        //canceled   
                    }   
                });   
                alert.show();   
        	}
        	catch(Exception e){
        		
        	}
        		
        		
            }
        });
    }
    void startPresCalibration(){
    	TextView viewPower= (TextView) findViewById(R.id.power1);
    	AlertDialog.Builder alert = new AlertDialog.Builder(viewPower.getContext());   
        alert.setTitle("Start Pressure Calibraion");   
        alert.setMessage("Do You Want to Start Pressure Calibraion?  ");   
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
            public void onClick(DialogInterface dialog, int whichButton){
            	//start pres cal;
        		mStartPresCalFlag=true;
    			mTimer=1;
        		mBlthService.startPresCal();
    			mStartMethodButton.setEnabled(false);
    		}   
        });   
           
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
            public void onClick(DialogInterface dialog, int whichButton){   
                //canceled   
            }   
        });   
        alert.show();   
    }
    void deleteBatch(String st) {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        File path = Environment.getExternalStoragePublicDirectory("questron\\MW\\batches");
        File file = new File(path, st);
        file.delete();
    }
    private void fillBatchList(){
    	File path=Environment.getExternalStoragePublicDirectory("questron\\MW\\batches");
    	path.mkdirs();
    	File mfile=new File(path.toString());
    	File[] file_list=mfile.listFiles();
    	batches_list.clear();
    	if(file_list!=null){
    		if(D) Log.d("nooh"+file_list.length,"");
    		for( int i=0; i< file_list.length; i++)
    		{
    			if(D) Log.d("this is files"+file_list[i].getName(),"ttt");
    			batches_list.add( file_list[i].getName() );
    		}
    	}
    	
    }
    public String getAlias(String st){
    	Global gs = (Global) getApplication();
    	if(gs.mQblockSettings.alias_list==null)
    		return null;
    	
    	for(int i=0;i<gs.mQblockSettings.alias_list.size();i++){
    		if(gs.mQblockSettings.alias_list_name.get(i).equals(st))
    				return gs.mQblockSettings.alias_list.get(i);
    	}
    	return null;
    }

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
}

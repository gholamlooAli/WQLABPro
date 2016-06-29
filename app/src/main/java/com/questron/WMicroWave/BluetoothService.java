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
//ret[0] = (byte)(x & 0xff);  x is short ret is byte
//ret[1] = (byte)((x >> 8) & 0xff);
package com.questron.WMicroWave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothService {
    // Debugging
    private static final String TAG = "BlueToothChat";
    private static final String TAG2 = "tag2";
    private static final String TAG3 = "resetStatus";
    private static final boolean D = false;
    // Code for Start of Method Frame
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
    public static final short CMND_DUMMY		=1130;
    
    public static final int METHOD_READ_BYTE = 32;
    public static final int METHOD_STRT_BYTE = 4;
    public static final int CALIB_STRT_BYTE = 4;
    public static final int METHOD_STPD_BYTE = 4;
    public static final int METHOD_SEND_BYTE = 16;
    public static final int READ_STATUS_BYTE = 28;
    public static final int READ_SETTINGS_BYTE = 28;
    public static final int TEST_ROW_BYTE = 16;	//24 BYTES for every min of test
    //public static final int METHOD_RUNNING = 1;
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
    public static final int POWER_TIME_PROTECT=13;
    public static final int PRES_CAL_COMPLETED=	14;
    //     microwave model
  
	
    public static final int OPEN_POWERONLY = 1;
    public static final int CLOSED_POWERONLY = 2;
    public static final int OPEN_OPTICALTEMP=	3;//open ves & opt temp
    public static final int IRONLY  =	4;
    public static final int IR_PRES =	5;//ir & press
    public static final int IR_OPTTEMP = 6;//ir & opt temp
    public static final int IR_PRES_OPTTEMP = 7;//ir & press & opt temp
    //public static final int METHOD_SEND_STATUS = 2;
    public short  mDataLenght;
    public int    mWaitByte;	//shows the number of bytes that expect to receive	
    public short    mCmndStatus;	//shows the status in waiting for byte mode
    public short    mSentCmnd;	//shows the last sent command
    public byte[] mReadBuffer = new byte[7000*3];	//stores bytes coming from cpu
    public int    mReadBufferIndex;
    public byte[][] mRxDataBuffer ;//= new byte[this.mDataLenght][7000];	//stores bytes coming from cpu
    public int    mRxDataBufferIndex;
    public int    mRxDataBufferIndexOnChart;
   // public short  mRxDataRowNo;
    public short  mTemp;
    public int    mTestTime;
    public int    mTotalTime;
    public short  mRunMode;
    public short   mSoftStatus;
    public short  mStatus1,mStatus2,mVesselNo,mVesselHi,mVesselLo,mTestTotalUsedPower,mResetStatus,mOptTemp;
    public boolean bDoor,bMotorEnable,bMotorSW,bMotorOpt,bCool,bExhaust,bInterlock;
    public boolean bMagLo,bMagHi,bAskTestResultBigFlag=false;
    public boolean bOptVesDetFlag,bPauseFlag,bRampHoldFlag,bSyncOnLineFlag,bIRFailedFlag;
    public short  mCurStep,noOfDataCopy,noOfDataCopyPage;
    public short  mTimeInterval;
    public short  mDataIndex;
    public short  mMethodFinishMode;
    public short  mDataRowNo;
    public int    mDataTotalNo;
    public short  mTempResolution,mIRVesselNo,mFailedIRVesselNo,mPower;
    public Boolean mBatchPageFlag=false;
    private Global  mgs;
    //public int    mRxDataRowNo=0;//no of received data row from cpu
    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothChatSecure";
   
    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    // Member fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    //private AcceptThread mSecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    // timer 
    public int mTimer=0;
    public long mTimerRefDataInterval=200;
    protected Handler taskHandler = new Handler();
    public Boolean bEnableAskStatus = false;
    //public Boolean mAskTestResultBegin=false;
    /**
     * Constructor. Prepares a new BluetoothChat session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
     */
    public BluetoothService(Context context, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
        mWaitByte=0;
        mCmndStatus=0;
        mSentCmnd=0;
        mgs =(Global) context.getApplicationContext();
        this.setDataLength(mgs.mQblockSettings.SystemConfig);
       // mRunnable= new Runnable();
    }

    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
    private synchronized void setState(int state) {
       if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(WQblockBluetooth.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }
    public void setDataLength(int sysConfig){
    	if(sysConfig<3)//power only
    		this.mDataLenght=1;
    	else if	(sysConfig==OPEN_OPTICALTEMP)//open ves & opt temp
    		this.mDataLenght=2;
    	else if	(sysConfig==IR_PRES)//ir & press
    		this.mDataLenght=6;
    	else if	(sysConfig==IR_OPTTEMP)//ir & opt temp
    		this.mDataLenght=6;
    	else if	(sysConfig==IR_PRES_OPTTEMP)//ir & press & opt temp
    		this.mDataLenght=7;
    	else//default=4 IRONLY ir only
    		this.mDataLenght=5;
    	mRxDataBuffer = new byte[this.mDataLenght][7000];
    	Log.d(TAG, "bluetooth service data leng="+this.mDataLenght);
    }
    /**
     * Return the current connection state. */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume() */
    public synchronized void start() {
        //if (D) Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        setState(STATE_NONE);
        // Start the thread to listen on a BluetoothServerSocket
        /*if (mSecureAcceptThread == null) {
            mSecureAcceptThread = new AcceptThread(true);
            mSecureAcceptThread.start();
        }*/
        
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        //if (D) Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {
        if (D) Log.d(TAG, "connected, Socket Type:" + socketType);

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Cancel the accept thread because we only want to connect to one device
        /*if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }*/
        

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(WQblockBluetooth.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(WQblockBluetooth.DEVICE_NAME, device.getName());
        bundle.putString(WQblockBluetooth.DEVICE_ADDRESS, device.getAddress());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        Log.d(TAG, "bluetooth service stopped ,mstate="+mState);
        mWaitByte=0;
        mCmndStatus=0;
        mSentCmnd=0;
        //if (mState== 2) {
        if (mConnectThread != null) {	
            mConnectThread.cancel();
            if (D) Log.d(TAG, "stop connect");
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
           if(D) Log.d(TAG, "stop connected");
        }

        /*if (mSecureAcceptThread != null) {
            mSecureAcceptThread.cancel();
           if(D) Log.d(TAG, "stop accept");
            mSecureAcceptThread = null;
        }*/

       
        setState(STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
   

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
    	
    	// mmOutStream.write(buffer);
           if(D) Log.d(TAG,"connection failed");
            // Share the sent message back to the UI Activity
            mHandler.obtainMessage(WQblockBluetooth.MESSAGE_CONNECTION_FAILED, -1, -1,-1)
                    .sendToTarget();
        // Send a failure message back to the Activity
 /*       Message msg = mHandler.obtainMessage(WQblockBluetooth.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(WQblockBluetooth.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
*/
        // Start the service over to restart listening mode
        BluetoothService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        //this message was changed to next message
    	/*Message msg = mHandler.obtainMessage(WQblockBluetooth.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(WQblockBluetooth.TOAST, "Connection to Device was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
		*/
    	if(D) Log.d(TAG,"connection lost");
        // Share the sent message back to the UI Activity
        mHandler.obtainMessage(WQblockBluetooth.MESSAGE_CONNECTION_LOST, -1, -1,-1)
                .sendToTarget();
        // Start the service over to restart listening mode
        BluetoothService.this.start();
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    /*
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;

        public AcceptThread(boolean secure) {
            BluetoothServerSocket tmp = null;
            mSocketType = secure ? "Secure":"Insecure";

            // Create a new listening server socket
            try {
                if (secure) {
                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,
                        MY_UUID_SECURE);
                } 
            } catch (IOException e) {
               if(D) Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            if (D) Log.d(TAG, "Socket Type: " + mSocketType +
                    "BEGIN mAcceptThread" + this);
            setName("AcceptThread" + mSocketType);

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                   if(D) Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothService.this) {
                        switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            connected(socket, socket.getRemoteDevice(),
                                    mSocketType);
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                               if(D) Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                        }
                    }
                }
            }
            if (D) Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);

        }

        public void cancel() {
           // if (D) Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
               if(D) Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
            }
        }
    }



    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";
           if(D) Log.d(TAG,"device mame="+device.getName()+"    add="+device.getAddress()+"  bondstate="+device.getBondState());
            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                if (secure) {
                    tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_SECURE);
                    //tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                } 
            } catch (IOException e) {
               if(D) Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
           if(D) Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
            	if(D) Log.d(TAG,"mmSocket="+mmSocket);
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
            	if(D) Log.d(TAG,"connection failed"+e.toString());
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                   if(D) Log.e(TAG, "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
               if(D) Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
           if(D) Log.d(TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
               if(D) Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
           if(D) Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // *************************  Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    //Log.d(TAG,"recived byte bytes="+bytes);
                    if(mSentCmnd!=0){//METHOD_READ_STATUS){
                    	for(int i=0;i<bytes;i++)
                    		mReadBuffer[mReadBufferIndex++]=buffer[i];
                    	//Log.d(TAG,"recived byte mReadBufferIndex="+mReadBufferIndex);
                    	if(mReadBufferIndex>=mWaitByte){
                    		Log.d(TAG,"recived byte="+mReadBufferIndex);
                    		if(mSentCmnd==READ_STATUS_CMND){//********************* read back status of qblock
                    			if((mReadBufferIndex>mWaitByte)||(mReadBuffer[27]!='S')||(mReadBuffer[26]!='T')||(mReadBuffer[25]!='S')){
                    				Log.d(TAG,"ask statusDummy recived byte="+mReadBufferIndex+"**************************");
                    				askStatusDum();
                    				
                    			}
                    			else{
                    				storeStatus();
                    			// Send the obtained bytes to the UI Activity
                    				if(((mRunMode==METHOD_RUNNING)||(mRunMode==RUNPRESCAL))&&bEnableAskStatus&&(mDataTotalNo>mRxDataBufferIndex)){
                    					//ask test last row if a method is running 
                    					//and it's not status reading after connection(bEnableAskStatus=true)
                    					askTestResultLastData();
                    					Log.d(TAG,"ASK TEST RESULT last data mRxDataBufferIndex="+mRxDataBufferIndex+"  mDataTotalNo="
                    							+mDataTotalNo);
                    					//break; //exit do not mBlthService.mReadBufferIndex=0 and mBlthService.mSentCmnd=0;
                    				}
                    				else
                    				//Log.d(TAG,"status recived in service");
                    					mHandler.obtainMessage(WQblockBluetooth.MESSAGE_READ, mReadBufferIndex, -1, -1)
                    					.sendToTarget();
                        		}
                            }
                    		else if(mSentCmnd==SEND_TEST_RESULT_CMND){//***************** read back test result
                    			if(mReadBufferIndex>mWaitByte){
                    				Log.d(TAG," read back test Result  wrong*******************************************************");
                    				mReadBufferIndex=0;
                    		    	mSentCmnd=0;
                    				//break;
                    			}
                    			else{
                    				Log.d(TAG,"----------------mDataLenght="+mDataLenght);
	                    			for(int i=0;i<(mReadBufferIndex/mDataLenght);i++)
	                    				for(int j=0;j<mDataLenght;j++){
	                    					mRxDataBuffer[j][i+mRxDataBufferIndex]=mReadBuffer[(i*mDataLenght)+j];
	                    					//Log.i(TAG," read back test Result ...mRxDataBuffer["+j+"]["
	                    					//		+i+"]="+(mReadBuffer[(i*mDataLenght)+j]& 0xff));
	                    				}
	                    			
	                    			mRxDataBufferIndex+=mReadBufferIndex/mDataLenght;
	                    			Log.d(TAG," read back test Result ...mRxDataBufferIndex="+mRxDataBufferIndex);
	                    			
	                    			mHandler.obtainMessage(WQblockBluetooth.MESSAGE_READ, mReadBufferIndex, -1, mReadBuffer)
	                    					.sendToTarget();
                    			}
                    		}
                    		else if (mSentCmnd==SEND_TEST_DATA_ROWS_CMND){//**************** read back test result rows
                    			if(mReadBufferIndex>mWaitByte){
                    				Log.d(TAG," read back test Result last data wrong************************************************************");
                    				mReadBufferIndex=0;
                    		    	mSentCmnd=0;
                    				//break;
                    			}
                    			else{
                    				for(int i=0;i<(mWaitByte/*mReadBufferIndex*//mDataLenght);i++)
                    					for(int j=0;j<mDataLenght;j++){
                    						int ii=mRxDataBufferIndex+i;
                    						mRxDataBuffer[j][ii]=mReadBuffer[(i*mDataLenght)+j];
                    						if(D) Log.i(TAG2," read back test Result last data...mRxDataBuffer["+j+"]["
                        							+ii+"]="+mReadBuffer[(i*mDataLenght)+j]);
                    					}
                    				mRxDataBufferIndex+=mWaitByte/*mReadBufferIndex*//mDataLenght;
                    				
                    				Log.d(TAG," send message test row back");
                        			mHandler.obtainMessage(WQblockBluetooth.MESSAGE_READ, mWaitByte/*mReadBufferIndex*/, -1, mReadBuffer)
                                    .sendToTarget();
                    			}
                    		}
                    		else if (mSentCmnd==METHOD_START_CMND){//**************** START METHOD COMMAND
                    				mHandler.obtainMessage(WQblockBluetooth.MESSAGE_READ, mReadBufferIndex, -1, mReadBuffer)
                                    .sendToTarget();
                    			
                    		}
                    		else if (mSentCmnd==CMND_STARTPRESCAL){//**************** START PRES CALIBRATION COMMAND
                				mHandler.obtainMessage(WQblockBluetooth.MESSAGE_READ, mReadBufferIndex, -1, mReadBuffer)
                                .sendToTarget();
                			
                    		}
                    		else if (mSentCmnd==CMND_IGNORE_IRFAIL){//**************** IGNORE IR FAILED COMMAND
                				mHandler.obtainMessage(WQblockBluetooth.MESSAGE_READ, mReadBufferIndex, -1, mReadBuffer)
                                .sendToTarget();
                			
                    		}
                    		else if (mSentCmnd==METHOD_STOP_CMND){//**************** Stop METHOD COMMAND
                    				mHandler.obtainMessage(WQblockBluetooth.MESSAGE_READ, mReadBufferIndex, -1, mReadBuffer)
                                    .sendToTarget();
                    			
                    		}
                    		else{
                    			mHandler.obtainMessage(WQblockBluetooth.MESSAGE_READ, mReadBufferIndex, -1, mReadBuffer)
                                .sendToTarget();
                    		}
                    		
                    		
                    	}
                    }
                    else{
                    	Log.d(TAG,"after ask statusDummy recived byte=  "+bytes+"  **************************");
                    	mReadBufferIndex=0;//to reset the input buffer
                    }
                    	
                    	
                } catch (IOException e) {
                    Log.d(TAG, "io exception disconnected", e);
                    connectionLost();
                    // Start the service over to restart listening mode
                    //BluetoothService.this.start();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
               if(D) Log.d(TAG,"noof writeen byte="+buffer.length+" byte0="+buffer[0]+" byte1="+buffer[1]+" byte2="+buffer[2]);
                // Share the sent message back to the UI Activity
                //mHandler.obtainMessage(WQblockBluetooth.MESSAGE_WRITE, -1, -1, buffer)
                //        .sendToTarget();
            } catch (IOException e) {
               if(D) Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
            	mmInStream.close();
                mmSocket.close();
            } catch (IOException e) {
               if(D) Log.e(TAG, "close() of connect socket failed", e);
            }
        }
        
        
        
    }

    
    /**
     * write a method to the connected thread
     * @param mm
     */
    public void writeMethod(QBlockMethod mm) {
    	byte[] out;
    	mWaitByte=METHOD_SEND_BYTE;	//expect 16 bytes back
    	mSentCmnd=METHOD_WRITE_CMND;
    	mReadBufferIndex=0;
    	out=new byte[34];
    	out[0]=(byte)(METHOD_WRITE_CMND & 0X00FF);
    	out[1]=(byte)((METHOD_WRITE_CMND & 0xFF00)>>8);
    	out[2]=(byte)(mm.TarTemp[0]&0x00ff);
    	out[3]=(byte)((mm.TarTemp[0]&0xff00)>>8);
    	out[4]=(byte)(mm.RampTime [0]&0x00ff);
    	out[5]=(byte)((mm.RampTime [0]&0xff00)>>8);
    	out[6]=(byte)(mm.HoldTime  [0]&0x00ff);
    	out[7]=(byte)((mm.HoldTime [0]&0xff00)>>8);
    	out[8]=(byte)(mm.TarTemp[1]&0x00ff);
    	out[9]=(byte)((mm.TarTemp[1]&0xff00)>>8);
    	out[10]=(byte)(mm.RampTime [1]&0x00ff);
    	out[11]=(byte)((mm.RampTime [1]&0xff00)>>8);
    	out[12]=(byte)(mm.HoldTime  [1]&0x00ff);
    	out[13]=(byte)((mm.HoldTime [1]&0xff00)>>8);
    	out[14]=(byte)(mm.TarTemp[2]&0x00ff);
    	out[15]=(byte)((mm.TarTemp[2]&0xff00)>>8);
    	out[16]=(byte)(mm.RampTime [2]&0x00ff);
    	out[17]=(byte)((mm.RampTime [2]&0xff00)>>8);
    	out[18]=(byte)(mm.HoldTime  [2]&0x00ff);
    	out[19]=(byte)((mm.HoldTime [2]&0xff00)>>8);
    	out[20]=(byte)(mm.TarTemp[3]&0x00ff);
    	out[21]=(byte)((mm.TarTemp[3]&0xff00)>>8);
    	out[22]=(byte)(mm.RampTime [3]&0x00ff);
    	out[23]=(byte)((mm.RampTime [3]&0xff00)>>8);
    	out[24]=(byte)(mm.HoldTime  [3]&0x00ff);
    	out[25]=(byte)((mm.HoldTime [3]&0xff00)>>8);
    	out[26]=(byte)(mm.Type & 0x00ff);
    	out[27]=(byte)(mm.PresMode & 0x00ff);
    	out[28]=(byte)(mm.PowerLimit & 0x00ff);
    	out[29]=(byte)((mm.PowerLimit & 0xff00)>>8);
    	out[30]=(byte)(mm.PresLimit & 0x00ff);
    	out[31]=(byte)((mm.PresLimit & 0xff00)>>8);
    	out[32]=(byte)(mm.TempLimit & 0x00ff);
    	out[33]=(byte)((mm.TempLimit & 0xff00)>>8);
    	
    	writeStartFrame();
     	this.write(out);
     	Log.d(TAG,"sent write method cmnd");
    }
    
    public void saveMethod(QBlockMethod mm,File fileName){
			try   {  
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));//new File("save.bin"))); //Select where you wish to save the file...
				//ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.bin"))); //Select where you wish to save the file...
				oos.writeObject(mm); // write the class as an 'object' 
				oos.flush(); // flush the stream to insure all of the information was written to 'save.bin' 
				oos.close();// close the stream  
				Message msg = mHandler.obtainMessage(WQblockBluetooth.MESSAGE_TOAST);
		        Bundle bundle = new Bundle();
		        bundle.putString(WQblockBluetooth.TOAST, "Rceipe was saved");
		        msg.setData(bundle);
		        mHandler.sendMessage(msg);
			}  

			catch(Exception ex) {  

				//Log.v("Address Book",ex.getMessage());  
				ex.printStackTrace();  
				Message msg = mHandler.obtainMessage(WQblockBluetooth.MESSAGE_TOAST);
		        Bundle bundle = new Bundle();
		        bundle.putString(WQblockBluetooth.TOAST, "Recipe was not saved");
		        msg.setData(bundle);
		        mHandler.sendMessage(msg);
			}  

    }
    
    public void askTestResultLastData(){
    	byte[] out;
    	short startData,noOfData=0;
    	startData=(short)mRxDataBufferIndex;
    	if(mDataTotalNo>(short)mRxDataBufferIndex)
    		noOfData=(short) (mDataTotalNo-(short)mRxDataBufferIndex);
    	else
    		return;
    	out=new byte[6];
    	out[0]=(byte)(SEND_TEST_DATA_ROWS_CMND & 0X00FF);
    	out[1]=(byte)((SEND_TEST_DATA_ROWS_CMND & 0xFF00)>>8);
    	out[2]=(byte)(startData & 0X00FF);
    	out[3]=(byte)((startData & 0xFF00)>>8);
    	out[4]=(byte)(noOfData & 0X00FF);
    	out[5]=(byte)((noOfData & 0xFF00)>>8);
    	mWaitByte=noOfData*mDataLenght;	//expect 24 bytes back
    	mReadBufferIndex=0;
    	mSentCmnd=SEND_TEST_DATA_ROWS_CMND;
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG," ask test Result Last Data  ...start data="+startData+" noOfData="+noOfData);
    }
    public void askTestResultLastData2(short startData,short noOfData){
    	byte[] out;
    	out=new byte[6];
    	out[0]=(byte)(SEND_TEST_DATA_ROWS_CMND & 0X00FF);
    	out[1]=(byte)((SEND_TEST_DATA_ROWS_CMND & 0xFF00)>>8);
    	out[2]=(byte)(startData & 0X00FF);
    	out[3]=(byte)((startData & 0xFF00)>>8);
    	out[4]=(byte)(noOfData & 0X00FF);
    	out[5]=(byte)((noOfData & 0xFF00)>>8);
    	mWaitByte=noOfData*mDataLenght;	//expect 24 bytes back
    	mReadBufferIndex=0;
    	mSentCmnd=SEND_TEST_DATA_ROWS_CMND;
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG," ask test Result Last Data2  ...start data="+startData+" noOfData="+noOfData);
    }
    
    public void askTestResult(short noOfData){
    	byte[] out;
    	out=new byte[4];
    	if(noOfData>WQblockBluetooth.MAX_DATA){
    		this.noOfDataCopy=(short)(noOfData);
    		this.noOfDataCopyPage=1;
    		noOfData=WQblockBluetooth.MAX_DATA;
    		this.bAskTestResultBigFlag=true;
    	}
    	else{
    		this.bAskTestResultBigFlag=false;
    	}
    	out[0]=(byte)(SEND_TEST_RESULT_CMND & 0X00FF);
    	out[1]=(byte)((SEND_TEST_RESULT_CMND & 0xFF00)>>8);
    	out[2]=(byte)(noOfData & 0X00FF);
    	out[3]=(byte)((noOfData & 0xFF00)>>8);
    	
    	mWaitByte=noOfData*mDataLenght;	//expect 24 bytes back
    	mSentCmnd=SEND_TEST_RESULT_CMND;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG," ask test Result ... noOfData="+noOfData);
    }

    public Object loadMethodFile(File f)   {  

    	try  {  
    		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));  
    		Object o = ois.readObject();  
    		Message msg = mHandler.obtainMessage(WQblockBluetooth.MESSAGE_TOAST);
	        Bundle bundle = new Bundle();
	        bundle.putString(WQblockBluetooth.TOAST, "Recipe was loaded");
	        msg.setData(bundle);
	        mHandler.sendMessage(msg);
    		return o;  
    	}  

    	catch(Exception ex){  

    		Log.v("Address Book",ex.getMessage());  
    		ex.printStackTrace();  
    	}  
    	Message msg = mHandler.obtainMessage(WQblockBluetooth.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(WQblockBluetooth.TOAST, "Recipe was not loaded");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    	return null;  

    } 
    public void readMethod(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(METHOD_READ_CMND & 0X00FF);
    	out[1]=(byte)((METHOD_READ_CMND & 0xFF00)>>8);
    	out[2]=(byte)0xbb;
    	//t[3]=(byte)0x00;
    	//out[4]=(byte)0x00;
    	mWaitByte=METHOD_READ_BYTE;	//expect 24 bytes back
    	mSentCmnd=METHOD_READ_CMND;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    }
    public void sendSystemSettings(short deltat,short purgecycle,short purgetemp){
    	byte[] out;
    	out=new byte[14];
    	out[0]=(byte)(CMND_WRITE_SETTINGS & 0X00FF);
    	out[1]=(byte)((CMND_WRITE_SETTINGS & 0xFF00)>>8);
    	out[2]=0;
    	out[3]=(byte)(deltat & 0X00FF);
    	out[4]=(byte)(purgecycle & 0X00FF);
    	out[5]=(byte)(purgetemp & 0X00FF);
    	out[6]=(byte)((purgetemp & 0xFF00)>>8);
    	mWaitByte=READ_SETTINGS_BYTE;//expect 24 bytes back
    	mSentCmnd=CMND_READ_SETTINGS;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent send system settings cmnd");
    }
    public void ignoreIRFail(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CMND_IGNORE_IRFAIL & 0X00FF);
    	out[1]=(byte)((CMND_IGNORE_IRFAIL & 0xFF00)>>8);
    	mWaitByte=METHOD_STRT_BYTE;	//expect 4 bytes back
    	mSentCmnd=CMND_IGNORE_IRFAIL;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent ignore ir failure");
    }
    
    public void sendSystemConfig(short sysCon){
    	byte[] out;
    	out=new byte[4];
    	out[0]=(byte)(CMND_WRITE_CONFIG & 0X00FF);
    	out[1]=(byte)((CMND_WRITE_CONFIG & 0xFF00)>>8);
    	out[2]=(byte)(sysCon & 0X00FF);
    	out[3]=(byte)((sysCon & 0xFF00)>>8);
    	mWaitByte=1;	//expect 24 bytes back
    	mSentCmnd=CMND_WRITE_CONFIG;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent send system config cmnd");
    }
    public void readSystemSettings(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CMND_READ_SETTINGS & 0X00FF);
    	out[1]=(byte)((CMND_READ_SETTINGS & 0xFF00)>>8);
    	mWaitByte=READ_SETTINGS_BYTE;	//expect 26 bytes back
    	mSentCmnd=CMND_READ_SETTINGS;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent read system config cmnd");
    }
    
    public void manMWon(short power){
    	byte[] out;
    	out=new byte[4];
    	out[0]=(byte)(CMND_MAN_MAG_ON & 0X00FF);
    	out[1]=(byte)((CMND_MAN_MAG_ON & 0xFF00)>>8);
    	out[2]=(byte)(power & 0X00FF);
    	out[3]=(byte)((power & 0xFF00)>>8);
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent MAG ON cmnd");
    }
    public void manMWOff(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CMND_MAN_MAG_OFF & 0X00FF);
    	out[1]=(byte)((CMND_MAN_MAG_OFF & 0xFF00)>>8);
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent MAG off cmnd");
    }
    public void manCoolOn(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CMND_MAN_COOL_ON & 0X00FF);
    	out[1]=(byte)((CMND_MAN_COOL_ON & 0xFF00)>>8);
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent cooling fan on cmnd");
    }
    public void manCoolOff(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CMND_MAN_COOL_OFF & 0X00FF);
    	out[1]=(byte)((CMND_MAN_COOL_OFF & 0xFF00)>>8);
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent cooling fan off cmnd");
    }
    public void manExOn(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CMND_MAN_EX_ON & 0X00FF);
    	out[1]=(byte)((CMND_MAN_EX_ON & 0xFF00)>>8);
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent exhaust fan on cmnd");
    }
    
    public void manExOff(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CMND_MAN_EX_OFF & 0X00FF);
    	out[1]=(byte)((CMND_MAN_EX_OFF & 0xFF00)>>8);
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent exhaust off cmnd");
    }
    public void manMotorOn(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CMND_MAN_MOT_ON & 0X00FF);
    	out[1]=(byte)((CMND_MAN_MOT_ON & 0xFF00)>>8);
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent MOTOR on cmnd");
    }
    public void manMotorOff(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CMND_MAN_MOT_OFF & 0X00FF);
    	out[1]=(byte)((CMND_MAN_MOT_OFF & 0xFF00)>>8);
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"sent MOTOR off cmnd");
    }
    public void startPresCal(){
    	
    	int vesPos=0,i2=1;
    	for(int i=0;i<16;i++){
			if(mgs.mQblockSettings.bSelectedVessel[i]==true)
				vesPos+=i2;
			i2*=2;
		}
		
		int vesVol=mgs.mQblockSettings.VesselVolume;
		
		byte[] out=new byte[5];
    	out[0]=(byte)(CMND_STARTPRESCAL & 0X00FF);
    	out[1]=(byte)((CMND_STARTPRESCAL & 0xFF00)>>8);
    	out[2]=(byte)(vesVol & 0X00FF);
    	out[4]=(byte)(vesPos & 0X00FF);
    	out[3]=(byte)((vesPos & 0xFF00)>>8);
    	mWaitByte=METHOD_STRT_BYTE;	//expect 4 bytes back
    	mSentCmnd=CMND_STARTPRESCAL;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    	mRxDataBufferIndex=0;
		
    }
    public void startMethod(){
    	
    	int vesPos=0,i2=1;
    	for(int i=0;i<16;i++){
			if(mgs.mQblockSettings.bSelectedVessel[i]==true)
				vesPos+=i2;
			i2*=2;
		}
		
		int vesVol=mgs.mQblockSettings.VesselVolume;
		
    	byte[] out=new byte[5];
    	out[0]=(byte)(METHOD_START_CMND & 0X00FF);
    	out[1]=(byte)((METHOD_START_CMND & 0xFF00)>>8);
    	out[2]=(byte)(vesVol & 0X00FF);
    	out[4]=(byte)(vesPos & 0X00FF);
    	out[3]=(byte)((vesPos & 0xFF00)>>8);
    	mWaitByte=METHOD_STRT_BYTE;	//expect 4 bytes back
    	mSentCmnd=METHOD_START_CMND;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    	mRxDataBufferIndex=0;
		
    }
    public void stopMethod(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(METHOD_STOP_CMND & 0X00FF);
    	out[1]=(byte)((METHOD_STOP_CMND & 0xFF00)>>8);
    	mWaitByte=METHOD_STPD_BYTE;	//expect 4 bytes back
    	mSentCmnd=METHOD_STOP_CMND;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	
this.write(out);
    	
    }
    public void writeStartFrame(){
    	byte[] out;
    	out=new byte[5];
    	out[0]=(byte)0x00;
    	out[1]=(byte)0x00;
    	out[2]=(byte)0x00;
    	out[3]=(byte)0xaa;
    	out[4]=(byte)0xbb;
    	this.write(out);
    }
    public void askStatus(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(READ_STATUS_CMND & 0X00FF);
    	out[1]=(byte)((READ_STATUS_CMND & 0xFF00)>>8);
    	mWaitByte=READ_STATUS_BYTE;	//expect 5 bytes back
    	mSentCmnd=READ_STATUS_CMND;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"ask status written");
    }
    public void goCalibrationMode(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CALIB_CMND & 0X00FF);
    	out[1]=(byte)((CALIB_CMND & 0xFF00)>>8);
    	mWaitByte=CALIB_STRT_BYTE;	//expect 4 bytes back
    	mSentCmnd=CALIB_CMND;
    	mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"GOTO CALIBRATION MODE");
    }
    public void sendCalibrationExit(short cal20,short cal200){
    	byte[] out;
    	out=new byte[4];
    	out[0]=(byte)(SEND_CALIB_EXIT_CMND & 0X00FF);
    	out[1]=(byte)((SEND_CALIB_EXIT_CMND & 0xFF00)>>8);
    	out[2]=(byte)(cal20 & 0X00FF);
    	out[3]=(byte)(cal200 & 0X00FF);
    	//mWaitByte=CALIB_STRT_BYTE;	//expect 4 bytes back
    	//mSentCmnd=SEND_CALIB_EXIT_CMND;
    	//mReadBufferIndex=0;
    	writeStartFrame();
    	this.write(out);
    	Log.d(TAG,"exit CALIBRATION MODE");
    }
    public void askStatusDum(){
    	byte[] out;
    	out=new byte[3];
    	out[0]=(byte)(CMND_DUMMY & 0X00FF);
    	out[1]=(byte)((CMND_DUMMY & 0xFF00)>>8);
    	
    	writeStartFrame();
    	this.write(out);
    	mSentCmnd=0;
    	mReadBufferIndex=0;
    	Log.d(TAG,"ask status dum written");
    }
    /** 
     * Convert short var. to two bytes
     * @param s
     * @param lobyte
     * @param hibyte
     */
    public void shortToBytes(short s,byte lobyte,byte hibyte) {
        byte[] mlobyte,mhibyte ;
        mlobyte=new byte[1];
        mhibyte=new byte[1];
    	mlobyte[0]=(byte)(s & 0x00FF);
        mhibyte[0]=(byte)((s & 0xFF00)>>8);
        lobyte=mlobyte[0];
        hibyte=mhibyte[0];
    }
    public void storeStatus(){
    	mRunMode=(short)(mReadBuffer[0] & 0xff);	//store the status
    	//mMethodFinishMode=(short)(mReadBuffer[1] & 0xff); 
		//mDataRowNo=(short)(mReadBuffer[2] & 0xff);
		if(mDataRowNo>122)
			mDataRowNo=122;
		//mTimeInterval=(short)(mReadBuffer[3] & 0xff);
		mFailedIRVesselNo=(short)(mReadBuffer[3] & 0xff);
		mIRVesselNo=(short)(mReadBuffer[4] & 0xff);
		mTemp=(short)((mReadBuffer[5] & 0xff) | mReadBuffer[6]<<8);
		mPower=(short)((mReadBuffer[7] & 0xff) | mReadBuffer[8]<<8);
		
		mTestTime=(short)((mReadBuffer[9] & 0xff) | mReadBuffer[10]<<8 );
		mDataRowNo=(short) (mTestTime/256);
		mDataIndex=(short)(mTestTime-(mDataRowNo*256));
		mTotalTime=(short)((mReadBuffer[11] & 0xff) | mReadBuffer[12]<<8);
		//mTestTime= (int)((mReadBuffer[10] & 0x000000ff) | (mReadBuffer[9]  & 0x000000ff)<<8 | (mReadBuffer[8]  & 0x000000ff)<<16);
		//mTotalTime=(int)((mReadBuffer[14] & 0x000000ff) | (mReadBuffer[13] & 0x000000ff)<<8 | (mReadBuffer[12] & 0x000000ff)<<16);
		//Log.d(TAG,"buf14="+mReadBuffer[14]+" buf13="+mReadBuffer[13]+" buf12="+mReadBuffer[12]+"  inter="+this.mTimeInterval);
		mCurStep=(short)(mReadBuffer[15] & 0xff); 
		mDataTotalNo=(short)((mReadBuffer[13] & 0xff) | mReadBuffer[14]<<8);//mTestTime;//mDataRowNo*16+mDataIndex;
		mStatus1=(short)(mReadBuffer[16] & 0xff);
		mStatus2=(short)(mReadBuffer[17] & 0xff);
		mSoftStatus=(short)(mReadBuffer[18] & 0xff);
		mOptTemp=(short)(mReadBuffer[19] & 0xff);
		mVesselNo=(short)(mReadBuffer[4] & 0xff);
		mVesselHi=(short)(mReadBuffer[20] & 0xff);
		mVesselLo=(short)(mReadBuffer[21] & 0xff);
		mTestTotalUsedPower=(short)((mReadBuffer[22] & 0xff) | mReadBuffer[23]<<8);
		mResetStatus=(short)(mReadBuffer[24] & 0xff);
		Log.d(TAG,"-------------------------  this is reset status="+mResetStatus+" opt temp="+mOptTemp+"   temp="+mTemp+"   vessel hi ="+mVesselHi);
		//---  hard status byte 1--------------
		if((byte) (mReadBuffer[16] & 0x01)==0)
			bMagLo=false;
		else
			bMagLo=true;
		
		if((byte) (mReadBuffer[16] & 0x02)==0)
			bMagHi=false;
		else
			bMagHi=true;
		//---  hard status byte 2 -------------
		if((byte) (mReadBuffer[17] & 0x01)==0)
			bMotorEnable=false;
		else
			bMotorEnable=true;
		
		if((byte) (mReadBuffer[17] & 0x02)==0)
			bMotorSW=false;
		else
			bMotorSW=true;
		
		if((byte) (mReadBuffer[17] & 0x04)==0)
			bMotorOpt=false;
		else
			bMotorOpt=true;
		
		if((byte) (mReadBuffer[17] & 0x08)==0)
			bCool=false;
		else
			bCool=true;
		
		if((byte) (mReadBuffer[17] & 0x10)==0)
			bExhaust=false;
		else
			bExhaust=true;
		
		if((byte) (mReadBuffer[17] & 0x20)==0)
			bDoor=false;
		else
			bDoor=true;
		
		if((byte) (mReadBuffer[17] & 0x40)==0)
			bInterlock=false;
		else
			bInterlock=true;
		//---  soft status byte ---------------
		if((byte) (mReadBuffer[18] & 0x01)==0)
			bOptVesDetFlag=false;
		else
			bOptVesDetFlag=true;
		
		if((byte) (mReadBuffer[18] & 0x02)==0)
			bPauseFlag=false;
		else
			bPauseFlag=true;
		
		if((byte) (mReadBuffer[18] & 0x10)==0)
			bRampHoldFlag=false;
		else
			bRampHoldFlag=true;
		if((byte) (mReadBuffer[18] & 0x20)==0)
			bSyncOnLineFlag=false;
		else
			bSyncOnLineFlag=true;
		
		if((byte) (mReadBuffer[18] & 0x40)==0)
			bIRFailedFlag=false;
		else
			bIRFailedFlag=true;
		
		
		//--------------------------------------
		if(D) Log.d(TAG,"status back and was stored");
    }
    
} 
    
   

package com.questron.WMicroWave;

import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class StatusTimerTask extends TimerTask{
		private static final String TAG = "BlueToothChat";
        private Context context;
        private Handler mHandler = new Handler();
        private WQblockBluetooth mwqblock=null;
        // Write Custom Constructor to pass Context
        public StatusTimerTask(WQblockBluetooth wqblock) {
            //this.context = con;
        	this.mwqblock=wqblock;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

            // your code starts here.
            // I have used Thread and Handler as we can not show Toast without starting new thread when we are inside a thread.
            // As TimePicker has run() thread running., So We must show Toast through Handler.post in a new Thread. Thats how it works in Android..
        	
        	//if(mwqblock.mBatchPageFlag==false){
        	Global gs = (Global) this.mwqblock.getApplication();
        	if(this.mwqblock.bGatheringDataFlag){//after connection check for timeout
        		if(++mwqblock.mTimer>this.mwqblock.mTimeOut){//time out error
        			this.mwqblock.mBlthService.stop();
        			this.mwqblock.mBlthService.start();
        			this.mwqblock.connectDevice();
        			this.mwqblock.bGatheringDataFlag=false;
        			Log.d(TAG,"------- timed out stoped");
        			mwqblock.mTimer=0;
        		}
        	}
        	else{	
			        	if((mwqblock.mTimer<2) &&(mwqblock.mBlthService.mSentCmnd==0)&& mwqblock.mBlthService.bEnableAskStatus){
			        		if(mwqblock.mAskStatusFlag)	{// ask status once a second at timer=0 or =1
			        			mwqblock.mBlthService.askStatus();
			        			mwqblock.mAskStatusFlag=false;
			        		}
			        	}
			        	
			        	if((mwqblock.mTimer==1)&& mwqblock.mAskStatusFlag){
			        		mwqblock.mAskStatusFlag=false;
			        		if(mwqblock.mBlthService.mSentCmnd!=0)
			        			mwqblock.mBlthService.askStatus();
			        		
			        	}
			        	
			        	if((mwqblock.mTimer>=2)&& (mwqblock.mBlthService.mSentCmnd==0)){
			        			if(mwqblock.mWriteMethodFlag){
			        				mwqblock.mWriteMethodFlag=false;
					        		mwqblock.mBlthService.writeMethod(mwqblock.mQblockMethodWrite);
			        			}
			        			if(mwqblock.mStartPresCalFlag){
				        			mwqblock.mStartPresCalFlag=false;
				        			mwqblock.mBlthService.startPresCal();
			        			}
			        			if(mwqblock.mStopMethodFlag){
			        				mwqblock.mStopMethodFlag=false;
					        		mwqblock.mBlthService.stopMethod();
			        			}
			        			if(mwqblock.mStartMethodFlag){
			        				mwqblock.mStartMethodFlag=false;
			        				mwqblock.mBlthService.startMethod();
			        			}
			        			
			        	}
			        	
			        	
			        	if(++mwqblock.mTimer>4){
			            	mwqblock.mTimer=0;
			            	mwqblock.mAskStatusFlag=true;	//this flag is for to ask status once every second
			        	}
        	}           

        }

    
}

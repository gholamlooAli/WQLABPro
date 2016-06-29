package com.questron.WMicroWave;
import java.io.Serializable;

import android.text.format.Time;
public class QBlockBatch implements Serializable{
	//public static final int DATA_LENGHT = 5;
	public Boolean[] 	bSelectedVessel ;
	public QBlockMethod mQblockMethod;
	public String		mChemistName;
	public String		mBatchTime;
	public String   	mNote;
	public String   	mBlockName;
	public int			mTestTotalUsedPower;
	public int			mSystemConfig;
	//public byte[] 		mRxDataBuffer;
	public byte[][] mRxDataBuffer ;
	private static final long serialVersionUID = 110112;
	public QBlockBatch(){
		this.bSelectedVessel=new Boolean[16];
		this.mBatchTime=null;
		this.mNote=null;
		this.mBlockName=null;
		this.mChemistName=null;
		this.mQblockMethod=new QBlockMethod();
		this.mTestTotalUsedPower=0;
		this.mSystemConfig=4;//ir only
		for(int i=0;i<16;i++){
			this.bSelectedVessel[i]=false;
		}
		this.bSelectedVessel[11]=true;
		this.bSelectedVessel[12]=true;
	}
}

package com.questron.WMicroWave;
import java.io.Serializable;

import android.text.format.Time;




public class QBlockMethod implements Serializable{
	public short [] TarTemp;
	public short [] RampTime;
	public short [] HoldTime;
	public short 	Type;
	public short	PresMode;
	public short	PowerLimit;
	public short	PresLimit;
	public short	TempLimit;
	public String 	mRecipeName;
	public String 	mSampleType;
	public String 	mReagents;
	public String 	mRecipeChemistName;
	//public int 		mSelectedVialNo;
	public String	mRecipeTime;
	public String   mNote;
	public String   mBatchTime;
	public String   mBatchName;
	public String   mBatchNote;
	public String   mBatchChemist;
	public String   mSt1;
	public String   mSt2;
	public String   mSt3;
	public int   mTimeInterval;
	public int   mTempResolution;
	public short mReagentVolume[];
	public short mVesselNo;
	public short mSampleAmount;
	private static final long serialVersionUID = 110110; 
	public QBlockMethod(){
		this.mRecipeName=null;
		this.mSampleType=null;
		this.mReagents=null;
		this.mRecipeChemistName=null;
		//this.mSelectedVialNo=0;
		this.mRecipeTime=null;
		this.mNote=null;
		this.mBatchTime=null;
		this.mBatchName=null;
		this.mBatchNote=null;
		this.mBatchChemist=null;
		this.mSt1=null;
		this.mSt2=null;
		this.mSt3=null;
		this.mTimeInterval=15;
		this.mTempResolution=1;
		this.TarTemp=new short[4];
		this.RampTime=new short[4];
		this.HoldTime=new short[4];
		this.mReagentVolume=new short[5];
		for(int i=1;i<5;i++){
			this.TarTemp[i-1]=0;
			this.RampTime[i-1]=0;
			this.HoldTime[i-1]=0;
			this.mReagentVolume[i-1]=0;
		}
		this.mReagentVolume[4]=0;
		this.Type=0;
		this.PresMode=0;
		this.PowerLimit=1200;
		this.PresLimit=500;
		this.TempLimit=250;
		this.mVesselNo=1;
		this.mSampleAmount=0;
	}

}

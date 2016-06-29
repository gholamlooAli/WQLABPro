package com.questron.WMicroWave;

public class GridDataModel {
	private String time;
	private int power;
	private int press;
	private int optTemp;
	private int[] vesselTemp;
	public static final int TOTALVESSEL= 12;
	public GridDataModel(){
		this.vesselTemp=new int[TOTALVESSEL];
	}
	
	public int getPower() {
	    return power;
	  }
	public void setPower(int power){
		this.power=power;
	}
	
	public int getPress() {
	    return press;
	  }
	public void setPress(int press){
		this.press=press;
	}
	
	public int getOptTemp() {
	    return optTemp;
	  }
	public void setOptTemp(int optTemp){
		this.optTemp=optTemp;
	}
	public int getTemp(int vslNo) {
		if((vslNo>0) && (vslNo<=TOTALVESSEL))
			return vesselTemp[vslNo-1];
		else
			return 0;
	  }
	public void setTemp(int vslNo,int vslTemp){
		if((vslNo>0) && (vslNo<=TOTALVESSEL))
			this.vesselTemp[vslNo-1]=vslTemp;
	}
	
	public String getTime() {
	    return time;
	  }
	public void setTime(int time) {
		int h,m,s;
    	h=time/3600;
    	m=(time-(h*3600))/60;
    	s=time-((h*3600)+(m*60));
    	String sp1,sp2;
    	if (m<10)
    		sp1=":0";
    	else
    		sp1=":";
    	if (s<10)
    		sp2=":0";
    	else
    		sp2=":";
	    this.time = h+sp1+m+sp2+s;
	  }
}

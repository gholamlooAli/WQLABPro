package com.questron.WMicroWave;


	import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application; 
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
	/*** This is a global POJO that we attach data to which we
	 * * want to use across the application**/
	 public class Global extends Application{
		 private static final String TAG = "Global";
		public Boolean  bSettingsChanged = false;
		public Boolean  bEnableSendBut=false;
		public int		iNoOfSelectedQblock;//base=1 , =0 means no block selected
		public int	[]  iSelectedQblock=new int[8];//saves the numbers of selected
		public int      iSelectedQblockCntr;
		public List<String> deviceName_list;
		public List<String> deviceAddress_list;
		public Boolean 		mLockSoftWare=false;
		
		public QBlockSettings mQblockSettings=new QBlockSettings();
		
//change this to true if program needs to compile for cpi also disable appropriate block in manifest.xml file 
		public Boolean CPI= false;
		
		
		private String testMe; 
		public String getTestMe() {
			return testMe;
		}
		public void setTestMe(String testMe) {
			this.testMe = testMe;
		}
		@Override	
		public void onCreate() {
			super.onCreate();
			this.deviceName_list = new ArrayList<String>();
			this.deviceAddress_list = new ArrayList<String>();
			Log.e(TAG,"on create gloabal");
			//this.mLockSoftWare=true;
			this.deviceName_list.add("block 12345");
			this.deviceAddress_list.add("00:06:66:49:3C:68");
			openSetting();
	
					
		}
		
		
		public Boolean	openSetting(){	
			//load settings file
			File path=Environment.getExternalStoragePublicDirectory("questron\\MW\\settings");
			String st = "wqblockSettings";
			//File mfile = new File(getExternalFilesDir(null), st);
            try  {  
            	File mfile = new File(path,st);
    			path.mkdirs();
            	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(mfile));  
        		Object o = ois.readObject();
        		mQblockSettings=(QBlockSettings) o;
        		ois.close();
        		Log.e(TAG,"open settings global");
        		return true;
            }
            catch(Exception ex){  

        		Log.v("Address Book",ex.getMessage());  
        		ex.printStackTrace(); 
        		bSettingsChanged=true;
        		return false;
        	} 
		}
		
		public Boolean SaveSettings(){
			//if(bSettingsChanged){
				File path=Environment.getExternalStoragePublicDirectory("questron\\MW\\settings");
				try{
				String st = "wqblockSettings";
				path.mkdirs();  
	    		File file = new File(path,st); 
                //File file = new File(getExternalFilesDir(null), st);	
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));//new File("save.bin"))); //Select where you wish to save the file...
				//ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.bin"))); //Select where you wish to save the file...
				oos.writeObject(mQblockSettings); // write the class as an 'object' 
				oos.flush(); // flush the stream to insure all of the information was written to 'save.bin' 
				oos.close();
				bSettingsChanged=false;
				Log.i("TAG","Settings saved");
				return true;
				}
				catch(Exception ex) {  

					Log.v("Address Book",ex.getMessage());  
					ex.printStackTrace();  
					return false;
				}  
			//}
			//else
			//	return false;
			
		}
		
	}
	
	
	
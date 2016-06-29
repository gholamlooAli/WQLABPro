package com.questron.WMicroWave;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class DlgSelVessel extends DialogFragment implements
android.view.View.OnClickListener {
	private static final String TAG = "BluetoothChat";
public Activity c;
public Dialog d;
public Button yes, no;
/* The activity that creates an instance of this dialog fragment must     
 * * implement this interface in order to receive event callbacks.     
 * * Each method passes the DialogFragment in case the host needs to query it. */    
public interface NoticeDialogListener {        
	public void onDialogPositiveClick(DialogFragment dialog);        
	public void onDialogNegativeClick(DialogFragment dialog);    
	}        
// Use this instance of the interface to deliver action events    
NoticeDialogListener mListener;        
// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener    
@Override    
public void onAttach(Activity activity) {        
	super.onAttach(activity);        
	// Verify that the host activity implements the callback interface       
	try {            
		// Instantiate the NoticeDialogListener so we can send events to the host            
		mListener = (NoticeDialogListener) activity;        
		} 
	catch (ClassCastException e) {            
		// The activity doesn't implement the interface, throw exception            
		throw new ClassCastException(activity.toString()                    
				+ " must implement NoticeDialogListener");        
		}    
	}
	

/*
public DlgSelVessel(Activity a) {
	super(a);
	// TODO Auto-generated constructor stub
	this.c = a;
	}*/
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {    
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());   
	// Get the layout inflater    
	LayoutInflater inflater = getActivity().getLayoutInflater();    
	// Inflate and set the layout for the dialog    
	// Pass null as the parent view because its going in the dialog layout 
	View view = getActivity().getLayoutInflater().inflate(R.layout.dlg_select_vessels, null);
	final CheckBox cb1=(CheckBox) view.findViewById(R.id.cbVessel1);
	final CheckBox cb2=(CheckBox) view.findViewById(R.id.cbVessel2);
	final CheckBox cb3=(CheckBox) view.findViewById(R.id.cbVessel3);
	final CheckBox cb4=(CheckBox) view.findViewById(R.id.cbVessel4);
	final CheckBox cb5=(CheckBox) view.findViewById(R.id.cbVessel5);
	final CheckBox cb6=(CheckBox) view.findViewById(R.id.cbVessel6);
	final CheckBox cb7=(CheckBox) view.findViewById(R.id.cbVessel7);
	final CheckBox cb8=(CheckBox) view.findViewById(R.id.cbVessel8);
	final CheckBox cb9=(CheckBox) view.findViewById(R.id.cbVessel9);
	final CheckBox cb10=(CheckBox) view.findViewById(R.id.cbVessel10);
	final CheckBox cb11=(CheckBox) view.findViewById(R.id.cbVessel11);
	final CheckBox cb12=(CheckBox) view.findViewById(R.id.cbVessel12);
	final TextView tv1=(TextView) view.findViewById(R.id.messagePresCal);
	//final CheckBox cb13=(CheckBox) view.findViewById(R.id.cbBarGraph);
	Global gs = (Global) getActivity().getApplication();
	builder.setView(view)//inflater.inflate(R.layout.dlg_select_vessels, null))    
	// Add action buttons
		.setTitle("Select Vessels to be Digested ")
		.setPositiveButton("Start", new DialogInterface.OnClickListener() { 
					
		@Override               
		public void onClick(DialogInterface dialog, int id) {
			Global gs = (Global) getActivity().getApplication();
			int int1;
			/*
					if(cb1.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[0]=true;
					else
						gs.mQblockSettings.bSelectedVessel[0]=false;
					
					if(cb2.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[1]=true;
					else
						gs.mQblockSettings.bSelectedVessel[1]=false;
					if(cb3.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[2]=true;
					else
						gs.mQblockSettings.bSelectedVessel[2]=false;
					if(cb4.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[3]=true;
					else
						gs.mQblockSettings.bSelectedVessel[3]=false;
					if(cb5.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[4]=true;
					else
						gs.mQblockSettings.bSelectedVessel[4]=false;
					if(cb6.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[5]=true;
					else
						gs.mQblockSettings.bSelectedVessel[5]=false;
					if(cb7.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[6]=true;
					else
						gs.mQblockSettings.bSelectedVessel[6]=false;
					if(cb8.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[7]=true;
					else
						gs.mQblockSettings.bSelectedVessel[7]=false;
					if(cb9.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[8]=true;
					else
						gs.mQblockSettings.bSelectedVessel[8]=false;
					if(cb10.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[9]=true;
					else
						gs.mQblockSettings.bSelectedVessel[9]=false;
					if(cb11.isChecked()) 
						gs.mQblockSettings.bSelectedVessel[10]=true;
					else
						gs.mQblockSettings.bSelectedVessel[10]=false;
						
			
			gs.SaveSettings();
			*/
			// Send the positive button event back to the host activity                       
			mListener.onDialogPositiveClick(DlgSelVessel.this);                  
			} 
		
		})           
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {               
			public void onClick(DialogInterface dialog, int id) {                   
				//LoginDialogFragment.this.getDialog().cancel();               
				}           
			});
	Log.d(TAG, "--- ON create dlg 1---");
    if(gs.mQblockSettings.bSelectedVessel[0])
    	cb1.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[1])
    	cb2.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[2])
    	cb3.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[3])
    	cb4.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[4])
    	cb5.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[5])
    	cb6.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[6])
    	cb7.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[7])
    	cb8.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[8])
    	cb9.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[9])
    	cb10.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[10])
    	cb11.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[13])
    	cb12.setChecked(true);
    WQblockBluetooth aa=(WQblockBluetooth) this.getActivity();
    String st=aa.mQblockMethod.mRecipeName;
    if(st.equals("presCal.rcp")){
    	tv1.setText("Warning: This Recipe made to Calibrate Pressure Sensor..!  Click Start to Continue Or Cancel to cancel");//Integer.toString(mBlthService.mTemp));
    	builder.setTitle("Warning: This Recipe made to Calibrate Pressure Sensor..!  Click Start to Continue Or Cancel to cancel");//Integer.toString(mBlthService.mTemp));
    	Log.d(TAG,"---------pres cal method");
    }
    else{
    	Log.d(TAG,"---------not        pres cal method");
    }
    /*if(gs.mQblockSettings.bSelectedVessel[11])
    	cb12.setChecked(true);
    if(gs.mQblockSettings.bSelectedVessel[12])
    	cb13.setChecked(true);
    */
	return builder.create();

}



@Override
public void onResume()
{
    super.onResume();
    Window window = getDialog().getWindow();
    window.setLayout(1000, 700);
    //window.setGravity(Gravity.CENTER);
    //TODO:
}  
/*
protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dlg_select_vessels);
		yes = (Button) findViewById(R.id.btSaveVessels);
		no = (Button) findViewById(R.id.btCancelVessels);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);

}
*/
@Override
public void onClick(View v) {
	switch (v.getId()) {
	/*	
	case R.id.btSaveVessels:
			c.finish();
			break;
		case R.id.btCancelVessels:
			dismiss();
			break;
	*/	default:
			break;
	}
	dismiss();
	}
}

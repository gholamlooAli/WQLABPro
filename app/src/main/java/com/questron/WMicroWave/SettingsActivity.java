package com.questron.WMicroWave;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;

//import com.example.android.BluetoothChat.DeviceListActivity;
import com.questron.WMicroWave.R;
import com.questron.WMicroWave.R.drawable;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity{
	
	private final String swVersion="Version 1.0.7  Release Jun 16 , 2016";
	
	private Spinner chemist_spinner ;//= (Spinner) findViewById(R.id.settings_chemist_spinner);
	private Spinner alias_spinner ;
	private ArrayAdapter<String> aliasSpinnerAdapter;
	private BluetoothAdapter mBtAdapter;
	private List<String> listPairedName;
	private List<String> listAlias;
	private ArrayAdapter<String> spinnerAdapter=null;
	// Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final String TAG = "BluetoothChat";
    private String org_caption;
    public void checkMaxNo(View v,int int2){
		Global gs = (Global) getApplication();
		TextView view = (TextView) v;
		String st = view.getText().toString();
        int int1;
        try {
        	if(st.isEmpty()){
        		Toast.makeText(getApplicationContext(), "Warning: Not a Valid Value...", Toast.LENGTH_LONG).show();
        		view.setText("000");
        	}
        	else {
        			int1=Integer.parseInt(st);
        			if(int1>int2){
        				Toast.makeText(getApplicationContext(), "Warning: Entetred Vlue is Higher than Maximum Limit..", Toast.LENGTH_LONG).show();
        				view.setText(Integer.toString(int2));
        			}
        	}
        		//Log.d(TAG,"tartemp="+mm.TarTemp[0]);
        } catch(NumberFormatException nfe) {
		          // Handle parse error.
    		    	return ;
        }      
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Global gs = (Global) getApplication();
        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.settings);
        Log.d(TAG, "--- ON create Settings 1---");
        TextView et=(TextView) findViewById(R.id.settings_org_name);
        et.setText(gs.mQblockSettings.mOrganization);
        et=(TextView) findViewById(R.id.temp_limit);
        et.setText(Integer.toString(gs.mQblockSettings.TempLimit));
        et=(TextView) findViewById(R.id.vesselVolume);
        et.setText(Integer.toString(gs.mQblockSettings.VesselVolume));
        et=(TextView) findViewById(R.id.vesselNo);
        et.setText(Integer.toString(gs.mQblockSettings.VesselNo));
        et=(TextView) findViewById(R.id.valuePurgeTemp);
        et.setText(Integer.toString(gs.mQblockSettings.PurgeTemp));
        et=(TextView) findViewById(R.id.valuePurgeCycle);
        et.setText(Integer.toString(gs.mQblockSettings.PurgeCycle));
        et=(TextView) findViewById(R.id.valueDeltaT);
        et.setText(Integer.toString(gs.mQblockSettings.DeltaT));
        //find the microwave model and firmware revision
        String st=null;
        if(gs.mQblockSettings.SystemConfig==1){
        	st="OPEN_POWERONLY";
        }
        else if(gs.mQblockSettings.SystemConfig==2){
        	st="CLOSED_POWERONLY";
        }
        else if(gs.mQblockSettings.SystemConfig==3){
        	st="OPEN_OPTICALTEMP";
        }
        else if(gs.mQblockSettings.SystemConfig==4){
        	st="IRONLY";	
        }
        else if(gs.mQblockSettings.SystemConfig==5){
        	st="IR_PRES";	
        }
        else if(gs.mQblockSettings.SystemConfig==6){
        	st="IR_OPTTEMP";	
        }
        else if(gs.mQblockSettings.SystemConfig==7){
        	st="IR_PRES_OPTTEMP";	
        }
        String st2="firmWare=1."+gs.mQblockSettings.swRevision+"  Model="+st;
        et=(TextView) findViewById(R.id.fw_model);
        et.setText(st2);
        et=(TextView) findViewById(R.id.sw_version);
        et.setText(swVersion);
	    if (gs.CPI)	{org_caption = "Organization";}
	    	//textview1.setText("Organization Name");}
	    else 		{org_caption = "Organisation";}
	   // textview1.setText("Organisation Name");}
	    CheckBox cb1=(CheckBox) findViewById(R.id.cb_settings_Pressure);
	    if(gs.mQblockSettings.bSelectedVessel[15])
	    	cb1.setChecked(true);
	    CheckBox cb2=(CheckBox) findViewById(R.id.checkInsitu);
	    if(gs.mQblockSettings.bSelectedVessel[14])
	    	cb2.setChecked(true);
	    Log.d(TAG, "--- ON create Settings 12---");
	    TextView textview1=(TextView)findViewById(R.id.org_name_setting);
	    textview1.setText(org_caption + " Name");	
        listAlias = new ArrayList<String>();
        listPairedName = new ArrayList<String>();
		// Find and set up the ListView for newly discovered devices
        aliasSpinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listAlias);
        aliasSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alias_spinner = (Spinner) findViewById(R.id.settings_alias_spinner);
        alias_spinner.setAdapter(aliasSpinnerAdapter);
        alias_spinner.setSelection(0);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        fillAliasList();
        spinnerAdapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1,gs.mQblockSettings.chemist_list);//list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chemist_spinner = (Spinner) findViewById(R.id.settings_chemist_spinner);
        chemist_spinner.setAdapter(spinnerAdapter);
        chemist_spinner.setSelection(gs.mQblockSettings.selected_chemist);
        
        TextView view = (TextView) findViewById(R.id.valuePurgeCycle);
        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
        	@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
        		checkMaxNo(v,100);
    	    	
			}
        });
        view = (TextView) findViewById(R.id.valuePurgeTemp);
        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
        	@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
        		checkMaxNo(v,250);
    	    	
			}
        });
        view = (TextView) findViewById(R.id.valueDeltaT);
        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
        	@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
        		checkMaxNo(v,100);
    	    	
			}
        });
        Log.d(TAG, "--- ON create Settings 2---");
        //**************************************to show files in settings directory
        /*Spinner files_spinner = (Spinner) findViewById(R.id.settings_blocks_spinner);
        List<String> files_list = new ArrayList<String>();
	    File path=Environment.getExternalStoragePublicDirectory("settings");
	    path.mkdirs();
	    File mfile=new File(path.toString());
	    File[] file_list=mfile.listFiles();
	    if(file_list!=null){
	    	for( int i=0; i< file_list.length; i++){
	    		files_list.add( file_list[i].getName() );
	    	}
	    }
	    ArrayAdapter<String> filesAdapter = new ArrayAdapter<String>(this
	    		,android.R.layout.simple_list_item_1,files_list);
	    filesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    files_spinner.setAdapter(filesAdapter);
        *///********************************************************************	
        	
        	
        setResult(Activity.RESULT_CANCELED);
        // Initialize the button to perform device discovery
        Button locateButton = (Button) findViewById(R.id.settings_locate_button);
        locateButton.requestFocus();
        locateButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
            	//Intent serverIntent = null;
                Intent intent = new Intent(v.getContext(), DeviceListActivity.class);
                startActivityForResult(intent,REQUEST_CONNECT_DEVICE_SECURE);
                //v.setVisibility(View.GONE);
            }
        });
        Button deleteAliasBut=(Button) findViewById(R.id.settings_alias_remove_button);
        deleteAliasBut.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        		AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
                alert.setTitle("Remove Alias Name for Device");   
                //alert.setMessage("Add a Chemist");   
               // Global gs = (Global) getApplication();
                String st=listPairedName.get(alias_spinner.getSelectedItemPosition());
                //final EditText input = new EditText(v.getContext());   
                alert.setMessage("You are about to delete alias name for device  "+st);   
                   
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){
                    	Global gs = (Global) getApplication();
                    	try{
                    		String st=listPairedName.get(alias_spinner.getSelectedItemPosition());
                    		for(int i=0;i<gs.mQblockSettings.alias_list_name.size();i++){
                        		if(gs.mQblockSettings.alias_list_name.get(i).equals(st)){
                        			gs.mQblockSettings.alias_list.remove(i);
                        			gs.mQblockSettings.alias_list_name.remove(i);
                        			gs.SaveSettings();
                        			fillAliasList();
                        		}
                    		}
                    	}
                    	catch(Exception e){
                    		
                    	}
                		//spinnerAdapter.notifyDataSetChanged();  
                    }   
                });   
                   
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        //canceled   
                    }   
                });   
                alert.show();   

        		
        		
        		
            }
        });
        Button backBt = (Button) findViewById(R.id.settings_back_button);
        backBt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	    	finish();
            }
        });
        Log.d(TAG, "--- ON create Settings 3---");
        Button addAliasBut=(Button) findViewById(R.id.settings_add_alias_button);
        addAliasBut.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        		AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
                alert.setTitle("Add Alias Name for Device "+listPairedName.get(alias_spinner.getSelectedItemPosition()));   
                //alert.setMessage("Add a Chemist");   
                final EditText input = new EditText(v.getContext());   
               input.setInputType(InputType.TYPE_CLASS_TEXT);
                alert.setView(input);   
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        String st = input.getText().toString();
                        if(st.isEmpty())
                			return;
                        BasicNameValuePair nv=new BasicNameValuePair (listPairedName.get(alias_spinner.getSelectedItemPosition()),st);
                        Global gs = (Global) getApplication();
                        //List<String> listAlias = new ArrayList<String>();
                        //if(gs.mQblockSettings.alias_list.size()>0)
                        //	listAlias=gs.mQblockSettings.alias_list;
                        //check if already alias exist
                        String name=nv.getName();
                        addAlias(name,nv.getValue());
                        gs.SaveSettings();
                		fillAliasList();
                		//gs.mQblockSettings.selected_chemist=gs.mQblockSettings.chemist_list.size();
                		//chemist_spinner.setSelection(gs.mQblockSettings.selected_chemist);
                        //spinnerAdapter.notifyDataSetChanged();
                           
                    }   
                });   
                   
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        //canceled   
                    }   
                });   
                alert.show();   
            }
        });
        
        Button deleteChemistBut=(Button) findViewById(R.id.settings_chemist_remove_button);
        deleteChemistBut.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        		AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
                alert.setTitle("Remove Chemist Name from List");   
                //alert.setMessage("Add a Chemist");   
                Global gs = (Global) getApplication();
                String st=gs.mQblockSettings.chemist_list.get(gs.mQblockSettings.selected_chemist);
                //final EditText input = new EditText(v.getContext());   
               // input.setInputType(InputType.TYPE_CLASS_TEXT);
                alert.setMessage("You are about to delete   "+st + "   from list");   
                //alert.setView(input);   
                   
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){
                    	Global gs = (Global) getApplication();
                        gs.mQblockSettings.chemist_list.remove(gs.mQblockSettings.selected_chemist);
                    	gs.bSettingsChanged=true;
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
        });
        Button addChemistBut=(Button) findViewById(R.id.settings_add_chemist_button);
        addChemistBut.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        		AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
                alert.setTitle("Add Chemist Name to List");   
                //alert.setMessage("Add a Chemist");   
                final EditText input = new EditText(v.getContext());   
              input.setInputType(InputType.TYPE_CLASS_TEXT);
                alert.setView(input);   
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        String st = input.getText().toString();
                        if(st.isEmpty())
                			return;
                        Global gs = (Global) getApplication();
                		gs.mQblockSettings.chemist_list.add(st);
                		gs.bSettingsChanged=true;
                		gs.mQblockSettings.selected_chemist=gs.mQblockSettings.chemist_list.size();
                		chemist_spinner.setSelection(gs.mQblockSettings.selected_chemist);
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
        });
        Button updateOrgBut=(Button) findViewById(R.id.btUpdate_org);
        updateOrgBut.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        		AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
                alert.setTitle("Update " + org_caption + " Name");   
                //alert.setMessage("Add a Chemist");   
                final EditText input = new EditText(v.getContext());   
               input.setInputType(InputType.TYPE_CLASS_TEXT);
                alert.setView(input);   
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        String st = input.getText().toString();
                        if(st.isEmpty())
                			return;
                        Global gs = (Global) getApplication();
                		gs.mQblockSettings.mOrganization=st;
                		gs.SaveSettings();
                		TextView et=(TextView) findViewById(R.id.settings_org_name);
                		et.setText(st);
                           
                    }   
                });   
                   
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        //canceled   
                    }   
                });   
                alert.show();   
            }
        });
        
        Button updateTempLimit=(Button) findViewById(R.id.temp_limit_button);
        updateTempLimit.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Global gs = (Global) getApplication();
        		AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
                alert.setTitle("Current Maximum Temperature Limit is " + gs.mQblockSettings.TempLimit + " C");   
                alert.setMessage("To Change, Enter New Value and Click OK");   
                final EditText input = new EditText(v.getContext()); 
                //input.setImeOptions(EditorInfo.IME_ACTION_DONE);//.setInputType(InputType.TYPE_CLASS_NUMBER);
               input.setInputType(InputType.TYPE_CLASS_NUMBER);
                //input.setInputType(InputType.TYPE_CLASS_TEXT);
                alert.setView(input);   
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        String st = input.getText().toString();
                        int mytempLimit;
                        try {
                        	mytempLimit = Integer.parseInt(st);
                        
                        } catch(NumberFormatException nfe) {
                          // Handle parse error.
                        	return ;
                        }
                        if((mytempLimit<0) || (mytempLimit>400)){
                        	Toast.makeText(getApplicationContext(), "Warning: value is out of range, Maximum = 400 C...", Toast.LENGTH_LONG).show();
                        	return;
                        }	
                        Global gs = (Global) getApplication();
                		gs.mQblockSettings.TempLimit=mytempLimit;
                		gs.SaveSettings();
                		TextView et=(TextView) findViewById(R.id.temp_limit);
                		et.setText(st);
                           
                    }   
                });   
                   
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        //canceled   
                    }   
                });   
                alert.show();   
            }
        });
        Log.d(TAG, "--- ON create Settings 4---");
        Button updateVesVol=(Button) findViewById(R.id.ves_vol_button);
        updateVesVol.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Global gs = (Global) getApplication();
        		AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
                alert.setTitle("Current Vessel Volume is " + gs.mQblockSettings.VesselVolume + " (ml)");   
                alert.setMessage("To Change, Enter New Value and Click OK");   
                final EditText input = new EditText(v.getContext()); 
                //input.setImeOptions(EditorInfo.IME_ACTION_DONE);//.setInputType(InputType.TYPE_CLASS_NUMBER);
               input.setInputType(InputType.TYPE_CLASS_NUMBER);
                //input.setInputType(InputType.TYPE_CLASS_TEXT);
                alert.setView(input);   
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        String st = input.getText().toString();
                        int mytempLimit;
                        try {
                        	mytempLimit = Integer.parseInt(st);
                        
                        } catch(NumberFormatException nfe) {
                          // Handle parse error.
                        	return ;
                        }
                        if((mytempLimit<0) || (mytempLimit>120)){
                        	Toast.makeText(getApplicationContext(), "Warning: value is out of range, Maximum = 120 ml...", Toast.LENGTH_LONG).show();
                        	return;
                        }	
                        Global gs = (Global) getApplication();
                		gs.mQblockSettings.VesselVolume=mytempLimit;
                		gs.SaveSettings();
                		TextView et=(TextView) findViewById(R.id.vesselVolume);
                		et.setText(st);
                           
                    }   
                });   
                   
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        //canceled   
                    }   
                });   
                alert.show();   
            }
        });
        Button updateVesNo=(Button) findViewById(R.id.ves_no_button);
        updateVesNo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Global gs = (Global) getApplication();
        		AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
                alert.setTitle("Current Number of Vessel is " + gs.mQblockSettings.VesselNo + "");   
                alert.setMessage("To Change, Enter New Value and Click OK");   
                final EditText input = new EditText(v.getContext()); 
                //input.setImeOptions(EditorInfo.IME_ACTION_DONE);//.setInputType(InputType.TYPE_CLASS_NUMBER);
               input.setInputType(InputType.TYPE_CLASS_NUMBER);
                //input.setInputType(InputType.TYPE_CLASS_TEXT);
                alert.setView(input);   
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        String st = input.getText().toString();
                        int mytempLimit;
                        try {
                        	mytempLimit = Integer.parseInt(st);
                        
                        } catch(NumberFormatException nfe) {
                          // Handle parse error.
                        	return ;
                        }
                        if((mytempLimit<1) || (mytempLimit>12)){
                        	Toast.makeText(getApplicationContext(), "Warning: value is out of range, Maximum = 12...", Toast.LENGTH_LONG).show();
                        	return;
                        }	
                        Global gs = (Global) getApplication();
                		gs.mQblockSettings.VesselNo=mytempLimit;
                		gs.SaveSettings();
                		TextView et=(TextView) findViewById(R.id.vesselNo);
                		et.setText(st);
                           
                    }   
                });   
                   
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
                    public void onClick(DialogInterface dialog, int whichButton){   
                        //canceled   
                    }   
                });   
                alert.show();   
            }
        });
        chemist_spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id) {
                // TODO Auto-generated method stub
    //Toast.makeText(getBaseContext(),str[position], Toast.LENGTH_SHORT).show();
            	Global gs = (Global) getApplication();
        		gs.mQblockSettings.selected_chemist=position;
        		gs.bSettingsChanged=true;
                }

               @Override
               public void onNothingSelected(AdapterView<?> arg0) {
               // TODO Auto-generated method stub

               }

          });

     // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
	}
	 @Override
	    public void onDestroy() {
	        super.onDestroy();
	        // Stop the Bluetooth chat services
	        //if (mBlthService != null) mBlthService.stop();
	        Global gs = (Global) getApplication();
	        
	        CheckBox cb1=(CheckBox) findViewById(R.id.cb_settings_Pressure);
	        
	        TextView view = (TextView) findViewById(R.id.valuePurgeCycle);
	        checkMaxNo(view,100);
	        String st = view.getText().toString();
	        gs.mQblockSettings.PurgeCycle=Integer.parseInt(st);
	        
	        view = (TextView) findViewById(R.id.valuePurgeTemp);
	        checkMaxNo(view,250);
	        st = view.getText().toString();
	        gs.mQblockSettings.PurgeTemp=Integer.parseInt(st);
	        
	        view = (TextView) findViewById(R.id.valueDeltaT);
	        checkMaxNo(view,100);
	        st = view.getText().toString();
	        gs.mQblockSettings.DeltaT=Integer.parseInt(st);
	        Log.d(TAG,"gs.mQblockSettings.deltat="+gs.mQblockSettings.DeltaT);
	        
		    if(cb1.isChecked())
		    	gs.mQblockSettings.bSelectedVessel[15]=true;
		    else
		    	gs.mQblockSettings.bSelectedVessel[15]=false;
		    
		    CheckBox cb2=(CheckBox) findViewById(R.id.checkInsitu);
		    if(cb2.isChecked())
		    	gs.mQblockSettings.bSelectedVessel[14]=true;
		    else
		    	gs.mQblockSettings.bSelectedVessel[14]=false;
	        //gs.bSettingsChanged=true;
	        gs.SaveSettings();
	        Log.d(TAG, "--- ON DESTROY Settings---");
	    }
	// The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
          /*  if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }*/
        }
    };
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
            	String address = data.getExtras()
                        .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            	// Create the result Intent and include the MAC address
                Intent intent = new Intent();
                intent.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS, address);

                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                finish();
                //connectDevice(data, true);
            }
            break;
        
        }
    }
    public String addAlias(String st,String st2){
    	Global gs = (Global) getApplication();
    	String mst=null;
    	if(gs.mQblockSettings.alias_list.size()>0){
    		for(int i=0;i<gs.mQblockSettings.alias_list.size();i++){
    			if(gs.mQblockSettings.alias_list_name.get(i).equals(st)){
    				gs.mQblockSettings.alias_list.set(i, st2);
    				return mst;
    			}
    			
    		}
    	}
    	gs.mQblockSettings.alias_list_name.add(st);
    	gs.mQblockSettings.alias_list.add(st2);
    	
    	label: 
    	return mst;
    }
    public String getAlias(String st){
    	Global gs = (Global) getApplication();
    	String mst=null;
    	if(gs.mQblockSettings.alias_list.size()>0){
    		for(int i=0;i<gs.mQblockSettings.alias_list.size();i++){
    			if(gs.mQblockSettings.alias_list_name.get(i).equals(st))
    				mst=gs.mQblockSettings.alias_list.get(i);
    		}
    	}
    	return mst;
    }
    /*
    public int getAliasIndex(String st){
    	Global gs = (Global) getApplication();
    	if(gs.mQblockSettings.alias_list.size()==0)
    		return -1;
    	
    	for(int i=0;i<gs.mQblockSettings.alias_list.size();i++){
    		if(gs.mQblockSettings.alias_list_name.get(i).equals(st))
    				return i;
    	}
    	return -1;
    }
    */
    private void fillAliasList(){
    	Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
    	// If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
        	Global gs = (Global) getApplication();
        	listAlias.clear();
        	listPairedName.clear();
            //findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
            	listAlias.add(device.getName() + "    \n" +getAlias( device.getName()));
                //Log.d(TAG, "pairsize="+listAlias.size());
                listPairedName.add(device.getName());
            }
        }
        else {
            //String noDevices = getResources().getText(R.string.none_paired).toString();
            //aliasSpinnerAdapter.add(get("noDevices"));
        }
        this.aliasSpinnerAdapter.notifyDataSetChanged();
    	
    }

    
}
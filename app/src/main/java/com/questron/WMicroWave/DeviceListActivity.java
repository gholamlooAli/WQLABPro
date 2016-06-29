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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.questron.WMicroWave.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class DeviceListActivity extends Activity {
    // Debugging
	// Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    //private Boolean 		mLockSoftWare=false;
    // Member fields
    private BluetoothAdapter mBtAdapter;
    //private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<Model> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    
       // setListAdapter(adapter);

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.mLockSoftWare=true;
        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Initialize the button to perform device discovery
        Button scanButton = (Button) findViewById(R.id.button_scan);
        Global gs = (Global) getApplication();
        if(gs.mLockSoftWare)
        	scanButton.setVisibility(View.GONE);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });
        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        //mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name2);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        if(gs.mLockSoftWare)
        	mPairedDevicesArrayAdapter=new PairedDevicesArrayAdapter(this,this.getFixedPaired());
        else
        	mPairedDevicesArrayAdapter=new PairedDevicesArrayAdapter(this,getPaired());
        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        

        // Get a set of currently paired devices
     /*   Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        

        
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                
            }
            View v=pairedListView.getChildAt(1);
            Log.d(TAG, "row no====");
            if(v==null)
            	return;
            CheckedTextView mn=(CheckedTextView) v;
            mn.setChecked(false);
            
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
*/    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        
        this.unregisterReceiver(mReceiver);
        Global gs = (Global) getApplication();
        //if(gs.bSettingsChanged){
        	Log.d(TAG, "paircount="+mPairedDevicesArrayAdapter.getCount()+"settings has changed");
        	for(int i=0;i<8;i++)
        		gs.mQblockSettings.bSelectedBlock[i]=false;
        	for(int i=0;i<mPairedDevicesArrayAdapter.getCount();i++){
        		if(mPairedDevicesArrayAdapter.getItem(i).isSelected())
        			gs.mQblockSettings.bSelectedBlock[i]=true;
        		
        	gs.SaveSettings();//.bSettingsChanged=true;
        		if(i>7)
        			return;
        	}
       // }
    }
    private List<Model> getPaired() {
        List<Model> listPaired = new ArrayList<Model>();
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
        	Global gs = (Global) getApplication();
        	
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                listPaired.add(get(device.getName() + "\n" + device.getAddress()));
                Log.d(TAG, "pairsize="+listPaired.size());
                if(listPaired.size()<9 && listPaired.size()>0) {
                	if(gs.mQblockSettings.bSelectedBlock[listPaired.size()-1]==true)
                		listPaired.get(listPaired.size()-1).setSelected(true);
                	else
                		listPaired.get(listPaired.size()-1).setSelected(false);
                }
            }
        //list.add(get("iPhone"));
        // Initially select one of the items
        
        }
        else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            listPaired.add(get("noDevices"));
        }
        return listPaired;
      }
    private List<Model> getFixedPaired() {
        List<Model> listPaired = new ArrayList<Model>();
        // Get a set of currently paired devices
        Global gs = (Global) getApplication();
        	
        findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
        if(gs.deviceName_list.size()>0){
        	for(int i=0;i<gs.deviceName_list.size();i++){
        		listPaired.add(get(gs.deviceName_list.get(i)+"\n"+gs.deviceAddress_list.get(i)));
        	}
        }
        Log.d(TAG, "pairsize="+listPaired.size());
        if(listPaired.size()<9 && listPaired.size()>0) {
           	for(int i=0;i<listPaired.size();i++){
           		if(gs.mQblockSettings.bSelectedBlock[i]==true)
           			listPaired.get(i).setSelected(true);
           		else
           			listPaired.get(i).setSelected(false);
           	}
        }
        return listPaired;
      }

      private Model get(String s) {
        return new Model(s);
      }
    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
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
            }
        }
    };

}

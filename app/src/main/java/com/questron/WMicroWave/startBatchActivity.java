package com.questron.WMicroWave;


	import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

	import com.questron.WMicroWave.R;

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
import android.media.MediaScannerConnection;
	import android.os.Bundle;
import android.os.Environment;
	import android.os.Handler;
	import android.os.Message;
import android.text.format.Time;
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
	import android.widget.ArrayAdapter;
	import android.widget.Button;
	import android.widget.EditText;
	import android.widget.ListView;
import android.widget.Spinner;
	import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

	public class startBatchActivity extends Activity{
		private static final String TAG = "digest";
		private Spinner chemist_spinner ;//= (Spinner) findViewById(R.id.settings_chemist_spinner);
		private ArrayAdapter<String> spinnerAdapter=null;
		private String mRecipeName;
		private String mRecipeName2;
		private String mRecipeChemistName;
		private String mRecipeChemistName2;
		private int 	mSelectedVialNo;
		private int 	mSelectedVialNo2;
		private String	mRecipeTime;
		private Button mSendMethodButton;
		private Button mNewMethodButton;
		private Button mDeleteMethodButton;
		private QBlockMethod mQblockMethod;//=new QBlockMethod();
		//In an Activity
		private List<String> recipe_list;
	    private String[] mFileList;
	    private File mPath ;//= getExternalFilesDir(null);
	    private String mChosenFile;
	    private static final String FTYPE = ".rcp";    
	    private static final int DIALOG_LOAD_FILE = 1000;

	    private Activity mAct;
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.setTitle(" S A V I N G   B A T C H   F I L E");
	        // Setup the window
	        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	        setContentView(R.layout.start_batch);
	        Global gs = (Global) getApplication();
	        // Setup the window
	        chemist_spinner = (Spinner) findViewById(R.id.settings_chemist_spinner);
	        List<String> list = new ArrayList<String>();
	    	//list.add("list 1");
	    	spinnerAdapter = new ArrayAdapter<String>(this,
	        		android.R.layout.simple_list_item_1,gs.mQblockSettings.chemist_list);//list);
	        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        chemist_spinner.setAdapter(spinnerAdapter);
	        chemist_spinner.setSelection(gs.mQblockSettings.selected_chemist);

	    	//list.add("list 1");
	    	// Set result CANCELED in case the user backs out
	        setResult(Activity.RESULT_CANCELED);
	        
	     // Register for broadcasts when a device is discovered
	        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	        this.registerReceiver(mReceiver, filter);

	        // Register for broadcasts when discovery has finished
	        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	        this.registerReceiver(mReceiver, filter);
	        Intent i = getIntent();
	        //mQblockMethod = (QBlockMethod)i.getSerializableExtra("method");
	        //Log.d("TAG","this is intent"+mQblockMethod.TarTemp[0]);
	        
	        chemist_spinner.setOnItemSelectedListener(new OnItemSelectedListener()
	        {

	            @Override
	            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id) {
	                // TODO Auto-generated method stub
	            	Global gs = (Global) getApplication();
	        		gs.mQblockSettings.selected_chemist=position;
	        		gs.SaveSettings();
	                }

	               @Override
	               public void onNothingSelected(AdapterView<?> arg0) {
	               // TODO Auto-generated method stub

	               }


	              

	          });
	        	     // Initialise the send button with a listener that for click events
	        Button mProceedStartButton = (Button) findViewById(R.id.btProceedStart);
	        mProceedStartButton.requestFocus();
	        mProceedStartButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	Global gs = (Global) getApplication();
	            	Intent intent = new Intent();
	            	EditText et=(EditText) findViewById(R.id.batch_name);
	            	String st = et.getText().toString();
	            	if(st.length()==0){
	            		setResult(Activity.RESULT_CANCELED, intent);
	                	finish();
	            	}
	            		Log.d("thisis batchname="+st,"tt");
	            	intent.putExtra("BatchName", st);
	            	et=(EditText) findViewById(R.id.batch_note);
	            	st = et.getText().toString();
	            	Log.d("thisis batchname="+st,"tt");
	            	intent.putExtra("BatchNote", st);
	            	intent.putExtra("ChemistName", gs.mQblockSettings.chemist_list.get(gs.mQblockSettings.selected_chemist));
	            	setResult(Activity.RESULT_OK, intent);
                	finish();
                }   
            });   
	        Button mCancelStartButton = (Button) findViewById(R.id.btCancelStart);
	        mCancelStartButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	Intent intent = new Intent();
	           
	            	setResult(Activity.RESULT_CANCELED, intent);
                	finish();
                }   
            });   
		}
		  
		// The BroadcastReceiver that listens for discovered devices and
	    // changes the title when discovery is finished
	    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String action = intent.getAction();
	            
	            
	        }
	    };
	    
	    
	  

	
	
	}

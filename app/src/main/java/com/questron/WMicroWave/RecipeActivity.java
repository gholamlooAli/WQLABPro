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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
	import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

	public class RecipeActivity extends Activity {
		private static final String TAG = "digest";
		private String mRecipeName;
		private String mRecipeName2;
		private String mRecipeChemistName;
		private String mRecipeChemistName2;
		//private int 	mSelectedVialNo;
		//private int 	mSelectedVialNo2;
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

	    private Spinner recipes_spinner ;//= (Spinner) findViewById(R.id.settings_recipes_spinner);
		private ArrayAdapter<String> spinnerAdapter=null;
		private Activity mAct;
		
		
		public void checkMaxNo(View v,boolean hasFocus,int no){
			Global gs = (Global) getApplication();
			TextView view = (TextView) v;
			String st = view.getText().toString();
	        int int1,int2;
	        if(no==1)
	        	int2=gs.mQblockSettings.TempLimit;
	        else if (no==2)
	        	int2=600;
	        else
	        	int2=1200;//max power=1200
	        try {
	        	if(st.isEmpty()){
	        		if(no==1)
	        			Toast.makeText(getApplicationContext(), "Warning: Not a Valid Temperature Value...", Toast.LENGTH_LONG).show();
	        		else
	        			Toast.makeText(getApplicationContext(), "Warning: Not a Valid Time Value ...", Toast.LENGTH_LONG).show();
	        		view.setText("000");
	        	}
	        	else {
	        			int1=Integer.parseInt(st);
	        			if(int1>int2){
	        				if(no==1)
	        					Toast.makeText(getApplicationContext(), "Warning: Temperature Entetred is Higher than Maximum Limit..", Toast.LENGTH_LONG).show();
	        				else
	        					Toast.makeText(getApplicationContext(), "Warning: Time Entered is Higher than Maximum Limit..", Toast.LENGTH_LONG).show();
	        				view.setText(Integer.toString(int2));
	        			}
	        			//else
	        			//		Toast.makeText(getApplicationContext(), "Warning:good is ..", Toast.LENGTH_LONG).show();
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
	        this.setTitle(" D I G E S T I O N    R E C I P E");
	        // Setup the window
	        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	        setContentView(R.layout.digetion_recipe);
	        recipes_spinner = (Spinner) findViewById(R.id.recipe_list_spinner);
	        recipe_list = new ArrayList<String>();
	        mRecipeTime = null;
	        Global gs = (Global) getApplication();
	    	//list.add("list 1");
	        fillRecipeList();
	        TextView view = (TextView) findViewById(R.id.targetView1);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
	        		RadioButton rbTemp=(RadioButton) findViewById(R.id.rbTemp);
	    	    	if(rbTemp.isChecked())
	    	    		checkMaxNo(v,hasFocus,1);
	    	    	else
	    	    		checkMaxNo(v,hasFocus,3);
				}
	        });
	        view = (TextView) findViewById(R.id.targetView2);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
	        		RadioButton rbTemp=(RadioButton) findViewById(R.id.rbTemp);
	        		if(rbTemp.isChecked())
	    	    		checkMaxNo(v,hasFocus,1);
	    	    	else
	    	    		checkMaxNo(v,hasFocus,3);
				}
	        });
	        view = (TextView) findViewById(R.id.targetView3);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
	        		RadioButton rbTemp=(RadioButton) findViewById(R.id.rbTemp);
	        		if(rbTemp.isChecked())
	    	    		checkMaxNo(v,hasFocus,1);
	    	    	else
	    	    		checkMaxNo(v,hasFocus,3);
				}
	        });
	        view = (TextView) findViewById(R.id.targetView4);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
	        		RadioButton rbTemp=(RadioButton) findViewById(R.id.rbTemp);
	        		if(rbTemp.isChecked())
	    	    		checkMaxNo(v,hasFocus,1);
	    	    	else
	    	    		checkMaxNo(v,hasFocus,3);
				}
	        });
	        view = (TextView) findViewById(R.id.rampView1);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					checkMaxNo(v,hasFocus,2);
				}
	        });
	        view = (TextView) findViewById(R.id.rampView2);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					checkMaxNo(v,hasFocus,2);
				}
	        });
	        view = (TextView) findViewById(R.id.rampView3);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					checkMaxNo(v,hasFocus,2);
				}
	        });
	        view = (TextView) findViewById(R.id.rampView4);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					checkMaxNo(v,hasFocus,2);
				}
	        });
	        view = (TextView) findViewById(R.id.holdView1);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					checkMaxNo(v,hasFocus,2);
				}
	        });
	        view = (TextView) findViewById(R.id.holdView2);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					checkMaxNo(v,hasFocus,2);
				}
	        });
	        view = (TextView) findViewById(R.id.holdView3);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					checkMaxNo(v,hasFocus,2);
				}
	        });
	        view = (TextView) findViewById(R.id.holdView4);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					checkMaxNo(v,hasFocus,2);
				}
	        });
	        view = (TextView) findViewById(R.id.methodTempLim);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					checkMaxNo(v,hasFocus,1);
				}
	        });
	        view = (TextView) findViewById(R.id.methodPowerLim);
	        view.setOnFocusChangeListener(new View.OnFocusChangeListener(){
	        	@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					checkMaxNo(v,hasFocus,3);
				}
	        });
	        spinnerAdapter = new ArrayAdapter<String>(this,
	        		android.R.layout.simple_list_item_1,recipe_list);
	        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        recipes_spinner.setAdapter(spinnerAdapter);
	        	//recipes_spinner.setSelection(gs.mQblockSettings.selected_chemist);
	        // Set result CANCELED in case the user backs out
	        setResult(Activity.RESULT_CANCELED);
	        //mPath= new File(getExternalFilesDir(null), st);
	        Button backBt = (Button) findViewById(R.id.btRecipeBack);
            backBt.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                	    	finish();
                }
            });
	        // Initialize the new button to perform 
	        mNewMethodButton = (Button) findViewById(R.id.btRecipeNew);
	        mNewMethodButton.requestFocus();
	        mNewMethodButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	Global gs = (Global) getApplication();
	            	if(gs.bEnableSendBut)//exit the activity
	            		finish();
	            	mAct=(Activity) v.getContext();
	            	AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
	                alert.setTitle("Adding New Recipe: Enter the Recipe Name and Click Ok");   
	                //alert.setMessage("Enter the recipe name and click ok");   
	                final EditText input = new EditText(v.getContext());
	                input.setInputType(InputType.TYPE_CLASS_TEXT);
	                alert.setView(input);   
	                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
	                    public void onClick(DialogInterface dialog, int whichButton){
	                    	if(input.length()==0)
	                    		return;
	                    	mRecipeName2 = input.getText().toString()+".rcp";
	                        AlertDialog.Builder alert = new AlertDialog.Builder(mAct);   
	    	                alert.setTitle("Enter the Creator Name and Click Ok:");  
	    	                final EditText input = new EditText(mAct);
	    	                input.setInputType(InputType.TYPE_CLASS_TEXT);
	    	                alert.setView(input); 
	    	                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
	    	                    public void onClick(DialogInterface dialog, int whichButton){
	    	                    	if(input.length()==0)
	    	                    		return;
	    	                    	mRecipeChemistName = input.getText().toString();
	    	                    	Time mTime =new Time();
	    	                    	mTime.setToNow();
	    	                    	mRecipeTime=mTime.format3339(true);//.format2445();
	    	                    	mRecipeName=mRecipeName2;
	    	                    	loadParamToView();
	    	                    	
	    	                    	
	    	                    	
	    	                    	    
	    	                    }
	    	                });
	    	                
	    	                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
	    	                    public void onClick(DialogInterface dialog, int whichButton){   
	    	                        //canceled   
	    	                    }   
	    	                });   
	    	                alert.show();   
	    	                Log.d( TAG, "Pin Value ://////////// " + mRecipeName);
	    	        
	                    }   
	                });   
	                   
	                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
	                    public void onClick(DialogInterface dialog, int whichButton){   
	                        //canceled   
	                    }   
	                });   
	                alert.show();   
	                Log.d( TAG, "Pin Value ://////////// " + mRecipeName);
	            }
	        
	        }); 
	     // Register for broadcasts when a device is discovered
	        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	        this.registerReceiver(mReceiver, filter);

	        // Register for broadcasts when discovery has finished
	        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	        this.registerReceiver(mReceiver, filter);
	        Intent i = getIntent();
	        mQblockMethod = (QBlockMethod)i.getSerializableExtra("method");
	        mRecipeName=i.getStringExtra("RecipeName");
	        Log.d(TAG,"this is intent"+mQblockMethod.TarTemp[0]+"mae"+mRecipeName+"  ------vesno="+mQblockMethod.mVesselNo);
	        loadMethodToString(mQblockMethod);
	        
	        recipes_spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

	            @Override
	            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id) {
	                // TODO Auto-generated method stub
	            	Global gs = (Global) getApplication();
	            	//if(gs.bEnableSendBut&&(position==0))
	            	//	return;
	            	Log.d("TAG","this is positiion"+position);
	            	String st=recipe_list.get(position);
	            	if(openRecipe(st)){
	            		mRecipeName=st;
	            		mAct=(Activity) arg1.getContext();
	            		//setTitle("Recipe="+mRecipeName);
	            		TextView view = (TextView) findViewById(R.id.method_name);
	        	        view.setText(mRecipeName);
	        		}
	            	else 
	            		Toast.makeText(getApplicationContext(), "Warning: The Recipe File is Not a Valid and can Not be Opended...", Toast.LENGTH_LONG).show();
	            }

	               @Override
	               public void onNothingSelected(AdapterView<?> arg0) {
	               // TODO Auto-generated method stub

	               }

	          });
	        mDeleteMethodButton=(Button) findViewById(R.id.btRecipeDelete);
	        mDeleteMethodButton.setOnClickListener(new OnClickListener() {
	        	@Override
	        	public void onClick(View v) {
	        		
	        		AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
	                alert.setTitle("Delete Recipe");   
	                String st=recipe_list.get(recipes_spinner.getSelectedItemPosition());
	                alert.setMessage("Are You Sure to Delete Recipe="+st);   
	                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
	                    public void onClick(DialogInterface dialog, int whichButton){
	                    	//Global gs = (Global) getApplication();
	                    	//Log.w("this ","is position"+recipes_spinner.getSelectedItemPosition());
	                        //recipe_list.remove(recipes_spinner.getSelectedItemPosition());
	                    	//spinnerAdapter.notifyDataSetChanged(); 
	                    	String st=recipe_list.get(recipes_spinner.getSelectedItemPosition());
	                    	deleteRecipe(st);
	                    	fillRecipeList();
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
	     // Initialise the send button with a listener that for click events
	        mSendMethodButton = (Button) findViewById(R.id.btRecipeSave);
	        
            if(!gs.bEnableSendBut){
            	mSendMethodButton.setText("Save");//.setEnabled(false);
            }
            else{
            	mSendMethodButton.setText("Send");//.setEnabled(false);
            	mDeleteMethodButton.setEnabled(false);
            	//mNewMethodButton.setEnabled(false);
            	mNewMethodButton.setText("Cancel");
            	view = (TextView) findViewById(R.id.targetView1);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.targetView2);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.targetView3);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.targetView4);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.rampView1);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.rampView2);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.rampView3);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.rampView4);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.holdView1);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.holdView2);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.holdView3);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.holdView4);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.reagentml1);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.reagentml2);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.reagentml3);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.reagentml4);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.reagentml5);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.reagentName1);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.reagentName2);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.reagentName3);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.reagentName4);
            	view.setEnabled(false);
            	view = (TextView) findViewById(R.id.reagentName5);
            	view.setEnabled(false);
            	RadioButton rb=(RadioButton) findViewById(R.id.rbPower);
            	rb.setEnabled(false);
            	rb=(RadioButton) findViewById(R.id.rbTemp);
            	rb.setEnabled(false);
            	EditText et=(EditText) findViewById(R.id.sample_type_Text);
            	et.setEnabled(false);
            	et=(EditText) findViewById(R.id.recipe_note_Text);
            	et.setEnabled(false);
            	et=(EditText) findViewById(R.id.reagetns_Text);
            	et.setEnabled(false);
            }
	        mSendMethodButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	//Global gs = (Global) getApplication();
	            	//if(gs.bEnableSendBut){
	            	//	return;
	            	// Create the result Intent and include the method
	            	final QBlockMethod mm2=loadStringToMethod();
	                
	                Global gs = (Global) getApplication();
	                
	                if(gs.bEnableSendBut){//send recipe comes from details page
	                	AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
	                    alert.setTitle("Sending Recipe to Block");   
	                    alert.setMessage("Do You Want to Send Recipe File="+mRecipeName);//+".rcp");   
	                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
	                        public void onClick(DialogInterface dialog, int whichButton){   
	                        	Intent intent = new Intent();
	                        	intent.putExtra("method",mQblockMethod);// mm2);//put obj. in intent
	                        	intent.putExtra("RecipeName", mRecipeName);
	                        	// 	Set result and finish this Activity
	    	                	setResult(Activity.RESULT_OK, intent);
	    	                	finish();
	                        }   
	                    });   
	                       
	                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
	                        public void onClick(DialogInterface dialog, int whichButton){   
	                            //canceled   
	                        }   
	                    });   
	                    alert.show();   
	                }
	                else{	//save recipe button
	                	AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());   
	                    alert.setTitle("Saving Recipe File");   
	                    alert.setMessage("Do you want to Save File="+mRecipeName);//+".rcp");   
	                    //final EditText input = new EditText(v.getContext());   
	                    //alert.setView(input);   
	                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){   
	                        public void onClick(DialogInterface dialog, int whichButton){   
	                            
	                        	SaveRecipe();
	                        }   
	                    });   
	                       
	                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){   
	                        public void onClick(DialogInterface dialog, int whichButton){   
	                            //canceled   
	                        }   
	                    });   
	                    alert.show();   
	                	
	                }
	            }
	        });  
		
	        RadioGroup rg = (RadioGroup) findViewById(R.id.rgRecipeType);
	        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	        {
	            public void onCheckedChanged(RadioGroup group, int checkedId) {
	            	TextView view = (TextView) findViewById(R.id.rampView1);
	            	if(checkedId==R.id.rbTemp){
	            		//Toast.makeText(getBaseContext(), "temperat", Toast.LENGTH_SHORT).show();
	            		view.setVisibility(View.VISIBLE);//.setEnabled(true);
	            		view = (TextView) findViewById(R.id.targetView1);
	            		view.setNextFocusDownId(R.id.rampView1);
	            		view = (TextView) findViewById(R.id.targetView2);
	            		view.setNextFocusDownId(R.id.rampView2);
	            		view = (TextView) findViewById(R.id.targetView3);
	            		view.setNextFocusDownId(R.id.rampView3);
	            		view = (TextView) findViewById(R.id.targetView4);
	            		view.setNextFocusDownId(R.id.rampView4);
	            		
	            		view = (TextView) findViewById(R.id.rampView2);
	            		view.setVisibility(View.VISIBLE);
	            		view = (TextView) findViewById(R.id.rampView3);
	            		view.setVisibility(View.VISIBLE);
	            		view = (TextView) findViewById(R.id.rampView4);
	            		view.setVisibility(View.VISIBLE);
	            		view = (TextView) findViewById(R.id.targetTemp);
	            		view.setText("Temp");
	            		view = (TextView) findViewById(R.id.targetTemp1);
	            		view.setText("(\u2103)");
	            	}
	            	if(checkedId==R.id.rbPower){
	            		//Toast.makeText(getBaseContext(), "power", Toast.LENGTH_SHORT).show();
	            		view.setVisibility(View.INVISIBLE);//).setEnabled(false);
	            		view = (TextView) findViewById(R.id.targetView1);
	            		view.setNextFocusDownId(R.id.holdView1);
	            		view = (TextView) findViewById(R.id.targetView2);
	            		view.setNextFocusDownId(R.id.holdView2);
	            		view = (TextView) findViewById(R.id.targetView3);
	            		view.setNextFocusDownId(R.id.holdView3);
	            		view = (TextView) findViewById(R.id.targetView4);
	            		view.setNextFocusDownId(R.id.holdView4);
	            		view = (TextView) findViewById(R.id.rampView2);
	            		view.setVisibility(View.INVISIBLE);
	            		view = (TextView) findViewById(R.id.rampView3);
	            		view.setVisibility(View.INVISIBLE);
	            		view = (TextView) findViewById(R.id.rampView4);
	            		view.setVisibility(View.INVISIBLE);
	            		view = (TextView) findViewById(R.id.targetTemp);
	            		view.setText("Power");
	            		view = (TextView) findViewById(R.id.targetTemp1);
	            		view.setText("W");
	            	}
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
	    
	    public short textToShort(TextView view,int maxLimit){
	    	String st = view.getText().toString();
	    	int int1;
	    	short myint=0;
	        try {
	        	if(st.isEmpty())
	        		myint=0;
	        	else {
	        		int1=Integer.parseInt(st);
	        		if(int1>maxLimit)
	        			int1=maxLimit;
	        		myint = (short) int1;
	        	}
	        	Log.d(TAG,"myint="+myint);
	        } catch(NumberFormatException nfe) {
	          // Handle parse error.
	        	//return ;
	        }
	        return myint;
	    }
	    public QBlockMethod loadStringToMethod(){
	    	QBlockMethod mm=new QBlockMethod();
	    	Global gs = (Global) getApplication();
	    	int maxLimit;
	    	RadioButton rbTemp=(RadioButton) findViewById(R.id.rbTemp);
	    	if(rbTemp.isChecked()){
	    		mm.Type=0;
	    		maxLimit=gs.mQblockSettings.TempLimit;
	    	}
	    	else{
	    		mm.Type=1;
	    		maxLimit=1200;
	    	}
	    	int int1;
	    	//String st = view.getText().toString();
	        TextView view = (TextView) findViewById(R.id.targetView1);
	        mm.TarTemp[0]=textToShort(view,maxLimit);
	        view = (TextView) findViewById(R.id.rampView1);
	        mm.RampTime[0]=textToShort(view,600);
	        view = (TextView) findViewById(R.id.holdView1);
	        mm.HoldTime[0]=textToShort(view,600);
	        view = (TextView) findViewById(R.id.targetView2);
	        mm.TarTemp[1]=textToShort(view,maxLimit);
	        view = (TextView) findViewById(R.id.rampView2);
	        mm.RampTime[1]=textToShort(view,600);
	        view = (TextView) findViewById(R.id.holdView2);
	        mm.HoldTime[1]=textToShort(view,600);
	        view = (TextView) findViewById(R.id.targetView3);
	        mm.TarTemp[2]=textToShort(view,maxLimit);
	        view = (TextView) findViewById(R.id.rampView3);
	        mm.RampTime[2]=textToShort(view,600);
	        view = (TextView) findViewById(R.id.holdView3);
	        mm.HoldTime[2]=textToShort(view,600);
	        view = (TextView) findViewById(R.id.targetView4);
	        mm.TarTemp[3]=textToShort(view,maxLimit);
	        view = (TextView) findViewById(R.id.rampView4);
	        mm.RampTime[3]=textToShort(view,600);
	        view = (TextView) findViewById(R.id.holdView4);
	        mm.HoldTime[3]=textToShort(view,600);
	        
	        view = (TextView) findViewById(R.id.methodTempLim);
	        mm.TempLimit=textToShort(view,gs.mQblockSettings.TempLimit);
	        view = (TextView) findViewById(R.id.methodPowerLim);
	        mm.PowerLimit=textToShort(view,1200);
	        
	        view = (TextView) findViewById(R.id.reagentml1);
	        mm.mReagentVolume[0]=textToShort(view,100);
	        view = (TextView) findViewById(R.id.reagentml2);
	        mm.mReagentVolume[1]=textToShort(view,100);
	        view = (TextView) findViewById(R.id.reagentml3);
	        mm.mReagentVolume[2]=textToShort(view,100);
	        view = (TextView) findViewById(R.id.reagentml4);
	        mm.mReagentVolume[3]=textToShort(view,100);
	        view = (TextView) findViewById(R.id.reagentml5);
	        mm.mReagentVolume[4]=textToShort(view,100);
	        view = (TextView) findViewById(R.id.vesselNo1);
	        mm.mVesselNo=textToShort(view,12);
	        view = (TextView) findViewById(R.id.sample_amount_Text);
	        mm.mSampleAmount=textToShort(view,9500);
	        
	        EditText et=(EditText) findViewById(R.id.recipe_note_Text);
	        mm.mNote=et.getText().toString();
	        et=(EditText) findViewById(R.id.sample_type_Text);
	        mm.mSampleType=et.getText().toString();
	        et=(EditText) findViewById(R.id.reagetns_Text);
	        mm.mReagents=et.getText().toString();
	        
	        mm.mRecipeTime=this.mRecipeTime;
	        mm.mRecipeChemistName=this.mRecipeChemistName;
	        //Spinner sp=(Spinner) findViewById(R.id.recipe_vialmaterial_spinner);
	        //mm.mSelectedVialNo=sp.getSelectedItemPosition();
	        return mm;
	    }
	    public void loadMethodToString(QBlockMethod mm){
	    	//if(mm==null)
	    	//	return;
	    	//QBlockMethod mm=new QBlockMethod();
	    	RadioGroup rgType=(RadioGroup) findViewById(R.id.rgRecipeType);
	    	//TextView view1=null;
	    	TextView view1 = (TextView) findViewById(R.id.rampView1);
	    	if(mm.Type==0){//(short)1)
	    		rgType.check(R.id.rbTemp);
	    		view1.setVisibility(View.VISIBLE);//.setEnabled(true);
        		view1 = (TextView) findViewById(R.id.targetView1);
        		view1.setNextFocusDownId(R.id.rampView1);
        		view1 = (TextView) findViewById(R.id.targetView2);
        		view1.setNextFocusDownId(R.id.rampView2);
        		view1 = (TextView) findViewById(R.id.targetView3);
        		view1.setNextFocusDownId(R.id.rampView3);
        		view1 = (TextView) findViewById(R.id.targetView4);
        		view1.setNextFocusDownId(R.id.rampView4);
        		
        		view1 = (TextView) findViewById(R.id.rampView2);
        		view1.setVisibility(View.VISIBLE);
        		view1 = (TextView) findViewById(R.id.rampView3);
        		view1.setVisibility(View.VISIBLE);
        		view1 = (TextView) findViewById(R.id.rampView4);
        		view1.setVisibility(View.VISIBLE);
        		
	    		view1 = (TextView) findViewById(R.id.targetTemp);
        		view1.setText("Temp");
        		view1 = (TextView) findViewById(R.id.targetTemp1);
        		view1.setText("(\u2103)");
	    	}
	    	else{
	    		rgType.check(R.id.rbPower);
	    		view1.setVisibility(View.INVISIBLE);//).setEnabled(false);
        		view1 = (TextView) findViewById(R.id.targetView1);
        		view1.setNextFocusDownId(R.id.holdView1);
        		view1 = (TextView) findViewById(R.id.targetView2);
        		view1.setNextFocusDownId(R.id.holdView2);
        		view1 = (TextView) findViewById(R.id.targetView3);
        		view1.setNextFocusDownId(R.id.holdView3);
        		view1 = (TextView) findViewById(R.id.targetView4);
        		view1.setNextFocusDownId(R.id.holdView4);
        		view1 = (TextView) findViewById(R.id.rampView2);
        		view1.setVisibility(View.INVISIBLE);
        		view1 = (TextView) findViewById(R.id.rampView3);
        		view1.setVisibility(View.INVISIBLE);
        		view1 = (TextView) findViewById(R.id.rampView4);
        		view1.setVisibility(View.INVISIBLE);
	    		view1 = (TextView) findViewById(R.id.targetTemp);
        		view1.setText("Power");
        		view1 = (TextView) findViewById(R.id.targetTemp1);
        		view1.setText("W");
	    	}	
	        EditText view = (EditText) findViewById(R.id.targetView1);
	        view.setText(Integer.toString(mm.TarTemp[0]));
	        Log.d("mmTar0"," null"+mm.TarTemp[0]);
	        view = (EditText) findViewById(R.id.rampView1);
	        view.setText(Integer.toString(mm.RampTime[0]));
	        view = (EditText) findViewById(R.id.holdView1);
	        view.setText(Integer.toString(mm.HoldTime[0]));
	        
	        view = (EditText) findViewById(R.id.targetView2);
	        view.setText(Integer.toString(mm.TarTemp[1]));
	        view = (EditText) findViewById(R.id.rampView2);
	        view.setText(Integer.toString(mm.RampTime[1]));
	        view = (EditText) findViewById(R.id.holdView2);
	        view.setText(Integer.toString(mm.HoldTime[1]));
	        
	        view = (EditText) findViewById(R.id.targetView3);
	        view.setText(Integer.toString(mm.TarTemp[2]));
	        view = (EditText) findViewById(R.id.rampView3);
	        view.setText(Integer.toString(mm.RampTime[2]));
	        view = (EditText) findViewById(R.id.holdView3);
	        view.setText(Integer.toString(mm.HoldTime[2]));
	        
	        view = (EditText) findViewById(R.id.targetView4);
	        view.setText(Integer.toString(mm.TarTemp[3]));
	        view = (EditText) findViewById(R.id.rampView4);
	        view.setText(Integer.toString(mm.RampTime[3]));
	        view = (EditText) findViewById(R.id.holdView4);
	        view.setText(Integer.toString(mm.HoldTime[3]));
	       
	        //setTitle("Recipe="+mm.mRecipeName);
	        view = (EditText) findViewById(R.id.recipe_note_Text);
	        view.setText(mm.mNote);
	        view=(EditText) findViewById(R.id.sample_type_Text);
	        view.setText(mm.mSampleType);
	        view=(EditText) findViewById(R.id.reagetns_Text);
	        view.setText(mm.mReagents);
	        view = (EditText) findViewById(R.id.methodTempLim);
	        view.setText(Integer.toString(mm.TempLimit));
	        view = (EditText) findViewById(R.id.methodPowerLim);
	        view.setText(Integer.toString(mm.PowerLimit));
	         
	        try{
	        	view = (EditText) findViewById(R.id.vesselNo1);
	        	view.setText(Integer.toString(mm.mVesselNo));
	        	view = (EditText) findViewById(R.id.sample_amount_Text);
	        	view.setText(Integer.toString(mm.mSampleAmount));
	        	//Log.d(TAG," -------------------------- ------vesno="+mm.mVesselNo);
	        	view = (EditText) findViewById(R.id.reagentml1);
	        	if(mm.mReagentVolume != null)
		        	view.setText(Integer.toString(mm.mReagentVolume[0]));
	        	else  
	        		view.setText(Integer.toString(0));
	        	view = (EditText) findViewById(R.id.reagentml2);
	        	if(mm.mReagentVolume != null)
			        view.setText(Integer.toString(mm.mReagentVolume[1]));
	        	else  
	        		view.setText(Integer.toString(0));
			    view = (EditText) findViewById(R.id.reagentml3);
			    if(mm.mReagentVolume != null)
			        view.setText(Integer.toString(mm.mReagentVolume[2]));
			    else  
	        		view.setText(Integer.toString(0));
			    view = (EditText) findViewById(R.id.reagentml4);
			    if(mm.mReagentVolume != null)
			    	view.setText(Integer.toString(mm.mReagentVolume[3]));
			    else  
	        		view.setText(Integer.toString(0));
			    view = (EditText) findViewById(R.id.reagentml5);
			    if(mm.mReagentVolume != null)
			        view.setText(Integer.toString(mm.mReagentVolume[4]));
			    else  
	        		view.setText(Integer.toString(0));
		        
	        }
	        catch(NumberFormatException nfe){
	        	
	        }
	    	TextView view2 = (TextView) findViewById(R.id.recipe_chemist_name);
	        view2.setText(mm.mRecipeChemistName);
	        view2 = (TextView) findViewById(R.id.recipe_date);
	        if(mm.mRecipeTime!=null)
	        	view2.setText(mm.mRecipeTime);
	        Log.d(TAG,"jkj"+mm.mRecipeTime);
	        //Spinner sp=(Spinner) findViewById(R.id.recipe_vialmaterial_spinner);
	        //sp.setSelection(mm.mSelectedVialNo);
	        
	    } 
	    void deleteRecipe(String st) {
	        // Create a path where we will place our picture in the user's
	        // public pictures directory and delete the file.  If external
	        // storage is not currently mounted this will fail.
	        File path = Environment.getExternalStoragePublicDirectory("questron\\MW\\recipe");
	        File file = new File(path, st);
	        file.delete();
	    }
	    Boolean openRecipe(String st) {
	        // Create a path where we will place our picture in the user's
	        // public pictures directory and delete the file.  If external
	        // storage is not currently mounted this will fail.
	        File path = Environment.getExternalStoragePublicDirectory("questron\\MW\\recipe");
	        File file = new File(path, st);
	        try  {  
        		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));  
        		Object o = ois.readObject();
        		mQblockMethod=(QBlockMethod) o;
        		mQblockMethod.mRecipeName=st;
        		Log.d("TAG","this is open"+mQblockMethod.TarTemp[0]);
        		loadMethodToString(mQblockMethod);
        		return true;
            }
            catch(Exception ex){  

        		Log.v("Address Book this is not qblock method",ex.getMessage());  
        		ex.printStackTrace(); 
        		//bSettingsChanged=true;
        		return false;
        	} 
	        //file.delete();
	    }

	    public Boolean SaveRecipe(){
	    	//File path = getExternalFilesDir("recipe");
	    	File path=Environment.getExternalStoragePublicDirectory("questron\\MW\\recipe");
	    	//File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
	    	String st = mRecipeName;//+".rcp";//"wqblockSettings";
	    	   mQblockMethod=loadStringToMethod();
	    	try {        // Make sure the Pictures directory exists.        
	    		path.mkdirs();  
	    		File file = new File(path,st); 
	    		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));//new File("save.bin"))); //Select where you wish to save the file...
				//ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.bin"))); //Select where you wish to save the file...
				oos.writeObject(mQblockMethod);//mQblockSettings); // write the class as an 'object' 
				oos.flush(); // flush the stream to insure all of the information was written to 'save.bin' 
				oos.close();
				//recipe_list.add(st);
				fillRecipeList();
				spinnerAdapter.notifyDataSetChanged();
				/*
				// Tell the media scanner about the new file so that it is        
				// immediately available to the user.        
				MediaScannerConnection.scanFile(this,               
						new String[] { file.toString() }, null,               
						new MediaScannerConnection.OnScanCompletedListener() {            
					public void onScanCompleted(String path, Uri uri) {               
						Log.i("ExternalStorage", "Scanned " + path + ":");               
						Log.i("ExternalStorage", "-> uri=" + uri);           
						}        
					});
					*/
				//bSettingsChanged=false;
				Toast.makeText(this, "The Recipe is Saved...", Toast.LENGTH_LONG).show();
				//this.loadFileList();
				return true;
				}
				catch(Exception ex) {  

					Log.v("Address Book",ex.getMessage());  
					ex.printStackTrace();  
					return false;
				}  
			
			
		}
	    private void loadParamToView(){
	    	//setTitle("Recipe="+mRecipeName);
	    	TextView view = (TextView) findViewById(R.id.recipe_chemist_name);
	        view.setText(mRecipeChemistName);
	        view = (TextView) findViewById(R.id.recipe_date);
	        view.setText(mRecipeTime);
	        view = (TextView) findViewById(R.id.method_name);
	        view.setText(mRecipeName);
	    	
	    }

	    private void fillRecipeList(){
	    	File path=Environment.getExternalStoragePublicDirectory("questron\\MW\\recipe");
	    	path.mkdirs();
	    	File mfile=new File(path.toString());
	    	File[] file_list=mfile.listFiles();
	    	recipe_list.clear();
	    	if(file_list!=null){
	    		Log.d("nooh"+file_list.length,"");
	    		for( int i=0; i< file_list.length; i++)
	    		{
	    			Log.d("this is files"+file_list[i].getName(),"ttt");
	    			recipe_list.add( file_list[i].getName() );
	    		}
	    	}
	    	
	    }
		
	
	
	}

package com.questron.WMicroWave;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomGridViewAdapter extends ArrayAdapter<GridDataModel>{
	//private ArrayList<GridDataModel> objects;
	public static final int TEMP_MINIMUM = 59;
	Context context;
	int layoutResourceId;
	ArrayList<GridDataModel> data = new ArrayList<GridDataModel>();

	public CustomGridViewAdapter(Context context, int layoutResourceId,ArrayList<GridDataModel> data) {
			
		super(context, layoutResourceId, data);

		  this.layoutResourceId = layoutResourceId;
		  this.context = context;
		  this.data = data;

	}

	 @Override
	
	 public View getView(int position, View convertView, ViewGroup parent) {
		 //LayoutInflater inflater = (LayoutInflater) context
					//.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		  View row = convertView;
		  
		  //holder.txtTitle1 = (TextView) row.findViewById(R.id.grrow1);
		  if (row == null) {
			   LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			   row = inflater.inflate(layoutResourceId, parent, false);
		  } 
		  GridDataModel griddata = data.get(position);
		  if(griddata!=null){
			  TextView tv1 = (TextView) row.findViewById(R.id.grrow1);
			  TextView tv2 = (TextView) row.findViewById(R.id.grrow2);
			  TextView tv3 = (TextView) row.findViewById(R.id.grrow3);
			  TextView tv4 = (TextView) row.findViewById(R.id.grrow4);
			  TextView tv5 = (TextView) row.findViewById(R.id.grrow5);
			  TextView tv6 = (TextView) row.findViewById(R.id.grrow6);
			  TextView tv7 = (TextView) row.findViewById(R.id.grrow7);
			  TextView tv8 = (TextView) row.findViewById(R.id.grrow8);
			  TextView tv9 = (TextView) row.findViewById(R.id.grrow9);
			  TextView tv10 = (TextView) row.findViewById(R.id.grrow10);
			  TextView tv11 = (TextView) row.findViewById(R.id.grrow11);
			  TextView tv12 = (TextView) row.findViewById(R.id.grrow12);
			  TextView tv13 = (TextView) row.findViewById(R.id.grrow13);
			  TextView tv14 = (TextView) row.findViewById(R.id.grrow14);
			  TextView tv15 = (TextView) row.findViewById(R.id.grrow15);
			  TextView tv16 = (TextView) row.findViewById(R.id.grrow16);
			  if(tv1!=null)
				  tv1.setText(griddata.getTime());
			  if(tv2!=null)
				  tv2.setText(Integer.toString(griddata.getPower()));
			  if(tv3!=null){
				  if(griddata.getTemp(1)<TEMP_MINIMUM)
					  tv3.setText(" ");
				  else
					  tv3.setText(Integer.toString(griddata.getTemp(1)));
			  }
			  if(tv4!=null){
				  if(griddata.getTemp(2)<TEMP_MINIMUM)
					  tv4.setText(" ");
				  else
					  tv4.setText(Integer.toString(griddata.getTemp(2)));
			  }
			  if(tv5!=null){
				  if(griddata.getTemp(3)<TEMP_MINIMUM)
					  tv5.setText(" ");
				  else
					  tv5.setText(Integer.toString(griddata.getTemp(3)));
			  }
			  if(tv6!=null){
				  if(griddata.getTemp(4)<TEMP_MINIMUM)
					  tv6.setText(" ");
				  else
					  tv6.setText(Integer.toString(griddata.getTemp(4)));
			  }
			  if(tv7!=null){
				  if(griddata.getTemp(5)<TEMP_MINIMUM)
					  tv7.setText(" ");
				  else
					  tv7.setText(Integer.toString(griddata.getTemp(5)));
			  }
			  if(tv8!=null){
				  if(griddata.getTemp(6)<TEMP_MINIMUM)
					  tv8.setText(" ");
				  else
					  tv8.setText(Integer.toString(griddata.getTemp(6)));
			  }
			  if(tv9!=null){
				  if(griddata.getTemp(7)<TEMP_MINIMUM)
					  tv9.setText(" ");
				  else
					  tv9.setText(Integer.toString(griddata.getTemp(7)));
			  }
			  if(tv10!=null){
				  if(griddata.getTemp(8)<TEMP_MINIMUM)
					  tv10.setText(" ");
				  else
					  tv10.setText(Integer.toString(griddata.getTemp(8)));
			  }
			  if(tv11!=null){
				  if(griddata.getTemp(9)<TEMP_MINIMUM)
					  tv11.setText(" ");
				  else
					  tv11.setText(Integer.toString(griddata.getTemp(9)));
			  }
			  if(tv12!=null){
				  if(griddata.getTemp(10)<TEMP_MINIMUM)
					  tv12.setText(" ");
				  else
					  tv12.setText(Integer.toString(griddata.getTemp(10)));
			  }
			  if(tv13!=null){
				  if(griddata.getTemp(11)<TEMP_MINIMUM)
					  tv13.setText(" ");
				  else
					  tv13.setText(Integer.toString(griddata.getTemp(11)));
			  }
			  if(tv14!=null){
				  if(griddata.getTemp(12)<TEMP_MINIMUM)
					  tv14.setText(" ");
				  else
					  tv14.setText(Integer.toString(griddata.getTemp(12)));
			  }
			  
			  if(tv15!=null){
				  if(griddata.getOptTemp()==0)
					  tv15.setText(" ");
				  else
					  tv15.setText(Integer.toString(griddata.getOptTemp()));
			  }
			  if(tv16!=null)
				tv16.setText(Integer.toString(griddata.getPress()));
			  
		  }
		  return row;
	 }
	 

}

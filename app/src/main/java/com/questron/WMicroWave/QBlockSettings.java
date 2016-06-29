package com.questron.WMicroWave;

	import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;



import android.widget.Spinner;

	public class QBlockSettings implements Serializable{
		public Boolean[] 	bSelectedBlock ;
		public Boolean[] 	bSelectedVessel ;
		public List<String> chemist_list;//Spinner chemist_spinner;// = (Spinner) findViewById(R.id.settings_chemist_spinner);
		public int 			selected_chemist;
		public List<String> alias_list_name;
		public List<String> alias_list;
		public String		mOrganization;
		public int			TempLimit;
		public int			VesselVolume;
		public int			VesselNo;
		public int			SystemConfig;
		public int			PurgeTemp;
		public int			PurgeCycle;
		public int			DeltaT;
		public int			swRevision;
		public int			int2;
		public int			int3;
		public int			int4;
		public int			int5;
		private static final long serialVersionUID = 110111; 
		public QBlockSettings(){
			this.mOrganization=null;
			this.bSelectedBlock=new Boolean[8];
			this.bSelectedVessel=new Boolean[16];
			this.chemist_list = new ArrayList<String>();
			this.alias_list_name=new ArrayList<String>();
			this.alias_list=new ArrayList<String>();
			this.selected_chemist=0;
			this.TempLimit=230;
			this.VesselVolume=30;
			this.VesselNo=12;
			this.SystemConfig=1;
			this.PurgeTemp=100;
			this.PurgeCycle=5;
			this.DeltaT=50;
			this.int2=0;
			this.swRevision=0;
			chemist_list.add("ali");
			for(int i=0;i<8;i++){
				this.bSelectedBlock[i]=false;
			}
			for(int i=0;i<16;i++){
				this.bSelectedVessel[i]=false;
			}
			this.bSelectedVessel[11]=true;	//power
			this.bSelectedVessel[12]=true;	//bar
		}

	}


package com.hinasch.unlsaga.util;

import java.util.ArrayList;
import java.util.List;

import com.hinasch.lib.UpdateEvent;
import com.hinasch.unlsaga.Unsaga;

public class ScannerPool {

	protected List<UpdateEvent> eventList;
	protected final int SIZE = 10;
	public ScannerPool(){
		this.eventList = new ArrayList();
	}
	
	public void addEvent(UpdateEvent e){
		Unsaga.debug("Adding Event[size:"+this.eventList.size()+"]");
		if(this.eventList.size()<SIZE){
			this.eventList.add(e);
		}

	}
	
	public void update(){
		UpdateEvent removeEvent = null;
		if(!this.eventList.isEmpty()){
			for(UpdateEvent e:eventList){
				if(e.hasFinished()){
					removeEvent = e;
				}else{
					e.loop();
				}
				
			}
		}
		this.removeEvent(removeEvent);
	}
	
	public void removeEvent(UpdateEvent ev){
		if(this.eventList.contains(ev)){
			this.eventList.remove(ev);
		}

	}
}

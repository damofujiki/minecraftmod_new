package com.hinasch.unlsaga.item;

import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;


public interface IUnsagaMaterialTool {

	
	//public UnsagaMaterial unsMaterial = MaterialList.dummy; //Dummy
	//public HashMap<String,Icon> iconMap = new HashMap();
	
	public ToolCategory getCategory();
	
	public UnsagaMaterial getMaterial();
	
	public HelperUnsagaItem helper = new HelperUnsagaItem();
}

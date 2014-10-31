package com.hinasch.lib;





public class DebugLog {

	public final String modname;
	public boolean isDebug = true;
	public DebugLog(String name){
		this.modname = name;
	}
	
	public void setDebug(boolean isDebug){
		this.isDebug = isDebug;
	}
	
	public static boolean checkDebugDetectClass(){

		boolean debug = false;
		String className1[] = {
				"hinasch.mods.Debug"
		};
		String cn = null;
		for(int i=0;i<className1.length;i++){
			try{
				//cn = getClassName(className1[i]);
				cn = className1[i];
				System.out.println(cn);
				//cn="realterrainbiomes.mods.RTBiomesCore";
				cn = ""+Class.forName(cn);
				System.out.println(cn+"is ok.");
				switch(i){
				case 0:
					debug = true;
					break;
				}
			}catch(ClassNotFoundException e){

			}
		}

		System.out.println("check end");


		return debug;
	}
	
	public void log(Object... par1){



		String str = "["+this.modname+"]";
		for(Object obj:par1){
			if(obj!=null){
				Class clas = obj.getClass();
				str += clas.cast(obj).toString()+":";
			}else{
				str += "Null!";
			}

		}
		if(isDebug){
			System.out.println(str);
		}
	}
	
	public  void log(Object par1,Class parent){
		if(isDebug){
			System.out.println("["+this.modname+"/"+parent.getName()+"]"+par1);
		}
	}
}

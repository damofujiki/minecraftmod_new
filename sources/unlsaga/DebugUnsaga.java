package com.hinasch.unlsaga;

public class DebugUnsaga {

	protected float var1[];
	protected float var2[];
	
	protected int int1;
	protected int int2;
	
	
	public float incr = 0.01F;
	public int index;
	
	public DebugUnsaga(){
		var1 = new float[6];
		index = 0;
	}
	
	public void increaseFloat(int par1){
		this.var1[par1] += 0.01F;
		System.out.println(var1[par1]);
	}
	
	public void increaseFloat(int par1,float par2){
		this.var1[par1] += par2;
		System.out.println(var1[par1]);
	}
	
	public void decreaseFloat(int par1){
		this.var1[par1] -= 0.01F;
		System.out.println(var1[par1]);
	}
	
	public void decreaseFloat(int par1,float par2){
		this.var1[par1] -= par2;
		System.out.println(var1[par1]);
	}
	
	public void registFloat(float par1,int par2){
		this.var1[par2] = par1;
	}
	
	public float getFloat(int par1){
		return this.var1[par1];
	}
	
	public void nextIndex(){
		this.index += 1;
		if(this.index >= this.var1.length){
			this.index = 0;
		}
		Unsaga.debug("インデックス："+this.index);
	}
}

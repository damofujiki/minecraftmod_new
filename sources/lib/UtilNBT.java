package com.hinasch.lib;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.google.common.base.Preconditions;

public class UtilNBT {

	public static final int NBTKEY_COMPOUND = 10;
	
	protected static String KEY = "FreeState";
	public static int ERROR = -1;
	
	public static void initNBTTag(ItemStack is){

		NBTTagCompound nbt = is.getTagCompound();
		if(nbt == null){
			nbt = new NBTTagCompound();
			is.setTagCompound(nbt);
		}

		return;
	}
	
	public static boolean hasTag(ItemStack is){
		Preconditions.checkNotNull(is);
		return is.hasTagCompound();
	}
	
	public static boolean hasKey(ItemStack is,String key){
		Preconditions.checkNotNull(is);
		if(hasTag(is)){
			NBTTagCompound nbt = is.getTagCompound();
			if(nbt.hasKey(key)){
				return true;
			}
		}
		return false;
	}
	
	public static void removeTag(ItemStack is,String key){
		Preconditions.checkNotNull(is);
		NBTTagCompound nbt = is.getTagCompound();
		nbt.removeTag(key);
		return;
	}
	
	public static void clearNBT(ItemStack is){
		Preconditions.checkNotNull(is);
		is.setTagCompound(null);
		return;
	}
	public static void setFreeTag(ItemStack is,String key,int val){
		Preconditions.checkNotNull(is);
		initNBTTag(is);
		NBTTagCompound nbt = is.getTagCompound();
		nbt.setInteger(key, (int)val);
		return;
	}
	
	public static void setFreeTag(ItemStack is,String key,String val){
		Preconditions.checkNotNull(is);
		initNBTTag(is);
		NBTTagCompound nbt = is.getTagCompound();
		nbt.setString(key, val);
		return;
	}
	
	public static void setFreeTag(ItemStack is,String key,float val){
		Preconditions.checkNotNull(is);
		initNBTTag(is);
		NBTTagCompound nbt = is.getTagCompound();
		nbt.setFloat(key, (float)val);
		return;
	}
	
	public static void setFreeTag(ItemStack is,String key,boolean val){
		Preconditions.checkNotNull(is);
		initNBTTag(is);
		NBTTagCompound nbt = is.getTagCompound();
		nbt.setBoolean(key, val);
		return;
	}
	
	public static int readFreeTag(ItemStack is,String key){
		Preconditions.checkNotNull(is);
		NBTTagCompound nbt = is.getTagCompound();
		Preconditions.checkNotNull(nbt);
		if(!nbt.hasKey(key)){
			System.out.println("tag not found key:"+key);
			return ERROR;
		}
		int rt = (int)nbt.getInteger(key);
		return rt;
	}
	
	public static float readFreeFloat(ItemStack is,String key){
		Preconditions.checkNotNull(is);
		NBTTagCompound nbt = is.getTagCompound();
		Preconditions.checkNotNull(nbt);
		if(!nbt.hasKey(key)){
			System.out.println("tag not found key:"+key);
			return ERROR;
		}
		float rt = (float)nbt.getFloat(key);
		return rt;
	}
	
	public static String readFreeStrTag(ItemStack is,String key){
		Preconditions.checkNotNull(is);
		NBTTagCompound nbt = is.getTagCompound();
		Preconditions.checkNotNull(nbt);
		if(!nbt.hasKey(key)){
			System.out.println("tag not found key:"+key);
			return "";
		}
		String rt = nbt.getString(key);
		return rt;
	}
	
	public static boolean readFreeTagBool(ItemStack is,String key){
		Preconditions.checkNotNull(is);
		NBTTagCompound nbt = is.getTagCompound();
		Preconditions.checkNotNull(nbt);
		if(!nbt.hasKey(key)){
			System.out.println("tag not found key:"+key);
			return false;
		}
		boolean rt = nbt.getBoolean(key);
		return rt;
	}
	
	public static void setState(ItemStack is,int state){
		setFreeTag(is,KEY,state);
	}
	
	public static void setState(ItemStack is,boolean state){
		setFreeTag(is,KEY,state);
	}
	
	public static int readState(ItemStack is){
		int rt = 0;
		rt = readFreeTag(is,KEY);
		return rt;
	}
	
	public static boolean hasiInitState(ItemStack is){
		NBTTagCompound nbt = is.getTagCompound();
		if(nbt==null){
			initNBTTag(is);
		}
		return readFreeTag(is,KEY)==ERROR ? false : true;
	}
	
	public static boolean readStateBool(ItemStack is){
		boolean rt = false;
		rt = readFreeTagBool(is,KEY);
		return rt;
	}
	
	//compoundそのものを格納できるNBT。最後にnbt.setTagで登録する。５１２までネストできるらしい。
	public static NBTTagList getTagList(){
		return new NBTTagList();
	}
	
	public static NBTTagList getTagList(NBTTagCompound nbt,String key){
		NBTTagList tags = nbt.getTagList(key, NBTKEY_COMPOUND);

		Preconditions.checkNotNull(tags);
		return tags;
	}

	public static void writeItemStacksToNBTTag(NBTTagList tagList,ItemStack[] iss){

		for(int i=0;i<iss.length;i++){
			if(iss[i]!=null){
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
				iss[i].writeToNBT(compound);
				tagList.appendTag(compound);
			}
		}
	}

	public static ItemStack[] getItemStacksFromNBT(NBTTagList tagList,int length){

		ItemStack[] iss = new ItemStack[length];

        for (int i = 0; i < tagList.tagCount(); ++i)
        {

            NBTTagCompound nbttagcompound1 = (NBTTagCompound)tagList.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;


            if (j >= 0 && j < iss.length)
            {
            	iss[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);

            }
        }
        return iss;
	}
	
	
}

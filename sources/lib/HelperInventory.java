package com.hinasch.lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Lists;
import com.hinasch.sortinv.SortInv;

public class HelperInventory {


	public static final int INV_HELM = 0;
	public static final int INV_ARMOR  =1;
	public static final int INV_LEGGINS = 2;
	public static final int INV_BOOTS = 3;
	protected final IInventory inv;
	public HelperInventory(IInventory par1){
		this.inv = par1;
	}
	

	public IInventory getInv(){
		return this.inv;
	}
	public static void decrCurrentHeldItem(EntityPlayer ep,int decr){
		if(!ep.capabilities.isCreativeMode){
			IInventory invp = ep.inventory;
			int current = ep.inventory.currentItem;
			invp.decrStackSize(current, decr);
			return;
		}
		

	}
	public ItemStack getFirstInv(){
		return this.inv.getStackInSlot(0);
	}
	
	
	public boolean contains(ItemStack is){
		boolean fl = false;
		for(int i=0;i<this.inv.getSizeInventory();i++){
			ItemStack chestis = this.inv.getStackInSlot(i);
			if(chestis!=null && is.isItemEqual(chestis)){
				fl = true;
			}
		}
		return fl;
	}
	
	public void addItemStack(ItemStack is){
		if(!this.isInvFull()){
			for(int i=0;i<inv.getSizeInventory();i++){
				if(inv.getStackInSlot(i)==null){
					inv.setInventorySlotContents(i,is);
					return;
				}
			}
		}
	}
	public boolean isInvFull(){
		int inSlot = 0;
		for(int i=0;i<inv.getSizeInventory();i++){
			if(inv.getStackInSlot(i)!=null){
				inSlot +=1;
			}
		}
		if(inv.getSizeInventory()<=inSlot){
			return true;
		}
		return false;
	}
	public int getAmount(ItemStack is){
		int stack = 0;
		for(int i=0;i<this.inv.getSizeInventory();i++){
			ItemStack chestis = this.inv.getStackInSlot(i);
			if(chestis!=null && is.isItemEqual(chestis)){
				stack += this.inv.getStackInSlot(i).stackSize;
			}
		}
		return stack;
	}
	
	public Map<Integer,ItemStack> getInvItems(ItemStack is,boolean avoidDamage){
//		Map<Integer,ItemStack> map = new HashMap();
//		for(int i=0;i<this.inv.getSizeInventory();i++){
//			ItemStack chestis = this.inv.getStackInSlot(i);
//			if(chestis!=null){
//				if(avoidDamage && is.getItem()==chestis.getItem()){
//					map.put(i, chestis);
//				}else{
//					if(is.isItemEqual(chestis)){
//						map.put(i, chestis);
//					}
//				}
//
//			}
//
//		}
//		return map;
		
		CustomCheck checker = new CustomCheck(){
			public boolean avoidDamage;
			public ItemStack itemStackIn;
			
			public CustomCheck setStatus(boolean par1,ItemStack par2){
				this.avoidDamage = par1;
				this.itemStackIn = par2;
				return this;
			}
			
			@Override
			public boolean check(ItemStack chestis){
				if(avoidDamage && this.itemStackIn.getItem() == chestis.getItem()){
					return  true;
				}
				if(this.itemStackIn.isItemEqual(chestis)){
					return true;
				}
				return false;
			}
		}.setStatus(avoidDamage, is);
		
		return this.getInvItems(checker);
	}
	
	public Map<Integer,ItemStack> getInvItems(CustomCheck checker){
		Map<Integer,ItemStack> map = new HashMap();
		for(int i=0;i<this.inv.getSizeInventory();i++){
			ItemStack chestis = this.inv.getStackInSlot(i);
			if(chestis!=null){
				if(checker.check(chestis)){
					map.put(i, chestis);
				}

			}

		}
		return map;
	}
	
	public int getAmount(Item item){
//		int stack = 0;
//		for(int i=0;i<this.inv.getSizeInventory();i++){
//			ItemStack chestis = this.inv.getStackInSlot(i);
//			if(chestis!=null && chestis.getItem()==item){
//				stack += this.inv.getStackInSlot(i).stackSize;
//			}
//		}
//		return stack;
		return this.getAmount(new CustomCheck(){

			public Item item;
			public CustomCheck setItem(Item item){
				this.item = item;
				return this;
			}
			@Override
			public boolean check(ItemStack is){
				return is.getItem() == item;
			}
		}.setItem(item));
	}
	
	public int getAmount(CustomCheck check){
		int stack = 0;
		for(int i=0;i<this.inv.getSizeInventory();i++){
			ItemStack chestis = this.inv.getStackInSlot(i);
			if(chestis!=null && check.check(chestis)){
				stack += this.inv.getStackInSlot(i).stackSize;
			}
		}
		return stack;
	}
	
	public boolean contains(PairIDList pairList){
		int flag =0;
		for(PairID pair:pairList.list){
			if(this.contains(pair.getAsItemStack()) && this.getAmount(pair.getAsItemStack())>=pair.stack){
				flag += 1;
				System.out.println(pair);
				System.out.println("fl:"+flag);
			}
		}
		System.out.println(pairList.list.size());
		if(pairList.list.size()<=flag){
			return true;
		}
		return false;
	}
	
	//インベントリの場所によらずアイテムを消費
	public void decrItemStack(ItemStack is,int par1){
		int decr = par1;
		for(int i=0;i<this.inv.getSizeInventory();i++){
			ItemStack chestis = this.inv.getStackInSlot(i);
			if(chestis!=null && decr>0 && is.isItemEqual(chestis)){
				if(chestis.stackSize<decr){
					decr -= chestis.stackSize;
					this.inv.setInventorySlotContents(i, null);
				}else{
					this.inv.decrStackSize(i, decr);
					decr = 0;
				}
			}
		}		
	}
	
	//上のをまとめて実行するやつ
	public void decrItemStack(PairIDList pairList){
		for(PairID pair:pairList.list){
			this.decrItemStack(pair.getAsItemStack(), pair.getStack());
		}
	}
	//もともとのやつと同じ。ちなみにアイテム増減は蔵鯖両方で実行したほうがよい1.7.2現在
	public void decrItemStack(int par1,int stack){
		this.inv.decrStackSize(par1,stack);
	}
	
	public ItemStack getArmorInv(int par1){

		if(this.inv instanceof InventoryPlayer){
			return ((InventoryPlayer) this.inv).armorItemInSlot(par1);
		}
		return null;
	}
	
	public void setArmor(int par1,ItemStack is){

		if(this.inv instanceof InventoryPlayer){
			((InventoryPlayer) this.inv).armorInventory[par1] = is;
		}
		return;
	}
	
	public void swapSlot(int num1,int num2){
		ItemStack is = inv.getStackInSlot(num1)!=null?inv.getStackInSlot(num1).copy() : null;
		ItemStack is2 =  inv.getStackInSlot(num2)!=null?inv.getStackInSlot(num2).copy() : null;
		inv.setInventorySlotContents(num1, is2);
		inv.setInventorySlotContents(num2, is);
	}
	public void collectStack(int num1,int num2){
		int stack1 = inv.getStackInSlot(num1).stackSize;
		int stack2 = inv.getStackInSlot(num2).stackSize;
		SortInv.log(num1+"のスタックは"+stack1,this.getClass());
		SortInv.log(num2+"のスタックは"+stack2,this.getClass());
		stack1 += stack2;
		stack2 = 0;
		if(stack1>64){

			stack2 = stack1 - 64;
			stack1 = 64;
		}
		inv.getStackInSlot(num1).stackSize = stack1;
		inv.getStackInSlot(num2).stackSize = stack2;
		SortInv.log(num1+"のスタックは"+stack1+"に",this.getClass());
		SortInv.log(num2+"のスタックは"+stack2+"に",this.getClass());		
		if(inv.getStackInSlot(num2).stackSize<=0){
			inv.setInventorySlotContents(num2, null);
		}
	}
	public void sort(){
		ItemStack selected = null;
		boolean collected = false;
		for(int i=0;i<this.inv.getSizeInventory();i++){
			if(this.inv.getStackInSlot(i)!=null){
				selected = this.inv.getStackInSlot(i);
				collected = false;
				if(i+1<inv.getSizeInventory() && selected.isStackable() && selected.stackSize<64){
					
					for(int j=i+1;j<this.inv.getSizeInventory();j++){
						if(!collected && this.inv.getStackInSlot(j)!=null && selected.isItemEqual(this.inv.getStackInSlot(j))){
							SortInv.log(i+":"+j, this.getClass());
							this.collectStack(i, j);
							if(selected.stackSize>=64){
								collected = true;
							}

						}
					}
				}

			}
		}
		
		for(int tier=0;tier<7;tier++){
			for(int i=0;i<this.inv.getSizeInventory();i++){
				if(this.inv.getStackInSlot(i)!=null){
					this.checkPair(i,tier);
				}
			}
		}
		
		for(int i=0;i<this.inv.getSizeInventory();i++){
			if(this.inv.getStackInSlot(i)==null){
				this.checkPair(i, 99);
			}
		}

	}
	
	
	public void checkPair(int selectedSlot,int sw){
		ItemStack selectedStack = this.inv.getStackInSlot(selectedSlot);
		if(selectedSlot+1<this.inv.getSizeInventory()){
			for(int i=selectedSlot+1;i<this.inv.getSizeInventory();i++){
				if(this.inv.getStackInSlot(i)!=null){
					ItemStack pairStack = this.inv.getStackInSlot(i);
					switch(sw){
					case 0:
						if(pairStack.isItemEqual(selectedStack)){
							if(selectedSlot!=i){
								this.swapSlot(selectedSlot+1, i);
							}
							
						}
						break;
					case 1:
						if(pairStack.getItem()==selectedStack.getItem()){
							if(selectedSlot!=i){
								this.swapSlot(selectedSlot+1, i);
							}
						}
						break;
					case 2:
						if(pairStack.getItem() instanceof ItemTool){
							if(selectedSlot!=i){
								this.swapSlot(selectedSlot+1, i);
							}
						}
						break;
					case 3:
						if(pairStack.getItem() instanceof ItemFood){
							if(selectedSlot!=i){
								this.swapSlot(selectedSlot+1, i);
							}
						}
						break;	
					case 4:
						if(pairStack.getItem() instanceof ItemSword){
							if(selectedSlot!=i){
								this.swapSlot(selectedSlot+1, i);
							}
						}
						break;	
					case 5:
						List<String> idnames1 = HSLibs.getOreNames(OreDictionary.getOreIDs(pairStack));
						List<String> idnames2 = HSLibs.getOreNames(OreDictionary.getOreIDs(selectedStack));
						boolean sw1 = false;
						boolean sw2 = false;
						for(String na:idnames1){
							if(na.contains("ore"))sw1 = true;
						}
						for(String na:idnames2){
							if(na.contains("ore"))sw2 = true;
						}
						if(sw1&&sw2){
							if(selectedSlot!=i){
								this.swapSlot(selectedSlot+1, i);
							}
						}
						break;	
					case 6:
						List idList1 = Lists.newArrayList(OreDictionary.getOreIDs(pairStack));
						List idList2 = Lists.newArrayList(OreDictionary.getOreIDs(selectedStack));
						if(idList1!=null && idList2!=null && UtilCollection.listContainsList(idList1, idList2)){
							if(selectedSlot!=i){
								this.swapSlot(selectedSlot+1, i);
							}
						}
						break;	
					case 99:
						if(selectedSlot!=i){
							this.swapSlot(selectedSlot, i);
						}
						break;
					}

				}
			}
		}
	}
	public void spreadChestContents(World world,IInventory inv,XYZPos pos){
		Random field_149955_b = new Random();
        for (int i1 = 0; i1 < inv.getSizeInventory(); ++i1)
        {
            ItemStack itemstack = inv.getStackInSlot(i1);

            if (itemstack != null)
            {
                float f = field_149955_b.nextFloat() * 0.8F + 0.1F;
                float f1 =field_149955_b.nextFloat() * 0.8F + 0.1F;
                EntityItem entityitem;

                for (float f2 = field_149955_b.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem))
                {
                    int j1 = field_149955_b.nextInt(21) + 10;

                    if (j1 > itemstack.stackSize)
                    {
                        j1 = itemstack.stackSize;
                    }

                    itemstack.stackSize -= j1;
                    entityitem = new EntityItem(world, (double)((float)pos.x + f), (double)((float)pos.y + f1), (double)((float)pos.z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                    float f3 = 0.05F;
                    entityitem.motionX = (double)((float)field_149955_b.nextGaussian() * f3);
                    entityitem.motionY = (double)((float)field_149955_b.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double)((float)field_149955_b.nextGaussian() * f3);

                    if (itemstack.hasTagCompound())
                    {
                        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                    }
                }
            }
        }
	}
	

	public static class CustomCheck{
		
		public boolean check(ItemStack is){
			return false;
		}
	}
}

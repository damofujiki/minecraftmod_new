package com.hinasch.unlsaga.inventory.container;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hinasch.lib.Statics;
import com.hinasch.lib.base.ContainerBase;
import com.hinasch.lib.net.PacketGuiButtonBaseNew;
import com.hinasch.unlsaga.DebugUnsaga;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.client.gui.GuiSmithUnsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedEntityTag;
import com.hinasch.unlsaga.inventory.InventorySmithUnsaga;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.network.packet.PacketGuiButtonNew;
import com.hinasch.unlsaga.villager.smith.ForgingProcess;
import com.hinasch.unlsaga.villager.smith.MaterialInfo;
import com.hinasch.unlsaga.villager.smith.ValidPayment;
import com.hinasch.unlsaga.villager.smith.ValidPayment.EnumPayValues;

import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerSmithUnsaga extends ContainerBase{

	protected IMerchant theMerchant;
	protected EntityPlayer ep;
	protected World worldobj;
	protected InventorySmithUnsaga inventorySmith;
	protected final int inv_PAYMENT = 0;
	protected final int inv_BASE = 1;
	protected final int inv_MATERIAL = 2;
	protected final int inv_FORGED = 3;
	protected byte currentCategory = 0; //GUI側と同期される
	protected DebugUnsaga debug;

	protected String smithType;

	public static final String SMITH_TYPE_REPAIR_PRO = "healDamage";
	public static final String SMITH_TYPE_ADD_ABILTY = "addAbility";

	protected int id = 0;



	public ContainerSmithUnsaga(IMerchant par1,World par2,EntityPlayer ep){

		super(ep, new InventorySmithUnsaga(ep,par1));
		this.ep = ep;
		if(debug==null){
			debug = new DebugUnsaga();
		}

		//this.helper = new SmithHelper();
		this.theMerchant = par1;
		this.theMerchant.setCustomer(ep);
		this.smithType = "normal";

		Unsaga.debug("Container:"+this.theMerchant);
		if(this.theMerchant instanceof EntityVillager){
			EntityVillager villager = (EntityVillager) this.theMerchant;
			if(ExtendedEntityTag.hasTag(villager, SMITH_TYPE_REPAIR_PRO)){
				this.smithType = SMITH_TYPE_REPAIR_PRO;
				Unsaga.debug("type:修理のプロ");
			}
			if(ExtendedEntityTag.hasTag(villager,SMITH_TYPE_ADD_ABILTY)){
				this.smithType = SMITH_TYPE_ADD_ABILTY;
				Unsaga.debug("type:素材を活かす");
			}
		}
		this.worldobj = par2;
		this.inventorySmith = (InventorySmithUnsaga) this.inv;
		this.addSlotToContainer(new Slot(this.inventorySmith, this.inv_PAYMENT, 28, 53)); //Emerald
		this.addSlotToContainer(new Slot(this.inventorySmith, this.inv_BASE, 28, 53-(18*2))); //Base Material
		this.addSlotToContainer(new Slot(this.inventorySmith, this.inv_MATERIAL, 28+(18*2)-8, 53-(18*2))); //Material2
		this.addSlotToContainer(new Slot(this.inventorySmith, this.inv_FORGED, 28+(18*6)+1, 52)); 

	}
	

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		// TODO 自動生成されたメソッド・スタブ
		return this.theMerchant.getCustomer() == entityplayer;
	}

	@Override
	public int getGuiID(){
		return Unsaga.guiNumber.SMITH;
	}
	
	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return Unsaga.packetDispatcher;
	}

	
	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID,int buttonID,Object... args){
		Unsaga.debug(args);
		return new PacketGuiButtonNew(guiID,buttonID,(Byte)args[0]);
	}
	
	
	@Override
	public void onPacketData() {
		Unsaga.debug(this.id);
		this.currentCategory = (Byte)this.argsSent[0];
		ToolCategory category = ToolCategory.toolArray.get(currentCategory);
		MaterialInfo baseItemInfo = null;
		MaterialInfo subItemInfo = null;

		if(this.buttonID==GuiSmithUnsaga.DOFORGE){
			byte flagcount = 0;
			if(this.inventorySmith.getPayment()!=null){
				for(Iterator<ValidPayment> ite=ValidPayment.validPayList.iterator();ite.hasNext();){
					if(ite.next().compare(this.inventorySmith.getPayment())){
						Unsaga.debug("会いました",this.getClass());
						flagcount += 1;
					}
				}
			}
			if(this.inventorySmith.getBaseItem()!=null){
				baseItemInfo = new MaterialInfo(this.inventorySmith.getBaseItem());
				if(baseItemInfo.getMaterial().isPresent()){
					UnsagaMaterial material = baseItemInfo.getMaterial().get();
					if(Unsaga.items.isForgeMaterial(category, material)){
						Unsaga.debug("ベースおｋです",this.getClass());
						flagcount +=1;
					}
				}
			}
			if(this.inventorySmith.getMaterial()!=null){
				subItemInfo = new MaterialInfo(this.inventorySmith.getMaterial());
				if(subItemInfo.getMaterial().isPresent()){
					Unsaga.debug("素材に使えます",this.getClass());
					flagcount += 1;
				}
			}
			if(this.inventorySmith.getForged()==null){
				flagcount += 1;
			}
			if(flagcount>=4){
				this.worldobj.playSoundAtEntity((Entity) this.theMerchant, Statics.soundAnvilUse, 1.0F, 1.0F);
				ForgingProcess newforge = new ForgingProcess(this.ep,category,baseItemInfo, subItemInfo,this.worldobj.rand,this.smithType);
				newforge.doForge(getPaymentValue());
				ItemStack newstack = newforge.getForgedItemStack();
				ForgingProcess.EnumWorkResult result = newforge.transplantAbilities(newstack, (EntityVillager) this.theMerchant, this.getPaymentValue());
				Unsaga.debug(result,this.getClass());
				this.inventorySmith.setInventorySlotContents(inv_FORGED, newstack);
				this.inventorySmith.decrStackSize(inv_BASE, 1);
				this.inventorySmith.decrStackSize(inv_MATERIAL, 1);
				this.inventorySmith.decrStackSize(inv_PAYMENT, 1);
			}
		}

	}

	public EnumPayValues getPaymentValue(){
		if(this.inventorySmith.getPayment()!=null){
			return ValidPayment.getValueFromItemStack(this.inventorySmith.getPayment());
		}
		return null;
	}


	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		this.theMerchant.setCustomer(null);
	}


}

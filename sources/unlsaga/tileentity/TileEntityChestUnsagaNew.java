package com.hinasch.unlsaga.tileentity;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

import com.hinasch.lib.ItemUtil;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.util.ChatMessageHandler;
import com.hinasch.unlsaga.util.HelperChestUnsaga;
import com.hinasch.unlsaga.util.translation.Translation;

public class TileEntityChestUnsagaNew extends TileEntityChest{


	protected int level;
	//protected int trapNumber = 0;
	protected TrapChest trap;
	protected boolean unlocked = true;
	//protected boolean defused;
	protected boolean slimeTrapOccured = false;
	protected boolean trapOccured = true;
	protected boolean magicLock = false;
	protected boolean hasOpened = false;
	protected boolean chestInitialized;
	protected int count = 0;

	public TileEntityChestUnsagaNew(){
		//		if(!this.initialized){
		//			this.unlocked = true;
		//			//this.defused = false;
		//			this.trapOccured = true;
		//			this.magicLock = false;
		//			this.hasOpened = false;
		//			this.slimeTrapOccured = false;
		//			this.level = 0;
		//			this.trapNumber = 0;
		//		}

		this.trap = ChestTraps.dummy;
		Unsaga.debug("つくられました");
	}


	public void activateChest(EntityPlayer ep){
		if(!this.hasOpened){
			ChatMessageHandler.sendChatToPlayer(ep,Translation.localize("msg.touch.chest"));
			if(!this.trapOccured){
				this.activateTrap(ep);
			}
			if(this.unlocked){
				if(!this.magicLock){
					this.openChest(ep);
				}else{
					ChatMessageHandler.sendChatToPlayer(ep,Translation.localize("msg.chest.magiclocked"));
				}
			}else{
				ChatMessageHandler.sendChatToPlayer(ep,Translation.localize("msg.chest.locked"));
			}
		}




		//宝箱は空
	}


	public void activateTrap(EntityPlayer ep) {
		int var1 = 80;
		int var2 = 7 * (SkillPanels.getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.avoidTrap)+1);
		var1 -= var2;
		
		if(ep.getRNG().nextInt(100)<var1){
			this.trap.activate(this, ep);
		}



		if(!this.slimeTrapOccured && ep.getRNG().nextInt(100)<var1){
			ChestTraps.slime.activate(this, ep);
		}


	}
	
	public void setTrapOccured(boolean par1){
		this.trapOccured = par1;
	}

	public boolean doChance(int par1){
		if(this.worldObj.rand.nextInt(100)<par1){
			return true;
		}
		return false;
	}

	public boolean doChance(Random random,int par1){
		if(random.nextInt(100)<par1){
			return true;
		}
		return false;
	}

	public int getChestLevel(){
		return this.level;
	}

	@Override
	public Packet getDescriptionPacket() {

		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbttagcompound);
	}

	public int getTrapNumber() {
		// TODO 自動生成されたメソッド・スタブ
		return this.trap.number;
	}
	
	public String getTrapName(){
		return this.trap.name;
	}

	public boolean hasSetSlimeTrap(){
		return !this.hasOccuredSlimeTrap();
	}
	public boolean hasChestOpened(){
		return this.hasOpened;
	}

	public boolean hasInitialized(){
		return this.chestInitialized;
	}

	public boolean hasOccuredSlimeTrap(){
		return this.slimeTrapOccured;
	}

	public void initChest(World world){







		if(this.hasOpened){
			this.lidAngle = 1.0F;
		}

		Unsaga.debug(this.chestInitialized+":初期化");
		this.initChestLevel(world);
		if(this.getChestLevel()>20){
			if(this.doChance(world.rand, 60)){
				this.unlocked = false;
			}
			if(this.doChance(world.rand, 40)){
				this.magicLock = true;
			}
			if(this.doChance(world.rand,30)){
				this.trapOccured = true;
			}else{
				this.trapOccured = false;
			}
			if(this.doChance(world.rand,20)){
				this.slimeTrapOccured = true;
			}
			this.trap = ChestTraps.getTrap(world.rand.nextInt(3));

		}


		this.chestInitialized = true;
	}

	public void initChestLevel(World world){
		this.level = world.rand.nextInt(99)+1;

	}

	public boolean isMagicLocked(){
		return this.magicLock;
	}

	public boolean isTrapOccured(){
		return this.trapOccured;
	}

	public boolean isUnlocked(){
		return this.unlocked;
	}

	public void obtainItem() {
		this.hasOpened = true;
		HelperChestUnsaga hc = new HelperChestUnsaga(this.level);
		ItemStack is = hc.getChestItem(this.worldObj.rand);
		//ぬるぽの可能性が…？
		if(!this.worldObj.isRemote && is!=null){
			ItemUtil.dropItem(worldObj, is, xCoord, yCoord, zCoord);
		}

	}

	public void setSlimeTrapOccured(boolean par1){
		this.slimeTrapOccured = par1;
	}




	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
		this.level = pkt.func_148857_g().getInteger("chestLevel");
    }


	private void openChest(EntityPlayer ep) {
		this.openInventory();
		this.obtainItem();
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);



		this.unlocked = nbt.getBoolean("Unlocked");
		//this.defused = par1NBTTagCompound.getBoolean("Defused");
		this.trapOccured = nbt.getBoolean("TrapOccured");
		this.magicLock = nbt.getBoolean("MagicalLock");
		this.hasOpened = nbt.getBoolean("HasOpened");
		this.level = nbt.getInteger("chestLevel");
		this.slimeTrapOccured = nbt.getBoolean("slimeTrap");

		this.chestInitialized = nbt.getBoolean("initializedChest");

		this.trap = ChestTraps.getTrap(nbt.getInteger("trap"));



		//Unsaga.debug("読み込まれました");
	}

	public void reductionChestLevel(){
		this.level = (int)((float)this.level * 0.5F);

	}

	public void setChestLevel(int par1){
		this.level = par1;
	}

	public void setDefused(boolean par1){
		this.trapOccured = true;
	}

	public void setMagicLock(boolean par1){
		this.magicLock = par1;
	}
	
	public void setUnlocked(boolean par1){
		this.unlocked = par1;
	}

	@Override
    public void updateEntity() {
		super.updateEntity();
    	if(this.hasOpened){
    		this.count += 1;
    		if(this.count>100 && !this.worldObj.isRemote){
    			this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
    		}
    	}
    }
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("Unlocked", (boolean)this.unlocked);
		//par1NBTTagCompound.setBoolean("Defused", (boolean)this.defused);
		nbt.setBoolean("TrapOccured", (boolean)this.trapOccured);
		nbt.setBoolean("MagicalLock", (boolean)this.magicLock);
		nbt.setBoolean("HasOpened", (boolean)this.hasOpened);
		nbt.setInteger("chestLevel",(int)this.level);
		nbt.setBoolean("slimeTrap", this.slimeTrapOccured);

		nbt.setBoolean("initializedChest", this.chestInitialized);
		nbt.setInteger("trap", this.trap.number);








	}
}

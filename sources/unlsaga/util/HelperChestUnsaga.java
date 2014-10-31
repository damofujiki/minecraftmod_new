package com.hinasch.unlsaga.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomChestContent;

import com.hinasch.lib.XYZPos;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.WorldSaveDataUnsaga;
import com.hinasch.unlsaga.init.UnsagaItems;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.NoFunctionItems;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.network.packet.PacketParticle;
import com.hinasch.unlsaga.tileentity.TileEntityChestUnsagaNew;
import com.hinasch.unlsaga.util.translation.Translation;
import com.hinasch.unlsagamagic.item.ItemTablet;

public class HelperChestUnsaga {

	//public static List<ItemStack> validChestItems;
	public List<WeightedRandomChestContent> chestContents;
	public int chestLevel;
	//public HookUnsagaMagic hook;
	public TileEntityChestUnsagaNew tileEntityChest;

	public HelperChestUnsaga(int chestLevel){
		this.chestContents = new ArrayList();
		this.chestLevel = chestLevel;
		//if(Unsaga.configs.module.isMagicEnabled())this.hook = new HookUnsagaMagic();

		this.buildContents();
	}


	public HelperChestUnsaga(TileEntityChestUnsagaNew chest){
		this.tileEntityChest = chest;
	}

	protected void buildContents(){
		Random rand = new Random();
		
		//素材に関連付けたアイテムを拾ってくる
		for(Iterator<UnsagaMaterial> ite=Unsaga.materialManager.getAllMaterialValues().iterator();ite.hasNext();){
			UnsagaMaterial us = ite.next();
			if(us.getAssociatedItemStack().isPresent()){
				ItemStack is = us.getAssociatedItemStack().get();
				addChestContent(is,us.rank);
			}
		}

		//
		for(NoFunctionItems.ItemData item:Unsaga.noFunctionItems.getList().values()){
			UnsagaMaterial us = item.associated;
			ItemStack is = Unsaga.noFunctionItems.getItemStack(1, item.number);
			addChestContent(is,us.rank);
		}

//			ItemStack is = new ItemStack(Unsaga.magic.items.blender,1);
//			addChestContent(is,6);
			ItemStack tablet = ItemTablet.getRandomMagicTablet(rand);
			addChestContent(tablet,3);
			
		
		ItemStack musket = new ItemStack(Unsaga.items.musket,1);
		addChestContent(musket,4);
		
		

		for(int i=0;i<15;i++){
			int randomRank = rand.nextInt(9)+1;
			int min = randomRank;
			int max = randomRank + 3;
			ItemStack is = Unsaga.items.getRandomWeapon(rand,min,max , UnsagaItems.EnumSelecterItem.ALL);
			UnsagaMaterial us = (is.getItem() instanceof IUnsagaMaterialTool)? HelperUnsagaItem.getMaterial(is) : null;
			if(us!=null){
				addChestContent(is,us.rank+1);
			}
		}

	}

	protected void addChestContent(ItemStack is,int rank){
		if(rank<100){
			int var1 = (10-rank)*(this.chestLevel/2+1);
			int var2 = var1/3;
			var1 = MathHelper.clamp_int(var1, 1, 200);
			var2 = MathHelper.clamp_int(var1, 1, 10);
			if(rank>this.chestLevel/8)return;
			Unsaga.debug("add:",is,var1,var2);
			this.chestContents.add(new WeightedRandomChestContent(is,1,var2,var1));
		}
	}


	public ItemStack getChestItem(Random rand){

		if(!this.chestContents.isEmpty()){

			WeightedRandomChestContent getItem = (WeightedRandomChestContent) WeightedRandom.getRandomItem(rand, this.chestContents);

			return getItem.theItemId;//this.chestContents.get(rand.nextInt(this.chestContents.size()));


		}
		return new ItemStack(Items.gold_nugget,1);
	}

	public WeightedRandomChestContent[] getChestContentsUnsaga(){

		if(!this.chestContents.isEmpty()){
			Unsaga.debug("parsed Hashset to Array.");
			return this.chestContents.toArray(new WeightedRandomChestContent[this.chestContents.size()]);

		}else{
			return null;
		}

	}


	public void tryDefuse(EntityPlayer ep) {
		if(SkillPanels.getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.defuse)>=0){
			int prob = 50 + SkillPanels.getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.defuse);
			if(this.tileEntityChest.doChance(ep.getRNG(),prob)){
				this.tileEntityChest.setDefused(true);
				ChatMessageHandler.sendChatToPlayer(ep, Translation.localize("msg.chest.defused"));

			}else{
				this.tileEntityChest.activateTrap(ep);
			}
		}

	}

	public boolean tryUnlockMagicalLock() {
		if(this.tileEntityChest.doChance(50)){

			this.tileEntityChest.setMagicLock(false);
			return true;
		}else{
			return false;
		}

	}

	public void tryUnlock(EntityPlayer ep) {
		if(!this.tileEntityChest.isTrapOccured()){
			this.tileEntityChest.activateTrap(ep);
		}

		if(SkillPanels.getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.unlock)>=0){
			int prob = 50 + WorldSaveDataUnsaga.getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.unlock) * 8;
			if(this.tileEntityChest.doChance(prob)){
				ChatMessageHandler.sendChatToPlayer(ep, Translation.localize("msg.chest.unlocked"));
				this.tileEntityChest.setUnlocked(true);
			}else{
				ChatMessageHandler.sendChatToPlayer(ep, Translation.localize("msg.failed"));
			}
			if(this.tileEntityChest.doChance(20)){
				this.tileEntityChest.activateTrap(ep);
			}
		}



	}

	public void tryPenetration(EntityPlayer ep){
		ep.closeScreen();
		if(SkillPanels.getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.penetration)>=0){
			int prob = 60 + WorldSaveDataUnsaga.getHighestLevelOfPanel(ep.worldObj, ep, Unsaga.skillPanels.penetration) * 8;
			if(this.tileEntityChest.doChance(prob)){
				String msg = "Trap:"+this.tileEntityChest.getTrapName();
				
				ChatMessageHandler.sendChatToPlayer(ep, Translation.localize(msg));
				msg = "SlimeTrap:"+(this.tileEntityChest.hasSetSlimeTrap()?"true" : "false");
				ChatMessageHandler.sendChatToPlayer(ep, Translation.localize(msg));
			}else{
				ChatMessageHandler.sendChatToPlayer(ep, Translation.localize("msg.failed"));
				if(this.tileEntityChest.doChance(20)){
					this.tileEntityChest.activateTrap(ep);
				}
			}
		}
		

	}
	public void divination(EntityPlayer openPlayer) {
		Random rand = openPlayer.getRNG();
		int div = SkillPanels.getHighestLevelOfPanel(openPlayer.worldObj, openPlayer, Unsaga.skillPanels.divination);
		if(div>=0){
			int lv =0;
			if(rand.nextInt(100)<=50+(10*div)){
				ChatMessageHandler.sendChatToPlayer(openPlayer, Translation.localize("msg.chest.divination.succeeded"));
				lv = this.tileEntityChest.getChestLevel() + rand.nextInt(7)+1;


			}else{

				if(rand.nextInt(10)<=2){
					ChatMessageHandler.sendChatToPlayer(openPlayer, Translation.localize("msg.chest.divination.catastrophe"));
					lv = 2;
				}else{
					ChatMessageHandler.sendChatToPlayer(openPlayer, Translation.localize("msg.chest.divination.failed"));
					lv = this.tileEntityChest.getChestLevel() - rand.nextInt(7)+1;
				}


			}
			this.tileEntityChest.setChestLevel(MathHelper.clamp_int(lv, 1, 100));
			String str = Translation.localize("msg.chest.divination.levelis");
			String formatted = String.format(str, this.tileEntityChest.getChestLevel());
			ChatMessageHandler.sendChatToPlayer(openPlayer, Translation.localize(formatted));
			XYZPos xyz = XYZPos.tileEntityPosToXYZ(tileEntityChest);
			PacketParticle pp = new PacketParticle(xyz,5,3);
			Unsaga.packetDispatcher.sendTo(pp, (EntityPlayerMP) openPlayer);
		}

	}
	//	
	//    public static void generateChestContents(Random par0Random, WeightedRandomChestContent[] par1ArrayOfWeightedRandomChestContent, IInventory par2IInventory, int par3)
	//    {
	//        for (int j = 0; j < par3; ++j)
	//        {
	//            WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
	//            int k = weightedrandomchestcontent.theMinimumChanceToGenerateItem + par0Random.nextInt(weightedrandomchestcontent.theMaximumChanceToGenerateItem - weightedrandomchestcontent.theMinimumChanceToGenerateItem + 1);
	//
	//            ItemStack[] stacks = ChestGenHooks.generateStacks(par0Random, weightedrandomchestcontent.theItemId, weightedrandomchestcontent.theMinimumChanceToGenerateItem, weightedrandomchestcontent.theMaximumChanceToGenerateItem);
	//
	//            
	//            for (ItemStack item : stacks)
	//            {
	//            	if(item!=null){
	////            		if(item.getItem() instanceof ItemMagicTablet){
	////            			ItemMagicTablet.doActivateMagicTablet(par0Random, item);
	////            		}
	////            		if(item.getItem() instanceof ItemAccessory){
	////            			int id = par0Random.nextInt(ItemAccessory.abilities.length);
	////            			if(par0Random.nextInt(5)<2){
	////            				UtilSkill.setAbility(item, 0, id);
	////            			}
	////            			
	////            		}
	//            	}
	//                par2IInventory.setInventorySlotContents(par0Random.nextInt(par2IInventory.getSizeInventory()), item);
	//            }
	//        }
	//    }

	//    public static int getChestLevelFromPlayer(EntityPlayer ep){
	//    	int lv = ep.experienceLevel *3;
	//    	lv += ep.getRNG().nextInt(15)-10;
	//    	lv = MathHelper.clamp_int(lv, 1, 100);
	//    	return lv;
	//    }
}

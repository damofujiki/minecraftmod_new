package com.hinasch.unlsaga.item.armor;

import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.hinasch.lib.HelperInventory;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.client.model.ModelArmorColored;
import com.hinasch.unlsaga.init.UnsagaItems;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

public class ItemArmorUnsaga extends ItemArmor implements IUnsagaMaterialTool,IAbility{

	//必要なのかよくわからない
	public static int getRenderIndex(UnsagaMaterial mat){
		if(mat.isChild){
			if(mat.getParentMaterial()==Unsaga.materialManager.cloth){
				return 0;
			}
		}
		if(mat==Unsaga.materialManager.liveSilk){
			return 0;
		}
		return 1;
	}
	
	protected String[] armorTextureFiles;
	protected ToolCategory armorType;
	protected HelperUnsagaItem helper;
	protected UnsagaMaterial material;
	protected ModelArmorColored modelBiped;
	protected int armorTypeInt;
	
	protected String path = Unsaga.DOMAIN+":textures/models/armor/";
	
	
	public ItemArmorUnsaga(ToolCategory armorType, int armortypeint,UnsagaMaterial material) {
		super(material.getArmorMaterial(), getRenderIndex(material), armortypeint);
		this.material = material;
		this.armorType = armorType;
		this.armorTypeInt = armortypeint;
		this.armorTextureFiles = new String[2];
		this.armorTextureFiles[0] = path+getArmorTextureFilename(material)+".png";
		this.armorTextureFiles[1] = path+getArmorTextureFilename(material)+"2.png";
		this.helper = new HelperUnsagaItem(material, this.itemIcon, armorType);
		if(this.material == Unsaga.materialManager.crocodileLeather){
			modelBiped = new ModelArmorColored(1.001F);
		}else{
			modelBiped = new ModelArmorColored(1.08F);
		}
		
		Unsaga.items.putItemToMap(this,UnsagaItems.Key.makeKey(this.armorType, material));

		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		helper.addUnsagaItemInfo(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
	
	//染める
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot)
    {
    	modelBiped.bipedHead.showModel = armorSlot == 0;
    	modelBiped.bipedHeadwear.showModel = armorSlot == 0;
    	modelBiped.bipedBody.showModel = armorSlot == 1;
    	modelBiped.bipedCloak.showModel = armorSlot == 1;
    	modelBiped.bipedLeftArm.showModel = armorSlot == 1;
    	modelBiped.bipedRightArm.showModel = armorSlot == 1;
    	modelBiped.bipedRightLeg.showModel = (armorSlot == 3)||(armorSlot == 2);
    	modelBiped.bipedLeftLeg.showModel = (armorSlot == 3)||(armorSlot == 2);
    	
    	modelBiped.setItemStack(itemStack);
        return modelBiped;
    }
	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		return helper.getIsRepairable(par1ItemStack, par2ItemStack) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
		ItemArmorUnsaga armorunsaga = (ItemArmorUnsaga)stack.getItem();
		ToolCategory unsagatype = armorunsaga.armorType;
		UnsagaMaterial mate = HelperUnsagaItem.getMaterial(stack);
		for(int i=0;i<2;i++){
			if(mate.getSpecialArmorTexture(unsagatype, 0).isPresent()){
				this.armorTextureFiles[i] = path+mate.getSpecialArmorTexture(unsagatype, i).get()+".png";
			}
				
		}
		
		if(unsagatype==ToolCategory.HELMET || unsagatype==ToolCategory.BOOTS || unsagatype==ToolCategory.ARMOR)
		{
			return armorTextureFiles[0];
		}
		if(unsagatype==ToolCategory.LEGGINS)
		{
			return armorTextureFiles[1];
		}
		Unsaga.debug("Unknown ArmorType???");
		return armorTextureFiles[0];
	}

    public String getArmorTextureFilename(UnsagaMaterial mat){
//		if(getRenderIndex(mat)==0 && armorType.equals("boots")){
//			return "socks";
//		}
		return "armor";
		
	}
    
    @Override
    public int getColor(ItemStack par1ItemStack)
    {
		
		return helper.getColorFromItemStack(par1ItemStack, 0);
    }
    
    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {


		return helper.getColorFromItemStack(par1ItemStack, par2);
        
    }
    
	@Override
    public IIcon getIconFromDamageForRenderPass(int par1, int par2)
    {
        return this.itemIcon;
    }
	
	
//	@Override
//    public String getItemDisplayName(ItemStack par1ItemStack)
//    {
//    	if(HelperUnsagaWeapon.getMaterial(par1ItemStack).getSpecialName(this.armorType, 0).isPresent()){
//    		if(Translation.isJapanese()){
//    			return HelperUnsagaWeapon.getMaterial(par1ItemStack).getSpecialName(this.armorType, 1).get();
//    		}else{
//    			if(!HelperUnsagaWeapon.getMaterial(par1ItemStack).getSpecialName(this.armorType, 0).get().equals("*")){
//    				return HelperUnsagaWeapon.getMaterial(par1ItemStack).getSpecialName(this.armorType, 0).get();
//    			}
//    			
//    		}
//    	}
//    	
//        return super.getItemDisplayName(par1ItemStack);
//    }
	
//	@Override
//    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
//    {
//    	helper.getSubItems(par1, par2CreativeTabs, par3List);
//        
//    }
	
	//無理矢理頭にかぶる
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {

        //int i = EntityLiving.getArmorPosition(par1ItemStack) - 1;
		int num = par2World.rand.nextInt(4);
		HelperInventory invp = new HelperInventory(par3EntityPlayer.inventory);
        ItemStack itemstack1 = par3EntityPlayer.getCurrentArmor(num);

        if(par3EntityPlayer.isSneaking()){
            if (itemstack1 == null)
            {
            	invp.setArmor(num, par1ItemStack.copy());
            	//par3EntityPlayer.inventory.armorInventory[num] = par1ItemStack.copy();
                //par3EntityPlayer.setCurrentItemOrArmor(1, par1ItemStack.copy()); //Forge: Vanilla bug fix associated with fixed setCurrentItemOrArmor indexs for players.
            	par1ItemStack.stackSize --;
            }

            return par1ItemStack;
        }
        return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);

    }
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister)
	{
		if(this.material.getSpecialIcon(this.armorType).isPresent()){
			this.itemIcon = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+this.material.getSpecialIcon(armorType).get());
			return;
		}
		this.itemIcon = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+this.armorType.toString().toLowerCase());
	}
	
	@Override
    public boolean requiresMultipleRenderPasses()
    {
        return false;
    }
	
	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return this.armorType;
	}

	@Override
	public int getMaxAbility() {
		// TODO 自動生成されたメソッド・スタブ
		return 2;
	}

	@Override
	public UnsagaMaterial getMaterial() {
		// TODO 自動生成されたメソッド・スタブ
		return this.material;
	}

}

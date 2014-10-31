package com.hinasch.unlsaga.item.panel;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.hinasch.lib.UtilNBT;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.WorldSaveDataUnsaga;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSkillPanel extends Item{

	protected final String ICON_BASE = "panel_base";
	protected final String[] ICONTOP_NAMES = {"panel_key","panel_melee","panel_roll","panel_comu","panel_negative"};

	protected IIcon[] iconsTop;
	protected final int[] iconColors = {0x7cfc00,0x1e90ff,0xffc0cb,0xff8c00,0xdc143c};
	
	public ItemSkillPanel(){
		this.setTextureName(Unsaga.DOMAIN+":"+ICON_BASE);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.iconsTop = new IIcon[this.ICONTOP_NAMES.length];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
		for(SkillPanels.PanelData data:Unsaga.skillPanels.getPanels()){
			for(int i=0;i<5;i++){
				ItemStack is = new ItemStack(item,1,data.getID());
				setLevel(is,i);
				list.add(is);
			}
			
		}
    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	if(par3EntityPlayer.capabilities.isCreativeMode){
    		if(par3EntityPlayer.isSneaking()){
    			WorldSaveDataUnsaga.clearData(par2World);
    		}
    	}
    		
        return par1ItemStack;
    }
    
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    	int level = getLevel(par1ItemStack)+1;
    	par3List.add("Level "+level);
    }
    
	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		int damage = par1ItemStack.getItemDamage();
		if(par2==0){
			return this.iconColors[getLevel(par1ItemStack)];
		}
		return super.getColorFromItemStack(par1ItemStack, par2);
	}
	
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int meta = par1ItemStack.getItemDamage();
        return "item."+getPanelData(meta).getName();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(this.getIconString());
        for(int i=0;i<this.ICONTOP_NAMES.length;i++){
        	this.iconsTop[i] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+ICONTOP_NAMES[i]);
        }
    }
    
	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int pass)
	{
		if(pass==0){
			return this.itemIcon;
		}
		return this.iconsTop[getPanelData(par1).getIconNumber()];
	}
	
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	public SkillPanels.PanelData getPanelData(int damage){
		return Unsaga.skillPanels.getData(damage);
	}
	
	public static void setLevel(ItemStack is,int level){
		UtilNBT.setFreeTag(is, "level", level);
	}
	
	public static void setLock(ItemStack is,boolean par1){
		UtilNBT.setFreeTag(is, "lock", par1);
	}
	
	public static boolean hasLocked(ItemStack is){
		if(is.hasTagCompound()){
			return UtilNBT.readFreeTagBool(is, "lock");
		}
		return false;
	}
	public static int getLevel(ItemStack is){
		if(is.hasTagCompound()){
			return UtilNBT.readFreeTag(is, "level");
		}
		return 0;
	}
	
	@Override
    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        return 1;
    }
}

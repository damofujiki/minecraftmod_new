package com.hinasch.unlsaga.item.weapon.base;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.item.IUnsagaMaterialTool;
import com.hinasch.unlsaga.item.UnsagaEnum.ToolCategory;
import com.hinasch.unlsaga.material.UnsagaMaterial;
import com.hinasch.unlsaga.util.HelperUnsagaItem;

public class ItemStaffBase extends ItemSword implements IUnsagaMaterialTool,IAbility{

	private static final Set blocksEffectiveAgainst = Sets.newHashSet(new Block[] {Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail});

	public final UnsagaMaterial unsMaterial;
	public final ToolMaterial toolMaterial;
    public float efficiencyOnProperMaterial = 4.0F;
	//public static final Block[] blocksEffectiveAgainst;;
	protected final float weaponDamage;
	protected IIcon[] icons;
	
	public ItemStaffBase(UnsagaMaterial uns) {
		super(uns.getToolMaterial());
		this.unsMaterial = uns;
		this.icons = new IIcon[2];
		this.weaponDamage = 3.0F + uns.getToolMaterial().getDamageVsEntity();
		this.helper.init(uns, itemIcon, getCategory());
		this.toolMaterial = uns.getToolMaterial();
		this.efficiencyOnProperMaterial = uns.getToolMaterial().getEfficiencyOnProperMaterial();
		this.setMaxDamage((int)((float)uns.getToolMaterial().getMaxUses() * 0.7F));
		this.toolClass = "pickaxe";

	}

	@Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
	
	@Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		return helper.getIsRepairable(par1ItemStack, par2ItemStack) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
    }
	
	//thanx:http://forum.minecraftuser.jp/viewtopic.php?f=21&t=9494&start=300
	@Override
    public Multimap getItemAttributeModifiers()
    {
        return helper.getItemAttributeModifiers(field_111210_e, this.weaponDamage);
    }
	

    
	@Override
    public IIcon getIconFromDamageForRenderPass(int par1, int par2)
    {
		if(par2==0){
			return this.icons[0];
		}
        return this.icons[1];
    }
	
	@Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
		return helper.getColorFromItemStack(par1ItemStack, par2);
    }
	
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		helper.addUnsagaItemInfo(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
	
    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
    	this.itemIcon = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"staff");
    	this.icons[0] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"staff_1");
    	this.icons[1] = par1IconRegister.registerIcon(Unsaga.DOMAIN+":"+"staff_2");
    }



    
    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
    	if(HelperUnsagaItem.getCurrentWeight(par1ItemStack)>5){
    		return EnumAction.none;
    	}
        return EnumAction.block;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return par1ItemStack;
    }
    
    @Override
    public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_)
    {
        if ((double)p_150894_3_.getBlockHardness(p_150894_2_, p_150894_4_, p_150894_5_, p_150894_6_) != 0.0D)
        {
            p_150894_1_.damageItem(3, p_150894_7_);
        }

        return true;
    }
    
	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.STAFF;
	}

	@Override
	public int getMaxAbility() {
		// TODO 自動生成されたメソッド・スタブ
		return 1;
	}

	@Override
	public UnsagaMaterial getMaterial() {
		// TODO 自動生成されたメソッド・スタブ
		return this.unsMaterial;
	}
	
    private String toolClass;
    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass)
    {
        int level = super.getHarvestLevel(stack, toolClass);
        if (level == -1 && toolClass != null && toolClass.equals(this.toolClass))
        {
            return this.toolMaterial.getHarvestLevel();
        }
        else
        {
            return level;
        }
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack)
    {
        return toolClass != null ? ImmutableSet.of(toolClass) : super.getToolClasses(stack);
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta)
    {
        if (ForgeHooks.isToolEffective(stack, block, meta))
        {
            return efficiencyOnProperMaterial;
        }
        return super.getDigSpeed(stack, block, meta);
    }
    
    @Override
    public boolean func_150897_b(Block p_150897_1_)
    {
        return p_150897_1_ == Blocks.obsidian ? this.toolMaterial.getHarvestLevel() == 3 : (p_150897_1_ != Blocks.diamond_block && p_150897_1_ != Blocks.diamond_ore ? (p_150897_1_ != Blocks.emerald_ore && p_150897_1_ != Blocks.emerald_block ? (p_150897_1_ != Blocks.gold_block && p_150897_1_ != Blocks.gold_ore ? (p_150897_1_ != Blocks.iron_block && p_150897_1_ != Blocks.iron_ore ? (p_150897_1_ != Blocks.lapis_block && p_150897_1_ != Blocks.lapis_ore ? (p_150897_1_ != Blocks.redstone_ore && p_150897_1_ != Blocks.lit_redstone_ore ? (p_150897_1_.getMaterial() == Material.rock ? true : (p_150897_1_.getMaterial() == Material.iron ? true : p_150897_1_.getMaterial() == Material.anvil)) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2);
    }
    
    @Override
    public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_)
    {
        return this.blocksEffectiveAgainst.contains(p_150893_2_) ? this.efficiencyOnProperMaterial : 1.0F;
    }
    
    
}

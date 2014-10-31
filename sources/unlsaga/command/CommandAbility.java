package com.hinasch.unlsaga.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.collect.Lists;
import com.hinasch.lib.ChatHandler;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.ability.Ability;
import com.hinasch.unlsaga.ability.HelperAbility;
import com.hinasch.unlsaga.ability.IAbility;
import com.hinasch.unlsaga.ability.skill.HelperSkill;
import com.hinasch.unlsaga.item.armor.ItemAccessory;
import com.hinasch.unlsaga.item.armor.ItemArmorUnsaga;

public class CommandAbility extends CommandBase{

	@Override
	public int compareTo(Object o) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public String getCommandName() {
		// TODO 自動生成されたメソッド・スタブ
		return "setability";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		// TODO 自動生成されたメソッド・スタブ
		return "/setability <abilityID>";
	}

	@Override
	public List getCommandAliases() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
    
	@Override
	public void processCommand(ICommandSender sender, String[] params) {
		
		if(sender instanceof EntityPlayer && params!=null){
			if(params.length==1 && params[0]!=null){
				EntityPlayer player = (EntityPlayer)sender;
				int abilityNum = Integer.parseInt(params[0]);
				Ability ability = abilityNum>=0  && abilityNum<=4095 ? Unsaga.abilityManager.getAbilityFromInt(abilityNum) : null;
				if(ability==null){
					ChatHandler.sendChatToPlayer((EntityPlayer) sender, "illegal abilityID.");
					return;
				}
				if(!player.worldObj.isRemote){
					if(player.getHeldItem().getItem() instanceof IAbility){
						ItemStack is = player.getHeldItem();
						HelperAbility helper = null;


						if(is.getItem() instanceof ItemAccessory || is.getItem() instanceof ItemArmorUnsaga){
							helper =  new HelperAbility(is,(EntityPlayer)player);
								helper.setAbilityListToNBT(Lists.newArrayList(ability));
								ChatHandler.sendChatToPlayer((EntityPlayer) sender, "ability "+ability.getName(0)+" has set.");
								return;


						}else{
							 helper = new HelperSkill(is,(EntityPlayer)player);
							if(helper.isAbilityApplicable(ability)){
								helper.setAbilityListToNBT(Lists.newArrayList(ability));
								ChatHandler.sendChatToPlayer((EntityPlayer) sender, "ability "+ability.getName(0)+" has set.");
								return;
							}else{
								ChatHandler.sendChatToPlayer((EntityPlayer) sender, "this ability isn't applicable to this item:"+ability.getName(0));
							}
						}

					}else{
						ChatHandler.sendChatToPlayer((EntityPlayer) sender, "can't set ability to this item.");
					}
				}
			}else{
				ChatHandler.sendChatToPlayer((EntityPlayer) sender, "parameter error. ");
			}

		}
		
	}

//	@Override
//	public boolean canCommandSenderUseCommand(ICommandSender sender) {
//		if(sender instanceof EntityPlayer){
//			ItemStack is = ((EntityPlayer) sender).getHeldItem();
//			if(HelperAbility.canItemStackGainAbility(is)){
//				return true;
//			}
//		}
//		return false;
//	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}

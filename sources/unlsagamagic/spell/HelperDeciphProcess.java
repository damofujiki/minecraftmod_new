package com.hinasch.unlsagamagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;

import com.hinasch.lib.HSLibs;
import com.hinasch.unlsaga.Unsaga;
import com.hinasch.unlsaga.event.extendeddata.ExtendedPlayerData;
import com.hinasch.unlsaga.item.panel.SkillPanels;
import com.hinasch.unlsaga.util.ChatUtil;
import com.hinasch.unlsaga.util.FiveElements;
import com.hinasch.unlsaga.util.translation.Translation;
import com.hinasch.unlsagamagic.item.ItemTablet;

public class HelperDeciphProcess {

	public void progress(World world,EntityPlayer ep,ItemStack is){
			SpellMixTable table = Unsaga.magic.elementCalculator.getFiguredTable(world,ep);
			Spell spell = ItemTablet.getSpell(is);
			int difficultySpell = spell.difficultyDecipher;
			FiveElements.Enums elementMagic = spell.element;
			int elementpoint = table.getInt(elementMagic);
			int progressDecipher = (elementpoint*3) / HSLibs.exceptZero(difficultySpell/2);
			if(progressDecipher<2){
				progressDecipher=2;
			}
			progressDecipher *= 2; 
			if(!world.isRemote){
				String mes = Translation.localize("msg.spell.deciphering.progress");
				String formatted = String.format(mes, progressDecipher);
				ChatUtil.addMessageNoLocalized(ep, formatted);
			}
			if(SkillPanels.hasPanel(world, ep, Unsaga.skillPanels.knowledgeAncient)){
				int level = SkillPanels.getHighestLevelOfPanel(world, ep, Unsaga.skillPanels.knowledgeAncient)+1;
				progressDecipher += (level*2);
			}
			Unsaga.debug("解読度:"+progressDecipher+":"+is);
			ItemTablet.progressDeciphering(ep, is, progressDecipher);
		
	}
	
	public void progressOnTakingXP(PlayerPickupXpEvent e){
			ExtendedPlayerData data = ExtendedPlayerData.getData(e.entityPlayer);
				if(data.getTablet()!=null){
					progress(e.entityPlayer.worldObj,e.entityPlayer,data.getTablet());
				}
			
		
	}
}

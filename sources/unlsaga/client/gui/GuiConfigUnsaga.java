package com.hinasch.unlsaga.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import com.hinasch.unlsaga.Unsaga;

import cpw.mods.fml.client.config.GuiConfig;

public class GuiConfigUnsaga extends GuiConfig{

	public GuiConfigUnsaga(GuiScreen parentScreen) {
		super(parentScreen, new ConfigElement(Unsaga.configFile.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), Unsaga.MODID, "config.unsaga", false,
				false, "Unsaga Mod Configuration", GuiConfig.getAbridgedConfigPath(Unsaga.configFile.toString()));
		// TODO 自動生成されたコンストラクター・スタブ
	}

}

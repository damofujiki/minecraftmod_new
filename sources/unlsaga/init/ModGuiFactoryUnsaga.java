package com.hinasch.unlsaga.init;

import java.util.Set;

import com.hinasch.unlsaga.client.gui.GuiConfigUnsaga;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.IModGuiFactory;

public class ModGuiFactoryUnsaga implements IModGuiFactory{

	@Override
	public void initialize(Minecraft minecraftInstance) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		// TODO 自動生成されたメソッド・スタブ
		return GuiConfigUnsaga.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(
			RuntimeOptionCategoryElement element) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}

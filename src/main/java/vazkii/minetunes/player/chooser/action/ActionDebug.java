package vazkii.minetunes.player.chooser.action;

import java.io.File;

import vazkii.minetunes.gui.GuiDevTools;

public class ActionDebug implements ISelectorAction {

	public static final ActionDebug instance = new ActionDebug();
	
	@Override
	public void select(File file) {
		GuiDevTools.debugLog("File: " + file.getAbsolutePath());		
	}

}

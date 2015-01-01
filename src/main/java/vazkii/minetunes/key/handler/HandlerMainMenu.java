package vazkii.minetunes.key.handler;

import java.util.concurrent.Callable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import vazkii.minetunes.gui.GuiMineTunesMenu;
import vazkii.minetunes.key.KeyHandler;

public class HandlerMainMenu extends KeyHandler {

	@Override
	public void keyDown(KeyBinding key) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.currentScreen == null)
			mc.displayGuiScreen(new GuiMineTunesMenu());
	}

	@Override
	public void keyUp(KeyBinding key) {
		// NO-OP
	}


}

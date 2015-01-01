package vazkii.minetunes.gui;

import net.minecraft.client.gui.GuiScreen;

public class GuiMineTunes extends GuiScreen {

	public void drawBox(int x, int y, int width, int height) {
		drawRect(x, y, x + width, y + height, 0x55000000);
		drawRect(x - 2, y - 2, x + width + 2, y + height + 2, 0x44000000);
	}
	
}

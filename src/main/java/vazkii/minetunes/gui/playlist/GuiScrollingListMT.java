package vazkii.minetunes.gui.playlist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.GuiScrollingList;

public abstract class GuiScrollingListMT extends GuiScrollingList {

	public GuiScrollingListMT(int width, int height, int top, int left, int entryHeight) {
		super(Minecraft.getMinecraft(), width, height, top, top + height, left, entryHeight);
	}
	
	@Override
	protected void drawBackground() {
		// TODO Auto-generated method stub
	}

}

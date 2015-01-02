package vazkii.minetunes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.relauncher.ReflectionHelper;

public abstract class GuiScrollingListMT extends GuiScrollingList {

	public GuiScrollingListMT(int width, int height, int top, int left, int entryHeight) {
		super(Minecraft.getMinecraft(), width, height, top, top + height, left, entryHeight);
	}
	
	public void resetScroll() {
		ReflectionHelper.setPrivateValue(GuiScrollingList.class, this, 0F, "scrollDistance");
	}
	
	@Override
	protected void drawBackground() {
		// TODO Auto-generated method stub
	}

}

package vazkii.minetunes.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import vazkii.minetunes.config.MTConfig;

// Stolen from ReCubed
// FOR SHAME!
// Oh wait I made that mod
public class GuiMoveHUD extends GuiMineTunes {

	int originalPosX, originalPosY, originalRelativePos;

	public GuiMoveHUD() {
		originalPosX = MTConfig.hudPosX;
		originalPosY = MTConfig.hudPosY;
		originalRelativePos = MTConfig.hudRelativeTo;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
		int mx = par1;
		int my = par2;
		int quadrant = getQuadrant(width, height, mx, my);

		String clickToSet = StatCollector.translateToLocal("minetunes.gui.clickToSet");
		String escapeToReset = StatCollector.translateToLocal("minetunes.gui.escapeToReset");
		
		int sWidth = Math.max(fontRendererObj.getStringWidth(clickToSet), fontRendererObj.getStringWidth(escapeToReset));
		
		drawBox(width / 2 - sWidth / 2 - 4, 16, sWidth + 8, 29);
		
		drawCenteredString(fontRendererObj, clickToSet, width / 2, 20, 0xFFFFFF);
		drawCenteredString(fontRendererObj, escapeToReset, width / 2, 31, 0xFFFFFF);

		switch(quadrant) {
		case 0 : {
			mx = width - mx;
			break;
		}
		case 1 : break;
		case 2 : {
			my = height - my;
			break;
		}
		case 3 : {
			mx = width - mx;
			my = height - my;
			break;
		}
		}

		fontRendererObj.drawStringWithShadow("W", 0, 0, 0xFFFFFF);
		fontRendererObj.drawStringWithShadow("A", 0, height - 9, 0xFFFFFF);
		fontRendererObj.drawStringWithShadow("S", width - 6, height - 9, 0xFFFFFF);
		fontRendererObj.drawStringWithShadow("D", width - 6, 0, 0xFFFFFF);

		MTConfig.hudPosX = mx;
		MTConfig.hudPosY = my;
		MTConfig.hudRelativeTo = quadrant;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		if(par3 == 0)
			saveAndExit();

		super.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		if(par2 == 1) {
			MTConfig.hudPosX = originalPosX;
			MTConfig.hudPosY = originalPosY;
			MTConfig.hudRelativeTo = originalRelativePos;
			
			saveAndExit();
		}

		switch(par1) {
		case 'w' : {
			MTConfig.hudPosX = 0;
			MTConfig.hudPosY = 0;
			MTConfig.hudRelativeTo = 1;

			saveAndExit();
			return;
		}
		case 'a' : {
			MTConfig.hudPosX = 0;
			MTConfig.hudPosY = 0;
			MTConfig.hudRelativeTo = 2;

			saveAndExit();
			return;
		}
		case 's' : {
			MTConfig.hudPosX = 0;
			MTConfig.hudPosY = 0;
			MTConfig.hudRelativeTo = 3;

			saveAndExit();
			return;
		}
		case 'd' : {
			MTConfig.hudPosX = 0;
			MTConfig.hudPosY = 0;
			MTConfig.hudRelativeTo = 0;

			saveAndExit();
			return;
		}
		}
	}
	
	private void saveAndExit() {
		MTConfig.findCompoundAndWrite();
		mc.displayGuiScreen(new GuiPlaylistManager());
	}

	private int getQuadrant(int width, int height, int mx, int my) {
		boolean xpasses = mx >= width / 2;
		boolean ypasses = my >= height / 2;

		if(xpasses) {
			if(ypasses)
				return 3;
			return 0;
		}

		if(ypasses)
			return 2;
		return 1;
	}

}


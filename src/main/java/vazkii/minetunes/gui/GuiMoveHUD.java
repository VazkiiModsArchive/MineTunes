package vazkii.minetunes.gui;

import java.io.IOException;

import net.minecraft.client.resources.I18n;
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

		String clickToSet = I18n.format("minetunes.gui.clickToSet");
		String escapeToReset = I18n.format("minetunes.gui.escapeToReset");
		
		int sWidth = Math.max(fontRenderer.getStringWidth(clickToSet), fontRenderer.getStringWidth(escapeToReset));
		
		drawBox(width / 2 - sWidth / 2 - 4, 16, sWidth + 8, 29);
		
		drawCenteredString(fontRenderer, clickToSet, width / 2, 20, 0xFFFFFF);
		drawCenteredString(fontRenderer, escapeToReset, width / 2, 31, 0xFFFFFF);

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

		fontRenderer.drawStringWithShadow("W", 0, 0, 0xFFFFFF);
		fontRenderer.drawStringWithShadow("A", 0, height - 9, 0xFFFFFF);
		fontRenderer.drawStringWithShadow("S", width - 6, height - 9, 0xFFFFFF);
		fontRenderer.drawStringWithShadow("D", width - 6, 0, 0xFFFFFF);

		MTConfig.hudPosX = mx;
		MTConfig.hudPosY = my;
		MTConfig.hudRelativeTo = quadrant;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException {
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


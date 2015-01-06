package vazkii.minetunes.player;

import java.awt.Color;
import java.awt.Point;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import vazkii.minetunes.MineTunes;
import vazkii.minetunes.config.MTConfig;
import vazkii.minetunes.gui.GuiMoveHUD;
import vazkii.minetunes.playlist.MP3Metadata;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class HUDHandler {

	public static boolean showVolume = false;
	
	private float[] oldFFT = new float[SpectrumTools.BANDS];
	
	@SubscribeEvent
	public void onDrawScreen(RenderGameOverlayEvent.Post event) {
		if(event.type == ElementType.ALL && MTConfig.hudEnabled) {
			Minecraft mc = Minecraft.getMinecraft();
			int width = event.resolution.getScaledWidth();
			int height = event.resolution.getScaledHeight();
			Point coords = getCoords(width, height, MTConfig.hudRelativeTo, MTConfig.hudPosX, MTConfig.hudPosY);

			if(MineTunes.musicPlayerThread != null && MineTunes.musicPlayerThread.player != null && MineTunes.musicPlayerThread.playingMP3 != null) {
				MP3Metadata meta = MineTunes.musicPlayerThread.playingMP3;

				boolean rightSide = MTConfig.hudRelativeTo == 0 || MTConfig.hudRelativeTo == 3;

				String note = MineTunes.musicPlayerThread.paused ? "\u258E\u258E" : "\u266C";
				int noteWidth = mc.fontRenderer.getStringWidth(note) * 2;
				int noteSpace = 4;

				String time = MP3Metadata.getLengthStr((int) ((double) meta.lengthMs - (double) meta.lengthMs * MineTunes.musicPlayerThread.getFractionPlayed()));
				String title = meta.title + " (" + time + ")";
				String artist = meta.artist;
				String volume = showVolume ? (String.format(StatCollector.translateToLocal("minetunes.gui.volume"), "+" + (int) (MineTunes.musicPlayerThread.getRelativeVolume() * 100) + "%")) : "";

				int padding = 4;

				int titleWidth = mc.fontRenderer.getStringWidth(title);
				int artistWidth = mc.fontRenderer.getStringWidth(artist);
				int volumeWidth = mc.fontRenderer.getStringWidth(volume);
				
				int textWidth = Math.max(titleWidth, Math.max(artistWidth, volumeWidth));
				int hudWidth = textWidth + noteWidth + noteSpace + padding * 2;
				int hudHeight = (showVolume ? 30 : 20) + padding * 2;

				int x = coords.x;
				int y = coords.y;
				
				if(rightSide)
					x -= hudWidth;

				if(x < 0)
					x = padding;
				if(y < 0)
					y = 0;

				int xf = x + hudWidth;
				int yf = y + hudHeight;

				if(xf > width)
					x -= (xf - width);
				if(yf > height)
					y -= (yf - height);

				Color color = Color.getHSBColor((float) (System.currentTimeMillis() % 5000) / 5000F, 1F, 1F);

				int noteX = x + padding + (rightSide ? textWidth + noteSpace : 0);
				int noteY = y + padding;
				int noteColor = color.getRGB();

				GL11.glPushMatrix();
				GL11.glScalef(2F, 2F, 2F);
				GL11.glTranslatef((float) noteX / 2, (float) noteY / 2, 0F);
				GL11.glTranslatef(0.5F, 0.5F, 0F);
				mc.fontRenderer.drawString(note, 0, 0, color.darker().darker().getRGB());
				GL11.glTranslatef(-0.5F, -0.5F, 0F);
				mc.fontRenderer.drawString(note, 0, 0, noteColor);
				GL11.glPopMatrix();

				int diffTitle = 0;
				int diffArtist = 0;
				int diffVolume = 0;

				int textLeft = x + padding + (rightSide ? 0 : noteWidth + noteSpace);
				int spectrumLeft = textLeft;
				
				if(rightSide) {
					diffTitle = textWidth - titleWidth;
					diffArtist = textWidth - artistWidth;
					diffVolume = textWidth - volumeWidth;
				}

				int spaceWidth = SpectrumTools.BANDS - 1;
				int minWidth = 2 * SpectrumTools.BANDS + spaceWidth;
				int analyzerWidth = Math.max(minWidth, textWidth * 2 - spaceWidth);
				renderSpectrumAnalyzer(mc, textLeft, y + padding + 20, analyzerWidth, 150, noteColor);
				
				mc.fontRenderer.drawStringWithShadow(title, textLeft + diffTitle, y + padding, 0xFFFFFF);
				mc.fontRenderer.drawStringWithShadow(artist, textLeft + diffArtist, y + 10 + padding, 0xDDDDDD);
				if(showVolume)
					mc.fontRenderer.drawStringWithShadow(volume, textLeft + diffVolume, y + 20 + padding, 0xDDDDDD);
				
			}
		}
	}

	// Adapted from kjdsp
	// https://code.google.com/p/libkj-java/
	private void renderSpectrumAnalyzer(Minecraft mc, int x, int y, int width, int height, int color) {
    	float[] wFFT = SpectrumTools.getFFTCalculation();
    	if(wFFT == null)
    		return;
    	
    	int bandWidth = (int) ((float) width / (float) SpectrumTools.FFT_SAMPLE_SIZE);
    	float multiplier = (SpectrumTools.FFT_SAMPLE_SIZE / 2) / SpectrumTools.BANDS;
		
    	int lightColor = 0xFF << 24 | color;
    	int darkColor = new Color(lightColor).darker().getRGB();
    	
    	for(int i = 0; i < SpectrumTools.BANDS; i++) {
    		float wFs = 0;
    		// -- Average out nearest bands.
    		for(int j = 0; j < multiplier; j++)
    			wFs += wFFT[i + j];
    		
    		// -- Log filter.
    		wFs = (wFs * (float) Math.log(i + 2));  
    		
    		if(wFs > 1.0f) 
    			wFs = 1.0f; 
    		
    		// -- Compute SA decay...
    		if(wFs >= (oldFFT[i] - SpectrumTools.DECAY))
    			oldFFT[i] = wFs;
    		else {
    			oldFFT[i] -= SpectrumTools.DECAY;
    			
    			if(oldFFT[i] < 0)
    				oldFFT[i] = 0;
    			
    			wFs = oldFFT[i];
    		}
    		
    		int bHeight = (int) ((float) height * wFs) + 1;
    		
    		
    		Gui.drawRect(x + i, y - bHeight, x + bandWidth + i, y, i % 2 == 0 ? lightColor : darkColor);
    		
    		x += bandWidth;
    	}
	}
	
	private static boolean shouldRenderHUD() {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.currentScreen != null && mc.currentScreen instanceof GuiMoveHUD)
			return true;
		return MTConfig.hudEnabled;
	}

	public static Point getCoords(int screenX, int screenY, int relativePos, int posX, int posY) {
		switch(relativePos) {
		case 0 : return new Point(screenX - posX, posY);
		case 1 : return new Point(posX, posY);
		case 2 : return new Point(posX, screenY - posY);
		default : return new Point(screenX - posX, screenY - posY);
		}
	}

}
package vazkii.minetunes.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import vazkii.minetunes.MineTunes;
import vazkii.minetunes.player.chooser.FileSelector;
import vazkii.minetunes.player.chooser.action.ActionDebug;
import vazkii.minetunes.player.chooser.action.ActionPlayMp3;
import vazkii.minetunes.player.chooser.filter.MusicFilter;
import vazkii.minetunes.player.chooser.filter.PlaylistFilter;

public class GuiDevTools extends GuiMineTunes {

	private static final int max = 35;
	private volatile static List<String> debugOut = new ArrayList(max);
			
	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(-1, width - 102, height - 22, 100, 20, StatCollector.translateToLocal("minetunes.gui.exit")));
		
		buttonList.add(new GuiButton(0, 10, 30, 200, 20, StatCollector.translateToLocal("minetunes.guidev.clear")));
		buttonList.add(new GuiButton(1, 10, 55, 200, 20, StatCollector.translateToLocal("minetunes.guidev.resetThread")));
		buttonList.add(new GuiButton(2, 10, 80, 200, 20, StatCollector.translateToLocal("minetunes.guidev.chooser")));
		buttonList.add(new GuiButton(3, 10, 105, 200, 20, StatCollector.translateToLocal("minetunes.guidev.playMp3")));
		buttonList.add(new GuiButton(4, 10, 130, 200, 20, StatCollector.translateToLocal("minetunes.guidev.playLast")));
		buttonList.add(new GuiButton(5, 10, 155, 200, 20, StatCollector.translateToLocal("minetunes.guidev.playPause")));
		buttonList.add(new GuiButton(6, 10, 180, 200, 20, StatCollector.translateToLocal("minetunes.guidev.volumeControl")));
	}
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		int boxHeight = (buttonList.size() - 1) * 25 + 50; 
		drawBox(0, -4, 220, boxHeight);
		
		for(int i = 0; i < debugOut.size(); i++)
			fontRendererObj.drawStringWithShadow(debugOut.get(debugOut.size() - i - 1), 240, (i + 1) * 10, 0xFFFFFF);
		
		super.drawScreen(mx, my, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch(button.id) {
		case -1:
			mc.displayGuiScreen(null);
			break;
		case 0:
			debugOut.clear();
			break;
		case 1:
			if(MineTunes.musicPlayerThread != null)
				MineTunes.musicPlayerThread.forceKill();
			MineTunes.startThread();
			debugLog("Reset Thread: " + MineTunes.musicPlayerThread);
			
			break;
		case 2:
			new FileSelector(new PlaylistFilter(), JFileChooser.FILES_AND_DIRECTORIES, ActionDebug.instance);
			break;
		case 3:
			new FileSelector(new MusicFilter(), JFileChooser.FILES_ONLY, ActionPlayMp3.instance);
			break;
		case 4:
			ActionPlayMp3.instance.playLast();
			break;
		case 5:
			if(MineTunes.musicPlayerThread != null)
				MineTunes.musicPlayerThread.pauseOrPlay();
			break;
		case 6:
			if(MineTunes.musicPlayerThread != null) {
				float gainVal = 0.5F;
				MineTunes.musicPlayerThread.addGain(isShiftKeyDown() ? -gainVal : gainVal);
				float gain = MineTunes.musicPlayerThread.getGain();
				debugLog("Audio Gain: " + gain + " (Relative Volume: " + MineTunes.musicPlayerThread.getRelativeVolume(gain) + ")");
			}
			break;
		}
	}
	
	public static void debugLog(String s) {
		debugLog(s, true);
	}
	
	public static void debugLog(String s, boolean print) {
		if(MineTunes.DEBUG_MODE) {
			debugOut.add(s);
			if(print)
				System.out.println(s);
			
			while(debugOut.size() > max)
				debugOut.remove(0);
		}
	}
	
	public static void logThrowable(Throwable e) {
		e.printStackTrace();
		for(int i = e.getStackTrace().length - 1; i >= 0; i--)
			debugLog(EnumChatFormatting.DARK_RED + e.getStackTrace()[i].toString(), false);
	}
	
}

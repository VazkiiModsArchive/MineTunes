package vazkii.minetunes.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.minecraft.util.EnumChatFormatting;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import vazkii.minetunes.MineTunes;
import vazkii.minetunes.gui.GuiDevTools;

public class ThreadMusicPlayer extends Thread {

	AdvancedPlayer player;
	volatile File queuedFile;

	boolean kill = false;

	public ThreadMusicPlayer() {
		setDaemon(true);
		setName("mineTunes Player Thread");
		start();
	}

	@Override
	public void run() {
		try {
			GuiDevTools.debugLog("Starting " + this);
			while(!kill) {
				if(queuedFile != null) {
					GuiDevTools.debugLog("Queued: " + queuedFile.getAbsolutePath());
					resetPlayer();
					player = new AdvancedPlayer(new FileInputStream(queuedFile));
					queuedFile = null;
					GuiDevTools.debugLog("Player Loaded");
				}

				boolean played = false;
				if(player != null) {
					GuiDevTools.debugLog("Playing File");
					played = player.play(Integer.MAX_VALUE);
				}

				if(played) {
					GuiDevTools.debugLog("Reached Final Frame, next...");
					next();
				}
			}
		} catch(Exception e) {
			GuiDevTools.logThrowable(e);
		}
	}

	public void next() {
		resetPlayer();
	}

	public void resetPlayer() {
		GuiDevTools.debugLog("Resetting Player.");

		if(player != null)
			player.close();

		player = null;
	}

	public void play(File file) {
		resetPlayer();
		queuedFile = file;
	}

	public void forceKill() {
		try {
			resetPlayer();
			interrupt();

			finalize();
			kill = true;
		} catch(Throwable e) {
			GuiDevTools.logThrowable(e);
		}
	}
}

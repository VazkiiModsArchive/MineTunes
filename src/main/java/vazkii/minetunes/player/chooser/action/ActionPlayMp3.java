package vazkii.minetunes.player.chooser.action;

import java.io.File;

import vazkii.minetunes.MineTunes;

public class ActionPlayMp3 implements ISelectorAction {

	public static final ActionPlayMp3 instance = new ActionPlayMp3();
	
	String lastFile = ""; 
	
	@Override
	public void select(File file) {
		lastFile = file.getAbsolutePath();
		play(file);
	}
	
	public void play(File file) {
		if(MineTunes.musicPlayerThread == null)
			MineTunes.startThread();
		
		MineTunes.musicPlayerThread.play(file);
		ActionDebug.instance.select(file);
	}
	
	public void playLast() {
		File file = new File(lastFile);
		if(file.exists() && file.getName().endsWith(".mp3"))
			play(file);
	}

}

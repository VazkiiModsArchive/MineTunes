package vazkii.minetunes.player.chooser.action;

import java.io.File;

import vazkii.minetunes.MineTunes;

public class ActionPlayMp3 implements ISelectorAction {

	public static final ActionPlayMp3 instance = new ActionPlayMp3();
	
	@Override
	public void select(File file) {
		if(MineTunes.musicPlayerThread == null)
			MineTunes.startThread();
		
		MineTunes.musicPlayerThread.play(file);
		ActionDebug.instance.select(file);
	}

}

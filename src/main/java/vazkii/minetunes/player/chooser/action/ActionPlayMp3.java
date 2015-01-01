package vazkii.minetunes.player.chooser.action;

import java.io.File;

public class ActionPlayMp3 implements ISelectorAction {

	public static final ActionPlayMp3 instance = new ActionPlayMp3();
	
	@Override
	public void select(File file) {
		// TODO
		ActionDebug.instance.select(file);
	}

}

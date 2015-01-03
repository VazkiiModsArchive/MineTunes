package vazkii.minetunes.key.handler;

import net.minecraft.client.settings.KeyBinding;
import vazkii.minetunes.MineTunes;
import vazkii.minetunes.key.KeyHandler;

public class HandlerPlayPause extends KeyHandler {

	boolean down = false;
	
	@Override
	public void keyDown(KeyBinding key) {
		if(!down && isInValidGui()) {
			if(MineTunes.musicPlayerThread != null)
				MineTunes.musicPlayerThread.pauseOrPlay();
			else {
				MineTunes.startMusicPlayerThread();
				MineTunes.musicPlayerThread.next();
			}
		}
			
		
		down = true;
	}

	@Override
	public void keyUp(KeyBinding key) {
		down = false;
	}

}

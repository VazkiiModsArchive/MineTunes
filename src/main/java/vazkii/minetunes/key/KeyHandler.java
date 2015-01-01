package vazkii.minetunes.key;

import net.minecraft.client.settings.KeyBinding;

public abstract class KeyHandler {

	public abstract void keyDown(KeyBinding key);
	
	public abstract void keyUp(KeyBinding key);
	
}

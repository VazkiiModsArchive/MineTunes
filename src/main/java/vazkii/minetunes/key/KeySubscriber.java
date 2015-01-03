package vazkii.minetunes.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class KeySubscriber {

	public static final KeySubscriber instance = new KeySubscriber();
	
	@SubscribeEvent
	public void playerTick(ClientTickEvent event) {
		if(event.phase == Phase.START && Minecraft.getMinecraft().thePlayer != null) {
			for(KeyBinding key : KeyBindings.handlers.keySet()) {
				KeyHandler handler = KeyBindings.handlers.get(key);
				if(key.getIsKeyPressed())
					handler.keyDown(key);
				else handler.keyUp(key);
			}
		}
	}
	
}

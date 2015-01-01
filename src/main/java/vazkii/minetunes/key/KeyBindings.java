package vazkii.minetunes.key;

import java.util.HashMap;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import vazkii.minetunes.key.handler.HandlerMainMenu;
import vazkii.minetunes.lib.LibKeys;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public final class KeyBindings {

	public static HashMap<KeyBinding, KeyHandler> handlers = new HashMap();
	
	public static KeyBinding keyMenu;
	
	public static void init() {
		keyMenu = registerKey(new KeyBinding(LibKeys.MENU, Keyboard.KEY_HOME, LibKeys.KEY_CATEGORY), new HandlerMainMenu());
		
		FMLCommonHandler.instance().bus().register(KeySubscriber.instance);
	}
	
	private static KeyBinding registerKey(KeyBinding key, KeyHandler handler) {
		ClientRegistry.registerKeyBinding(key);
		handlers.put(key, handler);
		return key;
	}
	
}

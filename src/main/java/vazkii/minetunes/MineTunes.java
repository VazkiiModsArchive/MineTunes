package vazkii.minetunes;

import java.lang.management.ManagementFactory;

import vazkii.minetunes.key.KeyBindings;
import vazkii.minetunes.lib.LibMisc;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION)
public class MineTunes {

	public static final boolean DEBUG_MODE = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		KeyBindings.init();
	}
	
}

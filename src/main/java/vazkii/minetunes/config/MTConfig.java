package vazkii.minetunes.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.minetunes.gui.GuiDevTools;
import vazkii.minetunes.lib.LibMisc;

public final class MTConfig {

	private static final String CONFIG_FILE = "config.dat";

	private static final String TAG_PLAY_MODE = "playMode";
	private static final String TAG_HUD_ENABLED = "hudEnabled";
	private static final String TAG_HUD_RELATIVE_TO = "hudRelativeTo";
	private static final String TAG_HUD_POS_X = "hudPosX";
	private static final String TAG_HUD_POS_Y = "hudPosY";

	public static int playMode = 0;

	public static boolean hudEnabled = true;
	public static int hudRelativeTo = 0;
	public static int hudPosX = 0;
	public static int hudPosY = 0;	

	public static void findCompoundAndLoad() {
		try {
			File file = CacheHelper.getCacheFile(CONFIG_FILE);
			NBTTagCompound cmp = CacheHelper.getCacheCompound(file);

			if(cmp.hasNoTags())
				findCompoundAndWrite(file);
			else {
				playMode = cmp.getInteger(TAG_PLAY_MODE);
				
				hudEnabled = cmp.getBoolean(TAG_HUD_ENABLED);
				hudRelativeTo = cmp.getInteger(TAG_HUD_RELATIVE_TO);
				hudPosX = cmp.getInteger(TAG_HUD_POS_X);
				hudPosY = cmp.getInteger(TAG_HUD_POS_Y);
			}
		} catch (IOException e) {
			GuiDevTools.logThrowable(e);
		}
	}

	public static void findCompoundAndWrite() {
		try {
			findCompoundAndWrite(CacheHelper.getCacheFile(CONFIG_FILE));
		} catch (IOException e) {
			GuiDevTools.logThrowable(e);
		}
	}

	public static void findCompoundAndWrite(File f) {
		NBTTagCompound cmp = new NBTTagCompound();

		cmp.setInteger(TAG_PLAY_MODE, playMode);
		
		cmp.setBoolean(TAG_HUD_ENABLED, hudEnabled);
		cmp.setInteger(TAG_HUD_RELATIVE_TO, hudRelativeTo);
		cmp.setInteger(TAG_HUD_POS_X, hudPosX);
		cmp.setInteger(TAG_HUD_POS_Y, hudPosY);

		CacheHelper.injectNBTToFile(f, cmp);
	}

}

package vazkii.minetunes.playlist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.minetunes.MineTunes;
import vazkii.minetunes.config.CacheHelper;
import vazkii.minetunes.gui.GuiDevTools;

// So meta
public final class PlaylistList {

	private static final String PLAYLISTS_FILE = "playlists.dat";

	private static final String TAG_PLAYLIST_COUNT = "playlistCount";
	private static final String TAG_PLAYLIST_PREFIX = "playlist";
	
	public static volatile List<String> playlistNames = new ArrayList();
	public static volatile HashMap<String, Playlist> playlists = new LinkedHashMap();

	public static void loadPlaylist(File file, String name) {
		if(MineTunes.playlistCreatorThread == null)
			MineTunes.startPlaylistCreatorThread(file, name);
	}

	public static void findCompoundAndLoad() {
		try {
			playlists.clear();
			playlistNames.clear();
			
			File file = CacheHelper.getCacheFile(PLAYLISTS_FILE);
			NBTTagCompound cmp = CacheHelper.getCacheCompound(file);
			
			int count = cmp.getInteger(TAG_PLAYLIST_COUNT);
			for(int i = 0; i < count; i++) {
				NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_PLAYLIST_PREFIX + i);
				Playlist playlist = Playlist.build(cmp1);
				playlistNames.add(playlist.storeName);
				playlists.put(playlist.storeName, playlist);
			}
		} catch (IOException e) {
			GuiDevTools.logThrowable(e);
		}
	}

	public static void findCompoundAndWrite() {
		try {
			findCompoundAndWrite(CacheHelper.getCacheFile(PLAYLISTS_FILE));
		} catch (IOException e) {
			GuiDevTools.logThrowable(e);
		}
	}

	public static void findCompoundAndWrite(File f) {
		NBTTagCompound cmp = new NBTTagCompound();

		cmp.setInteger(TAG_PLAYLIST_COUNT, playlists.size());
		
		int i = 0;
		for(String key : playlists.keySet()) {
			Playlist playlist = playlists.get(key);
			NBTTagCompound cmp1 = new NBTTagCompound();
			playlist.writeToNBT(cmp1, key);
			
			cmp.setTag(TAG_PLAYLIST_PREFIX + i, cmp1);
			i++;
		}

		CacheHelper.injectNBTToFile(f, cmp);
	}

}

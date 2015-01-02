package vazkii.minetunes.playlist;

import java.io.File;
import java.util.HashMap;

import vazkii.minetunes.MineTunes;
import vazkii.minetunes.gui.GuiDevTools;

// So meta
public final class PlaylistList {

	public static final HashMap<String, Playlist> playlists = new HashMap();
	
	public static void loadPlaylist(File file, String name) {
		if(MineTunes.playlistCreatorThread == null)
			MineTunes.startPlaylistCreatorThread(file, name);
	}
	
}

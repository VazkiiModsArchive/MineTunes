package vazkii.minetunes.playlist;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vazkii.minetunes.MineTunes;

// So meta
public final class PlaylistList {

	public static final List<String> playlistNames = new ArrayList();
	public static final HashMap<String, Playlist> playlists = new HashMap();
	
	public static void loadPlaylist(File file, String name) {
		if(MineTunes.playlistCreatorThread == null)
			MineTunes.startPlaylistCreatorThread(file, name);
	}
	
}

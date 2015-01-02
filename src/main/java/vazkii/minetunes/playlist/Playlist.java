package vazkii.minetunes.playlist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vazkii.minetunes.config.MTConfig;
import vazkii.minetunes.gui.playlist.GuiPlaylistManager;
import vazkii.minetunes.playlist.provider.IProviderStateCallback;
import vazkii.minetunes.playlist.provider.PlaylistProvider;
import vazkii.minetunes.playlist.provider.ProviderDirectory;
import vazkii.minetunes.playlist.provider.ProviderM3U;

public class Playlist {

	public List<MP3Metadata> metadataList = new ArrayList();

	Random rand = new Random();

	public static Playlist build(File file, IProviderStateCallback callback) {
		PlaylistProvider provider = file.isDirectory() ? ProviderDirectory.instance : file.getName().endsWith(".m3u") ? ProviderM3U.instance : null;
		return provider == null ? null : provider.provide(file, callback);
	}

	public MP3Metadata nextSong() {
		int index = nextSongIndex();
		boolean valid = index >= 0 && index < metadataList.size();
		
		if(valid) {
			GuiPlaylistManager.selectCurrentSong(index);
			return metadataList.get(index);
		}
		
		return null;
	}

	public int nextSongIndex() {
		MTConfig.playMode = 3;
		
		switch(MTConfig.playMode) {
		case 1: return repeat();
		case 2: return loop();
		case 3: return shuffle();
		}
		return stop();
	}

	public int stop() {
		return -1;
	}

	public int repeat() {
		return GuiPlaylistManager.getCurrentSong();
	}

	public int loop() {
		int selected = GuiPlaylistManager.getCurrentSong();
		int index = selected + 1;

		if(index >= metadataList.size())
			index = 0;

		return index;
	}

	public int shuffle() {
		int selected = GuiPlaylistManager.getCurrentSong();
		int size = metadataList.size();
		if(size < 2)
			return selected;

		int index = selected;
		int next = loop();
		int tries = 0;
		while((index == selected || index == next) && tries < 50) {
			index = rand.nextInt(size);
			++tries;
		}

		return index;
	}

}

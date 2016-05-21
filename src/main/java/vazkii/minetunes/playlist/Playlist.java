package vazkii.minetunes.playlist;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.minetunes.config.MTConfig;
import vazkii.minetunes.gui.GuiPlaylistManager;
import vazkii.minetunes.playlist.provider.IProviderStateCallback;
import vazkii.minetunes.playlist.provider.PlaylistProvider;
import vazkii.minetunes.playlist.provider.ProviderDirectory;
import vazkii.minetunes.playlist.provider.ProviderM3U;

public class Playlist {

	private static final String TAG_NAME = "name";
	private static final String TAG_FILE_PATH = "filePath";
	private static final String TAG_SONG_COUNT = "songCount";
	private static final String TAG_SONG_PREFIX = "song";
	
	public final List<MP3Metadata> metadataList;
	public final File file;
	
	public String storeName = "";
	
	Random rand = new Random();
	
	public Playlist(File file, Collection<MP3Metadata> metaSet) {
		this.file = file;
		this.metadataList = new ArrayList(metaSet);
	}

	public static Playlist build(File file, IProviderStateCallback callback) {
		PlaylistProvider provider = file.isDirectory() ? ProviderDirectory.instance : file.getName().endsWith(".m3u") ? ProviderM3U.instance : null;
		return provider == null ? null : provider.provide(file, callback);
	}
	
	public static Playlist build(NBTTagCompound cmp) {
		String name = cmp.getString(TAG_NAME);
		String filePath = cmp.getString(TAG_FILE_PATH);
		int count = cmp.getInteger(TAG_SONG_COUNT);
		
		Set<MP3Metadata> metadataSet = new TreeSet();
		for(int i = 0; i < count; i++) {
			NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_SONG_PREFIX + i);
			metadataSet.add(new MP3Metadata(i, cmp1));
		}
		
		Playlist playlist = new Playlist(new File(filePath), metadataSet);
		playlist.storeName = name;
		return playlist;
	}
	
	public void writeToNBT(NBTTagCompound cmp, String name) {
		cmp.setString(TAG_NAME, name);
		cmp.setString(TAG_FILE_PATH, file.getAbsolutePath());
		cmp.setInteger(TAG_SONG_COUNT, metadataList.size());
		
		int i = 0;
		for(MP3Metadata meta : metadataList) {
			NBTTagCompound cmp1 = new NBTTagCompound();
			meta.writeToNBT(cmp1);
			
			cmp.setTag(TAG_SONG_PREFIX + i, cmp1);
			i++;
		}
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

package vazkii.minetunes.playlist;

import java.io.File;

import vazkii.minetunes.MineTunes;
import vazkii.minetunes.gui.GuiDevTools;
import vazkii.minetunes.playlist.provider.IProviderStateCallback;

public class ThreadPlaylistCreator extends Thread implements IProviderStateCallback {

	private volatile String state;
	
	File file;
	String name;
	
	public ThreadPlaylistCreator(File file, String name) {
		this.file = file;
		this.name = name;
		
		setDaemon(true);
		setName("mineTunes Playlist Generator Thread");
		start();
	}
	
	@Override
	public void run() {
		while(PlaylistList.playlistNames.contains(name))
			name += "_";
		
		GuiDevTools.debugLog("Building Playlist with name " + name);
		
		Playlist playlist = Playlist.build(file, this);
		if(playlist != null && playlist.metadataList.size() > 0) {
			GuiDevTools.debugLog("Valid Playlist, adding to set");
			GuiDevTools.debugLog("MP3 Count: " + playlist.metadataList.size());
			
			PlaylistList.playlistNames.add(name);
			PlaylistList.playlists.put(name, playlist);
		} else GuiDevTools.debugLog("Not adding Playlist to set, null or empty.");
		
		PlaylistList.findCompoundAndWrite();
		
		MineTunes.playlistCreatorThread = null;
	}

	@Override
	public void setState(String state) {
		this.state = state;
		GuiDevTools.debugLog(state);
	}
	
	public synchronized String getProgressState() {
		return state;
	}
	
}

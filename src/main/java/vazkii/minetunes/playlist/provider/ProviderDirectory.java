package vazkii.minetunes.playlist.provider;

import java.io.File;
import java.io.IOException;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import vazkii.minetunes.MineTunes;
import vazkii.minetunes.gui.GuiDevTools;
import vazkii.minetunes.player.chooser.filter.MusicFilter;
import vazkii.minetunes.playlist.MP3Metadata;
import vazkii.minetunes.playlist.Playlist;

public class ProviderDirectory extends PlaylistProvider {

	public static final ProviderDirectory instance = new ProviderDirectory();

	@Override
	public Playlist provide(File file, IProviderStateCallback callback) {
		foundFiles = 0;
		processedFiles = 0;
		
		this.callback = callback;

		Playlist playlist = new Playlist();
		crawlDirectory(file, true, playlist);
		crawlDirectory(file, false, playlist);

		return playlist;
	}

	private void crawlDirectory(File dir, boolean scan, Playlist playlist) {
		GuiDevTools.debugLog("Crawling " + dir.getName());

		for(File file : dir.listFiles(MusicFilter.instance)) {
			if(file.isHidden())
				continue;
			
			if(file.isDirectory())
				crawlDirectory(file, scan, playlist);
			else {
				if(scan)
					foundFiles++;
				else try {
					playlist.metadataSet.add(new MP3Metadata(file));
					processedFiles++;
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				updateState();
			}
		}
	}

	@Override
	public String getDescription() {
		return "Directory-based Playlist Provider";
	}

}

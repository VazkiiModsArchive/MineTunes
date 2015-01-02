package vazkii.minetunes.playlist.provider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

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
		Set<MP3Metadata> metadataSet = new TreeSet();

		crawlDirectory(file, true, playlist, metadataSet);
		crawlDirectory(file, false, playlist, metadataSet);

		playlist.metadataList = new ArrayList(metadataSet);
		return playlist;
	}

	private void crawlDirectory(File dir, boolean scan, Playlist playlist, Set<MP3Metadata> metadataSet) {
		GuiDevTools.debugLog("Crawling " + dir.getName());

		for(File file : dir.listFiles(MusicFilter.instance)) {
			if(file.isHidden())
				continue;
			
			if(file.isDirectory())
				crawlDirectory(file, scan, playlist, metadataSet);
			else {
				if(scan)
					foundFiles++;
				else try {
					name = file.getName();
					metadataSet.add(new MP3Metadata(file));
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

package vazkii.minetunes.playlist;

import java.io.File;
import java.io.IOException;

import com.mpatric.mp3agic.ID3Wrapper;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class MP3Metadata implements Comparable<MP3Metadata> {

	public final File file;
	
	public final String artist;
	public final String title;
	public final String album;
	public final String genre;
	public final String filename;
	
	public final String length;
	public final int frameCount;
	
	public MP3Metadata(File file, Mp3File mp3) {
		this.file = file;
		filename = file.getName();
		
		ID3Wrapper id3 = new ID3Wrapper(mp3.getId3v1Tag(), mp3.getId3v2Tag());
		String artist = id3.getArtist();
		String title = id3.getTitle();
		String album = id3.getAlbum();
		String genre = id3.getGenreDescription();
		
		if(artist == null || title == null || artist.isEmpty() || title.isEmpty()) {
			this.artist = "";
			this.title = filename.replace("\\.mp3$", "");
		} else {
			this.artist = artist;
			this.title = title;
		}
		
		if(album == null || album.isEmpty())
			this.album = "(No Album)";
		else this.album = album;
		
		if(genre == null || genre.isEmpty())
			this.genre = "(Genre not Defined)";
		else this.genre = genre;
		
		long ms = mp3.getLengthInMilliseconds();
		int m =(int) (ms / (60 * 1000));
		int s = (int) ((ms / 1000) % 60);
		length = String.format("%d:%02d", m, s);
		
		frameCount = mp3.getFrameCount();
	}

	public MP3Metadata(File file) throws UnsupportedTagException, InvalidDataException, IOException {
		this(file, new Mp3File(file));
	}
	
	public boolean isEqualFile(MP3Metadata data) {
		return this == data || data.file == file || data.file.getAbsolutePath().equals(file.getAbsolutePath());
	}
	
	public String getFullName() {
		return artist + " - " + title;
	}
	
	public String getSortingKey() {
		return title + artist + album;
	}
	
	@Override
	public int compareTo(MP3Metadata o) {
		return getSortingKey().compareTo(o.getSortingKey());
	}
	
}

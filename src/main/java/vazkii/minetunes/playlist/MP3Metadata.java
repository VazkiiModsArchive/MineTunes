package vazkii.minetunes.playlist;

import java.io.File;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

import com.mpatric.mp3agic.ID3Wrapper;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class MP3Metadata implements Comparable<MP3Metadata> {

	private static final String TAG_ARTIST = "artist";
	private static final String TAG_TITLE = "title";
	private static final String TAG_ALBUM = "album";
	private static final String TAG_GENRE = "genre";
	private static final String TAG_FILENAME = "filename";
	private static final String TAG_LENGTH = "length";
	private static final String TAG_LENGTHMS = "lengthMs";
	private static final String TAG_FRAME_COUNT = "frameCount";
	
	private static final String TAG_FILE_PATH = "filepath";

	public final int index;
	
	public final File file;
	
	public final String artist;
	public final String title;
	public final String album;
	public final String genre;
	public final String filename;
	
	public final String length;
	public final long lengthMs;
	public final int frameCount;
	
	public MP3Metadata(int index, File file, Mp3File mp3) {
		this.index = index;
		this.file = file;
		filename = file.getName();
		
		ID3Wrapper id3 = new ID3Wrapper(mp3.getId3v1Tag(), mp3.getId3v2Tag());
		String artist = id3.getArtist();
		String title = id3.getTitle();
		String album = id3.getAlbum();
		String genre = id3.getGenreDescription();
		
		if(artist == null || title == null || artist.isEmpty() || title.isEmpty()) {
			this.artist = "";
			this.title = filename.replace(".mp3", "");
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
		
		lengthMs = mp3.getLengthInMilliseconds();
		length = getLengthStr(lengthMs); 
		frameCount = mp3.getFrameCount();
	}

	public MP3Metadata(int index, File file) throws UnsupportedTagException, InvalidDataException, IOException {
		this(index, file, new Mp3File(file));
	}
	
	public MP3Metadata(int index, NBTTagCompound cmp) {
		this.index = index;
		artist = cmp.getString(TAG_ARTIST);
		title = cmp.getString(TAG_TITLE);
		album = cmp.getString(TAG_ALBUM);
		genre = cmp.getString(TAG_GENRE);
		filename = cmp.getString(TAG_FILENAME);
		length = cmp.getString(TAG_LENGTH);
		lengthMs = cmp.getLong(TAG_LENGTHMS);
		frameCount = cmp.getInteger(TAG_FRAME_COUNT);
		file = new File(cmp.getString(TAG_FILE_PATH));
	}
	
	public void writeToNBT(NBTTagCompound cmp) {
		cmp.setString(TAG_ARTIST, artist);
		cmp.setString(TAG_TITLE, title);
		cmp.setString(TAG_ALBUM, album);
		cmp.setString(TAG_GENRE, genre);
		cmp.setString(TAG_FILENAME, filename);
		cmp.setString(TAG_LENGTH, length);
		cmp.setLong(TAG_LENGTHMS, lengthMs);
		cmp.setInteger(TAG_FRAME_COUNT, frameCount);
		cmp.setString(TAG_FILE_PATH, file.getAbsolutePath());
	}
	
	public static String getLengthStr(long ms) {
		int m = (int) (ms / (60 * 1000));
		int s = (int) ((ms / 1000) % 60);
		return String.format("%d:%02d", m, s);
	}
	
	public boolean isEqualFile(MP3Metadata data) {
		return this == data || data.file == file || data.file.getAbsolutePath().equals(file.getAbsolutePath());
	}
	
	public String getFullName() {
		return artist.trim() + " - " + title.trim();
	}
	
	public String getSortingKey() {
		return (title.trim() + artist.trim() + album.trim()).toLowerCase();
	}
	
	@Override
	public int compareTo(MP3Metadata o) {
		return getSortingKey().compareTo(o.getSortingKey());
	}
	
}

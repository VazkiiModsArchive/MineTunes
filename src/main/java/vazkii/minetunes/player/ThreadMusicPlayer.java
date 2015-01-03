package vazkii.minetunes.player;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import vazkii.minetunes.gui.GuiDevTools;
import vazkii.minetunes.gui.GuiPlaylistManager;
import vazkii.minetunes.playlist.MP3Metadata;
import vazkii.minetunes.playlist.Playlist;

public class ThreadMusicPlayer extends Thread {

	public static final float MIN_GAIN = -20F;
	public static final float MAX_GAIN = 0F;
	public static final float MED_GAIN = (MAX_GAIN - MIN_GAIN) / 2 * (Math.abs(MIN_GAIN) / MIN_GAIN); 
	public volatile static float gain = MED_GAIN;
	
	EventListener listener = new EventListener();
	AdvancedPlayer player;

	volatile MP3Metadata playingMP3;
	volatile boolean queued = false;

	volatile boolean kill = false;
	volatile boolean playing = false;
	volatile boolean paused = false; 
	volatile int pauseFrames = 0;

	volatile List<MP3Metadata> lastSongs = new ArrayList();
	
	public ThreadMusicPlayer() {
		setDaemon(true);
		setName("mineTunes Player Thread");
		start();
	}

	@Override
	public void run() {
		try {
			GuiDevTools.debugLog("Starting " + this);
			while(!kill) {
				if(queued && playingMP3 != null) {
					GuiDevTools.debugLog("Queued: " + playingMP3.file.getAbsolutePath());
					if(player != null)
						resetPlayer();
					player = new AdvancedPlayer(new FileInputStream(playingMP3.file));
					player.setPlayBackListener(listener);
					queued = false;
					GuiDevTools.debugLog("Player (Re)loaded");
				}

				boolean played = false;
				if(player != null && player.getAudioDevice() != null) {
					if(pauseFrames == 0) {
						GuiDevTools.debugLog("Playing File");
						player.play();
						playing = true;
					} else if(!paused) {
						GuiDevTools.debugLog("Was paused, restarting at " + pauseFrames);
						int startFrame = pauseFrames;
						pauseFrames = 0;
						player.play(startFrame, Integer.MAX_VALUE);
						playing = true;
					}
					played = true;
				}

				if(played && !paused && !queued) {
					GuiDevTools.debugLog("Song done, next...");
					next();
				}
			}
		} catch(Exception e) {
			GuiDevTools.logThrowable(e);
		}
	}

	public void next() {
		next(true);
	}
	
	public void next(boolean add) {
		Playlist playlist = GuiPlaylistManager.getCurrentPlaylist();
		MP3Metadata mp3 = playlist == null ? null : playlist.nextSong();
		if(mp3 != null)
			play(mp3, add);
		else {
			playingMP3 = null;
			resetPlayer();
		}
	}
	
	public void prev() {
		if(lastSongs.size() < 2)
			return;
		
		removeFromLastPlayed(lastSongs.get(lastSongs.size() - 1));
		MP3Metadata meta = lastSongs.get(lastSongs.size() - 1);
		play(meta, false);
	}
	
	public void resetPlayer() {
		GuiDevTools.debugLog("Resetting Player.");

		playing = false;
		if(player != null)
			player.close();

		player = null;
	}

	public void play(MP3Metadata metadata) {
		play(metadata, true);
	}
	
	public void play(MP3Metadata metadata, boolean add) {
		resetPlayer();
		
		if(add)
			addToLastPlayed(metadata);
		
		playingMP3 = metadata;
		paused = false;
		queued = true;
	}
	
	private void addToLastPlayed(MP3Metadata meta) {
		lastSongs.add(meta);
		
		while(lastSongs.size() > 10)
			lastSongs.remove(0);
	}

	private void removeFromLastPlayed(MP3Metadata meta) {
		lastSongs.remove(meta);
	}
	
	public void pauseOrPlay() {
		if(player != null)
			if(!paused) {
				GuiDevTools.debugLog("Pausing...");
				paused = true;
				if(player.getAudioDevice() != null)
					player.stop();
			} else {
				GuiDevTools.debugLog("Unpaused...");
				queued = true;
				paused = false;
			}
	}
	
	public void onPlaylistChange() {
		lastSongs.clear();
	}
	
	public float getGain() {
		if(player == null)
			return gain;
		
		AudioDevice device = player.getAudioDevice();
		if(device != null && device instanceof JavaSoundAudioDevice)
			return ((JavaSoundAudioDevice) device).getGain();
		return gain;
	}
	
	public void addGain(float gain) {
		setGain(getGain() + gain);
	}
	
	public void setGain(float gain) {
		if(player == null)
			return;
		
		this.gain = Math.min(MAX_GAIN, Math.max(MIN_GAIN, gain));
		AudioDevice device = player.getAudioDevice();
		if(device != null && device instanceof JavaSoundAudioDevice)
			((JavaSoundAudioDevice) device).setGain(this.gain);
	}
	
	public float getRelativeVolume() {
		return getRelativeVolume(getGain());
	}
	
	public float getRelativeVolume(float gain) {
		float width = MAX_GAIN - MIN_GAIN;
		float rel = Math.abs(gain - MIN_GAIN);
		return rel / Math.abs(width);
	}
	
	public MP3Metadata getPlayingMetadata() {
		return playingMP3;
	}

	public int getFramesPlayed() {
		return player == null ? 0 : player.getFrames();
	}
	
	public float getFractionPlayed() {
		int pFrames = getFramesPlayed();
		int tFrames = playingMP3 == null ? 0 : playingMP3.frameCount;
		if(pFrames == 0 || tFrames == 0)
			return 0F;
		
		return (float) pFrames / (float) tFrames;
	}
	
	public boolean isPaused() {
		return paused && playingMP3 != null;
	}
	
	public void forceKill() {
		try {
			resetPlayer();
			interrupt();

			finalize();
			kill = true;
		} catch(Throwable e) {
			GuiDevTools.logThrowable(e);
		}
	}

	class EventListener extends PlaybackListener {

		@Override
		public void playbackFinished(PlaybackEvent evt) {
			if(paused) {
				pauseFrames = evt.getSource().getFrames();
				GuiDevTools.debugLog("Paused at " + pauseFrames);
			}
		}

	}
}

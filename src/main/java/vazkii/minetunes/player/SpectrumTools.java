package vazkii.minetunes.player;

import javazoom.jl.decoder.Decoder;
import kj.dsp.KJFFT;
import vazkii.minetunes.MineTunes;

public final class SpectrumTools {

	public static final float DECAY = 0.03F;
	public static final int FFT_SAMPLE_SIZE = 32;
	public static final int BANDS = FFT_SAMPLE_SIZE >> 1;

	public static final KJFFT fft = new KJFFT(FFT_SAMPLE_SIZE);
	
	private static float[] mergeStereoSamples(float[] sampleLeft, float[] sampleRight) {
		float[] buffer = new float[sampleLeft.length];
		
		for(int i = 0; i < sampleLeft.length; i++)
			buffer[i] = (sampleLeft[i] + sampleRight[i]) * 0.5F;
		
		return buffer;
	}
	
	public static float[] getFFTCalculation(float[] sampleLeft, float[] sampleRight) {
		return getFFTCalculation(mergeStereoSamples(sampleLeft, sampleRight));
	}
	
	public static float[] getFFTCalculation(float[] sample) {
		return fft.calculate(sample);
	}
	
	public static float[] getFFTCalculation() {
		if(MineTunes.musicPlayerThread == null || MineTunes.musicPlayerThread.player == null)
			throw new RuntimeException("No player!");
		
		Decoder decoder = MineTunes.musicPlayerThread.player.getDecoder();
		float[] sampleLeft = null;
		float[] sampleRight = null;
		
		sampleLeft = decoder.getSamples(0);
		if(sampleLeft == null)
			return null;
		
		if(decoder.getOutputChannels() == 2)
			sampleRight = decoder.getSamples(1);
		
		return sampleRight != null ? getFFTCalculation(sampleLeft, sampleRight) : getFFTCalculation(sampleLeft);
	}
	
}

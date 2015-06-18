/*
 * Copyright (c) 2012, Finn Kuusisto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *     
 *     Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package kuusisto.tinysound.internal;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

/**
 * The MemMusic class is an implementation of the Music interface that stores
 * all audio data in memory for low latency.
 * 
 * @author Finn Kuusisto
 */
public class MemMusic implements Music {
	
	private byte[] left;
	private byte[] right;
	private Mixer mixer;
	private MusicReference reference;
	
	/**
	 * Construct a new MemMusic with the given music data and the Mixer with
	 * which to register this MemMusic.
	 * @param left left channel of music data
	 * @param right right channel of music data
	 * @param mixer Mixer with which this Music is registered
	 */
	public MemMusic(byte[] left, byte[] right, Mixer mixer) {
		this.left = left;
		this.right = right;
		this.mixer = mixer;
		this.reference = new MemMusicReference(this.left, this.right, false,
				false, 0, 0, 1.0, 0.0);
		this.mixer.registerMusicReference(this.reference);
	}
	
	/**
	 * Play this MemMusic and loop if specified.
	 * @param loop if this MemMusic should loop
	 */
	@Override
	public void play(boolean loop) {
		this.reference.setLoop(loop);
		this.reference.setPlaying(true);
	}
	
	/**
	 * Play this MemMusic at the specified volume and loop if specified.
	 * @param loop if this MemMusic should loop
	 * @param volume the volume to play the this Music
	 */
	@Override
	public void play(boolean loop, double volume) {
		this.setLoop(loop);
		this.setVolume(volume);
		this.reference.setPlaying(true);
	}
	
	/**
	 * Play this MemMusic at the specified volume and pan, and loop if specified
	 * .
	 * @param loop if this MemMusic should loop
	 * @param volume the volume to play the this MemMusic
	 * @param pan the pan at which to play this MemMusic [-1.0,1.0], values
	 * outside the valid range will be ignored
	 */
	@Override
	public void play(boolean loop, double volume, double pan) {
		this.setLoop(loop);
		this.setVolume(volume);
		this.setPan(pan);
		this.reference.setPlaying(true);
	}
	
	/**
	 * Stop playing this MemMusic and set its position to the beginning.
	 */
	@Override
	public void stop() {
		this.reference.setPlaying(false);
		this.rewind();
	}
	
	/**
	 * Stop playing this MemMusic and keep its current position.
	 */
	@Override
	public void pause() {
		this.reference.setPlaying(false);
	}
	
	/**
	 * Play this MemMusic from its current position.
	 */
	@Override
	public void resume() {
		this.reference.setPlaying(true);
	}
	
	/**
	 * Set this MemMusic's position to the beginning.
	 */
	@Override
	public void rewind() {
		this.reference.setPosition(0);
	}
	
	/**
	 * Set this MemMusic's position to the loop position.
	 */
	@Override
	public void rewindToLoopPosition() {
		long byteIndex = this.reference.getLoopPosition();
		this.reference.setPosition(byteIndex);
	}
	
	/**
	 * Determine if this MemMusic is playing.
	 * @return true if this MemMusic is playing
	 */
	@Override
	public boolean playing() {
		return this.reference.getPlaying();
	}
	
	/**
	 * Determine if this MemMusic has reached its end and is done playing.
	 * @return true if this MemMusic has reached the end and is done playing
	 */
	@Override
	public boolean done() {
		return this.reference.done();
	}
	
	/**
	 * Determine if this MemMusic will loop.
	 * @return true if this MemMusic will loop
	 */
	@Override
	public boolean loop() {
		return this.reference.getLoop();
	}
	
	/**
	 * Set whether this MemMusic will loop.
	 * @param loop whether this MemMusic will loop
	 */
	@Override
	public void setLoop(boolean loop) {
		this.reference.setLoop(loop);
	}
	
	/**
	 * Get the loop position of this MemMusic by sample frame.
	 * @return loop position by sample frame
	 */
	@Override
	public int getLoopPositionByFrame() {
		int bytesPerChannelForFrame = TinySound.FORMAT.getFrameSize() /
			TinySound.FORMAT.getChannels();
		long byteIndex = this.reference.getLoopPosition();
		return (int)(byteIndex / bytesPerChannelForFrame);
	}
	
	/**
	 * Get the loop position of this MemMusic by seconds.
	 * @return loop position by seconds
	 */
	@Override
	public double getLoopPositionBySeconds() {
		int bytesPerChannelForFrame = TinySound.FORMAT.getFrameSize() /
			TinySound.FORMAT.getChannels();
		long byteIndex = this.reference.getLoopPosition();
		return (byteIndex / (TinySound.FORMAT.getFrameRate() *
				bytesPerChannelForFrame));
	}
	
	/**
	 * Set the loop position of this MemMusic by sample frame.
	 * @param frameIndex sample frame loop position to set
	 */
	@Override
	public void setLoopPositionByFrame(int frameIndex) {
		//get the byte index for a channel
		int bytesPerChannelForFrame = TinySound.FORMAT.getFrameSize() /
			TinySound.FORMAT.getChannels();
		long byteIndex = (long)(frameIndex * bytesPerChannelForFrame);
		this.reference.setLoopPosition(byteIndex);
	}
	
	/**
	 * Set the loop position of this MemMusic by seconds.
	 * @param seconds loop position to set by seconds
	 */
	@Override
	public void setLoopPositionBySeconds(double seconds) {
		//get the byte index for a channel
		int bytesPerChannelForFrame = TinySound.FORMAT.getFrameSize() /
			TinySound.FORMAT.getChannels();
		long byteIndex = (long)(seconds * TinySound.FORMAT.getFrameRate()) *
			bytesPerChannelForFrame;
		this.reference.setLoopPosition(byteIndex);
	}
	
	/**
	 * Get the volume of this MemMusic.
	 * @return volume of this MemMusic
	 */
	@Override
	public double getVolume() {
		return this.reference.getVolume();
	}
	
	/**
	 * Set the volume of this MemMusic.
	 * @param volume the desired volume of this MemMusic
	 */
	@Override
	public void setVolume(double volume) {
		if (volume >= 0.0) {
			this.reference.setVolume(volume);
		}
	}

	/**
	 * Get the pan of this MemMusic.
	 * @return pan of this MemMusic
	 */
	@Override
	public double getPan() {
		return this.reference.getPan();
	}

	/**
	 * Set the pan of this MemMusic.  Must be between -1.0 (full pan left) and
	 * 1.0 (full pan right).  Values outside the valid range will be ignored.
	 * @param pan the desired pan of this MemMusic
	 */
	@Override
	public void setPan(double pan) {
		if (pan >= -1.0 && pan <= 1.0) {
			this.reference.setPan(pan);
		}
	}
	
	/**
	 * Unload this MemMusic from the system.  Attempts to use this MemMusic
	 * after unloading will result in error.
	 */
	@Override
	public void unload() {
		//unregister the reference
		this.mixer.unRegisterMusicReference(this.reference);
		this.reference.dispose();
		this.mixer = null;
		this.left = null;
		this.right = null;
		this.reference = null;
	}
	
	/////////////
	//Reference//
	/////////////
	
	/**
	 * The MemMusicReference is an implementation of the MusicReference
	 * interface.
	 * 
	 * @author Finn Kuusisto
	 */
	private static class MemMusicReference implements MusicReference {

		private byte[] left;
		private byte[] right;
		private boolean playing;
		private boolean loop;
		private int loopPosition;
		private int position;
		private double volume;
		private double pan;
		
		/**
		 * Construct a new MemMusicReference with the given audio data and
		 * settings.
		 * @param left left channel of music data
		 * @param right right channel of music data
		 * @param playing true if the music should be playing
		 * @param loop true if the music should loop
		 * @param loopPosition byte index of the loop position in music data
		 * @param position byte index position in music data
		 * @param volume volume to play the music
		 * @param pan pan to play the music
		 */
		public MemMusicReference(byte[] left, byte[] right, boolean playing,
				boolean loop, int loopPosition, int position, double volume,
				double pan) {
			this.left = left;
			this.right = right;
			this.playing = playing;
			this.loop = loop;
			this.loopPosition = loopPosition;
			this.position = position;
			this.volume = volume;
			this.pan = pan;
		}
		
		/**
		 * Get the playing setting of this MemMusicReference.
		 * @return true if this MemMusicReference is set to play
		 */
		@Override
		public synchronized boolean getPlaying() {
			return this.playing;
		}
		
		/**
		 * Get the loop setting of this MemMusicReference.
		 * @return true if this MemMusicReference is set to loop
		 */
		@Override
		public synchronized boolean getLoop() {
			return this.loop;
		}
		
		/**
		 * Get the byte index of this MemMusicReference.
		 * @return byte index of this MemMusicReference
		 */
		@Override
		public synchronized long getPosition() {
			return this.position;
		}
		
		/**
		 * Get the loop-position byte index of this MemMusicReference.
		 * @return loop-position byte index of this MemMusicReference
		 */
		@Override
		public synchronized long getLoopPosition() {
			return this.loopPosition;
		}
		
		/**
		 * Get the volume of this MemMusicReference.
		 * @return volume of this MemMusicReference
		 */
		@Override
		public synchronized double getVolume() {
			return this.volume;
		}

		/**
		 * Get the pan of this MemMusicReference.
		 * @return pan of this MemMusicReference
		 */
		@Override
		public synchronized double getPan() {
			return this.pan;
		}
		
		/**
		 * Set whether this MemMusicReference is playing.
		 * @param playing whether this MemMusicReference is playing
		 */
		@Override
		public synchronized void setPlaying(boolean playing) {
			this.playing = playing;
		}
		
		/**
		 * Set whether this MemMusicReference will loop.
		 * @param loop whether this MemMusicReference will loop
		 */
		@Override
		public synchronized void setLoop(boolean loop) {
			this.loop = loop;
		}
		
		/**
		 * Set the byte index of this MemMusicReference.
		 * @param position the byte index to set
		 */
		@Override
		public synchronized void setPosition(long position) {
			if (position >= 0 && position < this.left.length) {
				this.position = (int)position;
			}
		}
		
		/**
		 * Set the loop-position byte index of this MemMusicReference.
		 * @param loopPosition the loop-position byte index to set
		 */
		@Override
		public synchronized void setLoopPosition(long loopPosition) {
			if (loopPosition >= 0 && loopPosition < this.left.length) {
				this.loopPosition = (int)loopPosition;
			}
		}
		
		/**
		 * Set the volume of this MemMusicReference.
		 * @param volume the desired volume of this MemMusicReference
		 */
		@Override
		public synchronized void setVolume(double volume) {
			this.volume = volume;
		}

		/**
		 * Set the pan of this MemMusicReference.  Must be between -1.0 (full
		 * pan left) and 1.0 (full pan right).
		 * @param pan the desired pan of this MemMusicReference
		 */
		@Override
		public synchronized void setPan(double pan) {
			this.pan = pan;
		}
		
		/**
		 * Get the number of bytes remaining for each channel until the end of
		 * this MemMusicReference.
		 * @return number of bytes remaining for each channel
		 */
		@Override
		public synchronized long bytesAvailable() {
			return this.left.length - this.position;
		}
		
		/**
		 * Determine if there are no bytes remaining and play has stopped.
		 * @return true if there are no bytes remaining and the reference is no
		 * longer playing
		 */
		@Override
		public synchronized boolean done() {
			long available = this.left.length - this.position;
			return available <= 0 && !this.playing;
		}
		
		/**
		 * Skip a specified number of bytes of the audio data.
		 * @param num number of bytes to skip
		 */
		@Override
		public synchronized void skipBytes(long num) {
			for (int i = 0; i < num; i++) {
				this.position++;
				//wrap if looping, stop otherwise
				if (this.position >= this.left.length) {
					if (this.loop) {
						this.position = this.loopPosition;
					}
					else {
						this.playing = false;
					}
				}
			}
		}
		
		/**
		 * Get the next two bytes from the music data in the specified
		 * endianness.
		 * @param data length-2 array to write in next two bytes from each
		 * channel
		 * @param bigEndian true if the bytes should be read big-endian
		 */
		@Override
		public synchronized void nextTwoBytes(int[] data, boolean bigEndian) {
			if (bigEndian) {
				//left
				data[0] = ((this.left[this.position] << 8) |
						(this.left[this.position + 1] & 0xFF));
				//right
				data[1] = ((this.right[this.position] << 8) |
						(this.right[this.position + 1] & 0xFF));
			}
			else {
				//left
				data[0] = ((this.left[this.position + 1] << 8) |
						(this.left[this.position] & 0xFF));
				//right
				data[1] = ((this.right[this.position + 1] << 8) |
						(this.right[this.position] & 0xFF));
			}
			this.position += 2;
			//wrap if looping, stop otherwise
			if (this.position >= this.left.length) {
				if (this.loop) {
					this.position = this.loopPosition;
				}
				else {
					this.playing = false;
				}
			}
		}

		/**
		 * Does any cleanup necessary to dispose of resources in use by this
		 * MemMusicReference.
		 */
		@Override
		public synchronized void dispose() {
			this.playing = false;
			this.position = this.left.length + 1;
			this.left = null;
			this.right = null;
		}
		
	}

}

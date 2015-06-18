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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

/**
 * The StreamMusic class is an implementation of the Music interface that
 * streams audio data from a temporary file to reduce memory overhead.
 * 
 * @author Finn Kuusisto
 */
public class StreamMusic implements Music {
	
	private URL dataURL;
	private Mixer mixer;
	private MusicReference reference;
	
	/**
	 * Construct a new StreamMusic with the given data and the Mixer with which
	 * to register this StreamMusic.
	 * @param dataURL URL of the temporary file containing audio data
	 * @param numBytesPerChannel the total number of bytes for each channel in
	 * the file
	 * @param mixer Mixer that will handle this StreamSound
	 * @throws IOException if a stream cannot be opened from the URL
	 */
	public StreamMusic(URL dataURL, long numBytesPerChannel, Mixer mixer)
			throws IOException {
		this.dataURL = dataURL;
		this.mixer = mixer;
		this.reference = new StreamMusicReference(this.dataURL, false, false, 0,
				0, numBytesPerChannel, 1.0, 0.0);
		this.mixer.registerMusicReference(this.reference);
	}

	/**
	 * Play this StreamMusic and loop if specified.
	 * @param loop if this StreamMusic should loop
	 */
	@Override
	public void play(boolean loop) {
		this.reference.setLoop(loop);
		this.reference.setPlaying(true);
	}
	
	/**
	 * Play this StreamMusic at the specified volume and loop if specified.
	 * @param loop if this StreamMusic should loop
	 * @param volume the volume to play the this StreamMusic
	 */
	@Override
	public void play(boolean loop, double volume) {
		this.setLoop(loop);
		this.setVolume(volume);
		this.reference.setPlaying(true);
	}
	
	/**
	 * Play this StreamMusic at the specified volume and pan, and loop if
	 * specified.
	 * @param loop if this StreamMusic should loop
	 * @param volume the volume to play the this StreamMusic
	 * @param pan the pan at which to play this StreamMusic [-1.0,1.0], values
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
	 * Stop playing this StreamMusic and set its position to the beginning.
	 */
	@Override
	public void stop() {
		this.reference.setPlaying(false);
		this.rewind();
	}
	
	/**
	 * Stop playing this StreamMusic and keep its current position.
	 */
	@Override
	public void pause() {
		this.reference.setPlaying(false);
	}
	
	/**
	 * Play this StreamMusic from its current position.
	 */
	@Override
	public void resume() {
		this.reference.setPlaying(true);
	}
	
	/**
	 * Set this StreamMusic's position to the beginning.
	 */
	@Override
	public void rewind() {
		this.reference.setPosition(0);
	}
	
	/**
	 * Set this StreamMusic's position to the loop position.
	 */
	@Override
	public void rewindToLoopPosition() {
		long byteIndex = this.reference.getLoopPosition();
		this.reference.setPosition(byteIndex);
	}
	
	/**
	 * Determine if this StreamMusic is playing.
	 * @return true if this StreamMusic is playing
	 */
	@Override
	public boolean playing() {
		return this.reference.getPlaying();
	}
	
	/**
	 * Determine if this StreamMusic has reached its end and is done playing.
	 * @return true if this StreamMusic has reached the end and is done playing
	 */
	@Override
	public boolean done() {
		return this.reference.done();
	}
	
	/**
	 * Determine if this StreamMusic will loop.
	 * @return true if this StreamMusic will loop
	 */
	@Override
	public boolean loop() {
		return this.reference.getLoop();
	}
	
	/**
	 * Set whether this StreamMusic will loop.
	 * @param loop whether this StreamMusic will loop
	 */
	@Override
	public void setLoop(boolean loop) {
		this.reference.setLoop(loop);
	}

	/**
	 * Get the loop position of this StreamMusic by sample frame.
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
	 * Get the loop position of this StreamMusic by seconds.
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
	 * Set the loop position of this StreamMusic by sample frame.
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
	 * Set the loop position of this StreamMusic by seconds.
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
	 * Get the volume of this StreamMusic.
	 * @return volume of this StreamMusic
	 */
	@Override
	public double getVolume() {
		return this.reference.getVolume();
	}
	
	/**
	 * Set the volume of this StreamMusic.
	 * @param volume the desired volume of this StreamMusic
	 */
	@Override
	public void setVolume(double volume) {
		if (volume >= 0.0) {
			this.reference.setVolume(volume);
		}
	}

	/**
	 * Get the pan of this StreamMusic.
	 * @return pan of this StreamMusic
	 */
	@Override
	public double getPan() {
		return this.reference.getPan();
	}

	/**
	 * Set the pan of this StreamMusic.  Must be between -1.0 (full pan left)
	 * and 1.0 (full pan right).  Values outside the valid range will be ignored
	 * .
	 * @param pan the desired pan of this StreamMusic
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
		this.dataURL = null;
		this.reference = null;
	}
	
	/////////////
	//Reference//
	/////////////

	/**
	 * The StreamMusicReference is an implementation of the MusicReference
	 * interface.
	 */
	private static class StreamMusicReference implements MusicReference {
		
		private URL url;
		private InputStream data;
		private long numBytesPerChannel; //not per frame, but the whole sound
		private byte[] buf;
		private byte[] skipBuf;
		private boolean playing;
		private boolean loop;
		private long loopPosition;
		private long position;
		private double volume;
		private double pan;
		
		/**
		 * Constructs a new StreamMusicReference with the given audio data and
		 * settings.
		 * @param dataURL URL of the temporary file containing audio data
		 * @param playing true if the music should be playing
		 * @param loop true if the music should loop
		 * @param loopPosition byte index of the loop position in music data
		 * @param position byte index position in music data
		 * @param numBytesPerChannel the total number of bytes for each channel
		 * in the file
		 * @param volume volume to play the music
		 * @param pan pan to play the music
		 * @throws IOException if a stream cannot be opened from the URL
		 */
		public StreamMusicReference(URL dataURL, boolean playing, boolean loop,
				long loopPosition, long position, long numBytesPerChannel,
				double volume, double pan) throws IOException {
			this.url = dataURL;
			this.playing = playing;
			this.loop = loop;
			this.loopPosition = loopPosition;
			this.position = position;
			this.numBytesPerChannel = numBytesPerChannel;
			this.volume = volume;
			this.pan = pan;
			this.buf = new byte[4];
			this.skipBuf = new byte[50];
			//now get the data stream
			this.data = this.url.openStream();
		}

		/**
		 * Get the playing setting of this StreamMusicReference.
		 * @return true if this StreamMusicReference is set to play
		 */
		@Override
		public synchronized boolean getPlaying() {
			return this.playing;
		}

		/**
		 * Get the loop setting of this StreamMusicReference.
		 * @return true if this StreamMusicReference is set to loop
		 */
		@Override
		public synchronized boolean getLoop() {
			return this.loop;
		}
		
		/**
		 * Get the byte index of this StreamMusicReference.
		 * @return byte index of this StreamMusicReference
		 */
		@Override
		public synchronized long getPosition() {
			return this.position;
		}
		
		/**
		 * Get the loop-position byte index of this StreamMusicReference.
		 * @return loop-position byte index of this StreamMusicReference
		 */
		@Override
		public synchronized long getLoopPosition() {
			return this.loopPosition;
		}

		/**
		 * Get the volume of this StreamMusicReference.
		 * @return volume of this StreamMusicReference
		 */
		@Override
		public synchronized double getVolume() {
			return this.volume;
		}

		/**
		 * Get the pan of this StreamMusicReference.
		 * @return pan of this StreamMusicReference
		 */
		@Override
		public synchronized double getPan() {
			return this.pan;
		}

		/**
		 * Set whether this StreamMusicReference is playing.
		 * @param playing whether this StreamMusicReference is playing
		 */
		@Override
		public synchronized void setPlaying(boolean playing) {
			this.playing = playing;
		}

		/**
		 * Set whether this StreamMusicReference will loop.
		 * @param loop whether this StreamMusicReference will loop
		 */
		@Override
		public synchronized void setLoop(boolean loop) {
			this.loop = loop;
		}

		/**
		 * Set the byte index of this StreamMusicReference.
		 * @param position the byte index to set
		 */
		@Override
		public synchronized void setPosition(long position) {
			if (position >= 0 && position < this.numBytesPerChannel) {
				//if it's later, skip
				if (position >= this.position) {
					this.skipBytes(position - this.position);
				}
				else { //otherwise skip from the beginning
					//first close our current stream
					try {
						this.data.close();
					} catch (IOException e) {
						//whatever...
					}
					//open a new stream
					try {
						this.data = this.url.openStream();
						this.position = 0;
						this.skipBytes(position);
					} catch (IOException e) {
						System.err.println("Failed to open stream for StreamMusic");
						this.playing = false;
					}
				}
			}
		}

		/**
		 * Set the loop-position byte index of this StreamMusicReference.
		 * @param loopPosition the loop-position byte index to set
		 */
		@Override
		public synchronized void setLoopPosition(long loopPosition) {
			if (loopPosition >= 0 && loopPosition < this.numBytesPerChannel) {
				this.loopPosition = loopPosition;
			}
		}

		/**
		 * Set the volume of this StreamMusicReference.
		 * @param volume the desired volume of this StreamMusicReference
		 */
		@Override
		public synchronized void setVolume(double volume) {
			this.volume = volume;
		}

		/**
		 * Set the pan of this StreamMusicReference.  Must be between -1.0 (full
		 * pan left) and 1.0 (full pan right).
		 * @param pan the desired pan of this StreamMusicReference
		 */
		@Override
		public synchronized void setPan(double pan) {
			this.pan = pan;
		}

		/**
		 * Get the number of bytes remaining for each channel until the end of
		 * this StreamMusicReference.
		 * @return number of bytes remaining for each channel
		 */
		@Override
		public synchronized long bytesAvailable() {
			return this.numBytesPerChannel - this.position;
		}
		
		/**
		 * Determine if there are no bytes remaining and play has stopped.
		 * @return true if there are no bytes remaining and the reference is no
		 * longer playing
		 */
		@Override
		public synchronized boolean done() {
			long available = this.numBytesPerChannel - this.position;
			return available <= 0 && !this.playing;
		}

		/**
		 * Skip a specified number of bytes of the audio data.
		 * @param num number of bytes to skip
		 */
		@Override
		public synchronized void skipBytes(long num) {
			//couple of shortcuts if we are going to complete the stream
			if ((this.position + num) >= this.numBytesPerChannel) {
				//if we're not looping, nothing special needs to happen
				if (!this.loop) {
					this.position += num;
					//now stop since we're out
					this.playing = false;
					return;
				}
				else {
					//compute the next position
					long loopLength = this.numBytesPerChannel -
						this.loopPosition;
					long bytesOver = (this.position + num) -
						this.numBytesPerChannel;
					long nextPosition = this.loopPosition +
						(bytesOver % loopLength);
					//and set us there
					this.setPosition(nextPosition);
					return;
				}
			}
			//this is the number of bytes to skip per channel, so double it
			long numSkip = num * 2;
			//spin read since skip is not always supported apparently and won't
			//guarantee a correct skip amount
			int tmpRead = 0;
			int numRead = 0;
			try {
				while (numRead < numSkip && tmpRead != -1) {
					//determine safe length to read
					long remaining = numSkip - numRead;
					int len = remaining > this.skipBuf.length ?
							this.skipBuf.length : (int)remaining;
					//and read
					tmpRead = this.data.read(this.skipBuf, 0, len);
					numRead += tmpRead;
				}
			} catch (IOException e) {
				//hmm... I guess invalidate this reference
				this.position = this.numBytesPerChannel;
				this.playing = false;
			}
			//increment the position appropriately
			if (tmpRead == -1) { //reached end of file in the middle of reading
				this.position = this.numBytesPerChannel;
				this.playing = false;
			}
			else {
				this.position += num;
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
			//try to read audio data
			int tmpRead = 0;
			int numRead = 0;
			try {
				while (numRead < this.buf.length && tmpRead != -1) {
					tmpRead = this.data.read(this.buf, numRead,
							this.buf.length - numRead);
					numRead += tmpRead;
				}
			} catch (IOException e) {
				//this shouldn't happen if the bytes were written correctly to
				//the temp file, but this sound should now be invalid at least
				this.position = this.numBytesPerChannel;
				System.err.println("Failed reading bytes for stream sound");
			}
			//copy the values into the caller buffer
			if (bigEndian) {
				//left
				data[0] = ((this.buf[0] << 8) |
						(this.buf[1] & 0xFF));
				//right
				data[1] = ((this.buf[2] << 8) |
						(this.buf[3] & 0xFF));
			}
			else {
				//left
				data[0] = ((this.buf[1] << 8) |
						(this.buf[0] & 0xFF));
				//right
				data[1] = ((this.buf[3] << 8) |
						(this.buf[2] & 0xFF));
			}
			//increment the position appropriately
			if (tmpRead == -1) { //reached end of file in the middle of reading
				//this should never happen
				this.position = this.numBytesPerChannel;
			}
			else {
				this.position += 2;
			}
			//wrap if looping, stop otherwise
			if (this.position >= this.numBytesPerChannel) {
				if (this.loop) {
					this.setPosition(this.loopPosition);
				}
				else {
					this.playing = false;
				}
			}
		}

		/**
		 * Does any cleanup necessary to dispose of resources in use by this
		 * StreamMusicReference.
		 */
		@Override
		public synchronized void dispose() {
			this.playing = false;
			this.position = this.numBytesPerChannel;
			this.url = null;
			try {
				this.data.close();
			} catch (IOException e) {
				//whatever... this should never happen
			}
		}
	}
}
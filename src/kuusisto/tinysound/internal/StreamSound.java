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

import kuusisto.tinysound.Sound;

/**
 * Thes StreamSound class is an implementation of the Sound interface that
 * streams audio data from a temporary file to reduce memory overhead.
 * 
 * @author Finn Kuusisto
 */
public class StreamSound implements Sound {
	
	private URL dataURL;
	private long numBytesPerChannel;
	private Mixer mixer;
	private final int ID;
	
	/**
	 * Construct a new StreamSound with the given data and Mixer which will
	 * handle this StreamSound.
	 * @param dataURL URL of the temporary file containing audio data
	 * @param numBytesPerChannel the total number of bytes for each channel in
	 * the file
	 * @param mixer Mixer that will handle this StreamSound
	 * @param id unique ID of this StreamSound
	 * @throws IOException if a stream cannot be opened from the URL
	 */
	public StreamSound(URL dataURL, long numBytesPerChannel, Mixer mixer,
			int id) throws IOException {
		this.dataURL = dataURL;
		this.numBytesPerChannel = numBytesPerChannel;
		this.mixer = mixer;
		this.ID = id;
		//open and close a stream to check for immediate issues
		InputStream temp = this.dataURL.openStream();
		temp.close();
	}

	/**
	 * Plays this StreamSound.
	 */
	@Override
	public void play() {
		this.play(1.0);
	}

	/**
	 * Plays this StreamSound with a specified volume.
	 * @param volume the volume at which to play this StreamSound
	 */
	@Override
	public void play(double volume) {
		this.play(volume, 0.0);
	}

	/**
	 * Plays this MemSound with a specified volume and pan.
	 * @param volume the volume at which to play this MemSound
	 * @param pan the pan value to play this MemSound [-1.0,1.0], values outside
	 * the valid range will assume no panning (0.0)
	 */
	@Override
	public void play(double volume, double pan) {
		//dispatch a SoundReference to the mixer
		SoundReference ref;
		try {
			ref = new StreamSoundReference(this.dataURL.openStream(),
					this.numBytesPerChannel, volume, pan, this.ID);
			this.mixer.registerSoundReference(ref);
		} catch (IOException e) {
			System.err.println("Failed to open stream for Sound");
		}
	}

	/**
	 * Stops this StreamSound from playing.  Note that if this StreamSound was
	 * played repeatedly in an overlapping fashion, all instances of this
	 * StreamSound still playing will be stopped.
	 */
	@Override
	public void stop() {
		this.mixer.unRegisterSoundReference(this.ID);
	}

	/**
	 * Unloads this StreamSound from the system.  Attempts to use this
	 * StreamSound after unloading will result in error.
	 */
	@Override
	public void unload() {
		this.mixer.unRegisterSoundReference(this.ID);
		this.mixer = null;
		this.dataURL = null;
	}
	
	/////////////
	//Reference//
	/////////////
	
	/**
	 * The StreamSoundReference class is an implementation of the SoundReference
	 * interface.
	 * 
	 * @Finn Kuusisto
	 */
	private static class StreamSoundReference implements SoundReference {
		
		public final int SOUND_ID;
		
		private InputStream data;
		private long numBytesPerChannel; //not per frame, but the whole sound
		private long position;
		private double volume;
		private double pan;
		private byte[] buf;
		private byte[] skipBuf;
		
		/**
		 * Construct a new StreamSoundReference with the given reference data.
		 * @param data the stream of the audio data
		 * @param numBytesPerChannel the total number of bytes for each channel
		 * in the stream
		 * @param volume volume at which to play the sound
		 * @param pan pan at which to play the sound
		 * @param soundID ID of the StreamSound for which this is a reference
		 */
		public StreamSoundReference(InputStream data, long numBytesPerChannel,
				double volume, double pan, int soundID) {
			this.data = data;
			this.numBytesPerChannel = numBytesPerChannel;
			this.volume = (volume >= 0.0) ? volume : 1.0;
			this.pan = (pan >= -1.0 && pan <= 1.0) ? pan : 0.0;
			this.position = 0;
			this.buf = new byte[4];
			this.skipBuf = new byte[20];
			this.SOUND_ID = soundID;
		}

		/**
		 * Get the ID of the StreamSound that produced this
		 * StreamSoundReference.
		 * @return the ID of this StreamSoundReference's parent StreamSound
		 */
		@Override
		public int getSoundID() {
			return this.SOUND_ID;
		}

		/**
		 * Gets the volume of this StreamSoundReference.
		 * @return volume of this StreamSoundReference
		 */
		@Override
		public double getVolume() {
			return this.volume;
		}

		/**
		 * Gets the pan of this StreamSoundReference.
		 * @return pan of this StreamSoundReference
		 */
		@Override
		public double getPan() {
			return this.pan;
		}

		/**
		 * Get the number of bytes remaining for each channel.
		 * @return number of bytes remaining for each channel
		 */
		@Override
		public long bytesAvailable() {
			return this.numBytesPerChannel - this.position;
		}		

		/**
		 * Skip a specified number of bytes of the audio data.
		 * @param num number of bytes to skip
		 */
		@Override
		public void skipBytes(long num) {
			//terminate early if it would finish the sound
			if (this.position + num >= this.numBytesPerChannel) {
				this.position = this.numBytesPerChannel;
				return;
			}
			//this is the number of bytes to skip per channel, so double it
			long numSkip = num * 2;
			//spin read since skip is not always supported apparently and won't
			//guarantee a correct skip amount
			int tmpRead = 0;
			long numRead = 0;
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
			}
			//increment the position appropriately
			if (tmpRead == -1) { //reached end of file in the middle of reading
				this.position = this.numBytesPerChannel;
			}
			else {
				this.position += num;
			}
		}
		
		/**
		 * Get the next two bytes from the sound data in the specified
		 * endianness.
		 * @param data length-2 array to write in next two bytes from each
		 * channel
		 * @param bigEndian true if the bytes should be read big-endian
		 */
		@Override
		public void nextTwoBytes(int[] data, boolean bigEndian) {
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
				this.position = this.numBytesPerChannel;
			}
			else {
				this.position += 2;
			}
		}

		/**
		 * Does any cleanup necessary to dispose of resources in use by this
		 * StreamSoundReference.
		 */
		@Override
		public void dispose() {
			this.position = this.numBytesPerChannel;
			try {
				this.data.close();
			} catch (IOException e) {
				//whatever... this shouldn't happen
			}
			this.buf = null;
			this.skipBuf = null;
		}

	}

}

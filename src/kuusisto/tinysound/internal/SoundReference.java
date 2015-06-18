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

/**
 * The SoundReference interface is the Mixer's interface to the audio data of a
 * Sound object.  SoundReference is an internal interface of the TinySound
 * system and should be of no real concern to the average user of TinySound.
 * 
 * @author Finn Kuusisto
 */
public interface SoundReference {

	/**
	 * Get the ID of the Sound that produced this SoundReference.
	 * @return the ID of this SoundReference's parent Sound
	 */
	public int getSoundID();
	
	/**
	 * Gets the volume of this SoundReference.
	 * @return volume of this SoundReference
	 */
	public double getVolume();
	
	/**
	 * Gets the pan of this SoundReference.
	 * @return pan of this SoundReference
	 */
	public double getPan();
	
	/**
	 * Get the number of bytes remaining for each channel.
	 * @return number of bytes remaining for each channel
	 */
	public long bytesAvailable();
	
	/**
	 * Skip a specified number of bytes of the audio data.
	 * @param num number of bytes to skip
	 */
	public void skipBytes(long num);
	
	/**
	 * Get the next two bytes from the sound data in the specified endianness.
	 * @param data length-2 array to write in next two bytes from each channel
	 * @param bigEndian true if the bytes should be read big-endian
	 */
	public void nextTwoBytes(int[] data, boolean bigEndian);
	
	/**
	 * Does any cleanup necessary to dispose of resources in use by this
	 * SoundReference.
	 */
	public void dispose();
	
}

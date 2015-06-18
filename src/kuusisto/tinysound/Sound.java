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
package kuusisto.tinysound;

/**
 * The Sound interface  is an abstraction for sound effects.  Sound objects
 * should only be loaded via the TinySound <code>loadSound()</code> functions.
 * Sounds can be played repeatedly in an overlapping fashion.
 * 
 * @author Finn Kuusisto
 */
public interface Sound {

	/**
	 * Plays this Sound.
	 */
	public void play();
	
	/**
	 * Plays this Sound with a specified volume.
	 * @param volume the volume at which to play this Sound
	 */
	public void play(double volume);
	
	/**
	 * Plays this Sound with a specified volume and pan.
	 * @param volume the volume at which to play this Sound
	 * @param pan the pan value to play this Sound [-1.0,1.0], values outside
	 * the valid range will assume no panning (0.0)
	 */
	public void play(double volume, double pan);
	
	/**
	 * Stops this Sound from playing.  Note that if this Sound was played
	 * repeatedly in an overlapping fashion, all instances of this Sound still
	 * playing will be stopped.
	 */
	public void stop();
	
	/**
	 * Unloads this Sound from the system.  Attempts to use this Sound after
	 * unloading will result in error.
	 */
	public void unload();
	
}

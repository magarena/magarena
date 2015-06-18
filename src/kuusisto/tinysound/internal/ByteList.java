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

import java.util.Arrays;

/**
 * The ByteList class is a dynamically sized array of primitive bytes.  This
 * allows the use of a dynamically sized array without the extra overhead that
 * comes with List\<Byte\>.  ByteList is an internal class of the TinySound
 * system and should be of no real concern to the average user of TinySound.
 * 
 * @author Finn Kuusisto
 */
public class ByteList {
	
	private int numBytes;
	private byte[] data;
	
	/**
	 * Create a new ByteList of default starting size.
	 */
	public ByteList() {
		this(10);
	}
	
	/**
	 * Create a new ByteList of a specified starting size.  If the size is not
	 * valid, the default starting size is used.
	 * @param startSize the desired start size for the backing array
	 */
	public ByteList(int startSize) {
		startSize = startSize >= 0 ? startSize : 10;
		this.data = new byte[startSize];
		this.numBytes = 0;
	}
	
	/**
	 * Add a byte to the end of this ByteList.
	 * @param b the byte to add
	 */
	public void add(byte b) {
		if (this.numBytes >= this.data.length
				&& this.numBytes >= Integer.MAX_VALUE) {
			throw new RuntimeException("Array reached maximum size");
		}
		else if (this.numBytes >= this.data.length) {
			//grow the backing array
			long tmp = this.data.length * 2;
			int newSize = tmp > Integer.MAX_VALUE ?
					Integer.MAX_VALUE : (int)tmp;
			this.data = Arrays.copyOf(this.data, newSize);
		}
		this.data[this.numBytes] = b;
		this.numBytes++;
	}
	
	/**
	 * Get a byte at a specified index in this ByteList.
	 * @param i the index of the byte to get
	 * @return the byte at index i
	 */
	public byte get(int i) {
		if (i < 0 || i > this.numBytes) {
			throw new ArrayIndexOutOfBoundsException(i);
		}
		return this.data[i];
	}
	
	/**
	 * Get the number of bytes that have been added to this ByteList.
	 * @return the number of bytes added to this ByteList
	 */
	public int size() {
		return this.numBytes;
	}
	
	/**
	 * Get an array of all the bytes added to this ByteList.  This does not
	 * affect the backing array.
	 * @return an array of the bytes added to this ByteList
	 */
	public byte[] asArray() {
		return Arrays.copyOf(this.data, this.numBytes);
	}
	
	/**
	 * Clear this ByteList of all added bytes.
	 */
	public void clear() {
		this.data = new byte[10];
		this.numBytes = 0;
	}

}

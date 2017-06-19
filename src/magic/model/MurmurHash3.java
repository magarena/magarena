package magic.model;

/**
 * MurmurHash3 implementation in Java, based on Austin Appleby's <a href=
 * "https://code.google.com/p/smhasher/source/browse/trunk/MurmurHash3.cpp"
 * >original in C</a>
 *
 * Only implementing x64 version, because this should always be faster on 64 bit
 * native processors, even 64 bit being ran with a 32 bit OS; this should also
 * be as fast or faster than the x86 version on some modern 32 bit processors.
 *
 * @author Patrick McFarland
 * @see <a href="http://sites.google.com/site/murmurhash/">MurmurHash website</a>
 * @see <a href="http://en.wikipedia.org/wiki/MurmurHash">MurmurHash entry on Wikipedia</a>
 * @since 5.0
 */
public class MurmurHash3 {

   static class State {
      long h1;
      long h2;

      long k1;
      long k2;

      long c1;
      long c2;
   }

   static long getblock(byte[] key, int i) {
      return
           ((key[i + 0] & 0x00000000000000FFL))
         | ((key[i + 1] & 0x00000000000000FFL) << 8)
         | ((key[i + 2] & 0x00000000000000FFL) << 16)
         | ((key[i + 3] & 0x00000000000000FFL) << 24)
         | ((key[i + 4] & 0x00000000000000FFL) << 32)
         | ((key[i + 5] & 0x00000000000000FFL) << 40)
         | ((key[i + 6] & 0x00000000000000FFL) << 48)
         | ((key[i + 7] & 0x00000000000000FFL) << 56);
   }

   static void bmix(State state) {
      state.k1 *= state.c1;
      state.k1 = (state.k1 << 23) | (state.k1 >>> 64 - 23);
      state.k1 *= state.c2;
      state.h1 ^= state.k1;
      state.h1 += state.h2;

      state.h2 = (state.h2 << 41) | (state.h2 >>> 64 - 41);

      state.k2 *= state.c2;
      state.k2 = (state.k2 << 23) | (state.k2 >>> 64 - 23);
      state.k2 *= state.c1;
      state.h2 ^= state.k2;
      state.h2 += state.h1;

      state.h1 = state.h1 * 3 + 0x52dce729;
      state.h2 = state.h2 * 3 + 0x38495ab5;

      state.c1 = state.c1 * 5 + 0x7b7d159c;
      state.c2 = state.c2 * 5 + 0x6bce6396;
   }

   static long fmix(long k) {
      k ^= k >>> 33;
      k *= 0xff51afd7ed558ccdL;
      k ^= k >>> 33;
      k *= 0xc4ceb9fe1a85ec53L;
      k ^= k >>> 33;

      return k;
   }

   /**
    * Hash a value using the x64 128 bit variant of MurmurHash3
    *
    * @param key value to hash
    * @param seed random value
    * @return 128 bit hashed key, in an array containing two longs
    */
   private static State MurmurHash3_x64_128(final long[] key, final int seed) {
      State state = new State();

      state.h1 = 0x9368e53c2f6af274L ^ seed;
      state.h2 = 0x586dcd208f7cd3fdL ^ seed;

      state.c1 = 0x87c37b91114253d5L;
      state.c2 = 0x4cf5ad432745937fL;

      for (int i = 0; i < key.length / 2; i++) {
         state.k1 = key[i * 2];
         state.k2 = key[i * 2 + 1];

         bmix(state);
      }

      final long tail = key.length > 0 ? key[key.length - 1] : 0;

      // Key length is odd
      if ((key.length & 1) == 1) {
         state.k1 ^= tail;
         bmix(state);
      }

      state.h2 ^= key.length * 8;

      state.h1 += state.h2;
      state.h2 += state.h1;

      state.h1 = fmix(state.h1);
      state.h2 = fmix(state.h2);

      state.h1 += state.h2;
      state.h2 += state.h1;

      return state;
   }

   /**
    * Hash a value using the x64 64 bit variant of MurmurHash3
    *
    * @param key value to hash
    * @param seed random value
    * @return 64 bit hashed key
    */
   public static long MurmurHash3_x64_64(final long[] key, final int seed) {
      // Exactly the same as MurmurHash3_x64_128, except it only returns state.h1
      return MurmurHash3_x64_128(key, seed).h1;
   }

   /**
    * Hash a value using the x64 32 bit variant of MurmurHash3
    *
    * @param key value to hash
    * @param seed random value
    * @return 32 bit hashed key
    */
   public static int MurmurHash3_x64_32(final long[] key, final int seed) {
      return (int) (MurmurHash3_x64_64(key, seed) >>> 32);
   }

   /**
    * Hashes a byte array efficiently.
    *
    * @param payload a byte array to hash
    * @return a hash code for the byte array
    */
   public static long hash(long[] payload) {
      return MurmurHash3_x64_64(payload, 9001);
   }
}

//=========================================================================
//
//  Part of PEG parser generator Mouse.
//
//  Copyright (C) 2009, 2010, 2013
//  by Roman R. Redziejowski (www.romanredz.se).
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//
//-------------------------------------------------------------------------
//
//  Change log
//    090721 Created for Mouse 1.1.
//   Version 1.3
//    100504 Added c.diag to arguments of begin in saved and savedInner.
//    100504 In Cache(String) set diag to name instead of null.
//   Version 1.6
//    130416 Allowed m=0 to enable performance comparisons.
//
//=========================================================================

package magic.grammar;

import magic.grammar.Source;


//HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
//
//  ParserMemo
//
//HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH


public class ParserMemo extends ParserBase
{
  //-------------------------------------------------------------------
  //  Cache size.
  //-------------------------------------------------------------------
  int cacheSize = 1;

  //-------------------------------------------------------------------
  //  Phrase to reuse.
  //-------------------------------------------------------------------
  Phrase reuse;

  //-------------------------------------------------------------------
  //  List of Cache objects for initialization.
  //-------------------------------------------------------------------
  protected Cache[] caches;

  //-------------------------------------------------------------------
  //  Constructor
  //-------------------------------------------------------------------
  protected ParserMemo()
    {}

  //-------------------------------------------------------------------
  //  Initialize
  //-------------------------------------------------------------------
  public void init(Source src)
    {
      super.init(src);
      for (Cache c: caches) // Reset Cache objects
        c.reset();
    }

  //-------------------------------------------------------------------
  //  Set cache size.
  //-------------------------------------------------------------------
  public void setMemo(int m)
    {
      if (m<0 | m>9) throw new Error("m=" + m + " is outside range 0-9");
      cacheSize = m;
    }


  //=====================================================================
  //
  //  Methods called from parsing procedures
  //
  //=====================================================================
  //-------------------------------------------------------------------
  //  If saved result found, use it, otherwise begin new procedure.
  //  Version for Rule.
  //-------------------------------------------------------------------
  protected boolean saved(Cache c)
    {
      reuse = c.find();
      if (reuse!=null)                 // If found Phrase to reuse..
        return true;                   // .. return

      begin(c.name,c.diag);            // Otherwise push new Phrase
      c.save(current);                 // .. and cache it
      return false;
    }

  //-------------------------------------------------------------------
  //  If saved result found, use it, otherwise begin new procedure.
  //  Version for Inner.
  //-------------------------------------------------------------------
  protected boolean savedInner(Cache c)
    {
      reuse = c.find();
      if (reuse!=null)                 // If found Phrase to reuse..
        return true;                   // .. return

      begin("",c.diag);                // Otherwise push new Phrase
      c.save(current);                 // .. and cache it
      return false;
    }

  //-------------------------------------------------------------------
  //  Reuse Rule
  //-------------------------------------------------------------------
  protected boolean reuse()
    {
      if (reuse.success)
      {
        pos = reuse.end;               // Update position
        current.end = pos;             // Update end of current
        current.rhs.add(reuse);        // Attach p to rhs of current
        current.errMerge(reuse);       // Merge error info with current
        return true;
      }
      else
      {
        current.errMerge(reuse);       // Merge error info with current
        return false;
      }
    }

  //-------------------------------------------------------------------
  //  Reuse Inner
  //-------------------------------------------------------------------
  protected boolean reuseInner()
    {
      if (reuse.success)
      {
        pos = reuse.end;               // Update position
        current.end = pos;             // Update end of current
        current.rhs.addAll(reuse.rhs); // Add rhs to rhs of current
        current.errMerge(reuse);       // Merge error info with current
        return true;
      }
      else
      {
        current.errMerge(reuse);       // Merge error info with current
        return false;
      }
    }

  //-------------------------------------------------------------------
  //  Reuse predicate
  //-------------------------------------------------------------------
  protected boolean reusePred()
    {
      if (reuse.success)
        return true;
      else
      {
        current.errMerge(reuse);       // Merge error info with current
        return false;
      }
    }


  //HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
  //
  //  Cache
  //
  //HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

  protected class Cache
  {
    public final String name;
    public final String diag;

    Phrase[] cache;
    int last;

    public Cache(final String name)
      {
        this.name = name;
        this.diag = name;
      }

    public Cache(final String name, final String diag)
      {
        this.name = name;
        this.diag = diag;
      }

    void reset()
      {
        cache = new Phrase[cacheSize];
        last = 0;
      }

    void save(Phrase p)
      {
        if (cacheSize==0) return;
        last = (last+1)%cacheSize;
        cache[last] = p;
      }

    Phrase find()
      {
        if (cacheSize==0) return null;
        for (Phrase p: cache)
          if (p!=null && p.start==pos) return p;
        return null;
      }
  }

}




//=========================================================================
//
//  Part of PEG parser generator Mouse.
//
//  Copyright (C) 2009, 2010, 2011
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
//    100429 In error trace added name of the Phrase owning error info.
//    100504 Added c.diag to arguments of begin in saved and savedInner.
//    100504 In error trace changed current.name to current.diag.
//    101127 Renamed 'startpos' to 'endpos' in tracing.
//   Version 1.4
//    111004 Added methods to implement ^[s].
//   Version 1.5
//    111105 Revised methods for ^[s] and ^[c].
//
//=========================================================================

package magic.grammar;

import java.util.BitSet;


//HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
//
//  ParserTest
//
//HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

public class ParserTest extends ParserMemo
{
  //-------------------------------------------------------------------
  //  Cache size.
  //-------------------------------------------------------------------
  int cacheSize = 1;

  //-------------------------------------------------------------------
  //  Trace switches.
  //-------------------------------------------------------------------
  public boolean traceRules;        // Trace Rules
  public boolean traceInner;        // Trace subexpressions
  public boolean traceError;        // Trace error info

  //-------------------------------------------------------------------
  //  Constructor
  //-------------------------------------------------------------------
  protected ParserTest()
    {}

  //-------------------------------------------------------------------
  //  Set cache size.
  //-------------------------------------------------------------------
  public void setMemo(int m)
    {
      if (m<0 | m>9) throw new Error("m=" + m + " outside range 0-9");
      cacheSize = m;
    }

  //-------------------------------------------------------------------
  //  Set trace
  //-------------------------------------------------------------------
  public void setTrace(String trace)
    {
      super.setTrace(trace);
      traceRules = trace.indexOf('r')>=0;
      traceInner = trace.indexOf('i')>=0;
      traceError = trace.indexOf('e')>=0;
    }

  //-------------------------------------------------------------------
  //  Access to cache list
  //-------------------------------------------------------------------
  public Cache[] caches()
    { return (Cache[])caches; }

  //-------------------------------------------------------------------
  //  Write trace
  //-------------------------------------------------------------------
  void trace(final String s)
    { System.out.println(s); }


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
      c.calls++;
      if (traceRules) trace(source.where(pos) + ": INIT " + c.name);
      reuse = c.find();
      if (reuse!=null)
      {
        c.reuse++;
        if (traceRules) trace("REUSE " + (reuse.success? "succ " : "fail "));
        return true;
      }

      begin(c.name,c.diag);
      c.save(current);
      if (c.prevpos.get(pos)) c.rescan++;
      else c.prevpos.set(pos);
      return false;
    }

  //-------------------------------------------------------------------
  //  If saved result found, use it, otherwise begin new procedure.
  //  Version for Inner.
  //-------------------------------------------------------------------
  protected boolean savedInner(Cache c)
    {
      c.calls++;
      if (traceInner) trace(source.where(pos) + ": INIT " + c.name);
      reuse = c.find();
      if (reuse!=null)
      {
        c.reuse++;
        if (traceInner) trace("REUSE " + (reuse.success? "succ " : "fail "));
        return true;
      }

      begin("",c.diag);
      c.save(current);
      if (c.prevpos.get(pos)) c.rescan++;
      else c.prevpos.set(pos);
      return false;
    }


  //-------------------------------------------------------------------
  //  Accept Rule
  //-------------------------------------------------------------------
  protected boolean accept(Cache c)
    {
      super.accept();
      traceAccept(c,traceRules);
      return true;
    }

  //-------------------------------------------------------------------
  //  Accept Inner
  //-------------------------------------------------------------------
  protected boolean acceptInner(Cache c)
    {
      super.acceptInner();
      traceAccept(c,traceInner);
      return true;
    }

  //-------------------------------------------------------------------
  //  Accept And-predicate (argument was accepted)
  //-------------------------------------------------------------------
  protected boolean acceptAnd(Cache c)
    {
      super.acceptAnd();
      traceAccept(c,traceInner);
      return true;
    }

  //-------------------------------------------------------------------
  //  Accept Not-predicate (argument was rejected)
  //-------------------------------------------------------------------
  protected boolean acceptNot(Cache c)
    {
      super.acceptNot();
      traceAccept(c,traceInner);
      return true;
    }

  //-------------------------------------------------------------------
  //  Trace accept
  //-------------------------------------------------------------------
  private void traceAccept(Cache c, boolean cond)
    {
      if (cond)
      {
        trace(source.where(pos) + ": ACCEPT " + c.name);
        if (traceError) trace(current.diag + "  --" + current.errMsg());
      }
      c.succ++;
    }


  //-------------------------------------------------------------------
  //  Reject Rule
  //-------------------------------------------------------------------
  protected boolean reject(Cache c)
    {
      int endpos = pos;
      super.reject();
      traceReject(c,traceRules,endpos);
      return false;
    }

  //-------------------------------------------------------------------
  //  Reject Inner
  //-------------------------------------------------------------------
  protected boolean rejectInner(Cache c)
    {
      int endpos = pos;
      super.rejectInner();
      traceReject(c,traceInner,endpos);
      return false;
    }

  //-------------------------------------------------------------------
  //  Reject And-predicate (argument was rejected)
  //-------------------------------------------------------------------
  protected boolean rejectAnd(Cache c)
    {
      int endpos = pos;
      super.rejectAnd();
      traceReject(c,traceInner,endpos);
      return false;
    }

  //-------------------------------------------------------------------
  //  Reject Not-predicate (argument was accepted)
  //-------------------------------------------------------------------
  protected boolean rejectNot(Cache c)
    {
      int endpos = pos;
      super.rejectNot();
      traceReject(c,traceInner,endpos);
      return false;
    }

  //-------------------------------------------------------------------
  //  Trace reject
  //-------------------------------------------------------------------
  private void traceReject(Cache c, boolean cond, int endpos)
    {
      if (cond)
      {
        trace(source.where(endpos) + ": REJECT " + c.name);
        if (traceError) trace(current.diag + "  --" + current.errMsg());
      }
      if (pos==endpos) c.fail++; // No backtrack
      else                         // Backtrack
      {
        int b = endpos-pos;
        c.back++;
        c.totback += b;
        if (b>c.maxback)
        {
          c.maxback = b;
          c.maxbpos = pos;
        }
      }
    }


  //-------------------------------------------------------------------
  //  Execute expression 'c'
  //-------------------------------------------------------------------
  protected boolean next(char ch,Cache c)
    {
      int endpos = pos;
      boolean succ = super.next(ch);
      return traceTerm(endpos,succ,c);
    }

  //-------------------------------------------------------------------
  //  Execute expression ^'c'
  //-------------------------------------------------------------------
  protected boolean nextNot(char ch,Cache c)
    {
      int endpos = pos;
      boolean succ = super.nextNot(ch);
      return traceTerm(endpos,succ,c);
    }

  //-------------------------------------------------------------------
  //  Execute expression &'c', !^'c'
  //-------------------------------------------------------------------
  protected boolean ahead(char ch,Cache c)
    {
      int endpos = pos;
      boolean succ = super.ahead(ch);
      return traceTerm(endpos,succ,c);
    }

  protected boolean aheadNotNot(char ch,Cache c)
    { return ahead(ch,c); }

  //-------------------------------------------------------------------
  //  Execute expression !'c', &^'c'
  //-------------------------------------------------------------------
  protected boolean aheadNot(char ch,Cache c)
    {
      int endpos = pos;
      boolean succ = super.aheadNot(ch);
      return traceTerm(endpos,succ,c);
    }


  //-------------------------------------------------------------------
  //  Execute expression "s"
  //-------------------------------------------------------------------
  protected boolean next(String s,Cache c)
    {
      int endpos = pos;
      boolean succ = super.next(s);
      return traceTerm(endpos,succ,c);
    }

  //-------------------------------------------------------------------
  //  Execute expression &"s"
  //-------------------------------------------------------------------
  protected boolean ahead(String s,Cache c)
    {
      int endpos = pos;
      boolean succ = super.ahead(s);
      return traceTerm(endpos,succ,c);
    }

  //-------------------------------------------------------------------
  //  Execute expression !"s"
  //-------------------------------------------------------------------
  protected boolean aheadNot(String s,Cache c)
    {
      int endpos = pos;
      boolean succ = super.aheadNot(s);
      return traceTerm(endpos,succ,c);
    }


  //-------------------------------------------------------------------
  //  Execute expression [s]
  //-------------------------------------------------------------------
  protected boolean nextIn(String s,Cache c)
    {
      int endpos = pos;
      boolean succ = super.nextIn(s);
      return traceTerm(endpos,succ,c);
    }

  //-------------------------------------------------------------------
  //  Execute expression ^[s]
  //-------------------------------------------------------------------
  protected boolean nextNotIn(String s,Cache c)
    {
      int endpos = pos;
      boolean succ = super.nextNotIn(s);
      return traceTerm(endpos,succ,c);
    }

  //-------------------------------------------------------------------
  //  Execute expression &[s], !^[s]
  //-------------------------------------------------------------------
  protected boolean aheadIn(String s,Cache c)
    {
      int endpos = pos;
      boolean succ = super.aheadIn(s);
      return traceTerm(endpos,succ,c);
    }

  protected boolean aheadNotNotIn(String s,Cache c)
    { return aheadIn(s,c); }

  //-------------------------------------------------------------------
  //  Execute expression ![s], &^[s]
  //-------------------------------------------------------------------
  protected boolean aheadNotIn(String s,Cache c)
    {
      int endpos = pos;
      boolean succ = super.aheadNotIn(s);
      return traceTerm(endpos,succ,c);
    }


  //-------------------------------------------------------------------
  //  Execute expression [a-z]
  //-------------------------------------------------------------------
  protected boolean nextIn(char a, char z, Cache c)
    {
      int endpos = pos;
      boolean succ = super.nextIn(a,z);
      return traceTerm(endpos,succ,c);
    }

  //-------------------------------------------------------------------
  //  Execute expression &[a-z]
  //-------------------------------------------------------------------
  protected boolean aheadIn(char a, char z, Cache c)
    {
      int endpos = pos;
      boolean succ = super.aheadIn(a,z);
      return traceTerm(endpos,succ,c);
    }

  //-------------------------------------------------------------------
  //  Execute expression ![a-z]
  //-------------------------------------------------------------------
  protected boolean aheadNotIn(char a, char z, Cache c)
    {
      int endpos = pos;
      boolean succ = super.aheadNotIn(a,z);
      return traceTerm(endpos,succ,c);
    }


  //-------------------------------------------------------------------
  //  Execute expression _
  //-------------------------------------------------------------------
  protected boolean next(Cache c)
    {
      int endpos = pos;
      boolean succ = super.next();
      return traceTerm(endpos,succ,c);
    }

  //-------------------------------------------------------------------
  //  Execute expression &_
  //-------------------------------------------------------------------
  protected boolean ahead(Cache c)
    {
      int endpos = pos;
      boolean succ = super.ahead();
      return traceTerm(endpos,succ,c);
    }

  //-------------------------------------------------------------------
  //  Execute expression !_
  //-------------------------------------------------------------------
  protected boolean aheadNot(Cache c)
    {
      int endpos = pos;
      boolean succ = super.aheadNot();
      return traceTerm(endpos,succ,c);
    }


  //-------------------------------------------------------------------
  //  Trace term
  //-------------------------------------------------------------------
  private boolean traceTerm(int endpos, boolean succ, Cache c)
    {
      c.calls++;
      if (c.prevpos.get(endpos)) c.rescan++;
      else c.prevpos.set(endpos);
      if (succ) { c.succ++; return true; }
      else { c.fail++; return false; }
    }



  //HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
  //
  //  Cache object
  //
  //HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

  public class Cache extends ParserMemo.Cache
  {
    public int calls  ; // Total number of calls
    public int rescan ; // How many were rescans without reuse
    public int reuse  ; // How many were rescans with reuse
    public int succ   ; // How many resulted in success
    public int fail   ; // How many resulted in failure, no backtrack
    public int back   ; // How many resulted in backtrack
    public int totback; // Accumulated amount of backtrack
    public int maxback; // Maximum length of backtrack
    public int maxbpos; // Position of naximal backtrack
    BitSet prevpos    ; // Scan history


    public Cache(final String name)
      { super(name); }

    public Cache(final String name,final String diag)
      { super(name,diag); }

    void save(Phrase p)
      {
        if (cacheSize==0) return;
        super.save(p);
      }

    Phrase find()
      {
        if (cacheSize==0) return null;
        return super.find();
      }

    void reset()
      {
        super.reset();
        calls   = 0;
        rescan  = 0;
        reuse   = 0;
        succ    = 0;
        fail    = 0;
        back    = 0;
        totback = 0;
        maxback = 0;
        maxbpos = 0;
        prevpos = new BitSet(60000);
      }
  }
}




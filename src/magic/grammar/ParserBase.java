//=========================================================================
//
//  Part of PEG parser generator Mouse.
//
//  Copyright (C) 2009, 2010, 2011, 2012
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
//
//   090720 Created for Mouse 1.1.
//  Version 1.2
//   100320 Bug fix in accept(): upgrade error info on success.
//   100320 Bug fix in rejectNot(): backtrack before registering failure.
//  Version 1.3
//   100429 Bug fix in errMerge(Phrase): assignment to errText replaced
//          by clear + addAll (assignment produced alias resulting in
//          explosion of errText in memo version).
//   101105 Changed errMerge(msg,pos) to errAdd(who).
//   101105 Commented error handling.
//   101129 Added 'boolReject'.
//   101203 Convert result of 'listErr' to printable.
//  Version 1.4
//   110918 Changed 'listErr' to separate 'not' texts as 'not expected'.
//   111004 Added methods to implement ^[s].
//   111004 Implemented method 'where' of Phrase.
//  Version 1.5
//   111027 Revised methods for ^[s] and ^[c].
//   111104 Implemented methods 'rule' and 'isTerm' of Phrase.
//  Version 1.5.1
//   120102 (Steve Owens) Ensure failure() method does not emit blank
//          line when error info is absent.
//  Version 1.6
//   120130 rhsText: return empty string for empty range.
//
//=========================================================================

package magic.grammar;

import magic.grammar.Source;
import java.util.Vector;


//HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
//
//  ParserBase
//
//HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH


public class ParserBase implements magic.grammar.CurrentRule
{
  //-------------------------------------------------------------------
  //  Input
  //-------------------------------------------------------------------
  Source source;                    // Source of text to parse
  int endpos;                       // Position after the end of text
  int pos;                          // Current position in the text

  //-------------------------------------------------------------------
  //  Semantics (base)
  //-------------------------------------------------------------------
  protected magic.grammar.SemanticsBase sem;

  //-------------------------------------------------------------------
  //  Trace string.
  //-------------------------------------------------------------------
  protected String trace = "";

  //-------------------------------------------------------------------
  //  Current phrase (top of parse stack).
  //-------------------------------------------------------------------
  Phrase current = null;

  //-------------------------------------------------------------------
  //  Constructor
  //-------------------------------------------------------------------
  protected ParserBase()
    {}

  //-------------------------------------------------------------------
  //  Initialize parsing
  //-------------------------------------------------------------------
  public void init(Source src)
    {
      source = src;
      pos = 0;
      endpos = source.end();
      current = new Phrase("","",0); // Dummy bottom of parse stack
    }

  //-------------------------------------------------------------------
  //  Implementation of Parser interface CurrentRule
  //-------------------------------------------------------------------
  public Phrase lhs()
    { return current; }

  public Phrase rhs(int i)
    { return current.rhs.elementAt(i); }

  public int rhsSize()
    { return current.rhs.size(); }

  public String rhsText(int i,int j)
    {
      if (j<=i) return "";
      return source.at(rhs(i).start,rhs(j-1).end);
    }

  //-------------------------------------------------------------------
  //  Set trace
  //-------------------------------------------------------------------
  public void setTrace(String trace)
    {
      this.trace = trace;
      sem.trace = trace;
    }

  //-------------------------------------------------------------------
  //  Print final error message (if not caught otherwise).
  //-------------------------------------------------------------------
  protected boolean failure()
    {
      if (current.errPos>=0)
        System.out.println(current.errMsg());
      return false;
    }

  //=====================================================================
  //
  //  Methods called from parsing procedures
  //
  //=====================================================================
  //-------------------------------------------------------------------
  //  Initialize processing of a nonterminal
  //-------------------------------------------------------------------
  protected void begin(final String name)
    {
      Phrase p = new Phrase(name,name,pos);
      p.parent = current;
      current = p;
    }

  protected void begin(final String name,final String diag)
    {
      Phrase p = new Phrase(name,diag,pos);
      p.parent = current;
      current = p;
    }

  //-------------------------------------------------------------------
  //  Accept Rule
  //  Note: 'upgrade error info' is applied when rule such as
  //  R = A/B/C/... or R = (A/B/C/...)* consumed empty string after one
  //  or more of A,B,C failed without advancing cursor.
  //  In case of a later failure, it gives message 'expected R',
  //  which is not strictly correct because R succeeded, but is
  //  more comprehensible than 'expected A or B or C or ...'.
  //-------------------------------------------------------------------
  protected boolean accept()
    {
      Phrase p = pop();                // Pop p from compile stack
      p.rhs = null;                    // Remove right-hand side of p
      if (p.errPos==p.start)           // Upgrade error info of p
        p.errSet(p.diag,p.start);
      p.success = true;                // Indicate p successful
      current.end = pos;               // Update end of parent
      current.rhs.add(p);              // Attach p to rhs of parent
      current.errMerge(p);             // Merge error info with parent
      return true;
    }

  //-------------------------------------------------------------------
  //  Accept Inner
  //-------------------------------------------------------------------
  protected boolean acceptInner()
    {
      Phrase p = pop();                // Pop p from compile stack
      p.success = true;                // Indicate p successful
      current.end = pos;               // Update end of parent
      current.rhs.addAll(p.rhs);       // Add rhs of p to rhs of parent
      current.errMerge(p);             // Merge error info with parent
      return true;
    }

  //-------------------------------------------------------------------
  //  Accept And-predicate (argument was accepted)
  //  Note: we ignore all failures encountered in processing the argument.
  //-------------------------------------------------------------------
  protected boolean acceptAnd()
    {
      Phrase p = pop();                // Pop p from compile stack
      p.end = p.start;                 // Reset end of P
      p.rhs = null;                    // Remove right-hand side of p
      p.errClear();                    // Remove error info from p
      p.success = true;                // Indicate p successful
      pos = p.start;                   // Backtrack to start of p
      return true;
    }

  //-------------------------------------------------------------------
  //  Accept Not-predicate (argument was rejected)
  //  Note: we ignore all failures encountered in processing the argument.
  //-------------------------------------------------------------------
  protected boolean acceptNot()
    {
      Phrase p = pop();                // Pop p from compile stack
      p.rhs = null;                    // Remove right-hand side of p
      p.errClear();                    // Remove error info from p
      p.success = true;                // Indicate p successful
      return true;
    }



  //-------------------------------------------------------------------
  //  Reject Rule
  //  Note: 'upgrade error info' is applied when rule such as
  //  R = A/B/C/... failed after one or more of A,B,C failed without
  //  advancing cursor. In case of a later failure, it gives message
  //  'expected R', instead of 'expected A or B or C or ...'.
  //-------------------------------------------------------------------
  protected boolean reject()
    {
      Phrase p = pop();                // Pop p from compile stack
      p.end = p.start;                 // Reset end of p
      p.rhs = null;                    // Remove right-hand side of p
      if (p.errPos==p.start)           // Upgrade error info of p
        p.errSet(p.diag,p.start);
      p.success = false;               // Indicate p failed
      current.errMerge(p);             // Merge error info with parent
      pos = p.start;                   // Backtrack to start of p
      return false;
    }

  //-------------------------------------------------------------------
  //  Simulate failure after boolean action returned false.
  //  Note: the action was called after the Rule accepted some text.
  //  We ignore all failures encountered in the process
  //  and report failure at the start of the text.
  //-------------------------------------------------------------------
  protected boolean boolReject()
    {
      pos = current.start;             // Backtrack to start
      current.end = pos;               // Reset end
      current.rhs.clear();             // Clear right-hand side
      current.errSet(current.diag,pos);// Register failure
      return false;
    }

  //-------------------------------------------------------------------
  //  Reject Inner
  //-------------------------------------------------------------------
  protected boolean rejectInner()
    {
      Phrase p = pop();                // Pop p from compile stack
      p.end = p.start;                 // Reset end of p
      p.rhs = null;                    // Remove right-hand side of p
      p.success = false;               // Indicate p failed
      current.errMerge(p);             // Merge error info with parent
      pos = p.start;                   // Backtrack to start of p
      return false;
    }

  //-------------------------------------------------------------------
  //  Reject And-predicate (argument was rejected)
  //  Note: we ignore all failures encountered in processing the argument,
  //  and register failure at the point of call of the predicate.
  //-------------------------------------------------------------------
  protected boolean rejectAnd()
    {
      Phrase p = pop();                // Pop p from compile stack
      p.rhs = null;                    // Remove right-hand side of p
      p.errSet(p.diag,pos);            // Register 'xxx expected'
      p.success = false;               // Indicate p failed
      current.errMerge(p);             // Merge error info with parent
      return false;
    }

  //-------------------------------------------------------------------
  //  Reject Not-predicate (argument was accepted)
  //  Note: we ignore all failures encountered in processing the argument,
  //  and register failure at the point of call of the predicate.
  //-------------------------------------------------------------------
  protected boolean rejectNot()
    {
      Phrase p = pop();                // Pop p from compile stack
      p.end = p.start;                 // Reset end of p
      p.rhs = null;                    // Remove right-hand side of p
      pos = p.start;                   // Backtrack to start of p
      p.errSet(p.diag,pos);            // Register 'xxx not expected'
      p.success = false;               // Indicate p failed
      current.errMerge(p);             // Merge error info with parent
      return false;
    }


  //-------------------------------------------------------------------
  //  Execute expression 'c'
  //-------------------------------------------------------------------
  protected boolean next(char ch)
    {
      if (pos<endpos && source.at(pos)==ch) return consume(1);
      else return fail("'" + ch + "'");
    }

  //-------------------------------------------------------------------
  //  Execute expression ^'c'
  //-------------------------------------------------------------------
  protected boolean nextNot(char ch)
    {
      if (pos<endpos && source.at(pos)!=ch) return consume(1);
      else return fail("not '" + ch + "'");
    }

  //-------------------------------------------------------------------
  //  Execute expression &'c', !^'c'
  //-------------------------------------------------------------------
  protected boolean ahead(char ch)
    {
      if (pos<endpos && source.at(pos)==ch) return true;
      else return fail("'" + ch + "'");
    }

  protected boolean aheadNotNot(char ch)  // temporary
    { return ahead(ch); }

  //-------------------------------------------------------------------
  //  Execute expression !'c', &^'c'
  //-------------------------------------------------------------------
  protected boolean aheadNot(char ch)
    {
      if (pos<endpos && source.at(pos)==ch) return fail("not '" + ch + "'");
      else return true;
    }


  //-------------------------------------------------------------------
  //  Execute expression "s"
  //-------------------------------------------------------------------
  protected boolean next(String s)
    {
      int lg = s.length();
      if (pos+lg<=endpos && source.at(pos,pos+lg).equals(s)) return consume(lg);
      else return fail("'" + s + "'");
    }

  //-------------------------------------------------------------------
  //  Execute expression &"s"
  //-------------------------------------------------------------------
  protected boolean ahead(String s)
    {
      int lg = s.length();
      if (pos+lg<=endpos && source.at(pos,pos+lg).equals(s)) return true;
      else return fail("'" + s + "'");
    }

  //-------------------------------------------------------------------
  //  Execute expression !"s"
  //-------------------------------------------------------------------
  protected boolean aheadNot(String s)
    {
      int lg = s.length();
      if (pos+lg<=endpos && source.at(pos,pos+lg).equals(s)) return fail("not '" + s + "'");
      else return true;
    }


  //-------------------------------------------------------------------
  //  Execute expression [s]
  //-------------------------------------------------------------------
  protected boolean nextIn(String s)
    {
      if (pos<endpos && s.indexOf(source.at(pos))>=0) return consume(1);
      else return fail("[" + s + "]");
    }

  //-------------------------------------------------------------------
  //  Execute expression ^[s]
  //-------------------------------------------------------------------
  protected boolean nextNotIn(String s)
    {
      if (pos<endpos && s.indexOf(source.at(pos))<0) return consume(1);
      else return fail("not [" + s + "]");
    }

  //-------------------------------------------------------------------
  //  Execute expression &[s], !^[s]
  //-------------------------------------------------------------------
  protected boolean aheadIn(String s)
    {
      if (pos<endpos && s.indexOf(source.at(pos))>=0) return true;
      else return fail("[" + s + "]");
    }

  protected boolean aheadNotNotIn(String s) // temporary
    { return aheadIn(s); }

  //-------------------------------------------------------------------
  //  Execute expression ![s], &^[s]
  //-------------------------------------------------------------------
  protected boolean aheadNotIn(String s)
    {
      if (pos<endpos && s.indexOf(source.at(pos))>=0) return fail("not [" + s + "]");
      else return true;
    }


  //-------------------------------------------------------------------
  //  Execute expression [a-z]
  //-------------------------------------------------------------------
  protected boolean nextIn(char a, char z)
    {
      if (pos<endpos && source.at(pos)>=a && source.at(pos)<=z)
        return consume(1);
      else return fail("[" + a + "-" + z + "]");
    }

  //-------------------------------------------------------------------
  //  Execute expression &[a-z]
  //-------------------------------------------------------------------
  protected boolean aheadIn(char a, char z)
    {
      if (pos<endpos && source.at(pos)>=a && source.at(pos)<=z)
        return true;
      else return fail("[" + a + "-" + z + "]");
    }

  //-------------------------------------------------------------------
  //  Execute expression ![a-z]
  //-------------------------------------------------------------------
  protected boolean aheadNotIn(char a, char z)
    {
      if (pos<endpos && source.at(pos)>=a && source.at(pos)<=z)
        return fail("not [" + a + "-" + z + "]");
      else return true;
    }


  //-------------------------------------------------------------------
  //  Execute expression _
  //-------------------------------------------------------------------
  protected boolean next()
    {
      if (pos<endpos) return consume(1);
      else return fail("any character");
    }

  //-------------------------------------------------------------------
  //  Execute expression &_
  //-------------------------------------------------------------------
  protected boolean ahead()
    {
      if (pos<endpos) return true;
      else return fail("any character");
    }

  //-------------------------------------------------------------------
  //  Execute expression !_
  //-------------------------------------------------------------------
  protected boolean aheadNot()
    {
      if (pos<endpos) return fail("end of text");
      else return true;
    }


  //-------------------------------------------------------------------
  //  Pop Phrase from compile stack
  //-------------------------------------------------------------------
  private Phrase pop()
    {
      Phrase p = current;
      current = p.parent;
      p.parent = null;
      return p;
    }

  //-------------------------------------------------------------------
  //  Consume terminal
  //-------------------------------------------------------------------
  private boolean consume(int n)
    {
      Phrase p = new Phrase("","",pos);
      pos += n;
      p.end = pos;
      current.rhs.add(p);
      current.end = pos;
      return true;
    }

  //-------------------------------------------------------------------
  //  Fail
  //-------------------------------------------------------------------
  private boolean fail(String msg)
    {
      current.errAdd(msg);
      return false;
    }



  //HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
  //
  //  Phrase
  //
  //HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

  public class Phrase implements magic.grammar.Phrase
  {
    //===================================================================
    //
    //  Data
    //
    //===================================================================

    final String name;
    final String diag;
    final int start;
    int end;
    boolean success;
    Vector<Phrase> rhs = new Vector<Phrase>(10,10);
    Object value = null;
    Phrase parent = null;

    //-----------------------------------------------------------------
    //  Errors encountered in processing of this Phrase.
    //  We keep information about the failure farthest down in text,
    //  and only failure of a rule or a terminal (inner expressions
    //  do not have diagnostic names.
    //  - 'errPos' is position the failure, or -1 if there was none.
    //  - 'errTxt' identifies the expression(s) that failed at 'errPos'.
    //     There may be several such expressions if 'errPos' was reached
    //     on several attempts. The expressions are identified
    //     by their diagnostic names.
    //-----------------------------------------------------------------
    int errPos = -1;
    Vector<String> errTxt = new Vector<String>();


    //===================================================================
    //
    //  Constructor
    //
    //===================================================================

    Phrase(final String name,final String diag,int start)
      {
        this.name = name;
        this.diag = diag;
        this.start = start;
        this.end = start;
      }

    //===================================================================
    //
    //  Interface 'magic.grammar.Phrase'
    //
    //===================================================================
    //-----------------------------------------------------------------
    //  Set value
    //-----------------------------------------------------------------
    public void put(Object o)
      { value = o; }

    //-----------------------------------------------------------------
    //  Get value
    //-----------------------------------------------------------------
    public Object get()
      { return value; }

    //-----------------------------------------------------------------
    //  Get text
    //-----------------------------------------------------------------
    public String text()
      { return source.at(start,end); }

    //-------------------------------------------------------------------
    //  Get i-th character of text
    //-------------------------------------------------------------------
    public char charAt(int i)
      { return source.at(start+i); }

    //-----------------------------------------------------------------
    //  Is text empty?
    //-----------------------------------------------------------------
    public boolean isEmpty()
      { return start==end; }

    //-------------------------------------------------------------------
    //  Get name of rule that created this Phrase.
    //-------------------------------------------------------------------
    public String rule()
      { return name; }

    //-------------------------------------------------------------------
    //  Was this Phrase created by rule 'rule'?
    //-------------------------------------------------------------------
    public boolean isA(String rule)
      { return name.equals(rule); }

    //-------------------------------------------------------------------
    //  Was this Phrase created by a terminal?
    //-------------------------------------------------------------------
    public boolean isTerm()
      { return name.isEmpty(); }

    //-----------------------------------------------------------------
    //  Get error message
    //-----------------------------------------------------------------
    public String errMsg()
      {
        if (errPos<0) return "";
        return source.where(errPos) + ":" + listErr();
      }

    //-----------------------------------------------------------------
    //  Clear error information
    //-----------------------------------------------------------------
    public void errClear()
      {
        errTxt.clear();
        errPos = -1;
      }

    //-----------------------------------------------------------------
    //  Describe position of i-th character of the Phrase in source text.
    //-----------------------------------------------------------------
    public String where(int i)
      { return source.where(start+i); }


    //===================================================================
    //
    //  Operations on error info
    //
    //===================================================================

    //-----------------------------------------------------------------
    //  Set fresh info ('who' failed 'where'), discarding any previous.
    //-----------------------------------------------------------------
    void errSet(final String who, int where)
      {
        errTxt.clear();
        errTxt.add(who);
        errPos = where;
      }

    //-----------------------------------------------------------------
    //  Add info about 'who' failing at current position.
    //-----------------------------------------------------------------
    void errAdd(final String who)
      {
        if (errPos>pos) return;   // If current position older: forget
        if (errPos<pos)           // If current position newer: replace
        {
          errTxt.clear();
          errPos = pos;
          errTxt.add(who);
          return;
        }
                                  // If error at same position: add
        errTxt.add(who);
      }

    //-----------------------------------------------------------------
    //  Merge error info with with that from Phrase 'p'.
    //-----------------------------------------------------------------
    void errMerge(final Phrase p)
      {
        if (p.errPos<pos && errPos<pos) // If we passed all error points
        {
          errClear();
          return;
        }

        if (p.errPos<0) return;         // If no error in p: forget
        if (errPos>p.errPos) return;    // If error in p older: forget
        if (errPos<p.errPos)            // If error in p newer: replace all info
        {
          errTxt.clear();
          errPos = p.errPos;
          errTxt.addAll(p.errTxt);
          return;
        }
                                        // If error in p at same position
        errTxt.addAll(p.errTxt);        // Add all from p
      }

    //-----------------------------------------------------------------
    //  List errors
    //-----------------------------------------------------------------
    private String listErr()
      {
        StringBuilder one = new StringBuilder();
        StringBuilder two = new StringBuilder();
        Vector<String> done = new Vector<String>();
        for (String s: errTxt)
        {
          if (done.contains(s)) continue;
          done.add(s);
          if (s.startsWith("not "))
            toPrint(" or " + s.substring(4),two);
          else
            toPrint(" or " + s,one);
        }

        if (one.length()>0)
        {
          if (two.length()==0)
            return " expected " + one.toString().substring(4);
          else
            return " expected " + one.toString().substring(4) +
                   "; not expected " + two.toString().substring(4);
        }
        else
          return " not expected " + two.toString().substring(4);
      }

    //-----------------------------------------------------------------
    //  Convert string to printable and append to StringBuilder.
    //-----------------------------------------------------------------
    private void toPrint(final String s, StringBuilder sb)
      {
        for (int i=0;i<s.length();i++)
        {
          char c = s.charAt(i);
          switch(c)
          {
            case '\b': sb.append("\\b"); continue;
            case '\f': sb.append("\\f"); continue;
            case '\n': sb.append("\\n"); continue;
            case '\r': sb.append("\\r"); continue;
            case '\t': sb.append("\\t"); continue;
            default:
              if (c<32 || c>256)
              {
                String u = "000" + Integer.toHexString(c);
                sb.append("\\u" + u.substring(u.length()-4,u.length()));
              }
              else sb.append(c);
              continue;
          }
        }
      }
  }

}




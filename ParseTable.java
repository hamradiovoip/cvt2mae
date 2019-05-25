/** File: ParseTable.java */

package cvt2mae;

/**
 * This class parses string row input data into data for a spot element.
 * It is similar to StringTokenizer class 
 * except the null is returned for each token field when there
 * are multiple delimiters repeated.
 * Also, for now, a single character is the delimiter.
 * <P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), G. Thornwall (SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $   $Revision: 1.9 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */
public class ParseTable
{ 
  /* [TODO] PFL - rewrite using StringBuffer for speedup and saving memory. */
  
  /** Input working string */
  static String 
    str;
  /** string to return if see a null input string */
  static String                      
    nullStr;
  /** delimiter character */
  static private int
    delim; 
  /** Done parsing flag */                   
  static private boolean
    doneFlag;
  /** token assembly buf - alloc once */
  static private char 
    tokBuf[]= new char[10000];  
  
  
  /**
   * ParseTable() - constructor to create empty unnamed table
   * @param t is delimiter between fields and data in rows
   */
  public ParseTable(int t)
  { /* ParseTable */
    delim= t;
    nullStr= new String("");
  } /* ParseTable */
  
  /**
   * ParseTable() - constructor to create empty named table
   * @param t is delimiter between fields and data in rows
   * @param ns is value to use for "null string"
   */
  public ParseTable(int t, String ns)
  { /* ParseTable */
    delim= t;
    nullStr= new String(ns);
  } /* ParseTable */
  
  
  /**
   * getAllDelimTokens() - get array of all delimited tokens into tokArray[].
   * If there is no more data, it will leave those cells in
   *  tokArray[0:tokArray.length-1] set to "".
   * @param line is the source string
   * @param tokArray is the array to return the data
   * @param rmvTrailingWhiteSpaceFlag if trailing white space is to be removed
   * @return number of columns found else 0
   */
  public static int getAllDelimTokens(String line, String tokArray[],
  boolean rmvTrailingWhiteSpaceFlag)
  { /* getAllDelimTokens */
    str= line;
    
    doneFlag= (line==null || line.length()==0);
    if(doneFlag)
      return(0);                     /* no data */
    String token;                    /* token being assembled */
    char
      ch,                            /* current character */
      lineBuf[]= line.toCharArray(); /* cvt input string to char[]*/
    int
      nFound= 0,                     /* # tokens found */
      bufSize= line.length(),        /* size of input buffer */
      bufCtr= 0,                     /* working input buffer index */
      nCols= tokArray.length;        /* max # of columns to test */
    
    /* Parse data from line buffer into tokens */
    for(int c=0; c<nCols; c++)
    { /* get and store next token*/
      int
        lastNonSpaceTokCtr= 0,       /* idx of last non-space char*/
        tokCtr= 0;                   /* size of tokBuf */
      if(bufCtr>=bufSize)
        doneFlag= true;
      
      while(bufCtr<bufSize && lineBuf[bufCtr]!=delim)
      { /* build token*/
        ch= lineBuf[bufCtr++];
        if(ch=='\r' || ch=='\n' || ch=='\0')
          doneFlag= true;         /* break on LF or CR */
        else if(ch=='"')
          continue;               /* ignore double quote */
        else
        { /* track total string len and last non-space char */
          tokBuf[tokCtr++]= ch;
          if(ch!=' ')
            lastNonSpaceTokCtr= tokCtr;  /* saves doing trim */
        }
      } /* build token */
      
      if(tokCtr==0 && doneFlag)
        break;                     /* no token */
      
      if(tokCtr>0)
      { /* use this token */
        token= new String(tokBuf,0,tokCtr); /* cvt char[] to string */
        
        /* get just string we want with no trailing whitespace */
        if(rmvTrailingWhiteSpaceFlag)
          token= token.substring(0,lastNonSpaceTokCtr);
      } /* use this token */
      else
        token= "";                   /* no more data */
      
      if(bufCtr>bufSize)
        token= null;                 /* don't save "" if beyond list */
      
      tokArray[nFound++]= token;     /* i.e. save token */
      
      if(bufCtr<bufSize && lineBuf[bufCtr]==delim)
        bufCtr++;		         /* move past TAB */
    } /* get and store field names*/
    
    return(nFound);
  } /* getAllDelimTokens */
  
  
  /**
   * getDelimTokens() - get array of delimited tokens into tokArray[].
   * The number of expected columns is specified and if there is no
   * more data, it will leave those cells tokArray[0:useTokFlag.length-1]
   * set to null. If useTokFlag[] is null, then return all tokens.
   * @param line is the source string
   * @param useTokFlag is a list of tokens to get. Set un-needed tokens false to
   *        speedup or all if this is null
   * @param tokArray is the array of returned data
   * @param rmvTrailingWhiteSpaceFlag to remove trailing white space
   * @param missingDataStr to use if missing data
   * @return number of columns found else 0
   */
  public static int getDelimTokens(String line, boolean useTokFlag[],
                                   String tokArray[],
  boolean rmvTrailingWhiteSpaceFlag,
  String missingDataStr)
  { /* getDelimTokens */
    str= line;
    doneFlag= (line==null || line.length()==0);
    if(doneFlag)
      return(0);                     /* no data */
    String token;                    /* token being assembled */
    char
      ch,                            /* current character */
      lineBuf[]= line.toCharArray(); /* cvt input string to char[]*/
    int
      nFound= 0,                     /* # tokens found */
      bufSize= line.length(),        /* size of input buffer */
      bufCtr= 0,                     /* working input buffer index */
      nCols= (useTokFlag==null)
               ? 1000
               : useTokFlag.length;  /* max # of columns to test */
               
    /* Parse data from line buffer into tokens */
    for(int c=0; c<nCols; c++)
    { /* get and store next token*/
      int
        lastNonSpaceTokCtr= 0,       /* idx of last non-space char*/
        tokCtr= 0;                   /* size of tokBuf */
      if(bufCtr>=bufSize)
        doneFlag= true;
      
      while(bufCtr<bufSize && lineBuf[bufCtr]!=delim)
      { /* build token*/
        ch= lineBuf[bufCtr++];
        if(ch=='\r' || ch=='\n' || ch=='\0')
          doneFlag= true;         /* break on LF or CR */
        else if(ch=='"')
          continue;               /* ignore double quote */
        else
        { /* track total string len and last non-space char */
          tokBuf[tokCtr++]= ch;
          if(ch!=' ')
            lastNonSpaceTokCtr= tokCtr;  /* saves doing trim */
        }
      } /* build token */
      
      //if(tokCtr==0 && doneFlag)
      //  break;                     /* no token */
      
      if(tokCtr>0 && (useTokFlag==null || useTokFlag[nFound]))
      { /* use this token */
        token= new String(tokBuf,0,tokCtr); /* cvt char[] to string */
        
        /* get just string we want with no trailing whitespace */
        if(rmvTrailingWhiteSpaceFlag)
          token= token.substring(0,lastNonSpaceTokCtr);
      } /* use this token */
      else
        token= missingDataStr;       /* no more data */
      
      if(bufCtr>bufSize)
        token= null;                 /* don't save "" if beyond list */      
      tokArray[nFound++]= token;     /* i.e. save token */
      
      if(doneFlag)
        break;                       /* no more data */
      
      if(bufCtr<bufSize && lineBuf[bufCtr]==delim)
        bufCtr++;		         /* move past TAB */
    } /* get and store field names*/
    
    return(nFound);
  } /* getDelimTokens */
  
  
  /**
   * countTokens() - count tokens in the string
   * @param line is string to analyze
   * @return number of tokens in the string
   */
  public int countTokens(String line)
  { /* countTokens */
    str= line;
    doneFlag= (line.length()==0);
    int
      c= 0,
      p;
    String s= str;
    
    if (! doneFlag)
      c++;
    while (s.length() >0)
    {
      if ((p= s.indexOf(delim)) > -1)
      {
        c++;
        s= new String(s.substring(p+1));
      }
      else
        s= new String("");
    }
    return(c);
  } /* countTokens */
  
  
  /**
   * hasMoreTokens() - return true if more tokens in string
   * @return true if more tokens to read
   */
  public boolean hasMoreTokens()
  { /* hasMoreTokens */
    return(!doneFlag);
  } /* hasMoreTokens */
  
  
  /**
   * nextToken() - get and return next token.
   * @return "" if no more tokens and set done flag to true.
   */
  public String nextToken()
  { /* nextToken */
    String  r;
    int p;
    
    if ((p= str.indexOf(delim)) > -1)
    {
      r= str.substring(0,p);
      str= new String(str.substring(p+1));
    }
    else
    {
      r= new String(str);
      str= new String("");
      doneFlag= true;
    }
    if (r.length()==0)
      r= new String(nullStr);
    
    if ((str.length()==0 && r.length() > 0) &&
    (r.substring(r.length()-1).equals("\n")))
      r= new String(r.substring(0,r.length()-1));
    return(r);
  }  /* nextToken */
  
  
  /**
   * getString() - get current string str
   * @return current string
   */
  public String getString()
  { /* getString */
    return(str);
  } /* getString */
  
} /* end of class ParseTable */

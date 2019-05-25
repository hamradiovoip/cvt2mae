/** File: UtilCM.java */

package cvt2mae;

import java.awt.*;
import java.awt.event.*;
import java.awt.List;
import java.io.*;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;

/** 
 * Collection of utility functions used by various other classes.
 *<P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), G. Thornwall (SAIC), B. Stephens(SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $   $Revision: 1.10 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class UtilCM 
{
  /** Contents of last status messages */
  public TextField
    statusText= null;
  /** */
  public TextField
    status2Text= null;
  /** */
  public TextField
    status3Text= null;
  /** output and error text frames */
  public TextFrame
    out= null;
  /** error msg text frame */
  public TextFrame
    err= null;
  /** Local debugging flag. If set, then logMsg() output is also
   * printed to System.out */
  public static boolean 
    dbugFlag= false;
  
  
  /**
   * UtilCM() - constructor for setup utility pkg
   * @param dbugFlag
   */
  public UtilCM(boolean dbugFlag)
  { /* UtilCM */
    this.dbugFlag= dbugFlag;
  } /* UtilCM */
  
  
  /**
   * setStatusTextFrames() - set status text frames
   * @param statusText is text field 1
   * @param statusText is text field 2
   * @param statusText is text field 3
   */
  public void setStatusTextFrames(TextField statusText,
                                  TextField status2Text,
                                  TextField status3Text)
  { /* setStatusTextFrames */
    this.statusText= statusText;
    this.status2Text= status2Text;
    this.status3Text= status3Text;
  } /* setStatusTextFrames */
  
  
  /**
   * setOutErrTextFrames() - set out and err text frames
   * @param out is text output field
   * @param err is text error output field
   */
  public void setOutErrTextFrames(TextFrame out, TextFrame err)
  { /* setOutErrTextFrames */
    this.out= out;
    this.err= err;
  } /* setOutErrTextFrames */
  
  
  /**
   * logMsg() - log the message in statusText and log file window
   * @param msg is message to log
   * @param color to draw the message
   */
  public void logMsg(String msg, Color color)
  { /* logMsg */
    logMsg(msg, false, color);
  } /* logMsg */
  
  
  /**
   * logMsg() - log the message in statusText and log file window
   * drawing it in Color.black.
   * @param msg is message to log
   */
  public void logMsg(String msg)
  { /* logMsg */
    logMsg(msg, false, Color.black);
  } /* logMsg */
  
  
  /**
   * logMsg() - log the message in statusText and log file window
   * @param msg is message to log
   * @param isVisibleFlag to make log popup visible/invisible
   * @param color to draw the message
   * @see TextFrame#appendLog
   */
  public void logMsg(String msg, boolean isVisibleFlag, Color color)
  { /* logMsg */
    if(statusText!=null)
    {
      statusText.setText(msg);
      if(color!=null)
        statusText.setForeground(color);
      else
        statusText.setForeground(Color.red);
    }
    
    if(out!=null)
    {
      if(isVisibleFlag)
        out.appendLog(msg,isVisibleFlag);
      else
        out.appendLog(msg);
    }
    
    if(dbugFlag)
      System.out.println(msg);
  } /* logMsg */
  
  
  /**
   * logMsg2() - log the message in status2Text and log file window
   * @param msg is message to log
   * @param color to draw the message
   */
  public void logMsg2(String msg, Color color)
  { /* logMsg2 */
    logMsg2(msg, false, color);
  } /* logMsg2 */
  
  
  /**
   * logMsg2() - log the message in status2Text and log file window
   * @param msg is message to log
   * @param isVisibleFlag to make log popup visible/invisible
   * @param color to draw the message
   * @see TextFrame#appendLog
   */
  public void logMsg2(String msg, boolean isVisibleFlag, Color color)
  { /* logMsg2 */
    if(status2Text!=null)
    {
      status2Text.setText(msg);
      if(color!=null)
        status2Text.setForeground(color);
      else
        status2Text.setForeground(Color.red);
    }
    
    if(out!=null)
    {
      if(isVisibleFlag)
        out.appendLog(msg,isVisibleFlag);
      else
        out.appendLog(msg);
    }
    
    if(dbugFlag)
      System.out.println(msg);
  } /* logMsg2 */
  
  
  /**
   * logMsg3() - log the message in status3Text and log file window
   * @param msg is message to log
   * @param color to draw the message
   */
  public void logMsg3(String msg, Color color)
  { /* logMsg3 */
    logMsg3(msg, false, color);
  } /* logMsg3 */
  
  
  /**
   * logMsg3() - log the message in status3Text and log file window
   * @param msg is message to log
   * @param isVisibleFlag to make log popup visible/invisible
   * @param color to draw the message
   * @see TextFrame#appendLog
   */
  public void logMsg3(String msg, boolean isVisibleFlag, Color color)
  { /* logMsg3 */
    if(status3Text!=null)
    {
      status3Text.setText(msg);
      if(color!=null)
        status3Text.setForeground(color);
      else
        status3Text.setForeground(Color.red);
    }
    
    if(out!=null)
    {
      if(isVisibleFlag)
        out.appendLog(msg,isVisibleFlag);
      else
        out.appendLog(msg);
    }
    
    if(dbugFlag)
      System.out.println(msg);
  } /* logMsg3 */
  
  
  /**
   * dateStr() - return a new Date string of the current day and time
   * @return date string
   */
  public static String dateStr()
  { /* dateStr */
    Date dateObj= new Date();
    String date= dateObj.toString();
    
    return(date);
  } /* dateStr */
  
  
  /**
   * timeStr() - return a new daytime HH:MM:SS string of the current time
   * @return time of day string
   */
  public static String timeStr()
  { /* timeStr */
    Calendar cal= Calendar.getInstance();
    int
      hrs= cal.get(Calendar.HOUR_OF_DAY),
      mins= cal.get(Calendar.MINUTE),
      secs= cal.get(Calendar.SECOND);
    String dayTime= hrs+":"+mins+":"+secs;
    
    return(dayTime);
  } /* timeStr */
  
  
  /**
   * mapComma2Tab() - map "," to "\t" in the string
   * @param s input string to map
   * @return mapped string
   */
  public static String mapComma2Tab(String s)
  { /* mapComma2Tab */
    if(s==null)
      return(null);
    String sR= "";
    char
      ch,                          /* current character */
      sBuf[]= s.toCharArray();     /* cvt input string to char[]*/
    int
      sSize= s.length(),            /* size of input buffer */
      sCtr= 0;                      /* working input buffer index */
    while(sCtr<sSize)
    { /* build token*/
      ch= sBuf[sCtr++];
      if(ch==',')
        ch= '\t';
      sR += (""+ch);
    }
    return(sR);
  } /* mapComma2Tab */
  
  
  /**
   * cvb2s() - convert boolean String
   * @param b is boolean input to convert
   * @return output string as either "true" or "false"
   */
  public static String cvb2s(boolean b)
  { /* cvb2s */
    if(b)
      return("true");
    else
      return("false");
  } /* cvb2s */
  
  
  /**
   * cvs2f() - convert String to float
   * @param str containing floating point number to convert to float
   * @return converted number else defaultFloat if illegal number.
   */
  public static float cvs2f(String str, float defaultFloat)
  { /* cvs2f */
    if(str==null)
      return(defaultFloat);    
    float f= defaultFloat;
    
    if(str != null)
    {
      try
      {
        Float F= new Float(str);
        f= F.floatValue();
      }
      catch(NumberFormatException e)
      {f= defaultFloat;}
      return(f);
    }
    
    return(f);
  } /* cvs2f */
  
  
  /**
   * cvs2l() - convert String to long
   * @param str containing integer to convert to long
   * @return converted number else defaultLong if illegal number.
   */
  public static long cvs2l(String str, long defaultLong)
  { /* cvs2l */
    if(str==null)
      return(defaultLong);    
    long lV= defaultLong;
    
    if(str != null)
    {
      try
      {
        Long L= new Long(str);
        lV= L.longValue();
      }
      catch(NumberFormatException e)
      {lV= defaultLong;}
      return(lV);
    }
    
    return(lV);
  } /* cvs2l */
  
  
  /**
   * cvs2i() - convert String to int
   * @param str containing integer to convert to int
   * @return converted number else defaultInt if illegal number.
   */
  public static int cvs2i(String str, int defaultInt)
  { /* cvs2i */
    if(str==null)
      return(defaultInt);
    int i;
    try
    {
      i= java.lang.Integer.parseInt(str);
    }
    catch(NumberFormatException e)
    {i= defaultInt;}
    
    return(i);
  } /* cvs2i */
  
  
  /**
   * cvs2b() - convert String to boolean
   * @param str containing either "true" or "false"
   * @return converted boolean else false if illegal boolean name.
   */
  public static boolean cvs2b(String str, boolean defaultBool)
  { /* cvs2i */
    if(str==null)
      return(defaultBool);
    boolean b= defaultBool;
    
    if(str.equals("TRUE") || str.equals("true") ||
    str.equals("T") || str.equals("t") || str.equals("1"))
      b= true;
    return(b);
  } /* cvs2b */
  
  
  /**
   * mapIllegalChars() - map characters to '_'
   * i.e. ' ', '\t', ':', ';', '\"', ',', '\'' '@', '*', '=' to '_'
   * @param str to map
   * @return String mapped
   */
  static String mapIllegalChars(String str)
  { /* mapIllegalChars */
    String sR= str;
    char
      ch,
      cBuf[]= new char[str.length()];
    
    for(int i= str.length()-1; i>=0;i--)
    {
      ch= str.charAt(i);
      if(ch==' ' ||ch=='\t' ||  ch==':' || ch==';' || ch=='\"' ||
      ch==',' ||  ch=='@' || ch=='*' ||  ch=='=' || ch=='\'')
        ch= '_';
      
      cBuf[i]= ch;
    }
    
    sR= new String(cBuf);
    return(sR);
  } /* mapIllegalChars */
  
  
  /**
   * mapSpaceToMinus() - map " " to "-"
   * @param str to map
   * @return mapped string
   */
  public static String mapSpaceToMinus(String str)
  { /* mapSpaceToMinus */
    if(str==null)
      return(null);
    String sR= "";
    char
      ch,                          /* current character */
      sBuf[]= str.toCharArray();   /* cvt input string to char[]*/
    int
      sSize= str.length(),          /* size of input buffer */
      sCtr= 0;                      /* working input buffer index */
    while(sCtr<sSize)
    { /* build token*/
      ch= sBuf[sCtr++];
      if(ch==' ')
        ch= '-';
      sR += (""+ch);
    }
    return(sR);
  } /* mapSpaceToMinus */
  
  
  /**
   * delFile() - Delete specifile file specified with full path.
   * @param fullFileName of file to delete
   */
  public boolean delFile(String fullFileName)
  { /* delFile */
    try
    {
      File f= new File(fullFileName);
      if(f!=null)
        f.delete();
    }
    catch (Exception e)
    {
      logMsg("Can't delete file ["+fullFileName+"]",Color.red);
      return(false);
    }
    
    return(true);
  } /* delFile */
  
  
  /**
   * removeQuotes() - remove '\"' characters from line
   * @param line to check and edit out quotes
   */
  public static String removeQuotes(String line)
  { /* removeQuotes */
    if(line.indexOf('\"')==-1)
      return(line);   /* there are no embedded quotes to remove */
    
    /* copy all chars but quotes */
    int
      sSize= line.length(),            /* size of input buffer */
      sCtr= 0,                      /* working input buffer index */
      oCnt= 0;
    char
      ch,                          /* current character */
      sInBuf[]= line.toCharArray(),    /* cvt input string to char[]*/
      sOutBuf[]= new char[sSize];
    while(sCtr<sSize)
    { /* build token*/
      ch= sInBuf[sCtr++];
      if(ch!='\"')
        sOutBuf[oCnt++]= ch;
    }
    
    line= new String(sOutBuf,0,oCnt);  /* cvt char[] to string */
    return(line);
  } /* removeQuotes */
  
  
  /**
   * copyFile() - binary copy of one file or URL toa local file
   * @param srcName is either a full path local file name or
   *        a http:// prefixed URL string of the source file.
   * @param dstName is the full path of the local destination file name
   * @param optUpdateMsg (opt) will display message in showMsg() and
   *        increasing ... in showMsg2(). One '.' for every 10K bytes read.
   *        This only is used when reading a URL. Set to null if not used.
   * @param optEstInputFileLth is the estimate size of the in;ut file if known
   *        else 0. Used in progress bar.
   * @return true if succeed.
   */
  public boolean copyFile(String srcName, String dstName,
                          String optUpdateMsg, int optEstInputFileLth)
  { /* copyFile */
    try
    { /* copy data from input to output file */
      FileOutputStream dstFOS= new FileOutputStream(new File(dstName));
      FileInputStream srcFIS= null;
      int
        bufSize= 20000,
        nBytesRead= 0,
        nBytesWritten= 0;
      byte buf[]= new byte[bufSize];
      
      boolean isURL= (srcName.startsWith("http://"));
      if(isURL)
      { /* Copy the file from Web site */
        if(optUpdateMsg!=null)
          logMsg(optUpdateMsg, Color.red);
        String sDots= "";
        URL url= new URL(srcName);
        InputStream urlIS= url.openStream();
        while(true)
        { /* read-write loop */
          if(optUpdateMsg!=null)
          { /* show progress every read */
            sDots += ".";
            String
              sPct= (optEstInputFileLth>0)
                       ? ((int)((100*nBytesRead)/optEstInputFileLth))+"% "
                       : "",
              sProgress= "Copying " + sPct + sDots;
            logMsg2(sProgress, Color.red);
          }
          nBytesRead= urlIS.read(buf);
          if(nBytesRead==-1)
            break;         /* end of data */
          else
          {
            dstFOS.write(buf,0,nBytesRead);
            nBytesWritten += nBytesRead;
          }
        } /* read-write loop */
        dstFOS.close();
        if(optUpdateMsg!=null)
        {
          logMsg("", Color.black);
          logMsg2("", Color.black);
        }
      }
      else
      { /* copy the file on the local file system */
        srcFIS= new FileInputStream(new File(srcName));
        while(true)
        { /* read-write loop */
          nBytesRead= srcFIS.read(buf);
          if(nBytesRead==-1)
            break;         /* end of data */
          else
          {
            dstFOS.write(buf,0,nBytesRead);
            nBytesWritten += nBytesRead;
          }
        } /* read-write loop */
        srcFIS.close();
        dstFOS.close();
      } /* copy the file on the local file system */
    } /* copy data from input to output file */
    
    catch(Exception e1)
    { /* just fail if any problems at all! */
      return(false);
    }
    
    return(true);
  } /* copyFile */
  
  
  /**
   * readBytesFromURL() - read binary data from URL 
   * @param srcName is either a full path local file name or 
   *        a http:// prefixed URL string of the source file.
   * @param optUpdateMsg (opt) will display message in showMsg() and 
   *        increasing ... in showMsg2(). One '.' for every 10K bytes read.
   *        This only is used when reading a URL. Set to null if not used.
   * @return a byte[] if succeed, else null.
   */
  public byte[] readBytesFromURL(String srcName, String optUpdateMsg)
  { /* readBytesFromURL */
     if(!srcName.startsWith("http://"))
       return(null);
     int
        bufSize= 20000,
        nBytesRead= 0,
        nBytesWritten= 0,
        oByteSize= bufSize;
     byte
       buf[]= null,
       oBuf[]= null;      
    
    try
    { /* copy data from input to output file */
      buf= new byte[bufSize];
      oBuf= new byte[bufSize];
      
      /* Copy the file from Web site */
      if(optUpdateMsg!=null)
        logMsg(optUpdateMsg);
      String sDots= "";
      URL url= new URL(srcName);
      InputStream urlIS= url.openStream();
      while(true)
      { /* read-write loop */
        if(optUpdateMsg!=null)
        { /* show progress every read */
          sDots += ".";
          String sProgress= "Reading " + sDots;
          logMsg2(sProgress, Color.red); 
        }     
        
        nBytesRead= urlIS.read(buf);
        if(nBytesRead==-1)
          break;         /* end of data */
        else
        { /* copy buf to end of oBuf */
          if(nBytesRead+nBytesWritten > oByteSize)
          { /* regrow oBuf */
            byte tmp[]= new byte[oByteSize+bufSize];
            for(int i=0;i<nBytesWritten;i++)
              tmp[i]= oBuf[i];
            oBuf= tmp;
            oByteSize += bufSize;
          }
           for(int i=0;i<nBytesRead;i++)
              oBuf[nBytesWritten++]= buf[i];
          nBytesWritten += nBytesRead;  /* append bytes to end of list */
          }
        } /* read-write loop */
        
        /* shrink oBuf to exact size needed */
        byte tmp[]= new byte[nBytesWritten];
        for(int i=0;i<nBytesWritten;i++)
          tmp[i]= oBuf[i];
        oBuf= tmp;
              
        if(optUpdateMsg!=null)
        {
          logMsg("",Color.black);
          logMsg2("",Color.black);
        }      
    } /* copy data from input to output file */
    
    catch(Exception e1)
    { /* just fail if any problems at all! */
     return(null);
    }
    
    return(oBuf);
  } /* readBytesFromURL */
  
    
  /**
   * deleteLocalFile() - delete local file.
   * @return false if failed.
   */
  public boolean deleteLocalFile(String fileName)
  { /* deleteLocalFile */
    try
    {
      File srcF= new File(fileName);
      if(srcF.exists())
        srcF.delete();      /* delete it first */
    }
    catch(Exception e)
    { return(false); }
    
    return(true);
  } /* deleteLocalFile */
  
  
  /**
   * updateCvt2MaeJarFile() - update MAExplorer.jar into program install area.
   *<PRE>
   * [1] Define directory for MAExplorer.jar path and other file and URL names.
   * [2] Backup the old MAExplorer.jar as MAExplorer.jar.bkup
   * [3] Open the url: from maeJarURL. This is hardwired to be
   *         "http://maexplorer.sourceforge.net/Cvt2Mae/Cvt2Mae.jar"
   *     and read the file from the Web into local file "Cvt2Mae.jar.tmp"
   * [4] Move the "MAExplorer.jar.tmp" file into "Cvt2Mae.jar" in the program directory
   *
   * Since changing the MAExplorer.jar file is a potential security risk,
   * we make this procedure final and hardwire the maeJarURL!
   *</PRE>
   * @return true if succeed
   * @see #copyFile
   * @see #deleteLocalFile
   */
  final boolean updateCvt2MaeJarFile()
  { /* updateCvt2MaeJarFile */
    /* [1] Define directory for MAExplorer.jar path and other file
     * and URL names.
     */
    String
      fileSep= System.getProperty("file.separator"),
      userDir= System.getProperty("user.dir")+fileSep,
      localC2MjarFile= userDir + "Cvt2Mae.jar",
      localC2MjarFileBkup= userDir + "Cvt2Mae.jar.bkup",
      localC2MjarFileTmp= userDir + "Cvt2Mae.jar.tmp",
      c2mJarURL= "http://maexplorer.sourceforge.net/Cvt2Mae/Cvt2Mae.jar",
      c2mJarServer= "maexplorer.sourceforge.net";
    
    /* [2] Backup the old Cvt2Mae.jar as Cvt2Mae.jar.bkup if it exists
     * (it won't if you are running from the debugger!).
     * But first, delete old backup if it exists.
     */
    deleteLocalFile(localC2MjarFileBkup);
    copyFile(localC2MjarFile, localC2MjarFileBkup, null,0);
    
    /* [3] Open the url: c2mJarURL and read the file from the Web into
     * "Cvt2Mae.jar.tmp"
     */
    String updateMsg= "Updating your Cvt2Mae.jar file from "+c2mJarServer+
                      " server.";
    File f= new File(localC2MjarFileBkup);
    int estInputFileLth= (f!=null) ? (int)f.length() : 0;
    if(! copyFile(c2mJarURL,localC2MjarFileTmp,updateMsg,estInputFileLth))
      return(false);
    
    /* [4] Move the "Cvt2Mae.jar.tmp" file into  "Cvt2Mae.jar" in the
     * program directory where it was installed.
     */
    if(! deleteLocalFile(localC2MjarFile))
      return(false);
    if(! copyFile(localC2MjarFileTmp, localC2MjarFile, null,0))
      return(false);
    
    return(true);
  } /* updateCvt2MaeJarFile */
  
  
  /**
   * updateCvt2MaeProgram() - verify that want to update the Cvt2Mae.jar file
   * from the maexplorer.sourceforge.net server.
   */
  boolean updateCvt2MaeProgram()
  { /* updateCvt2MaeProgram */
    String
      c2mJarURL= "http://maexplorer.sourceforge.net/Cvt2Mae/Cvt2Mae.jar",
      c2mJarServer= "maexplorer.sourceforge.net";
    String
      c2mVersionStr= "",
       c2mVersionURL= "http://maexplorer.sourceforge.net/Cvt2Mae/C2MJarVersion.txt";       
    byte bVersion[]= readBytesFromURL(c2mVersionURL, null);
    if(bVersion!=null)        
    { /* display the current version # on the server if found */
      String s= new String(bVersion);
      int idxNULL= s.indexOf('\0');
      if(idxNULL!=-1)
        s= s.substring(0,idxNULL);
      int 
        idxLF= s.indexOf('\r'),
        idxCR= s.indexOf('\n'),          
        idx= (idxLF!=-1 && idxCR!=-1)
                ? Math.min(idxLF,idxCR)
                : Math.max(idxLF,idxCR); 
                   
      c2mVersionStr= "[V." + ((idx==-1) ? s : s.substring(0,idx)) + "] ";   
    }
    PopupDialog
      pd =new PopupDialog("Update Cvt2Mae.jar  "+c2mVersionStr+
                          " from "+c2mJarServer+
                          " Web site - are you sure?", false);        
     if(!pd.answer)
       return(false);
     else
    { /* do it */
      boolean flag= updateCvt2MaeJarFile();
      if(!flag)
      {
        logMsg2("FAILED! Unable to update Cvt2Mae.jar file from "+c2mJarServer+".",
                Color.red );
        logMsg3("Make sure you are connected to the Internet and the "+
                c2mJarServer+" server is up.", Color.red);
        return(false);
      }
      else
      {
        logMsg2("Finished updating new Cvt2Mae.jar file from "+c2mJarServer+".",
                Color.blue);
        logMsg3("You must exit and restart Cvt2Mae to use the new version.",
                Color.blue);
        return(true);
      }
    } /* do it */
  } /* updateCvt2MaeProgram */
    
    
}  /* end of class UtilCM */

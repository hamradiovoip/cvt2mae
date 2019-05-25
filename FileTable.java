/** File: FileTable.java */

package cvt2mae;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;

/**
 * This class reads data from files and writes data to files.
 * It also reads tab-delimited data from a file into a Table 
 *    (tRows,tCols,tFields,tData).
 * It can be used to create an empty table (not associated with a file).
 * <P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), G. Thornwall (SAIC), B. Stephens(SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $   $Revision: 1.9 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class FileTable 
{    
  /** Local debugging switch */
  private static boolean
    DBUG_FILE_TABLE= false;
  
  /** number of lines found in file */
  public static int
    estLinesInFile;       
  /** estimated row number where Samples are listed */
  public static int
    estSamplesRowNbr;      
  /** estimated row number where Fields are listed */
  public static int
    estFieldsRowNbr;       
  /** estimated number of Fields */
  public static int
    estMaxNbrField;       
  /** estimated max number Grids found in file */
  public static int
    estMaxGridsInFile;       
  /** estimated max number Rows in file */
  public static int
    estMaxRowsInFile;      
  /** estimated max number Cols in file */
  public static int
    estMaxColsInFile;        
  /** estimated row where data starts */
  public static int
    estRowWithData;  
  /** size of input buffer */
  public int
    bufSize;    
  /** number of columns/row */
  public int
    tCols;		     
  /** number of rows. i.e. number row Clone Id's */
  public int
    tRows;  
  
  /** error message log it not null */
  public String
    errMsgLog;  
  /** file to read or write I/O */
  public String
    fileName;               
  /** Table name */
  public String
    tName;                  
  /** names of table fields */
  public String
    tFields[];
  /** row vectors [0:tRows-1][0:tCols-1] */
  public String
    tData[][];		     
  /** file descriptor for I/O */
  private File
    file;                    
  
  
  /**
   * FileTable() - generic Constructor, set some defaults to 0.
   * @param tableName String name of table
   */
  public FileTable(String tableName)
  { /* FileTable */
    this.tName= tableName;
    tRows= 0;
    tCols= 0;
    errMsgLog= null;
    
    estLinesInFile= 0;          /* number of lines found in file */
    estSamplesRowNbr= 0;        /* estimated row # where Samples are listed */
    estFieldsRowNbr= 0;         /* estimated row # where Fields are listed */
    
    estMaxGridsInFile= 0;       /* estimated max # Grids found in file */
    estMaxRowsInFile= 0;        /* estimated max # Rows in file */
    estMaxColsInFile= 0;        /* estimated max # Cols in file */
  } /* FileTable */
  
  
  /**
   * FileTable() - Constructor to make empty table of known size
   * @param tableName String name of table
   * @param rows max rows in table
   * @param cols max columns in table
   */
  public FileTable(String tableName, int rows, int cols)
  { /* FileTable */
    this.tName= tableName;
    this.tRows= rows;
    this.tCols= cols;
    if(cols>1)
      this.tFields= new String[cols];
    if(rows>1 && cols>1)
      this.tData= new String[rows][cols];
    
    errMsgLog= null;
    estLinesInFile= 0;   /* # of lines found in file */
    estSamplesRowNbr= 0; /* estimated row # where Samples are listed */
    estFieldsRowNbr= 0;  /* estimated row # where Fields are listed */
  } /* FileTable */
  
  
  /**
   * promptFileName() - prompt for file name
   * @param promptDir default dir
   * @param msg message to display in file dialog
   * @return full file path if successfull else null
   */
  public String promptFileName(String promptDir, String msg)
  { /* promptFileName */
    Frame fdFrame= new Frame("FileDialog");
    FileDialog fd= new FileDialog(fdFrame, msg, FileDialog.LOAD);
    
    if(promptDir!=null)
      fd.setDirectory(promptDir);
    
    fd.setVisible(true);
    
    /* It will wait until user presses done as the File Dialog is modal */
    String newFile= fd.getFile();
    if(newFile==null)
      return(null);
    
    promptDir= fd.getDirectory();
    
    String fullPath= promptDir + newFile;
    
    return(fullPath);
  } /* promptFileName */
  
  
  /**
   * checkForBadTable() - verify that it is a well formed table
   * with all field names present and with no duplicate entries.
   * @return true if successful, also stuff error mesasge into
   *         "errMsgLog" to be displayed later.
   */
  public boolean checkForBadTable()
  { /* checkForBadTable */
    if(tRows<0 || tCols<=0)
    {
      errMsgLog += "FIOEM-CFBT bad Table (tRows,tCols)=("+
                   tRows+","+tCols+")";
      return(false);
    }
    
    if(tFields==null || tFields.length!=tCols)
    {
      errMsgLog += "FIOEM-CFBT bad Table tField[] data size";
      return(false);
    }
    
    if(tData==null || tData.length!=tRows || tData[0].length!=tCols)
    {
      errMsgLog += "FIOEM-CFBT bad Table tData[][] data size";
      return(false);
    }
    
    /* Look for errors!!! Empty or duplicate Field name entries */
    for(int c=0;c<tCols;c++)
    { /* check fields */
      if(tFields[c]==null)
      {
        errMsgLog += "FIOEM-CFBT bad Table - empty field #"+c;
        return(false);
      }
      
      for(int c2=0;c2<tCols;c2++)
        if(c!=c2 && tFields[c].equals(tFields[c2]))
        {
          errMsgLog += "FIOEM-RF bad Table dupl. fields: #"+c+
                       " and #"+c2+ " ["+tFields[c]+"]";
          return(false);
        }
    } /* check fields */
    
    return(true);
  } /* checkForBadTable */
  
  
  /**
   * readTableFieldsFromFile() - open file and read row r tab-delim data.
   * @param fileName file name
   * @param r row to read
   * @return rFields is array of fields to return if successful else null.
   */
  public String[] readTableFieldsFromFile(String fileName, int r)
  { /* readTableFieldsFromFile */
    String rawData= null;
    
    /* [1] Read the row r line of data */
    try
    { /* get the row r line of data */
      FileReader  file= new FileReader(fileName);
      BufferedReader buffer= new BufferedReader(file);
      for(int i= 0;i<r;i++)
        rawData= buffer.readLine(); /* read the file to string */
      
      if(rawData==null || rawData.length()==0)
        return(null);
      else
        file.close();
    } /* get the row r line of data */
    
    catch (Exception e)
    {
      return(null);
    }
    
    /* [2] Convert it to char buffer */
    char
      ch,
      inputBuf[]= rawData.toCharArray(),  /* cvt input string to char[] */
      tokBuf[]= new char[1000],           /* token buffer */
      lineBuf[]= new char[10000];         /* line buffer */
    int
      c= 0,		                            /* running count of cols */
      bufSize= rawData.length(),          /* size of input buffer */
      bufCtr= 0,                          /* working input buffer index */
      tokSize= 0,                         /* size of tokBuf */
      lineSize= 0,                        /* size of lineBuf */
      lineCtr= 0;                         /* working line buffer index */
    String
      fields[]= new String[500],          /* more than ever need */
      token;                              /* field delimited token */
    
    /* [3] Parse data from buffer into field and data tables */     
    boolean
      doneFlag= false,
      sawNonSpaceLeadingChar;
    
    /* [3.1] Get a list of the tab-delimited Field names. */
    while(bufCtr<bufSize && !doneFlag)
    { /* get and store field names*/
      tokSize= 0;
      sawNonSpaceLeadingChar= false;
      
      while(bufCtr<bufSize && !doneFlag &&
            (ch= inputBuf[bufCtr])!='\t' && ch!='\n' && ch!='\r')
      { /* build token for field name */
        bufCtr++;
        if(ch=='"')
          continue;
        if(!sawNonSpaceLeadingChar)
        { /* handle leading whiteb space */
          if(ch==' ')
            continue;
          else
            sawNonSpaceLeadingChar= true;
        } /* handle leading white space */
        else if(ch=='\n' || ch=='\r' || ch=='"' || ch=='\0')
        {
          doneFlag= true;
          break;
        }
        tokBuf[tokSize++]= ch;     /* save all non-space leading
         * chars up to TAB or EOL */
      } /* build token for field name */
      
      tokBuf[tokSize]= '\0';          /* terminate token */
      if(bufCtr==bufSize || inputBuf[bufCtr]=='\t')
        bufCtr++;		          /* move past TAB */
      else if(bufCtr==bufSize || inputBuf[bufCtr]=='\n')
      { /* get rid of LF */
        // Possible Error GT what abt \r?
        doneFlag= true;
      }
      
      /* Rmv trailing whitespace by adjusting tokSize downward */
      for(int i=tokSize;i>=0;i--)
      { /* find first non-space character from the end */
        if(tokBuf[i]!=' ')
          break;
        else tokSize--;
      }
      
      token= (tokSize<=0)
                ? "" /* change null to "" */
                : new String(tokBuf,0,tokSize);
      
      fields[c++]= token;     /* i.e. save field name */
    } /* get and store field names*/
    
    /* [4] Copy to new global field list of exactly the right size */
    String rFields[]= new String[c];
    for(int i= 0;i<c; i++)
    {
      rFields[i]= fields[i];
    }
    
    if(DBUG_FILE_TABLE)
    {
      System.out.println("FT-RTFFF fileName="+fileName+
                         "\n rawData='"+rawData+"'");
      for(int i= 0;i<c; i++)
        System.out.println("  fields["+i+"]='"+fields[i]+"'");
      System.out.println("  c="+c+" rFields.length="+rFields.length);
    }
    
    return(rFields);
  } /* readTableFieldsFromFile */
  
  
  /**
   * countLinesInFile() - open file and count # of lines in the file.
   * Set estLinesInFile to number lines found.
   * @param fileName of file to read
   * @return number of lines else 0.
   */
  public static int countLinesInFile(String fileName)
  { /* countLinesInFile */
    String sLine;
    int
      maxTabs= 0,    /* count max # of tabs in case needed - not used */
      nLines= 0;
    
    /* [1] Read the row r line of data */
    estLinesInFile= 0;   /* # of lines found in file */
    
    try
    { /* get the row r line of data */
      FileReader file= new FileReader(fileName);
      BufferedReader buffer= new BufferedReader(file);
      do
      { /* read lines until the end of the file */
        sLine= buffer.readLine();     /* read and toss lines */
        if(sLine!=null)
          nLines++;
      }
      while(sLine!=null);
      
      file.close();
    } /* get the row r line of data */
    
    catch (Exception e)
    { }
    
    estLinesInFile= nLines;               /* copy to state */
    
    return(nLines);
  } /* countLinesInFile */
  
  
  /**
   * countLinesAndTabCountsInFile() - open file and count number of lines & tabs<BR>
   * Set estLinesInFile to # lines found.<BR>
   * Set estSamplesRowNbr to estimated row # where Samples are listed.<BR>
   * Set estFieldsRowNbr to estimated row # where Fields are listed. <BR>
   *<p>
   * Try to estimate row numbers for Samples row and for Field row
   * If lookForSampleTabsFlag is FALSE, it assumes that
   * the first row with initial max number tabs with non-null data
   * is the Fields row, otherwise] it is the Samples row.
   *<p>
   * Note that the Samples row may contain fewer tabs than the Fields
   * row - but it is assumed that both contain more tabs than any
   * other row. When we have satisfied the initial counts, just
   * stop analyzing the tab counts to make it run MUCH MUCH faster.
   *
   * @param cvt instance of Cvt2Mae
   * @param fileName file to count
   * @param lookForSampleTabsFlag if true, look for sample tabs
   * @param msg message for logMsg
   * @param color is the logMsg color
   * @return number of lines in file else 0.
   * @see UtilCM#logMsg
   * @see #countNonNullTabData
   */
  public static int countLinesAndTabCountsInFile(Cvt2Mae cvt, String fileName,
                                                  boolean lookForSampleTabsFlag,
                                                  String msg, Color color)
  { /* countLinesAndTabCountsInFile */
    String rawData;
    int
      maxRowsToCheckForTabs= 50, /* ck min # rows for tabs */
      maxTabs= 0,                /* current maxTabs row */
      maxTabsField= 0,           /* # tabs in Fields row */
      prevMaxTabsSample= 0,      /* # tabs in Samples row */
      rowMaxTabsField= 0,        /* row where Fields is found */
      rowPrevMaxTabsSample= 0,   /* row where Samples is found */
      nMaxTabs= 0,               /* # of rows with current maxTabs */
      nMaxTabsField= 0,          /* # of rows with maxTabsField */
      nPrevMaxTabsSample= 0,     /* # of rows with nextMaxTabsSample */
      nLines= 0;                 /* running line counter */
    
    /* [1] Open file and read the row r line of data */
    estLinesInFile= 0;           /* # of lines found in file */
    estSamplesRowNbr= 0;         /* estimated row # where Samples are listed */
    
    try
    { /* get the row r line of data */
      FileReader file= new FileReader(fileName);
      BufferedReader buffer= new BufferedReader(file);
      
      /* [1.1] get 1 line at a time until eof */
      do
      { /* read lines until the end of the file */
        rawData= buffer.readLine();  /* read and toss lines */
        if(rawData==null)
          continue;                  /* stop since probably at the end */
        
        nLines++;                    /* count lines */
        
        /* [1.2] Add '.' to logmsg; ie poor man's progressbar */
        if((nLines%400)==0)
        {
          msg += ".";
          cvt.util.logMsg(msg, color);
        }
        
        /* [1.3] Look at the first few rows for tabs */
        if(nLines < maxRowsToCheckForTabs)
        { /* check Tab counts */
          int
          nTabs= countNonNullTabData(rawData);
          
         /* [1.3.1] Will capture Field and Sample rows via counters.
          * Following logic captures Field and Sample rows .
          */
          if(maxTabs < nTabs)
          { /* start new counter */
            maxTabs= nTabs;
            nMaxTabs= 1;
          } /* start new counter */
          else
            nMaxTabs++;                /* # of rows with max # tabs */
          
          /* [1.3.2] Found a new maximum tab, keep prev size */
          if(maxTabsField < maxTabs)
          { /* found new maxTabsRow */
            prevMaxTabsSample= maxTabsField; /* copy to NEXT */
            nPrevMaxTabsSample= nMaxTabsField;
            rowPrevMaxTabsSample= rowMaxTabsField; /* Samples row */
            
            maxTabsField= maxTabs;   /* start new counter */
            estMaxNbrField= Math.max(maxTabsField+1,estMaxNbrField);
            nMaxTabsField= 1;
            rowMaxTabsField= nLines; /* capture FIRST Fields row */
          } /* found new maxTabsRow */
          
          else if(maxTabsField==maxTabs)
          { /* same size, increment maxTabsRow */
            nMaxTabsField++;
          } /* same size, increment maxTabsRow */
          
         /*
         if(DBUG_FILE_TABLE)
            System.out.println("\nFT-CLATC.1 rawData='"+rawData+"'"+
                               "\n maxTabsField="+maxTabsField+
                               "\n prevMaxTabsSample="+prevMaxTabsSample+
                               "\n rowMaxTabsField="+rowMaxTabsField+
                               "\n rowPrevMaxTabsSample="+rowPrevMaxTabsSample+
                               "\n nMaxTabs="+nMaxTabs+
                               "\n nMaxTabsField="+nMaxTabsField+
                               "\n nPrevMaxTabsSample="+nPrevMaxTabsSample+
                               "\n nLines="+nLines
                               );
       */
        } /* check Tab counts */
      } /* read lines until the end of the file */
      while(rawData!=null);
      /*
      if(DBUG_FILE_TABLE)
        System.out.println("\nFT-CLATC.2 maxTabsField="+maxTabsField+
                           "\n prevMaxTabsSample="+prevMaxTabsSample+
                           "\n rowMaxTabsField="+rowMaxTabsField+
                           "\n rowPrevMaxTabsSample="+rowPrevMaxTabsSample+
                           "\n nMaxTabs="+nMaxTabs+
                           "\n nMaxTabsField="+nMaxTabsField+
                           "\n nPrevMaxTabsSample="+nPrevMaxTabsSample+
                           "\n nLines="+nLines
                           );
      */
      file.close();
    } /* get the row r line of data */
    
    catch (Exception e)
    {
      return(0);                  /* failed */
    }
    
    /* [2] Set global state variables */
    estLinesInFile= nLines;      /* copy to state */
    if(lookForSampleTabsFlag)
    { /* compute Samples row */
      if(prevMaxTabsSample!=0)
        estSamplesRowNbr= rowPrevMaxTabsSample;
      else
      { /* impute it */
        estSamplesRowNbr= rowMaxTabsField;
        rowMaxTabsField++;   /* assign Fields to NEXT line */
      }
    } /* compute Samples row */
    estFieldsRowNbr= rowMaxTabsField;
    estRowWithData= rowMaxTabsField+1;
    /*
    if(DBUG_FILE_TABLE)
      System.out.println("\nFT-CLATC.3 estSamplesRowNbr="+estSamplesRowNbr+
                         " estFieldsRowNbr="+estFieldsRowNbr);
     */
    
    return(nLines);
  } /* countLinesAndTabCountsInFile */
  
  
  /**
   * findMaxGridRowColInFile() - open file and find Max Grid Row Col in file
   * This assumes that ("grid, grid_row, and grid_col") are in the FieldMap.
   * then set
   *<PRE>
   *    estMaxGridsInFile   as estimated max # Grids found in file
   *    estMaxRowsInFile    as estimated max # Rows in file
   *    estMaxColsInFile    as estimated max # Cols in file
   *</PRE>
   * @param cvt instance of Cvt2Mae
   * @param fileName file to count
   * @param msg message for logMsg
   * @param color logMsg color
   * @param rowNbrDataStarts row that data starts
   * @return true if successful
   * @see FileTable
   * @see FileTable#readTableFieldsFromFile
   * @see ParseTable
   * @see ParseTable#getDelimTokens
   * @see UtilCM#cvs2i
   * @see UtilCM#logMsg
   * @see #countNonNullTabData
   */
  public static boolean findMaxGridRowColInFile(Cvt2Mae cvt,
                                               String fileName,
                                               String msg,
                                               Color color,
                                               int rowWithFields,
                                               int rowNbrDataStarts)
  { /* findMaxGridRowColInFile */
    String
      sGrid, /* store the string values to be converted */
      sRow,  /* store the string values to be converted */
      sCol,  /* store the string values to be converted */
      tokArray[],
      line;
    MaeConfigData mcd= cvt.mcd;
    ParseTable ptGQ= new ParseTable('\t');
    FileTable fioQ= new FileTable("GetQuantInputFields");
    String fieldQ[]= fioQ.readTableFieldsFromFile(fileName, rowWithFields);    
    if(fieldQ==null)
    {
      cvt.util.logMsg("Error: unable to read ["+fileName+"]", Color.red);
      return(false);
    }
    
    /* first test if ("grid, grid_row, and grid_col") */
    int
      idxGrid,
      idxGridRow,
      idxGridCol,
      nFound,
      grid,
      row,
      col,
      nDataFieldsQ= fieldQ.length,
      nLines= 0,                 /* running line counter */
      gridNamesLen= 8,
      rowNamesLen= 5,
      colNamesLen= 8;
    String
      gridNames[]= {"block","Block","BLOCK","grid","Grid","GRID",
                    "Location","PlateID"},
       rowNames[]= {"row","Row","ROW","PlateRow","grid row"},
       colNames[]= {"col","Col","Column","COLUMN","PlateCol","COL", 
                   "grid column","Grid Column"};    
    
    /* [1] Test the various combinations of (G,R,C) in FieldMap. */
    idxGridCol= -1;
    idxGrid= -1;
    idxGridRow= -1;
    
    for(int g=0;g<gridNamesLen; g++)
    {
      idxGrid= FileTable.lookupSubstringFieldIdx(fieldQ, gridNames[g]);
      if(idxGrid>=0)
        break; /* found */
    }
    
    for(int r=0; r<rowNamesLen; r++)
    {
      idxGridRow= FileTable.lookupSubstringFieldIdx(fieldQ,rowNames[r]);
      if(idxGridRow>=0)
        break; /* found */
    }
    
    for(int c=0; c<colNamesLen; c++)
    {
      idxGridCol= FileTable.lookupSubstringFieldIdx(fieldQ,colNames[c]);
      if(idxGridCol>=0)
        break; /* found */
    }
    if(idxGrid==-1 || idxGridRow==-1 || idxGridCol==-1)
    {
      
      return(false); /* none found */
    }
    
    /* [2] Create line parsing arrays */
    tokArray= new String[nDataFieldsQ];
    boolean
     useTokFlag[]= new boolean[nDataFieldsQ];
    useTokFlag[idxGrid]= true;
    useTokFlag[idxGridRow]= true;
    useTokFlag[idxGridCol]= true;
    
    estMaxGridsInFile= 0;       /* estimated max # Grids found in file */
    estMaxRowsInFile= 0;        /* estimated max # Rows in file */
    estMaxColsInFile= 0;        /* estimated  max # Cols in file */
    
    /* [3] Read through the file but just check for (G,R,C) */
    try
    { /* get the row r line of data */
      FileReader file= new FileReader(fileName);
      BufferedReader buffer= new BufferedReader(file);
      
      /* [3.1] get 1 line at a time until eof */
      do
      { /* read lines until the end of the file */
        line= buffer.readLine();  /* read and toss lines */
        if(line==null)
          continue;             /* stop since probably at the end */
        nLines++;           /* count lines */
        
        if(nLines>=rowNbrDataStarts)
        { /* do not read fields */       
          /* [3.2] Add '.' to logmsg; ie poor man's progressbar */
          if((nLines%400)==0)
          {
            msg += ".";
            cvt.util.logMsg(msg, color);
          }
          
          /* [3.3] Look at the first few rows for tabs */
          nFound= ptGQ.getDelimTokens(line, useTokFlag,  /* tokens to use*/
                                      tokArray,          /* returned data*/
                                      true, ""           /* missingDataStr */
                                      );
          /* [3.4] Get the values, should be a positive integer */
          sGrid= tokArray[idxGrid];
          sRow= tokArray[idxGridRow];
          sCol= tokArray[idxGridCol];
          
          int len= sGrid.length();
          
          for(int x= 0; x < len; x++)
          {
            char ch= sGrid.charAt(x);
            if(!Character.isDigit(ch))
              return(false); /* bad number */
          }
          
          grid= UtilCM.cvs2i(sGrid, 0);
          
          len= sRow.length();
          for(int x= 0; x < len; x++)
          {
            char ch= sRow.charAt(x);
            if(!Character.isDigit(ch))
              return(false); /* bad number */
          }
          
          row= UtilCM.cvs2i(sRow, 0);
          
          len= sCol.length();
          for(int x= 0; x < len; x++)
          {
            char ch= sCol.charAt(x);
            if(!Character.isDigit(ch))
              return(false); /* bad number */
          }
          
          col= UtilCM.cvs2i(sCol, 0);
          
          /* [3.5] Compute maxima */
          estMaxGridsInFile= Math.max(estMaxGridsInFile, grid);
          estMaxRowsInFile= Math.max(estMaxRowsInFile, row);
          estMaxColsInFile= Math.max(estMaxColsInFile, col);
        }/* do not read fields */
      } /* read lines until the end of the file */
      while(line!=null);
      
      file.close();
    } /* get the row r line of data */
    
    catch (Exception e)
    {
      return(false);                  /* failed */
    }
    
    /* [2] Set global state variables to the estimates */
    mcd.maxGrids= estMaxGridsInFile;
    mcd.maxGridRows= estMaxRowsInFile;
    mcd.maxGridCols= estMaxColsInFile;
    
    return(true);
  } /* findMaxGridRowColInFile */
  
  
  /**
   * countNonNullTabData() - count Non-Null data tabs in line
   * Do not count tabs after reach field with no data.
   * @param sLine line to count lines on
   * @return number of non-null data tabs
   */
  public static int countNonNullTabData(String sLine)
  { /* countNonNullTabData */
    if(sLine==null)
      return(0);
    char inputBuf[]= sLine.toCharArray();  /* cvt input string to char[] */
    int
      nTabs= 0,
      bufSize= sLine.length(),        /* size of input buffer */
      bufCtr= 0;                      /* working input buffer index */
    
    while(bufCtr<bufSize)
      if(inputBuf[bufCtr++]=='\t')
      {
        if((bufCtr<bufSize) && inputBuf[bufCtr]!='\t')
          nTabs++;
      }
    
    return(nTabs);
  } /* countNonNullTabData */
  
  
  /**
   * findRowWithKeywordsInFile() - open file and look for first line with
   * keywords (case-independent) in it.
   * If needAllKeywordsFlag is set then all keywords need to be present.
   * @param cvt instance of Cvt2Mae
   * @param fileName file to open and search
   * @param keyWords array of keywords to search
   * @param needAllKeywordsFlag boolean  look at all keywords flag
   * @param maxLinesToCheck only look at max number of lines
   * @param msg Not used, Future
   * @param color Not used, Future
   * @return line number (starting at line 1), else return 0
   */
  public static int findRowWithKeywordsInFile(Cvt2Mae cvt, String fileName,
                                             String keyWords[],
                                             boolean needAllKeywordsFlag,
                                             int maxLinesToCheck,
                                             String msg, Color color)
  { /* findRowWithKeywordsInFile */
    FileReader file= null;
    String
      tokArray[]= new String[300],
      rawData;
    int
      lineR= 0,                 /* line # to return */
      nKeywordsPresent= 0,
      nKeywords= keyWords.length,
      nLines= 0;                 /* running line counter */
    String sKeyLC[]= new String[nKeywords];
    
    for(int k=0;k<nKeywords;k++)
      sKeyLC[k]= keyWords[k].toLowerCase();
    
    /* [1] Open file and read uo to row maxLinesToCheck line of data */
    try
    { /* check each line for keywords */
      file= new FileReader(fileName);
      BufferedReader
      buffer= new BufferedReader(file);
      
      /* [1.1] get 1 line at a time until eof */
      do
      { /* read lines until the end of the file or max lines to read */
        rawData= buffer.readLine();  /* read and toss lines */
        if(rawData==null)
          continue;             /* stop since probably at the end */
        nLines++;               /* count lines */
        
        /* only look at max # of lines */
        if(nLines > maxLinesToCheck)
        { /* failed */
          file.close();
          return(0);
        }
        /* get list of tokens in the line and then look for keywords */
        ParseTable pt= new ParseTable('\t');
        int nFound= pt.getAllDelimTokens(rawData, tokArray, true);
        
        /* Test each word against keyWord[] */
        nKeywordsPresent= 0;
        String
          sKey,
          sTok;
        
        for(int c=0;c<nFound;c++)
          tokArray[c]= tokArray[c].toLowerCase();
        
        for(int k=0;k<nKeywords;k++)
        { /* test each keyword for match */
          sKey= sKeyLC[k];
          for(int c=0;c<nFound;c++)
          { /* test each word */
            sTok= tokArray[c];
            if(sTok.startsWith(sKey))
            {
              nKeywordsPresent++;
              break;
            }
          }
        }
        
        if(nKeywordsPresent==nKeywords)
        {
          file.close();
          return(nLines);
        }
      } /* read lines until the end of the file or max lines to read  */
      while(rawData!=null);
      /*
      if(DBUG_FILE_TABLE)
        System.out.println("\nFT-FRWKIF.2  nLines="+nLines);
      */
      file.close();
    } /* check each line for keywords */
    
    catch (Exception e)
    { /* failed */
      return(0);
    }
    
    return(lineR);
  } /* findRowWithKeywordsInFile */
  
  
  /**
   * cvtStrToTable() - convert tab-delim String data to Table data structure.
   * Set up the table data structures in this instance of FileTable:
   *  (tRows, tCols, tFields[], tData[][]).
   * @param sData data to convert to table
   * @return true if successful
   * @see #computeTableSize
   * @see #storeDataBufParser
   */
  public boolean cvtStrToTable(String sData)
  { /* cvtStrToTable */
    if(sData==null)
      return(false);
    
    if(!computeTableSize(sData))
      return(false);                /* set up (tRows,tCols) */
    
    if(!storeDataBufParser(sData))
      return(false);                /* set up tFields[] and tData[][] */
    
    if(!checkForBadTable())
      return(false);                /* validata tFields data */
    
    return(true);
  } /* cvtStrToTable */
  
  
  /**
   * readFileAsTable() - read file and tab-delim data as Table.
   * Set up the table data structures in this instance of FileTable:
   *  (tRows, tCols, tFields[], tData[][]).
   * @param fileName to open and read to get data to convert
   * @return true if successful
   * @see #computeTableSize
   * @see #readFileAsString
   * @see #storeDataBufParser
   */
  public boolean readFileAsTable(String fileName)
  { /* readFileAsTable */
    String
      sData= readFileAsString(fileName); /* read the file to string */
    if(sData==null)
      return(false);
    
    if(!computeTableSize(sData))
      return(false);                  /* set up (tRows,tCols) */
    
    if(!storeDataBufParser(sData))
      return(false);                  /* set up tFields[] and tData[][] */
    
    if(!checkForBadTable())
      return(false);                  /* valid data tFields data */
    
    return(true);
  } /* readFileAsTable */
  
  
  /**
   * writeData() - write string data to file
   * @param fileName file to write data to disk
   * @param data write data to disk
   * @return true if successful
   * @see #writeStringToFile
   */
  public boolean writeData(String fileName, String data)
  { /* writeData */
    boolean flag= false;
    
    if(fileName==null)
      return(false);
    
    flag= writeStringToFile(fileName, data);
    
    return(flag);
  } /* writeData */
  
  
  /**
   * readFileAsString() - read file from disk and return data as String
   * @param fileName file name to read data
   * @return data as String, null if failed
   */
  public String readFileAsString(String fileName)
  { /* readFileAsString */
    String sR;
    if(fileName==null)
      return(null);
    File f;
    RandomAccessFile rin= null;
    byte dataB[]= null;
    int size;
    
    try
    {
      f= new File(fileName);
      if(!f.canRead())
      {
        errMsgLog += "FIO-RFFD Can't read ["+fileName+"]";
        return(null);
      }
      
      if(!f.exists())
      {
        errMsgLog += "FIO-RFFD File not found ["+fileName+"]";
        return(null);
      }
      
      rin= new RandomAccessFile(f,"r");
      size= (int) f.length();
      dataB= new byte[size]; /* make char array exact size needed! */
      
      rin.readFully(dataB);
      rin.close();                   /* done reading */
      f=null;
      System.runFinalization();
      System.gc();
      
      sR= new String(dataB);         /* convert String from char[]*/
      dataB= null;
      System.runFinalization();
      System.gc();
      
      return(sR);
    }
    
    catch (SecurityException e)
    {
      errMsgLog += "FIO-RFFD secur.Excep.["+fileName+"] "+e;
    }
    
    catch (FileNotFoundException e)
    {
      errMsgLog += "FIO-RFFD FileNotFoundExcep.["+fileName+"] "+e;
    }
    catch (IOException e)
    {
      errMsgLog += "FIO-RFFD IOExcep.["+fileName+"] "+e;
    }
    
    return(null);             /* error */
  } /* readFileAsString */
  
  
  /**
   * readFileAsString() - Will read file from disk & returns as String
   * @param f File to read
   * @return data read from the file, else null if failed.
   */
  public String readFileAsString(File f)
  { /* readFileAsString */
    String sR;
    RandomAccessFile rin= null;
    byte dataB[]= null;
    int size;
    
    try
    {
      if(!f.canRead())
      {
        errMsgLog += "FIO-RFFD Can't read ["+fileName+"]";
        return(null);
      }
      
      if(!f.exists())
      {
        errMsgLog += "FIO-RFFD File not found ["+fileName+"]";
        return(null);
      }
      
      rin= new RandomAccessFile(f,"r");
      size= (int)f.length();
      dataB= new byte[size]; /* make char array exact size needed! */
      
      rin.readFully(dataB);
      rin.close();                  /* done reading */
      f= null;
      System.runFinalization();
      System.gc();
      
      sR= new String(dataB);        /* convert String from char[]*/
      dataB= null;
      System.runFinalization();
      System.gc();
      
      return(sR);
    }
    
    catch (SecurityException e)
    {
      errMsgLog += "FIO-RFFD secur.Excep.["+fileName+"] "+e;
      return(null);
    }
    
    catch (FileNotFoundException e)
    {
      errMsgLog += "FIO-RFFD FileNotFoundExcep.["+fileName+"] "+e;
    }
    catch (IOException e)
    {
      errMsgLog += "FIO-RFFD IOExcep.["+fileName+"] "+e;
    }
    
    return(null);             /* error */
  } /* readFileAsString */
  
  
  /**
   * writeStringToFile() - write data to disk file
   * @param fileName is name of the file to write
   * @param data to write to the file
   * @return true if successful false if failed
   */
  public boolean writeStringToFile(String fileName, String data)
  { /* writeStringToFile */
    
    /** [TODO] write file to disk & returns true if ok */
    String sR;
    File f;
    FileWriter out= null;
    char dataBuf[];
    int size= data.length();
    
    try
    { /* try to write it */
      f= new File(fileName);
      out= new FileWriter(f);
      if(!f.canWrite())
        return(false);
      dataBuf= new char[size];
      for(int i=0;i<size;i++)
        dataBuf[i]= data.charAt(i);
      out.write(dataBuf, 0, size);
      out.close();                       /* done writing */
      return(true);
    }
    
    catch (SecurityException e)
    {
      errMsgLog += "FIO-WFTD secur.Excep.["+fileName+"] "+e;
    }
    catch (FileNotFoundException e)
    {
      errMsgLog += "FIO-WFTD fileNotFound["+fileName+"] "+e;
    }
    catch (IOException e)
    {
      errMsgLog += "FIO-WFTD IOexcep.["+ fileName+"] "+e;
    }
    
    return(false);             /* error */
  } /* writeStringToFile */
  
  
  /**
   * storeDataBufParser() - Put data into Table tData[r][c] and tFields[c],
   * given a raw data String from the whole file.
   * The tData[0:tRow-1][0:tCol-1] and tFields[0:tRow-1]
   * @param rawData is raw data string read from file to convert into Table
   * @return true if successful false if failed
   */
  private boolean storeDataBufParser(String rawData)
  { /* storeDataBufParser */
    char
      inputBuf[]= rawData.toCharArray(), /* cvt input string to char[] */
      tokBuf[]= new char[10000],          /* token buffer */
      lineBuf[]= new char[10000];         /* line buffer */
    int
      bufSize= rawData.length(),         /* size of input buffer */
      bufCtr= 0,                         /* working input buffer index */
      tokSize= 0,                        /* size of tokBuf */
      lineSize= 0,                       /* size of lineBuf */
      lineCtr= 0;                        /* working line buffer index */
    String token;                        /* field delimited token */
     
    this.bufSize= bufSize;               /* could use for progress bar */
    
    /* [1] Allocate the Table data structures */
    tData= new String[tRows][tCols];     /* data [0:tRows-1][0:tCols-1] */
    tFields= new String[tCols];          /* field names [0:tCols-1] */
    if(rawData==null)
    {
      return(false);
    }
    
    /* [2] Parse data from buffer into field and data tables */
    if(rawData != null)
    { /* data buffer exists, parse it */
      char ch;
      int
        i= 0,
        c= 0,		    /* running count of rows and cols */
        r= 0;
      boolean sawNonSpaceLeadingChar;
      
      /* [2.1] Store table Field names first */
      for(c=0; c<tCols; c++)
      { /* get and store field names*/
        tokSize= 0;
        sawNonSpaceLeadingChar= false;
        while((ch= inputBuf[bufCtr])!='\t' && ch!='\n') // GREG "\r" ??
        { /* build token for field name */
          bufCtr++;
          if(!sawNonSpaceLeadingChar)
          { /* handle leading white space */
            if(ch==' ')
              continue;
            else sawNonSpaceLeadingChar= true;
          } /* handle leading white space */
          else if(ch=='\r' || ch=='"')
            continue;              /* ignore CR and double quote */
          tokBuf[tokSize++]= ch;   /* save all non-space leading
                                    * chars up to TAB or EOL */
        } /* build token for field name */
        
        tokBuf[tokSize]= '\0';     /* terminate token */
        if(inputBuf[bufCtr]=='\t')
          bufCtr++;		 /* move past TAB */
        else if(inputBuf[bufCtr]=='\n')
          bufCtr++;                /* get ride of LF */
        
        /* Rmv trailing whitespace by adjusting tokSize downward */
        for(i=tokSize;i>=0;i--)
        { /* find first non-space character from the end */
          if(tokBuf[i]!=' ')
            break;
          else tokSize--;
        }
        
        if(tokSize<=0)
          token= "";               /* change null to "" */
        else
          token= new String(tokBuf,0,tokSize);
        
        tFields[c]= token;         /* i.e. save field name */
      } /* get and store field names*/
      
      
      /* [2.2] Get data rows from buffer and save in table. */
      for(r=0; r<tRows; r++)
      { /* parse remaining rows into tData[r][] row data */
        /* [2.1] Read a line of the buffer in case there are fields
         * missing at the end.
         * [TODO] optimize for speedup...
         */
        lineSize= 0;                /* start new line */
        if(bufCtr < bufSize)
        { /* build lineBuf for this row */
          sawNonSpaceLeadingChar= false;
          while((bufCtr < bufSize) && (ch= inputBuf[bufCtr]) != '\n')
          { /* build lineBuf[0:lineSize-1] */
            bufCtr++;
            if(!sawNonSpaceLeadingChar)
            { /* handle leading white space */
              if(ch==' ')
                continue;
              else
                sawNonSpaceLeadingChar= true;
            }
            if(ch=='\r' || ch=='"')
              continue;               /* ignore CR and double quote */
            lineBuf[lineSize++]= ch;  /* save all non-space
                                       * leading chars up to EOL*/
          } /* build lineBuf[0:lineSize-1] */
          
          lineBuf[lineSize]= '\0';    /* terminate line buffer */
          
          if((bufCtr < bufSize) && inputBuf[bufCtr]=='\n')
            bufCtr++;                 /* Get rid of LF */
          
          /* Rmv trailing whitespace by adjusting lineSize downward*/
          for(i=lineSize;i>=0;i--)
          { /* find first non-space character from the end */
            if(lineBuf[i]!=' ')
              break;
            else lineSize--;
          }
        } /* build lineBuf for this row*/
        else
          break;                     /* no more data in input buffer */        
        
        /* [2.2] Extract tab-delimited fields from line buffer */
        if(lineSize==0)
          break;                      /* no more data in input buffer */
        
        lineCtr= 0;	                  /* get next line */
        for(c=0; c<tCols; c++)
        { /* process columns for this row */
          tokSize= 0;
          token= "";
          if(lineCtr < lineSize)
          { /* build and save [r][c] token */
            tokSize= 0;
            sawNonSpaceLeadingChar= false;
            while(lineCtr < lineSize &&  (ch= lineBuf[lineCtr]) != '\t')
            { /* build token*/
              lineCtr++;
              if(!sawNonSpaceLeadingChar)
              { /* handle leading white space */
                if(ch==' ')
                  continue;
                else
                  sawNonSpaceLeadingChar= true;
              } /* handle leading white space */
              if(ch=='\r' || ch=='"')
                continue;             /* ignore CR & double quote */
              tokBuf[tokSize++]= ch;  /* save all non-space
                                       * leading chars up to TAB */
            } /* build token*/
          } /* build and save [r][c] token*/
          
          lineCtr++;                 /* move past \t */
          tokBuf[tokSize]= '\0';     /* terminate token */
          
          /* Rmv trailing whitespace by adj. tokSize downward */
          for(i=tokSize;i>=0;i--)
          { /* find first non-space character from the end */
            if(tokBuf[i]!=' ')
              break;
            else tokSize--;
          } /* find first non-space character from the end */
          
          if(tokSize<=0)
            token= "";
          else
            token= new String(tokBuf,0,tokSize);
          
          tData[r][c]= token;         /* every slot gets data!! */
        } /* process columns for this row */
      } /* parse remaining rows into tData[r][] row data */
    } /* data buffer exists, parse it */
    
    return(true);
  } /* storeDataBufParser */
  
  
  /**
   * computeTableSize() - setup the (tRows,tCols) of table data.
   * This is a tab-delimited file consisting of the field names
   * in the first row followed by data fields.
   * and set (tRows,tCols, tFields[], tData[][]) to (0,0,null,null).
   * @param rawData raw data representing string table to analyze
   * @return true if successful false if failed
   */
  private boolean computeTableSize(String rawData)
  { /* computeTableSize */
    boolean flag= false;
    
    if(rawData!=null && rawData.length()>0)
    { /* compute table sizes */
      char
        ch,
        lines[],
        inputBuf[]= rawData.toCharArray();
      int
        bufSize= inputBuf.length,
        bufSizeM1= (bufSize-1),
        cols= 0,
        rows= 0;

      /* Count cols and rows */
      if(inputBuf[0] != '\0')
      { /* process buffer */
        tRows= 0;                      /* count first row */
        tCols= 1;                      /* there is at least 1 col*/
        
        for(int x=0; x < bufSize; x++)
        { /* check for EOL */
          ch= inputBuf[x];
          if(ch=='\n' || ch=='\r')
          { /* count rows */
            tRows++;               /* check for end of line */
            if((ch=='\n' || ch=='\r') && ( x < bufSizeM1))
              x++;                 /* handle CRLF or LFCR cases */
          }
          else if(tRows==0 && ch=='\t')
            tCols++;                   /* count cols only for Field Names */
        } /* check for EOL */
        
        flag= true;
      } /* process buffer */
    } /* compute table sizes */
    
    if(!flag)
    { /* failed */
      /* set (tRows,tCols, tFields[], tData[][]) to (0,0,null,null). */
      tRows= 0;
      tCols= 0;
      tFields= null;
      tData= null;
      return(false);
    }
    
    return(true);
  } /* computeTableSize */
  
  
  /**
   * lookupFieldIdx() - lookup index of field if exists.
   * if it does not exist, return -1
   * @param fieldName
   * @return index if successful, -1 if not found
   */
  public int lookupFieldIdx(String fieldName)
  { /* lookupFieldIdx */
    int idx= -1;          /* failure default */
    
    for(int i=0; i<tCols; i++)
      if(fieldName.equals(tFields[i]))
      {
        idx= i;
        break;
      }
    return(idx);
  } /* lookupFieldIdx */
  
  
  /**
   * lookupSubstringFieldIdx() - lookup index of case independent field
   * substring if exists and startsWith tblField[].
   * if it does not exist, return -1
   * @param fieldName field name to look up
   * @param tblFields array of fields
   * @return index if successful, -1 if not found
   */
  public static int lookupSubstringFieldIdx(String tblFields[],
                                            String fieldName)
  { /* lookupSubstringFieldIdx */
    int
      tblCols= tblFields.length,
      idx= -1;          /* failure default */
    String fieldNameLC= fieldName.toLowerCase();
    
    for(int i=0; i<tblCols; i++)
      if(fieldNameLC.startsWith(tblFields[i].toLowerCase()))
      {
        idx= i;
        break;
      }
    return(idx);
  } /* lookupSubstringFieldIdx */
  
  
  /**
   * freeTable() - free Table tData[][] and tFields so can garbage
   * collect it Also zero tRows, tCols
   */
  public void freeTable()
  { /* freeTable */
    tRows= 0;
    tCols= 0;
    tFields= null;
    tData= null;
  } /* freeTable */
  
} /* end of class FileTable */

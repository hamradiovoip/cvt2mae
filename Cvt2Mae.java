/** File: Cvt2Mae.java */

package cvt2mae;

import java.awt.*;
import java.awt.event.*;
import java.awt.List;
import java.io.*;
import java.util.*;

/** 
 * MicroArray Cvt2Mae main class. 
 * 
 * This application converts microarray data files to MAExplorer files format.
 * Data types includes: user-defined, Incyte GEM, Affymetrix, and others.
 * The data is stored in MaeConfigData and may be edited prior
 * to saving in the MAExplorer tree maePrj/(Config/ MAE/ Quant/ State/ Report/).
 * Cvt2Mae stores array layouts in  /ArrayLayouts as *.alo files.
 *
 * <P>
 * This work was produced by  Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 * <P>
 *
 * @author P. Lemkin (NCI), B. Stephens(SAIC), G. Thornwall (SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $  $Revision: 1.32 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class Cvt2Mae 
{
  /*
   * TODOs: 
   * 1. Allow adding samples or data to existing tree.
   *    usefull for MADB, instead of downloading entire data, just
   *    the new stuff. 
   * 2. Merge new data from existing project with another existing project.
   * 3. Need to check and compare the layout to make sure it is the same for 
   *    the data being used.
   * 4. Resolve the short number of rows problem where the actual data rows
   *    is +1 more than the number of intensity data rows in the raw user data file
   * 5. Pseudo image coords are not guess in "edit layout" correctly. Need to look
   *    at both data file and also separate GIPO file to determine what the geometry is.
   *    Problem: what do people call grid row, grid col; not consistant.
   */ 
  
  /** Title line version numbers */
  public final static String
    VERSION= "11-26-2002 V.0.73 (Beta)";
  /** RCS Version number and dat1 */
  public final static String
     RCS= "$Date: 2005/10/20 11:45:56 $  $Revision: 1.32 $";
  /** Current Version number for Array layout forces reading if older version is
   * the same else don't read older versions! 
   */
  public final static String
    ALO_VERSION= "1.21"; 
  /** Mae Config Data */
  public MaeConfigData		
    mcd;			
  /** Global data DataIO instance */
  public DataIO
    dio;                       
  /** Global utilities UtilCM instance */
  public UtilCM
    util;			
  /** Global SetupLayouts layouts instance */ 
  public SetupLayouts 
    sul;			
  /** Global GUI popup frame */
  public static CvtGUI
    gui;                            
  /** single MODAL instance of popup FieldMapGUI if not null*/
  public FieldMapGUI
    fmg= null;   
  /** flag: for debugging - always TRUE*/
  public static boolean
    CONSOLE_FLAG= true;                   
  /** flag: for debugging - always FALSE */
  public static boolean
    NEVER= false;      
  /** flag: set if want log windows */
  public static boolean
    USE_LOG_WINDOW= false;          
  /** flag: primary debug flag - only while debugging */
  public static boolean
    DBUG_FLAG= false;            
  /** flag: extra debugging for I/O in readData() */
  public static boolean
    DBUG_READ_WRITE= false; 
  /** Add Image Analysis Method GUI */
  public static boolean
    USE_IAM_FLAG= false;            
  /** flag: Force source and output files directories to use 
   * hardwired definitions DBUG_IN_DIR and DBUG_OUT_DIR 
   * while debugging.
   */
  public static boolean
    USE_DBUG_DIR= false;   
  
  /** 
   * if USE_DBUG_DIR, then can save time when debugging by going directly
   * to debugging input source and output directories.
   */			      
  public final static String
    //DBUG_IN_DIR= "C:\\Temp",
    //DBUG_IN_DIR= "C:\\Temp\\IncyteData", 
    //DBUG_IN_DIR= "C:\\Temp\\GenePix",
    //DBUG_IN_DIR= "C:\\Temp\\ArrayVision",
    DBUG_IN_DIR= "C:\\Temp\\AffyData",
    //DBUG_IN_DIR= "C:\\Temp\\ScanalyzeData",
    //DBUG_IN_DIR="C:\\Temp\\Mae-HumanBrainU133A",
    DBUG_OUT_DIR= "C:\\Temp\\junk";
    //DBUG_IN_DIR= "G:\\Temp\\AffyData",
    //DBUG_OUT_DIR= "G:\\Temp\\junk"; 
  
  /* --- Preset sizes of data structures --- */
  /** max # of tokens/row of an input file */
  public final static int
    MAX_IN_TOKENS_PER_ROW= 512;  
  /** max # of source files that can convert */
  public final static int
    MAX_SRC_FILES= 400;          
  /** max # of Array Layouts */
  public final static int
    MAX_ARRAY_LAYOUTS= 300;
  /** max # of image analysis methods that were used to quantify data -
   * possibly in separate data files.
   */        
  public final static int
    MAX_IMG_ANALY_METHODS= 20;   
  
  /** top level GUI title */
  public String
    title;                      
  /** JVM ("os.name") that we are running in */
  public String
    osName;                    
  /** JVM ("file.separator") */
  public String
    fileSep;                    
  /** user directory - JVM ("user.dir")+fileSep */
  public String
    userDir;                    
  /** date captured when this program is run */
  public String
    date;                       
  /** flag: true if Windows PC */
  public boolean
    isWinPCflag;
  /** flag: true if SUNOS */
  public boolean
    isSunFlag;
  /** flag: use Max list of required fields */
  public boolean
    isMacFlag;
  public boolean
    useMaxFieldsRequiredFlag;
  /** flag: if clean up user data whitespace */
  public boolean
    rmvTrailingWhiteSpaceFlag;
  /** flag: enabled when may edit MapUserToMae */
  public boolean
    enableMapQueryFlag;
  /** flag: user has selected input file */
  public boolean
    selectedInputFileFlag;
  /** flag: user has selected chip layout */
  public boolean
    selectedChipLayoutFlag;
  /** flag: user has selected output path */
  public boolean
    selectedOutputPathFlag;
  /** flag: user has selected quantification method*/      
  public boolean
    selectedImageAnalysisFlag;
  
  /** Possible global exception error string for error reporting */
  public String
    sExceptErr= null; 
  /** Possible global exception error for error reporting */  
  public Exception
    exceptErr= null;   
  /** for fieldMapGUI, setting first time defaults*/     
  public boolean
    fmgFirstTimeFlagGipo= true;  
  /** for fieldMapGUI, setting first time defaults*/     
  public boolean 
    fmgFirstTimeFlagQuant= true;  
  /** for fieldMapGUI, setting first time defaults*/     
  public boolean 
    fmgFirstTimeFlagSample= true;  
  /** for newline, Mac= "\r" Sun ="\n" PC= "\r\n" */
  public String
    newline;
  
  
  /**
   * Cvt2Mae() - Constructor
   * @see #init()
   */
  public Cvt2Mae()
    { /* Cvt2Mae */
      init();
    } /* Cvt2Mae */
  
  
  /**
   * main() - for Cvt2Mae started by stand-alone application
   * @param args is the command line arg list
   */
  public static void main(String args[])
  { /* main */
    Cvt2Mae
    cvt= new Cvt2Mae();
  } /* main */
  
  
  /**
   * init() - initialize data structures and create and start the GUI.
   * @see CvtGUI
   * @see DataIO
   * @see MaeConfigData
   * @see SetupLayouts
   * @see SetupLayouts#setupImageAnalysisMethods
   * @see UtilCM
   * @see UtilCM#dateStr
   * @see UtilCM#logMsg
   * @see UtilCM#timeStr
   */
  public void init()
  { /* init */
    /* [0] Set defaults */
    osName= System.getProperty("os.name");
    if(DBUG_FLAG)
      System.out.println("os name = "+ osName);
    
    isWinPCflag= osName.startsWith("Win");  /* TRUE if any Windows PC */
    isSunFlag= osName.equals("Solaris");
    fileSep= System.getProperty("file.separator");
    
    isMacFlag= osName.equals("Mac OS");
    if(isMacFlag)
      newline= System.getProperty("line.separator");
    else
      newline= "\n";
    userDir= System.getProperty("user.dir") + fileSep;
    rmvTrailingWhiteSpaceFlag= false;   /*set if clean up user data */
    
    /* [1] Create data structures for editing config, samples, startup data. */
    
    /* [1.1] Create debugging windows */
    util= new UtilCM(DBUG_FLAG);          /* setup utilty package */
    date=  util.dateStr() + util.timeStr();
    
    /* [1.2] Create main data structure for holding MAE data */
    mcd= new MaeConfigData(MAX_SRC_FILES);
    mcd.cvt= this;
    mcd.version= VERSION;
    mcd.date= date;
    
    if(DBUG_FLAG)
      util.logMsg("\n---------------------------------------------------"+
                  "\nCvt2Mae - "+VERSION+
                  "\nDate: "+date,
                  Color.green);
    
    /* [2] Setup the initial state */
    useMaxFieldsRequiredFlag= false;
    enableMapQueryFlag= false;
    selectedInputFileFlag= false;         /* make them do it all */
    selectedChipLayoutFlag= false;
    selectedOutputPathFlag= false;
    selectedImageAnalysisFlag= false;    
    
    /* [3] Setup DataIO state */
    dio= new DataIO(this);
    
    /* [4] Setup the list of know array types */
    sul= new SetupLayouts(this);
    if(!sul.setupArrayLayouts())
    {
      System.out.println("Dryrot - can't setup layouts.");
      return;
    }
    
    if(!sul.setupImageAnalysisMethods())
    {
      System.out.println("Dryrot - can't setup image analysis methods");
      return;
    }
    
    /* [5] Build GUI */
    title= "Cvt2Mae: convert array data to MAExplorer files - Version: "+ VERSION;
    gui= new CvtGUI(this, title);
  } /* init */
  
}  /* end of class Cvt2Mae */

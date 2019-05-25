/** File: DataIO.java */

package cvt2mae;

import java.awt.*;
import java.awt.event.*;
import java.awt.List;
import java.io.*;
import java.util.*;

/**
 * This class contains methods to read and write array data, GIPO, .quant, and MAE files.
 *<P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author B Stephens, (SAIC), P. Lemkin (NCI), G. Thornwall(SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $   $Revision: 1.21 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class DataIO 
{
  /** Global links */
  public Cvt2Mae
    cvt;                      
  /** Mae Config Data */
  public MaeConfigData		
    mcd;			
  /** Global utilities */ 
  public UtilCM
    util;			
  /** Global layouts */ 
  public SetupLayouts 
    sul;			
  /** Global GUI popup frame */     
  public CvtGUI
    gui;                       
  /** GUI title */ 
  public String
    title;
  /** [MAX_IN_TOKENS_PER_ROW] holds input tokens*/
  public String            
    tokArray[];             
  /** [MAX_IN_TOKENS_PER_ROW] tokens to get for FieldGQ data */
  public boolean 
    useTokFlagGQ[];             
  /** data elements from the input table */
  public Element
    datum[];               
  /** # of infill spots generated */
  public int
    infillCnt;
  /** expected # of tokens for this layout */
  public int
    expectedNbrTokens;         
  
  /* --- GIPO fields used to generate the GIPO file --- */
  
  /** GIPO index fields for fieldNames[] */
  public int
    idxLocation; 
  /** GIPO index  */
  public int
    idxField;
  /** GIPO index for Grid */
  public int
    idxGrid;
  /** GIPO index for grid row*/
  public int
    idxGridRow;
  /** GIPO index for grid column */
  public int
    idxGridCol;
  /** GIPO index for NAME_GRC; (Molecular Dynamics "NAME_GRC" spec )*/
  public int
    idxNAME_GRC;
  /** GIPO index for plate */
  public int
    idxPlate;
  /** GIPO index for plate row */
  public int
    idxPlateRow;
  /** GIPO index for plate column */
  public int
    idxPlateCol;
  /** GIPO index for 'GIPO' QualCheck data*/
  public int
    idxQualCheckGIPO;
  /** generic Genomic ID - used when no other ids */
  public int
    idxIdentifier;            
  /** GIPO index for clone ID */
  public int
    idxClone_ID;
  /** GIPO index for gene name */
  public int
    idxGeneName; 
  /** GIPO index Unigene cluster ID */
  public int
    idxUnigene_cluster_ID;
  /** GIPO index Unigene cluster name */
  public int
    idxUnigene_cluster_name;
  /** GIPO index 'GenBank' identifier data. See http://ncbi.nlm.nih.gov/ */
  public int
    idxGenBankAcc;
  /** GIPO index 'GenBank' identifier data. See http://ncbi.nlm.nih.gov/ */
   public int
     idxGenBankAcc3;
  /** GIPO index 'GenBank' identifier data. See http://ncbi.nlm.nih.gov/ */
   public int
    idxGenBankAcc5;
  /** GIPO index 'dbEST' identifier data. */
  public int
    idxDbEst3;
  /** GIPO index 'dbEST' identifier data. */
  public int
    idxDbEst5;
  /** GIPO index SwissProtID */
  public int
    idxSwissProtID;
  /** GIPO index  */
  public int
    idxLocusLinkID;
  
  /* --- Quant fields used to generate .quant files. --- */
  /** Quant index fields are for fieldNames[] */
  public int
    idxX;
  /** GIPO index  */
  public int
    idxY;
  /** GIPO index  */
  public int
    idxRawIntensity;
  /** GIPO index  */
  public int
    idxRawIntensity1;
  /** GIPO index  */
  public int
    idxRawIntensity2;
  /** GIPO index  */
  public int
    idxRawBackground;
  /** GIPO index  */
  public int
    idxRawBackground1;
  /** GIPO index  */
  public int
    idxRawBackground2;
  /** GIPO index  */
  public int
    idxQualCheck; 
  /** GIPO index for Spot Detection value*/
  public int
    idxDetValue;
  /** GIPO index for Affy Diff Call*/
  public int
    idxDiffCall;
  /** GIPO index for Fole Change*/
  public int
    idxFoldChange;
  
  /** Special hack for Affy files - see chkAndEditFieldNames().
   * This will contain the column for the Field "Description" if
   * needed, else -1.
   */
  private static int
    affyFieldDescriptionCol= -1; 
  
  
  /**
   * DataIO() - Constructor
   * @param cvt Cvt2Mae instance
   */
  public DataIO(Cvt2Mae cvt)
  { /* DataIO */
    this.cvt= cvt;
    mcd= cvt.mcd;
    util= cvt.util;
    sul= cvt.sul;
    gui= cvt.gui;
    tokArray= new String[cvt.MAX_IN_TOKENS_PER_ROW];
    useTokFlagGQ= new boolean[cvt.MAX_IN_TOKENS_PER_ROW];
  } /* DataIO */
  
  
  /**
   * extractDatumFromDataRow() - extract datum[spotID] from row of token data
   * Return spotID # (Location#) if ok else -1
   * @param tokArray tokens in row
   * @param rowNbr row number in file
   * @param spotNbr sequential spot number
   * @param line raw line
   * @param grid computed Grid
   * @param gRow computed Row
   * @param gCol computed Column
   * @return SpotID number location if ok, else -1
   * @see Element
   */
  private int extractDatumFromDataRow(String tokArray[],  int rowNbr,
                                      int spotNbr, String line,
                                      int grid, int gRow, int gCol)
  { /* extractDatumFromDataRow */
    int iLocation= -1;
    String sLoc;
    
    cvt.exceptErr= null;            /* clear global exception error */
    cvt.sExceptErr= null;
    
    /* [1] Analyze data and save in output variables */
    try
    { /* get and convert data */
      /* Extract GIPO data from the file if present */
      Element d= new Element();          /* new entries have null data */
      
      datum[spotNbr]= d;           /* save in master spot list */
      d.spotNbr= (""+spotNbr);
      
      if(idxLocation!=-1)
      { /* they specified a location ID */
        sLoc= tokArray[idxLocation];
        try
        { /* Location is an integer - use it */
          iLocation= Integer.parseInt(sLoc);
        }
        catch (NumberFormatException e)
        { /* just use sequential spot # */
          iLocation= spotNbr;
        }
        d.location= sLoc;       /* save unique string name */
        
        /* Capture highest (iLocation) */
        if(iLocation>mcd.highestID)
          mcd.highestID= iLocation;      /* find max spot ID */
      } /* they specified a location ID */
      
      else
      { /* There is no Location ID - just use sequential spot # */
        iLocation= spotNbr;
        d.location= (""+iLocation); /* synthesize it as a string */
        mcd.highestID= iLocation;   /* fake it */
      }
      
      d.iLocation= iLocation;         /* Also save it as an integer */
      
      if(idxField!=-1)
        d.field= tokArray[idxField];
      else
        d.field= "1";
      
      if(idxGrid!=-1)
        d.grid= tokArray[idxGrid];
      else
        d.grid= (""+grid);            /* synthesize it */
      
      if(idxGridCol!=-1)
        d.grid_col= tokArray[idxGridCol];
      else
        d.grid_col= (""+gCol);        /* synthesize it */
      
      if(idxGridRow!=-1)
        d.grid_row= tokArray[idxGridRow];
      else
        d.grid_row= (""+gRow);        /* synthesize it */
      
      if(idxNAME_GRC!=-1)
        d.NAME_GRC= tokArray[idxNAME_GRC];
      
      if(idxPlate!=-1)
        d.plate= tokArray[idxPlate];
      
      if(idxPlateCol!=-1)
        d.plate_col= tokArray[idxPlateCol];
      
      if(idxPlateRow!=-1)
        d.plate_row= tokArray[idxPlateRow];
      
      // Is this from GIPO??
      if(idxQualCheck!=-1)
        d.qualCheck= tokArray[idxQualCheck];
      
      if(idxIdentifier!=-1)
        d.identifier= tokArray[idxIdentifier];
      
      if(idxClone_ID!=-1)
        d.cloneID= tokArray[idxClone_ID];
      
      if(idxGeneName!=-1)
        d.geneName= tokArray[idxGeneName];
      
      if(idxUnigene_cluster_ID!=-1)
        d.unigene_cluster_ID= tokArray[idxUnigene_cluster_ID];
      
      if(idxUnigene_cluster_name!=-1)
        d.unigene_cluster_name= tokArray[idxUnigene_cluster_name];
      
      if(idxGenBankAcc!=-1)
        d.genBankAcc= tokArray[idxGenBankAcc];
      
      if(idxGenBankAcc3!=-1)
        d.genBankAcc3= tokArray[idxGenBankAcc3];
      
      if(idxGenBankAcc5!=-1)
        d.genBankAcc5= tokArray[idxGenBankAcc5];
      
      if(idxDbEst3!=-1)
        d.dbEst3= tokArray[idxDbEst3];
      
      if(idxDbEst5!=-1)
        d.dbEst5= tokArray[idxDbEst5];
      
      if(idxSwissProtID!=-1)
        d.swissProtID= tokArray[idxSwissProtID];
      
      if(idxLocusLinkID!=-1)
        d.locusLinkID= tokArray[idxLocusLinkID];
      
      /* Extract Quantification data from the same file if present */
      if(idxRawIntensity!=-1)
        d.rawIntensity= tokArray[idxRawIntensity];
      
      if(idxRawIntensity1!=-1)
        d.rawIntensity1= tokArray[idxRawIntensity1];
      
      if(idxRawIntensity2!=-1)
        d.rawIntensity2= tokArray[idxRawIntensity2];
      
      if(idxRawBackground!=-1)
        d.rawBkgrd= tokArray[idxRawBackground];
      
      if(idxRawBackground1!=-1)
        d.rawBkgrd1= tokArray[idxRawBackground1];
      
      if(idxRawBackground2!=-1)
        d.rawBkgrd2= tokArray[idxRawBackground2];
      
      // is this for Quant?
      if(idxQualCheck!=-1)
        d.qualCheck= tokArray[idxQualCheck];
      
      if(idxDetValue!=-1)
        d.detValue= tokArray[idxDetValue];
      
      if(idxDiffCall!=-1)
        d.diffCall= tokArray[idxDiffCall];
      
      if(idxFoldChange!=-1)
        d.foldChange= tokArray[idxFoldChange];
    } /* get and convert data */
    
    catch(Exception e)
    {
      cvt.exceptErr= e;   /* clear global exception error */
      cvt.sExceptErr= "Bad data rowNbr="+rowNbr+
                      " iLocation="+iLocation+
                      " spotNbr="+spotNbr+
                      cvt.newline + " line='"+line+"'"+
                      cvt.newline + " Exception="+cvt.exceptErr+
                      cvt.newline + " Datum";

      return(-1);    /* STOP!  bad row of data */
    }
    
    return(iLocation);
  } /* extractDatumFromDataRow */
  
  
  /**
   * setupFilenames() - setup proper file names based on path analysis
   * These are kept in the mcd.XXXX state.
   */
  public void setupFilenames()
  { /* setupFilenames */
    mcd= cvt.mcd;              /* set up state links */
    util= cvt.util;
    sul= cvt.sul;
    gui= cvt.gui;
    
    String prjChoiceStr= gui.getProjectStr();
    
    /* Setup path-free file names. [TODO] change srcName to dirName */
    mcd.configName= "MaeConfig.txt";
    mcd.samplesName= "SamplesDB.txt";
    mcd.maeName= "Start.mae";
    mcd.gipoName= "GIPO.txt";
    
    /* Do output directory specific setups */
    if(prjChoiceStr.startsWith("Create New"))
    { /* make project dirs, Config/, MAE/ Quant/ & setup files */
      mcd.maePrjDir= mcd.existingPrjDir;
      
      mcd.configDir= mcd.maePrjDir+"Config"+mcd.fileSep;
      mcd.gipoFile= mcd.configDir+mcd.gipoName;
      
      mcd.quantDir= mcd.maePrjDir+"Quant"+mcd.fileSep;
      
      /* [CHECK] if mcd.quantDstFile[] is computed previously */
      for(int i=0;i<mcd.nQuantFiles;i++)
        mcd.quantDstFile[i]= mcd.quantDir+mcd.quantName[i]+".quant";
      
      mcd.configFile= mcd.maePrjDir+"Config"+mcd.fileSep+mcd.configName;
      mcd.samplesFile= mcd.maePrjDir+"Config"+mcd.fileSep+mcd.samplesName;
      mcd.maeFile= mcd.maePrjDir+"MAE"+mcd.fileSep+mcd.maeName;
    }
    
    else if(mcd.existingPrjDir!=null &&
            prjChoiceStr.startsWith("Merge with Existing"))
    { /* set input to existing project directory */
      mcd.maePrjDir= mcd.existingPrjDir;
      
      mcd.configDir= mcd.maePrjDir+"Config"+mcd.fileSep;
      mcd.gipoFile= mcd.configDir+mcd.gipoName;
      
      mcd.quantDir= mcd.maePrjDir+"Quant"+mcd.fileSep;
      
      /* [CHECK] if mcd.quantDstFile[] is computed previously */
      for(int i=0;i<mcd.nQuantFiles;i++)
        mcd.quantDstFile[i]= mcd.quantDir+mcd.quantName[i]+".quant";
      mcd.maeFile= mcd.maePrjDir+"MAE"+mcd.fileSep+mcd.maeName;
    }
    
    else if(prjChoiceStr.startsWith("Use Input"))
    { /* set input to output directory, all files same level */
      mcd.maePrjDir= mcd.dirName;
      
      mcd.configDir= mcd.maePrjDir;
      mcd.gipoFile= mcd.configDir+mcd.gipoName;
      
      mcd.quantDir= mcd.maePrjDir;
      
      /* [CHECK] if mcd.quantDstFile[] is computed previously */
      for(int i=0;i<mcd.nQuantFiles;i++)
        mcd.quantDstFile[i]= mcd.quantDir+mcd.quantName[i]+".quant";
      mcd.maeFile= mcd.maePrjDir+"MAE"+mcd.fileSep+mcd.maeName;
    }
  } /* setupFilenames */
  
  
  /* setupFieldNameIndices() - setup lookup FieldName indices
   * A field does not exist if its index is -1.
   * @see FieldMap#lookupMaeFieldToFieldNameIndex
   */
  public void setupFieldNameIndices()
  { /* setupFieldNameIndices */
    FieldMap fm= mcd.fm;
    
    /* GIPO fields  mapped from data file fields */
    idxLocation= fm.lookupMaeFieldToFieldNameIndex("Location");
    idxField= fm.lookupMaeFieldToFieldNameIndex("field");
    idxGrid= fm.lookupMaeFieldToFieldNameIndex("grid");
    idxGridRow= fm.lookupMaeFieldToFieldNameIndex("grid row");
    idxGridCol= fm.lookupMaeFieldToFieldNameIndex("grid col");
    idxNAME_GRC= fm.lookupMaeFieldToFieldNameIndex("NAME_GRC");
    
    idxPlate= fm.lookupMaeFieldToFieldNameIndex("plate");
    idxPlateRow= fm.lookupMaeFieldToFieldNameIndex("plate row");
    idxPlateCol= fm.lookupMaeFieldToFieldNameIndex("plate col");
    
    idxQualCheckGIPO= (mcd.hasQualCheckGIPOdataFlag)
                         ? fm.lookupMaeFieldToFieldNameIndex("QualCheck")
                         : -1;
    
    idxIdentifier= fm.lookupMaeFieldToFieldNameIndex("Identifier");
    idxClone_ID= fm.lookupMaeFieldToFieldNameIndex("Clone ID");
    idxGeneName= fm.lookupMaeFieldToFieldNameIndex("GeneName");
    idxUnigene_cluster_ID= fm.lookupMaeFieldToFieldNameIndex("Unigene cluster ID");
    idxUnigene_cluster_name= fm.lookupMaeFieldToFieldNameIndex("Unigene cluster name");
    idxGenBankAcc= fm.lookupMaeFieldToFieldNameIndex("GenBankAcc");
    idxGenBankAcc3= fm.lookupMaeFieldToFieldNameIndex("GenBankAcc3'");
    idxGenBankAcc5= fm.lookupMaeFieldToFieldNameIndex("GenBankAcc5'");
    idxDbEst3= fm.lookupMaeFieldToFieldNameIndex("dbEst3'");
    idxDbEst5= fm.lookupMaeFieldToFieldNameIndex("dbEst5'");
    idxSwissProtID= fm.lookupMaeFieldToFieldNameIndex("SwissProtID");
    idxLocusLinkID= fm.lookupMaeFieldToFieldNameIndex("LocusID");
    
    /* Quant fields mapped from data file fields */
    /* [TODO] handle multiple sample entries in the same file... */
    idxX= fm.lookupMaeFieldToFieldNameIndex("X");
    idxY= fm.lookupMaeFieldToFieldNameIndex("Y");
    idxRawIntensity= fm.lookupMaeFieldToFieldNameIndex("RawIntensity");
    
    idxRawIntensity1= fm.lookupMaeFieldToFieldNameIndex("RawIntensity1");
    
    if(idxRawIntensity1==-1)/* if -1 then genePix Cy3 etc... */
      idxRawIntensity1= fm.lookupMaeFieldToFieldNameIndex("Cy3");
    
    idxRawIntensity2= fm.lookupMaeFieldToFieldNameIndex("RawIntensity2");
    
    if(idxRawIntensity2==-1)
      idxRawIntensity2= fm.lookupMaeFieldToFieldNameIndex("Cy5");
    
    idxRawBackground= fm.lookupMaeFieldToFieldNameIndex("Background");
    
    idxRawBackground1= fm.lookupMaeFieldToFieldNameIndex("Background1");
    
    if(idxRawBackground1==-1)
      idxRawBackground1= fm.lookupMaeFieldToFieldNameIndex("Cy3Bkg");
    
    idxRawBackground2= fm.lookupMaeFieldToFieldNameIndex("Background2");
    
    if(idxRawBackground2==-1)
      idxRawBackground2= fm.lookupMaeFieldToFieldNameIndex("Cy5Bkg");
    
    idxQualCheck= (mcd.hasQualCheckQuantDataFlag)
                     ? fm.lookupMaeFieldToFieldNameIndex("QualCheck")
                     : -1;
    idxDetValue= fm.lookupMaeFieldToFieldNameIndex("DetValue");
    idxDiffCall= fm.lookupMaeFieldToFieldNameIndex("DiffCall");
    idxFoldChange= fm.lookupMaeFieldToFieldNameIndex("FoldChange");
  } /* setupFieldNameIndices */
  
  
  /**
   * readData() - read composite vendor or user data from file.
   * This assumes that the current state is setup in the MCD config
   * data structure.
   * @param n is the nth user data file to read
   * @return true if successful
   * @see Element
   * @see FieldMap#genUseTokFlags
   * @see FileTable
   * @see FileTable#readTableFieldsFromFile
   * @see ParseTable
   * @see ParseTable#getAllDelimTokens
   * @see TextFrame#appendLog
   * @see UtilCM#logMsg
   * @see #checkIfRequiredFieldsAreNull
   * @see #chkAndEditFieldNames
   * @see #extractDatumFromDataRow
   * @see #genMultIdxMapOfFieldNameData
   * @see #updateInfillLocationIDs
   */
  public boolean readData(int n)
  { /* readData */
    mcd= cvt.mcd;              /* set up state links */
    util= cvt.util;
    sul= cvt.sul;
    gui= cvt.gui;
    
    FileReader
     fileG= null,             /* file reader for separate GIPO data */
     fileQ= null,             /* file reader for separate Quant data */
     fileGQ= null;            /* file reader for combined GIPO&Quant data */
    BufferedReader
      bufferG= null,           /* file buffer for fileG */
      bufferQ= null,           /* file buffer for fileQ */
      bufferGQ= null;          /* file buffer for fileGQ */
    String
      chipsetStr= gui.getChipsetStr(), /* get chipset string if selected */
      dataG[]= null,           /* [0:nSepGIPOFields-1] separate GIPO data if used*/
      dataQ[]= null,           /* [0:nDataFieldsQ-1] separate Quant */
      fieldGtok,               /* GIPO token from FieldG to add to end of FieldQ lineQ data */
      lineG= null,             /* GIPO data used if separate GIPO file */
      lineQ= null,             /* Quant data used if separate GIPO file */
      line= null,              /* read line (G'+Q) by line */
      sHeader= null,           /* keep header line */
      fieldNamesGQ[]= null,    /* names of fields (FieldGQ) */
      sampleNames[];           /* names of samples */
    int
      sampleFieldIdx[],        /* column field index for samples */
      nSamples= 0,             /* RESET # of samples */
      nDataFieldsQ= 0,         /* RESET # of fields/Quant input data. Read each file */
      nSepGIPOfields,          /* # of fields/GIPO in separate GIPO if used */
      nDataFieldsGQ;           /* RESET # of fieldsGQ/array */

    ParseTable ptGQ= null;     /* line parser assuming both GIPO and
                                * Quant data are in the same line */
    int
      gRow= 1,                 /* RESET generating d.(G,R,C) data */
      gCol= 0,
      grid= 1,
      r= 1,		                 /* working row counter */
      nFound,
      i,
      spotID= -1,              /* RESET spot ID from parser */
      spotNbr= 0,              /* RESET! sequential spot number */
      rowNbr= 0;               /* RESET row # of file */
    boolean hasMultDatasetsFlag= mcd.hasMultDatasetsFlag;
    
    /* Finite state machine parser */
    int
      LF_HEADER_LINE= 0,            /* i.e. names of samples if exists */
      LF_SAMPLE_ROW= 1,             /* i.e. names of samples if exists */
      LF_FIELD_ROW= 2,              /* i.e. names of fields */
      LF_DATA_ROWS= 3,	        /* in raw data rows */
      lookForState= LF_HEADER_LINE; /* current lookFor state */
    
    /* [1] Set proper geometry */
    if(mcd.rowWithSamples!=0)
      lookForState= LF_SAMPLE_ROW;
    else if(mcd.rowWithFields!=0)
      lookForState= LF_FIELD_ROW;
    
    /* [1.1] Do special file preparation if using separate GIPO files */
    if(mcd.hasSeparateGIPOandQuantFilesFlag)
    {
      lookForState= LF_DATA_ROWS;  /* we will position data at this point*/
      /* [TODO] open GIPO file and position at start of data */
    }
    
    /* [1.2] Set up array of datums' to hold parsed data elements
     * If an entry is null, that is the end of the list.
     */
    datum= new Element[mcd.maxRowsExpected+2];	//[TODO] chk maybe wrong index?
    
    /* [1.3] Reset the max counters to zero. We will be finding
     * the maximum Location ID used.
     */
    mcd.highestID= 0;           /* highestID ("Location") seen */
    
    /* [2] Parse input file looking for rows that may or may not
     * exist depending on the setupLayout. If all rows are expected
     * then they will occur on:
     *    mcd.rowWithSamples
     *    mcd.rowWithFields
     *    mcd.commentToken
     *    mcd.rowWithData
     */
    int
      linesInFile= mcd.linesInFile[n],             /* # of rows in file */
      quantSampleNbrInFile= mcd.quantSampleNbrInFile[n], /* col# where sample
                                                          * name resides */
      quantSrcFieldNbr= mcd.quantSrcFieldNbr[n],   /* col# where field
                                                    * name resides */
      nextQuantSrcFieldNbr= mcd.quantSrcFieldNbr[n+1]; /* col# where field name resides
                                                        * for NEXT sample */
    /* Open Quant src file set by file input dialog entry stuffed in these
     * data  structure when the user types it in.
     */
    String filename= mcd.quantSrcDir[n]+mcd.quantSrcFile[n];  
   
    /* Get the Quant data field names for this file and compute expected
     * FieldGQ length.
     */
    FileTable fioQ= new FileTable("GetGIPOandQuantInputFields");
    dataQ= fioQ.readTableFieldsFromFile(filename, mcd.rowWithFields);
    if(dataQ==null)
    {
      util.logMsg("Error: unable to read ["+filename+"]", Color.red);
      return(false);
    }
    
    dataG= (!mcd.hasSeparateGIPOandQuantFilesFlag)
             ? null
             : fioQ.readTableFieldsFromFile(mcd.separateGIPOinputFile, 
                                            mcd.rowWithSepGIPOFields);
    nDataFieldsQ= dataQ.length;
    nSepGIPOfields= mcd.nSepGIPOfields;
    nDataFieldsGQ= nDataFieldsQ+mcd.nSepGIPOfieldsUsed; /* RESET # of fieldsGQ/array */
    fieldNamesGQ= new String[nDataFieldsGQ];
    
    /* Setup fieldNames[] as data from FieldGQ from lineG and lineQ */
    for(int k=0;k<nDataFieldsQ;k++)
      fieldNamesGQ[k]= dataQ[k];
    nDataFieldsGQ= nDataFieldsQ;
    for(int k=0;k<nSepGIPOfields;k++)
      if(mcd.sepGIPOfieldsUsed[k])
        fieldNamesGQ[nDataFieldsGQ++]= dataG[k];
    
    useTokFlagGQ= mcd.fm.genUseTokFlags(fieldNamesGQ);
    
    if(cvt.DBUG_READ_WRITE)
      util.logMsg("DIO-RD[2] n="+n+" filename='"+filename+"'"+
                  cvt.newline + "  quantSampleNbrInFile="+quantSampleNbrInFile+
                  cvt.newline + "  quantSrcFieldNbr="+quantSrcFieldNbr+
                  cvt.newline + "  nextQuantSrcFieldNbr="+nextQuantSrcFieldNbr+
                  cvt.newline + "  linesInFile="+linesInFile+
                  cvt.newline + "  rowWithSamples="+mcd.rowWithSamples+
                  cvt.newline + "  rowWithFields="+mcd.rowWithFields+
                  cvt.newline + "  rowWithSepGIPOFields="+ mcd.rowWithSepGIPOFields+
                  cvt.newline + "  rowWithSepGIPOData="+ mcd.rowWithSepGIPOData+
                  cvt.newline + "  nDataFieldsQ="+nDataFieldsQ+
                  cvt.newline + "  nSepGIPOfields="+nSepGIPOfields+
                  cvt.newline + "  nDataFieldsGQ="+nDataFieldsGQ+
                  cvt.newline + "  commentToken='"+mcd.commentToken+"'"+
                  cvt.newline+ "  initialKeyword='"+mcd.initialKeyword+"'",
                  Color.magenta);

    /* [2.1] Read and parse data line by line */
    try
    { /* read and parse data */
      if(mcd.hasSeparateGIPOandQuantFilesFlag)
      { /* Read two files: FieldG and FieldQ */
        nSepGIPOfields= mcd.nSepGIPOfields;
        /* Open GIPO file and position at start of data */
        fileG= new FileReader(mcd.separateGIPOinputFile);
        bufferG= new BufferedReader(fileG,50000);
        fileQ= new FileReader(filename);
        bufferQ= new BufferedReader(fileQ,50000);
        
        /* Preposition files at start of data */
        int
          nFieldGrowsToSkip= mcd.rowWithSepGIPOData,
          nFieldQrowsToSkip= mcd.rowWithData;
        for(int j=1;j<nFieldGrowsToSkip;j++)
          lineG= bufferG.readLine();
        for(int j=1;j<nFieldQrowsToSkip;j++)
        {
          lineQ= bufferQ.readLine();
          rowNbr++;
        }
      } /* Read two files: FieldG and FieldQ */
      else
      { /* Read single file (FieldGQ) */
        fileGQ= new FileReader(filename);
        bufferGQ= new BufferedReader(fileGQ,50000);
      }
      
      if(cvt.DBUG_READ_WRITE)
        util.logMsg("DIO-ReadData()[2].1 n="+n+" filename='"+filename+"'"+
                    "\n lineQ='"+lineQ+"'"+
                    "\n lineG='"+lineG+"'"+
                    "\n line='"+line+"'",
                    Color.magenta);
      
      ptGQ= new ParseTable('\t',""); /* used in either case */
      
      while(true)
      { /* parse data line by line */
        if(mcd.hasSeparateGIPOandQuantFilesFlag)
        { /* Read two files: FieldG and FieldQ which are at same data line */
          lineG= bufferG.readLine();
          lineQ= bufferQ.readLine();
          if(lineG==null || lineQ==null)
          { /* make sure both are null together! */
            break;    /* finished reading the file */
          }
          
          /* Tokenize lineG into dataG[]. Note the fieldG was checked when
           * browsed the file so do not need to do it here.
           */
          int nFoundGIPO= ptGQ.getAllDelimTokens(lineG, dataG, true);
          /* Extract only fields from FieldG that want to add to FieldG to make FieldGQ */
          line= lineQ;
          for(int k= 0; k<nSepGIPOfields;k++)
            if(mcd.sepGIPOfieldsUsed[k])
            { /* add GIPO token at end of line */
              fieldGtok= ("\t" + dataG[k]);
              line += fieldGtok;
            }
          
          if(cvt.DBUG_READ_WRITE)
            util.logMsg("DIO-ReadData()[2].2 rowNBr="+rowNbr+
                        "\n lineQ='"+lineQ+"'"+
                        "\n lineG='"+lineG+"'"+
                        "\n line='"+line+"'",
                        Color.magenta);
          
        } /* Read two files: FieldG and FieldQ which are at same data line */
        else
        { /* Read single file (FieldGQ) */
          line= bufferGQ.readLine();
        }
        
        if(line==null)
        { /* all done - no more data */
          util.logMsg("End of data",Color.black);
          break;            /* break - no more data */
        }
        
        rowNbr++;         /* count rows starting at 1 */
        
        if(cvt.DBUG_READ_WRITE)
          util.logMsg("\nDIO-ReadData 2.0 rowNbr="+rowNbr+
                      " look4state="+lookForState+
                      "\n  line='"+line+"'");
        
        /* [2.1.1] look for header line  */
        if(lookForState==LF_HEADER_LINE)
        { /* found header */
          
          if(cvt.DBUG_READ_WRITE)
            util.logMsg("DIO-ReadData 2.1 LF_HEADER_LINE rowNbr="+rowNbr+
                        "\n  line='"+line+"'");
          
          sHeader= line;
          
         /* if multi samples flag set look for samples row, otherwise
          *  find fields row.
          */
          if(hasMultDatasetsFlag)
            lookForState= LF_SAMPLE_ROW;  /* get sample names */
          else
            lookForState= LF_FIELD_ROW;   /* get field names */
          continue;       /* ignore comment lines, goto next FSM */
        } /* found header */
        
        /* [2.1.2] look for sample row */
        else if(lookForState==LF_SAMPLE_ROW)
        { /* found list of samples row */
          if(cvt.DBUG_READ_WRITE)
            util.logMsg("\nDIO-ReadData() 2.2 LF_SAMPLE_ROW rowNbr="+rowNbr+
                        "\n  line='"+line+"'", Color.magenta);
          if(mcd.rowWithSamples>0 && rowNbr<mcd.rowWithSamples)
            continue;               /* keep looking */
          
          util.logMsg("Reading samples line ....", Color.black);
          nFound= ptGQ.getAllDelimTokens(line, tokArray, /* returned data*/
                                         cvt.rmvTrailingWhiteSpaceFlag );
          if(nFound==0)
            break;
          
          if(mcd.chkAndEditFieldNamesFlag)
          { /* do special handling if any */
            chkAndEditFieldNames(tokArray,"SampleNames");
          }
          
          nSamples= 0;             /* # of samples */
          for(int k=0;k<nFound;k++)
            if(tokArray[k].length()>0)
              nSamples++;          /* count samples */
          sampleNames= new String[nSamples];
          sampleFieldIdx= new int[nSamples];
          
          nSamples= 0;             /* recompute the list */
          for(int k=0;k<nFound;k++)
            if(tokArray[k].length()>0)
            { /* make the sample list */
              sampleNames[nSamples]= tokArray[k];
              sampleFieldIdx[nSamples++]= k;
            }
          
          lookForState= LF_FIELD_ROW;  /* now get the fields */
          continue;
        } /* found list of samples row */
        
        
        /* [2.1.3] look for fields row */
        else if(lookForState==LF_FIELD_ROW)
        { /* found list of fields row */
          if(cvt.DBUG_READ_WRITE)
            util.logMsg("\nDIO-ReadDdata() 2.3 LF_FIELD_ROW rowNbr="+rowNbr+
                        "\n line='"+line+"'", Color.magenta);
          if(mcd.rowWithFields>0 && rowNbr<mcd.rowWithFields)
            continue;               /* keep looking */
          
          if(mcd.initialKeyword.length()>0 &&
             !line.startsWith(mcd.initialKeyword))
          { /* bad data */
            util.logMsg("Error: when reading data file, can't find initial Field keyword='"+
                        mcd.initialKeyword+"', row #="+rowNbr+
                        "\n line='"+line+"'",
                        Color.red);
            break;
          } /* bad data */
          
          util.logMsg("Reading Fields line ....", Color.black);
          nDataFieldsGQ= ptGQ.getAllDelimTokens(line, tokArray, /* returned data*/
                                                cvt.rmvTrailingWhiteSpaceFlag );
          if(nDataFieldsGQ==0)
          { /* bad data */
            util.logMsg("Error: when reading Field row, # of fields is 0"+
                        "\n  line='"+line+"'", Color.red);
            break;
          } /* bad data */
          
          if(mcd.chkAndEditFieldNamesFlag)
          { /* do special handling if any */
            chkAndEditFieldNames(tokArray,"FieldNames");
          }          
          
          /* [2.1.3.1] look for fields row */
          fieldNamesGQ= new String[nDataFieldsGQ];
          for(int k=0;k<nDataFieldsGQ;k++)
            fieldNamesGQ[k]= tokArray[k];  /* save field names */
          
         /* [2.1.3.2]  Remap field where Sample starts to
          * indices for each .quant sample to be generated.
          */
          boolean mapOK= genMultIdxMapOfFieldNameData(fieldNamesGQ, 
                                                      nDataFieldsGQ, n);
          if(!mapOK)
            break;
          
          lookForState= LF_DATA_ROWS;  /* now get the data */
          continue;
        } /* found list of fields row */        
        
        /* [2.1.4] look for data row */
        else if(lookForState==LF_DATA_ROWS)
        { /* Data is NOT an initialKeyword */
          if(cvt.DBUG_READ_WRITE)
            util.logMsg("\nDIO-ReadData() 2.4 LF_DATA_ROWS rowNbr="+rowNbr+
                        "\n  line='"+line+"'", Color.magenta);
          if(mcd.commentToken.length()>0 &&
          line.startsWith(mcd.commentToken))
            continue;
          
          if(mcd.rowWithData>0 && rowNbr<mcd.rowWithData)
            continue;               /* keep looking */
          
          if(line.length()==0)
          {
            util.logMsg("Error empty row - missing data ....", Color.red);
            break;      /* break - no more data */
          }
          
          /* [2.1.4.1] Compute next (grid,row,col) */
          gCol++;
          if (gCol > mcd.maxGridCols)
          { /* go to next row */
            gCol= 1;   /* NOTE: not 0 since using it now */
            gRow++;
            if (gRow > mcd.maxGridRows)
            { /* go to next grid */
              gRow= 1;
              grid++;
            } /* go to next grid */
          } /* go to next row */          
          
          /* [2.1.4.2] Get all tokens tokArray[0:nFound-1] in one call. */
          nFound= ptGQ.getDelimTokens(line, useTokFlagGQ, /* tokens to use*/
                                      tokArray,           /* returned data*/
                                      cvt.rmvTrailingWhiteSpaceFlag,
                                      ""                  /* missingDataStr */
                                      );
          if(nFound==0)
            break;                /* no more data */
          
          if(!checkIfRequiredFieldsAreNull(useTokFlagGQ,tokArray))
            break;                /* no more data */
          
          if(expectedNbrTokens > 0 && nFound < expectedNbrTokens)
          { /* bad data */
            util.logMsg("Error: when reading data file,"+
                        " # data tokens ("+ nFound+
                        ") < # expected("+expectedNbrTokens+")"+
                        "\n row number="+rowNbr+
                        "\n line='"+line+"'",
                        Color.red);
            break;
          } /* bad data */
          
         /* [2.1.4.3] Analyze data and save in output variables.
          * Extract datum[cur] from row of token data.
          * If spotID is -1, then bad data
          * go report this and exit.
          */
          mcd.maxSpotNbr= ++spotNbr;   /* sequential spot number */
          int location= extractDatumFromDataRow(tokArray,rowNbr,spotNbr,
                                                line,grid,gRow,gCol);
          if(location==-1)
          { /* bad data */
            System.out.println(cvt.sExceptErr);
            util.logMsg(cvt.sExceptErr, Color.red);
            break;
            //return(false);    /* STOP!  bad row of data */
          }
          
          /* [2.1.4.4] Display where we are in the file */
          if (rowNbr%200==0)
          {
            util.logMsg("==> "+((n>0)? "" : "re-")+ "reading row #"+rowNbr+
                        " ["+mcd.quantSrcFile[n]+"]", Color.black);
            util.logMsg2("For sample #"+(n+1)+" ["+ mcd.quantName[n]+"]",
                         Color.black);
          }
        } /* Data is NOT an initialKeyword */
      } /* parse data line by line */
      
      if(fileG!=null)
        fileG.close();
      if(fileQ!=null)
        fileQ.close();
      if(fileGQ!=null)
        fileGQ.close();
    }  /* read and parse data */
    
    catch (IOException ioe)
    {
      gui.err.appendLog("Can't find file ["+filename+"]",true);
      return(false);
    }
    
      /* If using Location IDs, then sort Datum[1:mcd.maxRowsExpected]
       * by Location add infill spots and renumber
       * grid coordinates
       */
    if(idxLocation!=-1)
      updateInfillLocationIDs();
    
    return(true);
  } /* readData */
  
  
  /**
   * checkIfRequiredFieldsAreNull() - test if the Required fields are ""
   * Return true if the required fields are ""
   * @param useTokFlagGQ boolean array
   * @param tokArray String array
   * @return true if required fields are null, false if required fields are not null
   */
  private boolean checkIfRequiredFieldsAreNull(boolean useTokFlagGQ[],String tokArray[])
  { /* checkIfRequiredFieldsAreNull */
    if(idxRawIntensity!=-1 && tokArray[idxRawIntensity].equals(""))
      return(false);
    else if(idxRawIntensity1!=-1 && tokArray[idxRawIntensity1].equals(""))
      return(false);
    else
      return(true);
  } /* checkIfRequiredFieldsAreNull */
  
  
  /**
   * genMultIdxMapOfFieldNameData() - generate quant idx map of data from
   * field map and sames of input file fields.<BR>
   * [1] Generate a map of fields where Samples starts to the
   *     indices for each .quant sample to be generated.<BR>
   * [2] Generate useTokFlag[] from fieldNames[] and
   *     FieldMap data. This analyzes the field map and
   *     extracts only the data required.<BR>
   * [3] Setup lookup FieldName indices for mapping data.
   *     Note: for multiple samples/file this will remap
   *     things like rawIntensity by flaging other fields.<BR>
   * Return true if succeed.
   * @param fieldNamesGQ array of field names
   * @param nDataFields max number of data fields
   * @param n nth data file from 0
   * @return true if successful
   * @see FieldMap#genUseTokFlags
   * @see FieldMap#lookupUserIndex
   * @see UtilCM#logMsg
   * @see #setupFieldNameIndices
   */
  public boolean genMultIdxMapOfFieldNameData(String fieldNamesGQ[],
                                              int nDataFields, int n)
  { /* genMultIdxMapOfFieldNameData */
    if(fieldNamesGQ==null || nDataFields==0 ||n<0)
      return(false);
    
    int
      quantSrcFieldNbr= mcd.quantSrcFieldNbr[n], /* col# where field name resides */
      nextQuantSrcFieldNbr= mcd.quantSrcFieldNbr[n+1]; /* col# where field
                                                        * name resides for NEXT sample*/
    
    /* [1] Generate a map of fields where Samples start to the
     * indices for each .quant sample to be generated.
     */
    if(mcd.hasMultDatasetsFlag && quantSrcFieldNbr!=-1)
    { /* Remap field where Sample starts */
      /* E.g. for RawIntensity, etc. This will select
       * the field corresponding to the n'th instance
       * of the userFieldMap[] variable.
       * This are indicated in the ArrayLayout
       * FieldMap entries that start with a '*'.
       * We then offset each entry by quantSrcFieldNbr
       * Which is the column of the Sample Name.
       */
      FieldMap fm= mcd.fm;
      int
        idx,
        mapFieldCnt[]= new int[fm.nMap]; /* zero counters */
      
      for(int k=0;k<nDataFields;k++)
      { /* rmv all repeat instances except quantSrcFieldNbr */
        idx= fm.lookupUserIndex(fieldNamesGQ[k]); /* NOTE: is unique */
        if(idx==-1 || !fm.repeatMap[idx])
          continue;                             /* not a repeated field */
        else
          mapFieldCnt[idx]++; /* count it */
        //if(mapFieldCnt[idx] != quantSrcFieldNbr)
        
        /* Add "N.A." to begining of all fields
         * except for the current sample being analyzed.
         * This then prevents that field from being
         * used to when we get the data for the current
         * sample to stuff into the .quant file.
         */
        //if(k != quantSrcFieldNbr)
        boolean validFlag= (k>=quantSrcFieldNbr &&
                            (nextQuantSrcFieldNbr>k || n>=(mcd.nQuantFiles-1)));
        if(!validFlag)
          fieldNamesGQ[k]= "N.A." + fieldNamesGQ[k];
      } /* rmv all repeat instances except quantSrcFieldNbr */
    } /* Remap field where Sample starts */
    
    /* [2] Generate useTokFlag[] from fieldNamesGQ[] and FieldMap data.
     * This analyzes the field map and extracts only the data required.
     */
    useTokFlagGQ= mcd.fm.genUseTokFlags(fieldNamesGQ);
    if(useTokFlagGQ==null)
    { /* bad field map */
      util.logMsg("Error: DRYROT - illegal useTokFlagGQ[] map",  Color.red);
      return(false);
    } /* bad field map */
    
    /* [3] Setup lookup FieldName indices for mapping data.
     * Note: for multiple samples/file this will remap
     * things like rawIntensity by flaging other fields
     * with N.A.- prefix to keep them from matching.
     */
    setupFieldNameIndices();
    
    if(cvt.DBUG_FLAG)
    {
      System.out.println("DIO-GMIDMFND.2 after setupFieldNameIndices() call"+
                         "\n idxIdentifier="+idxIdentifier+
                         "\n idxGeneName="+idxGeneName+
                         "\n idxGenBankAcc="+idxGenBankAcc+
                         "\n idxRawIntensity="+idxRawIntensity+
                         "\n idxQualCheck="+idxQualCheck
                         );
    }
    return(true);
  } /* genMultIdxMapOfFieldNameData */
  
  
  /**
   * chkAndEditFieldNames() - check and edit Sample or Field names.
   * This does special handling required for various types of data.
   * Change the names in the tokArray[].
   * Return true if made a change.
   * @param tokArray array of tokens to search through
   * @param lookForOpr look for this
   * @return true if a change was made to the field
   */
  public boolean chkAndEditFieldNames(String tokArray[], String lookForOpr)
  { /* chkAndEditFieldNames */
    boolean madeChangeFlag= false;
    if(!mcd.chkAndEditFieldNamesFlag)
      return(false);
    int lth= tokArray.length;
    boolean
      isAffyFlag= mcd.vendor.equals("Affymetrix"),
      lookForSamplesFlag= lookForOpr.equals("SampleNames"),
      lookForFieldsFlag= lookForOpr.equals("FieldNames");
    String tok;

    if(cvt.DBUG_FLAG)
    {
      System.out.println("DIO.CAEFN.1 INITIAL DATA lookForOpr='"+lookForOpr+"'"+
                         " lth="+lth+" madeChangeFlag="+madeChangeFlag+
                         "\n isAffyFlag="+isAffyFlag+
                         " lookForSamplesFlag="+lookForSamplesFlag+
                         " lookForFieldsFlag="+lookForFieldsFlag);
      for(int i=0; i<lth;i++)
      { /* check tokens */
        tok= tokArray[i];
        if(tok==null)
          continue;
        System.out.println(" tokArray["+i+"]='"+tok+"'");
      }
    }
    
    for(int i=0; i<lth;i++)
    { /* check tokens */
      tok= tokArray[i];
      if(tok==null)
        continue;
      if(lookForSamplesFlag)
      {
        if(isAffyFlag && tok.equals("Description"))
        { /* remove "Description" in Samples Row - should be in field names*/
          tokArray[i]= "";               /* remove it */
          affyFieldDescriptionCol= i;    /* remember it for later */
        }
      }
      if(lookForFieldsFlag)
      {
        if(isAffyFlag && i==affyFieldDescriptionCol && tok.equals(""))
        { /* Add "Description" in Fields Row */
          tokArray[i]= "Description";     /* Add it */
        }
        if(isAffyFlag && i==0 && tok.equals(""))
        { /* Add "probe set" in Fields Row */
          tokArray[i]= "probe set";     /* Add it */
        }
      }
    } /* check tokens */
    
    if(cvt.DBUG_FLAG)
    {
      System.out.println("DIO.CAEFN.2 FINAL DATA lookForOpr='"+lookForOpr+"'"+
                         " lth="+lth+" madeChangeFlag="+madeChangeFlag);
      for(int i=0; i<lth;i++)
      { /* check tokens */
        tok= tokArray[i];
        if(tok==null)
          continue;
        System.out.println(" tokArray["+i+"]='"+tok+"'");
      }
    }
    
    return(madeChangeFlag);
  } /* chkAndEditFieldNames */
  
  
  /**
   * updateInfillLocationIDs() - if using Location IDs, then sort
   * datum[1:mcd.maxRowsExpected] by Location add infill spots
   * and renumber grid coordinates. This may grow it to
   * datum[1:mcd.maxRowsComputed]
   * where mcd.maxRowsComputed= mcd.highestID or spotNbr if no "Location".
   * @return true if updated
   * @see Element
   * @see PseudoArray
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   */
  private boolean updateInfillLocationIDs()
  { /* updateInfillLocationIDs */
    if(idxLocation==-1)
      return(false);             /* nothing to do */
    
    /* [1] Sort datum[] by Location so can merge data between files. */
    boolean isAlphaLocationFlag= true;
    try
    { /* determine if the Location is integer or alphanumeric */
      Integer.parseInt(datum[1].location);
      isAlphaLocationFlag= false;
    }
    catch(Exception e)
    { /* is alphanumeric - don't add INFILL spots [for FUTURE] */
      isAlphaLocationFlag= true;
    }
    
    /* [2] The Location ID is alpanumeric. Don't add INFILL entries
     * but do a QuickSort on Location.
     * [TODO] may need to add infill entries if merging data
     * from various input files (or existing databases) where
     * there are IDs missing from either of the files.
     */
    if(isAlphaLocationFlag)
    { /* Alphabetic Location ID */
      /* Sort datum[] by d.location */
      util.logMsg3("Sorting data by 'Location' IDs", Color.black);
      quickSortByLocStr(datum, 1, mcd.maxSpotNbr);
      return(true);         /* NOTE: no INFILLs added */
    } /* Alphabetic Location ID */
    
    /* [3] Is it an Integer Location, so add INFILLs to sorted list
     * where there are null entries. Need to renumber Grid
     * coordinates since adding more data. Update the mcd.XXX state.
     * [CHECK] do we update the al[] entry?
     */
    if(!isAlphaLocationFlag)
    { /* make new eDatum[0:highestID+1] list and sort datum[] */
      util.logMsg("Generating sorted 'Location' IDs with 'INFILL' spots.",
                  Color.magenta);
      
      /* [3.1] Renumber grid coordinates as well */
      PseudoArray psa= new PseudoArray(mcd.highestID);
      int
        maxGrids,
        maxGridRows,
        maxGridCols,
        maxRowsComputed;
      
      if(mcd.hasQuantXYcoordsFlag == false)
      {
        maxGrids= psa.maxGrids;
        maxGridRows= psa.maxGridRows;
        maxGridCols= psa.maxGridCols;
        maxRowsComputed= psa.maxRowsComputed;
        
        /* Replace it with possibly larger grid */
        mcd.maxGrids= maxGrids;
        mcd.maxGridRows= maxGridRows;
        mcd.maxGridCols= maxGridCols;
        mcd.maxRowsComputed= maxRowsComputed;
      }
      else
      {
        maxGrids= mcd.maxGrids;
        maxGridRows= mcd.maxGridRows;
        maxGridCols= mcd.maxGridCols;
        maxRowsComputed= mcd.maxRowsComputed;
      }
      /*
      if(cvt.bDBUG_FLAG)
        util.logMsg3("DIO-ILID.3.2 maxGrids="+maxGrids+
                    " maxGridRows="+maxGridRows+
                    " maxGridCols="+maxGridCols+
                    "\n mcd.highestID="+mcd.highestID,
                    Color.red);
       */
      
      /* [3.2] Copy datum[0:maxRowsEstimated] into
       * eDatum[0:highestID] by d.iLocation
       */
      String
        sInfillSpot= "*INFILL*",
        s1= "1",
        s0= "0",
        sBAD= "8";
      Element
        d,
        e,
        eDatum[]= new Element[mcd.highestID+1];
      int iLoc;
      
      for(int k=1;k<=mcd.maxSpotNbr;k++)
      { /* copy data that exists - note: NO Location 0 */
        d= datum[k];
        if(d==null)
        {
          /*
          if(cvt.DBUG_FLAG)
            util.logMsg3("DIO-ILID.3.2.1 datum[k="+k+"] is null", Color.red);
         */
          continue;
        }
        iLoc= d.iLocation;
        if(iLoc<1 || iLoc>mcd.highestID)
        {
          if(cvt.DBUG_FLAG)
            util.logMsg3("DIO-ILID.3.2.2 illegal datum[k="+k+
                         "].iLocation="+iLoc, Color.red);
          continue;
        }
        eDatum[iLoc]= d;
      }
      
      if(cvt.DBUG_FLAG)
        util.logMsg3("DIO-ILID.3.3 maxSpotNbr="+mcd.maxSpotNbr+
                     " mcd.highestID="+mcd.highestID, Color.red);
      
      /* [3.3] Regenerate e.(G,R,C) data */
      int
        gRow= 1,
        gCol= 0,
        grid= 1;
      for(int k=1;k<=mcd.highestID;k++)
      { /* insert new Grid, Row, Column data */
        e= eDatum[k];
        if(e==null)
        { /* add infill spot */
          e= new Element();
          eDatum[k]= e;
          e.spotNbr= ""+k;
          e.location= null;   /* mark as INFILL spot for now */
          e.iLocation= k;
        }
        
        e.field= ""+mcd.maxFields;
        
        gCol++;
        if (gCol > maxGridCols)
        { /* go to next row */
          gCol= 1;   /* NOTE: not 0 since using it now */
          gRow++;
          if (gRow > maxGridRows)
          { /* go to next grid */
            gRow= 1;
            grid++;
          } /* go to next grid */
        } /* go to next row */
        
        if(idxNAME_GRC!=-1)
          e.NAME_GRC= "GRID-   "+grid+"-R"+gRow+"C"+gCol;
        else
        { /* add at next free spot */
          e.grid= ""+grid;
          e.grid_row= ""+gRow;
          e.grid_col= ""+gCol;
        }
      } /* insert new Grid, Row, Column data */
      
      /*
      if(cvt.DBUG_FLAG)
        util.logMsg3("DIO-ILID.3.4 grid="+grid+" gRow="+gRow+" gCol="+gCol,
                     Color.red);
      */
      
     /* [3.4] Insert INFILL datums for missing data
      * for all eDatum[k]==null instances.
      */
      infillCnt= 0;
      for(int k=1;k<=mcd.highestID;k++)
      { /* insert INFILL datums for missing data */
        e= eDatum[k];
        if(e==null)
        {
          if(cvt.DBUG_FLAG)
            util.logMsg3("DIO-ILID.3.4.1 illegal eDatum["+k+"] is null", Color.red);
          continue;
        }
        
        if(e.location==null)
        { /* add infill spot */
          infillCnt++;
          /*
          if(cvt.DBUG_FLAG)
          {
            util.logMsg2("Infilling Location ["+k+"]", Color.magenta);
            util.logMsg3("Total # Infills: "+infillCnt, Color.magenta);
          }
          */
          
          e.location= ""+k;
          
          /* Put all BOGUS infill spots at same end spot */
          if(idxPlate!=-1)
            e.plate= e.grid;
          if(idxPlateRow!=-1)
            e.plate_row= e.grid_row;
          if(idxPlateCol!=-1)
            e.plate_col= e.grid_col;
          
          if(idxIdentifier!=-1)
            e.identifier= sInfillSpot;
          
          if(idxClone_ID!=-1)
            e.cloneID= sInfillSpot;
          if(idxGenBankAcc!=-1)
            e.genBankAcc= sInfillSpot;
          
          e.qualCheck= sBAD;             /* BAD */
          e.geneName= sInfillSpot;
          
          if(idxRawIntensity!=-1)
            e.rawIntensity= s1;
          if(idxRawIntensity1!=-1)
            e.rawIntensity1= s1;
          if(idxRawIntensity2!=-1)
            e.rawIntensity2= s1;
          if(idxRawBackground!=-1)
            e.rawBkgrd= s0;
          if(idxRawBackground1!=-1)
            e.rawBkgrd1= s0;
          if(idxRawBackground2!=-1)
            e.rawBkgrd2= s0;
          // is this for GIPO or Quant??
          if(idxQualCheck!=-1)
            e.qualCheck= sBAD;
          if(idxDetValue!=-1)
            e.detValue= s0;
          if(idxDiffCall!=-1)
            e.diffCall= s0;
          if(idxFoldChange!=-1)
            e.foldChange=s0;
          
        } /* add infill spot */
      } /* insert INFILL datums for missing data */
      
      /* [3.5] Use revised datum list */
      datum= eDatum;
    } /* make new eDatum[0:highestID+1] list and sort datum[] */
    
    util.logMsg("Finished infilling missing Locations", Color.black);
    util.logMsg2("Total Infills: "+infillCnt, Color.black);
    util.logMsg3("", Color.black);
    
    return(true);
  } /* updateInfillLocationIDs */
  
  
  /**
   * quickSortByIntLoc() - recursive sort the Element list
   * datum[].iLocation.  Based on the QuickSort method by James
   * Gosling from Sun's SortDemo applet
   * @param d array of Elements to sort
   * @param lo0 low index
   * @param hi0 high index
   */
  void quickSortByIntLoc(Element d[], int lo0, int hi0)
  { /* quickSortByIntLoc */
    int
      lo= lo0,
      hi= hi0,
      mid;
    Element t;
    
    if (hi0 > lo0)
    {  /* need to sort */
      mid= d[(lo0 + hi0)/2].iLocation;
      while(lo <= hi)
      { /* check if swap within range */
        while((lo < hi0) && (d[lo].iLocation < mid) )
          ++lo;
        while((hi > lo0) && (d[hi].iLocation > mid) )
          --hi;
        if(lo <= hi)
        {
          t= d[lo];
          d[lo]= d[hi];
          d[hi]= t;
          ++lo;
          --hi;
        }
      } /* check if swap within range */
      
      if(lo0 < hi)
        quickSortByIntLoc(d, lo0, hi);
      if(lo < hi0)
        quickSortByIntLoc(d, lo, hi0);
    } /* need to sort */
  } /* quickSortByIntLoc */
  
  
  /**
   * quickSortByLocStr() - sort the Element list datum[].location.
   * Note: d.location is a String and may not be a numeric string.
   * Based on the QuickSort method by James Gosling from Sun's
   * SortDemo applet
   * @param d array of Elements to sort
   * @param lo0 low index
   * @param hi0 high index
   */
  void quickSortByLocStr(Element d[], int lo0, int hi0)
  { /* quickSortByLocStr */
    int
      lo= lo0,
      hi= hi0;
    String mid;
    Element t;
    
    if (hi0 > lo0)
    {  /* need to sort */
      mid= d[(lo0 + hi0)/2].location;
      while(lo <= hi)
      { /* check if swap within range */
        while((lo < hi0) && (d[lo].location.compareTo(mid)<0) )
          ++lo;
        while((hi > lo0) && (d[hi].location.compareTo(mid)>0) )
          --hi;
        if(lo <= hi)
        {
          t= d[lo];
          d[lo]= d[hi];
          d[hi]= t;
          ++lo;
          --hi;
        }
      } /* check if swap within range */
      
      if(lo0 < hi)
        quickSortByLocStr(d, lo0, hi);
      if(lo < hi0)
        quickSortByLocStr(d, lo, hi0);
    } /* need to sort */
  } /* quickSortByLocStr */
  
  
  /**
   * addGenomicFieldsNamesFromDescr() - setup genomic ids field names from Description
   *
   * "Cluster Incl AW208667:uo62e02.x1 Mus musculus cDNA, 3  end /clone=IMAGE-2647130 /clone_end=3  /gb=AW208667 /gi=6514607 /ug=Mm.4609 /len=474"
   * Add field "Clone_ID" from /clone=IMAGE-2647130
   * Add field "GenBankAcc" from  /gb=AW208667
   * Add field "UniGeneID from  /ug=Mm.4609
   * Add field "LocusID" from  /gb=AW208667
   *
   * @return field names based on Description
   */
  public String addGenomicFieldsNamesFromDescr()
  { /* addGenomicFieldsNamesFromDescr */
    
    /* [TODO] - both adding new columns in field header and actual
     * data.  do at GIPO generation time. */
    
    if(!mcd.vendor.equals("Affymetrix"))
      return("");
    
    String sR= "Clone_ID\tGenBankAcc\tUniGeneID\tLocusID\t";
    
    return(sR);
  } /* addGenomicFieldsNamesFromDescr */
  
  
  /**
   * addGenomicDataFromDescr() - setup Affy genomic ids data from Description
   * "Cluster Incl AW208667:uo62e02.x1 Mus musculus cDNA, 3  end /clone=IMAGE-2647130 /clone_end=3 /gb=AW208667 /gi=6514607 /ug=Mm.4609 /len=474"
   * Add field "Clone_ID" from /clone=IMAGE-2647130
   * Add field "GenBankAcc" from  /gb=AW208667
   * Add field "UniGeneID from  /ug=Mm.4609
   * Add field "LocusID" from  /gb=AW208667
   * @param sDescr Description
   * @return field names based on Description
   */
  public String addGenomicDataFromDescr(String sDescr /* Description */)
  { /* addGenomicDataFromDescr */
    
    /* [TODO] - both adding new columns in field header and actual data.
     * do at GIPO generation time.*/
    
    if(!mcd.vendor.equals("Affymetrix") || sDescr==null || sDescr.length()==0)
      return("\t\t\t\t");
    
    String
      cloneData= "",
      genBankData= "",
      uniGeneData= "",
      locusIDData= "";
    
    /* [1] Try to get it from /xxx description first */
    int
      idxClone= sDescr.indexOf("/clone="), /* Possibliy flakey, seen some strange things with  the '/'*/
      idxGenBank= sDescr.indexOf("/gb="),
      idxUniGene= sDescr.indexOf("/ug="),
      idxLocusID= sDescr.indexOf("/gb="),
      nextField= affyFieldDescriptionCol,
      idxCloneEnd= -1,
      idxGenBankEnd= -1,
      idxUniGeneEnd= -1,
      idxLocusIDEnd= -1;
    
    /* [2] pull out the start of the str */
    String
      sClone= (idxClone!=-1) ? sDescr.substring(idxClone) : null,
      sGenBank= (idxGenBank!=-1) ? sDescr.substring(idxGenBank+4) : null,
      sUniGene= (idxUniGene!=-1) ? sDescr.substring(idxUniGene+4) : null,
      sLocusID= (idxLocusID!=-1) ? sDescr.substring(idxLocusID+4) : null;
    
    /* [3] find the next space and get the index of it so we can isolate the str we want via end index */
    if(sClone!=null)
      idxCloneEnd= sClone.indexOf(" ");
    if(sGenBank!=null)
      idxGenBankEnd= sGenBank.indexOf(" ");
    if(sUniGene!=null)
      idxUniGeneEnd= sUniGene.indexOf(" ");
    if(sLocusID!=null)
      idxLocusIDEnd= sLocusID.indexOf(" ");
    
    /* [4] Now pull out the ID */
    if(idxClone!=-1 && idxCloneEnd!=-1)
      cloneData= sClone.substring(0,idxCloneEnd);
    if(idxGenBank!=-1 && idxGenBankEnd!=-1)
      genBankData= sGenBank.substring(0,idxGenBankEnd);
    if(idxUniGene!=-1 && idxUniGeneEnd!=-1)
      uniGeneData= sUniGene.substring(0,idxUniGeneEnd);
    if(idxLocusID!=-1 && idxLocusIDEnd!=-1)
      locusIDData= sLocusID.substring(0,idxLocusIDEnd);
    
    /* [5] If /xxx fails, try to get it from the GBID = ... of description*/
    if(idxGenBank==-1)
    { /* pick up the GeneBank ID from the start of the Description */
      /* eg. "U85611 =HSU85611 Human DNA-PK ..." */
      int idxSpaceEqual= sDescr.indexOf(" =");      
      genBankData= (idxSpaceEqual==-1) ? "" : sDescr.substring(0,idxSpaceEqual);
      
      /* [5.1] use genbank ID to lookup locus link */
      if(idxLocusID==-1)
        locusIDData= genBankData;
    } /* pick up the GeneBank ID from the start of the Description */
    
    String sR= cloneData+"\t"+genBankData+"\t"+uniGeneData+"\t"+locusIDData+"\t";
    
    return(sR);
  } /* addGenomicDataFromDescr */
  
  
  /**
   * writeGipoData() - write out the MAExplorer GIPO data file xxxx.gipo
   * @return true if successful
   * @see CvtGUI#getChipsetStr
   * @see CvtGUI#getProjectStr
   * @see Element
   * @see TextFrame#appendLog
   */
  public boolean writeGipoData()
  { /* writeGipoData */
    mcd= cvt.mcd;                      /* set up state links */
    util= cvt.util;
    sul= cvt.sul;
    gui= cvt.gui;
    
    String
      chipsetStr= gui.getChipsetStr(),
      mkPrjStr= gui.getProjectStr(),   /* get the project string if selected */
      fn,
      gipoFields= "",                  /* Fields for first line of GIPO file */
      newline,
      osName= System.getProperty("os.name");
    
    boolean isMacFlag= osName.equals("Mac OS");    
    if(isMacFlag)
      newline= System.getProperty("line.separator");
    else
      newline= "\n";
    
    try
    { /* write out the derived fields */
      /* [1] setup file writer */
      File outputFile= new File(mcd.gipoFile);
      FileWriter fw = new FileWriter(outputFile);
      
      /* [2] Synthesize GIPO list of Fields from mcd data */
      gipoFields= makeGipoFieldsTabDelimStr();
      fw.write(gipoFields + newline);
      
      /* [3] Write out GIPO data */
      int maxDatum= Math.min(mcd.highestID,(datum.length-1));
      
      if(cvt.DBUG_FLAG)
        System.out.println("DIO-WGD mcd.highestID="+mcd.highestID+
                           " datum.length="+datum.length+
                           " maxDatum="+maxDatum);
      
      util.logMsg2("", Color.black);
      util.logMsg3("", Color.black);
      
      for(int i=1;i<=maxDatum;i++)
      { /* write out row into each file */
        Element d= datum[i];
        if(d==null)
          break;            /* end of the list */
        
        if(cvt.DBUG_FLAG &&
        (i==maxDatum || d.location.equals("Location")))
          System.out.println("DIO-WGD i="+i+" maxDatum="+maxDatum+
                           " d="+d);
        
        /* [4] generate data String */
        String sGipo= cvtGipoDatumToTabDelimStr(d);
        
        fw.write(sGipo + newline);
        
        if (i%200==0)
          util.logMsg("==> writing GIPO row #"+i, Color.black);
      } /* write out row into each file */
      
      /* [5] Close the output files */
      fw.close();      
    } /* write out the derived fields */
    
    catch(IOException ioe)
    {
      gui.err.appendLog("Couldn't write GIPO file ["+mcd.gipoFile+"]");
      return(false);
    }
    
    return(true);
  } /* writeGipoData */
  
  
  /**
   * writeQuantData() - write out the MAExplorer Quant data files
   * These include the set of xxxxnnn.quant files
   * @param n is the nth quant file to write
   * @return true if successful
   * @see CvtGUI#getChipsetStr
   * @see CvtGUI#getProjectStr
   * @see TextFrame#appendLog
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   * @see #cvtQuantDatumToTabDelimStr
   * @see #makeQuantFieldsTabDelimStr
   */
  public boolean writeQuantData(int n)
  { /* writeQuantData */
    mcd= cvt.mcd;                   /* set up state links */
    util= cvt.util;
    sul= cvt.sul;
    gui= cvt.gui;
    
    String
      quantFile= mcd.quantDstFile[n],
      chipsetStr= gui.getChipsetStr(),
      mkPrjStr= gui.getProjectStr(), /* get the project string if selected */
      fn,
      quantFields= "",               /* Fields for first line of Quant file */
      newline,
      osName= System.getProperty("os.name");
    
    boolean isMacFlag= osName.equals("Mac OS");
    if(isMacFlag)
      newline= System.getProperty("line.separator");
    else
      newline= "\n";
    
    try
    { /* write out the derived fields */      
      File outputFile= new File(quantFile);
      FileWriter  fw = new FileWriter(outputFile);     
      
      /* [2] Synthesize List of active Quant fields from mcd data */
      quantFields= makeQuantFieldsTabDelimStr();
      
      /* [3] Now write out the data */
      fw.write(quantFields + newline);
      
      int maxDatum= Math.min(mcd.highestID,(datum.length-1));
      
      if(cvt.DBUG_FLAG)
        System.out.println("C2M-WQD mcd.highestID="+mcd.highestID+
                           " datum.length="+datum.length+
                           " maxDatum="+maxDatum);
      
      util.logMsg2("", Color.black);
      util.logMsg3("", Color.black);
      
      for(int i=1;i<=maxDatum;i++)
      { /* write out row into each file */
        Element  d= datum[i];
        if(d==null)
          break;            /* end of the list */
        
        /* Note: tabs are NOT needed for delim in the last col for the
         * final row, but tabs needed for all delim between the rest
         * of the cols.
         */
        String sQuantTab= cvtQuantDatumToTabDelimStr(d);
        
        fw.write(sQuantTab + newline);
        
        if(i%200==0)
          util.logMsg("==> writing Quant row #"+i+" for sample #"+(n+1)+" ["+
                      mcd.quantName[n]+"]", Color.black);
      } /* write out row into each file */
      
      /* [4] Close the output files */
      fw.close();
    } /* write out the derived fields */
    
    catch(IOException ioe)
    {
      if(cvt.DBUG_FLAG)
        System.out.println("DIO-WQD exception ioe='"+ioe+"'");
      gui.err.appendLog("Couldn't write Quant file ["+quantFile+"]");
      return(false);
    }
    
    return(true);
  } /* writeQuantData */
  
  
  /**
   * makeGipoFieldsTabDelimStr() - make tab-delimited GIPO Fields string
   * @return tab-delimited GIPO Fields string
   * @see #addGenomicFieldsNamesFromDescr
   */
  public String makeGipoFieldsTabDelimStr()
  { /* makeGipoFieldsTabDelimStr */
    String s= "SpotNbr\t";
    
    if(idxLocation!=-1)
      s += "Location\t";
    
    if(idxField!=-1)
      s += "field\t";
    if(idxNAME_GRC!=-1)
      s += "NAME_GRC\t";
    else
      s += "grid\tgrid col\tgrid row\t";
    
    if(idxPlate!=-1)
      s += "plate\t";
    if(idxPlateCol!=-1)
      s += "plate col\t";
    if(idxPlateRow!=-1)
      s += "plate row\t";
    
    if(idxQualCheckGIPO!=-1)
      s += "QualCheck\t";
    
    if(idxIdentifier!=-1)
      s += "Identifier\t";
    
    if(!mcd.chkAndEditFieldNamesFlag && idxClone_ID!=-1)
      s += "Clone ID\t";
    
    if(!mcd.chkAndEditFieldNamesFlag && idxUnigene_cluster_ID!=-1)
      s += "Unigene cluster ID\t";       /* may be long */
    
    if(!mcd.chkAndEditFieldNamesFlag && idxGenBankAcc!=-1)
      s +="GenBankAcc\t";
    if(idxGenBankAcc3!=-1)
      s += "GenBankAcc3\t";
    if(idxGenBankAcc5!=-1)
      s += "GenBankAcc5\t";
    
    if(idxDbEst3!=-1)
      s += "dbEst3\t";
    if(idxDbEst5!=-1)
      s += "dbEst5\t";
    
    if(idxSwissProtID!=-1)
      s += "SwissProtID\t";
    
    if(!mcd.chkAndEditFieldNamesFlag && idxLocusLinkID!=-1)
      s += "LocusID\t";
    
    if(idxUnigene_cluster_name!=-1)
      s += "Unigene cluster name\t";     /* may be long */
    
    if(idxGeneName!=-1)
      s += "GeneName\t";                /* make last since longest field */
    
    if(mcd.chkAndEditFieldNamesFlag)
      s += addGenomicFieldsNamesFromDescr();
    
    if(s.endsWith("\t"))
      s= s.substring(0,s.length()-1);   /* remove tailing "\t" */
    
    return(s);
  } /* makeGipoFieldsTabDelimStr */
  
  
  /**
   * makeQuantFieldsTabDelimStr() - make tab-delimited Quant Fields string
   * @return tab-delimited Quant Fields string
   */
  public String makeQuantFieldsTabDelimStr()
  { /* makeQuantFieldsTabDelimStr */
    String s= "SpotNbr\t";
    
    if(idxLocation!=-1)
      s += "Location\t";
    if(idxField!=-1)
      s += "field\t";
    if(idxNAME_GRC!=-1)
      s += "NAME_GRC\t";
    else
      s += "grid\tgrid col\tgrid row\t";
    
    if(idxRawIntensity!=-1)
      s += "RawIntensity\t";
    if(idxRawIntensity1!=-1)
      s += "RawIntensity1\t";
    if(idxRawIntensity2!=-1)
      s += "RawIntensity2\t";
    
    if(idxRawBackground!=-1)
      s += "Background\t";
    if(idxRawBackground1!=-1)
      s += "Background1\t";
    if(idxRawBackground2!=-1)
      s += "Background2\t";
    
    if(idxQualCheck!=-1)
      s += "QualCheck\t";
    
    if(idxDetValue!=-1)
      s += "DetValue\t";
    if(idxDiffCall!=-1)
      s += "DiffCall\t";
    if(idxFoldChange!=-1)
      s+= "FoldChange\t";
    
    if(s.endsWith("\t"))
      s= s.substring(0,s.length()-1);   /* remove tailing "\t" */
    
    return(s);
  } /* makeQuantFieldsTabDelimStr */
  
  
  /**
   * cvtUserDataToQualCheck()  map user data range to valid QualCheck range
   * @param qcData QualCheck data
   * @return qcData, else if GenePix data then bad spot data
   */
  private String cvtUserDataToQualCheck(String qcData)
  { /* cvtUserDataToQualCheck */
    /* [TODO] clean up this mess with proper mappings...*/
    if(mcd.vendor.equals("Axon"))
    { /* [HACK] for GenePix bad spot data */
      int  i;
      try
      {
        i= java.lang.Integer.parseInt(qcData);
        if(i<0)
          return("8");    /* C_BAD_SPOT= 8 */
        else
          return("2");    /* C_GOOD_MID= 2 */
      }
      catch(NumberFormatException e)
      {
        return(qcData);
      }
    } /* [HACK] for GenePix bad spot data */
    else
      return(qcData);
  } /* cvtUserDataToQualCheck */
  
  
  /**
   * cvtGipoDatumToTabDelimStr() - convert Element to tab-delimited GIPO string
   * @param d Element to convert
   * @return tab-delimited GIPO string
   * @see #addGenomicDataFromDescr
   * @see #cvtUserDataToQualCheck
   */
  public String cvtGipoDatumToTabDelimStr(Element d)
  { /* cvtGipoDatumToTabDelimStr */
    String s= d.spotNbr+"\t";
    
    if(idxLocation!=-1)
      s += d.location+"\t";
    
    if(idxField!=-1)
      s += d.field+"\t";
    
    if(idxNAME_GRC!=-1)
      s += d.NAME_GRC+"\t";
    else
      s += d.grid+"\t" + d.grid_col+"\t" + d.grid_row+"\t";
    
    if(idxPlate!=-1)
      s += d.plate+"\t";
    if(idxPlateCol!=-1)
      s += d.plate_col+"\t";
    if(idxPlateRow!=-1)
      s += d.plate_row+"\t";
    
    if(idxQualCheckGIPO!=-1)
    {
      String
      qcData= cvtUserDataToQualCheck(d.qualCheck);
      s += qcData+"\t";
    }
    
    if(idxIdentifier!=-1)
      s += d.identifier+"\t";
    
    if(!mcd.chkAndEditFieldNamesFlag && idxClone_ID!=-1)
      s += d.cloneID+"\t";
    
    if(!mcd.chkAndEditFieldNamesFlag && idxUnigene_cluster_ID!=-1)
      s += d.unigene_cluster_ID+"\t";
    
    if(!mcd.chkAndEditFieldNamesFlag && idxGenBankAcc!=-1)
      s += d.genBankAcc+"\t";
    if(idxGenBankAcc3!=-1)
      s += d.genBankAcc3+"\t";
    if(idxGenBankAcc5!=-1)
      s += d.genBankAcc5+"\t";
    
    if(idxDbEst3!=-1)
      s += d.dbEst3+"\t";
    if(idxDbEst5!=-1)
      s += d.dbEst5+"\t";
    if(idxSwissProtID!=-1)
      s += d.swissProtID+"\t";
    
    if(!mcd.chkAndEditFieldNamesFlag && idxLocusLinkID!=-1)
      s += d.locusLinkID+"\t";
    
    if(idxUnigene_cluster_name!=-1)
      s += d.unigene_cluster_name+"\t";
    
    if(idxGeneName!=-1)
      s += d.geneName+"\t";
    
    if(mcd.chkAndEditFieldNamesFlag)
      s += addGenomicDataFromDescr(d.geneName);
    
    if(s.endsWith("\t"))
      s= s.substring(0,s.length()-1);   /* remove tailing "\t" */
    
    return(s);
  } /* cvtGipoDatumToTabDelimStr */
  
  
  /**
   * cvtQuantDatumToTabDelimStr() - convert Element to tab-delimited Quant string
   */
  public String cvtQuantDatumToTabDelimStr(Element d)
  { /* cvtQuantDatumToTabDelimStr */
    String s= d.spotNbr+"\t";
    
    if(idxLocation!=-1)
      s += d.location+"\t";
    
    if(idxField!=-1)
      s += d.field+"\t";
    
    if(idxNAME_GRC!=-1)
      s += d.NAME_GRC+"\t";
    else
      s += d.grid+"\t" + d.grid_col+"\t" + d.grid_row+"\t";
    
    if(idxRawIntensity!=-1)
      s += d.rawIntensity+"\t";
    if(idxRawIntensity1!=-1)
      s += d.rawIntensity1+"\t";
    if(idxRawIntensity2!=-1)
      s += d.rawIntensity2+"\t";
    
    if(idxRawBackground!=-1)
      s += d.rawBkgrd+"\t";
    if(idxRawBackground1!=-1)
      s += d.rawBkgrd1+"\t";
    if(idxRawBackground2!=-1)
      s += d.rawBkgrd2+"\t";
    
    if(idxQualCheck!=-1)
    {
      String
      qcData= cvtUserDataToQualCheck(d.qualCheck);
      s += qcData+"\t";
    }
    
    if(idxDetValue!=-1)
      s += d.detValue+"\t";
    if(idxDiffCall!=-1)
      s += d.diffCall+"\t";
    if(idxFoldChange!=-1)
      s += d.foldChange+"\t";
    
    if(s.endsWith("\t"))
      s= s.substring(0,s.length()-1);   /* remove tailing "\t" */
    
    return(s);
  } /* cvtQuantDatumToTabDelimStr */
  
  
  /**
   * writeMAEstartupFile() - create MAE/Start.mae file
   * @return true if successful, false if error
   * @see MaeStartupData#writeMAEstartupFile
   */
  private boolean writeMAEstartupFile()
  { /* writeMAEstartupFile */
    return(mcd.msud.writeMAEstartupFile());
  } /* writeMAEstartupFile */
  
  
  /**
   * writeConfigFile() - create Config/MaExplorerConfig-fn.txt file
   * @return true if successful, false if error
   * @see MaeConfigData#writeConfigFile
   */
  private boolean writeConfigFile(MaeConfigData mcd)
  { /* writeConfigFile */
    return(mcd.writeConfigFile());
  } /* writeConfigFile */
  
} /* end of class DataIO */

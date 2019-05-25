/** File: SetupLayouts.java */

package cvt2mae;

import java.awt.*;
import java.awt.event.*;
import java.awt.List;
import java.io.*;
import java.util.*;

/**
 * This class sets upthe  array layouts from either preloads or files.
 * Cvt2Mae stores array layouts in  /ArrayLayouts as *.alo files.
 * When the user presses "Done" after Editing an array layout
 * (make sure it has a unique name) it should then copy that layout
 * to an .alo file and also move it to the top of the 
 * sul.alList[i].layoutName[] list and then update the GUI by redoing
 * the cvt.gui.chipsetChoice Choice widget and refresh the screen.
 * <P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), G. Thornwall (SAIC), B. Stephens(SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $   $Revision: 1.24 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class SetupLayouts 
{
  /** REMAP codes - remapping GIPO. Code of 0 means no remap in progress */
  final public static int
    REMAP_GIPO= 1;           
  /** REMAP codes - remapping QUANT. Code of 0 means no remap in progress */
  final public static int
    REMAP_QUANT= 2;
  /** REMAP codes - remapping SAMPLE. Code of 0 means no remap in progress */
  final public static int
    REMAP_SAMPLE= 3;
  /** Global link to Cvt2Mae instance */
  public Cvt2Mae 
    cvt;
  /** Global link to MaeConfigData instance */
  public MaeConfigData		
    mcd;     
  /** Global link to UtilCM instance */
  public UtilCM
    util;
  /** [0:maxAL-1] array layouts to choose from max of MAX_ARRAY_LAYOUTS */
  public ArrayLayout
    alList[];                 
  /** max # of array layouts in the ALO list */
  public int
    maxAL= 0;                 
  /** index of first default array layout in the ALO list */
  public int
    firstAL= 0;                   
  /** index of last default array layout in the ALO list */
  public int
    lastAL= 0;           
  /** array layout to use, -1 means failed */
  public int
    useAL= 0;                 
  /** set to true if read .alo files */
  public boolean 
    readALOflag= false;       
  /** [0:nIAused-1] image analysis methods to choose from max of 
   * MAX_IMG_ANALY_METHODS
   */
  public ImageAnalysisMethod
    iam[];                   
  /** max # of image analysis methods */
  public int
    maxIAM;                   
  /** method to use, -1 means failed */  
  public int
    useIAM;
  /** the index of the first user defined ALO. This is so we dont'
   * delete internally defined ALOs.
   */
  public int
    firstUserALO;
  /** # of all *.alo files found in the ArrayLayout/ directory */ 
  public int
    nALOfilesFound= 0;                
  /** [0:nALOfilesFound-1]  *.alo ALO files found the the ArrayLayout/ 
   * directory */ 
  public String
    aloFile[];
  /** full path of AL file trying to read */    
  private String
    fullPathAloFile;          
  /** data elements from the input table */ 
  private Element
    datum[];                   
  /** expected # of tokens for this layout  if not 0. */
  private int 
    expectedNbrTokens;         
  /** Special Field Map for mapping our symbols see getMinReqFieldList() */
  public static FieldMap
    specialFM;                 
  /** ALO variable: Version # of .alo file */
  private String 
    aloFileVersion;            
  /** ALO variable: variables for parsed .alo file */
  private String 
    aloFileName;
  /** ALO variable: vendor making the array */
  private String 
    alo_vendor;
  /** ALO variable: name of the layout  */
  private String 
    alo_layoutName;
  /** ALO variable: species of sample */
  private String 
    alo_species;
  /** ALO variable: name of the quantification program */
  private String 
    alo_quantTool;
  /** ALO variable: UniGene 2 letter prefix (eg. "Hs" for human) */
  private String 
    alo_UniGeneSpeciesPrefix;
  /** ALO variable: optional comment token */
  private String 
    alo_commentToken;
  /** ALO variable: optional initial keyword in leftmost field  */
  private String 
    alo_initialKeyword;
  /** ALO variable: input file has multiple samples */
  private boolean
    alo_hasMultDatasetsFlag;
  /** ALO variable: specify (F,G,R,C) from # of spots in PseudoArray */
  private boolean 
    alo_specifyGeometryByNbrSpotsFlag;
  /** ALO variable: allow negative quant dat */
  private boolean 
    alo_allowNegQuantDataFlag;
  /** ALO variable: has background intensity data */ 
  public boolean
    alo_hasBkgrdDataFlag;    
  /** ALO variable: has actual quant XY coordinates data */ 
  public boolean
    alo_hasQuantXYcoordsFlag; 
  /** ALO variable: number of duplicate Fields of Grids/array  */
  private int 
    alo_maxFields;
  /** ALO variable: # of grids/field  */
  private int 
    alo_nGridsPerField;
  /** ALO variable: # of rows/grid */
  private int 
    alo_rowsPerGrid;
  /** ALO variable: # of columns/grid */
  private int 
    alo_colsPerGrid;
  /** ALO variable: maximum number of spots (elements or rows)/sample */
  private int 
    alo_maxRowsExpected;
  /** ALO variable: # of expected tokens */
  private int 
    alo_expectedNbrTokens;
  /** ALO variable: # of row of FieldQ input file that contains sample 
   * names */
  private int 
    alo_rowWithSamples;
  /** ALO variable: # of row of FieldQ input file that contains Field names */
  private int 
    alo_rowWithFields;
  /** ALO variable: # of row of FieldQ input file that contains data */
  private int 
    alo_rowWithData;
  /** ALO variable: # of row of FieldG separate GIPO input file that
   *  contains Field names */
  private int 
    alo_rowWithSepGIPOFields;
  /** ALO variable: # of row of FieldG separate GIPO input file that
   * contains data */
  private int 
    alo_rowWithSepGIPOData;
  /** ALO variable: use generated PseudoArray image */
  private boolean 
    alo_pseudoArrayFlag;
  /** ALO variable: data is ratio data (eg. Cy3/Cy5) vs. 
   *  single sample intensity data.
   */
  private boolean 
    alo_useRatioDataFlag;         
  /** flag: check and edit Samples and Field names data before use.
   * This is for handling the bad Affy fields data generated by the old Affy
   * software.
   */
  private boolean
    alo_chkAndEditFieldNamesFlag;    
  /** ALO variable: list of FieldMaps for the layout  */
  private String
    alo_desiredFields[];
  /** ALO variable: bit mcd.PROP_xxxx properties */
  private long
    alo_bitProps;   
  /** Optional GEO Platform ID */    
  private String
    alo_geoPlatformID;
  /** Default desired fields table with no data. This
   * may be added later.
   */    
  private final String
    defDesiredFields[]= {"Table name\tMAE field\tUser field"
    };    
    
    
    /**
     * SetupLayouts() - Constructor
     * @param cvt is instance of Cvt2Mae
     */
    public SetupLayouts(Cvt2Mae cvt)
    { /* SetupLayouts */
      this.cvt= cvt;
      mcd= cvt.mcd;
      util= cvt.util;
      specialFM= null;
    } /* SetupLayouts */
    
    
    /**
     * removeALOfileFromALOlist() - remove ALO entry if match file name
     * @param aloFName is name of .alo file
     */
    public boolean removeALOfileFromALOlist(String aloFName)
    { /* removeALOfileFromALOlist */
      int  idx= -1;
      for(int i=firstUserALO;i<maxAL;i++)
        if(aloFName.equals(alList[i].aloFileName))
        { /* found it */
          idx= i;
          break;
        }
      if(idx==-1)
        return(false);
      /* shorten the list */
      for(int i=idx;i<maxAL;i++)
        alList[i]= alList[i+1];
      return(true);
    } /* removeALOfileFromALOlist */
    
    
    /**
     * deleteOldStyleAffyALOnamesatV70() - hack to remove bogus .alo names from
     * the ArrayLayout directory so they don't appear in the list with the new names.
     * If the name is found, it removes it from the list & deletes the file.
     */
    private void deleteOldStyleAffyALOnamesatV70()
    { /* deleteOldStyleAffyALOnamesatV70 */
      int filesTocheck= nALOfilesFound;
      for(int i=0; i<filesTocheck; i++)
      {
        
        if(aloFile[i].equals("Affymetrix-Generic.alo"))
        {
          cvt.util.delFile(mcd.aloDir + "Affymetrix-Generic.alo");
          removeALOfileFromALOlist(aloFile[i]);
        }
        else if(aloFile[i].equals("Affymetrix-Mouse.alo"))
        {
          cvt.util.delFile(mcd.aloDir + "Affymetrix-Mouse.alo");
          removeALOfileFromALOlist(aloFile[i]);
        }
        else if(aloFile[i].equals("Affymetrix-Human.alo"))
        {
          cvt.util.delFile(mcd.aloDir + "Affymetrix-Human.alo");
          removeALOfileFromALOlist(aloFile[i]);
        }
        else if(aloFile[i].equals("Affymetrix-MouseGenDescr.alo"))
        {
          cvt.util.delFile(mcd.aloDir + "Affymetrix-MouseGenDescr.alo");
          removeALOfileFromALOlist(aloFile[i]);
        }
        else if(aloFile[i].equals("Affymetrix-HumanGenDescr.alo"))
        {
          cvt.util.delFile(mcd.aloDir + "Affymetrix-HumanGenDescr.alo");
          removeALOfileFromALOlist(aloFile[i]);
        }
        
        else if(aloFile[i].equals("Affymetrix-Generic-MAS-5.alo"))
        {
          cvt.util.delFile(mcd.aloDir + "Affymetrix-Generic-MAS-5.alo");
          removeALOfileFromALOlist(aloFile[i]);
        }
        else if(aloFile[i].equals("Affymetrix-MAS4-Generic.alo"))
        {
          cvt.util.delFile(mcd.aloDir + "Affymetrix-MAS4-Generic.alo");
          removeALOfileFromALOlist(aloFile[i]);
        }
        else if(aloFile[i].equals("Affymetrix-MAS4-Human.alo"))
        {
          cvt.util.delFile(mcd.aloDir + "Affymetrix-MAS4-Human.alo");
          removeALOfileFromALOlist(aloFile[i]);
        }
        else if(aloFile[i].equals("Affymetrix-MAS4-Mouse.alo"))
        {
          cvt.util.delFile(mcd.aloDir + "Affymetrix-MAS4-Mouse.alo");
          removeALOfileFromALOlist(aloFile[i]);
        }
      }/* remove older affy files that may be on user disk */
    } /* deleteOldStyleAffyALOnamesatV70 */
    
    
    /**
     * readArrayLayouts() - read list of known array layouts
     * from disk layout database directory.
     * Note: new types could be added. by just adding them to the
     * directory.
     *<PRE>
     * 1. read directory contents
     * 2. read the layouts into alList[1:maxAL-1]. Note: slot [0] is for
     * <User-defined> entry that is pushed before we get here so maxAL
     * starts at 1.
     *</PRE>
     * @return return true if you can set up at least ONE ALO, else
     *   return false if can't find or setup ANY of the layouts.
     * @see ArrayLayout
     * @see FileTable#readFileAsTable
     * @see FileTable
     * @see #getValueByName
     * @see #parseArrayLayoutToTableData
     */
    private boolean readArrayLayouts()
    { /* readArrayLayouts */
      /* [1] Read directory contents */
      /* [1.1] Get list of layout name .alo files and save them
       * into aloFile[0:nALOfilesFound-1] (with .alo extension).
       */
      File fAloDir= new File(mcd.aloDir);    /* current dir */
      String aloFileNamesOnDisk[]= fAloDir.list();   /* list of .alo files */;
      if(aloFileNamesOnDisk==null)
        return(false);                    /* no files */
      int nDir= aloFileNamesOnDisk.length;
      boolean differentVersionsFlag= false;
      
      nALOfilesFound= 0;
      aloFile= new String[nDir];  /* worst case size > than needed */
      FileTable fioDir= new FileTable("ALO table");
      
      /* [1.2] Make sure only get .alo files that are not BUILTINs! */
      for(int i=0;i<nDir;i++)
        if(aloFileNamesOnDisk[i].endsWith(".alo"))
        { /* only push unique ALOs on the disk */
          boolean  foundItFlag= false;
          for(int k=0;k<maxAL;k++)
            if(alList[k].aloFileName.equals(aloFileNamesOnDisk[i]))
            {
              foundItFlag= true;
              break;
            }
          if(! foundItFlag)
            aloFile[nALOfilesFound++]= aloFileNamesOnDisk[i];
        } /* only push unique ALOs on the disk */
      
      /* [1.3] Delete Older non-built in Affy files, special case since we changed to MAS4 and MAS5.
       * This is a hack to remove bogus .alo names from
       * the ArrayLayout directory so they don't appear in the list with the new names.
       * If the name is found, it removes it from the list & deletes the file.
       */
      deleteOldStyleAffyALOnamesatV70();
      
      /* [2] Read the layouts into alList[0:maxAL-1]. */
      for(int i=0;i<nALOfilesFound;i++)
      { /* read .alo file[i] as table and save into alList[i].xxx */
        /* read array layout data from file as FileTable
         * Note: data is 2 column table (Name, Value) so use FileTable.
         */
        fullPathAloFile= mcd.aloDir + aloFile[i];
        FileTable fio= new FileTable(aloFile[i]);
        
        if(!fio.readFileAsTable(fullPathAloFile))
        { /* problem with the file */
          differentVersionsFlag= true; /* force a dump of new version*/
          continue;                    /* can't read file */
        }
        
        /* Check if entry already exists in the alList[0:maxAL-1] DB
         * and if the version number is less than the current version
         * number. If this is the case, then use the new layout.
         */
        aloFileVersion= getValueByName(fio,"aloFileVersion");
        if(aloFileVersion==null)
        { /* check the version number */
          aloFileVersion= getValueByName(fio,"aloFileVersion");
          boolean thisALOversionIsDiffFlag= (!aloFileVersion.equals(cvt.ALO_VERSION));
          if(thisALOversionIsDiffFlag)
          { /* old version number - reread and try to merge data to new format */
            differentVersionsFlag= true;
            cvt.util.logMsg("Old ArrayLayout version ["+
                            aloFileVersion+
                            "] different from expected version ["+cvt.ALO_VERSION+"]",
                            Color.red);
            cvt.util.logMsg2("Ignoring ArrayLayout file ["+fullPathAloFile+"]",
                             Color.red);
            cvt.util.logMsg3("Using defaults & creating new ArrayLayout files.",
                             Color.red);

           /* [TODO] merge data from the old layout if possible to the
            * new layout for the same layout name. Do special version
            * dependent parsing.
            */            
          } /* old version number - reread and try to merge data to new format */
        } /* check the version number */
        
        
        /* Parse & save each alo table item using N,V into alList[i].xxx.
         * [Note] currently, it returns false if version number is
        * different
        */
        if(!parseArrayLayoutToTableData(fio))
        { /* bad table */
          continue;
        }
        
        int useIdx= -1;
        /* Try to find layout we just read in the default database */
        for(int j=0;j<maxAL;j++)
          if(alo_layoutName.equals(alList[j].layoutName))
          { /* found it - now overwrite the default with data from disk */
            useIdx= j;
            break;
          }
        
        /* If array layout on disk does not exist in the default database
         * possibly because the user defined it with <User-Defined>,
         * then just add it as a new array layout.
         */
        if(useIdx==-1)
          useIdx= maxAL++;
        
        /* Save the parsed data as a new instance */
        alList[useIdx]=
        new ArrayLayout(aloFileName, alo_vendor,
                        alo_layoutName, alo_species, alo_quantTool,
                        alo_UniGeneSpeciesPrefix, alo_commentToken,
                        alo_initialKeyword, alo_hasMultDatasetsFlag,
                        alo_specifyGeometryByNbrSpotsFlag,
                        alo_maxFields, alo_nGridsPerField,
                        alo_rowsPerGrid, alo_colsPerGrid,
                        alo_maxRowsExpected, alo_expectedNbrTokens,
                        alo_rowWithSamples, alo_rowWithFields, alo_rowWithData,
                        alo_rowWithSepGIPOFields, alo_rowWithSepGIPOData,
                        alo_pseudoArrayFlag,
                        alo_useRatioDataFlag,
                        alo_allowNegQuantDataFlag,
                        alo_chkAndEditFieldNamesFlag,
                        alo_hasBkgrdDataFlag,
                        alo_hasQuantXYcoordsFlag,
                        alo_bitProps,
                        alo_desiredFields,
                        alo_geoPlatformID);

        /*
        if(cvt.DBUG_FLAG && alo_layoutName.equals("Affymetrix - Human"))
          System.out.println("SL-RAL alList["+(maxAL-1)+"]="+
                             alList[maxAL-1].toString());
       */
      } /* read .alo file[i] as table and save into alList[i].xxx */
      
      if(maxAL>1)
        return(true);   /* read at least ONE .alo files */
      else
        return(false);  /* could not read ANY .alo files */
    } /* readArrayLayouts */
    
    
    /**
     * parseArrayLayoutToTableData() - parse (N,V) table data  to alo_XXXX vars.
     * @param ft is FileTable of array layouts
     * @return false if problem...
     * @see #getValueByName
     * @see #getValueByNameBool
     * @see #getValueByNameInt
     */
    private boolean parseArrayLayoutToTableData(FileTable ft)
    { /* parseArrayLayoutToTableData */
      if(ft==null)
        return(false);               /* only read the latest version */
      
      aloFileVersion= getValueByName(ft,"aloFileVersion");
      
      aloFileName= getValueByName(ft,"aloFileName");
      alo_vendor= getValueByName(ft,"vendor");
      
      alo_layoutName= getValueByName(ft,"layoutName");
      alo_species= getValueByName(ft,"species");
      alo_quantTool= getValueByName(ft,"quantTool");
      alo_UniGeneSpeciesPrefix= getValueByName(ft,"UniGeneSpeciesPrefix");
      
      alo_hasMultDatasetsFlag= getValueByNameBool(ft,"hasMultDatasetsFlag");
      alo_specifyGeometryByNbrSpotsFlag= getValueByNameBool(ft,
                                                            "specifyGeometryByNbrSpotsFlag");
      alo_allowNegQuantDataFlag= getValueByNameBool(ft,"allowNegQuantDataFlag");
      alo_chkAndEditFieldNamesFlag= getValueByNameBool(ft,"chkAndEditFieldNamesFlag");
      
      alo_maxFields= getValueByNameInt(ft,"maxFields");
      alo_nGridsPerField= getValueByNameInt(ft,"nGridsPerField");
      alo_rowsPerGrid= getValueByNameInt(ft,"rowsPerGrid");
      alo_colsPerGrid= getValueByNameInt(ft,"colsPerGrid");
      alo_maxRowsExpected= getValueByNameInt(ft,"maxRowsExpected");
      
      alo_expectedNbrTokens= getValueByNameInt(ft,"expectedNbrTokens");
      alo_pseudoArrayFlag= getValueByNameBool(ft,"pseudoArrayFlag");
      alo_useRatioDataFlag= getValueByNameBool(ft,"useRatioDataFlag");
      
      alo_rowWithSamples= getValueByNameInt(ft,"rowWithSamples");
      alo_rowWithFields= getValueByNameInt(ft,"rowWithFields");
      alo_rowWithData= getValueByNameInt(ft,"rowWithData");
      
      alo_rowWithSepGIPOFields= getValueByNameInt(ft,"rowWithSepGIPOFields");
      alo_rowWithSepGIPOData= getValueByNameInt(ft,"rowWithSepGIPOData");
      
      alo_initialKeyword= getValueByName(ft,"initialKeyword");
      alo_commentToken= getValueByName(ft,"commentToken");
      
      alo_bitProps= getValueByNameLong(ft,"bitProps");
      
      alo_geoPlatformID= getValueByName(ft,"geoPlatformID");
      
      /* Notes on the structure of the FieldMap entries.
       *
       * The FieldMap parser expects a 1D array of (*mT\tuT\tuF) entries.
       * However, because the .alo format is tab-delimited (N,V) strings,
       * we can not use tabs. The (*mT\tuT\tuF) was converted to the values
       * "(<mT>,<uT>,<uF>)" with the (N,V) entry being:
       *     fm.(mT,mF,uT,uF)[i]\t"(<mT>,<uT>,<uF>)"
       * and the '*' into a separate True/False as <flag> so the (N,V) entry is:
       *     fm.repeatFlag[i]\t<flag>       *
       * Note:
       *  1. the table definitions are specified in row [0].
       *  2. entries [1:n] are tab-delimited (NOT comma-delimited). However,
       *     when the FieldMap associated with an ArrayLayout is saved in the
       *     .alo file, it is converted to a comma-delimited string since tabs
       *     are reserved for the .alo (N,V) data.
       */
      String
        mTmFuTuFcomma,
        mTmFuTuFtab,
        sRepeatFlag,
        mTmFuTuFname,
        sField,
        sRepeatName,
        dsTmp[]= new String[100];
      boolean repeatFlag;
      int
        tCols= getValueByNameInt(ft,"fm.tCols"),  /* get actual size */
        nMap= getValueByNameInt(ft,"fm.nMap");    /* get actual size */
      String tField[]= new String[tCols];
      
      /*
      if(cvt.DBUG_FLAG)
        System.out.println("\n--------\nSL-PALTTD aloFileName="+aloFileName+
                           "  nMap=" + nMap+ "  tCols=" + tCols);
      */
      
      for(int i= 0;i<tCols;i++)
      { /* map individual fm.tFields[i] entries */
        sField= "fm.tFields["+i+"]";
        tField[i]= getValueByName(ft,sField);
      }
      
      for(int i= 0;i<nMap;i++)
      { /* map individual fm.(mT,mF,uT,uF)[i] entries */
        mTmFuTuFname= "fm.(mT,mF,uT,uF)["+i+"]";
        sRepeatName= "fm.repeatFlag["+i+"]";
        mTmFuTuFcomma= getValueByName(ft,mTmFuTuFname);
        repeatFlag= getValueByNameBool(ft,sRepeatName);
        mTmFuTuFtab= UtilCM.mapComma2Tab(mTmFuTuFcomma);
        sRepeatFlag= (repeatFlag) ? "*" : "";
        dsTmp[i]= sRepeatFlag + mTmFuTuFtab;
      /*
      if(cvt.DBUG_FLAG)
        System.out.println("SL-PALTTD.1 .alo entry["+i+"]"+
                           "\n  mTmFuTuFname='"+mTmFuTuFname+
                           "'\n  mTmFuTuFcomma='"+mTmFuTuFcomma+
                           "'\n  mTmFuTuFtab='"+mTmFuTuFtab+
                           "'\n  sRepeatName="+sRepeatName+
                           "' repeatFlag="+repeatFlag+
                           " sRepeatFlag='"+sRepeatFlag+
                           "'\n  dsTmp["+i+"]='"+dsTmp[i]+"'");
        */
      } /* map individual fm.(mT,mF,uT,uF)[i] entries */
      
      alo_desiredFields= new String[nMap+1];
      alo_desiredFields[0]= tField[0];
      for(int i=1;i<tCols;i++)
        alo_desiredFields[0] += "\t"+tField[i];
      for(int i= 0;i<nMap;i++)
        alo_desiredFields[i+1]= dsTmp[i];
      /*
      if(cvt.DBUG_FLAG)
        for(int i= 0;i<nMap;i++)
          System.out.println("SL-PALTTD.2 alo_desiredFields["+i+"]='"+
                             alo_desiredFields[i]+"'");
      */
      
      return(true);
    } /* parseArrayLayoutToTableData */
    
    
    /**
     * getValueByName() - get Value by Name for Table with Fields[0:1] = {Name","Value"}.
     * @param ft is FileTable
     * @param name to lookup
     * @return value if found, else null if failed.
     */
    private String getValueByName(FileTable ft, String name)
    { /* getValueByName */
      String
        tData[][]= ft.tData,
        sName,
        sValue;
      int tRows= ft.tRows;
      
      if(tData==null || tRows==0)
        return(null);
      
      for(int r=0; r<tRows; r++)
      { /* look for name in row instance and return Value */
        sName= tData[r][0];
        if(sName!=null)
          if(sName.equals(name))
          { /* found it */
            sValue= tData[r][1];
            return(sValue);
          }
      } /* look for name in row instance and return Value */
      
      return(null);
    } /* getValueByName*/
    
    
    /**
     * getValueByNameLong() - get long Value by Name
     * for Table with Fields[0:1] = {Name","Value"}.
     * @param ft is FileTable
     * @param name to lookup
     * @return value if found, else 0 if failed.
     * @see UtilCM#cvs2l
     * @see #getValueByName
     */
    private long getValueByNameLong(FileTable ft, String name)
    { /* getValueByNameLong */
      String sVal= getValueByName(ft, name);
      if(sVal==null)
        return(0);
      long iVal= UtilCM.cvs2l(sVal,0L);
      return(iVal);
    } /* getValueByNameLong */
    
    
    /**
     * getValueByNameInt() - get int Value by Name
     * for Table with Fields[0:1] = {Name","Value"}.
     * @param ft is FileTable
     * @param name to lookup
     * @return value if found, else 0 if failed.
     * @see UtilCM#cvs2i
     * @see #getValueByName
     */
    private int getValueByNameInt(FileTable ft, String name)
    { /* getValueByNameInt */
      String sVal= getValueByName(ft, name);
      if(sVal==null)
        return(0);
      int iVal= UtilCM.cvs2i(sVal,0);
      return(iVal);
    } /* getValueByNameInt */
    
    
    /**
     * getValueByNameBool() - get int Value by Name
     * for Table with Fields[0:1] = {Name","Value"}.
     * @param ft is FileTable
     * @param name to lookup
     * @return value if found, else false if failed.
     * @see UtilCM#cvs2b
     * @see #getValueByName
     */
    private boolean getValueByNameBool(FileTable ft, String name)
    { /* getValueByNameInt */
      String sVal= getValueByName(ft, name);
      if(sVal==null)
        return(false);
      boolean bVal= UtilCM.cvs2b(sVal,false);
      return(bVal);
    } /* getValueByNameBool */
    
    
    /**
     * cvArrayLayoutToTableDataStr() - make prettyprint (N,V) string
     * for writing to .alo file.
     * Note: no final CRLF.
     * @param al is array layout to use
     * @return String table for this array layout else null if not found
     * @see FieldMap
     */
    private String cvArrayLayoutToTableDataStr(ArrayLayout al)
    { /* cvArrayLayoutToTableDataStr */
      String
        sFM,
        sFMR,
        s= "Name\tValue";
      
      s += "\naloFileVersion\t" + cvt.ALO_VERSION;
      
      s += "\naloFileName\t" + al.aloFileName;
      
      s += "\nvendor\t" + al.vendor;
      
      s += "\nlayoutName\t" + al.layoutName;
      s += "\nspecies\t" + al.species;
      s += "\nquantTool\t" + al.quantTool;
      s += "\nUniGeneSpeciesPrefix\t" + al.UniGeneSpeciesPrefix;
      
      s += "\nhasMultDatasetsFlag\t" + al.hasMultDatasetsFlag;
      s += "\nspecifyGeometryByNbrSpotsFlag\t" +
      al.specifyGeometryByNbrSpotsFlag;
      s += "\nallowNegQuantDataFlag\t" + al.allowNegQuantDataFlag;
      s += "\nchkAndEditFieldNamesFlag\t" + al.chkAndEditFieldNamesFlag;
      
      s += "\nmaxFields\t" + al.maxFields;
      s += "\nnGridsPerField\t" + al.nGridsPerField;
      s += "\nrowsPerGrid\t" + al.rowsPerGrid;
      s += "\ncolsPerGrid\t" + al.colsPerGrid;
      s += "\nmaxRowsExpected\t" + al.maxRowsExpected;
      
      s += "\nexpectedNbrTokens\t" + al.expectedNbrTokens;
      s += "\npseudoArrayFlag\t" + al.pseudoArrayFlag;
      s += "\nuseRatioDataFlag\t" + al.useRatioDataFlag;
      
      s += "\nrowWithSamples\t" + al.rowWithSamples;
      s += "\nrowWithFields\t" + al.rowWithFields;
      s += "\nrowWithData\t" + al.rowWithData;
      s += "\ninitialKeyword\t" +  al.initialKeyword;
      s += "\ncommentToken\t" + al.commentToken;
      s += "\ngeoPlatformID\t" + al.geoPlatformID;
      
      s += "\nbitProps\t" + al.bitProps;
      
      /* Notes on the structure of the FieldMap entries.
       *
       * The FieldMap parser expects a 1D array of (*mT\tuT\tuF) entries.
       * However, because the .alo format is tab-delimited (N,V) strings,
       * we can not use tabs. The (*mT\tuT\tuF) was converted to the values
       * "(<mT>,<uT>,<uF>)" with the (N,V) entry being:
       *     fm.(mT,mF,uT,uF)[i]\t"(<mT>,<uT>,<uF>)"
       * and the '*' into a separate True/False as <flag> so the (N,V) entry is:
       *     fm.repeatFlag[i]\t<flag>       *
       * Note:
       *  1. the table definitions are specified in row [0].
       *  2. entries [1:n] are tab-delimited (NOT comma-delimited). However,
       *     when the FieldMap associated with an ArrayLayout is saved in the
       *     .alo file, it is converted to a comma-delimited string since tabs
       *     are reserved for the .alo (N,V) data.
       */
      FieldMap fm= al.fieldMap;
      
      s += "\nfm.fmNbr\t" + fm.fmNbr;
      s += "\nfm.mapName\t" + fm.mapName;
      s += "\nfm.nMap\t" + fm.nMap;
      s += "\nfm.tCols\t" + fm.tCols;
      
      /*
      if(cvt.DBUG_FLAG)
        System.out.println("\n--------\nSL-CALTTDS fm.fmNbr="+fm.fmNbr+
                           "  fm.mapName=" + fm.mapName+
                           "  fm.nMap=" + fm.nMap+
                           "  fm.tCols=" + fm.tCols);
      */
      
      for(int i=0;i<fm.tCols;i++)
      { /* add fields entries as "fm.tFields[i]\t<field name>" */
        sFM= "fm.tFields["+i+"]\t" +fm.tFields[i];
        s += "\n"+ sFM;
        /*
        if(cvt.DBUG_FLAG)
          System.out.println("SL-CALTTDS.1 fields["+i+"]='"+ sFM+"'");
        */
      }
      
      for(int i=0;i<fm.nMap;i++)
      { /* add tab-delimited map entries - but NOT Field[] names */
        /* Note: the tField[] names
         *    "Table name\tMAE field\tUser field"
         * and
         *    '*' repeat flag
         * are output separately.
         */
        String
          mT= fm.maeTableMap[i],
          mF= fm.maeFieldMap[i],
          uF= fm.userFieldMap[i],
          sRepeatFlag= (fm.repeatMap[i]) ? "TRUE" : "FALSE";
          sFM= "fm.(mT,mF,uT,uF)["+i+"]\t"+mT+","+mF+","+uF;
          sFMR= "fm.repeatFlag["+i+"]\t"+sRepeatFlag;
          s += ("\n"+ sFM);
          s += ("\n"+ sFMR);
        /*
        if(cvt.DBUG_FLAG)
          System.out.println("SL-CALTTDS.2 entries ["+i+"]\n '"+ sFM+"'"+
                             "\n '"+sFMR+"'");
        */
      }
      
      return(s);
    } /* cvArrayLayoutToTableDataStr */
    
    
    /**
     * checkAndMakeALOdir()- check for ArrayLayout/ directory tree.
     * If not found, make the directory
     * @param aloDir is location of ArrayLayout/ directory
     * @return true if successful.
     */
    private boolean checkAndMakeALOdir(String aloDir)
    { /* checkAndMakeALOdir */
      try
      {
        File f= new File(aloDir);
        
        if(!f.isDirectory())
          f.mkdirs();             /* make it */
        return(true);
      }
      
      catch (Exception e)
      {
        return(false);
      }
    } /* checkAndMakeALOdir */
    
    
    /**
     * writeArrayLayout() - write edited ArrayLayout 'a' else no-op.
     * Save file in the disk layout database directory.
     * Note: new types could be added. by just adding them to the directory.
     * @param a is array layout to write
     * @param logMsgFlag to log this transaction
     * @return true if write the array layout.
     * @see UtilCM#logMsg
     * @see UtilCM#logMsg2
     * @see UtilCM#logMsg3
     * @see #cvArrayLayoutToTableDataStr
     */
    public boolean writeArrayLayout(ArrayLayout a, boolean logMsgFlag)
    { /* writeArrayLayout */
      if(a==null || !a.saveArrayFlag)
        return(false);
      
      FileWriter aloFW;
      BufferedWriter aloBuf;
      PrintWriter aloPW;
      
      try
      { /* write out the derived fields */
        if(!checkAndMakeALOdir(mcd.aloDir))
          return(false);
        aloFW= new FileWriter(mcd.aloDir+a.aloFileName);
        aloBuf= new BufferedWriter(aloFW);
        aloPW= new PrintWriter(aloBuf);
        
        String data= cvArrayLayoutToTableDataStr(a);
        /*
        if(cvt.DBUG_FLAG && a.layoutName.equals("Affymetrix - Human"))
          System.out.println("SL-WAL a["+a.layoutName+"]="+
                             alList[maxAL-1].toString()+
                             "\n data="+data);
        */
        
        /* Write the table data */
        aloPW.println(data);
        
        /* Close the output files */
        aloBuf.close();
        if(logMsgFlag)
        {
          util.logMsg("Saved edited array layout ["+a.layoutName+"]",
          Color.black);
          util.logMsg2("to file ["+a.aloFileName+"]", Color.black);
        }
        
        a.saveArrayFlag= false;       /* done */
      } /* write out the derived fields */
      
      /* catch IO errors */
      catch(IOException ioe)
      {
        //System.out.println("SL-WAL - IO error, Couldn't write [" +aloFileName + "]\n");
        util.logMsg("Can't save array layout ["+a.layoutName+"]", Color.red);
        util.logMsg2("", Color.black);
        util.logMsg3("", Color.black);
        a.saveArrayFlag= false;       /* done */
        return(false);
      }
      
      return(true);
    } /* writeArrayLayout */
    
    
    /**
     * chkUniqueLayoutName() - return TRUE if layout name is unique.
     * @param layoutName to check
     * @return true if name is unique
     */
    private boolean chkUniqueLayoutName(String layoutName)
    { /* chkUniqueLayoutName */
      for(int i=0;i<maxAL;i++)
        if(alList[i].layoutName.equals(layoutName))
          return(false);
      return(true);
    } /* chkUniqueLayoutName */
    
    
    /**
     * setupArrayLayouts() - setup list of known array layouts.
     * Read from configuration file where new types could be added.
     * @return true if find or setup the layouts.
     * @see ArrayLayout
     * @see UtilCM#logMsg
     * @see UtilCM#logMsg2
     * @see UtilCM#logMsg3
     * @see #readArrayLayouts
     * @see #setupDefaultArrayLayouts
     * @see #writeArrayLayout
     */
    public boolean setupArrayLayouts()
    { /* setupArrayLayouts */
      alList= new ArrayLayout[cvt.MAX_ARRAY_LAYOUTS];
      maxAL= 0;
      useAL= -1;                  /* will be >= 0 if select chip */
      
      /* [1] Case 0 is special user-defined version */
      long defBitProps= (0);  /* no mcd.PROP_xxx bits */
      alList[maxAL++]= new ArrayLayout("",              /* aloFileName */
                                       "?",             /* vendor */
                                       "<User-defined>", /* array layout name */
                                       "?",             /* species */
                                       "?",             /* Quantitation tool */
                                       "?",             /* UniGeneSpeciesPrefix */
                                       "",              /* commentToken */
                                       "",              /* initialKeyword Field */
                                       true,            /* hasMultDatasetsFlag */
                                       true,            /* specifyGeometryByNbrSpotsFlag */
                                       0,               /* maxFields */
                                       0,               /* nGridsPerField */
                                       0,               /* rowsPerGrid */
                                       0,               /* colsPerGrid */
                                       0,               /* nElements in array */
                                       0,               /* expectedNbrTokens */
                                       0,               /* rowWithSamples */
                                       0,               /* rowWithFields */
                                       0,               /* rowWithData */
                                       0,               /* rowWithSepGIPOFields */
                                       0,               /* rowWithSepGIPOData */
                                       true,            /* is pseudo array layout */
                                       true,            /* is Cy3/Cy5 data */
                                       false,           /* allowNegQuantDataFlag */
                                       false,           /* chkAndEditFieldNamesFlag */
                                       false,           /* hasBkgrdDataFlag */
                                       false,           /* hasQuantXYcoordsFlag */
                                       defBitProps,     /* default bit props */
                                       defDesiredFields, /* list of desired fields */
                                       ""                /* geoPlatformID */
                                       );

      /* [2] Setup default layouts and then overide them
       * with data from the /ArrayLayouts/*.alo if it exists
       * and has a current version number.
       */
      util.logMsg("Creating Array Layout .alo files in", Color.black);
      util.logMsg2("Folder:  "+mcd.aloDir, Color.black);
      
      /* [2.1] Setup default list of array layouts list */
      setupDefaultArrayLayouts(); /* Use the hardwired values*/
      firstUserALO= maxAL;        /* Capture the end of the internal ALO list */
      
      /* [2.2] Try to read the ALOs from the /ArrayLayout/*.alo */
      readALOflag= readArrayLayouts();
      
      /* [2.3] Write out all layouts to ../ArrayLayout/*.alo files */
      for(int i=1;i<maxAL;i++)
      { /* put into .../ArrayLayout/ cache of .alo files */
        alList[i].saveArrayFlag= true;
        writeArrayLayout(alList[i], false);
      }
      
      /*
      if(cvt.DBUG_FLAG)
      {
        for(int i=1;i<maxAL;i++)
         if(alList[i].layoutName.equals("Affymetrix - Human"))
           System.out.println("SL-SAL alList["+i+"]="+alList[i].toString());
       }
      */
      
      return(true);
    } /* setupArrayLayouts */
    
    
    /**
     * setupDefaultArrayLayouts() - setup list of default array layouts
     * These are hardwired values.
     * Note: put generic types FIRST - then add special instances that
     * may contain other options such as species specific or in the
     * case of the Affy data, getting the genomic IDs from the  Description,
     * etc..
     * @see ArrayLayout
     * @see #chkUniqueLayoutName
     */
    private void setupDefaultArrayLayouts()
    { /* setupDefaultArrayLayouts */
      firstAL= maxAL;        /* capture the first default ALO */
      
      long defGenePixBitProps= (mcd.PROP_CLONEID | mcd.PROP_ALLOW_NEG_DATA |
                               mcd.PROP_BKGRD_DATA | mcd.PROP_XYCOORDS |
                               mcd.PROP_QUALCHK_SPOT_DATA);
      /* GenePix Fields:
       * Block	Column	Row	Name	ID	X	Y
       * Dia.	F635 Median	F635 Mean	F635 SD	B635 Median
       * B635 Mean	B635 SD	% > B635+1SD	% > B635+2SD	F635 % Sat.
       * F532 Median	F532 Mean	F532 SD	B532 Median	B532 Mean
       * B532 SD	% > B532+1SD	% > B532+2SD	F532 % Sat.
       * Ratio of Medians	Ratio of Means	Median of Ratios
       * Mean of Ratios	Ratios SD	Rgn Ratio	Rgn R²
       * F Pixels	B Pixels	Sum of Medians	Sum of Means
       * Log Ratio	F635 Median - B635	F532 Median - B532
       * F635 Mean - B635	F532 Mean - B532	Flags
       */
      String 
        desFieldsGenePix[]= {"Table name\tMAE field\tUser field",/* Table def */
                             "QuantTable\tRawIntensity1\tF532 Mean - B532",
                             "QuantTable\tBackground1\tB532 Mean",
                             "QuantTable\tRawIntensity2\tF635 Mean - B635",
                             "QuantTable\tBackground2\tB635 Mean",
                             "QuantTable\tGeneName\tName",
                             "QuantTable\tgrid\tBlock",
                             "QuantTable\tgrid col\tColumn",
                             "QuantTable\tgrid row\tRow",
                             "QuantTable\tX\tX",
                             "QuantTable\tY\tY",
                             "QuantTable\tQualCheck\tFlags",
                             "QuantTable\tCy3\tF532 Mean - B532",
                             "QuantTable\tCy5\tF635 Mean - B635",
                             "QuantTable\tCy3Bkg\tB532 Mean",
                             "QuantTable\tCy5Bkg\tB635 Mean",
                             "GipoTable\tClone ID\tID",
                             "GipoTable\tgrid\tBlock",
                             "GipoTable\tgrid col\tColumn",
                             "GipoTable\tGeneName\tName",
                             "GipoTable\tgrid row\tRow",
                           };

      if(chkUniqueLayoutName("GenePixPro3"))
        alList[maxAL++]= new ArrayLayout("GenePixPro3-Generic.alo",   /* aloFileName */
                                         "Axon",          /* vendor */
                                         "GenePixPro3 - generic",/* array layout name */
                                         "?",         /* species */
                                         "GenePix",       /* Quantitation tool */
                                         "?",            /* UniGeneSpeciesPrefix */
                                         "",              /* commentToken */
                                         "",              /* initialKeyword Field */
                                         false,           /* hasMultDatasetsFlag */
                                         false,           /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0,               /* expectedNbrTokens */
                                         0,               /* rowWithSamples */
                                         30,              /* rowWithFields */
                                         31,              /* rowWithData */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         true,            /* is Cy3/Cy5 data */
                                         true,            /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         true,            /* hasBkgrdDataFlag */
                                         true,            /* hasQuantXYcoordsFlag */
                                         defGenePixBitProps, /* bitProps */
                                         desFieldsGenePix, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );

      long defScanalyzeBitProps= (mcd.PROP_CLONEID | mcd.PROP_BKGRD_DATA |
                                  mcd.PROP_QUALCHK_SPOT_DATA |
                                  mcd.PROP_SEPARATE_GIPO_AND_QUANT_FILES);
      /* Scanalyze Fields:
       * GIPO (.GAL) file:
       *      Block Row Column ID Name
       * Scanalyze (.DAT) file:
       *    HEADER SPOT GRID TOP LEFT BOT RIGHT ROW COL CH1I CH1B CH1AB CH2I CH2B CH2AB
       *    SPIX BGPIX EDGE RAT2 MRAT REGR CORR LFRAT CH1GTB1 CH2GTB1 CH1GTB2 CH2GTB2
       *    CH1EDGEA CH2EDGEA FLAG CH1KSD CH1KSP CH2KSD CH2KSP
       *
       * [TODO]
       *   1. could compute (X,Y) as ((TOP+BOT)/2, (LEFT+RIGHT)/2) as the
       *      center of an elipse of width=(RIGHT-LEFT), height=(BOT-TOP).
       *   2. could estimate (nGridsPerField, rowsPerGrid, colsPerGrid) by
       *      scanning the input DAT files.
       * The Flag value is assigned by the ScanAnalyze program.
       */
      String
         desFieldsScanalyze[]= {"Table name\tMAE field\tUser field",  /* Table def */
                               "QuantTable\tRawIntensity1\tCH1I",
                               "QuantTable\tBackground1\tCH1B",
                               "QuantTable\tRawIntensity2\tCH2I",
                               "QuantTable\tBackground2\tCH2B",
                               "QuantTable\tGeneName\tName",
                               "QuantTable\tgrid\tGRID",
                               "QuantTable\tgrid col\tCOL",
                               "QuantTable\tgrid row\tROW",
                               "QuantTable\tQualCheck\tFLAG",
                               "QuantTable\tCy3\tCH1I",
                               "QuantTable\tCy5\tCH2I",
                               "QuantTable\tCy3Bkg\tCH1B",
                               "QuantTable\tCy5Bkg\tCH2B",
                               "GipoTable\tgrid\tGRID",
                               "GipoTable\tgrid col\tCOL",
                               "GipoTable\tgrid row\tROW",
                               "GipoTable\tClone ID\tID",
                               "GipoTable\tGeneName\tName"
                             };

      if(chkUniqueLayoutName("Scanalyze - generic"))
        alList[maxAL++]= new ArrayLayout("Scanalyze-Generic.alo", /* aloFileName */
                                        "?",             /* vendor */
                                        "Scanalyze - generic",     /* array layout name */
                                        "?",             /* species */
                                        "Scanalyze",     /* Quantitation tool */
                                        "?",             /* UniGeneSpeciesPrefix */
                                        "",              /* commentToken */
                                        "",              /* initialKeyword Field */
                                        false,           /* hasMultDatasetsFlag */
                                        false,           /* specifyGeometryByNbrSpotsFlag */
                                        1,               /* maxFields */
                                        0,               /* nGridsPerField */
                                        0,               /* rowsPerGrid */
                                        0,               /* colsPerGrid */
                                        0,               /* nElements in array */
                                        0,               /* expectedNbrTokens */
                                        0,               /* rowWithSamples */
                                        0,               /* rowWithFields */
                                        0,               /* rowWithData */
                                        0,               /* rowWithSepGIPOFields */
                                        0,               /* rowWithSepGIPOData */
                                        true,            /* is pseudo array layout */
                                        true,            /* is Cy3/Cy5 data */
                                        true,            /* allowNegQuantDataFlag */
                                        false,           /* chkAndEditFieldNamesFlag */
                                        true,            /* hasBkgrdDataFlag */
                                        true,            /* hasQuantXYcoordsFlag */
                                        defScanalyzeBitProps, /* bitProps */
                                        desFieldsScanalyze, /* list of desired fields */
                                        ""                /* geoPlatformID */
                                        );
      
      
      /* Fields for OLD MAS4.0 Generic Affymetrix 'Pivot Tab' data:
       * Row 1 contains "Expression Analysis: Pivot Tab"
       * Row 2 - ignored
       * Row 3 contains names of samples over the Avg Diff field.
       *
       * Row 4 contains field names
       * [0] probe set
       * [Repeated pairs] (Avg Diff, Abs Call), (Avg Diff, Abs Call),
       *                  (Diff Call, Fold Change)
       * [after quant data] Identifier
       * [at end] Description
       *
       * Row 5 to end contains data
       */
      long defAffyBitPropsMAS4= (mcd.PROP_GENBANKID | mcd.PROP_QUALCHK_SPOT_DATA |
                                 mcd.PROP_ALLOW_NEG_DATA);
      String
        desFieldsAffyMAS4[]= {"Table name\tMAE field\tUser field",  /* Table def */
                              "GipoTable\tLocation\tprobe set",       /* note: is ascii */
                              "*QuantTable\tRawIntensity\tAvg Diff",  /* '*' means repeated */
                              "*QuantTable\tQualCheck\tAbs Call",
                              "GipoTable\tGenBankAcc\tIdentifier",    /* alternate names */
                              //"GipoTable\tGenBankAcc\tAccession #", /* alternate names */
                              "GipoTable\tClone_ID\tProbe Set",
                              "GipoTable\tGeneName\tDescription"
      };

      if(chkUniqueLayoutName("Affymetrix - generic MAS4"))
        alList[maxAL++]= new ArrayLayout("Affymetrix-Generic-MAS4.alo", /* aloFileName */
                                         "Affymetrix",    /* vendor */
                                         "Affymetrix - generic MAS4",    /* array layout name */
                                         "?",             /* species */
                                         "MAS4.0",        /* Quantitation tool */
                                         "??",            /* UniGeneSpeciesPrefix */
                                         "",              /* commentToken */
                                         "",              /* initialKeyword Field */
                                         true,            /* hasMultDatasetsFlag */
                                         true,            /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0,               /* expectedNbrTokens */
                                         3,               /* rowWithSamples */
                                         4,               /* rowWithFields */
                                         0,               /* rowWithData */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         false,           /* is Cy3/Cy5 data */
                                         true,            /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         false,           /* hasBkgrdDataFlag */
                                         false,           /* hasQuantXYcoordsFlag */
                                         defAffyBitPropsMAS4, /* bitProps */
                                         desFieldsAffyMAS4, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );

      
      /* Fields for New MAS-5.0 Generic Affymetrix 'Pivot Tab' data:
       * NOTE: this is what the data looks like AFTER we convert using a special file
       * conversion routine  xxxx() that
       *  1. reads the yyyyyy.txt file as generated by MAS-5,
       *  2. generate a temp file yyyyy.c2mtmp
       *  3. read the yyyyyy.txt file and generate required data for the new formated file
       *  4. Then replace yyyyyy.txt with yyyyyy.c2mtmp so can use the current infrastructure
       *     to read and convert the data.
       *
       * Row 1 contains names of samples over the (Sample, Detection, Detection p-value) fields
       * Row 2 contains field names
       * Row 3 Contains the data
       * E.g. (bogus data)
       *            sample1		                           sample2  . . .
       *   Location Signal Detection "Detection p-value" Signal Detection "Detection p-value" ... Descriptions	GenBankAcc	SwissProt
       *   IL2_at   388.9	 A	       0.986335	           554.5	A         0.877746                M16762 Mouse interleukin 2 (IL-2) gene, exon 4	M16762	P12345
       *
       * Row 5 to end contains data.
       *
       * NOTE: the PROP_USE_2ND_INPUT_CHIP_FILE_FLAG bit sets the
       * mcd.use2ndInputChipFileFlag flag  if inputing paired chips
       * (e.g. Affy U133A and U133B chips) are used.
       *
       */
      long defAffyBitPropsMAS5= (mcd.PROP_GENBANKID | mcd.PROP_QUALCHK_SPOT_DATA |
                                 mcd.PROP_ALLOW_NEG_DATA |
                                 mcd.PROP_USE_SRC_TMP_FILE_FLAG |
                                 mcd.PROP_SWISSPROTID);
      String
        desFieldsAffyMAS5[]= {"Table name\tMAE field\tUser field",  /* Table def */
                              "QuantTable\tLocation\tprobe set",    /* note: is ascii */
                              "*QuantTable\tRawIntensity\tSignal",  /* '*' means repeated */
                              "*QuantTable\tQualCheck\tDetection",  /* extended Diff Call codes */
                              "*QuantTable\tDetValue\tDetection p-value",  /* only MAS5 */
                              "GipoTable\tLocation\tprobe set",     /* note: is ascii */
                              "GipoTable\tGeneName\tDescriptions"
                            };

      /* NOTE: this assumes multiple files */
      if(chkUniqueLayoutName("Affymetrix - generic MAS5"))
        alList[maxAL++]= new ArrayLayout("Affymetrix-Generic-MAS5.alo", /* aloFileName */
                                         "Affymetrix",    /* vendor */
                                         "Affymetrix - generic MAS5",    /* array layout name */
                                         "Human",         /* species */
                                         "MAS5.0",        /* Quantitation tool */
                                         "Hs",            /* UniGeneSpeciesPrefix */
                                         "",              /* commentToken */
                                         "",              /* initialKeyword Field */
                                         true,            /* hasMultDatasetsFlag */
                                         true,            /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0,               /* expectedNbrTokens */
                                         1,               /* rowWithSamples - AFTER map in Cvt2Mae */
                                         2,               /* rowWithFields - AFTER map in Cvt2Mae */
                                         3,               /* rowWithData - AFTER map in Cvt2Mae */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         false,           /* is Cy3/Cy5 data */
                                         true,            /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         false,           /* hasBkgrdDataFlag */
                                         false,           /* hasQuantXYcoordsFlag */
                                         defAffyBitPropsMAS5, /* bitProps */
                                         desFieldsAffyMAS5, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );
                            

      /* NOTE: this assumes multiple Affy MAS5.0 files */
      if(chkUniqueLayoutName("Affymetrix - generic separate files MAS5"))
        alList[maxAL++]= new ArrayLayout("Affymetrix-Generic-separate-MAS5.alo", /* aloFileName */
                                         "Affymetrix",    /* vendor */
                                         "Affymetrix - generic separate files MAS5", /* array layout name */
                                         "Human",         /* species */
                                         "MAS5.0",        /* Quantitation tool */
                                         "Hs",            /* UniGeneSpeciesPrefix */
                                         "",              /* commentToken */
                                         "",              /* initialKeyword Field */
                                         false,           /* hasMultDatasetsFlag */
                                         true,            /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0,               /* expectedNbrTokens */
                                         1,               /* rowWithSamples - AFTER map in Cvt2Mae */
                                         2,               /* rowWithFields - AFTER map in Cvt2Mae */
                                         3,               /* rowWithData - AFTER map in Cvt2Mae */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         false,           /* is Cy3/Cy5 data */
                                         true,            /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         false,           /* hasBkgrdDataFlag */
                                         false,           /* hasQuantXYcoordsFlag */
                                         defAffyBitPropsMAS5, /* bitProps */
                                         desFieldsAffyMAS5, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );

      /* Dual a+B chips MAS-5.0 Generic Affymetrix 'Pivot Tab' data:
       * The PROP_USE_2ND_INPUT_CHIP_FILE_FLAG bit sets the
       * mcd.use2ndInputChipFileFlag flag  if inputing paired chips
       * (e.g. Affy U133A and U133B chips) are used.
       */
      long defAffyBitPropsMAS5AB= (mcd.PROP_GENBANKID | mcd.PROP_QUALCHK_SPOT_DATA |
                                   mcd.PROP_ALLOW_NEG_DATA |
                                   mcd.PROP_USE_SRC_TMP_FILE_FLAG |
                                   mcd.PROP_SWISSPROTID |
                                   mcd.PROP_USE_2ND_INPUT_CHIP_FILE_FLAG);

      /* NOTE: this assumes multiple A+B sets of Affy MAS5.0 files */
      if(chkUniqueLayoutName("Affymetrix - generic A+B MAS5"))
        alList[maxAL++]= new ArrayLayout("Affymetrix-Generic-A+B-MAS5.alo", /* aloFileName */
                                         "Affymetrix",    /* vendor */
                                         "Affymetrix - generic A+B MAS5",    /* array layout name */
                                         "Human",         /* species */
                                         "MAS5.0",        /* Quantitation tool */
                                         "Hs",            /* UniGeneSpeciesPrefix */
                                         "",              /* commentToken */
                                         "",              /* initialKeyword Field */
                                         true,            /* hasMultDatasetsFlag */
                                         true,            /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0,               /* expectedNbrTokens */
                                         1,               /* rowWithSamples - AFTER map in Cvt2Mae */
                                         2,               /* rowWithFields - AFTER map in Cvt2Mae */
                                         3,               /* rowWithData - AFTER map in Cvt2Mae */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         false,           /* is Cy3/Cy5 data */
                                         true,            /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         false,           /* hasBkgrdDataFlag */
                                         false,           /* hasQuantXYcoordsFlag */
                                         defAffyBitPropsMAS5AB, /* bitProps */
                                         desFieldsAffyMAS5, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );                            

      /* NOTE: this assumes multiple files */
      if(chkUniqueLayoutName("Affymetrix - generic A+B separate files MAS5"))
        alList[maxAL++]= new ArrayLayout("Affymetrix-Generic-A+B-separate-MAS5.alo", /* aloFileName */
                                         "Affymetrix",    /* vendor */
                                         "Affymetrix - generic A+B separate files MAS5", /* array layout name */
                                         "Human",         /* species */
                                         "MAS5.0",        /* Quantitation tool */
                                         "Hs",            /* UniGeneSpeciesPrefix */
                                         "",              /* commentToken */
                                         "",              /* initialKeyword Field */
                                         false,           /* hasMultDatasetsFlag */
                                         true,            /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0,               /* expectedNbrTokens */
                                         1,               /* rowWithSamples - AFTER map in Cvt2Mae */
                                         2,               /* rowWithFields - AFTER map in Cvt2Mae */
                                         3,               /* rowWithData - AFTER map in Cvt2Mae */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         false,           /* is Cy3/Cy5 data */
                                         true,            /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         false,           /* hasBkgrdDataFlag */
                                         false,           /* hasQuantXYcoordsFlag */
                                         defAffyBitPropsMAS5AB, /* bitProps */
                                         desFieldsAffyMAS5, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );

                            
      /* Generic Incyte array data */
      long defIncyteBitProps= (mcd.PROP_PLATE_DATA | mcd.PROP_CLONEID |
                               mcd.PROP_GENBANKID | mcd.PROP_BKGRD_DATA |
                               mcd.PROP_ALLOW_NEG_DATA);
      
      /* Fields for Incyte GEM
       * [0:4] GEMID Location DiffExpr BalancedDiffExpr P1Signal
       * [5:9] P1S/B P1Area% P2BalancedSignal P2Signal P2S/B
       * [10:14] P2Area% Probe1 P1Description Probe2 P2Description
       * [15:19] GeneID PlateRow PlateCol PlateID GeneName
       * [20:24] CloneID CloneSource AccessionNum Locus IncyteCloneID
       * [25:26] PCRStatus #
       */
      String
        desFieldsIncyte[]= {"Table name\tMAE field\tUser field",  /* Table def */
                            "QuantTable\tLocation\tLocation",
                            //"QuantTable\tRawIntensity1\tP1Signal",
                            //"QuantTable\tBackground1\tP1S/B",
                            //"QuantTable\tRawIntensity2\tP2Signal",
                            //"QuantTable\tBackground2\tP2S/B",
                            "QuantTable\tCy3\tP1Signal",
                            "QuantTable\tCy3Bkg\tP1S/B",
                            "QuantTable\tCy5\tP2Signal",
                            "QuantTable\tCy5Bkg\tP2S/B",
                            "GipoTable\tLocation\tLocation",
                            "GipoTable\tplate\tPlateID",
                            "GipoTable\tplate row\tPlateRow",
                            "GipoTable\tplate col\tPlateCol",
                            "GipoTable\tGeneName\tGeneName",
                            "GipoTable\tClone ID\tCloneID",
                            "GipoTable\tGenBankAcc\tAccessionNum",
                            "GipoTable\tLocusID\tLocus",
                            "GipoTable\tExtraInfo1\tCloneSource",
                            "GipoTable\tExtraInfo2\tIncyteClone"
                           };

      if(chkUniqueLayoutName("Incyte - generic"))
        alList[maxAL++]= new ArrayLayout("Incyte-Generic.alo",  /* aloFileName */
                                         "Incyte",        /* vendor */
                                         "Incyte - generic",  /* array layout name */
                                         "?",         /* species */
                                         "GemTool",       /* Quantitation tool */
                                         "??",            /* UniGeneSpeciesPrefix */
                                         "#",             /* commentToken */
                                         "GEMID",         /* initialKeyword Field */
                                         false,           /* hasMultDatasetsFlag */
                                         true,            /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0,               /* expectedNbrTokens */
                                         0,               /* rowWithSamples */
                                         7,               /* rowWithFields */
                                         0,               /* rowWithData */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         true,            /* is Cy3/Cy5 data */
                                         false,           /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         true,            /* hasBkgrdDataFlag */
                                         false,           /* hasQuantXYcoordsFlag */
                                         defIncyteBitProps, /* bitProps */
                                         desFieldsIncyte, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );
      
      /* Now add special cases where pre-assigned the species name for convenience */
      if(chkUniqueLayoutName("Affymetrix - Mouse MAS4"))
        alList[maxAL++]= new ArrayLayout("Affymetrix-Mouse-MAS4.alo", /* aloFileName */
                                         "Affymetrix",    /* vendor */
                                         "Affymetrix - Mouse MAS4", /* array layout name */
                                         "Mouse",         /* species */
                                         "MAS4.0",    /* Quantitation tool */
                                         "Mm",            /* UniGeneSpeciesPrefix */
                                         "",              /* commentToken */
                                         "",              /* initialKeyword Field */
                                         true,            /* hasMultDatasetsFlag */
                                         true,            /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0,               /* expectedNbrTokens */
                                         3,               /* rowWithSamples */
                                         4,               /* rowWithFields */
                                         0,               /* rowWithData */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         false,           /* is Cy3/Cy5 data */
                                         true,            /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         false,           /* hasBkgrdDataFlag */
                                         false,           /* hasQuantXYcoordsFlag */
                                         defAffyBitPropsMAS4, /* bitProps */
                                         desFieldsAffyMAS4, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );

      if(chkUniqueLayoutName("Affymetrix - Human MAS4"))
        alList[maxAL++]= new ArrayLayout("Affymetrix-Human-MAS4.alo",  /* aloFileName */
                                         "Affymetrix",    /* vendor */
                                         "Affymetrix - Human MAS4", /* array layout name */
                                         "Human",         /* species */
                                         "MAS4.0",    /* Quantitation tool */
                                         "Hs",            /* UniGeneSpeciesPrefix */
                                         "",              /* commentToken */
                                         "",              /* initialKeyword Field */
                                         true,            /* hasMultDatasetsFlag */
                                         true,            /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0 ,              /* expectedNbrTokens */
                                         3,               /* rowWithSamples */
                                         4,               /* rowWithFields */
                                         0,               /* rowWithData */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         false,           /* is Cy3/Cy5 data */
                                         true,            /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         false,           /* hasBkgrdDataFlag */
                                         false,           /* hasQuantXYcoordsFlag */
                                         defAffyBitPropsMAS4, /* bitProps */
                                         desFieldsAffyMAS4, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );

        int defAffyBitPropsEdit= 0;
        String desFieldsAffyEdit[]= {"Table name\tMAE field\tUser field", /* Table def */
                                     "GipoTable\tLocation\tprobe set",     /* note: is ascii */
                                     "*QuantTable\tRawIntensity\tAvg Diff", /* '*' means repeated */
                                     "*QuantTable\tQualCheck\tAbs Call",
                                     "GipoTable\tClone_ID\tProbe Set",  /* may be problem if dup */
                                     "GipoTable\tGeneName\tDescription"
                                    };
        if(chkUniqueLayoutName("Affymetrix - Mouse MAS4, use Genomic Descriptions"))
          alList[maxAL++]= new ArrayLayout("Affymetrix-MouseGenDescr-MAS4.alo", /* aloFileName */
                                           "Affymetrix",    /* vendor */
                                           "Affymetrix - Mouse MAS4, use Genomic Descriptions", /* array layout name */
                                           "Mouse",         /* species */
                                           "MAS4.0",    /* Quantitation tool */
                                           "Mm",            /* UniGeneSpeciesPrefix */
                                           "",              /* commentToken */
                                           "",              /* initialKeyword Field */
                                           true,            /* hasMultDatasetsFlag */
                                           true,            /* specifyGeometryByNbrSpotsFlag */
                                           1,               /* maxFields */
                                           0,               /* nGridsPerField */
                                           0,               /* rowsPerGrid */
                                           0,               /* colsPerGrid */
                                           0,               /* nElements in array */
                                           0,               /* expectedNbrTokens */
                                           3,               /* rowWithSamples */
                                           4,               /* rowWithFields */
                                           0,               /* rowWithData */
                                           0,               /* rowWithSepGIPOFields */
                                           0,               /* rowWithSepGIPOData */
                                           true,            /* is pseudo array layout */
                                           false,           /* is Cy3/Cy5 data */
                                           true,            /* allowNegQuantDataFlag */
                                           true,            /* chkAndEditFieldNamesFlag */
                                           false,           /* hasBkgrdDataFlag */
                                           false,           /* hasQuantXYcoordsFlag */
                                           defAffyBitPropsEdit, /* bitProps */
                                           desFieldsAffyEdit, /* list of desired fields */
                                           ""                /* geoPlatformID */
                                           );
        
        if(chkUniqueLayoutName("Affymetrix - Human MAS4, use Genomic Descriptions"))
          alList[maxAL++]= new ArrayLayout("Affymetrix-HumanGenDescr-MAS4.alo",  /* aloFileName */
                                           "Affymetrix",    /* vendor */
                                           "Affymetrix - Human MAS4, use Genomic Descriptions", /* array layout name */
                                           "Human",         /* species */
                                           "MAS4.0",    /* Quantitation tool */
                                           "Hs",            /* UniGeneSpeciesPrefix */
                                           "",              /* commentToken */
                                           "",              /* initialKeyword Field */
                                           true,            /* hasMultDatasetsFlag */
                                           true,            /* specifyGeometryByNbrSpotsFlag */
                                           1,               /* maxFields */
                                           0,               /* nGridsPerField */
                                           0,               /* rowsPerGrid */
                                           0,               /* colsPerGrid */
                                           0,               /* nElements in array */
                                           0 ,              /* expectedNbrTokens */
                                           3,               /* rowWithSamples */
                                           4,               /* rowWithFields */
                                           0,               /* rowWithData */
                                           0,               /* rowWithSepGIPOFields */
                                           0,               /* rowWithSepGIPOData */
                                           true,            /* is pseudo array layout */
                                           false,           /* is Cy3/Cy5 data */
                                           true,            /* allowNegQuantDataFlag */
                                           true,            /* chkAndEditFieldNamesFlag */
                                           false,           /* hasBkgrdDataFlag */
                                           false,           /* hasQuantXYcoordsFlag */
                                           defAffyBitPropsEdit, /* bitProps */
                                           desFieldsAffyEdit, /* list of desired fields */
                                           ""                /* geoPlatformID */
                                           );     
                                    
      /* Add the Species specific Incyte */
      if(chkUniqueLayoutName("Incyte - Mouse"))
        alList[maxAL++]= new ArrayLayout("Incyte-Mouse.alo",  /* aloFileName */
                                         "Incyte",        /* vendor */
                                         "Incyte - Mouse",  /* array layout name */
                                         "Mouse",         /* species */
                                         "GemTool",       /* Quantitation tool */
                                         "Mm",            /* UniGeneSpeciesPrefix */
                                         "#",             /* commentToken */
                                         "GEMID",         /* initialKeyword Field */
                                         false,           /* hasMultDatasetsFlag */
                                         true,            /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0,               /* expectedNbrTokens */
                                         0,               /* rowWithSamples */
                                         7,               /* rowWithFields */
                                         0,               /* rowWithData */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         true,            /* is Cy3/Cy5 data */
                                         false,           /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         true,            /* hasBkgrdDataFlag */
                                         false,           /* hasQuantXYcoordsFlag */
                                         defIncyteBitProps, /* bitProps */
                                         desFieldsIncyte, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );

      if(chkUniqueLayoutName("Incyte - Human"))
        alList[maxAL++]= new ArrayLayout("Incyte-Human.alo",  /* aloFileName */
                                         "Incyte",        /* vendor */
                                         "Incyte - Human",  /* array layout name */
                                         "Human",         /* species */
                                         "GemTool",       /* Quantitation tool */
                                         "Hs",            /* UniGeneSpeciesPrefix */
                                         "#",             /* commentToken */
                                         "GEMID",         /* initialKeyword Field */
                                         false,           /* hasMultDatasetsFlag */
                                         true,            /* specifyGeometryByNbrSpotsFlag */
                                         1,               /* maxFields */
                                         0,               /* nGridsPerField */
                                         0,               /* rowsPerGrid */
                                         0,               /* colsPerGrid */
                                         0,               /* nElements in array */
                                         0,               /* expectedNbrTokens */
                                         0,               /* rowWithSamples */
                                         7,               /* rowWithFields */
                                         0,               /* rowWithData */
                                         0,               /* rowWithSepGIPOFields */
                                         0,               /* rowWithSepGIPOData */
                                         true,            /* is pseudo array layout */
                                         true,            /* is Cy3/Cy5 data */
                                         false,           /* allowNegQuantDataFlag */
                                         false,           /* chkAndEditFieldNamesFlag */
                                         true,            /* hasBkgrdDataFlag */
                                         false,           /* hasQuantXYcoordsFlag */
                                         defIncyteBitProps, /* bitProps */
                                         desFieldsIncyte, /* list of desired fields */
                                         ""                /* geoPlatformID */
                                         );
                           
      lastAL= Math.max(firstAL,(maxAL-1));    /* capture the last default ALO */
    } /* setupDefaultArrayLayouts */
    
    
    /**
     * setupImageAnalysisMethods() - setup list of image analysis methods
     * [DEPRICATED FOR NOW].
     * [TODO] read from configuration file where new types could be added.
     * @return false if can't find or setup the image analysis methods.
     * @see ImageAnalysisMethod
     */
    public boolean setupImageAnalysisMethods()
    { /* setupImageAnalysisMethods */
      boolean flag= true;
      
      iam= new ImageAnalysisMethod[cvt.MAX_IMG_ANALY_METHODS];
      maxIAM= 0;
      useIAM= -1;   /* will be >= 0 if select method */
      
      /* [TODO] Check ALL of these so correct and complete */
      iam[maxIAM++]= new ImageAnalysisMethod("NONE",
                                             "pick array data", "",
                                             "",
                                             "",
                                             "",
                                             ""
                                             );
      iam[maxIAM++]= new ImageAnalysisMethod("NONE-data in GIPO",
                                             "N.A.", "N.A.",
                                             "Tab-Delim",
                                             "GIPO:FGRC",
                                             "plate:FGRC",
                                             "single"
                                             );
      iam[maxIAM++]= new ImageAnalysisMethod("NONE-data in GIPO",
                                             "N.A.", "N.A.",
                                             "Tab-Delim",
                                             "GIPO:GRC",
                                             "plate:GRC",
                                             "single"
                                             );
      iam[maxIAM++]= new ImageAnalysisMethod("Incyte",
                                             "GemTool", "N.A.",
                                             "Tab-Delim",
                                             "GIPO:GRC",
                                             "plate:GRC",
                                             "multiple"
                                             );
      iam[maxIAM++]= new ImageAnalysisMethod("Affymetrix",
                                             "???", "N.A.",
                                             "Tab-Delim",
                                             "GIPO:GRC",
                                             "plate:GRC",
                                             "multiple"
                                             );
      iam[maxIAM++]= new ImageAnalysisMethod("Genomic Solutions",
                                             "???", "N.A.",
                                             "Tab-Delim",
                                             "???",
                                             "???",
                                             "???"
                                             );
      iam[maxIAM++]= new ImageAnalysisMethod("Scanalytics",
                                             "IPlab", "N.A.",
                                             "Tab-Delim",
                                             "2DGrid:PatchRow,PatchCol,RowInPatch,ColInPatch",
                                             "-none-",
                                             "single"
                                             );
      iam[maxIAM++]= new ImageAnalysisMethod("NHGRI",
                                             "ScanArray", "N.A.",
                                             "Tab-Delim",
                                             "-none-,block,block row,block col",
                                             "plate:GRC",
                                             "single"
                                             );
      iam[maxIAM++]= new ImageAnalysisMethod("Molecular Dynamics",
                                             "ImageQuant-NT", "Version 5",
                                             "Tab-Delim",
                                             "NAME-GRC",
                                             "plate:GRC",
                                             "single"
                                             );
      iam[maxIAM++]= new ImageAnalysisMethod("Res.Gen.",
                                             "Pathways", "Version 2.01",
                                             "Tab-Delim",
                                             "field,grid,grid row,grid col",
                                             "plate,plate row,plate col",
                                             "single");
      flag= (flag && (maxIAM>0));
      
      return(flag);
    } /* setupImageAnalysisMethods */
    
    
    /**
     * setPseudoArrayToCurMCD() - set PseudoArray values to mcd.XXX state.
     * But only if mcd.specifyGeometryByNbrSpotsFlag is true.
     * @return true if generated pseudo array data in ALO.
     * @see PseudoArray
     * @see UtilCM#logMsg
     */
    public boolean setPseudoArrayToCurMCD()
    { /* setPseudoArrayToCurMCD */
      if(mcd.maxRowsExpected>0 && mcd.specifyGeometryByNbrSpotsFlag)
      { /* extrapolate geometry based on # of gene rows in sample */
        PseudoArray psa= new PseudoArray(mcd.maxRowsExpected);
        
        mcd.maxFields= 1;                 /* force it to 1 field */
        mcd.maxGrids= psa.maxGrids;
        mcd.maxGridRows= psa.maxGridRows;
        mcd.maxGridCols= psa.maxGridCols;
        
        mcd.nbrGenesCalc= mcd.maxGrids*mcd.maxGridRows*mcd.maxGridCols;
        mcd.nbrSpotsCalc= mcd.maxFields*mcd.nbrGenesCalc;
        
        util.logMsg("Extrapolating setup (grid,row,col) sizes from # rows in sample",
        Color.black);
        return(true);
      }
      else
        return(false);
    } /* setPseudoArrayToCurMCD */
    
    
    /**
     * setPseudoArrayToCurALO() - set PseudoArray values to al[useAL].XXX state.
     * But only if al.specifyGeometryByNbrSpotsFlag is true.
     * @return true if generated pseudo array data in ALO.
     * @see PseudoArray
     * @see UtilCM#logMsg
     */
    public boolean setPseudoArrayToCurALO()
    { /* setPseudoArrayToCurALO */
      ArrayLayout al= alList[useAL];
      
      if(al.maxRowsExpected>0 && al.specifyGeometryByNbrSpotsFlag)
      { /* extrapolate geometry based on # of gene rows in sample */
        PseudoArray psa= new PseudoArray(al.maxRowsExpected);
        
        al.maxFields= 1;        /* force it to 1 field */
        al.nGridsPerField= psa.maxGrids;
        al.rowsPerGrid= psa.maxGridRows;
        al.colsPerGrid= psa.maxGridCols;
        util.logMsg("Extrapolating setup (grid,row,col) sizes from # rows in sample",
        Color.black);
        return(true);
      }
      else
        return(false);
    } /* setPseudoArrayToCurALO */
    
    
    /**
     * copyMCDtoALOstate() - Copy mcd.XXX state to al[useAL].XXX state.
     */
    public void copyMCDtoALOstate()
    { /* copyMCDtoALOstate */
      ArrayLayout al= alList[useAL];
      
      /* [1] copy ALO names and sizes */
      al.vendor= mcd.vendor;                   /* for special flag */
      al.layoutName= mcd.layoutName;
      al.species= mcd.species;
      al.aloFileName= mcd.aloFileName;
      al.maxRowsExpected= mcd.maxRowsExpected;
      
      /* [2] Copy proper geometry */
      al.specifyGeometryByNbrSpotsFlag= mcd.specifyGeometryByNbrSpotsFlag;
      al.maxFields= mcd.maxFields;
      al.nGridsPerField= mcd.maxGrids;
      al.rowsPerGrid= mcd.maxGridRows;
      al.colsPerGrid= mcd.maxGridCols;
      
      /* copy array info */
      al.quantTool= mcd.maAnalysisProg;
      al.hasMultDatasetsFlag= mcd.hasMultDatasetsFlag;
      al.UniGeneSpeciesPrefix= mcd.UniGeneSpeciesPrefix;
      al.species= mcd.species;
      al.allowNegQuantDataFlag= mcd.allowNegQuantDataFlag;
      
      al.pseudoArrayFlag= mcd.pseudoArrayFlag;
      al.chkAndEditFieldNamesFlag= mcd.chkAndEditFieldNamesFlag;
      al.hasBkgrdDataFlag= mcd.hasBkgrdDataFlag;
      al.hasQuantXYcoordsFlag= mcd.hasQuantXYcoordsFlag;
      
      /* Don't overide if using the Edit Wizard. Only do
       * during initial call.
       */
      al.useRatioDataFlag= mcd.useRatioDataFlag;
      al.rowWithSamples= mcd.rowWithSamples;
      al.rowWithData= mcd.rowWithData;
      
      al.rowWithSepGIPOFields= mcd.rowWithSepGIPOFields;
      al.rowWithSepGIPOData= mcd.rowWithSepGIPOData;
      
      al.commentToken= mcd.commentToken;
      al.initialKeyword= mcd.initialKeyword;
      
      /* set mcd.hasXXXX state for a.bitProps */
      mcd.bitProps= mcd.calcBitPropFromMCDstate();
      al.bitProps= mcd.bitProps;
      
      /* Copy EXISTING fieldMap. Note: it must be built before
       * we come here to copy it!
       */
      al.fieldMap= mcd.fm;
      
      /*
      if(cvt.DBUG_FLAG)
        System.out.println("C2M-CCMA mcd.highestID="+mcd.highestID+
                          " vendor="+mcd.vendor+
                          "\n  mcd.specifyGeometryByNbrSpotsFlag="+
                          mcd.specifyGeometryByNbrSpotsFlag+
                          "\n  mcd.specifyByGridGeometryFlag="+
                          mcd.specifyByGridGeometryFlag+
                          "\n  mcd.maxRowsExpected="+mcd.maxRowsExpected+
                          "\n  mcd.maxFields="+mcd.maxFields+
                          "\n  mcd.maxGrids="+mcd.maxGrids+
                          "\n  mcd.maxGridRows="+mcd.maxGridRows+
                          "\n  mcd.maxGridCols="+mcd.maxGridCols+
                          "\n  mcd.nbrGenesCalc="+ mcd.nbrGenesCalc+
                          "\n  mcd.nbrSpotsCalc="+mcd.nbrSpotsCalc+
                          "\n  mcd.maAnalysisProg="+mcd.maAnalysisProg+
                          "\n  mcd.hasMultDatasetsFlag="+
                          mcd.hasMultDatasetsFlag+
                          "\n  mcd.UniGeneSpeciesPrefix="+
                          mcd.UniGeneSpeciesPrefix+
                          "\n  mcd.species="+mcd.species+
                          "\n  expectedNbrTokens="+expectedNbrTokens+
                          "\n  mcd.pseudoArrayFlag="+mcd.pseudoArrayFlag+
                          "\n  mcd.useRatioDataFlag="+mcd.useRatioDataFlag+
                          "\n  mcd.nbrGenesCalc="+mcd.nbrGenesCalc+
                          "\n  mcd.nbrSpotsCalc="+mcd.nbrSpotsCalc+
                          "\n  mcd.rowWithSamples="+ mcd.rowWithSamples+
                          "\n  mcd.rowWithFields="+ mcd.rowWithFields+
                          "\n  mcd.rowWithData="+ mcd.rowWithData+
                          "\n  mcd.rowWithSepGIPOFields="+ mcd.rowWithSepGIPOFields+
                          "\n  mcd.rowWithSepGIPOData="+ mcd.rowWithSepGIPOData+
                          "\n  commentToken="+mcd.commentToken+
                          "\n  mcd.initialKeyword="+mcd.initialKeyword+
                          "\n "+mcd.fm.toString()+
                          "\n "+al.toString() );
       */
      
    } /* copyMCDtoALOstate */
    
    
    /**
     * copyALOtoMCDstate() - Copy al[useAL].XXX state to mcd.XXX state.
     * @see MaeConfigData#setMCDstateFromBitProp
     * @see ArrayLayout
     */
    public void copyALOtoMCDstate()
    { /* copyALOtoMCDstate */
      ArrayLayout al= alList[useAL];
      
      /* [1] copy ALO names and sizes */
      mcd.vendor= al.vendor;                   /* for special flag */
      mcd.layoutName= al.layoutName;
      mcd.species= al.species;
      mcd.aloFileName= al.aloFileName;
      mcd.maxRowsExpected= al.maxRowsExpected;
      
      /* [2] copy geometry */
      mcd.specifyGeometryByNbrSpotsFlag= al.specifyGeometryByNbrSpotsFlag;
      mcd.maxFields= al.maxFields;
      mcd.maxGrids= al.nGridsPerField;
      mcd.maxGridRows= al.rowsPerGrid;
      mcd.maxGridCols= al.colsPerGrid;
      
      /* Compute derived values based on geometry */
      mcd.highestID= mcd.maxRowsExpected;    /* fill missing data */
      mcd.nbrGenesCalc= mcd.maxGrids*mcd.maxGridRows*mcd.maxGridCols;
      mcd.nbrSpotsCalc= mcd.maxFields*mcd.nbrGenesCalc;
      
      mcd.maAnalysisProg= al.quantTool;
      mcd.hasMultDatasetsFlag= al.hasMultDatasetsFlag;
      mcd.UniGeneSpeciesPrefix= al.UniGeneSpeciesPrefix;
      mcd.species= al.species;
      mcd.allowNegQuantDataFlag= al.allowNegQuantDataFlag;
      expectedNbrTokens= al.expectedNbrTokens;
      mcd.pseudoArrayFlag= al.pseudoArrayFlag;
      mcd.chkAndEditFieldNamesFlag= al.chkAndEditFieldNamesFlag;
      mcd.hasBkgrdDataFlag= al.hasBkgrdDataFlag;
      mcd.hasQuantXYcoordsFlag= al.hasQuantXYcoordsFlag;
      
      /* Don't overide if using the Edit Wizard. Only do
       * during initial call.
       */
      mcd.useRatioDataFlag= al.useRatioDataFlag;
      
      mcd.rowWithSamples= al.rowWithSamples;
      mcd.rowWithFields= al.rowWithFields;
      mcd.rowWithData= al.rowWithData;
      
      mcd.rowWithSepGIPOFields= al.rowWithSepGIPOFields;
      mcd.rowWithSepGIPOData= al.rowWithSepGIPOData;
      
      mcd.commentToken= al.commentToken;
      mcd.initialKeyword= al.initialKeyword;
      
      /* set mcd.hasXXXX state for a.bitProps */
      mcd.setMCDstateFromBitProp(al.bitProps);
      
      mcd.fm= al.fieldMap;	/* Copy existing field map */
      
      /*
      if(cvt.DBUG_FLAG)
        System.out.println("C2M-CCAM mcd.highestID="+mcd.highestID+
                          " vendor="+mcd.vendor+
                          "\n  mcd.specifyGeometryByNbrSpotsFlag="+
                          mcd.specifyGeometryByNbrSpotsFlag+
                          "\n  mcd.specifyByGridGeometryFlag="+
                          mcd.specifyByGridGeometryFlag+
                          "\n  mcd.maxRowsExpected="+mcd.maxRowsExpected+
                          "\n  mcd.maxFields="+mcd.maxFields+
                          "\n  mcd.maxGrids="+mcd.maxGrids+
                          "\n  mcd.maxGridRows="+mcd.maxGridRows+
                          "\n  mcd.maxGridCols="+mcd.maxGridCols+
                          "\n  mcd.nbrGenesCalc="+ mcd.nbrGenesCalc+
                          "\n  mcd.nbrSpotsCalc="+mcd.nbrSpotsCalc+
                          "\n  mcd.maAnalysisProg="+mcd.maAnalysisProg+
                          "\n  mcd.hasMultDatasetsFlag="+
                          mcd.hasMultDatasetsFlag+
                          "\n  mcd.UniGeneSpeciesPrefix="+
                          mcd.UniGeneSpeciesPrefix+
                          "\n  mcd.species="+mcd.species+
                          "\n  expectedNbrTokens="+expectedNbrTokens+
                          "\n  mcd.pseudoArrayFlag="+mcd.pseudoArrayFlag+
                          "\n  mcd.useRatioDataFlag="+mcd.useRatioDataFlag+
                          "\n  mcd.nbrGenesCalc="+mcd.nbrGenesCalc+
                          "\n  mcd.nbrSpotsCalc="+mcd.nbrSpotsCalc+
                          "\n  mcd.rowWithSamples="+ mcd.rowWithSamples+
                          "\n  mcd.rowWithFields="+ mcd.rowWithFields+
                          "\n  mcd.rowWithData="+ mcd.rowWithData+
                          "\n  mcd.rowWithSepGIPOFields="+ mcd.rowWithSepGIPOFields+
                          "\n  mcd.rowWithSepGIPOData="+ mcd.rowWithSepGIPOData+
                          "\n  commentToken="+mcd.commentToken+
                          "\n  mcd.initialKeyword="+mcd.initialKeyword+
                          "\n "+mcd.fm.toString()+
                          "\n "+al.toString() );
       */
    } /* copyALOtoMCDstate */
    
    
    /**
     * getMinReqFieldList() - get minimal required list of fields
     * based on current mcd state. Based on the semantics of the current
     * state that is set by "Edit Layout", we may not want to require
     * particular fields or we may want to rename them
     * (eg. use Cy3, Cy5 instead of RawIntensity1, RawIntensity2 if
     * if mcd.useRatioDataFlag is set, etc.
     * @param remapMode to enable remaping of field names
     * @return list of minimum required fields else null if error
     * @see FieldMap#addEntry
     * @see FieldMap
     */
    private String[] getMinReqFieldList(int remapMode)
    {  /* getMinReqFieldList */
      int nF= 0;
      String
        fullList[]= new String[100],
        maeFields[]= null;
      
      specialFM= new FieldMap("SpecialFM");
      
      switch(remapMode)
      { /* popup field map editor */
        
        case REMAP_GIPO:
          if(mcd.specifyGeometryByNbrSpotsFlag /* || mcd.hasLocationIdFlag */)
            fullList[nF++]= "Location";
          else
          { /* use (F,G,R,C) geometry */
            if(mcd.hasLocationIdFlag)
              fullList[nF++]= "Location";
            if(mcd.maxFields>1)
              fullList[nF++]= "field";
            if(mcd.useMolDyn_NAME_GRC_specFlag)
              fullList[nF++]= "NAME_GRC";
            else
            { /* explicit G,R,C */
              fullList[nF++]= "grid";
              fullList[nF++]= "grid row";
              fullList[nF++]= "grid col";
            } /* explicit G,R,C */
          } /* use (F,G,R,C) geometry */
          
          if(mcd.hasQualCheckGIPOdataFlag)
            fullList[nF++]= "QualCheck";
          
          if(mcd.hasCloneIDsFlag)
            fullList[nF++]= "Clone ID";
          
          if(mcd.hasGenBankIDsFlag)
          { /* one or more GenBank IDs */
            fullList[nF++]= "GenBankAcc";
            fullList[nF++]= "GenBankAcc3'";
            fullList[nF++]= "GenBankAcc5'";
          }
          
          if(mcd.hasUniGeneIDsFlag)
            fullList[nF++]= "Unigene cluster ID";
          
          if(mcd.hasDB_ESTIDsFlag)
          { /* has one or more dbEST data */
            fullList[nF++]= "dbEst3'";
            fullList[nF++]= "dbEst35'";
          }
          
          if(mcd.hasSwissProtIDsFlag)
            fullList[nF++]= "SwissProtID";
          
          if(!mcd.hasCloneIDsFlag && !mcd.hasGenBankIDsFlag &&
             !mcd.hasUniGeneIDsFlag && !mcd.hasDB_ESTIDsFlag &&
            !mcd.hasSwissProtIDsFlag)
          {
            fullList[nF++]= "Identifier";  /* alternate id */
          }
          
          if(mcd.hasPlateDataFlag)
          { /* has one or more plate data */
            fullList[nF++]= "plate";
            fullList[nF++]= "plate row";
            fullList[nF++]= "plate col";
          }
          
          if(mcd.hasGeneClassDataFlag)
            fullList[nF++]= "Gene Class";
          
          fullList[nF++]= "GeneName";
          
          if(mcd.hasUniGeneNameFlag)
            fullList[nF++]= "Unigene cluster name";
          
          break;
          
        case REMAP_QUANT:
          if(mcd.specifyGeometryByNbrSpotsFlag /* || mcd.hasLocationIdFlag */)
            fullList[nF++]= "Location";
          else
          { /* use (F,G,R,C) geometry */
            if(mcd.hasLocationIdFlag)
              fullList[nF++]= "Location";
            if(mcd.maxFields>1)
              fullList[nF++]= "field";
            if(mcd.useMolDyn_NAME_GRC_specFlag)
              fullList[nF++]= "NAME_GRC";
            else
            { /* explicit G,R,C */
              fullList[nF++]= "grid";
              fullList[nF++]= "grid row";
              fullList[nF++]= "grid col";
            } /* explicit G,R,C */
          } /* use (F,G,R,C) geometry */
          
          if(mcd.useRatioDataFlag)
          { /* fluoresent data */
            fullList[nF++]= "Cy3";
            fullList[nF++]= "Cy5";
            
            specialFM.addEntry("QuantTable", "RawIntensity1",
                               "QuantTable", "Cy3");
            specialFM.addEntry("QuantTable", "RawIntensity2",
                               "QuantTable", "Cy5");
          }
          else
          { /* intensity  data */
            if(mcd.maxFields==1)
              fullList[nF++]= "RawIntensity";
            else
            { /* duplicate fields */
              fullList[nF++]= "RawIntensity1";
              fullList[nF++]= "RawIntensity2";
            }
          } /* intensity  data */
          
          if(mcd.hasQuantXYcoordsFlag)
          {
            fullList[nF++]= "X";
            fullList[nF++]= "Y";
          }
          
          if(mcd.hasQualCheckQuantDataFlag)
            fullList[nF++]= "QualCheck";
          
          if(mcd.hasBkgrdDataFlag)
          { /* background data */
            if(mcd.useRatioDataFlag)
            { /* fluoresent data */
              fullList[nF++]= "Cy3Bkg";
              fullList[nF++]= "Cy5Bkg";
              specialFM.addEntry("QuantTable", "Background1",
                                 "QuantTable", "Cy3Bkg");
              specialFM.addEntry("QuantTable", "Background2", 
                                 "QuantTable", "Cy5Bkg");
            }
            else
            { /* using Intensity data */
              if(mcd.maxFields==1)
                fullList[nF++]= "Background";
              else
              { /* duplicate fields */
                fullList[nF++]= "Background1";
                fullList[nF++]= "Background2";
              }
            } /* using Intensity data */
          } /* background data */
          
          if(mcd.useSrcTmpFileFlag)
            fullList[nF++]= "DetValue";
          break;
          
        case REMAP_SAMPLE:
          fullList[nF++]= "Sample_ID";
          fullList[nF++]= "Project";
          fullList[nF++]= "Database_File";
          fullList[nF++]= "DatabaseFileID";
          fullList[nF++]= "FilterType";
          break;
      } /* popup field map editor */
      
      /* Make list exactly nF size */
      maeFields= new String[nF];
      for(int i=0;i<nF;i++)
        maeFields[i]= fullList[i];
      
      return(maeFields);
    } /* getMinReqFieldList */
    
    
    /**
     * getMaeToUserFields() - extract desired fields from row of tokens
     * by reading them from the fileName.
     * This is used in the Assign GIPO/QUANT fields operations.
     * @param fileName to read
     * @param fm is field map to use
     * @param row to use
     * @param remapMode
     * @param useMaxFieldsFlag
     * @return true if successful.
     * @see FieldMapGUI
     * @see FileTable
     * @see FileTable#readTableFieldsFromFile
     * @see UtilCM#logMsg
     * @see UtilCM#logMsg2
     * @see #getMinReqFieldList
     */
    public boolean getMaeToUserFields(String fileName, FieldMap fm, int row,
                                      int remapMode, boolean useMaxFieldsFlag)
    { /* getMaeToUserFields */
      String
        gipoHelpMsg=
          "These fields define the data for the Gene-In-Plate-Order table (GIPO)."+
          "\nIt maps spot positions in the array to corresponding gene identifications."+
          "\nFor each MAExplorer field on the left, assign a unique item "+
          "\nfrom from the choice list of user data fields on right."+
          "\nOmit selection of fields which do not exist in your data.",
       quantHelpMsg=
         "These fields define the data for the quantified spot data table."+
         "\nIt maps the quantification values to gene identification for"+
         "\neach spot in the array."+
         "\nFor each MAExplorer field on the left, assign a unique item "+
         "\nfrom from the choice list of user data fields on right."+
         "\nOmit selection of fields which do not exist in your data.",
       sampleHelpMsg=
         "These fields define the data for the hybridized sample table."+
         "\nIt maps the hybridized sample file name to data about that sample."+
         "\nFor each MAExplorer field on the left, assign a unique item "+
         "\nfrom from the choice list of user data fields on right."+
         "\nOmit selection of fields which do not exist in your data.";
      
      String
        maeFields[],             /* mae label fields requested */
        userFieldList[]= null;   /* selectable fields from user file */
      
      if(cvt.NEVER && cvt.fmg!=null)
      { /* been there - done that - MODAL still in progress */
        util.logMsg("May map only one set of features at a time.", Color.red);
        util.logMsg2("First press 'Done' or 'Cancel' to exit current popup.",
                     Color.red);        
        return(false);            /* only one at a time */
      }
      else
        fm.remapMode= remapMode;                   /* save Mode */
      
      if(fileName!=null)
      { /* get field names from the input file */
        FileTable ft= new FileTable(fileName);
        String tmp[]= ft.readTableFieldsFromFile(fileName, row);        
        if(tmp==null)
          return(false);
        
        /* Create special list with "<not used>" as first entry */
        userFieldList= new String[tmp.length+1];
        userFieldList[0]= "<not used>";
        for(int i=0;i<tmp.length;i++)
          userFieldList[i+1]= tmp[i];
      } /* get field names from the input file */      
      else
      { /* Must have a file */
        util.logMsg("You may not assign fields unless you have opened an input file.",
                    Color.red);
        util.logMsg2("First do step (2) to find an input file and then try again.",
                     Color.red);        
        return(false);
      }
      
      /*
      if(cvt.DBUG_FLAG)
      {
        System.out.println("SL-EF.1 fileName="+fileName+
                           " fieldList="+fieldList+" fm="+fm);
        if(fieldList==null)
          return(false);
        System.out.println("  # fields="+fieldList.length);
        for(int i= 0;i<fieldList.length; i++)
          System.out.println("  fieldList["+i+"]='"+fieldList[i]+"'");
      }
      */
      
      if(mcd.hasSeparateGIPOandQuantFilesFlag)
      { /* Read two files: fieldG'Q= FieldG' and FieldQ */
        int
          k= 0,
          nExtraGIPOfields= mcd.nSepGIPOfields - 3, /* assumes Block,Row,Col */
          nUserFieldList= userFieldList.length,
          //nFieldGQ= nUserFieldList + nExtraGIPOfields;
          nFieldGQ= (remapMode==REMAP_GIPO) ? mcd.nSepGIPOfields : nUserFieldList;
        String fieldGQ[]= new String[nFieldGQ];

        /* Need to make sure they picked the correct items in
         * "specify GIPO field names" chooser before continueing from here. 
         */
        if(remapMode==REMAP_GIPO)
        { /* for GIPO, use the names in the GIPO file NOT the Quant src file */
          for(int i=0;i<mcd.nSepGIPOfields;i++)
           // if(mcd.sepGIPOfieldsUsed[i])  /* chicken and egg - allow all for now */
              fieldGQ[(k++)]= mcd.sepGIPOfields[i];
        }
        else
        { /* Then also add data in Quant source file */
          for(int i=0;i<nUserFieldList;i++)
              fieldGQ[(k++)]= userFieldList[i];        
        }
        
        userFieldList= fieldGQ;               /* replace it with possibly different size list */        
        fm.fieldsOfUserFile= userFieldList;   /* assign choices */        
      } /* Read two files: fieldG'Q= FieldG' and FieldQ  */
      
      else
      { /* Read single file (FieldGQ) */
        fm.fieldsOfUserFile= userFieldList;   /* assign choices */
      }
      
      /* Popup a Field Map editor, then get the MODAL
       * results use them to create the entries in the Field Map
       * using calls to fm.addEntry(mT,mF,uT,uF) etc..
       * [TODO] make sure that Cvt2Mae can not proceed to
       * next step until we have pressed Done or Cancel
       */
      switch(remapMode)
      { /* popup field map editor */
        case REMAP_GIPO:
          maeFields= getMinReqFieldList(REMAP_GIPO);
          cvt.fmg= new FieldMapGUI(cvt, maeFields, /* mae Field Labels*/
                                   fm.fieldsOfUserFile,
                                   null,          /* mouseoverList[] text */
                                   "Assign user fields to GIPO fields", /* title */
                                   gipoHelpMsg,   /* when press HELP button */
                                   "<not used>",  /* selected if unused */
                                   REMAP_GIPO
                                   );
          break;
        case REMAP_QUANT:
          maeFields= getMinReqFieldList(REMAP_QUANT);
          cvt.fmg= new FieldMapGUI(cvt, maeFields, /* mae Field Labels*/
                                   fm.fieldsOfUserFile,
                                   null,           /* mouseoverList[] text */
                                   "Assign user fields to Quantitation fields", /* title */
                                   quantHelpMsg,   /*  when press HELP button */
                                   "<not used>",   /* selected if unused */
                                   REMAP_QUANT
                                   );
          break;
        case REMAP_SAMPLE:
          maeFields= getMinReqFieldList(REMAP_SAMPLE);
          cvt.fmg= new FieldMapGUI(cvt, maeFields, /* mae Field Labels*/
                                   fm.fieldsOfUserFile,
                                   null,           /* mouseoverList[] text */
                                   "Assign user fields to Sample DB fields", /* title */
                                   sampleHelpMsg,  /* when press HELP button */
                                   "<not used>",   /* selected if unused */
                                   REMAP_SAMPLE
                                   );
          break;
      } /* popup field map editor */
      
      return(true);
    } /* getMaeToUserFields */
    
    
    /**
     * getPrintStringFieldRow() - get print string of specified row of file
     * @param dirName is directory name of file
     * @param srcFileName file name
     * @param row to read
     * @param msg to display while reading file
     * @return print string of the row else "" if failed
     * @see FileTable
     * @see FileTable#readTableFieldsFromFile
     */
    public String getPrintStringFieldRow(String dirName, String srcFileName,
                                         int row, String msg)
    { /* getPrintStringFieldRow */
      String fileName= (dirName==null) ? srcFileName : (dirName + srcFileName);
      FileTable ft= new FileTable(fileName);
      if(ft==null)
        return("");
      String
        sR= " Data from row #"+row+" in file["+fileName+"]\n",
        tmp[]= ft.readTableFieldsFromFile(fileName, row);
      
      if(tmp==null)
        return("Can't read row #"+row+" in file["+fileName+"]");
      
      if(msg==null)
        msg= "";                 /* prevent problems */
      
      for(int i=0;i<tmp.length;i++)
        sR += "    "+msg+"["+(i+1)+"] = '"+tmp[i]+"'\n";
      
      return(sR);
    } /* getPrintStringFieldRow */
    
    
    
}  /* end of class SetupLayouts */


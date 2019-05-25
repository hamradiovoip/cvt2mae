/** File: FieldMap.java */

package cvt2mae;


/**
 * FieldMap maps MAE (table,field) entries to User (T,F) entries 
 * which allows user's to specify variables in terms of what they require
 * and have it mapped to what MAExplorer requires.
 *<BR> 
 * This will parse a 1D array of (mT,mF,uT,uF) entries into discrete arrays
 * that are used in the actual field name mappings. Entries with a '*' prefix
 * mean that that entry may be repeated any number of times.
 *<PRE> 
 * Note:<BR> 
 *  1. the table definitions are specified in row [0].<BR> 
 *  2. entries [1:n] are tab-delimited (NOT comma-delimited). However,
 *     when the FieldMap associated with an ArrayLayout is saved in the .alo
 *     file, it is converted to a comma-delimited string since tabs are
 *     reserved for the .alo (N,V) data.<BR> 
 *
 * For example (part of the Affymetrix spec):
 *  desiredFields[]= {"Table name\tMAE field\tUser field",  // Table def at [0]
 *		      "GipoTable\tLocation\tprobe set",     // entries at [1:n]
 *  	              "*QuantTable\tRawIntensity\tAvg Diff", 
 *		      "*QuantTable\tQualCheck\tAbs Call",
 *                    "GipoTable\tGenBankAcc\tIdentifier", 
 *		      "GipoTable\tClone_ID\tProbe Set",
 *		      "GipoTable\tGeneName\tDescription"
 *		      };	
 *</PRE>
 *<P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), B. Stephens(SAIC), G. Thornwall (SAIC), NCI-Frederick, Frederick, MD
 * @version  $Date: 2005/10/20 11:45:56 $   $Revision: 1.6 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class FieldMap
{
  /** Global link to Cvt2Mae instance */
  private static Cvt2Mae
    cvt;
  /** master global counter for field map instances */
  public static int
    masterNbr= 0;
  /** initial max map size */
  public static int
    MAX_MAP_SIZE= 50;  
  /** full file path for file that contains field names */	       
  public String
    userFile;
  /** [0:nFieldsOfUserFile-1] user fields of current file */
  public String
    fieldsOfUserFile[]; 
  /** row in file where fields are found */
  public int
    rowWithFields;  
  /** # of data fields in user file */ 
  public int
    nFieldsOfUserFile;
  /** sul.REMAP_GIPO, .REMAP_QUANT, .REMAP_SAMPLE  
   * 0 if no remap in progress. Clear to 0 when finished mapping 
   * one of the 3 modes.
   */      
  public int
    remapMode;                
  /** GIPO fields selected by user */
  public String
    userGipoFields[];
  /** Quant fields selected by user */
  public String
    userQuantFields[];   
  /** Sample fields selected by user */    
  public String
    userSampleFields[];
  
  /** GIPO fields currently selected by user */
  public int
    pickedGipoFields[];
  /** Quant fields currently selected by user */
  public int
    pickedQuantFields[];   
  /** Sample fields currently selected by user */    
  public int
    pickedSampleFields[];
  /** unique FieldMap instance number */
  public int
    fmNbr;   
  /** maximum size of the map */
  public int
    maxSize;        
  /** # of map entries */
  public int
    nMap;  
  /** [0:nMap-1] map of internal MAE Table name */ 
  public String
    maeTableMap[];    
  /** [0:nMap-1] map of internal MAE Field name */ 
  public String
    maeFieldMap[];    
  /** [0:nMap-1] map external user's Table name */
  public String
    userTableMap[];   
  /** [0:nMap-1] map external user's Field name */
  public String
    userFieldMap[];  
  /** [0:nMap-1] map entry is repeated for each sample. NOTE: this is
   * indicated in the "mT,mF,uT,uF" string by a '*' prefix so it becomes
   * "*mT,mF,uT,uF". This means there may be 1 or more instances of this
   * field and they will be in different columns. (eg. Affy data) 
   */
  public boolean
    repeatMap[];   
  /** # of fields in table field list */
  public int
    tCols;
  /** unique map name */
  public String 
    mapName;  
  /** [0:tCols-1] field names for table */
  public String 
    tFields[];            
  /* --- Data for input file fields --- */
  /** [0:nMapFields-1] field names read from the data file. If a separate GIPO
   * file is used, then this also includes the mcd.sepGIPOfields[] such that
   * mcd.sepGIPOfieldsUsed[] fields are true.
   */
  public String
    fieldNamesGQ[];    
  /** # ofentries in  fieldNamesGQ */
  public int
    nMapFieldsGQ; 
  /** # of data file fields found in the map and marked true in useTokFlag[].
   * and is less than nMapFields. This is a debugging count and is not used
   * in any other methods.
   */
  public int
    nFieldsUsed; 
  
  /** [0:nMapFields-1] index map from fieldNames[f] to userFieldMap[u]. I.e.
   * idxUFmap[f]= u, such that userFieldMap[u] data equals fieldNames[f]
   */
  public int
    idxUFmap[];  
  /** [0:nMapFields-1] index map from userFieldMap[u] to fieldNames[f]. I.e.
   * idxFUmap[u]= f, such that userFieldMap[u] data same as fieldNames[f]
   */
  public int
    idxFUmap[];         
  
  /** [0:nMapFields-1] flag fields used to tokenize when parsing input table
   * data corresponding to fieldNames[0:nMapFields-1].  This avoids
   * having to parse tokens that are not going to be used - greatly
   * improving the efficiency of the parser. 
   */
  public boolean
    useTokFlag[];      
  /** new line character */  
  String 
    newline;
  
  
  /**
   * FieldMap() - constructor
   * @param mapName name of the map
   * @see #clearMap
   */
  public FieldMap(String mapName)
  { /* FieldMap */
    this.fmNbr= ++masterNbr;        /* generate serial #s */
    this.mapName= mapName;
    
    maxSize= MAX_MAP_SIZE;
    clearMap();                    /* clear out the map */
    
    String defFieldNames[]= {"Table name", "MAE field", "User field"};
    setFieldList(defFieldNames.length, defFieldNames);
    
    /* Lists of indexes of fields currently selected by user */
    pickedGipoFields= new int[maxSize];
    pickedQuantFields= new int[maxSize];
    pickedSampleFields= new int[maxSize];
  } /* FieldMap */
  
  
  /**
   * clearMap() - clear the map and reallocate the tables
   */
  public void clearMap()
  { /* clearMap */
    nMap= 0;
    remapMode= 0;                   /* no remap in progress */
    
    maeTableMap= new String[maxSize];
    maeFieldMap= new String[maxSize];
    userTableMap= new String[maxSize];
    userFieldMap= new String[maxSize];
    repeatMap= new boolean[maxSize];
    
    /* [CHECK] do we want to reinit this as well? */
    /*
    pickedGipoFields= new int[maxSize];
    pickedQuantFields= new int[maxSize];
    pickedSampleFields= new int[maxSize];
    */
  } /* clearMap */
  
  
  /**
   * setFieldList() - save the tFields[0:tCols-1]
   * @param tFields [0:tCols-1] names
   * @param tCols number of fields in field list
   */
  public void setFieldList(int tCols, String tFields[])
  { /* setFieldList */
    this.tCols= tCols;
    this.tFields= tFields;
    
    useTokFlag= new boolean[tCols];
    for(int i=0; i<tCols;i++)
      useTokFlag[i]= false;           /* default is use NONE */
  } /* setFieldList */
  
  
  /**
   * useField() - set flag to true in useFieldFlag[] for fName field.
   * @param fName field to use
   */
  public void useField(String fName)
  { /* useField */
    for(int i=0; i<tCols;i++)
      if(tFields[i].equals(fName))
      {
        useTokFlag[i]= true;        /* set to use this field */
        break;
      }
  } /* useField */
  
  
  /**
   * isEntry() - has an entry for (mT,mF) in the list
   * Return true if an entry exists.
   * @param mT Table map
   * @param mF Field map
   * @return true if an entry exists
   */
  public boolean isEntry(String mT, String mF)
  { /* isEntry */
    for(int i=0;i<nMap;i++)
      if(maeTableMap[i].equals(mT) && maeTableMap[i].equals(mF))
        return(true);
    
    return(false);
  } /* isEntry */
  
  
  /**
   * addEntry() - add an entry to the list
   * @param mT Table map
   * @param mF Field map
   * @param uT user Table Map
   * @param uF user Field Map
   */
  public void addEntry(String mT, String mF, String uT, String uF)
  { /* addEntry */
    addEntry(mT, mF, uT, uF, false);
  } /* addEntry */
  
  
  /**
   * addEntry() - add or replace an entry to the FieldMap list.
   * @param mT Table map
   * @param mF Field map
   * @param uT user Table Map
   * @param uF user Field Map
   * @param repeatFlag map entry is repeated for each sample
   */
  public void addEntry(String mT, String mF, String uT, String uF,
  boolean repeatFlag)
  { /* addEntry */
    int idx= nMap; /* will put new entry at end if does not already exist */
    
    for(int i=0;i<nMap;i++)
      if(mT.equals(maeTableMap[i]) && mF.equals(maeFieldMap[i]))
      { /* found existing entry */
        idx= i;
        break;
      }
    
    maeTableMap[idx]= mT;
    maeFieldMap[idx]= mF;
    userTableMap[idx]= uT;
    userFieldMap[idx]= uF;
    repeatMap[idx]= repeatFlag;
    if(idx==nMap)
      nMap++;
    
    /*
    if(cvt.DBUG_FLAG)
      System.out.println("FM-AE mapTF["+nMap+"]=("+
                         mT+","+mF+","+uT+","+uF+") repeat="+repeatFlag);
    */
  } /* addEntry */
  
  
  /**
   * rmvEntry() - remove an entry to the list by marking it inactive.
   * @param mT Table map
   * @param mF Field map
   * @return true if successful
   */
  public boolean rmvEntry(String mT, String mF)
  { /* rmvEntry */
    int idx= lookupMaeIndex(mT, mF);
    if(idx==-1)
      return(false);
    
    for(int i=idx;i<(nMap-1);i++)
    { /* Drop entry from list by copying entries towards front of list */
      maeTableMap[i]= maeTableMap[i+1];
      maeFieldMap[i]= maeFieldMap[i+1];
      userTableMap[i]= userTableMap[i+1];
      userFieldMap[i]= userFieldMap[i+1];
      repeatMap[i]= repeatMap[i+1];
    }
    
    /* Remove it so no trace at the end of the list. */
    maeTableMap[nMap-1]= null;
    maeFieldMap[nMap-1]= null;
    userTableMap[nMap-1]= null;
    userFieldMap[nMap-1]= null;
    repeatMap[nMap-1]= false;
    
    nMap--;                 /* decrement the count */
    
    return(true);
  } /* rmvEntry */
  
  
  /**
   * isDuplicateEntries() - test if duplcate entries for mT entries in mT subtable.
   * @param mT Table map
   * @return true if found
   */
  public boolean isDuplicateEntries(String mT)
  { /* isDuplicateEntries */
    for(int i=0;i<nMap;i++)
      if(mT.equals(maeTableMap[i]))
      { /* look for mF duplicates with this mT sub table */
        String
        uFi= userFieldMap[i];
        for(int j=0;j<nMap;j++)
          if(i!=j && uFi.equals(userFieldMap[j]))
            return(true); /* found one! */
      } /* look for mF duplicates with this mT sub table */
    return(false);
  } /* isDuplicateEntries */
  
  
  /**
   * lookupMaeIndex() - find map index given (mT, mF)
   * @param mT Table map
   * @param mF Field map
   * @return index if found, else -1 if not found
   */
  public int lookupMaeIndex(String mT, String mF)
  { /* lookupMaeIndex */
    int idx= -1;
    
    for(int i=0;i<nMap;i++)
      if(maeTableMap[i].equals(mT) &&
      maeFieldMap[i].equals(mF))
      {
        idx= i;
        break;
      }
    return(idx);
  } /* lookupMaeIndex */
  
  
  /**
   * lookupUserFieldFromMaeTF() - find map index given (mT, mF)
   * @param mT Table map
   * @param mF Field map
   * @return field else null if not found.
   */
  public String lookupUserFieldFromMaeTF(String mT, String mF)
  { /* lookupUserFieldFromMaeTF */
    int idx= lookupMaeIndex(mT, mF);
    if(idx!=-1)
      return(userTableMap[idx]);
    
    return(null);
  } /* lookupUserFieldFromMaeTF */
  
  
  /**
   * lookupUserIndex() - find map index given (uT, uF)
   * @param mT Table map
   * @param mF Field map
   * @return index else -1 if not found
   */
  public int lookupUserIndex(String uT, String uF)
  { /* lookupUserIndex */
    int idx= -1;
    for(int i=0;i<nMap;i++)
      if(userTableMap[i].equals(uT) &&
      userFieldMap[i].equals(uF))
      {
        idx= i;
        break;
      }
    
    return(idx);
  } /* lookupUserIndex */
  
  
  /**
   * lookupUserIndex() - find map index given (uF)
   * @param uF Field map
   * @return index else -1 if not found
   */
  public int lookupUserIndex(String uF)
  { /* lookupUserIndex */
    int idx= -1;
    for(int i=0;i<nMap;i++)
      if(userFieldMap[i].equals(uF))
      {
        idx= i;
        break;
      }
    
    return(idx);
  } /* lookupUserIndex */
  
  
  /**
   * lookupUserTFmapPair() - findUser "uT,uF" given (mT, mF)
   * Return null if not found.
   * @param mT Table map
   * @param mF Field map
   * @return TF pair else null if not found.
   */
  public String lookupUserTFmapPair(String mT, String mF)
  { /* lookupUserTFmapPair */
    int idx= lookupMaeIndex(mT, mF);
    if(idx==-1)
      return(null);
    String uTFpair= userTableMap[idx]+","+userFieldMap[idx];
    
    return(uTFpair);
  } /* lookupUserTFmapPair */
  
  
  /**
   * lookupUTFmapPair() - findUser "uT,uF" given (mT, mF)
   * @param mT Table map
   * @param mF Field map
   * @return looked up pair else (mT+","+mF) if not found - effectively a no-op.
   */
  public String lookupUTFmapPair(String mT, String mF)
  { /* lookupUTFmapPair */
    int idx= lookupMaeIndex(mT, mF);
    if(idx==-1)
      return(mT+","+mF);
    String uTFpair= userTableMap[idx]+","+userFieldMap[idx];
    
    return(uTFpair);
  } /* lookupUTFmapPair */
  
  
  /**
   * getFieldsFromUserFile() - get fields from user file
   * Return the # of rows found and save the data in
   * fieldsOfUserFile[0:nFieldsOfUserFile-1].
   * @param cvt is the global instance of Cvt2Mae
   * @param userFile is the file
   * @param rowWithFields location in file
   * @return field if found, else 0 if can't do it.
   * @see FileTable
   * @see FileTable#readTableFieldsFromFile
   */
  public int getFieldsFromUserFile(Cvt2Mae cvt, String userFile,
  int rowWithFields)
  { /* getFieldsFromUserFile */
    this.cvt= cvt;
    this.userFile= userFile;
    this.rowWithFields= rowWithFields;
    
    fieldsOfUserFile= null;
    nFieldsOfUserFile= 0;
    
    /* Open file and read row r tab-delim data if ok */
    FileTable fio= new FileTable(userFile);
    fieldsOfUserFile= fio.readTableFieldsFromFile(userFile,rowWithFields);
    if(fieldsOfUserFile!=null)
      nFieldsOfUserFile= fieldsOfUserFile.length;
    
    return(nFieldsOfUserFile);
  } /* getFieldsFromUserFile */
  
  
  /**
   * genUseTokFlags() - generate useTokFlagGQ[0:nMapFields-1] from
   * fieldNames[0:nMapFields-1] and FieldMap userFieldMap[0:nMap-1] data.
   * The useTokFlag[] is used by ParseTokens getDelimTokens() to avoid
   * having to parse tokens that are not going to be used - greatly
   * improving the efficiency of the parser.
   *<PRE>
   * This method finds pairs of (u,v)such that
   *     userFieldMap[u].equals(fieldNames[f])
   * and sets
   *     useTokFlag[f]= true;
   *     idxUFmap[f]= u;
   * </PRE>
   * @param fieldNames is the input table field names array.
   * @return useTokFlag[] if found, else null if errors.
   */
  public boolean[] genUseTokFlags(String fieldNamesGQ[])
  { /* genUseTokFlags */
    nMapFieldsGQ= fieldNamesGQ.length;
    this.fieldNamesGQ= fieldNamesGQ;      /* capture field map */
    
    useTokFlag= new boolean[nMapFieldsGQ];  /* default is all false */
    idxUFmap= new int[nMapFieldsGQ];
    idxFUmap= new int[MAX_MAP_SIZE];
    nFieldsUsed= 0;
    
    for(int u= 0;u<nMap;u++)
    { /* test userFieldMap[u] to see if found in fieldNames[f] */
      for(int f= 0;f<nMapFieldsGQ;f++)
        if(userFieldMap[u].equals(fieldNamesGQ[f]))
        { /* found first match - mark it as field to use */
          useTokFlag[f]= true;
          idxUFmap[f]= u; /* userFieldMap[u] data equals fieldNames[f] */
          idxFUmap[u]= f; /* fieldNames[f] data equals userFieldMap[u] */
          nFieldsUsed++;  /* debugging count - may multiple count
           * if doing N:1 mappings of fieldNames. */
          break;
        }  /* found match - mark it as field to use */
    } /* test userFieldMap[u] to see if found in fieldNames[f] */
    
    if(cvt.DBUG_FLAG)
    {
      System.out.println("FM-GUTF nMap="+nMap+" nMapFieldsGQ="+nMapFieldsGQ+
                         " nFieldsUsed="+nFieldsUsed);
      for(int f= 0;f<nMapFieldsGQ;f++)
        if(useTokFlag[f])
          System.out.println("   useTokFlag[f="+
                             f+"]="+ fieldNamesGQ[f]+"]= "+useTokFlag[f]+
                             " idxUFmap[f]="+idxUFmap[f]);
      for(int u= 0;u<nMap;u++)
        System.out.println("   uT[u="+u+"]="+userTableMap[u]+
                           " mF[u]="+ maeFieldMap[u]+
                           " uF[u]="+ userFieldMap[u]+
                           " idxFUmap[u]="+idxFUmap[u]);
      System.out.println("FM-GUTF.1 fm="+this.toString());
    }
    
    return(useTokFlag);
  } /* genUseTokFlags */
  
  
  /**
   * lookupMaeFieldToFieldNameIndex() - lookup FieldName index if exists
   * in the FieldNames[0:nMapFields-1]
   * @param mFsearch string to search for
   * @return FieldName index, else return -1.
   *
   */
  public int lookupMaeFieldToFieldNameIndex(String mFsearch)
  { /* lookupMaeFieldToFieldNameIndex */
    int
      f,
      idx= -1;
    String mF;
    
    for(int u= 0;u<nMap;u++)
    { /* lookup TF map index for valid FieldNames */
      f= idxFUmap[u];	/* maps to index in field names list */
      mF= maeFieldMap[u];
      if(mF.equals(mFsearch))
      { /* found match - return index for fieldNames[] */
        idx= f;
        break;
      }
    } /* lookup TF map index for valid FieldNames */
    
    return(idx);
  } /* lookupMaeFieldToFieldNameIndex */
  
  
  /**
   * toString() - make prettyprint string for object.
   * NOTE: This is ONLY for visual consumption.
   * @return pretty print string of this FieldMap instance.
   */
  public String toString()
  { /* toString */    
    String osName= System.getProperty("os.name");
    boolean isMacFlag= osName.equals("Mac OS");
    if(isMacFlag)
      newline= System.getProperty("line.separator");
    else
      newline= "\n";
    
    String s= "FieldMap["+fmNbr+"] mapName='"+mapName+"' nMap="+nMap+
              " tCols="+tCols;
    for(int i=0;i<tCols;i++)
    { /* add fields entries */
      s += newline+" tFields["+i+"]="+tFields[i];
    }
    for(int i=0;i<nMap;i++)
    { /* add map entries */
      String
      mT= maeTableMap[i],
      mF= maeFieldMap[i],
      uT= userTableMap[i],
      uF= userFieldMap[i];
      s += newline + " (mT,mF,uT,uF)["+i+"]=("+mT+","+mF+","+uT+","+uF+")";
    }
    
    return(s);
  } /* toString */
  
  
}  /* end of class FieldMap */

/** File: MaeSampleData.java */

package cvt2mae;

import java.awt.*;
import java.util.*;
import java.io.*;

/**
 * This class tracks the SampleDB.txt file data containing the list of samples.
 * It contains methods to read and write the Sample DB file.
 * Note: the data is kept in MaeConfigData mcd.
 * <P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), G. Thornwall (SAIC), B. Stephens(SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $   $Revision: 1.5 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */


public class MaeSampleData
{ /* MaeSampleData class */  
  /** link to global MaeConfigData database */
  public MaeConfigData
    mcd;                   
  /** Field names in the table */
  private String
    tFields[];
  /** [0:tRows-1][0:tCols-1] table data that is actually a 1D array [0:tRows-1] of 
   * rowData[0:tCols-1] 
   */  
  private String
    tData[][];             
  /** # of rows in the table */
  private int
    tRows;                 
  /** # of columns in the table */
  private int
    tCols;                 
  /** idx of "Sample_ID" field */
  private int
    idxSample_ID;          
  /** idx of "Project" field */
  private int
    idxProject;
  /** idx of "Database_File" field */            
  private int
    idxDatabase_File;      
  
  
  /**
   * MaeSampleData() - constructor
   * @param mcd is instance of MaeConfigData
   */
  public MaeSampleData(MaeConfigData mcd)
  { /* MaeSampleData */
    this.mcd= mcd;
  } /* MaeSampleData */
  
  
  /**
   * lookupFieldIndex() - lookup field index in the tFields[0:tCols-1] data.
   * @param field to lookup
   * @return index value >=0 if found, else return -1.
   */
  private int lookupFieldIndex(String field)
  { /* lookupFieldIndex */
    for (int i=0;i<tCols;i++)
      if(field.equals(tFields[i]))
        return(i);
    
    return(-1);
  } /* lookupFieldIndex */
  
  
  /**
   * findSampleFields() - find SampleDB fields in the first row.
   * @return true if all fields are present.
   * @see #lookupFieldIndex
   */
  private boolean findSampleFields()
  { /* findSampleFields */
    idxSample_ID= lookupFieldIndex("Sample_ID");
    idxProject= lookupFieldIndex("Project");
    idxDatabase_File= lookupFieldIndex("Database_File");
    
    boolean flag= (idxSample_ID!=-1 && idxProject!=-1 && idxDatabase_File!=-1);
    
    return(true);
  } /* findSampleFields */
  
  
  /**
   * readSamplesFile() - read Config/SamplesDB-fn.txt file into mcd state.
   * As a minimum it reads the following from mcd.samplesFile:
   *<PRE>
   *  File Field            MaeConfigData (mcd) variable
   *  ============          ============================
   *  "Sample_ID"           mcd.quantName[0:mcd.nQuantFiles-1]
   *  "Project"             mcd.prjName[0:mcd.nQuantFiles-1)
   *  "Database_File"       mcd.quantName[0:mcd.nQuantFiles-1]
   *</PRE>
   * @return true if succeed.
   */
  public boolean readSamplesFile()
  { /* readSamplesFile */
    /* [TODO] This needs to be rewritten! */
    
    FileTable sdbTbl= new FileTable("SamplesDB File");
    
    /* Read the table */
    if(!sdbTbl.readFileAsTable(mcd.samplesFile))
      return(false);
    
    /* Copy data to local variables */
    tCols= sdbTbl.tRows;
    tRows= sdbTbl.tCols;
    tFields= sdbTbl.tFields;
    tData= sdbTbl.tData;
    
    /* Find indices of SampleDB fields */
    if(!findSampleFields())
      return(false);
    
    /* read the variable part of the file */
    mcd.nQuantFiles= tCols;
    for(int r=0;r<tCols;r++)
    {
      String tRowData[]= tData[r];
      mcd.quantName[r]= tRowData[idxSample_ID];
      mcd.prjName[r]= tRowData[idxProject];
      mcd.quantName[r]= tRowData[idxDatabase_File];
    }
    
    return(true);
  } /* readSamplesFile */
  
  
  /**
   * writeSamplesFile() - create Config/SamplesDB-fn.txt file
   * As a minimum it writes the following to mcd.samplesFile:
   *<PRE>
   *  File Field            MaeConfigData (mcd) variable
   *  ============          ============================
   *  "Sample_ID"           mcd.quantName[0:mcd.nQuantFiles-1]
   *  "Project"             mcd.prjName[0:mcd.nQuantFiles-1]
   *  "Database_File"       mcd.quantName[0:mcd.nQuantFiles-1]
   *</PRE>
   * @return true if succeed.
   */
  public boolean writeSamplesFile()
  { /* writeSamplesFile */
    try
    { /* write out the derived fields */
      
      File outputFile= new File(mcd.samplesFile);
      FileWriter fw= new FileWriter(outputFile);
      
      String newline;
      if(mcd.cvt.isMacFlag)
        newline= System.getProperty("line.separator");
      else
        newline= "\n";
      
      /* Write the field names */
      fw.write("Sample_ID\tProject\tDatabase_File" + newline);
      
      /* Write the variable part of the file */
      for(int n=0;n<mcd.nQuantFiles;n++)
        fw.write(mcd.quantName[n]+"\t"+ /* Sample_ID */
                 mcd.prjName[n]+"\t"+   /* Project */
                 mcd.quantName[n]+      /* Database_File */
                 newline);
      
      fw.close();	    /* Close the output files */
    } /* write out the derived fields */
    
    catch(IOException ioe)
    {
      //err.appendLog("Couldn't write ["+mcd.samplesFile+"]\n");
      System.out.println("Couldn't write ["+mcd.samplesFile+"]\n");
      return(false);
    }
    
    return(true);
  } /* writeSamplesFile */
  
  
} /* MaeSampleData class */

/** File: MaeStartupData.java */

package cvt2mae;

import java.awt.*;
import java.util.*;
import java.io.*;

/**
 * This class read/write methods for MAE startup database.
 * Note: the data is kept in MaeConfigData mcd.
 *  <P>
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


public class MaeStartupData
{ /* MaeStartupDataclass */
  /** link to global MaeConfigData instance */
  public MaeConfigData
    mcd;
  
  
  /**
   * MaeStartupData() - Constructor MaeStartupData.
   * @param mcd is instance of MaeConfigData
   */
  public MaeStartupData(MaeConfigData mcd)
  { /* MaeStartupData */
    this.mcd= mcd;
  } /* MaeStartupData */
  
  
  /**
   * writeMAEstartupFile() - create MAE/Start.mae file via str. This one
   * works on the Mac
   * @return true if can write the file
   */
  public boolean writeMAEstartupFile()
  { /* writeMAEstartupFile */
    FileWriter maeFW;
    BufferedWriter maeBuf;
    PrintWriter maePW;
    String strBuf= new String("Name\tValue\n"); /* Write the field names */
    
    /* create the derived fields */
    
    /* Write the variable part of the file */
    strBuf= strBuf.concat("configFile\t"+ mcd.configName+"\n");
    if(mcd.database.length()>0)
      strBuf= strBuf.concat("database\t"+ mcd.database+"\n");
    if(mcd.dbSubset.length()>0)
      strBuf= strBuf.concat("dbSubset\t"+ mcd.dbSubset+"\n");
    
    for(int n=0;n<mcd.nQuantFiles;n++)
      strBuf= strBuf.concat("image"+(n+1)+"\t"+ mcd.quantName[n]+"\n");
    
    int
      n2= (mcd.nQuantFiles==1)
            ? 1
            : ((mcd.nQuantFiles==2 || mcd.nQuantFiles==3)
                 ? 2 : mcd.nQuantFiles/2);
    if(n2==0)
      n2= 1;
    String
      sX= "Xlist\t1",
      sY= "Ylist\t"+n2,
      sE= "Elist\t1";
    
    if(mcd.nQuantFiles>2)
    { /* build the Xlist and YLists */
      sX= "Xlist\t";         /* reset the lists */
      sY= "Ylist\t";
      for(int n=1;n<=n2;n++)
        sX += (n>1) ? (","+n) : (""+n); /* add rest as comma delim list*/
        for(int n=n2+1;n<=mcd.nQuantFiles;n++)
          sY += (n>n2) ? (","+n) : (""+n); /* add rest as comma delim list*/
    } /* build the Xlist and YLists */
    
    if(mcd.nQuantFiles>1)
      for(int n=2;n<=mcd.nQuantFiles;n++)
        sE += (","+n);    /* add rest as a comma delim list */
    
    strBuf= strBuf.concat(sX+"\n");
    strBuf= strBuf.concat(sY+"\n");
    strBuf= strBuf.concat(sE+"\n");
    
    strBuf= strBuf.concat("classNameX\tX Samples"+"\n");
    strBuf= strBuf.concat("classNameY\tY Samples"+"\n");
    strBuf= strBuf.concat("usePseudoXYcoords\tTRUE"+"\n");
    
    strBuf= strBuf.concat("useRatioData\t"+
                          ((mcd.useRatioDataFlag)
                              ? "TRUE" : "FALSE")+"\n");
    if(mcd.useRatioDataFlag)
    { /* ratio data specific stuff */
      /* If ratio data, save mcd.swapCy5Cy3DataFlag[] status */
      if(mcd.useRatioDataFlag)
        for(int n=0;n<mcd.nQuantFiles;n++)
        { /* Save the swapCy5Cy3 status */
          boolean
          flag= mcd.swapCy5Cy3DataFlag[n];
          String sFlag= (flag) ? "TRUE" : "FALSE";
          strBuf= strBuf.concat("HPcy53Flag-"+n+"\t" + sFlag+"\n");
        }
    } /* ratio data specific stuff */
    
    strBuf= strBuf.concat("allowNegQuantDataFlag\t"+
    mcd.allowNegQuantDataFlag+"\n");
    
    if(mcd.useRatioDataFlag && mcd.allowNegQuantDataFlag);
    { /* turn on Filter by Positive data if possible neg ratio data */
      /* HP data Filter subset mode: F1F2 HP-E list - hps.msListE[]*/
      int
        SS_MODE_ELIST= 7;
      strBuf= strBuf.concat("usePosQuantDataFlag\tTRUE"+"\n");
      strBuf= strBuf.concat("posQuantTestMode\t"+SS_MODE_ELIST+"\n");
    }
    
    try /* write out the derived fields */
    {
      FileWriter fw= new FileWriter(mcd.maeFile);
      BufferedWriter buff= new BufferedWriter(fw,50000);
      PrintWriter pw= new PrintWriter(buff);
      pw.print(strBuf);
      
      buff.close();
    }
    
    catch(IOException ioe)
    {
      System.out.println("MSD-WMSF: IO error, Couldn't write ["+
                         mcd.maeFile+"]\n");
      return(false);
    }
    
    return(true);
  } /* writeMAEstartupFile */
  
} /*  MaeStartupData class */

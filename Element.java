/** File: Element.java */

package cvt2mae;

/**
 * This class represents individual array data element corresponding to a spot.
 * It contains the relevant fields from the input data file that are used 
 * by the MAExplorer program.
 * <P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), B. Stephens(SAIC), G. Thornwall (SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $   $Revision: 1.8 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class Element 
{
  /** int value of Location */
  int
    iLocation;                
  /** sequential id for this Element */
  String
    spotNbr;
  /** either spotNbr or Location value */
  String
    location;
  /** generic Genomic ID */  
  String
    identifier;		                 
  /** GIPO data */
  String field;
  /** GIPO data */
  String
    grid;
  /** GIPO data */
  String
    grid_col;
  /** GIPO data */
  String
    grid_row;
  /** GIPO data */
  String
    NAME_GRC;                     
  /** GIPO data */
  String
    plate;
  /** GIPO data */
  String
    plate_col;
  /** GIPO data */
  String
    plate_row;
  /** GIPO data */
  String
    qualCheck;    
  /** GIPO data */
  String
    cloneID;
  /** GIPO data */
  String
    geneName;
  /** GIPO data */
  String
    unigene_cluster_ID;
  /** GIPO data */
  String
    unigene_cluster_name;
  /** GIPO data */
  String
    genBankAcc;
  /** GIPO data */
  String
    genBankAcc3;
  /** GIPO data */
  String
    genBankAcc5;
  /** GIPO data */
  String
    dbEst3;
  /** GIPO data */
  String
    dbEst5;
  /** GIPO data */
  String
    swissProtID;
  /** GIPO data */
  String
    locusLinkID;
  /** Quantification data  */
  String
    rawIntensity;
  /** Quantification data  */
  String              
    rawIntensity1;
  /** Quantification data  */
  String
    rawIntensity2;
  /** Quantification data  */
  String
    rawBkgrd;
  /** Quantification data  */
  String
    rawBkgrd1;
  /** Quantification data  */
  String
    rawBkgrd2;
  /** Quantification data  */
  String
    qualCheck1;
  /** Quantification data  */
  String
    qualCheck2;
  /** quant data Affy feature - Spot Detection Value (Det p-value)  */
  String 
    detValue;
  /** quant data Affy feature - "Diff call" is PM, MM, AM */
  String                 
    diffCall;
  /** [Future] quant data Affy feature - "Fold change" */
  String    
    foldChange; 
  
  
  /**
   * Element() - constructor for empty element
   * Note: strings are null!
   */
  public Element()
  { /* Element */
    
  } /* Element */
  
}  /* end of class Element */

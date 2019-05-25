/** File: ImageAnalysisMethod.java */

package cvt2mae;

/**
 * This class specifies segmentation software tool used in quantifying the image spot data. 
 * This information may be needed in disecting the input file.
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

public class ImageAnalysisMethod
{
  /** Tab-Delim string */
  public static final String
    TAB_DELIM_FMT= "Tab-Delim";
  /** Comma-Delim string */
  public static final String
    COMMA_DELIM_FMT= "Comma-Delim";
  /** XML string */
  public static final String
    XML_FMT= "XML";
  /** vendor who supplies the quantification tool */
  String
    vendor;       
  /** analysis program name; may be "N.A."  */
  String
    quantTool;       
  /** of quantTool; may be "N.A." */
  String
    versionQT;       
  /** "tab-delimited", "comma-delimited", "XML" */
  String
    format;          
  /** how the spots are encoded on the array */
  String
    spotCoding;      
  /** how the plates are encoded on the array  */
  String
    plateCoding;    
  /** "single" or "multiple" */
  String
    multHPsPerFile;  
  
  
  /**
   * ImageAnalysisMethod() - constructor
   * @param vendor vendor who supplies the quantification tool
   * @param quantTool analysis program name; may be "N.A."
   * @param versionQT of quantTool; may be "N.A."
   * @param format "tab-delimited", "comma-delimited", "XML"
   * @param spotCoding how the spots are encoded on the array
   * @param plateCoding how the plates are encoded on the array
   * @param multHPsPerFile "single" or "multiple"
   *
   */
  ImageAnalysisMethod(String vendor,
                      String quantTool,
                      String versionQT,
                      String format,
                      String spotCoding,
                      String plateCoding,
                      String multHPsPerFile )
  { /* ImageAnalysisMethod */
    this.vendor= vendor;
    this.quantTool= quantTool;
    this.versionQT= versionQT;
    this.format= format;
    this.spotCoding= spotCoding;
    this.plateCoding= plateCoding;
    this.multHPsPerFile= multHPsPerFile;
    
  } /* ImageAnalysisMethod */
  
  
} /* end of ImageAnalysisMethod */

/** File: PseudoArray.java */

package cvt2mae;

/**
 * This class generates a pseudo array from maxRowsExpected of spots. 
 * It assumes ONE field (i.e. no duplicate grids). The results are left in:
 *   (maxRowsComputed, maxGrids, maxGridRows, maxGridCols).
 *<P>
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

public class PseudoArray
{
  /** Optimal grid size for MAExplorer viewing */
  final public static int
    OPT_GRID_SIZE= 1200;           
  /** desired rows/cols aspect aspect for a grid */ 
  final public static double
    ROWS_TO_COLS_ASPECT_RATIO= 3.0/4.0;
  /** ARG: initial # of rows specified */     
  public int
    maxRowsExpected;
  /** RTN: final #rows computed for new max[G,R,C]*/             
  public int
    maxRowsComputed;               
  /** RTN: # estimated grids/field */
  public int
    maxGrids;                      
  /** RTN: # estimated rows/grid */
  public int
    maxGridRows;                   
  /** RTN: # estimated columns/grid */
  public int
    maxGridCols;                   
  
  
  /**
   * PseudoArray() - compute a reasonable PseudoArray geometry
   * such that we can visualize it easily with MAExplorer without it hogging
   * the whole screen.
   * We do not want too many grids or with # cols too large.
   * A maximum size of about 40 columns/grid is ok.
   * @param maxRowsExpected is the total # of spots in the array
   */
  PseudoArray(int maxRowsExpected)
  { /* PseudoArray */
    this.maxRowsExpected= maxRowsExpected;
    int
      n= maxRowsExpected,
      extra= 0,
      optimalGridSize= OPT_GRID_SIZE;
    
    /* Estimate # of grids. Assume a square aspect ratio */
    if(n<=optimalGridSize)
      maxGrids= 1;
    else
      maxGrids= (int)(n/optimalGridSize)+1;
    
    /* Estimate rows and columns from a rectangular grid
     * where cols= (4/3) rows. Then,
     * c= (4/3)r. r*c= area. Then (4/3)*r*r= area or
     * r= sqrt((3/4)*area).
     */
    if(maxRowsExpected>0)
      while(true)
      { /* iterate to optimal size leaving results in this.xxxx */
        float
          gridSize= n/maxGrids;
        maxGridRows= (int)Math.sqrt((double)ROWS_TO_COLS_ASPECT_RATIO*gridSize);
        maxGridCols= (int)(maxGridRows/ROWS_TO_COLS_ASPECT_RATIO);
        maxGridCols += extra;
        int estTotSize=  maxGrids*maxGridRows*maxGridCols;
        if(estTotSize>maxRowsExpected)
          break;
        else
          extra++;        /* keep trying until get out */
      } /* iterate to optimal size */
    
    maxRowsComputed= maxGrids*maxGridRows*maxGridCols;
  } /* PseudoArray */
  
} /* end of class PseudoArray */

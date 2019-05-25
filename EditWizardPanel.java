/** File: EditWizardPanel.java */

package cvt2mae;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import java.util.Date;

/**
 * Wizard GUI for editing data in MAE state (Edit Layout). 
 * <P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), B. Stephens(SAIC), G. Thornwall (SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $  $Revision: 1.15 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class EditWizardPanel extends Panel implements ItemListener, 
						      TextListener, MouseListener
{
  /* --- Data structures for specific MAE database files --- */
  /** global link to EditMaeProjectFiles instance */
  private EditMaeProjectFiles
    empf;                  
  /** global link to Cvt2Mae instance*/ 
  private static Cvt2Mae
    cvt;                        
  /** global link to CvtUGI instance */
  private CvtGUI
    gui;                        
  /** global linkn to SetupLayout instance */
  private SetupLayouts
    sul;                        
  /** global link to UtilCM intance */
  private UtilCM 
    util;                      
  /** Configuration copy of original MaeConfigData instance data for reset */
  private MaeConfigData
    origMcd;			
  /** link to global instance of MaeConfigData */
  private MaeConfigData
    mcd;	
  /** link to global MaeSampleData instance */        
  private MaeSampleData
    msd;	   
  /** link to global MaeStartupData instance */			    
  private MaeStartupData
    msud;
  /** last possible PANEL */
  final public int
    LAST_PANEL= 11;             
  /** # of popup panels */
  final public int
    MAXPANELS= LAST_PANEL;     
  /** # of popup panel rows */
  final public int
    PP_GRIDROWS= 10;          
  /** # of popup panel columns */
  final public int
    PP_GRIDCOLS= 1;   
  /** popup operation for xxx_PP() method */
  final public int              
    POPUP_PANEL= 1;            
  /** save operation for xxx_PP() method */
  final public int
    SAVE_PANEL= 2;            
  /** reset panel data mcd.xxxx to origMcd.xxx data */
  final public int
    DEFAULTS_PANEL= 3;  
  /** labels go here */
  private Panel	
    labelPanel;			
  /** text fields go here */
  private Panel	
    textPanel;			
  /** panel for data, labels & textFields */
  private Panel	
    mainPanel;
  /** check box */
  private Checkbox   
    checkBox;		
  /** # popup panel grid row layouts */
  public int
    gridRow;			
  /** # popup panel grid column layouts */
  public int                   
    gridCol;
  /** keep track of current panel */ 
  public int
    curPanel;
  /** keep track of current row */
  public int
    currentRow;			
  /** made changes when edited */ 
  public boolean
    madeChangesFlag;           
  /** to hide rows, [TODO] */ 
  public boolean  
    hiddenFlag;			
  /** max number of forms for data entry */  
  public int
    maxPanelCtr;		
  /** global font */
  private Font
    font;
  /** darker gray */
  private Color
    bkgrd1= new Color(204, 204, 204);
  /** lighter gray */
  private Color
    bkgrd2= new Color(240, 240, 240);
  /** global bk grd color */
  private Color
    bkGrdClr;		
  /** labels used in data forms */
  private Label
    label1;			
  /** labels used in data forms */
  private Label
    label2;			
  /** labels used in data forms */
  private Label
    label3;			
  /** labels used in data forms */
  private Label
    label4;			
  /** labels used in data forms */
  private Label
    label5;			
  /** labels used in data forms */
  private Label
    label6;			
  /** labels used in data forms */
  private Label
    label7;
  /** labels used in data forms */
  private Label
    label8;
  /** labels used in data forms */
  private Label
    label9;
   /** labels used in data forms */
  private Label
    label10;
  /** textFields used in data forms*/
  private TextField
    textField1;			
  /** textFields used in data forms*/
  private TextField
    textField2;			
  /** textFields used in data forms*/
  private TextField
    textField3;			
  /** textFields used in data forms*/
  private TextField
    textField4;			
  /** textFields used in data forms*/
  private TextField
    textField5;			
  /** textFields used in data forms*/
  private TextField
    textField6;			
  /** textFields used in data forms*/
  private TextField
    textField7;			
  /** textFields used in data forms*/
  private TextField
    textField8;			
  /** textFields used in data forms*/
  private TextField
    textField9;
  /** textFields used in data forms*/
  private TextField
    textField10;	
  /** checkbox used in data forms */
  private Checkbox     
    useCy5OverCy3CB;         
  /** checkbox used in data forms */
  private Checkbox     
    swapRowsColsCB;       	
  /** checkbox used in data forms */
  private Checkbox     
    useRatioDataCB;		
  /** checkbox used in data forms */
  private Checkbox     
    useCy3Cy5RatioDataCB;	
  /** checkbox used in data forms */
  private Checkbox     
    bkgdCorrectCB;		
  /** checkbox used in data forms */
  private Checkbox     
    ratioMedianCorrectionCB;	
  /** checkbox used in data forms */
  private Checkbox     
    allowNegQuantDataCB;	
  /** checkbox used in data forms */
  private Checkbox     
    usePseudoXYcoordsCB;	
  /** checkbox used in data forms */
  private Checkbox     
    reuseXYcoordsCB;	      
  /** checkbox used in data forms */
  private Checkbox     
    presentViewCB;	        
  /** checkbox used in data forms */
  private Checkbox     
    ignoreExtraFIELDSCB;
  /** checkbox used in data forms */
  private Checkbox     
    specifyGeometryByNbrSpotsCB; 
  /** checkbox used in data forms */
  private Checkbox     
    useRatioMedianCorrectionCB; 
  /** checkbox used in data forms */
  private Checkbox     
    useBackgroundCorrectionCB;  
  /** checkbox used in data forms */
  private Checkbox     
    useCy5_Cy3CB;      
  /** checkbox used in data forms */
  private Checkbox     
    usePosQuantDataCB;
  /** checkbox used in data forms */
  private Checkbox
    hasGeneClassDataCB;
  /** checkbox used in data forms */
  private Checkbox
    hasPlateDataCB;
  /** checkbox used in data forms */
  private Checkbox
    hasCloneIDsCB;
  /** checkbox used in data forms */
  private Checkbox
    hasGenBankIDsCB;
  /** checkbox used in data forms */
  private Checkbox
    hasUniGeneIDsCB;
  /** checkbox used in data forms */
  private Checkbox
    hasDB_ESTIDsCB;
  /** checkbox used in data forms */
  private Checkbox
    hasLocusIDsCB;
  /** checkbox used in data forms */
  private Checkbox
    hasSwissProtIDsCB;
  /** checkbox used in data forms */
  private Checkbox
    hasUniGeneNameCB;
  /** checkbox used in data forms for Assign fields */
  private Checkbox
    hasQuantXYcoordsCB;      
  /** checkbox used in data forms */
  private Checkbox
    hasBkgrdDataCB;
  /** checkbox used in data forms */
  private Checkbox
    hasQualCheckQuantDataCB;
  /** checkbox used in data forms */
  private Checkbox
    hasQualCheckGIPOdataCB;
  /** checkbox used in data forms */
  private Checkbox
    useMolDyn_NAME_GRC_specCB;
  /** checkbox used for getting genomic IDs from Description */
  private Checkbox
    hasGenomicIDsfromDescrCB; 
  /** checkbox used for has Location */
  private Checkbox
    hasLocationIdCB;
  /** PP Q&A choice */
  private Choice
    speciesChoice; 
  /** holds current mouseover data */
  public String
    mouseOverStr[];        
  /** placed in status label */  
  public String
    panelTitle;			
  /** use whatever is first in the list */ 
  public String
    firstPanelStr;           
  /** frame OK prompt dialog box */
  private Frame
    popupDialogFrameOK;    
  /** frame for yes/no dialog box */
  private Frame
    popupDialogFrame;	
  /** OK prompt dialog box */
  private PopupEditPrjDialog
    okpdq;		
  /** yes/no dialog box  */
  private PopupEditPrjDialog
    pdq;		
  /** TF altered T=data saved */
  public boolean
    textFieldFlag;		
  /** CB altered T=data save */
  public boolean
    checkBoxFlag;		
  /** if file saved */
  public boolean
    fileSavedFlag;	
  /** Geo Platform ID for web access */
  public String
    geoPlatformID;
  
 
  /**
   * EditWizardPanel() - constructor
   * @param empf EditMaeProjectFile
   * @param int initialCurPannel
   * @see #setupPopupPanelGUI
   */
  public EditWizardPanel(EditMaeProjectFiles empf, int initialCurPannel)
  { /* EditWizardPanel */
    this.empf= empf;
    mcd= empf.mcd;
    origMcd= empf.origMcd;    /* CHECK] beofre 8-20-02 it copied mcd not origMcd!!! */
    cvt= mcd.cvt;             /* update */
    gui= cvt.gui;
    sul= cvt.sul;
    util= cvt.util;
    bkGrdClr= bkgrd1;                    /*  */
    currentRow= 0;                       /* start at first row */
    hiddenFlag= true;
    fileSavedFlag= true;
    textFieldFlag= true;
    checkBoxFlag= true;
    curPanel= initialCurPannel;		  /* start at panel 1 */
    maxPanelCtr= MAXPANELS;
    madeChangesFlag= false;   /* set if made any legal changes.  This is then
     * tested on "Done" to determine if enable
     * "Save Layout" flag. */
    geoPlatformID= "";
    setupPopupPanelGUI();     /* initialize */
  } /* EditWizardPanel */
  
  
  /**
   * setupPopupPanelGUI() - create initial popup panel for display
   * This will be reset with new data for each new panel.
   * @see PopupEditPrjDialog
   */
  private void setupPopupPanelGUI()
  { /* setupPopupPanelGUI */
    bkGrdClr= bkgrd1;                   /*  */
    
    currentRow= 0;                      /* start at first row */
    hiddenFlag= true;
    fileSavedFlag= true;
    curPanel= 1;		          /* start at first panel */
    maxPanelCtr= MAXPANELS;
    
    gridRow= PP_GRIDROWS;               /* # of elements in popup panel */
    gridCol= PP_GRIDCOLS;
    
    firstPanelStr= "[1] Array layout name and vendor - (ALO file version:"+
                   cvt.ALO_VERSION+")";
    
    /* use whatever is first in the list */   
    
    label1= new Label();
    label2= new Label();
    label3= new Label();
    label4= new Label();
    label5= new Label();
    label6= new Label();
    label7= new Label();
    label8= new Label();
    label9= new Label();
    label10= new Label();
    
    label1.setFont(font);
    label1.setBackground(bkGrdClr);
    label1.setForeground(Color.black);
    
    label2.setFont(font);
    label2.setBackground(bkGrdClr);
    label2.setForeground(Color.black);
    
    label3.setFont(font);
    label3.setBackground(bkGrdClr);
    label3.setForeground(Color.black);
    
    label4.setFont(font);
    label4.setBackground(bkGrdClr);
    label4.setForeground(Color.black);
    
    label5.setFont(font);
    label5.setBackground(bkGrdClr);
    label5.setForeground(Color.black);
    
    label6.setFont(font);
    label6.setBackground(bkGrdClr);
    label6.setForeground(Color.black);
    
    label7.setFont(font);
    label7.setBackground(bkGrdClr);
    label7.setForeground(Color.black);
    
    
    label8.setFont(font);
    label8.setBackground(bkGrdClr);
    label8.setForeground(Color.black);
    
    label9.setFont(font);
    label9.setBackground(bkGrdClr);
    label9.setForeground(Color.black);
    
    label10.setFont(font);
    label10.setBackground(bkGrdClr);
    label10.setForeground(Color.black);
    
    label1.addMouseListener(this);
    label2.addMouseListener(this);
    label3.addMouseListener(this);
    label4.addMouseListener(this);
    label5.addMouseListener(this);
    label6.addMouseListener(this);
    label7.addMouseListener(this);
    label8.addMouseListener(this);
    label9.addMouseListener(this);
    label10.addMouseListener(this);
    
    textField1= new TextField();
    textField2= new TextField();
    textField3= new TextField();
    textField4= new TextField();
    textField5= new TextField();
    textField6= new TextField();
    textField7= new TextField();
    textField8= new TextField();
    textField9= new TextField();
    textField10= new TextField();
    
    
    textField1.setBackground(Color.white);
    textField1.setFont(font);
    textField1.setForeground(Color.black);
    
    textField2.setBackground(Color.white);
    textField2.setFont(font);
    textField2.setForeground(Color.black);
    
    textField3.setBackground(Color.white);
    textField3.setFont(font);
    textField3.setForeground(Color.black);
    
    textField4.setBackground(Color.white);
    textField4.setFont(font);
    textField4.setForeground(Color.black);
    
    textField5.setBackground(Color.white);
    textField5.setFont(font);
    textField5.setForeground(Color.black);
    
    textField6.setBackground(Color.white);
    textField6.setFont(font);
    textField6.setForeground(Color.black);
    
    textField7.setBackground(Color.white);
    textField7.setFont(font);
    textField7.setForeground(Color.black);
    
    textField8.setBackground(Color.white);
    textField8.setFont(font);
    textField8.setForeground(Color.black);
    
    textField9.setBackground(Color.white);
    textField9.setFont(font);
    textField9.setForeground(Color.black);
    
    textField10.setBackground(Color.white);
    textField10.setFont(font);
    textField10.setForeground(Color.black);
    
    
    if(cvt.NEVER)
    { /* Setup text listeners - only if do something with the typed data */
      textField1.addTextListener(this);
      textField2.addTextListener(this);
      textField3.addTextListener(this);
      textField4.addTextListener(this);
      textField5.addTextListener(this);
      textField6.addTextListener(this);
      textField7.addTextListener(this);
      textField8.addTextListener(this);
      textField9.addTextListener(this);
      textField10.addTextListener(this);
    }
    
    useCy5OverCy3CB= new Checkbox();
    swapRowsColsCB= new Checkbox();
    useRatioDataCB= new Checkbox();
    specifyGeometryByNbrSpotsCB= new Checkbox();
    useCy3Cy5RatioDataCB= new Checkbox();
    bkgdCorrectCB= new Checkbox();
    ratioMedianCorrectionCB= new Checkbox();
    allowNegQuantDataCB= new Checkbox();
    usePseudoXYcoordsCB= new Checkbox();
    reuseXYcoordsCB= new Checkbox();
    presentViewCB= new Checkbox();
    ignoreExtraFIELDSCB= new Checkbox();
    
    useRatioDataCB.setBackground(bkGrdClr);
    useRatioDataCB.setFont(font);
    
    hasGeneClassDataCB= new Checkbox();
    
    hasLocationIdCB= new Checkbox();
    hasCloneIDsCB= new Checkbox();
    hasGenBankIDsCB= new Checkbox();
    hasUniGeneIDsCB= new Checkbox();
    hasDB_ESTIDsCB= new Checkbox();
    hasLocusIDsCB= new Checkbox();
    hasSwissProtIDsCB= new Checkbox();
    hasPlateDataCB= new Checkbox();
    hasGenomicIDsfromDescrCB= new Checkbox();
    hasUniGeneNameCB= new Checkbox();
    hasQuantXYcoordsCB= new Checkbox();
    hasBkgrdDataCB= new Checkbox();
    hasQualCheckQuantDataCB= new Checkbox();
    hasQualCheckGIPOdataCB= new Checkbox();
    useMolDyn_NAME_GRC_specCB= new Checkbox();
    
    /* Highlight selected checkboxes */
    useRatioDataCB.setForeground(Color.red);
    specifyGeometryByNbrSpotsCB.setForeground(Color.red);
    
    /* for popup dialog boxes */
    popupDialogFrame= new Frame();
    pdq= new PopupEditPrjDialog(popupDialogFrame, mcd);
    
    popupDialogFrameOK= new Frame();
    okpdq= new PopupEditPrjDialog(popupDialogFrameOK, mcd, " ");
    
    /* for Mouseover */
    mouseOverStr= new String[MAXPANELS];
    for(int i=0;i<MAXPANELS;i++)
      mouseOverStr[i]= "";
  } /* setupPopupPanelGUI */
  
  
  /**
   * addMouseOver() - add current mouse over string for label[idx]
   * @param idx index for array of mouseOver messages
   * @param msg String message for mouseOver
   */
  private void addMouseover(int idx, String msg)
  { /* addMouseOver */
    mouseOverStr[idx]= msg;
  } /* addMouseOver */
  
  
  /**
   * next() - goto next data panel
   * handle the last entry by staying at the same panel.
   * Note: we can call next() with the doneFlag set
   * to just save the curPanel.
   * @param doneFlag boolean passed down to doEditPanel
   * @return next panel if it exists else null
   * @see #doEditPanel
   */
  public Panel next(boolean doneFlag)
  { /* next */
    Panel tmpPanel= null;
    int lastPanel= (empf.editFilesFlag) ? LAST_PANEL+1 : LAST_PANEL;
    
    /* [1] Save the current panel.
     * Note that if all data fields filled out by user OK
     * do not let go to the next panel until done.
     */
    tmpPanel= doEditPanel(SAVE_PANEL, doneFlag, curPanel);
    
    /* [2] Popup the next panel */
    curPanel++;                         /* compute number of next panel */
    curPanel= Math.min(curPanel, lastPanel);
    tmpPanel= doEditPanel(POPUP_PANEL, doneFlag, curPanel); /* Popup  if allowed */
    
    return(tmpPanel);
  } /* next */
  
  
  /**
   * back() - go back one data set and popup the edit panel.
   * @return current panel
   * @see #doEditPanel
   */
  public Panel back()
  { /* back */
    Panel tmpPanel= null;
    
    if(curPanel > 1)
    { /* within range */
      /* Save the current panel.
       * Note that if all data fields filled out by user OK
       * do not let go to the next panel until done.
       */
      tmpPanel= doEditPanel(SAVE_PANEL, false, curPanel);
      
      curPanel--;         /* backup to previous panel */
      
      if(curPanel<=1)
        curPanel= 1;     /* don't go below 1st panel */
      
      tmpPanel= doEditPanel(POPUP_PANEL, false, curPanel);
    } /* within range */
    
    return(tmpPanel);
  } /* back */
  
  
  /**
   * resetCurrentPanelDefaults() - reset current panel from origMcd.xxxx data.
   * @return current panel
   * @see #doEditPanel
   */
  public Panel resetCurrentPanelDefaults()
  { /* resetCurrentPanelDefaults */
    Panel tmpPanel= doEditPanel(DEFAULTS_PANEL, false, curPanel);
    return(tmpPanel);
  } /* resetCurrentPanelDefaults */
  
  
  /**
   * doEditPanel() - popup panelNbr edit panel to allow changing a subset of the data.
   * @param panelCode is either POPUP_PANEL or SAVE_PANEL
   * @param doneFlag if allow pressing the Done button
   * @param panelNbr to edit
   * @return Panel being edited
   * @see #arrayLayoutNamePanel_PP
   * @see #classNamePanel_PP
   * @see #databasePanel_PP
   * @see #DNAcalibPanel_PP
   * @see #filesPanel_PP
   * @see #fluoresentPanel_PP
   * @see #GeneNamesPanel_PP
   * @see #GeometricPanel_PP
   * @see #GenomicIDsPanel_PP
   * @see #InputFileRowsPanel_PP
   * @see #thresholdsPanel_PP
   * @see #XYcoordsPanel_PP
   */
  public Panel doEditPanel(int panelCode, boolean doneFlag, int panelNbr)
  { /* doEditPanel */
    Panel tmpPanel= null;                 /* next panel to popup */
    
    /*
    if(EMPF_DBUG && panelCode==SAVE_PANEL)
      System.out.println("EMPF-DEP panelTitle["+panelNbr+"]="+panelTitle+
                         " madeChangesFlag="+madeChangesFlag);
    */
    
    if(panelCode==POPUP_PANEL)
    { /* POPUP specific */
      empf.backButtonFlag= true;        /* Default, then overide */
      empf.nextButtonFlag= true;
      empf.addFinishButtonFlag= false;  /* only add for last panel if
       * editing file and going forward */
      
      if((empf.editFilesFlag && panelNbr>=LAST_PANEL+1) ||
          (!empf.editFilesFlag && panelNbr>=LAST_PANEL))
      { /* set status for last panel */
        empf.nextButtonFlag= false;
        empf.addFinishButtonFlag= true;
      }
    } /* POPUP specific */
    
    switch(panelNbr)
    { /* dispatch panels */
      case 1:
        empf.backButtonFlag= false;
        tmpPanel= arrayLayoutNamePanel_PP(panelCode);
        break;
      case 2:
        tmpPanel= GeometricPanel_PP(panelCode);
        break;
      case 3:
        tmpPanel= InputFileRowsPanel_PP(panelCode);
        break;
      case 4:
        tmpPanel= fluoresentPanel_PP(panelCode);
        break;
      case 5:
        tmpPanel= XYcoordsPanel_PP(panelCode);
        break;
      case 6:
        tmpPanel= GenomicIDsPanel_PP(panelCode);
        break;
      case 7:
        tmpPanel= GeneNamesPanel_PP(panelCode);
        break;
      case 8:
        tmpPanel= DNAcalibPanel_PP(panelCode);
        break;
      case 9:
        tmpPanel= databasePanel_PP(panelCode);
        break;
      case 10:
        tmpPanel= classNamePanel_PP(panelCode);
        break;
      case 11:
        tmpPanel= thresholdsPanel_PP(panelCode);
        break;
      case 12:
        tmpPanel= filesPanel_PP(panelCode);
        break;
        
      default:
        break;
    } /* dispatch panels */
    
    return(tmpPanel);
  } /* doEditPanel */
  
  
  /**
   * reset() - reset current data panel to orig data
   */
  public void reset()
  { /* reset */
    /* [TODO] copy orig to mcd */
    textFieldFlag= true;
    checkBoxFlag= true;
    fileSavedFlag= true;
  } /* reset */
  
  
  /**
   * globalReset() - reset all data to orig
   */
  public void globalReset()
  { /* globalReset */
    if(empf.editFilesFlag)
    { /* reset if data read in from file */
      textFieldFlag= true;
      checkBoxFlag= true;
      fileSavedFlag= true;
    }
  } /* globalReset */
  
  
  /**
   * setupPanels() - setup common parameters for the popup panels (_PP)
   * @param gridRow Grid Row
   * @param gridCol Grid Column
   */
  private void setupPanels(int gridRow, int gridCol)
  { /* setupPanels */
    /* main panel for the data forms */
    mainPanel= new Panel();
    mainPanel.setLayout(new GridLayout(1, 2));
    mainPanel.setFont(font);
    mainPanel.setBackground(bkGrdClr);
    mainPanel.setForeground(Color.black);
    
    /* label panel for data panel */
    labelPanel= new Panel();
    labelPanel.setLayout(new GridLayout(gridRow, gridCol));
    labelPanel.setFont(font);
    labelPanel.setBackground(bkGrdClr);
    labelPanel.setForeground(Color.black);
    
    label1= new Label(" ");       /* default to empty slots */
    label2= new Label(" ");
    label3= new Label(" ");
    label4= new Label(" ");
    label5= new Label(" ");
    label6= new Label(" ");
    label7= new Label(" ");
    label8= new Label(" ");
    label9= new Label(" ");
    label10= new Label(" ");
    
    
    label1.addMouseListener(this);
    label2.addMouseListener(this);
    label3.addMouseListener(this);
    label4.addMouseListener(this);
    label5.addMouseListener(this);
    label6.addMouseListener(this);
    label7.addMouseListener(this);
    label8.addMouseListener(this);
    label9.addMouseListener(this);
    label10.addMouseListener(this);
    
    /* text panel for data entry */
    textPanel= new Panel();
    textPanel.setLayout(new GridLayout(gridRow, gridCol));
    textPanel.setFont(font);
    textPanel.setBackground(bkGrdClr);
    textPanel.setForeground(Color.black);
    
    speciesChoice= null;              /* will create when used */
    /* for Mouseover */
    mouseOverStr= new String[MAXPANELS];
    for(int i=0;i<MAXPANELS;i++)
      mouseOverStr[i]= "";
    
    if(empf.mouseoverTA!=null)
      empf.mouseoverTA.setText("");   /* clear it out */
  } /* setupPanels */
  
  
  /**
   * addLabelPanelsToMainPanel() - add all labelPanels' to mainPanel
   */
  private void addLabelPanelsToMainPanel()
  { /* addLabelPanelsToMainPanel */
    labelPanel.add(label1);
    labelPanel.add(label2);
    labelPanel.add(label3);
    labelPanel.add(label4);
    labelPanel.add(label5);
    labelPanel.add(label6);
    labelPanel.add(label7);
    labelPanel.add(label8);
    labelPanel.add(label9);
    labelPanel.add(label10);
    mainPanel.add(labelPanel);
  } /* addLabelPanelsToMainPanel */
  
  
  /**
   * arrayLayoutNamePanel_PP() - popup panel handler for array layout
   * name and vendor.
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables).
   * @param int panelCode is POPU_PANEL or SAVE_PANEL
   * @return panel being edited
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel arrayLayoutNamePanel_PP(int panelCode)
  { /* arrayLayoutNamePanel_PP */
    panelTitle= " ["+curPanel+
                "] Array layout name and vendor - (ALO file version:"+
                cvt.ALO_VERSION+")";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.layoutName= origMcd.layoutName;
      mcd.aloFileName= origMcd.aloFileName;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      String
        layoutName= textField1.getText(),
        vendor= textField2.getText();
      
      /*
      if(EMPF_DBUG)
        System.out.println("EMPF-aloPP panelTitle["+curPanel+"]="+panelTitle+
                           " madeChangesFlag="+madeChangesFlag+
                           " layoutName="+layoutName+
                           " vendor="+vendor);
     */
      
      /* Save edited data.
       * [TODO] Verify reasonable data - ...
      */
      if(layoutName.length()>0 && !layoutName.equals(mcd.layoutName))
      { /* ok - save it */
        /* [TODO] make sure if new name, not same
         * as other name.
         * Problem: what if editing current
         * name and left it alone ...
         */
        String aloFileName= util.mapSpaceToMinus(layoutName);
        if(!aloFileName.endsWith(".alo"))
          aloFileName += ".alo";
        mcd.layoutName= layoutName;
        mcd.aloFileName= aloFileName;
        madeChangesFlag= true;
      }  /* ok - save it */
      
      if(!vendor.equals(mcd.vendor))
      { /* ok - save it */
        mcd.vendor= vendor;
        madeChangesFlag= true;
      } /* ok - save it */
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("Array layout name");
      addMouseover(1,"Unique name of the array layout designator. This is generally"+
                   "\nspecified by the chip vendor. If it is your own chip"+
                   "\nthen use your own designator to differentiate"+
                   "\nyour chip designs.");label2.setText("Vendor name for the array");
      
      addMouseover(2,"Name of the chip vendor. If your are specifying a"+
                   "\n<User-defined> chip, then you can use whatever you"+
                   "\nwish - eg. your organization.");      
      
      addLabelPanelsToMainPanel(); /* add all labelPanels' to mainPanel */
      
      textField1.setText(mcd.layoutName);
      textPanel.add(textField1);
      
      textField2.setText(mcd.vendor);
      textPanel.add(textField2);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* arrayLayoutNamePanel_PP */
  
  
  /**
   * GeometricPanel_PP() - popup panel handler for Grid (F,G,R,C) data.
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables checked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel GeometricPanel_PP(int panelCode)
  { /* GeometricPanel_PP */
    panelTitle= " ["+curPanel+
                "] Grid geometry data";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.maxFields= origMcd.maxFields;
      mcd.maxGrids= origMcd.maxGrids;
      mcd.maxGridRows= origMcd.maxGridRows;
      mcd.maxGridCols= origMcd.maxGridCols;
      mcd.maxRowsExpected= origMcd.maxRowsExpected;
      mcd.maxRowsComputed= origMcd.maxRowsComputed;  /* computed */
      mcd.nbrGenesCalc= origMcd.nbrGenesCalc;
      mcd.nbrSpotsCalc= origMcd.nbrSpotsCalc;
      mcd.spotRadius= origMcd.spotRadius;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      int
        maxFields= util.cvs2i(textField1.getText(),0),
        maxGrids= util.cvs2i(textField2.getText(),0),
        maxGridRows= util.cvs2i(textField3.getText(),0),
        maxGridCols= util.cvs2i(textField4.getText(),0),
        maxRowsExpected= util.cvs2i(textField7.getText(),0);
      boolean
        useMolDyn_NAME_GRC_specFlag= useMolDyn_NAME_GRC_specCB.getState(),
        specifyGeometryByNbrSpotsFlag= specifyGeometryByNbrSpotsCB.getState();
      
      /* [TODO] Verify reasonable data - ... */
      boolean
        validFGRCrangesFlag= (!specifyGeometryByNbrSpotsFlag &&
                              (maxRowsExpected > 1) ||
                               (maxFields > 0 && maxFields<=2 && maxGrids > 0 &&
                                maxGridCols > 0 && maxGridRows > 0)),
        changeFGRCflag= (validFGRCrangesFlag &&
                         (maxFields!=maxFields || maxGrids!=mcd.maxGrids ||
                          maxGridRows!=mcd.maxGridRows ||
                          maxGridCols!=mcd.maxGridCols)),
        changeNbrflag= (specifyGeometryByNbrSpotsFlag &&
                        maxRowsExpected>1 &&
                        maxRowsExpected!=mcd.maxRowsExpected);
      
      /*
      if(EMPF_DBUG)
        System.out.println("EMPF-GeoPP panelTitle["+curPanel+"]="+panelTitle+
                           " madeChangesFlag="+madeChangesFlag+
                           " validFGRCrangesFlag="+validFGRCrangesFlag+
                           " changeFGRCflag="+changeFGRCflag+
                           " changeNbrflag="+changeNbrflag);
       */
      // TODO if the "use # spots..." flag is set to true then it should not go into
      // here
      if(changeFGRCflag || changeNbrflag ||
         mcd.specifyGeometryByNbrSpotsFlag!=specifyGeometryByNbrSpotsFlag ||
         mcd.useMolDyn_NAME_GRC_specFlag!=useMolDyn_NAME_GRC_specFlag)
      { /* ok - save it */
        mcd.specifyGeometryByNbrSpotsFlag= specifyGeometryByNbrSpotsFlag;
        mcd.useMolDyn_NAME_GRC_specFlag= useMolDyn_NAME_GRC_specFlag;
        int maxRowsComputed= mcd.maxRowsComputed;
        if(specifyGeometryByNbrSpotsFlag)
        { /* compute a reasonable PseudoArray geometry */
          mcd.maxRowsExpected= maxRowsExpected;
          maxFields= 1;   /* force it to 1 field */
          PseudoArray
          psa= new PseudoArray(maxRowsExpected);
          maxGrids= psa.maxGrids;
          maxGridRows= psa.maxGridRows;
          maxGridCols= psa.maxGridCols;
          maxRowsComputed= psa.maxRowsComputed;
        }
        else
          mcd.useMolDyn_NAME_GRC_specFlag= useMolDyn_NAME_GRC_specFlag;
        
        /* Compute # of spots and genes */
        mcd.nbrGenesCalc= maxGrids*maxGridRows*maxGridCols;
        mcd.nbrSpotsCalc= maxFields*mcd.nbrGenesCalc;
        
        mcd.maxFields= maxFields;
        mcd.maxGrids= maxGrids;
        mcd.maxGridRows= maxGridRows;
        mcd.maxGridCols= maxGridCols;
        mcd.maxRowsComputed= maxRowsComputed;
        if(specifyGeometryByNbrSpotsFlag)
          mcd.maxRowsExpected= mcd.nbrGenesCalc;
        
        /* Heuristic: Compute better spot size.
         * The spot size may be ignored by MAExplorer.
         */
        if(maxGridCols*maxFields > 30)
          mcd.spotRadius= 5;
        else
          mcd.spotRadius= 7;
        
        madeChangesFlag= true;
      } /* ok - save it */
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("Number of duplicated spot Fields in array");
      addMouseover(1, "This is the number of duplicated spot Fields in array."+
                   "\nEVERY spot is duplicated. For example, each grid of spots is"+
                   "\nduplicated. We refer to these in MAExplorer as F1 and F2."+
                   "\nIf there are no duplicates, then there is 1 field.");
      
      label2.setText("Number of Grids per Field");
      addMouseover(2, "Number of Grids per Field. A grid contains"+
                   "\nGrid Rows X Grid Columns of spots.");
      
      label3.setText("Number of spots per Grid Row");
      addMouseover(3, "Number of spots in a grid row.");
      
      label4.setText("Number of spots per Grid Column");
      addMouseover(4, "Number of spots in a grid column.");      
      
      label5.setText("Use Mol.Dynamics 'NAME-GRC' else (Grid,Row,Col)");
      addMouseover(5, "Use the Molecular Dynamics 'NAME-GRC' specification for"+
                   "\n (grid, row, column) otherwise use separate fields for"+
                   " (grid, grid_row, grid_col)");
      
      label6.setText("Specify array layout by Grid-geometry OR by # spots/array");
      addMouseover(6, "If you specify the array layout by Grid-geometry (ABOVE),"+
                   "\nthen enter (#Fields, #Grids, #Grid-rows,#Grid-cols)."+
                   "\nIf you specify the layout by the maximum number of spots"+
                   "\nin the array (BELOW), it will estimate a pseudo-layout"+
                   "\nthat the spots will fit on the this array for"+
                   "\nvisualization purposes. It does not correspond to the"+
                   "\nactual array layout which you do not have to enter.");
      
      label7.setText("Maximum number of spots in array");
      addMouseover(7, "This is the maximum number of spots that may occur"+
                   "\nin your data.");
      
      addLabelPanelsToMainPanel(); /* add all labelPanels' to mainPanel */
      
      textField1.setText(String.valueOf(mcd.maxFields));
      textPanel.add(textField1);
      
      textField2.setText(String.valueOf(mcd.maxGrids));
      textPanel.add(textField2);
      
      textField3.setText(String.valueOf(mcd.maxGridRows));
      textPanel.add(textField3);
      
      textField4.setText(String.valueOf(mcd.maxGridCols));
      textPanel.add(textField4);
      
      useMolDyn_NAME_GRC_specCB.setLabel("Use Mol.Dyn. 'NAME-GRC else above explict (Grid,Row,Col)");
      useMolDyn_NAME_GRC_specCB.addItemListener(this);
      useMolDyn_NAME_GRC_specCB.setBackground(Color.white);
      useMolDyn_NAME_GRC_specCB.setState(mcd.useMolDyn_NAME_GRC_specFlag);
      textPanel.add(useMolDyn_NAME_GRC_specCB);
      
      specifyGeometryByNbrSpotsCB.setLabel("Use # spots (BELOW), else grid-geometry (ABOVE)");
      specifyGeometryByNbrSpotsCB.addItemListener(this);
      specifyGeometryByNbrSpotsCB.setBackground(Color.white);
      specifyGeometryByNbrSpotsCB.setState(mcd.specifyGeometryByNbrSpotsFlag);
      textPanel.add(specifyGeometryByNbrSpotsCB);
      
      textField7.setText(String.valueOf(mcd.maxRowsExpected));
      textPanel.add(textField7);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* GeometricPanel_PP */
  
  
  /**
   * InputFileRowsPanel_PP() - popup panel handler for input file rows
   * data. [NOTE] If the states are changed, vars in MaeConfigData()
   * must be changed as well (all variables chked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see CvtGUI#reanalyzeSourceFileList
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel InputFileRowsPanel_PP(int panelCode)
  { /* InputFileRowsPanel_PP */
    panelTitle= " ["+curPanel+
                "] Input file starting rows data";
    int
      rowWithSamples= 0,
      rowWithFields= 0,
      rowWithData= 0,
      rowWithSepGIPOFields= 0,
      rowWithSepGIPOData= 0;

    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.rowWithSamples= origMcd.rowWithSamples;
      mcd.rowWithFields= origMcd.rowWithFields;
      mcd.rowWithData= origMcd.rowWithData;
      mcd.rowWithSepGIPOFields= origMcd.rowWithSepGIPOFields;
      mcd.rowWithSepGIPOData= origMcd.rowWithSepGIPOData;
      mcd.commentToken= origMcd.commentToken;
      mcd.initialKeyword= origMcd.initialKeyword;
      mcd.hasMultDatasetsFlag= origMcd.hasMultDatasetsFlag;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      rowWithSamples= util.cvs2i(textField1.getText(),0);
      rowWithFields= util.cvs2i(textField2.getText(),0);
      rowWithData= util.cvs2i(textField3.getText(),0);
      rowWithSepGIPOFields= util.cvs2i(textField4.getText(),0);
      rowWithSepGIPOData= util.cvs2i(textField5.getText(),0);
      String
        commentToken= textField6.getText(),
        initialKeyword= textField7.getText();
      
      /* [TODO] Verify reasonable data - ... */
      if(rowWithSamples<0)
        rowWithSamples= 0;            /* map <0 to 0 */
      if(rowWithFields<0)
        rowWithFields= 0;             /* map <0 to 0 */
      if(rowWithData<0)
        rowWithData= 0;               /* map <0 to 0 */
      if(rowWithSepGIPOFields<0)
        rowWithSepGIPOFields= 0;      /* map <0 to 0 */
      if(rowWithSepGIPOData<0)
        rowWithSepGIPOData= 0;        /* map <0 to 0 */
      
      if(rowWithFields!=mcd.rowWithFields)
      { /* ok - save it */
        mcd.rowWithFields= rowWithFields;
        madeChangesFlag= true;
      } /* ok - save it */
      
      if(rowWithData!=mcd.rowWithData)
      { /* ok - save it */
        mcd.rowWithData= rowWithData;
        madeChangesFlag= true;
      } /* ok - save it */
      
      if(rowWithSepGIPOFields!=mcd.rowWithSepGIPOFields)
      { /* ok - save it */
        mcd.rowWithSepGIPOFields= rowWithSepGIPOFields;
        madeChangesFlag= true;
      } /* ok - save it */
      
      if(rowWithSepGIPOData!=mcd.rowWithSepGIPOData)
      { /* ok - save it */
        mcd.rowWithSepGIPOData= rowWithSepGIPOData;
        madeChangesFlag= true;
      } /* ok - save it */
      
      if(mcd.commentToken!=commentToken)
      { /* ok - save it */
        mcd.commentToken= commentToken;
        madeChangesFlag= true;
      } /* ok - save it */
      
      if(mcd.initialKeyword!=initialKeyword)
      { /* ok - save it */
        mcd.initialKeyword= initialKeyword;
        madeChangesFlag= true;
      } /* ok - save it */
      
      if(rowWithSamples!=mcd.rowWithSamples)
      { /* ok - save it */
        mcd.rowWithSamples= rowWithSamples;
        if(rowWithSamples>0)
        { /* note: switch 1 sample/file to mult. samples/file */
          /* We must reset selected samples and reread file again! */
          mcd.hasMultDatasetsFlag= true;
        }
        else
        { /* note: switch TO 1 sample/file */
          /* [CHECK] GREG - setting to false, is the ok?? */
          mcd.hasMultDatasetsFlag= false;
        }
        
        /* If true in step 2.,  do NOT allow step 3. */
        mcd.needToReAnalyzeFilesFlag= true;
        gui.reanalyzeSourceFileList(true);
        madeChangesFlag= true;
      } /* ok - save it */
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("(Optional) Row containing a list sample names");
      String
        sampleRowMsg= "Number of the row containing the names of the multiple samples"+
                      "\nif the file contains multiple samples. [Row #s start at row 1.]"+
                      "\nIf there are no sample names, set it to 0 or leave blank. If you change"+
                      "\nit from 0 to any positive row number, it removes ll input files and"+
                      "\nre-reads the first file to get the proper data field names so you may use"+
                      "\nit with the Assign GIPO Fields and Assign Quant Field assignment operations.\n";
      if(mcd.rowWithSamples>0)
      { /* only add "Sample[i] = <value>" if exists */
        String 
          sFile= (mcd.quantSrcDir[0] + mcd.quantSrcFile[0]),
          sampleRowData= sul.getPrintStringFieldRow("", sFile, mcd.rowWithSamples,
                                                    "Current Sample column");
        sampleRowMsg += sampleRowData;
      }
      if(!mcd.hasMultDatasetsFlag)
        sampleRowMsg= "Not used with your data.\n"+sampleRowMsg;
      addMouseover(1,sampleRowMsg);
      
      label2.setText("Row containing a list of quantitative file Field names");
      String
        fieldNameRowMsg= "Number of row that contains the names of the data file Field names."+
                         "\nEg. grid, row, column, GeneBank ID, GeneName, Clone ID, etc."+
                         "\n[Row #s start at row 1.]\n";
      if(mcd.rowWithFields>0)
      {
        String
          sFile= (mcd.quantSrcDir[0] + mcd.quantSrcFile[0]),
          fieldsRowData= sul.getPrintStringFieldRow("", sFile, mcd.rowWithFields,
                                                    "Current Field name column");
        fieldNameRowMsg += fieldsRowData;
      }
      addMouseover(2,fieldNameRowMsg);
      
      label3.setText("First row containing quantitative file Data");
      String
        dataRowMsg= "Number of first row that contains quantitative array Data in the file."+
                    "\nIt is assumed that this is followed by the rest of the array data."+
                    "\n[Row #s start atrow 1.]\n";
      if(mcd.rowWithData>0)
      {
        String 
          sFile= (mcd.quantSrcDir[0] + mcd.quantSrcFile[0]),          
          dataRowData= sul.getPrintStringFieldRow("", sFile, mcd.rowWithData,
                                                  "Current 1st Data column");
        dataRowMsg += dataRowData;
      }
      addMouseover(3, dataRowMsg);
      
      label4.setText("Row containing opt. separate GIPO file Field names");
      String
        gipoFieldNameRowMsg= "Number of row that contains the names of optional GIPO file Field names"+
                             "\nin the file. Eg. grid, row, column, GeneBank ID, GeneName, Clone ID, etc."+
                             "\n[Row #s start at row 1.]\n";
      if(mcd.rowWithSepGIPOFields>0)
      {
        String gipoFieldsRowData= sul.getPrintStringFieldRow(null,
                                                             mcd.separateGIPOinputFile,
                                                             mcd.rowWithSepGIPOFields,
                                                             "Current GIPO Field name column");
        gipoFieldNameRowMsg += gipoFieldsRowData;
      }
      addMouseover(4,gipoFieldNameRowMsg);
      
      label5.setText("First row containing opt. separate GIPO file Data");
      String
        gipoDataRowMsg= "Number of first row that contains optional separate GIPO file array Data."+
                        "\nIt is assumed that this is followed by the rest of the array data."+
                        "\n[Row #s start at row 1.]\n";
      if(mcd.rowWithSepGIPOData>0)
      {
        String gipoDataRowData= sul.getPrintStringFieldRow(null,
                                                           mcd.separateGIPOinputFile,
                                                           mcd.rowWithSepGIPOData,
                                                           "Current 1st GIPO file Data column");
        gipoDataRowMsg += gipoDataRowData;
      }
      addMouseover(5, gipoDataRowMsg);
      
      label6.setText("(Optional) Comment token");
      addMouseover(6, "If a line starts with this token (eg. '#'), the line will be skipped."+
                   "\nLeave it blank, if there are no comment lines.");
      
      label7.setText("(Optional) Initial keyword for each data row");
      addMouseover(7, "If you specify this, it checks for it in each data row");      
      
      addLabelPanelsToMainPanel(); /* add all labelPanels' to mainPanel */
      
      textField1.setText(String.valueOf(mcd.rowWithSamples));
      textPanel.add(textField1);
      
      textField2.setText(String.valueOf(mcd.rowWithFields));
      textPanel.add(textField2);
      
      textField3.setText(String.valueOf(mcd.rowWithData));
      textPanel.add(textField3);
      
      textField4.setText(String.valueOf(mcd.rowWithSepGIPOFields));
      textPanel.add(textField4);
      
      textField5.setText(String.valueOf(mcd.rowWithSepGIPOData));
      textPanel.add(textField5);
      
      textField6.setText(mcd.commentToken);
      textPanel.add(textField6);
      
      textField7.setText(mcd.initialKeyword);
      textPanel.add(textField7);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* InputFileRowsPanel_PP */  
  
  
  /**
   * fluoresentPanel_PP() - popup panel handler for fluorescent dye names
   * e.g. Cy3 & Cy5.
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables checked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel fluoresentPanel_PP(int panelCode)
  { /* fluoresentPanel_PP */
    panelTitle= " ["+curPanel+
                "] Ratio fluorescence data";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.useRatioDataFlag= origMcd.useRatioDataFlag;
      mcd.useCy5OverCy3Flag= origMcd.useCy5OverCy3Flag;
      mcd.hasBkgrdDataFlag= origMcd.hasBkgrdDataFlag;
      mcd.fluorescentLbl1= origMcd.fluorescentLbl1;
      mcd.fluorescentLbl2= origMcd.fluorescentLbl2;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      boolean
        useRatioDataFlag= useRatioDataCB.getState(),
        useCy5OverCy3Flag= useCy3Cy5RatioDataCB.getState(),
        hasBkgrdDataFlag= hasBkgrdDataCB.getState();
      String
        fluorescentLbl1= textField3.getText(),
        fluorescentLbl2= textField4.getText();
      
      /* [TODO] Verify reasonable data - ... */
      if(useRatioDataFlag!=mcd.useRatioDataFlag ||
         useCy5OverCy3Flag!=mcd.useCy5OverCy3Flag)
      { /* ok - save it */
        mcd.useRatioDataFlag= useRatioDataFlag;
        mcd.useCy5OverCy3Flag= useCy5OverCy3Flag;
        mcd.ratioMedianCorrectionFlag= useRatioDataFlag;  /* force flag */
        madeChangesFlag= true;
      } /* ok - save it */
      
      if(!fluorescentLbl1.equals(mcd.fluorescentLbl1) ||
         !fluorescentLbl2.equals(mcd.fluorescentLbl2) )
      { /* ok - save it */
        mcd.fluorescentLbl1= fluorescentLbl1;
        mcd.fluorescentLbl2= fluorescentLbl2;
        madeChangesFlag= true;
      } /* ok - save it */
      
      if(hasBkgrdDataFlag!=mcd.hasBkgrdDataFlag)
      { /* ok - save it */
        mcd.hasBkgrdDataFlag= hasBkgrdDataFlag;
        madeChangesFlag= true;
      } /* ok - save it */
      
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("Ratio (i.e. Cy3,Cy5) or Intensity Data");
      addMouseover(1, "Data for MAExplorer is either ratio data such as"+
                   "\nCy3/Cy5, or intensity data such as P33, etc.");
      
      label2.setText("If Ratio data, use (Cy5/Cy3) else (Cy3/Cy5)");
      addMouseover(2, "Ratio data may be presented as either (Cy5/Cy3)or (Cy3/Cy5)");
      
      label3.setText("Fluorescent dye for intensity 1 (if ratio data)"); /* Cy3 */
      addMouseover(3, "The dye to associate with quantified data intensity 1.");
      
      label4.setText("Fluorescent dye for intensity 2 (if ratio data)"); /* Cy5 */
      addMouseover(4, "The dye to associate with quantified data intensity 2.");
      
      label5.setText("Have background intensity data");
      addMouseover(5, "The input data file includes background intensity data that you "+
                   "\nwant to include. You do NOT have to include that data.");
      
      addLabelPanelsToMainPanel(); /* add all labelPanels' to mainPanel */
      
      useRatioDataCB.setLabel("Use Ratio else Intensity data");
      useRatioDataCB.addItemListener(this);
      useRatioDataCB.setBackground(Color.white);
      useRatioDataCB.setState(mcd.useRatioDataFlag);
      textPanel.add(useRatioDataCB);
      
      useCy3Cy5RatioDataCB.setLabel("Use (Cy5/Cy3) else (Cy3/Cy5)");
      useCy3Cy5RatioDataCB.addItemListener(this);
      useCy3Cy5RatioDataCB.setBackground(Color.white);
      useCy3Cy5RatioDataCB.setState(mcd.useCy5OverCy3Flag);
      textPanel.add(useCy3Cy5RatioDataCB);
      
      textField3.setText(mcd.fluorescentLbl1); /* Cy 3 */
      textPanel.add(textField3);
      
      textField4.setText(mcd.fluorescentLbl2); /* Cy 5 */
      textPanel.add(textField4);
      
      hasBkgrdDataCB.setLabel("Has background data");
      hasBkgrdDataCB.addItemListener(this);
      hasBkgrdDataCB.setBackground(Color.white);
      hasBkgrdDataCB.setState(mcd.hasBkgrdDataFlag);
      textPanel.add(hasBkgrdDataCB);
      
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* fluoresentPanel_PP */
  
  
  /**
   * XYcoordsPanel_PP() - popup panel handler for spot coordinate options
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables checked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel XYcoordsPanel_PP(int panelCode)
  { /* XYcoordsPanel_PP */
    panelTitle= " ["+curPanel+
                "] (Opt.) Microarray (X,Y) coordinate options";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.usePseudoXYcoordsFlag= origMcd.usePseudoXYcoordsFlag;
      mcd.hasQuantXYcoordsFlag= origMcd.hasQuantXYcoordsFlag;
      mcd.reuseXYcoordsFlag= origMcd.reuseXYcoordsFlag;
      mcd.swapRowsColsFlag= origMcd.swapRowsColsFlag;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      boolean
        usePseudoXYcoordsFlag= usePseudoXYcoordsCB.getState(),
        hasQuantXYcoordsFlag=  hasQuantXYcoordsCB.getState(),
        reuseXYcoordsFlag= reuseXYcoordsCB.getState(),
        swapRowsColsFlag= swapRowsColsCB.getState();
      
      /* [TODO] Verify reasonable data - ... */
      if(usePseudoXYcoordsFlag!=mcd.usePseudoXYcoordsFlag ||
         hasQuantXYcoordsFlag!=mcd.hasQuantXYcoordsFlag ||
         reuseXYcoordsFlag!=mcd.reuseXYcoordsFlag ||
         swapRowsColsFlag!=mcd.swapRowsColsFlag)
      { /* ok - save it */
        mcd.usePseudoXYcoordsFlag= usePseudoXYcoordsFlag;
        mcd.reuseXYcoordsFlag= reuseXYcoordsFlag;
        if(mcd.usePseudoXYcoordsFlag)
          mcd.reuseXYcoordsFlag= true;
        mcd.hasQuantXYcoordsFlag= hasQuantXYcoordsFlag;
        mcd.swapRowsColsFlag= swapRowsColsFlag;
        
        madeChangesFlag= true;
      } /* ok - save it */
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("Use microarray pseudo (X,Y) coordinates");
      addMouseover(1, "Generate a microarray pseudo image using a representation of"+
                   "\nthe array based on Grids, Grid Rows, and Grid Columns."+
                   "\nOtherwise, use the (X,Y) data supplied for each spot -"+
                   "\nif it exists. If this option is set, it will overide the actual"+
                   "\n(X,Y) coordinates if that option is selected as well.");
      
      label2.setText("Use actual microarray pseudo (X,Y) coordinates");
      addMouseover(2, "The actual (X,Y) coordinate data exists for each spot."+
                   "\nIf the data exists but you do NOT select this option,"+
                   "\nit will use the pseudo-array option.");
      
      label3.setText("Reuse (X,Y) coordinates of first sample for all samples");
      addMouseover(3, "Reuse (X,Y) coordinates of first sample for all samples. This"+
                   "\nis used if you want to 'Flicker' array pseudo images between"+
                   "\ntwo samples.");
      
      label4.setText("Swap microarray rows and columns");
      addMouseover(4, "Reverse rows and columns in the microarray pseudo image.");
      
      
      addLabelPanelsToMainPanel();   /* add all labelPanels' to mainPanel */
      
      usePseudoXYcoordsCB.setLabel("Generate array pseudo X Y coordinates");
      usePseudoXYcoordsCB.addItemListener(this);
      usePseudoXYcoordsCB.setBackground(Color.white);
      usePseudoXYcoordsCB.setState(mcd.usePseudoXYcoordsFlag);
      textPanel.add(usePseudoXYcoordsCB);
      
      hasQuantXYcoordsCB.setLabel("Have actual X Y coordinates for each sample");
      hasQuantXYcoordsCB.addItemListener(this);
      hasQuantXYcoordsCB.setBackground(Color.white);
      hasQuantXYcoordsCB.setState(mcd.hasQuantXYcoordsFlag);
      textPanel.add(hasQuantXYcoordsCB);
      
      reuseXYcoordsCB.setLabel("Reuse array X Y coords for all arrays");
      reuseXYcoordsCB.addItemListener(this);
      reuseXYcoordsCB.setBackground(Color.white);
      reuseXYcoordsCB.setState(mcd.reuseXYcoordsFlag);
      textPanel.add(reuseXYcoordsCB);
      
      swapRowsColsCB.setLabel("Swap array rows and columns");
      swapRowsColsCB.addItemListener(this);
      swapRowsColsCB.setBackground(Color.white);
      swapRowsColsCB.setState(mcd.swapRowsColsFlag);
      textPanel.add(swapRowsColsCB);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* XYcoordsPanel_PP */
  
  
  /**
   * GenomicIDsPanel_PP() - popup panel handler for Genomic IDs options.
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables checked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel GenomicIDsPanel_PP(int panelCode)
  { /* GenomicIDsPanel_PP */
    panelTitle= " ["+curPanel+
                "] (Opt.) Genomic Identifier options";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.hasLocationIdFlag= origMcd.hasLocationIdFlag;
      mcd.hasCloneIDsFlag= origMcd.hasCloneIDsFlag;
      mcd.hasGenBankIDsFlag= origMcd.hasGenBankIDsFlag;
      mcd.hasUniGeneIDsFlag= origMcd.hasUniGeneIDsFlag;
      mcd.hasDB_ESTIDsFlag= origMcd.hasDB_ESTIDsFlag;
      mcd.hasLocusIDsFlag= origMcd.hasLocusIDsFlag;
      mcd.hasSwissProtIDsFlag= origMcd.hasSwissProtIDsFlag;
      mcd.hasPlateDataFlag= origMcd.hasPlateDataFlag;
      mcd.geoPlatformID= origMcd.geoPlatformID;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)      
    { /* save data from panel back into the state */
      String  tmpStr= textField10.getText();
      
      if(tmpStr.length()>0)
      { /* Bad Geo Platform ID value */
        boolean
          badDigitsFlag= true,
          badPrefixFlag= tmpStr.startsWith("GPL");
        
        for(int i=3; i<tmpStr.length(); i++)
          if(!Character.isDigit(tmpStr.charAt(i)))
             badDigitsFlag= false;
        
        if(badPrefixFlag == false || badDigitsFlag == false)
        {
          /* popup warning window */
          String  msg= "Illegal GEO Platform ID name ["+ tmpStr+"]. "+
                       "Go back to the Genomic Identifer options panel to correct this.";
          DialogBox  db= new DialogBox(this.gui, msg);
        }
        
      } /* Bad Geo Platform ID value */
      boolean
        hasLocationIdFlag= hasLocationIdCB.getState(),
        hasCloneIDsFlag= hasCloneIDsCB.getState(),
        hasGenBankIDsFlag= hasGenBankIDsCB.getState(),
        hasUniGeneIDsFlag= hasUniGeneIDsCB.getState(),
        hasDB_ESTIDsFlag= hasDB_ESTIDsCB.getState(),
        hasLocusIDsFlag= hasLocusIDsCB.getState(),
        hasSwissProtIDsFlag= hasSwissProtIDsCB.getState(),
        hasPlateDataFlag= hasPlateDataCB.getState(),
        hasGenomicIDsfromDescrFlag= hasGenomicIDsfromDescrCB.getState();
      geoPlatformID= textField10.getText();
      
      /* Save all if any changes in any of them. */
      if(hasLocationIdFlag!= mcd.hasLocationIdFlag ||
         hasCloneIDsFlag!= mcd.hasCloneIDsFlag ||
         hasGenBankIDsFlag!= mcd.hasGenBankIDsFlag ||
         hasUniGeneIDsFlag!= mcd.hasUniGeneIDsFlag ||
         hasDB_ESTIDsFlag!= mcd.hasDB_ESTIDsFlag ||
         hasLocusIDsFlag!= mcd.hasLocusIDsFlag ||
         hasSwissProtIDsFlag!= mcd.hasSwissProtIDsFlag ||
         hasPlateDataFlag!= mcd.hasPlateDataFlag ||
         hasGenomicIDsfromDescrFlag!= mcd.chkAndEditFieldNamesFlag ||
         !geoPlatformID.equals(mcd.geoPlatformID))
      { /* ok - save it */
        if(hasGenomicIDsfromDescrFlag)
        {
          hasCloneIDsFlag= false;
          hasGenBankIDsFlag= false;
          hasUniGeneIDsFlag= false;
        }
        mcd.hasLocationIdFlag= hasLocationIdFlag;
        mcd.hasCloneIDsFlag= hasCloneIDsFlag;
        mcd.hasGenBankIDsFlag= hasGenBankIDsFlag;
        mcd.hasUniGeneIDsFlag= hasUniGeneIDsFlag;
        mcd.hasDB_ESTIDsFlag= hasDB_ESTIDsFlag;
        mcd.hasLocusIDsFlag= hasLocusIDsFlag;
        mcd.hasSwissProtIDsFlag= hasSwissProtIDsFlag;
        mcd.hasPlateDataFlag= hasPlateDataFlag;
        mcd.chkAndEditFieldNamesFlag= hasGenomicIDsfromDescrFlag;
        mcd.geoPlatformID= geoPlatformID;
        madeChangesFlag= true;
      } /* ok - save it */
      
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("Has Location data");
      addMouseover(1, "The user data file has Location identifier data."+
                   "\nThese could be 'probe_set' for Affymetrix, 'Incyte ID'"+
                   "\nfor Incyte, etc. and are used as the gene identifier"+
                   "\nif there are no other IDs.");
      
      label2.setText("Has Clone ID data");
      addMouseover(2, "The user data file has I.M.A.G.E 'Clone ID' data.");
      
      label3.setText("Has GenBank data");
      addMouseover(3, "The user data file has 'GenBank' identifier data."+
                   "\nSee http://ncbi.nlm.nih.gov/ for more information.");
      
      label4.setText("Has UniGene ID data");
      addMouseover(4, "The user data file has 'UniGene' identifier data.");
      
      label5.setText("Has dbEST data");
      addMouseover(5, "The user data file has 'dbEST' identifier data.");
      
      label6.setText("Has LocusLink data");
      addMouseover(6, "The user data file has 'LocusID' identifier data.");
      
      label7.setText("Has SwissProt data");
      addMouseover(7, "The user data file has 'SwissProt' identifier data."+
                  "\nSee http://www.expasy.ch/ for more information.");
      
      label8.setText("Has Plate data");
      addMouseover(8, "The user data file has user Plate well identifier data."+
                   "\nThis uniquely identifies the source of the spotted clone.");
      
      label9.setText("Get Genomic IDs from 'Description'");
      addMouseover(9, "The Genomic IDs are encoded in the 'Description' field of"+
                   "\nuser input file (the Affymetrix encodeing of ids)."+
                   "\nIt will find and generate genomic IDs for:"+
                   "\n  Clone_ID if /cl=XXXX is in the Description,"+
                   "\n  GenBank  if /gb=XXXX is in the Description,"+
                   "\n  UniGene  if /ug=XXXX is in the Description."+
                   "\nIf this switch is enabled, then the explicit ID options"+
                   "\nare disabled."
                   );
      label10.setText("Use Geo Platform IDs");
      addMouseover(10, "\nGEO is the NCBI Gene Expression Omnibus.It is a gene expression "+
                  "\nand hybridization array data repository."+
                  "\nAn example of an ID is:'GPL80'."+
                  "\nSee: 'http://www.ncbi.nlm.nih.gov/geo/' for more info");
      
      addLabelPanelsToMainPanel(); /* add all labelPanels' to mainPanel */
      
      hasLocationIdCB.setLabel("User data file has Location data");
      hasLocationIdCB.addItemListener(this);
      hasLocationIdCB.setBackground(Color.white);
      hasLocationIdCB.setState(mcd.hasLocationIdFlag);
      textPanel.add(hasLocationIdCB);
      
      hasCloneIDsCB.setLabel("User data file has Clone ID data");
      hasCloneIDsCB.addItemListener(this);
      hasCloneIDsCB.setBackground(Color.white);
      hasCloneIDsCB.setState(mcd.hasCloneIDsFlag);
      textPanel.add(hasCloneIDsCB);
      
      hasGenBankIDsCB.setLabel("User data file has GeneBank data");
      hasGenBankIDsCB.addItemListener(this);
      hasGenBankIDsCB.setBackground(Color.white);
      hasGenBankIDsCB.setState(mcd.hasGenBankIDsFlag);
      textPanel.add(hasGenBankIDsCB);
      
      hasUniGeneIDsCB.setLabel("User data file has UniGene data");
      hasUniGeneIDsCB.addItemListener(this);
      hasUniGeneIDsCB.setBackground(Color.white);
      hasUniGeneIDsCB.setState(mcd.hasUniGeneIDsFlag);
      textPanel.add(hasUniGeneIDsCB);
      
      hasDB_ESTIDsCB.setLabel("User data file has dbEST data");
      hasDB_ESTIDsCB.addItemListener(this);
      hasDB_ESTIDsCB.setBackground(Color.white);
      hasDB_ESTIDsCB.setState(mcd.hasDB_ESTIDsFlag);
      textPanel.add(hasDB_ESTIDsCB);
      
      hasLocusIDsCB.setLabel("User data file has LocusLink data");
      hasLocusIDsCB.addItemListener(this);
      hasLocusIDsCB.setBackground(Color.white);
      hasLocusIDsCB.setState(mcd.hasLocusIDsFlag);
      textPanel.add(hasLocusIDsCB);
      
      hasSwissProtIDsCB.setLabel("User data file has SwissProt data");
      hasSwissProtIDsCB.addItemListener(this);
      hasSwissProtIDsCB.setBackground(Color.white);
      hasSwissProtIDsCB.setState(mcd.hasSwissProtIDsFlag);
      textPanel.add(hasSwissProtIDsCB);
      
      hasPlateDataCB.setLabel("User data file has Plate data");
      hasPlateDataCB.addItemListener(this);
      hasPlateDataCB.setBackground(Color.white);
      hasPlateDataCB.setState(mcd.hasPlateDataFlag);
      textPanel.add(hasPlateDataCB);
      
      hasGenomicIDsfromDescrCB.setLabel("Get Genomic IDs from 'Description'");
      hasGenomicIDsfromDescrCB.addItemListener(this);
      hasGenomicIDsfromDescrCB.setBackground(Color.white);
      hasGenomicIDsfromDescrCB.setState(mcd.chkAndEditFieldNamesFlag);
      textPanel.add(hasGenomicIDsfromDescrCB);
      
      textField10.setText(mcd.geoPlatformID);
      textField10.addTextListener(this);
      textField10.setBackground(Color.white);
      textPanel.add(textField10);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* GenomicIDsPanel_PP */
  
  
  /**
   * GeneNamesPanel_PP() - popup panel handler for GeneNames IDs options.
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables checked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel GeneNamesPanel_PP(int panelCode)
  { /* GeneNamesPanel_PP */
    panelTitle= " ["+curPanel+
                "] (Opt.) Gene names (or description) options";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.hasGeneClassDataFlag= origMcd.hasGeneClassDataFlag;
      mcd.hasUniGeneNameFlag= origMcd.hasUniGeneNameFlag;
      mcd.hasQualCheckQuantDataFlag= origMcd.hasQualCheckQuantDataFlag;
      mcd.hasQualCheckGIPOdataFlag= origMcd.hasQualCheckGIPOdataFlag;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      boolean
        hasGeneClassDataFlag= hasGeneClassDataCB.getState(),
        hasUniGeneNameFlag= hasUniGeneNameCB.getState(),
        hasQualCheckQuantDataFlag= hasQualCheckQuantDataCB.getState(),
        hasQualCheckGIPOdataFlag= hasQualCheckGIPOdataCB.getState();
      
      /* [TODO] Verify reasonable data - ... */
      if(hasGeneClassDataFlag!= mcd.hasGeneClassDataFlag ||
         hasUniGeneNameFlag!= mcd.hasUniGeneNameFlag ||
         hasQualCheckQuantDataFlag!= mcd.hasQualCheckQuantDataFlag ||
         hasQualCheckGIPOdataFlag!= mcd.hasQualCheckGIPOdataFlag)
      { /* ok - save it */
        mcd.hasGeneClassDataFlag= hasGeneClassDataFlag;
        mcd.hasUniGeneNameFlag= hasUniGeneNameFlag;
        mcd.hasQualCheckQuantDataFlag= hasQualCheckQuantDataFlag;
        mcd.hasQualCheckGIPOdataFlag= hasQualCheckGIPOdataFlag;
        madeChangesFlag= true;
      } /* ok - save it */
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("Has Gene Class user data");
      addMouseover(1, "The user data file has Gene Class ontology data for"+
                   "\neach gene. [FUTURE]");
      
      label2.setText("Has UniGene Name user data");
      addMouseover(2, "The user data file has UniGene Name data. This could"+
                   "\nbe used if the default 'GeneName' description is not"+
                   "\navailable.");
      
      label3.setText("Has separate per-spot QualCheck user data per-sample");
      addMouseover(3, "The user data file has 'Quant' QualCheck data. This data"+
                   "\nis on a per-spot basis for each array hybridization."+
                   "\nThe code (see MAExplorer Reference Manual Appendix C Table C.4.2)"+
                   "\nmay be used to flag bad spots or missing spot data.");

      label4.setText("Has 'GIPO' QualCheck user data for entire DB");
      addMouseover(4, "The user data file has GIPO QualCheck data. This data"+
                   "\nis on a GIPO basis for the entire database."+
                   "\nThe code (see MAExplorer Reference Manual Appendix C)"+
                   "\nmay be used to flag bad gene data.");      
      
      addLabelPanelsToMainPanel();    /* add all labelPanels' to mainPanel */
      
      hasGeneClassDataCB.setLabel("User data file has Gene Class data");
      hasGeneClassDataCB.addItemListener(this);
      hasGeneClassDataCB.setBackground(Color.white);
      hasGeneClassDataCB.setState(mcd.hasGeneClassDataFlag);
      textPanel.add(hasGeneClassDataCB);
      
      hasUniGeneNameCB.setLabel("User data file has UniGene Name data");
      hasUniGeneNameCB.addItemListener(this);
      hasUniGeneNameCB.setBackground(Color.white);
      hasUniGeneNameCB.setState(mcd.hasUniGeneNameFlag);
      textPanel.add(hasUniGeneNameCB);
      
      hasQualCheckQuantDataCB.setLabel("User data has separate per-spot QualCheck data");
      hasQualCheckQuantDataCB.addItemListener(this);
      hasQualCheckQuantDataCB.setBackground(Color.white);
      hasQualCheckQuantDataCB.setState(mcd.hasQualCheckQuantDataFlag);
      textPanel.add(hasQualCheckQuantDataCB);
      
      hasQualCheckGIPOdataCB.setLabel("User data file has 'GIPO' QualCheck data");
      hasQualCheckGIPOdataCB.addItemListener(this);
      hasQualCheckGIPOdataCB.setBackground(Color.white);
      hasQualCheckGIPOdataCB.setState(mcd.hasQualCheckGIPOdataFlag);
      textPanel.add(hasQualCheckGIPOdataCB);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* GeneNamesPanel_PP */
  
  
  /**
   * DNAcalibPanel_PP() - popup panel handler for calibDNA & name of
   * researcher's clones.
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables checked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel DNAcalibPanel_PP(int panelCode)
  { /* DNAcalibPanel_PP */
    panelTitle= " ["+curPanel+
                "] (Opt.) DNA Calibration and user plate names, UniGene species name";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.calibDNAname= origMcd.calibDNAname;
      mcd.yourPlateName= origMcd.yourPlateName;
      mcd.emptyWellName= origMcd.emptyWellName;
      mcd.UniGeneSpeciesPrefix= origMcd.UniGeneSpeciesPrefix;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      String
        calibDNAname= textField1.getText(),
        yourPlateName= textField2.getText(),
        emptyWellName= textField3.getText(),
        species= textField4.getText(),
        textUniGeneSpeciesPrefix= textField5.getText(),  /* special */
        chooserUniGeneSpeciesPrefix= speciesChoice.getSelectedItem(),
        UniGeneSpeciesPrefix= chooserUniGeneSpeciesPrefix;
      
      if(!textUniGeneSpeciesPrefix.startsWith("?") &&
         chooserUniGeneSpeciesPrefix.startsWith("?"))
        UniGeneSpeciesPrefix= textUniGeneSpeciesPrefix; /* explicit defn */
      
      /* [TODO] Verify reasonable data - ... */
      if(! calibDNAname.equals(mcd.calibDNAname))
      { /* ok - save it */
        mcd.calibDNAname= calibDNAname;
        madeChangesFlag= true;
      }
      
      if(! yourPlateName.equals(mcd.yourPlateName))
      { /* ok - save it */
        mcd.yourPlateName= yourPlateName;
        madeChangesFlag= true;
      }
      
      if(! species.equals(mcd.species))
      { /* ok - save it */
        mcd.species= species;
        madeChangesFlag= true;
      }
      
      if(! emptyWellName.equals(mcd.emptyWellName))
      { /* ok - save it */
        mcd.emptyWellName= emptyWellName;
        madeChangesFlag= true;
      }
      
      if(! UniGeneSpeciesPrefix.equals(mcd.UniGeneSpeciesPrefix))
      { /* ok - save it */
        mcd.UniGeneSpeciesPrefix= UniGeneSpeciesPrefix;
        for(int k=0; k<mcd.nSpeciesList;k++)
          if(UniGeneSpeciesPrefix.equals(mcd.speciesList[k]) &&
             !mcd.speciesNames[k].equals("?"))
          {
            mcd.species= mcd.speciesNames[k];
            break;
          }
        madeChangesFlag= true;
      }  /* ok - save it */
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      /* calibDNAname */
      label1.setText("Name of calibration DNA (if in database)");
      addMouseover(1, "This is the default name given to calibration DNA spotted"+
                   "\non the array for calibration purposes and indicated in"+
                   "\nthe GIPO file 'Clone ID' or 'GenBank ID' field.");
      
      /* yourPlateName */
      label2.setText("Name of researcher's special clones (if in database)");
      addMouseover(2, "This is the default name given in place of a I.M.A.G.E."+
                   "\nClone ID when the researcher's clones have not yet been"+
                   "\nplaced im the I.M.A.G.E. respository and thus have no ID."+
                   "\nIt indicated in the GIPO file 'Clone ID' or 'GenBank ID' "+
                   "\nfield.");

      /* empty wells */
      label3.setText("Name of empty wells");
      addMouseover(3,"If you have empty or blank rows of spot data, type in the name "+
                  "(i.e. 'empty') if any.");
      
      /* species */
      label4.setText("Name species (opt)");
      addMouseover(4,"Species name (Mouse, Human, ...). "+
                   "It is used to document the Array Layout");
      
      /* species */
      label5.setText("Name UniGene Species prefix (opt)");
      addMouseover(5, "UniGene species prefix (Mouse Mm, Human Hs, etc.). This is"+
                   "\nused in querying Genomic Web databases. If you do not see"+
                   "\nthe prefix you want in the choice menu, type it in.");
      
      addLabelPanelsToMainPanel(); /* add all labelPanels' to mainPanel */
      
      textField1.setText(mcd.calibDNAname);
      textPanel.add(textField1);
      
      textField2.setText(mcd.yourPlateName);
      textPanel.add(textField2);
      
      textField3.setText(mcd.emptyWellName);
      textPanel.add(textField3);
      
      textField4.setText(mcd.species);
      textPanel.add(textField4);
      
      /* special (textField, label, choice) for Species */
      Panel
      speciesPanel= new Panel();
      textPanel.add(speciesPanel);
      
      textField5.setText(mcd.UniGeneSpeciesPrefix);
      speciesPanel.add(textField5);
      
      Label
      speciesMsgLabel= new Label("or select from");
      speciesPanel.add(speciesMsgLabel);
      
      speciesChoice= new Choice();
      int lastSelected= 0;  /* default to [0] or "Hs" */
      
      for(int i=0; i<mcd.nSpeciesList;i++)
      {
        speciesChoice.add(mcd.speciesList[i]);
        if(mcd.UniGeneSpeciesPrefix.equals(mcd.speciesList[i]))
          lastSelected= i;
      }
      speciesChoice.addItemListener(this);
      speciesChoice.select(mcd.speciesList[lastSelected]);
      speciesChoice.setBackground(gui.F_BG_COLOR);
      speciesPanel.add(speciesChoice);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* DNAcalibPanel_PP */
  
  
  /**
   * databasePanel_PP() - popup panel handler for names of DB and quant pgm.
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables checked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel databasePanel_PP(int panelCode)
  { /* databasePanel_PP */
    panelTitle= " ["+curPanel+
                "] (Opt.) Database and data quantification program";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.database= origMcd.database;
      mcd.dbSubset= origMcd.dbSubset;
      mcd.dbPrjName= origMcd.dbPrjName;
      mcd.maAnalysisProg= origMcd.maAnalysisProg;
      return(null);
    }
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      String
        database= textField1.getText(),
        dbSubset= textField2.getText(),
        dbPrjName= textField3.getText(),
        maAnalysisProg= textField4.getText();
      
      /*
      if(EMPF_DBUG)
        System.out.println("EMPF-dbPP.1 panelTitle["+curPanel+"]="+panelTitle+
                           " madeChangesFlag="+madeChangesFlag+
                           " database='"+database+
                           "' dbSubset='"+dbSubset+
                           "' dbPrjName='"+dbPrjName+
                           "' maAnalysisProg='"+maAnalysisProg+"'");
      */
      
      /* [TODO] Verify reasonable data - ... */
      if(!database.equals(mcd.database))
      { /* ok - save it */
        mcd.database= database;
        madeChangesFlag= true;
      }  /* ok - save it */
      
      if(!dbSubset.equals(mcd.dbSubset))
      { /* ok - save it */
        mcd.dbSubset= dbSubset;
        madeChangesFlag= true;
      }  /* ok - save it */
      
      if(!dbPrjName.equals(mcd.dbPrjName))
      { /* ok - save it */
        mcd.dbPrjName= dbPrjName;
        /* set all of the samples to mcd.dbPrjName	*/
        mcd.setAllSamplesToDbPrjName();
        madeChangesFlag= true;
      }  /* ok - save it */
      
      if(!maAnalysisProg.equals(mcd.maAnalysisProg))
      { /* ok - save it */
        mcd.maAnalysisProg= maAnalysisProg;
        madeChangesFlag= true;
      }  /* ok - save it */
      
      /*
      if(EMPF_DBUG)
        System.out.println("EMPF-dbPP.2 panelTitle["+curPanel+"]="+panelTitle+
                           " madeChangesFlag="+madeChangesFlag);
      */
      
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("Your name of the created database (opt)");
      addMouseover(1, "The name you want to give to the created database name.");
      
      label2.setText("Your name of the database subset (opt)");
      addMouseover(2, "Your name for the database subset used in the initial"+
                   "\n.mae startup file.");
      
      label3.setText("Generic project name for all samples (opt)");
      addMouseover(3, "Generic name of the project to be used for all samples in the database."+
                   "\nIf no name is specified, it uses the input data files folder.");
      
      label4.setText("Name of spot quantification program (opt)");
      addMouseover(4, "Name of the program used to quantitate the spot data"+
                   "\nfrom the sample images.");
      
      addLabelPanelsToMainPanel(); /* add all labelPanels' to mainPanel */
      
      textField1.setText(mcd.database);
      textPanel.add(textField1);
      
      textField2.setText(mcd.dbSubset);
      textPanel.add(textField2);
      
      textField3.setText(mcd.dbPrjName);
      textPanel.add(textField3);
      
      textField4.setText(mcd.maAnalysisProg);
      textPanel.add(textField4);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* databasePanel_PP */
  
  
  /**
   * classNamePanel_PP() - popup panel handler for sample (X,Y) class names.
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables checked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  Panel classNamePanel_PP(int panelCode)
  { /* classNamePanel_PP */
    panelTitle= " ["+curPanel+
                "] (Opt.) Hybridized sample (X,Y) 'set' class names";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.classNameX= origMcd.classNameX;
      mcd.classNameY= origMcd.classNameY;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      String
        classNameX= textField1.getText(),
        classNameY= textField2.getText();
      
      /* [TODO] Verify reasonable data - ... */
      if(!classNameX.equals(mcd.classNameX) ||
      !classNameY.equals(mcd.classNameY))
      { /* ok - save it */
        mcd.classNameX= classNameX;
        mcd.classNameY= classNameY;
        madeChangesFlag= true;
      }
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("Default name of X samples 'set'");
      addMouseover(1,"This is the name for the samples assign to the 'X set'.");
      
      label2.setText("Default name of Y samples 'set'");
      addMouseover(2,"This is the name for the samples assign to the 'Y set'.");
      
      addLabelPanelsToMainPanel(); /* add all labelPanels' to mainPanel */
      
      textField1.setText(mcd.classNameX);
      textPanel.add(textField1);
      
      textField2.setText(mcd.classNameY);
      textPanel.add(textField2);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* classNamePanel_PP */
  
  
  /**
   * thresholdsPanel_PP() - thresholds used in data Filtering.
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables checked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel thresholdsPanel_PP(int panelCode)
  { /* thresholdsPanel_PP */
    panelTitle= " ["+curPanel+
                "] (Opt.) Default data Filtering thresholds";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.clusterDistThr= origMcd.clusterDistThr;
      mcd.maxGenesToRpt= origMcd.maxGenesToRpt;
      mcd.nbrOfClustersThr= origMcd.nbrOfClustersThr;
      mcd.pValueThr= origMcd.pValueThr;
      mcd.spotCVthr= origMcd.spotCVthr;
      mcd.diffThr= origMcd.diffThr;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      float clusterDistThr= util.cvs2f(textField1.getText(),0.0F);
      int
        maxGenesToRpt= util.cvs2i(textField2.getText(),0),
        nbrOfClustersThr= util.cvs2i(textField3.getText(),0);
      float
        pValueThr= util.cvs2f(textField4.getText(),0.0F),
        spotCVthr= util.cvs2f(textField5.getText(),0.0F),
        diffThr= util.cvs2f(textField6.getText(),0.0F);
      
      /* [TODO] Verify reasonable data - ... */
      if(mcd.clusterDistThr!=clusterDistThr &&
         clusterDistThr>=0.0F && clusterDistThr<=1000.0F)
      { /* ok - save it */
        mcd.clusterDistThr= clusterDistThr;
        madeChangesFlag= true;
      }  /* ok - save it */
      
      if(mcd.maxGenesToRpt!=maxGenesToRpt && maxGenesToRpt>=1)
      { /* ok - save it */
        mcd.maxGenesToRpt= maxGenesToRpt;
        madeChangesFlag= true;
      }  /* ok - save it */
      
      if(mcd.nbrOfClustersThr!=nbrOfClustersThr &&
         nbrOfClustersThr>=1 && nbrOfClustersThr<=1000)
      { /* ok - save it */
        mcd.nbrOfClustersThr= nbrOfClustersThr;
        madeChangesFlag= true;
      }  /* ok - save it */
      
      if(mcd.pValueThr!=pValueThr && pValueThr>=0.0F && pValueThr<=1.0F)
      { /* ok - save it */
        mcd.pValueThr= pValueThr;
        madeChangesFlag= true;
      }  /* ok - save it */
      
      if(mcd.spotCVthr!=spotCVthr && spotCVthr>=0.0F && spotCVthr<=1.0F)
      { /* ok - save it */
        mcd.spotCVthr= spotCVthr;
        madeChangesFlag= true;
      }  /* ok - save it */
      
      if(mcd.diffThr!= diffThr && diffThr>=0.0F && diffThr<=4.0F)
      { /* ok - save it */
        mcd.diffThr= diffThr;
        madeChangesFlag= true;
      }  /* ok - save it */
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("Default cluster similarity threshold [0 : 1000]");
      addMouseover(1,"Default cluster similarity threshold used in some of the"+
                   "\nclustering methods. This is the initial value shown in"+
                   "\npopup sliders.");
      
      label2.setText("Default # genes in highest/lowest");
      addMouseover(2,"The number of genes reported in gene Reports or in the"+
                   "\ndata Filter when this restriction is invoked.");
      
      label3.setText("Default # clusters for K-means clustering [1 : 1000]");
      addMouseover(3,"Default # of clusters used in the K-means clustering method."+
                   "\nThis is the initial value shown in popup sliders.");
      
      label4.setText("Default p-value threshold (for t-tests) [0.0 : 1.0]");
      addMouseover(4,"Default p-Value used in the t-Test data Filter."+
                   "\nThis is the initial value shown in popup sliders.");
      
      label5.setText("Default Coeff. Of Variation threshold [0.0 : 1.0]");
      addMouseover(5,"Default Coefficient Of Variation used in the data Filters. This"+
                   "\nis the initial value shown in popup sliders.");
      
      label6.setText("Default absolute difference threshold [0.0 : 4.0]");
      addMouseover(6,"Default absolute difference threshold used in the data Filters."+
                   "\nThis is the initial value shown in popup sliders.");
      
      addLabelPanelsToMainPanel(); /* add all labelPanels' to mainPanel */
      
      textField1.setText(String.valueOf(mcd.clusterDistThr));
      textPanel.add(textField1);
      
      textField2.setText(String.valueOf(mcd.maxGenesToRpt));
      textPanel.add(textField2);
      
      textField3.setText(String.valueOf(mcd.nbrOfClustersThr));
      textPanel.add(textField3);
      
      textField4.setText(String.valueOf(mcd.pValueThr));
      textPanel.add(textField4);
      
      textField5.setText(String.valueOf(mcd.spotCVthr));
      textPanel.add(textField5);
      
      textField6.setText(String.valueOf(mcd.diffThr));
      textPanel.add(textField6);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* thresholdsPanel_PP */
  
  
  /**
   * filesPanel_PP() - popup panel handler gipo, samples DB, quant file names.
   * [NOTE] If the states are changed, vars in MaeConfigData() must be
   * changed as well (all variables checked).
   * @param int panelCode is POPUP_PANEL or SAVE_PANEL
   * @return Panel being edited
   * @see PseudoArray
   * @see UtilCM#cvs2i
   * @see #addLabelPanelsToMainPanel
   * @see #addMouseover
   * @see #setupPanels
   */
  private Panel filesPanel_PP(int panelCode)
  { /* filesPanel_PP */
    panelTitle= " ["+curPanel+
                "] GIPO & Sample DB files, Quant file naming convention";
    
    if(panelCode==DEFAULTS_PANEL)
    { /* reset mcd.xxx data for this panel from origMcd.xxx values */
      mcd.gipoFile= origMcd.gipoFile;
      mcd.samplesDBfile= origMcd.samplesDBfile;
      mcd.quantFileExt= origMcd.quantFileExt;
      return(null);
    }
    
    else if(panelCode==SAVE_PANEL)
    { /* save data from panel back into the state */
      String
        gipoFile= textField1.getText(),
        samplesDBfile= textField2.getText(),
        quantFileExt= textField3.getText();
      
      /* [TODO] How do we check if the spelling is correct if
       * the files do not exist yet?
       */
      if(gipoFile!=null && samplesDBfile!=null && quantFileExt!=null)
      { /* ok - save it */
        mcd.gipoFile= gipoFile;
        mcd.samplesDBfile= samplesDBfile;
        mcd.quantFileExt= quantFileExt;
        
        madeChangesFlag= true;
      }  /* ok - save it */
    } /* save data from panel back into the state */
    
    else
    { /* popup query panel */
      setupPanels(gridRow, gridCol);
      
      label1.setText("GIPO (array print) file name");
      addMouseover(1, "Name of the GIPO (array print) file. This file contains"+
                  "\nrows of data for each spot or oligo in the array, it's"+
                  "\n (rgid,row,column) in the array, its Clone ID or"+
                  "\nGenBank ID, its Gene name (if known), the original"+
                  "\nplate position (for spotted arrays), etc.");
      
      label2.setText("Hybridized array samples database file name");
      addMouseover(2, "Name of the hybridized array samples database file name."+
                   "\nThis lists all of the samples that exists in the database"+
                   "\nthat you may want to load.");

      label3.setText("Alternate quantification spot file extension");
      addMouseover(3, "Alternate quantification spot file extension"+
                   "\nto use instead of the default '.quant' value.");
      
      addLabelPanelsToMainPanel(); /* add all labelPanels' to mainPanel */
      
      textField1.setText(mcd.gipoFile);
      textPanel.add(textField1);
      
      textField2.setText(mcd.samplesDBfile);
      textPanel.add(textField2);
      
      textField3.setText(mcd.quantFileExt);
      textPanel.add(textField3);
      
      mainPanel.add(textPanel);
      this.add(mainPanel, BorderLayout.CENTER);
      this.setVisible(true);
      
      return(mainPanel);
    } /* popup query panel */
    
    return(null);
  } /* filesPanel_PP */
  
  
  /**
   * itemStateChanged() - handler if check box state was changed
   * @param e ItemEvent
   */
  public void itemStateChanged(ItemEvent e)
  { /* itemStateChanged */
    checkBoxFlag= false;
    fileSavedFlag= false;   
    
    Object itemObj= (Object) e.getSource();
    
    /* [1] Check if has genomic ids has been choosen */
    if(itemObj instanceof Checkbox)
    {
      boolean genomicIdFlag= hasGenomicIDsfromDescrCB.getState();  
      
      /* [1.1] Must shut others off since we will be getting it from "Descriptions"*/
      if(genomicIdFlag)
      {
        hasCloneIDsCB.setState(false);
        hasGenBankIDsCB.setState(false);
        hasLocusIDsCB.setState(false);
        hasUniGeneIDsCB.setState(false);
      }      
    }
    
    /*
    if(DEBUGGING)
       System.out.println("chk box changed");
    */
  } /* itemStateChanged */
  
  
  /**
   * textValueChanged() - clear flags
   * @param e is text changed event
   */
  public void textValueChanged(TextEvent e)
  { /* itemStateChanged */
    fileSavedFlag= false;
    textFieldFlag= false;
    /*
    if(DEBUGGING)
      System.out.println("TF changed");
    */
  } /* itemStateChanged */
  
  
  /**
   * mouseEntered() - put mouse over data in MouseOver text area
   * @param me is mouse entered event
   */
  public void mouseEntered(MouseEvent me)
  { /* mouseMoved */
    Label lbl= (Label)me.getSource();
    int idx= 0;
    
    if(lbl==label1)
      idx= 1;
    else if(lbl==label2)
      idx= 2;
    else if(lbl==label3)
      idx= 3;
    else if(lbl==label4)
      idx= 4;
    else if(lbl==label5)
      idx= 5;
    else if(lbl==label6)
      idx= 6;
    else if(lbl==label7)
      idx= 7;
    else if(lbl==label8)
      idx= 8;
    else if(lbl==label9)
      idx= 9;
    else if(lbl==label10)
      idx= 10;
    else
      return;             /* ForGetAboutIt! */
    
    String data=  mouseOverStr[idx];
    if(data!=null && data.length()>0)
      empf.mouseoverTA.setText(data);
    //else
    //empf.mouseoverTA.setText("");
  } /* mouseMoved */
  
  
  /**
   * mouseExited() - clear mouseover text area.
   * @param me is mouse exited event
   */
  public void mouseExited(MouseEvent me)
  { /* mouse */    
    /* Shut off for now since we want it remain on when mouse is
     * elsewhere, this will leave the last mouseOver item displayed 
     */
    //empf.mouseoverTA.setText("");
  } /* mouse */
  
  
  public void mouseMoved(MouseEvent me)  { }
  public void mouseClicked(MouseEvent me)  { }
  public void mousePressed(MouseEvent me)  { }
  public void mouseReleased(MouseEvent me)  { }
  
  
  
  /** ======================  CLASS DialogBox ========================  **/
  public class DialogBox extends Dialog implements ActionListener, WindowListener
  {
    String
      msg;
    boolean
      okFlag;
    Frame
      f;
    
    /**
     * DialogBox() - Constructor
     * @param f frame
     * @param msg str to be displayed in dialog box.
     * @see #buildGUI
     */
    public DialogBox(Frame f, String msg)
    {
      super(f, "Message", true);
      okFlag= true;
      this.msg= msg;
      this.f= f;
      buildGUI();
    }
    
    
    /**
     * buidlGUI() - Build and display GUI for dialog box.
     */
    private void buildGUI()
    { /* buildGUI */
      String displayMsg;
      Button
        yesButton= new Button("OK"),
        noButton= new Button("Cancel");
      Label label;
      
      yesButton.addActionListener(this);
      noButton.addActionListener(this);
      if(msg!=null)
        displayMsg= msg;
      else
        displayMsg= "Choose OK or Cancel";
      label= new Label(displayMsg);
      
      Panel
        buttonPanel= new Panel(),
        mainPanel= new Panel(new BorderLayout());
      
      buttonPanel.add(yesButton);
      //buttonPanel.add(noButton);
      mainPanel.add(label,BorderLayout.NORTH);
      mainPanel.add(buttonPanel,BorderLayout.SOUTH);
      this.add(mainPanel);
      this.setSize(250,300);
      this.setTitle(displayMsg);
      this.pack();
      
      /* put the Dialog box in the middle of the frame */
      Dimension
        myDim= getSize(),
        frameDim= f.getSize(),
        screenSize= getToolkit().getScreenSize();      
      Point loc= f.getLocation();
      
      loc.translate((frameDim.width - myDim.width)/2,(frameDim.height - myDim.height)/2);
      loc.x= Math.max(0,Math.min(loc.x,screenSize.width - getSize().width));
      loc.y= Math.max(0,Math.min(loc.y, screenSize.height - getSize().height));
      
      setLocation(loc.x,loc.y);
      this.setVisible(true);
    } /* buildGUI */
    
    
    public void actionPerformed(java.awt.event.ActionEvent ae)
    {  /* actionPerformed */
      String cmd= ae.getActionCommand();
      Button item= (Button) ae.getSource();
      if(cmd.equals("OK"))
      {
        okFlag= true;
        close();
      }
      else if(cmd.equals("Cancel"))
      {
        okFlag= false;
        close();/* stop without saving */
      }
    }  /* actionPerformed */
    
    
    /**
     * close() - close this popup
     */
    void close()
    { /* close */
      this.dispose();
    } /* close */
    
    
    /**
     * windowClosing() - close down the window.
     */
    public void windowClosing(WindowEvent e)
    { /* close */
      close();
    } /* close */
    
    
    /* Others not used at this time */
    public void windowOpened(WindowEvent e)  { }
    public void windowActivated(WindowEvent e)  { }
    public void windowClosed(WindowEvent e)  {}
    public void windowDeactivated(WindowEvent e)  { }
    public void windowDeiconified(WindowEvent e)  { }
    public void windowIconified(WindowEvent e)  { }
  }/* End Class DialogBox */
  
  
  
} /* class EditWizardPanel */

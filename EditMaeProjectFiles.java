/** File: EditMaeProjectFiles.java */

package cvt2mae;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import java.util.Date;

/**
 * GUI for editing data in MAE, Config, Samples and Startup data files 
 * for use in MAExplorer. Uses MaeConfigData, MaeStartupData, MaeSampleData, 
 * and FileTable data.
 *<P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author G. Thornwall (SAIC), P. Lemkin (NCI), B. Stephens(SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $  $Revision: 1.6 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class EditMaeProjectFiles extends Frame implements ActionListener,
							  WindowListener
{ 
  /*
   * [TODO]
   * 1. check data before save, prompt user to fix data OR check before
   *    going to next Pi?
   * 2. help text for help menu.
   * 3. reset to orig data, each panel and global? 
   */
  
  /** size of mouseover TextArea */
  final public static int
    TA_ROWS= 9;   
  /** size of mouseover TextArea */
  final public static int
    TA_COLS= 70;  
  /** default frame size width */
  final public static int
    DEFAULT_WIDTH= 660; 
  /** default frame size width */
  final public static int
    DEFAULT_HEIGHT= 470; 
  /** maximum # of sample files at one time */
  final public static int
    MAX_SAMPLES= 300;           
  /** for debuging */
  final public static boolean
    EMPF_DBUG= true;
  
  /* --- Data structures for specific MAE database files --- */
  /** global link to Cvt2Mae instance */
  public Cvt2Mae
    cvt;  
  /** global link to CvtGUI instance */
  public CvtGUI
    gui;    
  /** global link to SetupLayouts instance */
  public SetupLayouts
    sul;
  /** global link to UtilCM instance */
  public UtilCM 
    util; 
  /** copy ofMaeConfigData  data for reset */      
  public MaeConfigData
    origMcd;
  /** working MaeConfigData data */
  public MaeConfigData
    mcd;			
  /** global instance of MaeSampleData sample DB state */
  public MaeSampleData
    msd;			
  /** global instance of MaeStartupData Startup DB state */
  public MaeStartupData
    msud;	
  /** current table for getting (name,value) pairs from file */
  public FileTable
    nvTbl;                      
  /** turn off next button if at first Panel */
  public boolean
    backButtonFlag;		
  /** turn off next button if at last panel */
  public boolean
    nextButtonFlag;
  /** (!editFilesFlag && !mcd.vendor.equals("<User-defined>")) */
  public boolean
    genericFinishButtonFlag;
  /** set only for last panel */
  public boolean
    addFinishButtonFlag;      
  /** editing data from file, else from Cvt2Mae */
  public boolean
    editFilesFlag;             
  /** errors in getConfigValuesFromTable() bools */
  public boolean
    errConversionFlag;	        
  
  /* --- GUI vars for stand-alone editor invoked by EditMaeProject --- */
  /** main menubar for standalone if using main in EditMaeProject*/
  private MenuBar
    menuBar;			
  
  /** new project (future) */
  private MenuItem 
    newItem;			
  /** open file */
  private MenuItem 
    openItem;			
  /** save file */
  private MenuItem 
    saveItem;			
  /** save as file */
  private MenuItem
    saveAsItem;			
  /** edit cut */
  private MenuItem
    cutItem;			
  /** edit copy */
  private MenuItem
    copyItem;			
  /** edit paste */
  private MenuItem
    pasteItem;			
  /** help menu help page */
  private MenuItem
    displayHelpItem;		
  /** misc */
  private MenuItem
    aboutMenuItem;		
  /** edit del */
  private MenuItem
    delMenuItem;		
  /** exit menu item */
  private MenuItem
    exitItem;			
  /** list menu item */
  private MenuItem
    listMenuItem;		
  /** File menu */
  private Menu 
    fileMenu;			
  /** Edit menu */
  private Menu 
    editMenu;			
  /** Help menu */
  private Menu 
    helpMenu;			
  /** Tools menu */
  private Menu 
    toolsMenu;			
  /** checkbox toggle for expert mode */
  private CheckboxMenuItem
    expertCheckboxMenuItem;	
  /** place data forms here */
  private Panel
    mainPanel;			
  /** data entry */
  private Panel
    textPanel;			
  /** also for data entry */
  private Panel
    labelPanel;			
  /** status data goes here */
  private Panel
    statusPanel;		
  /** mouse over & buttons go here*/
  private Panel
    lowerPanel;			
  /** mouse over goes here */
  private Panel
    taPanel;			
  /** buttons goes here */
  private Panel
    buttonPanel;		
  /** for any error msgs, Panel titles */
  private Label
    statusLabel;
  private Label
    /** show file name here */
    fileNameLabel;
  /** mouseover TextArea */
  public TextArea
    mouseoverTA;	        
  /** PP Q&A move back 1 panel */
  private Button
    backButton;	
  /** PP Q&A go forward to next panel */
  private Button
    /** PP Q&A done editing */
    nextButton;			
  private Button
    /** PP Q&A reset just current panel from origMcd */
    finishButton;              
  private Button
    /** PP Q&A cancel editing */
    defaultButton;             
  private Button
    /** PP Q&A cancel editing */
    cancelButton;              
  /** font */
  public Font
    font;			
  /** frame background color*/
  public Color
    bkGrdClr;
  /** frame size */
  public Dimension
    frameDim;	
  /** index for Value for getValueByName methods */
  public int
    idxValue;
  /** index for Parameter for getValueByName methods */		
  public int	
    idxParameter;		
  /** popup EditWizardPanel configuation Q&A panel */
  public EditWizardPanel
    ecp;			
  /** window frame size width */
  public int
    preferredWidth;		
  /** window frame size height */
  public int
    preferredHeight;		
  /** default "Open" directory */
  private String
    defOpenDir;		        
  /** frame OK prompt dialog box */
  public Frame
    popupDialogFrameOK;	
  /** frame for yes/no dialog box */
  public Frame
    popupDialogFrame;	
  /** OK prompt dialog box */
  public PopupEditPrjDialog
    okpdq;			
  /** yes/no dialog box */
  public PopupEditPrjDialog
    pdq;			
  /** title of window */
  public String
    title;
  /** name of file being edited */
  public String        
    fileName;   
  
  
  /**
   * EditMaeProjectFiles() - Constructor
   * @param defOpenDir flag to declare saving on disk
   * @param mcd instance of MaeConfigData if it exists
   * @param origMcd is instance of MaeConfigData if it exists
   * @param editFilesFlag is true for editing disk file, false for Cvt2Mae edit
   * @param title of the window
   * @see MaeConfigData
   * @see #createGUI
   */
  public EditMaeProjectFiles(String defOpenDir, MaeConfigData mcd,
  MaeConfigData origMcd, boolean editFilesFlag,
  String title)
  { /* EditMaeProjectFiles */
    super(title);
    
    this.defOpenDir= defOpenDir;
    this.mcd= mcd;
    this.origMcd= origMcd;
    this.editFilesFlag= editFilesFlag;
    this.title= title;
    
    cvt= mcd.cvt;             /* update */
    gui= cvt.gui;
    sul= cvt.sul;
    util= cvt.util;
    
    errConversionFlag= false;
    addFinishButtonFlag= false;
    
    if(!cvt.isWinPCflag)
    {
      /* Need more space for Sun OS */
      preferredWidth= DEFAULT_WIDTH+50;
      preferredHeight= DEFAULT_HEIGHT+75;
      frameDim= getPreferredSize();
    }
    else
    {
      preferredWidth= DEFAULT_WIDTH;
      preferredHeight= DEFAULT_HEIGHT;
      frameDim= getPreferredSize();
    }
    
    /* Set up initial data file */
    if(mcd==null)
    {
      this.mcd= new MaeConfigData(MAX_SAMPLES);
      mcd.vendor= "<User-defined>";      /* edit from the file */
      mcd.layoutName= "<User-defined>";
    }
    
    nextButtonFlag= true;
    
    genericFinishButtonFlag= (!editFilesFlag &&
                              ((mcd.vendor!=null && 
                                !mcd.vendor.equals("<User-defined>")) ||
                               mcd.vendor==null));
    /*
    if(DEBUGGING)
      System.out.println("EPF genericFinishButtonFlag="+genericFinishButtonFlag+
                         "\n editFilesFlag="+editFilesFlag+
                         "\n mcd.vendor="+mcd.vendor);
    */
    
    msud= this.mcd.msud;           /* setup links to rest of state */
    msd= this.mcd.msd;
    
    idxParameter= 0;               /* .mae Startup file fields */
    idxValue= 1;                   /* 2nd column */
    
    fileName= null;
    
    createGUI();                    /* create GUI */
  } /* EditMaeProjectFiles */
  
  
  /**
   * createGUI() - create main GUI, menus, buttons main panel
   * @see EditWizardPanel
   * @see EditWizardPanel#doEditPanel
   * @see PopupEditPrjDialog
   * @see #createMenuBarGUI
   */
  private void createGUI()
  { /* createGUI */    
    /* [1] setup dir name */
    mcd.fileSep= System.getProperty("file.separator");
    mcd.userDir= System.getProperty("user.dir") + mcd.fileSep;
    defOpenDir= mcd.userDir;          /* in jws finds the bin dir
     * instead of current dir*/
    
    /* [2] create the edit panel */
    ecp= new EditWizardPanel(this, 1 /* initialCurPanel */ );
    
    /* [3] create first panel & add to main panel */
    mainPanel= ecp.doEditPanel(ecp.POPUP_PANEL,false,1);/* popup 1rst panel*/
    add(mainPanel, BorderLayout.CENTER);
    
    /* [4] Setup some components on main panel */
    statusPanel= new Panel();
    statusPanel.setLayout(new GridLayout(2, 1));
    statusPanel.setFont(font);
    statusPanel.setName("statusPanel");
    statusPanel.setBackground(bkGrdClr);
    statusPanel.setForeground(Color.black);
    
    statusLabel= new Label();
    statusLabel.setFont(font);
    statusLabel.setName("statusLabel");
    statusLabel.setBackground(bkGrdClr);
    statusLabel.setForeground(Color.black);
    statusLabel.setText(ecp.firstPanelStr);
    statusPanel.add(statusLabel);
    
    fileNameLabel= new Label();
    fileNameLabel.setFont(font);
    fileNameLabel.setName("fileName");
    fileNameLabel.setBackground(bkGrdClr);
    fileNameLabel.setForeground(Color.black);
    fileNameLabel.setText("");
    statusPanel.add(fileNameLabel);
    
    add(statusPanel, BorderLayout.NORTH);
    
    buttonPanel= new Panel();
    buttonPanel.setFont(font);
    buttonPanel.setName("buttonPanel");
    buttonPanel.setBackground(bkGrdClr);
    buttonPanel.setForeground(Color.black);
    
    backButton= new Button("<Back");
    backButton.setFont(font);
    backButton.setBackground(Color.lightGray);
    backButton.setForeground(Color.black);
    backButton.addActionListener(this);
    buttonPanel.add(backButton);
    backButton.setEnabled(backButtonFlag);
    
    nextButton= new Button("Next>");
    nextButton.setFont(font);
    nextButton.setBackground(Color.lightGray);
    nextButton.setForeground(Color.black);
    nextButton.addActionListener(this);
    buttonPanel.add(nextButton);
    
    finishButton= new Button("Finish");
    finishButton.setFont(font);
    finishButton.setBackground(Color.lightGray);
    finishButton.setForeground(Color.black);
    finishButton.addActionListener(this);
    finishButton.setEnabled(genericFinishButtonFlag);
    buttonPanel.add(finishButton);
    
    defaultButton= new Button("Defaults");
    defaultButton.setFont(font);
    defaultButton.setBackground(Color.lightGray);
    defaultButton.setForeground(Color.black);
    defaultButton.addActionListener(this);
    //buttonPanel.add(defaultButton);
    
    cancelButton= new Button("Cancel");
    cancelButton.setFont(font);
    cancelButton.setBackground(Color.lightGray);
    cancelButton.setForeground(Color.black);
    cancelButton.addActionListener(this);
    buttonPanel.add(cancelButton);
    
    /* [4.1] Add the button panel to main popup */
    add(buttonPanel, BorderLayout.SOUTH);
    
    /* [5] Add the Mouseover panel */
    taPanel= new Panel();
    taPanel.setFont(font);
    taPanel.setBackground(bkGrdClr);
    taPanel.setForeground(Color.black);
    
    lowerPanel= new Panel();
    lowerPanel.setBackground(bkGrdClr);
    lowerPanel.setForeground(Color.black);
    lowerPanel.setLayout(new BorderLayout());
    
    /* Setup text area. If strings are going to be large then need
     * to resize, (adding SB will not work since the cursor is in use!)
     */
    mouseoverTA= new TextArea(" ", TA_ROWS, TA_COLS,
    TextArea.SCROLLBARS_BOTH);
    taPanel.add(mouseoverTA, BorderLayout.NORTH);
    
    lowerPanel.add(taPanel, BorderLayout.NORTH);
    lowerPanel.add(buttonPanel, BorderLayout.SOUTH);
    add(lowerPanel, BorderLayout.SOUTH);
    
    /* [6] Add extra space */
    Panel
      spPanelE= new Panel(),
      spPanelW= new Panel();
    
    spPanelE.setBackground(bkGrdClr);
    spPanelW.setBackground(bkGrdClr);
    
    add(spPanelE, BorderLayout.EAST);
    add(spPanelW, BorderLayout.WEST);
    
    /* [7] If using stand-alone editor, then create the menu bar.
     * Don't add menu bar for use with Cvt2Mae.
     */
    if(editFilesFlag)
    {
      createMenuBarGUI();
      setMenuBar(menuBar);
    }
    
    /* [8] Handle close window events */
    this.addWindowListener(this);  /* so activate window controls*/
    
    /* [9] Center frame on the screen and popup editor window */
    Dimension screen= Toolkit.getDefaultToolkit().getScreenSize();
    Point pos= new Point((screen.width-this.getSize().width)/5,
                         (screen.height-this.getSize().height)/5);
    this.setLocation(pos);
    
    setSize(frameDim);	     /* set frame size */
    pack();
    setVisible(true);
    
    /* [10] Create popup dialog boxes for later use. DO NOT popup now. */
    popupDialogFrame= new Frame();
    pdq= new PopupEditPrjDialog(popupDialogFrame, mcd);
    popupDialogFrameOK= new Frame();
    okpdq= new PopupEditPrjDialog(popupDialogFrameOK, mcd, " ");
  } /* createGUI */
  
  
  /**
   * createMenuBarGUI() - create separate menu bar GUI used in stand-alone editor.
   */
  private void createMenuBarGUI()
  { /* createMenuBarGUI */
    /* [TODO] group these logically with extra CRLFs */
    
    /* [1] menu bar and other components */
    menuBar= new MenuBar();
    
    fileMenu= new Menu();
    newItem= new MenuItem();
    openItem= new MenuItem();
    saveItem= new MenuItem();
    saveAsItem= new MenuItem();
    exitItem= new MenuItem();
    
    editMenu= new Menu();
    cutItem= new MenuItem();
    copyItem= new MenuItem();
    pasteItem= new MenuItem();
    delMenuItem= new MenuItem();
    
    toolsMenu= new Menu();
    expertCheckboxMenuItem= new CheckboxMenuItem();
    listMenuItem= new MenuItem();
    
    helpMenu= new Menu();
    displayHelpItem= new MenuItem();
    aboutMenuItem= new MenuItem();
    
    mainPanel= new Panel();
    labelPanel= new Panel();
    textPanel= new Panel();
    
    /* [2] top pull down menus for the frame */
    fileMenu.addActionListener(this);
    
    fileMenu.setName("File");               /* pull-down menu */
    fileMenu.setLabel("File");
    
    newItem.setLabel("New");
    newItem.setActionCommand("New");
    fileMenu.add(newItem);
    
    openItem.setLabel("Open");
    openItem.setActionCommand("Open");
    fileMenu.add(openItem);
    
    saveItem.setLabel("Save");
    saveItem.setActionCommand("Save");
    fileMenu.add(saveItem);
    
    saveAsItem.setLabel("Save As");
    saveAsItem.setActionCommand("Save As");
    fileMenu.add(saveAsItem);
    
    fileMenu.addSeparator();
    
    exitItem.setLabel("Exit");
    exitItem.setActionCommand("Exit");
    fileMenu.add(exitItem);
    
    menuBar.add(fileMenu);
    
    /* -------------------------------------- */
    
    editMenu.setName("Edit");               /* pull-down menu */
    editMenu.setLabel("Edit");
    
    cutItem.setLabel("Cut");
    cutItem.setActionCommand("Cut");
    editMenu.add(cutItem);
    
    copyItem.setLabel("Copy");
    copyItem.setActionCommand("Copy");
    editMenu.add(copyItem);
    
    pasteItem.setLabel("Paste");
    pasteItem.setActionCommand("Paste");
    editMenu.add(pasteItem);
    
    editMenu.addSeparator();
    
    delMenuItem.setLabel("Delete");
    delMenuItem.setActionCommand("Delete");
    editMenu.add(delMenuItem);
    
    // FUTURE  menuBar.add(editMenu);
    
    /* -------------------------------------- */
    
    toolsMenu.setName("Tools");               /* pull-down menu */
    toolsMenu.setLabel("Tools");
    
    expertCheckboxMenuItem.setLabel("Expert Mode");
    toolsMenu.add(expertCheckboxMenuItem);
    
    listMenuItem.setName("ListMenuItem");
    listMenuItem.setLabel("Show Entire List");
    listMenuItem.setActionCommand("Show Entire List");
    toolsMenu.add(listMenuItem);
    
    // FUTURE  menuBar.add(toolsMenu);
    
    /* -------------------------------------- */
    
    helpMenu.setName("Help");                 /* pull-down menu */
    helpMenu.setLabel("Help");
    
    displayHelpItem.setLabel("Display Help");
    delMenuItem.setActionCommand("Delete");
    helpMenu.add(displayHelpItem);
    
    aboutMenuItem.setLabel("About");
    aboutMenuItem.setActionCommand("About");
    helpMenu.add(aboutMenuItem);
    
    menuBar.add(helpMenu);
  } /* createMenuBarGUI */
  
  
  /**
   * handleActions() - Menu item events.
   * @param e ActionEvent
   */
  boolean handleActions(ActionEvent e)
  { /* handleActions */
    String actCmd= e.getActionCommand();
    
    return true;
  } /* handleActions */
  
  
  /**
   * promptFileName() - create file dialog to prompt for file name
   * @param promptDir default directory
   * @param msg window message
   * @return full path name
   */
  String promptFileName(String promptDir, String msg)
  { /* promptFileName */
    Frame fdFrame= new Frame("FileDialog");
    FileDialog fd= new FileDialog(fdFrame, msg, FileDialog.LOAD);
    if(promptDir!=null)
      fd.setDirectory(promptDir);
    
    fd.setVisible(true);
    
    String newFile= fd.getFile();
    
    if(newFile==null)
      return(null);
    
    promptDir= fd.getDirectory();
    
    String fullPath= promptDir + newFile;
    
    return(fullPath);
  } /* promptFileName */
  
  
  /**
   * readConfigFile() - read config file
   * @param defaultPromptDir is the default directory if not null
   * @return true if succeed
   */
  public FileTable readConfigFile(String defaultPromptDir)
  { /* readConfigFile */    
    /* [1] open file dialog and get full path and file name */
    String fileName= promptFileName(defaultPromptDir, "Load File");
    
    /* [2] read file as table */
    if(fileName!=null)
    { /* read the file */
      FileTable fio= new FileTable("Config File");
      
      if(!fio.readFileAsTable(fileName))
        return(null);
      
      /* save file name */
      this.fileName= fileName;
      mcd.configFile= fileName;
      
      return(fio);
    } /* read the file */
    
    else
      return(null);
  } /* readConfigFile */
  
  
  /**
   * saveAs() - check and then save data to new named file
   * popup a filename dialog to get the file name.
   * @return true if succeed
   * @see #promptFileName
   * @see #saveData
   */
  public boolean saveAs()
  { /* saveAs */
    /* TODO: warn user that all data might not be filled out and
     * ready to be used by MAExplorer.
     */
    
    /* save all data from panel, textfields etc */
    this.fileName= promptFileName(mcd.userDir, "save file");
    
    if(saveData(this.fileName))
    {
      mcd.configFile= this.fileName;
      return(true);
    }
    
    return(false);
  } /* saveAs */
  
  
  /**
   * save() - check and then save data to file.
   * @param fileName is name of file to save
   * @return true if were able to save the file
   */
  public boolean save(String fileName)
  { /* save */
    boolean rFlag= true;
    
    /* [TODO] may want to do additional checks towarn user that all data
     * might not be filled out and ready to be used by MAExplorer.
     * Note that all data is checked by event handlers, but may want
     * to do more.
     */
    
    /* Save if file exits just over write it without prompting user? */
    if(fileName==null)
    { /* no file name given or error */
      rFlag= saveAs();
    }
    else
    { /* save all data from panels, textfields etc */
      rFlag= saveData(fileName);
    }
    
    return(rFlag);
  } /* save */
  
  
  /**
   * saveData() - save data to file
   * @param fileName to save
   * @return true if succeed
   * @see MaeConfigData#writeConfigFile
   * @see PopupEditPrjDialog#updatePopupDialog
   */
  boolean saveData(String fileName)
  { /* saveData */
    if(mcd!=null && fileName!=null)
    { /* write out Config data to file */
      if(mcd.writeConfigFile())
      { /* since saved reset flags */
        ecp.textFieldFlag= true;
        ecp.checkBoxFlag= true;
        ecp.fileSavedFlag= true;
        this.fileName= fileName;	/* update fileName */
        mcd.configFile=fileName;
        System.out.println("Saved edited configuration data ok");
      }
      else
      { /* problem writing Config file */
        mcd.configFile= null;
        this.fileName= null;
        return(false);
      }
    } /* write out data to file */
    else
    {
      okpdq.updatePopupDialog("Error saving data.");
      System.out.println("ECP: Error saving data null file name");
      return(false);
    }
    
    return(true);
  }/* saveData */
  
  
  /**
   * actionPerformed() - button listener for menus and panel buttons
   * @param e ActionEvent
   * @see CvtGUI#chkReanlyzeButtonState
   * @see CvtGUI#saveChipValues
   * @see CvtGUI#setEditLayoutEditing
   * @see CvtGUI#setMapEditing
   * @see EditWizardPanel
   * @see EditWizardPanel#back
   * @see EditWizardPanel#doEditPanel
   * @see EditWizardPanel#next
   * @see EditWizardPanel#reset
   * @see EditWizardPanel#resetCurrentPanelDefaults
   * @see MaeConfigData
   * @see PopupEditPrjDialog#alertTimeout
   * @see PopupEditPrjDialog#updatePopupDialog
   * @see #readConfigFile
   * @see #save
   *
   */
  public void actionPerformed(ActionEvent e)
  { /* actionPerformed */
    String cmd= e.getActionCommand();
    
    if(cmd.equals("EXIT"))
    { /* exit */
      
      /* [1] before exit chk for unsaved data */
      if(ecp.fileSavedFlag)
        pdq.alertTimeout("Exit without saving file?");
      
      /* [2] exit if all saved ok */
      if(pdq.flag)
        System.exit(0);
      else
      { /* try to save first before exit */
        if(save(fileName))
          System.exit(0);
      } /* save data first */
    } /* exit */
    
    else if(cmd.equals("Open")) /* Open existing file to edit*/
    { /* Open */
      FileTable cfgTbl= readConfigFile(defOpenDir);  /* read into cfg */
      
      if(cfgTbl!=null)
      { /* Read config file */
        /* [1] since new file opened, reset flags */
        ecp.textFieldFlag= true;
        ecp.checkBoxFlag= true;
        ecp.fileSavedFlag= true;
        statusLabel.setText(ecp.firstPanelStr);
        
        /* [2] store values from file in proper variables */
        if(!getConfigValuesFromTable(cfgTbl))
          return;
        
        /* [3] create edit panel */
        ecp= new EditWizardPanel(this,1 /* initialCurPanel */);
        
        /* [4] create and display new panel */
        Panel tmpPanel= ecp.doEditPanel(ecp.POPUP_PANEL,false,1); /* popup first panel */
        
        remove(mainPanel);
        mainPanel=tmpPanel;
        
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
        
        /* [5] stuff file name in status window */
        if(this.fileName!=null)
          fileNameLabel.setText("File: "+ this.fileName);
        else
          System.out.println("EMPF: Error reading file");
      } /* Read config file */
      
      else
      { /* error reading data */
        okpdq.updatePopupDialog("No File Choosen.");
        System.out.println("EMPF: Error reading data");
      } /* error reading data */
    } /* Open */
    
    else if(cmd.equals("Save"))
    { /* Save */
      save(fileName);
    } /* Save */
    
    else if(cmd.equals("Save As"))
    { /* SaveAs */
      saveAs();
      if(this.fileName!=null)
        fileNameLabel.setText("File: "+ this.fileName);
    } /* SaveAs */
    
    else if(cmd.equals("<Back"))
    { /* back */
      Panel tmpPanel= ecp.back();
      
      if(tmpPanel!=null)
      {
        remove(mainPanel);
        mainPanel= tmpPanel;
        add(mainPanel, BorderLayout.CENTER);
        statusLabel.setText(ecp.panelTitle);
        finishButton.setEnabled(genericFinishButtonFlag);
        backButton.setEnabled(backButtonFlag);
        nextButton.setEnabled(nextButtonFlag);
        
        setVisible(true);
      }
    } /* back */
    
    else if(cmd.equals("Next>"))
    { /* next */
      Panel tmpPanel= ecp.next(false);
      
      if(tmpPanel!=null)
      {
        remove(mainPanel);
        mainPanel= tmpPanel;
        add(mainPanel, BorderLayout.CENTER);
        statusLabel.setText(ecp.panelTitle);
        finishButton.setEnabled(genericFinishButtonFlag ||
        addFinishButtonFlag);
        backButton.setEnabled(backButtonFlag);
        nextButton.setEnabled(nextButtonFlag);
        
        setVisible(true);
      }
    } /* next */
    
    else if(cmd.equals("Finish"))
    { /* Finish */
      Panel tmpPanel= ecp.next(true);  /* Save the last entry */
      mcd.beingEditedFlag= false;
      mcd.editMaePrjFilesFlag= true;
      
      if(ecp.madeChangesFlag)
      { /* user did make changes - not just reviewed data */
        mcd.editedLayoutFlag= true;        /* Enable "Save Layout" */
        if(mcd.hasUserDefineChipsetFlag)
        { /* force GIPO assignment for User-Defined */
          cvt.fmgFirstTimeFlagGipo= true;
          cvt.fmgFirstTimeFlagQuant= true;
          cvt.fmgFirstTimeFlagSample= true;
          gui.setMapEditing(true);
        }
        /* enable "Save Layout" if any changes */
        gui.setEditLayoutEditing(true);
        
        sul.alList[sul.useAL].bitProps= mcd.calcBitPropFromMCDstate();
        /* Set it in the Array layout */
        sul.alList[sul.useAL].saveArrayFlag= true;
      }
      
      if(editFilesFlag)
      { /* save the data in the current file - if local editing */
        save(fileName);
      }
      else
      { /* notify Cvt2Mae that done so it can use mcd. data */
        gui.saveChipValues();  /* save data that was entered by user */
        mcd.didEditALOflag= true;
        gui.chkReanlyzeButtonState();
        this.dispose();        /* close window */
      }
    } /* Finish */
    
    else if(cmd.equals("Cancel"))
    { /* Cancel */
      cancel();                  /* cancel the editor window */
    } /* Cancel */
    
    else if(cmd.equals("Defaults"))
    { /* Defaults - reset current wizard panel to origMcd data */
      ecp.resetCurrentPanelDefaults();
    } /* Defaults */
    
    else if(cmd.equals("Reset"))
    { /* reset */
      /* TODO need 2 resets, 1 global other local panel */
      ecp.reset();
    } /* reset */
    
    else if(cmd.equals("New"))
    { /* new file */
      /* prompt first, clear/reset data */      
      pdq.updatePopupDialog("Delete old data without saving to file?");
      
      if(pdq.flag)
      { /* overwrite data */
        mcd= new MaeConfigData(MAX_SAMPLES);
        msud= mcd.msud;
        msd= mcd.msd;
      }
      else
      { /* save file */
        saveAs();
      }
    } /* new */
  }/* actionPerformed */
  
  
  /**
   * cancel() - cancel the editor window
   */
  private void cancel()
  { /* cancel */
    mcd.beingEditedFlag= false;
    mcd.editMaePrjFilesFlag= true;
    mcd.editedLayoutFlag= false; /* reset it - must edit first to "Save Layout" */
    
    /* Restore the original state in cvt.mcd */
    mcd.restoreMCDstate(origMcd);
    
    this.dispose();
  } /* cancel */
  
  
  /**
   * dateStr() - return a new Date string of the current day and time
   * @return current date
   */
  public static String dateStr()
  { /* dateStr */
    Date dateObj= new Date();
    String date= dateObj.toString();
    
    return(date);
  } /* dateStr */
  
  
  /**
   * getPreferredSize() - get the preferred size
   * @return the preferred size
   */
  public Dimension getPreferredSize()
  { /* getPreferredSize */
    return(new Dimension(preferredWidth, preferredHeight));
  } /* getPreferredSize */
  
  
  /**
   * exitForm() - Exit the Application
   * @param evt WindowEvent
   */
  private void exitForm(WindowEvent evt)
  { /* exitForm */
    mcd.editMaePrjFilesFlag= true;
    System.exit(0);
  } /* exitForm */
  
  
  /**
   * getValueByName() - get String Value by Name
   * @return Value by Name, null if failed.
   */
  public String getValueByName(String name)
  { /* getValueByName */
    String sR= null;
    
    for(int mRow=0; mRow<nvTbl.tRows; mRow++)
    { /* look for name in row Parameter instance and return Value */
      String sTable= nvTbl.tData[mRow][idxParameter];
      if(sTable==null)
      {
        //System.out.println("getValueByName()error w/ table");
        return(null);
      }
      else if(sTable.equals(name))
      { /* found it */
        System.out.println("getValueByName()found value");
        sR= nvTbl.tData[mRow][idxValue];
        break;
      }
    }/* look for name in row Parameter instance and return Value */
    return(sR);
  } /* getValueByName */
  
  
  /**
   * getIntValueByName() - get integer value by name
   * @return value by Name, -1 if failed.
   */
  public int getIntValueByName(String name)
  { /* getIntValueByName */
    String sR= null;
    
    for(int mRow=0; mRow<nvTbl.tRows; mRow++)
    { /* look for name in row Parameter instance and return Value */
      String sTable= nvTbl.tData[mRow][idxParameter];
      if(sTable==null)
      {
        //System.out.println("getIntValueByName()error w/ table");
        return(-1) ;
      }
      else if(sTable.equals(name))
      { /* found it */
        
        sR= nvTbl.tData[mRow][idxValue];
        break;
      }
    }/* look for name in row Parameter instance and return Value */
    int iR= Integer.parseInt(sR);
    
    return(iR);
  } /* getIntValueByName*/
  
  
  /**
   * getBoolValueByName() - get integer value by name from table
   * @param name get integer value by name
   * @return value by name from table, false if failed.
   */
  public boolean getBoolValueByName(String name)
  { /* getBoolValueByName */
    String sR= null;
    
    for(int mRow=0; mRow<nvTbl.tRows; mRow++)
    { /* look for name in row Parameter instance and return Value */
      String sTable= nvTbl.tData[mRow][idxParameter];
      if(sTable==null)
      {
        errConversionFlag= true;    /* [TODO] handle error detected */
        return(false);
      }
      else if(sTable.equals(name))
      { /* found it */
        sR= nvTbl.tData[mRow][idxValue];
        break;
      }
    }
    Boolean bR= Boolean.valueOf(sR);
    
    if(bR.equals(Boolean.TRUE))
      return(true);
    else
      return(false);
  } /* getBoolValueByName*/
  
  
  /**
   * getConfigValuesFromTable() - get Config mcd. values from Config Table read in.
   * @param cfgTbl FileTable
   * @return true if succeed.
   * @see UtilCM#cvs2i
   * @see UtilCM#cvs2f
   * @see #getBoolValueByName
   * @see #getValueByName
   */
  public boolean getConfigValuesFromTable(FileTable cfgTbl)
  { /* getConfigValuesFromTable*/
    /* [TODO] Make sure have LATEST fields from both Config and .mae.
     * If editing a .mae file, then any fields not found here are simply
     * saved in a string and copied back to the output file at the end.
     */    
    if(cfgTbl==null)
      return(false);
    
    nvTbl= cfgTbl;           /* setup the Name Value parser table */
    
    float iTmp;
    
    /* [1] Parameters which configure the size of the array */
    mcd.nbrGENES= util.cvs2i(getValueByName("NBR_CLONES"), 0);
    mcd.nbrSPOTS= util.cvs2i(getValueByName("NBR_SPOTS"), 0);
    mcd.specifyGeometryByNbrSpotsFlag=
          util.cvs2b(getValueByName("specifyGeometryByNbrSpots"), false);
    mcd.specifyByGridGeometryFlag=
           util.cvs2b(getValueByName("specifyByGridGeometry"), false);
    
    /* Required fields */
    mcd.maxFields= util.cvs2i(getValueByName("MAX_FIELDS"), 0);
    mcd.maxGrids= util.cvs2i(getValueByName("MAX_GRIDS"), 0);
    mcd.maxGridCols= util.cvs2i(getValueByName("MAX_GRID_COLS"), 0);
    mcd.maxGridRows= util.cvs2i(getValueByName("MAX_GRID_ROWS"), 0);
    
    mcd.canvasHSize= util.cvs2i(getValueByName("CanvasHorSize"), 0);
    mcd.canvasVSize= util.cvs2i(getValueByName("CanvasVertSize"), 0);
    
    /* [2] Parameters which configure the thresholds */
    mcd.spotRadius= util.cvs2i(getValueByName("SpotRadius"), 0);
    mcd.diffThr= util.cvs2f(getValueByName("diffThr"), 0.0F);
    mcd.clusterDistThr= util.cvs2f(getValueByName("clusterDistThr"), 0.0F);
    mcd.nbrOfClustersThr= util.cvs2i(getValueByName("nbrOfClustersThr"), 0);
    mcd.pValueThr= util.cvs2f(getValueByName("pValueThr"), 0.0F);
    mcd.spotCVthr= util.cvs2f(getValueByName("spotCVthr"), 0.0F);
    
    /* [3] Parameters which define URLs */
    mcd.proxyServer= getValueByName("proxyServer"); /* only for applet */
    
    /* Handle both spellings for GenBank (i.e. GenBank) */
    mcd.genBankCloneURL= getValueByName("genBankCloneURL");
    if(mcd.genBankCloneURL==null)
      mcd.genBankCloneURL= getValueByName("geneBankCloneURL");
    
    mcd.genBankCloneURLepilogue= getValueByName("genBankCloneURLepilogue");
    if(mcd.genBankCloneURLepilogue==null)
      mcd.genBankCloneURLepilogue= getValueByName("geneBankCloneURLepilogue");
    
    mcd.dbEstURL= getValueByName("dbEstURL");
    mcd.mAdbURL= getValueByName("mAdbURL");
    
    mcd.gbid2LocusLinkURL= getValueByName("gbid2LocusLinkURL");
    mcd.locusLinkURL= getValueByName("locusLinkURL");
    
    mcd.genBankAccURL= getValueByName("genBankAccURL");
    if(mcd.genBankAccURL==null)
      mcd.genBankAccURL= getValueByName("geneBankAccURL");
    
    mcd.IMAGE2unigeneURL= getValueByName("IMAGE2unigeneURL");
    
    mcd.IMAGE2GenBankURL= getValueByName("IMAGE2GenBankURL");
    if(mcd.IMAGE2GenBankURL==null)
      mcd.IMAGE2GenBankURL= getValueByName("IMAGE2GenBankURL");
    
    mcd.IMAGE2GidURL= getValueByName("IMAGE2GIDURL");
    
    mcd.geneCardURL= getValueByName("GeneCardURL");
    mcd.histologyURL= getValueByName("histologyURL");
    mcd.modelsURL= getValueByName("modelsURL");
    
    mcd.swissProtURL= getValueByName("swissProtURL");
    mcd.pirURL= getValueByName("pirURL");
    mcd.medMinerURL= getValueByName("medMinerURL");
    mcd.medMinerURLepilogue= getValueByName("medMinerURLepilogue");
    
    /* [4] Get parameters of names of things */
    mcd.database= getValueByName("dataBase");
    mcd.dbSubset= getValueByName("dbSubset");
    mcd.maAnalysisProg= getValueByName("maAnalysisProgram");
    mcd.classNameX= getValueByName("classNameX");
    mcd.classNameY= getValueByName("classNameY");
    mcd.calibDNAname= getValueByName("calibDNAname");
    mcd.yourPlateName= getValueByName("yourPlateName");
    mcd.emptyWellName= getValueByName("emptyWellName");
    mcd.gipoFile= getValueByName("gipoFile");
    mcd.samplesDBfile= getValueByName("samplesDBfile");
    mcd.quantFileExt= getValueByName("quantFileExt");
    
    
    /* [5] Get scalar and boolean parameters values */
    mcd.useCy5OverCy3Flag= getBoolValueByName("useCy5/Cy3");
    mcd.useMouseOverFlag= getBoolValueByName("useMouseOver");
    mcd.swapRowsColsFlag= getBoolValueByName("swapRowsColumns");
    mcd.useRatioDataFlag= getBoolValueByName("useRatioData");
    mcd.bkgdCorrectFlag= getBoolValueByName("useBackgroundCorrection");
    mcd.ratioMedianCorrectionFlag= getBoolValueByName("useRatioMedianCorrection");
    mcd.allowNegQuantDataFlag= getBoolValueByName("allowNegQuantDataFlag");
    
    mcd.usePseudoXYcoordsFlag= getBoolValueByName("usePseudoXYcoords");
    mcd.reuseXYcoordsFlag= getBoolValueByName("reuseXYcoords");
    mcd.maxGenesToRpt= util.cvs2i(getValueByName("maxClonesReported"), 0);
    mcd.presentViewFlag= getBoolValueByName("presentationViewFlag");
    
    /* [6] Get optional PARAMs */
    // take out mcd.fontFamily= getValueByName("fontFamily");
    mcd.fluorescentLbl1= getValueByName("flourescentLbl1");
    mcd.fluorescentLbl2= getValueByName("flourescentLbl2");
    
    /* Handle incorrect spelling problem... */
    if(mcd.fluorescentLbl1==null)
      mcd.fluorescentLbl1= getValueByName("fluorescentLbl1");
    if(mcd.fluorescentLbl2==null)
      mcd.fluorescentLbl2= getValueByName("fluorescentLbl2");
    
    mcd.ignoreExtraFIELDS= getBoolValueByName("ignoreExtraFields");
    
    return(true);
  } /* getConfigValuesFromTable */
  
  
  /**
   * windowClosing() - close down the window.
   * @param e WindowEvent
   */
  public void windowClosing(WindowEvent e)
  {
    cancel();                  /* cancel the editor window */
  }
  
  /* Others not used at this time */
  public void windowOpened(WindowEvent e)  { }
  public void windowActivated(WindowEvent e)  { }
  public void windowClosed(WindowEvent e)  { }
  public void windowDeactivated(WindowEvent e)  { }
  public void windowDeiconified(WindowEvent e)  { }
  public void windowIconified(WindowEvent e)  { }
  
} /* EditMaeProjectFiles class */

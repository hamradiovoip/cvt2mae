/** File: CvtGUI.java */

package cvt2mae;

import java.awt.*;
import java.awt.event.*;
import java.awt.List;
import java.io.*;
import java.util.*;

/* Class to build GUI and start the program. It also contains an event handler.
 *
 *<P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), B. Stephens(SAIC), G. Thornwall (SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $   $Revision: 1.24 $ 
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class CvtGUI extends Frame implements ActionListener, ItemListener, WindowListener
{
  /** Cvt2Mae instance */
  public Cvt2Mae
    cvt;                         
  /** MaeConfigData instance */
  public MaeConfigData		
    mcd;                         
  /** Used for defaults when Editing MCD */
  public MaeConfigData		
    origMcd;                     
  /** SetupLayouts instance */
  public SetupLayouts
    sul;                           
  /** Utilities instance */
  public UtilCM
    util;                       
  /** FileTable instance */
  public FileTable			
    ft;			     
  /** GUI width of application Frame window (generic and for PC) */
  final public static int
    GUI_WIDTH= 680;          
  /** GUI height of application Frame window (generic and for PC) */
  final public static int
    GUI_HEIGHT= 730;          
  /** GUI width of application Frame window (Sun) */
  final public static int
    SUN_GUI_WIDTH= 870;	
  /**  GUI height of application Frame window (Sun) */
  final public static int
    SUN_GUI_HEIGHT= 830;
  /** max number of samples */
  final public static int
    MAX_SAMPLE_SIZE= 1000;
  /** max number of fields */
  final public static int
    MAX_FIELD_SIZE= 200;
  /** Source array data input file browser */  
  public FileDialog
    fdI;
  /** FileDialog MAE output directory browser */ 
  public FileDialog		
    fdO;
  /** Background color */
  public static final Color
    A_BG_COLOR= Color.gray;
  /** Background color */
  public static final Color
    F_BG_COLOR= Color.white;
  /** Background color */
  public static final Color
    P_BG_COLOR= Color.lightGray;
  /** Menu font */   
  public static final Font 
    menu_font= new Font("Serif", Font.BOLD+Font.ITALIC,14);
  /** default font */
  public static final Font 
    default_font= new Font("SansSerif", Font.BOLD+Font.ITALIC,12);
  /** button font */
  public static final Font 
    button_font= new Font("Serif", Font.BOLD,12);
  /** Button font */
  public static final Font 
    text_font= new Font("MonoSpaced", Font.BOLD,12);
  /** Window title */
  public String
    title;
  /** Popup text dialog */
  public PopupTextDialog
    ptd;    
  /** Main window layout */
  public GridBagLayout
    layout;
  /** GridBagConstraints */
  public GridBagConstraints 
    constraints;
  /** "Remove Array Layout" button to remove old or bogus entries */
  public Button
    removeAloButton;
  /** "Save Layout" button to save current array layout just edited to files */
  public Button 
    saveArrayLayoutButton;    
  /** "Run" the converter button */
  public Button 
    runButton;
  /** "Edit Layout" edit project button */
  public Button 
    editLayoutButton;          
  /** "Assign GIPO fields" button  to map user to MAE GIPO fields */
  public Button 
    mapGipoFieldsButton;     
  /** "Assign Quant fields" button to map user to MAE Quant fields */
  public Button 
    mapQuantFieldsButton;     
  /** "Abort" or "Done" button to stop Cvt2Mae */
  public Button 
    exitButton;   
  /** "Reset" button, resets everything in the GUI main window */
  public Button 
    resetButton;        
  /** "Browse input file name" browse to where source file is button */
  public Button 
    browseSrcFileButton;        
  /** "Browse input GIPO file name" browse to where file is button */
  public Button 
    browseGIPOsrcFileButton; 
  /** Edit the field names for sep gipo button */
  public Button 
    editSepGIPOfieldsButton;
  /** "Analyze files" if <User-defined> button */
  public Button 
    analyzeSrcFilesButton; 
  /** "Remove sample" file from samples list button */
  public Button 
    removeSampleButton;       
  /** "Rename sample" quantName(File)[] entry button */
  public Button 
    renameSampleButton;        
  /** "Update Cvt2Mae button */
  public Button 
    updateCvt2MaeButton;   
  /** ("Output Window") */
  public TextFrame 
    out= null;
  /** ("Error Window") */
  public TextFrame 
    err= null;  
  /** list for showing input files... */
  public List
    inFilesList;
  /** input samples (i.e. .quant) list */
  public List
    inSamplesList;  
  /** list status and error messages here */
  public TextField
    statusText;              
  /** list additional message 2 here */
  public TextField
    status2Text;              
  /** list additional message 3 here */
  public TextField
    status3Text;              
  /** full output project directory name */ 
  public TextField
    prjDirText;              
  /** full .mae startup file path name */ 
  public TextField
    maeNameText;     
  /** Generic space holder */     
  public Label 
    spaceLabel;
  /** space holder for rebuilding the scanMultSampPanel */     
  public Label           
    smspSpaceLabel1;
  /** space holder for rebuilding the scanMultSampPanel */     
  public Label           
    smspSpaceLabel2;
  /** 2.0 Select Input label*/
  public Label 
    fileNameLabel;		
  /** 2.1 Samples to uselabel */
  public Label 
    scanMultSampLabel;		
  /** 1.0 select chip set */
  public Label 
    chipsetLabel;		
  /** 1.1 Select Quant. software */
  public Label 
    imgAnalyLabel;		
  /** MAExplorer startup File */
  public Label 
    maeOutLabel;	
  /** Project output folder */
  public Label 
    prjDirLabel;		
  /** 3. Select Project Output */
  public Label 
    makPrjLabel;	
  /** label for contents of ChipSet */     
  public Label
    vendorLbl;               
  /** Layout name */
  public Label
    layoutNameLbl;	
  /** Spots/microarray */
  public Label
    maxRowsExpectedLbl;	
  
  /** values contents of ChipSet */
  public TextField
    vendorText;       
  /** layout name text*/
  public TextField
    layoutNameText;
  /** maximum rows expected text */
  public TextField
    maxRowsExpectedText;
  /** use maximum fields for mapTF gui */
  public Checkbox
    useMaxFieldsCB;
  /** use multiple samples/file in analyzing files */
  public Checkbox
    multSamplesPerFileCB;;
  /** separate GIPO and Quant input data files */
  public Checkbox
      sepGipoQuantFilesCB;    
  /** browser input data panel */
  public Panel
    browserPanel;
  /** panel to hold changable chipsetChoice */
  public Panel
    chipsetChoicePanel;
  /** panel to hold "Edit Layout", "Saveas Layout" buttons */
  public Panel
    aloPanel;	
  /** panel to hold  "Assign GIPO fields" and "Assign QUANT fields" buttons */
  public Panel
    mapFieldsPanel;	
  /** control panel holding aloLayout and mapFieldsPanel for editing */
  public Panel
    ctrlUserDefPanel;
  /** panel to hold list of samples and controls for editing it */
  public Panel
    scanMultSampPanel;
  /** panel to hold the layout */
  public Panel
    layoutPanel;
  /** choice of chipset to use: mouse, human etc, */
  public Choice
    chipsetChoice;        
  /** choice of type of output project */
  public Choice
    prjChoice;        
  /** choice of image analysis pkg used to generate quantification files if
   * they are NOT part of the input file.  
   */
  public Choice
    imgAnalysisChoice;         
  /** */
  public String
    dName;
  /** */
  public String  
    fName;
  
  /**
   * CvtGUI() - Constructor to build GUI
   * @param cvt is the Cvt2Mae instance
   * @param title window title
   * @see FileTable
   * @see #buildGUI
   */
  public CvtGUI(Cvt2Mae cvt,  String title)
  { /* CvtGUI */
    super(title);
    
    this.title= title;
    this.cvt= cvt;
    
    mcd= cvt.mcd;
    sul= cvt.sul;
    util= cvt.util;
    
    ft= new FileTable("CvtGUI");      /* instance just used  by CvtGUI */
    
    buildGUI();
  } /* CvtGUI */
  
  
  /**
   * getChipsetStr() - get the chipset string if selected, else return null
   * @return chip set choice string else null if not found
   */
  public String getChipsetStr()
  { /* getChipsetStr */
    if(chipsetChoice!=null)
      return(chipsetChoice.getSelectedItem());
    return(null);
  } /* getChipsetStr */
  
  
  /**
   * getProjectStr() - get the project string if selected else return null
   * @return project string if found, else null
   */
  public String getProjectStr()
  { /* getProjectStr */
    if(prjChoice!=null)
      return(prjChoice.getSelectedItem());
    return(null);
  } /* getProjectStr */
  
  
  /**
   * buildGUI() - build Cvt2Mae Graphical User Interface (GUI)
   * @see PopupTextDialog
   * @see #drawGUI
   */
  public void buildGUI()
  { /* buildGUI */
    if(cvt.isWinPCflag)
      setSize(GUI_WIDTH,GUI_HEIGHT);  /* Set the GUI Frame size */
    else
      setSize(SUN_GUI_WIDTH,SUN_GUI_HEIGHT);
    
    layout= new GridBagLayout();
    constraints= new GridBagConstraints();
    constraints.fill= GridBagConstraints.BOTH;
    setLayout(layout);
    
    setBackground(A_BG_COLOR);
    setFont(default_font);
    
    /* For possible popup text dialog boxes */
    Frame
      ptdFrame= new Frame();
      ptd= new PopupTextDialog(this,ptdFrame);
    
    drawGUI();         /* build the gui */
    
    Point pos= new Point(50,50);   /* make slightly off ULHC */
    this.setLocation(pos);
    
    setVisible(true);
  } /* BuildGUI */
  
  
  /**
   * addComponent() - add component to the GridBag layout
   * @param obj component
   * @param x holds the x-coordinate cell location for the component
   * @param y holds the y-coordinate cell location for the component
   * @param xw holds the component's width in cells
   * @param yw holds the component's height in cells
   * @param xg holds the component's column weight
   * @param yg holds the component's row weight
   */
  public void addComponent(Component obj, int x, int y, int xw,int yw,
                           int xg, int yg)
  { /* addComponent */
    constraints.gridx= x;
    constraints.gridy= y;
    constraints.gridwidth= xw;
    constraints.gridheight= yw;
    constraints.weightx= xg;
    constraints.weighty= yg;
    layout.setConstraints(obj,constraints);
    add(obj);
  } /* addComponent */
  
  
  /**
   * drawGUI() - build the GUI and set up event listeners.
   * @see UtilCM#setOutErrTextFrames
   * @see UtilCM#setStatusTextFrames
   * @see #setChipValuesGUI
   */
  public void drawGUI()
  { /* drawGUI */
    int guiRow= 0;
    String spaces= "                                                     ";
    
    if(cvt.USE_LOG_WINDOW)
    {
      out= new TextFrame(cvt,"Output Window");
      err= new TextFrame(cvt,"Error Window");
    }
    
    /* [1] Handle close window events */
    this.addWindowListener(this);  /* so activate window controls*/
    
    /* [1.1] Build floating panels that may appear in different parts
     * of the window based on the data being analyzed.
     */
    
    /* [1.1.1] Combine [editLayoutButton & writeArrayButton] in aloPanel */
    aloPanel = new Panel(new GridLayout(2,1));
    editLayoutButton= new Button("Edit Layout");
    editLayoutButton.setBackground(Color.cyan);      /* P_BG_COLOR */
    editLayoutButton.addActionListener(this);
    aloPanel.add(editLayoutButton);
    editLayoutButton.setEnabled(false);
    
    saveArrayLayoutButton= new Button("Save Layout");
    saveArrayLayoutButton.setBackground(P_BG_COLOR);
    saveArrayLayoutButton.addActionListener(this);
    saveArrayLayoutButton.setEnabled(false);
    aloPanel.add(saveArrayLayoutButton);
    
    /* [1.1.2] Combine ["Assign GIPO fields" and "Assign QUANT fields"]
     * buttons inside of mapFieldsPanel
     */
    mapFieldsPanel= new Panel(new GridLayout(2,2,1,1)); /* (2,3,1,1) */
    mapFieldsPanel.setBackground(F_BG_COLOR);
    
    mapGipoFieldsButton= new Button("Assign GIPO fields");
    mapGipoFieldsButton.setBackground(Color.cyan); /* P_BG_COLOR */
    mapGipoFieldsButton.addActionListener(this);
    mapFieldsPanel.add(mapGipoFieldsButton);
    mapGipoFieldsButton.setEnabled(cvt.enableMapQueryFlag);
    
    mapQuantFieldsButton= new Button("Assign Quant fields");
    mapQuantFieldsButton.setBackground(Color.cyan); /* P_BG_COLOR */
    mapQuantFieldsButton.addActionListener(this);
    mapFieldsPanel.add(mapQuantFieldsButton);
    mapQuantFieldsButton.setEnabled(cvt.enableMapQueryFlag);
    
    useMaxFieldsCB= new Checkbox("Expert assign-mode",
    cvt.useMaxFieldsRequiredFlag);
    useMaxFieldsCB.addItemListener(this);
    mapFieldsPanel.add(useMaxFieldsCB);
    
    /* [2] Create GUI for main window */
    spaceLabel= new Label("Enter data for steps 1, 2, and 3. Then 4. press "+
    "'Run' to convert your data to MAExplorer format.",
    Label.CENTER);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow++,2,1,1,1);
    
    chipsetLabel= new Label(" 1. Select Chipset: ",Label.LEFT);
    chipsetLabel.setBackground(F_BG_COLOR);
    addComponent(chipsetLabel,0,guiRow,1,1,1,1);
    
    chipsetChoicePanel= new Panel();
    chipsetChoice= new Choice();
    chipsetChoicePanel.add(chipsetChoice);  /* since chipset may change */
    chipsetChoice.setBackground(F_BG_COLOR);
    chipsetChoice.add("-- select a chip layout --");
    /* [NOTE]: MUST have the layout list setup before get here!!! */
    for(int i=0;i<sul.maxAL;i++)
    {
      String aloName= sul.alList[i].layoutName;
      chipsetChoice.add(aloName);      /* note: variable #*/
    }
    sul.useAL= 0;                                       /* default */
    chipsetChoice.select("-- select a chip layout --"); /* default */
    chipsetChoice.addItemListener(this);
    
    removeAloButton= new Button("Remove Layout");
    removeAloButton.setBackground(P_BG_COLOR);
    removeAloButton.addActionListener(this);
    removeAloButton.setEnabled(true);
    chipsetChoicePanel.add(removeAloButton);
    
    layoutPanel= new Panel(new FlowLayout());
    layoutPanel.setBackground(F_BG_COLOR);
    addComponent(layoutPanel,1,guiRow++,1,1,1,1);
    
    layoutPanel.add(chipsetChoicePanel);
    
    spaceLabel= new Label("",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    layoutPanel.add(spaceLabel);
    
    /* ---------------------- */
    
    spaceLabel= new Label("",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow++,2,1,1,1);
    
    /* ---------------------- */
    if(cvt.USE_IAM_FLAG)
    { /* Add Image Analysis Method GUI */
      imgAnalyLabel= new Label(" 1.1 (opt.) Select Quant. software used: ",
                               Label.LEFT);
      imgAnalyLabel.setBackground(F_BG_COLOR);
      addComponent(imgAnalyLabel,0,guiRow,1,1,1,1);
      
      imgAnalysisChoice= new Choice();
      imgAnalysisChoice.setBackground(F_BG_COLOR);
      String iamStr;
      for(int i=0;i<sul.maxIAM;i++)
      {
        iamStr= "["+sul.iam[i].vendor+"] "+
        sul.iam[i].quantTool+" ("+
        sul.iam[i].spotCoding+")";
        imgAnalysisChoice.add(iamStr);           /* note: variable # */
      }
      sul.useIAM= 0;                              /* default */
      iamStr= "["+sul.iam[sul.useIAM].vendor+"] "+
      sul.iam[sul.useIAM].quantTool+" ("+
      sul.iam[sul.useIAM].spotCoding+")";
      imgAnalysisChoice.select(iamStr);           /* default */
      imgAnalysisChoice.addItemListener(this);
      imgAnalysisChoice.setEnabled(false);
      addComponent(imgAnalysisChoice,1,guiRow++,1,1,1,1);
      
      /* ---------------------- */
      
      spaceLabel= new Label("",Label.LEFT);
      spaceLabel.setBackground(F_BG_COLOR);
      addComponent(spaceLabel,0,guiRow++,2,1,1,1);
    } /* Add Image Analysis Method GUI */
    
    /* ---------------------- */
    
    fileNameLabel= new Label(" 2. Select Input Data Files: ",Label.LEFT);
    fileNameLabel.setBackground(F_BG_COLOR);
    addComponent(fileNameLabel,0,guiRow,1,1,1,1);
    
    browserPanel= new Panel(new FlowLayout());
    browserPanel.setBackground(F_BG_COLOR);
    addComponent(browserPanel,1,guiRow++,1,1,1,1);
    
    browseSrcFileButton= new Button("Browse input file name");
    browseSrcFileButton.setBackground(P_BG_COLOR);
    browseSrcFileButton.addActionListener(this);
    browseSrcFileButton.setEnabled(false);   /* set when select Chip */
    browserPanel.add(browseSrcFileButton);
    
    sepGipoQuantFilesCB= new Checkbox("Separate GIPO");
    sepGipoQuantFilesCB.setBackground(P_BG_COLOR);
    sepGipoQuantFilesCB.addItemListener(this);
    sepGipoQuantFilesCB.setState(mcd.hasSeparateGIPOandQuantFilesFlag);
    sepGipoQuantFilesCB.setEnabled(false);
    browserPanel.add(sepGipoQuantFilesCB);
    
    browseGIPOsrcFileButton= new Button("Browse GIPO file");
    browseGIPOsrcFileButton.setBackground(P_BG_COLOR);
    browseGIPOsrcFileButton.addActionListener(this);
    browseGIPOsrcFileButton.setEnabled(mcd.hasSeparateGIPOandQuantFilesFlag);
    browserPanel.add(browseGIPOsrcFileButton);
    
    editSepGIPOfieldsButton= new Button("Edit separate GIPO Fields");
    editSepGIPOfieldsButton.setBackground(P_BG_COLOR);
    editSepGIPOfieldsButton.addActionListener(this);
    editSepGIPOfieldsButton.setEnabled(mcd.hasSeparateGIPOandQuantFilesFlag);
    browserPanel.add(editSepGIPOfieldsButton);
    
    /* Will add "[x] Mult samples/file" and "Analyze" buttons later
     * if select "<User-defined">  chip layout
     */
    multSamplesPerFileCB= null;
    analyzeSrcFilesButton= null;
    
    /* ---------------------- */
    
    inFilesList= new java.awt.List(4);
    inFilesList.setBackground(F_BG_COLOR);
    addComponent(inFilesList,0,guiRow++,2,1,1,1);
    
    /* ---------------------- */
    
    spaceLabel= new Label(" 2.1 Edit array layout and map fields: ",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow,1,1,1,1);
    
    /* Allow editing for data using aloLayout and mapFieldsPanel */
    ctrlUserDefPanel= new Panel(new FlowLayout());
    ctrlUserDefPanel.setBackground(F_BG_COLOR);
    addComponent(ctrlUserDefPanel,1,guiRow++,1,1,1,1);
    
    /* Add aloPanel [editLayoutButton and writeArrayButton] */
    ctrlUserDefPanel.add(aloPanel);
    spaceLabel= new Label("",Label.LEFT);
    ctrlUserDefPanel.add(spaceLabel);
    
    /* Add mapFieldsPanel [mapGipoFieldsButton and mapQuantFieldsButton] */
    ctrlUserDefPanel.add(mapFieldsPanel);
    spaceLabel= new Label("",Label.LEFT);
    ctrlUserDefPanel.add(spaceLabel);
    
    /* ---------------------- */
    
    /* Scan for multiple samples in the same file */
    scanMultSampLabel= new Label(" 2.2 Samples to use '<<file>> sample name' : ",
    Label.LEFT);
    scanMultSampLabel.setBackground(F_BG_COLOR);
    addComponent(scanMultSampLabel,0,guiRow,1,1,1,1);
    
    scanMultSampPanel= new Panel(new FlowLayout());
    scanMultSampPanel.setBackground(F_BG_COLOR);
    addComponent(scanMultSampPanel,1,guiRow++,1,1,1,1);
    
    removeSampleButton= new Button("Remove sample");
    removeSampleButton.setBackground(P_BG_COLOR);
    removeSampleButton.addActionListener(this);
    removeSampleButton.setEnabled(false);
    
    renameSampleButton= new Button("Rename sample");
    renameSampleButton.setBackground(P_BG_COLOR);
    renameSampleButton.addActionListener(this);
    renameSampleButton.setEnabled(false);
    
    spaceLabel= new Label("",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    scanMultSampPanel.add(spaceLabel);
    scanMultSampPanel.add(removeSampleButton);
    scanMultSampPanel.add(renameSampleButton);
    
    /* ---------------------- */
    inSamplesList= new List(4);
    inSamplesList.setBackground(F_BG_COLOR);
    addComponent(inSamplesList,0,guiRow++,2,1,1,1);
    
    /* ----------------------- */
    
    spaceLabel= new Label("",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow,1,1,1,1);
    
    Panel
    chipValuesPanel= new Panel();
    chipValuesPanel.setBackground(F_BG_COLOR);
    addComponent(chipValuesPanel,1,guiRow++,1,1,1,1);
    
    setChipValuesGUI(chipValuesPanel);
    
    /* ----------------------- */
    
    spaceLabel= new Label("",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow++,2,1,1,1);
    
    /* ----------------------- */
    
    makPrjLabel= new Label(" 3. Select Project Output Folder: ",
    Label.LEFT);
    makPrjLabel.setBackground(F_BG_COLOR);
    addComponent(makPrjLabel,0,guiRow,1,1,1,1);
    
    prjChoice= new Choice();
    prjChoice.setBackground(F_BG_COLOR);
    prjChoice.add("--Select Output Folder--");
    prjChoice.add("Create New project folder");
    prjChoice.add("Merge with Existing project folder");
    prjChoice.add("Use Input folder for output files");
    prjChoice.select("--Select Output Folder--");
    prjChoice.addItemListener(this);
    addComponent(prjChoice,1,guiRow++,1,1,1,1);
    
    /* ------------------------- */
    
    spaceLabel= new Label("",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow++,2,1,1,1);
    
    /* ------------------------ */
    
    prjDirLabel= new Label("Project output folder: ",Label.RIGHT);
    prjDirLabel.setBackground(F_BG_COLOR);
    addComponent(prjDirLabel,0,guiRow,1,1,1,1);
    
    prjDirText= new TextField("", 50);
    prjDirText.setBackground(F_BG_COLOR);
    prjDirText.setEditable(false);
    addComponent(prjDirText,1,guiRow++,1,1,1,1);
    
    /* -------------------------- */
    
    maeOutLabel= new Label("MAExplorer startup File: ",Label.RIGHT);
    maeOutLabel.setBackground(F_BG_COLOR);
    addComponent(maeOutLabel,0,guiRow,1,1,1,1);
    
    maeNameText= new TextField("",50);
    maeNameText.setBackground(F_BG_COLOR);
    maeNameText.setEditable(false);
    addComponent(maeNameText,1,guiRow++,1,1,1,1);
    
    /* ---------------------------- */
    
    spaceLabel= new Label("",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow++,2,1,1,1);
    
    /* ---------------------------- */
    
    
    spaceLabel= new Label("4. Edit and Run                 ", Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow,1,1,1,1);
    
    Panel
    ctrlPanel= new Panel(new FlowLayout());
    ctrlPanel.setBackground(F_BG_COLOR);
    addComponent(ctrlPanel,1,guiRow++,1,1,1,1);
    
    runButton= new Button("Run - do conversion");
    runButton.setBackground(Color.green);        /* P_BG_COLOR */
    runButton.addActionListener(this);
    runButton.setEnabled(false);
    ctrlPanel.add(runButton);
    
    spaceLabel= new Label("                                                ",Label.LEFT);
    ctrlPanel.add(spaceLabel);
    
    /* Put Abort/Done and Reset in same horizontal panel */
    Panel exitResetPanel= new Panel(new GridLayout(1,2));
    exitResetPanel.setBackground(F_BG_COLOR);
    ctrlPanel.add(exitResetPanel);
    
    exitButton= new Button("Abort");
    exitButton.setBackground(Color.red); /* P_BG_COLOR */
    exitButton.addActionListener(this);
    exitResetPanel.add(exitButton);
    
    resetButton= new Button("Reset");
    resetButton.setBackground(Color.red); /* P_BG_COLOR */
    resetButton.addActionListener(this);
    exitResetPanel.add(resetButton);
    
    /* ---------------------- */
    
    spaceLabel= new Label("",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow++,2,1,1,1);
    
    /* -------------------------- */
    
    Panel bottomPanel= new Panel();
    bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    bottomPanel.setBackground(F_BG_COLOR);
    spaceLabel= new Label("",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    bottomPanel.add(spaceLabel);
    updateCvt2MaeButton= new Button("Update Cvt2Mae");
    updateCvt2MaeButton.setBackground(Color.lightGray); /* P_BG_COLOR */
    updateCvt2MaeButton.addActionListener(this);
    bottomPanel.add(updateCvt2MaeButton);
    
    spaceLabel= new Label("Status:  ",Label.RIGHT);
    spaceLabel.setBackground(F_BG_COLOR);
    bottomPanel.add(spaceLabel);
    addComponent(bottomPanel,0,guiRow,1,1,1,1);
    
    statusText= new TextField("", 50);
    statusText.setBackground(F_BG_COLOR);
    statusText.setForeground(Color.red);
    addComponent(statusText,1,guiRow++,1,1,1,1);
    
    /* -------------------------- */
    
    spaceLabel= new Label("",Label.RIGHT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow,1,1,1,1);
    
    status2Text= new TextField("", 50);
    status2Text.setBackground(F_BG_COLOR);
    status2Text.setForeground(Color.red);
    addComponent(status2Text,1,guiRow++,1,1,1,1);
    
    /* -------------------------- */
    
    spaceLabel= new Label("",Label.RIGHT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow,1,1,1,1);
    
    status3Text= new TextField("", 50);
    status3Text.setBackground(F_BG_COLOR);
    status3Text.setForeground(Color.red);
    addComponent(status3Text,1,guiRow++,1,1,1,1);
    
    /* ---------------------- */
    
    spaceLabel= new Label("",Label.LEFT);
    spaceLabel.setBackground(F_BG_COLOR);
    addComponent(spaceLabel,0,guiRow++,2,1,1,1);
    
    /* Update Utility methods */
    util.setStatusTextFrames(statusText, status2Text, status3Text);
    util.setOutErrTextFrames(out, err);
  } /* drawGUI */
  
  
  /**
   * rebuildSrcBrowserPanels() - rebuild it with or without Analyze button
   * "Analyze for Mult samples/File" button
   * @param addAnalyzeButtonFlag
   **/
  public void rebuildSrcBrowserPanels(boolean addAnalyzeButtonFlag)
  { /* rebuildSrcBrowserPanels */
    
    /* [1] Remove buttons so can rebuild again */
    if(multSamplesPerFileCB!=null)
      browserPanel.remove(multSamplesPerFileCB);
    if(smspSpaceLabel1!=null)
      scanMultSampPanel.remove(smspSpaceLabel1);
    if(analyzeSrcFilesButton!=null)
    {
      scanMultSampPanel.remove(analyzeSrcFilesButton);
      analyzeSrcFilesButton= null;      /* for next time */
      if(smspSpaceLabel2!=null)
        scanMultSampPanel.remove(smspSpaceLabel2);
    }
    scanMultSampPanel.remove(removeSampleButton);
    scanMultSampPanel.remove(renameSampleButton);
    
    /* [2] Rebuild browserPanel */
    if(addAnalyzeButtonFlag)
    { /* add reanalyze components */
      multSamplesPerFileCB= new Checkbox("Mult. samples/file");
      multSamplesPerFileCB.setBackground(P_BG_COLOR);
      multSamplesPerFileCB.addItemListener(this);
      multSamplesPerFileCB.setState(mcd.hasMultDatasetsFlag);
      browserPanel.add(multSamplesPerFileCB);
    } /* add reanalyze components */
    
    /* [3] Rebuild scanMultSampPanel */
    /* Need a spacer. Do we need to remove that first as well or just leave it there? */
    smspSpaceLabel1= new Label("",Label.LEFT);
    smspSpaceLabel1.setBackground(F_BG_COLOR);
    
    smspSpaceLabel2= new Label("",Label.LEFT);
    smspSpaceLabel2.setBackground(F_BG_COLOR);
    
    scanMultSampPanel.add(smspSpaceLabel1);
    
    if(addAnalyzeButtonFlag)
    { /* add reanalyze components */
      saveArrayLayoutButton.setEnabled(false);
      
      analyzeSrcFilesButton= new Button("Analyze input files");
      analyzeSrcFilesButton.setBackground(P_BG_COLOR);
      analyzeSrcFilesButton.addActionListener(this);
      mcd.activeReanalyeButtonFlag= (mcd.didEditALOflag &&
      mcd.didGIPOassignFlag &&
      mcd.didQuantFlag &&
      mcd.hasMultDatasetsFlag);
      analyzeSrcFilesButton.setEnabled(mcd.activeReanalyeButtonFlag);
      
      scanMultSampPanel.add(analyzeSrcFilesButton);
      scanMultSampPanel.add(smspSpaceLabel2);
      /* Need a spacer. Do we need to remove that first as well or just leave it there? */
    } /* add reanalyze components */
    
    removeSampleButton= new Button("Remove sample");
    removeSampleButton.setBackground(P_BG_COLOR);
    removeSampleButton.addActionListener(this);
    removeSampleButton.setEnabled(true);
    scanMultSampPanel.add(removeSampleButton);
    
    renameSampleButton= new Button("Rename sample");
    renameSampleButton.setBackground(P_BG_COLOR);
    renameSampleButton.addActionListener(this);
    renameSampleButton.setEnabled(true);
    scanMultSampPanel.add(renameSampleButton);
    
    /* [4] Now do the actual repacking of the panels */
    pack();
    repaint();
  } /* rebuildSrcBrowserPanels */
  
  
  /**
   * setOutputPathByDirBrowser() - set the output path by dir. browser
   * @see DataIO#setupFilenames
   * @see UtilCM#logMsg
   */
  public void setOutputPathByDirBrowser()
  { /* setOutputPathByDirBrowser */
    fdO= new FileDialog(this, "Select the Project Folder to save converted data");
    if(cvt.USE_DBUG_DIR)
    { /* ONLY when debugging */
      mcd.existingPrjDir= cvt.DBUG_OUT_DIR;
      fdO.setDirectory(mcd.existingPrjDir);
    } /* ONLY when debugging */
    
    fdO.setMode(FileDialog.SAVE);
    fdO.setFile("Select Project Folder - then press 'Save'");
    if(cvt.DBUG_FLAG)
      util.logMsg("C2M-SOPBDB fdO.getDirectory()="+fdO.getDirectory());
    fdO.setVisible(true);
    
    mcd.existingPrjDir= fdO.getDirectory();  /* get selected results */
    
    if(mcd.existingPrjDir==null)
      return;                               /* nothing selected */
    
    cvt.dio.setupFilenames();               /* setup proper file names
                                             * based on path analysis */
    
    /* Update text areas */
    prjDirText.setText(mcd.maePrjDir);
    maeNameText.setText(mcd.maeFile);
    
    repaint();
  } /* setOutputPathByDirBrowser */
  
  
  /**
   * setChipValuesGUI() - if chipValuesPanel is not null, create grid contents else just update it.
   * @param cvp panel to update
   */
  public void setChipValuesGUI(Panel cvp)
  { /* setChipValuesGUI */
    if(cvp!=null)
    { /* create new grid contents */
      int
        maxChars= 25,
        nRows= 3;  /* [NOTE] increase if adding more */
      
      cvp.setLayout(new GridLayout(nRows,2));
      
      vendorLbl= new Label(" Vendor");
      vendorText= new TextField("",maxChars);
      vendorText.setEditable(false);
      cvp.add(vendorLbl);
      cvp.add(vendorText);
      
      layoutNameLbl= new Label(" Layout name");
      layoutNameText= new TextField("",maxChars);
      layoutNameText.setEditable(false);
      cvp.add(layoutNameLbl);
      cvp.add(layoutNameText);
      
      maxRowsExpectedLbl= new Label(" Spots/microarray");
      maxRowsExpectedText= new TextField("",maxChars);
      maxRowsExpectedText.setEditable(false);
      cvp.add(maxRowsExpectedLbl);
      cvp.add(maxRowsExpectedText);
    } /* create new grid contents */
    
  } /* setChipValuesGUI */
  
  
  /**
   * updateChipValuesGUIfromMCD() - update MCD chip layout values to text area GUI
   */
  public void updateChipValuesGUIfromMCD()
  { /* updateChipValuesGUIfromMCD */
    
    /* [1] Update Text areas in GUI so user sees the
     * Array Layout being used.
     */
    if(vendorText!=null)
      vendorText.setText(""+mcd.vendor);
    
    if(layoutNameText!=null)
      layoutNameText.setText(""+mcd.layoutName);
    //layoutNameText.setText(""+mcd.aloFileName);
    
    if(maxRowsExpectedText!=null)
      maxRowsExpectedText.setText(""+mcd.maxRowsExpected);
    
    /* [2] Try to set the quantification method if active in GUI */
    if(cvt.USE_IAM_FLAG)
    { /* Use Image Analysis Method GUI */
      //cvt.selectedImageAnalysisFlag= false;
      for(int i=0;i<sul.maxIAM;i++)
        if(mcd.maAnalysisProg.equals(sul.iam[i].quantTool))
        {
          cvt.selectedImageAnalysisFlag= true;
          sul.useIAM= i;
          String iamStr= "["+sul.iam[sul.useIAM].vendor+"] "+
                          sul.iam[sul.useIAM].quantTool+" ("+
                          sul.iam[sul.useIAM].spotCoding+")";
          imgAnalysisChoice.select(iamStr); /* default */
          break;
        }
    } /* Use Image Analysis Method GUI */
    repaint();
  } /* updateChipValuesGUIfromMCD */
  
  
  /**
   * saveChipValues() - save edited MCD values to ALO and update GUI text areas.
   * These may have been changed by user during EditMaePrjFiles.
   * @see ArrayLayout
   * @see SetupLayouts#copyMCDtoALOstate
   * @see SetupLayouts#setPseudoArrayToCurMCD
   * @see #updateChipValuesGUIfromMCD
   */
  public void saveChipValues()
  { /* saveChipValues */
    ArrayLayout al= sul.alList[sul.useAL];
    
    /* [1] Update text areas in GUI describing current Array Layout */
    updateChipValuesGUIfromMCD();
    
    /* [2] Estimate a reasonable PseudoArray geometry  and save
     * it in the ALO state.
     */
    if(mcd.maxRowsExpected>0 && mcd.specifyGeometryByNbrSpotsFlag &&
    mcd.nSrcFiles==1)
      sul.setPseudoArrayToCurMCD();
    
    /* [3] Update ALO state from the edited MCD Config state.
     * Copy mcd.XXX state to al[useAL].XXX state.
     * This will let us save the ALO if we want. Also,
     * if we re-edit the ALO, we will copy it back into the
     * MCD before we edit it.
     */
    sul.copyMCDtoALOstate();
    
    /* [4] Try to set the quantification method - not really used now */
    //selectedImageAnalysisFlag= false;
    for(int i=0;i<sul.maxIAM;i++)
      if(mcd.maAnalysisProg.equals(sul.iam[i].quantTool))
      {
        cvt.selectedImageAnalysisFlag= true;
        sul.useIAM= i;
        String iamStr= "["+sul.iam[sul.useIAM].vendor+"] "+
                       sul.iam[sul.useIAM].quantTool+" ("+
                       sul.iam[sul.useIAM].spotCoding+")";
        imgAnalysisChoice.select(iamStr); /* default */
        break;
      }
  } /* saveChipValues */
  
  
  /**
   * itemStateChanged() - event handler for Choices
   * @param e item event
   * @see DataIO#setupFilenames
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   * @see #chkReanlyzeButtonState
   * @see #setOutputPathByDirBrowser
   */
  public void itemStateChanged(ItemEvent e)
  { /* itemStateChanged */
    Object obj= e.getSource();
    Choice itemC= (obj instanceof Choice) ? (Choice)obj : null;
    Checkbox itemCB= (obj instanceof Checkbox) ? (Checkbox)obj : null;
    
    if(itemC==prjChoice)
    { /* setup the output files path */
      String prjChoiceStr= prjChoice.getSelectedItem();
      
      cvt.selectedOutputPathFlag= true;
      browseSrcFileButton.setEnabled(false);      /* disable it */
      sepGipoQuantFilesCB.setEnabled(false);
      browseGIPOsrcFileButton.setEnabled(false);
      editSepGIPOfieldsButton.setEnabled(false);
      removeSampleButton.setEnabled(false);
      renameSampleButton.setEnabled(false);
      editLayoutButton.setEnabled(false);
      saveArrayLayoutButton.setEnabled(false);
      
      if(!prjChoiceStr.startsWith("Use Input"))
      { /* get the full output path by dir. browser */
        setOutputPathByDirBrowser();
      }
      cvt.dio.setupFilenames();          /* setup proper file names
       * based on path analysis */
      prjDirText.setText(mcd.maePrjDir); /* Update text areas */
      maeNameText.setText(mcd.maeFile);
      
      util.logMsg("Now press 'Run' to convert your data to MAExplorer format",
                  Color.black);
      util.logMsg2("",Color.black);
      util.logMsg3("",Color.black);
      runButton.setEnabled(true);
      repaint();
    } /* setup the output files path */
    
    else if(itemC==chipsetChoice)
    { /* change the chip set being used */
      String arrayLayoutStr= chipsetChoice.getSelectedItem();
      util.logMsg("",Color.black);
      util.logMsg2("",Color.black);
      util.logMsg3("",Color.black);
      changeChipLayoutAndCheckState(arrayLayoutStr);
    } /* change the chip set being used */
    
    else if(cvt.USE_IAM_FLAG && itemC==imgAnalysisChoice)
    { /* change the chip set being used */
      String imgAnalyStr= imgAnalysisChoice.getSelectedItem();
      for(int i=0;i<sul.maxIAM;i++)
      { /* search for matching array quantification tool */
        String iamStr= "["+sul.iam[i].vendor+"] "+
                       sul.iam[i].quantTool+" ("+
                       sul.iam[i].spotCoding+")";
        if(imgAnalyStr.equals(iamStr))
        { /* found tool */
          sul.useIAM= i;
          cvt.selectedImageAnalysisFlag= true;
          //updateImgAnalysisValues();
          repaint();
          break;
        } /* found tool */
      } /* search for matching array quantification tool */
    } /* change the chip set being used */
    
    else if(useMaxFieldsCB==itemCB)
    {
      cvt.useMaxFieldsRequiredFlag= itemCB.getState();
    }
    else if(multSamplesPerFileCB==itemCB)
    { /* enable/disable using multiple samples per FieldG input file */
      mcd.hasMultDatasetsFlag= itemCB.getState();
      sul.alList[sul.useAL].hasMultDatasetsFlag= mcd.hasMultDatasetsFlag;
      chkReanlyzeButtonState();
      repaint();
    }
    
    else if(sepGipoQuantFilesCB==itemCB)
    { /* enable/disable using separate GIPO file */
      mcd.hasSeparateGIPOandQuantFilesFlag= itemCB.getState();
      browseGIPOsrcFileButton.setEnabled(mcd.hasSeparateGIPOandQuantFilesFlag);
      // editSepGIPOfieldsButton.setEnabled(mcd.hasSeparateGIPOandQuantFilesFlag);
    }
  } /* itemStateChanged */
  
  
  /**
   * setMapEditing() - enable/disable Map User to MAE fields editing
   * @param flag enables/disables GipoFieldsButton, GipoFieldsButton and enableMapQueryFlag
   */
  public void setMapEditing(boolean flag)
  { /* setMapEditing */
    cvt.enableMapQueryFlag= flag;
    mapGipoFieldsButton.setEnabled(flag);
    mapQuantFieldsButton.setEnabled(flag);
    repaint();
  } /* setMapEditing */
  
  
  /**
   * setEditLayoutEditing() - enable/disable Edit Layout editing
   * @param flag enables/disables the  Edit Layout editing
   */
  public void setEditLayoutEditing(boolean flag)
  { /* setEditLayoutEditing */
    //cvt.enableMapQueryFlag= flag;
    editLayoutButton.setEnabled(flag);
    saveArrayLayoutButton.setEnabled(flag);
    Color salColor= (flag) ? Color.magenta : P_BG_COLOR;
    saveArrayLayoutButton.setBackground(salColor);
    repaint();
  } /* setEditLayoutEditing */
  
  
  /**
   * runProcessData() - convert the input data into output files
   * @see DataIO#readData
   * @see DataIO#writeGipoData
   * @see DataIO#writeQuantData
   * @see MaeConfigData#createMAEproject
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   */
  public void runProcessData()
  { /* runProcessData */
    /* [TODO] If separate GIPO file, then first read & convert it.
     * [DEPRICATE] 1-17-02 PFL. probably not needed
     */
    
    /* [1] Make sure we have all the data we need to proceed */
    if(!cvt.selectedInputFileFlag || !cvt.selectedChipLayoutFlag ||
       !cvt.selectedOutputPathFlag)
    { /* user needs to answer all questions 1,2,3 */
      if(!cvt.selectedChipLayoutFlag)
      {
        util.logMsg("You need to first do   1. Select Chipset",
        Color.red);
        chipsetChoice.setEnabled(true);   /* set to select Chip */
        return;
      }
      
      else if(!cvt.selectedInputFileFlag)
      {
        util.logMsg("You need to first do   2. Select Input Data File",
                    Color.red);
        /* Enable file browser button again */
        browseSrcFileButton.setEnabled(true); /* set to select input file */
        sepGipoQuantFilesCB.setEnabled(true);
        browseGIPOsrcFileButton.setEnabled(mcd.hasSeparateGIPOandQuantFilesFlag);
        // editSepGIPOfieldsButton.setEnabled(mcd.hasSeparateGIPOandQuantFilesFlag);
        return;
      }
      
      else if(!cvt.selectedOutputPathFlag)
      {
        util.logMsg("You need to first do   3. Select Project Output Folder",
                    Color.red);
        prjChoice.setEnabled(true); /* set to select output folder */
        return;
      }
    } /* user needs to answer all questions 1,2,3 */
    
    /* [2] Setup project directory */
    boolean wroteGIPOflag= false;
    String mkPrjStr= prjChoice.getSelectedItem();
    
    editLayoutButton.setEnabled(false);              /* disable buttons */
    saveArrayLayoutButton.setEnabled(false);
    mapGipoFieldsButton.setEnabled(false);
    mapQuantFieldsButton.setEnabled(false);
    prjChoice.setEnabled(false);
    runButton.setEnabled(false);
    
    util.logMsg("===> run",true,Color.black);
    util.logMsg("     reading",true,Color.black);
    
    if(mkPrjStr.startsWith("Create New"))
    { /* make prj dirs, Config/, MAE/ Quant/ & setup files */
      if(!mcd.checkAndMakeMAEdirTree(mcd.maePrjDir))
        return;
    } /* make prj dirs, Config/, MAE/ Quant/ & setup files */
    
    /* [3] Setup proper file names based on path analysis */
    cvt.dio.setupFilenames();
    
    /* [4] Make sure all input files have same # of lines */
    int linesInFirstFile= mcd.linesInFile[0];
    if(linesInFirstFile>0)
      for(int n=1;n<mcd.nSrcFiles;n++)
        if(linesInFirstFile != mcd.linesInFile[n])
        { /* found file with wrong # of lines */
          util.logMsg("Different # of lines in files - aborting.", Color.red);
          util.logMsg2("|"+mcd.quantSrcFile[0]+"|="+linesInFirstFile+
                       " |"+mcd.quantSrcFile[n]+"|="+linesInFirstFile,
                       Color.red);
          return;
        } /* found file with wrong # of lines */    
    
      /* [5] [TODO] If separate GIPO file, then first read & convert it.
       * [DEPRICATE] 1-17-02 PFL. probably not needed
       */
    if(!mcd.gipoInGeneralSrcFileFlag)
    { /* convert separate GIPO file */
      
    } /* convert separate GIPO file */    
    
    /* [6] Write the nQuantFiles Quant files. In addition, create the
     * GIPO file on the first input file. Then convert data read
     * from each input file into the .quant files. Note that if
     * if there are multiple samples in the same srcFile, it
     * will be read multiple times since it is specified by srcFile[n].
     */
    for(int n=0;n<mcd.nQuantFiles;n++)
    { /* process source files */
      if (!cvt.dio.readData(n))
        continue;         /* problem with the file */
      
      if(cvt.DBUG_FLAG)
      {
        ArrayLayout al= cvt.sul.alList[sul.useAL];
        System.out.println("\nCG.RPD[6] sul.useAL="+sul.useAL+" n="+n+
                           " quantDstFile[n]="+mcd.quantDstFile[n]+
                           "\n mcd.nSrcFiles="+mcd.nSrcFiles+
                           " mcd.nQuantFiles="+mcd.nQuantFiles);
        System.out.println("CG.RPD.2 sul.alList[useAL]="+ al.toString());
        System.out.println("CG.RPD.3 cvt.mcd.fm="+cvt.mcd.fm.toString());
      }
      
      if(!wroteGIPOflag && mcd.gipoInGeneralSrcFileFlag)
      { /* only write GIPO for first file */        
        util.logMsg("     writing GIPO",true,Color.black);
        if(!cvt.dio.writeGipoData())
        { /* [TODO] handle fatal error */
          util.logMsg("Can't write GIPO file.",true,Color.red);
          return;
        }
        wroteGIPOflag= true;
      }
      util.logMsg("     writing Quant["+n+"]",true,Color.black);
      if(!cvt.dio.writeQuantData(n))
        return;           /* problem with the file */
    } /* process source files */
    
    /* [7] Setup project directory */
    if(mkPrjStr.startsWith("Create New"))
    { /* make prj dirs, Config/, MAE/ Quant/ & setup files */
      try
      {
        mcd.createMAEproject();
      }
      catch(IOException e2)
      {
        return;
      }
    } /* make prj dirs, Config/, MAE/ Quant/ & setup files */
    
    /* [8] Tell them we are all done */
    util.logMsg("===> Finished writing out data files. Press 'Done' to exit",
                Color.green);
    util.logMsg2("To start MAExplorer, go to project folder & click on Start.mae.",
                 Color.green);
    if(out!=null)
      out.setVisible(false);
    
    exitButton.setLabel("Done");
    exitButton.setBackground(Color.yellow);
    
    /* disable other buttons */
    runButton.setEnabled(false);
  } /* runProcessData */
  
  
  /**
   * reanalyzeSourceFileList() - re-analyze for mult samples from 1st
   * file, iff mcd.needToReAnalyzeFilesFlag is set. Also, check
   * whether mcd.hasMultDatasetsFlag is set.<BR>  Note: This is
   * called from EditMaeProjectFiles when change the row.<BR> with
   * samples when have multiple samples. and from the "Analyze" button.
   * @param isEditingFlag if currently using the EditMaeProjectFiles
   * @see #addSourceFileToList
   */
  public void reanalyzeSourceFileList(boolean isEditingFlag)
  { /* reanalyzeSourceFileList */
    if(!mcd.needToReAnalyzeFilesFlag || mcd.nSrcFiles<=0)
      return;                     /* no-op */
    
    mcd.needToReAnalyzeFilesFlag= false;   /* reset flag */
    
    util.logMsg2("Re-analyzing input files for multiple samples", Color.black);
    String dName= mcd.dbPrjName;      /* use generic global DB name*/
    
    /* reset lists... to NULL
     * mcd.nQuantFiles= 0;
     * [TODO] GCT: why set to false?
     * mcd.cvt.selectedInputFileFlag= false;
     */
    
    inFilesList.removeAll();   /* source file list!! [TODO] rename */
    
    for(int i=0;i<mcd.nSrcFiles;i++)
    { /* re-analyze source files based on new expected row data */
      String
        fName= mcd.srcFile[i],
        tmpSrcFile= mcd.tmpSrcFile[i];
      addSourceFileToList(dName, fName, tmpSrcFile, true, isEditingFlag);
    }
      /*
    if(cvt.DBUG_FLAG)
    {
      ArrayLayout al= sul.alList[sul.useAL];
      System.out.println("\nCG.RSFE.1 mcd.nSrcFiles="+mcd.nSrcFiles+
                         " mcd.nQuantFiles="+mcd.nQuantFiles);
      System.out.println("CG.RSFE.2 sul.alList[useAL]="+ al.toString());
      System.out.println("CG.RSFE.2.3 mcd.fm="+mcd.fm.toString());
    }
   */
  } /* reanalyzeSourceFileList */
  
  
  /**
   * addSourceFileToList() - add file to list and do intermediate processing.
   * Make sure it is not already on the "2.1 Samples to use list".
   *<PRE>
   * If analyzeFileFlag, then also:
   * 1. Count lines in the list and update (G,R,C) if first file
   *    and <User-defined> chip.
   * 2. Enable User-to-MAE fields map editing if selected chip layout.
   * 3. Setup the filenames.
   * 4. Update text areas.
   *</PRE>
   * @param dName is the directory name
   * @param fName is the file name
   * @param fNameTmp is the tmp file name if not null
   * @param analyzeFileFlag requests the data file be analyzed
   * @param isEditingFlag if currently using the EditMaeProjectFiles
   * @see DataIO#setupFilenames
   * @see FileTable#findMaxGridRowColInFile
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   * @see UtilCM#mapIllegalChars
   * @see #scanForMultSamples
   * @see #updateMCDstateFromSrcFileData
   */
  public void addSourceFileToList(String dName, String fName, String fNameTmp,
                                  boolean analyzeFileFlag, boolean isEditingFlag)
  { /* addSourceFileToList */
    boolean srcFileExistsFlag= false;
    String srcFname= (fNameTmp==null) ? (dName + fName) : fNameTmp;
    int curSrcFile= mcd.nSrcFiles;  /* current source file is
                                     * last file in the list unless
                                     * overridden when relanalyze
                                     * and we are using an older file. */
    
    /* [1] Make sure not in list already */
    for(int i= 0;i<mcd.nSrcFiles;i++)
      if(mcd.srcFile[i].equals(fName))
      { /* already in the list - don't add again since we are reanalyzing it */
        util.logMsg("Note - file ["+fName+"] is already in the list.",
                    Color.red);
        srcFileExistsFlag= true;
        curSrcFile= i;                       /* found existing file - save index */
        break;
      }
    mcd.dirName= dName;                       /* use selected results */
    if(mcd.dbPrjName==null || mcd.dbPrjName.length()==0)
      mcd.dbPrjName= dName;                   /* default if not defined */
   
    if(!srcFileExistsFlag)
    { /* only push name if does not exist - we are reanalyzing it */
      mcd.srcFile[curSrcFile]= fName;
      mcd.tmpSrcFile[curSrcFile]= fNameTmp;
      mcd.prjName[curSrcFile]= mcd.dbPrjName; /* default. This will be
                                               * overidden w/ mcd.dbPrjName
                                               * if defined in the editor */
    } /* only push name if does not exist - we are reanalyzing it */
    
    if(!mcd.hasMultDatasetsFlag)
    { /* save source files in both srcFile[] and quantDstFile[] */
      /* NOTE: this assumes that the fName's are unique. */
      mcd.quantSampleNbrInFile[mcd.nQuantFiles]= 1;
      mcd.quantSrcFieldNbr[mcd.nQuantFiles]= -1;     /* where sample starts*/
      
      if(!mcd.useSrcTmpFileFlag)        //(fNameTmp==null)
      { /* 1:1 mapping to file */
        String qFile= mcd.dirName + fName;         
        mcd.quantSrcDir[mcd.nQuantFiles]= dName;
        mcd.quantSrcFile[mcd.nQuantFiles]= fName;    /* was qFile */
        srcFname= dName + fName;
      }
      else
      { /* go through tmp file in the current directory */        
        mcd.quantSrcDir[mcd.nQuantFiles]= "";
        mcd.quantSrcFile[mcd.nQuantFiles]= fNameTmp;
        srcFname= fNameTmp;
      }
      
      mcd.quantDstFile[mcd.nQuantFiles]= "IS-REPLACED"; /* is REPLACED */
     
      String protectedFname= UtilCM.mapIllegalChars(fName);
      mcd.quantName[mcd.nQuantFiles]= protectedFname;
      mcd.quantNameOrig[mcd.nQuantFiles]= protectedFname;
      mcd.prjName[mcd.nQuantFiles++]= mcd.dbPrjName;
      
      /* overide w/mcd.dbPrjName */
      if(analyzeFileFlag)
        updateQuantNameList();                   /* update display */
    } /* save source files in both srcFile[] and quantDstFile[] */
    
    if(!analyzeFileFlag)
      return;
    
    /* [2] Count # of lines in file, looking for Fields and Samples
     * locations. They could have more tabs than the actual number of
     * actual tokens (i.e. not "" or null) .
     */
    String
      fullPath= ((fNameTmp!=null) ? fNameTmp : (dName+fName)),
      msg= "Counting lines in ["+fName+"] .";
    boolean lookForSampleRowFlag= mcd.hasMultDatasetsFlag;
    /* Set from Layout or checkbox */
    
    util.logMsg(msg,Color.magenta);
    
    /* Set from Layout or checkbox */
    mcd.linesInFile[curSrcFile]= 
           FileTable.countLinesAndTabCountsInFile(cvt, fullPath,
                                                  lookForSampleRowFlag,
                                                  msg, Color.magenta);
    util.logMsg("There are "+mcd.linesInFile[curSrcFile]+
                " rows of data in file ["+fName+"]", Color.black);
    
    /*
    if(cvt.DBUG_FLAG)
      System.out.println("CGUI-ASFTL ft.estSamplesRowNbr="+FileTable.estSamplesRowNbr+
                         "ft.estFieldsRowNbr="+FileTable.estFieldsRowNbr+
                         "\n mcd.rowWithSamples="+mcd.rowWithSamples+
                         " mcd.rowWithFields="+mcd.rowWithFields);
    */
    if(mcd.linesInFile[curSrcFile]>0)
    { /* update starting Samples and Field rows */
      //ArrayLayout al= sul.alList[sul.useAL]; /* update layout as well! */
      String sMsg= "The ";
      
     /* Handle special case: where analyzing mult samples/file
      * where the estSamplesRowNbr==0 but fields is not.
      * Then force estSamplesRowNbr to estFieldsRowNbr-1
      */
      if(cvt.NEVER && !isEditingFlag &&
         FileTable.estSamplesRowNbr==0 && mcd.hasMultDatasetsFlag &&
         FileTable.estFieldsRowNbr>0)
      { /* rule where no sample row was found - use field row  */
        FileTable.estSamplesRowNbr= FileTable.estFieldsRowNbr;
        FileTable.estFieldsRowNbr++;  /* bump it down one row */
      }
      
      if(!isEditingFlag && FileTable.estSamplesRowNbr!=0)
      { /* we found a samples row */
        /* [CHECK] if overide mcd. with FileTable. value */
        mcd.rowWithSamples= FileTable.estSamplesRowNbr;
        sMsg += "Sample names row is "+mcd.rowWithSamples+", ";
        /*
        if(cvt.DBUG_FLAG)
          System.out.println("Samples row is "+mcd.rowWithSamples);
        */
      }
      
      if(!isEditingFlag && FileTable.estFieldsRowNbr!=0)
      { /* we found a Fields name row */
        mcd.rowWithFields= FileTable.estFieldsRowNbr;
        sMsg += "Field names row is "+mcd.rowWithFields;
        /*
        if(cvt.DBUG_FLAG)
          System.out.println("Field row is "+mcd.rowWithFields);
         */
      }
      
      if(!isEditingFlag && FileTable.estRowWithData!=0)
      { /* we found a Data row */
        /* [CHECK] if overide mcd. with FileTable. value */
        mcd.rowWithData= FileTable.estRowWithData;
        sMsg += "starting data row is "+mcd.rowWithData+", ";				
        /*
        if(cvt.DBUG_FLAG)
          System.out.println("Data row is "+mcd.rowWithData);
         */
      } /* we found a Data row */
      
      util.logMsg2(sMsg, Color.black);
    } /* update starting Samples and Field rows */
    
    cvt.selectedInputFileFlag= true;
    inFilesList.add(fName);  /* Makes list of names - may have
                              * mult inFiles / srcFile where each
                              * inFile maps to 1 .quant file.
                              * e.g. Affy files, etc. */
    
    /* [3] Get the name without the file extension if any and
     * then setup the filenames.
     */
    int idx= fName.lastIndexOf(".");
    if(idx==-1)
      mcd.srcName= fName;
    else
      mcd.srcName= fName.substring(0,idx-1);
    
    /* [4] If no (G,R,C) and required then, try to get from Quant input file */
    if(//!mcd.specifyGeometryByNbrSpotsFlag && mcd.hasSeparateGIPOandQuantFilesFlag &&
       //mcd.separateGIPOinputFile!=null &&
       curSrcFile==0 &&
       (mcd.maxGrids==0 || mcd.maxGridRows==0 || mcd.maxGridCols== 0))
    { /* try to find values in the Quant data input file */
      msg= "Trying to find maximum value of (Grid, Grid Rows, Grid Cols). ";
      boolean flag= FileTable.findMaxGridRowColInFile(cvt, fullPath, msg,
                                                      Color.magenta,
                                                      mcd.rowWithFields,
                                                      mcd.rowWithData);
      if(flag)
        util.logMsg3("The # of (Grids, Grid Rows, Grid Cols) is("+
                     mcd.maxGrids+","+mcd.maxGridRows+","+mcd.maxGridCols+
                     ")", Color.black);
      else
        util.logMsg3("Use 'Edit Layout' to set the number of (Grids, Grid Rows, Grid Cols)",
                     Color.red);
    } /* try to find values in the Quant data input file */
    
    /* [5] Count source files if not reanalyzing existing files. */
    if(!srcFileExistsFlag)
      mcd.nSrcFiles++;
    
    /* [6] Setup proper file names based on path analysis */
    cvt.dio.setupFilenames();
    
    /* [7] Analyze srcFiles[] for multiple samples Then add them to the
     * quantFiles[] list if it is not already in the list.
     * Scan it for multiple samples. If generated tmp input file, we
     * scan it AFTER that is generated.
     */
    if(mcd.hasMultDatasetsFlag)
      scanForMultSamples(dName, fName, fNameTmp); 
    
    /* [8] Update pseudo array geometry in the MCD state
     * if mcd.specifyGeometryByNbrSpotsFlag, then estimate (
     * grid,row,col) size from mcd.linesInFile[0].
     * then update the chip layout GUI in the main frame,
     * then copy all relevant mcd.XXXX to sul.alList[sul.useAL].XXXX.
     */
    updateMCDstateFromSrcFileData();
    
    repaint();
  } /* addSourceFileToList */
  
  
  /**
   * updateMCDstateFromSrcFileData() - update MCD state from file data.
   *<PRE>
   * 1. Update pseudo array geometry in the MCD state
   *    if mcd.specifyGeometryByNbrSpotsFlag, then estimate
   *    (grid,row,col) size from mcd.linesInFile[0].<BR>
   * 2. then update the chip layout GUI in the main frame.<BR>
   * 3. then copy all relevant mcd.XXXX to sul.alList[sul.useAL].XXXX.<BR>
   *</PRE>
   * @see SetupLayouts#copyMCDtoALOstate
   * @see #setMapEditing
   * @see #setRenameRemoveEnables
   * @see #updateChipValuesGUIfromMCD
   */
  public void updateMCDstateFromSrcFileData()
  { /* updateMCDstateFromSrcFileData */
    
    /* [1] We only estimate PseudoArray for first file. If there are
     * more than one source file - WE CURRENTLY ASSUME THEY ARE ALL
     * THE SAME # of rows!!!
     */
    if(mcd.nSrcFiles==1)
    { /* update MCD with estimated pseudo array for <User-defined> */
      int
        nLinesInFile= mcd.linesInFile[mcd.nSrcFiles-1],
        rowWithData= mcd.rowWithData,
        nbrDataRows= nLinesInFile - rowWithData;
      mcd.maxRowsExpected= nbrDataRows;
      //mcd.specifyGeometryByNbrSpotsFlag= false;   /* clear it */
      
      /* Estimate a reasonable PseudoArray geometry */
      if(mcd.specifyGeometryByNbrSpotsFlag && mcd.maxRowsExpected>0)
        sul.setPseudoArrayToCurMCD();
    } /* update MCD with estimated pseudo array for <User-defined> */
    
    /* [2] Copy all relevant mcd.XXXX to sul.alList[sul.useAL].XXXX. */
    sul.copyMCDtoALOstate();
    
    /* [3] Update main GUI chip layout text areas  from MCD state */
    updateChipValuesGUIfromMCD();
    
    /* [4] Now can enable editing of the MCD state. */
    boolean allowEditingFlag= (cvt.selectedInputFileFlag &&
                               cvt.selectedChipLayoutFlag);
    
    editLayoutButton.setEnabled(allowEditingFlag);
    setRenameRemoveEnables(allowEditingFlag);
    setMapEditing(allowEditingFlag);
    
    /*
    if(cvt.DBUG_FLAG)
      System.out.println("CG-UMFSFD al["+sul.useAL+"="+al.toString());
    */
  } /* updateMCDstateFromSrcFileData */
  
  
  /**
   * setChipLayoutAndCheckState() - set cur. chip layout and check state
   * if al.specifyGeometryByNbrSpotsFlag, then estimate (grid,row,col
   * Then copy all relevant mcd.XXXX to sul.alList[sul.useAL].XXXX.
   * @see SetupLayouts#setPseudoArrayToCurALO
   * @see #setMapEditing
   * @see #updateChipValuesGUIfromMCD
   */
  public void setChipLayoutAndCheckState()
  { /* seteChipLayoutAndCheckState */
    if(sul.useAL<0)
      return;           /* no-op */
    
    ArrayLayout al= sul.alList[sul.useAL];  /* chip layout selected */
    boolean allowEditingFlag= (cvt.selectedInputFileFlag &&
                               cvt.selectedChipLayoutFlag);
    
    if(al.specifyGeometryByNbrSpotsFlag && mcd.nSrcFiles==1)
    { /* update MCD with estimated pseudo array for <User-defined> */
      int nbrDataRows= mcd.linesInFile[mcd.nSrcFiles-1]-mcd.rowWithData;
      al.maxRowsExpected= nbrDataRows;
      //mcd.specifyGeometryByNbrSpotsFlag= false; /* force it */
      
      /* Estimate a reasonable PseudoArray geometry and save
       * it in the ALO state.
       */
      if(al.maxRowsExpected>0)
        sul.setPseudoArrayToCurALO();
    } /* update MCD with estimated pseudo array for <User-defined> */
    
    /* [2] Update MCD Config state from the ALO state.
     * Copy al[useAL].XXX state from the mcd.XXX state. This
     * means that if we edit the ALO, we have the latest ALO
     * in the MCD for editing.
    */
    sul.copyALOtoMCDstate();
    
    /* [3] Update main GUI chip layout text areas  from MCD state */
    updateChipValuesGUIfromMCD();
    
    /* [4] Now can enable editing of the MCD state. */
    editLayoutButton.setEnabled(allowEditingFlag);
    setMapEditing(allowEditingFlag);
   
    /*
    if(cvt.DBUG_FLAG)
      System.out.println("CG-SCLACS al["+sul.useAL+"="+al.toString());
    */
  } /* setChipLayoutAndCheckState */
  
  
  /**
   * changeChipLayoutAndCheckState() - set cur. chip layout and check state
   * This is called by the chip choice handler.
   * @param arrayLayoutStr is the name of the array layout
   * @see #rebuildSrcBrowserPanels
   * @see #setChipLayoutAndCheckState
   */
  public void changeChipLayoutAndCheckState(String arrayLayoutStr)
  { /* changeChipLayoutAndCheckState */
    int foundSelectionNbr= -1;
    
    /* Note: entry al[0] is just a comment ... */
    for(int i=0;i<sul.maxAL;i++)
    { /* search for matching array layout *
      /* Note: the ArrayLayout list starts with "<User-defined>" at [0]
       * whereas it is [1] in the chipsetChoice list.
       */
      if(arrayLayoutStr.equals(sul.alList[i].layoutName))
      { /* found the layout */
        foundSelectionNbr= i;
        mcd.editedLayoutFlag= false;  /* reset it - must edit
                                       * first to "Save Layout" */
        
        /* Note: default is to force it to est. geometry by # spots
         * when we read the FIRST file. We may want to overide
         * this later if it turns out we actually do have
         * a geometry.
         */
        mcd.specifyGeometryByNbrSpotsFlag= true;
        
        sul.useAL= foundSelectionNbr;     /* Set current chip state */
        cvt.selectedChipLayoutFlag= true;
        
        /* if "<User-defined>" chip layout,then rebuild source browser
         * panel with "Analyze" button "[x] Mult samples/file"
         */
        if(arrayLayoutStr.equals("<User-defined>"))
        { /* redo main GUI for additional buttons */
          mcd.hasUserDefineChipsetFlag= true;
          mcd.hasMultDatasetsFlag= true;     /* force it */
          sul.alList[sul.useAL].hasMultDatasetsFlag= mcd.hasMultDatasetsFlag;
          /* Reset editing flags - all of which must be set
           * true before you can press analyze to reanalye the data.
           */
          mcd.didEditALOflag= false;
          mcd.didGIPOassignFlag= false;
          mcd.didQuantFlag= false;
          rebuildSrcBrowserPanels(true);
        }
        else
        { /* redo GUI to simplify and remove buttons */
          mcd.hasUserDefineChipsetFlag= false;
          rebuildSrcBrowserPanels(false);
        }
        
        /* Update the current chip layout */
        setChipLayoutAndCheckState();
        
        /* Enable GUI */
        browseSrcFileButton.setEnabled(true); /* enable */
        sepGipoQuantFilesCB.setEnabled(true);
        sepGipoQuantFilesCB.setState(mcd.hasSeparateGIPOandQuantFilesFlag);
        browseGIPOsrcFileButton.setEnabled(mcd.hasSeparateGIPOandQuantFilesFlag);
        // editSepGIPOfieldsButton.setEnabled(mcd.hasSeparateGIPOandQuantFilesFlag);
        removeSampleButton.setEnabled(true);
        renameSampleButton.setEnabled(true);
        
        repaint();
        break;
      } /* found the layout */
    } /* search for matching array layout */
  } /* changeChipLayoutAndCheckState */
  
  
  /**
   * setRenameRemoveEnables() enable/disable Rename and Remove buttons
   * @param flag enable/disable Rename and Remove button
   */
  public void setRenameRemoveEnables(boolean flag)
  { /* setRenameRemoveEnables */
    if(removeSampleButton!=null)
      removeSampleButton.setEnabled(flag);
    if(renameSampleButton!=null)
      renameSampleButton.setEnabled(flag);
  } /* setRenameRemoveEnables */
  
  
  /**
   * chkReanlyzeButtonState() -  check if the Reanalyze button is active.
   * Also update the "Save Layouts" if ready.
   * Buttons that become ready are changed to Magenta so they
   * are easier to see.
   * @return active reanalyze button flag
   */
  public boolean chkReanlyzeButtonState()
  { /* chkReanlyzeButtonState */
    mcd.activeReanalyeButtonFlag= (mcd.didEditALOflag &&
    mcd.didGIPOassignFlag &&
    mcd.didQuantFlag &&
    mcd.hasMultDatasetsFlag);
    if(analyzeSrcFilesButton!=null)
    {
      analyzeSrcFilesButton.setEnabled(mcd.activeReanalyeButtonFlag);
      if(mcd.activeReanalyeButtonFlag)
        analyzeSrcFilesButton.setBackground(Color.magenta);
    }
    
    if(mcd.editedLayoutFlag &&
    ((mcd.activeReanalyeButtonFlag && mcd.hasUserDefineChipsetFlag) ||
    !mcd.hasUserDefineChipsetFlag ||
    (!cvt.fmgFirstTimeFlagGipo && !cvt.fmgFirstTimeFlagQuant)))
    { /* enable "Save Layout" button */
      saveArrayLayoutButton.setEnabled(true);
      saveArrayLayoutButton.setBackground(Color.magenta);
    }
    
    return(mcd.activeReanalyeButtonFlag);
  } /* chkReanlyzeButtonState */
  
  
  /**
   * scanForMultSamples() - analyze ithSample srcFiles[ithSample-1] for
   * multiple samples. If consecutive fields have the same name,
   * only use the column for the first name.
   * Then add them to the quantFiles[] list if it is not already in
   * the list.
   * @param dName is the directory name
   * @param fName is the file name
   * @param fNameTmp is the tmp file name if not null
   * @return true if read successsfully
   * @see DataIO#chkAndEditFieldNames
   * @see FieldMap#lookupUserIndex
   * @see FileTable
   * @see FileTable#readTableFieldsFromFile
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   * @see #updateQuantNameList
   */
  public boolean scanForMultSamples(String dName, String fName, String fNameTmp)
  { /* scanForMultSamples */
    if(mcd.nSrcFiles<=0 || !mcd.hasMultDatasetsFlag || mcd.rowWithSamples==-1)
    {
      /* [TODO] need better error handling. Maybe copy srcFiles[] to quantFiles[] */
      return(false);
    }
    
    String
      sFile,                    /* src file path and filename */
      sampleFields[],           /* sample names fields */
      sDataFields[],            /* data fields */
      qFile= null,              /* full path quant Files */
      qName= null,              /* quant name part of file */
      sDFname= null;
    int
      idxDataField= -1,         /* index in input file for rawIntensity data*/
      nSamples,
      nDataFields,
      sColInFile,
      nthSample= 0;
    FileTable fio= new FileTable("GetSampleNames");
    
    /* [1] Analyze last file entered for its list of sample names */    
    sFile= (fNameTmp==null) ? (dName + fName) : fNameTmp;
    sampleFields= fio.readTableFieldsFromFile(sFile, mcd.rowWithSamples);
    sDataFields= fio.readTableFieldsFromFile(sFile, mcd.rowWithFields);
    
    if(sampleFields==null)
    { /* missing data */
      util.logMsg("Warning - missing Sample names data - probably incorrect sample row # ["+
                  mcd.rowWithSamples+"]",Color.red);
      util.logMsg2("Change the Sample names row using 'Edit Layout' and try again.",
                   Color.red);
      return(false);
    }
    if(sDataFields==null)
    { /* missing data */
      util.logMsg("Warning - missing data Field names data - probably incorrect Field names row # ["+
                  mcd.rowWithFields+"]",Color.red);
      util.logMsg2("Change the Field names row using 'Edit Layout' and try again.",
                   Color.red);
      return(false);
    }
    
    if(mcd.chkAndEditFieldNamesFlag)
    { /* do special handling if any */
      cvt.dio.chkAndEditFieldNames(sampleFields,"SampleNames");
      cvt.dio.chkAndEditFieldNames(sDataFields,"FieldNames");
    } /* do special handling if any */
    
    /* [2] Read samples line and get list of samples and
     * also record which columns in file contain the sample data.
     * Verify that the data fields associated with a sample are
     * valid. Then it adds the samples to the quantDstFile[]/quantName[]
     * lists.
     */
    nSamples= sampleFields.length;
    nDataFields= sDataFields.length;
    util.logMsg("",Color.black);
    util.logMsg2("Do (step 3) after finished adding files.",Color.black);
    util.logMsg3("Found "+nSamples+" samples with "+nDataFields+
                 " data files/sample",Color.black);
    
    for(int j=0;j<nSamples;j++)
    { /* look for sample names */
      qName= sampleFields[j];
      if(qName!=null && qName.length()>0)
      { /* found one - add to list if not already in list */
        /* [2.1] Verify that corresponding data field name is valid */
        sDFname= sDataFields[j];
        idxDataField= mcd.fm.lookupUserIndex(sDFname);
        
        if(idxDataField==-1)
        {
          /* msg for log file */
          util.logMsg3("Invalid sample ["+qName+"] field ["+sDFname+"].",
                       Color.red);
          /* final msg */
          util.logMsg("Some sample names don't have proper field names"+
                      " - ignoring them.", Color.red);
          util.logMsg2("Adding rest of the samples to sample list.  Do (step 3) after add all files.",
                       Color.black);
          continue;
        }
        
        /* [2.2] ok - add to list if not already in the list */
        qFile= mcd.dirName + qName;
        boolean inListFlag= false;
        for(int k=0;k<mcd.nQuantFiles;k++)
          if(mcd.quantSrcFile[k].equals(fName) && 
             mcd.quantNameOrig[k].equals(qName))
          { /* look for duplicate files */
            inListFlag= true;
            break;
          }
        
        /* [2.3] Not in list, so go add it */
        if(!inListFlag)
        { /* add to list */
          mcd.quantSampleNbrInFile[mcd.nQuantFiles]= ++nthSample;
          mcd.quantSrcFieldNbr[mcd.nQuantFiles]= j;   /* col # */            
          
          mcd.quantSrcFile[mcd.nQuantFiles]= (fNameTmp==null)
                                               ? fName : fNameTmp;
          mcd.quantSrcDir[mcd.nQuantFiles]= (mcd.useSrcTmpFileFlag) 
                                              ? "" : mcd.dirName;
          mcd.quantDstFile[mcd.nQuantFiles]= qFile ;  /* the output .quant file */
          
          String protectedFname= UtilCM.mapIllegalChars(qName);
          mcd.quantName[mcd.nQuantFiles]= protectedFname;
          mcd.quantNameOrig[mcd.nQuantFiles]= protectedFname;
          mcd.linesInFile[mcd.nQuantFiles++]= mcd.linesInFile[0];
        } /* add to list */
        
      } /* found one - add to list if not already in list */
    } /* look for sample names */
    
    /* Update the gui.inSamplesList */
    updateQuantNameList();
    
    return(true);
  } /* scanForMultSamples */
  
  
  /**
   * getFullSampleGUIname() - return the full sample[j] GUI name
   * @param j jth sample
   * @return full sample[j] GUI name
   */
  public String getFullSampleGUIname(int j)
  { /* getFullSampleGUIname */
    String
      sRename= (mcd.quantNameOrig[j].equals(mcd.quantName[j]))
                   ? "]"
                   : "] renamed to ["+mcd.quantName[j]+"]",
      sName= "<<" + mcd.quantSrcFile[j] + ">>  ["+
             mcd.quantNameOrig[j] + sRename;
    return(sName);
  } /* getFullSampleGUIname */
  
  
  /**
   * analyzeGIPOfileHeader() - analyze seperate GIPO file to find the
   * list of field names, the starting row of the data and possibly
   * determine which fields will be used when merge the data with the
   * separate Quant file field data.
   * @return true if found the list of field names ok, false if error or not found
   * @see FileTable
   * @see FileTable#findRowWithKeywordsInFile
   * @see FileTable#countLinesAndTabCountsInFile
   * @see FileTable#readTableFieldsFromFile
   * @see PopupGetSepGIPOnamesForGridRowCol
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   */
  public boolean analyzeGIPOfileHeader()
  { /* analyzeGIPOfileHeader */
    FileTable fio= new FileTable("GetGIPOheader");
    
    /* [1] Analyze last file entered for its list of sample names */
    String
      sMsg,
      gipoFile= mcd.separateGIPOinputFile;
    
      /* [2] Try to estimate Field rows by looking for keywords that
       * are propably in FieldG */
    String
      keyWords[]= {"row", "col"};
      
    sMsg= "Looking for keywords (row,col) in ["+mcd.separateGIPOinputFile+"]";
    mcd.nSepGIPOrows= 0;	/* not counting them for now... */
  
    /* Find the row in the file which has ALL keywords. Then
     * save the this as the starting row of the FieldG fields.
     */
    mcd.rowWithSepGIPOFields=
            FileTable.findRowWithKeywordsInFile(cvt, gipoFile, keyWords,
                                                true,    /* needAllKeywordsFlag */
                                                100,     /* maxLinesToCheck */
                                                sMsg, Color.magenta);

    /* [3] Test if starting rows are valid, if not, try to find it by counting tabs. */
    if(mcd.rowWithSepGIPOFields<=0)
    { /* Estimate Fields row by # of tabs analysis */
      sMsg= "Counting lines in ["+mcd.separateGIPOinputFile+"]";
      mcd.nSepGIPOrows=
             FileTable.countLinesAndTabCountsInFile(cvt, gipoFile, false, sMsg,
                                                    Color.magenta);
      mcd.rowWithSepGIPOFields= fio.estFieldsRowNbr;
      util.logMsg("There are "+mcd.nSepGIPOrows+
                  " rows of data in GIPO file ["+mcd.separateGIPOinputFile+"]",
                  Color.black);
    } /* Estimate Fields row by # of tabs analysis */
    
    /* [4] Test if found it */
    if(mcd.rowWithSepGIPOFields<=0)
    { /* trouble */
      util.logMsg("Can not discover Field names row in GIPO file ["+
                  mcd.separateGIPOinputFile+"].", Color.red);
      util.logMsg2("Use 'Edit Layout' to set GIPO Field names & starting Data row numbers.",
                   Color.red);
      return(false);
    }
    
    /* [5] Assume GIPO data starts +1 more */
    mcd.rowWithSepGIPOData= mcd.rowWithSepGIPOFields+1;
    util.logMsg("The GIPO file Field row is "+mcd.rowWithSepGIPOFields,Color.black);
    util.logMsg2("The starting data row is "+mcd.rowWithSepGIPOData, Color.black);
    util.logMsg3("If not correct, use 'Edit Layout' to set these GIPO row numbers.",
                 Color.black);
    
    /* [6] Get the actual field names since will need it for readData() parser */
    mcd.sepGIPOfields= fio.readTableFieldsFromFile(gipoFile,
                                                   mcd.rowWithSepGIPOFields);
    mcd.nSepGIPOfields= mcd.sepGIPOfields.length;
    mcd.sepGIPOfieldsUsed= new boolean[mcd.nSepGIPOfields];
    
   /* [7] setup the sepGIPOfieldsUsed[0:nSepGIPOfields-1] flags that
    * are set if include fields in separate GIPO when merge data when
    * generate the (FieldG+FieldQ) data in readData().
    * I.e. do NOT include (Block, Row, Col) in GIPO if (Grid, Row, Col)
    * exist in the Quant file.
    */
    PopupGetSepGIPOnamesForGridRowCol
          pgNames= new PopupGetSepGIPOnamesForGridRowCol(cvt, this,
                                                        "Block", "Row", "Column", 
                                                        false);
    mcd.sepGipoGridName= pgNames.sepGipoGridName;
    mcd.sepGipoRowName= pgNames.sepGipoRowName;
    mcd.sepGipoColName= pgNames.sepGipoColName;
    
    String sBadFields= "";
    if(!pgNames.gridFlag)
      sBadFields = "Grid ";
    if(!pgNames.rowFlag)
      sBadFields += "Row ";
    if(!pgNames.colFlag)
      sBadFields += "Column ";
    
    if(!pgNames.dataIsValid)
    { /* bad GIPO names selected */
      util.logMsg("Your [ "+sBadFields+"] choice(s) are not GIPO file fields.", Color.red);
      util.logMsg2("Try again, and pick or enter fields from those found GIPO input file.",
                   Color.red);
      return(false);
    }
    
    mcd.nSepGIPOfieldsUsed= 0;
    for(int i=0;i<mcd.nSepGIPOfields;i++)
    { /* build Use GIPO token map and # actually used */
      String fName= mcd.sepGIPOfields[i];
      boolean flag= (!fName.equals(mcd.sepGipoGridName) &&
                     !fName.equals(mcd.sepGipoRowName)  &&
                     !fName.equals(mcd.sepGipoColName));
      mcd.sepGIPOfieldsUsed[i]= flag;  /* include all but dupl. stuff */
      if(flag)
        mcd.nSepGIPOfieldsUsed++;      /* # actually used */
    }/* build Use GIPO token map and # actually used */
    
    String
      msg= "Trying to find maximum value of (Grid, Grid Rows, Grid Cols) from Separate GIPO. ";
    boolean flag= FileTable.findMaxGridRowColInFile(cvt, gipoFile, msg, Color.magenta,
                                                    mcd.rowWithSepGIPOFields,
                                                    mcd.rowWithSepGIPOData);
    
    return(true);
  } /* analyzeGIPOfileHeader */
  
  
  /**
   * updateQuantNameList() - update the list of samples in GUI
   * @see #getFullSampleGUIname
   */
  public void updateQuantNameList()
  { /* updateQuantNameList */
    /* Update the gui.inSamplesList */
    inSamplesList.removeAll();
    for(int j=0;j<mcd.nQuantFiles;j++)
    {
      String sName= getFullSampleGUIname(j);
      mcd.fullSampleName[j]= sName;   /* remember for editing */
      inSamplesList.add(sName);       /* update GUI */
    }
    
    repaint();
  } /* updateQuantNameList */
  
  
  /**
   * removeSample() - removes selected sample if any from GUI and
   * quantXXXX[] lists. Get selected item from item handler
   * @see #removeSampleByName
   */
  public void removeSample()
  { /* removeSample */
    int selIdxList[]= inSamplesList.getSelectedIndexes();
    if(selIdxList==null)
      return;
    
    int n= selIdxList.length;
    if(n==0)
      return;
    String samplesToRemove[]= new String[n];
    int m= 0;
    
    /* Remove entry from GUI List and also from quantXXXX[] arrays */
    for(int j=(n-1);j>=0;j--)
    {
      int i= selIdxList[j];
      samplesToRemove[m++]= mcd.fullSampleName[i];
    }
    
    /* Remove all entries from the list by full sample name */
    for(int j=0;j<m;j++)
      removeSampleByName(samplesToRemove[j]);
  } /* removeSample */
  
  
  /**
   * removeSampleByName() - removes sample if valid from GUI and
   * quantXXXX[] lists. It sets the sample to null and then
   * does a bubble sort to remove all null entries.
   * No-op if can't find valid name.
   * @param name of the sample to remove
   * @see #updateQuantNameList
   */
  public void removeSampleByName(String name)
  { /* removeSampleByName */
    int
      k,
      i= -1;
    
    for(int n=0;n<mcd.nQuantFiles;n++)
      if(mcd.fullSampleName[n].equals(name))
      {
        i= n;                      /* found it! */
        break;
      }
    if(i==-1 || mcd.nQuantFiles==0)
      return;                        /* bogus name */
    /*
    if(cvt.DBUG_FLAG)
    {
      System.out.println("CG-RSBN.1 i="+i+" name='"+name+"'");
      for(int m=0;m<mcd.nQuantFiles;m++)
        System.out.println("CG-RSBN.1.1 mcd.quantName[m="+m+"]="+
                           mcd.quantName[m]);
    }
    */
    
    /* Remove entry from GUI List and also from quantXXXX[] arrays */
    mcd.quantName[i]= null;
    mcd.quantNameOrig[i]= null;
    mcd.prjName[i]= null;
    mcd.linesInFile[i]= 0;
    mcd.quantSampleNbrInFile[i]= 0;
    mcd.quantSrcFieldNbr[i]= 0;
    mcd.quantSrcFile[i]= null;
    mcd.quantSrcDir[i]= null;
    mcd.quantDstFile[i]= null;
    
    /* Remove null entries from the list by bubble sort */
    if(i>=(mcd.nQuantFiles-1))
    { /* deleted entry at the end of the list */
      /* Note: do nothing since just drop last entry */
    }
    else
    { /* deleted entry in front part of list */
      for(int j=0;j<(mcd.nQuantFiles-1);j++)
      { /* bubble sort entries toward front of the list */
        if(j<i)
          continue;       /* leave list alone  for j<i */
        else
          k= j+1;         /* entry to copy */
        mcd.quantName[j]= mcd.quantName[k];
        mcd.quantNameOrig[j]= mcd.quantNameOrig[k];
        mcd.prjName[j]= mcd.prjName[k];
        mcd.linesInFile[j]= mcd.linesInFile[k];
        mcd.quantSampleNbrInFile[j]= mcd.quantSampleNbrInFile[k];
        mcd.quantSrcFieldNbr[j]= mcd.quantSrcFieldNbr[k];
        mcd.quantSrcDir[j]= mcd.quantSrcDir[k];
        mcd.quantSrcFile[j]= mcd.quantSrcFile[k];
        mcd.quantDstFile[j]= mcd.quantDstFile[k];
      } /* bubble sort entries toward front of the list */
    } /* deleted entry in front part of list */
    
    /*
    if(cvt.DBUG_FLAG)
    {
      for(int m=0;m<(mcd.nQuantFiles-1);m++)
        System.out.println("CG-RSBN.2 mcd.quantName[m="+m+"]="+
                           mcd.quantName[m]);
    }
    */
    
    mcd.nQuantFiles--;      /* decrement the count since removed it */
    
    /* Rebuild the GUI list  from the shortend list - overight the old list */
    updateQuantNameList();  /* update the list of samples in GUI */
  } /* removeSampleByName */
  
  
  /**
   * renameSample() - rename selected sample quantXXX name
   * and update GUI and quantXXXX[] lists.
   * Renaming a sample changes the quantName[] and thus quantDstFile[]
   * but does not rename the quantNameOrig[] (i.e. original) entry.
   * Get selected item from item handler
   *
   * @see PopupTextDialog#updatePopupTextDialog
   * @see UtilCM#logMsg2
   * @see UtilCM#mapIllegalChars
   */
  public void renameSample()
  { /* renameSample */
    /* [TODO] - finish - see below. */
    int selIdxList[]= inSamplesList.getSelectedIndexes();
    if(selIdxList==null)
      return;
    
    int
      i,
      n= selIdxList.length,
     cnt= 0;
    if(n!=1)
      return;
    
    /* [1] Rename entry from GUI List and in from quantXXXX[] arrays */
    i= selIdxList[0];
    String
      qName= mcd.quantName[i],       /* OLD name */
      newQname= qName,               /* default to the old name */
      newQfile,
      guiStr;
    
    /* [2] Assign value of newQname (default qName) using popup Dialog box.*/
    ptd.updatePopupTextDialog("Rename sample", qName);
    
    if(!ptd.flag )
      return;                         /* pressed "Cancel" */
    else
      newQname= UtilCM.mapIllegalChars(ptd.sText); /* get new data */
    if(newQname.equals(qName))
      return;                         /* no change */
    for(int j=0;j<mcd.nQuantFiles;j++)
      if(i!=j && mcd.quantName[i].equals(newQname))
      {
        util.logMsg2("Can't rename a sample to name of another sample"+
                     " - try again.", Color.red);
        return;
      }
    
    /* [3] Rename entries in list and GUI */
    newQfile= mcd.dirName + newQname;  /* Make newQfile from newQname */
    mcd.quantDstFile[i]= newQfile;
    mcd.quantName[i]= newQname;
    guiStr= getFullSampleGUIname(i);
    mcd.fullSampleName[i]= guiStr;
    inSamplesList.replaceItem(guiStr,i);
    repaint();
  } /* renameSample */
  
  
  /**
   * getNewSrcFileFromDialog() - get new source file using dialog popup
   *
   * @see #addSourceFileToList
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   */
  public void getNewSrcFileFromDialog()
  { /* getNewSrcFileFromDialog */
    String fNameTmp= null;      /* this is not null if using a tmp file */
    
    /* get the full input path = directory and specific file */
    if(!cvt.selectedChipLayoutFlag)
    {
      util.logMsg("You need to first do '1. Select Chipset'"+
                   " - before selecting files",  Color.red);
      return;
    }
    
    chipsetChoice.setEnabled(false);
    
    util.logMsg("Either continue adding input files (step 2),",
                 Color.black);
    util.logMsg2("or define Output Folder (step 3) when done adding files.",
                 Color.black);
    String 
      promptMsg= (!mcd.use2ndInputChipFileFlag)
                  ? "Select next input file to convert (you may use 'ALL' or 'ALL.<ext>')"
                  : "Select next A-chip input file to convert";
    fdI= new FileDialog(this, promptMsg);
               
    if(mcd.dirName!=null && mcd.dirName.length()!=0)
      fdI.setDirectory(mcd.dirName);
    if(cvt.USE_DBUG_DIR && mcd.nSrcFiles<1)
    { /* ONLY when debugging first file */
      mcd.dirName= cvt.DBUG_IN_DIR;
      fdI.setDirectory(mcd.dirName);
    }
    fdI.setMode(FileDialog.LOAD);
    /*
    if(cvt.DBUG_FLAG)
       util.logMsg("C2M-AP fdI.getDirectory()="+fdI.getDirectory());
    */
    fdI.setVisible(true);
    
    /* If using new affy MAS5.0, then then must remap fields into separate
     * sample and field names as a temp file and then reread it.
     */
    if(mcd.useSrcTmpFileFlag)
    { /* get affy MAS5 data to convert via temp file */
      dName= fdI.getDirectory();  /* get selected results */
      fName= fdI.getFile();
      fNameTmp= fName +"-" + mcd.nSrcFiles +".tmp";
      if(!mcd.use2ndInputChipFileFlag)
        createTempAffyFileForMAS5(dName, fName, fNameTmp, null, null,null);
      else
      { /* use additional chip to concatenate to first chip before make temp file */
        String
          dNameA= dName,
          fNameA= fName,
          fNameTmpA= fNameTmp,
          dNameB,
          fNameB,
          fNameTmpB;
        fdI= new FileDialog(this, "Select corresponding B-chip input file to convert");
        if(mcd.dirName!=null && mcd.dirName.length()!=0)
          fdI.setDirectory(mcd.dirName);
        fdI.setMode(FileDialog.LOAD);
        fdI.setVisible(true);  
        dNameB= fdI.getDirectory();  /* get selected results */
        fNameB= fdI.getFile();
        fNameTmpB = fNameB +"-" + mcd.nSrcFiles +".tmp";
        createTempAffyFileForMAS5(dNameA, fNameA, fNameTmpA,
                                  dNameB, fNameB, fNameTmpB);
      }/* use additional chip to concatenate to first chip before make temp file */
    } /* get affy MAS5 data to convert via temp file */
    else
    { /* NOT using temp file */
      dName= fdI.getDirectory();      /* get selected results */
      fName= fdI.getFile();
    }
    
    if(dName==null || fName==null)
      return;
    
    util.logMsg("",Color.black);
    util.logMsg2("",Color.black);
    util.logMsg3("", Color.black);    
    
    /* If file specification is '*.<ext>",then find all files with "<.ext>"
     * and add each one to the list, else just add the single file.
     */
    if(fName.equals("*") || fName.equals("ALL") ||
       (fName.startsWith("*.") && fName.length()>2) ||
       (fName.startsWith("ALL.") && fName.length()>4))
    { /* add a list of all files in the folder with the specified file extension */
      File folder= new File(dName);
      if(folder==null)
        return;
      String
        extName= ((fName.startsWith("*.") && fName.length()>2)
                    ? fName.substring(1)
                    : ((fName.startsWith("ALL.") && fName.length()>4)
                         ? fName.substring(3)
                         : null)),
        dirList[]= folder.list();
      int nDirList= dirList.length;
      if(nDirList==0)
        return;
      for(int i=0;i<nDirList;i++)
        if(extName==null || dirList[i].endsWith(extName))
        {
          fName= dirList[i];
          addSourceFileToList(dName, fName, fNameTmp, true, false);
        }
    } /* add a list of all files in the folder with the specified file extension */
    
    else
    { /* Add new file to list and do intermediate processing */
      addSourceFileToList(dName, fName, fNameTmp, true, false);
    }
  } /* getNewSrcFileFromDialog */
  
  
  /**
   * countNonNullTabData() - count Non-Null data tabs in line
   * Do not count tabs after reach field with no data.
   * @param sLine line to count lines on
   * @return number of non-null data tabs
   */
  public static int countNonNullTabData(String sLine)
  { /* countNonNullTabData */
    if(sLine==null)
      return(0);
    char inputBuf[]= sLine.toCharArray();  /* cvt input string to char[] */
    int
      nTabs= 0,
      bufSize= sLine.length(),        /* size of input buffer */
      bufCtr= 0;                      /* working input buffer index */
    
    while(bufCtr<bufSize)
      if(inputBuf[bufCtr++]=='\t')
      {
        if((bufCtr<bufSize) && inputBuf[bufCtr]!='\t')
          nTabs++;
      }
    
    return(nTabs);
  } /* countNonNullTabData */
  
  
  /**
   * parseTabDelimDataFromString() - basic parse tab delim data from raw string
   * @param sLine line to parse out delimited data
   * @return String[]
   */
  public static String[] parseTabDelimDataFromString(String rawData)
  { /* parseTabDelimDataFromString */
    /* [1] Convert it to char buffer */
    char
      ch,
      inputBuf[]= rawData.toCharArray(),    /* cvt input string to char[] */
      tokBuf[]= new char[1000],             /* token buffer */
      lineBuf[]= new char[10000];           /* line buffer */
    int
      c= 0,		                    /* running count of cols */
      bufSize= rawData.length(),          /* size of input buffer */
      bufCtr= 0,                          /* working input buffer index */
      tokSize= 0,                         /* size of tokBuf */
      lineSize= 0,                        /* size of lineBuf */
      lineCtr= 0;                         /* working line buffer index */
    String
      fields[]= new String[500],          /* more than ever need */
      token;                              /* field delimited token */
    
    /* [3] Parse data from buffer into field and data tables */    
    boolean
      doneFlag= false,
      sawNonSpaceLeadingChar;
    
    /* [3.1] Get a list of the tab-delimited Field names. */
    while(bufCtr<bufSize && !doneFlag)
    { /* get and store field names*/
      tokSize= 0;
      sawNonSpaceLeadingChar= false;
      
      while(bufCtr<bufSize && !doneFlag &&
           (ch= inputBuf[bufCtr])!='\t' && ch!='\n' && ch!='\r')
      { /* build token for field name */
        bufCtr++;
        if(ch=='"')
          continue;
        if(!sawNonSpaceLeadingChar)
        { /* handle leading white space */
          if(ch==' ')
            continue;
          else
            sawNonSpaceLeadingChar= true;
        } /* handle leading white space */
        else if(ch=='\n' || ch=='\r' || ch=='"' || ch=='\0')
        {
          doneFlag= true;
          break;
        }
        tokBuf[tokSize++]= ch;             /* save all non-space leading
                                            * chars up to TAB or EOL */
      } /* build token for field name */
      
      tokBuf[tokSize]= '\0';                /* terminate token */
      if(bufCtr==bufSize || inputBuf[bufCtr]=='\t')
        bufCtr++;		                        /* move past TAB */
      else if(bufCtr==bufSize || inputBuf[bufCtr]=='\n')
      { /* get rid of LF */
        // Possible Error GT what abt \r?
        doneFlag= true;
      }
      
      /* Rmv trailing whitespace by adjusting tokSize downward */
      for(int i=tokSize;i>=0;i--)
      { /* find first non-space character from the end */
        if(tokBuf[i]!=' ')
          break;
        else tokSize--;
      }
      
      if(tokSize<=0)
        token= "";                            /* change null to "" */
      else
        token= new String(tokBuf,0,tokSize);
      
      fields[c++]= token;                     /* i.e. save field name */
    } /* get and store field names*/
    
    return(fields);
  }  /* parseTabDelimDataFromString */
  
  
  /**
   * CountLineInFile() - open file and count number of lines & tabs<BR>
   * Set estLinesInFile to # lines found.<BR>
   * Set estSamplesRowNbr to estimated row # where Samples are listed.<BR>
   * Set estFieldsRowNbr to estimated row # where Fields are listed. <BR>
   *<p>
   * Try to estimate row numbers for Samples row and for Field row
   * If lookForSampleTabsFlag is FALSE, it assumes that
   * the first row with initial max number tabs with non-null data
   * is the Fields row, otherwise] it is the Samples row.
   *<p>
   * Note that the Samples row may contain fewer tabs than the Fields
   * row - but it is assumed that both contain more tabs than any
   * other row. When we have satisfied the initial counts, just
   * stop analyzing the tab counts to make it run MUCH MUCH faster.
   *
   * @param cvt instance of Cvt2Mae
   * @param fileName file to count
   * @param lookForSampleTabsFlag if true, look for sample tabs
   * @param msg message for logMsg
   * @param color is the logMsg color
   * @return number of lines in file else 0.
   * @see UtilCM#logMsg
   * @see #countNonNullTabData
   */
  public static int CountLineInFile(String fileName)
  { /* CountLineInFile */
    String rawData;
    int nLines= 0;                 /* running line counter */
    
    /* [1] Open file and read the row r line of data */    
    try
    { /* get the row r line of data */
      FileReader file= new FileReader(fileName);
      BufferedReader buffer= new BufferedReader(file);
      
      /* [1.1] get 1 line at a time until eof */
      do
      { /* read lines until the end of the file */
        rawData= buffer.readLine();  /* read and toss lines */
        if(rawData==null)
          continue;             /* stop since probably at the end */
        
        nLines++;               /* count lines */
      } /* read lines until the end of the file */
      while(rawData!=null);
      
      file.close();
    } /* get the row r line of data */
    
    catch (Exception e)
    {
      return(0);                  /* failed */
    }
    return(nLines);
  } /* CountLineInFile */
  
  
  /**
   * readTableFieldsFromFile() - open file and read row r tab-delim data.
   * @param fileName file name
   * @param r row to read
   * @return rFields is array of fields to return if successful else null.
   */
  public String[] readTableFieldsFromFile(String fileName, int r)
  { /* readTableFieldsFromFile */
    String rawData= null;
    
    /* [1] Read the row r line of data */
    try
    { /* get the row r line of data */
      FileReader file= new FileReader(fileName);
      BufferedReader buffer= new BufferedReader(file);
      for(int i= 0;i<r;i++)
        rawData= buffer.readLine(); /* read the file to string */
      
      if(rawData==null || rawData.length()==0)
        return(null);
      else
        file.close();
    } /* get the row r line of data */
    
    catch (Exception e)
    {
      return(null);
    }
    
    /* [2] Convert it to char buffer */
    char
      ch,
      inputBuf[]= rawData.toCharArray(),    /* cvt input string to char[] */
      tokBuf[]= new char[1000],             /* token buffer */
      lineBuf[]= new char[10000];           /* line buffer */
    int 
      c= 0,		                    /* running count of cols */
      bufSize= rawData.length(),          /* size of input buffer */
      bufCtr= 0,                          /* working input buffer index */
      tokSize= 0,                         /* size of tokBuf */
      lineSize= 0,                        /* size of lineBuf */
      lineCtr= 0;                         /* working line buffer index */
    String
      fields[]= new String[500],          /* more than ever need */
      token;                              /* field delimited token */
    
    /* [3] Parse data from buffer into field and data tables */
    
    boolean
      doneFlag= false,
      sawNonSpaceLeadingChar;
    
    /* [3.1] Get a list of the tab-delimited Field names */
    while(bufCtr<bufSize && !doneFlag)
    { /* get and store field names*/
      tokSize= 0;
      sawNonSpaceLeadingChar= false;
      
      while(bufCtr<bufSize && !doneFlag &&
            (ch= inputBuf[bufCtr])!='\t' && ch!='\n' && ch!='\r')
      { /* build token for field name */
        bufCtr++;
        if(ch=='"')
          continue;
        if(!sawNonSpaceLeadingChar)
        { /* handle leading whiteb space */
          if(ch==' ')
            continue;
          else
            sawNonSpaceLeadingChar= true;
        } /* handle leading white space */
        else if(ch=='\n' || ch=='\r' || ch=='"' || ch=='\0')
        {
          doneFlag= true;
          break;
        }
        tokBuf[tokSize++]= ch;               /* save all non-space leading
         * chars up to TAB or EOL */
      } /* build token for field name */
      
      tokBuf[tokSize]= '\0';          /* terminate token */
      if(bufCtr==bufSize || inputBuf[bufCtr]=='\t')
        bufCtr++;		                         /* move past TAB */
      else if(bufCtr==bufSize || inputBuf[bufCtr]=='\n')
      { /* get rid of LF */
        // Possible Error GT what abt \r?
        doneFlag= true;
      }
      
      /* Rmv trailing whitespace by adjusting tokSize downward for the field names */
      for(int i=tokSize;i>=0;i--)
      { /* find first non-space character from the end */
        if(tokBuf[i]!=' ')
          break;
        else tokSize--;
      }
      
      if(tokSize<=0)
        token= "";                              /* change null to "" */
      else
        token= new String(tokBuf,0,tokSize);
      
      fields[c++]= token;                        /* i.e. save field name */
    } /* get and store field names*/
    
    /* [4] Copy to new global field list of exactly the right size */
    String rFields[]= new String[c];
    for(int i= 0;i<c; i++)
    {
      rFields[i]= fields[i];
    }
    
    return(rFields);
  } /* readTableFieldsFromFile */
  
  
  /**
   * createTempAffyFileForMAS5() - Create tmp file in MAS-4 format to fake Cvt2Mae iwhen cvt  MAS-5 data.
   * It takes the "sample_Signal sample_Detection" and "Sample_Detection p-value"
   * and creates a sample row with the "sample" name above "Signal" where it
   * strips the sample_" from each of the symbols. This then matches the expected
   * Cvt2Mae multi-sample format.
   * @param dirName directory name
   * @param fileName is the file name
   * @return true if ok, else false if error
   */
  public boolean createTempAffyFileForMAS5(String dirNameA, String fileNameA,
                                           String tmpFileNameA, String dirNameB, 
                                           String fileNameB, String tmpFileNameB)
  { /* createTempAffyFileForMAS5 */
    int
      lineNbr= 0,                                /* for counting lines in B chip */
      iCols= 0,
      oCols= 0,
      idxDescriptions= 0,
      idxGenbankAcc= -1,
      idxSwissProtID= -1,
      nChips= (tmpFileNameB==null)  ? 1 : 2;
    String
      sampleName[]= new String[MAX_FIELD_SIZE],   /* will output to row 1 */
      fieldName[]= new String[MAX_FIELD_SIZE],    /* will output to row 2 */
      inFields[],                                 /* input from row 1 */
      sLine,
      dirName= dirNameA,                          /* default if using a single chip */
      fileName= fileNameA,
      tmpFileName= tmpFileNameA;  
    File outFile= null;
    FileWriter fw= null;
    
    /* [1] Parse sample names and field names from first line/row */
    //nChips= 1;   /* [WHILE DEBUGGING] */   
    
    for(int n=1;n<=nChips;n++)
    { /* process A and possibly B chips */
      if(n==1)
      { /* use A chip */
        dirName= dirNameA;
        fileName= fileNameA;
        tmpFileName= tmpFileNameA;
      }
      else
      {/* use B chip */
        dirName= dirNameB;
        fileName= fileNameB;
        tmpFileName= tmpFileNameB;
      }
 
      lineNbr= 0;       
      FileReader inFile= null;
      BufferedReader inBuf= null;
      try
      { /* get the row r line of data */
        inFile= new FileReader(dirName + fileName);
        inBuf= new BufferedReader(inFile);
        
        sLine= inBuf.readLine();   /* first row will have the field and sample names to be parsed */
        lineNbr++;
        sLine= UtilCM.removeQuotes(sLine);
        if(sLine!=null)
        {/* parse out each col */
          /* find out how many cols there are by counting tabs */
          iCols= countNonNullTabData(sLine);
          oCols= iCols;
           
          /* sample MAS-5 tab-delim. data looks like:
           *    sample1-1B_Signal sample1-1B_Detection sample1-1B_Detection p-value ... Descriptions
           */
          inFields= parseTabDelimDataFromString(sLine);          
          /* if(cvt.DBUG_FLAG)
            for(int x= 0; x<cols ;x++)
              System.out.println("cols= " + cols + " fields" + "[" + x +"]= " + inFields[x]);
           */
        } /* parse out each col */
        else
        {
          inFile.close();
          return(false);
        }
      } /* get the row r line of data */
      
      catch (Exception e)
      { return(false); }
      
      /* [2] Parse sample names out of the first row of fields[] data. */
      for(int x= 0; x<=iCols ;x++)
      { /* create sample list */
        String item= inFields[x];
        
        sampleName[x]= "";   /* no sample name is the default */
        fieldName[x]= item;  /* the default is to copy the field name */
        
        /* If find a special case, then overwrite with the field name less the "sample_" */
        if(item.endsWith("_Signal") )
        {
          int  index= item.indexOf("Signal");
          fieldName[x]= ("Signal");
          sampleName[x]= item.substring(0,index-1);  /* save sample name */
        }
        else if( item.endsWith("_Detection") )
          fieldName[x]= ("Detection");
        else if( item.endsWith("_Detection p-value") )
          fieldName[x]= ("Detection p-value");
        
        else if( item.endsWith("Descriptions") )
        {
          idxDescriptions= x;
          idxGenbankAcc= x+1;
          idxSwissProtID= x+2;
          oCols= iCols+2;
        }
      } /* create sample list */
      
      /* [TODO] add other ids, e.g. LocusID, UniGeneID, etc. */
      fieldName[0]= "probe set";   /* force missing data */
      fieldName[idxGenbankAcc]= "GenBankAcc";
      fieldName[idxSwissProtID]= "SwissProtID";
      sampleName[idxGenbankAcc]= "";
      sampleName[idxSwissProtID]= "";
      
      FieldMap fm= cvt.mcd.fm;
      //fm.addEntry("GipoTable", "probe_setID", "GipoTable", "probe set");
      fm.addEntry("GipoTable", "GenBankAcc", "GipoTable", "GenBankAcc");
      fm.addEntry("GipoTable", "SwissProtID", "GipoTable", "SwissProtID");
      
      if(cvt.DBUG_FLAG)
      {
        System.out.println("iCols= " + iCols+" oCols= " + oCols);
        for(int y= 0; y<=iCols; y++)
          System.out.println(" inFields[" + y +"]= '" + inFields[y]+"'");
        for(int y= 0; y<=oCols; y++)
          System.out.println(" sampleName[" + y +"]= '" + sampleName[y]+"'");
        for(int c= 0; c<=oCols; c++)
          System.out.println("[" + c +"]= '" + fieldName[c]+"'");
      }
      
      /* [3] Create file in the older format from the new format */
      try
      { /* write out row into each tmp file */
        /* [1] setup file writer */
        /* open output file and write out fields line */
        if(n==1 && fw==null)
        { /* only create output file for first input file */
          outFile= new File( tmpFileName);
          fw= new FileWriter(outFile);
        }
        
        /* write out the samples and the field names in rows 1 and 2 */
        String
          sSamples= "",
          sFields= "",
          oLine= "",
          descr= "",
          gbID= "",
          spID= "",
          sTmp;
        int
          strIdx= -1,
          hsLth= "Homologous to sp ".length(),
          idxColon;
        for(int z=0; z<=oCols; z++)
        {
          sSamples += sampleName[z] + ((z<oCols) ? "\t" : "\n");
          sFields += fieldName[z] + ((z<oCols) ? "\t" : "\n");
        }
        if(n==1)
        { /* write out header only for first file */
          fw.write(sSamples);
          fw.write(sFields);
        }
        
        while((sLine= inBuf.readLine())!=null)
        { /* parse out extra ids from Description and write the line */
          lineNbr++;
          sLine= UtilCM.removeQuotes(sLine);
          oLine= sLine;
          
          /* Parse out extra ids from Description and write the line.
           * Get the description data by search for the last \t in the sLine
           * and descr is all data to right of that.
           */
          strIdx= sLine.lastIndexOf("\t");
          descr= sLine.substring(strIdx+1);
          gbID= null;   //"GB_ID";   /* [DBUGGING] replace these two with null */
          spID= null;   //"SP_ID";
          
          /* For input desccription data
           *   "M60469 Mouse tumor necrosis factor receptor 2 mRNA, complete cds"
           * 1) parse <letter><5 digits><space> to find the GenBankAcc id value
           */
          if(descr.length()>=6 && Character.isLetter(descr.charAt(0)) &&
             Character.isDigit(descr.charAt(1)) &&
             Character.isDigit(descr.charAt(2)) &&
             Character.isDigit(descr.charAt(3)) &&
             Character.isDigit(descr.charAt(4)) &&
             Character.isDigit(descr.charAt(5)) &&
             Character.isSpace(descr.charAt(6)) )
          {
            gbID= descr.substring(0,6);
          }
          
           /* And for data
            *    Homologous to sp P38377: PROTEIN TRANSPORT PROTEIN SEC61 ALPHA SUBUNIT."
            * 2) parse <"Homologous to sp "><swiss-prot-id>":"
            */
          if(descr.length()>hsLth && descr.startsWith("Homologous to sp "))
          {
            sTmp= descr.substring(hsLth);
            if(sTmp!=null && (idxColon= sTmp.indexOf(":"))!=-1)
              spID= sTmp.substring(0,idxColon);
          }
          
          oLine = sLine + "\t";
          if(gbID!=null)
            oLine += gbID;
          oLine += "\t";
          if(spID!=null)
            oLine += spID;
          fw.write(oLine + "\n");
        }
      } /* write out row into each file */
      
      catch(IOException ioe)
      { return(false); }
      
    } /* process A and possibly B chips */
    
    /* [4] Close the output files */
    if(fw!=null)
    { /* close the output file */
      try
      { fw.close(); }
      catch(IOException ioe)
      {
        try
        { fw.close(); }
        catch (Exception e2)
        {}
        return(false);
      }
    } /* close the output file */
    
    /* [5] filename to temp so it will  now read it instead of what user picked */
    return(true);
  } /* createTempAffyFileForMAS5 */
  
  
  /**
   * getGIPOsrcFileFromDialog() - get source  GIPO file using dialog popup
   * @return GIPO source file name else null
   */
  public String getGIPOsrcFileFromDialog()
  { /* getGIPOsrcFileFromDialog */
    fdI= new FileDialog(this, "Enter seperate input GIPO file to convert");
    if(mcd.dirName!=null && mcd.dirName.length()!=0)
      fdI.setDirectory(mcd.dirName);
    
    if(cvt.USE_DBUG_DIR && mcd.nSrcFiles<1)
    { /* ONLY when debugging first file */
      mcd.dirName= cvt.DBUG_IN_DIR;
      fdI.setDirectory(mcd.dirName);
    }
    fdI.setMode(FileDialog.LOAD);
    /*
    if(cvt.DBUG_FLAG)
      util.logMsg("C2M-AP fdI.getDirectory()="+fdI.getDirectory());
    */
    fdI.setVisible(true);
    
    String
      dName= fdI.getDirectory(),      /* get selected results */
      fName= fdI.getFile();
    if(dName==null || fName==null)
      return(null);
    
    String gipoSrcFile= dName + fName;
    
    return(gipoSrcFile);
  } /* getGIPOsrcFileFromDialog */
  
  
  /**
   * getNewDirSrcFilesFromgetGIPOsrcFileFromDialog() - get source GIPO file using
   * dialog popupDialog()(get new source files from directory
   * using dialog popup). This gets all files in the directory.
   * NOTE: some of these may not be valid data files. So we do NOT
   * analyze the samples until we give the user a chance to
   * "Remove files" or "Remove Samples" on these files.
   * @see #addSourceFileToList
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   */
  public void getNewDirSrcFilesFromDialog()
  { /* getNewDirSrcFilesFromDialog */
    /* [TODO] This code is not valid - needs to be rewritten....  */
    
    util.logMsg("Adding all files in directory. Remove files not wanted.",
                Color.black);
    util.logMsg("Continue adding input files (step 2),",
                Color.black);
    util.logMsg3("or define Output Folder (step 3) when done adding files.",
                 Color.black);
    fdI= new FileDialog(this, "Select the input folder containing files to convert");
    if(mcd.dirName!=null && mcd.dirName.length()!=0)
      fdI.setDirectory(mcd.dirName);
    if(cvt.USE_DBUG_DIR && mcd.nSrcFiles<1)
    { /* ONLY when debugging first file */
      mcd.dirName= cvt.DBUG_IN_DIR;
      fdI.setDirectory(mcd.dirName);
    }
    fdI.setMode(FileDialog.SAVE);
    fdI.setVisible(true);
    
    String
      dName= fdI.getDirectory(),     /* get selected results */
      fNames[]= null;
    if(dName==null)
      return;
    try
    { /* lookup the files in the directory */
      File
      f= new File(dName);
      if(f.isDirectory())
      {
        fNames= f.list();
        /*
        if(cvt.DBUG_FLAG)
        {
          System.out.println("Input folder='"+dName+"'");
          for(int k=0;k<fNames.length;k++)
            System.out.println(" I.F.#"+(k+1)+"='"+fNames[k]+"'");
        }
        */
      }
    } /* lookup the files in the directory */
    
    catch (Exception ef)
    {
    }
    
    if(fNames==null)
      return;
    
    for(int i=0; i<fNames.length; i++)
    { /* Add new file to list and do intermediate processing */
      util.logMsg("Adding file #"+i+" ["+fNames[i]+"]",Color.black);
      util.logMsg2("",Color.black);
      util.logMsg3("", Color.black);
      
     /* [TODO] avoid doing full processing of the files
      * since we may want to delete them prior to analyzing
      * them.
      * [NOTE] the analyzeFilesFlag is set to FALSE!
      */
      addSourceFileToList(dName, fNames[i], null /* mcd.tmpSrcFile[i] */, false, false);
    } /* Add new file to list and do intermediate processing */
  } /* getNewDirSrcFilesFromDialog */
  
  
  /**
   * removeALOfileFromArrayLayoutDirectory() - remove ALO selected from /ArrayLayout dir.
   * @return true if successful
   * @see SetupLayouts#removeALOfileFromALOlist
   * @see UtilCM#logMsg
   */
  public boolean removeALOfileFromArrayLayoutDirectory()
  { /* removeALOfileFromArrayLayoutDirectory */
    FileDialog
    fd= new FileDialog(this, "Select Array Layout file to delete");
    fd.setDirectory(mcd.aloDir);
    fd.setMode(FileDialog.LOAD);
    fd.setVisible(true);
    
    String
      dName= fd.getDirectory(),      /* get selected results */
      aloFileName= fd.getFile(),
     fullFile= dName+aloFileName;
    
    if(aloFileName==null)
      return(false);
    else
    { /* delete the array layout on disk and in core */
      /* [1] delete the file */
      try
      {
        File
        f= new File(fullFile);
        if(f!=null)
          f.delete();
      }
      catch (Exception e)
      {
        cvt.util.logMsg("Can't delete file ["+fullFile+"]",Color.red);
        return(false);
      }
      
      /* [2] refresh the chipsetChoice list and set list to unspecified. */
      boolean foundIt= false;
      for(int i=sul.firstUserALO;i<sul.maxAL;i++)
        if(aloFileName.equals(sul.alList[i].aloFileName))
        { /* found it */
          String aloName= sul.alList[i].layoutName;
          try
          {
            chipsetChoice.remove(aloName);      /* note: variable #*/
          }
          catch(Exception e)
          { /* ignore if item is not found in the choice list */
          }
          foundIt= true;
          break;
        }
      if(!foundIt)
      {
        cvt.util.logMsg("Can't remove internal Array Layout ["+aloFileName+"]",
                        Color.red);
        return(false);
      }
      sul.useAL= 0;                                       /* force default */
      chipsetChoice.select("-- select a chip layout --"); /* default */
      
      /* [3] fixup the in-core database by removing the entry */
      sul.removeALOfileFromALOlist(aloFileName);
      cvt.util.logMsg("Removed Array Layout ["+aloFileName+"]", Color.black);
    } /* delete the array layout on disk and in core */
    return(true);
    
  } /* removeALOfileFromArrayLayoutDirectory */
  
  
  /**
   * actionPerformed() - event handler for Buttons
   * @param e is ActionEvent for button press
   * @see EditMaeProjectFiles
   * @see MaeConfigData#checkIfRequiredParamsAreSet
   * @see MaeConfigData#resetMaeConfigData
   * @see PopupDialog
   * @see PopupGetSepGIPOnamesForGridRowCol
   * @see SetupLayouts#getMaeToUserFields
   * @see TextFrame#appendLog
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   * @see #analyzeGIPOfileHeader
   * @see #getNewSrcFileFromDialog
   * @see #renameSample
   * @see #removeALOfileFromArrayLayoutDirectory
   * @see #removeSample
   * @see #runProcessData
   */
  public void actionPerformed(ActionEvent e)
  { /* actionPerformed */
    /* [TODO] do not allow Run until finished editing */
    Button itemB= (Button)e.getSource();
    
    util.logMsg("", Color.black);   /* clear out the message area */
    util.logMsg2("", Color.black);
    util.logMsg3("", Color.black);
    
    if(itemB==removeSampleButton)
    { /* remove selected sample if any */
      removeSample();
      return;
    }
    
    else if(itemB==renameSampleButton)
    { /* rename selected sample if any */
      renameSample();
      return;
    }
    
    else if(itemB==removeAloButton)
    { /* rename selected Array Layout by searching with Dialog popup if any */
      removeALOfileFromArrayLayoutDirectory();
      return;
    }
    
    else if(itemB==runButton)
    { /* run button */
      util.logMsg2("", Color.black); /* clear it */
      util.logMsg3("", Color.black);
      
      /* check to see if user has created a new layout, make sure recalc.
       * button has been pressed.*/
      
      /* [TODO]
      if(mcd.activeReanalyeButtonFlag==false && mcd.hasUserDefineChipsetFlag)
      {
         PopupDialog pd= new PopupDialog();     
         pd.openDialog("Reminder, you first need to click on the recalc button for <user-def>");
      }
      */
      
      if(mcd.checkIfRequiredParamsAreSet())
        runProcessData(); /* process the input data into output files */
    } /* run button */
    
    else if(itemB==editLayoutButton)
    { /* edit layout */
      if(!mcd.editMaePrjFilesFlag)
        return;   /* only 1 at a time */
      
      if(mcd.hasSeparateGIPOandQuantFilesFlag && mcd.separateGIPOinputFile==null)
      { /* Missing GIPO file since specified separate GIPO with checkbox */
        cvt.util.logMsg("You have not defined the separate GIPO file.", Color.red);
        cvt.util.logMsg2("Go 'Browse GIPO file' to define it, then try again.",
                         Color.red);
        return;
      }
      
      mcd.editMaePrjFilesFlag= false;    /* edit only 1 Layout window at a time */
      
     /* Make copy of variables that need to restore the
      * mcd state that may be changed by using the Edit Wizard
      * but that need to be put back to this state if press
      * the RESET button.
      */
      origMcd= mcd.resetMaeConfigData(mcd);
      
      EditMaeProjectFiles empf= new EditMaeProjectFiles(null, mcd, origMcd, false,
                                                        "Edit MAExplorer project");
      /* [TODO] do not allow Run until finished editing */
    } /* edit layout */
    
    else if(itemB==saveArrayLayoutButton)
    { /* save edited array layout */
      String aloFileName= sul.alList[sul.useAL].aloFileName;
      if(aloFileName==null || aloFileName.length()==0)
      {
        util.logMsg("Can't save Array Layout.",Color.red);
        util.logMsg2("Re-edit layout - define array layout name ["+aloFileName+"]",
                     Color.red);
        return;
      }
      else if(mcd.editedLayoutFlag)
      { /* make sure name is not the same as any default name */
        if(sul.useAL>=sul.firstAL && sul.useAL<=sul.lastAL)
        { /* trying to save edited default ALO */
          for(int k=0;k<sul.nALOfilesFound;k++)
            if(aloFileName.equals(sul.aloFile[k]))
            {
              util.logMsg("Can't save Array Layout under same name as default array.",
                           Color.red);
              util.logMsg2("Re-edit layout - change array layout name ["+
                           aloFileName+"]", Color.red);
              return;
            }
        }
        sul.writeArrayLayout(sul.alList[sul.useAL], true);
      }
    } /* save edited array layout */
    
    else if(itemB==editSepGIPOfieldsButton)
    { /* re-edit the separate GIPO fields */
      
      /* [7] setup the sepGIPOfieldsUsed[0:nSepGIPOfields-1] flags that
       * are set if include fields in separate GIPO when merge data when
       * generate the (FieldG+FieldQ) data in readData().
       * I.e. do NOT include (Block, Row, Col) in GIPO if (Grid, Row, Col)
       * exist in the Quant file.
       */
      if(mcd.sepGipoGridName==null)
        mcd.sepGipoGridName= "Block";
      if(mcd.sepGipoRowName==null)
        mcd.sepGipoRowName= "Row";
      if(mcd.sepGipoColName==null)
        mcd.sepGipoColName= "Column";
      
      PopupGetSepGIPOnamesForGridRowCol
      pgNames= new PopupGetSepGIPOnamesForGridRowCol(cvt, this,
                                                     mcd.sepGipoGridName,
                                                     mcd.sepGipoRowName,
                                                     mcd.sepGipoColName,
                                                     true);
      mcd.sepGipoGridName= pgNames.sepGipoGridName;
      mcd.sepGipoRowName= pgNames.sepGipoRowName;
      mcd.sepGipoColName= pgNames.sepGipoColName;
      
      String sBadFields= "";
      if(!pgNames.gridFlag)
        sBadFields = "Grid ";
      if(!pgNames.rowFlag)
        sBadFields += "Row ";
      if(!pgNames.colFlag)
        sBadFields += "Column ";
      
      mcd.nSepGIPOfieldsUsed= 0;
      for(int i=0;i<mcd.nSepGIPOfields;i++)
      { /* build Use GIPO token map and # actually used */
        String fName= mcd.sepGIPOfields[i];
        boolean flag= (!fName.equals(mcd.sepGipoGridName) &&
                       !fName.equals(mcd.sepGipoRowName)  &&
                       !fName.equals(mcd.sepGipoColName));
        mcd.sepGIPOfieldsUsed[i]= flag;  /* include all but dupl. stuff */
        if(flag)
          mcd.nSepGIPOfieldsUsed++;      /* # actually used */
      }/* build Use GIPO token map and # actually used */
    } /* re-edit the separate GIPO fields */
    
    else if(itemB==mapGipoFieldsButton)
    { /* "Assign GIPO Fields" map user to MAE fields */
      boolean flag= true;
      
      if(mcd.rowWithFields > mcd.rowWithData)
      { /* Missing Quant file Field and Data rows out of order */
        cvt.util.logMsg("The row number for the quantified data file Field ["+
                        mcd.rowWithFields+"] is > Data row ["+ mcd.rowWithData+"].",
                        Color.red);
        cvt.util.logMsg2("Press 'Edit Layout' and edit panel 'Input file starting rows data'.",
                         Color.red);
        cvt.util.logMsg3("To correct this and then try again.",  Color.red);
        flag= false;
      }
      
      else if(mcd.rowWithFields<=0)
      { /* Missing Quant data file File row value */
        cvt.util.logMsg("The row # ["+ mcd.rowWithFields+"] for the quantified data file Field is not defined.",
                        Color.red);
        cvt.util.logMsg2("Press 'Edit Layout' and edit panel 'Input file starting rows data'.",
                         Color.red);
        cvt.util.logMsg3("To correct this and then try again.", Color.red);
        flag= false;
      }
      
      else if(mcd.rowWithData<=0)
      { /* Missing Quant data file Data row value */
        cvt.util.logMsg("The row # ["+ mcd.rowWithData+"] for the quantified data file Data is not defined.",
                        Color.red);
        cvt.util.logMsg2("Press 'Edit Layout' and edit panel 'Input file starting rows data'.",
                         Color.red);
        cvt.util.logMsg3("To correct this and then try again.",  Color.red);
        flag= false;
      }
      
      if(flag)
      { /* map GIPO fields */
        String fName;
        int gipoFieldRow;
        if(! mcd.gipoInGeneralSrcFileFlag)
        {/* separate GIPO file */
          fName= mcd.separateGIPOinputFile;                   
          gipoFieldRow= mcd.rowWithSepGIPOFields;
        }
        else
        { /* GIPO is part of quant source file */
          fName= (mcd.quantSrcDir[0] + mcd.quantSrcFile[0]);  /* same file */       
          gipoFieldRow= mcd.rowWithFields;
        }
        sul.getMaeToUserFields(fName, mcd.fm, gipoFieldRow, sul.REMAP_GIPO, 
                               cvt.useMaxFieldsRequiredFlag);
      }
    } /* map user to MAE fields */
    
    else if(itemB==mapQuantFieldsButton)
    { /* "Assign Quant Fields" map user to MAE fields */
      boolean flag= true;
      
      if(mcd.rowWithFields > mcd.rowWithData)
      { /* Missing Quant file Field and Data rows out of order */
        cvt.util.logMsg("The row number for the quantified data file Field ["+
                        mcd.rowWithFields+"] is > Data row ["+ mcd.rowWithData+"].",
                        Color.red);
        cvt.util.logMsg2("Press 'Edit Layout' and edit panel 'Input file starting rows data'.",
                         Color.red);
        cvt.util.logMsg3("To correct this and then try again.", Color.red);
        flag= false;
      }
      
      else if(mcd.rowWithFields<=0)
      { /* Missing Quant data file File row value */
        cvt.util.logMsg("The row # ["+ mcd.rowWithFields+"] for the quantified data file Field is not defined.",
                        Color.red);
        cvt.util.logMsg2("Press 'Edit Layout' and edit panel 'Input file starting rows data'.",
                         Color.red);
        cvt.util.logMsg3("To correct this and then try again.", Color.red);
        flag= false;
      }
      
      else if(mcd.rowWithData<=0)
      { /* Missing Quant data file Data row value */
        cvt.util.logMsg("The row # ["+ mcd.rowWithData+"] for the quantified data file Data is not defined.",
                        Color.red);
        cvt.util.logMsg2("Press 'Edit Layout' and edit panel 'Input file starting rows data'.",
                         Color.red);
        cvt.util.logMsg3("To correct this and then try again.", Color.red);
        flag= false;
      }
      if(flag)
      { /* remap quant data */
        String fName= (mcd.quantSrcDir[0] + mcd.quantSrcFile[0]);
        sul.getMaeToUserFields(fName, mcd.fm, mcd.rowWithFields, sul.REMAP_QUANT,
                               cvt.useMaxFieldsRequiredFlag);
      }
    } /* map user to MAE fields */
    
    else if(itemB==resetButton)
    { /* "Reset" button */
      Cvt2Mae oldCvt= cvt;
      cvt.init();                    /* startup new instance */
      oldCvt.gui.dispose();
      oldCvt.gui= null;              /* clean up so can G.C. */
      oldCvt.mcd.msd= null;
      oldCvt.mcd.msud= null;
      oldCvt.mcd= null;
      oldCvt.sul= null;
      oldCvt.dio= null;
      oldCvt.fmg= null;
      oldCvt.util= null;
      oldCvt.fmg= null;
      System.gc();
    } /* "Reset" button */
    
    else if(itemB==updateCvt2MaeButton)
    { /* "Update Cvt2Mae" */ 
      util.updateCvt2MaeProgram();
    }
    
    else if(itemB==exitButton)
    { /* "Done" or "Abort" */
      if(err!=null)
        err.appendLog("exit",true);
      if(mcd.useSrcTmpFileFlag)
      {
        try
        { /* remove the tmp affy files if possible */
          for(int i=0;i<mcd.nQuantFiles;i++)
          {
            File file= new File(mcd.tmpSrcFile[i]);
            file.delete();
          }
        }
        catch (Exception eD)
        { }
      }
      System.exit(0);
    }
    
    else if(itemB==browseSrcFileButton)
    { /* get full path = directory & specific file using dialog popup */
      String arrayLayoutStr= chipsetChoice.getSelectedItem();
      
      /* <User-defined> can have mult data or not*/
      if(arrayLayoutStr.equals("<User-defined>"))
      {
        mcd.hasMultDatasetsFlag= multSamplesPerFileCB.getState();
        editLayoutButton.setEnabled(true);
      }
      getNewSrcFileFromDialog();     /* call each time press button */
    } /* get full path = directory & specific file using dialog popup */
    
    else if(itemB==browseGIPOsrcFileButton)
    { /* get source GIPO file using dialog popup and analyze header */
      mcd.separateGIPOinputFile= getGIPOsrcFileFromDialog();
      if(mcd.separateGIPOinputFile==null)
      { /* disable separate GIPO file */
        sepGipoQuantFilesCB.setState(false);
        util.logMsg("No GIPO file was specified - not using separate GIPO file",
                    Color.black);
        return;                      /*FORGETABOUTIT */
      }
      
    /* Analyze the GIPO file header and setup fieldNameGIPO[], starting FieldG
     * row and the starting row for the GIPO data.
     */
      analyzeGIPOfileHeader();
      String arrayLayoutStr= chipsetChoice.getSelectedItem();
      
      if(arrayLayoutStr.equals("<User-defined>"))
        editLayoutButton.setEnabled(true);   // not sure abt doing this
      
      editSepGIPOfieldsButton.setEnabled(mcd.hasSeparateGIPOandQuantFilesFlag);
    } /* get source GIPO file using dialog popup and analyze header */
    
    else if(itemB==analyzeSrcFilesButton)
    { /* re-analyze the source files since may have removed some */
      /* [TODO] reset to mult samples from 1st file.
       * We only reanalyze files after Edit Layout, Assign GIPO
       * fields, Assign Quant fieldes and we have pressed the
       * button.
       *
       * [CHECK] do we want to keep the call from
       * EditeMaeProjectFiles when change the row with samples
       * when have multiple samples?
       */
      reanalyzeSourceFileList(false /* not editing the mcd */);
      analyzeSrcFilesButton.setEnabled(false); /* clear it */
      saveArrayLayoutButton.setEnabled(false);
      mcd.activeReanalyeButtonFlag= false;
    }
    
    else
    {
      util.logMsg("G2M.actionPerformed() DRYROT! - BOGUS itemB", Color.red);
      util.logMsg((""+e), Color.red);
    }
  } /* actionPerformed */
  
  
  /**
   * windowClosing() - close down the window.
   * @param e is window closing event
   */
  public void windowClosing(WindowEvent e)
  {
    if(err!=null)
      err.appendLog("exit",true);
    System.exit(0);
  }
  
  /* Others not used at this time */
  public void windowOpened(WindowEvent e)  { }
  public void windowActivated(WindowEvent e)  { }
  public void windowClosed(WindowEvent e)  { }
  public void windowDeactivated(WindowEvent e)  { }
  public void windowDeiconified(WindowEvent e)  { }
  public void windowIconified(WindowEvent e)  { }
  
  
  /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
  /*                 CLASS  PopupTextDialog                         */
  /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
  /**
   * This class displays a dialog window containing 2 buttons Ok and Cancel
   * as well as a TextField box to define text.
   */
  class PopupTextDialog extends Dialog
  implements ActionListener, WindowListener
  {
    /** link to globals */
    public CvtGUI
      gui;
    /** size of frame */
    public int
      width;
    /** size of frame */
    public int
      height;
    /** Frame */
    public Frame
      frame;
    /** for msg label */
    public Label
     label;
    /** for text field data */
    public TextField
      textField;
    /* Ok/Cancel flag */
    public boolean
      flag;
    /** button pressed flag */
    public boolean
      alertDone;
    /** wait for button to be pushed */
    public boolean
      sleepFlag;
    /** Tried this instead of "this" */
    public ActionListener
      listener;
    /** prompt */
    public String
      title;
    /** text in text widget */
    public String
      sText;
    /** 50 spaces */
    public String
      spaces;
    
    
    
    /**
     * PopupTextDialog() - Constructor for yes/no buttons
     * @param gui instance of CvtGUI
     * @param f frame
     * @see #startPopupTextDialog
     */
    PopupTextDialog(CvtGUI gui, Frame f)
    { /* PopupTextDialog */
      super(f,"dialog box",true);
      
      this.gui= gui;
      
      this.title= "                               ";
      this.sText= "";
      
      /* [1] set some defaults */
      width= 200;
      height= 35;
      frame= f;
      alertDone= false;
      flag= true;
      spaces= "                                                        ";
      
      /* [2] create popup and hide it for use later */
      this.startPopupTextDialog("TextDialog");
    } /* PopupTextDialog */
    
    
    /**
     * startPopupTextDialogOK() - create a hidden dialog panel within a frame.
     * This is reused each time we popup a panel.
     * @param windowTitle is the window title
     */
    public void startPopupTextDialogOK(String windowTitle)
    
    { /* startPopupTextDialogOK */
      Panel buttonPanel= null;	/* place buttons here */
      Button ok;
      GridLayout  gl;           /* for layout of text fields, label, etc */      
      
      /* [1] initialize */
      gl= new GridLayout(3,1);
      this.setLayout(gl);	    /* set gridlayout to frame */
      
      /* [1] Create User instruction label */
      label= new Label(spaces);
      
      /* [2] Create the buttons and arrange to handle button clicks */
      buttonPanel= new Panel();
      
      ok= new Button("Ok");
      ok.addActionListener(this);
      buttonPanel.add("Center",ok);
      
      /* [3] add label msg to panel */
      this.add(label);
      
      /* [4] add buttons panel */
      if(buttonPanel!=null)
        this.add(buttonPanel);         /* buttons  */
      this.addWindowListener(this);    /* listener for window events */
      
      /* [5] add components and create frame */
      this.setTitle(windowTitle);     /* frame title */
      this.pack();
      
      /* Center frame on the screen, PC only */
      Dimension screen= Toolkit.getDefaultToolkit().getScreenSize();
      Point pos= new Point((screen.width-frame.getSize().width)/2,
                           (screen.height-frame.getSize().height)/2);
      this.setLocation(pos);
      
      this.setVisible(false); /* hide frame which can be shown later */
    } /* startPopupTextDialogOK */
    
    
    /**
     * startPopupTextDialog() - create a hidden dialog panel within a frame.
     * @param windowTitle is the title of this PopupTextDialog window
     */
    public void startPopupTextDialog(String windowTitle)
    { /* startPopupTextDialog */
      Panel buttonPanel= null;	 /* place buttons here */
      Button  
        okButton,		             /* update data */
        cancelButton;	           /* use default data */
      GridLayout gl;             /* for layout of text fields, label, etc */
            
      /* [1] initialize */
      gl= new GridLayout(3,1);
      this.setLayout(gl);	    /* set gridlayout to frame */
      
      /* [2] Create the buttons and arrange to handle button clicks */
      buttonPanel= new Panel();
      
      okButton= new Button("Ok");
      cancelButton= new Button("Cancel");
      
      okButton.addActionListener(this);
      buttonPanel.add("Center",okButton);
      cancelButton.addActionListener(this);
      buttonPanel.add(cancelButton);
      
      /* [3] Create User instruction label */
      label= new Label(spaces);
      this.add(label);
      
      /* [4] add label msg to panel */
      textField= new TextField(50);
      this.add(textField);
      
      /* [5] add buttons panel */
      if(buttonPanel!=null)
        this.add(buttonPanel);         /* buttons  */
      this.addWindowListener(this);    /* listener for window events */
      
      /* [6] add components and create frame */
      this.setTitle(windowTitle);     /* frame title */
      this.pack();
      
      /* Center frame on the screen, PC only */
      Dimension screen= Toolkit.getDefaultToolkit().getScreenSize();
      Point pos= new Point((screen.width-frame.getSize().width)/2,
                           (screen.height-frame.getSize().height)/2);
      this.setLocation(pos);
      
      this.setVisible(false); /* hide frame which can be shown later */
    } /* startPopupTextDialog */
    
    
    /**
     * updatePopupTextDialog() - display/unhide popup dialog frame and set new vals.
     * Remove recreate actionListeners &  components
     * @param title new title
     * @param sText new data in text field
     */
    public void updatePopupTextDialog(String title, String sText)
    { /* updatePopupTextDialog */
      alertDone= false;           /* reset the flag */
      flag= true;
      
      this.title= title;
      this.sText= sText;
      
      label.setText(title);      /* change label */
      textField.setText(sText);  /* change data */
      
      this.setVisible(true);		  /* display it; unhide it */
    } /* updatePopupTextDialog */
    
    
    /**
     * actionPerformed() - Handle button clicks
     * @param e action event
     */
    public void actionPerformed(ActionEvent e)
    { /* actionPerformed */
      String cmd= e.getActionCommand();
      
      /* [1] see which button was pushed and do the right thing & hide window  */
      if (cmd.equals("Ok"))
      {
        flag= true;
        sText= textField.getText();
        this.setVisible(false);/* hide frame which can be shown later */
      }
      else
        if(cmd.equals("Cancel"))
        {
          flag= false;
          this.setVisible(false);/* hide frame which can be shown later*/
        }
      
      alertDone= true;
    } /* actionPerformed */
    
    
    /**
     * windowClosing() - close down the window on PC only.
     * @param e Window closing Event
     */
    public void windowClosing(WindowEvent e)
    {
      e.getWindow().dispose();
    }
    
    
    /* Others not used at this time */
    public void windowOpened(WindowEvent e)  { }
    public void windowActivated(WindowEvent e)  { }
    public void windowClosed(WindowEvent e)  { }
    public void windowDeactivated(WindowEvent e)  { }
    public void windowDeiconified(WindowEvent e)  { }
    public void windowIconified(WindowEvent e)  { }
    
  } /* end of PopupTextDialog class */
  
  
}  /* end of class CvtGUI */

/** File: FieldMapGUI.java */

package cvt2mae;

import java.awt.*;
import java.awt.event.*;
import java.awt.List;
import java.io.*;
import java.util.*;

/**
 * GUI wizard for chooser data for FieldMap for GIPO and Quant data field name mapping.
 *<P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), G. Thornwall (SAIC), B. Stephens(SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $   $Revision: 1.10 $
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */

public class FieldMapGUI extends Frame implements ActionListener,
						  ItemListener, MouseListener, WindowListener
{ /* FieldMapGUI */
  
  /* [CHECK] partially implemented modal type frame. Need to have the
   * main program wait until press Done or Cancel.
   */
  
  /** global links */
  private Cvt2Mae
    cvt;                
  /** Global setup layouts DB */          
  private SetupLayouts
    sul;           
  /** size of mouseover TextArea */        
  final private int
    TA_ROWS= 7;
  /** size of mouseover TextArea */
  final private int
    TA_COLS= 50;	
  /** font size of muose over */
  final private int
    MOUSEOVER_FONT_SIZE= 9;	
  /** font type for labels */
  final private String
    LABELFONT= "Helvetica";
  /** font type for mouseover */
  final private String
    MOUSEOVERFONT= "SansSerif";	
  
  /** mouseover TextArea */
  private TextArea		
    ta;				
  /** actual Choice list */
  private Choice
    fieldChoiceList[];		
  /** actual label list */
  private Label
    fieldLabelList[];	
  /** main title */
  private Label
    titleLabel;		
  
  /** str to create choice list */
  private String
    fieldChoiceStr[];
  /** str to create label list */
  private String
    fieldLabelStr[];		
  /** choice list is diff size */
  private int
    fieldChoiceListIndex;
  /** num of labels &  # of choice lists */
  private int
    fieldIndex;			
  /** title */
  public String
    mainTitleStr;		
  /** holds current mouseover data */
  private String
    mouseoverList[];	
  
  /** main font for labels & choice lists*/
  private Font
    labelFont;
  /** mouseover font */
  private Font
    mouseOverFont;
  
  /** close window & return without data */
  private Button
    cancelButton;	
  /** popup a help window [TODO] */
  private Button
    helpButton;			
  /** close window & return with data */
  private Button
    doneButton;	
  
  /** holds title label */
  private Panel
    titlePanel;			
  /** main panel */
  private Panel
    centerPanel;		
  /** space on rt */
  private Panel
    rightPanel;			
  /** space on left*/
  private Panel
    leftPanel;			
  /** buttons at bottom */
  private Panel
    buttonPanel;		
  /** labels go here */
  private Panel
    fieldsLabelPanel;		
  /** choice lists go here */
  private Panel
    choicePanel;
  
  /** window for mouseover popup */
  private Window
    mouseoverWindow;	
  
  /** track picked choices, stops duplicates*/
  private int
    pickedIndex[];
  
  /** backgrd color for mouse over  */
  private Color
    mouseoverColor;	
  /** frame window size */
  private int  
    frameHeight;		
  /** frame window size */
  private int  
    frameWidth;			
  /** a popup help text window */
  private Frame
    helpWindow;	
  
  /** msg display in help window */
  private String
    helpMsg;			
  /** os type flag */
  private boolean
    isWinPCflag;		       
  /** string selected if unused */
  private String
    unusedStr;             
  
  /** for toggle allowing duplicate fields */
  private Checkbox
    duplicateChkBox;	
  
  /** Data that is read when it is ready */
  public boolean
    dataReadyFlag= false;	
  /** duplicate fields flag */
  public boolean
    duplicateFlag= true;	
  /**  */
  public boolean
    dectectionValueFlag= false;	
  /** number of FieldChoice selected */
  public int 
    numFieldChoiceSelected;
  /** the table we are remaping label name into MAE variable */  
  public int 
    remapMode;      
  
  /** RETURN data here */		
  public String
    rtnDataList[]= null;	
  
  
  /**
   * FieldMapGUI() - Constructor called from within Cvt2Mae with proper lists.
   * @param cvt parent
   * @param fieldLabelStr internal MAE field names array
   * @param fieldChoiceStr external user fields to choose from
   * @param mouseoverList display text when mouse ove
   * @param mainTitleStr title of popup Field GUI
   * @param helpMsg elp msg when press HELP button
   * @param unusedStr string selected if unused
   * @param remapMode the table we are remapaing
   * @see PopupFieldMapDialog
   * @see PopupFieldMapDialog#updatePopupFieldMapDialog
   * @see #initFM
   * @see #initFieldMapGUI
   */
  public FieldMapGUI(Cvt2Mae cvt, String fieldLabelStr[],
                     String fieldChoiceStr[], String mouseoverList[],
                     String mainTitleStr, String helpMsg, String unusedStr,
                     int remapMode)
  { /* Constructor */
    this.cvt= cvt;
    sul= cvt.sul;
    this.remapMode= remapMode;
    
    /* [1] get size of list */
    fieldIndex= fieldLabelStr.length;            /* # MAE fields to be mapped */    
    fieldChoiceListIndex= fieldChoiceStr.length; /* # user fields could map */
    
    if(mouseoverList!=null && mouseoverList.length!=fieldIndex)
    { /* create Mouseover popup */
      System.out.println("FieldMapGUI(): Error in list sizes, unequal");
      String msg="Possible error with lists, unequal sizes, click OK to close";
      Font font= new Font("Dialog", Font.BOLD, 10);
      
      PopupFieldMapDialog pd= new PopupFieldMapDialog(this, msg, font);      
      pd.updatePopupFieldMapDialog(msg);
    } /* create Mouseover popup */
    
    /* [2] save lists */
    this.fieldLabelStr= fieldLabelStr;
    this.fieldChoiceStr= fieldChoiceStr;
    this.mouseoverList= mouseoverList;
    this.unusedStr= unusedStr;
    
    /* [3] Make new AWT obs */
    fieldLabelList= new Label[fieldIndex];
    fieldChoiceList= new Choice[fieldChoiceListIndex];
    
    /* [4] misc stuff */
    this.mainTitleStr= mainTitleStr;
    this.helpMsg= helpMsg;
    numFieldChoiceSelected= 0;
    initFM();		                       /* init data */
    
    initFieldMapGUI();                 /* build GUI */
  } /* Constructor */
  
  
  /**
   * initFM() - initialize non-GUI objects
   */
  public void initFM()
  { /* initFM */    
    /* [1] Track choice lists  */
    pickedIndex= new int[fieldIndex+1];
    
    for(int i=0; i<fieldIndex; i++)
      pickedIndex[i]= 0;
    
    /* [2] Set mouseover defaults */
    mouseOverFont= new Font(MOUSEOVERFONT, Font.BOLD, MOUSEOVER_FONT_SIZE);
    
    /* special light yellow color for mouseover */
    mouseoverColor= Color.yellow;
    
    /* [3] get size of label font & number of fields
     * and set frame based on that size.
     */
    labelFont= new Font(LABELFONT, Font.PLAIN, 12);
    FontMetrics fontMetrics;
    fontMetrics= getFontMetrics(labelFont);
    int
      charHeight= fontMetrics.getHeight() + 8,
      idx= indexToLongestStr(this.fieldLabelStr),
      strWidth= fontMetrics.stringWidth(this.fieldLabelStr[idx]),
      wOffset= (cvt.isWinPCflag)
                 ? 15                   /* for the Windows PC */
                 : (cvt.isSunFlag)
                     ? (15+90)          /* For SOLARIS */
                     : 15,              /* [TODO] check for mac */
      w= 2*(strWidth + 100)+ wOffset;	  /* (40) add a little extra */
    
    this.frameHeight= (this.fieldIndex * charHeight) + 100;
    this.frameWidth= w;
    
    this.dataReadyFlag= false;
  } /* initFM */
  
  
  /**
   * initFieldMapGUI() - initialize GUI list method
   * @see CvtGUI#chkReanlyzeButtonState
   * @see FieldMap
   * @see FieldMap#isDuplicateEntries
   */
  public void initFieldMapGUI()
  { /* initFieldMapGUI */
    int rows= fieldIndex;
    FieldMap fm= cvt.mcd.fm;
    
    /* [1] Create text area for Mouseover */
    mouseoverWindow= new Window(this);
    ta= new TextArea(" ", TA_ROWS, TA_COLS, TextArea.SCROLLBARS_NONE);
    ta.setFont(mouseOverFont);
    ta.setBackground(mouseoverColor);
    
    mouseoverWindow.add(ta);
    addWindowListener(this);     /* so activate window controls*/
    
    /* [2] Create panels */
    titlePanel= new Panel();
    
    centerPanel= new Panel();
    centerPanel.setLayout(new GridLayout(1,2));
    
    leftPanel= new Panel();
    leftPanel.setLayout(new GridLayout(rows,1));
    leftPanel.add(new Label(" "));
    
    rightPanel= new Panel();
    rightPanel.setLayout(new GridLayout(rows,1));
    rightPanel.add(new Label(" "));
    
    buttonPanel= new Panel();
    
    choicePanel= new Panel();
    choicePanel.setLayout(new GridLayout(rows,1));
    
    fieldsLabelPanel= new Panel();
    fieldsLabelPanel.setLayout(new GridLayout(rows,1));
    
    this.setLayout(new BorderLayout());
    
    /* [3] Create labels, add mouse listeners & add to fieldsLabelPanel*/
    for(int i=0; i<fieldIndex; i++)
    { /* create labels */
      fieldLabelList[i]= new Label(fieldLabelStr[i]);
      fieldLabelList[i].addMouseListener(this);
      fieldLabelList[i].setFont(labelFont);
      fieldsLabelPanel.add(fieldLabelList[i]);
    } /* create labels */
    
    titleLabel= new Label(mainTitleStr);
    titlePanel.add(titleLabel);
    
   /* [4] Create and add choice lists & add to panel. The fieldIndex is 
    * the # of items on right and left, and also the number if items choosen 
    * in the lists on left.
    */
    for(int i=0; i<fieldIndex; i++)
    { /* add choice lists */
      fieldChoiceList[i]= new Choice();
      fieldChoiceList[i].addItemListener(this);
      fieldChoiceList[i].setFont(labelFont);
      
      for(int j=0; j<fieldChoiceListIndex; j++)
        fieldChoiceList[i].add(fieldChoiceStr[j]);
      
      /* will not find any first time thru, must add in #5 first time thru*/
      if(fm!=null)
      { /* find index of selected item */        
        int fsf= findSelectedField(fm, fieldChoiceList[i], i);        
        if(fsf!=-1)
          fieldChoiceList[i].select(fsf);        
      } /* find index of selected item */
      
      choicePanel.add(fieldChoiceList[i]);
    } /* add choice lists */
    
    
    /* [5] Use defaults from arrayLayout (preselect choice lists)
     * if first time and  save this so it will come up the next
     * time it is called.
     */
    if(cvt.fmgFirstTimeFlagGipo || cvt.fmgFirstTimeFlagQuant ||
       cvt.fmgFirstTimeFlagSample)
    { /* if not first time */
      for(int i=0; i<fieldIndex; i++)
      { /* check each label item */
        /* [5.1] get field labels and match up with proper choice */
        String field= fieldLabelStr[i]; /* get field name to search for */
        boolean foundItFlag= false;
        
        for(int j=0; j<fm.nMap; j++)
        { /* get field labels and match up with proper choice */
          if(foundItFlag)
            break;
          switch(remapMode)
          { /* case */
            case 0:
              break;
              
            case SetupLayouts.REMAP_GIPO:
              if(cvt.fmgFirstTimeFlagGipo)
              { /* Gipo */
                /* find correct matching maeFieldMap - userFieldMap tuple */
                if(fm.maeFieldMap[j].compareTo(field)==0 &&
                   fm.userTableMap[j].compareTo("GipoTable")==0)
                  
                  /* now go find the index in the choice list to select correct item */
                  for(int x=0; x<fieldChoiceListIndex; x++)
                    if(fieldChoiceStr[x].compareTo(fm.userFieldMap[j])==0)
                    {
                      fieldChoiceList[i].select(x);
                      foundItFlag= true;
                      break;
                    }
              } /* Gipo */
              break;
              
            case SetupLayouts.REMAP_QUANT:
              if(cvt.fmgFirstTimeFlagQuant)
              { /* Quant */
                /* find correct matching maeFieldMap - userFieldMap tuple */
                if(fm.maeFieldMap[j].compareTo(field)==0 &&
                   fm.userTableMap[j].compareTo("QuantTable")==0)
                { /* now go find the index in the choice list to select correct item */
                  for(int x=0; x<fieldChoiceListIndex; x++)
                    if(fieldChoiceStr[x].compareTo(fm.userFieldMap[j])==0)
                    {
                      fieldChoiceList[i].select(x);
                      foundItFlag= true;
                      break;
                    }
                }
              } /* Quant */
              break;
              
            case SetupLayouts.REMAP_SAMPLE:
              if(cvt.fmgFirstTimeFlagSample)
              { /* Sample */
                /* find correct matching maeFieldMap - userFieldMap tuple */
                if(fm.maeFieldMap[j].compareTo(field)==0 &&
                   fm.userTableMap[j].compareTo("SampleTable")==0)
                { /* now go find the index in the choice list to select correct item */
                  for(int x=0; x<fieldChoiceListIndex; x++)
                    if(fieldChoiceStr[x].compareTo(fm.userFieldMap[j])==0)
                    {
                      fieldChoiceList[i].select(x);
                      foundItFlag= true;
                      break;
                    }
                }
              }/* SAMPLE */
              break;
              
          } /* Sample */
        } /* get field labels and match up with proper choice */
      } /* check each label item */
      
      /* [5.2] Save data */
      switch(remapMode)
      {
        case 0:
          break;
        case SetupLayouts.REMAP_GIPO:
          if(cvt.fmgFirstTimeFlagGipo)
          {
            fm.userGipoFields= new String[fieldIndex];
            cvt.mcd.didGIPOassignFlag= true;
          }
          break;
        case SetupLayouts.REMAP_QUANT:
          if(cvt.fmgFirstTimeFlagQuant)
          {
            fm.userQuantFields= new String[fieldIndex];
            cvt.mcd.didQuantFlag= true;
          }
          break;
        case SetupLayouts.REMAP_SAMPLE:
          if(cvt.fmgFirstTimeFlagSample)
          {
            fm.userSampleFields= new String[fieldIndex];
            cvt.mcd.didQuantFlag= true;
          }
          break;
      } /* case */
      
      cvt.gui.chkReanlyzeButtonState();
      
      for(int i=0; i<fieldIndex; i++)
      { /* save data */
        String
          useStr= fieldChoiceList[i].getSelectedItem(),
          maeStr= fieldLabelStr[i];
        if(unusedStr.equals(useStr))
          continue;                       /* ignore it. */
        
        /* update list of user fields */
        switch(remapMode)
        {
          case SetupLayouts.REMAP_GIPO:
            if(cvt.fmgFirstTimeFlagGipo)
            {
              fm.userGipoFields[i]= useStr;
              fm.addEntry("GipoTable", maeStr, "GipoTable", useStr);
              cvt.fmgFirstTimeFlagGipo=false;
            }
            break;
          case SetupLayouts.REMAP_QUANT:
            if(cvt.fmgFirstTimeFlagQuant)
            {
              fm.userQuantFields[i]= useStr;
              fm.addEntry("QuantTable", maeStr, "QuantTable", useStr);
              cvt.fmgFirstTimeFlagQuant= false;
            }
            break;
          case SetupLayouts.REMAP_SAMPLE:
            if(cvt.fmgFirstTimeFlagSample)
            {
              fm.userSampleFields[i]= useStr;
              fm.addEntry("SampleTable", maeStr, "SampleTable", useStr);
              cvt.fmgFirstTimeFlagSample= false;
            }
            break;
        }
      } /* save data */
      
      fm.remapMode= 0;              /* Clear it */
      dataReadyFlag= true;
      cvt.fmg= null;                /* Clear MODAL instance */
      cvt.mcd.fm= fm;
      
      /* Also update the ArrayLayout fieldmap! */
      cvt.sul.alList[sul.useAL].fieldMap= fm;
    } /* if not first time */
    
    /* [5.3] Lookup the duplicateFlag status for this table */
    switch(remapMode)
    {
      case SetupLayouts.REMAP_GIPO:
        duplicateFlag= fm.isDuplicateEntries("GipoTable");
        break;
      case SetupLayouts.REMAP_QUANT:
        duplicateFlag= fm.isDuplicateEntries("QuantTable");
        break;
      case SetupLayouts.REMAP_SAMPLE:
        duplicateFlag= fm.isDuplicateEntries("SampleTable");
        break;
    }
    
    /* [6] Create buttons */
    doneButton= new Button("Done");
    doneButton.setLabel("Done");
    doneButton.setName("Done");
    doneButton.setBackground(Color.lightGray);
    doneButton.setForeground(Color.black);
    doneButton.addActionListener(this);
    doneButton.setEnabled(true);
    
    cancelButton= new Button("Cancel");
    cancelButton.setLabel("Cancel");
    cancelButton.setName("Cancel");
    cancelButton.setBackground(Color.lightGray);
    cancelButton.setForeground(Color.black);
    cancelButton.addActionListener(this);
    cancelButton.setEnabled(true);
    
    helpButton= new Button("Help");
    helpButton.setLabel("Help");
    helpButton.setName("Help");
    helpButton.setBackground(Color.lightGray);
    helpButton.setForeground(Color.black);
    helpButton.addActionListener(this);
    helpButton.setEnabled(true);
    
    buttonPanel.add(doneButton, BorderLayout.CENTER);
    buttonPanel.add(cancelButton, BorderLayout.CENTER);
    buttonPanel.add(helpButton, BorderLayout.CENTER);
    
    duplicateChkBox= new Checkbox();      /* check box for duplicate fields */
    duplicateChkBox.setLabel("Allow duplicates");
    duplicateChkBox.addItemListener(this);
    duplicateChkBox.setState(duplicateFlag);
    buttonPanel.add(duplicateChkBox, BorderLayout.CENTER);
    
    /* [7] Place panels in correct locations */
    centerPanel.add(fieldsLabelPanel);
    centerPanel.add(choicePanel);
    this.add(rightPanel, BorderLayout.EAST);
    this.add(leftPanel, BorderLayout.WEST);
    this.add(centerPanel, BorderLayout.CENTER);
    this.add(titlePanel, BorderLayout.NORTH);
    this.add(buttonPanel, BorderLayout.SOUTH);
    
    /* [8] set frame size */
    /* [TODO] analyse number of lists to set custom size, use fieldIndex... */
    this.setSize(frameWidth,frameHeight);
    
    /* [9] Position this frame inset from the parent frame. */
    Point
      guiLoc= cvt.gui.getLocation(),
      pos;
    int
      newX= 30+guiLoc.x,
     newY= 30+guiLoc.y;
    if(newX>0 && newY>0)
      pos= new Point(newX, newY);
    else
      pos= new Point(50,50);
    this.setLocation(pos);
    
    /* [10] Popup it up */
    setVisible(true);
  } /* initFieldMapGUI */
  
  
  /**
   * findSelectedField() - find which item was choosen before.
   * @param fm FieldMap to use
   * @param fieldChoiceList list of fields in choice list
   * @param i  index
   * @return selected item number else -1 if not picked.
   */
  private int findSelectedField(FieldMap fm, Choice fieldChoiceList, int i)
  { /* findSelectedField */
    /* [1] Check each item in the choice list if it has
     * been choosen before in either
     * fm.userGipoFields, fm.userQuantFields or fm.userSampleFields
     */
    for(int x=0; x<fieldChoiceListIndex; x++)
    {/* check each item */
      String item= fieldChoiceList.getItem(x);/* get field item */
      
      switch(remapMode)
      { /* case */
        case 0:
          return(-1);
          
        case SetupLayouts.REMAP_GIPO:
          if(fm.userGipoFields!=null && fm.userGipoFields[i]!=null)
            if(item.compareTo(fm.userGipoFields[i])==0)
            {
              return(x); /* found in choice list, return idx*/
            }
          break;
          
        case SetupLayouts.REMAP_QUANT:
          if(fm.userQuantFields!=null && fm.userQuantFields[i]!=null)
            if(item.compareTo(fm.userQuantFields[i])==0)
              return(x); /* found in choice list, return idx*/
          break;
          
        case SetupLayouts.REMAP_SAMPLE:
          if(fm.userSampleFields!=null && fm.userSampleFields[i]!=null)
            if(item.compareTo(fm.userSampleFields[i])==0)
              return(x); /* found in choice list, return idx*/
          break;
      } /* case */
    } /* check each item */
    return(-1);
  } /* findSelectedField */
  
  
  /**
   * indexToLongestStr() - find max size of str in array and return
   * its array index (location within array).
   * @param str  is the String to analyze
   * @return array index
   */
  private int indexToLongestStr(String[] str)
  { /* indexToLongestStr */
    int
      index= 0,		/* return array index */
      arraySize= str.length,	/* size of array */
      maxSize=0;		/* keep track of max str size */
    
    for(int i= 0; i < arraySize; i++)
    { /* find longest string */
      int curSize= str[i].length();
      if(maxSize<curSize)
      {
        maxSize= curSize;	/* save max size */
        index= i;		/* save index */
      }
    } /* find longest string */
    
    return(index);
  } /* indexToLongestStr */
  
  
  /**
   * itemStateChanged() - event handler for Choice list.
   * @param ie ItemEvent
   * @see UtilCM#logMsg
   * @see UtilCM#logMsg2
   * @see UtilCM#logMsg3
   */
  public void itemStateChanged(ItemEvent ie)
  { /* itemStateChanged */
    /* Check if same one has been picked */
    Object itemObj= (Object)ie.getSource();
    
    /* [1] Check if duplicates allowed */
    if(itemObj instanceof Checkbox)
    {
      duplicateFlag= duplicateChkBox.getState();
      return;
    }
    
    /* [2] Handle choices */
    if(! (itemObj instanceof Choice))
      return;
    
    Choice choice= (Choice)itemObj;
    int index= choice.getSelectedIndex();
    String item= choice.getSelectedItem();
    int idx= 0;
    
    /* There are changes. We should allow them to reanalyze */
    cvt.mcd.needToReAnalyzeFilesFlag= true;
    
    /* [2.1] Get index for which choice list was picked */
    for(int i=0; i<fieldIndex; i++)
      if(choice==fieldChoiceList[i])
      {
        idx= i;
        break;
      }
    int pickIdx= findFieldChoiceItem(index);
    
    /* [2.2] Do the right thing  */
    if(duplicateFlag)
    {/* allow duplicates */      
      switch(remapMode)
      {
        case SetupLayouts.REMAP_GIPO:
          cvt.mcd.fm.pickedGipoFields[idx]= index;
          break;
        case SetupLayouts.REMAP_QUANT:
          cvt.mcd.fm.pickedQuantFields[idx]= index;
          break;
        case SetupLayouts.REMAP_SAMPLE:
          cvt.mcd.fm.pickedSampleFields[idx]= index;
          break;
      }
      
      cvt.util.logMsg("",Color.black);   /* clear message area */
      cvt.util.logMsg2("", Color.black);
      cvt.util.logMsg3("", Color.black);
    }/* allow duplicates */
    
    else
    { /* do not allow duplicates, chk for duplicates */      
      if(pickIdx!=-1)
      { /* already picked, set to first item and issue error msg */
        choice.select(0);
        
        switch(remapMode)
        {
          case SetupLayouts.REMAP_GIPO:
            cvt.mcd.fm.pickedGipoFields[idx]= 0;
            break;
          case SetupLayouts.REMAP_QUANT:
            cvt.mcd.fm.pickedQuantFields[idx]= 0;
            break;
          case SetupLayouts.REMAP_SAMPLE:
            cvt.mcd.fm.pickedSampleFields[idx]= 0;
            break;
        }
        
        cvt.util.logMsg("This field was already selected for ["+
        fieldLabelStr[pickIdx]+"].", Color.red);
        cvt.util.logMsg2("Select another field.", Color.red);
        cvt.util.logMsg3("", Color.black);
      }/* already picked, set to first item and issue error msg */
      
      else
      { /* selection was ok */        
        switch(remapMode)
        {
          case SetupLayouts.REMAP_GIPO:
            cvt.mcd.fm.pickedGipoFields[idx]= index;
            break;
          case SetupLayouts.REMAP_QUANT:
            cvt.mcd.fm.pickedQuantFields[idx]= index;
            break;
          case SetupLayouts.REMAP_SAMPLE:
            cvt.mcd.fm.pickedSampleFields[idx]= index;
            break;
        }
        
        cvt.util.logMsg("",Color.black);   /* clear message area */
        cvt.util.logMsg2("", Color.black);
        cvt.util.logMsg3("", Color.black);
      } /* selection was ok */
    } /* do not allow duplicates, chk for duplicates */
  } /* itemStateChanged */
  
  
  /**
   * findFieldChoiceItem() - return pickedIndex if itemIdx is found else -1.
   * @param itemIdx
   * @return pickedIndex if itemIdx is found else -1 if not found
   */
  public int findFieldChoiceItem(int itemIdx)
  { /* findFieldChoiceItem */
    if(itemIdx==0)
      return(-1);            /* allow multiple of first item */
    
    switch(remapMode)
    { /* find them */
      case SetupLayouts.REMAP_GIPO:
        for(int i=0; i<fieldIndex; i++)
          if(itemIdx == cvt.mcd.fm.pickedGipoFields[i])
            return(i);
        
        break;
      case SetupLayouts.REMAP_QUANT:
        for(int i=0; i<fieldIndex; i++)
          if(itemIdx == cvt.mcd.fm.pickedQuantFields[i])
            return(i);
        
        break;
      case SetupLayouts.REMAP_SAMPLE:
        for(int i=0; i<fieldIndex; i++)
          if(itemIdx == cvt.mcd.fm.pickedSampleFields[i])
            return(i);
        
        break;
    } /* find them */
    
    return(-1);
  } /* findFieldChoiceItem */
  
  
  /**
   * isIntensityOrBackground() - test if it is a legal intensity or background MAE Quant names.
   * This is used in generating field mapes in saveChoiceData()
   * when user defines the intensity mappings.
   * @param maeStr string to search for
   * @return true if it is one of the legal intensity or background MAE Quant names
   */
  private boolean isIntensityOrBackground(String maeStr)
  { /* isIntensityOrBackground */
    boolean
      flag= (maeStr.equals("RawIntensity") ||
             maeStr.equals("RawIntensity1") ||
             maeStr.equals("RawIntensity2") ||
             maeStr.equals("Background") ||
             maeStr.equals("Background1") ||
             maeStr.equals("Background2")
             );
    return(flag);
  } /* isIntensityOrBackground */
  
  
  /**
   * saveChoiceData() - check to see if ALL choice lists have been picked by user,
   * if ok, then copy to fm.userXXXFields[].
   * @param checkAllFlag to check all choices
   * @return true if successful
   * @see FieldMap#addEntry
   * @see CvtGUI#setEditLayoutEditing
   */
  public boolean saveChoiceData(boolean checkAllFlag)
  { /* saveChoiceData */
    /* [1] go thru and check each choice list to see if
     * they have been choosen
     */
    int nSelected= 0;
    for(int i=0; i<fieldIndex; i++)
      if(fieldChoiceList[i].getSelectedIndex() == 0)
        nSelected++;           /* count them */
    
    numFieldChoiceSelected= nSelected;
    
    if(checkAllFlag && nSelected!=fieldIndex)
      return(false);
    
    /* [2] Save data, since all has been choosen */
    FieldMap
    fm= cvt.mcd.fm;
    switch(remapMode)
    {
      case 0:
        return(false);
      case SetupLayouts.REMAP_GIPO:
        fm.userGipoFields= new String[fieldIndex];
        cvt.mcd.didGIPOassignFlag= true;
        break;
      case SetupLayouts.REMAP_QUANT:
        fm.userQuantFields= new String[fieldIndex];
        cvt.mcd.didQuantFlag= true;
        break;
      case SetupLayouts.REMAP_SAMPLE:
        fm.userSampleFields= new String[fieldIndex];
        break;
    }
    
    cvt.gui.chkReanlyzeButtonState();
    
    for(int i=0; i<fieldIndex; i++)
    { /* save data */
      String
        useStr= fieldChoiceList[i].getSelectedItem(),
        maeStr= fieldLabelStr[i];
      if(unusedStr.equals(useStr))
        continue;                       /* ignore it. */
      
      /* update list of user fields */
      switch(remapMode)
      {
        case SetupLayouts.REMAP_GIPO:
          fm.userGipoFields[i]= useStr;
          fm.addEntry("GipoTable", maeStr, "GipoTable", useStr);
          break;
        case SetupLayouts.REMAP_QUANT:
          boolean
          repeatFlag= ((cvt.mcd.hasMultDatasetsFlag) &&
          isIntensityOrBackground(maeStr));
          fm.userQuantFields[i]= useStr;
          fm.addEntry("QuantTable", maeStr, "QuantTable", useStr,
          repeatFlag);
          break;
        case SetupLayouts.REMAP_SAMPLE:
          fm.userSampleFields[i]= useStr;
          fm.addEntry("SampleTable", maeStr, "SampleTable", useStr);
          break;
      }
    } /* save data */
    
    fm.remapMode= 0;              /* clear it */
    dataReadyFlag= true;
    cvt.fmg= null;               /* Clear MODAL instance */
    cvt.mcd.fm= fm;
    
    /* Also update the ArrayLayout fieldmap! */
    cvt.sul.alList[sul.useAL].fieldMap= fm;
    
    /* Enable Save Layout button if made changes */
    if(cvt.mcd.editedLayoutFlag && !cvt.fmgFirstTimeFlagGipo &&
       !cvt.fmgFirstTimeFlagQuant)
    { /* enable "Save Layout" if change and not user-defined */
      cvt.gui.setEditLayoutEditing(true);
    }
    
    return(true);
  } /* saveChoiceData */
  
  
  /**
   * actionPerformed() - button event handler
   * @param ae ActionEvent
   * @see PopupFieldMapDialog
   */
  public void actionPerformed(ActionEvent ae)
  { /* actionPerformed */
    String cmd= ae.getActionCommand();
    
    if(cmd.equals("Done"))
    { /* Done */
      boolean checkAllFlag= false;           /* may not need all flags */
      
      if(saveChoiceData(checkAllFlag))
      { /* finished defining key mappings - copy back to */
        this.dispose();
      }
      else
      { /* not finished defining key mappings */
        String msg= "Before exiting you must assign all items. Go back and check.";
        Font font= new Font("Dialog", Font.BOLD, 10);
        
        PopupFieldMapDialog  pd= new PopupFieldMapDialog(this, msg, font);
        
        pd.updatePopupFieldMapDialog(msg);
      }
    } /* Done */
    
    else if(cmd.equals("Cancel"))
    { /* Cancel */
      cancel();
    } /* Cancel */
    
    else  if(cmd.equals("Help"))
    { /* help */
      TextArea helpTA= new TextArea();
      
      if(helpMsg!=null)
      {
        helpTA.setText(helpMsg);
      }
      else
      {
        String msg= "Choose from choice list on right for each item.";
        helpTA.setText(msg);
      }
      
      helpTA.setFont(mouseOverFont);
      helpTA.setBackground(mouseoverColor);
      
      helpWindow = new Frame();
      helpWindow.setSize(400,250);
      Panel
        helpPanel= new Panel();
        helpPanel.setLayout(new BorderLayout());
      
      helpPanel.add(helpTA, BorderLayout.NORTH);
      
      Button okButton= new Button("OK");
      Panel lowerPanel= new Panel();
      okButton.setLabel("OK");
      okButton.setName("OK");
      okButton.setBackground(Color.lightGray);
      okButton.setForeground(Color.black);
      okButton.addActionListener(this);
      okButton.setEnabled(true);
      lowerPanel.add(okButton);
      helpPanel.add(lowerPanel, BorderLayout.SOUTH);
      
      helpWindow.add(helpPanel);
      
      helpWindow.setVisible(true);
    } /* help */
    
    else if(cmd.equals("OK"))
    { /* OK close help window */
      helpWindow.dispose();
    } /* OK close help window */
  } /* actionPerformed */
  
  
  /**
   * cancel() - cancel this assignement
   */
  private void cancel()
  { /* Cancel */
    rtnDataList= null;
    dataReadyFlag= false;        /*  no data saved*/
    cvt.mcd.fm.remapMode= 0;     /* clear it */
    cvt.fmg= null;               /* Clear MODAL instance */
    this.dispose();
  } /* Cancel */
  
  
  /**
   * mouseEntered() - put mouse over data in MouseOver text area
   * @param me MouseEvent
   */
  public void mouseEntered(MouseEvent me)
  { /* mouseMoved */
    /* [1] get coords for location to place mouseover window */
    Component src = (Component) me.getSource(); /* get label obj  */
    Dimension d= src.getSize();                 /* size of obj */
    Point loc= src.getLocationOnScreen();   /* location on the screen */
    int
      xOffset = 5,                      /* moves towards left */
      yOffset= 3,                       /* moves down a little  */
      y= loc.y + yOffset,
      x= d.width + loc.x - xOffset;
    
    /* [2] display mouse over */
    Label lbl= (Label) me.getSource();
    int idx= 0;
    
    for(int i=0; i<fieldIndex; i++)
      if(lbl == fieldLabelList[i])
        idx= i;
    
    String data= (mouseoverList==null)
                    ? null
                    : mouseoverList[idx];           /* get data to display */
    
    if(data!=null && data.length()>0)
    { /* display mouseover */
      ta.setText(data);
      
    /* get size of data and set mouseover window
     * based on that size */
      FontMetrics fm = getFontMetrics(mouseOverFont);
      int
        charHeight= fm.getHeight() + 8,
        strWidth= fm.stringWidth(data),
        w= strWidth + 10;	                        /* add a little extra */
      
      mouseoverWindow.setBounds(x, y, w, charHeight);
      
      /* TODO add small delay before displaying mouseover */
      
      mouseoverWindow.setVisible(true);
    } /* display mouseover */
    else
      ta.setText("");
  } /* mouseMoved */
  
  
  /**
   * mouseExited() - clear mouseover text area.
   * @param me MouseEvent
   */
  public void mouseExited(MouseEvent me)
  { /* mouse */
    ta.setText("");
    mouseoverWindow.dispose(); /* close mouseover window */
  } /* mouse */
  
  
  public void mouseMoved(MouseEvent me)  { }
  public void mouseClicked(MouseEvent me)  { }
  public void mousePressed(MouseEvent me)  { }
  public void mouseReleased(MouseEvent me)  { }
  
  
  /**
   * windowClosing() - close down the window on PC only.
   * @param e WindowEvent
   */
  public void windowClosing(WindowEvent e)
  {
    cancel();
  }
  
  
  /* Others not used at this time */
  public void windowOpened(WindowEvent e)  { }
  public void windowActivated(WindowEvent e)  { }
  public void windowClosed(WindowEvent e)  { }
  public void windowDeactivated(WindowEvent e)  { }
  public void windowDeiconified(WindowEvent e)  { }
  public void windowIconified(WindowEvent e)  { }
  
  
  
  /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
  /*                 CLASS  PopupFieldMapDialog                     */
  /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ */
  /**
   * This class displays a dialog window containing 2 buttons yes and no
   * to pass the infomormation on - chk flag for what user clicked on
   * no= false; yes= true.
   *
   */
  class PopupFieldMapDialog extends Dialog implements ActionListener, WindowListener
  {
    
    /** size of frame */
    public int
      width;
    /** size of frame */
    public int
      height;
    /** Frame */
    public Frame
      frame;
    /** for msglabel */
    Label
      label;
    /* yes/no flag */
    public boolean
      flag;
    /** button pressed flag */
    public boolean
      alertDone;
    /** wait for button to be pushed */
    boolean
      sleepFlag;
    /** Tried this instead of "this" */
    ActionListener
      listener;
    /** 50 spaces */
    private String
      spaces;
    
    
    /**
     * PopupFieldMapDialog() - Constructor for popup dialog w/ yes/no buttons
     * @param f Frame
     * @see #startPopupFieldMapDialogOK
     */
    PopupFieldMapDialog(Frame f)
    { /* PopupFieldMapDialog */
      super(f,"dialog box",true);
      
      /* [1] set some defaults */
      width= 100;
      height= 25;
      frame= f;
      alertDone= false;
      flag= true;
      spaces= "                                                        ";
      
      /* [2] create popup and hide it for use later */
      this.startPopupFieldMapDialog("Dialog");
    } /* PopupFieldMapDialog */
    
    
    /**
     * PopupFieldMapDialog() - Constructor for simple OK type msg
     * @param f Frame
     * @param msg Message for display in popup window
     * @see #startPopupFieldMapDialogOK
     */
    PopupFieldMapDialog(Frame f, String msg)
    { /* PopupFieldMapDialog */
      super(f,"dialog box",true);
      
      /* [1] set some defaults */
      width= 400;
      height= 150;
      frame= f;
      alertDone= false;
      flag= true;
      spaces= "                                                        ";
      
      /* [2] create popup and hide it for use later */
      this.startPopupFieldMapDialogOK("Dialog");
    } /* PopupFieldMapDialog */
    
    
    /**
     * PopupFieldMapDialog() - Constructor for simple OK type msg
     * @param f is Frame to use
     * @param msg  is the message for display in popup window
     * @param font to use this font for msg string
     * @see #startPopupFieldMapDialogOK
     */
    PopupFieldMapDialog(Frame f, String msg, Font font)
    { /* PopupFieldMapDialog */
      super(f,"dialog box",true);
      
      /* [1] set size */
      /* get size of data and set dialog
       * based on that size 
       */
      FontMetrics fm = this.getFontMetrics(font);
      int strWidth= fm.stringWidth(msg);
      
      width= strWidth + 50;	/* add a little extra DOES not work TODO*/
      
      height= 150;
      frame= f;
      alertDone= false;
      flag= true;
      spaces= "                                                        ";
      
      /* [2] create popup and hide it for use later */
      this.startPopupFieldMapDialogOK("Dialog");
    } /* PopupFieldMapDialog */
    
    
    /**
     * startPopupFieldMapDialogOK() - create a hidden dialog panel within a frame.
     * This is reused each time we popup a panel.
     * @param windowTitle window title
     */
    public void startPopupFieldMapDialogOK(String windowTitle)
    { /* startPopupFieldMapDialogOK */
      Panel buttonPanel= null;	/* place buttons here */
      Button  ok;
      GridLayout gl;            /* for layout of text fields, label, etc */      
      
      /* [1] initialize */
      gl= new GridLayout(3,1);
      this.setLayout(gl);	    /* set gridlayout to frame */
      
      /* [2] Create User instruction label */
      label= new Label(spaces);
      
      /* [3] Create the buttons and arrange to handle button clicks */
      buttonPanel= new Panel();
      
      ok= new Button("Ok");
      ok.addActionListener(this);
      buttonPanel.add("Center",ok);
      
      /* [4] add label msg to panel */
      this.add(label);
      
      /* [5] add buttons panel */
      if(buttonPanel!=null)
        this.add(buttonPanel);         /* buttons  */
      this.addWindowListener(this);    /* listener for window events */
      
      /* [6] add components and create frame */
      this.setTitle(windowTitle);     /* frame title */
      this.pack();
      
      /* [7] Center frame on the screen, PC only */
      Dimension screen= Toolkit.getDefaultToolkit().getScreenSize();
      Point pos= new Point((screen.width - frame.getSize().width) / 2,
                           (screen.height - frame.getSize().height) / 2);
      this.setLocation(pos);
      
      this.setVisible(false); /* hide frame which can be shown later */
    } /* startPopupFieldMapDialogOK */
    
    
    /**
     * startPopupFieldMapDialog() - create a hidden dialog panel within a frame.
     * @param windowTitle window title
     */
    public void startPopupFieldMapDialog(String windowTitle)
    { /* startPopupFieldMapDialog */
      Panel buttonPanel= null; 	/* place buttons here */
      Button
        yes,		                 /* update data */
        no;			                 /* use default data */
      GridLayout  gl;            /* for layout of text fields, label, etc */     
      
      /* [1] initialize */
      gl= new GridLayout(3,1);
      this.setLayout(gl);	/* set gridlayout to frame */
      
      /* [2] Create User instruction label */
      label= new Label(spaces);
      
      /* [3] Create the buttons and arrange to handle button clicks */
      buttonPanel= new Panel();
      
      yes= new Button("Yes");
      no= new Button("No");
      
      yes.addActionListener(this);
      buttonPanel.add("Center",yes);
      no.addActionListener(this);
      buttonPanel.add(no);
      
      /* [4] add label msg to panel */
      this.add(label);
      
      /* [5] add buttons panel */
      if(buttonPanel!=null)
        this.add(buttonPanel);         /* buttons  */
      this.addWindowListener(this);    /* listener for window events */
      
      /* [6] add components and create frame */
      this.setTitle(windowTitle);      /* frame title */
      this.pack();
      
      /* [7] Center frame on the screen */
      Dimension screen= Toolkit.getDefaultToolkit().getScreenSize();
      Point pos= new Point((screen.width-frame.getSize().width)/2,
                           (screen.height-frame.getSize().height)/2);
      this.setLocation(pos);
      
      this.setVisible(false); /* hide frame which can be shown later */
    } /* startPopupFieldMapDialog */
    
    
    /**
     * updatePopupFieldMapDialog() - display/unhide popup dialog
     * frame and set new vals.
     * Remove recreate actionListeners &  components
     * @param defaultMsg to use
     */
    public void updatePopupFieldMapDialog(String defaultMsg)
    { /* updatePopupFieldMapDialog */      
      alertDone= false;/* reset the flag */
      flag= true;
      label.setText(defaultMsg); /* change label */
      this.setSize(width,height);
      Point pos= new Point(100,100);
      this.setLocation(pos);
      
      this.setVisible(true);		 /* display it; unhide it */
    } /* updatePopupFieldMapDialog */
    
    
    /**
     * alertTimeout() - update the popup dialog msg - wait for CONTINUE
     * @param msg is the message string
     * @see #updatePopupFieldMapDialog
     */
    public void alertTimeout(String msg)
    { /* alertTimeout */
      this.sleepFlag= false;	/* flag for waiting */
      updatePopupFieldMapDialog(msg);
    } /* alertTimeout */
    
    
    /**
     * actionPerformed() - Handle button clicks
     * @param ae ActionEvent
     */
    public void actionPerformed(ActionEvent ae)
    { /* actionPerformed */
      String cmd= ae.getActionCommand();
      
     /* [1] see which button was pushed and do the right thing &
      * hide window 
      */
      if (cmd.equals("Yes"))
      {
        flag= true;
        this.setVisible(false);/* hide frame which can be shown later */
      }
      else if(cmd.equals("No"))
      {
        flag= false;
        this.setVisible(false);/* hide frame which can be shown later*/
      }
      else if(cmd.equals("Ok"))
      {
        flag= true;
        this.setVisible(false);/* hide frame which can be shown later*/
      }

      alertDone= true;
    } /* actionPerformed */
    
    
    /**
     * windowClosing() - close down the window on PC only.
     * @param we WindowEvent
     */
    public void windowClosing(WindowEvent we)
    {
      we.getWindow().dispose();
    }
    
    
    /*Others not used at this time */
    public void windowOpened(WindowEvent e)  { }
    public void windowActivated(WindowEvent e) { }
    public void windowClosed(WindowEvent e)  { }
    public void windowDeactivated(WindowEvent e)  { }
    public void windowDeiconified(WindowEvent e)  { }
    public void windowIconified(WindowEvent e)  { }
    
  } /* end of PopupFieldMapDialog class */
  
  
} /* FieldMapGUI */

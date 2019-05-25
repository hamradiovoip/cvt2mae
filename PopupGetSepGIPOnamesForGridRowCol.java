/** File: PopupBinOprDialogQuery.java */

package cvt2mae;

import java.text.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.*;

/**
 * Popup triple dialog for requesting (Grid, Row, Column) names. 
 * This requests the names of the separate GIPO file (Grid, Row, Col) field names
 * name of the destination operand.  It is used with Gene sets and
 * with Conditions sets.
 *<P>
 * This class displays 3 dialog windows containing editable TextFields. 
 * There are also 2 buttons (OK & cancel) to pass the information on.
 *<P>
 * This work was produced by Peter Lemkin of the National Cancer
 * Institute, an agency of the United States Government.  As a work of
 * the United States Government there is no associated copyright.  It is
 * offered as open source software under the Mozilla Public License
 * (version 1.1) subject to the limitations noted in the accompanying
 * LEGAL file.
 *<P>
 * @author P. Lemkin (NCI), B. Stephens(SAIC), G. Thornwall (SAIC), NCI-Frederick, Frederick, MD
 * @version $Date: 2005/10/20 11:45:56 $   $Revision: 1.6 $ 
 * @see <A HREF="http://maexplorer.sourceforge.org/">MAExplorer Home</A>
 */


class PopupGetSepGIPOnamesForGridRowCol extends Dialog
  implements ActionListener, ItemListener, WindowListener 
{
  /** link to global instance */
  private Cvt2Mae
    cvt;                               
  /** Mae Config Data */
  public MaeConfigData		
    mcd;
  /** separate GIPO grid name */            
  public String
    sepGipoGridName;   
  /** separate GIPO grid row name */            
  public String     
    sepGipoRowName;
  /** separate GIPO grid column name */            
  public String
    sepGipoColName;	
  /** Tried this instead of "this" */
  private ActionListener 
    listener;	
  /* spaces */
  private String
    spaces= "                                          "; 
  /** data is valid flag */
  boolean
    dataIsValid= false;
  /** grid data is valid flag */
  boolean
    gridFlag= false;
  /** row data is valid flag */
  boolean
    rowFlag= false;
  /** column data is valid flag */
  boolean
    colFlag= false; 
  /** column data size */
  int 
    colSize;
  /** size of frame */
  int 
    width;
  /** size of frame */
  int 
    height;
  /** frame to put popup in */
  private Frame
    frame;
  /** panel that holds choice list */
  private Panel
    optionPanel1;        
  /** panel that holds choice list */
  private Panel
    optionPanel2;    
  /** panel that holds choice list */
  private Panel
    optionPanel3;
  /** opt. option choice list */ 
  private Choice
    optionChoice1;
  /** opt. option choice list */ 
  private Choice
    optionChoice2;
  /** opt. option choice list */ 
  private Choice
    optionChoice3; 
  /** place text to be edited here */
  TextField
    textField1;
  /** place text to be edited here */
  TextField
    textField2;
  /** place text to be edited here */
  TextField
    textField3;
  /** for data label */
  Label				
    label1;	
  /** for data label */
  Label					        
    label2;
  /** for data label */
  Label				
    label3;
  /** wait for button to be pushed */
  boolean
    sleepFlag;	
  /** list of option values if present */
  String
    optionValues[]= null;        
  /** optionValues[0:nOptions] */
  int
    nOptions= 0;                
  /** check for valid choices */
  String
    separateGipoList[]= null;   
  /** allow user to cancel the interaction */
  private boolean
    allowCancel;
  
  
  /**
   * PopupGetSepGIPOnamesForGridRowCol() - Constructor
   * @param cvt is instance of Cvt2Mae
   * @param f is frame to use
   * @param defGridName is default grid name
   * @param defRowName is default grid row name
   * @param defColName is default grid column name
   * @param allowCancel to allow user to cancel GUI
   * @see #startPopupDialog
   */
  PopupGetSepGIPOnamesForGridRowCol(Cvt2Mae cvt, Frame f, String defGridName,
                                    String defRowName, String defColName,
                                    boolean allowCancel)
  { /* PopupGetSepGIPOnamesForGridRowCol*/
    super(f,"Specify names of GIPO (Grid, Row, Col) fields",true);
    
    /* [1] set some defaults */
    this.cvt= cvt;
    this.mcd = cvt.mcd;
    
    sepGipoGridName= defGridName;
    sepGipoRowName= defRowName;
    sepGipoColName= defColName;
    this.allowCancel= allowCancel;
    
    dataIsValid= false;
    gridFlag= false;
    rowFlag= false;
    colFlag= false;
    colSize= 45;
    frame= f;
    
    /* [2] create popup and hide it for use later */
    startPopupDialog("Specify separate GIPO file fields", colSize,
                     "Enter GIPO 'Grid' field name from pull-down list or type it",
                     "Enter GIPO 'Row' field name from pull-down list or type it",
                     "Engter GIPO 'Column' field name from pull-down list or type it");
    
  } /* PopupGetSepGIPOnamesForGridRowCol*/
  
  
  /**
   * startPopupDialog() - create a hidden dialog panel within a frame.
   * @param windowTitle to use for the window title
   * @param colSize is the size of textField
   * @param dataMsg1 is the label for textField 1
   * @param dataMsg2 is the label for textField 2
   * @param dataMsg3 is the label for textField 3
   */
  void startPopupDialog(String windowTitle, int colSize, String dataMsg1,
  String dataMsg2, String dataMsg3)
  { /* startPopupDialog */
    Panel buttonPanel;	     	/* place buttons here */
    Button
      ok,		                  /* update data */
      cancel;			            /* use default data */
    GridLayout gd;            /* for layout of text fields, label, etc */    
    
    /* [1] initialize */
    gd= new GridLayout(11,1);
    this.setLayout(gd);	/* set gridlayout to frame */
    
    Label titleLbl= new Label("Specify GIPO file field names for Grid, Row, "+
                              "and Columns. This will be different for different chips");
    this.add(titleLbl);
    
    /* [1.1] Create User instruction label */
    label1= new Label(dataMsg1);
    label2= new Label(dataMsg2);
    label3= new Label(dataMsg3);
    
    /* [2] Create the buttons and arrange to handle button clicks */
    buttonPanel= new Panel();
    
    ok= new Button("Ok");
    ok.addActionListener(this);
    
    cancel= new Button("Cancel"); /* possible BUG, if cancel, and user continues,
     * could be problesm! */
    cancel.addActionListener(this);
    
    buttonPanel.add("Center",ok);
    buttonPanel.add("Center", cancel);
    
    /* [3] add data text fields to panel  */
    textField1= new TextField(colSize);
    textField2= new TextField(colSize);
    textField3= new TextField(colSize);
    
    optionPanel1= new Panel();
    optionPanel2= new Panel();
    optionPanel3= new Panel();
    
    /* [4] add to grid */
    this.add(label1);	                /* data description label */
    this.add(optionPanel1);
    this.add(textField1);             /* editable text */
    
    this.add(label2);	                /* data description label */
    this.add(optionPanel2);
    this.add(textField2);             /* editable text */
    
    this.add(label3);	                /* data description label */
    this.add(optionPanel3);
    this.add(textField3);             /* editable text */
    
    this.add(buttonPanel);           /* buttons (ok & cancel) */
    this.addWindowListener(this);    /* listener for window events */
    
    /* [5] add components and create frame */
    this.setTitle(windowTitle);      /* frame title */
    this.pack();
    
    /* Center frame on the screen, PC only */
    Dimension screen= Toolkit.getDefaultToolkit().getScreenSize();
    Point pos= new Point((screen.width-frame.getSize().width)/2,
                         (screen.height-frame.getSize().height)/4);
    this.setLocation(pos);
    
    /* [6] Add data text fields to panel */
    textField1.setText(sepGipoGridName);
    textField2.setText(sepGipoRowName);
    textField3.setText(sepGipoColName);
    
    /* [7] Add commonly found option choices to option choices */
    optionChoice1= new Choice();
    optionChoice2= new Choice();
    optionChoice3= new Choice();
    optionPanel1.add(optionChoice1);
    optionPanel2.add(optionChoice2);
    optionPanel3.add(optionChoice3);
    
    optionChoice1.add("Block");
    optionChoice1.add("Grid");
    optionChoice1.add("Array Block");
    optionChoice1.add("Array Grid");
    
    optionChoice2.add("Row");
    optionChoice2.add("Grid Row");
    optionChoice2.add("Grid-Row");
    optionChoice2.add("Array Grid Row");
    optionChoice2.add("Array Row");
    
    optionChoice3.add("Col");
    optionChoice3.add("Grid  Col");
    optionChoice3.add("Grid-Col");
    optionChoice3.add("Array Grid Col");
    optionChoice3.add("Array Col");
    optionChoice3.add("Column");
    optionChoice3.add("Grid  Column");
    optionChoice3.add("Grid-Column");
    optionChoice3.add("Array Grid Column");
    optionChoice3.add("Array Column");
    
    /* [8] Add fields from actual separate GIPO file */
    for(int i=0;i<mcd.nSepGIPOfields;i++)
    {
      String field = mcd.sepGIPOfields[i];
      
      optionChoice1.add(field);
      optionChoice2.add(field);
      optionChoice3.add(field);
    }
    optionChoice1.addItemListener(this);
    optionChoice2.addItemListener(this);
    optionChoice3.addItemListener(this);
    
    /* [9] Add components and unhide */
    this.sleepFlag= true;	/* flag for waiting */
    this.setVisible(true);		/* display it; unhide it */
  } /* startPopupDialog */
  
  
  /**
   * actionPerformed() - Handle button clicks
   * @param e is button press event
   * @see PopupDialog
   */
  public void actionPerformed(ActionEvent e)
  { /* actionPerformed */
    String cmd= e.getActionCommand();
    
    /* [1] see which button was pushed and do the right thing,
     * hide window and return default/altered data
      */
    if (cmd.equals("Cancel"))
    { /* send default data back - data is already stored into this.data */      
      /* NOTE: this could be bad since nothing will be selected
      * and could have bad side effects later, warn user before cont.
       */
      if(!allowCancel)
      {
        PopupDialog
          pd =new PopupDialog("Warning: you will not have any GIPO fields selected. "+
                              "Are you sure you want to do this?", true);
        
        if(pd.answer==true)
          this.setVisible(false); /* hide frame which can be shown later */
      }
      else
        this.setVisible(false);
    } /* send default data back */
    
    else
      if(cmd.equals("Ok"))
      { /* Acccept data - hide window and return data back entered by user */        
        /* check if each GIPO fields are valid in the separate GIPO file */
        sepGipoGridName= textField1.getText(); /* altered data returned */
        
        for(int i=0;i<mcd.nSepGIPOfields;i++)
          if(mcd.sepGIPOfields[i].equals(sepGipoGridName))
            gridFlag= true;
        
        sepGipoRowName= textField2.getText(); /* altered data returned */
        
        for(int i=0;i<mcd.nSepGIPOfields;i++)
          if(mcd.sepGIPOfields[i].equals(sepGipoRowName))
            rowFlag= true;
        
        sepGipoColName= textField3.getText(); /* altered data returned */
        
        for(int i=0; i< mcd.nSepGIPOfields; i++)
          if(mcd.sepGIPOfields[i].equals(sepGipoColName))
            colFlag= true;
        
      /* check to see if all fields are valid, if not then popup
       * a warning */
        if(sepGipoGridName.equals(sepGipoRowName))
          new PopupDialog("Error, GIPO Grid Name is same as Row Name");
        else if(sepGipoGridName.equals(sepGipoColName))
          new PopupDialog("Error, GIPO Grid Name is same as Column Name");
        else if(sepGipoRowName.equals(sepGipoColName))
          new PopupDialog("Error, GIPO Column Name is same as Row Name");
        else if(gridFlag == false)
          new PopupDialog("Your Grid choice '"+ sepGipoGridName +
                          "' is not one of the Gipo fields. Please choose correct field.");
        else if(rowFlag == false)
          new PopupDialog("Your Row choice '"+ sepGipoRowName +
                          "' is not one of the Gipo fields. Please choose correct field.");
        else if(colFlag == false)
          new PopupDialog("Your Column choice  '"+ sepGipoColName +
                          "' is not one of the Gipo fields. Please choose correct field.");
        
        if(gridFlag && colFlag && rowFlag)
        {
          dataIsValid= true;
          this.setVisible(false); /* hide frame which can be shown later */
        }
        else
          dataIsValid= false;
      }  /* Acccept data - hide window and return data back entered by user */
    
  } /* actionPerformed */
  
  
  /**
   * itemStateChanged() - event handler for Choices
   * @param e is choice select event
   */
  public void itemStateChanged(ItemEvent e)
  { /* itemStateChanged */
    Object obj= e.getSource();
    Choice itemC= (Choice)obj;
    String optionStr;
    
    if(itemC==optionChoice1)
    { /* change the option used - get matching option */
      optionStr= optionChoice1.getSelectedItem();
      if(optionStr!=null)
      { /* update text field */
        textField1.setText(optionStr);
        repaint();
      }
    }
    
    else if(itemC==optionChoice2)
    { /* change the option used - get matching option */
      optionStr= optionChoice2.getSelectedItem();
      if(optionStr!=null)
      { /* update text field */
        textField2.setText(optionStr);
        repaint();
      }
    }
    
    else if(itemC==optionChoice3)
    { /* change the option used - get matching option */
      optionStr= optionChoice3.getSelectedItem();
      if(optionStr!=null)
      { /* update text field */
        textField3.setText(optionStr);
        repaint();
      }
    }    
  } /* itemStateChanged */
  
  
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
  public void windowActivated(WindowEvent e)  { }
  public void windowClosed(WindowEvent e)  { }
  public void windowDeactivated(WindowEvent e)  { }
  public void windowDeiconified(WindowEvent e)  { }
  public void windowIconified(WindowEvent e)  { }
  
} /* end of PopupGetSepGIPOnamesForGridRowColclass */

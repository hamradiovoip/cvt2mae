/** File: PopupEditPrjDialog.java */

package cvt2mae;

import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;

/**
 * Class to popup up dialog for Edit Project.
 * This class displays a dialog window containing 2 buttons yes and no
 * to pass the infomormation on - chk flag for what user clicked on
 * no= false; yes= true.
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


public class PopupEditPrjDialog extends Dialog
  implements ActionListener, WindowListener 
{
  /** link to global MaeConfigData instance */
  private MaeConfigData
    mcd;	                
  /** size of frame width */
  public int 
    width;			
  /** size of frame height */
  public int 
    height;
  /** Frame holding popup dialog */
  public Frame
    frame;
  /** for msg label */
  Label				
    label;	
  /** flag: yes/no response flag */		
  public boolean
    flag;
  /** button pressed flag */
  public boolean		
    alertDone;  
  /** wait for button to be pushed */                
  boolean    
    sleepFlag;			
  /** action listener for edited project dialog */
  ActionListener 
    listener;		
  /** 50 spaces */	
  private String
    spaces;                     
  
  
  /**
   * PopupEditPrjDialog() - Constructor for yes/no buttons
   * @param f is frame to put dialog  in
   * @param mcd is instance of MaeConfigData
   * @see #startPopupDialogOK
   */
  PopupEditPrjDialog(Frame f, MaeConfigData mcd)
  { /* PopupEditPrjDialog */
    super(f,"dialog box",true);
    
    this.mcd= mcd;
    
    /* [1] set some defaults */
    width= 100;
    height= 25;
    frame= f;
    alertDone= false;
    flag= true;
    spaces= "                                                        ";
    
    /* [2] create popup and hide it for use later */
    this.startPopupDialog("Dialog");
  } /* PopupEditPrjDialog */
  
  
  /**
   * PopupEditPrjDialog() - Constructor for OK button only
   * @param f is frame to put dialog  in
   * @param mcd is instance of MaeConfigData
   * @param msg is message to use in prompt
   * @see #startPopupDialogOK
   */
  PopupEditPrjDialog(Frame f, MaeConfigData mcd, String msg)
  { /* PopupEditPrjDialog */
    super(f,"dialog box",true);
    
    this.mcd= mcd;
    
    /* [1] set some defaults */
    width= 100;
    height= 25;
    frame= f;
    alertDone= false;
    flag= true;
    spaces= "                                                        ";
    
    /* [2] create popup and hide it for use later */
    this.startPopupDialogOK("Dialog");
  } /* PopupEditPrjDialog */
  
  
  /**
   * startPopupDialogOK() - create a hidden dialog panel within a frame.
   * This is reused each time we popup a panel.
   * @param windowTitle to use in popup
   */
  public void startPopupDialogOK(String windowTitle)
  { /* startPopupDialogOK */
    Panel buttonPanel= null;	/* place buttons here */
    Button ok;
    GridLayout  gl;            /* for layout of text fields, label, etc */
    
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
  } /* startPopupDialogOK */
  
  
  /**
   * startPopupDialog() - create a hidden dialog panel within a frame.
   * @param windowTitle to use in popup
   */
  public void startPopupDialog(String windowTitle)
  { /* startPopupDialog */
    Panel buttonPanel= null;	 /* place buttons here */
    Button
      yes,		                 /* update data */
      no;	                  	/* use default data */
    GridLayout gl;             /* for layout of text fields, label, etc */
    
    /* [1] initialize */
    gl= new GridLayout(3,1);
    this.setLayout(gl);	    /* set gridlayout to frame */
    
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
    this.setTitle(windowTitle);     /* frame title */
    this.pack();
    
    /* [7] Center frame on the screen, PC only */
    Dimension screen= Toolkit.getDefaultToolkit().getScreenSize();
    Point pos= new Point((screen.width-frame.getSize().width)/2,
                         (screen.height-frame.getSize().height)/2);
    this.setLocation(pos);
    
    this.setVisible(false); /* hide frame which can be shown later */
  } /* startPopupDialog */
  
  
  /**
   * updatePopupDialog() - display/unhide popup dialog frame and set new vals.
   * Remove recreate actionListeners &  components
   * @param defaultMsg to use in popup
   */
  public void updatePopupDialog(String defaultMsg)
  { /* updatePopupDialog */
    alertDone= false;/* reset the flag */
    flag= true;
    label.setText(defaultMsg); /* change label */
    
    this.setVisible(true);		 /* display it; unhide it */
  } /* updatePopupDialog */
  
  
  /**
   * alertTimeout() - update the popup dialog msg - wait for CONTINUE
   * @param msg to use in timeout popup
   * @see #updatePopupDialog
   */
  public void alertTimeout(String msg)
  { /* alertTimeout */
    this.sleepFlag= false;	/* flag for waiting */
    
    updatePopupDialog(msg);
  } /* alertTimeout */
  
  
  /**
   * actionPerformed() - Handle button clicks
   * @param ae ActionEvent,  button press event
   */
  public void actionPerformed(ActionEvent ae)
  { /* actionPerformed */
    String cmd= ae.getActionCommand();
    
    /* [1] see which button was pushed and do the right thing &
     * hide window .
     */
    if (cmd.equals("Yes"))
    {
      flag= true;
      this.setVisible(false);/* hide frame which can be shown later */
    }
    else
      if(cmd.equals("No"))
      {
        flag= false;
        this.setVisible(false);/* hide frame which can be shown later*/
      }
      else
        if(cmd.equals("Ok"))
        {
          flag= true;
          this.setVisible(false); /* hide frame which can be shown later*/
        }
    
    alertDone= true;
  } /* actionPerformed */
  
  
  /**
   * windowClosing() - close down the window on PC only.
   * @param we window closing event
   */
  public void windowClosing(WindowEvent we)
  {
    mcd.editMaePrjFilesFlag= false;
    mcd.editedLayoutFlag= false; /* reset it - must edit first
     * to "Save Layout" */
    we.getWindow().dispose();
  }
  
  
  /*Others not used at this time */
  public void windowOpened(WindowEvent e)  { }
  public void windowActivated(WindowEvent e)  { }
  public void windowClosed(WindowEvent e)  { }
  public void windowDeactivated(WindowEvent e)  { }
  public void windowDeiconified(WindowEvent e)  { }
  public void windowIconified(WindowEvent e)  { }
  
} /* end of PopupEditPrjDialog class */

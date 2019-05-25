/** File: TextFrame.java */

package cvt2mae;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * This class extends frame and adds a textarea and a several control buttons.
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


/**
 * TextFrame() - simple text area frame with control buttons
 */
public class TextFrame extends Frame implements ActionListener 
{
  /** Global link to Cvt2Mae instance */
  public Cvt2Mae
    cvt;                       
  /** Global link to CvtGUI popup frame */
  public CvtGUI
    gui;  
  /** Text area  for displaying the text*/
  TextArea
    area;
  /** hide button */
  Button 
    hidebutton;
  /** clear button */
  Button 
    clearbutton;
  /** GUI layout */
  GridBagLayout
    layout;
  /** grid bag constraints */
  GridBagConstraints
    constraints;
  
  
  /**
   * TextFrame() - constructor
   * @param cvt
   * @param title
   */
  public TextFrame(Cvt2Mae cvt, String title)
  { /* TextFrame */
    super(title);
    
    this.cvt= cvt;
    gui= cvt.gui;
    
    setSize(320,250);
    setFont(gui.default_font);
    
    layout= new GridBagLayout();
    constraints= new GridBagConstraints();
    constraints.fill= GridBagConstraints.BOTH;
    setLayout(layout);
    
    setBackground(gui.A_BG_COLOR);
    
    area= new TextArea();
    area.setBackground(gui.F_BG_COLOR);
    area.setFont(gui.text_font);
    addComponent(area,0,0,2,1,1,100);
    
    hidebutton= new Button("Hide");
    hidebutton.setBackground(gui.P_BG_COLOR);
    hidebutton.setFont(gui.button_font);
    hidebutton.addActionListener(this);
    addComponent(hidebutton,0,1,1,1,1,1);
    
    clearbutton= new Button("Clear");
    clearbutton.setBackground(gui.P_BG_COLOR);
    clearbutton.setFont(gui.button_font);
    clearbutton.addActionListener(this);
    addComponent(clearbutton,1,1,1,1,1,1);
  } /* TextFrame */
  
  
  /**
   * addComponent() - add a text component to the frame
   * @param obj to add
   * @param x is 0 or 1 for left or right side of a row
   * @param y is the row number
   * @param xw holds the component's width in cells
   * @param yw holds the component's height in cells
   * @param xg holds the component's column weight
   * @param yg holds the component's row weight
   */
  private void addComponent(Component obj, int x, int y,
                            int xw,int yw, int xg, int yg)
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
   * actionPerformed() - event handler
   * @param ae is button press event
   */
  public void actionPerformed(ActionEvent ae)
  { /* actionPerformed */
    if (ae.getSource() == hidebutton)
      setVisible(false);
    
    if (ae.getSource() == clearbutton)
      area.setText("");
  } /* actionPerformed */
  
  
  /**
   * appendLog() - save text into log area
   * @param text to append
   */
  public void appendLog(String text)
  { /* appendLog */
    area.append(text+"\n");
  } /* appendLog */
  
  
  /**
   * appendLog() - save text into log area and set visible/invisible
   * @param text to append
   * @param setVisibleFlag to make it visible
   */
  public void appendLog(String text, boolean setVisibleFlag)
  { /* appendLog */
    area.append(text+"\n");
    if(setVisibleFlag)
      this.setVisible(true);
  } /* appendLog */
  
  
  /**
   * clearLog() - clear the log area
   */
  public void clearLog()
  { /* clearLog */
    area.setText("");
  } /* clearLog */
  
  
  /*
   * simplify() -  remove the clear button from the button panel
   */
  public void simplify()
  { /* simplify */
    remove(clearbutton);
  } /* simplify */
  
} /* end of class TextFrame */

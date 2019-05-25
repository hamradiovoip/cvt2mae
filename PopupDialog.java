/** File: PopupDialog.java */

package cvt2mae;

import java.awt.*;
import java.awt.event.*;
import java.awt.List;
import java.io.*;
import java.util.*;
import java.awt.Color;


/** 
 * Class to popup a dialog box for error msgs, warnings, etc.
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

public class PopupDialog extends Frame implements WindowListener 
{
  /** return if yes or no was clicked */
  boolean
    answer; /* true: YES, close window; false: NO, close window */
  
  /**
   * PopupDialog() - create dialog box with OK button
   * @param msg to use in title
   * @see MainDialog
   */
  PopupDialog(String msg)
  {
    super("Dialog"); 
    answer= true;    
    MainDialog md = new MainDialog(this, msg, true, Color.black);
  }
  
  
  /**
   * PopupDialog() - create dialog box with yes & no buttons
   * @param msg to use in title
   * @param yesNoFlag if add yes/no checkbox
   * @see MainDialog
   */
  PopupDialog(String msg, boolean yesNoFlag)
  {
    super("Dialog");     
    answer= true;
    MainDialog md = new MainDialog(this, msg, true, true, Color.black);    
    answer= md.answer;
  }
  
  
  /**
   * windowClosing() - close down the window on PC only.
   * @param we WindowEvent
   */
  public void windowClosing(WindowEvent we)
  {
    we.getWindow().dispose();
  }
  
  
  /* Others not used at this time */
  public void windowOpened(WindowEvent e)  { }
  public void windowActivated(WindowEvent e)  { }
  public void windowClosed(WindowEvent e)  { }
  public void windowDeactivated(WindowEvent e)  { }
  public void windowDeiconified(WindowEvent e)  { }
  public void windowIconified(WindowEvent e)  { }
  
  
  /**
   * MainDialog() - Class extends Dialog class.
   */
  public class MainDialog extends Dialog implements ActionListener
  {
    /** answer when exit dialog */
    boolean
      answer;
    
    /**
     * MainDialog() - create dialog box with OK button
     * @param frame to put dialog in
     * @param msg message for prompt
     * @param modal to set as model dialog
     * @param color to use
     */
    public MainDialog(Frame frame, String msg, boolean modal, Color color)
    { /* MainDialog */
      super(frame, msg, modal);
      
      Button button= new Button("OK");
      Label label= new Label(msg, Label.CENTER)
      
      {
        public Dimension getPreferredSize()
        {
          Dimension d = super.getPreferredSize();
          return(new Dimension(d.width+40, d.height+40));
        }
      };
      
      label.setForeground(color); /* default */
      Panel p = new Panel();
      this.setTitle(msg);
      this.setModal(modal);
      
      p.add(button,BorderLayout.CENTER);
      button.addActionListener(this);
      this.add(label,BorderLayout.NORTH);
      this.add(p,BorderLayout.SOUTH);
      this.pack();
      this.setVisible(true);
    } /* MainDialog */
    
    
    /**
     * MainDialog() - create dialog box with Yes & No buttons
     * @param frame to put dialog in
     * @param msg message for prompt
     * @param modal to set as model dialog
     * @param yesNoFlag if add Yes/No buttons
     * @param color to use
     */
    public MainDialog(Frame frame, String msg, boolean modal,
    boolean yesNoFlag, Color color)
    { /* MainDialog */
      super(frame, msg, modal);
      
      Button
        noButton= new Button("No"),
        yesButton= new Button("Yes");
      
      Label label= new Label(msg, Label.CENTER)
      {
        public Dimension getPreferredSize()
        {
          Dimension
          d = super.getPreferredSize();
          return(new Dimension(d.width+40, d.height+40));
        }
      };
      
      label.setForeground(color); /* default */
      Panel p = new Panel();
      this.setTitle(msg);
      this.setModal(modal);
      
      p.add(yesButton,BorderLayout.WEST);
      p.add(noButton,BorderLayout.EAST);
      yesButton.addActionListener(this);
      noButton.addActionListener(this);
      
      this.add(label,BorderLayout.NORTH);
      this.add(p,BorderLayout.SOUTH);
      this.pack();
      this.setVisible(true);
    } /* MainDialog */
    
    
    /**
     * actionPerformed() -  button press handler- close window
     * @param evt is button press event
     */
    public void actionPerformed(ActionEvent evt)
    { /* actionPerformed */
      String what= evt.getActionCommand();
      
      if("Yes".equals(what))
      {
        answer= true;
        this.dispose();
      }
      else if("No".equals(what))
      {
        answer= false;
        this.dispose();
      }
      else if("OK".equals(what))
      {
        answer= false;
        this.dispose();
      }
    } /* actionPerformed */
    
  }/* class  MainDialog */
  
} /* class PopupDialog */

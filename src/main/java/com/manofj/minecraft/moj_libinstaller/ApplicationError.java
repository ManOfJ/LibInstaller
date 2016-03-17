package com.manofj.minecraft.moj_libinstaller;

import static com.manofj.minecraft.moj_libinstaller.utils.AppUtils.i18n;
import static com.manofj.minecraft.moj_libinstaller.utils.AppUtils.logText;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;


class ApplicationError extends JDialog {

  private static final long serialVersionUID = 1L;


  public static void showDialog() {

    JDialog dialog = new ApplicationError();
    dialog.setVisible( true );

  }


  public ApplicationError() {

    {
      setTitle( i18n( "errordialog.msg.01" ) );
      setModalityType( ModalityType.APPLICATION_MODAL );
      setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
      setSize( 480, 360 );
      setLocationRelativeTo( null );
    }

    SpringLayout springLayout = new SpringLayout();
    getContentPane().setLayout( springLayout );

    JScrollPane displayContainer = new JScrollPane();
    springLayout.putConstraint( SpringLayout.NORTH, displayContainer, 0, SpringLayout.NORTH, getContentPane() );
    springLayout.putConstraint( SpringLayout.WEST, displayContainer, 0, SpringLayout.WEST, getContentPane() );
    springLayout.putConstraint( SpringLayout.SOUTH, displayContainer, -36, SpringLayout.SOUTH, getContentPane() );
    springLayout.putConstraint( SpringLayout.EAST, displayContainer, 0, SpringLayout.EAST, getContentPane() );
    getContentPane().add( displayContainer );

    JTextArea logDisplay = new JTextArea();
    displayContainer.setViewportView( logDisplay );
    {
      logDisplay.setText( logText() );
    }

    JPanel buttonContainer = new JPanel();
    springLayout.putConstraint( SpringLayout.NORTH, buttonContainer, 0, SpringLayout.SOUTH, displayContainer );
    springLayout.putConstraint( SpringLayout.SOUTH, buttonContainer, 0, SpringLayout.SOUTH, getContentPane() );
    springLayout.putConstraint( SpringLayout.WEST, buttonContainer, 0, SpringLayout.WEST, getContentPane() );
    springLayout.putConstraint( SpringLayout.EAST, buttonContainer, 0, SpringLayout.EAST, getContentPane() );
    getContentPane().add( buttonContainer );
    {
      buttonContainer.setBorder( new EmptyBorder( 4, 4, 4, 4 ) );
      buttonContainer.setLayout( new BorderLayout( 0, 0 ) );
    }

    JButton closeButton = new JButton( i18n( "common.btn.close" ) );
    buttonContainer.add( closeButton );
    {
      closeButton.addActionListener( new ActionListener() {
        @Override
        public void actionPerformed( ActionEvent e ) {
          System.exit( 1 );
        }
      } );
    }

    {
      this.addWindowListener( new WindowAdapter() {
        @Override
        public void windowClosing( WindowEvent e ) {
          System.exit( 1 );
        }
      } );
    }
  }

}

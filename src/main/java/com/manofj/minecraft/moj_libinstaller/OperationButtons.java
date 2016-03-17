package com.manofj.minecraft.moj_libinstaller;

import static com.manofj.minecraft.moj_libinstaller.utils.AppUtils.i18n;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;


class OperationButtons extends JPanel {

  private static final long serialVersionUID = 1L;


  public final JButton cancelButton;
  public final JButton nextButton;
  public final JButton prevButton;
  public final JButton completeButton;


  public OperationButtons() {
    SpringLayout springLayout = new SpringLayout();
    setLayout( springLayout );

    completeButton = new JButton( i18n( "common.btn.complete" ) );
    springLayout.putConstraint( SpringLayout.NORTH, completeButton, 0, SpringLayout.NORTH, this );
    springLayout.putConstraint( SpringLayout.EAST, completeButton, 0, SpringLayout.EAST, this );
    add( completeButton );
    {
      completeButton.setVisible( false );
    }

    cancelButton = new JButton( i18n( "common.btn.cancel" ) );
    springLayout.putConstraint( SpringLayout.NORTH, cancelButton, 0, SpringLayout.NORTH, this );
    springLayout.putConstraint( SpringLayout.EAST, cancelButton, 0, SpringLayout.EAST, this );
    add( cancelButton );

    nextButton = new JButton( i18n( "common.btn.next" ) );
    springLayout.putConstraint( SpringLayout.NORTH, nextButton, 0, SpringLayout.NORTH, this );
    springLayout.putConstraint( SpringLayout.EAST, nextButton, -4, SpringLayout.WEST, cancelButton );
    add( nextButton );

    prevButton = new JButton( i18n( "common.btn.prev" ) );
    springLayout.putConstraint( SpringLayout.NORTH, prevButton, 0, SpringLayout.NORTH, this );
    springLayout.putConstraint( SpringLayout.EAST, prevButton, -4, SpringLayout.WEST, nextButton );
    add( prevButton );

    {
      List< JButton > list = Arrays.asList( completeButton, cancelButton, nextButton, prevButton );
      int maxWidth = Integer.MIN_VALUE;
      for ( JButton btn : list ) maxWidth = Math.max( maxWidth, btn.getPreferredSize().width );
      for ( JButton btn : list ) btn.setPreferredSize( new Dimension( maxWidth, 28 ) );
    }

  }


  public void resetAllButtons() {

    completeButton.setVisible( false );
    completeButton.setEnabled( true );

    cancelButton.setVisible( true );
    cancelButton.setEnabled( true );

    nextButton.setVisible( true );
    nextButton.setEnabled( true );

    prevButton.setVisible( true );
    prevButton.setEnabled( true );

  }

}

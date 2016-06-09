package com.manofj.minecraft.moj_libinstaller;

import static com.manofj.minecraft.moj_libinstaller.utils.AppUtils.i18n;
import static com.manofj.minecraft.moj_libinstaller.utils.AppUtils.i18nf;

import java.awt.SystemColor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.manofj.minecraft.moj_libinstaller.models.ForgeVersion;
import com.manofj.minecraft.moj_libinstaller.models.LibraryData;
import com.manofj.minecraft.moj_libinstaller.utils.AppUtils;
import com.manofj.minecraft.moj_libinstaller.utils.FileUtils;
import com.manofj.minecraft.moj_libinstaller.utils.GsonUtils;


class Install extends JPanel {

  private static final long serialVersionUID = 1L;


  private Map< String, File > forgeVersionByJsonPath = Collections.emptyMap();
  private Map< String, Set< LibraryData > > installDetails = Collections.emptyMap();
  private OperationButtons operationButtons;

  private JTextPane message;
  private JScrollPane displayContainer;
  private JTextArea logDisplay;

  public Install() {

    SpringLayout springLayout = new SpringLayout();
    setLayout( springLayout );

    message = new JTextPane();
    springLayout.putConstraint( SpringLayout.NORTH, message, 0, SpringLayout.NORTH, this );
    springLayout.putConstraint( SpringLayout.WEST, message, 0, SpringLayout.WEST, this );
    springLayout.putConstraint( SpringLayout.SOUTH, message, 48, SpringLayout.NORTH, this );
    springLayout.putConstraint( SpringLayout.EAST, message, 0, SpringLayout.EAST, this );
    add( message );
    {
      message.setText( i18n( "install.msg.01" ) );
      message.setBackground( SystemColor.control );
      message.setDisabledTextColor( SystemColor.textText );
      message.setEnabled( false );
      message.setEditable( false );
    }

    displayContainer = new JScrollPane();
    springLayout.putConstraint( SpringLayout.NORTH, displayContainer, 0, SpringLayout.SOUTH, message );
    springLayout.putConstraint( SpringLayout.WEST, displayContainer, 0, SpringLayout.WEST, this );
    springLayout.putConstraint( SpringLayout.SOUTH, displayContainer, 0, SpringLayout.SOUTH, this );
    springLayout.putConstraint( SpringLayout.EAST, displayContainer, 0, SpringLayout.EAST, this );
    add( displayContainer );

    logDisplay = new JTextArea();
    displayContainer.setViewportView( logDisplay );
    {
      logDisplay.setText( AppUtils.logText() );
      logDisplay.setBorder( new EmptyBorder( 4, 4, 4, 4 ) );
      logDisplay.setDisabledTextColor( SystemColor.textText );
      logDisplay.setEnabled( false );
      logDisplay.setEditable( false );
    }

  }

  private void initialization() {

    operationButtons.resetAllButtons();
    operationButtons.nextButton.setVisible( false );
    operationButtons.prevButton.setVisible( false );

    SwingUtilities.invokeLater( new Runnable() {
      @Override
      public void run() {

        install();

        operationButtons.cancelButton.setVisible( false );
        operationButtons.completeButton.setVisible( true );

        message.setText( i18nf( "install.msg.02", i18n( "common.btn.complete" ) ) );

      }
    } );

  }

  @SuppressWarnings( "unchecked" )
  private void install() {

    for ( String forgeVersion : installDetails.keySet() ) {

      File path = forgeVersionByJsonPath.get( forgeVersion );

      backup( path );

      ForgeVersion version = GsonUtils.readForgeVersion( path, forgeVersion );

      List< LibraryData > libraries = version.getLibraries();
      if ( libraries != null ) {

        Set< String > removeLibs = new HashSet< String >();
        for ( LibraryData data : installDetails.get( forgeVersion ) ) {
          String name = data.getName();
          removeLibs.add( name.substring( 0, name.lastIndexOf( ':' ) ) );
        }

        Iterator< LibraryData > it = libraries.iterator();
        while ( it.hasNext() ) {
          String name = it.next().getName();

          if ( removeLibs.contains( name.substring( 0, name.lastIndexOf( ':' ) ) ) )
            it.remove();

        }

        libraries.addAll( installDetails.get( forgeVersion ) );
      }
      version.setLibraries( libraries );

      BufferedWriter bw = null;
      try {

        bw = FileUtils.newWriter( path );
        if ( bw != null ) {

          bw.write( GsonUtils.toPrettyString( version ) );

          AppUtils.log( "install.msg.03", path.getAbsolutePath() );

        }

      } catch ( IOException e ) {

        AppUtils.log( e, "install.err.01", path.getAbsolutePath() );

      } finally {
        FileUtils.close( bw );
      }

      logDisplay.setText( AppUtils.logText() );
      SwingUtilities.invokeLater( new Runnable() {
        private void resetScrollBar( JScrollBar scrollBar ) {
          scrollBar.setValue( scrollBar.getMinimum() );
        }

        @Override
        public void run() {

          resetScrollBar( displayContainer.getVerticalScrollBar() );
          resetScrollBar( displayContainer.getHorizontalScrollBar() );

        }
      } );
    }

  }

  private void backup( File src ) {

    File backup = new File( src.getParentFile(), src.getName() + ".bak" );
    if ( FileUtils.copy( src, backup ) )
      AppUtils.log( "install.msg.04", backup.getAbsolutePath() );
    else
      AppUtils.log( "install.err.02", backup.getAbsolutePath() );

  }


  public void setOperationButtons( OperationButtons operationButtons ) {
    this.operationButtons = operationButtons;
  }

  public void setForgeVersionByJsonPath( Map< String, File > forgeVersionByJsonPath ) {
    this.forgeVersionByJsonPath = forgeVersionByJsonPath;
  }

  public void setInstallDetails( Map< String, Set< LibraryData > > installDetails ) {
    this.installDetails = installDetails;
  }

  @Override
  public void setVisible( boolean aFlag ) {
    super.setVisible( aFlag );
    if ( aFlag ) {

      Main.assertion( operationButtons != null, "common.err.01" );
      Main.assertion( !forgeVersionByJsonPath.isEmpty(), "common.err.04" );
      Main.assertion( !installDetails.isEmpty(), "install.err.03" );

      initialization();

    }
  }
}

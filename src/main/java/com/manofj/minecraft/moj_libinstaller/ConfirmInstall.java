package com.manofj.minecraft.moj_libinstaller;

import static com.manofj.minecraft.moj_libinstaller.utils.AppUtils.i18n;
import static com.manofj.minecraft.moj_libinstaller.utils.AppUtils.i18nf;

import java.awt.SystemColor;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.manofj.minecraft.moj_libinstaller.models.Libraries;
import com.manofj.minecraft.moj_libinstaller.models.LibraryData;
import com.manofj.minecraft.moj_libinstaller.utils.GsonUtils;


class ConfirmInstall extends JPanel {

  private static final long serialVersionUID = 1L;


  private Set< String > installSettings = Collections.emptySet();
  private Map< String, File > forgeVersionByJsonPath = Collections.emptyMap();
  private OperationButtons operationButtons = null;

  private JTextPane confirmMessage;
  private JScrollPane displayContainer;
  private JTextArea installDetailsDisplay;

  private Map< String, Set< LibraryData > > installDetails =
    new TreeMap< String, Set< LibraryData > >( new Comparator< String >() {
      @Override
      public int compare( String o1, String o2 ) {
        return o2.compareTo( o1 );
      }
    } );

  public ConfirmInstall() {

    SpringLayout springLayout = new SpringLayout();
    setLayout( springLayout );

    confirmMessage = new JTextPane();
    springLayout.putConstraint( SpringLayout.NORTH, confirmMessage, 0, SpringLayout.NORTH, this );
    springLayout.putConstraint( SpringLayout.SOUTH, confirmMessage, 48, SpringLayout.NORTH, this );
    springLayout.putConstraint( SpringLayout.WEST, confirmMessage, 0, SpringLayout.WEST, this );
    springLayout.putConstraint( SpringLayout.EAST, confirmMessage, 0, SpringLayout.EAST, this );
    add( confirmMessage );
    {
      confirmMessage.setBackground( SystemColor.control );
      confirmMessage.setDisabledTextColor( SystemColor.textText );
      confirmMessage.setEnabled( false );
      confirmMessage.setEditable( false );
    }

    displayContainer = new JScrollPane();
    springLayout.putConstraint( SpringLayout.NORTH, displayContainer, 0, SpringLayout.SOUTH, confirmMessage );
    springLayout.putConstraint( SpringLayout.WEST, displayContainer, 0, SpringLayout.WEST, this );
    springLayout.putConstraint( SpringLayout.SOUTH, displayContainer, 0, SpringLayout.SOUTH, this );
    springLayout.putConstraint( SpringLayout.EAST, displayContainer, 0, SpringLayout.EAST, this );
    add( displayContainer );

    installDetailsDisplay = new JTextArea();
    displayContainer.setViewportView( installDetailsDisplay );
    {
      installDetailsDisplay.setBorder( new EmptyBorder( 4, 4, 4, 4 ) );
      installDetailsDisplay.setDisabledTextColor( SystemColor.textText );
      installDetailsDisplay.setEnabled( false );
      installDetailsDisplay.setEditable( false );
    }

  }

  private void initialization() {

    operationButtons.resetAllButtons();

    buildInstallDetails();
    if ( installDetails.isEmpty() ) {

      confirmMessage.setText( i18nf( "confirm.msg.01", i18n( "common.btn.complete" ) ) );
      installDetailsDisplay.setText( i18n( "confirm.msg.02" ) );

      operationButtons.nextButton.setVisible( false );
      operationButtons.completeButton.setVisible( true );

    } else {

      confirmMessage.setText( i18nf( "confirm.msg.03", i18n( "common.btn.next" ) ) );
      installDetailsDisplay.setText( makeDisplayText() );

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

  private void buildInstallDetails() {
    installDetails.clear();

    Map< String, List< LibraryData > > mcVersionByDetail = new HashMap< String, List< LibraryData > >();
    for ( String path : installSettings ) {

      Map< String, List< LibraryData > > json = GsonUtils.readInstallSetting( path );
      for ( String mcVersion : json.keySet() ) {

        List< LibraryData > detail = mcVersionByDetail.get( mcVersion );
        if ( detail == null ) {

          detail = new ArrayList< LibraryData >();
          mcVersionByDetail.put( mcVersion, detail );

        }
        detail.addAll( json.get( mcVersion ) );

      }
    }

    for ( String forgeVersion : forgeVersionByJsonPath.keySet() ) {

      int hyphen = forgeVersion.indexOf( '-' );
      String mcVersion = forgeVersion.substring( 0, hyphen );

      List< LibraryData > detail = mcVersionByDetail.get( mcVersion );
      if ( detail != null ) {

        File jsonPath = forgeVersionByJsonPath.get( forgeVersion );
        Libraries libraries = GsonUtils.readFromJson( jsonPath, Libraries.class );

        Map< String, LibraryData > latestLibs = new HashMap< String, LibraryData >();
        for ( LibraryData data : detail ) {
          String name = data.getName();
          String mavenId = name.substring( 0, name.lastIndexOf( ':' ) );

          LibraryData latest = latestLibs.get( mavenId );
          if ( latest == null || name.compareTo( latest.getName() ) > 0 )
            latestLibs.put( mavenId, data );

        }

        Set< LibraryData > libs = new HashSet< LibraryData >( latestLibs.values() );
        List< LibraryData > values = libraries.getValues();
        if ( values == null )
          values = Collections.emptyList();
        libs.removeAll( values );

        if ( !libs.isEmpty() ) installDetails.put( forgeVersion, libs );

      }

    }

  }

  private String makeDisplayText() {

    StringWriter buf = new StringWriter();
    PrintWriter writer = new PrintWriter( buf );

    for ( String forgeVersion : installDetails.keySet() ) {

      writer.println( forgeVersion );
      writer.println( "//================" );

      for ( LibraryData elem : installDetails.get( forgeVersion ) )
        writer.println( "  " + elem.getName() );

      writer.println( "================//" );
      writer.println();

    }

    buf.flush();
    return buf.toString();

  }

  public void setOperationButtons( OperationButtons operationButtons ) {
    this.operationButtons = operationButtons;
  }

  public void setInstallSettings( Set< String > installSettings ) {
    this.installSettings = installSettings;
  }

  public void setForgeVersionByJsonPath( Map< String, File > forgeVersionByJsonPath ) {
    this.forgeVersionByJsonPath = forgeVersionByJsonPath;
  }

  public Map< String, Set< LibraryData > > getInstallDetails() {
    return Collections.unmodifiableMap( installDetails );
  }

  @Override
  public void setVisible( boolean aFlag ) {
    super.setVisible( aFlag );
    if ( aFlag ) {

      Main.assertion( operationButtons != null, "common.err.01" );
      Main.assertion( !installSettings.isEmpty(), "common.err.03" );
      Main.assertion( !forgeVersionByJsonPath.isEmpty(), "common.err.04" );

      initialization();

    }
  }
}

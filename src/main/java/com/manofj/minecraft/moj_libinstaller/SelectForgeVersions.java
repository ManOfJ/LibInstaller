package com.manofj.minecraft.moj_libinstaller;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static com.manofj.minecraft.moj_libinstaller.utils.AppUtils.i18n;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.manofj.minecraft.moj_libinstaller.models.LibraryData;
import com.manofj.minecraft.moj_libinstaller.utils.AppUtils;
import com.manofj.minecraft.moj_libinstaller.utils.FileUtils;
import com.manofj.minecraft.moj_libinstaller.utils.GsonUtils;


class SelectForgeVersions extends JPanel {

  private static final long serialVersionUID = 1L;


  public static final String GSON_LIBRARY_URL =
    "http://central.maven.org/maven2/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar";


  private Set< String > installSettings = Collections.emptySet();
  private OperationButtons operationButtons = null;

  private JTextField mcDirPath;
  private JPanel versionsContainer;
  private JList< String > versions;
  private DefaultListModel< String > versionsModel;

  private Map< String, File > versionByPath = new HashMap< String, File >();
  private Set< String > validMcVersions = new HashSet< String >();

  public SelectForgeVersions() {
    SpringLayout springLayout = new SpringLayout();
    setLayout( springLayout );

    JLabel mcDirLabel = new JLabel( i18n( "forge.msg.01" ) );
    springLayout.putConstraint( SpringLayout.NORTH, mcDirLabel, 0, SpringLayout.NORTH, this );
    springLayout.putConstraint( SpringLayout.WEST, mcDirLabel, 0, SpringLayout.WEST, this );
    add( mcDirLabel );

    JButton chooseMcDir = new JButton( "..." );
    springLayout.putConstraint( SpringLayout.NORTH, chooseMcDir, 8, SpringLayout.SOUTH, mcDirLabel );
    springLayout.putConstraint( SpringLayout.EAST, chooseMcDir, 0, SpringLayout.EAST, this );
    add( chooseMcDir );
    {
      chooseMcDir.addActionListener( new ActionListener() {
        @Override
        public void actionPerformed( ActionEvent e ) {

          JFileChooser chooser = new JFileChooser();
          chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

          switch ( chooser.showOpenDialog( null ) ) {
            default:
              break;
            case JFileChooser.APPROVE_OPTION:
              File file = chooser.getSelectedFile();
              mcDirPath.setText( file.getAbsolutePath() );
              break;
          }

        }
      } );
    }

    mcDirPath = new JTextField() {
      private static final long serialVersionUID = 1L;

      @Override
      protected void paintComponent( Graphics g ) {
        super.paintComponent( g );
        if ( getText().isEmpty() ) {

          Graphics2D g2d = (Graphics2D) g;
          g2d.setRenderingHint( KEY_ANTIALIASING, VALUE_ANTIALIAS_ON );
          g2d.setColor( getDisabledTextColor() );
          g2d.drawString(
            i18n( "forge.msg.02" ),
            getInsets().left,
            g.getFontMetrics().getMaxAscent() + getInsets().top );

        }
      }
    };
    springLayout.putConstraint( SpringLayout.NORTH, mcDirPath, 0, SpringLayout.NORTH, chooseMcDir );
    springLayout.putConstraint( SpringLayout.WEST, mcDirPath, 0, SpringLayout.WEST, this );
    springLayout.putConstraint( SpringLayout.SOUTH, mcDirPath, 0, SpringLayout.SOUTH, chooseMcDir );
    springLayout.putConstraint( SpringLayout.EAST, mcDirPath, -4, SpringLayout.WEST, chooseMcDir );
    add( mcDirPath );
    {
      mcDirPath.getDocument().addDocumentListener( new DocumentListener() {
        @Override public void removeUpdate( DocumentEvent e ) {
          forgeVersionValidation();
        }
        @Override public void insertUpdate( DocumentEvent e ) {
          forgeVersionValidation();
        }
        @Override public void changedUpdate( DocumentEvent e ) {
          forgeVersionValidation();
        }
      } );
    }

    versionsContainer = new JPanel();
    springLayout.putConstraint( SpringLayout.NORTH, versionsContainer, 8, SpringLayout.SOUTH, chooseMcDir );
    springLayout.putConstraint( SpringLayout.WEST, versionsContainer, 0, SpringLayout.WEST, mcDirLabel );
    springLayout.putConstraint( SpringLayout.SOUTH, versionsContainer, 0, SpringLayout.SOUTH, this );
    springLayout.putConstraint( SpringLayout.EAST, versionsContainer, 0, SpringLayout.EAST, this );
    add( versionsContainer );
    SpringLayout sl_panel = new SpringLayout();
    versionsContainer.setLayout( sl_panel );

    JLabel versionsLabel = new JLabel( i18n( "forge.msg.03" ) );
    sl_panel.putConstraint( SpringLayout.NORTH, versionsLabel, 0, SpringLayout.NORTH, versionsContainer );
    sl_panel.putConstraint( SpringLayout.WEST, versionsLabel, 0, SpringLayout.WEST, versionsContainer );
    versionsContainer.add( versionsLabel );

    versions = new JList< String >();
    versionsModel = new DefaultListModel< String >();
    sl_panel.putConstraint( SpringLayout.NORTH, versions, 8, SpringLayout.SOUTH, versionsLabel );
    sl_panel.putConstraint( SpringLayout.WEST, versions, 0, SpringLayout.WEST, versionsContainer );
    sl_panel.putConstraint( SpringLayout.SOUTH, versions, 0, SpringLayout.SOUTH, versionsContainer );
    sl_panel.putConstraint( SpringLayout.EAST, versions, 0, SpringLayout.EAST, versionsContainer );
    versionsContainer.add( versions );
    {
      versions.setBorder( new CompoundBorder( new EtchedBorder( EtchedBorder.LOWERED, null, null ), null ) );
      versions.setModel( versionsModel );
      versions.setToolTipText( i18n( "forge.msg.04" ) );

      versions.addKeyListener( new KeyListener() {
        @Override public void keyTyped( KeyEvent e ) {}
        @Override public void keyPressed( KeyEvent e ) {}

        @Override
        public void keyReleased( KeyEvent e ) {
          switch ( e.getKeyCode() ) {
            default:
              break;
            case KeyEvent.VK_DELETE:

              for ( Object elem : versions.getSelectedValues() ) {
                versionsModel.removeElement( elem );
                versionByPath.remove( String.valueOf( elem ) );
              }

              break;
          }
        }
      } );
    }

  }

  private String findMinecraftDir() {
    try {

      String appData = System.getenv( "APPDATA" );
      File path = new File( appData, ".minecraft" ).getCanonicalFile();
      if ( path.isDirectory() ) return path.getAbsolutePath();

      File cd = new File( "./" ).getCanonicalFile();
      File versionsDir = new File( cd, "versions" );
      if ( versionsDir.isDirectory() ) return cd.getAbsolutePath();

    } catch ( IOException e ) {
      AppUtils.log( e, "common.err.02" );
    }

    return "";
  }

  private File getMinecraftDir() {

    try {

      String path = mcDirPath.getText();
      if ( !path.isEmpty() ) {

        File pathname = new File( path ).getCanonicalFile();
        if ( pathname.isDirectory() ) return pathname;

      }

    } catch ( IOException e ) { /* ignore */ }

    return null;

  }

  private boolean loadGsonLibrary( File mcDir ) {
    try {

      Class.forName( "com.google.gson.Gson" );
      return true;

    } catch ( ClassNotFoundException e ) {

      try {

        File libsDir = new File( mcDir, "libraries" );
        File gsonDir = new File( libsDir, "com/google/code/gson" ).getCanonicalFile();
        return AppUtils.loadLibrary( gsonDir, "gson-" );

      } catch ( IOException e1 ) {
        AppUtils.log( e1, i18n( "common.err.02" ) );
      }

    }
    return false;
  }

  private void downloadGsonLibrary() {

    String tmpDir = System.getenv( "TMP" );
    if ( tmpDir.equals( "TMP" ) )
      tmpDir = System.getProperty( "java.io.tmpdir" );

    if ( tmpDir != null ) {

      File dir = new File( tmpDir, "moj" );
      File gsonJar = new File( dir, "gson-2.2.4.jar" );
      if ( gsonJar.isFile() ) {

        AppUtils.loadLibrary( dir, "gson-" );
        return;

      }

      try {

        if ( dir.mkdirs() ) AppUtils.log( "forge.msg.05", dir.getAbsolutePath() );
        FileUtils.download( gsonJar, new URL( GSON_LIBRARY_URL ) );

        AppUtils.log( "forge.msg.06", gsonJar.getAbsolutePath() );

      } catch ( IOException e ) {

        AppUtils.log( e, "forge.err.01" );

      }

      AppUtils.loadLibrary( dir, "gson-" );

    }

  }

  private void buildValidMinecraftVersion() {
    if ( !validMcVersions.isEmpty() ) return;

    for ( String path : installSettings ) {

      Map< String, List< LibraryData > > setting = GsonUtils.readInstallSetting( path );
      validMcVersions.addAll( setting.keySet() );

    }

  }

  private void listupForgeVersions( File mcDir ) {
    versionsModel.clear();

    File versionsPath = new File( mcDir, "versions" );
    if ( versionsPath.isDirectory() ) {
      versionByPath.clear();

      for ( File version : FileUtils.list( versionsPath ) ) {
        if ( !version.isDirectory() ) continue;

        String name = version.getName();
        if ( isInstallableForgeVersion( name ) ) {

          File json = null;
          for ( File path : FileUtils.list( version ) ) {
            if ( path.isFile() && path.getName().endsWith( ".json" ) ) {

              json = path;
              break;

            }
          }
          if ( json != null ) versionByPath.put( name, json );

        }
      }

      List< String > tmp = new ArrayList< String >( versionByPath.keySet() );
      Collections.sort( tmp );
      Collections.reverse( tmp );
      for ( String s : tmp ) versionsModel.addElement( s );

    }
  }

  private boolean isInstallableForgeVersion( String version ) {
    if ( version.contains( "forge" ) || version.contains( "Forge" ) ) {
      int hyphen = version.indexOf( '-' );
      if ( hyphen != -1 ) {

        String mcVersion = version.substring( 0, hyphen );
        return validMcVersions.contains( mcVersion );

      }
    }
    return false;
  }

  private void initialization() {

    if ( mcDirPath.getText().isEmpty() )
      mcDirPath.setText( findMinecraftDir() );

    forgeVersionValidation();

  }

  private void forgeVersionValidation() {

    operationButtons.resetAllButtons();
    operationButtons.nextButton.setEnabled( false );

    versionsContainer.setVisible( false );

    File mcDir = getMinecraftDir();
    if ( mcDir != null ) {

      if ( !loadGsonLibrary( mcDir ) ) {

        switch ( JOptionPane.showConfirmDialog( this, i18n( "forge.msg.07" ) ) ) {
          case JOptionPane.YES_OPTION:

            downloadGsonLibrary();
            Main.assertion( loadGsonLibrary( mcDir ), "forge.err.02" );
            break;

          case JOptionPane.NO_OPTION:

            Main.assertion( false, "forge.err.02" );
            break;

        }

      }

      buildValidMinecraftVersion();
      listupForgeVersions( mcDir );

      operationButtons.nextButton.setEnabled( !versionsModel.isEmpty() );

      versionsContainer.setVisible( true );
    }

  }


  public void setOperationButtons( OperationButtons operationButtons ) {
    this.operationButtons = operationButtons;
  }

  public void setInstallSettings( Set< String > installSettings ) {
    this.installSettings = installSettings;
    validMcVersions.clear();
  }

  public Map< String, File > getForgeVersionByJsonPath() {
    return Collections.unmodifiableMap( versionByPath );
  }

  @Override
  public void setVisible( boolean aFlag ) {
    super.setVisible( aFlag );
    if ( aFlag ) {

      Main.assertion( operationButtons != null, "common.err.01" );
      Main.assertion( !installSettings.isEmpty(), "common.err.03" );

      initialization();

    }
  }

}

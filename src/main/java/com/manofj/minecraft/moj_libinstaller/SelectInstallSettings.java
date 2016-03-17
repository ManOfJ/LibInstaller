package com.manofj.minecraft.moj_libinstaller;

import static com.manofj.minecraft.moj_libinstaller.utils.AppUtils.i18n;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.TransferHandler;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.manofj.minecraft.moj_libinstaller.utils.AppUtils;
import com.manofj.minecraft.moj_libinstaller.utils.FileUtils;


class SelectInstallSettings extends JPanel {

  private static final long serialVersionUID = 1L;


  private JList< String > settings;
  private DefaultListModel< String > settingsModel;

  private OperationButtons operationButtons = null;

  public SelectInstallSettings( String[] defaultSettings ) {

    SpringLayout springLayout = new SpringLayout();
    setLayout( springLayout );

    JLabel listLabel = new JLabel( i18n( "setting.msg.01" ) );
    springLayout.putConstraint( SpringLayout.NORTH, listLabel, 0, SpringLayout.NORTH, this );
    springLayout.putConstraint( SpringLayout.WEST, listLabel, 0, SpringLayout.WEST, this );
    add( listLabel );

    settings = new JList< String >();
    settingsModel = new DefaultListModel< String >();
    springLayout.putConstraint( SpringLayout.NORTH, settings, 8, SpringLayout.SOUTH, listLabel );
    springLayout.putConstraint( SpringLayout.WEST, settings, 0, SpringLayout.WEST, this );
    springLayout.putConstraint( SpringLayout.SOUTH, settings, 0, SpringLayout.SOUTH, this );
    springLayout.putConstraint( SpringLayout.EAST, settings, 0, SpringLayout.EAST, this );
    add( settings );

    {
      settings.setToolTipText( i18n( "setting.msg.02" ) );
      settings.setModel( settingsModel );
      settings.setBorder( new EtchedBorder( EtchedBorder.LOWERED, null, null ) );

      for ( String setting : defaultSettings ) {
        File file = new File( setting );
        addInstallSetting( file );
      }
      addInstallSetting( new File( "./" ) );

      settings.setTransferHandler( new TransferHandler() {
        private static final long serialVersionUID = 1L;


        @Override
        public boolean canImport( TransferSupport support ) {
          return support.isDataFlavorSupported( DataFlavor.javaFileListFlavor );
        }

        @SuppressWarnings( "unchecked" )
        @Override
        public boolean importData( TransferSupport support ) {
          if ( canImport( support ) ) {
            try {

              Transferable data = support.getTransferable();
              List< File > files = (List< File >) data.getTransferData( DataFlavor.javaFileListFlavor );
              for ( File f : files ) addInstallSetting( f );

              return true;

            } catch ( Exception e ) {

              AppUtils.log( e, "setting.err.01" );

            }
          }
          return false;
        }
      } );

      settings.addKeyListener( new KeyListener() {
        @Override public void keyTyped( KeyEvent e ) {}
        @Override public void keyPressed( KeyEvent e ) {}

        @Override
        public void keyReleased( KeyEvent e ) {
          switch ( e.getKeyCode() ) {
            default: break;
            case KeyEvent.VK_DELETE:

              for ( Object elem : settings.getSelectedValues() )
                settingsModel.removeElement( elem );

              break;
          }
        }
      } );

      settingsModel.addListDataListener( new ListDataListener() {
        @Override public void intervalRemoved( ListDataEvent e ) { validation(); }
        @Override public void intervalAdded( ListDataEvent e ) { validation(); }
        @Override public void contentsChanged( ListDataEvent e ) { validation(); }
      } );
    }

  }

  private void initialization() { validation(); }

  private void validation() {

    operationButtons.resetAllButtons();
    operationButtons.nextButton.setEnabled( !settingsModel.isEmpty() );
    operationButtons.prevButton.setVisible( false );

  }

  private void addInstallSetting( File pathname ) {
    try {
      File canonicalPathname = pathname.getCanonicalFile();

      if ( canonicalPathname.isFile() ) {

        String name = canonicalPathname.getName();
        if ( name.startsWith( "libinstall-" ) && name.endsWith( ".json" ) ) {

          String absolutePath = canonicalPathname.getAbsolutePath();
          if ( !settingsModel.contains( absolutePath ) )
            settingsModel.addElement( absolutePath );

        }

      } else if ( canonicalPathname.isDirectory() ) {

        for ( File f : FileUtils.list( canonicalPathname ) )
          if ( f.isFile() ) addInstallSetting( f );

      } else {

        AppUtils.log( "setting.err.02", pathname.getAbsolutePath() );

      }
    } catch ( IOException e ) {

      AppUtils.log( "common.err.02", pathname.getAbsoluteFile() );

    }
  }


  public void setOperationButtons( OperationButtons operationButtons ) {
    this.operationButtons = operationButtons;
  }

  public Set< String > getInstallSettings() {
    Set< String > set = new HashSet< String >();

    Enumeration< String > elements = settingsModel.elements();
    while ( elements.hasMoreElements() ) set.add( elements.nextElement() );

    return set;
  }

  @Override
  public void setVisible( boolean aFlag ) {
    super.setVisible( aFlag );
    if ( aFlag ) {

      Main.assertion( operationButtons != null, "common.err.01" );

      initialization();

    }
  }
}

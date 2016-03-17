package com.manofj.minecraft.moj_libinstaller;

import static com.manofj.minecraft.moj_libinstaller.utils.AppUtils.i18n;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Map;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.manofj.minecraft.moj_libinstaller.models.LibraryData;
import com.manofj.minecraft.moj_libinstaller.utils.AppUtils;


public class Main extends JFrame {

  private static final long serialVersionUID = 1L;


  private JPanel mainPane;
  private CardLayout mainLayout;

  private SelectInstallSettings selectInstallSettings;
  private SelectForgeVersions selectForgeVersions;
  private ConfirmInstall confirmInstall;
  private Install install;


  static void assertion( boolean _assert, String message, Object... args ) {
    if ( _assert ) return;

    AppUtils.log( message, args );
    ApplicationError.showDialog();

  }


  public static void main( final String[] args ) {
    SwingUtilities.invokeLater( new Runnable() {
      public void run() {
        try {
          JFrame frame = new Main( args );
          frame.setVisible( true );
        } catch ( Exception e ) {
          e.printStackTrace();
        }
      }
    } );
  }


  public Main( String[] args ) {

    {
      setTitle( i18n( "main.msg.01" ) );
      setSize( 480, 360 );
      setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
      setResizable( false );
      setLocationRelativeTo( null );
    }

    selectInstallSettings = new SelectInstallSettings( args );
    selectForgeVersions = new SelectForgeVersions();
    confirmInstall = new ConfirmInstall();
    install = new Install();
    OperationButtons operationButtons = new OperationButtons();

    JPanel contentPane = new JPanel();
    contentPane.setBorder( null );
    setContentPane( contentPane );
    SpringLayout sl_contentPane = new SpringLayout();
    contentPane.setLayout( sl_contentPane );

    mainPane = new JPanel();
    sl_contentPane.putConstraint( SpringLayout.SOUTH, mainPane, -36, SpringLayout.SOUTH, contentPane );
    sl_contentPane.putConstraint( SpringLayout.NORTH, mainPane, 0, SpringLayout.NORTH, contentPane );
    sl_contentPane.putConstraint( SpringLayout.WEST, mainPane, 0, SpringLayout.WEST, contentPane );
    sl_contentPane.putConstraint( SpringLayout.EAST, mainPane, 0, SpringLayout.EAST, contentPane );
    contentPane.add( mainPane );

    {
      mainLayout = new CardLayout();

      mainPane.setBorder( new EmptyBorder( 4, 4, 0, 4 ) );
      mainPane.setLayout( mainLayout );

      mainPane.add( selectInstallSettings, "select_install_settings" );
      mainPane.add( selectForgeVersions, "select_forge_versions" );
      mainPane.add( confirmInstall, "confirm_install" );
      mainPane.add( install, "install" );
    }

    JPanel footer = new JPanel();
    sl_contentPane.putConstraint( SpringLayout.SOUTH, footer, 0, SpringLayout.SOUTH, contentPane );
    sl_contentPane.putConstraint( SpringLayout.NORTH, footer, -32, SpringLayout.SOUTH, contentPane );
    sl_contentPane.putConstraint( SpringLayout.WEST, footer, 0, SpringLayout.WEST, contentPane );
    sl_contentPane.putConstraint( SpringLayout.EAST, footer, 0, SpringLayout.EAST, contentPane );
    contentPane.add( footer );

    {
      footer.setBorder( new EmptyBorder( 0, 4, 4, 4 ) );
      footer.setLayout( new BorderLayout( 0, 0 ) );
      footer.add( operationButtons, BorderLayout.CENTER );

      operationButtons.completeButton.addActionListener( new ActionListener() {
        @Override public void actionPerformed( ActionEvent e ) { dispose(); }
      } );

      operationButtons.cancelButton.addActionListener( new ActionListener() {
        @Override public void actionPerformed( ActionEvent e ) { confirmExit(); }
      } );

      operationButtons.nextButton.addActionListener( new ActionListener() {
        @Override
        public void actionPerformed( ActionEvent e ) {
          for ( Component c : mainPane.getComponents() ) {
            if ( !c.isVisible() ) continue;

            if ( c instanceof SelectInstallSettings ) {

              Set< String > settings = selectInstallSettings.getInstallSettings();
              selectForgeVersions.setInstallSettings( settings );

              mainLayout.next( mainPane );

              break;

            } else if ( c instanceof SelectForgeVersions ) {

              Set< String > settings = selectInstallSettings.getInstallSettings();
              Map< String, File > targets = selectForgeVersions.getForgeVersionByJsonPath();
              confirmInstall.setInstallSettings( settings );
              confirmInstall.setForgeVersionByJsonPath( targets );

              mainLayout.next( mainPane );

              break;

            } else if ( c instanceof ConfirmInstall ) {

              Map< String, File > targets = selectForgeVersions.getForgeVersionByJsonPath();
              Map< String, Set< LibraryData > > installDetails = confirmInstall.getInstallDetails();
              install.setForgeVersionByJsonPath( targets );
              install.setInstallDetails( installDetails );

              mainLayout.next( mainPane );

              break;

            } else {

              assertion( false, "main.err.01", c.getClass() );

            }
          }
        }
      } );

      operationButtons.prevButton.addActionListener( new ActionListener() {
        @Override public void actionPerformed( ActionEvent e ) { mainLayout.previous( mainPane ); }
      } );
    }

    {
      selectInstallSettings.setOperationButtons( operationButtons );
      selectForgeVersions.setOperationButtons( operationButtons );
      confirmInstall.setOperationButtons( operationButtons );
      install.setOperationButtons( operationButtons );

      mainLayout.first( mainPane );

      addWindowListener( new WindowAdapter() {
        @Override public void windowClosing( WindowEvent e ) { confirmExit(); }
      } );
    }

  }


  private void confirmExit() {

    int result = JOptionPane.showConfirmDialog(
      this,
      i18n( "main.msg.03" ),
      i18n( "main.msg.02" ),
      JOptionPane.YES_NO_OPTION );

    if ( result == JOptionPane.YES_OPTION ) this.dispose();

  }

}

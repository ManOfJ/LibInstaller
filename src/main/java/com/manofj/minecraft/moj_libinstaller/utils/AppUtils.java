package com.manofj.minecraft.moj_libinstaller.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;


public final class AppUtils {
  private AppUtils() {}


  private static final ResourceBundle messages = ResourceBundle.getBundle( "application" );

  private static final StringWriter logBuffer = new StringWriter();
  private static final PrintWriter logPrinter = new PrintWriter( logBuffer );

  private static final Set< URL > libraries = new HashSet< URL >();


  private static boolean addLibraryURL( URL url ) {
    if ( !libraries.add( url ) ) return false;

    ClassLoader loader = ClassLoader.getSystemClassLoader();
    if ( loader instanceof URLClassLoader ) {

      try {

        Method addURL = ( URLClassLoader.class ).getDeclaredMethod( "addURL", URL.class );
        addURL.setAccessible( true );
        addURL.invoke( loader, url );
        return true;

      } catch ( Exception e ) {

        log( e, "apputils.err.03" );

      }

    }

    return false;
  }


  public static String i18n( String key ) {
    return messages.containsKey( key ) ? messages.getString( key ) : key;
  }

  public static String i18nf( String key, Object... args ) {
    return String.format( i18n( key ), args );
  }

  public static void log( String message, Object... args ) {

    logPrinter.println( i18nf( message, args ) );

  }

  public static void log( Throwable cause, String message, Object... args ) {

    log( message, args );
    cause.printStackTrace( logPrinter );

  }

  public static String logText() {

    logPrinter.flush();
    return logBuffer.toString();

  }

  public static boolean loadLibrary( File dir, String libName ) {

    if ( !dir.isDirectory() ) {

      log( "apputils.err.01", dir );
      return false;

    }

    for ( File f : FileUtils.list( dir ) ) {
      if ( f.isDirectory() ) {

        if ( loadLibrary( f, libName ) ) return true;

      } else if ( f.isFile() ) {

        String name = f.getName();
        if ( name.startsWith( libName ) && name.endsWith( ".jar" ) ) {

          try {

            URL url = f.toURI().toURL();
            if ( addLibraryURL( url ) ) {

              log( "apputils.msg.01", name );
              return true;

            }

          } catch ( IOException e ) { /* ignore */ }

        }

      }
    }

    log( "apputils.err.02", dir.getAbsolutePath(), libName );
    return false;
  }

}

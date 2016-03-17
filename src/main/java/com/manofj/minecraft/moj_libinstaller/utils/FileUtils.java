package com.manofj.minecraft.moj_libinstaller.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;


public class FileUtils {
  private FileUtils() {}


  public static final File[] EMPTY_LIST = new File[ 0 ];

  public static boolean download( File dist, URL src ) {

    InputStream is = null;
    ReadableByteChannel in = null;
    FileOutputStream fos = null;
    FileChannel out = null;
    try {

      URLConnection connection = src.openConnection();
      is = connection.getInputStream();
      in = Channels.newChannel( is );

      fos = new FileOutputStream( dist );
      out = fos.getChannel();

      out.transferFrom( in, 0, connection.getContentLength() );

      return true;

    } catch ( IOException e ) {

      return false;

    } finally {
      close( in, is, out, fos );
    }

  }

  public static boolean copy( File src, File dist ) {

    FileInputStream fis = null;
    FileChannel in = null;
    FileOutputStream fos = null;
    FileChannel out = null;
    try {

      fis = new FileInputStream( src );
      in = fis.getChannel();
      fos = new FileOutputStream( dist );
      out = fos.getChannel();

      in.transferTo( 0, in.size(), out );

      return true;

    } catch ( IOException e ) {

      return false;

    } finally {
      close( in, fis, out, fos );
    }

  }

  public static BufferedReader newReader( File pathname ) {

    InputStream is = null;
    try {

      is = new FileInputStream( pathname );
      return new BufferedReader( new InputStreamReader( is, "UTF-8" ) );

    } catch ( IOException e ) { close( is ); }

    return null;

  }

  public static BufferedWriter newWriter( File pathname ) {

    OutputStream os = null;
    try {

      os = new FileOutputStream( pathname );
      return new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );

    } catch ( IOException e ) { close( os ); }

    return null;

  }

  public static File[] list( File pathname ) {
    if ( !pathname.isDirectory() ) return EMPTY_LIST;

    File[] list = pathname.listFiles();
    return list == null ? EMPTY_LIST : list;

  }

  public static void close( Closeable... closeables ) {

    for ( Closeable c : closeables )
      try { c.close(); } catch ( IOException e ) { /* ignore */ }

  }

}

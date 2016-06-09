package com.manofj.minecraft.moj_libinstaller.utils;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.manofj.minecraft.moj_libinstaller.models.ForgeVersion;
import com.manofj.minecraft.moj_libinstaller.models.LibraryData;


public final class GsonUtils {
  private GsonUtils() {}


  private static final Map< String, Map< String, List< LibraryData > > > installSettingCache;

  static {

    installSettingCache = new HashMap< String, Map< String, List< LibraryData > > >();

  }


  private static Type _installSettingJsonType = null;
  private static Type installSettingJsonType() {
    if ( _installSettingJsonType == null )
      _installSettingJsonType = new TypeToken< Map< String, List< LibraryData > > >(){}.getType();

    return _installSettingJsonType;
  }


  public static String toString( Object obj ) {

    Gson gson = new Gson();
    return gson.toJson( obj );

  }

  public static String toPrettyString( Object obj ) {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson( obj );

  }

  public static < T > T readFromJson( File file, Class< T > clazz, Gson gson ) {

    Reader r = FileUtils.newReader( file );
    try {

      return ( r == null ) ? null : gson.fromJson( r, clazz );

    } finally {
      FileUtils.close( r );
    }

  }

  public static < T > T readFromJson( String path, Class< T > clazz, Gson gson ) {
    return readFromJson( new File( path ), clazz, gson );
  }

  public static < T > T readFromJson( File file, Class< T > clazz ) {
    return readFromJson( file, clazz, new Gson() );
  }

  public static < T > T readFromJson( String path, Class< T > clazz ) {
    return readFromJson( path, clazz, new Gson() );
  }

  public static < T > T readFromJson( File file, Type type, Gson gson ) {

    Reader r = FileUtils.newReader( file );
    try {

      return ( r == null ) ? null : gson.< T >fromJson( r, type );

    } finally {
      FileUtils.close( r );
    }

  }

  public static < T > T readFromJson( String path, Type type, Gson gson ) {
    return readFromJson( new File( path ), type, gson );
  }

  public static < T > T readFromJson( File file, Type type ) {
    return readFromJson( file, type, new Gson() );
  }

  public static < T > T readFromJson( String path, Type type ) {
    return readFromJson( path, type, new Gson() );
  }


  public static Map< String, List< LibraryData > > readInstallSetting( String path ) {

    if ( !installSettingCache.containsKey( path ) ) {

      Map< String, List< LibraryData > > installSetting = readFromJson( path, installSettingJsonType() );
      installSettingCache.put( path, installSetting );

    }
    return installSettingCache.get( path );

  }

  public static ForgeVersion readForgeVersion( File path, String forgeVersion ) {

    Gson gson = new GsonBuilder().create();
    return readFromJson( path, ForgeVersion.class, gson );

  }

}

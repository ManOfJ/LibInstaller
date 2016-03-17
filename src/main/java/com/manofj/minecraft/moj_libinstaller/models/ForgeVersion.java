package com.manofj.minecraft.moj_libinstaller.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Since;

import com.manofj.minecraft.moj_libinstaller.utils.ObjectUtils;


public final class ForgeVersion {

  private static transient Map< String, Double > versionByJsonSince;

  static {

    Map< String, Double > tmp = new HashMap< String, Double >();
    tmp.put( "1.8.9", 0.1 );
    versionByJsonSince = Collections.unmodifiableMap( tmp );

  }

  private String inheritsFrom;
  private String id;
  private String time;
  private String releaseTime;
  private String type;
  private String minecraftArguments;
  private List< Object > libraries;
  private String mainClass;
  private int minimumLauncherVersion;
  private String assets;
  private String jar;

  @Since( 0.1 )
  private Object downloads;


  public static double forgeVersionToJsonSince( String forgeVersion ) {

    String mcVersion;
    {

      int index = forgeVersion.indexOf( '-' );
      if ( index != -1 )
        mcVersion = forgeVersion.substring( 0, index );
      else
        mcVersion = forgeVersion;

    }

    if ( versionByJsonSince.containsKey( mcVersion ) )
      return versionByJsonSince.get( mcVersion );

    return 0;

  }


  public String getInheritsFrom() {
    return inheritsFrom;
  }

  public String getId() {
    return id;
  }

  public String getTime() {
    return time;
  }

  public String getReleaseTime() {
    return releaseTime;
  }

  public String getType() {
    return type;
  }

  public String getMinecraftArguments() {
    return minecraftArguments;
  }

  public List< Object > getLibraries() {
    return libraries == null ? null : new ArrayList< Object >( libraries );
  }

  public String getMainClass() {
    return mainClass;
  }

  public int getMinimumLauncherVersion() {
    return minimumLauncherVersion;
  }

  public String getAssets() {
    return assets;
  }

  public String getJar() {
    return jar;
  }

  public void setInheritsFrom( String inheritsFrom ) {
    this.inheritsFrom = inheritsFrom;
  }

  public void setId( String id ) {
    this.id = id;
  }

  public void setTime( String time ) {
    this.time = time;
  }

  public void setReleaseTime( String releaseTime ) {
    this.releaseTime = releaseTime;
  }

  public void setType( String type ) {
    this.type = type;
  }

  public void setMinecraftArguments( String minecraftArguments ) {
    this.minecraftArguments = minecraftArguments;
  }

  public void setLibraries( List< Object > libraries ) {
    this.libraries = libraries;
    if ( libraries != null )
      this.libraries = new ArrayList< Object >( libraries );
  }

  public void setMainClass( String mainClass ) {
    this.mainClass = mainClass;
  }

  public void setMinimumLauncherVersion( int minimumLauncherVersion ) {
    this.minimumLauncherVersion = minimumLauncherVersion;
  }

  public void setAssets( String assets ) {
    this.assets = assets;
  }

  public void setJar( String jar ) {
    this.jar = jar;
  }


  public Object getDownloads() {
    return downloads;
  }

  public void setDownloads( Object downloads ) {
    this.downloads = downloads;
  }

  @Override
  public boolean equals( Object obj ) {
    if ( this == obj ) return true;

    if ( obj instanceof ForgeVersion ) {

      ForgeVersion other = (ForgeVersion) obj;
      return ObjectUtils.equals( this.getInheritsFrom(),           other.getInheritsFrom() )
          && ObjectUtils.equals( this.getId(),                     other.getId() )
          && ObjectUtils.equals( this.getTime(),                   other.getTime() )
          && ObjectUtils.equals( this.getReleaseTime(),            other.getReleaseTime() )
          && ObjectUtils.equals( this.getType(),                   other.getType() )
          && ObjectUtils.equals( this.getMinecraftArguments(),     other.getMinecraftArguments() )
          && ObjectUtils.equals( this.getLibraries(),              other.getLibraries() )
          && ObjectUtils.equals( this.getMainClass(),              other.getMainClass() )
          && ObjectUtils.equals( this.getMinimumLauncherVersion(), other.getMinimumLauncherVersion() )
          && ObjectUtils.equals( this.getAssets(),                 other.getAssets() )
          && ObjectUtils.equals( this.getJar(),                    other.getJar() )
          && ObjectUtils.equals( this.getDownloads(),              other.getDownloads() );

    }
    return false;

  }

  @Override
  public int hashCode() {

    int result = 1;
    result = 31 * result + ( inheritsFrom != null ? inheritsFrom.hashCode() : 0 );
    result = 31 * result + ( id != null ? id.hashCode() : 0 );
    result = 31 * result + ( time != null ? time.hashCode() : 0 );
    result = 31 * result + ( releaseTime != null ? releaseTime.hashCode() : 0 );
    result = 31 * result + ( type != null ? type.hashCode() : 0 );
    result = 31 * result + ( minecraftArguments != null ? minecraftArguments.hashCode() : 0 );
    result = 31 * result + ( libraries != null ? libraries.hashCode() : 0 );
    result = 31 * result + ( mainClass != null ? mainClass.hashCode() : 0 );
    result = 31 * result + minimumLauncherVersion;
    result = 31 * result + ( assets != null ? assets.hashCode() : 0 );
    result = 31 * result + ( jar != null ? jar.hashCode() : 0 );
    result = 31 * result + ( downloads != null ? downloads.hashCode() : 0 );
    return result;

  }
}

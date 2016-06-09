package com.manofj.minecraft.moj_libinstaller.models;

import java.util.ArrayList;
import java.util.List;

import com.manofj.minecraft.moj_libinstaller.utils.ObjectUtils;


public final class ForgeVersion {

  private String inheritsFrom;
  private String id;
  private String time;
  private String releaseTime;
  private String type;
  private String minecraftArguments;
  private List< LibraryData > libraries;
  private String mainClass;
  private Integer minimumLauncherVersion;
  private String assets;
  private String jar;
  private Object downloads;


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

  public List< LibraryData > getLibraries() {
    return ( libraries == null ) ? null : new ArrayList< LibraryData >( libraries );
  }

  public String getMainClass() {
    return mainClass;
  }

  public Integer getMinimumLauncherVersion() {
    return minimumLauncherVersion;
  }

  public String getAssets() {
    return assets;
  }

  public String getJar() {
    return jar;
  }

  public Object getDownloads() {
    return downloads;
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

  public void setLibraries( List< LibraryData > libraries ) {
    this.libraries = ( libraries == null ) ? null : new ArrayList< LibraryData >( libraries );
  }

  public void setMainClass( String mainClass ) {
    this.mainClass = mainClass;
  }

  public void setMinimumLauncherVersion( Integer minimumLauncherVersion ) {
    this.minimumLauncherVersion = minimumLauncherVersion;
  }

  public void setAssets( String assets ) {
    this.assets = assets;
  }

  public void setJar( String jar ) {
    this.jar = jar;
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
    result = 31 * result + ( inheritsFrom           == null ? 0 : inheritsFrom.hashCode() );
    result = 31 * result + ( id                     == null ? 0 : id.hashCode() );
    result = 31 * result + ( time                   == null ? 0 : time.hashCode() );
    result = 31 * result + ( releaseTime            == null ? 0 : releaseTime.hashCode() );
    result = 31 * result + ( type                   == null ? 0 : type.hashCode() );
    result = 31 * result + ( minecraftArguments     == null ? 0 : minecraftArguments.hashCode() );
    result = 31 * result + ( libraries              == null ? 0 : libraries.hashCode() );
    result = 31 * result + ( mainClass              == null ? 0 : mainClass.hashCode() );
    result = 31 * result + ( minimumLauncherVersion == null ? 0 : minimumLauncherVersion.hashCode() );
    result = 31 * result + ( assets                 == null ? 0 : assets.hashCode() );
    result = 31 * result + ( jar                    == null ? 0 : jar.hashCode() );
    result = 31 * result + ( downloads              == null ? 0 : downloads.hashCode() );
    return result;

  }
}

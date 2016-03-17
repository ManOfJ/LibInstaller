package com.manofj.minecraft.moj_libinstaller.models;

import com.manofj.minecraft.moj_libinstaller.utils.ObjectUtils;


public final class LibraryData {

  private String name;
  private String url;

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public void setUrl( String url ) {
    this.url = url;
  }


  @Override
  public boolean equals( Object obj ) {
    if ( obj == this ) return true;

    if ( obj instanceof LibraryData ) {

      LibraryData other = (LibraryData) obj;
      return ObjectUtils.equals( this.getName(), other.getName() )
          && ObjectUtils.equals( this.getUrl(), other.getUrl() );

    }
    return false;

  }

  @Override
  public int hashCode() {

    int result = 1;
    result = 31 * result + ( getName() == null ? 0 : getName().hashCode() );
    result = 31 * result + ( getUrl()  == null ? 0 : getUrl().hashCode()  );

    return result;

  }

}

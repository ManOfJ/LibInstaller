package com.manofj.minecraft.moj_libinstaller.models;

import com.manofj.minecraft.moj_libinstaller.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


public final class LibraryData {

  private String name;
  private String url;
  private List< String > checksums;
  private Boolean serverreq;
  private Boolean clientreq;


  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public List< String > getChecksums() {
    return ( checksums == null ) ? null : new ArrayList< String >( checksums );
  }

  public Boolean getServerreq() {
    return serverreq;
  }

  public Boolean getClientreq() {
    return clientreq;
  }


  public void setName( String name ) {
    this.name = name;
  }

  public void setUrl( String url ) {
    this.url = url;
  }

  public void setChecksums( List< String > checksums ) {
    this.checksums = ( checksums == null ) ? null : new ArrayList< String >( checksums );
  }

  public void setServerreq( Boolean serverreq ) {
    this.serverreq = serverreq;
  }

  public void setClientreq( Boolean clientreq ) {
    this.clientreq = clientreq;
  }


  @Override
  public boolean equals( Object obj ) {
    if ( obj == this ) return true;

    if ( obj instanceof LibraryData ) {

      LibraryData other = (LibraryData) obj;
      return ObjectUtils.equals( this.getName(),      other.getName() )
          && ObjectUtils.equals( this.getUrl(),       other.getUrl() )
          && ObjectUtils.equals( this.getChecksums(), other.getChecksums() )
          && ObjectUtils.equals( this.getServerreq(), other.getServerreq() )
          && ObjectUtils.equals( this.getClientreq(), other.getClientreq() );

    }
    return false;

  }

  @Override
  public int hashCode() {

    int result = 1;
    result = 31 * result + ( name       == null ? 0 : name.hashCode() );
    result = 31 * result + ( url        == null ? 0 : url.hashCode()  );
    result = 31 * result + ( checksums  == null ? 0 : checksums.hashCode()  );
    result = 31 * result + ( serverreq  == null ? 0 : serverreq.hashCode()  );
    result = 31 * result + ( clientreq  == null ? 0 : clientreq.hashCode()  );
    return result;

  }

}

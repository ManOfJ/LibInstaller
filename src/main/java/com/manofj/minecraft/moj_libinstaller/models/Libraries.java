package com.manofj.minecraft.moj_libinstaller.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import com.manofj.minecraft.moj_libinstaller.utils.ObjectUtils;


public final class Libraries {

  @SerializedName( "libraries" )
  private List< LibraryData > values;


  public List< LibraryData > getValues() {
    return values == null ? null : new ArrayList< LibraryData >( values );
  }

  public void setValues( List< LibraryData > values ) {
    this.values = values;
    if ( values != null )
      this.values = new ArrayList< LibraryData >( values );
  }


  @Override
  public boolean equals( Object obj ) {
    if ( obj == this ) return true;

    if ( obj instanceof Libraries ) {

      Libraries other = (Libraries) obj;
      return ObjectUtils.equals( this.getValues(), other.getValues() );

    }
    return false;

  }

  @Override
  public int hashCode() {

    int result = 1;
    result = 31 * result + ( getValues() == null ? 0 : getValues().hashCode() );
    return result;

  }

}

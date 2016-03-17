package com.manofj.minecraft.moj_libinstaller.utils;

public class ObjectUtils {
  private ObjectUtils() {}

  public static boolean equals( Object obj1, Object obj2 ) {
    return obj1 == null ? obj2 == null : obj1.equals( obj2 );
  }

}

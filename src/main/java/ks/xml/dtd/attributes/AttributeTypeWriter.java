// -*- mode: java; coding: utf-8-unix -*-

package ks.xml.dtd.attributes;

public interface AttributeTypeWriter {

  void writeString(String s);

  void writeToken(AttributeToken t);

  void writeEnumeration(Iterable<String> it);

  void writeNotations(Iterable<String> it);
}

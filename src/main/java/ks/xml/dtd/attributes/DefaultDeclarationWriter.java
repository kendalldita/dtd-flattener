// -*- mode: java; coding: utf-8-unix -*-

package ks.xml.dtd.attributes;

public interface DefaultDeclarationWriter {

  void writeRequired();

  void writeImplied();

  void writeValue(String v);
}

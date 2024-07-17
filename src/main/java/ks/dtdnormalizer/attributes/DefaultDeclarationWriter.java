// -*- mode: java; coding: utf-8-unix -*-

package ks.dtdnormalizer.attributes;

public interface DefaultDeclarationWriter {

  void writeRequired();

  void writeImplied();

  void writeValue(String v);
}

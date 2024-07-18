// -*- mode: java; coding: utf-8-unix -*-

package ks.xml.dtd.attributes;

import org.apache.xerces.xni.XNIException;

public interface AttributeDeclarationWriter {

  void writeAttributeDeclaration(AttributeDeclaration d)
    throws XNIException;
}

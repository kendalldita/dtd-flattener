package ks.dtdnormalizer.attributes;

import org.apache.xerces.xni.XNIException;

public interface AttributeDeclarationWriter {

  void writeAttributeDeclaration(AttributeDeclaration d)
    throws XNIException;

}

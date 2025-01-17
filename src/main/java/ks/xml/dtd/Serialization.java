// -*- mode: java; coding: utf-8-unix -*-

package ks.xml.dtd;

import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;

import org.apache.xerces.xni.*;

public interface Serialization {

  XMLLocator getLocator();

  void setLocator(XMLLocator loc);

  boolean isWithAbsolutePaths();

  void setWithAbsolutePaths(boolean f);

  Path getBasePath();

  void setBasePath(Path path);

  Writer getSerializationWriter();

  void setSerializationWriter(Writer w);

  void resetTargetResource(URI uri) throws Exception;

  void startDocument(String root) throws XNIException;

  void endDocument() throws XNIException;

  void xmlDeclaration(String version, String encoding, String standalone)
    throws XNIException;

  void doctypeDeclaration(String root, String publicId, String systemId)
    throws XNIException;

  void textDeclaration(String version, String encoding)
    throws XNIException;

  void comment(String fmt, Object... args)
    throws XNIException;

  void processingIntruction(String target, XMLString data)
    throws XNIException;

  void startConditionalSection(String condition)
    throws XNIException;

  void endConditionalSection();

  void startExternalSubset()
    throws XNIException;

  void endExternalSubset()
    throws XNIException;

  void internalEntityDeclaration(String name,
                                 XMLString text, XMLString rawText)
    throws XNIException;

  void externalEntityDeclaration(String name,
                                 String publicId, String systemId)
    throws XNIException;

  void startEntity(String name)
    throws XNIException;

  void endEntity()
    throws XNIException;

  void elementDeclaration(String name, String contentModel)
    throws XNIException;

  void startAttributeListDeclaration(String name)
    throws XNIException;

  void endAttributeListDeclaration()
    throws XNIException;

  void attributeDeclaration(String name, String type,
                            String[] enumeration, String defaultType,
                            XMLString defaultValue)
    throws XNIException;

  void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations)
    throws XNIException;

  void redefinition(String entityName) throws XNIException;

  void startElement(String name) throws XNIException;

  void endElement() throws XNIException;

  void emptyElement(String name) throws XNIException;

  void element(String name, String text) throws XNIException;

  void attribute(String name, String text) throws XNIException;

  void text(String text) throws XNIException;

  void stackTrace(Exception e) throws XNIException;

  void flush() throws XNIException;
}

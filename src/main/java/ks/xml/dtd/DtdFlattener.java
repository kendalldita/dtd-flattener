// -*- mode: java; coding: utf-8-unix -*-

package ks.xml.dtd;

import ks.xml.dtd.cli.HelpFactory;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import picocli.CommandLine;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static com.google.common.io.Resources.getResource;

public final class DtdFlattener {

  public static final String COMMENTS = "dtd.comments";

  public static final String SERIALIZATION = "dtd.serialization";

  public static final String DUPLICATES = "dtd.duplicates";

  public static final String VERBOSITY = "dtd.verbosity";

  public static final String BASE = "dtd.base";

  private File input;

  private File output;

  private File catalog;

  private boolean xmlFormat;

  private int verbosity = 0;

  private boolean withComments = false;

  private boolean absolute = false;

  public static void main(final String[] args) throws Exception {
    System.setProperty("picocli.ansi", "true");
    CommandLine.Help.ColorScheme colorScheme = new CommandLine.Help.ColorScheme.Builder()
        .commands    (CommandLine.Help.Ansi.Style.bold)
        .options     (CommandLine.Help.Ansi.Style.fg_green)
        .parameters  (CommandLine.Help.Ansi.Style.fg_green)
        .optionParams(CommandLine.Help.Ansi.Style.italic, CommandLine.Help.Ansi.Style.fg_cyan)
        .errors      (CommandLine.Help.Ansi.Style.fg_red, CommandLine.Help.Ansi.Style.bold)
        .stackTraces (CommandLine.Help.Ansi.Style.italic)
        .applySystemProperties() // optional: allow end users to customize
        .build();
    CommandLine cli = new CommandLine(new DtdFlattenerParameters(new DtdFlattener()));
    cli.setColorScheme(colorScheme);
    cli.setHelpFactory(new HelpFactory());
    System.exit(cli.execute(args));
  }

  public File getInput() {
    return input;
  }

  public void setInput(File input) {
    this.input = input;
  }

  public File getOutput() {
    return output;
  }

  public void setOutput(File output) {
    this.output = output;
  }

  public File getCatalog() {
    return catalog;
  }

  public void setCatalog(File catalog) {
    this.catalog = catalog;
  }

  public boolean isXmlFormat() {
    return xmlFormat;
  }

  public void setXmlFormat(boolean xmlFormat) {
    this.xmlFormat = xmlFormat;
  }

  public int getVerbosity() {
    return verbosity;
  }

  public void setVerbosity(int verbosity) {
    this.verbosity = verbosity;
  }

  public boolean isWithComments() {
    return withComments;
  }

  public void setWithComments(boolean withComments) {
    this.withComments = withComments;
  }

  public boolean isAbsolute() {
    return absolute;
  }

  public void setAbsolute(boolean absolute) {
    this.absolute = absolute;
  }

  public int go(DtdFlattenerParameters params) throws Exception {
    StringWriter sw = new StringWriter();
    Serialization out = isXmlFormat() ?
      new XmlSerialization(sw) : new DtdSerialization(sw, isWithComments());
    out.setWithAbsolutePaths(isAbsolute());
    out.setBasePath(getCatalog().toPath());
    final XniConfiguration configuration = getXniConfiguration(out);
    parseDocuments(configuration);
    String xml = sw.toString();
    if (isXmlFormat()) {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      StringReader srdr = new StringReader(sw.toString());
      InputSource ins = new InputSource(srdr);
      Document doc = db.parse(ins);
      NodeList nl = doc.getElementsByTagName("raw-content-model");
      for (int i = 0, e = nl.getLength(); i < e; ++i) {
        Element el = (Element) nl.item(i);
        Element parent = (Element) el.getParentNode();
        Element rel = (Element) el.getParentNode().getPreviousSibling();
        parent.appendChild(rel);
      }
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer trans = tf.newTransformer();
      DOMSource ds = new DOMSource(doc);
      File f = getOutput();
      StringWriter sww = new StringWriter();
      StreamResult sr = new StreamResult(sww);
      sr.setSystemId(f.getAbsoluteFile().toURI().toASCIIString());
      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      trans.transform(ds, sr);
      xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + sww.toString();
    }
    try (BufferedWriter bw = Files.newBufferedWriter(getOutput().toPath(),
                                                     StandardCharsets.UTF_8,
                                                     StandardOpenOption.CREATE,
                                                     StandardOpenOption.WRITE))
    {
      bw.write(xml);
    }
    return 0;
  }

  private XniConfiguration getXniConfiguration(Serialization out) throws Exception {
    final XniConfiguration configuration = new XniConfiguration();
    final DtdHandler tracer = new DtdHandler(out, configuration);
    final DocumentHandler dh = new DocumentHandler(out, configuration);
    configuration.setDocumentHandler(dh);
    configuration.setEntityResolver(new IdentifierResolver(getCatalog().getAbsolutePath()));
    configuration.setErrorHandler(new ErrorHandler(out));
    configuration.setDTDContentModelHandler(tracer);
    configuration.setDTDHandler(tracer);
    configuration.initialize();
    return configuration;
  }

  private void parseDocuments(final XniConfiguration cfg) throws Exception {
    Serialization s = ((DocumentHandler)cfg.getDocumentHandler()).getSerializer();
    s.resetTargetResource(getOutput().toURI());
    final XMLInputSource xis = new XMLInputSource(null, getInput().getAbsolutePath(), null);
    cfg.parse(xis);
  }
}

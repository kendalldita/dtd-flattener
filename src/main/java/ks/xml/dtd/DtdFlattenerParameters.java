package ks.xml.dtd;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "dtd-flattener", description = "Flattens DTDs for performance or analysis",
         version = "dtd-flattener 1.1",
         sortOptions = false,
         headerHeading = "Usage:%n",
         synopsisHeading = "%n",
         descriptionHeading = "%nDescription:%n%n",
         parameterListHeading = "%nParameters:%n",
         optionListHeading = "%nOptions:%n",
         mixinStandardHelpOptions = true)
public class DtdFlattenerParameters implements Callable<Integer> {

  private DtdFlattener flattener;

  @CommandLine.Parameters(index = "0", description = "Output file")
  File output;

  @CommandLine.Parameters(index = "1", description = "Input xml document")
  File input;

  @CommandLine.Parameters(index = "2", description = "XML catalog file")
  File catalog;

  @Option(names = {"--xml"}, description = "Create XML representation of DTD",
    required = false, defaultValue = "false")
  boolean xmlFormat = false;

  @Option(names = {"--comments"}, description = "Include location comments (default: false)",
          required = false, defaultValue = "false")
  boolean withComments = false;

  @Option(names = {"--absolute"}, description = "Use absolute file paths in comments (default: false)",
          required = false, defaultValue = "false")
  boolean absolute = false;

  @Option(names = {"-d", "--debug"}, description = "Logging level 0-9 (default: ${DEFAULT-VALUE})",
    required = false, defaultValue = "0")
  int level = 0;

  @Option(names = { "-h", "--help", "-?", "-help"}, description = "Display this help", usageHelp = true)
  private boolean help;

  @Option(names = { "-V", "--version", "-version"}, description = "Display version information", versionHelp = true)
  private boolean versionRequested;

  public DtdFlattenerParameters() { }

  public DtdFlattenerParameters(DtdFlattener flattener) {
    this.flattener = flattener;
  }

  @Override
  public Integer call() throws Exception {
    flattener.setInput(input.getAbsoluteFile());
    flattener.setOutput(output.getAbsoluteFile());
    flattener.setCatalog(catalog.getAbsoluteFile());
    flattener.setXmlFormat(xmlFormat);
    flattener.setVerbosity(level);
    flattener.setWithComments(withComments);
    flattener.setAbsolute(absolute);
    return flattener.go(this);
  }
}

package ks.xml.dtd.cli;

import picocli.CommandLine;

import java.lang.reflect.Field;
import java.util.List;

public class HelpFactory implements CommandLine.IHelpFactory {

  @Override
  public CommandLine.Help create(CommandLine.Model.CommandSpec commandSpec, CommandLine.Help.ColorScheme colorScheme) {
    return new CommandLine.Help(commandSpec, colorScheme) {
      @Override
      public String parameterList(List<CommandLine.Model.PositionalParamSpec> positionalParams) {
        int usageHelpWidth = commandSpec.usageMessage().width();
        int longOptionsColumnWidth = longOptionsColumnWidth(createDefaultLayout());
        int descriptionWidth = usageHelpWidth - 1 - longOptionsColumnWidth;
        TextTable tt = TextTable.forColumns(colorScheme,
          new Column(2, 0, Column.Overflow.TRUNCATE), // "*"
          new Column(0, 0, Column.Overflow.SPAN), // "-c"
          new Column(0, 0, Column.Overflow.TRUNCATE), // ","
          new Column(longOptionsColumnWidth, 0, Column.Overflow.SPAN),  // " --create"
          new Column(descriptionWidth, 4, Column.Overflow.WRAP)); // " Creates a ..."
        tt.setAdjustLineBreaksForWideCJKCharacters(commandSpec.usageMessage().adjustLineBreaksForWideCJKCharacters());
        Layout layout = new Layout(colorScheme, tt, createDefaultOptionRenderer(), createDefaultParameterRenderer());
        return parameterList(positionalParams, layout, parameterLabelRenderer());
      }
    };
  }

  int longOptionsColumnWidth(CommandLine.Help.Layout layout) { // bit of a hack...
    try {
      Field table = CommandLine.Help.Layout.class.getDeclaredField("table");
      table.setAccessible(true);
      CommandLine.Help.TextTable tt = (CommandLine.Help.TextTable) table.get(layout);
      return tt.columns()[3].width;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}

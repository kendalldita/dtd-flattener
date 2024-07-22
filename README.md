[//]: # "-*- mode: markdown; coding: utf-8-unix -*-"

# A program for flattening DTDs

## Overview

This program takes an XML catalog, and an XML document with a DOCTYPE
statement as input, and produces either a DTD as a single file, or an
XML document describing the DTD.

DTDs are "flattened" by recursively expanding all entity references.

## Usage

```
dtd-flattener [-hV] [--absolute] [--comments] [--xml] [-d=<level>] <output>
              <input> <catalog>

Description:

Flattens DTDs for performance or analysis

Parameters:
  <output>                  Output file
  <input>                   Input xml document
  <catalog>                 XML catalog file

Options:
      --xml                 Create XML representation of DTD
      --comments            Include location comments (default: false)
      --absolute            Use absolute file paths in comments (default: false)
  -d, --debug=<level>       Logging level 0-9 (default: 0)
  -h, -?, -help, --help     Display this help
  -V, -version, --version   Display version information
```

## Dependencies

JDK 1.8 or greater

## Not implemented

`NDATA` productions are not handled, e.g.:

```
<!NOTATION gif PUBLIC "-//CompuServe//NOTATION Graphics Interchange Format 89a//EN"
                      "image/gif">
<!ENTITY neko SYSTEM "cat.gif" NDATA gif>
```

The entire entity declaration is ignored if it references a declared notation.

## Formats

The DTD format produces a valid XML DTD.

The XML format describes elements and their content models, with
location information describing which entities definitions were
originally found in.

## XML format

A schema for the XML format can be found in [dtd.rnc](etc/dtd.rnc).

The XML document can be used to parse DTDs programmatically.

### XML representation of content models

Element declarations are represented by a copy of the text from the DTD, followed by an element detailing the content model, e.g.:

```
<element-declaration name="simpletable">
  <location href="../plugins/org.oasis-open.dita.v1_3/dtd/base/dtd/commonElements.mod" line="1558"/>
  <raw-content-model>((sthead)?,(strow)+)</raw-content-model>
  <content-model element="simpletable">
    <group>
      <group>
        <element name="sthead"/>
      </group>
      <occur type="?"/>
      <sep type=","/>
      <group>
        <element name="strow"/>
      </group>
      <occur type="+"/>
    </group>
  </content-model>
</element-declaration>
```

## Building

To build dtd-flattener.jar:

```
./gradlew clean build
ls build/libs/dtd-flattener.jar
```
## Author

Kendall Shaw <kshaw@kendallshaw.com>

This project moved from https://github.com/queshaw/dtd-normalizer

## Copyright

This program and the accompanying materials are made available under
the terms of the Eclipse Public License v. 2.0 which is available at
https://www.eclipse.org/legal/epl-2.0/.

SPDX-License-Identifier: EPL-2.0

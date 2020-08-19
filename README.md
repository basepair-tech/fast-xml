# FastXml

![CI](https://github.com/basepair-tech/fast-xml/workflows/CI/badge.svg?branch=master)

FastXml is a wrapper around the [VDT-XML](http://vtd-xml.sourceforge.net/) library to provide a nicer interface for parsing XML files.


## Installation

FastXml is available from JCenter as well as Github Packages 
```
repositories {
  jcenter()
}

dependencies {
  implementation("tech.basepair:fast-xml:0.1.0")
}
```

## Parsing an XML File

    val xml = """<document>
          |  <a>Hello</a>
          |  <b>Goodbye</b>
          |  <c>Welcome</c>
          |</document>
        """;
    val doc: Document? = FastXmlParser.parse(xml);

A XML document can be parsed from a String representing XML, a File, or an InputStream and will return a Document.

## Using the Document

The Document offers a way to select nodes and elements from an XML document.

    val doc = FastXmlParser.parse(xml)!!;
    doc.selectNode("/a");

If there is namespaces you can add the namespaces to the Document in order to be able to refer to them in the xpath queries.

    val doc = FastXmlParser.parser("ns" to "http://basepair.tech").parse(xml)!!;
    orders: NodeList = doc.selectNodes("/orders/ns:order");

If a node does not exist when an xpath is used it will return null.
If you expect a node to exist you can use the _selectRequiredNode_ method and if it's not found it will throw an _InvalidDateException_

    val doc = FastXmlParser.parse(xml)!!;
    doc.selectRequiredNode("/a/b");

## Selecting values from nodes

Values can be selected from a Document or a node as the following types using the _selectAs*_ methods:

  - Int
  - Double
  - Boolean
  - BigInteger
  - BigDecimal

        val doc = FastXmlParser.parse(xml)!!;
        value: Int? = doc.selectAsInt('/a/b');

## Selecting Node attributes

Attributes on nodes can be selected using the _getAttr_ or _getRequiredAttr_ methods on the Node object or using an xpath.

    val doc = FastXmlParser.parse(xml)!!;
    doc.selectNode("/a").getAttr("href");

or

    val href: Node = doc.selectRequiredNode("/a/@href")!!;
    String attrName = href.getValue();
    String url = href.getText();

## Publishing artifacts

The fast-xml artifacts are published to both jcenter and github packages.
To publish them both the following commands need to be run:
```
./gradlew publish
./gradlew bintrayUpload
```
or run the task:
```
./gradlew publishAndBintrayUpload
```

## Release

This is using the Gradle release plugin https://github.com/researchgate/gradle-release

The release is configured to publish the artifacts to both Bintray and Github and depends on the `publishAndBintrayUpload` task.

```
./gradlew release
```

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

### Using with a Java Project

You will need to add the Kotlin runtime as a dependency to your project:

```
implementation 'org.jetbrains.kotlin:kotlin-stdlib:1.4.0'
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
If you expect a node to exist you can use the `selectRequiredNode` method and if it's not found it will throw an `InvalidDateException`

    val doc = FastXmlParser.parse(xml)!!;
    doc.selectRequiredNode("/a/b");

## Selecting a list of nodes

If you want to get all the nodes that match an xpath you can use the `selectNodes()` to get a list of nodes that match the xpath query.

    val doc = FastXmlParser.parse(xml)!!;
    doc.selectNodes("/a/b")

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

Attributes on nodes can be selected using the `getAttr` or `getRequiredAttr` methods on the Node object or using an xpath.

    val doc = FastXmlParser.parse(xml)!!;
    doc.selectNode("/a").getAttr("href");

or

    val href: Node = doc.selectRequiredNode("/a/@href")!!;
    String attrName = href.getValue();
    String url = href.getText();

## Publishing artifacts

The fast-xml artifact is published to jcenter.
To publish the artifact just run:
```
./gradlew bintrayUpload
```

## Release

This is using the Gradle release plugin https://github.com/researchgate/gradle-release

The release is configured to publish the artifacts to Bintray and depends on the `bintrayUpload` task.

```
./gradlew release
```

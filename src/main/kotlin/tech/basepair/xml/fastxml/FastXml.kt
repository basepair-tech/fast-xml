/*
 * Copyright 2020 Base Pair Pty Ltd
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package tech.basepair.xml.fastxml

import com.ximpleware.ParseException
import com.ximpleware.VTDGen
import mu.KotlinLogging
import org.jetbrains.annotations.Nullable
import tech.basepair.xml.fastxml.api.Document
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

private val LOG = KotlinLogging.logger {}

/**
 * FastXml is a XML Parser that encapsulates the VTD-XML parser and provides an
 * Document interface to performing xpath queries.
 * All xpath queries and parsing is performed by the underlying VTD-XML parser.
 *
 * For more details on the VTD-XML parser see here: https://vtd-xml.sourceforge.io/
 */
sealed class FastXml {

  /**
   * Represents the XMLParser that underlyingly will use VTD-XML to parse the XML
   * But instead of using the VTDNav directly it is wrapped in a FastXML Document
   * that provides an interface to the document to perform XPath queries on
   */
  interface XmlParser {

    /**
     * Parse a string of XML and return a Document if there is no error parsing
     * @return null if the xml fails to parse
     */
    fun parse(xml: String): Document?

    /**
     * Parse a File and return a Document if there is no error parsing
     * @return null if the xml fails to parse
     */
    fun parse(xmlFile: File): Document?

    /**
     * Parse an InputStream of XML and return a Document if there is no error parsing
     * @return null if the xml fails to parse
     */
    fun parse(inputStream: InputStream): Document?

  }

  companion object {
    /**
     * Creates the XmlParser.
     * @param namespaces a list of name spaces pairs that enable the parser in namespace aware mode
     *        If no namespaces are provied then the namespace aware parameter will be disabled for
     *        underlying VTDGen parser
     *
     * @see VTDGen
     */
    @JvmStatic
    fun parser(vararg namespaces: Pair<String, String>): XmlParser = FastXmlParser(*namespaces)

    /**
     * Parse a string of XML and return a Document if there is no error parsing
     * @return null if the xml fails to parse
     */
    @JvmStatic
    fun parse(xml: String) = FastXmlParser().parse(xml)

    /**
     * Parse an InputStream of XML and return a Document if there is no error parsing
     * @return null if the xml fails to parse
     */
    @JvmStatic
    fun parse(xmlFile: File) = FastXmlParser().parse(xmlFile)

    @JvmStatic
    fun parse(inputStream: InputStream) = FastXmlParser().parse(inputStream)
  }

  private class FastXmlParser(vararg _namespaces: Pair<String,String>) : XmlParser {
    private val namespaces: List<Pair<String, String>> = listOf(*_namespaces)

    override fun parse(xml: String): Document? {
      return parseBytes(xml.toByteArray(Charset.defaultCharset()))
    }

    override fun parse(xmlFile: File): Document? {
      return parse(xmlFile.inputStream())
    }

    override fun parse(inputStream: InputStream): Document? {
      return inputStream.use { input -> parseBytes(input.readBytes())}
    }

    private fun parseBytes(bytes: ByteArray): Document? = getDocument(bytes)

    private fun getDocument(bytes: ByteArray): Document? {
      val vg = VTDGen()
      vg.setDoc(bytes)
      try {
        vg.parse(namespaces.isNotEmpty())
      } catch (e: ParseException) {
        LOG.error(e) { "Unable to parse document" }
        return null
      }

      return DocumentImpl(vg.nav, namespaces)
    }
  }

}

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

import com.ximpleware.AutoPilot
import com.ximpleware.NavException
import com.ximpleware.VTDNav
import mu.KotlinLogging
import org.jetbrains.annotations.TestOnly
import tech.basepair.xml.fastxml.api.Document
import tech.basepair.xml.fastxml.api.Node
import tech.basepair.xml.fastxml.api.NodeList
import tech.basepair.xml.fastxml.exception.DocumentException
import tech.basepair.xml.fastxml.exception.InvalidDataException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigDecimal
import java.math.BigInteger

private val LOG = KotlinLogging.logger {}

internal fun <T> checkRequired(value: T?, message: String): T {
  if (value === null) {
    throw InvalidDataException(message)
  }
  return value
}

internal class DocumentImpl : Document {

  private val nav: VTDNav
  private val docStart: NodeImpl
  private val rootNode: NodeImpl
  private val autoPilot: AutoPilot

  internal constructor(_nav: VTDNav, namespaces: List<Pair<String, String>>) {
    nav = _nav
    docStart = NodeImpl(0, this)
    rootNode = NodeImpl(nav.rootIndex, this)
    autoPilot = getAutoPilot(namespaces)
  }

  override fun getRoot(): Node = rootNode

  override fun selectNode(xpath: String): Node? {
    return selectNode(docStart, xpath)
  }

  override fun selectRequiredNode(xpath: String): Node {
    return selectRequiredNode(docStart, xpath)
  }

  override fun selectNodes(xpath: String): NodeList {
    return NodeListImpl(selectNodes(docStart, xpath))
  }

  override fun selectText(xpath: String): String? {
    return selectText(docStart, xpath)
  }

  override fun selectRequiredText(xpath: String): String {
    return selectRequiredText(docStart, xpath)
  }

  override fun selectAsInt(xpath: String): Int? {
    return selectAsInt(docStart, xpath)
  }

  override fun selectAsRequiredInt(xpath: String): Int {
    return selectAsRequiredInt(docStart, xpath)
  }

  override fun selectAsDouble(xpath: String): Double? {
    return selectAsDouble(docStart, xpath)
  }

  override fun selectAsRequiredDouble(xpath: String): Double {
    return selectAsRequiredDouble(docStart, xpath)
  }

  override fun selectAsBoolean(xpath: String): Boolean {
    return selectAsBoolean(docStart, xpath)
  }

  override fun selectAsBigInteger(xpath: String): BigInteger? {
    return selectAsBigInteger(docStart, xpath)
  }

  override fun selectAsRequiredBigInteger(xpath: String): BigInteger {
    return selectAsRequiredBigInteger(docStart, xpath)
  }

  override fun selectAsBigDecimal(xpath: String): BigDecimal? {
    return selectAsBigDecimal(docStart, xpath)
  }

  override fun selectAsRequiredBigDecimal(xpath: String): BigDecimal {
    return selectAsRequiredBigDecimal(docStart, xpath)
  }

  /**
   * Formats the Document using default indentation level 2
   */
  override fun format(): String {
    return format(2)
  }

  /**
   * Formats the Document using the specified indentation level
   */
  override fun format(indent: Int): String {
    val sb = StringBuilder()
    formatNode(rootNode, 0, indent, sb)
    return sb.toString()
  }

  override fun toString(): String {
    val stream = ByteArrayOutputStream()
    return try {
      nav.dumpXML(stream)
      stream.toString()
    } catch (e: IOException) {
      LOG.error(e) { "Error converting Document to xml" }
      ""
    }
  }

  private fun push() {
    nav.push()
  }

  private fun pop() {
    nav.pop()
  }

  private fun getAutoPilot(namespaces: List<Pair<String, String>>): AutoPilot {
    val ap = AutoPilot(nav)

    for (namespace in namespaces) {
      ap.declareXPathNameSpace(namespace.first, namespace.second)
    }

    return ap
  }

  fun selectNode(node: NodeImpl, xpath: String): NodeImpl? {
    push()
    try {
      toNode(node)
      autoPilot.selectXPath(xpath)
      val result = autoPilot.evalXPath()
      return if (result == -1) {
        null
      } else {
        NodeImpl(result, this)
      }
    } finally {
      pop()
    }
  }

  fun selectRequiredNode(node: NodeImpl, xpath: String): NodeImpl {
    return checkRequired(selectNode(node, xpath), "Required node was not found with xpath: $xpath")
  }

  fun selectNodes(node: NodeImpl, xpath: String): List<NodeImpl> {
    push()
    val nodes: MutableList<NodeImpl> = ArrayList()
    return try {
      toNode(node)
      autoPilot.selectXPath(xpath)
      var result: Int = autoPilot.evalXPath()
      while (result != -1) {
        nodes.add(NodeImpl(result, this))
        result = autoPilot.evalXPath()
      }
      nodes
    } catch(e: Exception) {
      LOG.error(e) { "Error selecting nodes for xpath: $xpath" }
      emptyList()
    } finally {
      pop()
    }
  }

  fun selectNamespaceUri(node: NodeImpl): String {
    push()

    try {
      toNode(node)
      autoPilot.selectXPath("namespace-uri(.)")
      return autoPilot.evalXPathToString()
    } finally {
      pop()
    }
  }

  fun selectText(node: NodeImpl, xpath: String): String? {
    push()

    try {
      toNode(node)
      autoPilot.selectXPath(xpath)
      val result: Int = autoPilot.evalXPath()
      return if (result == -1) {
        null
      } else {
        when (nav.getTokenType(result)) {
          VTDNav.TOKEN_ATTR_NAME -> getValueAtIndex(result + 1)
          else -> getValueAtIndex(nav.text)
        }
      }
    } catch(e: Exception) {
      LOG.error(e) {"Error selecting text for xpath: $xpath" }
      return null
    } finally {
      pop()
    }
  }

  fun selectRequiredText(node: NodeImpl, xpath: String): String {
    return checkRequired(selectText(node, xpath), "Invalid data for node: $node, at xpath: $xpath")
  }

  fun selectAsInt(node: NodeImpl, xpath: String): Int? {
    return selectText(node, xpath)?.toIntOrNull()
  }

  fun selectAsRequiredInt(node: NodeImpl, xpath: String): Int {
    return checkRequired(selectAsInt(node, xpath), "Invalid data for node: $node, at xpath: $xpath")
  }

  fun selectAsDouble(node: NodeImpl, xpath: String): Double? {
    return selectText(node, xpath)?.toDoubleOrNull()
  }

  fun selectAsRequiredDouble(node: NodeImpl, xpath: String): Double {
    return checkRequired(selectAsDouble(node, xpath), "Invalid data for node: $node, at xpath: $xpath")
  }

  fun selectAsBoolean(node: NodeImpl, xpath: String): Boolean {
    return selectText(node, xpath)?.toBoolean() ?: false
  }

  fun selectAsBigInteger(node: NodeImpl, xpath: String): BigInteger? {
    return selectText(node, xpath)?.toBigIntegerOrNull()
  }

  fun selectAsRequiredBigInteger(node: NodeImpl, xpath: String): BigInteger {
    return checkRequired(selectAsBigInteger(node, xpath), "Invalid data for node: $node, at xpath: $xpath")
  }

  fun selectAsBigDecimal(node: NodeImpl, xpath: String): BigDecimal? {
    return selectText(node, xpath)?.toBigDecimalOrNull()
  }

  fun selectAsRequiredBigDecimal(node: NodeImpl, xpath: String): BigDecimal {
    return checkRequired(selectAsBigDecimal(node, xpath), "Invalid data for node: $node, at xpath: $xpath")
  }

  fun getText(node: NodeImpl): String? {
    push()
    return try {
      toNode(node)
      when(nav.getTokenType(node.getIndex())) {
        VTDNav.TOKEN_ATTR_NAME -> getValueAtIndex(node.getIndex() + 1)
        else -> getValueAtIndex(nav.text)
      }
    } catch (e: DocumentException) {
      LOG.error(e) { "Unable to get text from node: $node" }
      null
    } finally {
      pop()
    }
  }

  /**
   * Gets the text of the node also including the CDATA tag if it is present
   */
  private fun getElementText(node: NodeImpl): String? {
    push()
    return try {
      toNode(node)
      val sb = StringBuilder()
      val depth = nav.currentDepth
      var i = nav.currentIndex
      while (nav.getTokenType(i) == VTDNav.TOKEN_STARTING_TAG) {
        i++
      }
      while (nav.getTokenDepth(i) >= depth
          && !(nav.getTokenType(i) == VTDNav.TOKEN_STARTING_TAG && nav.getTokenDepth(i) == depth)
          && i < nav.tokenCount) {
        if (nav.getTokenType(i) == VTDNav.TOKEN_CHARACTER_DATA || nav.getTokenType(i) == VTDNav.TOKEN_CDATA_VAL) {
          if (nav.getTokenType(i) == VTDNav.TOKEN_CDATA_VAL) {
            sb.append("<[CDATA[")
            sb.append(nav.toString(i))
            sb.append("]]>")
          } else {
            sb.append(nav.toString(i))
          }
        }
        i++
      }
      sb.toString()
    } finally {
      pop()
    }

  }

  fun getValue(node: NodeImpl): String {
    push()
    return try {
      toNode(node)
      getValueAtIndex(node.getIndex())!!
    } finally {
      pop()
    }
  }

  fun hasAttr(node: NodeImpl, name: String): Boolean {
    push()
    return try {
      toNode(node)
      hasAttrValue(name)
    } catch (e: DocumentException) {
      LOG.error(e) { "Unable to check attribute: $name" }
      false
    } finally {
      pop()
    }
  }

  fun getAttr(node: NodeImpl, name: String): String? {
    push()
    return try {
      toNode(node)
      getAttrValue(name)
    } catch (e: Exception) {
      LOG.error(e) { "Unable to attribute: $name" }
      null
    } finally {
      pop()
    }
  }

  fun getAttrs(node: NodeImpl): Map<String, String> {
    push()
    val attributes = mutableMapOf<String, String>()
    try {
      toNode(node)
      autoPilot.selectAttr("*")
      var result = autoPilot.iterateAttr()
      while(result != -1) {
        val attrName = getValueAtIndex(result)!!
        val attrValue = getValueAtIndex(result + 1)!!
        attributes[attrName] = attrValue
        result = autoPilot.iterateAttr()
      }
    } catch (e: Exception) {
      LOG.error(e) { "Error getting attributes" }
    } finally {
      pop()
    }

    return attributes
  }

  fun getRequiredAttr(node: NodeImpl, name: String): String {
    push()
    try {
      toNode(node)
      return checkRequired(getAttrValue(name), "Attribute $name not available")
    } catch (e: DocumentException) {
      throw InvalidDataException("Unable to navigate to get attribute", e)
    } finally {
      pop()
    }
  }

  private fun toNode(node: NodeImpl) {
    try {
      nav.recoverNode(node.getIndex())
    } catch (e: NavException) {
      throw DocumentException("Unable to locate node.", e)
    }
  }

  private fun getValueAtIndex(index: Int): String? {
    if (index == -1) {
      return null
    }
    try {
      return nav.toString(index)
    } catch (e: NavException) {
      throw DocumentException("Unable to get value from document.", e)
    }
  }

  private fun hasAttrValue(name: String): Boolean {
    try {
      return nav.hasAttr(name)
    } catch(e: NavException) {
      throw DocumentException("Unable to check hasAttr for $name", e)
    }
  }

  private fun getAttrValue(name: String): String? {
    try {
      val result: Int = nav.getAttrVal(name)
      return if (result != -1) {
        getValueAtIndex(result)
      } else {
        null
      }
    } catch (e: NavException) {
      throw DocumentException("Unable to get attribute: $name", e)
    }
  }

  fun getXml(node: NodeImpl): String {
    push()
    try {
      toNode(node)
      val elementFragment = nav.elementFragment
      val os1 = elementFragment.toInt()
      val len = (elementFragment shr 32).toInt()
      val stream = ByteArrayOutputStream()
      stream.write(nav.xml.bytes, os1, len)
      return stream.toString()
    } catch (e: NavException) {
      throw DocumentException("Error getting XML.", e)
    } finally {
      pop()
    }
  }

  private fun formatNode(node: NodeImpl, indent: Int, indentInc: Int, sb: StringBuilder) {
    val tagName = node.getValue()
    val attrs = node.getAttrs()
    val children = selectNodes(node,"./*")
    val indentStr = " ".repeat(indent)

    sb.append(indentStr).append("<").append(tagName)

    if (attrs.isNotEmpty()) {
      for ((k, v) in attrs) {
        sb.append(" ").append(k).append("=\"").append(v).append("\"")
      }
    }

    if (children.isEmpty()) {
      val text = getElementText(node)
      if (text != null) {
        sb.append(">").append(text).append("</").append(tagName).append(">")
      } else {
        sb.append(" />")
      }
    } else {
      sb.append(">\n")
      for (child in children) {
        formatNode(child, indent + indentInc, indentInc, sb)
      }
      sb.append(indentStr).append("</").append(tagName).append(">")
    }

    // Add a newline unless the end of the Document
    if (indent != 0) {
      sb.append("\n")
    }

  }

}

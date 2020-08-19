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

import tech.basepair.xml.fastxml.api.Node
import tech.basepair.xml.fastxml.api.NodeList
import tech.basepair.xml.fastxml.exception.InvalidDataException
import java.math.BigDecimal
import java.math.BigInteger

internal class NodeImpl(_index: Int, _doc: DocumentImpl) : Node {
  private val index: Int = _index
  private val doc: DocumentImpl = _doc

  fun getIndex() = index

  override fun getNamespaceUri(): String {
    return doc.selectNamespaceUri(this)
  }

  override fun getNsPrefix(): String? {
    val value = doc.getValue(this)
    val index = value.indexOf(':')
    return if (index == -1) null else value.substring(0, index)
  }

  override fun getAttr(name: String): String? {
    return doc.getAttr(this, name)
  }

  override fun getRequiredAttr(name: String): String {
    return doc.getRequiredAttr(this, name)
  }

  override fun getAttrAsInt(name: String): Int? {
    return doc.getAttr(this, name)?.toIntOrNull()
  }

  override fun getAttrAsRequiredInt(name: String): Int {
    return doc.getRequiredAttr(this, name).toInt()
  }

  override fun getAttrAsDouble(name: String): Double? {
    return doc.getAttr(this, name)?.toDoubleOrNull()
  }

  override fun getAttrAsRequiredDouble(name: String): Double {
    return doc.getRequiredAttr(this, name).toDouble()
  }

  override fun getAttrAsBoolean(name: String): Boolean {
    return doc.getAttr(this, name)?.toBoolean() ?: false
  }

  override fun getAttrAsRequiredBoolean(name: String): Boolean {
    return doc.getRequiredAttr(this, name).toBoolean()
  }

  override fun getAttrAsBigInteger(name: String): BigInteger? {
    return doc.getAttr(this, name)?.toBigIntegerOrNull()
  }

  override fun getAttrAsRequiredBigInteger(name: String): BigInteger {
    return doc.getRequiredAttr(this, name).toBigInteger()
  }

  override fun getAttrAsBigDecimal(name: String): BigDecimal? {
    return doc.getAttr(this, name)?.toBigDecimalOrNull()
  }

  override fun getAttrAsRequiredBigDecimal(name: String): BigDecimal {
    return doc.getRequiredAttr(this, name).toBigDecimal()
  }

  override fun hasAttr(name: String): Boolean {
    return doc.hasAttr(this, name)
  }

  override fun getAttrs(): Map<String, String> {
    return doc.getAttrs(this)
  }

  override fun getValue(): String? {
    return getValue(false)
  }

  override fun getValue(withoutPrefix: Boolean): String {
    val value = doc.getValue(this)
    return if (withoutPrefix) value.substringAfter(':') else value
  }

  override fun getText(): String? {
    return doc.getText(this)
  }

  override fun getRequiredText(): String {
    return checkRequired(doc.getText(this), "Unable to get text")
  }

  override fun getTextAsInt(): Int? {
    return getText()?.toIntOrNull()
  }

  override fun getTextAsRequiredInt(): Int {
    return handleException { getRequiredText().toInt() }
  }

  override fun getTextAsDouble(): Double? {
    return getText()?.toDoubleOrNull()
  }

  override fun getTextAsRequiredDouble(): Double {
    return handleException { getRequiredText().toDouble() }
  }

  override fun getTextAsBoolean(): Boolean {
    return getText()?.toBoolean() ?: false
  }

  override fun getTextAsRequiredBoolean(): Boolean {
    return getRequiredText().toBoolean()
  }

  override fun getTextAsBigInteger(): BigInteger? {
    return getText()?.toBigIntegerOrNull()
  }

  override fun getTextAsRequiredBigInteger(): BigInteger {
    return handleException { getRequiredText().toBigInteger() }
  }

  override fun getTextAsBigDecimal(): BigDecimal? {
    return getText()?.toBigDecimalOrNull()
  }

  override fun getTextAsRequiredBigDecimal(): BigDecimal {
    return handleException { getRequiredText().toBigDecimal() }
  }

  override fun selectNode(xpath: String): Node? {
    return doc.selectNode(this, xpath)
  }

  override fun selectRequiredNode(xpath: String): Node {
    return doc.selectRequiredNode(this, xpath)
  }

  override fun selectNodes(xpath: String): NodeList {
    return NodeListImpl(doc.selectNodes(this, xpath))
  }

  override fun selectText(xpath: String): String? {
    return doc.selectText(this, xpath)
  }

  override fun selectRequiredText(xpath: String): String {
    return doc.selectRequiredText(this, xpath)
  }

  override fun selectAsInt(xpath: String): Int? {
    return doc.selectAsInt(this, xpath)
  }

  override fun selectAsRequiredInt(xpath: String): Int {
    return doc.selectAsRequiredInt(this, xpath)
  }

  override fun selectAsDouble(xpath: String): Double? {
    return doc.selectAsDouble(this, xpath)
  }

  override fun selectAsRequiredDouble(xpath: String): Double {
    return doc.selectAsRequiredDouble(this, xpath)
  }

  override fun selectAsBoolean(xpath: String): Boolean {
    return doc.selectAsBoolean(this, xpath)
  }

  override fun selectAsBigInteger(xpath: String): BigInteger? {
    return doc.selectAsBigInteger(this, xpath)
  }

  override fun selectAsRequiredBigInteger(xpath: String): BigInteger {
    return doc.selectAsRequiredBigInteger(this, xpath)
  }

  override fun selectAsBigDecimal(xpath: String): BigDecimal? {
    return doc.selectAsBigDecimal(this, xpath)
  }

  override fun selectAsRequiredBigDecimal(xpath: String): BigDecimal {
    return doc.selectAsRequiredBigDecimal(this, xpath)
  }

  override fun toString(): String {
    return doc.getXml(this)
  }

  private fun <T> handleException(supplier: () -> T): T {
    return try {
      supplier()
    } catch (e: Exception) {
      throw InvalidDataException("Error get value", e)
    }
  }

}

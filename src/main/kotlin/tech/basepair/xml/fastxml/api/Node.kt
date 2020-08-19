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

package tech.basepair.xml.fastxml.api

import java.math.BigDecimal
import java.math.BigInteger

/**
 * Represents the Node of an XML Document.
 * This is the result of any XPath query executed on the [Document] or on
 * a [Node]
 * This allows extracting the data of the Node such as tag name, text or attributes as well
 * as querying for additional nodes using xpath,
 * @see XPathQueryable
 */
interface Node : XPathQueryable {

  /**
   * Gets the namespace of the current node.
   * Will return an empty string if there is none.
   * Can return an empty String if no namespace Uri is defined on the node
   */
  fun getNamespaceUri(): String

  /**
   * Returns the Namespace prefix if there is one defined.
   */
  fun getNsPrefix(): String?

  /**
   * Gets the provided attributes value if present
   */
  fun getAttr(name: String): String?

  /**
   * Gets the provided named attribute value.
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getRequiredAttr(name: String): String

  /**
   * Gets the provided named attribute value as an Int if present
   * @return null if no attribute or is not convertible to an Int
   */
  fun getAttrAsInt(name: String): Int?

  /**
   * Gets the provided named attribute value as an Int
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getAttrAsRequiredInt(name: String): Int

  /**
   * Gets the provided named attribute value as a Double if present
   * @return null if no attribute or is not convertible to a Double
   */
  fun getAttrAsDouble(name: String): Double?

  /**
   * Gets the provied named attribute value as a Double
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getAttrAsRequiredDouble(name: String): Double

  /**
   * Gets the provided named attribute value as a Boolean if present
   * @return false if no attribute is present or value is not convertible to a Boolean
   */
  fun getAttrAsBoolean(name: String): Boolean

  /**
   * Gets the provided named attribute value as a Boolean
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getAttrAsRequiredBoolean(name: String): Boolean

  /**
   * Gets the provided named attribute value as a BigInteger if present
   * @return null if no attribute or is not convertible to a BigInteger
   */
  fun getAttrAsBigInteger(name: String): BigInteger?

  /**
   * Gets the provided named attribute value as a BigInteger
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getAttrAsRequiredBigInteger(name: String): BigInteger

  /**
   * Gets the provided named attribute value as a BigDecimal if present
   * @return null if no attribute or is not convertible to a BigDecimal
   */
  fun getAttrAsBigDecimal(name: String): BigDecimal?

  /**
   * Gets the provided named attribute value as a BigDecimal
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getAttrAsRequiredBigDecimal(name: String): BigDecimal

  /**
   * Checks if the provided named attribute exists
   */
  fun hasAttr(name: String): Boolean

  /**
   * Gets the nodes attributes
   * @return An empty Map if no attributes are present
   */
  fun getAttrs(): Map<String, String>

  /**
   * Gets the value of the current nodes tag.
   * @param withoutPrefix controls whether the namespace prefix is stripped from the returned value
   */
  fun getValue(withoutPrefix: Boolean): String

  /**
   * Gets the value of the current nodes tag with prefix by default
   * @see [getValue]
   */
  fun getValue(): String?

  /**
   * Gets the text value of the current node if present
   * @return null if no text is present
   */
  fun getText(): String?

  /**
   * Gets the text value of the current node
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getRequiredText(): String

  /**
   * Gets the text value of the current node as an Int if present
   * @return null if no text is present or the text cannot be converted to an Int
   */
  fun getTextAsInt(): Int?

  /**
   * Gets the text value of the current node as an Int
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getTextAsRequiredInt(): Int

  /**
   * Gets the text value of the current node as a Double if present
   * @return null if no text is present or the text cannot be converted to a Double
   */
  fun getTextAsDouble(): Double?

  /**
   * Gets the text value of the current node as a Double
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getTextAsRequiredDouble(): Double

  /**
   * Gets the text value of the current node as a Boolean
   * @return false if no text is present or the text cannot be converted to a Boolean
   */
  fun getTextAsBoolean(): Boolean

  /**
   * Gets the text value of the current node as a Boolean
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getTextAsRequiredBoolean(): Boolean

  /**
   * Gets the text value of the current node as a BigInteger if present
   * @return null if no text is present or the text cannot be converted to a BigInteger
   */
  fun getTextAsBigInteger(): BigInteger?

  /**
   * Gets the text value of the current node as a BigInteger
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getTextAsRequiredBigInteger(): BigInteger

  /**
   * Gets the text value of the current node as a BigDecimal if present
   * @return null if no text is present or the text cannot be converted to a BigDecimal
   */
  fun getTextAsBigDecimal(): BigDecimal?

  /**
   * Gets the text value of the current node as a BigDecimal
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun getTextAsRequiredBigDecimal(): BigDecimal

}

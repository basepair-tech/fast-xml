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
 * Represents the objects that an XPath can be executed against
 * @see Document
 * @see Node
 */
interface XPathQueryable {

  /**
   * Execute the xpath and retrieve the Node at the path if present
   * @return null if no node is found at the xpath
   */
  fun selectNode(xpath: String): Node?

  /**
   * Execute the xpath and retrieve the Node at the path
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun selectRequiredNode(xpath: String): Node

  /**
   * Execute the xpath and retrieve a NodeList of the Nodes that match
   * @return An empty NodeList if no nodes match the xpath
   */
  fun selectNodes(xpath: String): NodeList

  /**
   * Executes the xpath and returns the text value of the Node at the path if present
   * @return null if there is no text at the path or no node matches
   */
  fun selectText(xpath: String): String?

  /**
   * Executes the xpath and retrieves the text value of the Node
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun selectRequiredText(xpath: String): String

  /**
   * Executes the xpath and returns an Int value of the text of the Node if present
   * @return null if no node matches or the value cannot be parsed to an Int
   */
  fun selectAsInt(xpath: String): Int?

  /**
   * Executes the xpath and returns an Int value of the text of the Node
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun selectAsRequiredInt(xpath: String): Int

  /**
   * Executes the xpath and returns a Double value of the text of the Node if present
   * @return null if no node matches or the value cannot be parsed to a Double
   */
  fun selectAsDouble(xpath: String): Double?

  /**
   * Executes the xpath and returns a Double value of the text of the Node
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun selectAsRequiredDouble(xpath: String): Double

  /**
   * Executes the xpath and returns a Boolean value of the text of the Node if present
   * @return false if no node matches or the value cannot be parsed to a Boolean
   */
  fun selectAsBoolean(xpath: String): Boolean

  /**
   * Executes the xpath and returns a BigInteger value of the text of the Node if present
   * @return null if no node matches or the value cannot be parsed to a BigInteger
   */
  fun selectAsBigInteger(xpath: String): BigInteger?

  /**
   * Executes the xpath and returns a BigInteger value of the text of the Node
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun selectAsRequiredBigInteger(xpath: String): BigInteger

  /**
   * Executes the xpath and returns a BigDecimal value of the text of the Node if present
   * @return null if no node matches or the value cannot be parsed to a BigDecimal
   */
  fun selectAsBigDecimal(xpath: String): BigDecimal?

  /**
   * Executes the xpath and returns a BigDecimal value of the text of the Node
   * @throws [tech.basepair.xml.fastxml.exception.InvalidDataException]
   */
  fun selectAsRequiredBigDecimal(xpath: String): BigDecimal

}

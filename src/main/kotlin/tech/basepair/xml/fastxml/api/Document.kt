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

/**
 * Represents an XML Document
 */
interface Document: XPathQueryable {

  /**
   * Gets the Root Node of the XML Document
   *
   *    <?xml version="1.0" encoding="UTF-8"?>
   *    <root>
   *      <node>value</node>
   *    </root>
   *
   * getRoot of the above Document would return the <root> Node
   *
   */
  fun getRoot(): Node

  /**
   * Formats the XML Document with a default indentation of 2 spaces
   */
  fun format(): String

  /**
   * Formats the XML Document with the provided indentation
   */
  fun format(indent: Int): String

}

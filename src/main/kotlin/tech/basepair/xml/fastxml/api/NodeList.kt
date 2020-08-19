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

import java.util.stream.Stream

/**
 * Represents a list of nodes that can be iterated and streamed
 *
 *     doc.selectNodes("/b").stream()
 *       .filter(n -> n.hasAttr("num"))
 *       .collect(NodeListCollector.collect())
 *
 * @author jaiew
 */
interface NodeList : Iterable<Node> {

  /**
   * @return the size of the NodeList
   */
  fun size(): Int

  /**
   * @return true if there are no nodes in the NodeList
   */
  fun isEmpty(): Boolean

  /**
   * Retrieves a Node from the NodeList at the given index
   */
  fun get(index: Int): Node

  /**
   * @return a Stream of the NodeList
   */
  fun stream(): Stream<Node>

}

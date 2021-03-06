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
import java.util.stream.Stream

/**
 * An Implementation of [NodeList]
 *
 * @author jaiew
 */
internal class NodeListImpl(_nodes: List<Node>) : NodeList {
  private val nodes: List<Node> = _nodes;

  override fun size() = nodes.size

  override fun isEmpty() = nodes.isEmpty()

  override fun get(index: Int) = nodes[index]

  override fun stream(): Stream<Node> = nodes.stream()

  override fun iterator(): Iterator<Node> = nodes.iterator()

}

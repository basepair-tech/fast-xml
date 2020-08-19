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

package tech.basepair.xml.fastxml;

import tech.basepair.xml.fastxml.api.Node;
import tech.basepair.xml.fastxml.api.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A Collector for creating a NodeList after Streaming over an existing NodeList.
 *
 *    doc.selectNodes("/b").stream()
 *      .filter(n -> n.hasAttr("num"))
 *      .collect(NodeListCollector.collect())
 *
 * @author jaiew
 */
public class NodeListCollector {

  public static Collector<Node, ?, NodeList> collect() {
    return Collector.of((Supplier<List<Node>>) ArrayList::new, List::add, (left, right) -> {
      left.addAll(right);
      return left;
    }, NodeListImpl::new);
  }

}

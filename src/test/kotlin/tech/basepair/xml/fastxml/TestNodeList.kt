package tech.basepair.xml.fastxml

import tech.basepair.xml.fastxml.api.Document
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestNodeList {

  private lateinit var documentNoNamespaces: Document
  private lateinit var documentWithNamespaces: Document

  @BeforeTest
  fun init() {
    documentNoNamespaces = FastXml.parser().parse(File(ORDER_DOC))!!
    documentWithNamespaces = FastXml.parser("ns" to "http://basepair.tech").parse(File(ORDER_DOC))!!
  }

  @Test
  fun testSelectNodes() {
    val items = documentNoNamespaces.selectNodes("/orders/order/items/item")
    assertEquals(24, items.size())
  }

  @Test
  fun testSelectingNodesFromNode() {
    val itemsNode = documentNoNamespaces.selectNode("/orders/order/items")!!
    val items = itemsNode.selectNodes("./item")
    assertEquals(6, items.size())
  }

  @Test
  fun testEmptyNodes() {
    val nodes = documentNoNamespaces.selectNodes("/invalid")
    assertTrue(nodes.isEmpty())
  }

  @Test
  fun testGetNode() {
    val nodes = documentNoNamespaces.selectNodes("/orders/order/items/item")
    val firstItem = nodes.get(0)
    assertEquals("1", firstItem.getText())
  }

}

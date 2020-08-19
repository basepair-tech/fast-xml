package tech.basepair.xml.fastxml

import tech.basepair.xml.fastxml.api.Document
import tech.basepair.xml.fastxml.exception.InvalidDataException
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.exp
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

const val ORDER_DOC = "./src/test/resources/orders.xml"
const val UNFORMATTED_DOC = "./src/test/resources/unformatted.xml"

class TestDocument {

  private lateinit var documentNoNamespaces: Document
  private lateinit var documentWithNamespaces: Document

  @BeforeTest
  fun init() {
    documentNoNamespaces = FastXml.parser().parse(File(ORDER_DOC))!!
    documentWithNamespaces = FastXml.parser("ns" to "http://basepair.tech").parse(File(ORDER_DOC))!!
  }

  @Test
  fun testGetDocRootWorks() {
    assertEquals("orders", documentNoNamespaces.getRoot().getValue())
  }

  @Test
  fun testSelectNode() {
    val node = documentNoNamespaces.selectNode("/orders/order")
    assertNotNull(node,"Node should not be null")
    assertEquals("order", node.getValue())
  }

  @Test
  fun testSelectInvalidNodeIsNull() {
    val node = documentNoNamespaces.selectNode("//invalid")
    assertNull(node, "no xpath match should be null")
  }

  @Test
  fun testSelectNodeWithNamespace() {
    val node = documentWithNamespaces.selectNode("/orders/ns:order")
    assertNotNull(node,"Node should not be null")
    assertEquals("ns:order", node.getValue())
  }

  @Test
  fun testSelectNodes() {
    val nodes = documentNoNamespaces.selectNodes("/orders/order")
    assertEquals(4, nodes.size(), "Expected 4 order nodes")
    assertEquals("order", nodes.get(0).getValue()!!, "Expected first node to be order node")
    assertEquals("order", nodes.get(1).getValue()!!, "Expected second node to be order node")
    assertEquals("ns:order", nodes.get(2).getValue()!!, "Expected third node to be ns:order node")
    assertEquals("order", nodes.get(3).getValue(true), "Expected fourth node to be order node")
  }

  @Test
  fun testSelectNodesWithNamespace() {
    val nodes = documentWithNamespaces.selectNodes("/orders/ns:order")
    assertEquals(2, nodes.size(), "Expected 2 order nodes")
    assertEquals("ns:order", nodes.get(0).getValue()!!, "Expected first node to be ns:order node")
    assertEquals("ns:order", nodes.get(1).getValue()!!, "Expected second node to be ns:order node")
    assertEquals("http://basepair.tech", nodes.get(0).getNamespaceUri())
  }

  @Test
  fun testSelectLocalName() {
    val nodes = documentWithNamespaces.selectNodes("/orders/*[local-name() = 'order']")
    assertEquals(4, nodes.size(), "There should 4 order nodes")
    assertEquals("order", nodes.get(0).getValue(true), "Expected first node to be order node")
    assertEquals("order", nodes.get(1).getValue(true), "Expected second node to be order node")
    assertEquals("order", nodes.get(2).getValue(true), "Expected third node to be order node")
    assertEquals("order", nodes.get(3).getValue(true), "Expected fourth node to be order node")
  }

  @Test
  fun testSelectRequiredNode() {
    val node = documentNoNamespaces.selectRequiredNode("/orders/order")
    assertNotNull(node, "Required order node should not be null")
  }

  @Test
  fun selectAsBoolean() {
    assertTrue(documentNoNamespaces.selectAsBoolean("/orders/order/available"))
    assertFalse(documentNoNamespaces.selectAsBoolean("/orders/order/name"), "Non boolean value should be false")
    assertFalse(documentNoNamespaces.selectAsBoolean("/invalid"), "Invalid path as boolean value should be false")
  }

  @Test
  fun testSelectAsDouble() {
    assertEquals(49.00, documentNoNamespaces.selectAsDouble("/orders/order/price"), "Double result should be 49.00")
    assertNull(documentNoNamespaces.selectAsDouble("/orders/order/name"), "Invalid value should be null")
    assertNull(documentNoNamespaces.selectAsDouble("/invalid"), "Invalid path should be null")
  }

  @Test
  fun testSelectRequiredDouble() {
    try {
      assertEquals(49.00, documentNoNamespaces.selectAsRequiredDouble("/orders/order/price"))
    } catch(e: InvalidDataException) {
      fail("InvalidDataException should not be thrown")
    }
  }

  @Test
  fun testSelectRequiredDoubleThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectAsRequiredDouble("/invalid")
    }
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectAsRequiredDouble("/orders/order/name")
    }
  }

  @Test
  fun testSelectAsInt() {
    assertEquals(15, documentNoNamespaces.selectAsInt("/orders/order/qty"))
    assertNull(documentNoNamespaces.selectAsInt("/orders/order/name"), "Invalid value should be null")
    assertNull(documentNoNamespaces.selectAsInt("/invalid"), "Invalid path should be null")
  }

  @Test
  fun testSelectRequiredInt() {
    try {
      assertEquals(15, documentNoNamespaces.selectAsRequiredInt("/orders/order/qty"))
    } catch (e: InvalidDataException) {
      fail("InvalidDAtaException should not be thrown")
    }
  }

  @Test
  fun testSelectRequiredIntThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectAsRequiredInt("/invalid")
    }
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectAsRequiredInt("/orders/order/name")
    }
  }

  @Test
  fun testSelectBigInteger() {
    assertEquals(BigInteger.valueOf(15), documentNoNamespaces.selectAsBigInteger("/orders/order/qty"))
    assertNull(documentNoNamespaces.selectAsBigInteger("/orders/order/name"), "Invalid value should be null")
    assertNull(documentNoNamespaces.selectAsBigInteger("/invalid"), "Invalid path should be null")
  }

  @Test
  fun testSelectRequiredBigInteger() {
    try {
      assertEquals(BigInteger.valueOf(15), documentNoNamespaces.selectAsRequiredBigInteger("/orders/order/qty"))
    } catch (e: InvalidDataException) {
      fail("InvalidDAtaException should not be thrown")
    }
  }

  @Test
  fun testSelectRequiredBigIntegerThrowsException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectAsRequiredBigInteger("/invalid")
    }
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectAsRequiredBigInteger("/orders/order/name")
    }
  }

  @Test
  fun testSelectBigDecimal() {
    assertEquals(BigDecimal(15), documentNoNamespaces.selectAsBigDecimal("/orders/order/qty"))
    assertNull(documentNoNamespaces.selectAsBigDecimal("/orders/order/name"), "Invalid value should be null")
    assertNull(documentNoNamespaces.selectAsBigDecimal("/invalid"), "Invalid path should be null")
  }

  @Test
  fun testSelectRequiredBigDecimal() {
    try {
      assertEquals(BigDecimal(15), documentNoNamespaces.selectAsRequiredBigDecimal("/orders/order/qty"))
    } catch (e: InvalidDataException) {
      fail("InvalidDAtaException should not be thrown")
    }
  }

  @Test
  fun testSelectRequiredBigDecimalThrowsException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectAsRequiredBigDecimal("/invalid")
    }
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectAsRequiredBigDecimal("/orders/order/name")
    }
  }

  @Test
  fun testSelectText() {
    assertEquals("Order 1", documentNoNamespaces.selectText("/orders/order/name"))
    assertNull(documentNoNamespaces.selectText("/invalid"))
  }

  @Test
  fun testSelectRequiredText() {
    try {
      assertEquals("Order 1", documentNoNamespaces.selectRequiredText("/orders/order/name"))
    } catch(e: InvalidDataException) {
      fail("Unexpected InvalidDataException")
    }
  }

  @Test
  fun testSelectRequiredTextThrowsException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectRequiredText("/invalid")
    }
  }

  @Test
  fun testFormatDocument() {
    val unformattedDoc = FastXml.parse(File(UNFORMATTED_DOC))!!

    val unformmatedXml = unformattedDoc.getRoot().toString()
    val formattedXml = unformattedDoc.format()

    val expectedFormattedXml = """<root>
      |  <node attr="1">
      |    <subnode attr="2">value</subnode>
      |  </node>
      |</root>
    """.trimMargin()

    assertEquals(1, unformmatedXml.lines().size)
    assertEquals(expectedFormattedXml, formattedXml)
  }

}

package tech.basepair.xml.fastxml

import tech.basepair.xml.fastxml.api.Document
import tech.basepair.xml.fastxml.exception.InvalidDataException
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class TestNode {

  private lateinit var documentNoNamespaces: Document
  private lateinit var documentWithNamespaces: Document

  @BeforeTest
  fun init() {
    documentNoNamespaces = FastXml.parser().parse(File(ORDER_DOC))!!
    documentWithNamespaces = FastXml.parser("ns" to "http://basepair.tech").parse(File(ORDER_DOC))!!
  }

  @Test
  fun testSelectNode() {
    val price = documentNoNamespaces.selectNode("/orders/order/price")
    val qty = documentNoNamespaces.selectNode("/orders/order/qty")
    assertNotNull(price)
    assertNotNull(qty)
    assertEquals("price", price.getValue())
    assertEquals("49.00", price.getText())
    assertEquals("qty", qty.getValue())
    assertEquals("15", qty.getText())
  }

  @Test
  fun testSelectOnNode() {
    val order = documentNoNamespaces.selectNode("/orders/order")
    assertNotNull(order)

    val price = order.selectNode("./price")
    assertNotNull(price)

    assertEquals("price", price.getValue())
    assertEquals("49.00", price.getText())
  }

  @Test
  fun testRequiredText() {
    try {
      assertEquals("49.00", documentNoNamespaces.selectNode("/orders/order/price")!!.getRequiredText())
    } catch (e: InvalidDataException) {
      fail("Unexpected InvalidDataException thrown")
    }
  }

  @Test
  fun testRequiredTextThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectNode("/orders/order/items")!!.getRequiredText()
    }
  }

  @Test
  fun testTextAsInt() {
    assertEquals(15, documentNoNamespaces.selectNode("/orders/order/qty")!!.getTextAsInt())
    assertNull(documentNoNamespaces.selectNode("/orders/order/name")!!.getTextAsInt())
  }

  @Test
  fun testTextAsRequiredInt() {
    try {
      assertEquals(15, documentNoNamespaces.selectNode("/orders/order/qty")!!.getTextAsRequiredInt())
    } catch (e: InvalidDataException) {
      fail("InvalidDataException should not have been thrown.")
    }
  }

  @Test
  fun testTextAsRequiredIntThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectNode("/orders/order/name")!!.getTextAsRequiredInt()
    }
  }

  @Test
  fun testTextAsDouble() {
    assertEquals(49.00, documentNoNamespaces.selectNode("/orders/order/price")!!.getTextAsDouble())
    assertNull(documentNoNamespaces.selectNode("/orders/order/name")!!.getTextAsDouble())
  }

  @Test
  fun testTextAsRequiredDouble() {
    try {
      assertEquals(49.00, documentNoNamespaces.selectNode("/orders/order/price")!!.getTextAsRequiredDouble())
    } catch (e: InvalidDataException) {
      fail("InvalidDataException should not have been thrown.")
    }
  }

  @Test
  fun testTextAsRequiredDoubleThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectNode("/orders/order/name")!!.getTextAsRequiredDouble()
    }
  }

  @Test
  fun testTextAsBoolean() {
    assertEquals(true, documentNoNamespaces.selectNode("/orders/order/available")!!.getTextAsBoolean())
    assertEquals(false, documentNoNamespaces.selectNode("/orders/order/name")!!.getTextAsBoolean())
  }

  @Test
  fun testTextAsRequiredBoolean() {
    try {
      assertEquals(true, documentNoNamespaces.selectNode("/orders/order/available")!!.getTextAsRequiredBoolean())
    } catch (e: InvalidDataException) {
      fail("InvalidDataException should not have been thrown.")
    }
  }

  @Test
  fun testTextAsRequiredBooleanWhenInvalid() {
    val value = documentNoNamespaces.selectNode("/orders/order/name")!!.getTextAsRequiredBoolean()
    assertFalse(value)
  }

  @Test
  fun testTextAsBigInteger() {
    assertEquals(BigInteger.valueOf(15), documentNoNamespaces.selectNode("/orders/order/qty")!!.getTextAsBigInteger())
    assertEquals(null, documentNoNamespaces.selectNode("/orders/order/name")!!.getTextAsBigInteger())
  }

  @Test
  fun testTextAsRequiredBigInteger() {
    try {
      assertEquals(BigInteger.valueOf(15), documentNoNamespaces.selectNode("/orders/order/qty")!!.getTextAsRequiredBigInteger())
    } catch (e: InvalidDataException) {
      fail("InvalidDataException should not have been thrown.")
    }
  }

  @Test
  fun testTextAsRequiredBigIntegerThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectNode("/orders/order/name")!!.getTextAsRequiredBigInteger()
    }
  }

  @Test
  fun testTextAsBigDecimal() {
    assertEquals(BigDecimal("15"), documentNoNamespaces.selectNode("/orders/order/qty")!!.getTextAsBigDecimal())
    assertEquals(null, documentNoNamespaces.selectNode("/orders/order/name")!!.getTextAsBigDecimal())
  }

  @Test
  fun testTextAsRequiredBigDecimal() {
    try {
      assertEquals(BigDecimal("15"), documentNoNamespaces.selectNode("/orders/order/qty")!!.getTextAsRequiredBigDecimal())
    } catch (e: InvalidDataException) {
      fail("InvalidDataException should not have been thrown.")
    }
  }

  @Test
  fun testTextAsRequiredBigDecimalThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectNode("/orders/order/name")!!.getTextAsRequiredBigDecimal()
    }
  }

  @Test
  fun testAttributes() {
    val order = documentNoNamespaces.selectNode("/orders/order")!!
    assertTrue(order.hasAttr("num"))
    assertEquals("1", order.getAttr("num"))

    assertFalse(order.hasAttr("invalid"))
    assertNull(order.getAttr("invalid"))

    assertEquals(1, order.getAttrAsInt("num"))
    assertNull(order.getAttrAsInt("invalid"))

    assertEquals("true", order.getAttr("processed"))
    assertEquals(true, order.getAttrAsBoolean("processed"))
    assertEquals(false, order.getAttrAsBoolean("invalid"))

    val item = order.selectNode("./items/item")!!
    assertTrue(item.hasAttr("cost"))
    assertEquals(40.00, item.getAttrAsDouble("cost"))
    assertNull(item.getAttrAsDouble("invalid"))

    val attrs = order.getAttrs()
    assertEquals(2, attrs.size)
    assertTrue(attrs.containsKey("num"))
    assertTrue(attrs.containsKey("processed"))
    assertEquals("1", attrs["num"])
    assertEquals("true", attrs["processed"])

    val emptyAttrs = order.selectNode("./items")!!.getAttrs()
    assertEquals(0, emptyAttrs.size)
  }

  @Test
  fun testRequiredAttribute() {
    val order = documentNoNamespaces.selectNode("/orders/order")!!
    assertEquals("1", order.getRequiredAttr("num"))
    assertEquals(1, order.getAttrAsRequiredInt("num"))
    assertEquals(BigInteger.valueOf(1), order.getAttrAsBigInteger("num"))
    assertEquals("true", order.getRequiredAttr("processed"))
    assertEquals(true, order.getAttrAsRequiredBoolean("processed"))
    assertEquals(40.00, order.selectNode("./items/item")!!.getAttrAsRequiredDouble("cost"))
    assertEquals(BigDecimal("40.00"), order.selectNode("./items/item")!!.getAttrAsRequiredBigDecimal("cost"))
  }

  @Test
  fun testRequiredAttributeThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectNode("/orders/order")!!.getRequiredAttr("invalid")
    }
  }

  @Test
  fun testRequiredAttributeAsBooleanThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectNode("/orders/order")!!.getAttrAsRequiredBoolean("invalid")
    }
  }

  @Test
  fun testRequiredAttributeAsIntThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectNode("/orders/order")!!.getAttrAsRequiredInt("invalid")
    }
  }

  @Test
  fun testRequiredAttributeAsDoubleThrowsInvalidDataException() {
    assertFailsWith<InvalidDataException> {
      documentNoNamespaces.selectNode("/orders/order")!!.getAttrAsRequiredBoolean("invalid")
    }
  }

  @Test
  fun testSelectingNodeDetailsInAnyOrder() {
    val order = documentNoNamespaces.selectNode("/orders/order[2]")!!
    val orderName = order.selectNode("./name")!!
    val orderPrice = order.selectNode("./price")!!
    val orderAvailable = order.selectNode("./available")!!
    val orderQuantity = order.selectNode("./qty")!!

    assertEquals("name", orderName.getValue())
    assertEquals("Order 2", orderName.getText())
    assertEquals("order", order.getValue())
    assertEquals("price", orderPrice.getValue())
    assertEquals("qty", orderQuantity.getValue())
    assertEquals("available", orderAvailable.getValue())
  }

  @Test
  fun testSelectAttributeWithXpath() {
    val attr = documentNoNamespaces.selectNode("/orders/order")!!.selectNode("@num")!!
    assertEquals("num", attr.getValue())
    assertEquals("1", attr.getText())
  }

  @Test
  fun testNsPrefix() {
    val order = documentWithNamespaces.selectNode("/orders/ns:order")!!
    assertEquals("ns:order", order.getValue())
    assertEquals("ns", order.getNsPrefix())
    assertEquals("order", order.getValue(true))
  }

}

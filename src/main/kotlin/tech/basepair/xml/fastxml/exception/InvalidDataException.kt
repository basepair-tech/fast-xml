package tech.basepair.xml.fastxml.exception

class InvalidDataException : Exception {
  constructor(message: String?, cause: Throwable?) : super(message, cause)
  constructor(message: String?) : super(message)
}

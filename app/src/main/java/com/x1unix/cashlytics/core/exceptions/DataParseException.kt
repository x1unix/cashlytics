package com.x1unix.cashlytics.core.exceptions

class DataParseException(message: String, source: String) : ParseException(message, source) {
    constructor(message: String) : this(message, "undefined")
}
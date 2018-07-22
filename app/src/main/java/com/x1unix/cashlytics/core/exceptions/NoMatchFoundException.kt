package com.x1unix.cashlytics.core.exceptions

class NoMatchFoundException(message: String, source: String): ParseException(message, source) {
    constructor(message: String) : this(message, "undefined")
}
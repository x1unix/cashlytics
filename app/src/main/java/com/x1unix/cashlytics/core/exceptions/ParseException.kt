package com.x1unix.cashlytics.core.exceptions

abstract class ParseException(message: String, val source: String): Exception(message)
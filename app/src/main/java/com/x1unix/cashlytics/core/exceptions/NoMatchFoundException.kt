package com.x1unix.cashlytics.core.exceptions

class NoMatchFoundException(message: String, val data: String, source: String): ParseException(message + " in '${data}'", source) {

}
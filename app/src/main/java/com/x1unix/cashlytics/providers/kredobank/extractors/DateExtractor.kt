package com.x1unix.cashlytics.providers.kredobank.extractors

import com.x1unix.cashlytics.exceptions.DataParseException
import java.util.regex.Pattern
import com.x1unix.cashlytics.exceptions.NoMatchFoundException
import com.x1unix.cashlytics.providers.MetadataExtractor
import com.x1unix.cashlytics.providers.MetadataParseResult
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

const val KREDO_DATE_PATTERN = """(\d{1,2}\.\d{1,3}\.\d{4}\s\d{1,2}:\d{1,2})""";
const val KREDO_DATE_FORMAT = "dd.MM.yyyy HH:mm"

/**
 * Event datetime extractor
 */
class DateExtractor : MetadataExtractor<LocalDateTime> {

    private val matcher = Pattern.compile(KREDO_DATE_PATTERN)

    val formatter = DateTimeFormat.forPattern(KREDO_DATE_FORMAT)

    override fun extractData(message: String) : MetadataParseResult<LocalDateTime> {
        val matcher = this.matcher.matcher(message)

        if ((!matcher.find()) || (matcher.groupCount() == 0)) {
            throw NoMatchFoundException("Cannot extract date metadata from the message")
        }

        val dateString = matcher.group(0)

        try {
            val dateTime = formatter.parseLocalDateTime(dateString)

            return MetadataParseResult(dateTime, dateString, message);
        } catch (ex: IllegalArgumentException) {
            throw DataParseException("Failed to parse date using pattern '$KREDO_DATE_FORMAT' (${ex.message})", dateString)
        }
    }

}
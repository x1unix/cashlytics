package com.x1unix.cashlytics.core.providers.kredobank.extractors

import com.x1unix.cashlytics.core.exceptions.DataParseException
import java.util.regex.Pattern
import com.x1unix.cashlytics.core.exceptions.NoMatchFoundException
import com.x1unix.cashlytics.core.providers.MetadataExtractor
import com.x1unix.cashlytics.core.providers.MetadataParseResult
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

/**
 * Event datetime extractor
 */
class DateExtractor : MetadataExtractor<LocalDateTime> {

    private val matcher = Pattern.compile(KREDO_DATE_PATTERN)

    private val altMatcher = Pattern.compile(KREDO_ALT_DATE_PATTERN)

    val formatter = DateTimeFormat.forPattern(KREDO_DATE_FORMAT)

    override fun extractData(message: String) : MetadataParseResult<LocalDateTime> {
        val matcher = this.matcher.matcher(message)

        if ((!matcher.find()) || (matcher.groupCount() == 0)) {
            // Try to read the date in alternate way
            return readAlternateData(message)
        }

        val dateString = matcher.group(0)

        try {
            val dateTime = formatter.parseLocalDateTime(dateString)

            return MetadataParseResult(dateTime, dateString, message)
        } catch (ex: IllegalArgumentException) {
            throw DataParseException("Failed to parse date using pattern '$KREDO_DATE_FORMAT' (${ex.message})", dateString)
        }
    }

    /**
     * Reads the datetime in alternate way.
     * Used for revert action messages
     */
    private fun readAlternateData(message: String) : MetadataParseResult<LocalDateTime> {
        val matcher = altMatcher.matcher(message)

        if ((!matcher.find()) || (matcher.groupCount() < ALT_DATE_GROUP_SZ)) {
            throw NoMatchFoundException("Cannot extract date metadata from the message", message)
        }

        val date = matcher.group(1)
        val time = matcher.group(2)
        val dateString = "$date $time"

        try {
            val dateTime = formatter.parseLocalDateTime(dateString)

            return MetadataParseResult(dateTime, matcher.group(0), message)
        } catch (ex: IllegalArgumentException) {
            throw DataParseException("Failed to parse date using pattern '$KREDO_DATE_FORMAT' (${ex.message})", dateString)
        }
    }

    companion object {
        /**
         * Default date pattern
         */
        const val KREDO_DATE_PATTERN = """(\d{1,2}\.\d{1,3}\.\d{4}\s\d{1,2}:\d{1,2})"""

        /**
         * Common date format
         */
        const val KREDO_DATE_FORMAT = "dd.MM.yyyy HH:mm"

        /**
         * Alternate date pattern (used in revert messages)
         */
        const val KREDO_ALT_DATE_PATTERN = """DATE ([0-9]{1,2}\.[0-9]{1,2}\.[0-9]{4}), TIME ([0-9]{1,2}:[0-9]{1,2}):"""

        /**
         * Alternate date regex group size
         */
        const val ALT_DATE_GROUP_SZ = 2
    }

}
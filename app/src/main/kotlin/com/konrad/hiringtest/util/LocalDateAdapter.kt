package com.konrad.hiringtest.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class LocalDateAdapter : JsonAdapter<LocalDate>() {

    override fun fromJson(reader: JsonReader): LocalDate? {
        val parser = DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ISO_DATE_TIME)
            .appendOptional(DateTimeFormatter.ISO_DATE)
            .toFormatter()
        return LocalDate.parse(reader.nextString(), parser)
    }

    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        writer.value(value?.toString())
    }
}
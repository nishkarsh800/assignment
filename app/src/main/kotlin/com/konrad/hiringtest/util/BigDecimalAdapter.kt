package com.konrad.hiringtest.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.math.BigDecimal

class BigDecimalAdapter : JsonAdapter<BigDecimal>() {
    override fun fromJson(reader: JsonReader): BigDecimal = BigDecimal(reader.nextString())

    override fun toJson(writer: JsonWriter, value: BigDecimal?) {
        writer.value(value.toString())
    }
}
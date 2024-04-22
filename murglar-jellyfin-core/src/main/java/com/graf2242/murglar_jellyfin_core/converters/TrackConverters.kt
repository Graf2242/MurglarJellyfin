package com.graf2242.murglar_jellyfin_core.converters

import com.badmanners.murglar.lib.core.model.track.source.Bitrate
import com.badmanners.murglar.lib.core.model.track.source.Extension
import javax.print.DocFlavor.STRING

fun extensionConverter(name: String): Extension =
    when (name) {
        "flac" -> Extension.FLAC
        "mp3" -> Extension.MP3
        "wav" -> Extension.WAV
        "aac" -> Extension.AAC
        "ogg" -> Extension.OGG
        else -> {Extension.UNKNOWN}
    }

fun bitrateConverter(bitrate: Int): Bitrate =
    when {
        bitrate < 33 -> Bitrate.B_32
        bitrate < 65 -> Bitrate.B_64
        bitrate < 97 -> Bitrate.B_96
        bitrate < 129 -> Bitrate.B_128
        bitrate < 161 -> Bitrate.B_160
        bitrate < 193 -> Bitrate.B_192
        bitrate < 257 -> Bitrate.B_256
        bitrate < 321 -> Bitrate.B_320
        else -> Bitrate.B_UNKNOWN
    }

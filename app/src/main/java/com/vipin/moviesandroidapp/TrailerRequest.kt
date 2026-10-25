package com.vipin.moviesandroidapp

/** What Flutter sends with `openTrailer`. */
data class TrailerRequest(
    val movieId: Int,
    val title: String,
    /** YouTube video key, null when TMDB has no trailer. */
    val youtubeKey: String?,
) {
    /** Full HTML page with the YouTube player, or null when there is no trailer. */
    fun embedHtml(): String? {
        val key = youtubeKey ?: return null
        return """
            <!DOCTYPE html>
            <html>
            <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
              html, body { margin: 0; height: 100%; background: #000; }
              iframe { position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: 0; }
            </style>
            </head>
            <body>
            <iframe
              src="https://www.youtube-nocookie.com/embed/$key?playsinline=1&autoplay=1&rel=0"
              allow="autoplay; encrypted-media; picture-in-picture"
              allowfullscreen></iframe>
            </body>
            </html>
        """.trimIndent()
    }

    companion object {
        /** Builds a request from the MethodChannel arguments, or null if invalid. */
        fun fromArguments(arguments: Any?): TrailerRequest? {
            val map = arguments as? Map<*, *> ?: return null
            val movieId = (map["movieId"] as? Number)?.toInt() ?: return null
            val title = map["title"] as? String ?: return null
            val key = (map["youtubeKey"] as? String)?.takeIf { it.isNotEmpty() }
            return TrailerRequest(movieId, title, key)
        }
    }
}
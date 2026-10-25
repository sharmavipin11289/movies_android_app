package com.vipin.moviesandroidapp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TrailerRequestTest {

    @Test
    fun fromArguments_parsesAllArguments() {
        val request = TrailerRequest.fromArguments(
            mapOf("movieId" to 550, "title" to "Fight Club", "youtubeKey" to "abc123")
        )

        assertEquals(550, request?.movieId)
        assertEquals("Fight Club", request?.title)
        assertEquals("abc123", request?.youtubeKey)
    }

    @Test
    fun fromArguments_keepsKeyNullWhenFlutterSendsNull() {
        val request = TrailerRequest.fromArguments(
            mapOf("movieId" to 1, "title" to "No Trailer", "youtubeKey" to null)
        )

        assertNotNull(request)
        assertNull(request?.youtubeKey)
    }

    @Test
    fun fromArguments_treatsEmptyKeyAsNull() {
        val request = TrailerRequest.fromArguments(
            mapOf("movieId" to 1, "title" to "t", "youtubeKey" to "")
        )

        assertNull(request?.youtubeKey)
    }

    @Test
    fun fromArguments_returnsNullWhenMovieIdIsMissing() {
        assertNull(TrailerRequest.fromArguments(mapOf("title" to "t")))
    }

    @Test
    fun fromArguments_returnsNullWhenArgumentsAreNotAMap() {
        assertNull(TrailerRequest.fromArguments("oops"))
        assertNull(TrailerRequest.fromArguments(null))
    }

    @Test
    fun embedHtml_containsTheYouTubeEmbedUrl() {
        val request = TrailerRequest(1, "t", "abc123")

        assertTrue(request.embedHtml()!!.contains("youtube-nocookie.com/embed/abc123"))
    }

    @Test
    fun embedHtml_isNullWithoutKey() {
        assertNull(TrailerRequest(1, "t", null).embedHtml())
    }
}
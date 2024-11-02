package com.example.cinemagic.SearchScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SearchSearchResults(
    @Json(name = "results") val searchResultList:List<SearchRepo>
)
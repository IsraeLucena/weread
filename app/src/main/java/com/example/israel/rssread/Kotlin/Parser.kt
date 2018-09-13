package com.example.israel.rssread.Kotlin

import org.json.JSONArray

class Parser {

    companion object {
        fun parseBody(body: String): List<Item> {

            val jsonArray = JSONArray(body)
            return parseJSONArray(jsonArray)
        }

        fun parseJSONArray(
                jsonArray: JSONArray): MutableList<Item> {
            val items = mutableListOf<Item>()

            for (index in 0 until jsonArray.length()) {
                val json = jsonArray.getJSONObject(index)

                val id = json.getString("id")
                val name = if (json.has("name")) json.getString("name") else ""
                val description = if (json.has("desc")) json.getString("desc") else ""

                items.add(Item(id, name, description))
            }

            return items
        }
    }
}
package com.example.insta

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Post")
class Post : ParseObject(){

    companion object{
        const val KEY_AUTHOR = "author"
        const val KEY_IMAGE = "image"
        const val KEY_DESCRIPTION = "description"
    }

    fun getAuthor() = getParseUser(KEY_AUTHOR)
    fun setAuthor(author: ParseUser) = put(KEY_AUTHOR, author)

    fun getImage() = getParseFile(KEY_IMAGE)
    fun setImage(parsefile: ParseFile) = put(KEY_IMAGE, parsefile)

    fun getDescription() = getString(KEY_DESCRIPTION)
    fun setDescription(description: String) = put(KEY_DESCRIPTION, description)




}
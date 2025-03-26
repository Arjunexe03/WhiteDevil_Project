package org.whitedevil.igrs.ui.items

data class Credit(
    val name: String,
    val author: String,
    val license: String,
    val version: String,
    val url: String
)

data class Problem(
    val name: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val problem: String = ""
)

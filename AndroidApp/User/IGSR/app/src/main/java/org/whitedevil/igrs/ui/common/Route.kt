package org.whitedevil.igrs.ui.common

object Route {

    const val HOME = "home"
    const val SETTINGS = "settings"
    const val SETTINGS_PAGE = "settings_page"
    const val DARK_THEME = "dark_theme"
    const val CREDITS = "credits"
    const val LANGUAGES = "languages"
    const val ABOUT = "about"
    const val APPEARANCE = "appearance"
}

infix fun String.arg(arg: String) = "$this/{$arg}"

infix fun String.id(id: Int) = "$this/$id"
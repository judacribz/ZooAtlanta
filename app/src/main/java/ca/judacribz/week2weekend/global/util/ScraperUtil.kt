package ca.judacribz.week2weekend.global.util

fun extractUrl(urlStr: String): String =
    urlStr.substring(urlStr.indexOf("(") + 1, urlStr.indexOf(")"))
package ca.judacribz.week2weekend.global.util

import org.jsoup.nodes.Element

fun extractUrl(urlStr: String): String =
    urlStr.substring(urlStr.indexOf("(") + 1, urlStr.indexOf(")"))

fun Element.getFirstElementByTag(tag: String): Element? =
    getElementsByTag(tag)[0] ?: null
package ca.judacribz.zooatlanta.global.util

import org.jsoup.nodes.Element

fun extractUrl(urlStr: String?): String? =
    urlStr?.substring(urlStr.indexOf("(") + 1, urlStr.indexOf(")"))

fun Element.getFirstElementByTag(tag: String) = getElementsByTag(tag)[0] ?: null

fun Element.getFirstClassByTag(classId: String) = getElementsByClass(classId)[0] ?: null
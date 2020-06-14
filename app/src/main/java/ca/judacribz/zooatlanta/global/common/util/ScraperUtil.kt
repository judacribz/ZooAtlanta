package ca.judacribz.zooatlanta.global.common.util

import ca.judacribz.zooatlanta.global.common.constants.ATTR_HREF
import ca.judacribz.zooatlanta.global.common.constants.TAG_A
import org.jsoup.nodes.Element

fun extractImageUrl(urlStr: String?): String? =
    urlStr?.substring(urlStr.indexOf("(") + 1, urlStr.indexOf(")"))

fun Element.getFirstATagUrl() = getFirstElementByTag(TAG_A)?.attr(ATTR_HREF)

fun Element.getFirstElementByTagText(tag: String) = getFirstElementByTag(tag)?.text()

fun Element.getFirstElementByTag(tag: String): Element? = getElementsByTag(tag).first()

fun Element.getFirstElementByClass(classId: String): Element? = getElementsByClass(classId).first()

fun Element.getFirstElementByClassText(classId: String) = getFirstElementByClass(classId)?.text()

fun Element.getFirstElementByClass(vararg classId: String): Element? {
    var element: Element? = this

    classId.forEach {
        element = element?.getFirstElementByClass(it)
    }

    return element
}
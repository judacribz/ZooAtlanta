package com.zooatlanta.data.remote

import com.zooatlanta.core.coroutines.DispatcherProvider
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

class ZooAtlantaScraper @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
) {
    companion object {
        private const val BASE_URL = "https://zooatlanta.org"
        private const val ANIMALS_PATH = "/animals/"
        private const val USER_AGENT = "Mozilla/5.0 (Android 14; Mobile; rv:126.0) Gecko/126.0 Firefox/126.0"
        private const val REFERRER = "https://www.google.com"
        private const val CARD_SELECTOR = "div.animal.card, article.animal.card"
        private const val CATEGORY_CARD_SELECTOR = "div.popular-category, article.popular-category"
        private const val FEATURED_IMAGE_CLASS = "featured-image"
        private const val DIET_LABEL = "Diet "
        private const val STATUS_LABEL = " Status In The Wild "
        private const val RANGE_LABEL = " Range "
        private const val READ_MORE_LABEL = " Read More"
    }

    suspend fun fetchCategories(): List<AnimalCategoryDto> = withContext(dispatcherProvider.io) {
        val document = loadDocument(BASE_URL + ANIMALS_PATH)
        document
            .select(CATEGORY_CARD_SELECTOR)
            .mapNotNull { element ->
                val anchor = element.selectFirst("a") ?: return@mapNotNull null
                val url = sanitizeUrl(anchor.attr("href")) ?: return@mapNotNull null
                val name = element.selectFirst(".title")?.text()?.ifBlank { null }
                    ?: anchor.text().ifBlank { null } ?: return@mapNotNull null
                val slug = resolveSlug(url)
                AnimalCategoryDto(
                    name = name,
                    slug = slug,
                    url = url
                )
            }
            .distinctBy { it.slug }
    }

    suspend fun fetchAnimals(category: AnimalCategoryDto?): List<AnimalDto> =
        withContext(dispatcherProvider.io) {
            val document = when (category) {
                null -> loadDocument(BASE_URL + ANIMALS_PATH)
                else -> loadDocument(category.url)
            }
            extractAnimals(document, category)
        }

    private fun extractAnimals(document: Document, category: AnimalCategoryDto?): List<AnimalDto> {
        return document.select(CARD_SELECTOR).mapNotNull { element ->
            parseAnimalCard(element, category)
        }
    }

    private fun parseAnimalCard(element: Element, category: AnimalCategoryDto?): AnimalDto? {
        val name = element.selectFirst("h3")?.text()?.ifBlank { null } ?: return null
        val scientificName = element.selectFirst("em")?.text()?.ifBlank { null }
        val styleAttribute = element.getElementsByClass(FEATURED_IMAGE_CLASS)
            .firstOrNull()
            ?.attr("style")
        val imageUrl = styleAttribute?.let { style ->
            val startIndex = style.indexOf('(')
            val endIndex = style.indexOf(')')
            if (startIndex >= 0 && endIndex > startIndex) {
                sanitizeUrl(style.substring(startIndex + 1, endIndex))
            } else {
                null
            }
        }
        val backText = element.selectFirst(".flipper .back .container")?.text().orEmpty()
        val diet = extractSection(backText, DIET_LABEL, STATUS_LABEL)
        val status = extractSection(backText, STATUS_LABEL, RANGE_LABEL)
        val habitatRange = extractSection(backText, RANGE_LABEL, READ_MORE_LABEL)
        val profileUrl = element.selectFirst("a")?.attr("href")?.let(::sanitizeUrl)

        return AnimalDto(
            name = name,
            scientificName = scientificName,
            imageUrl = imageUrl,
            diet = diet,
            habitatRange = habitatRange,
            conservationStatus = status,
            profileUrl = profileUrl,
            categoryName = category?.name,
            categorySlug = category?.slug
        )
    }

    private fun extractSection(text: String, startLabel: String, endLabel: String): String? {
        val startIndex = text.indexOf(startLabel)
        val endIndex = text.indexOf(endLabel)
        if (startIndex < 0 || endIndex <= startIndex) return null
        val value = text.substring(startIndex + startLabel.length, endIndex)
        return value.trim().ifBlank { null }
    }

    private fun loadDocument(url: String): Document =
        Jsoup.connect(url)
            .userAgent(USER_AGENT)
            .referrer(REFERRER)
            .timeout(15_000)
            .followRedirects(true)
            .get()

    private fun sanitizeUrl(rawUrl: String): String? {
        val trimmed = rawUrl.trim()
        if (trimmed.isEmpty()) return null
        val absolute = when {
            trimmed.startsWith("http", ignoreCase = true) -> trimmed
            trimmed.startsWith("//") -> "https:${trimmed.removePrefix("//")}"
            trimmed.startsWith("/") -> BASE_URL + trimmed
            else -> BASE_URL.trimEnd('/') + "/" + trimmed.removePrefix("./")
        }
        return absolute.replace("(?i)https://+".toRegex(), "https://")
    }

    private fun resolveSlug(url: String): String {
        val cleaned = url.substringBefore('?').trimEnd('/')
        return cleaned.substringAfterLast('/').ifBlank {
            cleaned.substringAfter(BASE_URL).trim('/').replace('/', '-')
        }
    }
}

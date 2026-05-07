package com.example.newapp.repository

import com.example.newapp.model.News
import com.example.newapp.model.NewsEntity
import com.example.newapp.provider.NewsDao
import com.example.newapp.provider.NewsProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface NewsRepository {
    suspend fun getNews(country: String): List<News>
    fun getNew(title: String): News
    fun getNewsLocally(): Flow<List<News>>
}

class NewsRepositoryImp @Inject constructor(
    private val newsProvider: NewsProvider,
    private val newsDao: NewsDao
) : NewsRepository {

    override suspend fun getNews(country: String): List<News> {
        try {
            val response = newsProvider.topHeadlines(country)
            if (response.isSuccessful) {
                val articles = response.body()?.articles ?: emptyList()
                val entities = articles.map {
                    NewsEntity(
                        title = it.title,
                        content = it.content,
                        author = it.author,
                        url = it.url,
                        urlToImage = it.urlToImage ?: ""
                    )
                }
                newsDao.clearNews()
                newsDao.insertNews(entities)
                return articles
            }
        } catch (e: Exception) {

        }
        return emptyList()
    }

    override fun getNew(title: String): News {
        TODO("Not yet implemented")
    }

    override fun getNewsLocally(): Flow<List<News>> {
        return newsDao.getNewsLocally().map { entities ->
            entities.map {
                News(
                    title = it.title,
                    content = it.content,
                    author = it.author,
                    url = it.url,
                    urlToImage = it.urlToImage
                )
            }
        }
    }
}
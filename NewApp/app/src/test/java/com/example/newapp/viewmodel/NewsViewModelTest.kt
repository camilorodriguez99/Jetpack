package com.example.newapp.viewmodel

import com.example.newapp.model.News
import com.example.newapp.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private lateinit var viewModel: NewsViewModel
    private lateinit var repository: NewsRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(NewsRepository::class.java)
        viewModel = NewsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getNew_returnsCorrectNewsItem() = runTest {
        val testNews = News(
            title = "A",
            content = "B",
            author = "C",
            url = "D",
            urlToImage = "E"
        )
        val newsList = listOf(testNews)

        `when`(repository.getNewsLocally()).thenReturn(flowOf(newsList))

        val result = viewModel.getNew("A")

        assertEquals(testNews, result)
    }
}
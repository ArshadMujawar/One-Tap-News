package com.asm.onetapnews

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }
    private fun fetchData() {
       val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=9d38bf60774649f39483771848e0fac1"
       val jsonObjectRequest = JsonObjectRequest(
           Request.Method.GET,
           url,
           null,
           {
            val newsJsonArray = it.getJSONArray("articles")
            val newsArray = ArrayList<News>()
            for (i in 0 until newsJsonArray.length()) {
                val newsJSONObject = newsJsonArray.getJSONObject(i)
                val news = News (
                    newsJSONObject.getString("title"),
                    newsJSONObject.getString("author"),
                    newsJSONObject.getString("url"),
                    newsJSONObject.getString("urlToImage")
                )
                newsArray.add(news)
            }
               mAdapter.updateNews(newsArray)
           },
           {

           })

    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

}

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}
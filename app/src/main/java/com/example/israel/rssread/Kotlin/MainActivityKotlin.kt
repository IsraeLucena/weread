package com.example.israel.rssread.Kotlin
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.israel.rssread.R
import com.wedeploy.android.Callback
import com.wedeploy.android.WeDeploy
import com.wedeploy.android.data.RealTime
import com.wedeploy.android.query.SortOrder
import com.wedeploy.android.transport.Response

import org.json.JSONArray
import java.lang.Exception

class MainActivityKotlin : AppCompatActivity() {

    var realTime: RealTime? = null
    lateinit var weDeploy: WeDeploy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_kotlin)
 //       setSupportActionBar(toolbar)
        weDeploy = WeDeploy.Builder().build()

        val recyclerView = findViewById<RecyclerView>(R.id.list_item_recycler)

//        fab.setOnClickListener { view ->
 //           val intent = Intent(this, AddItemActivity::class.java)
 //           startActivity(intent)
 //       }

        weDeploy.data(URL)
                .orderBy("name", SortOrder.ASCENDING)
                .get("items")
                .execute(object: Callback {
                    override fun onSuccess(response: Response?) {
                        response?.let {
                            val items = Parser.parseBody(response.body)
                            refreshRecyclerView(recyclerView, items)
                        }
                    }

                    override fun onFailure(e: Exception?) {
                    }
                })

        realTime = weDeploy.data(URL)
                .orderBy("name", SortOrder.ASCENDING)
                .watch("items")
                .on("changes", { args ->
                    args.forEach {
                        val items = Parser.parseJSONArray(it as JSONArray)
                        refreshRecyclerView(recyclerView, items)
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()

        realTime?.close()
    }

    fun refreshRecyclerView(recyclerView: RecyclerView, items: List<Item>) {
        if (recyclerView.adapter == null) {
            val linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

            recyclerView.layoutManager = linearLayoutManager
            recyclerView.adapter = ItemAdapter(items)

        } else {
            (recyclerView.adapter as ItemAdapter).items = items
            (recyclerView.adapter as ItemAdapter).notifyDataSetChanged()
        }
    }

    companion object {
        const val URL = "https://data-israelweread.wedeploy.io"
    }

}

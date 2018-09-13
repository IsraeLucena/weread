package com.example.israel.rssread.Kotlin

import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.israel.rssread.R
import com.wedeploy.android.Callback
import com.wedeploy.android.WeDeploy
import com.wedeploy.android.transport.Response
import org.json.JSONObject
import java.lang.Exception


class AddItemActivity : AppCompatActivity(), Callback {

    override fun onSuccess(response: Response?) {
        Snackbar.make(container, "Item Saved", Snackbar.LENGTH_LONG)
                .show()
    }

    override fun onFailure(e: Exception?) {
        e?.let {
            Snackbar.make(container, e.message!!, Snackbar.LENGTH_LONG)
                    .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item_kotlin)

        weDeploy = WeDeploy.Builder().build()
        container = findViewById(R.id.content)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val descEditText = findViewById<EditText>(R.id.descEditText)
        val addButton = findViewById<Button>(R.id.addButton)

        val parcelable = intent.extras?.getParcelable<Item>("item")

        parcelable?.let {
            itemId = it.id
            nameEditText.setText(it.name)
            descEditText.setText(it.desc)
        }

        addButton.setOnClickListener {
            saveItem(nameEditText, descEditText)
        }
    }

    private fun saveItem(nameEditText: EditText, descEditText: EditText) {
        val name = nameEditText.text
        val desc = descEditText.text

        if (name.isNotEmpty()) {

            val itemJSON = JSONObject()

            itemJSON.put("name", name)
            itemJSON.put("desc", desc)

            if (itemId.isEmpty()) {

                weDeploy.data(MainActivityKotlin.URL)
                        .create("items", itemJSON)
                        .execute(this)

            } else {

                weDeploy.data(MainActivityKotlin.URL)
                        .update("items/$itemId", itemJSON)
                        .execute(this)
            }

        } else {
            showError()
        }
    }

    private fun showError() {
        AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Please fill the name")
                .setNeutralButton("OK", { dialog, which ->
                })
                .show()
    }

    lateinit var container: LinearLayout
    lateinit var weDeploy: WeDeploy
    var itemId = ""
}
package com.rdevansh.quotesforthebest

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var db:SQLiteDatabase? = null
    var cursor:Cursor?=null
    var categoriesAdapter:CategoriesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Read all quote categories from database
        val myNotesDatabaseHelper=QuotesOpenHelper(this)
        db=myNotesDatabaseHelper.readableDatabase

        cursor =  db!!.query("quote_categories", arrayOf("_id","image_resource_id","name"),
        null, null, null, null, null)

        var listOfCategories = mutableListOf<Category>()

        while(cursor!!.moveToNext())
        {
            val categoryId = cursor!!.getInt(0)
            val categoryResourceId = cursor!!.getInt(1)
            val categoryName = cursor!!.getString(2)

            val category:Category = Category(categoryId, categoryResourceId, categoryName)

            listOfCategories.add(category)

        }

        categoriesAdapter = CategoriesAdapter(this, listOfCategories){categoryId->
            val intent = Intent(this, QuotesDetailsActivity::class.java)
            intent.putExtra("QUOTE_CATEGORY_ID", categoryId)
            startActivity(intent)
        }

        // Using te=he Layout Manager
        val categoriesLayoutManager = LinearLayoutManager(this)

        rvQuoteCategories.adapter = categoriesAdapter
        rvQuoteCategories.layoutManager = categoriesLayoutManager

    }

    override fun onDestroy() {
        super.onDestroy()

        db!!.close()
        cursor!!.close()
    }
}
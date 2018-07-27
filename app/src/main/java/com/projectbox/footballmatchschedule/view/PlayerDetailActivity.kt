package com.projectbox.footballmatchschedule.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.projectbox.footballmatchschedule.R
import kotlinx.android.synthetic.main.activity_player_detail.*

class PlayerDetailActivity : AppCompatActivity() {

    companion object {
        const val Ext_Name = "player_name"
        const val Ext_Description = "player_description"
        const val Ext_Image = "player_image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)

        initUI()
    }

    private fun initUI() {
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(Ext_Name)

        val imgUrl = intent.getStringExtra(Ext_Image)
        if (imgUrl.isNullOrEmpty()) {
            img_fanart.visibility = View.GONE
        } else {
            Glide.with(this).load(imgUrl).into(img_fanart)
        }

        txt_description.text = intent.getStringExtra(Ext_Description)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}

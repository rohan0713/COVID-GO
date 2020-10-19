package com.example.covid_go

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log

lateinit var stateadptr: stateadapter
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fetchresults()

    }

    private fun fetchresults() {

        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) {
                client.api.execute()
            }

            if(response.isSuccessful){

                val data = Gson().fromJson(response.body?.string(), Response::class.java)
                launch(Dispatchers.Main){

                    bindcombineddata(data.statewise[0])
                    bindstatewisedata(data.statewise.subList(1 , data.statewise.size))
                }
            }
        }
    }

    private fun bindstatewisedata(subList: List<StatewiseItem>) {

        stateadptr = stateadapter(subList)
        list.adapter = stateadptr

    }

    @SuppressLint("SetTextI18n")
    private fun bindcombineddata(statewiseItem: StatewiseItem) {

        val lastupdatedtime = statewiseItem.lastupdatedtime
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastupdated.text = "Last Updated on : ${getTime(simpleDateFormat.parse(lastupdatedtime))}"
        Confirmed_cases.text = statewiseItem.confirmed
        active_cases.text = statewiseItem.active
        recovered_cases.text = statewiseItem.recovered
        deceased.text = statewiseItem.deaths
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(past : Date) : String{

        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

        return when{

            seconds < 60 -> {
                "few seconds ago"
            }

            minutes < 60 -> {
                "$minutes minutes ago"
            }
            hours < 60 -> {
                "$hours hour ${minutes%60} minutes ago"
            }
            else -> {

                SimpleDateFormat("dd/MM/yyyy  HH:mm:ss").format(past).toString()
            }

        }

    }
}
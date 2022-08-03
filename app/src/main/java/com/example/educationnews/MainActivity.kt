package com.example.educationnews

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.properties.Delegates

class Feed{
    var title: String =""
    var description: String = ""
    var link: String = ""
    var pubdate: String =""

    override fun toString(): String {
        return """
            title = $title
            description = $description
            link = $link
            pubdate = $pubdate
            """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val downloadFromWeb by lazy{DownloadFromWeb(this, xmlListView)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val downloadFromWeb = DownloadFromWeb(this, xmlListView)
        Log.d(TAG,"execute called")
        downloadFromWeb.execute("https://timesofindia.indiatimes.com/rssfeeds/913168846.cms")
        Log.d(TAG,"execute finshed")

    }

    override fun onDestroy() {
        super.onDestroy()
        downloadFromWeb.cancel(true)
    }

    companion object {
        private class DownloadFromWeb(context: Context, mainView: ListView): AsyncTask<String, Void, String>(){
            private val TAG = "DownloadFromWeb"
            var context by Delegates.notNull<Context>()
            var mainView by Delegates.notNull<ListView>()

            init {
                this.context= context
                this.mainView = mainView
            }

            override fun onPostExecute(result: String?) {
                Log.d(TAG,"onpost called")
                super.onPostExecute(result)
                Log.d(TAG,"this is result ${result}")
                val parseXml = ParseXml()
                parseXml.parsefun(result!!)

                val feedAdaptor = FeedAdaptor(context, R.layout.adaptor_view, parseXml.lst)
                mainView.adapter = feedAdaptor
            }

            override fun doInBackground(vararg params: String?): String {
                val rssFeed = downloadXml(params[0])
                return if(rssFeed.isNotEmpty()){
                    rssFeed
                } else{
                    Log.d(TAG,"error in returning the rssfeed from the downloadxml function")
                    ""
                }
            }


            private fun downloadXml(urlpath: String?): String{
                val rssFeed = StringBuilder()
                try{
                    val url = URL(urlpath)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    val response = connection.responseCode
                    Log.d(TAG,response.toString())

                    connection.inputStream.buffered().reader().use { reader ->
                       rssFeed.append(reader.readText())
                    }
                    return rssFeed.toString()

                }catch(e:Exception) {
                    val errorMessage: String = when(e){
                        is MalformedURLException -> "Url is incorrect ${e.message}"
                        is IOException -> "input output problem ${e.message}"
                        is SecurityException -> {e.printStackTrace()
                            "security problem ${e.message}"
                        }
                        else -> "unknown error"
                    }
                    Log.e(TAG,errorMessage)
                }
                return ""
            }
        }
    }
}
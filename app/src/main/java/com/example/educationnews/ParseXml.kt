package com.example.educationnews

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception

class ParseXml {

    private val TAG = "ParseXml"
    val lst = ArrayList<Feed>()

    fun parsefun(result: String): Boolean{
        Log.d(TAG,"result receive ${result}")
        var inEntry = false
        var status = true
        var aInEntry = false
        var content = ""
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val pullParser = factory.newPullParser()
            pullParser.setInput(result.reader())
            var eventType = pullParser.eventType
            var currentRecord = Feed()
            while (eventType!=XmlPullParser.END_DOCUMENT){
                val tagName = pullParser.name?.toLowerCase()
                when(eventType){
                    XmlPullParser.START_TAG -> {
                        if(tagName == "item"){
                            inEntry = true
                            Log.d(TAG,"entry tag status=${inEntry}")
                        }
                    }

                    XmlPullParser.TEXT -> {
                        content = pullParser.text
                    }

                    XmlPullParser.END_TAG ->{
                        if(inEntry){
                            when(tagName){
                                "title" -> currentRecord.title = content
                                "description" -> {
                                    if(content == "/a"){
                                        currentRecord.description = "No Description Available"
                                    }
                                    else{
                                        currentRecord.description = content
                                    }
                                }
                                "link"-> currentRecord.link = content
                                "pubdate" -> currentRecord.pubdate = content
                                "item"->{
                                    inEntry = false
                                    lst.add(currentRecord)
                                    currentRecord = Feed()
                                }
                            }

                        }
                    }
                }
                eventType = pullParser.nextToken()
            }
            for(i in lst){
                Log.d(TAG,"*************")
                Log.d(TAG,i.toString())
            }

        }catch (e:Exception){
            e.printStackTrace()
            status = false
        }
        return status
    }

}
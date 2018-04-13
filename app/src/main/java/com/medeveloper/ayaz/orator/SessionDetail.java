package com.medeveloper.ayaz.orator;

/**
 * Created by Ayaz on 4/10/2018.
 */

class SessionDetail {
    String date,time,word,meaning,topic,speaker;

    public SessionDetail(String date, String time, String word,String meaning, String topic, String speaker) {
        this.date = date;
        this.time = time;
        this.word = word;
        this.topic = topic;
        this.speaker = speaker;
        this.meaning=meaning;
    }

    public SessionDetail() {
    }
}

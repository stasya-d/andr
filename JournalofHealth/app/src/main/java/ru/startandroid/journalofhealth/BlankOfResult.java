package ru.startandroid.journalofhealth;

class BlankOfResult {

    String data;
    String high;
    String low;
    String pulse;
    String sugar;
    String comment;
    String all;
    int quality;

    BlankOfResult(String all, String data,
                  String high,
                  String low,
                  String pulse,
                  String sugar,
                  String comment,
                  int quality) {
        this.all = all;
        this.data = data;
        this.high = high;
        this.low = low;
        this.pulse = pulse;
        this.sugar = sugar;
        this.comment = comment;
        this.quality = quality;
    }
}
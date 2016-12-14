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

    BlankOfResult() {
    }

    BlankOfResult(String textPressure,
                  String textPulse,
                  String textSugar,
                  String textComment,
                  String data,
                  String high,
                  String low,
                  String pulse,
                  String sugar,
                  String comment,
                  int quality) {

        this.data = data;
        this.high = high;
        this.low = low;
        this.pulse = pulse;
        this.sugar = sugar;
        this.comment = comment;
        this.quality = quality;
        this.all = getAll(textPressure, textPulse, textSugar, textComment, high, low, pulse, sugar, comment);
    }

    public String getAll(String textPressure,
                         String textPulse,
                         String textSugar,
                         String textComment,
                         String high,
                         String low,
                         String pulse,
                         String sugar,
                         String comment) {
        String allInf = new String("");
        Boolean first = true;

        if (high.length() > 0 && low.length() > 0) {
            allInf = textPressure + " " + high + "/" + low;
            first = false;
        }
        if (pulse.length() > 0) {
            if (!first) allInf += "\n";
            allInf = allInf + textPulse + " " + pulse;
            first = false;
        }
        if (sugar.length() > 0) {
            if (!first) allInf += "\n";
            allInf = allInf + textSugar + " " + sugar;
            first = false;
        }
        if (comment.length() > 0) {
            if (!first) allInf += "\n";
            allInf = allInf + textComment + " " + comment;
        }
        return allInf;
    }

}
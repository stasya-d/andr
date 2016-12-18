package ru.startandroid.journalofhealth;

class Result {

    public String data;
    public String high;
    public String low;
    public String pulse;
    public String sugar;
    public String comment;
    public String all;
    int quality;

    Result() {
    }

    Result(String textPressure,
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

    String getAll(String textPressure,
                         String textPulse,
                         String textSugar,
                         String textComment,
                         String high,
                         String low,
                         String pulse,
                         String sugar,
                         String comment) {
        String allInf = "";
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
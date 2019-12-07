package com.hqyxjy.ldf.supercalendar;

public class Event {
    private String eventContent;
    private int identifier;
    public static int NOTHAVE = -999;
    public Event(){
        this.identifier = NOTHAVE;
        this.eventContent = null;
    }
    public Event(String eventContent) {
        this.eventContent = eventContent;
        this.identifier = 0;
    }
    public Event(String eventContent,int i){
        this.eventContent = eventContent;
        this.identifier = i;
    }

    public String getEventContent() {
        return eventContent;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }
}

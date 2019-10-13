package com.mytlogos.enterprisedesktop.background;



import java.time.LocalDateTime;

public interface EditEvent {
    int getId();

    int getObjectType();

    int getEventType();


    LocalDateTime getDateTime();

    String getFirstValue();

    String getSecondValue();
}

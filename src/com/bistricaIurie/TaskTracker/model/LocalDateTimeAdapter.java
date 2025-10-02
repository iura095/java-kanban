package com.bistricaIurie.TaskTracker.model;

import com.bistricaIurie.TaskTracker.model.error.TaskException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
        if (localDate == null) {
            jsonWriter.value("null");
        } else {
            jsonWriter.value(localDate.format(dateTimeFormatter));
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        String dateString = jsonReader.nextString();
        try {
            if (dateString.equals("null") || dateString.isEmpty()) {
                return null;
            } else {
                return LocalDateTime.parse(dateString, dateTimeFormatter);
            }
        } catch (DateTimeParseException e) {
            throw new TaskException("Неправильный формат данных.");
        } catch (NullPointerException e) { //Если dateString=null то isEmpty() кидает NPE
            return null;
        }
    }
}

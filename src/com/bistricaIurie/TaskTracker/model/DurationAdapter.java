package com.bistricaIurie.TaskTracker.model;

import com.bistricaIurie.TaskTracker.model.error.TaskException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.value(Duration.ZERO.toMinutes());
        } else {
            jsonWriter.value(duration.toMinutes());
        }
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        String dateString = jsonReader.nextString();
        try {
            if (!dateString.isEmpty()) {
                return Duration.ofMinutes(Integer.parseInt(dateString));
            } else {
                throw new TaskException("Неправильный формат данных.");
            }
        } catch (NullPointerException e) { //Если dateString=null то isEmpty() кидает NPE
            return Duration.ZERO;
        } catch (NumberFormatException e) {
            throw new TaskException("Неправильный формат данных.");
        }
    }
}

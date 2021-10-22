package de.blutmondgilde.otherlivingbeings.util.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.UUID;

public class GsonUUIDTypeAdapter extends TypeAdapter<UUID> {

    @Override
    public void write(JsonWriter out, UUID value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public UUID read(JsonReader in) throws IOException {
        UUID uuid;
        in.peek();
        uuid = UUID.fromString(in.nextString());
        return uuid;
    }
}

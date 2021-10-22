package de.blutmondgilde.otherlivingbeings.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.Util;

import java.io.IOException;
import java.util.UUID;

public class GsonUUIDTypeAdapter extends TypeAdapter<UUID> {

    @Override
    public void write(JsonWriter out, UUID value) throws IOException {
        out.beginObject().name("uuid").value(value.toString()).endObject();
    }

    @Override
    public UUID read(JsonReader in) throws IOException {
        UUID uuid = Util.NIL_UUID;
        in.beginObject();
        String fieldName = null;

        while (in.hasNext()) {
            JsonToken token = in.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldName = in.nextName();
            }

            if ("uuid".equals(fieldName)) {
                token = in.peek();
                uuid = UUID.fromString(in.nextString());
            }
        }
        return uuid;
    }
}

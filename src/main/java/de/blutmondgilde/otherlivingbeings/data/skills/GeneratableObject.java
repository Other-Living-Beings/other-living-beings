package de.blutmondgilde.otherlivingbeings.data.skills;

import com.google.gson.JsonObject;

public interface GeneratableObject<T> {
    JsonObject toJson();

    T fromJson(String jsonString);
}

package de.metaphoriker.progress.model.persistence;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.metaphoriker.progress.model.Task;
import de.metaphoriker.progress.model.TaskState;

import java.lang.reflect.Type;
import java.util.List;

public class TaskAdapter implements JsonSerializer<Task>, JsonDeserializer<Task> {

    @Override
    public JsonElement serialize(Task src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", src.id());
        jsonObject.addProperty("name", src.name());
        jsonObject.addProperty("state", src.state().name());
        jsonObject.add("subTasks", context.serialize(src.subTasks()));
        return jsonObject;
    }

    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        long id = jsonObject.get("id").getAsLong();
        String name = jsonObject.get("name").getAsString();
        TaskState state = TaskState.valueOf(jsonObject.get("state").getAsString());
        List<Long> subTasks = context.deserialize(jsonObject.get("subTasks"), List.class);
        return new Task(id, name, subTasks, state);
    }
}
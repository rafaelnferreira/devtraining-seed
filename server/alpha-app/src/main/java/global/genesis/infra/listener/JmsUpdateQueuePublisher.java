package global.genesis.infra.listener;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

public class JmsUpdateQueuePublisher {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public record UpdateMessage(
            @JsonProperty("entityType")
            String entityType,
            @JsonProperty("currentVersion")
            Map<String, Object> currentVersion,
            @JsonProperty("previousVersion")
            Map<String, Object> previousVersion,
            @JsonProperty("type")
            UpdateType type
    ) {
        @Override
        public Map<String, Object> currentVersion() {
            return currentVersion;
        }

        @Override
        public String entityType() {
            return entityType;
        }

        @Override
        public Map<String, Object> previousVersion() {
            return previousVersion;
        }

        @Override
        public UpdateType type() {
            return type;
        }
    }

    public enum UpdateType {
        INSERT,
        UPDATE,
        DELETE
    }
}

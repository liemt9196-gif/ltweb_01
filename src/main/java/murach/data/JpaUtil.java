package murach.data;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public final class JpaUtil {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
        createEntityManagerFactory();

    private JpaUtil() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return ENTITY_MANAGER_FACTORY;
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        String databaseUrl = System.getenv("DB_URL");

        if (databaseUrl == null || databaseUrl.isBlank()) {
            return Persistence.createEntityManagerFactory("maillistPU");
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("jakarta.persistence.jdbc.url", databaseUrl);
        properties.put("jakarta.persistence.jdbc.user", requiredEnvironment("DB_USERNAME"));
        properties.put("jakarta.persistence.jdbc.password", requiredEnvironment("DB_PASSWORD"));
        return Persistence.createEntityManagerFactory("maillistPU", properties);
    }

    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }
}

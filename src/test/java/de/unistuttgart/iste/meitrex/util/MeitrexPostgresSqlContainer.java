package de.unistuttgart.iste.meitrex.util;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * A singleton container for the PostgreSQL database.
 * THIS CURRENTLY COPIED FROM <a href="
 * https://github.com/MEITREX/common_test/blob/main/src/main/java/de/unistuttgart/iste/meitrex/common/testutil/MeitrexPostgresSqlContainer.java">
 * the common_test repository</a>
 * Currently, the common_test can not be used as a dependency because of problems with the graphql dependency.
 * TODO: Remove this class and use the common_test repository as a dependency
 */
public class MeitrexPostgresSqlContainer extends PostgreSQLContainer<MeitrexPostgresSqlContainer>
        implements BeforeAllCallback {

    private static final String IMAGE_VERSION = "postgres:latest";

    private static MeitrexPostgresSqlContainer container;

    private MeitrexPostgresSqlContainer() {
        super(IMAGE_VERSION);
    }

    public static MeitrexPostgresSqlContainer getInstance() {
        if (container == null) {
            container = new MeitrexPostgresSqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        getInstance().start();
    }
}
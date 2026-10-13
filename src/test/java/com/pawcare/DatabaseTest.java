package com.pawcare;
import org.junit.jupiter.api.BeforeEach;
import java.sql.*;
import java.nio.file.*;
/** Test database only. An optional external URL MUST name a database ending in _test. */
public abstract class DatabaseTest {
    @BeforeEach public void resetDatabase() throws Exception {
        String url=System.getProperty("pawcare.testUrl","jdbc:h2:mem:pawcare_test;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=5000");
        if(!url.startsWith("jdbc:h2:mem:") && !url.matches("jdbc:mysql://[^/]+/[a-zA-Z0-9_]+_test(\\?.*)?"))
            throw new IllegalArgumentException("Tests require an isolated database whose name ends in _test.");
        System.setProperty("pawcare.db.url",url); System.setProperty("pawcare.db.user",System.getProperty("pawcare.testUser","sa")); System.setProperty("pawcare.db.password",System.getProperty("pawcare.testPassword",""));
        try(Connection c=com.pawcare.util.DBConnection.getInstance().getConnection(); Statement s=c.createStatement()) {
            if(url.startsWith("jdbc:h2:")) s.execute("DROP ALL OBJECTS");
            else for(String table:new String[]{"payments","appointments","pets","services","customers","users","system_locks"}) s.execute("DROP TABLE IF EXISTS "+table);
            String sql=Files.readString(Path.of("database.sql")).replaceAll("(?m)^CREATE DATABASE.*$","").replaceAll("(?m)^USE pet_grooming_db;","");
            if(url.startsWith("jdbc:h2:")) sql=sql.replace(" ENGINE=InnoDB","").replace("DATE_ADD(CURDATE(),INTERVAL 1 DAY)","DATEADD('DAY',1,CURDATE())");
            for(String part:sql.split(";")) if(!part.trim().isEmpty()) s.execute(part);
        }
    }
}

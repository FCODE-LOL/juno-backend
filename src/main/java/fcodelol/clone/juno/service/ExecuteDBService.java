package fcodelol.clone.juno.service;


import fcodelol.clone.juno.controller.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
public class ExecuteDBService {
    @Autowired
    DataSource dataSource;
    private static final String DATA_SCRIPT = "sql/data.sql";
    private static final String CLEAR_DATA_SCRIPT = "sql/clear.sql";
    private static final String INIT_DB_SCRIPT = "sql/juno.sql";
    private static final String TURN_OFF_SECURITY_SCRIPT = "sql/turnOnSecurity.sql";
    private static final String TURN_ON_SECURITY_SCRIPT = "sql/turnOffSecurity.sql";
    @Transactional
    public void runScriptFile(String filename) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource(filename));
        resourceDatabasePopulator.execute(dataSource);
    }

    @Transactional
    public Response<String> loadDBData() {
        runScriptFile(DATA_SCRIPT);
        return new Response<>(200, "Load DB data success");
    }

    @Transactional
    public Response<String> clearDBData() {
        runScriptFile(CLEAR_DATA_SCRIPT);
        return new Response<>(200, "Clear data in DB");
    }

    @Transactional
    public Response<String> initDB() {
        runScriptFile(INIT_DB_SCRIPT);
        return new Response<>(200, "Init DB success");
    }

    void turnOffDBSecurity() {
        runScriptFile(TURN_OFF_SECURITY_SCRIPT);
    }

    void turnOnSecurityScript() {
        runScriptFile(TURN_ON_SECURITY_SCRIPT);
    }
}

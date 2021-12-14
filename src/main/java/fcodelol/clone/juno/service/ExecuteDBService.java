package fcodelol.clone.juno.service;

import fcodelol.clone.juno.controller.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final String dataScript = "sql/data.sql";
    private static final String clearDataScript = "sql/clear.sql";
    private static final String initDBScript = "sql/juno.sql";
    private static final String turnOffSecurityScript = "sql/turnOnSecurity.sql";
    private static final String turnOnSecurityScript = "sql/turnOffSecurity.sql";
    private static final Logger logger = LogManager.getLogger(ProductService.class);

    @Transactional
    public void runScriptFile(String filename) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource(filename));
        resourceDatabasePopulator.execute(dataSource);
    }

    @Transactional
    public Response loadDBData() {
        runScriptFile(dataScript);
        return new Response(200, "Load DB data success");
    }

    @Transactional
    public Response clearDBData() {
        runScriptFile(clearDataScript);
        return new Response(200, "Clear data in DB");
    }

    @Transactional
    public Response initDB() {
        runScriptFile(initDBScript);
        return new Response(200, "Init DB success");
    }

    void turnOffDBSecurity() {
        runScriptFile(turnOffSecurityScript);
    }

    void turnOnSecurityScript() {
        runScriptFile(turnOnSecurityScript);
    }
}

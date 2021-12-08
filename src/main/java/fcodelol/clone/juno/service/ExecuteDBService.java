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
        try {
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource(filename));
            resourceDatabasePopulator.execute(dataSource);
        } catch (Exception e) {
            logger.error("Run file " + filename + "exception: " + e.getMessage());
        }
    }

    @Transactional
    public Response loadDBData() {
        try {
            runScriptFile(dataScript);
            return new Response(200,"Load DB data success");
        } catch (Exception e) {
            logger.error("Load data in DB: " + e.getMessage());
            return new Response(500,e.getMessage());
        }
    }

    @Transactional
    public Response clearDBData() {
        try {
            runScriptFile(clearDataScript);
            return new Response(200,"Clear data in DB");
        } catch (Exception e) {
            logger.error("Clear data in DB: " + e.getMessage());
            return new Response(500,e.getMessage());
        }
    }

    @Transactional
    public Response initDB() {
        try {
            runScriptFile(initDBScript);
            return new Response(200,"Init DB success");
        } catch (Exception e) {
            logger.error("Init DB: " + e.getMessage());
            return new Response(500,e.getMessage());
        }
    }

    void turnOffDBSecurity(){
        runScriptFile(turnOffSecurityScript);
    }

    void turnOnSecurityScript(){
        runScriptFile(turnOnSecurityScript);
    }
}

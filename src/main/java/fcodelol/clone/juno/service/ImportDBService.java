package fcodelol.clone.juno.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
public class ImportDBService {
    @Autowired
    DataSource dataSource;
    private static final String dataScript = "sql/data.sql";
    private static final String clearDataScript = "sql/clear.sql";
    private static final Logger logger = LogManager.getLogger(ProductService.class);

    public String loadDBData() {
        try {
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource(dataScript));
            resourceDatabasePopulator.execute(dataSource);
            return "Load DB data success";
        } catch (Exception e) {
            logger.error("Load data in DB: " + e.getMessage());
            return e.getMessage();

        }
    }

    @Transactional
    public String clearDBData() {
        try {
            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource(clearDataScript));
            resourceDatabasePopulator.execute(dataSource);
            return "Clear data in DB";
        }
        catch (Exception e){
            logger.error("Clear data in DB: " + e.getMessage());
            return e.getMessage();
        }
    }
}

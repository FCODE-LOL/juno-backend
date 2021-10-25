package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.service.ExecuteDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImportDBController {
    @Autowired
    ExecuteDBService executeDBService;

    @PostMapping(value = "/import")
    public String importDB() {
        return executeDBService.loadDBData();
    }

    @DeleteMapping(value = "/clear")
    public String clearDB() {
        return executeDBService.clearDBData();
    }

    @PostMapping(value = "/init")
    public String initDB() {
        return executeDBService.initDB();
    }
}

package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.service.ImportDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImportDBController {
    @Autowired
    ImportDBService importDBService;

    @PostMapping(value = "/import")
    public String importDB() {
        return importDBService.loadDBData();
    }

    @DeleteMapping(value = "/clear")
    public String clearDB() {
        return importDBService.clearDBData();
    }

    @PostMapping(value = "/init")
    public String initDB() {
        return importDBService.initDB();
    }
}

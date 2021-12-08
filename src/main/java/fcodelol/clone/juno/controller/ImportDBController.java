package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.controller.response.Response;
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
    public Response importDB() {
        return executeDBService.loadDBData();
    }

    @DeleteMapping(value = "/clear")
    public Response clearDB() {
        return executeDBService.clearDBData();
    }

    @PostMapping(value = "/init")
    public Response initDB() {
        return executeDBService.initDB();
    }
}

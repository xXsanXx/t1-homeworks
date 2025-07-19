package com.synthetic_human.bishop_prototype;

import com.synthetic_human.core_starter.command.Command;
import com.synthetic_human.core_starter.command.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands")
public class BishopPrototypeController {

    @Autowired
    private CommandService commandService;

    @PostMapping("/add_command")
    public ResponseEntity<?> addCommand(@RequestBody Command command) {
        commandService.add(command);
        return ResponseEntity.ok("The command is added");
    }

}

package com.example.tea_leaves_project.controller;

import com.example.tea_leaves_project.dto.TypeTeaDto;
import com.example.tea_leaves_project.service.TypeteaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/typetea")
public class TypeteaController {
    @Autowired
    TypeteaService typeteaService;
    @GetMapping("/alltype")
    public ResponseEntity<?> alltype() {
        return new ResponseEntity<>(typeteaService.getALlTypeTea(), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletetypetea(WebRequest webRequest,@PathVariable int id) {
        return new ResponseEntity<>(typeteaService.deleteTypeTea(id), HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<?> addtypetea(WebRequest webRequest,@RequestBody TypeTeaDto typeTeaDto){
        return new ResponseEntity<>(typeteaService.addTypeTea(typeTeaDto), HttpStatus.OK);
    }
}

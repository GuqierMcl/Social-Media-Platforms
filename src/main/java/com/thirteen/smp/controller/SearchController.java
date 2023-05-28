package com.thirteen.smp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {


    @RequestMapping(method = RequestMethod.GET)
    public Object globalSearch(String query, String type){
        return null;
    }
}

package com.linkedin.backend.user.controllers;

import com.linkedin.backend.dto.ProfileDTO;
import com.linkedin.backend.dto.SearchResultDTO;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.utils.JSONReturn;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/network")
public class NetworkController {
    private final AppUserService appUserService;

    public NetworkController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/search")
    public List<SearchResultDTO> searchUsers(@Valid @RequestBody JSONReturn<String> searchTerms) {
        String[] terms = searchTerms.getPayload().split(" ", -2);

        if (terms.length == 0)
            return Collections.emptyList();
        
        String firstTerm = terms[0].trim().toLowerCase();

        String secondTerm = "________________________________";
        if (terms.length == 2)
            secondTerm = terms[1].trim().toLowerCase();

        return appUserService.getSearchResults(firstTerm, secondTerm)
                             .stream()
                             .map(SearchResultDTO::new)
                             .collect(Collectors.toList());
    }
}

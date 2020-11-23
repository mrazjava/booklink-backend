package com.github.mrazjava.booklink.service;

import com.github.mrazjava.booklink.rest.depot.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepotService {

    @Autowired
    private AuthorApi authorApi;

    @Autowired
    private WorkApi workApi;

    @Autowired
    private EditionApi editionApi;

    @Autowired
    private StatisticsApi statsApi;


    public Optional<DepotAuthor> findAuthorById(String id) {
        return Optional.ofNullable(authorApi.findByIdUsingGET(id));
    }

    public Optional<DepotAuthor> randomAuthorWithImage() {
        return authorApi.randomWithImageUsingGET(1).stream().findFirst();
    }

    public List<DepotAuthor> searchAuthors(String text) {
        return authorApi.searchTextUsingGET(text, false, null);
    }

    public Optional<DepotWork> findWorkById(String id) {
        return Optional.ofNullable(workApi.findByIdUsingGET2(id));
    }

    public List<DepotWork> findWorksByAuthorId(String authorId) {
        return workApi.findByKeyUsingGET1(authorId);
    }

    public Optional<DepotWork> randomWorkWithImage() {
        return workApi.randomWithImageUsingGET2(1).stream().findFirst();
    }

    public List<DepotWork> searchWorks(String text) {
        return workApi.searchTextUsingGET2(text, false, null);
    }

    public Optional<DepotEdition> findEditionById(String id) {
        return Optional.ofNullable(editionApi.findByIdUsingGET1(id));
    }

    public List<DepotEdition> findByAuthorId(String authorId) {
        return editionApi.findByKeyUsingGET(authorId);
    }

    public Optional<DepotEdition> randomEditionWithImage() {
        return editionApi.randomWithImageUsingGET1(1).stream().findFirst();
    }

    public List<DepotEdition> searchEditions(String text) {
        return editionApi.searchTextUsingGET1(text, false, null);
    }

    public DepotStats getCounts() {
        return statsApi.getCountsUsingGET();
    }
}

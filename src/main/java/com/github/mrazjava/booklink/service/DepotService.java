package com.github.mrazjava.booklink.service;

import com.github.mrazjava.booklink.rest.depot.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

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


    public Optional<DepotAuthor> findAuthorById(String id, Boolean imgS, Boolean imgM, Boolean imgL) {
        return ofNullable(authorApi.findByIdUsingGET(id, imgS, imgM, imgL));
    }

    public List<DepotAuthor> randomAuthorWithImage(Integer count) {
        return authorApi.randomRecordUsingGET(
                ofNullable(count).orElse(1), null, true, null, null);
    }

    public List<DepotAuthor> searchAuthors(String text) {
        return authorApi.searchTextUsingGET(text, false, null);
    }

    public Optional<DepotWork> findWorkById(String id, Boolean imgS, Boolean imgM, Boolean imgL) {
        return ofNullable(workApi.findByIdUsingGET4(id, imgS, imgM, imgL));
    }

    public List<DepotWork> findWorksByAuthorId(String authorId) {
        return workApi.findByKeyUsingGET1(authorId);
    }

    public List<DepotWork> randomWorkWithImage(Integer count) {
        return workApi.randomRecordUsingGET2(
        		ofNullable(count).orElse(1), null, true, null, null);
    }

    public List<DepotWork> searchWorks(String text) {
        return workApi.searchTextUsingGET2(text, false, null);
    }

    public Optional<DepotEdition> findEditionById(String id, Boolean imgS, Boolean imgM, Boolean imgL) {
        return ofNullable(editionApi.findByIdUsingGET2(id, imgS, imgM, imgL));
    }

    public List<DepotEdition> findByAuthorId(String authorId) {
        return editionApi.findByKeyUsingGET(authorId);
    }

    public Optional<DepotEdition> randomEditionWithImage() {
        return editionApi.randomRecordUsingGET1(1, null, true, null, null)
                .stream().findFirst();
    }

    public List<DepotEdition> searchEditions(String text) {
        return editionApi.searchTextUsingGET1(text, false, null);
    }
    
    public List<DepotAuthor> getAuthorsPaged(Integer pageNo, Integer pageSize, Boolean imgS, Boolean imgM, Boolean imgL) {
    	return authorApi.getAllUsingGET(pageNo, pageSize, imgS, imgM, imgL);
    }
    
    public List<DepotWork> getWorksPaged(Integer pageNo, Integer pageSize, Boolean imgS, Boolean imgM, Boolean imgL) {
    	return workApi.getAllUsingGET2(pageNo, pageSize, imgS, imgM, imgL);
    }
    
    public List<DepotEdition> getEditionsPaged(Integer pageNo, Integer pageSize, Boolean imgS, Boolean imgM, Boolean imgL) {
    	return editionApi.getAllUsingGET1(pageNo, pageSize, imgS, imgM, imgL);
    }

    public DepotStats getCounts() {
        return statsApi.getCountsUsingGET();
    }
}

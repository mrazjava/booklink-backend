package com.github.mrazjava.booklink.service;

import static java.util.Optional.ofNullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.mrazjava.booklink.rest.depot.AuthorApi;
import com.github.mrazjava.booklink.rest.depot.DepotAuthor;
import com.github.mrazjava.booklink.rest.depot.DepotEdition;
import com.github.mrazjava.booklink.rest.depot.DepotStats;
import com.github.mrazjava.booklink.rest.depot.DepotWork;
import com.github.mrazjava.booklink.rest.depot.EditionApi;
import com.github.mrazjava.booklink.rest.depot.StatisticsApi;
import com.github.mrazjava.booklink.rest.depot.WorkApi;
import com.github.mrazjava.booklink.rest.model.depot.WorkResponse;

@Service
public class DepotService {

    @Inject
    private Logger log;
    
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
        return authorApi.searchTextUsingGET(text, false, null, true, false, false);
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

    public List<WorkResponse> searchWorks(String text) {
        
        List<DepotWork> works = workApi.searchTextUsingGET2(text, false, null, false, false, false);
        String authorIds = works.stream()
                .map(DepotWork::getAuthors)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.joining(","));
        
        log.info("found {} work(s); fetching related authors [{}]", works.size(), authorIds);
        
        Map<String, DepotAuthor> authors = authorApi.findAllByIdUsingGET(authorIds, false, false, false)
            .stream()
            .collect(Collectors.toMap(DepotAuthor::getId, Function.identity()));

        return works.stream()
                .map(work -> {
                    List<DepotAuthor> wkAuthors = work.getAuthors().stream()
                            .map(a -> authors.get(a))
                            .filter(a -> a != null)
                            .collect(Collectors.toList());
                    return new WorkResponse(work, wkAuthors);
                })
                .collect(Collectors.toList());
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
        return editionApi.searchTextUsingGET1(text, false, null, true, false, false);
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

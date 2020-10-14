package com.github.mrazjava.booklink.service;

import com.github.mrazjava.booklink.rest.depot.ApiException;
import com.github.mrazjava.booklink.rest.depot.DepotAuthor;
import com.github.mrazjava.booklink.rest.depot.DepotAuthorApi;
import com.github.mrazjava.booklink.util.WordGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author AZ
 */
@Component
public class ProofOfConceptService {

    @Inject
    private Logger log;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private WordGenerator wordGenerator;

    @Inject
    private DepotAuthorApi depotAuthorApi;


    public int randomCount() {
        int count = new Random().nextInt();
        String uuid = UUID.randomUUID().toString();
        String now = OffsetDateTime.now().toString();
        log.debug("computed count: {} | {} - {}", count, uuid, now);
        return count;
    }

    public Set<String> randomWords() {
        int wordCount = ThreadLocalRandom.current().nextInt(1, 11);
        Set<String> words = new HashSet<>();
        for(int x=0; x<wordCount; x++) {
            words.add(wordGenerator.newWord(ThreadLocalRandom.current().nextInt(3, 11)));
        }
        return words;
    }

    public String randomAlpha() {
        return RandomStringUtils.randomAlphabetic(15);
    }

    public String randomAlphanumeric() {
        return RandomStringUtils.randomAlphanumeric(ThreadLocalRandom.current().nextInt(2, 21));
    }

    public String sayHello(Authentication auth) {
        String[] greetings = {"Howdy %s, it's ", "Hello %s. Time is ", "Wuzzaaaaa %s! my time is ", "What's up %s. Time is ", "How do you do, %s? It's now ", "%s, time is: "};
        int greetingIndx = new Random().nextInt((5 - 0) + 1) + 0;
        return String.format(greetings[greetingIndx], shuffle(auth.getName())) + new Date();
    }

    public DepotAuthor depotFindAuthor(String id) throws ApiException {
        return depotAuthorApi.findByIdUsingGET(id);
    }

    private String shuffle(String input){

        if(System.currentTimeMillis() % 2 == 0) {
            return input;
        }

        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }
    public String getEncodedPassword(String plainText) {
        return passwordEncoder.encode(plainText);
    }
}

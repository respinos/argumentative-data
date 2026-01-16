package org.example.argumentativedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@ConditionalOnProperty(name = "runVersionIncrementer", havingValue = "true")
public class PossibleObjectVersionIncrementer implements ApplicationRunner {
    private final PossibleObjectRepository repository;

    @Autowired
    public PossibleObjectVersionIncrementer(PossibleObjectRepository repository) {
        this.repository = repository;
    }

    public void run(int sampleSize, int iterationCount) {
        Random random = new Random();
        List<PossibleObject> rootObjects = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(obj -> obj.getParentId() == null)
                .collect(Collectors.toList());
        System.out.printf("-- incrementing %d over %d iterations\n", rootObjects.size(), iterationCount);
        for (int i = 0; i < iterationCount; i++) {
            Collections.shuffle(rootObjects, random);
            List<PossibleObject> sample = rootObjects.stream().limit(sampleSize).collect(Collectors.toList());
            for (PossibleObject root : sample) {
                incrementVersionRecursive(root);
            }
        }
    }

    private void incrementVersionRecursive(PossibleObject obj) {
        obj.setVersionNumber(obj.getVersionNumber() + 1);
        repository.save(obj);
        List<PossibleObject> children = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(child -> obj.getId().equals(child.getParentId()))
                .collect(Collectors.toList());
        for (PossibleObject child : children) {
            incrementVersionRecursive(child);
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        List<String> nonOptionArgs = args.getNonOptionArgs();
        if (nonOptionArgs.size() != 2) {
            System.err.println("Usage: --runVersionIncrementer <sampleSize> <iterationCount>");
            return;
        }
        int sampleSize = Integer.parseInt(nonOptionArgs.get(0));
        int iterationCount = Integer.parseInt(nonOptionArgs.get(1));
        run(sampleSize, iterationCount);
    }
}

package org.example.argumentativedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class PossibleObjectTsvImporter implements CommandLineRunner {
    private final PossibleObjectRepository repository;

    @Autowired
    public PossibleObjectTsvImporter(PossibleObjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        String filePath = "tmp/output.tsv";
        Map<String, Integer> identifierToId = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t", -1);
                if (fields.length < 5) continue;
                String binIdentifier = fields[0];
                String identifier = fields[1];
                String parentIdentifiers = fields[2];
                String type = fields[3];
                String revisionNumberStr = fields[4];
                Integer versionNumber = 1;
                try {
                    versionNumber = Integer.parseInt(revisionNumberStr);
                } catch (NumberFormatException ignored) {}
                Integer parentId = null;
                if (parentIdentifiers != null && !parentIdentifiers.isBlank()) {
                    parentId = identifierToId.get(parentIdentifiers);
                }
                PossibleObject obj = new PossibleObject(null, parentId, identifier, type, versionNumber, binIdentifier);
                obj = repository.save(obj);
                identifierToId.put(identifier, obj.getId());
            }
        } catch (IOException e) {
            System.err.println("Error reading TSV file: " + e.getMessage());
        }
    }
}

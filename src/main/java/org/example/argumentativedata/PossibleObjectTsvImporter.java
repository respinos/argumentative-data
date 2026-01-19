package org.example.argumentativedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class PossibleObjectTsvImporter implements CommandLineRunner {
    private final PossibleObjectRepository repository;
    private final ObjectFileRepository fileRepository;

    @Autowired
    public PossibleObjectTsvImporter(PossibleObjectRepository repository, ObjectFileRepository fileRepository) {
        this.repository = repository;
        this.fileRepository = fileRepository;
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

        filePath = "tmp/files.tsv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String header = reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t", -1);
                if (fields.length < 5) continue;
                String object_dentifier = fields[0];
                String identifier = fields[1];
                String fileFormat = fields[2];
                String fileFunction = fields[3];
                String sizeStr = fields[4];
                Integer size = 0;
                try {
                    size = Integer.parseInt(sizeStr);
                } catch (NumberFormatException ignored) {}
                String digestHex = fields[5];
                byte[] digest = null;
                if (digestHex != null && !digestHex.isBlank()) {
                    int len = digestHex.length();
                    digest = new byte[len / 2];
                    for (int i = 0; i < len; i += 2) {
                        digest[i / 2] = (byte) ((Character.digit(digestHex.charAt(i), 16) << 4)
                                + Character.digit(digestHex.charAt(i+1), 16));
                    }
                }
                String versionNumberStr = fields[6];
                Integer versionNumber = 1;
                try {
                    versionNumber = Integer.parseInt(versionNumberStr);
                } catch (NumberFormatException ignored) {}
                String lastFixityCheckStr = fields[7];
                LocalDateTime lastFixityCheck = null;
                if (lastFixityCheckStr != null && !lastFixityCheckStr.isBlank()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
                    lastFixityCheck = LocalDateTime.parse(lastFixityCheckStr, formatter);
                }
                Integer possibleObjectId = identifierToId.get(object_dentifier);
                ObjectFile objFile = new ObjectFile(null, identifier, fileFormat, fileFunction, size, digest, versionNumber, lastFixityCheck, possibleObjectId);
                objFile = fileRepository.save(objFile);
            }
        } catch (IOException e) {
            System.err.println("Error reading TSV file: " + e.getMessage());
        }
    }
}

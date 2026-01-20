package org.example.argumentativedata;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("object_files")
public class ObjectFile {
    @Id
    private Integer id;
    private String identifier;
    private String fileFormat;
    private String fileFunction;
    private Integer size;
    private byte[] digest;
    private Integer versionNumber;
    private LocalDateTime lastFixityCheck;

    // No-argument constructor (for Spring Data JDBC)
    public ObjectFile() {}

    // All-arguments constructor (for manual instantiation)
    public ObjectFile(Integer id, String identifier, String fileFormat, String fileFunction, Integer size, byte[] digest, Integer versionNumber, LocalDateTime lastFixityCheck) {
        this.id = id;
        this.identifier = identifier;
        this.fileFormat = fileFormat;
        this.fileFunction = fileFunction;
        this.size = size;
        this.digest = digest;
        this.versionNumber = versionNumber;
        this.lastFixityCheck = lastFixityCheck;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getFileFormat() { return fileFormat; }
    public void setFileFormat(String fileFormat) { this.fileFormat = fileFormat; }
    public String getFileFunction() { return fileFunction; }
    public void setFileFunction(String fileFunction) { this.fileFunction = fileFunction; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    public byte[] getDigest() { return digest; }
    public void setDigest(byte[] digest) { this.digest = digest; }
    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }
    public LocalDateTime getLastFixityCheck() { return lastFixityCheck; }
    public void setLastFixityCheck(LocalDateTime lastFixityCheck) { this.lastFixityCheck = lastFixityCheck; }
}

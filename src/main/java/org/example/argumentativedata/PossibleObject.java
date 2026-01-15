package org.example.argumentativedata;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.lang.Nullable;

@Table("possible_objects")
public class PossibleObject {
    @Id
    private Integer id;

    @Column("parent_id")
    @Nullable
    private Integer parentId;

    private String identifier;
    private String type;
    private Integer versionNumber = 1;

    public PossibleObject() {}

    public PossibleObject(Integer id, Integer parentId, String identifier, String type, Integer versionNumber) {
        this.id = id;
        this.parentId = parentId;
        this.identifier = identifier;
        this.type = type;
        this.versionNumber = versionNumber;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }
}

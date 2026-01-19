package org.example.argumentativedata;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ObjectFileRepository extends CrudRepository<ObjectFile, Integer> {
    List<ObjectFile> findByPossibleObjectId(Integer possibleObjectId);
}

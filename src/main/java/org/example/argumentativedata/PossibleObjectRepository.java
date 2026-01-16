package org.example.argumentativedata;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PossibleObjectRepository extends CrudRepository<PossibleObject, Integer> {
    List<PossibleObject> findByParentId(Integer parentId);
    // Additional query methods can be defined here if needed
}

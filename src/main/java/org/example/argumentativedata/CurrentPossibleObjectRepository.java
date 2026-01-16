package org.example.argumentativedata;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CurrentPossibleObjectRepository extends CrudRepository<PossibleObject, Integer> {
    List<PossibleObject> findAll();
    // You can add more query methods as needed
}

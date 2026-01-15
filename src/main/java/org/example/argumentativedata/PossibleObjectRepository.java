package org.example.argumentativedata;

import org.springframework.data.repository.CrudRepository;

public interface PossibleObjectRepository extends CrudRepository<PossibleObject, Integer> {
    // Additional query methods can be defined here if needed
}

package org.example.argumentativedata;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ObjectFileRepository extends CrudRepository<ObjectFile, Integer> {
    List<ObjectFile> findByPossibleObjectId(Integer possibleObjectId);

    @Query("SELECT COALESCE(SUM(size), 0) FROM object_files WHERE possible_object_id = :possibleObjectId")
    long sumSizeByPossibleObjectId(Integer possibleObjectId);

    @Query("WITH RECURSIVE descendants AS (\n" +
           "  SELECT id FROM possible_objects WHERE id = :rootId\n" +
           "  UNION ALL\n" +
           "  SELECT po.id FROM possible_objects po\n" +
           "  INNER JOIN descendants d ON po.parent_id = d.id\n" +
           ")\n" +
           "SELECT COALESCE(SUM(of.size), 0)\n" +
           "FROM object_files of\n" +
           "WHERE of.possible_object_id IN (SELECT id FROM descendants)")
    long sumSizeIncludingDescendants(Integer rootId);
}

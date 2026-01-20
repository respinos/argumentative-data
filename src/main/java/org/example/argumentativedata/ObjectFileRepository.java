package org.example.argumentativedata;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

// NOTE - copilot initially created ObjectFile as a separate
// repository; this became unnecessary when PossibleObject became
// an aggregate root (probably the wrong way of saying that).
// The original queries expected to find ObjectFile based on a
// PossibleObject.id BUT the aggregate pattern obscures that foreign
// key in the ObjectFile (or something: copilot was very confused by aggregates)

public interface ObjectFileRepository extends CrudRepository<ObjectFile, Integer> {
//    List<ObjectFile> findByPossibleObjectsKey(Integer possibleObjectsKey);

//    @Query("SELECT COALESCE(SUM(size), 0) FROM object_files WHERE possible_objects_key = :possibleObjectsKey")
//    long sumSizeByPossibleObjectId(Integer possibleObjectId);
//
//    @Query("WITH RECURSIVE descendants AS (\n" +
//           "  SELECT id FROM possible_objects WHERE id = :rootId\n" +
//           "  UNION ALL\n" +
//           "  SELECT po.id FROM possible_objects po\n" +
//           "  INNER JOIN descendants d ON po.parent_id = d.id\n" +
//           ")\n" +
//           "SELECT COALESCE(SUM(of.size), 0)\n" +
//           "FROM object_files of\n" +
//           "WHERE of.possible_objects_key IN (SELECT id FROM descendants)")
//    long sumSizeIncludingDescendants(Integer rootId);
}

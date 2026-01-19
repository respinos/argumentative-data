package org.example.argumentativedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class PossibleObjectController {
    private final PossibleObjectRepository repository;
    private final ObjectFileRepository objectFileRepository;

    @Autowired
    public PossibleObjectController(PossibleObjectRepository repository, ObjectFileRepository objectFileRepository) {
        this.repository = repository;
        this.objectFileRepository = objectFileRepository;
    }

    @GetMapping("/possible-object")
    public String getPossibleObjectById(@RequestParam("id") Integer id, Model model) {
        Optional<PossibleObject> possibleObject = repository.findById(id);
        model.addAttribute("possibleObject", possibleObject.orElse(null));

        var possibleObjectFileSize = objectFileRepository.sumSizeByPossibleObjectId(possibleObject.get().getId());
        model.addAttribute("possibleObjectFileSize", possibleObjectFileSize);

        List<PossibleObject> childPossibleObjects = possibleObject.isPresent()
            ? StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(obj -> id.equals(obj.getParentId()))
                .collect(Collectors.toList())
            : List.of();
        model.addAttribute("childPossibleObjects", childPossibleObjects);

        Map<Integer, Long> childObjectFileSizes = childPossibleObjects.stream()
                .collect(Collectors.toMap(
                        PossibleObject::getId,
                        obj -> objectFileRepository.sumSizeIncludingDescendants(obj.getId())
                ));

        model.addAttribute("childObjectFileSizes", childObjectFileSizes);

        return "possible_object";
    }
}

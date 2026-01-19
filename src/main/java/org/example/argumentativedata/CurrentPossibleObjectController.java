package org.example.argumentativedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class CurrentPossibleObjectController {
    private final CurrentPossibleObjectRepository repository;
    private final ObjectFileRepository objectFileRepository;

    @Autowired
    public CurrentPossibleObjectController(CurrentPossibleObjectRepository repository, ObjectFileRepository objectFileRepository) {
        this.repository = repository;
        this.objectFileRepository = objectFileRepository;
    }

    @GetMapping("/current-possible-objects")
    public String listCurrentPossibleObjects(Model model,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        // Only fetch root objects (parent_id == null) for paging and reporting
        List<PossibleObject> rootObjects = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(obj -> obj.getParentId() == null)
                .collect(Collectors.toList());
        int start = Math.min(page * size, rootObjects.size());
        int end = Math.min((page + 1) * size, rootObjects.size());
        List<PossibleObject> pageContent = rootObjects.subList(start, end);
        Page<PossibleObject> pageResult = new PageImpl<>(pageContent, PageRequest.of(page, size), rootObjects.size());

        // Aggregate file sizes for each root PossibleObject only (no descendants)
        Map<Integer, Long> objectFileSizes = pageContent.stream()
                .collect(Collectors.toMap(
                        PossibleObject::getId,
                        obj -> objectFileRepository.sumSizeIncludingDescendants(obj.getId())
                ));
        model.addAttribute("page", pageResult);
        model.addAttribute("objectFileSizes", objectFileSizes);
        return "current_possible_objects";
    }
}

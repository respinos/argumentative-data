package org.example.argumentativedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class CurrentPossibleObjectController {
    private final CurrentPossibleObjectRepository repository;

    @Autowired
    public CurrentPossibleObjectController(CurrentPossibleObjectRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/current-possible-objects")
    public String listCurrentPossibleObjects(Model model,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {
        List<PossibleObject> allObjects = StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(obj -> obj.getParentId() == null)
                .collect(Collectors.toList());
        int start = Math.min(page * size, allObjects.size());
        int end = Math.min((page + 1) * size, allObjects.size());
        List<PossibleObject> pageContent = allObjects.subList(start, end);
        Page<PossibleObject> pageResult = new PageImpl<>(pageContent, PageRequest.of(page, size), allObjects.size());
        model.addAttribute("page", pageResult);
        return "current_possible_objects";
    }
}

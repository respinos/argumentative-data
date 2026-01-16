package org.example.argumentativedata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class PossibleObjectController {
    private final PossibleObjectRepository repository;

    @Autowired
    public PossibleObjectController(PossibleObjectRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/possible-object")
    public String getPossibleObjectById(@RequestParam("id") Integer id, Model model) {
        Optional<PossibleObject> possibleObject = repository.findById(id);
        model.addAttribute("possibleObject", possibleObject.orElse(null));
        List<PossibleObject> childPossibleObjects = possibleObject.isPresent()
            ? StreamSupport.stream(repository.findAll().spliterator(), false)
                .filter(obj -> id.equals(obj.getParentId()))
                .collect(Collectors.toList())
            : List.of();
        model.addAttribute("childPossibleObjects", childPossibleObjects);
        return "possible_object";
    }
}

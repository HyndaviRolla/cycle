 package com.prodapt.practice.controller;
 
import java.util.List;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 
 
import com.prodapt.practice.entity.Cycle;
import com.prodapt.practice.repository.CycleRepository;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
 
@Controller
public class CycleController {
    @Autowired
    private CycleRepository cycleRepository;

    @GetMapping("/cycles")
    public String listCycles(Model model) {
        List<Cycle> cycles = cycleRepository.findAll();
        model.addAttribute("cycles", cycles);
        return "cycles";
    }
 
    @PostMapping("/cycles/add")
    public String addCycle(@ModelAttribute @Valid Cycle cycle, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {   
            return "redirect:/cycles";
        }   
        Cycle existingCycle = cycleRepository.findByName(cycle.getName());
        if (existingCycle != null) {   
            return "redirect:/cycles";
        }

        cycleRepository.save(cycle);
        return "redirect:/cycles";
    }
    @GetMapping("/cycles/return/{id}")
    public String returnCycle(@PathVariable Long id) throws NotFoundException {
        Cycle cycle = cycleRepository.findById(id).orElseThrow(() -> new NotFoundException());
      
		if (cycle.getStock() < 10 ) {
            cycle.setStock(cycle.getStock() + 1);  
            cycleRepository.save(cycle);
        }
        return "redirect:/cycles";
    }

    @PostMapping("/cycles/borrow/{id}")
    public String borrowCycle(@PathVariable Long id) throws NotFoundException {
        Cycle cycle = cycleRepository.findById(id).orElseThrow(() -> new NotFoundException());
        if (cycle.getStock() > 0) {
            cycle.setStock(cycle.getStock() - 1);
            cycleRepository.save(cycle);
        }
        return "redirect:/cycles";
    }
}


    


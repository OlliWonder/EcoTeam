package com.sber.java13.ecoteam.controller;

import com.sber.java13.ecoteam.dto.AddWasteToPointDTO;
import com.sber.java13.ecoteam.dto.WasteDTO;
import com.sber.java13.ecoteam.dto.WasteWithPointsDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.service.WasteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import static com.sber.java13.ecoteam.constants.UserRolesConstants.ADMIN;

@Controller
@RequestMapping("/wastes")
@Slf4j
public class WasteController {
    private final WasteService wasteService;
    
    public WasteController(WasteService wasteService) {
        this.wasteService = wasteService;
    }
    
    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         @ModelAttribute(name = "exception") final String exception,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        Page<WasteWithPointsDTO> result;
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ADMIN.equalsIgnoreCase(userName)) {
            result = wasteService.getAllWastesWithPoints(pageRequest);
        }
        else {
            result = wasteService.getAllNotDeletedWastesWithPoints(pageRequest);
        }
        model.addAttribute("wastes", result);
        model.addAttribute("exception", exception);
        return "wastes/viewAllWastes";
    }
    
    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("waste", wasteService.getWasteWithPoints(id));
        return "wastes/viewWaste";
    }
    
    @GetMapping("/add")
    public String create() {
        return "wastes/addWaste";
    }
    
    @PostMapping("/add")
    public String create(@ModelAttribute("wasteForm") WasteDTO wasteDTO) {
        wasteService.create(wasteDTO);
        return "redirect:/wastes";
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("waste", wasteService.getOne(id));
        return "wastes/updateWaste";
    }
    
    @PostMapping("/update")
    public String update(@ModelAttribute("bookForm") WasteDTO wasteDTO) {
        wasteService.update(wasteDTO);
        return "redirect:/wastes";
    }
    
    @PostMapping("/search")
    public String searchWastes(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "5") int pageSize,
                               @ModelAttribute("wasteSearchForm") WasteDTO wasteDTO,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        model.addAttribute("wastes", wasteService.findWastes(wasteDTO, pageRequest));
        return "wastes/viewAllWastes";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        wasteService.delete(id);
        return "redirect:/wastes";
    }
    
    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        wasteService.restore(id);
        return "redirect:/wastes";
    }
    
    @ExceptionHandler({MyDeleteException.class, AccessDeniedException.class})
    public RedirectView handleError(HttpServletRequest req,
                                    Exception ex,
                                    RedirectAttributes redirectAttributes) {
        log.error("Запрос: " + req.getRequestURL() + " вызвал ошибку " + ex.getMessage());
        redirectAttributes.addFlashAttribute("exception", ex.getMessage());
        return new RedirectView("/books", true);
    }
}

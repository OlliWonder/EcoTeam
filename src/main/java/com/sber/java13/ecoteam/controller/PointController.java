package com.sber.java13.ecoteam.controller;

import com.sber.java13.ecoteam.dto.AddWasteToPointDTO;
import com.sber.java13.ecoteam.dto.PointDTO;
import com.sber.java13.ecoteam.dto.PointWithWastesDTO;
import com.sber.java13.ecoteam.exception.MyDeleteException;
import com.sber.java13.ecoteam.service.PointService;
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
@RequestMapping("/points")
@Slf4j
public class PointController {
    private final PointService pointService;
    private final WasteService wasteService;
    
    public PointController(PointService pointService, WasteService wasteService) {
        this.pointService = pointService;
        this.wasteService = wasteService;
    }
    
    @GetMapping("")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         @ModelAttribute(name = "exception") final String exception,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        Page<PointWithWastesDTO> result;
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ADMIN.equalsIgnoreCase(userName)) {
            result = pointService.getAllPointsWithWastes(pageRequest);
        }
        else {
            result = pointService.getAllNotDeletedPointsWithWastes(pageRequest);
        }
        model.addAttribute("points", result);
        model.addAttribute("exception", exception);
        return "points/viewAllPoints";
    }
    
    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("point", pointService.getPointWithWastes(id));
        log.info(4 + model.toString());
        return "points/viewPoint";
    }
    
    @GetMapping("/add")
    public String create() {
        return "points/addPoint";
    }
    
    @PostMapping("/add")
    public String create(@ModelAttribute("pointForm") PointDTO pointDTO) {
        pointService.create(pointDTO);
        return "redirect:/points";
    }
    
    @GetMapping("/add-waste/{pointId}")
    public String addWaste(@PathVariable Long pointId, Model model) {
        model.addAttribute("wastes", wasteService.listAll());
        model.addAttribute("pointId", pointId);
        model.addAttribute("point", pointService.getOne(pointId).getTitle());
        return "points/addPointWaste";
    }
    
    @PostMapping("/add-waste")
    public String addWaste(@ModelAttribute("pointWasteForm") AddWasteToPointDTO addWasteToPointDTO) {
        pointService.addWasteToPoint(addWasteToPointDTO);
        log.info("3 " + addWasteToPointDTO.toString());
        return "redirect:/points";
    }
    
    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("point", pointService.getOne(id));
        return "points/updatePoint";
    }
    
    @PostMapping("/update")
    public String update(@ModelAttribute("pointForm") PointDTO pointDTO) {
        pointService.update(pointDTO);
        return "redirect:/points";
    }
    
    @PostMapping("/search")
    public String searchPoints(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "5") int pageSize,
                               @ModelAttribute("pointSearchForm") PointDTO pointDTO,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        model.addAttribute("points", pointService.findPoints(pointDTO, pageRequest));
        return "points/viewAllPoints";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        pointService.delete(id);
        return "redirect:/points";
    }
    
    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        pointService.restore(id);
        return "redirect:/points";
    }
    
    @ExceptionHandler({MyDeleteException.class, AccessDeniedException.class})
    public RedirectView handleError(HttpServletRequest req,
                                    Exception ex,
                                    RedirectAttributes redirectAttributes) {
        log.error("Запрос: " + req.getRequestURL() + " вызвал ошибку " + ex.getMessage());
        redirectAttributes.addFlashAttribute("exception", ex.getMessage());
        return new RedirectView("/points", true);
    }
}

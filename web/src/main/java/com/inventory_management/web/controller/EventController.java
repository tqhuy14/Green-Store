package com.inventory_management.web.controller;

import com.inventory_management.web.dto.EventDto;
import com.inventory_management.web.dto.ProductTypeDto;
import com.inventory_management.web.entity.Event;
import com.inventory_management.web.security.AuthenticatedUserService;
import com.inventory_management.web.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EventController {

    EventService eventService;
    AuthenticatedUserService authenticatedUserService;

    @Autowired
    public EventController(EventService eventService, AuthenticatedUserService authenticatedUserService) {
        this.eventService = eventService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @GetMapping("/events")
    public String listAndSearchUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "7") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sort", required = false) String sort,
            Model model,
            @ModelAttribute("userFunctions") List<String> userFunctions,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra nếu người dùng không có quyền truy cập chức năng này
        if (!authenticatedUserService.hasFunctions("QLSK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền truy cập trang (Quản Lý Sự Kiện)!");
            return "redirect:/home";
        }
        // Đặt giá trị mặc định cho các trường tìm kiếm nếu chúng là null
        name = (name == null) ? "" : name;

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        // Sắp xếp theo tiêu chí
        if ("sale-asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("sale").ascending());
        } else if ("sale-desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("sale").descending());
        }

        // Tìm kiếm và phân trang
        Page<EventDto> eventsPage = eventService.searchEvents(name, pageable);
        List<EventDto> events = eventsPage.getContent();

        // Đưa dữ liệu vào model
        model.addAttribute("events", events);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventsPage.getTotalPages());
        model.addAttribute("pageSize", size);

        // Giữ lại giá trị tìm kiếm và sắp xếp
        model.addAttribute("name1", name);
        model.addAttribute("sort", sort);

        return "event/events-list";
    }


    @GetMapping("/events/new")
    public String createTypesFrom(Model model,
                                  @ModelAttribute("userFunctions") List<String> userFunctions,
                                  RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("TSK", "QLSK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền thêm sự kiện");
            return "redirect:/events";
        }

        EventDto eventDto = new EventDto();
        model.addAttribute("event", eventDto);
        return "event/event-create-form";
    }

    @PostMapping("/events/new")
    public String addEvent(@Valid @ModelAttribute("event") EventDto eventDto,
                          BindingResult result, Model model) {

        Event existingName = eventService.findByName1(eventDto.getName());
        if (existingName != null && existingName.getName() != null && !existingName.getName().isEmpty()) {
            result.rejectValue("name", "error.event", "(*) Đã có sự kiện này");
        }

        if (!result.hasErrors()) {
            eventService.saveEvent(eventDto);
            model.addAttribute("info1", "success");
        }
        model.addAttribute("event", eventDto);
        return "event/event-create-form";
    }

    @GetMapping("/events/{eventID}/delete")
    public String deleteType(@PathVariable Long eventID,
                             @ModelAttribute("userFunctions") List<String> userFunctions,
                             RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("XSK", "QLSK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa sự kiện!");
            return "redirect:/events";
        }

        try {
            eventService.delete(eventID);
            redirectAttributes.addFlashAttribute("message", "Xóa sự kiện thành công");
        } catch (Exception e) {
            // Thêm thông báo lỗi nếu xóa không thành công
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete type. Please try again.");
        }

        return "redirect:/events";
    }

    @PostMapping("/events/delete")
    public String deleteType(@RequestParam(value = "eventIds", required = false) List<Long> eventIds,
                             @ModelAttribute("userFunctions") List<String> userFunctions,
                             RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("XNSK", "QLSK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền xóa sự kiện");
            return "redirect:/events";
        }

        try {
            for (Long eventId : eventIds) {
                eventService.delete(eventId);
            }
            redirectAttributes.addFlashAttribute("message", "Xóa sự kiện thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete user. Please try again.");
        }
        return "redirect:/events";
    }


    @PostMapping("/events/{eventID}/edit")
    public String updateRole(@PathVariable("eventID") Long eventID,
                             @Valid @ModelAttribute("event") EventDto eventDto,
                             BindingResult result, Model model) {

        Event existingName = eventService.findByName1(eventDto.getName());
        if (existingName != null && !existingName.getId().equals(eventID)) {
            result.rejectValue("name", "error.type", "(*) Tên sự kiện đã tồn tại");
        }

        if (result.hasErrors()) {
            model.addAttribute("event", eventDto);
            model.addAttribute("eventID", eventID);
            return "event/event-edit-form";
        }else{
            eventService.updateEvent(eventDto);
            model.addAttribute("info1", "success");
            model.addAttribute("event", eventDto);
            return "event/event-edit-form";
        }
    }

    @GetMapping("/events/{eventID}/edit")
    public String showEditForm(@PathVariable("eventID") Long eventID, Model model,
                               @ModelAttribute("userFunctions") List<String> userFunctions,
                               RedirectAttributes redirectAttributes) {

        if (!authenticatedUserService.hasFunctions("CSSK", "QLSK")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền chỉnh sửa sự kiện!");
            return "redirect:/events";
        }

        EventDto eventDto = eventService.findByID(eventID);
        model.addAttribute("event", eventDto);
        model.addAttribute("eventID", eventID);
        return "event/event-edit-form";
    }
}

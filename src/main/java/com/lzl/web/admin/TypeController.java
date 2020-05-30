package com.lzl.web.admin;


import com.lzl.po.Type;
import com.lzl.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";

        }
   @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type", new Type());
        return "admin/typeEdit";
    }
    @GetMapping("/types/{id}/input")
    public String typeEditor(Model model, @PathVariable Long id){
        model.addAttribute("type", typeService.getType(id));
        return "admin/typeEdit";
    }

    @PostMapping("/types")
    public String save(@Valid Type type,  BindingResult result, RedirectAttributes attributes){
        Type type1= typeService.getTypeByName(type.getName());
        if (type1!=null){
            result.rejectValue("name","nameError", "this name already exist");
        }
        if (result.hasErrors()){
            return"admin/typeEdit";
        }
        Type typ = typeService.saveType(type);

        if (typ == null){
            attributes.addFlashAttribute("message", "operation fails");
        }else {
            attributes.addFlashAttribute("message", "operation success");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String save(@Valid Type type,  BindingResult result, Long id,RedirectAttributes attributes){
        Type type2= typeService.getTypeByName(type.getName());
        if (type2!=null){
            result.rejectValue("name","nameError", "this name already exist");
        }
        if (result.hasErrors()){
            return"admin/typeEdit";
        }
        Type typ = typeService.updateType(id,type);

        if (typ == null){
            attributes.addFlashAttribute("message", "operation fails");
        }else {
            attributes.addFlashAttribute("message", "operation success");
        }
        return "redirect:/admin/types";
    }
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "delete success");
        return "redirect:/admin/types";
    }
}

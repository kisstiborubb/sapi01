package ro.sapientia2015.story.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.SearchDTO;
import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.WorkDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.Work;
import ro.sapientia2015.story.service.SprintService;
import ro.sapientia2015.story.service.WorkService;

@Controller
public class WorkController {
	@Resource
	private SprintService sprintService;
	
	@Resource
	private WorkService workService;
	
	public static final String VIEW_LIST = "work/list";
	public static final String VIEW_ADD = "work/add";
	public static final String VIEW_UPDATE = "work/update";
	public static final String VIEW_SEARCH = "work/search";
	public static final String VIEW_RESULT = "work/result";

	private static final String MODEL_ATTRIBUTE = "work";
    
	@RequestMapping(value = "/work/list/{id}", method = RequestMethod.GET)
	public String listWorksForSprint(@PathVariable("id") Long id, Model model) throws NotFoundException {
		Sprint sprint = sprintService.findById (id);
		
		model.addAttribute ("sprint", sprint);
		
		if (sprint != null)
			model.addAttribute("works", sprint.getWorks ());
		
		return VIEW_LIST;
	}
	
	@RequestMapping(value = "/work/search", method = RequestMethod.GET)
	public String search(Model model) {
		SearchDTO dto = new SearchDTO();
		
		model.addAttribute("search", dto);
		
		return VIEW_SEARCH;
	}

	@RequestMapping(value = "/work/postsearch", method = RequestMethod.POST)
	public String postSearch(@Valid @ModelAttribute ("search") SearchDTO searchDTO, BindingResult result, Model model) {
		if(result.hasErrors()){
			return VIEW_SEARCH;
		}
		
		if (searchDTO.getStart().getMillis () > searchDTO.getEnd().getMillis()) {
			result.rejectValue("start", "Start date cannot be later than end date.");
			
			return VIEW_SEARCH;
		}
		
		List<WorkDTO> works = new ArrayList<WorkDTO>();
		
		for (Work work : workService.findAll())
			if (work.getStart().getMillis() >= searchDTO.getStart().getMillis() && work.getEnd().getMillis() <= searchDTO.getEnd().getMillis()) {
				WorkDTO dto = new WorkDTO ();
				
				dto.setName(work.getName());
				dto.setStart(work.getStart());
				dto.setEnd(work.getEnd());
				dto.setSprintTitle(work.getSprint().getTitle());
				
				works.add(dto);
			}
		
		model.addAttribute("works", works);
		
		return VIEW_RESULT;
	}
	
	@RequestMapping(value = "/work/add/{id}", method = RequestMethod.GET)
	public String add(@PathVariable("id") Long id, Model model) {
		WorkDTO work = new WorkDTO();
		
		work.setSprintId (id);
		model.addAttribute("work", work);
		
		return VIEW_ADD;
	}
	
	@RequestMapping(value = "/work/saveadd", method = RequestMethod.POST)
	public String saveAdd(@Valid @ModelAttribute(MODEL_ATTRIBUTE) WorkDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
		if(result.hasErrors()){
			return VIEW_ADD;
		}
		
		if (dto.getStart().getMillis () > dto.getEnd().getMillis()) {
			result.rejectValue("start", "Start date cannot be later than end date.");
			
			return VIEW_ADD;
		}
		
		Long sprintId = dto.getSprintId ();
		Sprint sprint = sprintService.findById (sprintId);
		
		workService.add (dto, sprint);
		
		return createRedirectViewPath("/work/list/" + sprintId);
	}
	
	@RequestMapping(value = "/work/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Work updated = workService.findById(id);
        WorkDTO dto = new WorkDTO();
        
        dto.setId(updated.getId());
        dto.setName(updated.getName());
        dto.setStart(updated.getStart());
        dto.setEnd(updated.getEnd());
        dto.setSprintId(updated.getSprint().getId());
        
        model.addAttribute(MODEL_ATTRIBUTE, dto);

        return VIEW_UPDATE;
    }

    @RequestMapping(value = "/work/saveupdate", method = RequestMethod.POST)
    public String saveUpdate(@Valid @ModelAttribute(MODEL_ATTRIBUTE) WorkDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException{
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        if (dto.getStart().getMillis () > dto.getEnd().getMillis()) {
			result.rejectValue("start", "Start date cannot be later than end date.");
			
			return VIEW_UPDATE;
		}
        
        workService.update(dto);
        
        attributes.addFlashAttribute("feedbackMessage", "Work has been successfully updated.");

        return createRedirectViewPath("/work/list/" + dto.getSprintId ());
    }
	
	@RequestMapping(value = "/work/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
		Work work = workService.findById(id);
		Sprint sprint = work.getSprint ();
		
		workService.deleteById(id);
        
        attributes.addFlashAttribute("feedbackMessage", "Work has been successfully deleted.");
        
        return createRedirectViewPath("/work/list/" + sprint.getId ());
    }
	
	private String createRedirectViewPath(String requestMapping) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(requestMapping);
        return redirectViewPath.toString();
    }
	
}

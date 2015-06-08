package ro.sapientia2015.story.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.SprintService;
import ro.sapientia2015.story.service.StoryService;

@Controller
public class SprintController {

	@Resource
	private SprintService service;
	
	@Resource
	private StoryService storyService;

	public static final String VIEW_LIST = "sprint/list";
	public static final String VIEW_ADD = "sprint/add";
	public static final String VIEW_UPDATE = "sprint/update";
	public static final String VIEW_STORY_LIST = "sprint/storyList";
	
	protected static final String REQUEST_MAPPING_LIST = "/sprint/list";
	protected static final String REQUEST_MAPPING_VIEW = "/sprint/{id}";
	protected static final String PARAMETER_ID = "id";

	protected static final String MODEL_ATTRIBUTE = "sprint";
	protected static final String MODEL_ATTRIBUTE_STORIES = "stories";

	@RequestMapping(value = "/sprint/list", method = RequestMethod.GET)
	public String listSprints(Model model) {

		List<Sprint> sprints = service.findAll();
		model.addAttribute("sprints", sprints);
		model.addAttribute("stories", storyService.findAll());
		
		return VIEW_LIST;
	}
	
    private String createRedirectViewPath(String requestMapping) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(requestMapping);
        return redirectViewPath.toString();
    }
    
	@RequestMapping(value = "/sprint/add", method = RequestMethod.GET)
	public String showForm(Model model) {

		SprintDTO sprint = new SprintDTO();
		model.addAttribute("sprint", sprint);	
	        
		return VIEW_ADD;
	}

	@RequestMapping(value = "/sprint/add", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) SprintDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
		if(result.hasErrors()){					
			return VIEW_ADD;
		}
		
		service.add(dto);
		
		return createRedirectViewPath("/sprint/list");
	}
	
	@RequestMapping(value = "/sprint/update/{id}", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
        Sprint updated = service.findById(id);
        
        if(updated != null)
        {
        	List<Story> stories = storyService.findAll();//updated.getStories(); 
	        SprintDTO formObject = constructFormObjectForUpdateForm(updated);   	       	         
	        formObject.setStories(stories);
	        
	        model.addAttribute("stories", stories);
		    model.addAttribute(MODEL_ATTRIBUTE, formObject);
        }
	    
        return VIEW_UPDATE;
    }

    @RequestMapping(value = "/sprint/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) SprintDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_UPDATE;
        }

        service.update(dto);
       
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }
    
    private SprintDTO constructFormObjectForUpdateForm(Sprint updated) {
        SprintDTO dto = new SprintDTO();

        dto.setId(updated.getId());
        dto.setDescription(updated.getDescription());
        dto.setTitle(updated.getTitle());

        return dto;
    }    
    
    @RequestMapping(value = "/sprint/storyList/{id}", method = RequestMethod.GET)
	public String storyList(@PathVariable("id") Long id, Model model) throws NotFoundException
    {            
        Sprint sprint = service.findById(id);
        List<Story> stories = storyService.findAll();
        
        List<Story> tmpStories = stories;
        
        if(sprint != null)
        {
	        List<Story> sprintStories = sprint.getStories();
	       	        
	        for(Story story: stories)
	        {
	        	if(sprintStories != null && sprintStories.contains(story))
	        		tmpStories.remove(story);
	        }
	        
	        sprint.setStories(tmpStories); 
	        model.addAttribute("sprint", sprint);   
	        model.addAttribute("sprintStories", sprintStories); 	        
        }        
        
        model.addAttribute("stories", tmpStories);         
        
        return VIEW_STORY_LIST;
    }
    
    @RequestMapping(value = "/sprint/storyList", method = RequestMethod.POST)
    public String storyList(@Valid @ModelAttribute(MODEL_ATTRIBUTE) Sprint sprint, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
        if (result.hasErrors()) {
            return VIEW_STORY_LIST;
        }
        
        if(sprint != null && sprint.getStories() != null)
        {
        	sprint.setStories(sprint.getStories());        	
        	service.updateStories(constructFormObjectForUpdateForm(sprint));
        }
       
        return createRedirectViewPath(REQUEST_MAPPING_LIST);
    }
}

package ro.sapientia2015.story.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.SprintService;

@Controller
public class SprintController {

	@Resource
	private SprintService service;
	
	public static final String VIEW_LIST = "sprint/list";
	public static final String VIEW_ADD = "sprint/add";
	 protected static final String VIEW_UPDATE = "sprint/update";
	 protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.sprint.updated";
	 protected static final String PARAMETER_ID = "id";
	 protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";
	 protected static final String REQUEST_MAPPING_VIEW = "/sprint/list";
	public static final String MODEL_ATTRIBUTE = "sprint";

	@Resource
    private MessageSource messageSource;
	
	@RequestMapping(value = "/sprint/list", method = RequestMethod.GET)
	public String listSprints(Model model) {

		List<Sprint> sprints = service.findAll();
		model.addAttribute("sprints", sprints);
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
		model.addAttribute(MODEL_ATTRIBUTE, sprint);
		return VIEW_ADD;
	}

	@RequestMapping(value = "/sprint/add", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) SprintDTO dto, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()){
			return VIEW_ADD;
		}
		
		service.add(dto);
		
		return createRedirectViewPath("/sprint/list");
	}
	
	 @RequestMapping(value = "/sprint/update/{id}", method = RequestMethod.GET)
	    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
	        Sprint updated = service.findById(id);
	        SprintDTO formObject = constructFormObjectForUpdateForm(updated);
	        model.addAttribute(MODEL_ATTRIBUTE, formObject);

	        return VIEW_UPDATE;
	    }

	    @RequestMapping(value = "/sprint/update", method = RequestMethod.POST)
	    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) SprintDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
	        if (result.hasErrors()) {
	            return VIEW_UPDATE;
	        }

	        Sprint updated = service.update(dto);

	        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
	    }
	
	    private SprintDTO constructFormObjectForUpdateForm(Sprint updated) {
	        SprintDTO dto = new SprintDTO();

	        dto.setId(updated.getId());
	        dto.setDescription(updated.getDescription());
	        dto.setTitle(updated.getTitle());

	        return dto;
	    }
	    
	    private void addFeedbackMessage(RedirectAttributes attributes, String messageCode, Object... messageParameters) {
	        String localizedFeedbackMessage = getMessage(messageCode, messageParameters);
	        attributes.addFlashAttribute(FLASH_MESSAGE_KEY_FEEDBACK, localizedFeedbackMessage);
	    }
	    
	    private String getMessage(String messageCode, Object... messageParameters) {
	        Locale current = LocaleContextHolder.getLocale();
	        return messageSource.getMessage(messageCode, messageParameters, current);
	    }
}

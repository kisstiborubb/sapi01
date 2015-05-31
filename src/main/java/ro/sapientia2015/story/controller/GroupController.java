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

import ro.sapientia2015.story.dto.GroupDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Group;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.GroupService;

@Controller
public class GroupController {
	@Resource
	private GroupService service;
	
	protected static final String FEEDBACK_MESSAGE_KEY_ADDED = "feedback.message.group.added";
	protected static final String FEEDBACK_MESSAGE_KEY_UPDATED = "feedback.message.group.updated";
	protected static final String FEEDBACK_MESSAGE_KEY_DELETED = "feedback.message.group.deleted";
	
	protected static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";
	protected static final String REQUEST_MAPPING_VIEW = "/group/{id}";
	protected static final String REQUEST_MAPPING_LIST = "/group/list";
	protected static final String PARAMETER_ID = "id";
	protected static final String MODEL_ATTRIBUTE = "group";
	protected static final String MODEL_ATTRIBUTE_LIST = "groups";
	 
	public static final String VIEW_LIST = "group/list";
	protected static final String VIEW_ADD = "group/add";
	 protected static final String VIEW_UPDATE = "group/update";
	    protected static final String VIEW_VIEW = "group/view";
	    
	@Resource
    private MessageSource messageSource;
	
	 @RequestMapping(value = "/group/add", method = RequestMethod.GET)
	    public String showAddForm(Model model) {
	        GroupDTO formObject = new GroupDTO();
	        model.addAttribute(MODEL_ATTRIBUTE, formObject);

	        return VIEW_ADD;
	    }
	
	 @RequestMapping(value = "/group/add", method = RequestMethod.POST)
	    public String add(@Valid @ModelAttribute(MODEL_ATTRIBUTE) GroupDTO dto, BindingResult result, RedirectAttributes attributes) {
	        if (result.hasErrors()) {
	            return VIEW_ADD;
	        }

	        Group added = service.add(dto);
	        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_ADDED, added.getDescription());
	        attributes.addAttribute(PARAMETER_ID, added.getId());

	        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
	    }
	
	 @RequestMapping(value = "/group/delete/{id}", method = RequestMethod.GET)
	    public String deleteById(@PathVariable("id") Long id, RedirectAttributes attributes) throws NotFoundException {
	        Group deleted = service.deleteById(id);
	        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_DELETED, deleted.getDescription());
	        return createRedirectViewPath(REQUEST_MAPPING_LIST);
	    }
	 
	 @RequestMapping(value = REQUEST_MAPPING_LIST, method = RequestMethod.GET)
	    public String findAll(Model model) {
	        List<Group> models = service.findAll();
	        model.addAttribute(MODEL_ATTRIBUTE_LIST, models);
	        return VIEW_LIST;
	    }
	 
	 @RequestMapping(value = REQUEST_MAPPING_VIEW, method = RequestMethod.GET)
	    public String findById(@PathVariable("id") Long id, Model model) throws NotFoundException {
	        Group found = service.findById(id);
	        model.addAttribute(MODEL_ATTRIBUTE, found);
	        return VIEW_VIEW;
	    }
	 
	 @RequestMapping(value = "/group/update/{id}", method = RequestMethod.GET)
	    public String showUpdateForm(@PathVariable("id") Long id, Model model) throws NotFoundException {
	        Group updated = service.findById(id);
	        GroupDTO formObject = constructFormObjectForUpdateForm(updated);
	        model.addAttribute(MODEL_ATTRIBUTE, formObject);

	        return VIEW_UPDATE;
	    }
	 
	 @RequestMapping(value = "/group/update", method = RequestMethod.POST)
	    public String update(@Valid @ModelAttribute(MODEL_ATTRIBUTE) GroupDTO dto, BindingResult result, RedirectAttributes attributes) throws NotFoundException {
	        if (result.hasErrors()) {
	            return VIEW_UPDATE;
	        }

	        Group updated = service.update(dto);
	        addFeedbackMessage(attributes, FEEDBACK_MESSAGE_KEY_UPDATED, updated.getDescription());
	        attributes.addAttribute(PARAMETER_ID, updated.getId());

	        return createRedirectViewPath(REQUEST_MAPPING_VIEW);
	    }
	 
//	@RequestMapping(value = "/group/list", method = RequestMethod.GET)
//	public String listGroups(Model model) {
//
//		List<Group> groups = service.findAll();
//		model.addAttribute("groups", groups);
//		return VIEW_LIST;
//	}
	
	 private GroupDTO constructFormObjectForUpdateForm(Group updated) {
	        GroupDTO dto = new GroupDTO();

	        dto.setId(updated.getId());
	        dto.setDescription(updated.getDescription());	        

	        return dto;
	    }
	 
	private String createRedirectViewPath(String requestMapping) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(requestMapping);
        return redirectViewPath.toString();
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

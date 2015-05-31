package ro.sapientia2015.story.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.controller.StoryController;
import ro.sapientia2015.story.dto.GroupDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Group;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.service.GroupService;
import ro.sapientia2015.story.service.StoryService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Kiss Tibor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class GroupControllerTest {

    private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_DESCRIPTION = "description";   

    private GroupController controller;

    private MessageSource messageSourceMock;

    private GroupService serviceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new GroupController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(GroupService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showAddGroupForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(GroupController.VIEW_ADD, view);

        GroupDTO formObject = (GroupDTO) model.asMap().get(GroupController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getDescription());
        assertNull(formObject.getStories());
    }

    @Test
    public void add() {
    	 GroupDTO formObject = new GroupDTO();
    	 formObject.setDescription("group_description");

         Group model = Group.getBuilder("group_description")
                 .build();
        
         ReflectionTestUtils.setField(model, "id", 1L);       
         
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/group/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(GroupController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = StoryTestUtil.createRedirectViewPath(GroupController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(GroupController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, GroupController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void addEmptyGroup() {
        GroupDTO formObject = new GroupDTO();
   	    
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/group/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(GroupController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_DESCRIPTION);
    }
    
    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        Group model = Group.getBuilder("group_description")
                .build();
       
        ReflectionTestUtils.setField(model, "id", 1L);   
        
        when(serviceMock.deleteById(1L)).thenReturn(model);

        initMessageSourceForFeedbackMessage(GroupController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(1L, attributes);

        verify(serviceMock, times(1)).deleteById(1L);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, GroupController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = StoryTestUtil.createRedirectViewPath(GroupController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }
    
    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<Group> models = new ArrayList<Group>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(GroupController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(GroupController.MODEL_ATTRIBUTE_LIST));
    }
    
    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, StoryController.FLASH_MESSAGE_KEY_FEEDBACK);
    }

    private void assertFieldErrors(BindingResult result, String... fieldNames) {
        assertEquals(fieldNames.length, result.getFieldErrorCount());
        for (String fieldName : fieldNames) {
            assertNotNull(result.getFieldError(fieldName));
        }
    }

    private void assertFlashMessages(RedirectAttributes attributes, String messageCode, String flashMessageParameterName) {
        Map<String, ?> flashMessages = attributes.getFlashAttributes();
        Object message = flashMessages.get(flashMessageParameterName);

        assertNotNull(message);
        flashMessages.remove(message);
        assertTrue(flashMessages.isEmpty());

        verify(messageSourceMock, times(1)).getMessage(eq(messageCode), any(Object[].class), any(Locale.class));
        verifyNoMoreInteractions(messageSourceMock);
    }

    private BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
        WebDataBinder binder = new WebDataBinder(formObject);
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
        return binder.getBindingResult();
    }

    private void initMessageSourceForFeedbackMessage(String feedbackMessageCode) {
        when(messageSourceMock.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    }
}

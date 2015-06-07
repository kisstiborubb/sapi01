package ro.sapientia2015.story.controller;

import org.joda.time.DateTime;
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
import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.WorkDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.Work;
import ro.sapientia2015.story.service.SprintService;
import ro.sapientia2015.story.service.StoryService;
import ro.sapientia2015.story.service.WorkService;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class WorkControllerTest {
    private WorkController controller;

    private WorkService workServiceMock;
    
    private SprintService sprintServiceMock;
    
    @Resource
    private Validator validator;
    
    @Before
    public void setUp() {
        controller = new WorkController();

        workServiceMock = mock (WorkService.class);
        sprintServiceMock = mock(SprintService.class);
        
        ReflectionTestUtils.setField(controller, "workService", workServiceMock);
        ReflectionTestUtils.setField(controller, "sprintService", sprintServiceMock);
    }

    @Test
    public void listWorksForSprint_ReturnsProperPathToViewTemplate () throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.listWorksForSprint(new Long(1), model);

        assertEquals(WorkController.VIEW_LIST, view);
     }
    
    @Test
    public void listWorksForSprint_FindsSprintAndSetsUpProperViewModel() throws NotFoundException {
    	BindingAwareModelMap model = new BindingAwareModelMap();
    	Long sprintId = new Long(123);
    	Sprint expectedSprint = mock(Sprint.class);
    	List<Work> expectedWorks = new ArrayList<Work>(){{
    		add(mock(Work.class));
    		add(mock(Work.class));
    	}};
    	
    	when (expectedSprint.getWorks()).thenReturn(expectedWorks);
    	when (sprintServiceMock.findById(sprintId)).thenReturn(expectedSprint);
    	
    	controller.listWorksForSprint(sprintId, model);

    	Sprint sprint = (Sprint)model.asMap().get("sprint");
    	List<Work> works = (List<Work>)model.asMap().get("works");
    	
    	assertNotNull(sprint);
    	assertEquals(expectedSprint, sprint);
    	
    	assertNotNull(works);
    	assertEquals(expectedWorks, works);
    }
    
    @Test (expected = NotFoundException.class)
    public void listWorksForSprint_DoesntFindNonExistingSprintAndThrowsException () throws NotFoundException {
    	BindingAwareModelMap model = new BindingAwareModelMap();
    
    	when (sprintServiceMock.findById(any(Long.class))).thenThrow(new NotFoundException(null));
    	
    	controller.listWorksForSprint(new Long(123), model);
    }
    
    @Test
    public void add_ReturnsProperPathToViewTemplate () {
    	BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.add(new Long(1), model);

        assertEquals(WorkController.VIEW_ADD, view);
    }
    
    @Test 
    public void saveAdd_DoesntSaveWorkWithWrongDatesSet () throws NotFoundException {
    	WorkDTO dto = new WorkDTO();
    	Sprint sprint = new Sprint ();
    	
    	sprint.setId(new Long (123));
    	
    	dto.setName("name");
    	dto.setStart(DateTime.now().plusHours(1));
    	dto.setEnd(DateTime.now());
    	dto.setSprintId(sprint.getId ());
    	
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/work/add");
        BindingResult result = bindAndValidate(mockRequest, dto);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.saveAdd(dto, result, attributes);
        
        assertEquals (WorkController.VIEW_ADD, view);
    }
    
    @Test 
    public void saveAdd_DoesntSaveWorkWithMissingName () throws NotFoundException {
    	WorkDTO dto = new WorkDTO();
    	Sprint sprint = new Sprint ();
    	
    	sprint.setId(new Long (123));
    	
    	dto.setStart(DateTime.now().plusHours(1));
    	dto.setEnd(DateTime.now());
    	dto.setSprintId(sprint.getId ());
    	
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/work/add");
        BindingResult result = bindAndValidate(mockRequest, dto);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.saveAdd(dto, result, attributes);
        
        assertEquals (WorkController.VIEW_ADD, view);
    }
    
    @Test 
    public void saveAdd_DoesntSaveWorkWithMissingStartDate () throws NotFoundException {
    	WorkDTO dto = new WorkDTO();
    	Sprint sprint = new Sprint ();
    	
    	sprint.setId(new Long (123));
    	
    	dto.setName("name");
    	dto.setEnd(DateTime.now());
    	dto.setSprintId(sprint.getId ());
    	
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/work/add");
        BindingResult result = bindAndValidate(mockRequest, dto);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.saveAdd(dto, result, attributes);
        
        assertEquals (WorkController.VIEW_ADD, view);
    }
    
    @Test 
    public void saveAdd_SavesValidWork () throws NotFoundException {
    	WorkDTO dto = new WorkDTO();
    	Sprint sprint = new Sprint ();
    	
    	sprint.setId(new Long (123));
    	
    	dto.setName("name");
    	dto.setStart(DateTime.now());
    	dto.setEnd(DateTime.now().plusHours(1));
    	dto.setSprintId(sprint.getId ());
    	
    	when (sprintServiceMock.findById(sprint.getId())).thenReturn(sprint);
    	
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/work/add");
        BindingResult result = bindAndValidate(mockRequest, dto);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.saveAdd(dto, result, attributes);

        verify(workServiceMock, times(1)).add(dto, sprint);
        verifyNoMoreInteractions(workServiceMock);

        String expectedView = "redirect:/work/list/" + sprint.getId();
        assertEquals(expectedView, view);
    }
    
    @Test 
    public void update_ReturnsProperPathToViewTemplate () throws NotFoundException {
    	Work work = new Work ();
    	Sprint sprint = new Sprint ();
    	
    	sprint.setId(new Long (456));
    	
    	work.setId(new Long(123));
    	work.setName("name");
    	work.setStart(DateTime.now());
    	work.setEnd(DateTime.now().plusHours(1));
    	work.setSprint(sprint);
    	
    	when (workServiceMock.findById(work.getId())).thenReturn(work);
    	
    	BindingAwareModelMap model = new BindingAwareModelMap();
    	
        String view = controller.update (work.getId(), model);

        WorkDTO dto = (WorkDTO)model.asMap().get("work");
        
        assertNotNull(dto);
        
        assertEquals (work.getId(), dto.getId());
        assertEquals (work.getName(), dto.getName());
        assertEquals (work.getStart(), dto.getStart());
        assertEquals (work.getEnd(), dto.getEnd());
        assertEquals (sprint.getId(), dto.getSprintId());
        
        assertEquals(WorkController.VIEW_UPDATE, view);
    }
    
    @Test 
    public void saveUpdate_DoesntSaveWorkWithWrongDatesSet () throws NotFoundException {
    	WorkDTO dto = new WorkDTO();
    	Sprint sprint = new Sprint ();
    	
    	sprint.setId(new Long (123));
    	
    	dto.setName("name");
    	dto.setStart(DateTime.now().plusHours(1));
    	dto.setEnd(DateTime.now());
    	dto.setSprintId(sprint.getId ());
    	
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/work/saveupdate");
        BindingResult result = bindAndValidate(mockRequest, dto);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.saveUpdate(dto, result, attributes);
        
        assertEquals (WorkController.VIEW_UPDATE, view);
    }
    
    @Test 
    public void saveUpdate_DoesntSaveWorkWithMissingName () throws NotFoundException {
    	WorkDTO dto = new WorkDTO();
    	Sprint sprint = new Sprint ();
    	
    	sprint.setId(new Long (123));
    	
    	dto.setStart(DateTime.now().plusHours(1));
    	dto.setEnd(DateTime.now());
    	dto.setSprintId(sprint.getId ());
    	
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/work/saveupdate");
        BindingResult result = bindAndValidate(mockRequest, dto);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.saveUpdate(dto, result, attributes);
        
        assertEquals (WorkController.VIEW_UPDATE, view);
    }
    
    @Test 
    public void saveUpdate_DoesntSaveWorkWithMissingStartDate () throws NotFoundException {
    	WorkDTO dto = new WorkDTO();
    	Sprint sprint = new Sprint ();
    	
    	sprint.setId(new Long (123));
    	
    	dto.setName("name");
    	dto.setEnd(DateTime.now());
    	dto.setSprintId(sprint.getId ());
    	
    	MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/work/saveupdate");
        BindingResult result = bindAndValidate(mockRequest, dto);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.saveUpdate(dto, result, attributes);
        
        assertEquals (WorkController.VIEW_UPDATE, view);
    }
    
    @Test 
    public void saveUpdate_SavesValidWork () throws NotFoundException {
    	WorkDTO dto = new WorkDTO();
    	
    	dto.setId(new Long(123));
    	dto.setName("name");
    	dto.setStart(DateTime.now());
    	dto.setEnd(DateTime.now().plusHours(1));
    	dto.setSprintId(new Long(456));
    	
        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/work/add");
        BindingResult result = bindAndValidate(mockRequest, dto);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.saveUpdate(dto, result, attributes);

        verify(workServiceMock, times(1)).update (dto);
        verifyNoMoreInteractions(workServiceMock);

        String expectedView = "redirect:/work/list/" + dto.getSprintId();
        assertEquals(expectedView, view);
    }
    
    @Test
    public void delete_DeletesWork () throws NotFoundException {
    	Work work = new Work();
    	Sprint sprint = new Sprint ();
    	
    	work.setId(new Long (123));
    	work.setSprint(sprint);

    	sprint.setId(new Long(456));

    	when (workServiceMock.findById(work.getId())).thenReturn(work);
    	
    	RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.delete (work.getId (), attributes);
    	
    	verify(workServiceMock, times(1)).deleteById (work.getId());
        
        String expectedView = "redirect:/work/list/" + sprint.getId();
        assertEquals (expectedView, view);
    }
    
    private BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
        WebDataBinder binder = new WebDataBinder(formObject);
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
        return binder.getBindingResult();
    }
}

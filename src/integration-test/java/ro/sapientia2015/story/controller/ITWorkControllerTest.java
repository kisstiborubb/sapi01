package ro.sapientia2015.story.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ro.sapientia2015.common.controller.ErrorController;
import ro.sapientia2015.config.ExampleApplicationContext;
import ro.sapientia2015.context.WebContextLoader;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.controller.StoryController;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.model.Story;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("workData.xml")
public class ITWorkControllerTest {

    private static final String FORM_FIELD_DESCRIPTION = "description";
    private static final String FORM_FIELD_ID = "id";
    private static final String FORM_FIELD_TITLE = "title";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
        
        System.setProperty("user.timezone", "UTC");
    }

    @Test
    @ExpectedDatabase("workData.xml")
    public void listWorksForSprint() throws Exception {
    	DateTime start = new DateTime (2015, 10, 10, 11, 0, 0);
    	DateTime end = new DateTime (2015, 10, 10, 11, 30, 0);

        mockMvc.perform(get("/work/list/1"))
               .andExpect(status().isOk())
               .andExpect(view().name(WorkController.VIEW_LIST))
               .andExpect(forwardedUrl("/WEB-INF/jsp/work/list.jsp"))
               
               .andExpect(model().attribute("sprint", hasProperty("id", is(1L))))
               .andExpect(model().attribute("sprint", hasProperty("description", is("Lorem ipsum"))))
               .andExpect(model().attribute("sprint", hasProperty("title", is("Bar"))))
               
               .andExpect(model().attribute("works", hasItem(
        		   allOf(
    				   hasProperty("id", is(1L)),
    				   hasProperty("name", is("name1")),
    				   hasProperty("start", is(start)),
    				   hasProperty("end", is(end))
                   )
                )));
    }

    @Test
    @ExpectedDatabase ("workData.xml")
    public void postSearch_ReturnsOneResult () throws Exception {
    	DateTime start = new DateTime (2015, 10, 10, 11, 0, 0);
    	DateTime end = new DateTime (2015, 10, 10, 11, 30, 0);
    	
    	mockMvc.perform(post("/work/postsearch")
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("start", "10/10/2015 10:00")
                .param("end", "10/10/2015 11:33")
				)
		        
				.andExpect(status().isOk())
		        .andExpect(view().name(WorkController.VIEW_RESULT))
		        
		        .andExpect(model().attribute("works", hasItem(
        		   allOf(
    				   hasProperty("name", is("name1")),
    				   hasProperty("start", is(start)),
    				   hasProperty("end", is(end)),
    				   hasProperty("sprintTitle", is("Bar"))
                   )
                )));
    }
    
    @Test
    @ExpectedDatabase ("workData.xml")
    public void postSearch_ReturnsNoResult () throws Exception {
    	mockMvc.perform(post("/work/postsearch")
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("start", "10/10/2015 9:00")
                .param("end", "10/10/2015 10:00")
				)
		        
				.andExpect(status().isOk())
		        .andExpect(view().name(WorkController.VIEW_RESULT))
		        
		        .andExpect(model().attribute("works", hasSize(0)));
    }
    
    @Test
    @ExpectedDatabase(value="workData-add-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void saveadd_AddsWorkToDatabase() throws Exception {    	
        String expectedRedirectViewPath = StoryTestUtil.createRedirectViewPath("/work/list/1");

        mockMvc.perform(post("/work/saveadd")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "name3")
                .param("start", "10/10/2015 11:00")
                .param("end", "10/10/2015 11:30")
                .param("sprintId", "1")
        		)
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath));
    }

    @Test
    @ExpectedDatabase("workData-delete-expected.xml")
    public void delete_DeletesWorkFromDatabase () throws Exception {
        String expectedRedirectViewPath = StoryTestUtil.createRedirectViewPath("/work/list/1");
        
        mockMvc.perform(get("/work/delete/2"))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute("feedbackMessage", is("Work has been successfully deleted.")));
    }
}


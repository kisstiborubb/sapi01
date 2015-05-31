package ro.sapientia2015.story.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

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
import ro.sapientia2015.story.dto.GroupDTO;
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

/**
 * This test uses the annotation based application context configuration.
 * @author Kiss Tibor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = WebContextLoader.class, classes = {ExampleApplicationContext.class})
//@ContextConfiguration(loader = WebContextLoader.class, locations = {"classpath:exampleApplicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup("storyData.xml")
public class ITGroupControllerTest {

    private static final String FORM_FIELD_DESCRIPTION = "description";
    private static final String FORM_FIELD_ID = "id";
    //private static final String FORM_FIELD_TITLE = "title";

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void showAddForm() throws Exception {
        mockMvc.perform(get("/group/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(GroupController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/group/add.jsp"))
                .andExpect(model().attribute(GroupController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(GroupController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())));
//                .andExpect(model().attribute(GroupController.MODEL_ATTRIBUTE, hasProperty("title", isEmptyOrNullString())));
    }

    @Test
    @ExpectedDatabase("storyData.xml")
    public void addEmpty() throws Exception {
        mockMvc.perform(post("/group/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(GroupController.MODEL_ATTRIBUTE, new GroupDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(GroupController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/group/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(GroupController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attribute(GroupController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(GroupController.MODEL_ATTRIBUTE, hasProperty("description", isEmptyOrNullString())))
                .andExpect(model().attribute(GroupController.MODEL_ATTRIBUTE, hasProperty("stories", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase("storyData.xml")
    public void addWhenDescriptionIsTooLong() throws Exception {        
        String description;
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < 101; index++) {
            builder.append("g");
        }

        description = builder.toString();
        
        mockMvc.perform(post("/group/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, description)
                .sessionAttr(GroupController.MODEL_ATTRIBUTE, new GroupDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(GroupController.VIEW_ADD))
                .andExpect(forwardedUrl("/WEB-INF/jsp/group/add.jsp"))
                .andExpect(model().attributeHasFieldErrors(GroupController.MODEL_ATTRIBUTE, "description"))
                .andExpect(model().attribute(GroupController.MODEL_ATTRIBUTE, hasProperty("id", nullValue())))
                .andExpect(model().attribute(GroupController.MODEL_ATTRIBUTE, hasProperty("stories", isEmptyOrNullString())));
    }
    
    @Test
    @ExpectedDatabase(value="storyData-addGroup-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void add() throws Exception {
        String expectedRedirectViewPath = StoryTestUtil.createRedirectViewPath(GroupController.REQUEST_MAPPING_VIEW);

        mockMvc.perform(post("/group/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(FORM_FIELD_DESCRIPTION, "group_one")
                .sessionAttr(GroupController.MODEL_ATTRIBUTE, new GroupDTO())
        )
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(model().attribute(GroupController.PARAMETER_ID, is("3")))
                .andExpect(flash().attribute(GroupController.FLASH_MESSAGE_KEY_FEEDBACK, is("Group entry: group_one was added.")));
    }
    
    @Test
    @ExpectedDatabase("storyData-deleteGroup-expected.xml")
    public void deleteById() throws Exception {
        String expectedRedirectViewPath = StoryTestUtil.createRedirectViewPath(GroupController.REQUEST_MAPPING_LIST);
        mockMvc.perform(get("/group/delete/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedRedirectViewPath))
                .andExpect(flash().attribute(StoryController.FLASH_MESSAGE_KEY_FEEDBACK, is("Group entry: lorem ipsum2 was deleted.")));
    }
}


package ro.sapientia2015.story.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ro.sapientia2015.story.model.Story;
import static junit.framework.Assert.*;

/**
 * @author Kiss Tibor
 */
public class GroupTest {    
    private String DESCRIPTION = "group_description";

    @Test
    public void buildWithMandatoryInformation() {
        Group built = Group.getBuilder(DESCRIPTION).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(DESCRIPTION, built.getDescription());
        assertNull(built.getModificationTime());
        assertNull(built.getStories());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void buildWithAllInformation() {
    	List<Story> listStories = new ArrayList<Story>();
    	Story builtStory = Story.getBuilder("story_title").build();
    	listStories.add(builtStory);
    	
    	Group built = Group.getBuilder(DESCRIPTION)
    			.story(listStories)
    			.build();
    	
        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(DESCRIPTION, built.getDescription());
        assertEquals(listStories, built.getStories());
        assertNull(built.getModificationTime());
        assertEquals(0L, built.getVersion());
    }

    @Test
    public void prePersist() {
        Group group = new Group();
        group.prePersist();

        assertNull(group.getId());
        assertNotNull(group.getCreationTime());
        assertNull(group.getDescription());
        assertNull(group.getStories());
        assertNotNull(group.getModificationTime());
        assertEquals(0L, group.getVersion());
        assertEquals(group.getCreationTime(), group.getModificationTime());
    }

    @Test
    public void preUpdate() {
    	Group group = new Group();
        group.prePersist();

        pause(1000);

        group.preUpdate();

        assertNull(group.getId());
        assertNotNull(group.getCreationTime());
        assertNull(group.getDescription());
        assertNull(group.getStories());
        assertNotNull(group.getModificationTime());
        assertEquals(0L, group.getVersion());
        assertTrue(group.getModificationTime().isAfter(group.getCreationTime()));
    }

    private void pause(long timeInMillis) {
        try {
            Thread.currentThread().sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    }
}

package ro.sapientia2015.story.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static junit.framework.Assert.*;

public class SprintTest {

	private String TITLE = "title";
    private String DESCRIPTION = "description";
    
	 @Test
	 public void buildSprintWithMandatoryInformation() {
        Sprint built = Sprint.getBuilder(TITLE).build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertNull(built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
     }
	 
	 @Test
	 public void buildSprintWithAllInformation() {
		 Sprint built = Sprint.getBuilder(TITLE)
                .description(DESCRIPTION)
                .build();

        assertNull(built.getId());
        assertNull(built.getCreationTime());
        assertEquals(DESCRIPTION, built.getDescription());
        assertNull(built.getModificationTime());
        assertEquals(TITLE, built.getTitle());
        assertEquals(0L, built.getVersion());
	 }
	 
	 @Test
	 public void prePersist() {
        Sprint sprint = new Sprint();
        sprint.prePersist();

        assertNull(sprint.getId());
        assertNotNull(sprint.getCreationTime());
        assertNull(sprint.getDescription());
        assertNotNull(sprint.getModificationTime());
        assertNull(sprint.getTitle());
        assertEquals(0L, sprint.getVersion());
        assertEquals(sprint.getCreationTime(), sprint.getModificationTime());
    }

    @Test
    public void preUpdate() {
    	Sprint sprint = new Sprint();
    	sprint.prePersist();
    	
        pause(1000);

        sprint.preUpdate();

        assertNull(sprint.getId());
        assertNotNull(sprint.getCreationTime());
        assertNull(sprint.getDescription());
        assertNotNull(sprint.getModificationTime());
        assertNull(sprint.getTitle());
        assertEquals(0L, sprint.getVersion());
        assertTrue(sprint.getModificationTime().isAfter(sprint.getCreationTime()));
    }
    
    private void pause(long timeInMillis) {
        try {
            Thread.currentThread().sleep(timeInMillis);
        }
        catch (InterruptedException e) {
            //Do Nothing
        }
    } 
    
    @Test
    public void addStoriesToSprint() {
    	Sprint sprint = new Sprint();
    	sprint.prePersist();

    	Story story = new Story();
        story.prePersist();
        
        List<Story> stories = new ArrayList<Story>();
        stories.add(story);
        sprint.setStories(stories);
        
        pause(1000);
        
        sprint.preUpdate();

        assertNull(sprint.getId());
        assertNotNull(sprint.getCreationTime());
        assertNull(sprint.getDescription());
        assertNotNull(sprint.getModificationTime());
        assertNull(sprint.getTitle());
        assertEquals(0L, sprint.getVersion());
        assertTrue(sprint.getModificationTime().isAfter(sprint.getCreationTime()));
    }
}

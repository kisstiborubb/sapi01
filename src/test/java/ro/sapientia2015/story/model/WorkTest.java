package ro.sapientia2015.story.model;

import org.joda.time.DateTime;
import org.junit.Test;

import ro.sapientia2015.story.model.Story;
import static junit.framework.Assert.*;

public class WorkTest {

    @Test
    public void buildWithAllInformation() {
    	Work work = new Work ();
    	
    	DateTime start = DateTime.now ();
    	DateTime end = DateTime.now().plusHours(2);
    	
    	work.setName ("name");
    	work.setStart (start);
    	work.setEnd (end);
    	work.setSprint (new Sprint ());

        assertNull(work.getId());
        assertEquals("name", work.getName());
        assertEquals(start, work.getStart());
        assertEquals(end, work.getEnd());
        
        assertNull(work.getModificationTime());
        assertNull(work.getCreationTime());
        assertEquals(0L, work.getVersion());
    }

    @Test
    public void prePersist() {
        Work work = new Work();
        work.prePersist();

        assertNull(work.getId());
        assertNull(work.getName());
        assertNull(work.getStart());
        assertNull(work.getEnd());
        
        assertNotNull(work.getCreationTime());
        assertNotNull(work.getModificationTime());
        assertEquals(work.getCreationTime(), work.getModificationTime());
        assertEquals(0L, work.getVersion());
    }

    @Test
    public void preUpdate() {
        Work work = new Work();
        work.prePersist();

        pause(1000);

        work.preUpdate();

        assertNull(work.getId());
        assertNull(work.getName());
        assertNull(work.getStart());
        assertNull(work.getEnd());
        
        assertNotNull(work.getCreationTime());
        assertNotNull(work.getModificationTime());
        assertTrue(work.getModificationTime().isAfter(work.getCreationTime()));
        assertEquals(0L, work.getVersion());
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

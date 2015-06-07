package ro.sapientia2015.story.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.WorkDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.Work;
import ro.sapientia2015.story.repository.SprintRepository;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.story.repository.WorkRepository;
import ro.sapientia2015.story.service.RepositoryStoryService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RepositoryWorkServiceTest {
    private RepositoryWorkService service;

    private WorkRepository repositoryMock;

    @Before
    public void setUp() {
    	service = new RepositoryWorkService();
        repositoryMock = mock(WorkRepository.class);
        
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void findAll() {
        List<Work> works = new ArrayList<Work>();
        when(repositoryMock.findAll()).thenReturn(works);

        List<Work> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(works, actual);
    }

    @Test
    public void add() {
    	WorkDTO dto = new WorkDTO ();
    	Sprint sprint = new Sprint ();
    	
    	dto.setName("name");
    	dto.setStart(DateTime.now ());
    	dto.setEnd(DateTime.now ().plusHours(2));
  
    	Work work = new Work ();
    	
    	when (repositoryMock.save(any(Work.class))).thenReturn(work);
    	
        Work actual = service.add(dto, sprint);

        ArgumentCaptor<Work> argument = ArgumentCaptor.forClass (Work.class);
        
        assertEquals (work, actual);
        
        verify(repositoryMock, times(1)).save(argument.capture());
        verifyNoMoreInteractions(repositoryMock);
        
        assertNotNull (argument);
        assertEquals (dto.getName(), argument.getValue().getName());
        assertEquals (dto.getStart(), argument.getValue().getStart());
        assertEquals (dto.getEnd(), argument.getValue().getEnd());
    }
 }

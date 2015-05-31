package ro.sapientia2015.story.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.dto.GroupDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Group;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.GroupRepository;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.story.service.RepositoryStoryService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Kiss Tibor
 */
public class RepositoryGroupServiceTest {

    private RepositoryGroupService service;

    private GroupRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryGroupService();

        repositoryMock = mock(GroupRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
        GroupDTO dto = new GroupDTO();
        dto.setDescription("group_description");
 
        service.add(dto);
              
        ArgumentCaptor<Group> groupArgument = ArgumentCaptor.forClass(Group.class);
        verify(repositoryMock, times(1)).save(groupArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        Group model = groupArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getDescription(), model.getDescription());       
    }

    @Test
    public void deleteById() throws NotFoundException {
        Group model = Group.getBuilder("group_description")
                .build();
        when(repositoryMock.findOne(1L)).thenReturn(model);

        Group actual = service.deleteById(1L);

        verify(repositoryMock, times(1)).findOne(1L);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(1L)).thenReturn(null);

        service.deleteById(1L);

        verify(repositoryMock, times(1)).findOne(1L);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findAll() {
        List<Group> models = new ArrayList<Group>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<Group> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
        Group model = Group.getBuilder("group_description")
                .build();
        when(repositoryMock.findOne(1L)).thenReturn(model);

        Group actual = service.findById(1L);

        verify(repositoryMock, times(1)).findOne(1L);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(StoryTestUtil.ID)).thenReturn(null);

        service.findById(StoryTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
    	GroupDTO dto = new GroupDTO();
        dto.setDescription("group_description");
        
        Group model = Group.getBuilder("group_description")
                .build();
        
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        Group actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getDescription(), actual.getDescription());
        //assertEquals(dto.getTitle(), actual.getTitle());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        GroupDTO dto = new GroupDTO();
        dto.setDescription("group_description");
        
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}

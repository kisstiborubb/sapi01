package ro.sapientia2015.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.GroupDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Group;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.repository.GroupRepository;
import ro.sapientia2015.story.repository.StoryRepository;

import javax.annotation.Resource;

import java.util.List;

/**
 * @author Kiss Tibor
 */
@Service
public class RepositoryGroupService implements GroupService {

    @Resource
    private GroupRepository repository;

    @Transactional
    @Override
    public Group add(GroupDTO added) {

    	Group model = Group.getBuilder(added.getDescription())
                .story(added.getStories())
                .build();

        return repository.save(model);
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Group deleteById(Long id) throws NotFoundException {
    	Group deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Group> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public Group findById(Long id) throws NotFoundException {
    	Group found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Group update(GroupDTO updated) throws NotFoundException {
    	Group model = findById(updated.getId());
        model.update(updated.getDescription(), updated.getStories());

        return model;
    }
}

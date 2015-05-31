package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.GroupDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Group;

/**
 * @author Kiss Tibor
 */
public interface GroupService {

    public Group add(GroupDTO added);

    public Group deleteById(Long id) throws NotFoundException;

    public List<Group> findAll();

    public Group findById(Long id) throws NotFoundException;

    public Group update(GroupDTO updated) throws NotFoundException;
}

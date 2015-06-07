package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.SprintDTO;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.WorkDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.Work;

public interface WorkService {

    public Work add(WorkDTO added, Sprint sprint);

    public Work deleteById(Long id) throws NotFoundException;

    public List<Work> findAll();

    public Work findById(Long id) throws NotFoundException;

    public Work update(WorkDTO updated) throws NotFoundException;
}

package ro.sapientia2015.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.sapientia2015.story.dto.WorkDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Work;
import ro.sapientia2015.story.repository.WorkRepository;

import javax.annotation.Resource;

import java.util.List;

@Service
public class RepositoryWorkService implements WorkService {
    @Resource
    private WorkRepository repository;

    @Transactional
    @Override
    public Work add(WorkDTO added, Sprint sprint) {
    	Work model = new Work ();
    	
    	model.setName(added.getName());
    	model.setStart(added.getStart());
    	model.setEnd(added.getEnd());
        model.setSprint(sprint);
        
        return repository.save(model); 
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Work deleteById(Long id) throws NotFoundException {
        Work deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Work> findAll() {
       return repository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = {NotFoundException.class})
    @Override
    public Work findById(Long id) throws NotFoundException {
        Work found = repository.findOne(id);
        if (found == null) {
            throw new NotFoundException("No entry found with id: " + id);
        }

        return found;
    }

    @Transactional(rollbackFor = {NotFoundException.class})
    @Override
    public Work update(WorkDTO updated) throws NotFoundException {
        Work model = findById(updated.getId());

        model.setName(updated.getName());
        model.setStart(updated.getStart());
        model.setEnd(updated.getEnd());
        
        return model;
    }
}

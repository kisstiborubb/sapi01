package ro.sapientia2015.story.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.sapientia2015.story.model.Group;

/**
 * @author Kiss Tibor
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
}

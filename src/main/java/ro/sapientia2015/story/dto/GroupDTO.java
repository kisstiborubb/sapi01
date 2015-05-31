package ro.sapientia2015.story.dto;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import ro.sapientia2015.story.model.Group;
import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;

public class GroupDTO {
	private Long id;

	@NotEmpty
    @Length(max = Group.MAX_LENGTH_DESCRIPTION)
    private String description;
    
	private Group.Builder builder = new Group.Builder();
	
    private List<Story> stories;
    
    public GroupDTO(){
    	
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Story> getStories() {
		return stories;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
	}
	
	public Group.Builder getBuilder() {
		return builder;
	}

	public void setBuilder(Group.Builder builder) {
		this.builder = builder;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

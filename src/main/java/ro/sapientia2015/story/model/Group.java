package ro.sapientia2015.story.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ro.sapientia2015.story.model.Story.Builder;

@Entity
@Table(name="groups")
public class Group {
	public static final int MAX_LENGTH_DESCRIPTION = 100;
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	 public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "creation_time", nullable = false)
	    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	    private DateTime creationTime;
	
	 public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}

	public void setModificationTime(DateTime modificationTime) {
		this.modificationTime = modificationTime;
	}

	@Column(name = "modification_time", nullable = false)
	    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	    private DateTime modificationTime;
	 
	 @Version
	    private long version;
	 
	public void setVersion(long version) {
		this.version = version;
	}

	@Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String description;
	
	@Column(name = "story")
    @OneToMany
    private List<Story> stories;

	public Group() {

    }
	
	public static Builder getBuilder(String description) {
        return new Builder(description);
    }
	
	 @PrePersist
    public void prePersist() {
        DateTime now = DateTime.now();
        creationTime = now;
        modificationTime = now;
    }

	    @PreUpdate
	    public void preUpdate() {
	        modificationTime = DateTime.now();
	    }

	    public void update(String description, List<Story> stories) {
	        this.description = description;
	        this.stories = stories;
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

		public Long getId() {
			return id;
		}

		public DateTime getCreationTime() {
			return creationTime;
		}

		public DateTime getModificationTime() {
			return modificationTime;
		}

		public long getVersion() {
			return version;
		}

	public static class Builder {

        private Group built;

        public Builder() {
            built = new Group();
        }
        
        public Builder(String description) {
            built = new Group();
            built.description = description;
        }

        public Group build() {
            return built;
        }

        public Builder story(List<Story> stories) {
            built.stories = stories;
            return this;
        }
    }
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

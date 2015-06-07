package ro.sapientia2015.story.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Entity
@Table(name="work")
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "start", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime start;

    @Column(name = "end", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime end;
    
    @Version
    private long version;

    @ManyToOne
    private Sprint sprint;
    
    public Work() {

    }

    public Long getId() {
        return id;
    }

	public void setId(Long id) {
		this.id = id;
	}

	public DateTime getCreationTime() {
        return creationTime;
    }
	
	public void setCreationTime(DateTime creationTime) {
		this.creationTime = creationTime;
	}

	public DateTime getModificationTime() {
        return modificationTime;
    }

	public void setModificationTime(DateTime modificationTime) {
		this.modificationTime = modificationTime;
	}
	
    public long getVersion() {
        return version;
    }
    
	public void setVersion(long version) {
		this.version = version;
	}
	
    public String getName() {
        return name;
    }
	
	public void setName(String name) {
		this.name = name;
	}
	
    public DateTime getStart() {
        return start;
    }
	
	public void setStart(DateTime start) {
		this.start = start;
	}

	public DateTime getEnd() {
        return end;
    }
		
	public void setEnd(DateTime end) {
		this.end = end;
	}

	public Sprint getSprint() {
        return sprint;
    }
		
	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

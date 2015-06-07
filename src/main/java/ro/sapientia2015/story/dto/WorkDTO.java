package ro.sapientia2015.story.dto;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import ro.sapientia2015.story.model.Story;

public class WorkDTO {

    private Long id;

    @Length(max = 100)
    @NotEmpty
    private String name;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm", style="dd/MM/yyyy hh:mm")
    private DateTime start;
    
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm", style="dd/MM/yyyy hh:mm")
    private DateTime end;
    
    private Long sprintId;
    
    private String sprintTitle;
    
    public WorkDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

	public Long getSprintId() {
		return sprintId;
	}

	public void setSprintId(Long sprintId) {
		this.sprintId = sprintId;
	}
	
	public String getSprintTitle() {
		return sprintTitle;
	}

	public void setSprintTitle(String sprintTitle) {
		this.sprintTitle = sprintTitle;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

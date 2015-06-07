package ro.sapientia2015.story.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import ro.sapientia2015.story.model.Sprint;
import ro.sapientia2015.story.model.Story;

public class SearchDTO {
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm", style="dd/MM/yyyy hh:mm")
	private DateTime start;

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm", style="dd/MM/yyyy hh:mm")
	private DateTime end;

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
}

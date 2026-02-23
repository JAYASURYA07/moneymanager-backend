package in.surya.moneymanager.dto;

import lombok.Data;

import java.time.LocalDate;

@Data

public class FilterDto {
private String type;
private LocalDate startdate;
private LocalDate enddate;
private String keyword;
private String sortField;
private String sortOrder;


}

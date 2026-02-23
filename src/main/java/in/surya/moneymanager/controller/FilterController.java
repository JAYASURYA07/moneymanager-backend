package in.surya.moneymanager.controller;

import in.surya.moneymanager.dto.ExpenseDto;
import in.surya.moneymanager.dto.FilterDto;
import in.surya.moneymanager.dto.IncomeDto;
import in.surya.moneymanager.service.ExpenseService;
import in.surya.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.SortedMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransaction(@RequestBody FilterDto filter){
        LocalDate starDate=filter.getStartdate()!=null?filter.getStartdate():LocalDate.MIN;
        LocalDate endDate=filter.getEnddate()!=null?filter.getEnddate():LocalDate.now();
        String keyword=filter.getKeyword()!=null?filter.getKeyword():"";
        String sortField=filter.getSortField()!=null?filter.getSortField():"date";
        Sort.Direction direction =
                "desc".equalsIgnoreCase(filter.getSortOrder())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC; Sort sort=Sort.by(direction,sortField);
   if("income".equalsIgnoreCase(filter.getType())){
       List<IncomeDto> income=incomeService.filterincome(starDate,endDate,keyword,sort);
return ResponseEntity.ok(income);

   }
   else if("expense".equalsIgnoreCase(filter.getType() )){
       List<ExpenseDto> expense=expenseService.filterExpenses(starDate,endDate,keyword,sort);
       return ResponseEntity.ok(expense);

   }
   else{
       return ResponseEntity.badRequest().body("Invalid type.Must be 'income' or 'expense'");
   }
    }
}

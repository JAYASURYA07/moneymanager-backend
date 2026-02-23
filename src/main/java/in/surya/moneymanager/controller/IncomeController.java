package in.surya.moneymanager.controller;

import in.surya.moneymanager.dto.ExpenseDto;
import in.surya.moneymanager.dto.IncomeDto;
import in.surya.moneymanager.service.ExpenseService;
import in.surya.moneymanager.service.IncomeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.analysis.function.Exp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<IncomeDto> addIncome(@RequestBody IncomeDto dto) {

        IncomeDto saved = incomeService.addIncome(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDto>>getIncomes(){
        List<IncomeDto>income=incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.ok(income);
    }



@DeleteMapping("/{id}")
public ResponseEntity<Void>deleteIncomeById(@PathVariable Long id){
    incomeService.deleteIncomeById(id);
    return ResponseEntity.noContent().build();
}

}

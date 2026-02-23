package in.surya.moneymanager.controller;

import in.surya.moneymanager.dto.ExpenseDto;
import in.surya.moneymanager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody ExpenseDto dto) {

        ExpenseDto saved = expenseService.addExpense(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @GetMapping
    public ResponseEntity<List<ExpenseDto>>getExpenses(){
        List<ExpenseDto>expenses=expenseService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteexpense(@PathVariable Long id){
       expenseService.deleteExpenseById(id);
       return ResponseEntity.noContent().build();
    }
}

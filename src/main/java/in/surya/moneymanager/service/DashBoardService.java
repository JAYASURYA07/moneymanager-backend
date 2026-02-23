package in.surya.moneymanager.service;
import java.util.*;
import java.util.stream.Collectors;

import in.surya.moneymanager.dto.ExpenseDto;
import in.surya.moneymanager.dto.IncomeDto;
import in.surya.moneymanager.dto.RecentTransactionDto;
import in.surya.moneymanager.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;


    public Map<String,Object> getDashBoard() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<IncomeDto> latestincomes = incomeService.getLatest5incomeforcurrentuser();
        List<ExpenseDto> latestexpenses = expenseService.getLatest5expensesforcurrentuser();
  List<RecentTransactionDto>recentTransactions=  concat(latestincomes.stream().map(income -> RecentTransactionDto.builder()
                .id(income.getId())
                .profileId(profile.getId())
                .icon(income.getIcon())
                .name(income.getName())
                .amount(income.getAmount())
                .date(income.getDate())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .type("income").build()), latestexpenses.stream().map(expense ->
                RecentTransactionDto.builder().id(expense.getId()).
                        profileId(profile.getId()).icon(expense.getIcon())
                        .name(expense.getName())
                        .amount(expense.getAmount())
                        .type("expense").date(expense.getDate()).createdAt(expense.getCreatedAt()).updatedAt(expense.getUpdatedAt()).build())).
                sorted((a, b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());

                    }
                    return cmp;
                }).collect(Collectors.toList());
        returnValue.put("Total Balance",incomeService.getTotalIncomeForCurrentUser().
                subtract(expenseService.getTotalExpenseForCurrentUser()));

        returnValue.put("Total Income ",incomeService.getTotalIncomeForCurrentUser());
returnValue.put("Total Expenses",expenseService.getTotalExpenseForCurrentUser());
returnValue.put("Recet 5 Incomes ",recentTransactions);
return returnValue;

    }


}

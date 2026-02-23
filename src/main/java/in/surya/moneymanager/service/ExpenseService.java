package in.surya.moneymanager.service;

import in.surya.moneymanager.dto.ExpenseDto;
import in.surya.moneymanager.entity.CategoryEntity;
import in.surya.moneymanager.entity.ExpenseEntity;
import in.surya.moneymanager.entity.ProfileEntity;
import in.surya.moneymanager.repository.CategoryRepo;
import in.surya.moneymanager.repository.ExpenseRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.analysis.function.Exp;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepo categoryrepo;
    private final ExpenseRepo expenseRepo;
    private final ProfileService profileService;

    public ExpenseDto addExpense(ExpenseDto dto) {

        ProfileEntity profile = profileService.getCurrentProfile();

        CategoryEntity category = categoryrepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category Not Found"));

        ExpenseEntity newExpense = toEntity(dto, profile, category);

        newExpense = expenseRepo.save(newExpense);

        return toDTo(newExpense);
    }

    public List<ExpenseDto> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        LocalDate now=LocalDate.now();
LocalDate startdate=now.withDayOfMonth(1);
LocalDate enddate=now.withDayOfMonth(now.lengthOfMonth());
List<ExpenseEntity>list=expenseRepo.findByProfileIdAndDateBetween(profile.getId(),startdate,enddate);
return list.stream().map(this::toDTo).toList();
    }

public void deleteExpenseById(Long expenseId){
        ProfileEntity profile=profileService.getCurrentProfile();
        ExpenseEntity entity=expenseRepo.findById(expenseId).orElseThrow(()->new RuntimeException("Expense Not Found"));
        if(!entity.getProfile().getId().equals(profile.getId())){
throw new RuntimeException("Unauthorized to delete this expense");
        }
        expenseRepo.delete(entity);
}

public List<ExpenseDto>getLatest5expensesforcurrentuser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<ExpenseEntity> list=expenseRepo.findByProfileIdOrderByDateDesc(profile.getId());
       return list.stream().map(this::toDTo).toList();
}

public BigDecimal getTotalExpenseForCurrentUser(){
        ProfileEntity profile= profileService.getCurrentProfile();
BigDecimal total=expenseRepo.findTotalExpenseByProfileId(profile.getId());
    return total!=null ? total:BigDecimal.ZERO;
    }


public List<ExpenseDto>filterExpenses(LocalDate startdate, LocalDate enddate, String keyword, Sort sort){
   ProfileEntity profile=profileService.getCurrentProfile();
   List<ExpenseEntity>list=expenseRepo.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startdate,enddate,keyword,sort);
return list.stream().map(this::toDTo).toList();
}

public List<ExpenseDto> getExpensesForUserOnDate(Long profileId,LocalDate date){
        List<ExpenseEntity> list=expenseRepo.findByProfileIdAndDate(profileId,date);
        return list.stream().map(this::toDTo).toList();
}

private ExpenseEntity toEntity(ExpenseDto dto,
                                   ProfileEntity profile,
                                   CategoryEntity category) {

        return ExpenseEntity.builder()
                .name(dto.getName())

                .icon(dto.getIcon())
                .date(dto.getDate())
                .amount(dto.getAmount())

                .category(category)
                .profile(profile)
                .build();
    }

    private ExpenseDto toDTo(ExpenseEntity entity) {

        return ExpenseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryName(entity.getCategory() != null
                        ? entity.getCategory().getName()
                        : "N/A")
                .categoryId(entity.getCategory() != null
                        ? entity.getCategory().getId()
                        : null)
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

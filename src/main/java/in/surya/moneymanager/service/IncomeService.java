package in.surya.moneymanager.service;


import in.surya.moneymanager.dto.ExpenseDto;
import in.surya.moneymanager.dto.IncomeDto;
import in.surya.moneymanager.entity.CategoryEntity;

import in.surya.moneymanager.entity.ExpenseEntity;
import in.surya.moneymanager.entity.IncomeEntity;
import in.surya.moneymanager.entity.ProfileEntity;
import in.surya.moneymanager.repository.CategoryRepo;
import in.surya.moneymanager.repository.IncomeRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final CategoryRepo categoryrepo;
    private final IncomeRepo incomeRepo;
    private final ProfileService profileService;

    public IncomeDto addIncome(IncomeDto dto) {

        ProfileEntity profile = profileService.getCurrentProfile();

        CategoryEntity category = categoryrepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category Not Found"));

        IncomeEntity newExpense = toEntity(dto, profile, category);

        newExpense = incomeRepo.save(newExpense); // ✅ fixed

        return toDTo(newExpense);
    }


 public List<IncomeDto> getCurrentMonthIncomesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        LocalDate now=LocalDate.now();
        LocalDate startdate=now.withDayOfMonth(1);
        LocalDate enddate=now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity>list=incomeRepo.findByProfileIdAndDateBetween(profile.getId(),startdate,enddate);
        return list.stream().map(this::toDTo).toList();
    }

    public void deleteIncomeById(Long incomeId){
        ProfileEntity profile=profileService.getCurrentProfile();
        IncomeEntity entity=incomeRepo.findById(incomeId).orElseThrow(()->new RuntimeException("Income Not Found"));
        if(!entity.getProfile().getId().equals(profile.getId())){
            throw new RuntimeException("Unauthorized to delete this income");
        }
        incomeRepo.delete(entity);
    }


    public List<IncomeDto>getLatest5incomeforcurrentuser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<IncomeEntity> list=incomeRepo.findByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTo).toList();
    }

    public BigDecimal getTotalIncomeForCurrentUser(){
        ProfileEntity profile= profileService.getCurrentProfile();
        BigDecimal total=incomeRepo.findTotalIncomeByProfileId(profile.getId());
        return total!=null ? total:BigDecimal.ZERO;
    }


    public List<IncomeDto>filterincome(LocalDate startdate, LocalDate enddate, String keyword, Sort sort){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<IncomeEntity>list=incomeRepo.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startdate,enddate,keyword,sort);
        return list.stream().map(this::toDTo).toList();
    }

    private IncomeEntity toEntity(IncomeDto dto,
                                  ProfileEntity profile,
                                  CategoryEntity category) {

        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .date(dto.getDate())
                .amount(dto.getAmount())
                .category(category).profile(profile).build();

    }

    private IncomeDto toDTo(IncomeEntity entity) {

        return IncomeDto.builder()
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

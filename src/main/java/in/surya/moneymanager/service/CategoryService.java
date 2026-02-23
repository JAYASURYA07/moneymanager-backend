package in.surya.moneymanager.service;

import in.surya.moneymanager.dto.CategoryDto;
import in.surya.moneymanager.entity.CategoryEntity;
import in.surya.moneymanager.entity.ProfileEntity;
import in.surya.moneymanager.repository.CategoryRepo;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryService {
private final ProfileService profileService;
private final CategoryRepo categoryRepo;
    private final EntityManagerFactory entityManagerFactory;

    public CategoryDto saveCategory (CategoryDto categoryDto){
        ProfileEntity profile=profileService.getCurrentProfile();
        if(categoryRepo.existsByNameAndProfile_Id(categoryDto.getName(),
                profile.getId()))
        {
            throw new RuntimeException("Category with this name already exists ");
        }
        CategoryEntity newCategory=toEntity(categoryDto,profile);
        newCategory=categoryRepo.save(newCategory);
        return toDto(newCategory);
    }

    public List<CategoryDto> getCategoriesforcurrentuser(){
ProfileEntity profile=profileService.getCurrentProfile();
List<CategoryEntity>categories=categoryRepo.findByProfile_Id(profile.getId());
return categories.stream().map(this::toDto).toList();
    }

    public List<CategoryDto> getcategoriesBytypeforcurrentuser(String type){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<CategoryEntity>entities=categoryRepo.findByTypeAndProfile_Id(type, profile.getId());
        return entities.stream().map(this::toDto).toList();
    }

    public CategoryDto updatecategory(Long categoryId,CategoryDto dto){
        ProfileEntity profile=profileService.getCurrentProfile();
      CategoryEntity existingCategory= categoryRepo.findByIdAndProfile_Id(categoryId, profile.getId())
                .orElseThrow(()->new RuntimeException("Category NOT FOUND OR NOT ACCESSIBLE"));
        existingCategory.setName(dto.getName());
        existingCategory.setIcon(dto.getIcon());
        existingCategory.setType(dto.getType());
        existingCategory=categoryRepo.save(existingCategory);
        return toDto(existingCategory);
    }


    private CategoryEntity toEntity(CategoryDto categoryDto, ProfileEntity profile){
    return CategoryEntity.builder()
            .name(categoryDto.getName())
            .icon(categoryDto.getIcon())
            .profile(profile)
            .type(categoryDto.getType())
            .build();
}
private CategoryDto toDto(CategoryEntity entity){
    return CategoryDto.builder().id(entity.getId())
            .profileId(entity.getProfile()!=null ? entity.getProfile().getId():null )
            .name(entity.getName())
            .icon(entity.getIcon())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .type(entity.getType())
            .build();
}
}

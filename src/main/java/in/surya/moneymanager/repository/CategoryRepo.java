package in.surya.moneymanager.repository;

import in.surya.moneymanager.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {
List<CategoryEntity> findByProfile_Id(Long profileId);

Optional <CategoryEntity>findByIdAndProfile_Id(Long id,Long profileId);
List<CategoryEntity>findByTypeAndProfile_Id(String type,Long profileId);


    Boolean existsByNameAndProfile_Id(String name, Long profileId);
}

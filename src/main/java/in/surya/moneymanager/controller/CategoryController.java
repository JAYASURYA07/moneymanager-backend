package in.surya.moneymanager.controller;

import in.surya.moneymanager.dto.CategoryDto;
import in.surya.moneymanager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto>saveCategory(@RequestBody CategoryDto categoryDto){
       CategoryDto savedCategory=categoryService.saveCategory(categoryDto);
       return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>>getCategories(){
        List<CategoryDto>categories=categoryService.getCategoriesforcurrentuser();
   return ResponseEntity.ok(categories);
    }

    @GetMapping("/{type}")
    public  ResponseEntity<List<CategoryDto>>getcategoriesBytypeforcurrentuser(@PathVariable String type){
List<CategoryDto>list=categoryService.getcategoriesBytypeforcurrentuser(type);
return ResponseEntity.ok(list);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto>updatecategory(@PathVariable Long categoryId
    ,@RequestBody CategoryDto categoryDto){
        CategoryDto updatedcategory =categoryService.updatecategory(categoryId,categoryDto);
return ResponseEntity.ok(updatedcategory);
    }
}

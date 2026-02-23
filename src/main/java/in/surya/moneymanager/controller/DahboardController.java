package in.surya.moneymanager.controller;

import in.surya.moneymanager.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DahboardController {
   private final DashBoardService dashBoardService;

   @GetMapping
    public ResponseEntity<Map<String,Object>>getDashboard(){
    Map<String,Object>dashboarddata=dashBoardService.getDashBoard();
    return ResponseEntity.ok(dashboarddata);
   }
}

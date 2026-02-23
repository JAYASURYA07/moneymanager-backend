package in.surya.moneymanager.controller;

import in.surya.moneymanager.dto.AuthDTO;
import in.surya.moneymanager.dto.ProfileDTO;
import in.surya.moneymanager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileservice;



    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerprofile(@RequestBody ProfileDTO profileDto){
ProfileDTO registerProfile=profileservice.registerProfile(profileDto);
return ResponseEntity.status(HttpStatus.CREATED).body(registerProfile);}


    @GetMapping("/activate")
    public ResponseEntity<String>activateProfile(@RequestParam String token){
        boolean isActivated=profileservice.activateProfile(token);
        if(isActivated){
            return ResponseEntity.ok("Profile Activated Successfully");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation Token Not Found or Already Used ");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login (@RequestBody AuthDTO authdto) {
        try {
            if (!profileservice.isAccountActive(authdto.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "message", "Account is not active.Please activate your account first"
                ));
            }
            Map<String,Object>response=profileservice.authenticateAndGenerateToken(authdto);

       return ResponseEntity.ok(response); }
        catch (Exception e) {
return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
        "message",e.getMessage()));

        }
    }
    @GetMapping("/test")
    public String test(){
        return "TEST SUCCESSFUL";
    }
}

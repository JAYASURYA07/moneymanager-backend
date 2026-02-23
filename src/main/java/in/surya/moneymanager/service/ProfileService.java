package in.surya.moneymanager.service;
import in.surya.moneymanager.dto.AuthDTO;
import in.surya.moneymanager.dto.ProfileDTO;
import in.surya.moneymanager.entity.ProfileEntity;
import in.surya.moneymanager.repository.ProfileRepository;
import in.surya.moneymanager.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profilerepository;
    private final EmailService emailservice;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ProfileDTO registerProfile(ProfileDTO profileDto) {
        ProfileEntity newprofile = toEntity(profileDto);
        newprofile.setActivationToken(UUID.randomUUID().toString());
        newprofile = profilerepository.save(newprofile);

        String activationLink = "http://localhost:8081/api/v1.0/activate?token=" + newprofile.getActivationToken();
        String subject = "Activate your Money Manager Account";
        String body = "Click on the following link to activate your account : " + activationLink;
        emailservice.sendEmail(newprofile.getEmail(), subject, body);
        return todto(newprofile);
    }

    public ProfileEntity toEntity(ProfileDTO profileDto) {
        return ProfileEntity.builder().id(profileDto.getId())
                .fullName(profileDto.getFullName())
                .email(profileDto.getEmail())
                .passkey(passwordEncoder.encode(profileDto.getPasskey()))
                .profileImageUrl(profileDto.getProfileImageUrl())
                .createdAt(profileDto.getCreatedAt())
                .updatedAt(profileDto.getUpdatedAt())
                .build();
    }

    public ProfileDTO todto(ProfileEntity profileEntity) {
        return ProfileDTO.builder().id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .passkey(profileEntity.getPasskey())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .UpdatedAt(profileEntity.getUpdatedAt())
                .build();
    }

    public boolean activateProfile(String activationToken) {
        return profilerepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profilerepository.save(profile);
                    return true;
                })
                .orElse(false);
    }

    public boolean isAccountActive(String email) {
        return profilerepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      return  profilerepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Profile Not Found with thi email : " + authentication.getName()));
    }

    public ProfileDTO getpublicProfile(String email){
        ProfileEntity currentUser=null;
        if(email==null){
            currentUser=getCurrentProfile(); 
        }
        else{
            currentUser=profilerepository.findByEmail(email)
                    .orElseThrow(()->new UsernameNotFoundException("Profile Not Found with thi email : "+email));
        }
        return ProfileDTO.builder().id(currentUser.getId()).
                fullName(currentUser.getFullName()).email(currentUser.getEmail()).
                passkey(currentUser.getPasskey()).profileImageUrl(currentUser.getProfileImageUrl()).
                createdAt(currentUser.getCreatedAt()).
                UpdatedAt(currentUser.getUpdatedAt()).build();
    }


    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authdto) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authdto.getEmail(), authdto.getPasskey()));
String token=jwtUtil.generateToken(authdto.getEmail());
       return Map.of("token",token,
               "user",getpublicProfile(authdto.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException(("Invalid Email or Password"));
        }

    }

}

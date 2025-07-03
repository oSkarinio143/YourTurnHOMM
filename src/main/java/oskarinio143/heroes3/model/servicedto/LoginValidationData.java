package oskarinio143.heroes3.model.servicedto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import oskarinio143.heroes3.model.LoginForm;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class LoginValidationData {
    private String nextView;
    private String errorMessages = "";
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String confirmPassword;
    @NonNull
    private RedirectAttributes redirectAttributes;

    public void addErrorMessage(String message){
        errorMessages += message + "<br>";
    }
}

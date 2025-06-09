package oskarinio143.heroes3;

import org.springframework.stereotype.Service;

@Service
public class RedirectService {
    public String mainControllerChoice(int number){
        System.out.println(number);
        if(number == 1)
            return "/database";
        else if (number == 2)
            return "/comparison";
        else if (number == 3)
            return "/battle";
        throw new IncorrectNumber(1,3);
    }
}

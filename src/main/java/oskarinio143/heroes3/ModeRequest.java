package oskarinio143.heroes3;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ModeRequest {
    @NonNull
    private int number;

    @NonNull
    private Mode mode;
}

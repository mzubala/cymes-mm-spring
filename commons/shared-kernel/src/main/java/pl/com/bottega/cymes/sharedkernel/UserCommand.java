package pl.com.bottega.cymes.sharedkernel;

import lombok.Data;

@Data
public class UserCommand implements Command {
    private Long userId;
}

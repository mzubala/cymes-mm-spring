package pl.com.bottega.cymes.users.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserAccountRequest(
    @NotNull
    @Email
    String email,
    @NotBlank
    @Size(min = 6)
    String password,
    @NotBlank
    String firstName,
    @NotBlank
    String lastName
) {
}

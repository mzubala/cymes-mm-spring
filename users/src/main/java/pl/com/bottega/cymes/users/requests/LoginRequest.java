package pl.com.bottega.cymes.users.requests;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank String email, @NotBlank String password
) {

}

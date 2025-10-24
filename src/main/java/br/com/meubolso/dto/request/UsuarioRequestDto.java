package br.com.meubolso.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDto(
    @NotBlank(message = "login é obrigatório")
    @Size(max = 255, message = "login deve ter no máximo 255 caracteres")
    String login,

    @NotBlank(message = "password é obrigatório")
    @Size(min = 8, max = 255, message = "password deve ter entre 8 e 255 caracteres")
    String password,

    @NotBlank(message = "email é obrigatório")
    @Email(message = "email deve ser válido")
    @Size(max = 255, message = "email deve ter no máximo 255 caracteres")
    String email,

    @NotBlank(message = "role é obrigatório")
    @Pattern(regexp = "ADMIN|USER", message = "role deve ser ADMIN ou USER")
    @Size(max = 255, message = "role deve ter no máximo 255 caracteres")
    String role
) {}
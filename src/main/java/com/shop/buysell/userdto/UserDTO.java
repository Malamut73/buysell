package com.shop.buysell.userdto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class UserDTO {

    @NotEmpty(message = "Введите ваше имя")
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 букв")
    private String name;
    private String phoneNumber;
    @NotEmpty(message = "Введите адрес электронной почты")
    @Email(message = "Не коректный email")
    private String email;
    @NotBlank(message = "Введите пароль")
    private String password;
    @NotBlank(message = "Повторите пароль")
    private String matchPassword;
}

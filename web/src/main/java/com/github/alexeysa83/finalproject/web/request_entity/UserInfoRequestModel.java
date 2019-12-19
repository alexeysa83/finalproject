package com.github.alexeysa83.finalproject.web.request_entity;

import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;

import javax.validation.constraints.Pattern;

public class UserInfoRequestModel {
    @Pattern(regexp = "^([A-ZА-ЯЁ][a-zа-яё]{1,14})$|^$",
            message = "invalid.first")
    private String firstName;
    @Pattern(regexp = "^[A-ZА-ЯЁ][a-zа-яё]{1,14}$|^$",
            message = "invalid.last")
    private String lastName;
    @Pattern(regexp = "^[\\w]{2,20}[@][\\w]{2,6}[.][a-z]{2,6}$|^$", message = "invalid.email")
    private String email;
    @Pattern(regexp = "^[\\d]{8,12}$|^$", message = "invalid.phone")
    private String phone;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserInfoDto convertToUserInfoDto() {
        final UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setFirstName(firstName);
        userInfoDto.setLastName(lastName);
        userInfoDto.setEmail(email);
        userInfoDto.setPhone(phone);
        return userInfoDto;
    }
}

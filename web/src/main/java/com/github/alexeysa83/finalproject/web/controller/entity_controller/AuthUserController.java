package com.github.alexeysa83.finalproject.web.controller.entity_controller;

import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.auth.AuthUserService;
import com.github.alexeysa83.finalproject.web.request_entity.AuthUserRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.Utils.WebAuthenticationUtils.*;
import static com.github.alexeysa83.finalproject.web.Utils.MessageContainer.*;

@Controller
@RequestMapping(value = "/auth_users")
public class AuthUserController {

    private static final Logger log = LoggerFactory.getLogger(AuthUserController.class);

    private final AuthUserService authUserService;

    public AuthUserController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @GetMapping("/forward_to_registration_form")
    public String forwardToRegistrationFormJSP() {
        return "registration_form";
    }

    /**
     * Messages from BindingResult + null?
     */
    @PostMapping(value = "/registration")
    public String addUser(@Valid AuthUserRequestModel userFromRegisterForm,
                          BindingResult br,
                          ModelMap modelMap,
                          RedirectAttributesModelMap redirectModelMap) {
        final String RETURN_PAGE_FAIL_TO_ADD = "registration_form";
        final String RETURN_PAGE_SUCCESS_ADD = "redirect:/user_infos/";

        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            modelMap.addAttribute(ERROR_ATTRIBUTE, errorMessage);
//            final List<ObjectError> allErrors = br.getAllErrors();
//            for (ObjectError allError : allErrors) {
//                model.put("message", allError.getDefaultMessage());
//            }
            return RETURN_PAGE_FAIL_TO_ADD;
        }

        if (!userFromRegisterForm.isPasswordMatchRepeatPassword()) {
            modelMap.addAttribute(ERROR_ATTRIBUTE, INVALID_REPEAT_PASS);
            return RETURN_PAGE_FAIL_TO_ADD;
        }

        final AuthUserDto userFromDB = authUserService.createAuthUserAndUserInfo(
                userFromRegisterForm.convertToAuthUserDto());
        if (userFromDB == null) {
            modelMap.addAttribute(ERROR_ATTRIBUTE, LOGIN_IS_TAKEN);
            return RETURN_PAGE_FAIL_TO_ADD;
        }
        setUserInSession(userFromDB);
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_REGISTRATION);
        log.info("User id: {} registered at: {}", userFromDB.getId(), LocalDateTime.now());
        return RETURN_PAGE_SUCCESS_ADD + userFromDB.getId();
    }

    /**
     * Optimization
     * <p>
     * get FromAuthentification instead of get by id?
     */
    @PostMapping(value = "/{userToUpdateId}/update_login")
    public String updateAuthUserLogin(@PathVariable Long userToUpdateId,
                                      @Valid AuthUserRequestModel userFromUpdateForm,
                                      BindingResult br,
                                      RedirectAttributesModelMap redirectModelMap) {


        final String RETURN_PAGE_FAIL_UPDATE_LOGIN = "redirect:/user_infos/" + userToUpdateId + "?returnPage=user_update";
        final String RETURN_PAGE_SUCCESS_UPDATE_LOGIN = "redirect:/logout_custom";

        if (!getPrincipalUserAuthId().equals(userToUpdateId)) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE_FAIL_UPDATE_LOGIN;
        }

        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, errorMessage);
            return RETURN_PAGE_FAIL_UPDATE_LOGIN;
        }

        final String updatedLogin = userFromUpdateForm.getLogin();
        final boolean isLoginTaken = authUserService.checkLoginIsTaken(updatedLogin);
        if (isLoginTaken) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, LOGIN_IS_TAKEN);
            return RETURN_PAGE_FAIL_UPDATE_LOGIN;
        }

        final AuthUserDto userToUpdate = authUserService.getById(userToUpdateId);
        userToUpdate.setLogin(updatedLogin);
        final boolean isUpdated = authUserService.updateAuthUser(userToUpdate);

        if (!isUpdated) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_UPDATE);
            log.error("Failed to update login for user id: {}, at: {}", userToUpdateId, LocalDateTime.now());
            return RETURN_PAGE_FAIL_UPDATE_LOGIN;
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_UPDATE);
        log.info("Updated login for user id: {}, at: {}", userToUpdateId, LocalDateTime.now());
        return RETURN_PAGE_SUCCESS_UPDATE_LOGIN;
    }

    @PostMapping(value = "/{userToUpdateId}/update_password")
    public String updateAuthUserPassword(@PathVariable Long userToUpdateId,
                                         @RequestParam(name = "passwordCurrent") String passwordCurrent,
                                         @Valid AuthUserRequestModel userFromUpdateForm,
                                         BindingResult br,
                                         RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE_FAIL_UPDATE_PASSWORD = "redirect:/user_infos/" + userToUpdateId + "?returnPage=user_update";
        final String RETURN_PAGE_SUCCESS_UPDATE_PASSWORD = "redirect:/logout_custom";

        if (!getPrincipalUserAuthId().equals(userToUpdateId)) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE_FAIL_UPDATE_PASSWORD;
        }

        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, errorMessage);
            return RETURN_PAGE_FAIL_UPDATE_PASSWORD;
        }

        //New password and new repeat password check
        if (!userFromUpdateForm.isPasswordMatchRepeatPassword()) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, INVALID_REPEAT_PASS);
            return RETURN_PAGE_FAIL_UPDATE_PASSWORD;
        }

        final AuthUserDto userToUpdate = authUserService.getById(userToUpdateId);

        //Current password of updated user entered on jsp is compared with current password from DB
        final boolean isValid = passwordCurrent.equals(userToUpdate.getPassword());
        if (!isValid) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, WRONG_PASSWORD_ENTERED);
            log.info("Invalid password enter for user id: {} at: {}", userToUpdateId, LocalDateTime.now());
            return RETURN_PAGE_FAIL_UPDATE_PASSWORD;
        }

        userToUpdate.setPassword(userFromUpdateForm.getPassword());
        final boolean isUpdated = authUserService.updateAuthUser(userToUpdate);
        if (!isUpdated) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_UPDATE);
            log.error("Failed to update password for user id: {} , at: {}", userToUpdateId, LocalDateTime.now());
            return RETURN_PAGE_FAIL_UPDATE_PASSWORD;
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_UPDATE);
        log.info("Updated password for user id: {}, at: {}", userToUpdateId, LocalDateTime.now());
        return RETURN_PAGE_SUCCESS_UPDATE_PASSWORD;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{userToUpdateId}/update_role")
    public String updateAuthUserRole(@PathVariable Long userToUpdateId,
                                     @Valid AuthUserRequestModel userFromUpdateForm,
                                     BindingResult br,
                                     RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE_FAIL_UPDATE_ROLE = "redirect:/user_infos/" + userToUpdateId + "?returnPage=user_update";
        final String RETURN_PAGE_SUCCESS_UPDATE_ROLE_NEED_LOGOUT = "redirect:/logout_custom";
        final String RETURN_PAGE_SUCCESS_UPDATE_ROLE_AND_NO_LOGOUT = "redirect:/user_infos/" + userToUpdateId;

        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, errorMessage);
            log.error("Failed to update role to user id: {} , at: {}", userToUpdateId, LocalDateTime.now());
            return RETURN_PAGE_FAIL_UPDATE_ROLE;
        }

        final AuthUserDto userToUpdate = authUserService.getById(userToUpdateId);
        final Role updatedRole = userFromUpdateForm.getRoleInEnum();
        userToUpdate.setRole(updatedRole);

        final boolean isUpdated = authUserService.updateAuthUser(userToUpdate);
        if (!isUpdated) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_UPDATE);
            log.error("Failed to update role to user id: {} , at: {}", userToUpdateId, LocalDateTime.now());
            return RETURN_PAGE_FAIL_UPDATE_ROLE;
        }

        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_UPDATE);
        log.info("Updated role to user id: {} , at: {}", userToUpdateId, LocalDateTime.now());

        final boolean needLogout = getPrincipalUserAuthId().equals(userToUpdateId);
        if (needLogout) {
            return RETURN_PAGE_SUCCESS_UPDATE_ROLE_NEED_LOGOUT;
        }
        return RETURN_PAGE_SUCCESS_UPDATE_ROLE_AND_NO_LOGOUT;
    }

    @PostMapping(value = "/{userToDeleteId}/delete")
    public String deleteAuthUser(@PathVariable Long userToDeleteId,
                                 RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE_NEED_LOGOUT = "redirect:/logout_custom";
        final String RETURN_PAGE_NO_LOGOUT = "redirect:/user_infos/" + userToDeleteId;

        if ((!getPrincipalUserAuthId().equals(userToDeleteId)) & !(isPrincipalUserAdmin())) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE_NO_LOGOUT;
        }

        final boolean isDeleted = authUserService.deleteUser(userToDeleteId);
        if (!isDeleted) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_DELETE);
            log.error("Failed to delete user id: {} , at: {}", userToDeleteId, LocalDateTime.now());
            return RETURN_PAGE_NO_LOGOUT;
        }

        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_DELETE);
        log.info("User id: {} deleted at: {}", userToDeleteId, LocalDateTime.now());

        final boolean needLogout = getPrincipalUserAuthId().equals(userToDeleteId);
        if (needLogout) {
            return RETURN_PAGE_NEED_LOGOUT;
        }
        return RETURN_PAGE_NO_LOGOUT;
    }
}

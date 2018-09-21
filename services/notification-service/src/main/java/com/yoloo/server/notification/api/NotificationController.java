package com.yoloo.server.notification.api;

import com.yoloo.server.notification.usecase.ListNotificationsUseCase;
import com.yoloo.server.notification.vo.NotificationCollectionResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@Validated
@RequestMapping("/api/notifications")
@RestController
public class NotificationController {

  private final ListNotificationsUseCase listNotificationsUseCase;

  public NotificationController(ListNotificationsUseCase listNotificationsUseCase) {
    this.listNotificationsUseCase = listNotificationsUseCase;
  }

  @GetMapping
  public NotificationCollectionResponse listNotifications(
      @RequestParam("userId") long userId,
      @NotEmpty @RequestParam(value = "cursor", required = false) String cursor) {
    return listNotificationsUseCase.execute(userId, cursor);
  }
}

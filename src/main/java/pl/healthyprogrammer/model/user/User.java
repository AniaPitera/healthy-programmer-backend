package pl.healthyprogrammer.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import pl.healthyprogrammer.common.AuditBase;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends AuditBase {

    private String email;
    private String username;
    private String password;
    private boolean enabled;
    private String avatar;
    private boolean allowNotifications;
    private Long notificationInterval;
    private LocalDateTime lastNotificationSent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return enabled == user.enabled && allowNotifications == user.allowNotifications && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(avatar, user.avatar) && Objects.equals(notificationInterval, user.notificationInterval) && Objects.equals(lastNotificationSent, user.lastNotificationSent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username, password, enabled, avatar, allowNotifications, notificationInterval, lastNotificationSent);
    }
}

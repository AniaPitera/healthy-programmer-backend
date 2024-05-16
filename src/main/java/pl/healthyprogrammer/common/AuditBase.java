package pl.healthyprogrammer.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @CreatedBy
    protected UUID createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    @LastModifiedBy
    protected UUID lastModifiedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedAt;
}

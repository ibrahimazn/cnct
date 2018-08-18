package com.appfiss.account.util;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuppressWarnings("serial")
@MappedSuperclass
@EntityListeners({ AuditingEntityListener.class })
public abstract class ImmutableEntity {

	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "uuid", columnDefinition = "VARCHAR(255)")
	private UUID uuid;

	@Basic
	@CreatedDate
	@Column(name = "CREATED_AT", nullable = false, insertable = true, updatable = false, columnDefinition = "TIMESTAMP NOT NULL")
	private Instant createdAt;

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public UUID getUuid() {
		return uuid;
	}

	@PrePersist
	public void setUuid(UUID uuid) {
		this.uuid = UUID.randomUUID();
	}
}
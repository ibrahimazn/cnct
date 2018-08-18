package com.assistanz.slmlp.container.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * @author ibrahim
 *
 */
@Entity
@Table(name = "sampling")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public class Sampling implements Serializable {

  /** Id of the Dataset. */
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  /** Total number of records. */
  @Column(name = "sample_records")
  private Long sampledRecords;
  
  /** while row of records from. */
  @Column(name = "from_records")
  private Long from;
  
  /** while row of records from. */
  @Column(name = "to_records")
  private Long to;

  /** Train dataset ratio. */
  @Column(name = "train_ratio")
  private Float trainRatio;
  
  /** Test dataset ratio. */
  @Column(name = "test_ratio")
  private Float testRatio;

  /** Random seeds. */
  @Column(name = "random_seeds")
  private Float ramdomSeeds;

  /** Type of the Model. */
  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private TYPE modelType;

  /** Check whether model training has kfoldcross.*/
  @Column(name = "k_fold_cross")
  private Boolean kFoldCross;

  /**
   * Enum type for Model type.
   *
   */
  public enum TYPE {
    /** Predefined algorithm. */
    PREDEFINED,
    /** custom model creation. */
    CUSTOM
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSampledRecords() {
    return sampledRecords;
  }

  public void setSampledRecords(Long sampledRecords) {
    this.sampledRecords = sampledRecords;
  }

  public Float getRamdomSeeds() {
    return ramdomSeeds;
  }

  public void setRamdomSeeds(Float ramdomSeeds) {
    this.ramdomSeeds = ramdomSeeds;
  }

  public TYPE getModelType() {
    return modelType;
  }

  public void setModelType(TYPE modelType) {
    this.modelType = modelType;
  }

  public Boolean getkFoldCross() {
    return kFoldCross;
  }

  public void setkFoldCross(Boolean kFoldCross) {
    this.kFoldCross = kFoldCross;
  }

  public Long getFrom() {
    return from;
  }

  public void setFrom(Long from) {
    this.from = from;
  }

  public Long getTo() {
    return to;
  }

  public void setTo(Long to) {
    this.to = to;
  }

  public Float getTrainRatio() {
    return trainRatio;
  }

  public void setTrainRatio(Float trainRatio) {
    this.trainRatio = trainRatio;
  }

  public Float getTestRatio() {
    return testRatio;
  }

  public void setTestRatio(Float testRatio) {
    this.testRatio = testRatio;
  }
  
}

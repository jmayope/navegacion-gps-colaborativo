package com.ngcapp.utpdevs.ngc_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class SegmentDetail {

  private UUID id;

  @JsonProperty("segment_id")
  private UUID segmentId;

  private String instruction;

  @JsonProperty("instruction_type")
  private String instructionType; // 'turn', 'straight', 'arrive', 'depart'

  @JsonProperty("maneuver")
  private String maneuver; // 'turn-left', 'turn-right', 'u-turn', 'roundabout'

  @JsonProperty("distance_to_next")
  private BigDecimal distanceToNext; // en metros

  @JsonProperty("duration_to_next")
  private Integer durationToNext; // en segundos

  @JsonProperty("exit_number")
  private Integer exitNumber; // para salidas en autopistas

  @JsonProperty("side")
  private String side; // 'left', 'right'

  @JsonProperty("created_at")
  private OffsetDateTime createdAt;

  @JsonProperty("updated_at")
  private OffsetDateTime updatedAt;

  // Getters y Setters
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getSegmentId() {
    return segmentId;
  }

  public void setSegmentId(UUID segmentId) {
    this.segmentId = segmentId;
  }

  public String getInstruction() {
    return instruction;
  }

  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  public String getInstructionType() {
    return instructionType;
  }

  public void setInstructionType(String instructionType) {
    this.instructionType = instructionType;
  }

  public String getManeuver() {
    return maneuver;
  }

  public void setManeuver(String maneuver) {
    this.maneuver = maneuver;
  }

  public BigDecimal getDistanceToNext() {
    return distanceToNext;
  }

  public void setDistanceToNext(BigDecimal distanceToNext) {
    this.distanceToNext = distanceToNext;
  }

  public Integer getDurationToNext() {
    return durationToNext;
  }

  public void setDurationToNext(Integer durationToNext) {
    this.durationToNext = durationToNext;
  }

  public Integer getExitNumber() {
    return exitNumber;
  }

  public void setExitNumber(Integer exitNumber) {
    this.exitNumber = exitNumber;
  }

  public String getSide() {
    return side;
  }

  public void setSide(String side) {
    this.side = side;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
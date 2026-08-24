package com.ngcapp.utpdevs.ngc_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.UUID;

public class SegmentDetailRequest {

  @JsonProperty("segment_id")
  private UUID segmentId;

  private String instruction;

  @JsonProperty("instruction_type")
  private String instructionType;

  @JsonProperty("maneuver")
  private String maneuver;

  @JsonProperty("distance_to_next")
  private BigDecimal distanceToNext;

  @JsonProperty("duration_to_next")
  private Integer durationToNext;

  @JsonProperty("exit_number")
  private Integer exitNumber;

  @JsonProperty("side")
  private String side;

  // Getters y Setters
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
}
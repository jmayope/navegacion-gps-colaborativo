import 'package:flutter/foundation.dart';
import 'package:quiver/core.dart';

import 'index.dart';

@immutable
class Incident {

  const Incident({
    this.id,
    this.routeId,
    this.segmentId,
    this.userId,
    this.incidentType,
    this.severity,
    this.description,
    this.locationLat,
    this.locationLng,
    this.locationAddress,
    this.isPanic,
    this.isResolved,
    this.resolvedAt,
    this.reportPhotoUrl,
    this.createdAt,
    this.updatedAt,
  });

  final String? id;
  final String? routeId;
  final String? segmentId;
  final String? userId;
  final String? incidentType;
  final String? severity;
  final String? description;
  final double? locationLat;
  final double? locationLng;
  final String? locationAddress;
  final bool? isPanic;
  final bool? isResolved;
  final DateTime? resolvedAt;
  final String? reportPhotoUrl;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  factory Incident.fromJson(Map<String,dynamic> json) => Incident(
    id: json['id']?.toString(),
    routeId: json['route_id']?.toString(),
    segmentId: json['segment_id']?.toString(),
    userId: json['user_id']?.toString(),
    incidentType: json['incident_type']?.toString(),
    severity: json['severity']?.toString(),
    description: json['description']?.toString(),
    locationLat: json['location_lat'] != null ? (json['location_lat'] as num).toDouble() : null,
    locationLng: json['location_lng'] != null ? (json['location_lng'] as num).toDouble() : null,
    locationAddress: json['location_address']?.toString(),
    isPanic: json['is_panic'] != null ? json['is_panic'] as bool : null,
    isResolved: json['is_resolved'] != null ? json['is_resolved'] as bool : null,
    resolvedAt: json['resolved_at'] != null ? DateTime.parse(json['resolved_at'] as String) : null,
    reportPhotoUrl: json['report_photo_url']?.toString(),
    createdAt: json['created_at'] != null ? DateTime.parse(json['created_at'] as String) : null,
    updatedAt: json['updated_at'] != null ? DateTime.parse(json['updated_at'] as String) : null
  );
  
  Map<String, dynamic> toJson() => {
    'id': id,
    'route_id': routeId,
    'segment_id': segmentId,
    'user_id': userId,
    'incident_type': incidentType,
    'severity': severity,
    'description': description,
    'location_lat': locationLat,
    'location_lng': locationLng,
    'location_address': locationAddress,
    'is_panic': isPanic,
    'is_resolved': isResolved,
    'resolved_at': resolvedAt?.toIso8601String(),
    'report_photo_url': reportPhotoUrl,
    'created_at': createdAt?.toIso8601String(),
    'updated_at': updatedAt?.toIso8601String()
  };

  Incident clone() => Incident(
    id: id,
    routeId: routeId,
    segmentId: segmentId,
    userId: userId,
    incidentType: incidentType,
    severity: severity,
    description: description,
    locationLat: locationLat,
    locationLng: locationLng,
    locationAddress: locationAddress,
    isPanic: isPanic,
    isResolved: isResolved,
    resolvedAt: resolvedAt,
    reportPhotoUrl: reportPhotoUrl,
    createdAt: createdAt,
    updatedAt: updatedAt
  );


  Incident copyWith({
    Optional<String?>? id,
    Optional<String?>? routeId,
    Optional<String?>? segmentId,
    Optional<String?>? userId,
    Optional<String?>? incidentType,
    Optional<String?>? severity,
    Optional<String?>? description,
    Optional<double?>? locationLat,
    Optional<double?>? locationLng,
    Optional<String?>? locationAddress,
    Optional<bool?>? isPanic,
    Optional<bool?>? isResolved,
    Optional<DateTime?>? resolvedAt,
    Optional<String?>? reportPhotoUrl,
    Optional<DateTime?>? createdAt,
    Optional<DateTime?>? updatedAt
  }) => Incident(
    id: checkOptional(id, () => this.id),
    routeId: checkOptional(routeId, () => this.routeId),
    segmentId: checkOptional(segmentId, () => this.segmentId),
    userId: checkOptional(userId, () => this.userId),
    incidentType: checkOptional(incidentType, () => this.incidentType),
    severity: checkOptional(severity, () => this.severity),
    description: checkOptional(description, () => this.description),
    locationLat: checkOptional(locationLat, () => this.locationLat),
    locationLng: checkOptional(locationLng, () => this.locationLng),
    locationAddress: checkOptional(locationAddress, () => this.locationAddress),
    isPanic: checkOptional(isPanic, () => this.isPanic),
    isResolved: checkOptional(isResolved, () => this.isResolved),
    resolvedAt: checkOptional(resolvedAt, () => this.resolvedAt),
    reportPhotoUrl: checkOptional(reportPhotoUrl, () => this.reportPhotoUrl),
    createdAt: checkOptional(createdAt, () => this.createdAt),
    updatedAt: checkOptional(updatedAt, () => this.updatedAt),
  );

  @override
  bool operator ==(Object other) => identical(this, other)
    || other is Incident && id == other.id && routeId == other.routeId && segmentId == other.segmentId && userId == other.userId && incidentType == other.incidentType && severity == other.severity && description == other.description && locationLat == other.locationLat && locationLng == other.locationLng && locationAddress == other.locationAddress && isPanic == other.isPanic && isResolved == other.isResolved && resolvedAt == other.resolvedAt && reportPhotoUrl == other.reportPhotoUrl && createdAt == other.createdAt && updatedAt == other.updatedAt;

  @override
  int get hashCode => id.hashCode ^ routeId.hashCode ^ segmentId.hashCode ^ userId.hashCode ^ incidentType.hashCode ^ severity.hashCode ^ description.hashCode ^ locationLat.hashCode ^ locationLng.hashCode ^ locationAddress.hashCode ^ isPanic.hashCode ^ isResolved.hashCode ^ resolvedAt.hashCode ^ reportPhotoUrl.hashCode ^ createdAt.hashCode ^ updatedAt.hashCode;
}

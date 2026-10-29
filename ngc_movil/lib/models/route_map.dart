import 'package:flutter/foundation.dart';
import 'package:quiver/core.dart';

import 'index.dart';

@immutable
class RouteMap {

  const RouteMap({
    this.id,
    this.userId,
    this.originName,
    this.originLat,
    this.originLng,
    this.originAddress,
    this.destinationName,
    this.destinationLat,
    this.destinationLng,
    this.destinationAddress,
    this.estimatedDistance,
    this.estimatedDuration,
    this.isFavorite,
    this.status,
    this.inProgress,
    this.startedAt,
    this.completedAt,
    this.createdAt,
    this.updatedAt,
  });

  final String? id;
  final String? userId;
  final String? originName;
  final double? originLat;
  final double? originLng;
  final String? originAddress;
  final String? destinationName;
  final double? destinationLat;
  final double? destinationLng;
  final String? destinationAddress;
  final double? estimatedDistance;
  final int? estimatedDuration;
  final bool? isFavorite;
  final String? status;
  final bool? inProgress;
  final DateTime? startedAt;
  final dynamic? completedAt;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  factory RouteMap.fromJson(Map<String,dynamic> json) => RouteMap(
    id: json['id']?.toString(),
    userId: json['user_id']?.toString(),
    originName: json['origin_name']?.toString(),
    originLat: json['origin_lat'] != null ? (json['origin_lat'] as num).toDouble() : null,
    originLng: json['origin_lng'] != null ? (json['origin_lng'] as num).toDouble() : null,
    originAddress: json['origin_address']?.toString(),
    destinationName: json['destination_name']?.toString(),
    destinationLat: json['destination_lat'] != null ? (json['destination_lat'] as num).toDouble() : null,
    destinationLng: json['destination_lng'] != null ? (json['destination_lng'] as num).toDouble() : null,
    destinationAddress: json['destination_address']?.toString(),
    estimatedDistance: json['estimated_distance'] != null ? (json['estimated_distance'] as num).toDouble() : null,
    estimatedDuration: json['estimated_duration'] != null ? json['estimated_duration'] as int : null,
    isFavorite: json['is_favorite'] != null ? json['is_favorite'] as bool : null,
    status: json['status']?.toString(),
    inProgress: json['in_progress'] != null ? json['in_progress'] as bool : null,
    startedAt: json['started_at'] != null ? DateTime.parse(json['started_at'] as String) : null,
    completedAt: json['completed_at'] != null ? json['completed_at'] as dynamic : null,
    createdAt: json['created_at'] != null ? DateTime.parse(json['created_at'] as String) : null,
    updatedAt: json['updated_at'] != null ? DateTime.parse(json['updated_at'] as String) : null
  );
  
  Map<String, dynamic> toJson() => {
    'id': id,
    'user_id': userId,
    'origin_name': originName,
    'origin_lat': originLat,
    'origin_lng': originLng,
    'origin_address': originAddress,
    'destination_name': destinationName,
    'destination_lat': destinationLat,
    'destination_lng': destinationLng,
    'destination_address': destinationAddress,
    'estimated_distance': estimatedDistance,
    'estimated_duration': estimatedDuration,
    'is_favorite': isFavorite,
    'status': status,
    'in_progress': inProgress,
    'started_at': startedAt?.toIso8601String(),
    'completed_at': completedAt,
    'created_at': createdAt?.toIso8601String(),
    'updated_at': updatedAt?.toIso8601String()
  };

  RouteMap clone() => RouteMap(
    id: id,
    userId: userId,
    originName: originName,
    originLat: originLat,
    originLng: originLng,
    originAddress: originAddress,
    destinationName: destinationName,
    destinationLat: destinationLat,
    destinationLng: destinationLng,
    destinationAddress: destinationAddress,
    estimatedDistance: estimatedDistance,
    estimatedDuration: estimatedDuration,
    isFavorite: isFavorite,
    status: status,
    inProgress: inProgress,
    startedAt: startedAt,
    completedAt: completedAt,
    createdAt: createdAt,
    updatedAt: updatedAt
  );


  RouteMap copyWith({
    Optional<String?>? id,
    Optional<String?>? userId,
    Optional<String?>? originName,
    Optional<double?>? originLat,
    Optional<double?>? originLng,
    Optional<String?>? originAddress,
    Optional<String?>? destinationName,
    Optional<double?>? destinationLat,
    Optional<double?>? destinationLng,
    Optional<String?>? destinationAddress,
    Optional<double?>? estimatedDistance,
    Optional<int?>? estimatedDuration,
    Optional<bool?>? isFavorite,
    Optional<String?>? status,
    Optional<bool?>? inProgress,
    Optional<DateTime?>? startedAt,
    Optional<dynamic?>? completedAt,
    Optional<DateTime?>? createdAt,
    Optional<DateTime?>? updatedAt
  }) => RouteMap(
    id: checkOptional(id, () => this.id),
    userId: checkOptional(userId, () => this.userId),
    originName: checkOptional(originName, () => this.originName),
    originLat: checkOptional(originLat, () => this.originLat),
    originLng: checkOptional(originLng, () => this.originLng),
    originAddress: checkOptional(originAddress, () => this.originAddress),
    destinationName: checkOptional(destinationName, () => this.destinationName),
    destinationLat: checkOptional(destinationLat, () => this.destinationLat),
    destinationLng: checkOptional(destinationLng, () => this.destinationLng),
    destinationAddress: checkOptional(destinationAddress, () => this.destinationAddress),
    estimatedDistance: checkOptional(estimatedDistance, () => this.estimatedDistance),
    estimatedDuration: checkOptional(estimatedDuration, () => this.estimatedDuration),
    isFavorite: checkOptional(isFavorite, () => this.isFavorite),
    status: checkOptional(status, () => this.status),
    inProgress: checkOptional(inProgress, () => this.inProgress),
    startedAt: checkOptional(startedAt, () => this.startedAt),
    completedAt: checkOptional(completedAt, () => this.completedAt),
    createdAt: checkOptional(createdAt, () => this.createdAt),
    updatedAt: checkOptional(updatedAt, () => this.updatedAt),
  );

  @override
  bool operator ==(Object other) => identical(this, other)
    || other is RouteMap && id == other.id && userId == other.userId && originName == other.originName && originLat == other.originLat && originLng == other.originLng && originAddress == other.originAddress && destinationName == other.destinationName && destinationLat == other.destinationLat && destinationLng == other.destinationLng && destinationAddress == other.destinationAddress && estimatedDistance == other.estimatedDistance && estimatedDuration == other.estimatedDuration && isFavorite == other.isFavorite && status == other.status && inProgress == other.inProgress && startedAt == other.startedAt && completedAt == other.completedAt && createdAt == other.createdAt && updatedAt == other.updatedAt;

  @override
  int get hashCode => id.hashCode ^ userId.hashCode ^ originName.hashCode ^ originLat.hashCode ^ originLng.hashCode ^ originAddress.hashCode ^ destinationName.hashCode ^ destinationLat.hashCode ^ destinationLng.hashCode ^ destinationAddress.hashCode ^ estimatedDistance.hashCode ^ estimatedDuration.hashCode ^ isFavorite.hashCode ^ status.hashCode ^ inProgress.hashCode ^ startedAt.hashCode ^ completedAt.hashCode ^ createdAt.hashCode ^ updatedAt.hashCode;
}

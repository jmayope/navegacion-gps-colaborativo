import 'package:flutter/foundation.dart';
import 'package:quiver/core.dart';

import 'index.dart';

@immutable
class Route {

  const Route({
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
    this.startedAt,
    this.completedAt,
    this.createdAt,
    this.updatedAt,
  });

  final String? id;
  final String? userId;
  final String? originName;
  final String? originLat;
  final String? originLng;
  final String? originAddress;
  final String? destinationName;
  final String? destinationLat;
  final String? destinationLng;
  final String? destinationAddress;
  final String? estimatedDistance;
  final int? estimatedDuration;
  final bool? isFavorite;
  final String? status;
  final String? startedAt;
  final dynamic? completedAt;
  final String? createdAt;
  final String? updatedAt;

  factory Route.fromJson(Map<String,dynamic> json) => Route(
    id: json['id']?.toString(),
    userId: json['user_id']?.toString(),
    originName: json['origin_name']?.toString(),
    originLat: json['origin_lat']?.toString(),
    originLng: json['origin_lng']?.toString(),
    originAddress: json['origin_address']?.toString(),
    destinationName: json['destination_name']?.toString(),
    destinationLat: json['destination_lat']?.toString(),
    destinationLng: json['destination_lng']?.toString(),
    destinationAddress: json['destination_address']?.toString(),
    estimatedDistance: json['estimated_distance']?.toString(),
    estimatedDuration: json['estimated_duration'] != null ? json['estimated_duration'] as int : null,
    isFavorite: json['is_favorite'] != null ? json['is_favorite'] as bool : null,
    status: json['status']?.toString(),
    startedAt: json['started_at']?.toString(),
    completedAt: json['completed_at'] != null ? json['completed_at'] as dynamic : null,
    createdAt: json['created_at']?.toString(),
    updatedAt: json['updated_at']?.toString()
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
    'started_at': startedAt,
    'completed_at': completedAt,
    'created_at': createdAt,
    'updated_at': updatedAt
  };

  Route clone() => Route(
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
    startedAt: startedAt,
    completedAt: completedAt,
    createdAt: createdAt,
    updatedAt: updatedAt
  );


  Route copyWith({
    Optional<String?>? id,
    Optional<String?>? userId,
    Optional<String?>? originName,
    Optional<String?>? originLat,
    Optional<String?>? originLng,
    Optional<String?>? originAddress,
    Optional<String?>? destinationName,
    Optional<String?>? destinationLat,
    Optional<String?>? destinationLng,
    Optional<String?>? destinationAddress,
    Optional<String?>? estimatedDistance,
    Optional<int?>? estimatedDuration,
    Optional<bool?>? isFavorite,
    Optional<String?>? status,
    Optional<String?>? startedAt,
    Optional<dynamic?>? completedAt,
    Optional<String?>? createdAt,
    Optional<String?>? updatedAt
  }) => Route(
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
    startedAt: checkOptional(startedAt, () => this.startedAt),
    completedAt: checkOptional(completedAt, () => this.completedAt),
    createdAt: checkOptional(createdAt, () => this.createdAt),
    updatedAt: checkOptional(updatedAt, () => this.updatedAt),
  );

  @override
  bool operator ==(Object other) => identical(this, other)
    || other is Route && id == other.id && userId == other.userId && originName == other.originName && originLat == other.originLat && originLng == other.originLng && originAddress == other.originAddress && destinationName == other.destinationName && destinationLat == other.destinationLat && destinationLng == other.destinationLng && destinationAddress == other.destinationAddress && estimatedDistance == other.estimatedDistance && estimatedDuration == other.estimatedDuration && isFavorite == other.isFavorite && status == other.status && startedAt == other.startedAt && completedAt == other.completedAt && createdAt == other.createdAt && updatedAt == other.updatedAt;

  @override
  int get hashCode => id.hashCode ^ userId.hashCode ^ originName.hashCode ^ originLat.hashCode ^ originLng.hashCode ^ originAddress.hashCode ^ destinationName.hashCode ^ destinationLat.hashCode ^ destinationLng.hashCode ^ destinationAddress.hashCode ^ estimatedDistance.hashCode ^ estimatedDuration.hashCode ^ isFavorite.hashCode ^ status.hashCode ^ startedAt.hashCode ^ completedAt.hashCode ^ createdAt.hashCode ^ updatedAt.hashCode;
}

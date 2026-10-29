import 'package:flutter/foundation.dart';
import 'package:quiver/core.dart';

import 'index.dart';

@immutable
class User {

  const User({
    this.id,
    this.email,
    this.phone,
    this.fullName,
    this.documentType,
    this.documentNumber,
    this.isActive,
    this.isVerified,
    this.lastLocationLat,
    this.lastLocationLng,
    this.lastActivityAt,
    this.createdAt,
    this.updatedAt,
    this.password,
    this.isAdmin,
    this.notifications,
    this.shareLocation,
  });

  final String? id;
  final String? email;
  final String? phone;
  final String? fullName;
  final String? documentType;
  final String? documentNumber;
  final bool? isActive;
  final bool? isVerified;
  final String? lastLocationLat;
  final String? lastLocationLng;
  final String? lastActivityAt;
  final dynamic? createdAt;
  final dynamic? updatedAt;
  final String? password;
  final bool? isAdmin;
  final bool? notifications;
  final bool? shareLocation;

  factory User.fromJson(Map<String,dynamic> json) => User(
    id: json['id']?.toString(),
    email: json['email']?.toString(),
    phone: json['phone']?.toString(),
    fullName: json['full_name']?.toString(),
    documentType: json['document_type']?.toString(),
    documentNumber: json['document_number']?.toString(),
    isActive: json['is_active'] != null ? json['is_active'] as bool : null,
    isVerified: json['is_verified'] != null ? json['is_verified'] as bool : null,
    lastLocationLat: json['last_location_lat']?.toString(),
    lastLocationLng: json['last_location_lng']?.toString(),
    lastActivityAt: json['last_activity_at']?.toString(),
    createdAt: json['created_at'] != null ? json['created_at'] as dynamic : null,
    updatedAt: json['updated_at'] != null ? json['updated_at'] as dynamic : null,
    password: json['password']?.toString(),
    isAdmin: json['is_admin'] != null ? json['is_admin'] as bool : null,
    notifications: json['notifications'] != null ? json['notifications'] as bool : null,
    shareLocation: json['share_location'] != null ? json['share_location'] as bool : null
  );
  
  Map<String, dynamic> toJson() => {
    'id': id,
    'email': email,
    'phone': phone,
    'full_name': fullName,
    'document_type': documentType,
    'document_number': documentNumber,
    'is_active': isActive,
    'is_verified': isVerified,
    'last_location_lat': lastLocationLat,
    'last_location_lng': lastLocationLng,
    'last_activity_at': lastActivityAt,
    'created_at': createdAt,
    'updated_at': updatedAt,
    'password': password,
    'is_admin': isAdmin,
    'notifications': notifications,
    'share_location': shareLocation
  };

  User clone() => User(
    id: id,
    email: email,
    phone: phone,
    fullName: fullName,
    documentType: documentType,
    documentNumber: documentNumber,
    isActive: isActive,
    isVerified: isVerified,
    lastLocationLat: lastLocationLat,
    lastLocationLng: lastLocationLng,
    lastActivityAt: lastActivityAt,
    createdAt: createdAt,
    updatedAt: updatedAt,
    password: password,
    isAdmin: isAdmin,
    notifications: notifications,
    shareLocation: shareLocation
  );


  User copyWith({
    Optional<String?>? id,
    Optional<String?>? email,
    Optional<String?>? phone,
    Optional<String?>? fullName,
    Optional<String?>? documentType,
    Optional<String?>? documentNumber,
    Optional<bool?>? isActive,
    Optional<bool?>? isVerified,
    Optional<String?>? lastLocationLat,
    Optional<String?>? lastLocationLng,
    Optional<String?>? lastActivityAt,
    Optional<dynamic?>? createdAt,
    Optional<dynamic?>? updatedAt,
    Optional<String?>? password,
    Optional<bool?>? isAdmin,
    Optional<bool?>? notifications,
    Optional<bool?>? shareLocation
  }) => User(
    id: checkOptional(id, () => this.id),
    email: checkOptional(email, () => this.email),
    phone: checkOptional(phone, () => this.phone),
    fullName: checkOptional(fullName, () => this.fullName),
    documentType: checkOptional(documentType, () => this.documentType),
    documentNumber: checkOptional(documentNumber, () => this.documentNumber),
    isActive: checkOptional(isActive, () => this.isActive),
    isVerified: checkOptional(isVerified, () => this.isVerified),
    lastLocationLat: checkOptional(lastLocationLat, () => this.lastLocationLat),
    lastLocationLng: checkOptional(lastLocationLng, () => this.lastLocationLng),
    lastActivityAt: checkOptional(lastActivityAt, () => this.lastActivityAt),
    createdAt: checkOptional(createdAt, () => this.createdAt),
    updatedAt: checkOptional(updatedAt, () => this.updatedAt),
    password: checkOptional(password, () => this.password),
    isAdmin: checkOptional(isAdmin, () => this.isAdmin),
    notifications: checkOptional(notifications, () => this.notifications),
    shareLocation: checkOptional(shareLocation, () => this.shareLocation),
  );

  @override
  bool operator ==(Object other) => identical(this, other)
    || other is User && id == other.id && email == other.email && phone == other.phone && fullName == other.fullName && documentType == other.documentType && documentNumber == other.documentNumber && isActive == other.isActive && isVerified == other.isVerified && lastLocationLat == other.lastLocationLat && lastLocationLng == other.lastLocationLng && lastActivityAt == other.lastActivityAt && createdAt == other.createdAt && updatedAt == other.updatedAt && password == other.password && isAdmin == other.isAdmin && notifications == other.notifications && shareLocation == other.shareLocation;

  @override
  int get hashCode => id.hashCode ^ email.hashCode ^ phone.hashCode ^ fullName.hashCode ^ documentType.hashCode ^ documentNumber.hashCode ^ isActive.hashCode ^ isVerified.hashCode ^ lastLocationLat.hashCode ^ lastLocationLng.hashCode ^ lastActivityAt.hashCode ^ createdAt.hashCode ^ updatedAt.hashCode ^ password.hashCode ^ isAdmin.hashCode ^ notifications.hashCode ^ shareLocation.hashCode;
}

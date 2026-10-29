import 'package:flutter/foundation.dart';
import 'package:quiver/core.dart';

import 'index.dart';

@immutable
class UserContact {

  const UserContact({
    this.id,
    this.userId,
    this.fullName,
    this.phone,
    this.relationship,
    this.status,
    this.createdAt,
  });

  final String? id;
  final String? userId;
  final String? fullName;
  final String? phone;
  final String? relationship;
  final bool? status;
  final DateTime? createdAt;

  factory UserContact.fromJson(Map<String,dynamic> json) => UserContact(
    id: json['id']?.toString(),
    userId: json['user_id']?.toString(),
    fullName: json['full_name']?.toString(),
    phone: json['phone']?.toString(),
    relationship: json['relationship']?.toString(),
    status: json['status'] != null ? json['status'] as bool : null,
    createdAt: json['created_at'] != null ? DateTime.parse(json['created_at'] as String) : null
  );
  
  Map<String, dynamic> toJson() => {
    'id': id,
    'user_id': userId,
    'full_name': fullName,
    'phone': phone,
    'relationship': relationship,
    'status': status,
    'created_at': createdAt?.toIso8601String()
  };

  UserContact clone() => UserContact(
    id: id,
    userId: userId,
    fullName: fullName,
    phone: phone,
    relationship: relationship,
    status: status,
    createdAt: createdAt
  );


  UserContact copyWith({
    Optional<String?>? id,
    Optional<String?>? userId,
    Optional<String?>? fullName,
    Optional<String?>? phone,
    Optional<String?>? relationship,
    Optional<bool?>? status,
    Optional<DateTime?>? createdAt
  }) => UserContact(
    id: checkOptional(id, () => this.id),
    userId: checkOptional(userId, () => this.userId),
    fullName: checkOptional(fullName, () => this.fullName),
    phone: checkOptional(phone, () => this.phone),
    relationship: checkOptional(relationship, () => this.relationship),
    status: checkOptional(status, () => this.status),
    createdAt: checkOptional(createdAt, () => this.createdAt),
  );

  @override
  bool operator ==(Object other) => identical(this, other)
    || other is UserContact && id == other.id && userId == other.userId && fullName == other.fullName && phone == other.phone && relationship == other.relationship && status == other.status && createdAt == other.createdAt;

  @override
  int get hashCode => id.hashCode ^ userId.hashCode ^ fullName.hashCode ^ phone.hashCode ^ relationship.hashCode ^ status.hashCode ^ createdAt.hashCode;
}

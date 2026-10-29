import 'dart:math';

import 'package:flutter/material.dart';

String formatDate(DateTime date) {
  const months = [
    'ene', 'feb', 'mar', 'abr', 'may', 'jun',
    'jul', 'ago', 'sep', 'oct', 'nov', 'dic',
  ];
  return '${date.day} de ${months[date.month - 1]} de ${date.year}';
}

String formatDateToYYYYMMDD(DateTime date) {
  return date.toIso8601String().split('T')[0];
}

String formatDateToISOWithTimezone(DateTime date) {
  return date.toIso8601String();
}

String capitalizeWords(String text) {
  return text
      .split(' ')
      .map((word) => word.isEmpty
          ? word
          : word[0].toUpperCase() + word.substring(1))
      .join(' ');
}

String timeAgo(DateTime dateTime) {
  final now = DateTime.now();
  final difference = dateTime.difference(now);

  final isFuture = difference.isNegative == false;
  final duration = difference.abs();

  if (duration.inSeconds < 60) {
    return isFuture ? 'En unos momentos' : 'Hace unos momentos';
  }

  if (duration.inMinutes < 60) {
    final minutes = duration.inMinutes;
    return isFuture ? 'En ${minutes} min' : 'Hace ${minutes} min';
  }

  if (duration.inHours < 24) {
    final hours = duration.inHours;
    return isFuture ? 'En ${hours} h' : 'Hace ${hours} h';
  }

  if (duration.inDays == 1) {
    return isFuture ? 'Mañana' : 'Ayer';
  }

  if (duration.inDays < 7) {
    final days = duration.inDays;
    return isFuture ? 'En ${days} días' : 'Hace ${days} días';
  }

  if (duration.inDays < 30) {
    final weeks = (duration.inDays / 7).floor();
    return isFuture ? 'En ${weeks} sem.' : 'Hace ${weeks} sem.';
  }

  if (duration.inDays < 365) {
    final months = (duration.inDays / 30).floor();
    return isFuture ? 'En ${months} meses' : 'Hace ${months} meses';
  }

  final years = (duration.inDays / 365).floor();
  return isFuture ? 'En ${years} años' : 'Hace ${years} años';
}

Color getRandomColor() {
  final random = Random();

  return Color.fromARGB(
    255,
    random.nextInt(256),
    random.nextInt(256),
    random.nextInt(256),
  );
}

const String TOKEN_NAME = "ngc_token";

const String ROUTE_STATUS_IN_PROGRESS = 'in_progress';

const String USERS =  'users';
const String USER_CONTACTS =  'user_contacts';
const String USER_SESSIONS =  'user_sessions';
const String CHATS =  'chats';
const String ROUTES =  'routes';
const String INCIDENTS =  'incidents';
const String SEGMENTS =  'segments';
const String SEGMENT_DETAILS =  'segment_details';
const String ROUTE_QUERIES =  'route_queries';
const String MOOD_STATES =  'mood_states';
const String MOVEMENT_HISTORY =  'movement_history';
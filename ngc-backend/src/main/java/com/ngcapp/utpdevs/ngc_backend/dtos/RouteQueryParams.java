package com.ngcapp.utpdevs.ngc_backend.dtos;

import java.util.UUID;

public record RouteQueryParams(
  UUID p_user_id,
  String p_search
) {}

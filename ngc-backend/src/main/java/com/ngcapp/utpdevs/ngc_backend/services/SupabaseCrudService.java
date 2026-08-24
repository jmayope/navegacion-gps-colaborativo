package com.ngcapp.utpdevs.ngc_backend.services;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SupabaseCrudService {

    private final WebClient webClient;

    public SupabaseCrudService(WebClient webClient) {
        this.webClient = webClient;
    }


    // ============================================================
    // OBTENER TODOS
    // ============================================================

    /**
     * Obtiene todos los registros de una tabla.
     *
     * Ejemplo:
     *
     * List<Usuario> usuarios =
     *     supabaseCrudService.findAll(
     *         "users",
     *         Usuario[].class
     *     );
     */
    public <T> List<T> findAll(
            String table,
            Class<T[]> responseType
    ) {

        try {

            T[] result = webClient.get()
                    .uri("/" + table)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> buildError(response, table)
                    )
                    .bodyToMono(responseType)
                    .block();

            return result != null
                    ? Arrays.asList(result)
                    : new ArrayList<>();

        } catch (WebClientResponseException e) {

            throw buildException(
                    "obtener registros",
                    table,
                    e
            );
        }
    }


    // ============================================================
    // OBTENER POR ID
    // ============================================================

    /**
     * Obtiene un registro por ID.
     *
     * Ejemplo:
     *
     * Usuario usuario =
     *     supabaseCrudService.findById(
     *         "users",
     *         id,
     *         Usuario[].class
     *     );
     */
    public <T> T findById(
            String table,
            UUID id,
            Class<T[]> responseType
    ) {

        try {

            T[] result = webClient.get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path("/" + table)
                                    .queryParam("id", "eq." + id)
                                    .build()
                    )
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> buildError(response, table)
                    )
                    .bodyToMono(responseType)
                    .block();

            return result != null && result.length > 0
                    ? result[0]
                    : null;

        } catch (WebClientResponseException e) {

            throw buildException(
                    "obtener registro",
                    table,
                    e
            );
        }
    }


    // ============================================================
    // BUSCAR
    // ============================================================

    /**
     * Permite ejecutar filtros de PostgREST.
     *
     * Ejemplo:
     *
     * List<Usuario> usuarios =
     *     supabaseCrudService.find(
     *         "users",
     *         "username=eq.jorge",
     *         Usuario[].class
     *     );
     *
     * Otro ejemplo:
     *
     * "email=eq.test@gmail.com"
     *
     * También puedes utilizar:
     *
     * "status=eq.ACTIVE&order=created_at.desc"
     */
    public <T> List<T> find(
            String table,
            String query,
            Class<T[]> responseType
    ) {

        try {

            T[] result = webClient.get()
                    .uri(uriBuilder -> {

                        UriBuilder builder =
                                uriBuilder.path("/" + table);

                        if (query != null && !query.isBlank()) {

                            for (String parameter : query.split("&")) {

                                String[] parts = parameter.split("=", 2);

                                if (parts.length == 2) {

                                    builder.queryParam(
                                            parts[0],
                                            parts[1]
                                    );

                                }
                            }
                        }

                        return builder.build();
                    })
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> buildError(response, table)
                    )
                    .bodyToMono(responseType)
                    .block();

            return result != null
                    ? Arrays.asList(result)
                    : new ArrayList<>();

        } catch (WebClientResponseException e) {

            throw buildException(
                    "buscar registros",
                    table,
                    e
            );
        }
    }


    // ============================================================
    // INSERTAR
    // ============================================================

    /**
     * Inserta un registro.
     *
     * Ejemplo:
     *
     * Usuario usuario =
     *     supabaseCrudService.insert(
     *         "users",
     *         usuario,
     *         Usuario[].class
     *     );
     */
    public <T> T insert(
            String table,
            T entity,
            Class<T[]> responseType
    ) {

        try {

            T[] result = webClient.post()
                    .uri("/" + table)
                    .header(
                            "Prefer",
                            "return=representation"
                    )
                    .bodyValue(entity)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> buildError(response, table)
                    )
                    .bodyToMono(responseType)
                    .block();

            return result != null && result.length > 0
                    ? result[0]
                    : null;

        } catch (WebClientResponseException e) {

            throw buildException(
                    "insertar registro",
                    table,
                    e
            );
        }
    }


    // ============================================================
    // ACTUALIZAR POR ID
    // ============================================================

    /**
     * Actualiza un registro utilizando su ID.
     *
     * Ejemplo:
     *
     * Usuario usuario =
     *     supabaseCrudService.update(
     *         "users",
     *         id,
     *         usuario,
     *         Usuario[].class
     *     );
     */
    public <T> T update(
            String table,
            UUID id,
            T entity,
            Class<T[]> responseType
    ) {

        try {

            T[] result = webClient.patch()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path("/" + table)
                                    .queryParam(
                                            "id",
                                            "eq." + id
                                    )
                                    .build()
                    )
                    .header(
                            "Prefer",
                            "return=representation"
                    )
                    .bodyValue(entity)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> buildError(response, table)
                    )
                    .bodyToMono(responseType)
                    .block();

            return result != null && result.length > 0
                    ? result[0]
                    : null;

        } catch (WebClientResponseException e) {

            throw buildException(
                    "actualizar registro",
                    table,
                    e
            );
        }
    }


    // ============================================================
    // ACTUALIZAR CON FILTRO
    // ============================================================

    /**
     * Actualiza registros utilizando cualquier filtro PostgREST.
     *
     * Ejemplo:
     *
     * update(
     *     "users",
     *     "username=eq.jorge",
     *     usuario,
     *     Usuario[].class
     * );
     */
    public <T> List<T> update(
            String table,
            String query,
            T entity,
            Class<T[]> responseType
    ) {

        try {

            T[] result = webClient.patch()
                    .uri(uriBuilder -> {

                        UriBuilder builder =
                                uriBuilder.path("/" + table);

                        if (query != null && !query.isBlank()) {

                            for (String parameter : query.split("&")) {

                                String[] parts =
                                        parameter.split("=", 2);

                                if (parts.length == 2) {

                                    builder.queryParam(
                                            parts[0],
                                            parts[1]
                                    );
                                }
                            }
                        }

                        return builder.build();
                    })
                    .header(
                            "Prefer",
                            "return=representation"
                    )
                    .bodyValue(entity)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> buildError(response, table)
                    )
                    .bodyToMono(responseType)
                    .block();

            return result != null
                    ? Arrays.asList(result)
                    : new ArrayList<>();

        } catch (WebClientResponseException e) {

            throw buildException(
                    "actualizar registros",
                    table,
                    e
            );
        }
    }


    // ============================================================
    // ELIMINAR POR ID
    // ============================================================

    /**
     * Elimina un registro por ID.
     */
    public void delete(
            String table,
            UUID id
    ) {

        try {

            webClient.delete()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path("/" + table)
                                    .queryParam(
                                            "id",
                                            "eq." + id
                                    )
                                    .build()
                    )
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> buildError(response, table)
                    )
                    .bodyToMono(Void.class)
                    .block();

        } catch (WebClientResponseException e) {

            throw buildException(
                    "eliminar registro",
                    table,
                    e
            );
        }
    }


    // ============================================================
    // ELIMINAR CON FILTRO
    // ============================================================

    /**
     * Elimina registros utilizando cualquier filtro PostgREST.
     *
     * Ejemplo:
     *
     * delete(
     *     "users",
     *     "username=eq.jorge"
     * );
     */
    public void delete(
            String table,
            String query
    ) {

        try {

            webClient.delete()
                    .uri(uriBuilder -> {

                        UriBuilder builder =
                                uriBuilder.path("/" + table);

                        if (query != null && !query.isBlank()) {

                            for (String parameter : query.split("&")) {

                                String[] parts =
                                        parameter.split("=", 2);

                                if (parts.length == 2) {

                                    builder.queryParam(
                                            parts[0],
                                            parts[1]
                                    );
                                }
                            }
                        }

                        return builder.build();
                    })
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            response -> buildError(response, table)
                    )
                    .bodyToMono(Void.class)
                    .block();

        } catch (WebClientResponseException e) {

            throw buildException(
                    "eliminar registros",
                    table,
                    e
            );
        }
    }


    // ============================================================
    // EXISTENCIA
    // ============================================================

    /**
     * Verifica si existe al menos un registro.
     *
     * Ejemplo:
     *
     * boolean existe =
     *     supabaseCrudService.exists(
     *         "users",
     *         "username=eq.jorge"
     *     );
     */
    public boolean exists(
            String table,
            String query
    ) {

        try {

            List<Object> result = find(
                    table,
                    query + "&select=id",
                    Object[].class
            );

            return !result.isEmpty();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error verificando existencia en "
                            + table + ": "
                            + e.getMessage(),
                    e
            );
        }
    }


    // ============================================================
    // CONTAR
    // ============================================================

    /**
     * Obtiene la cantidad de registros que cumplen un filtro.
     *
     * Ejemplo:
     *
     * long cantidad =
     *     supabaseCrudService.count(
     *         "users",
     *         "status=eq.ACTIVE"
     *     );
     */
    public long count(
            String table,
            String query
    ) {

        try {

            List<Object> result = find(
                    table,
                    query + "&select=id",
                    Object[].class
            );

            return result.size();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error contando registros de "
                            + table + ": "
                            + e.getMessage(),
                    e
            );
        }
    }


    // ============================================================
    // MANEJO DE ERRORES
    // ============================================================

    private Mono<? extends Throwable> buildError(
            org.springframework.web.reactive.function.client.ClientResponse response,
            String table
    ) {

        return response
                .bodyToMono(String.class)
                .flatMap(error ->
                        Mono.error(
                                new RuntimeException(
                                        "Error Supabase [" +
                                                table +
                                                "] HTTP " +
                                                response.statusCode() +
                                                ": " +
                                                error
                                )
                        )
                );
    }


    private RuntimeException buildException(
            String operation,
            String table,
            WebClientResponseException e
    ) {

        return new RuntimeException(
                "Error al " +
                        operation +
                        " en tabla '" +
                        table +
                        "': " +
                        e.getResponseBodyAsString(),
                e
        );
    }
}
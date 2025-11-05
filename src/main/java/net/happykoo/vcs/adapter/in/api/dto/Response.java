package net.happykoo.vcs.adapter.in.api.dto;

public record Response<T> (T data) {
    public static <T> Response<T> ok() {
        return ok(null);
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>(data);
    }
}

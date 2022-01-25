package ru.softwave.pool.exception;

import java.util.function.Supplier;

public final class Exc {

    private Exc() {}

    /**
     * Генерирует ошибку, указанного типа
     *
     * @param errorType тип ошибки из enum ErrorType
     * @return возвращет сгенерированную ошибку в обёртке Supplier
     */
    public static Supplier<RestException> sup(ErrorType errorType) {
        return () -> Exc.gen(errorType);
    }

    /**
     * Генерирует ошибку, указанного типа
     *
     * @param errorType тип ошибки из enum ErrorType
     * @param message подробное описание ошибки
     * @return возвращет сгенерированную ошибку в обёртке Supplier
     */
    public static Supplier<RestException> sup(ErrorType errorType, String message) {
        return () -> Exc.gen(errorType, message);
    }

    /**
     * Генерирует ошибку типа "Сущность не найдена"
     *
     * @param message подробное описание ошибки
     * @return возвращет сгенерированную ошибку в обёртке Supplier
     */
    public static Supplier<RestException> notFoundSupplier(String message) {
        return sup(ErrorType.ENTITY_NOT_FOUND, message);
    }

    /**
     * Генерирует ошибку, указанного типа
     *
     * @param errorType тип ошибки из enum ErrorType
     * @return возвращет сгенерированную ошибку
     */
    public static RestException gen(ErrorType errorType) {
        return new RestException(errorType);
    }

    /**
     * Генерирует ошибку, указанного типа
     *
     * @param errorType тип ошибки из enum ErrorType
     * @param cause ошибка вызвавшая данную ошибку
     * @return возвращет сгенерированную ошибку
     */
    public static RestException gen(ErrorType errorType, Throwable cause) {
        return new RestException(errorType, cause);
    }

    /**
     * Генерирует ошибку, указанного типа
     *
     * @param errorType тип ошибки из enum ErrorType
     * @param message подробное описание ошибки
     * @return возвращет сгенерированную ошибку
     */
    public static RestException gen(ErrorType errorType, String message) {
        return new RestException(errorType, message);
    }

    /**
     * Генерирует ошибку, указанного типа
     *
     * @param errorType тип ошибки из enum ErrorType
     * @param message подробное описание ошибки
     * @param cause ошибка вызвавшая данную ошибку
     * @return возвращет сгенерированную ошибку
     */
    public static RestException gen(ErrorType errorType, String message, Throwable cause) {
        return new RestException(errorType, message, cause);
    }

}

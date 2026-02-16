#include "c_benchmark.h"
#include <stdlib.h>
#include <time.h>
#define MAX_ALLOC_SIZE (32 * 1024 * 1024) // 32 MB HARD LIMIT
#define CACHE_LINE_SIZE 64

static inline jlong get_time_ns() {
    struct timespec ts;
    clock_gettime(CLOCK_MONOTONIC, &ts);
    return (jlong) ts.tv_sec * 1000000000LL + ts.tv_nsec;
}

/**
 * Pointer Chasing – latență RAM
 */
JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_memoryLatencyBenchmark(
        JNIEnv *env, jobject thiz, jint size, jint iterations) {

    if (size <= 0 || size > MAX_ALLOC_SIZE || iterations <= 0)
        return -1;

    int elements = size / sizeof(int);
    if (elements <= 1) return -1;

    int *array = (int *) malloc(elements * sizeof(int));
    if (!array) return -1;

    int stride = CACHE_LINE_SIZE / sizeof(int);
    for (int i = 0; i < elements - stride; i++) {
        array[i] = i + stride;
    }
    array[elements - stride] = 0;

    int idx = 0;

    jlong start = get_time_ns();
    for (int i = 0; i < iterations; i++) {
        idx = array[idx];
    }
    jlong end = get_time_ns();

    free(array);
    return end - start;
}

/**
 * Bandwidth citire
 */
JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_memoryReadBandwidthBenchmark(
        JNIEnv *env, jobject thiz, jint size, jint iterations) {

    if (size <= 0 || size > MAX_ALLOC_SIZE || iterations <= 0)
        return -1;

    char *buffer = (char *) malloc(size);
    if (!buffer) return -1;

    for (int i = 0; i < size; i++) {
        buffer[i] = (char)(i & 0xFF);
    }

    volatile char sink;
    jlong start = get_time_ns();

    for (int it = 0; it < iterations; it++) {
        for (int i = 0; i < size; i += CACHE_LINE_SIZE) {
            sink = buffer[i];
        }
    }

    jlong end = get_time_ns();
    free(buffer);
    return end - start;
}

/**
 * Bandwidth scriere
 */
JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_memoryWriteBandwidthBenchmark(
        JNIEnv *env, jobject thiz, jint size, jint iterations) {

    if (size <= 0 || size > MAX_ALLOC_SIZE || iterations <= 0)
        return -1;

    char *buffer = (char *) malloc(size);
    if (!buffer) return -1;

    jlong start = get_time_ns();

    for (int it = 0; it < iterations; it++) {
        for (int i = 0; i < size; i += CACHE_LINE_SIZE) {
            buffer[i] = (char)(i & 0xFF);
        }
    }

    jlong end = get_time_ns();
    free(buffer);
    return end - start;
}

JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_nativeGetTime(JNIEnv *env, jobject thiz) {
    struct timespec ts;
    clock_gettime(CLOCK_MONOTONIC, &ts);
    return (jlong)(ts.tv_sec * 1000000000LL + ts.tv_nsec);
}

void swap(int* a, int* b) {
    int aux = *a;
    *a = *b;
    *b = aux;
}

int partition(int arr[], int low, int high) {
    int pivot = arr[high];
    int i = (low - 1);

    for (int j = low; j <= high - 1; j++) {
        if (arr[j] < pivot) {
            i++;
            swap(&arr[i], &arr[j]);
        }
    }
    swap(&arr[i + 1], &arr[high]);
    return (i + 1);
}

void quick_sort(int arr[], int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quick_sort(arr, low, pi - 1);
        quick_sort(arr, pi + 1, high);
    }
}

JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_quickSortBenchmark(JNIEnv *env, jobject thiz, jint arraySize) {
    int* arr = (int*)malloc(arraySize * sizeof(int));
    if (arr == NULL) return -1;

    for (int i = 0; i < arraySize; i++) {
        arr[i] = rand() % 10000;
    }

    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    quick_sort(arr, 0, arraySize - 1);

    clock_gettime(CLOCK_MONOTONIC, &end);

    jlong start_ns = start.tv_sec * 1000000000LL + start.tv_nsec;
    jlong end_ns = end.tv_sec * 1000000000LL + end.tv_nsec;

    free(arr);
    return end_ns - start_ns;
}


JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_matrixMultiplicationBenchmark(JNIEnv *env, jobject thiz, jint matrixSize) {
    int** matrixA = (int**)malloc(matrixSize * sizeof(int*));
    int** matrixB = (int**)malloc(matrixSize * sizeof(int*));
    int** result  = (int**)malloc(matrixSize * sizeof(int*));

    for (int i = 0; i < matrixSize; i++) {
        matrixA[i] = (int*)malloc(matrixSize * sizeof(int));
        matrixB[i] = (int*)malloc(matrixSize * sizeof(int));
        result[i]  = (int*)malloc(matrixSize * sizeof(int));
    }

    for (int i = 0; i < matrixSize; i++) {
        for (int j = 0; j < matrixSize; j++) {
            matrixA[i][j] = rand() % 100;
            matrixB[i][j] = rand() % 100;
            result[i][j]  = 0;
        }
    }

    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    for (int i = 0; i < matrixSize; i++) {
        for (int j = 0; j < matrixSize; j++) {
            for (int k = 0; k < matrixSize; k++) {
                result[i][j] += matrixA[i][k] * matrixB[k][j];
            }
        }
    }

    clock_gettime(CLOCK_MONOTONIC, &end);

    jlong start_ns = start.tv_sec * 1000000000LL + start.tv_nsec;
    jlong end_ns = end.tv_sec * 1000000000LL + end.tv_nsec;

    for (int i = 0; i < matrixSize; i++) {
        free(matrixA[i]);
        free(matrixB[i]);
        free(result[i]);
    }
    free(matrixA);
    free(matrixB);
    free(result);

    return end_ns - start_ns;
}

JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_floatMatrixMultiplicationBenchmark(JNIEnv *env, jobject thiz, jint matrixSize) {
    float** matrixA = (float**)malloc(matrixSize * sizeof(float*));
    float** matrixB = (float**)malloc(matrixSize * sizeof(float*));
    float** result  = (float**)malloc(matrixSize * sizeof(float*));

    for (int i = 0; i < matrixSize; i++) {
        matrixA[i] = (float*)malloc(matrixSize * sizeof(float));
        matrixB[i] = (float*)malloc(matrixSize * sizeof(float));
        result[i]  = (float*)malloc(matrixSize * sizeof(float));
    }

    for (int i = 0; i < matrixSize; i++) {
        for (int j = 0; j < matrixSize; j++) {
            matrixA[i][j] = (float)rand() / RAND_MAX * 100.0f;
            matrixB[i][j] = (float)rand() / RAND_MAX * 100.0f;
            result[i][j]  = 0.0f;
        }
    }

    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    for (int i = 0; i < matrixSize; i++) {
        for (int j = 0; j < matrixSize; j++) {
            for (int k = 0; k < matrixSize; k++) {
                result[i][j] += matrixA[i][k] * matrixB[k][j];
            }
        }
    }

    clock_gettime(CLOCK_MONOTONIC, &end);

    jlong start_ns = start.tv_sec * 1000000000LL + start.tv_nsec;
    jlong end_ns = end.tv_sec * 1000000000LL + end.tv_nsec;

    for (int i = 0; i < matrixSize; i++) {
        free(matrixA[i]);
        free(matrixB[i]);
        free(result[i]);
    }
    free(matrixA);
    free(matrixB);
    free(result);

    return end_ns - start_ns;
}

jlong fibonacci_recursive(jint n) {
    if (n <= 1) return n;
    return fibonacci_recursive(n - 1) + fibonacci_recursive(n - 2);
}

JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_fibonacciBenchmark(JNIEnv *env, jobject thiz, jint n) {
    if (n > 40) n = 40;

    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);

    jlong result = fibonacci_recursive(n);

    clock_gettime(CLOCK_MONOTONIC, &end);

    jlong start_ns = start.tv_sec * 1000000000LL + start.tv_nsec;
    jlong end_ns = end.tv_sec * 1000000000LL + end.tv_nsec;

    return end_ns - start_ns;
}

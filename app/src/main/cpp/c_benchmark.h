#ifndef C_BENCHMARK_H
#define C_BENCHMARK_H

#include <jni.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>

#ifdef __cplusplus
extern "C" {
#endif
#define CACHE_LINE_SIZE 64

JNIEXPORT jlong JNICALL Java_com_benchmarkandroidssc_app_BenchmarkUtils_nativeGetTime
        (JNIEnv *env, jobject thiz);

JNIEXPORT jlong JNICALL Java_com_benchmarkandroidssc_app_BenchmarkUtils_quickSortBenchmark
        (JNIEnv *env, jobject thiz, jint arraySize);

JNIEXPORT jlong JNICALL Java_com_benchmarkandroidssc_app_BenchmarkUtils_matrixMultiplicationBenchmark
        (JNIEnv *env, jobject thiz, jint size);

JNIEXPORT jlong JNICALL Java_com_benchmarkandroidssc_app_BenchmarkUtils_floatMatrixMultiplicationBenchmark
        (JNIEnv *env, jobject thiz, jint size);

JNIEXPORT jlong JNICALL Java_com_benchmarkandroidssc_app_BenchmarkUtils_fibonacciBenchmark
        (JNIEnv *env, jobject thiz, jint n);


JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_memoryLatencyBenchmark
        (JNIEnv *, jobject, jint size, jint iterations);

JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_memoryReadBandwidthBenchmark
        (JNIEnv *, jobject, jint size, jint iterations);

JNIEXPORT jlong JNICALL
Java_com_benchmarkandroidssc_app_BenchmarkUtils_memoryWriteBandwidthBenchmark
        (JNIEnv *, jobject, jint size, jint iterations);

void quick_sort(int arr[], int low, int high);
int partition(int arr[], int low, int high);
jlong fibonacci_recursive(jint n);

#ifdef __cplusplus
}
#endif

#endif // C_BENCHMARK_H

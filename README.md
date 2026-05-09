# Android Native Benchmark Suite

A native Android benchmarking application developed in **Kotlin** and **C/C++ (NDK)** for evaluating CPU, memory, GPU, and overall hardware performance on Android devices.

---

# Overview
This project aims to analyze and measure the performance of modern Android devices by implementing a set of low-level benchmark tests. The application evaluates computational power, memory subsystem efficiency, GPU rendering performance, and hardware configuration information, while also providing graphical visualization and comparison between devices.

The benchmark focuses on understanding how processor architecture, cache hierarchy, memory latency, and graphics throughput influence real-world performance.

---

# Features

## CPU Benchmarks
Tests designed to stress CPU execution units and measure raw computational power.

Implemented tests:
- Fibonacci computation
- QuickSort on large datasets
- Matrix multiplication

The benchmarks evaluate:
- instruction execution efficiency,
- cache usage,
- branch prediction,
- and pipeline utilization.

---

## Memory Benchmarks
Memory tests evaluate:
- memory latency,
- memory bandwidth,
- cache efficiency,
- and RAM access performance.

Implemented tests:
- Sequential memory access
- Random memory access
- Pointer chasing latency test

These benchmarks stress:
- L1 / L2 / L3 cache hierarchy
- RAM access speed
- Hardware prefetching mechanisms

---

## GPU Benchmarks
GPU performance is measured using OpenGL ES.

Metrics:
- Frame rendering time
- FPS (Frames Per Second)
- Rendering throughput

Implemented tests:
- Rendering large numbers of primitives
- Animated scene rendering

---

## System Information
The application retrieves:
- CPU core count
- RAM information
- Android version
- Device model
- GPU renderer information

---

# Technologies Used

- Kotlin
- Android SDK
- Android NDK (C/C++)
- JNI
- OpenGL ES
- Gradle

---

# Memory Benchmarking Concepts

## Pointer Chasing

Pointer chasing measures real memory latency by forcing each memory access to depend on the previous one.

This prevents:
- speculative execution,
- hardware prefetching,
- and cache optimizations.

As a result, the benchmark exposes true memory access latency.

---

## Memory Bandwidth

Bandwidth tests measure how much data can be transferred between RAM and CPU per second.

Sequential traversal of large memory regions allows the CPU to:
- load multiple cache lines in parallel,
- maximize throughput,
- and efficiently use hardware prefetchers.

When data is not found in one cache level (**cache miss**), the request moves to the next level, increasing access latency.

## Sequential vs Random Access

### Sequential Access
Sequential access benefits from:
- spatial locality,
- cache line reuse,
- and hardware prefetching.

Most accesses are served directly from cache with low latency.

### Random Access
Random access patterns:
- reduce cache efficiency,
- increase cache misses,
- and generate more RAM accesses.

This significantly reduces performance.

---

# QuickSort and Cache Performance

QuickSort performance strongly depends on array size and cache behavior.

- Small arrays fit entirely in cache and execute very quickly.
- Larger arrays exceed cache capacity, increasing RAM accesses.
- Very large arrays generate many cache misses and become memory-bound.

As dataset size increases:
- cache hit rates decrease,
- memory latency increases,
- and execution time grows significantly.


Provides:
- total RAM,
- available RAM,
- low memory threshold.

---

Returns the GPU renderer name using OpenGL ES.

---

# Project Structure

```text
app/
 ├── manifests/
 ├── java/
 ├── cpp/
 ├── res/
 └── build.gradle
```
---

# Build Process

The Android application compilation pipeline includes:

1. Kotlin source compilation to JVM bytecode
2. Conversion to `.dex` bytecode via D8/R8
3. Packaging resources and binaries into:
   - `.apk`
   - `.aab`

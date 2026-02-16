cmake_minimum_required(VERSION 3.18.1)

project("c-benchmark")

add_library(
    c_benchmark
    SHARED
        c_benchmark.c
)

include_directories(${CMAKE_SOURCE_DIR})

find_library(
    log-lib
    log
)

target_link_libraries(
    c_benchmark
    ${log-lib}
)
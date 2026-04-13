@echo off
setlocal

set IMAGE=maven:3.9.9-eclipse-temurin-21

docker run --rm -v "%cd%":/workspace -w /workspace %IMAGE% mvn %*

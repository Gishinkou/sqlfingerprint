#!/bin/bash

# Default version if not provided
VERSION=2.0.0

# Determine OS and extension
OS="$(uname -s)"
case "${OS}" in
    Linux*)     EXT="so";;
    Darwin*)    EXT="dylib";;
    CYGWIN*|MINGW*) EXT="dll";;
    *)          EXT="so";;
esac

LIB_NAME="libsqlfingerprint"
OUTPUT_FILE="../${LIB_NAME}.${VERSION}.${EXT}"
SYMLINK_FILE="../${LIB_NAME}.${EXT}"

echo "Building version: ${VERSION}"
echo "OS: ${OS}"
echo "Output: ${OUTPUT_FILE}"

cd lib || exit 1

# Build with ldflags to set version
go build -buildmode=c-shared \
    -ldflags "-X 'main.Version=${VERSION}'" \
    -o "${OUTPUT_FILE}" \
    lib.go

if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "Generated: ${OUTPUT_FILE}"
else
    echo "Build failed!"
    exit 1
fi

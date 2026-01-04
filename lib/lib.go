package main

/*
#include <stdlib.h>
*/
import "C"
import (
	"unsafe"

	"github.com/percona/go-mysql/query"
)

var Version string = "dev"

// main function is required for c-shared buildmode, but it won't be executed explicitly.
// It serves as an initialization entry point.
func main() {}

//export GetVersion
func GetVersion() *C.char {
	return C.CString(Version)
}

//export FingerprintSQL
func FingerprintSQL(q *C.char) *C.char {
	// Enable the feature to replace numbers in words (e.g., table_01 -> table_?)
	query.ReplaceNumbersInWords = true

	goStr := C.GoString(q)
	result := query.Fingerprint(goStr)

	return C.CString(result)
}

//export FreeString
func FreeString(str *C.char) {
	C.free(unsafe.Pointer(str))
}

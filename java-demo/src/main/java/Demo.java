import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class Demo {
    public interface SqlFingerprintLib extends Library {
        // Load the library
        // We will load it dynamically in main() using Native.load with specific
        // path/name
        SqlFingerprintLib INSTANCE = (SqlFingerprintLib) Native.load(System.getProperty("lib.name", "sqlfingerprint"),
                SqlFingerprintLib.class);

        // func FingerprintSQL(q *C.char) *C.charDefine the exported functions from Go
        // func FingerprintSQL(q *C.char) *C.char
        Pointer FingerprintSQL(String q);

        // func GetVersion() *C.char
        Pointer GetVersion();

        // func FreeString(str *C.char)
        void FreeString(Pointer p);
    }

    public static void main(String[] args) {
        // Assume the library is in the parent directory of the java-demo project
        // Or explicitly set: -Djna.library.path=/absolute/path/to/libs
        String libPath = System.getProperty("user.dir") + "/..";
        System.setProperty("jna.library.path", libPath);

        System.out.println("Using library path: " + libPath);

        try {
            // Check version
            Pointer verPtr = SqlFingerprintLib.INSTANCE.GetVersion();
            if (verPtr != null) {
                String version = verPtr.getString(0);
                System.out.println("Library Version: " + version);
                SqlFingerprintLib.INSTANCE.FreeString(verPtr);
            }

            String sql = "select app_name, user_name from users_00 where id = 1;";
            System.out.println("Original SQL: " + sql);

            // Call Go function
            Pointer ptr = SqlFingerprintLib.INSTANCE.FingerprintSQL(sql);

            if (ptr != null) {
                // Convert C string to Java String
                String result = ptr.getString(0);
                System.out.println("Fingerprinted SQL: " + result);

                // IMPORTANT: Free the memory allocated in Go
                SqlFingerprintLib.INSTANCE.FreeString(ptr);
            } else {
                System.err.println("Error: Returned null pointer");
            }
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load library. Ensure libsqlfingerprint.dylib (or .so/.dll) is in " + libPath);
            e.printStackTrace();
        }
    }
}

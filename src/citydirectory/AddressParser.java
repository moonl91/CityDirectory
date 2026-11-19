package citydirectory;
import java.io.IOException; import java.nio.file.Path; import java.util.List;
public interface AddressParser { List<AddressRecord> parse(Path path) throws IOException; }

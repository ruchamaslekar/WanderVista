package reviewData;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import  java.util.*;


/** Demonstrating DirectoryParser class */
public class DirectoryParser {

    List<Review> reviewList =new ArrayList<>();
    List<List<Review>> reviews =new ArrayList<>();
    Review review=null;
    /**
     * This method is parsing directory and subdirectories containing json files
     * This method would call parseReviewJson method
     * @param directory to search json file into
     */
    public List<List<Review>> parseDirectory(String directory) throws IOException {
        Path p = Paths.get(directory);
        try (DirectoryStream<Path> pathsInDir = Files.newDirectoryStream(p)) {
            for (Path path : pathsInDir) {
                /** print the name of each file in the directory*/
                if (Files.isRegularFile(path) && (path.toString().endsWith(".json"))) {
                    ReviewParser parser = new ReviewParser();
                    List<Review> reviewList1 = parser.parseReviewJson(path.toFile());
                    reviews.add(reviewList1);
                } else if (Files.isDirectory(path)) {
                     parseDirectory(path.toString());
                }
            }

        } catch (IOException e) {
            System.out.println("Can not open directory: " + directory);
        }
        return reviews;
    }
}

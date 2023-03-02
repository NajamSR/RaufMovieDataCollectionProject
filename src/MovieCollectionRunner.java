import java.util.ArrayList;

public class MovieCollectionRunner
{
    public static void main(String args[])
    {
        MovieCollection myCollection = new MovieCollection("src/movies_data.csv");
        myCollection.menu();
    }
}

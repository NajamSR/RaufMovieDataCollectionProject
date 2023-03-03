import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MovieCollection
{
    private ArrayList<Movie> movies;
    private Scanner scanner;

    private ArrayList<String> cast;
    private ArrayList<String> genres;
    private ArrayList<Movie> bestMovies;
    private ArrayList<Movie> moniestMovies;

    public MovieCollection(String fileName)
    {
        importMovieList(fileName);
        scanner = new Scanner(System.in);
        cast = new ArrayList<String>();
        for (Movie m : movies) {
            String cL = m.getCast();
            while (cL.contains("|")) {
                if (!cast.contains(cL.substring(0, cL.indexOf("|")))) {
                    cast.add(cL.substring(0, cL.indexOf("|")));
                }
                cL = cL.substring(cL.indexOf("|") + 1);
            }
            if (!cast.contains(cL)) {
                cast.add(cL);
            }
        }
        genres = new ArrayList<String>();
        for (Movie m : movies) {
            String cL = m.getGenres();
            while (cL.contains("|")) {
                if (!genres.contains(cL.substring(0, cL.indexOf("|")))) {
                    genres.add(cL.substring(0, cL.indexOf("|")));
                }
                cL = cL.substring(cL.indexOf("|") + 1);
            }
            if (!genres.contains(cL)) {
                genres.add(cL);
            }
        }
        bestMovies = new ArrayList<Movie>();
        for (int i = 0; i < 50; i++) {
            int idxOfBest = 0;
            for (int e = 0; e < movies.size(); e++) {
                if (!bestMovies.contains(movies.get(e)) && movies.get(e).getUserRating() > movies.get(idxOfBest).getUserRating()) {
                    idxOfBest = e;
                }
            }
            bestMovies.add(movies.get(idxOfBest));
        }
        moniestMovies = movies;
        for (int j = 1; j < moniestMovies.size(); j++)
        {
            Movie temp = moniestMovies.get(j);
            int tempRevenue = temp.getRevenue();

            int possibleIndex = j;
            while (possibleIndex > 0 && tempRevenue < moniestMovies.get(possibleIndex - 1).getRevenue())
            {
                moniestMovies.set(possibleIndex, moniestMovies.get(possibleIndex - 1));
                possibleIndex--;
            }
            moniestMovies.set(possibleIndex, temp);
        }
    }


    public ArrayList<Movie> getMovies()
    {
        return movies;
    }

    public void menu()
    {
        String menuOption = "";

        System.out.println("Welcome to the movie collection!");
        System.out.println("Total: " + movies.size() + " movies");

        while (!menuOption.equals("q"))
        {
            System.out.println("------------ Main Menu ----------");
            System.out.println("- search (t)itles");
            System.out.println("- search (k)eywords");
            System.out.println("- search (c)ast");
            System.out.println("- see all movies of a (g)enre");
            System.out.println("- list top 50 (r)ated movies");
            System.out.println("- list top 50 (h)ighest revenue movies");
            System.out.println("- (q)uit");
            System.out.print("Enter choice: ");
            menuOption = scanner.nextLine();

            if (!menuOption.equals("q"))
            {
                processOption(menuOption);
            }
        }
    }

    private void processOption(String option)
    {
        if (option.equals("t"))
        {
            searchTitles();
        }
        else if (option.equals("c"))
        {
            searchCast();
        }
        else if (option.equals("k"))
        {
            searchKeywords();
        }
        else if (option.equals("g"))
        {
            listGenres();
        }
        else if (option.equals("r"))
        {
            listHighestRated();
        }
        else if (option.equals("h"))
        {
            listHighestRevenue();
        }
        else
        {
            System.out.println("Invalid choice!");
        }
    }

    private void searchTitles()
    {
        System.out.print("Enter a title search term: ");
        String searchTerm = scanner.nextLine();

        // prevent case sensitivity
        searchTerm = searchTerm.toLowerCase();

        // arraylist to hold search results
        ArrayList<Movie> results = new ArrayList<Movie>();

        // search through ALL movies in collection
        for (int i = 0; i < movies.size(); i++)
        {
            String movieTitle = movies.get(i).getTitle();
            movieTitle = movieTitle.toLowerCase();

            if (movieTitle.indexOf(searchTerm) != -1)
            {
                //add the Movie object to the results list
                results.add(movies.get(i));
            }
        }

        // sort the results by title
        sortResults(results);

        // now, display them all to the user
        for (int i = 0; i < results.size(); i++)
        {
            String title = results.get(i).getTitle();

            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = results.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void sortResults(ArrayList<Movie> listToSort)
    {
        for (int j = 1; j < listToSort.size(); j++)
        {
            Movie temp = listToSort.get(j);
            String tempTitle = temp.getTitle();

            int possibleIndex = j;
            while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0)
            {
                listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
                possibleIndex--;
            }
            listToSort.set(possibleIndex, temp);
        }
    }

    private void displayMovieInfo(Movie movie)
    {
        System.out.println();
        System.out.println("Title: " + movie.getTitle());
        System.out.println("Tagline: " + movie.getTagline());
        System.out.println("Runtime: " + movie.getRuntime() + " minutes");
        System.out.println("Year: " + movie.getYear());
        System.out.println("Directed by: " + movie.getDirector());
        System.out.println("Cast: " + movie.getCast());
        System.out.println("Overview: " + movie.getOverview());
        System.out.println("User rating: " + movie.getUserRating());
        System.out.println("Box office revenue: " + movie.getRevenue());
    }

    private void searchCast()
    {
        System.out.print("Enter a cast member search term: ");
        String searchTerm = scanner.nextLine();

        // prevent case sensitivity
        searchTerm = searchTerm.toLowerCase();

        // arraylist to hold search results
        ArrayList<String> members = new ArrayList<String>();

        // search through ALL movies in collection
        for (int i = 0; i < cast.size(); i++)
        {
            String movieCast = cast.get(i);
            movieCast = movieCast.toLowerCase();

            if (movieCast.indexOf(searchTerm) != -1)
            {
                //add the Movie object to the results list
                members.add(cast.get(i));
            }
        }

        for (int j = 1; j < members.size(); j++)
        {
            String temp = members.get(j);

            int possibleIndex = j;
            while (possibleIndex > 0 && temp.compareTo(members.get(possibleIndex - 1)) < 0)
            {
                members.set(possibleIndex, members.get(possibleIndex - 1));
                possibleIndex--;
            }
            members.set(possibleIndex, temp);
        }
        for (int c = 1; c <= members.size(); c++) {
            System.out.println("" + c + ". " + members.get(c - 1));
        }
        System.out.println("Which cast member would you like to learn more about?");
        System.out.print("Enter number: ");

        int cho = scanner.nextInt();
        scanner.nextLine();
        String selectedMember = members.get(cho - 2);
        ArrayList<Movie> results = new ArrayList<Movie>();
        for (Movie v : movies) {
            if (v.getCast().contains(selectedMember)) {
                results.add(v);
            }
        }
        // sort the results by title
        sortResults(results);

        // now, display them all to the user
        for (int i = 0; i < results.size(); i++)
        {
            String title = results.get(i).getTitle();

            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = results.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void searchKeywords()
    {
        System.out.print("Enter a keyword search term: ");
        String searchTerm = scanner.nextLine();

        // prevent case sensitivity
        searchTerm = searchTerm.toLowerCase();

        // arraylist to hold search results
        ArrayList<Movie> results = new ArrayList<Movie>();

        // search through ALL movies in collection
        for (int i = 0; i < movies.size(); i++)
        {
            String movieKeywords = movies.get(i).getKeywords();
            movieKeywords = movieKeywords.toLowerCase();

            if (movieKeywords.indexOf(searchTerm) != -1)
            {
                //add the Movie object to the results list
                results.add(movies.get(i));
            }
        }

        // sort the results by title
        sortResults(results);

        // now, display them all to the user
        for (int i = 0; i < results.size(); i++)
        {
            String title = results.get(i).getTitle();

            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = results.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void listGenres()
    {
        for (int j = 1; j < genres.size(); j++)
        {
            String temp = genres.get(j);

            int possibleIndex = j;
            while (possibleIndex > 0 && temp.compareTo(genres.get(possibleIndex - 1)) < 0)
            {
                genres.set(possibleIndex, genres.get(possibleIndex - 1));
                possibleIndex--;
            }
            genres.set(possibleIndex, temp);
        }
        for (int c = 1; c <= genres.size(); c++) {
            System.out.println("" + c + ". " + genres.get(c - 1));
        }
        System.out.println("Which genre would you like to look into?");
        System.out.print("Enter number: ");

        int cho = scanner.nextInt();
        scanner.nextLine();
        String selectedGenre = genres.get(cho - 2);
        ArrayList<Movie> results = new ArrayList<Movie>();
        for (Movie v : movies) {
            if (v.getGenres().contains(selectedGenre)) {
                results.add(v);
            }
        }
        // sort the results by title
        sortResults(results);

        // now, display them all to the user
        for (int i = 0; i < results.size(); i++)
        {
            String title = results.get(i).getTitle();

            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;

            System.out.println("" + choiceNum + ". " + title);
        }

        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = results.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void listHighestRated()
    {
        for (int i = 1; i <= 50; i++) {
            System.out.println(i + ". " + bestMovies.get(i - 1).getTitle() + ": " + bestMovies.get(i - 1).getUserRating());
        }
        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = bestMovies.get(choice - 1);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void listHighestRevenue()
    {
        for (int i = moniestMovies.size() - 1; i >= moniestMovies.size() - 50; i--) {
            System.out.println(moniestMovies.size() - i + ". " + moniestMovies.get(i).getTitle() + ": " + moniestMovies.get(i).getRevenue());
        }
        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        Movie selectedMovie = moniestMovies.get(moniestMovies.size() - choice);

        displayMovieInfo(selectedMovie);

        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }

    private void importMovieList(String fileName)
    {
        try
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();

            movies = new ArrayList<Movie>();

            while ((line = bufferedReader.readLine()) != null)
            {
                String[] movieFromCSV = line.split(",");

                String title = movieFromCSV[0];
                String cast = movieFromCSV[1];
                String director = movieFromCSV[2];
                String tagline = movieFromCSV[3];
                String keywords = movieFromCSV[4];
                String overview = movieFromCSV[5];
                int runtime = Integer.parseInt(movieFromCSV[6]);
                String genres = movieFromCSV[7];
                double userRating = Double.parseDouble(movieFromCSV[8]);
                int year = Integer.parseInt(movieFromCSV[9]);
                int revenue = Integer.parseInt(movieFromCSV[10]);

                Movie nextMovie = new Movie(title, cast, director, tagline, keywords, overview, runtime, genres, userRating, year, revenue);
                movies.add(nextMovie);
            }
            bufferedReader.close();
        }
        catch(IOException exception)
        {
            // Print out the exception that occurred
            System.out.println("Unable to access " + exception.getMessage());
        }
    }
}

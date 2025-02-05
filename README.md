# ScoreBoard Library

**ScoreBoard Library** is a simple and efficient library for managing a sports scoreboard. It allows you to start new matches, update scores, finish matches, and retrieve a sorted scoreboard summary. The library includes robust validation mechanisms to ensure data integrity—such as preventing duplicate teams, forbidding identical team names (even with different letter cases), and ensuring score updates are valid.

## Features

- **Start a new match**  
  Adds a new match to the scoreboard while validating team names and ensuring teams are not already on the scoreboard.

- **Update match scores**  
  Updates the score of an existing match. It ensures the match exists and that the new scores are non-negative.

- **Finish a match**  
  Removes a match from the scoreboard, keeping only ongoing matches.

- **Retrieve the scoreboard summary**  
  Returns a sorted list of matches:
    - Matches are first sorted by the total score (home + away) in **descending order**.
    - If two matches have the same score, they are sorted by **start time**, with the most recent matches appearing first.

- **Validations include:**
    - Team names must not be `null`, empty, or contain only whitespace.
    - Team names must not be identical (case-insensitive).
    - A team cannot participate in multiple ongoing matches at the same time.
    - Scores cannot be negative.
    - Matches must exist before being updated or finished.

---

## Example usage

```java
import com.sportsradar.scoreboard.ScoreBoard;
import com.sportsradar.scoreboard.ScoreBoardImpl;
import com.sportsradar.scoreboard.Match;

import java.util.List;

public class ScoreBoardExample {
    public static void main(String[] args) {
        // Initialize the scoreboard
        ScoreBoard scoreBoard = new ScoreBoardImpl();

        // Start a new match
        scoreBoard.startNewMatch("Team A", "Team B");

        // Update match score
        scoreBoard.updateScore("Team A", "Team B", 2, 1);

        // Retrieve and display the scoreboard summary
        List<Match> summary = scoreBoard.getScoreBoardSummary();
        summary.forEach(System.out::println);

        // Finish the match
        scoreBoard.finishMatch("Team A", "Team B");
    }
}

```

## Important notes
**Sorting elements in scoreBoardSummary** - in the specification it was attached to requirement, I've understood that was described as inner functionality of that method thus it was not moved to TreeSet wit explicit sort on every modification of set.

**NanoTime** - provides sufficient precision for this functionality, as it is independent of system clock changes and ideal for measuring time intervals. It can be refactored to use Instant and Clock, which offer a more structured approach to handling timestamps. However, this introduces additional dependencies and requires mocking in tests to ensure deterministic behavior. While Instant and Clock are useful for absolute timestamps, nanoTime remains a suitable choice for measuring elapsed time.

**TeamName Validation in Update and Finish methods** - Can be considered redundant, as if there is any null - match will be considered non-existent, on the other hand I strongly disagree to leave null values unhandled therefore those was added. 
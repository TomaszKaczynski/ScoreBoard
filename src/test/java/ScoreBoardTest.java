import com.sportsradar.scoreboard.Match;
import com.sportsradar.scoreboard.ScoreBoard;
import com.sportsradar.scoreboard.ScoreBoardImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreBoardTest {
    ScoreBoard scoreBoard;
    private static final String HOME_TEAM = "HomeTeam";
    private static final String AWAY_TEAM = "AwayTeam";

    @BeforeEach
    public void setUp() {
        scoreBoard = new ScoreBoardImpl();
    }

    @Test
    public void newMatchShouldBeSuccessfullyAddedTest() {
        //When
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        //Then
        assertEquals(1, scoreBoard.getScoreBoardSummary().size());
        assertEquals(HOME_TEAM + " 0 - " + AWAY_TEAM + " 0",
                scoreBoard.getScoreBoardSummary().getFirst().toString());
    }

    @Test
    public void scoreForMatchShouldBeUpdatedTest() {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        //When
        scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, 1, 1);

        //THEN
        assertEquals(1, scoreBoard.getScoreBoardSummary().size());
        assertEquals(HOME_TEAM + " 1 - " + AWAY_TEAM + " 1",
                scoreBoard.getScoreBoardSummary().getFirst().toString());
    }

    @Test
    public void matchShouldNotBeUpdatedIfItDoesntExist() {
        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, 1, 1));
        assertEquals("No match found for HomeTeam and AwayTeam", exception.getMessage());
    }

    @Test
    public void matchShouldBeFinishedTest() {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);
        scoreBoard.startNewMatch(HOME_TEAM+"1", AWAY_TEAM+"1");

        //When
        scoreBoard.finishMatch(HOME_TEAM, AWAY_TEAM);

        //Then
        assertEquals(1, scoreBoard.getScoreBoardSummary().size());
        assertEquals(0, scoreBoard.getScoreBoardSummary().stream()
                .filter(match -> match.toString().equals("HomeTeam 0 - AwayTeam 0")).count());
    }

    @Test
    public void matchShouldNotBeFinishedIfItDoesntExist() {
        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.finishMatch(HOME_TEAM, AWAY_TEAM));
        assertEquals("No match found for HomeTeam and AwayTeam", exception.getMessage());
    }

    @Test
    public void scoreBoardSummaryShouldBeReturnedSortedTest() {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);
        scoreBoard.startNewMatch(HOME_TEAM+"1", AWAY_TEAM+"1");
        scoreBoard.updateScore(HOME_TEAM+"1", AWAY_TEAM+"1", 10, 0);
        scoreBoard.startNewMatch(HOME_TEAM+"2", AWAY_TEAM+"2");
        scoreBoard.startNewMatch(HOME_TEAM+"3", AWAY_TEAM+"3");
        scoreBoard.updateScore(HOME_TEAM+"2", AWAY_TEAM+"2", 0, 10);

        //When & Then
        List<Match> scoreBoardSummary = scoreBoard.getScoreBoardSummary();
        assertEquals(4, scoreBoardSummary.size());
        assertEquals("HomeTeam2 0 - AwayTeam2 10", scoreBoardSummary.getFirst().toString());
        assertEquals("HomeTeam1 10 - AwayTeam1 0", scoreBoardSummary.get(1).toString());
        assertEquals("HomeTeam3 0 - AwayTeam3 0", scoreBoardSummary.get(2).toString());
        assertEquals("HomeTeam 0 - AwayTeam 0", scoreBoardSummary.get(3).toString());
    }

    private static Stream<Arguments> provideHomeAndAwayTeamNames() {
        var otherTeamName = "OtherTeam";
        return Stream.of(
                Arguments.of(HOME_TEAM, otherTeamName, HOME_TEAM),
                Arguments.of(AWAY_TEAM, otherTeamName, AWAY_TEAM),
                Arguments.of(otherTeamName, HOME_TEAM, HOME_TEAM),
                Arguments.of(otherTeamName, AWAY_TEAM, AWAY_TEAM)
        );
    }

    @ParameterizedTest
    @MethodSource("provideHomeAndAwayTeamNames")
    public void newMatchShouldNotBeBetweenTeamsThatAreAlreadyOnBoardTest(String homeTeam, String awayTeam, String existingOnBoard) {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,
                () -> scoreBoard.startNewMatch(homeTeam, awayTeam));
        assertEquals(existingOnBoard + " is already on the board. Can't add it to the board.", exception.getMessage());
    }

    public void homeTeamNameAndAwayTeamNameShouldNotBeTheSame() {
        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,
                () -> scoreBoard.startNewMatch(HOME_TEAM, HOME_TEAM));
        assertEquals("HomeTeam name and AwayTeam name can't be the same.", exception.getMessage());
    }

    private static Stream<Arguments> provideNegativeScoreValues() {
        return Stream.of(
                Arguments.of(-1, 1),
                Arguments.of(1, -1),
                Arguments.of(-1, -1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNegativeScoreValues")
    public void scoreShouldNotBeUpdatedToNegativeValueTest(int homeTeamScore, int awayTeamScore) {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, homeTeamScore, awayTeamScore));
        assertEquals("One of team scores is negative. Can't update match.", exception.getMessage());
    }

    private static Stream<Arguments> provideNullTeamNameValue() {
        return Stream.of(
                Arguments.of(null, AWAY_TEAM),
                Arguments.of(HOME_TEAM, null),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullTeamNameValue")
    public void startNewMatchShouldNotAllowNullTeamNameValuesTest(String homeTeam, String awayTeam) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.startNewMatch(homeTeam, awayTeam));
        assertEquals("Team name can't be null or empty.", exception.getMessage());
    }

}

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
    private static final String HOME_TEAM = "HomeTeam";
    private static final String AWAY_TEAM = "AwayTeam";

    ScoreBoard scoreBoard;

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

    private static Stream<Arguments> provideHomeAndAwayTeamNamesIgnoreCase() {
        return Stream.of(
                Arguments.of(HOME_TEAM, AWAY_TEAM),
                Arguments.of(HOME_TEAM.toLowerCase(), AWAY_TEAM),
                Arguments.of(HOME_TEAM.toUpperCase(), AWAY_TEAM),
                Arguments.of(HOME_TEAM, AWAY_TEAM.toLowerCase()),
                Arguments.of(HOME_TEAM, AWAY_TEAM.toUpperCase())
        );
    }

    @ParameterizedTest
    @MethodSource("provideHomeAndAwayTeamNamesIgnoreCase")
    public void scoreForMatchShouldBeUpdatedTest(String homeTeamName, String awayTeamName) {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        //When
        scoreBoard.updateScore(homeTeamName, awayTeamName, 1, 1);

        //THEN
        assertEquals(1, scoreBoard.getScoreBoardSummary().size());
        assertEquals(HOME_TEAM + " 1 - " + AWAY_TEAM + " 1",
                scoreBoard.getScoreBoardSummary().getFirst().toString());
    }

    @Test
    public void matchShouldNotBeUpdatedIfItDoesntExistTest() {
        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, 1, 1));
        assertEquals("No match found for HomeTeam and AwayTeam", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideHomeAndAwayTeamNamesIgnoreCase")
    public void matchShouldBeFinishedTest(String homeTeamName, String awayTeamName) {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);
        scoreBoard.startNewMatch(HOME_TEAM+"1", AWAY_TEAM+"1");

        //When
        scoreBoard.finishMatch(homeTeamName, awayTeamName);

        //Then
        assertEquals(1, scoreBoard.getScoreBoardSummary().size());
        assertEquals(0, scoreBoard.getScoreBoardSummary().stream()
                .filter(match -> match.toString().equals("HomeTeam 0 - AwayTeam 0")).count());
    }

    @Test
    public void cannotFinishAlreadyFinishedMatchTest() {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);
        scoreBoard.startNewMatch(HOME_TEAM+"1", AWAY_TEAM+"1");
        scoreBoard.finishMatch(HOME_TEAM, AWAY_TEAM);
        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> scoreBoard.finishMatch(HOME_TEAM, AWAY_TEAM));
        assertEquals("No match found for HomeTeam and AwayTeam", exception.getMessage());
    }

    @Test
    public void matchShouldNotBeFinishedIfItDoesntExistTest() {
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

    @Test
    public void scoreBoardSummaryShouldWorkEvenWithoutOngoingMatchesTest() {
        //When & Then
        List<Match> scoreBoardSummary = scoreBoard.getScoreBoardSummary();
        assertTrue(scoreBoardSummary.isEmpty());
    }

    private static Stream<Arguments> provideHomeAndAwayTeamNames() {
        var otherTeamName = "OtherTeam";
        return Stream.of(
                Arguments.of(HOME_TEAM, otherTeamName, HOME_TEAM),
                Arguments.of(AWAY_TEAM, otherTeamName, AWAY_TEAM),
                Arguments.of(otherTeamName, HOME_TEAM, HOME_TEAM),
                Arguments.of(otherTeamName, AWAY_TEAM, AWAY_TEAM),
                Arguments.of(HOME_TEAM.toLowerCase(), otherTeamName, HOME_TEAM.toLowerCase()),
                Arguments.of(AWAY_TEAM.toLowerCase(), otherTeamName, AWAY_TEAM.toLowerCase()),
                Arguments.of(otherTeamName, HOME_TEAM.toUpperCase(), HOME_TEAM.toUpperCase()),
                Arguments.of(otherTeamName, AWAY_TEAM.toUpperCase(), AWAY_TEAM.toUpperCase())
        );
    }

    @ParameterizedTest
    @MethodSource("provideHomeAndAwayTeamNames")
    public void shouldNotAddMatchIfTeamExistsTest(String homeTeam, String awayTeam, String existingOnBoard) {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,
                () -> scoreBoard.startNewMatch(homeTeam, awayTeam));
        assertEquals(existingOnBoard + " is already on the board. Can't add it to the board.", exception.getMessage());
    }

    @Test
    public void homeTeamNameAndAwayTeamNameShouldNotBeTheSameTest() {
        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,
                () -> scoreBoard.startNewMatch(HOME_TEAM, HOME_TEAM));
        assertEquals("HomeTeam name and AwayTeam name can't be the same.", exception.getMessage());
    }

    @Test
    public void homeTeamNameAndAwayTeamNameShouldNotBeTheSameIgnoreCaseTest() {
        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class ,
                () -> scoreBoard.startNewMatch(HOME_TEAM, HOME_TEAM.toLowerCase()));
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

    private static Stream<Arguments> provideNullOrEmptyTeamNameValue() {
        return Stream.of(
                Arguments.of(null, AWAY_TEAM),
                Arguments.of(HOME_TEAM, null),
                Arguments.of(null, null),
                Arguments.of("", AWAY_TEAM),
                Arguments.of(HOME_TEAM, ""),
                Arguments.of("", ""),
                Arguments.of("   ", AWAY_TEAM),
                Arguments.of(HOME_TEAM, "   "),
                Arguments.of("   ", "   ")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullOrEmptyTeamNameValue")
    public void startNewMatchShouldNotAllowNullOrEmptyTeamNameValuesTest(String homeTeam, String awayTeam) {
        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.startNewMatch(homeTeam, awayTeam));
        assertEquals("Team name can't be null or empty.", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideNullOrEmptyTeamNameValue")
    public void updateScoreShouldNotAllowNullTeamNameValuesTest(String homeTeam, String awayTeam) {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);

        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.updateScore(homeTeam, awayTeam, 1, 1));
        assertEquals("Team name can't be null or empty.", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideNullOrEmptyTeamNameValue")
    public void finishMatchShouldNotAllowNullTeamNameValuesTest(String homeTeam, String awayTeam) {
        //When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.finishMatch(homeTeam, awayTeam));
        assertEquals("Team name can't be null or empty.", exception.getMessage());
    }



    @Test
    public void getScoreForAwayTeamTest() {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);
        scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, 5, 7);

        //When
        int scoreAway = scoreBoard.getScoreForTeam(AWAY_TEAM);

        //Then
        assertEquals(7, scoreAway);
    }

    @Test
    public void getScoreForHomeTeamTest() {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);
        scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, 5, 7);

        //When
        int scoreHome = scoreBoard.getScoreForTeam(HOME_TEAM);

        //Then
        assertEquals(5, scoreHome);
    }

    @Test
    public void getScoreForHomeUpperCaseTeamTest() {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);
        scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, 5, 7);

        //When
        int scoreHome = scoreBoard.getScoreForTeam(HOME_TEAM.toUpperCase());

        //Then
        assertEquals(5, scoreHome);
    }

    @Test
    public void getScoreForAwayUpperCaseTeamTest() {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);
        scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, 5, 7);

        //When
        int scoreHome = scoreBoard.getScoreForTeam(AWAY_TEAM.toUpperCase());

        //Then
        assertEquals(7, scoreHome);
    }

    @Test
    public void getScoreForTeamThatDoesNotExistTest() {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);
        scoreBoard.updateScore(HOME_TEAM, AWAY_TEAM, 5, 7);

        //When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.getScoreForTeam("NotExist"));

        //Then
        assertEquals("No match found for NotExist team.", exception.getMessage());
    }

    @Test
    public void getScoreForTeamThatDoesNotExistOnEmptyScoreboardTest() {
        //Given

        //When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreBoard.getScoreForTeam("NotExist"));

        //Then
        assertEquals("No match found for NotExist team.", exception.getMessage());
    }



}

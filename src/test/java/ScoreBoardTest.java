import com.sportsradar.scoreboard.ScoreBoard;
import com.sportsradar.scoreboard.ScoreBoardImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreBoardTest {
    ScoreBoard scoreBoard;
    private final String HOME_TEAM = "HomeTeam";
    private final String AWAY_TEAM = "AwayTeam";

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
    public void scoreBoardSummaryShouldBeReturnedTest() {
        //Given
        scoreBoard.startNewMatch(HOME_TEAM, AWAY_TEAM);
        scoreBoard.startNewMatch(HOME_TEAM+"1", AWAY_TEAM+"1");
        scoreBoard.startNewMatch(HOME_TEAM+"2", AWAY_TEAM+"2");
        scoreBoard.startNewMatch(HOME_TEAM+"3", AWAY_TEAM+"3");
        scoreBoard.updateScore(HOME_TEAM+"2", AWAY_TEAM+"2", 0, 10);

        //When & Then
        assertEquals(4, scoreBoard.getScoreBoardSummary().size());
        assertEquals("HomeTeam2 0 - AwayTeam2 10", scoreBoard.getScoreBoardSummary().getFirst().toString());
        assertEquals("HomeTeam3 0 - AwayTeam3 0", scoreBoard.getScoreBoardSummary().get(1).toString());
        assertEquals("HomeTeam1 0 - AwayTeam1 0", scoreBoard.getScoreBoardSummary().get(2).toString());
        assertEquals("HomeTeam 0 - AwayTeam 0", scoreBoard.getScoreBoardSummary().get(3).toString());

    }

}

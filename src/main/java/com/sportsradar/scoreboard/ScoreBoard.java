package com.sportsradar.scoreboard;

import java.util.List;

public interface ScoreBoard {
    void startNewMatch(String homeTeam, String awayTeam);
    void finishMatch(String homeTeam, String awayTeam);
    void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore);
    List<Match> getScoreBoardSummary();
}

package com.sportsradar.scoreboard;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardImpl implements ScoreBoard {
    private final List<Match> ongoingMatches = new ArrayList<>();

    @Override
    public void startNewMatch(String homeTeam, String awayTeam) {
        ongoingMatches.add(new Match(homeTeam, awayTeam, 0, 0));
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {

    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {

    }

    @Override
    public List<Match> getScoreBoardSummary() {
        return ongoingMatches;
    }
}

package com.sportsradar.scoreboard;

public record Match(String homeTeamName, String awayTeamName, int homeScore, int awayScore) {
    @Override
    public String toString() {
        return homeTeamName + " " + homeScore + " - " + awayTeamName + " " + awayScore;
    }
}

package com.sportsradar.scoreboard;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardImpl implements ScoreBoard {
    private final List<Match> ongoingMatches = new ArrayList<>();

    @Override
    public void startNewMatch(String homeTeam, String awayTeam) {
        ongoingMatches.add(new Match(homeTeam, awayTeam, 0, 0, System.currentTimeMillis()));
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        ongoingMatches.remove(MatchFinder.findMatch(homeTeam, awayTeam, ongoingMatches));
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
        Match matchToBeUpdated = MatchFinder.findMatch(homeTeam, awayTeam, ongoingMatches);
        Match updatedMatch = new Match(homeTeam, awayTeam, homeTeamScore, awayTeamScore, matchToBeUpdated.startTime());
        ongoingMatches.remove(matchToBeUpdated);
        ongoingMatches.add(updatedMatch);
    }

    @Override
    public List<Match> getScoreBoardSummary() {
        return ongoingMatches;
    }

    private static class MatchFinder {

        public static Match findMatch(String homeTeam, String awayTeam, List<Match> matches) {
            return matches.stream()
                    .filter(match -> match.homeTeamName().equals(homeTeam)
                            && match.awayTeamName().equals(awayTeam))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No match found for " + homeTeam + " and " + awayTeam));
        }

    }
}

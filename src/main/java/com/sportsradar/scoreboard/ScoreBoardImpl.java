package com.sportsradar.scoreboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreBoardImpl implements ScoreBoard {
    private final List<Match> ongoingMatches = new ArrayList<>();

    @Override
    public void startNewMatch(String homeTeam, String awayTeam) {
        MatchDataValidator.validateTeamNamesAreNotNullOrEmpty(homeTeam, awayTeam);
        MatchDataValidator.validateTeamsNamesAreNotSame(homeTeam, awayTeam);
        MatchDataValidator.validateTeamsAreNotPresentOnScoreboard(homeTeam, awayTeam, ongoingMatches);
        ongoingMatches.add(new Match(homeTeam, awayTeam, 0, 0, System.nanoTime()));
    }

    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        MatchDataValidator.validateTeamNamesAreNotNullOrEmpty(homeTeam, awayTeam);
        ongoingMatches.remove(MatchFinder.findMatch(homeTeam, awayTeam, ongoingMatches));
    }

    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeTeamScore, int awayTeamScore) {
        MatchDataValidator.validateTeamNamesAreNotNullOrEmpty(homeTeam, awayTeam);
        MatchDataValidator.validateIfScoreUpdateValueIsNegative(homeTeamScore, awayTeamScore);
        Match matchToBeUpdated = MatchFinder.findMatch(homeTeam, awayTeam, ongoingMatches);
        Match updatedMatch = new Match(homeTeam, awayTeam, homeTeamScore, awayTeamScore, matchToBeUpdated.startTime());
        ongoingMatches.remove(matchToBeUpdated);
        ongoingMatches.add(updatedMatch);
    }

    @Override
    public List<Match> getScoreBoardSummary() {
        return ongoingMatches.stream()
                .sorted(Comparator.comparing(  (Match match) -> match.homeScore() + match.awayScore()).reversed()
                        .thenComparing(Match::startTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
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

    private static class MatchDataValidator {

        public static void validateTeamNamesAreNotNullOrEmpty(String homeTeamName, String awayTeamName) {
            if (homeTeamName == null || homeTeamName.isBlank()
                    || awayTeamName == null || awayTeamName.isBlank()) {
                throw new IllegalArgumentException("Team name can't be null or empty.");
            }
        }

        public static void validateTeamsAreNotPresentOnScoreboard(String homeTeamName, String awayTeamName, List<Match> ongoingMatches) {
            String message = " is already on the board. Can't add it to the board.";
            if (ongoingMatches.stream()
                    .anyMatch(match -> match.homeTeamName().equalsIgnoreCase(homeTeamName) || match.awayTeamName().equalsIgnoreCase(homeTeamName))) {
                throw new IllegalArgumentException(homeTeamName + message);
            }
            if (ongoingMatches.stream()
                    .anyMatch(match -> match.homeTeamName().equalsIgnoreCase(awayTeamName) || match.awayTeamName().equalsIgnoreCase(awayTeamName))) {
                throw new IllegalArgumentException(awayTeamName + message);
            }
        }

        public static void validateTeamsNamesAreNotSame(String homeTeamName, String awayTeamName) {
            if(homeTeamName.equalsIgnoreCase(awayTeamName)) {
                throw new IllegalArgumentException("HomeTeam name and AwayTeam name can't be the same.");
            }
        }

        public static void validateIfScoreUpdateValueIsNegative(int homeTeamScore, int awayTeamScore) {
            if (homeTeamScore < 0 || awayTeamScore < 0) {
                throw new IllegalArgumentException("One of team scores is negative. Can't update match.");
            }
        }

    }
}

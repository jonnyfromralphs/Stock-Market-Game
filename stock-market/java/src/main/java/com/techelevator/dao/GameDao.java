package com.techelevator.dao;

import com.techelevator.model.Game;
import com.techelevator.model.Invitation;

import java.util.List;

public interface GameDao {

    List<Game> getAllGames(int userId);

    List<Game> getInvitedGames(int userId);

    List<Game> getAcceptedGames(int userId);

    List<Game> getRejectedGames(int userId);

    void updateInvitationStatus(Invitation invitation, int userId);

    Game getGameById(int gameId);

    Game findByGameName(String gameName);

    int create(String gameName, int organizerId, int gameLengthDays, String[] players);

    boolean delete(int gameId);

    boolean isGameEnded(int gameId);

}

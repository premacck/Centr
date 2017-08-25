package com.prembros.centr;

/**
 *
 * Created by Prem $ on 8/11/2017.
 */

@SuppressWarnings("WeakerAccess")
public interface PlayServices {
    boolean isStoreVersion();
    void signIn();
    void signOut();
    void rateGame();
    void unlockAchievement(String id);
    void incrementAchievement(String resId);
    void submitScore(int score);
    void showAchievements();
    void showLeaderBoard();
    void showScore();
    boolean isSignedIn ();
}

package com.prembros.centr;

/**
 *
 * Created by Prem $ on 8/11/2017.
 */

@SuppressWarnings("WeakerAccess")
public interface PlayServices {
    void signIn();
    void signOut();
    void rateGame();
    void unlockAchievement(int resId);
    void incrementAchievement(int resId);
    void submitScore(int score);
    void showAchievements();
    void showScore();
    boolean isSignedIn ();
}

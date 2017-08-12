package com.prembros.centr;

/**
 *
 * Created by Prem $ on 8/11/2017.
 */

@SuppressWarnings("WeakerAccess")
public interface PlayServices {
    boolean isStoreVersion();
    boolean signIn();
    boolean signOut();
    void rateGame();
    void unlockAchievement(String id);
    void incrementAchievement(String resId);
    void submitScore(int score);
    void showAchievements();
    void showScore();
    boolean isSignedIn ();
}

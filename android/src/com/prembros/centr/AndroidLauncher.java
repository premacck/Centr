package com.prembros.centr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication implements PlayServices {

	private final static int REQUEST_CODE = 1;
	private GameHelper gameHelper;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);

		gameHelper.setup(new GameHelper.GameHelperListener() {
			@Override
			public void onSignInFailed() {

			}

			@Override
			public void onSignInSucceeded() {

			}
		});

		initialize(new MyGdxGame(this), config);
	}

	@Override
	protected void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean isStoreVersion() {
		String installer = getPackageManager().getInstallerPackageName(getPackageName());
		return !TextUtils.isEmpty(installer);
	}

	@Override
	public boolean signIn() {
		try {
            gameHelper.beginUserInitiatedSignIn();
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Sign in failed: " + e.getMessage() + ".");
		}
		return isSignedIn();
	}

	@Override
	public boolean signOut() {
		try {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameHelper.signOut();
				}
			});
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Sign out failed: " + e.getMessage() + ".");
		}
		return !isSignedIn();
	}

	@Override
	public void rateGame() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("play.google.com/store/apps/details?id=com.prembros.centr")));
	}

	@Override
	public void unlockAchievement(String id) {
		Games.Achievements.unlock(gameHelper.getApiClient(), id);
	}

	@Override
	public void incrementAchievement(String id) {
		Games.Achievements.increment(gameHelper.getApiClient(), id, 1);
	}

	@Override
	public void submitScore(int score) {
		if (isSignedIn()) {
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), "CgkIvMGr3JQfEAIQAQ", score);
		}
	}

	@Override
	public void showAchievements() {
		if (isSignedIn()) {
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), REQUEST_CODE);
		} else signIn();
	}

	@Override
	public void showScore() {
		if (isSignedIn()) {
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), "CgkIvMGr3JQfEAIQAQ"), REQUEST_CODE);
		} else signIn();
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}
}

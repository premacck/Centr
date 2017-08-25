package com.prembros.centr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class AndroidLauncher
		extends AndroidApplication
		implements PlayServices, AdHandler, GameHelper.GameHelperListener, RewardedVideoAdListener {

	private static final String APP_ID = "ca-app-pub-0057541165660355~2956523724";
//	private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-0057541165660355/4854631585";
//	private static final String EARN_CENTROIN_AD_UNIT_ID = "ca-app-pub-0057541165660355/4854631585";
//	private static final String REVIVAL_AD_UNIT_ID = "ca-app-pub-0057541165660355/751405929";
	private static final String TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
	private static final int REQUEST_CODE = 1;

	private GameHelper gameHelper;
	private RewardedVideoAd rewardedVideoAd;
	private IInAppBillingService service;
	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
			service = IInAppBillingService.Stub.asInterface(iBinder);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			service = null;
		}
	};

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		MobileAds.initialize(this, APP_ID);
		rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
		rewardedVideoAd.setRewardedVideoAdListener(this);
		loadRewardedVideoAd(TEST_AD_UNIT_ID);

		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);
		gameHelper.setMaxAutoSignInAttempts(2);

		gameHelper.setup(this);

		Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

		initialize(new MyGdxGame(this, this), config);
	}

	private void loadRewardedVideoAd(String id) {
		rewardedVideoAd.loadAd(id, new AdRequest.Builder().addTestDevice("1D9C645C40BF8692A69F5D42126C91A7").build());
	}

	@Override
	public void showRewarrdedVideoAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (rewardedVideoAd.isLoaded()) {
					rewardedVideoAd.show();
				} else {
					loadRewardedVideoAd(TEST_AD_UNIT_ID);
					Toast.makeText(AndroidLauncher.this, "Something went wrong\nPlease try again", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public boolean hasRewardedVideoAd(){
		return rewardedVideoAd.isLoaded();
	}

	@Override
	public void showRevivalCountDown() {
		int progress = 100;
		final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
		final AlertDialog[] dialog = new AlertDialog[1];
		View root = View.inflate(this, R.layout.countdown, null);
		builder.setView(root);
		builder.setCancelable(false);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				dialog[0] = builder.show();
			}
		});

		final ProgressBar progressBar = root.findViewById(R.id.revive_progressbar);
		progressBar.setProgress(progress);
		for (progress = 100; progress >= 0; progress--) {
			final int finalProgress = progress;
			try {
				Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(finalProgress);
                    }
                });
				thread.start();
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		dialog[0].dismiss();
	}

	public void reviveButtonClick(View view) {
		showRewarrdedVideoAd();
		makeText(this, "Revived!!", Toast.LENGTH_SHORT).show();
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
	protected void onResume() {
		rewardedVideoAd.resume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		rewardedVideoAd.pause(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		rewardedVideoAd.destroy(this);
		if (service != null) {
			unbindService(serviceConnection);
		}
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
	public void signIn() {
		try {
            gameHelper.beginUserInitiatedSignIn();
		} catch (Exception e) {
			Gdx.app.log("MainActivity", "Sign in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut() {
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
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), String.valueOf(R.string.leaderboard_centr_hall_of_famers), score);
		}
	}

	@Override
	public void showAchievements() {
		if (isSignedIn()) {
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), REQUEST_CODE);
		} else signIn();
	}

	@Override
	public void showLeaderBoard() {
		if (isSignedIn()) {
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
					String.valueOf(R.string.leaderboard_centr_hall_of_famers)), REQUEST_CODE);
		} else signIn();
	}

	@Override
	public void showScore() {
		if (isSignedIn()) {
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
					String.valueOf(R.string.leaderboard_centr_hall_of_famers)), REQUEST_CODE);
		} else signIn();
	}

	@Override
	public boolean isSignedIn() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}

	@Override
	public void onRewardedVideoAdLoaded() {
		makeText(this, "onRewardedVideoAdLoaded", LENGTH_SHORT).show();
	}

	@Override
	public void onRewardedVideoAdOpened() {
		makeText(this, "onRewardedVideoAdOpened", LENGTH_SHORT).show();
	}

	@Override
	public void onRewardedVideoStarted() {
		makeText(this, "onRewardedVideoStarted", LENGTH_SHORT).show();
	}

	@Override
	public void onRewardedVideoAdClosed() {
		loadRewardedVideoAd(TEST_AD_UNIT_ID);
		makeText(this, "onRewardedVideoAdClosed", LENGTH_SHORT).show();
	}

	@Override
	public void onRewarded(RewardItem rewardItem) {
		makeText(this, "onRewarded!", LENGTH_SHORT).show();
	}

	@Override
	public void onRewardedVideoAdLeftApplication() {
		makeText(this, "onRewardedVideoAdLeftApplication", LENGTH_SHORT).show();
	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {
		loadRewardedVideoAd(TEST_AD_UNIT_ID);
		makeText(this, "onRewardedVideoAdFailedToLoad, ErrorCode: " + i, LENGTH_SHORT).show();
	}
}
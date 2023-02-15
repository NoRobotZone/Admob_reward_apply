package com.example.admob_reward_type

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class MainActivity : AppCompatActivity() {
    private var mRewardedAd: RewardedAd? = null

    private final var TAG = "MainActivity"

//    private var reward: Int = 0

//    var reward_text : TextView = findViewById(R.id.textView)




    fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            this, "ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.d("TAG", p0.message)
                    mRewardedAd = null
                }

                override fun onAdLoaded(p0: RewardedAd) {
                    Toast.makeText(applicationContext, "Ad loading succeed", Toast.LENGTH_SHORT).show()
                    mRewardedAd = p0
                }
            }
        )
    }

    fun showRewardedAd() {
        if (mRewardedAd != null) {
            mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("TAG", "Ad was dismissed")
                    mRewardedAd = null
                    loadRewardedAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    Log.d("TAG", "Ad failed to show.")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("TAG", "Ad showed fullscreen content.")
                    mRewardedAd = null
                }
            }
            mRewardedAd?.show(this, OnUserEarnedRewardListener() { rewardItem ->
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
//                reward += rewardAmount
//                reward_text.text = "$rewardType : $reward"
            })
        } else {
            Log.d("TAG", "The rewarded ad was not loaded yet")
        }
    }



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        val reward_button : Button = findViewById(R.id.rewardads)



        loadRewardedAd()



        reward_button.setOnClickListener{
            showRewardedAd()

        }



        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mRewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mRewardedAd = null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }


    }
}
package com.raywenderlich.whacardroid

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.google.ar.sceneform.rendering.Light
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.raywenderlich.whacardroid.Configuration.Companion.START_LIVES

class MainActivity : AppCompatActivity() {

  private lateinit var arFragment: ArFragment

  private lateinit var scoreboard: ScoreboardView

  private var droidRenderable: ModelRenderable? = null

  private var scoreboardRenderable: ViewRenderable? = null

  private var failLight: Light? = null

  private var gameHandler = Handler()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

    initResources()

  }

  private fun initResources() {

    failLight = Light.builder(Light.Type.POINT)
            .setColor(com.google.ar.sceneform.rendering.Color(Color.RED))
            .setShadowCastingEnabled(true)
            .setIntensity(0F)
            .build()

    scoreboard = ScoreboardView(this)
    scoreboard.onStartTapped =  {
      scoreboard.life = START_LIVES
      scoreboard.score = 0
    }

    ViewRenderable.builder()
            .setView(this, scoreboard)
            .build()
            .thenAccept {
              it.isShadowReceiver = true
              scoreboardRenderable = it
            }
            .exceptionally { it.toast(this) }

    ModelRenderable.builder()
            .setSource(this, R.raw.andy)
            .build()
            .thenAccept { droidRenderable = it }
            .exceptionally {it.toast(this)}
  }

  private fun failHit() {
    scoreboard.score -= 50
    scoreboard.life -= 1
    if (scoreboard.life <= 0) {
      // Game over
      gameHandler.removeCallbacksAndMessages(null)
    }
  }
}
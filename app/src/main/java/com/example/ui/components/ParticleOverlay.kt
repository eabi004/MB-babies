package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import com.example.ui.theme.KidsGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsPrimary
import com.example.ui.theme.KidsPurple
import com.example.ui.theme.KidsSecondary
import com.example.ui.theme.KidsYellow
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Particle(
    val xRatio: Float,
    val yRatio: Float,
    val velocityX: Float,
    val velocityY: Float,
    val size: Float,
    val color: Color,
    val shapeType: ParticleShape,
    val rotationSpeed: Float,
    val emoji: String? = null
)

enum class ParticleShape {
    CIRCLE, RECT, EMOJI
}

@Composable
fun ParticleOverlay(
    trigger: Boolean,
    modifier: Modifier = Modifier
) {
    if (!trigger) return

    val animProgress = remember(trigger) { Animatable(0f) }

    val particles = remember(trigger) {
        val colors = listOf(KidsPrimary, KidsSecondary, KidsYellow, KidsGreen, KidsOrange, KidsPurple, Color(0xFF00E5FF))
        val emojis = listOf("⭐", "✨", "🎉", "🎈", "🪙", "🌟")

        List(35) {
            val angle = Random.nextFloat() * 2f * Math.PI.toFloat()
            val speed = Random.nextFloat() * 800f + 400f
            val shape = ParticleShape.entries.random()

            Particle(
                xRatio = 0.5f + (Random.nextFloat() - 0.5f) * 0.1f,
                yRatio = 0.45f + (Random.nextFloat() - 0.5f) * 0.1f,
                velocityX = cos(angle.toDouble()).toFloat() * speed,
                velocityY = sin(angle.toDouble()).toFloat() * speed - 250f,
                size = Random.nextFloat() * 18f + 12f,
                color = colors.random(),
                shapeType = shape,
                rotationSpeed = (Random.nextFloat() - 0.5f) * 720f,
                emoji = if (shape == ParticleShape.EMOJI) emojis.random() else null
            )
        }
    }

    LaunchedEffect(trigger) {
        animProgress.snapTo(0f)
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1800, easing = FastOutLinearInEasing)
        )
    }

    if (animProgress.value < 1f) {
        val progress = animProgress.value

        Canvas(modifier = modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val gravity = 900f * progress * progress

            particles.forEach { p ->
                val posX = (p.xRatio * canvasWidth) + (p.velocityX * progress)
                val posY = (p.yRatio * canvasHeight) + (p.velocityY * progress) + gravity
                val alpha = (1f - progress).coerceIn(0f, 1f)
                val currentRotation = p.rotationSpeed * progress

                if (posX in -100f..(canvasWidth + 100f) && posY in -100f..(canvasHeight + 100f)) {
                    rotate(degrees = currentRotation, pivot = Offset(posX, posY)) {
                        when (p.shapeType) {
                            ParticleShape.CIRCLE -> {
                                drawCircle(
                                    color = p.color.copy(alpha = alpha),
                                    radius = p.size,
                                    center = Offset(posX, posY)
                                )
                            }
                            ParticleShape.RECT -> {
                                drawRect(
                                    color = p.color.copy(alpha = alpha),
                                    topLeft = Offset(posX - p.size, posY - p.size / 2f),
                                    size = Size(p.size * 2f, p.size)
                                )
                            }
                            ParticleShape.EMOJI -> {
                                val paint = android.graphics.Paint().apply {
                                    textSize = p.size * 2.2f
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    this.alpha = (alpha * 255).toInt()
                                }
                                drawContext.canvas.nativeCanvas.drawText(
                                    p.emoji ?: "⭐",
                                    posX,
                                    posY + p.size / 2f,
                                    paint
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

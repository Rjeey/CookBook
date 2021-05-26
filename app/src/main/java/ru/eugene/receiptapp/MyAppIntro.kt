package ru.eugene.receiptapp

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class MyAppIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        setIndicatorColor(
            selectedIndicatorColor = ContextCompat.getColor(baseContext, R.color.IntroSelected),
            unselectedIndicatorColor = ContextCompat.getColor(baseContext, R.color.IntroUnselected)
        )
        addSlide(
            AppIntroFragment.newInstance(
            title = "Welcome",
            description = "To Recipe App!",
            imageDrawable = R.drawable.img1,
            titleColor = Color.WHITE,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.GRAY

        ))
        addSlide(AppIntroFragment.newInstance(
            title = "This App is about recipes",
            imageDrawable = R.drawable.img2,
            titleColor = Color.WHITE,
            descriptionColor = Color.WHITE,
            backgroundColor = Color.GRAY
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        finish()
    }
}
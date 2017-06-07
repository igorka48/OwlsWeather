package owlsdevelopers.com.owlsweather.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.vansuita.materialabout.builder.AboutBuilder
import owlsdevelopers.com.owlsweather.R


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = AboutBuilder.with(this)
                .setPhoto(R.mipmap.ic_launcher_circle)
                .setCover(R.mipmap.profile_cover)
                .setName(R.string.app_name)
                .setSubTitle("Owls Developers")
                //.setBrief("I'm warmed of mobile technologies. Ideas maker, curious and nature lover.")
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .addGooglePlayStoreLink("com.OwlsWeather")
                .addGitHubLink("igorka48")
                //.addFacebookLink("user")
                .addFiveStarsAction()
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build()

        setContentView(view)
    }
}

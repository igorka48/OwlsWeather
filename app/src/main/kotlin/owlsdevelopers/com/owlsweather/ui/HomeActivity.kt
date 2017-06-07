package owlsdevelopers.com.owlsweather.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.activity_home.*
import owlsdevelopers.com.owlsweather.OwlsWeatherApplication
import owlsdevelopers.com.owlsweather.R
import owlsdevelopers.com.owlsweather.WeatherRcvService
import owlsdevelopers.com.owlsweather.data.DataManager

class HomeActivity : AppCompatActivity() {


     lateinit var data: DataManager
     var selectedPage = 0

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    /**
     * The [ViewPager] that will host the section contents.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = (applicationContext as OwlsWeatherApplication).dataManager
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        viewPager?.adapter = mSectionsPagerAdapter
        viewPager?.addOnPageChangeListener(
                object: ViewPager.SimpleOnPageChangeListener(){
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        selectedPage = position
                    }
                }
        )

        addTownView.visibility = GONE
        initAds()

    }

    override fun onResume() {
        super.onResume()
       // mSectionsPagerAdapter?.notifyDataSetChanged()
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        // Set up the ViewPager with the sections adapter.
        viewPager?.adapter = mSectionsPagerAdapter
        if(data.towns.isNotEmpty())
            WeatherRcvService.loadWeather(this, data.towns[0].townCode, false)
    }


    fun initAds(){
        val adRequest =  AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val settingsActivity = Intent(baseContext,
                        SettingsActivity::class.java)
                startActivity(settingsActivity)
                return true
            }
            R.id.add -> {
                val activity = Intent(baseContext,
                        AddTownActivity::class.java)
                startActivity(activity)
                return true
            }
            R.id.remove -> {
                val dm = (applicationContext as OwlsWeatherApplication).dataManager
                try {
                    dm.removeTown(selectedPage)
                    dm.save(this)

                    mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
                    // Set up the ViewPager with the sections adapter.
                    viewPager?.adapter = mSectionsPagerAdapter
                   // mSectionsPagerAdapter?.notifyDataSetChanged()
                   // viewPager.adapter.notifyDataSetChanged()
                } catch (e: Exception) {

                }

                return true
            }
            R.id.refresh -> {
                if(!data.towns.isEmpty())
                    WeatherRcvService.loadWeather(this, data.towns[selectedPage].townCode, true)
                return true
            }
            R.id.about -> {
                showAbout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }



    fun addTownAction(view: View){
         val activity = Intent(baseContext,
                 AddTownActivity::class.java)
         startActivity(activity)
    }



    fun showAbout(){
        val activity = Intent(baseContext,
                AboutActivity::class.java)
        startActivity(activity)
    }



    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return TownFragment.create(position)
        }

        override fun getCount(): Int {
            if(data.towns.isEmpty()){
                addTownView.visibility = VISIBLE
            } else {
                addTownView.visibility = GONE
            }
            return data.towns.size
        }

    }
}

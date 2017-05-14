package owlsdevelopers.com.owlsweather

import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup

import kotlinx.android.synthetic.main.activity_home.*
import owlsdevelopers.com.owlsweather.data.DataManager

class HomeActivity : AppCompatActivity() {


     lateinit var data: DataManager


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
        addTownView.visibility = GONE

//        fab.setOnClickListener({ view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        })

    }

    override fun onResume() {
        super.onResume()
        mSectionsPagerAdapter?.notifyDataSetChanged()
        if(data.towns.isNotEmpty())
            WeatherRcvService.loadWeather(this, data.town)
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
                   // dm.removeTown(mPager.getCurrentItem())
                   // initPager()
                } catch (e: Exception) {

                }

                return true
            }
            R.id.refresh -> {
                //val tf = mPagerAdapter.getItem(mPager.getCurrentItem())
                //if (tf != null) {
                //    tf!!.forceUpdate = true
                //    tf!!.loadWeather()
                //}
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return TownFragment.create(position)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return data.towns.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "SECTION 1"
                1 -> return "SECTION 2"
                2 -> return "SECTION 3"
            }
            return null
        }
    }
}

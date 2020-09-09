package com.app.washmania

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.app.washmania.others.BaseActivity

class WelcomeActivity : BaseActivity() {

    override fun initResources() {
        context = this
        viewPager = findViewById(R.id.view_pager) as ViewPager
        dotsLayout = findViewById(R.id.layoutDots) as LinearLayout
        btnSkip = findViewById(R.id.btn_skip) as Button
        btnNext = findViewById(R.id.btn_next) as Button
    }

    override fun initListeners() {
        layouts =
            intArrayOf(R.layout.welcome_slide1, R.layout.welcome_slide2, R.layout.welcome_slide3)

        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        // changeStatusBarColor();

        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager!!.setAdapter(myViewPagerAdapter)
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)

        btnSkip!!.setOnClickListener(View.OnClickListener { launchHomeScreen() })

        btnNext!!.setOnClickListener(View.OnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val current = getItem(+1)
            if (current < layouts!!.size) {
                // move to next screen
                viewPager!!.setCurrentItem(current)
            } else {
                launchHomeScreen()
            }
        })
    }

    private var viewPager: ViewPager? = null
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var dotsLayout: LinearLayout? = null
    private var layouts: IntArray? = null
    private var btnSkip: Button? = null
    private var btnNext: Button? = null
    var context: Context? = null
    private var dots: Array<TextView?>? = null


    override fun getLayout(): Int {
        return R.layout.welcome_activity
    }


    private fun launchHomeScreen() {
        val intent = Intent(context, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun addBottomDots(currentPage: Int) {
        dots = Array<TextView?>(layouts!!.size) { null }

        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        dotsLayout!!.removeAllViews()
        for (i in dots!!.indices) {
            dots!![i] = TextView(this)
            dots!![i]!!.text = Html.fromHtml("&#8226;")
            dots!![i]!!.textSize = 35f
            dots!![i]!!.setTextColor(colorsInactive[currentPage])
            dotsLayout!!.addView(dots!![i])
        }

        if (dots!!.size > 0)
            dots!![currentPage]!!.setTextColor(colorsActive[currentPage])
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.getCurrentItem() + i
    }


    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(layouts!![position], container, false)
            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    //  viewpager change listener
    internal var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                addBottomDots(position)

                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts!!.size - 1) {
                    // last page. make button text to GOT IT
                    btnNext!!.setText(getString(R.string.start))
                    btnSkip!!.setVisibility(View.GONE)
                } else {
                    // still pages are left
                    btnNext!!.setText(getString(R.string.next))
                    btnSkip!!.setVisibility(View.VISIBLE)
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(arg0: Int) {

            }
        }

}

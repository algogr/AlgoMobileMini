package gr.algo.algomobilemini

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import java.io.Serializable
import java.util.ArrayList

class TabPagerAdapter(fm: FragmentManager, private var tabCount: Int) :
        FragmentPagerAdapter(fm) {

    var finDocLines= mutableListOf<FinDocLine>()
    private val mFragmentList: ArrayList<Fragment> = ArrayList()
    override fun getItem(position: Int): Fragment? {
/*/
        when (position) {
            0 -> {
                val k=lineItem()
                val t=Tab1Fragment()
                return t
            }

            1 -> {

                val  t=Tab2Fragment()

                val args=Bundle()
                args.putSerializable("list",finDocLines as Serializable)

                t.arguments=args
                return t
            }
            else -> return null
        }
*/

        return mFragmentList.get(position)

    }

    override fun getCount(): Int {
        return tabCount
    }

    fun addFragment(fragment:Fragment){
        mFragmentList.add(fragment)
    }
}
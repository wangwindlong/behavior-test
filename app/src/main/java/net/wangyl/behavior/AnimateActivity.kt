package net.wangyl.behavior

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_animate.*


class AnimateActivity : AppCompatActivity() {

    private val mSheetBehavior: BottomSheetBehavior<*>? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_item)
        setSupportActionBar(toolbar)
        fab_1.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        initEvent()
        initData()

    }

    fun initEvent() {
        button_bottom_sheet_layout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
//                toggleBottomSheetLayout(mSheetBehavior)
            }
        })
        button_bottom_sheet_dialog.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (mBottomSheetDialog != null) {
                    if (mBottomSheetDialog!!.isShowing) {
                        mBottomSheetDialog!!.dismiss()
                    } else {
                        mBottomSheetDialog!!.show()
                    }
                }
            }
        })
    }

    fun initData() {
        recycler_sheet_item.setAdapter(object : BottomSheetAdapter(obtainSheetLayoutData()) {

        })
        createBottomSheetDialog()
    }

    private fun createBottomSheetDialog() {
        if (mBottomSheetDialog == null) {
            mBottomSheetDialog = BottomSheetDialog(this, R.style.Theme_Material_Light_Dialog_NoActionBar)
            val view: View =
                LayoutInflater.from(this).inflate(R.layout.list_content, null, false)
            mBottomSheetDialog!!.setContentView(view)
//            val recyclerView: RecyclerView = view.findViewById(R.id.recycler_sheet_dialog_content)
//            recyclerView.layoutManager = LinearLayoutManager(mContext)
//            recyclerView.adapter = BottomSheetAdapter(obtainSheetLayoutDialog())
            //			setBehaviorCallback();
        }
    }

    private fun obtainSheetLayoutData(): List<String>? {
        val data: MutableList<String> = ArrayList()
        for (index in 0..19) {
            data.add("Bottom Sheet Layout $index")
        }
        return data
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
        return super.onOptionsItemSelected(item)
    }
}
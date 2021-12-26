package com.example.internshipapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.example.internshipapp.R
import com.example.internshipapp.databinding.ActivityMainBinding
import com.example.internshipapp.db.AreaDatabase
import com.example.internshipapp.db.DataBaseCreator
import com.example.internshipapp.entity.Area
import com.example.internshipapp.ui.fragment.AllAreasFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var areaDatabase: AreaDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        areaDatabase = DataBaseCreator().initDB(this)
        begin(null, true)
    }

    private fun begin(areas: ArrayList<Area>?, isNeedToAddToStack: Boolean) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, AllAreasFragment(areas))
            if (isNeedToAddToStack) addToBackStack(null)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.app_menu, menu)
        val searchView: SearchView = menu?.findItem(R.id.search_bar)?.actionView as SearchView
        searchView.queryHint = getString(R.string.searching)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val areas = areaDatabase.areaDao().getByName(query)
                if (areas != null) {
                    begin(arrayListOf(areas), true)
                } else {
                    AlertDialog.Builder(this@MainActivity).setMessage("Ничего не найдено").show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.update_areas) {
            AlertDialog.Builder(this)
                .setMessage(R.string.update_all_areas_notification)
                .setPositiveButton("Да") { dialog, _ ->
                    areaDatabase.areaDao().deleteAll()
                    begin(null, false)
                    dialog.dismiss()
                }
                .setNegativeButton("Нет") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

        }
        return true
    }
}
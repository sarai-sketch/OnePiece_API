package com.practica_api

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.practica_api.databinding.ActivityMainBinding
import com.practica_api.di.AppContainer
import com.practica_api.ui.battle.BattleFragment
import com.practica_api.ui.characters.CharactersFragment
import com.practica_api.ui.favoritos.FavoritosFragment
import com.practica_api.ui.fruits.FruitsFragment
import com.practica_api.ui.sagas.SagasFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContainer.init(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.header) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = bars.top)
            insets
        }

        binding.toolbar.inflateMenu(R.menu.toolbar_menu)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_logout) {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            } else {
                false
            }
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            showTab(item.itemId)
            true
        }

        if (savedInstanceState == null) {
            binding.bottomNav.selectedItemId = R.id.nav_characters
        } else {
            updateTitle(binding.bottomNav.selectedItemId)
        }
    }

    private fun showTab(itemId: Int) {
        val fragment: Fragment = when (itemId) {
            R.id.nav_fruits -> FruitsFragment()
            R.id.nav_sagas -> SagasFragment()
            R.id.nav_favorites -> FavoritosFragment()
            R.id.nav_battle -> BattleFragment()
            else -> CharactersFragment()
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragmentContainer, fragment)
        }
        updateTitle(itemId)
    }

    private fun updateTitle(itemId: Int) {
        binding.toolbar.setTitle(
            when (itemId) {
                R.id.nav_fruits -> R.string.tab_fruits
                R.id.nav_sagas -> R.string.tab_sagas
                R.id.nav_favorites -> R.string.tab_favorites
                R.id.nav_battle -> R.string.tab_battle
                else -> R.string.tab_characters
            },
        )
    }
}

package br.com.wphec.sensors.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.com.wphec.sensors.database.AppDatabase
import br.com.wphec.sensors.extensions.display
import br.com.wphec.sensors.model.User
import br.com.wphec.sensors.preferences.dataStore
import br.com.wphec.sensors.preferences.usuarioLogadoPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class UsuarioBaseActivity : AppCompatActivity() {

    private val usuarioDao by lazy {
        AppDatabase.instance(this).UserDao()
    }
    private var _user: MutableStateFlow<User?> = MutableStateFlow(null)
    protected var user: StateFlow<User?> = _user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            verificaUsuarioLogado()
        }
    }

    private suspend fun verificaUsuarioLogado() {
        dataStore.data.collect { preferences ->
            preferences[usuarioLogadoPreferences]?.let { usuarioId ->
                buscaUsuario(usuarioId)
            } ?: vaiParaLogin()
        }
    }

    private suspend fun buscaUsuario(usuarioId: String) {
            _user.value = usuarioDao
                .searchById(usuarioId)
                .firstOrNull()
    }

    protected suspend fun deslogaUsuario() {
        dataStore.edit { preferences ->
            preferences.remove(usuarioLogadoPreferences)
        }
    }

    private fun vaiParaLogin() {
        display(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }

}
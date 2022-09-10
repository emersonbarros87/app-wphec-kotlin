package br.com.wphec.sensors.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import br.com.wphec.sensors.database.AppDatabase
import br.com.wphec.sensors.databinding.ActivityLoginBinding
import br.com.wphec.sensors.extensions.toast
import br.com.wphec.sensors.extensions.display
import br.com.wphec.sensors.preferences.dataStore
import br.com.wphec.sensors.preferences.usuarioLogadoPreferences
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val usuarioDao by lazy {
        AppDatabase.instance(this).UserDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
        configuraBotaoEntrar()
    }

    private fun configuraBotaoEntrar() {
        binding.activityLoginBotaoEntrar.setOnClickListener {
            val usuario = binding.activityLoginUsuario.text.toString()
            val senha = binding.activityLoginSenha.text.toString()
            autentica(usuario, senha)
        }
    }

    private fun autentica(usuario: String, senha: String) {
        lifecycleScope.launch {
            usuarioDao.authentication(usuario, senha)?.let { usuario ->
                dataStore.edit { preferences ->
                    preferences[usuarioLogadoPreferences] = usuario.id
                }
                display(ListaProdutosActivity::class.java)
                finish()
            } ?: toast("Falha na autenticação")
        }
    }

    private fun configuraBotaoCadastrar() {
        binding.activityLoginBotaoCadastrar.setOnClickListener {
            display(FormularioCadastroUsuarioActivity::class.java)
        }
    }

}
package br.com.wphec.sensors.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.wphec.sensors.database.AppDatabase
import br.com.wphec.sensors.databinding.ActivityFormularioCadastroUsuarioBinding
import br.com.wphec.sensors.extensions.toast
import br.com.wphec.sensors.model.User
import kotlinx.coroutines.launch

class FormularioCadastroUsuarioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioCadastroUsuarioBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDatabase.instance(this).UserDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
    }

    private fun configuraBotaoCadastrar() {
        binding.activityFormularioCadastroBotaoCadastrar.setOnClickListener {
            val novoUsuario = criaUsuario()
            cadastra(novoUsuario)
        }
    }

    private fun cadastra(user: User) {
        lifecycleScope.launch {
            try {
                dao.salva(user)
                finish()
            } catch (e: Exception) {
                Log.e("CadastroUsuario", "configuraBotaoCadastrar: ", e)
                toast("Falha ao cadastrar usuário")
            }
        }
    }

    private fun criaUsuario(): User {
        val usuario = binding.activityFormularioCadastroUsuario.text.toString()
        val nome = binding.activityFormularioCadastroNome.text.toString()
        val senha = binding.activityFormularioCadastroSenha.text.toString()
        return User(usuario, nome, senha)
    }
}
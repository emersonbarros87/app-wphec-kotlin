package br.com.wphec.sensors.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import br.com.wphec.sensors.database.AppDatabase
import br.com.wphec.sensors.database.dao.SensorDao
import br.com.wphec.sensors.databinding.ActivityFormSensorBinding
import br.com.wphec.sensors.model.Sensor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class FormularioProdutoActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityFormSensorBinding.inflate(layoutInflater)
    }
//    private var url: String? = null
    private var produtoId = 0L
    private val sensorDao: SensorDao by lazy {
        val db = AppDatabase.instance(this)
        db.SensorDao()
    }
    private val usuarioDao by lazy {
        AppDatabase.instance(this).UserDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = "Cadastrar Sensores"
        configuraBotaoSalvar()
//        binding.activityFormularioProdutoAlerta.setOnClickListener {
//            FormularioImagemDialog(this)
//                .mostra(campoAlerta) { imagem ->
//                    campoAlerta = imagem
////                    binding.activityFormularioProdutoImagem.tentaCarregarImagem(url)
//                }
//        }

        tentaCarregarProduto()
        lifecycleScope.launch {
            user
                .filterNotNull()
                .collect {
                    Log.i("FormularioProduto", "onCreate: $it")
            }
        }
    }

    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    override fun onResume() {
        super.onResume()
        tentaBuscarProduto()
    }

    private fun tentaBuscarProduto() {
        lifecycleScope.launch {
            sensorDao.searchById(produtoId).collect {
                it?.let { produtoEncontrado ->
                    title = "Alterar produto"
                    preencheCampos(produtoEncontrado)
                }
            }
        }
    }

    private fun preencheCampos(sensor: Sensor) {
        binding.activityFormularioProdutoDescricao
            .setText(sensor.volume)
        binding.activityFormularioProdutoNome
            .setText(sensor.date)
        binding.activityFormularioProdutoValor
            .setText(sensor.weather)
        binding.activityFormularioProdutoAlerta
            .setText(sensor.alert)
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar

        botaoSalvar.setOnClickListener {
            val produtoNovo = criaProduto()
            lifecycleScope.launch {
                sensorDao.save(produtoNovo)
                finish()
            }
        }
    }

    private fun criaProduto(): Sensor {
        val fieldVolume = binding.activityFormularioProdutoDescricao
        val volume = fieldVolume.text.toString() + "%"
        val fieldDate = binding.activityFormularioProdutoNome
        val date = fieldDate.text.toString()
        val fieldWeather = binding.activityFormularioProdutoValor
        val weather = fieldWeather.text.toString()
        val fieldAlert = binding.activityFormularioProdutoAlerta
        val alert = fieldAlert.text.toString()

        return Sensor(
            id = produtoId,
            volume = volume,
            date = date,
            weather = weather,
            alert = alert
        )
    }

}
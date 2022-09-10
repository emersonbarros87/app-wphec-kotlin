package br.com.wphec.sensors.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import br.com.wphec.sensors.R
import br.com.wphec.sensors.database.AppDatabase
import br.com.wphec.sensors.databinding.ActivityDetalhesProdutoBinding
import br.com.wphec.sensors.model.Sensor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetalhesProdutoActivity : AppCompatActivity() {

    private var produtoId: Long = 0L
    private var sensor: Sensor? = null
    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        AppDatabase.instance(this).SensorDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tentaCarregarProduto()
    }

    override fun onResume() {
        super.onResume()
        buscaProduto()
    }

    private fun buscaProduto() {
        lifecycleScope.launch {
            produtoDao.searchById(produtoId).collect { produtoEncontrado ->
                sensor = produtoEncontrado
                sensor?.let {
                    preencheCampos(it)
                } ?: finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_detalhes_produto_remover -> {
                sensor?.let {
                    lifecycleScope.launch {
                        produtoDao.remove(it)
                        finish()
                    }
                }

            }
            R.id.menu_detalhes_produto_editar -> {
                Intent(this, FormularioProdutoActivity::class.java).apply {
                    putExtra(CHAVE_PRODUTO_ID, produtoId)
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tentaCarregarProduto() {
        produtoId = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    private fun preencheCampos(sensorCarregado: Sensor) {
        with(binding) {
            activityDetalhesProdutoImagem.text = sensorCarregado.alert
            activityDetalhesProdutoNome.text = sensorCarregado.date
            activityDetalhesProdutoDescricao.text = sensorCarregado.volume
            activityDetalhesProdutoValor.text =
                sensorCarregado.weather
        }
    }

}
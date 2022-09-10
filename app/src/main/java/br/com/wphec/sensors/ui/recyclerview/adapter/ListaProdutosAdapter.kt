package br.com.wphec.sensors.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.wphec.sensors.databinding.ProdutoItemBinding
import br.com.wphec.sensors.extensions.formatCurrencyBrazil
import br.com.wphec.sensors.model.Sensor

class ListaProdutosAdapter(
    private val context: Context,
    sensors: List<Sensor> = emptyList(),
    var quandoClicaNoItem: (sensor: Sensor) -> Unit = {}
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val produtos = sensors.toMutableList()

    inner class ViewHolder(private val binding: ProdutoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var sensor: Sensor

        init {
            itemView.setOnClickListener {
                if (::sensor.isInitialized) {
                    quandoClicaNoItem(sensor)
                }
            }
        }

        fun vincula(sensor: Sensor) {
            this.sensor = sensor
            val nome = binding.produtoItemNome
            nome.text = sensor.date
            val descricao = binding.produtoItemDescricao
            descricao.text = sensor.volume
            val valor = binding.produtoItemValor
            val valorEmMoeda: String = sensor.alert
            valor.text = valorEmMoeda
            val visibilidade = if (sensor.alert != null) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.imageView.visibility = visibilidade

//            binding.imageView.tentaCarregarImagem(sensor.imagem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProdutoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]
        holder.vincula(produto)
    }

    override fun getItemCount(): Int = produtos.size

    fun atualiza(sensors: List<Sensor>) {
        this.produtos.clear()
        this.produtos.addAll(sensors)
        notifyDataSetChanged()
    }

}

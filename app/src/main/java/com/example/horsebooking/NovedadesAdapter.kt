import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.horsebooking.R

class NovedadesAdapter(private val novedadesList: MutableList<Novedad>, private val context: Context) :
    RecyclerView.Adapter<NovedadesAdapter.NovedadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovedadViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.novedad_item, parent, false)
        return NovedadViewHolder(view)
    }

    override fun onBindViewHolder(holder: NovedadViewHolder, position: Int) {
        val novedad: Novedad = novedadesList[position]
        holder.tituloTextView.text = novedad.titulo
        holder.fechaTextView.text = novedad.fecha
    }

    override fun getItemCount(): Int {
        return novedadesList.size
    }

    inner class NovedadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tituloTextView: TextView = itemView.findViewById(R.id.tituloTextView)
        var fechaTextView: TextView = itemView.findViewById(R.id.fechaTextView)
    }
}

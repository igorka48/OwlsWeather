package owlsdevelopers.com.owlsweather


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.survivingwithandroid.weather.lib.model.DayForecast
import kotlinx.android.synthetic.main.timestep_item.view.*


class TimestepAdapter(data: List<DayForecast>,
                      private val clickListener: TimestepClickListener) : RecyclerView.Adapter<TimestepAdapter.ViewHolder>() {


    var data: List<DayForecast> = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimestepAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.timestep_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimestepAdapter.ViewHolder, position: Int) {
        val dayForecast = data[position]
        holder.bindForecast(dayForecast)
        holder.itemView.setOnClickListener { clickListener.itemClicked(position) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bindForecast(forecast: DayForecast) {
            itemView.timeTextView.text = forecast.stringDate
            itemView.tempTextView.text = "" + forecast.forecastTemp.day
        }
    }


    interface TimestepClickListener {
        fun itemClicked(i: Int)
    }

}